package org.iplantc.de.theme.base.client.apps.grid;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author aramsey
 */
public interface TileListResources extends ClientBundle {

    public interface AppsTileStyle extends CssResource {

        String tileCell();

        String tileCellSelect();

        String infoMod();

        String commentMod();

        String nameMod();

        String favoriteMod();

        String integratorMod();

        String ratingMod();

        String workFlow();

        String privateApp();

    }

    @Source("org/iplantc/de/theme/base/client/apps/cells/AppsTile.css")
    AppsTileStyle style();

    @Source("org/iplantc/de/theme/base/client/desktop/window/cyverse_about.png")
    ImageResource tile();

    @Source("org/iplantc/de/resources/client/mini_logo.png")
    ImageResource miniLogo();

    @Source("org/iplantc/de/resources/client/list-items.gif")
    ImageResource listItems();

    @Source("org/iplantc/de/theme/base/client/diskResource/link.png")
    ImageResource link();
}

