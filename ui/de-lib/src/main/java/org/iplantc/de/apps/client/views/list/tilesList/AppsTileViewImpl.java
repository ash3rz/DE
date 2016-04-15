package org.iplantc.de.apps.client.views.list.tilesList;

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
import org.iplantc.de.apps.client.presenter.list.proxy.AppListLoadConfig;
import org.iplantc.de.apps.client.presenter.list.proxy.AppListProxy;
import org.iplantc.de.apps.client.presenter.list.proxy.AppLoadConfig;
import org.iplantc.de.apps.client.views.list.cells.AppTileCell;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.theme.base.client.apps.list.tiles.AppsTileListDefaultAppearance;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jstroot on 3/5/15.
 *
 * @author jstroot
 */
public class AppsTileViewImpl extends ContentPanel implements AppsListView.AppsTileView,
                                                              SelectionChangedEvent.SelectionChangedHandler<App>,
                                                              HasHandlers {

    interface AppsGridViewImplUiBinder extends UiBinder<Widget, AppsTileViewImpl> { }

    private static final AppsGridViewImplUiBinder ourUiBinder = GWT.create(AppsGridViewImplUiBinder.class);

    ListStore<App> listStore;
    @UiField ListView<App, App> listView;
    @UiField(provided = true) PagingLoader<AppLoadConfig, PagingLoadResult<App>> loader;
    @UiField PagingToolBar pagingToolBar;
    @UiField SimpleComboBox<String> sortBox;
    @UiField(provided = true) AppsListView.AppsListAppearance appearance;
    private AppsTileListDefaultAppearance<App> cellAppearance;
    private AppTileCell appTileCell;
    private AppListProxy appListProxy;

    @Inject
    AppsTileViewImpl(final AppsListView.AppsListAppearance appearance,
                     @Assisted final ListStore<App> listStore,
                     AppsTileListDefaultAppearance<App> cellAppearance,
                     AppTileCell appTileCell, AppListProxy appListProxy) {
        this.appearance = appearance;
        this.listStore = listStore;
        this.cellAppearance = cellAppearance;
        this.appTileCell = appTileCell;
        this.appListProxy = appListProxy;
        appTileCell.setHasHandlers(this);

        buildLoader();

        setWidget(ourUiBinder.createAndBindUi(this));

        pagingToolBar.bind(loader);

        sortBox.add(Arrays.asList(appearance.nameColumnLabel(), appearance.integratedByColumnLabel(), appearance.ratingColumnLabel()));
        sortBox.setValue(appearance.nameColumnLabel());
        sortBox.addSelectionHandler(new SelectionHandler<String>() {
            @Override
            public void onSelection(SelectionEvent<String> event) {
                mask();
                loader.getLastLoadConfig().setSortInfo(getSortInfo());
                loader.load(loader.getLastLoadConfig());
            }
        });

        listView.getSelectionModel().addSelectionChangedHandler(this);
        listView.setCell(this.appTileCell);
    }

    private void buildLoader() {
        loader = new PagingLoader<>(appListProxy);
        loader.useLoadConfig(new AppLoadConfig());
        loader.setReuseLoadConfig(true);
        loader.setRemoteSort(false);
        loader.addLoadHandler(new LoadResultListStoreBinding<AppLoadConfig, App, PagingLoadResult<App>>(listStore));
        appListProxy.setMaskable(this);
    }

    @UiFactory ListView<App, App> createListView() {
        return new ListView<>(listStore, new IdentityValueProvider<App>(), cellAppearance);
    }

    @Override
    public HandlerRegistration addAppNameSelectedEventHandler(AppNameSelectedEvent.AppNameSelectedEventHandler handler) {
        return addHandler(handler, AppNameSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return addHandler(handler, AppSelectionChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return addHandler(handler, AppInfoSelectedEvent.TYPE);
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
    public HandlerRegistration addAppRatingDeselectedHandler(AppRatingDeselected.AppRatingDeselectedHandler handler) {
        return addHandler(handler, AppRatingDeselected.TYPE);
    }

    @Override
    public HandlerRegistration addAppRatingSelectedHandler(AppRatingSelected.AppRatingSelectedHandler handler) {
        return addHandler(handler, AppRatingSelected.TYPE);
    }

    public ListView<App, App> getListView() {
        return listView;
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        // FIXME Move to appearance
        setHeadingText(Joiner.on(" >> ").join(event.getGroupHierarchy()));

        if (!event.getAppCategorySelection().isEmpty()) {
            // Reset Search
            setSearchPattern("");
        }

        pagingToolBar.setPageSize(appearance.toolbarPageSize());
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        int total = event.getResults() == null ? 0 : event.getResults().size();
        setHeadingText(appearance.searchAppResultsHeader(event.getSearchText(), total));
        AppListLoadConfig appListLoadConfig = new AppListLoadConfig();
        appListLoadConfig.setAppList(event.getResults());
        pagingToolBar.setPageSize(event.getResults().size());
        loader.load(appListLoadConfig);

    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        mask(appearance.beforeAppSearchLoadingMask());
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<App> event) {
        fireEvent(new AppSelectionChangedEvent(event.getSelection()));
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        listView.ensureDebugId(baseID + AppsModule.Ids.APP_TILES);
        appTileCell.setDebugBaseId(baseID + AppsModule.Ids.APP_TILES);
    }

    @Override
    public PagingLoader<AppLoadConfig, PagingLoadResult<App>> getLoader() {
        return loader;
    }

    @Override
    public List<SortInfo> getSortInfo() {
        List<SortInfo> sortInfoList = Lists.newArrayList();
        String sortField = sortBox.getCurrentValue();
        SortInfo sortInfo;
        if (sortField.equals(appearance.ratingColumnLabel())) {
            sortInfo = new SortInfoBean(sortField, SortDir.DESC);
        } else {
            sortInfo = new SortInfoBean(sortField, SortDir.ASC);
        }
        sortInfoList.add(sortInfo);
        return sortInfoList;
    }

    @Override
    public void setSearchPattern(String searchPattern) {
        appTileCell.setSearchRegexPattern(searchPattern);
    }

}
