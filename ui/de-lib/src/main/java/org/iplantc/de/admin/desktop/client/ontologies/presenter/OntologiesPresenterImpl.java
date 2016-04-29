package org.iplantc.de.admin.desktop.client.ontologies.presenter;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.apps.client.AdminCategoriesView;
import org.iplantc.de.admin.desktop.client.ontologies.OntologiesView;
import org.iplantc.de.admin.desktop.client.ontologies.events.HierarchySelectedEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.PublishOntologyClickEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.SaveOntologyHierarchyEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.SelectOntologyVersionEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.ViewOntologyVersionEvent;
import org.iplantc.de.admin.desktop.client.ontologies.gin.factory.OntologiesViewFactory;
import org.iplantc.de.admin.desktop.client.ontologies.service.OntologyServiceFacade;
import org.iplantc.de.client.models.DEProperties;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.ontologies.Ontology;
import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.models.ontologies.OntologyMetadata;
import org.iplantc.de.client.models.ontologies.OntologyVersionDetail;
import org.iplantc.de.client.util.CommonModelUtils;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;

import com.google.common.collect.Lists;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;

import java.util.Collections;
import java.util.List;

/**
 * @author aramsey
 */
public class OntologiesPresenterImpl implements OntologiesView.Presenter,
                                                ViewOntologyVersionEvent.ViewOntologyVersionEventHandler,
                                                SelectOntologyVersionEvent.SelectOntologyVersionEventHandler,
                                                SaveOntologyHierarchyEvent.SaveOntologyHierarchyEventHandler,
                                                PublishOntologyClickEvent.PublishOntologyClickEventHandler,
                                                HierarchySelectedEvent.HierarchySelectedEventHandler {

    @Inject DEProperties properties;
    @Inject IplantAnnouncer announcer;
    private OntologiesView view;
    private OntologyServiceFacade serviceFacade;
    private final TreeStore<OntologyHierarchy> treeStore;
    private OntologiesView.OntologiesViewAppearance appearance;
    private AdminCategoriesView.Presenter categoriesPresenter;
    private AdminAppsGridView.Presenter gridPresenter;
    private OntologyAutoBeanFactory beanFactory;
    private ListStore<App> listStore;

    @Inject
    public OntologiesPresenterImpl(OntologyServiceFacade serviceFacade,
                                   final TreeStore<OntologyHierarchy> treeStore,
                                   OntologyAutoBeanFactory beanFactory,
                                   OntologiesViewFactory factory,
                                   OntologiesView.OntologiesViewAppearance appearance,
                                   AdminCategoriesView.Presenter categoriesPresenter,
                                   AdminAppsGridView.Presenter gridPresenter) {
        this.serviceFacade = serviceFacade;
        this.beanFactory = beanFactory;
        this.treeStore = treeStore;
        this.appearance = appearance;

        this.categoriesPresenter = categoriesPresenter;
        this.gridPresenter = gridPresenter;
        listStore = gridPresenter.getView().getGrid().getStore();

        this.view = factory.create(treeStore, categoriesPresenter.getView(), gridPresenter.getView());

        categoriesPresenter.getView().addAppCategorySelectedEventHandler(gridPresenter);
        categoriesPresenter.getView().addAppCategorySelectedEventHandler(gridPresenter.getView());
        gridPresenter.addStoreRemoveHandler(categoriesPresenter);

        view.addViewOntologyVersionEventHandler(this);
        view.addSelectOntologyVersionEventHandler(this);
        view.addHierarchySelectedEventHandler(this);
        view.addSaveOntologyHierarchyEventHandler(this);
        view.addPublishOntologyClickEventHandler(this);

        getOntologies();
    }


    @Override
    public void go(HasOneWidget container) {
        HasId betaGroup = CommonModelUtils.getInstance().createHasIdFromString(DEProperties.getInstance().getDefaultBetaCategoryId());

        categoriesPresenter.go(betaGroup);
        container.setWidget(view);
    }

    @Override
    public void onViewOntologyVersion(ViewOntologyVersionEvent event) {
        getOntologies();
    }

    private void getOntologies() {
        serviceFacade.getOntologies(new AsyncCallback<List<Ontology>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Ontology> result) {
                Collections.reverse(result);
                view.showOntologyVersions(result);
            }
        });
    }

    @Override
    public void onSelectOntologyVersion(SelectOntologyVersionEvent event) {
        treeStore.clear();
        serviceFacade.getOntologyHierarchies(event.getSelectedOntology().getVersion(), new AsyncCallback<List<OntologyHierarchy>>() {
                                                 @Override
                                                 public void onFailure(Throwable caught) {
                                                     ErrorHandler.post(caught);
                                                 }

                                                 @Override
                                                 public void onSuccess(List<OntologyHierarchy> result) {
                                                     if (result.size() == 0) {
                                                         view.showEmptyTreePanel();
                                                     }
                                                     else {
                                                         addHierarchies(null, result);
                                                         view.showTreePanel();
                                                     }
            }
        });

    }

    private void addHierarchies(OntologyHierarchy parent, List<OntologyHierarchy> children) {
        if ((children == null)
            || children.isEmpty()) {
            return;
        }
        if (parent == null) {
            treeStore.add(children);
        } else {
            treeStore.add(parent, children);
        }

        for (OntologyHierarchy hierarchy : children) {
            addHierarchies(hierarchy, hierarchy.getSubclasses());
        }
    }

    @Override
    public void onSaveOntologyHierarchy(SaveOntologyHierarchyEvent event) {
        //Save TOPIC hierarchy
        serviceFacade.saveOntologyHierarchy(event.getOntology().getVersion(),
                                            properties.getEdamTopicIri(),
                                            new AsyncCallback<OntologyHierarchy>() {
                                                @Override
                                                public void onFailure(Throwable caught) {
                                                    ErrorHandler.post(caught);
                                                }

                                                @Override
                                                public void onSuccess(OntologyHierarchy result) {
                                                    addHierarchies(null, Lists.newArrayList(result));
                                                    announcer.schedule(new SuccessAnnouncementConfig("Topic saved"));
                                                }
                                            });

        //Save OPERATION hierarchy
        serviceFacade.saveOntologyHierarchy(event.getOntology().getVersion(),
                                            properties.getEdamOperationIri(),
                                            new AsyncCallback<OntologyHierarchy>() {
                                                @Override
                                                public void onFailure(Throwable caught) {
                                                    ErrorHandler.post(caught);
                                                }

                                                @Override
                                                public void onSuccess(OntologyHierarchy result) {
                                                    addHierarchies(null, Lists.newArrayList(result));
                                                    announcer.schedule(new SuccessAnnouncementConfig("Operation saved"));
                                                }
                                            });
        view.showTreePanel();
    }

    @Override
    public void onPublishOntologyClick(PublishOntologyClickEvent event) {
        serviceFacade.setActiveOntologyVersion(event.getNewActiveOntology().getVersion(), new AsyncCallback<OntologyVersionDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(OntologyVersionDetail result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.setActiveOntologySuccess()));
            }
        });
    }

    @Override
    public void onHierarchySelected(HierarchySelectedEvent event) {
        OntologyHierarchy hierarchy = event.getHierarchy();
        OntologyMetadata metadata = beanFactory.getMetadata().as();
        if (hierarchy.getIri().contains("operation")){
            metadata.setAttr(URL.encodeQueryString("rdf:type"));
        }
        else{
            metadata.setAttr(URL.encodeQueryString("has:topic"));
        }
        serviceFacade.getAppsByHierarchy(hierarchy.getIri(), metadata, new AsyncCallback<List<App>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<App> result) {
                listStore.replaceAll(result);
            }
        });
    }
}
