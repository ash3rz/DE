package org.iplantc.de.apps.client.presenter.tilesList.proxy;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;

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

import java.util.List;

/**
 * @author aramsey
 */
public class AppByCategoryProxy extends RpcProxy<AppByCategoryLoadConfig, PagingLoadResult<App>> {

    private final AppServiceFacade appService;

    private IsMaskable maskable;

    @Inject
    public AppByCategoryProxy(AppServiceFacade appService) {
        this.appService = appService;
    }

    @Override
    public void load(final AppByCategoryLoadConfig loadConfig,
                     final AsyncCallback<PagingLoadResult<App>> callback) {

//        if (null == loadConfig.getAppCategory().getId()) {
//            List<AppResource> appResources = Lists.newArrayList();
//            appResources.addAll(loadConfig.getAppCategory().getApps());
//            callback.onSuccess(new PagingLoadResultBean<>(appResources, appResources.size(), loadConfig.getOffset()));
//        } else {
            SortInfo sortInfo =
                    Iterables.getFirst(loadConfig.getSortInfo(), new SortInfoBean("name", SortDir.ASC));
            appService.getPagedApps(loadConfig.getAppCategory().getId(),
                                    loadConfig.getLimit(),
                                    sortInfo.getSortField(),
                                    loadConfig.getOffset(),
                                    sortInfo.getSortDir().toString(),
                                    new AsyncCallback<AppCategory>() {
                                        @Override
                                        public void onFailure(Throwable caught) {
                                            ErrorHandler.post(caught);
                                            maskable.unmask();
                                        }

                                        @Override
                                        public void onSuccess(AppCategory result) {
                                            List<App> appResources = Lists.newArrayList();
                                            appResources.addAll(result.getApps());
                                            callback.onSuccess(new PagingLoadResultBean<App>(
                                                    appResources,
                                                    result.getAppCount(),
                                                    loadConfig.getOffset()));
                                            maskable.unmask();
                                        }
                                    });
        }

//    }

    public void setMaskable(IsMaskable maskable) {
        this.maskable = maskable;
    }

}
