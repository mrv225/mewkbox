package mewkbot.listeners;

import java.util.EventListener;
import mewkbot.events.OnStartEvent;

/**
 *
 * @author Mewes
 */
public interface OnStartEventListener extends EventListener {
    public void onStartEventOccurred(OnStartEvent evt);
}