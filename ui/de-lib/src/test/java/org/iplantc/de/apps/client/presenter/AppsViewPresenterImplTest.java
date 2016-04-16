package org.iplantc.de.apps.client.presenter;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.client.events.SwapViewButtonClickedEvent;
import org.iplantc.de.apps.client.gin.factory.AppsViewFactory;
import org.iplantc.de.client.models.apps.AppCategory;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class AppsViewPresenterImplTest {

    private AppsViewPresenterImpl uut;
    @Mock AppCategoriesView categoriesViewMock;
    @Mock AppCategoriesView.Presenter categoriesPresenterMock;

    @Mock AppsListView.AppsGridView gridViewMock;
    @Mock AppsListView.AppsTileView tileViewMock;
    @Mock AppsListView.Presenter listPresenterMock;

    @Mock AppsToolbarView toolbarViewMock;
    @Mock AppsToolbarView.Presenter toolbarPresenterMock;

    @Mock AppsViewFactory viewFactoryMock;
    @Mock AppsView viewMock;
    @Mock AppCategory appCategoryMock;
    @Mock Tree<AppCategory, String> appCategoryTreeMock;
    @Mock TreeSelectionModel<AppCategory> appCategorySelectionModelMock;

    @Before public void setUp() {
        when(appCategoryTreeMock.getSelectionModel()).thenReturn(appCategorySelectionModelMock);
        when(categoriesViewMock.getTree()).thenReturn(appCategoryTreeMock);
        when(categoriesPresenterMock.getView()).thenReturn(categoriesViewMock);
        when(listPresenterMock.getGridView()).thenReturn(gridViewMock);
        when(listPresenterMock.getTilesView()).thenReturn(tileViewMock);
        when(toolbarPresenterMock.getView()).thenReturn(toolbarViewMock);
        when(viewMock.getGridView()).thenReturn(gridViewMock);
        when(viewMock.getListView()).thenReturn(tileViewMock);
        uut = new AppsViewPresenterImpl(viewFactoryMock,
                                        categoriesPresenterMock, listPresenterMock,
                                        toolbarPresenterMock);
        uut.view = viewMock;
    }

    @Test public void testConstructorEventHandlerWiring() {
        verify(viewFactoryMock).create(eq(categoriesPresenterMock),
                                       eq(listPresenterMock),
                                       eq(toolbarPresenterMock));

        // Verify categories wiring
        verify(categoriesViewMock).addAppCategorySelectedEventHandler(eq(listPresenterMock));
        verify(categoriesViewMock).addAppCategorySelectedEventHandler(eq(tileViewMock));
        verify(categoriesViewMock).addAppCategorySelectedEventHandler(eq(gridViewMock));
        verify(categoriesViewMock).addAppCategorySelectedEventHandler(eq(toolbarViewMock));

        // Verify grid wiring
        verify(listPresenterMock).addStoreAddHandler(categoriesPresenterMock);
        verify(listPresenterMock).addStoreRemoveHandler(categoriesPresenterMock);
        verify(listPresenterMock).addStoreClearHandler(categoriesPresenterMock);
        verify(listPresenterMock).addAppFavoritedEventHandler(categoriesPresenterMock);
        verify(tileViewMock).addAppSelectionChangedEventHandler(toolbarViewMock);
        verify(tileViewMock).addAppInfoSelectedEventHandler(categoriesPresenterMock);
        verify(gridViewMock).addAppSelectionChangedEventHandler(toolbarViewMock);
        verify(gridViewMock).addAppInfoSelectedEventHandler(categoriesPresenterMock);

        // Verify toolbar wiring
        verify(toolbarViewMock).addDeleteAppsSelectedHandler(listPresenterMock);
        verify(toolbarViewMock).addCopyAppSelectedHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addCopyWorkflowSelectedHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addRunAppSelectedHandler(listPresenterMock);
        verify(toolbarViewMock).addBeforeAppSearchEventHandler(tileViewMock);
        verify(toolbarViewMock).addBeforeAppSearchEventHandler(gridViewMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(listPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(tileViewMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(gridViewMock);
        verify(toolbarViewMock).addSwapViewButtonClickedEventHandler(uut);

        verify(categoriesPresenterMock, times(5)).getView();
        verify(listPresenterMock, times(5)).getGridView();
        verify(listPresenterMock, times(5)).getTilesView();
        verify(toolbarPresenterMock, times(14)).getView();


        verifyNoMoreInteractions(viewFactoryMock,
                                 categoriesPresenterMock, listPresenterMock,
                                 toolbarPresenterMock,
                                 categoriesViewMock,
                                 gridViewMock,
                                 toolbarViewMock);

        final List<String> dirStack = Lists.newArrayList();
        final List<String> output = Lists.newArrayList();
        for(String s : Splitter.on("/").split("/foo/bar/baz")){
            dirStack.add(s);
            output.add(Joiner.on("/").join(dirStack));
        }
        System.out.println(output);
    }

    @Test
    public void testOnSwapViewButtonClicked_tilesViewActive() {
        when(viewMock.getActiveView()).thenReturn(tileViewMock);
        when(categoriesPresenterMock.getSelectedAppCategory()).thenReturn(appCategoryMock);

        uut.onSwapViewButtonClicked(mock(SwapViewButtonClickedEvent.class));
        verify(viewMock).setActiveView(gridViewMock);

        verify(categoriesPresenterMock).go(eq(appCategoryMock));
    }

    @Test
    public void testOnSwapViewButtonClicked_gridViewActive() {
        when(viewMock.getActiveView()).thenReturn(gridViewMock);
        when(categoriesPresenterMock.getSelectedAppCategory()).thenReturn(appCategoryMock);

        uut.onSwapViewButtonClicked(mock(SwapViewButtonClickedEvent.class));
        verify(viewMock).setActiveView(tileViewMock);

        verify(categoriesPresenterMock).go(eq(appCategoryMock));
    }

    @Test
    public void testOnSwapViewButtonClicked_noCategory() {
        when(viewMock.getActiveView()).thenReturn(gridViewMock);
        when(categoriesPresenterMock.getSelectedAppCategory()).thenReturn(null);

        uut.onSwapViewButtonClicked(mock(SwapViewButtonClickedEvent.class));
        verify(viewMock).setActiveView(tileViewMock);

        verify(listPresenterMock).refreshActiveView();
    }

}
