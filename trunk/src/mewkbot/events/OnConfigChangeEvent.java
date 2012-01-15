package mewkbot.events;

import java.util.EventObject;

/**
 *
 * @author Mewes
 */
public class OnConfigChangeEvent extends EventObject {
    private String data = null;
    
    public OnConfigChangeEvent(Object source) {
        super(source);
    }
    
    public OnConfigChangeEvent(Object source, String data) {
        super(source);
        this.data = data;
    }
    
    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }
}