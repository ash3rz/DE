package org.iplantc.de.apps.client.presenter.list.proxy;

import org.iplantc.de.client.models.apps.AppCategory;

/**
 * @author aramsey
 */
public class AppCategoryLoadConfig extends AppLoadConfig {

    private AppCategory appCategory;

    public void setAppCategory(AppCategory appCategory) {
        this.appCategory = appCategory;
    }

    public AppCategory getAppCategory() {
        return appCategory;
    }
}
