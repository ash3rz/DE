package org.iplantc.de.apps.client.presenter.list.proxy;

import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;

import java.util.List;

/**
 * @author aramsey
 */
public class AppByCategoryLoadConfig extends FilterPagingLoadConfigBean {

    private static final long serialVersionUID = 1L;

    private AppCategory appCategory;

    private List<App> appList;

    public void setAppCategory(AppCategory appCategory) {
        this.appCategory = appCategory;
    }

    public AppCategory getAppCategory() {
        return appCategory;
    }

    public List<App> getAppList() {
        return appList;
    }

    public void setAppList(List<App> appList) {
        this.appList = appList;
    }
}
