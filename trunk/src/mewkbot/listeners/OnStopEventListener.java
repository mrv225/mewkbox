package mewkbot.listeners;

import java.util.EventListener;
import mewkbot.events.OnStopEvent;

/**
 *
 * @author Mewes
 */
public interface OnStopEventListener extends EventListener {
    public void onStopEventOccurred(OnStopEvent evt);
}