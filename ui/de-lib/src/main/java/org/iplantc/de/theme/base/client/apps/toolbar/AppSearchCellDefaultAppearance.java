package org.iplantc.de.theme.base.client.apps.toolbar;

import org.iplantc.de.apps.client.views.toolBar.cells.AppSearchCell;
import org.iplantc.de.theme.base.client.apps.AppsMessages;
import org.iplantc.de.theme.base.client.diskResource.search.cells.DiskResourceSearchCellDefaultAppearance;

import com.google.gwt.core.client.GWT;

/**
 * @author aramsey
 */
public class AppSearchCellDefaultAppearance extends DiskResourceSearchCellDefaultAppearance implements AppSearchCell.AppSearchCellAppearance {

    private AppsMessages appsMessages;

    public AppSearchCellDefaultAppearance() {
        this(GWT.<AppsMessages>create(AppsMessages.class));
    }

    public AppSearchCellDefaultAppearance(AppsMessages appsMessages) {
        this.appsMessages = appsMessages;
    }

    @Override
    public String allAppsOption() {
        return appsMessages.allAppsOption();
    }

    @Override
    public String setSearchEmptyText(String selectedItem) {
        return appsMessages.setSearchEmptyText(selectedItem);
    }

    @Override
    public String hierarchyAppsOption(String hierarchy) {
        return appsMessages.hierarchyAppsOption(hierarchy);
    }

    @Override
    public int dropDownColumnWidth() {
        return 150;
    }

    @Override
    public String dropDownColumnLabel() {
        return appsMessages.dropDownColumnLabel();
    }
}
