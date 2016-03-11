package org.iplantc.de.apps.client.views.grid.cells;

import org.iplantc.de.client.models.apps.App;

import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import java.util.List;

/**
 * @author aramsey
 */
public class AppTileCell extends CompositeCell<App> {


    /**
     * Construct a new {@link CompositeCell}.
     *
     * @param hasCells the cells that makeup the composite
     */
    public AppTileCell(List<HasCell<App, ?>> hasCells) {
        super(hasCells);
    }

    @Override
    public void render(Context context, App value, SafeHtmlBuilder sb) {
        super.render(context, value, sb);
    }

    @Override
    protected <X> void render(Context context, App value, SafeHtmlBuilder sb, HasCell<App, X> hasCell) {
        sb.appendHtmlConstant("<span>");
        hasCell.getCell().render(context, hasCell.getValue(value), sb);
        sb.appendHtmlConstant("</span>");
    }


}
