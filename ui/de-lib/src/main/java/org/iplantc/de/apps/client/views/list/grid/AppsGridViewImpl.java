package org.iplantc.de.apps.client.views.list.grid;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppNameSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.presenter.list.proxy.AppListProxy;
import org.iplantc.de.apps.client.presenter.list.proxy.AppLoadConfig;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.apps.App;

import com.google.common.base.Joiner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.LiveGridView;
import com.sencha.gxt.widget.core.client.grid.LiveToolItem;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;

/**
 * Created by jstroot on 3/5/15.
 *
 * @author jstroot
 */
public class AppsGridViewImpl extends ContentPanel implements AppsListView.AppsGridView,
                                                              SelectionChangedEvent.SelectionChangedHandler<App> {
    interface AppsGridViewImplUiBinder extends UiBinder<Widget, AppsGridViewImpl> { }

    private static final AppsGridViewImplUiBinder ourUiBinder = GWT.create(AppsGridViewImplUiBinder.class);

    @UiField(provided = true) final ListStore<App> listStore;
    @UiField ColumnModel cm;
    @UiField Grid<App> grid;
    @UiField LiveGridView gridView;
    @UiField ToolBar pagingToolbar;
    @UiField (provided = true) PagingLoader<AppLoadConfig, PagingLoadResult<App>> loader;
    private final AppColumnModel acm; // Convenience class

    private final AppsListAppearance appearance;
    private String searchRegexPattern;
    private AppListProxy appListProxy;
    private final Status selectionStatus;

    @Inject
    AppsGridViewImpl(final AppsListAppearance appearance,
                     @Assisted final ListStore<App> listStore,
                     AppListProxy appListProxy) {
        this.appearance = appearance;
        this.listStore = listStore;
        this.appListProxy = appListProxy;
        this.selectionStatus = new Status();

        buildLoader();

        setWidget(ourUiBinder.createAndBindUi(this));
        setUpPagingToolbar();

        this.acm = (AppColumnModel) cm;
        grid.getSelectionModel().addSelectionChangedHandler(this);
        gridView.setAutoExpandColumn(cm.getColumn(1));

        new QuickTip(grid).getToolTipConfig().setTrackMouse(true);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                final AppLoadConfig loadConfig = new AppLoadConfig();
                loadConfig.setLimit(gridView.getCacheSize());
                loader.useLoadConfig(loadConfig);
            }
        });
    }

    private void setUpPagingToolbar() {
        selectionStatus.setWidth(appearance.selectionStatusWidth());
        LiveToolItem liveToolItem = new LiveToolItem(grid);
        liveToolItem.setWidth(appearance.liveToolItemWidth());
        pagingToolbar.add(liveToolItem);
        pagingToolbar.add(new FillToolItem());
        pagingToolbar.add(selectionStatus);
        appearance.setPagingToolBarStyle(pagingToolbar);
        updateSelectionCount(0);
    }

    private void updateSelectionCount(int count) {
        selectionStatus.setText(count + " item(s)");
    }

    private void buildLoader() {
        loader = new PagingLoader<>(appListProxy);
        loader.useLoadConfig(new AppLoadConfig());
        loader.setReuseLoadConfig(true);
        loader.setRemoteSort(true);
        loader.addLoadHandler(new LoadResultListStoreBinding<AppLoadConfig, App, PagingLoadResult<App>>(listStore));
        appListProxy.setMaskable(this);
    }

    @UiFactory
    LiveGridView<App> createLiveGridView() {
        // CORE-5723 KLUDGE for Firefox bug with LiveGridView row height calculation.
        // Always use a row height of 25 for now.

        final LiveGridView<App> liveGridView = new LiveGridView<App>() {
            @Override
            protected void insertRows(int firstRow, int lastRow, boolean isUpdate) {
                super.insertRows(firstRow, lastRow, isUpdate);

                setRowHeight(25);
            }
        };
        liveGridView.setCacheSize(200);
        return liveGridView;
    }

    //<editor-fold desc="Handler Registrations">
    @Override
    public HandlerRegistration addAppCommentSelectedEventHandlers(AppCommentSelectedEvent.AppCommentSelectedEventHandler handler) {
        return acm.addAppCommentSelectedEventHandlers(handler);
    }

    @Override
    public HandlerRegistration addAppFavoriteSelectedEventHandlers(AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler handler) {
        return acm.addAppFavoriteSelectedEventHandlers(handler);
    }

    @Override
    public HandlerRegistration addAppFavoritedEventHandler(AppFavoritedEvent.AppFavoritedEventHandler eventHandler) {
        return addHandler(eventHandler, AppFavoritedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return acm.addAppInfoSelectedEventHandler(handler);
    }

    @Override
    public HandlerRegistration addAppNameSelectedEventHandler(AppNameSelectedEvent.AppNameSelectedEventHandler handler) {
        return acm.addAppNameSelectedEventHandler(handler);
    }

    @Override
    public HandlerRegistration addAppRatingDeselectedHandler(AppRatingDeselected.AppRatingDeselectedHandler handler) {
        return acm.addAppRatingDeselectedHandler(handler);
    }

    @Override
    public HandlerRegistration addAppRatingSelectedHandler(AppRatingSelected.AppRatingSelectedHandler handler) {
        return acm.addAppRatingSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return addHandler(handler, AppSelectionChangedEvent.TYPE);
    }
    //</editor-fold>

    @Override
    public Grid<App> getGrid() {
        return grid;
    }


    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        // FIXME Move to appearance
        setHeadingText(Joiner.on(" >> ").join(event.getGroupHierarchy()));

        if (!event.getAppCategorySelection().isEmpty()) {
            // Reset Search
            acm.setSearchRegexPattern("");
        }
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        int total = event.getResults() == null ? 0 : event.getResults().size();
        setHeadingText(appearance.searchAppResultsHeader(event.getSearchText(), total));


    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        mask(appearance.beforeAppSearchLoadingMask());
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<App> event) {
        updateSelectionCount(event.getSelection().size());
        fireEvent(new AppSelectionChangedEvent(event.getSelection()));
    }

    @Override
    public void refresh() {
        gridView.refresh();
    }

    @Override
    public void setSearchPattern(final String searchPattern) {
        this.searchRegexPattern = searchPattern;
        acm.setSearchRegexPattern(searchRegexPattern);
    }

    @Override
    public PagingLoader<AppLoadConfig, PagingLoadResult<App>> getLoader() {
        return loader;
    }

    @Override
    public List<? extends SortInfo> getSortInfo() {
        return grid.getLoader().getSortInfo();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        grid.ensureDebugId(baseID + AppsModule.Ids.APP_GRID);
        acm.ensureDebugId(baseID + AppsModule.Ids.APP_GRID);
    }

    @UiFactory
    ColumnModel<App> createColumnModel() {
        return new AppColumnModel(appearance);
    }
}
