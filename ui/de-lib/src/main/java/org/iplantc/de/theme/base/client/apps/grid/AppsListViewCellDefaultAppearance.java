package org.iplantc.de.theme.base.client.apps.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.sencha.gxt.theme.base.client.listview.ListViewCustomAppearance;

/**
 * @author aramsey
 */
public class AppsListViewCellDefaultAppearance<App> extends ListViewCustomAppearance<App> {

    private static final AppsGridViewResources.AppsGridViewStyles style;

    static {
        style =
                GWT.<AppsGridViewResources>create(AppsGridViewResources.class).style();
        style.ensureInjected();
    }

    public AppsListViewCellDefaultAppearance() {
        super("." + style.tileCell(), null, null);
    }

    @Override
    public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {
        builder.appendHtmlConstant("<div class='" + style.tileCell() + "'>");
        builder.append(content);
        builder.appendHtmlConstant("</div>");
    }
}
