package org.iplantc.de.apps.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class RefreshAppsSelectedEvent
        extends GwtEvent<RefreshAppsSelectedEvent.RefreshAppsSelectedEventHandler> {

    public static interface RefreshAppsSelectedEventHandler extends EventHandler {
        void onRefreshAppsSelected(RefreshAppsSelectedEvent event);
    }

    public interface HasRefreshAppsSelectedEventHandlers {
        HandlerRegistration addRefreshAppsSelectedEventHandler(RefreshAppsSelectedEventHandler handler);
    }

    public static Type<RefreshAppsSelectedEventHandler> TYPE =
            new Type<RefreshAppsSelectedEventHandler>();

    public Type<RefreshAppsSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RefreshAppsSelectedEventHandler handler) {
        handler.onRefreshAppsSelected(this);
    }
}
