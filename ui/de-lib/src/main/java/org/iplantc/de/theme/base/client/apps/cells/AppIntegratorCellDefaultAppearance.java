package org.iplantc.de.theme.base.client.apps.cells;

import org.iplantc.de.apps.client.views.grid.cells.AppIntegratorCell;
import org.iplantc.de.theme.base.client.apps.AppSearchHighlightAppearance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author jstroot
 */
public class AppIntegratorCellDefaultAppearance implements AppIntegratorCell.AppIntegratorCellAppearance {

    interface AppIntegratorCellCss extends CssResource {
        String integratorName();
    }

    interface Resources extends ClientBundle {
        @ClientBundle.Source("AppIntegratorCell.css")
        AppIntegratorCellCss css();
    }

    interface Templates extends SafeHtmlTemplates {
        @Template("<div class='{0}'>{1}</div>")
        SafeHtml cell(String className, SafeHtml integratorName);
    }

    private final AppSearchHighlightAppearance highlightAppearance;
    private Templates templates;
    private Resources resources;
    private AppIntegratorCellCss style;

    public AppIntegratorCellDefaultAppearance() {
        this(GWT.<AppSearchHighlightAppearance> create(AppSearchHighlightAppearance.class),
             GWT.<Templates> create(Templates.class),
             GWT.<Resources> create(Resources.class));
    }

    AppIntegratorCellDefaultAppearance(final AppSearchHighlightAppearance highlightAppearance,
                                       Templates templates,
                                       Resources resources) {
        this.highlightAppearance = highlightAppearance;
        this.templates = templates;
        this.resources = resources;
        this.style = resources.css();
        this.style.ensureInjected();
    }

    @Override
    public void render(SafeHtmlBuilder sb, String value, String pattern) {
        String highlightText = highlightAppearance.highlightText(value, pattern);
        sb.append(templates.cell(style.integratorName(), SafeHtmlUtils.fromTrustedString(highlightText)));
//        sb.append(SafeHtmlUtils.fromTrustedString(highlightText));
    }
}
