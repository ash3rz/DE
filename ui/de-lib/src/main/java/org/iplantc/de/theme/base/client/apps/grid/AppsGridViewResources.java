package org.iplantc.de.theme.base.client.apps.grid;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author aramsey
 */
public interface AppsGridViewResources extends ClientBundle {
    @Source("org/iplantc/de/theme/base/client/apps/grid/AppsGrid.css")
    AppsGridViewStyles style();

    @Source("org/iplantc/de/theme/base/client/desktop/window/cyverse_about.png")
    ImageResource tile();

    @Source("org/iplantc/de/resources/client/information.png")
    ImageResource info();

    @Source("org/iplantc/de/resources/client/run.png")
    ImageResource play();

    public interface AppsGridViewStyles extends CssResource {

        String tileCell();

        String info();

        String play();

    }
}

