package org.iplantc.de.apps.client.presenter;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppsGridView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.client.events.SwapViewButtonClickedEvent;
import org.iplantc.de.apps.client.gin.factory.AppsViewFactory;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.ListView;

/**
 * The presenter for the AppsView.
 *
 * @author jstroot
 */
public class AppsViewPresenterImpl implements AppsView.Presenter,
                                              SwapViewButtonClickedEvent.SwapViewButtonClickedEventHandler {

    protected final AppsView view;
    private final AppCategoriesView.Presenter categoriesPresenter;
    private final AppsListView.Presenter appsListPresenter;
    private final AppsGridView.Presenter appsGridPresenter;

    @Inject
    protected AppsViewPresenterImpl(final AppsViewFactory viewFactory,
                                    final AppCategoriesView.Presenter categoriesPresenter,
                                    final AppsListView.Presenter appsListPresenter,
                                    final AppsGridView.Presenter appsGridPresenter,
                                    final AppsToolbarView.Presenter toolbarPresenter) {
        this.categoriesPresenter = categoriesPresenter;
        this.appsListPresenter = appsListPresenter;
        this.appsGridPresenter = appsGridPresenter;
        this.view = viewFactory.create(categoriesPresenter,
                                       appsListPresenter,
                                       appsGridPresenter,
                                       toolbarPresenter);

        categoriesPresenter.getView().addAppCategorySelectedEventHandler(appsListPresenter);
        categoriesPresenter.getView().addAppCategorySelectedEventHandler(appsListPresenter.getView());
        categoriesPresenter.getView().addAppCategorySelectedEventHandler(toolbarPresenter.getView());

        // Wire up list store handlers
        appsListPresenter.addStoreAddHandler(categoriesPresenter);
        appsListPresenter.addStoreRemoveHandler(categoriesPresenter);
        appsListPresenter.addStoreClearHandler(categoriesPresenter);
        appsListPresenter.addAppFavoritedEventHandler(categoriesPresenter);
        appsListPresenter.getView().addAppSelectionChangedEventHandler(toolbarPresenter.getView());
        appsListPresenter.getView().addAppInfoSelectedEventHandler(categoriesPresenter);

        toolbarPresenter.getView().addDeleteAppsSelectedHandler(appsListPresenter);
        toolbarPresenter.getView().addCopyAppSelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addCopyWorkflowSelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addRunAppSelectedHandler(appsListPresenter);
        toolbarPresenter.getView().addBeforeAppSearchEventHandler(appsListPresenter.getView());
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(categoriesPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(appsListPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(appsListPresenter.getView());
        toolbarPresenter.getView().addSwapViewButtonClickedEventHandler(this);
    }

    @Override
    public ListView<App, App> getAppsGrid() {
        // FIXME Too many levels of misdirection
        return appsListPresenter.getView().getGrid();
    }

    @Override
    public App getSelectedApp() {
        return appsListPresenter.getSelectedApp();
    }

    @Override
    public AppCategory getSelectedAppCategory() {
        return categoriesPresenter.getSelectedAppCategory();
    }

    @Override
    public void go(final HasOneWidget container,
                   final HasId selectedAppCategory,
                   final HasId selectedApp) {
        categoriesPresenter.go(selectedAppCategory);
        container.setWidget(view);
    }

    @Override
    public AppsView.Presenter hideAppMenu() {
        view.hideAppMenu();
        return this;
    }

    @Override
    public AppsView.Presenter hideWorkflowMenu() {
        view.hideWorkflowMenu();
        return this;
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId);
    }

    @Override
    public void onSwapViewButtonClicked(SwapViewButtonClickedEvent event) {
        IsWidget activeView = view.getActiveView();
        if (activeView == view.getListView()) {
            activeView = view.getGridView();
        } else {
            activeView = view.getListView();
        }
        view.setActiveView(activeView);
    }
}
