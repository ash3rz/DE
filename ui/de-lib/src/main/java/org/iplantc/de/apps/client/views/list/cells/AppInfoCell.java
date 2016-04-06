package org.iplantc.de.apps.client.views.list.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.apps.App;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * @author jstroot
 */
public class AppInfoCell extends AbstractCell<App> implements HasCell<App, App> {

    @Override
    public Cell<App> getCell() {
        return this;
    }

    @Override
    public FieldUpdater<App, App> getFieldUpdater() {
        return null;
    }

    @Override
    public App getValue(App object) {
        return object;
    }

    public interface AppInfoCellAppearance {
        void render(SafeHtmlBuilder sb,
                    String debugId);
    }

    private final AppInfoCellAppearance appearance;
    private String baseID;
    private HasHandlers hasHandlers;

    public AppInfoCell() {
        this(GWT.<AppInfoCellAppearance> create(AppInfoCellAppearance.class));
    }

    public AppInfoCell(final AppInfoCellAppearance appearance) {
        super(CLICK);
        this.appearance = appearance;
    }

    @Override
    public void render(Cell.Context context, App value, SafeHtmlBuilder sb) {
        String debugId = baseID + "." + value.getId() + AppsModule.Ids.APP_INFO_CELL;
        appearance.render(sb, debugId);
    }

    @Override
    public void onBrowserEvent(final Cell.Context context,
                               final Element parent,
                               final App value,
                               final NativeEvent event,
                               final ValueUpdater<App> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget)) {
            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(value);
                    break;
                default:
                    break;
            }
        }
    }

    public void setBaseDebugId(String baseID) {
        this.baseID = baseID;
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        this.hasHandlers = hasHandlers;
    }

    private void doOnClick(App value) {
        if(hasHandlers != null){
            hasHandlers.fireEvent(new AppInfoSelectedEvent(value));
        }
    }

}
