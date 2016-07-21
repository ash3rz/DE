package org.iplantc.de.apps.client.views.list.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.apps.App;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author aramsey
 */
public class AppCardCell extends AbstractCell<App> implements HasCell<App, App> {

    private AppCommentCell.AppCommentCellAppearance appearance;

    public AppCardCell() {
        this(GWT.<AppCommentCell.AppCommentCellAppearance>create(AppCommentCell.AppCommentCellAppearance.class));
    }

    public AppCardCell(final AppCommentCell.AppCommentCellAppearance appearance) {
        super(CLICK);
        this.appearance = appearance;
    }
    @Override
    public void render(Context context, App value, SafeHtmlBuilder sb) {
        appearance.render(context, value, sb);
    }

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
}
