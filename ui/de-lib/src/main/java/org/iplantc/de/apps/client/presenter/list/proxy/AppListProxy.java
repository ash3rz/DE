package org.iplantc.de.apps.client.presenter.list.proxy;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.shared.exceptions.HttpRedirectException;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.widget.core.client.box.MessageBox;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author aramsey
 */
public class AppListProxy extends RpcProxy<AppLoadConfig, PagingLoadResult<App>> {

    class AppCategoryAsyncCallback implements AsyncCallback<AppCategory> {
        private final AsyncCallback<PagingLoadResult<App>> callback;
        private final AppCategoryLoadConfig categoryLoadConfig;

        public AppCategoryAsyncCallback(AsyncCallback<PagingLoadResult<App>> callback,
                                        AppCategoryLoadConfig categoryLoadConfig) {
            this.callback = callback;
            this.categoryLoadConfig = categoryLoadConfig;
        }

        @Override
        public void onFailure(Throwable caught) {
            if (caught instanceof HttpRedirectException) {
                MessageBox messageBox = new MessageBox(appearance.agaveAuthRequiredTitle(),
                                                       appearance.agaveAuthRequiredMsg());
                messageBox.setIcon(MessageBox.ICONS.info());
                messageBox.show();
            } else {
                ErrorHandler.post(caught);
            }
            maskable.unmask();
        }

        @Override
        public void onSuccess(AppCategory result) {
            List<App> appResources = Lists.newArrayList();
            appResources.addAll(result.getApps());
            callback.onSuccess(new PagingLoadResultBean<App>(appResources,
                                                             result.getAppCount(),
                                                             categoryLoadConfig.getOffset()));
            maskable.unmask();
        }
    }

    private final AppServiceFacade appService;
    private AppsListView.AppsListAppearance appearance;
    SortInfo sortInfo;
    String sortField;

    IsMaskable maskable;

    @Inject
    public AppListProxy(AppServiceFacade appService, AppsListView.AppsListAppearance appearance) {
        this.appService = appService;
        this.appearance = appearance;
    }

    public void setMaskable(IsMaskable maskable) {
        this.maskable = maskable;
    }

    @Override
    public void load(final AppLoadConfig loadConfig,
                     final AsyncCallback<PagingLoadResult<App>> callback) {

        if (null == loadConfig) {
            return;
        }

        sortInfo = getSortInfo(loadConfig);
        sortField = getSortField(sortInfo);

        maskable.mask(appearance.getAppsLoadingMask());

        if (loadConfig instanceof AppListLoadConfig) {
            AppListLoadConfig appListLoadConfig = (AppListLoadConfig)loadConfig;
            List<App> apps = appListLoadConfig.getAppList();

            Collections.sort(apps, new AppComparator(sortField));
            if (sortInfo.getSortDir() == SortDir.DESC) {
                Collections.reverse(apps);
            }
            callback.onSuccess(new PagingLoadResultBean<App>(apps,
                                                             appListLoadConfig.getAppList().size(),
                                                             0));
            maskable.unmask();

        }

        if (loadConfig instanceof AppCategoryLoadConfig) {
            final AppCategoryLoadConfig categoryLoadConfig = (AppCategoryLoadConfig)loadConfig;

            appService.getPagedApps(categoryLoadConfig.getAppCategory().getId(),
                                    categoryLoadConfig.getLimit(),
                                    sortField,
                                    categoryLoadConfig.getOffset(),
                                    sortInfo.getSortDir().toString(),
                                    new AppCategoryAsyncCallback(callback, categoryLoadConfig));
        }

    }

    private SortInfoBean getSortInfo(AppLoadConfig loadConfig) {
        return Iterables.getFirst(loadConfig.getSortInfo(),
                                  new SortInfoBean("name", SortDir.ASC));
    }

    public String getSortField(SortInfo sortInfo) {
        String sortField = sortInfo.getSortField();
        return getServiceFilterName(sortField);
    }

    public String getServiceFilterName(String input) {
        String filter = input.toLowerCase();
        if (filter.matches("[Nn]ame")) {
            return AppServiceFacade.NAME_SORT_FIELD;
        }
        if (filter.matches("[Ii](ntegrator|ntegrated).*")) {
            return AppServiceFacade.INTEGRATOR_SORT_FIELD;
        }
        if (filter.matches("(.*[Rr]ating)")) {
            return AppServiceFacade.RATING_SORT_FIELD;
        }
        return null;
    }

    private final class AppComparator implements Comparator<App> {

        private String sortField;

        public AppComparator(String sortField) {
            this.sortField = sortField;
        }

        @Override
        public int compare(App o1, App o2) {
            if (sortField.equals(AppServiceFacade.INTEGRATOR_SORT_FIELD)) {
                return o1.getIntegratorName().compareToIgnoreCase(o2.getIntegratorName());
            }

            if (sortField.equals(AppServiceFacade.RATING_SORT_FIELD)) {
                return Double.compare(o1.getRating().getAverageRating(), o2.getRating().getAverageRating());
            }

            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }
}
