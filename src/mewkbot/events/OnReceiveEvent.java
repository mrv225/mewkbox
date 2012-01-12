package mewkbot.events;

import java.util.EventObject;

/**
 *
 * @author Mewes
 */
public class OnReceiveEvent extends EventObject {
    private String data = null;
    
    public OnReceiveEvent(Object source) {
        super(source);
    }
    
    public OnReceiveEvent(Object source, String data) {
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