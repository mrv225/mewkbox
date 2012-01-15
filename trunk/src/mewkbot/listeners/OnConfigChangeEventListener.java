package mewkbot.listeners;

import java.util.EventListener;
import mewkbot.events.OnConfigChangeEvent;

/**
 *
 * @author Mewes
 */
public interface OnConfigChangeEventListener extends EventListener {
    public void onConfigChangeEventOccurred(OnConfigChangeEvent evt);
}