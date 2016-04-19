package org.iplantc.de.apps.client.presenter.list.proxy;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.services.AppServiceFacade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class AppListProxyTest {

    @Mock AppServiceFacade appServiceFacadeMock;
    @Mock AppsListView.AppsListAppearance appearanceMock;
    @Mock AppLoadConfig loadConfigMock;
    @Mock AppListLoadConfig appListLoadConfigMock;
    @Mock AppCategoryLoadConfig appCategoryLoadConfigMock;
    @Mock IsMaskable maskableMock;
    @Mock List<App> appListMock;
    @Mock List<SortInfoBean> listSortInfoMock;
    @Mock SortInfo sortInfoMock;
    @Mock AsyncCallback<PagingLoadResult<App>> pagingCallbackMock;
    @Mock AppCategory appCategoryMock;

    @Captor ArgumentCaptor<PagingLoadResult<App>> pagingResultCaptureMock;
    @Captor ArgumentCaptor<AsyncCallback<AppCategory>> asyncCallbackArgumentCaptor;

    private AppListProxy uut;


    @Before
    public void setUp() {
        uut = new AppListProxy(appServiceFacadeMock, appearanceMock);
    }

    @Test
    public void testLoad_AppListLoadConfig() {
        AppListLoadConfig loadConfig = mock(AppListLoadConfig.class);
        uut.maskable = maskableMock;
        uut.sortInfo = sortInfoMock;
        when(sortInfoMock.getSortDir()).thenReturn(SortDir.ASC);
        when(appListLoadConfigMock.getAppList()).thenReturn(appListMock);
        when(appListLoadConfigMock.getSortInfo()).thenReturn(listSortInfoMock);

        uut.load(loadConfig, pagingCallbackMock);
        verify(maskableMock).mask(eq(appearanceMock.getAppsLoadingMask()));

        verify(pagingCallbackMock).onSuccess(pagingResultCaptureMock.capture());

        verify(maskableMock).unmask();
    }


    @Test
    public void testLoad_AppCategoryLoadConfig() {
        AppCategoryLoadConfig loadConfig = mock(AppCategoryLoadConfig.class);
        uut.maskable = maskableMock;
        uut.sortInfo = sortInfoMock;
        SortDir sortDirMock = mock(SortDir.class);
        when(sortDirMock.toString()).thenReturn("sort");
        when(sortInfoMock.getSortDir()).thenReturn(sortDirMock);
        when(loadConfig.getLimit()).thenReturn(50);
        when(appCategoryMock.getId()).thenReturn("id");
        when(appCategoryLoadConfigMock.getAppCategory()).thenReturn(appCategoryMock);
        when(appListLoadConfigMock.getSortInfo()).thenReturn(listSortInfoMock);

        uut.load(appCategoryLoadConfigMock, pagingCallbackMock);
        verify(maskableMock).mask(eq(appearanceMock.getAppsLoadingMask()));

        verify(appServiceFacadeMock).getPagedApps(anyString(),
                                                  anyInt(),
                                                  anyString(),
                                                  anyInt(),
                                                  anyString(),
                                                  asyncCallbackArgumentCaptor.capture());

        asyncCallbackArgumentCaptor.getValue().onSuccess(appCategoryMock);

        verify(pagingCallbackMock).onSuccess(pagingResultCaptureMock.capture());

        verify(maskableMock).unmask();
    }
}
