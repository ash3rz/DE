package org.iplantc.de.apps.client.views.grid;

import org.iplantc.de.apps.client.AppsGridView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsTileView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppNameSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.apps.client.gin.factory.AppsGridViewFactory;
import org.iplantc.de.apps.client.gin.factory.AppsTileViewFactory;
import org.iplantc.de.client.models.apps.App;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

/**
 * @author aramsey
 */
public class AppsListViewImpl extends CardLayoutContainer implements AppsListView,
                                                                     AppNameSelectedEvent.AppNameSelectedEventHandler,
                                                                     AppRatingSelected.AppRatingSelectedHandler,
                                                                     AppRatingDeselected.AppRatingDeselectedHandler,
                                                                     AppCommentSelectedEvent.AppCommentSelectedEventHandler,
                                                                     AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler,
                                                                     AppUpdatedEvent.AppUpdatedEventHandler,
                                                                     SelectionChangedEvent.SelectionChangedHandler<App>,
                                                                     AppSelectionChangedEvent.AppSelectionChangedEventHandler,
                                                                     AppInfoSelectedEvent.AppInfoSelectedEventHandler {

    ListStore<App> listStore;
    private final AppsListAppearance appearance;
    private AppsListView activeView;
    private AppsGridView gridView;
    private AppsTileView tileView;

    @Inject
    AppsListViewImpl(final AppsListView.AppsListAppearance appearance,
                     AppsGridViewFactory gridViewFactory,
                     AppsTileViewFactory tileViewFactory,
                     @Assisted final ListStore<App> listStore) {
        this.appearance = appearance;
        this.listStore = listStore;
        this.gridView = gridViewFactory.create(listStore);
        this.tileView = tileViewFactory.create(listStore);

        this.gridView.addAppNameSelectedEventHandler(this);
        this.gridView.addAppRatingDeselectedHandler(this);
        this.gridView.addAppRatingSelectedHandler(this);
        this.gridView.addAppCommentSelectedEventHandlers(this);
        this.gridView.addAppFavoriteSelectedEventHandlers(this);
        this.gridView.addAppSelectionChangedEventHandler(this);
        this.gridView.addAppInfoSelectedEventHandler(this);
        add(gridView);

        this.tileView.addAppNameSelectedEventHandler(this);
        this.tileView.addAppRatingDeselectedHandler(this);
        this.tileView.addAppRatingSelectedHandler(this);
        this.tileView.addAppCommentSelectedEventHandlers(this);
        this.tileView.addAppFavoriteSelectedEventHandlers(this);
        this.tileView.addAppSelectionChangedEventHandler(this);
        this.tileView.addAppInfoSelectedEventHandler(this);
        add(tileView);

        activeView = tileView;
        setActiveWidget(activeView);
    }

    public void switchActiveView() {
        if (activeView == gridView) {
            activeView = tileView;
            setActiveWidget(tileView);
        } else {
            activeView = gridView;
            setActiveWidget(gridView);
        }
    }

    @Override
    public HandlerRegistration addAppCommentSelectedEventHandlers(AppCommentSelectedEvent.AppCommentSelectedEventHandler handler) {
        return addHandler(handler, AppCommentSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppFavoriteSelectedEventHandlers(AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler handler) {
        return addHandler(handler, AppFavoriteSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppFavoritedEventHandler(AppFavoritedEvent.AppFavoritedEventHandler eventHandler) {
        return addHandler(eventHandler, AppFavoritedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return addHandler(handler, AppInfoSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppNameSelectedEventHandler(AppNameSelectedEvent.AppNameSelectedEventHandler handler) {
        return addHandler(handler, AppNameSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppRatingDeselectedHandler(AppRatingDeselected.AppRatingDeselectedHandler handler) {
        return addHandler(handler, AppRatingDeselected.TYPE);
    }

    @Override
    public HandlerRegistration addAppRatingSelectedHandler(AppRatingSelected.AppRatingSelectedHandler handler) {
        return addHandler(handler, AppRatingSelected.TYPE);
    }

    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return addHandler(handler, AppSelectionChangedEvent.TYPE);
    }

    @Override
    public void onAppCommentSelectedEvent(AppCommentSelectedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onAppFavoriteSelected(AppFavoriteSelectedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onAppNameSelected(AppNameSelectedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onAppRatingDeselected(AppRatingDeselected event) {
        fireEvent(event);
    }

    @Override
    public void onAppRatingSelected(AppRatingSelected event) {
        fireEvent(event);
    }

    @Override
    public void onAppUpdated(AppUpdatedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onAppSelectionChanged(AppSelectionChangedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onAppInfoSelected(AppInfoSelectedEvent event) {
        fireEvent(event);
    }

    //</editor-fold>

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        for (Widget widget : getChildren()) {
            AppsListView next = (AppsListView)widget;
            next.onAppCategorySelectionChanged(event);
        }
    }

    @Override
    public void onOntologyHierarchySelectionChanged(OntologyHierarchySelectionChangedEvent event) {
        for (Widget widget : getChildren()) {
            AppsListView next = (AppsListView)widget;
            next.onOntologyHierarchySelectionChanged(event);
        }
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        for (Widget widget : getChildren()) {
            AppsListView next = (AppsListView)widget;
            next.onAppSearchResultLoad(event);
        }
    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        activeView.mask(appearance.beforeAppSearchLoadingMask());
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<App> event) {
       //do nothing
    }

    @Override
    public App getSelectedItem() {
        return activeView.getSelectedItem();
    }

    @Override
    public void select(App app, boolean keepExisting) {
        activeView.select(app, keepExisting);
    }

    @Override
    public void deselectAll() {
        activeView.deselectAll();
    }

    @Override
    public void setSearchPattern(final String searchPattern) {
        activeView.setSearchPattern(searchPattern);
    }

    @UiFactory
    ColumnModel<App> createColumnModel() {
        return new AppColumnModel(appearance);
    }

    @Override
    public void mask(String loadingMask) {
        activeView.mask(loadingMask);
    }

    @Override
    public void unmask() {
        activeView.unmask();
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        for (Widget widget : getChildren()) {
            AppsListView next = (AppsListView)widget;
            next.asWidget().ensureDebugId(baseID);
        }
    }
}
