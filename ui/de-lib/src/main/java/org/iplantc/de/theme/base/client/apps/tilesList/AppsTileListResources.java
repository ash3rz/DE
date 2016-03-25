package org.iplantc.de.theme.base.client.apps.tilesList;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author aramsey
 */
public interface AppsTileListResources extends ClientBundle {
    @Source("org/iplantc/de/theme/base/client/apps/tilesList/AppsTile.css")
    AppsGridViewStyles style();

    @Source("org/iplantc/de/theme/base/client/desktop/window/cyverse_about.png")
    ImageResource tile();

    public interface AppsGridViewStyles extends CssResource {

        String tileCell();

    }
}

