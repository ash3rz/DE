package org.iplantc.de.apps.client.events;

import org.iplantc.de.client.models.ontologies.OntologyHierarchy;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author aramsey
 */
public class HierarchiesLoadedEvent extends GwtEvent<HierarchiesLoadedEvent.HierarchiesLoadedEventHandler> {

    public interface HasHierarchiesLoadedEventHandlers {
        HandlerRegistration addHierarchiesLoadedEventHandler(HierarchiesLoadedEventHandler handler);
    }
    private List<OntologyHierarchy> hierarchyList;
    public static Type<HierarchiesLoadedEventHandler> TYPE = new Type<HierarchiesLoadedEventHandler>();

    public HierarchiesLoadedEvent(List<OntologyHierarchy> hierarchies) {
        this.hierarchyList = hierarchies;
    }

    public Type<HierarchiesLoadedEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(HierarchiesLoadedEventHandler handler) {
        handler.onHierarchiesLoaded(this);
    }

    public static interface HierarchiesLoadedEventHandler extends EventHandler {
        void onHierarchiesLoaded(HierarchiesLoadedEvent event);
    }

    public List<OntologyHierarchy> getHierarchyList() {
        return hierarchyList;
    }
}
