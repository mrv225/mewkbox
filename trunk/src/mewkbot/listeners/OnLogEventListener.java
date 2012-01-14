package mewkbot.listeners;

import java.util.EventListener;
import mewkbot.events.OnLogEvent;

/**
 *
 * @author Mewes
 */
public interface OnLogEventListener extends EventListener {
    public void onLogEventOccurred(OnLogEvent evt);
}