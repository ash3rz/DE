package org.iplantc.de.apps.client.models;

import org.iplantc.de.client.models.apps.App;

import com.google.gwt.editor.client.Editor;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author jstroot
 */
public interface AppProperties extends PropertyAccess<App>{

    ValueProvider<App, String> name();

    ValueProvider<App, String> integratorName();

    @Editor.Path("rating.average")
    ValueProvider<App, Double> rating();
}
