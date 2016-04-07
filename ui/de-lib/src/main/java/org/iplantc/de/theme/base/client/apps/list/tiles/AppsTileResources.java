package org.iplantc.de.theme.base.client.apps.list.tiles;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author aramsey
 */
public interface AppsTileResources extends ClientBundle {
    @Source("org/iplantc/de/theme/base/client/apps/list/tiles/AppsTile.css")
    AppsTileStyle style();

    @Source("org/iplantc/de/theme/base/client/desktop/window/cyverse_about.png")
    ImageResource tile();

    @Source("org/iplantc/de/resources/client/mini_logo.png")
    ImageResource miniLogo();

    public interface AppsTileStyle extends CssResource {

        String tileCell();

        String infoMod();

        String commentMod();

        String nameMod();

        String favoriteMod();

        String integratorMod();

        String ratingMod();
    }
}

