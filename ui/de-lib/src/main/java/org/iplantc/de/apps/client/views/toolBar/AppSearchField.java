package org.iplantc.de.apps.client.views.toolBar;

import org.iplantc.de.apps.client.events.HierarchiesLoadedEvent;
import org.iplantc.de.apps.client.presenter.toolBar.proxy.AppSearchRpcProxy;
import org.iplantc.de.apps.client.views.toolBar.cells.AppSearchCell;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.commons.client.events.SubmitTextSearchEvent;
import org.iplantc.de.commons.client.widgets.search.SearchFieldDecorator;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TriggerField;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A SearchField for the App Catalog main toolbar that performs remote app searches.
 * 
 * @author psarando
 * 
 */
public class AppSearchField extends TriggerField<String> implements ExpandEvent.HasExpandHandlers,
                                                                    CollapseEvent.HasCollapseHandlers,
                                                                    SubmitTextSearchEvent.SubmitTextSearchEventHandler,
                                                                    HierarchiesLoadedEvent.HierarchiesLoadedEventHandler {
    private class QueryStringPropertyEditor extends PropertyEditor<String> {
        @Override
        public String parse(CharSequence text) throws ParseException {
            clearInvalid();

            if (text.length() < 3) {
                return text.toString();
            }

            OntologyHierarchy hierarchy = appSearchCell.getCurrentValue();
            String hierarchyFilter = hierarchy.getIri();
            loader.load(getParams(text.toString(), hierarchyFilter));
            return text.toString();
        }

        @Override
        public String render(String object) {
            return object;
        }
    }

    private final AppSearchRpcProxy proxy;
    protected PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> loader;
    private AppSearchCell appSearchCell;

    @Inject
    public AppSearchField(AppSearchCell searchCell, AppUserServiceFacade appService) {
        super(searchCell);
        this.appSearchCell = searchCell;
        proxy = new AppSearchRpcProxy(appService);
        loader = new PagingLoader<>(proxy);
        setPropertyEditor(new QueryStringPropertyEditor());
        new SearchFieldDecorator<TriggerField<String>>(this).addSubmitTextSearchEventHandler(this);
    }

    @Override
    public AppSearchCell getCell() {
        return (AppSearchCell)super.getCell();
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseEvent.CollapseHandler handler) {
        return getCell().addCollapseHandler(handler);
    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandEvent.ExpandHandler handler) {
        return getCell().addExpandHandler(handler);
    }

    @Override
    public void onHierarchiesLoaded(HierarchiesLoadedEvent event) {
        getCell().onHierarchiesLoaded(event);
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        proxy.setHasHandlers(hasHandlers);
    }

    @Override
    public void onSubmitTextSearch(SubmitTextSearchEvent event) {
        finishEditing();
    }

    //Copied from SearchField
    protected FilterPagingLoadConfig getParams(String query, String hierarchyFilter) {
        FilterPagingLoadConfig config = getLoaderConfig();

        List<FilterConfig> filters = getConfigFilters(config);

        if (filters.isEmpty()) {
            FilterConfigBean textFilter = new FilterConfigBean();
            FilterConfigBean hierarchyFilterBean = new FilterConfigBean();
            filters.add(textFilter);
            filters.add(hierarchyFilterBean);
        }

        filters.get(0).setValue(query);
        filters.get(1).setValue(hierarchyFilter);

        config.setOffset(0);

        return config;
    }

    //Copied from SearchField
    protected FilterPagingLoadConfig getLoaderConfig() {
        FilterPagingLoadConfig config;
        if (loader.isReuseLoadConfig()) {
            config = loader.getLastLoadConfig();
        } else {
            config = new FilterPagingLoadConfigBean();
        }

        return config;
    }

    //Copied from SearchField
    protected List<FilterConfig> getConfigFilters(FilterPagingLoadConfig config) {
        List<FilterConfig> filters = config.getFilters();
        if (filters == null) {
            filters = new ArrayList<>();
            config.setFilters(filters);
        }

        return filters;
    }
}
