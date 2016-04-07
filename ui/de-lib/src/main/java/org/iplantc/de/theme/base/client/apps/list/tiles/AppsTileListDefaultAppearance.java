package org.iplantc.de.theme.base.client.apps.list.tiles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.sencha.gxt.theme.base.client.listview.ListViewCustomAppearance;

/**
 * @author aramsey
 */
public class AppsTileListDefaultAppearance<App> extends ListViewCustomAppearance<App> {

    private static final AppsTileResources.AppsTileStyle style;

    static {
        style =
                GWT.<AppsTileResources>create(AppsTileResources.class).style();
        style.ensureInjected();
    }

    public AppsTileListDefaultAppearance() {
        super("." + style.tileCell(), null, style.tileCellSelect());
    }

    @Override
    public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {
        builder.appendHtmlConstant("<div class='" + style.tileCell() + "'>");
        builder.append(content);
        builder.appendHtmlConstant("</div>");
    }
}
