package org.iplantc.de.apps.client.views.toolBar.cells;

import org.iplantc.de.apps.client.events.HierarchiesLoadedEvent;
import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;

import java.util.List;

/**
 * @author aramsey
 */
public class AppSearchCell extends TriggerFieldCell<String> implements ExpandEvent.HasExpandHandlers,
                                                                       CollapseEvent.HasCollapseHandlers,
                                                                       HideEvent.HideHandler,
                                                                       HierarchiesLoadedEvent.HierarchiesLoadedEventHandler,
                                                                       SelectionHandler<OntologyHierarchy> {

    public interface AppSearchCellAppearance extends TriggerFieldAppearance {
        String allAppsOption();

        String setSearchEmptyText(String selectedItem);

        String hierarchyAppsOption(String hierarchy);

        int dropDownColumnWidth();

        String dropDownColumnLabel();
    }

    private final AppSearchCellAppearance appearance;
    private boolean expanded;
    private AppSearchDropDown searchDropDown;
    private DelayedTask delayedTask;
    private OntologyAutoBeanFactory factory;

    @Inject
    AppSearchCell(AppSearchCellAppearance appearance, OntologyAutoBeanFactory factory) {
        super(appearance);
        this.appearance = appearance;
        this.factory = factory;
        searchDropDown = new AppSearchDropDown(appearance, this);
        addAllAppsToDropDown();
        expanded = false;
        delayedTask = new DelayedTask() {
            @Override
            public void onExecute() {
                collapse(lastContext, lastParent);
            }
        };
    }


    private void addAllAppsToDropDown() {
        OntologyHierarchy allApps = factory.getHierarchy().as();
        allApps.setLabel(appearance.allAppsOption());
        searchDropDown.add(allApps);
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseEvent.CollapseHandler handler) {
        return addHandler(handler, CollapseEvent.getType());
    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandEvent.ExpandHandler handler) {
        return addHandler(handler, ExpandEvent.getType());
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void onHide(HideEvent event) {
        collapse(lastContext, lastParent);
    }

    @Override
    protected boolean isFocusedWithTarget(Element parent, Element target) {
        return super.isFocusedWithTarget(parent, target) || (searchDropDown != null && searchDropDown.getElement().isOrHasChild(target));
    }

    @Override
    protected void onNavigationKey(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
        if (event.getKeyCode() == KeyCodes.KEY_DOWN && !isExpanded()) {
            event.stopPropagation();
            event.preventDefault();
            onTriggerClick(context, parent.<XElement> cast(), event, value, valueUpdater);
        }
    }

    @Override
    protected void onTriggerClick(Context context, XElement parent, NativeEvent event, String value, ValueUpdater<String> updater) {
        super.onTriggerClick(context, parent, event, value, updater);
        if (!isReadOnly() && !isDisabled()) {
            // blur is firing after the expand so context info on expand is being cleared
            // when value change fires lastContext and lastParent are null without this code
            if ((GXT.isWebKit()) && lastParent != null && lastParent != parent) {
                getInputElement(lastParent).blur();
            }

            if (expanded) {
                collapse(context, parent);
            }
            else {
                expand(context, parent, value, updater);
            }
        }
    }

    public void collapse(final Context context, final XElement parent) {
        if (!expanded) {
            return;
        }

        expanded = false;

        searchDropDown.hide();
        getInputElement(parent).focus();
        fireEvent(context, new CollapseEvent(context));
    }

    public void expand(final Context context, final XElement parent, String value, ValueUpdater<String> valueUpdater) {
        if (expanded) {
            return;
        }

        this.expanded = true;

        // expand may be called without the cell being focused
        // saveContext sets focusedCell so we clear if cell
        // not currently focused
        boolean focused = focusedCell != null;
        saveContext(context, parent, null, valueUpdater, value);
        if (!focused) {
            focusedCell = null;
        }

        // handle case when down arrow is opening menu
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                searchDropDown.show(parent, new Style.AnchorAlignment(Anchor.TOP_RIGHT, Anchor.BOTTOM_RIGHT, true));
                fireEvent(context, new ExpandEvent(context));
            }
        });
    }

    @Override
    public void onSelection(SelectionEvent<OntologyHierarchy> event) {
        setEmptyText(lastContext, lastParent, appearance.setSearchEmptyText(event.getSelectedItem().getLabel()));
        delayedTask.delay(250);
    }

    @Override
    public void onHierarchiesLoaded(HierarchiesLoadedEvent event) {
        List<OntologyHierarchy> hierarchies = event.getHierarchyList();
        for (OntologyHierarchy hierarchy : hierarchies) {
            searchDropDown.add(hierarchy);
        }
    }

    public OntologyHierarchy getCurrentValue() {
        return searchDropDown.getCurrentValue();
    }
}
