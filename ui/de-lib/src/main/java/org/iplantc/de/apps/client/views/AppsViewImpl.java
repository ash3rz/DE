package org.iplantc.de.apps.client.views;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.shared.AppsModule.Ids;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;

/**
 * @author jstroot
 */
public class AppsViewImpl extends Composite implements AppsView {
    @UiTemplate("AppsView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AppsViewImpl> {
    }

    @UiField(provided = true) final AppsToolbarView toolBar;
    @UiField(provided = true) final AppCategoriesView appCategoriesView;
    @UiField(provided = true) final AppsListView.AppsTileView appsTileView;
    @UiField(provided = true) final AppsListView.AppsGridView appsGridView;
    @UiField CardLayoutContainer centerPanel;
    private final AppsListView.Presenter listPresenter;

    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @Inject
    protected AppsViewImpl(@Assisted final AppCategoriesView.Presenter categoriesPresenter,
                           @Assisted final AppsListView.Presenter listPresenter,
                           @Assisted final AppsToolbarView.Presenter toolbarPresenter) {
        this.appCategoriesView = categoriesPresenter.getView();
        this.appsTileView = listPresenter.getTilesView();
        this.appsGridView = listPresenter.getGridView();
        this.toolBar = toolbarPresenter.getView();
        this.listPresenter = listPresenter;

        initWidget(uiBinder.createAndBindUi(this));
        setActiveView(appsTileView);
    }

    @Override
    public void hideAppMenu() {
        toolBar.hideAppMenu();
    }

    @Override
    public void hideWorkflowMenu() {
        toolBar.hideWorkflowMenu();
    }

    @Override
    public IsWidget getActiveView() {
        return centerPanel.getActiveWidget();
    }

    @Override
    public void setActiveView(IsWidget view) {
        centerPanel.setActiveWidget(view);
        listPresenter.setActiveView(view);
    }

    @Override
    public AppsListView.AppsTileView getListView() {
        return appsTileView;
    }

    @Override
    public AppsListView.AppsGridView getGridView() {
        return appsGridView;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        toolBar.asWidget().ensureDebugId(baseID + Ids.MENU_BAR);
        appsTileView.asWidget().ensureDebugId(baseID);
        appCategoriesView.asWidget().ensureDebugId(baseID);
    }

}
