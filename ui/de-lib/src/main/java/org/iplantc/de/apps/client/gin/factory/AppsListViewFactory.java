package org.iplantc.de.apps.client.gin.factory;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.client.models.apps.App;

import com.sencha.gxt.data.shared.ListStore;

/**
 * @author aramsey
 */
public interface AppsListViewFactory {
    AppsListView create(ListStore<App> listStore);
}
