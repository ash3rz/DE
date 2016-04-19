package org.iplantc.de.apps.client.presenter.list.proxy;

import org.iplantc.de.client.models.apps.App;

import java.util.List;

/**
 * @author aramsey
 */
public class AppListLoadConfig extends AppLoadConfig {

    private List<App> appList;

    public List<App> getAppList() {
        return appList;
    }

    public void setAppList(List<App> appList) {
        this.appList = appList;
    }
}
