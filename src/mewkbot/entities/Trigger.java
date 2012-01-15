package mewkbot.entities;

/**
 *
 * @author Mewes
 */
public class Trigger {
    private String channel;
    private String trigger;
    private String message;
    private Integer coolDown;

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * @return the trigger
     */
    public String getTrigger() {
        return trigger;
    }

    /**
     * @param trigger the trigger to set
     */
    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the coolDown
     */
    public Integer getCoolDown() {
        return coolDown;
    }

    /**
     * @param coolDown the coolDown to set
     */
    public void setCoolDown(Integer coolDown) {
        this.coolDown = coolDown;
    }
      
    public Trigger() {
        this.channel = null;
        this.trigger = null;
        this.message = null;
        this.coolDown = 0;
    }
        
    public Trigger(String channel, String trigger, String message, Integer coolDown) {
        this.channel = channel;
        this.trigger = trigger;
        this.message = message;
        this.coolDown = coolDown;
    }
}