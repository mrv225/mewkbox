package mewkbot.listeners;

import java.util.EventListener;
import mewkbot.events.OnSendEvent;

/**
 *
 * @author Mewes
 */
public interface OnSendEventListener extends EventListener {
    public void onSendEventOccurred(OnSendEvent evt);
}