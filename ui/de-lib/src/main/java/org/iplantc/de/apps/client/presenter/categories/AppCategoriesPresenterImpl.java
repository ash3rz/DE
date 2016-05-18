package org.iplantc.de.apps.client.presenter.categories;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.events.AppSavedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.EditAppEvent;
import org.iplantc.de.apps.client.events.EditWorkflowEvent;
import org.iplantc.de.apps.client.events.selection.CopyAppSelected;
import org.iplantc.de.apps.client.events.selection.CopyWorkflowSelected;
import org.iplantc.de.apps.client.gin.factory.AppCategoriesViewFactory;
import org.iplantc.de.apps.client.views.details.dialogs.AppDetailsDialog;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author jstroot
 */
public class AppCategoriesPresenterImpl implements AppCategoriesView.Presenter,
                                                   AppCategoriesView.AppCategoryHierarchyProvider,
                                                   AppUpdatedEvent.AppUpdatedEventHandler,
                                                   AppSavedEvent.AppSavedEventHandler {

    private static class AppCategoryComparator implements Comparator<AppCategory> {

        private final TreeStore<AppCategory> treeStore;

        public AppCategoryComparator(final TreeStore<AppCategory> treeStore) {
            this.treeStore = treeStore;
        }

        @Override
        public int compare(AppCategory group1, AppCategory group2) {
            if (treeStore.getRootItems().contains(group1)
                    || treeStore.getRootItems().contains(group2)) {
                // Do not sort Root groups, since we want to keep the service's root order.
                return 0;
            }

            return group1.getName().compareToIgnoreCase(group2.getName());
        }
    }

    protected static String FAVORITES;
    protected static String USER_APPS_GROUP;
    protected static String WORKSPACE;
    protected String searchRegexPattern;
    @Inject IplantAnnouncer announcer;
    @Inject AsyncProviderWrapper<AppDetailsDialog> appDetailsDlgAsyncProvider;
    @Inject AppServiceFacade appService;
    @Inject AppUserServiceFacade appUserService;
    @Inject AppCategoriesView.AppCategoriesAppearance appearance;
    private final EventBus eventBus;
    private final TreeStore<AppCategory> treeStore;
    private final AppCategoriesView view;

    @Inject
    AppCategoriesPresenterImpl(final TreeStore<AppCategory> treeStore,
                               final DEProperties props,
                               final JsonUtil jsonUtil,
                               final EventBus eventBus,
                               final AppCategoriesViewFactory viewFactory) {
        this.treeStore = treeStore;
        this.eventBus = eventBus;
        this.view = viewFactory.create(treeStore, this);

        final Store.StoreSortInfo<AppCategory> info = new Store.StoreSortInfo<>(new AppCategoryComparator(treeStore),
                                                                                SortDir.ASC);
        treeStore.addSortInfo(info);
        initConstants(props, jsonUtil);

        eventBus.addHandler(AppUpdatedEvent.TYPE, this);
        eventBus.addHandler(AppSavedEvent.TYPE, this);
    }

    @Override
    public List<String> getGroupHierarchy(AppCategory appCategory) {
        List<String> groupNames = Lists.newArrayList();

        for (AppCategory group : getGroupHierarchy(appCategory, null)) {
            groupNames.add(group.getName());
        }
        Collections.reverse(groupNames);
        return groupNames;
    }

    @Override
    public AppCategory getSelectedAppCategory() {
        return view.getTree().getSelectionModel().getSelectedItem();
    }

    @Override
    public AppCategoriesView getView() {
        return view;
    }

    @Override
    public void go(final HasId selectedAppCategory) {
        if (!treeStore.getAll().isEmpty()
                && selectedAppCategory != null) {
            AppCategory desiredCategory = treeStore.findModelWithKey(selectedAppCategory.getId());
            view.getTree().getSelectionModel().select(desiredCategory, false);
        } else {
            view.mask(appearance.getAppCategoriesLoadingMask());
            appService.getAppCategories(new AsyncCallback<List<AppCategory>>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                    view.unmask();
                }

                @Override
                public void onSuccess(List<AppCategory> result) {
                    List<AppCategory> filteredCategories = getFilteredCategories(result);
                    addAppCategories(null, filteredCategories);
                    view.getTree().expandAll();
                    if (selectedAppCategory != null) {
                        AppCategory desiredCategory = treeStore.findModelWithKey(selectedAppCategory.getId());
                        view.getTree().getSelectionModel().select(desiredCategory, false);
                    } else {
                        view.getTree().getSelectionModel().selectNext();
                        view.getTree().getSelectionModel().select(treeStore.getRootItems().get(0), false);
                    }
                    view.unmask();
                }
            });
        }
    }

    private List<AppCategory> getFilteredCategories(List<AppCategory> result) {
        List<AppCategory> filteredCategories = Lists.newArrayList();
        for (AppCategory category : result) {
            if (!category.isPublic()) {
                filteredCategories.addAll(category.getCategories());
            }
        }
        return filteredCategories;
    }

    @Override
    public void onAppSaved(AppSavedEvent event) {
        /* JDS When an app is saved, always assume that the app is in the
         * "Apps Under Development" group
         */
        view.getTree().getSelectionModel().deselectAll();
        AppCategory userAppCategory = findAppCategoryByName(USER_APPS_GROUP);
        view.getTree().getSelectionModel().select(userAppCategory, false);
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        searchRegexPattern = event.getSearchPattern();
        view.getTree().getSelectionModel().deselectAll();
    }

    @Override
    public void onAppUpdated(final AppUpdatedEvent event) {
        final AppCategory currentCategory = getSelectedAppCategory();
        if (currentCategory != null && FAVORITES.equals(currentCategory.getName())) {
            // If our current category is Favorites, initiate refetch by reselecting category
            // This will cause the favorite count to be updated
            view.getTree().getSelectionModel().deselectAll();
            view.getTree().getSelectionModel().select(currentCategory, false);
        }

    }

    @Override
    public void onCopyAppSelected(CopyAppSelected event) {
        Preconditions.checkArgument(event.getApps().size() == 1);
        // JDS For now, assume only one app
        final App appToBeCopied = event.getApps().iterator().next();
        appUserService.copyApp(appToBeCopied, new AsyncCallback<AppTemplate>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(final AppTemplate app) {
                // Update the user's private apps group count.
                if (!app.getId().isEmpty()) {
                    announcer.schedule(new SuccessAnnouncementConfig(appearance.copyAppSuccessMessage(appToBeCopied.getName())));

                    view.getTree().getSelectionModel().deselectAll();
                    AppCategory userCategory = findAppCategoryByName(USER_APPS_GROUP);

                    // Select "Apps Under Dev" to cause fetch of center
                    view.getTree().getSelectionModel().select(userCategory, false);
                    eventBus.fireEvent(new EditAppEvent(app, false));
                }
            }
        });

    }

    @Override
    public void onCopyWorkflowSelected(final CopyWorkflowSelected event) {
        Preconditions.checkArgument(event.getApps().size() == 1);
        // JDS For now, assume only one app
        final App appToBeCopied = event.getApps().iterator().next();
        appUserService.copyWorkflow(appToBeCopied.getId(), new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Add error message for the user.
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(String result) {
                // Update the user's private apps group count.
                view.getTree().getSelectionModel().deselectAll();
                AppCategory userAppsGrp = findAppCategoryByName(USER_APPS_GROUP);
                // Select "Apps Under Dev" to cause fetch of center
                view.getTree().getSelectionModel().select(userAppsGrp, false);

                // Fire an EditWorkflowEvent for the new workflow copy.
                Splittable serviceWorkflowJson = StringQuoter.split(result);
                eventBus.fireEvent(new EditWorkflowEvent(appToBeCopied, serviceWorkflowJson));
            }
        });
    }

    void addAppCategories(AppCategory parent, List<AppCategory> children) {
        if ((children == null)
                || children.isEmpty()) {
            return;
        }
        if (parent == null) {
            treeStore.add(children);
        } else {
            treeStore.add(parent, children);
        }

        for (AppCategory ag : children) {
            addAppCategories(ag, ag.getCategories());
        }
    }

    AppCategory findAppCategoryByName(String name) {
        for (AppCategory appCategory : treeStore.getAll()) {
            if (appCategory.getName().equalsIgnoreCase(name)) {
                return appCategory;
            }
        }

        return null;
    }

    List<AppCategory> getGroupHierarchy(AppCategory grp, List<AppCategory> groups) {
        if (groups == null) {
            groups = Lists.newArrayList();
        }
        groups.add(grp);
        for (AppCategory ap : treeStore.getRootItems()) {
            if (ap.getId().equals(grp.getId())) {
                return groups;
            }
        }
        return getGroupHierarchy(treeStore.getParent(grp), groups);
    }

    void initConstants(final DEProperties props,
                       final JsonUtil jsonUtil) {
        WORKSPACE = props.getPrivateWorkspace();

        if (props.getPrivateWorkspaceItems() != null) {
            JSONArray items = JSONParser.parseStrict(props.getPrivateWorkspaceItems()).isArray();
            USER_APPS_GROUP = jsonUtil.getRawValueAsString(items.get(0));
            FAVORITES = jsonUtil.getRawValueAsString(items.get(1));
        }
    }

}
