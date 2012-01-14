package mewkbot.listeners;

import java.util.EventListener;
import mewkbot.events.OnReceiveEvent;

/**
 *
 * @author Mewes
 */
public interface OnReceiveEventListener extends EventListener {
    public void onReceiveEventOccurred(OnReceiveEvent evt);
}