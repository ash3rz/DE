package org.iplantc.de.apps.client.views.grid.cells;

import org.iplantc.de.client.models.apps.App;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author jstroot
 */
public class AppIntegratorCell extends AbstractCell<String> implements HasCell<App, String> {

    @Override
    public Cell<String> getCell() {
        return this;
    }

    @Override
    public FieldUpdater<App, String> getFieldUpdater() {
        return null;
    }

    @Override
    public String getValue(App object) {
        return object.getIntegratorName();
    }

    public interface AppIntegratorCellAppearance {

        void render(SafeHtmlBuilder sb, String value, String pattern);
    }

    private final AppIntegratorCellAppearance appearance;
    private String pattern;

    public AppIntegratorCell() {
        this(GWT.<AppIntegratorCellAppearance> create(AppIntegratorCellAppearance.class));
    }

    public AppIntegratorCell(final AppIntegratorCellAppearance appearance) {
        this.appearance = appearance;
    }

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
        appearance.render(sb, value, pattern);

    }

    public void setSearchRegexPattern(final String pattern) {
        this.pattern = pattern;
    }
}
