package mewkbot.entities;

/**
 *
 * @author Mewes
 */
public class BotOperator {
    private String channel;
    private String nick;

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
     * @return the nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * @param nick the nick to set
     */
    public void setNick(String nick) {
        this.nick = nick;
    }
    
    public BotOperator() {
        this.channel = null;
        this.nick = null;
    }
        
    public BotOperator(String channel, String nick) {
        this.channel = channel;
        this.nick = nick;
    }
}