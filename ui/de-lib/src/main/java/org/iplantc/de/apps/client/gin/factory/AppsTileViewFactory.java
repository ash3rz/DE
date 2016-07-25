package org.iplantc.de.apps.client.gin.factory;

import org.iplantc.de.apps.client.AppsTileView;
import org.iplantc.de.client.models.apps.App;

import com.sencha.gxt.data.shared.ListStore;

/**
 * @author aramsey
 */
public interface AppsTileViewFactory {
    AppsTileView create(ListStore<App> listStore);
}
