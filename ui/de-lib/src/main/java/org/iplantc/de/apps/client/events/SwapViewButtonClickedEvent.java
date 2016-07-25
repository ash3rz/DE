
package org.iplantc.de.apps.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class SwapViewButtonClickedEvent extends GwtEvent<SwapViewButtonClickedEvent.SwapViewButtonClickedEventHandler> {

    public interface SwapViewButtonClickedEventHandler extends EventHandler {
        void onSwapViewButtonClicked(SwapViewButtonClickedEvent event);
    }

    public interface HasSwapViewButtonClickedEventHandlers {
        HandlerRegistration addSwapViewButtonClickedEventHandler(SwapViewButtonClickedEventHandler handler);
    }

    public static Type<SwapViewButtonClickedEventHandler> TYPE =
            new Type<SwapViewButtonClickedEventHandler>();

    public Type<SwapViewButtonClickedEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SwapViewButtonClickedEventHandler handler) {
        handler.onSwapViewButtonClicked(this);
    }
}
