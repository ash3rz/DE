package org.iplantc.de.theme.base.client.apps.cells;

import org.iplantc.de.apps.client.views.grid.cells.AppCardCell;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.commons.client.util.MD5Util;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author aramsey
 */
public class AppCardCellDefaultAppearance implements AppCardCell.AppCardCellAppearance {

    interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<img class='{0}' src='{1}'/>")
        SafeHtml img(String className, String gravatar);
    }

    public interface AppCardCellStyles extends CssResource {
        @CssResource.ClassName("icon")
        String icon();
    }

    public interface AppCardCellResources extends ClientBundle {
        @Source("AppCardCell.css")
        AppCardCellStyles css();
    }

    private AppCardCellResources resources;
    private Templates templates;
    private AppCardCellStyles styles;

    public AppCardCellDefaultAppearance() {
        this ((AppCardCellResources)GWT.create(AppCardCellResources.class),((Templates)GWT.create(Templates.class)));
    }

    public AppCardCellDefaultAppearance(AppCardCellResources appCardCellResources, Templates templates) {
        this.resources = appCardCellResources;
        this.templates = templates;
        this.styles = resources.css();
        styles.ensureInjected();
    }

    @Override
    public void render(Cell.Context context, App value, SafeHtmlBuilder sb) {
        String code = MD5Util.md5Hex(value.getId());
        String hash = "https://www.gravatar.com/avatar/" + code + "?d=identicon&s=60";
        sb.append(templates.img(styles.icon(), hash));
        sb.appendHtmlConstant("</div>");
    }
}
