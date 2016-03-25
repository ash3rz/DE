package org.iplantc.de.apps.client.views.tilesList;

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
import org.iplantc.de.apps.client.views.tilesList.cells.AppCommentCell;
import org.iplantc.de.apps.client.views.tilesList.cells.AppHyperlinkCell;
import org.iplantc.de.apps.client.views.tilesList.cells.AppInfoCell;
import org.iplantc.de.apps.client.views.tilesList.cells.AppIntegratorCell;
import org.iplantc.de.apps.client.views.tilesList.cells.AppRatingCell;
import org.iplantc.de.apps.client.views.tilesList.cells.AppTileCell;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.theme.base.client.apps.tilesList.AppsTileListDefaultAppearance;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
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
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import java.util.List;

/**
 * Created by jstroot on 3/5/15.
 *
 * @author jstroot
 */
public class AppsListViewImpl extends ContentPanel implements AppsListView,
                                                              SelectionChangedEvent.SelectionChangedHandler<App>,
                                                              HasHandlers {
    @Override
    public ListView<App, App> getGrid() {
        return null;
    }

    @Override
    public void setSearchPattern(String searchPattern) {

    }

    interface AppsGridViewImplUiBinder extends UiBinder<Widget, AppsListViewImpl> { }

    private static final AppsGridViewImplUiBinder ourUiBinder = GWT.create(AppsGridViewImplUiBinder.class);

    ListStore<App> listStore;
    @UiField ListView<App, App> grid;

    private final AppsListAppearance appearance;
    private AppsTileListDefaultAppearance<App> cellAppearance;

    @Inject
    AppsListViewImpl(final AppsListAppearance appearance,
                     @Assisted final ListStore<App> listStore,
                     AppsTileListDefaultAppearance<App> cellAppearance) {
        this.appearance = appearance;
        this.listStore = listStore;
        this.cellAppearance = cellAppearance;

        setWidget(ourUiBinder.createAndBindUi(this));

        grid.getSelectionModel().addSelectionChangedHandler(this);
        List<HasCell<App, ?>> cellList = Lists.newArrayList();
        AppHyperlinkCell appNameCell = new AppHyperlinkCell();
        AppInfoCell appInfoCell = new AppInfoCell();
        AppCommentCell appCommentCell = new AppCommentCell();
//        AppFavoriteCell appFavoriteCell = new AppFavoriteCell();
        AppRatingCell appRatingCell = new AppRatingCell();
        AppIntegratorCell appIntegratorCell = new AppIntegratorCell();
        appNameCell.setHasHandlers(this);
        appInfoCell.setHasHandlers(this);
        appCommentCell.setHasHandlers(this);
//        appFavoriteCell.setHasHandlers(this);
        appRatingCell.setHasHandlers(this);
        cellList.add(appNameCell);
        cellList.add(appInfoCell);
        cellList.add(appCommentCell);
//        cellList.add(appFavoriteCell);
        cellList.add(appRatingCell);
        cellList.add(appIntegratorCell);
        AppTileCell appTileCell = new AppTileCell(cellList);
        grid.setCell(appTileCell);
    }

    @UiFactory ListView<App, App> createListView() {
        return new ListView<App, App>(listStore, new IdentityValueProvider<App>(), cellAppearance);
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

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        // FIXME Move to appearance
        setHeadingText(Joiner.on(" >> ").join(event.getGroupHierarchy()));

        if (!event.getAppCategorySelection().isEmpty()) {
            // Reset Search
            setSearchPattern("");
        }
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        int total = event.getResults() == null ? 0 : event.getResults().size();
        setHeadingText(appearance.searchAppResultsHeader(event.getSearchText(), total));
        unmask();
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
        grid.ensureDebugId(baseID + AppsModule.Ids.APP_GRID);
    }
}
