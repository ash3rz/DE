package org.iplantc.de.theme.base.client.apps.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.sencha.gxt.theme.base.client.listview.ListViewCustomAppearance;

/**
 * @author aramsey
 */
public class TileListDefaultAppearance<App> extends ListViewCustomAppearance<App> {

    private final TileListResources resources = GWT.create(TileListResources.class);
    private final TileListResources.AppsTileStyle style = resources.style();

    public TileListDefaultAppearance() {
        super(null);

        style.ensureInjected();

        this.itemSelector = "." + style.tileCell();
        this.selectedStyle = style.tileCellSelect();
    }

    @Override
    public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {
        builder.appendHtmlConstant("<div class='" + style.tileCell() + "'>");
        builder.append(content);
        builder.appendHtmlConstant("</div>");
    }
}

