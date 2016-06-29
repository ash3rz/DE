package org.iplantc.de.apps.client.views.toolBar.cells;

import org.iplantc.de.client.models.ontologies.OntologyHierarchy;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aramsey
 */
public class AppSearchDropDown extends Composite {

    private ListStore<OntologyHierarchy> listStore;
    private Grid<OntologyHierarchy> grid;
    private AppSearchCell.AppSearchCellAppearance appearance;
    private SelectionHandler<OntologyHierarchy> selectionHandler;
    private BaseEventPreview eventPreview;
    private CheckBoxSelectionModel<OntologyHierarchy> checkBoxModel;

    public AppSearchDropDown(final AppSearchCell.AppSearchCellAppearance appearance, SelectionHandler<OntologyHierarchy> selectionHandler) {
        this.appearance = appearance;
        this.selectionHandler = selectionHandler;
        listStore = new ListStore<>(new ModelKeyProvider<OntologyHierarchy>() {
            @Override
            public String getKey(OntologyHierarchy item) {
                return item.getIri();
            }
        });

        setupGrid();
        initWidget(grid);
        setUpEventPreview();
    }

    void setUpEventPreview() {
        eventPreview = new BaseEventPreview() {

            @Override
            protected boolean onPreview(Event.NativePreviewEvent pe) {
                AppSearchDropDown.this.onPreviewEvent(pe);
                return super.onPreview(pe);
            }

            @Override
            protected void onPreviewKeyPress(Event.NativePreviewEvent pe) {
                super.onPreviewKeyPress(pe);
                onEscape(pe);
            }

        };

        eventPreview.getIgnoreList().add(getElement());
        eventPreview.setAutoHide(false);
    }

    private void setupGrid() {
        checkBoxModel = new CheckBoxSelectionModel<>(new IdentityValueProvider<OntologyHierarchy>());
        checkBoxModel.setSelectionMode(Style.SelectionMode.SINGLE);
        ColumnConfig colCheckBox = checkBoxModel.getColumn();
        ColumnConfig<OntologyHierarchy, String>
                label = new ColumnConfig<>(new ValueProvider<OntologyHierarchy, String>() {
            @Override
            public String getValue(OntologyHierarchy object) {
                return appearance.hierarchyAppsOption(object.getLabel());
            }

            @Override
            public void setValue(OntologyHierarchy object, String value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        }, appearance.dropDownColumnWidth(), appearance.dropDownColumnLabel());

        List<ColumnConfig<OntologyHierarchy, ?>> columns = new ArrayList<>();
        columns.add(colCheckBox);
        columns.add(label);
        ColumnModel<OntologyHierarchy> cm = new ColumnModel<>(columns);

        grid = new Grid<>(listStore, cm);
        grid.setSelectionModel(checkBoxModel);
        grid.getSelectionModel().addSelectionHandler(selectionHandler);
        grid.setHideHeaders(true);
        grid.setWidth(appearance.dropDownColumnWidth());
    }

    public void show(Element parent, Style.AnchorAlignment anchorAlignment) {
        getElement().makePositionable(true);
        RootPanel.get().add(this);
        onShow();
        getElement().updateZIndex(0);

        getElement().alignTo(parent, anchorAlignment, 0, 0);

        getElement().show();
        eventPreview.add();

        focus();
        fireEvent(new ShowEvent());
    }

    @Override
    public void hide() {
        super.hide();
        eventPreview.remove();
    }

    public void add(OntologyHierarchy hierarchy) {
        listStore.add(hierarchy);
    }

    public OntologyHierarchy getCurrentValue() {
        return grid.getSelectionModel().getSelectedItem();
    }

    protected void onPreviewEvent(Event.NativePreviewEvent pe) {
        int type = pe.getTypeInt();
        switch (type) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEWHEEL:
            case Event.ONSCROLL:
            case Event.ONKEYPRESS:
                XElement target = pe.getNativeEvent().getEventTarget().cast();

                // ignore targets within a parent with x-ignore, such as the listview in
                // a combo
                if (target.findParent(".x-ignore", 10) != null) {
                    return;
                }

                if (!getElement().isOrHasChild(target)) {
                    hide();
                }
        }
    }

    void onEscape(Event.NativePreviewEvent pe) {
        if (pe.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            pe.getNativeEvent().preventDefault();
            pe.getNativeEvent().stopPropagation();
            hide();
        }
    }
}
