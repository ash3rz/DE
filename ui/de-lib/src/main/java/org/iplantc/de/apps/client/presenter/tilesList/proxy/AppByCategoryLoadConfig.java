package org.iplantc.de.apps.client.presenter.tilesList.proxy;

import org.iplantc.de.client.models.apps.AppCategory;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;

/**
 * @author aramsey
 */
public class AppByCategoryLoadConfig extends FilterPagingLoadConfigBean {

    private static final long serialVersionUID = 1L;

    private AppCategory appCategory;

    public void setAppCategory(AppCategory appCategory) {
        this.appCategory = appCategory;
    }

    public AppCategory getAppCategory() {
        return appCategory;
    }

}
