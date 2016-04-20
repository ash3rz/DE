package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * This view contains the hierarchical {@link AppCategory} tree, and fires off selection events.
 *
 * Created by jstroot on 3/5/15.
 * @author jstroot
 */
public interface OntologyHierarchiesView extends IsWidget,
                                                 IsMaskable,
                                                 AppCategorySelectionChangedEvent.HasAppCategorySelectionChangedEventHandlers {

    interface OntologyHierarchiesAppearance {

        String hierarchyLabelName(OntologyHierarchy hierarchy);
    }

    interface Presenter extends AppSearchResultLoadEvent.AppSearchResultLoadEventHandler {

        OntologyHierarchy getSelectedHierarchy();

        OntologyHierarchiesView getView();

        void go(TabPanel tabPanel);
    }

    Tree<OntologyHierarchy, String> getTree();

}
