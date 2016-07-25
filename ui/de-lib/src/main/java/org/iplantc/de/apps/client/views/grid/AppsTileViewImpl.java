package org.iplantc.de.apps.client.views.grid;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsTileView;
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
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.apps.client.views.grid.cells.AppTileCell;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.theme.base.client.apps.grid.TileListDefaultAppearance;

import com.google.common.base.Joiner;
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
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import java.util.Arrays;

/**
 * @author aramsey
 */
public class AppsTileViewImpl extends ContentPanel
        implements AppsTileView, SelectionChangedEvent.SelectionChangedHandler<App>, HasHandlers {

    interface AppsGridViewImplUiBinder extends UiBinder<Widget, AppsTileViewImpl> {
    }

    private static final AppsGridViewImplUiBinder ourUiBinder =
            GWT.create(AppsGridViewImplUiBinder.class);

    ListStore<App> listStore;
    @UiField ListView<App, App> listView;
    @UiField(provided = true) AppsListView.AppsListAppearance appearance;
    private TileListDefaultAppearance<App> listAppearance;
    private AppTileCell appTileCell;

    @Inject
    AppsTileViewImpl(final AppsListView.AppsListAppearance appearance,
                     @Assisted final ListStore<App> listStore,
                     TileListDefaultAppearance<App> listAppearance,
                     AppTileCell appTileCell) {
        this.appearance = appearance;
        this.listStore = listStore;
        this.listAppearance = listAppearance;
        this.appTileCell = appTileCell;
        appTileCell.setHasHandlers(this);

        setWidget(ourUiBinder.createAndBindUi(this));

        listView.getSelectionModel().addSelectionChangedHandler(this);
        listView.setCell(this.appTileCell);
    }

    @UiFactory
    SimpleComboBox<String> createSortBox() {
        SimpleComboBox<String> comboBox = new SimpleComboBox<>(new StringLabelProvider<>());
        comboBox.add(Arrays.asList(appearance.nameColumnLabel(),
                                   appearance.integratedByColumnLabel(),
                                   appearance.ratingColumnLabel()));
        comboBox.setValue(appearance.nameColumnLabel());
        return comboBox;
    }

    @UiFactory
    ListView<App, App> createListView() {
        return new ListView<>(listStore, new IdentityValueProvider<App>(), listAppearance);
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
    }

    @Override
    public void onOntologyHierarchySelectionChanged(OntologyHierarchySelectionChangedEvent event) {
        setHeadingText(Joiner.on(" >> ").join(event.getPath()));

        if (event.getSelectedHierarchy() != null) {
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
//        listView.ensureDebugId(baseID + AppsModule.Ids.APP_TILES);
//        appTileCell.setDebugBaseId(baseID + AppsModule.Ids.APP_TILES);
    }

    @Override
    public App getSelectedItem() {
        return listView.getSelectionModel().getSelectedItem();
    }

    @Override
    public void select(App app, boolean keepExisting) {
        listView.getSelectionModel().select(app, keepExisting);
    }

    @Override
    public void deselectAll() {
        listView.getSelectionModel().deselectAll();
    }

    @Override
    public void setSearchPattern(String searchPattern) {
        appTileCell.setSearchRegexPattern(searchPattern);
    }

    @Override
    public void switchActiveView() {
        //do nothing
    }
}
