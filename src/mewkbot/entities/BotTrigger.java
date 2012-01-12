package mewkbot.entities;

/**
 *
 * @author Mewes
 */
public class BotTrigger {
    private String channel;
    private String trigger;
    private String message;

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
      
    public BotTrigger() {
        this.channel = null;
        this.trigger = null;
        this.message = null;
    }
        
    public BotTrigger(String channel, String trigger, String message) {
        this.channel = channel;
        this.trigger = trigger;
        this.message = message;
    }
}
