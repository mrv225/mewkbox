package mewkbot.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mewes
 */
public class Configuration {
    private String host;
    private int port;
    private String pass;
    private String name;
    private String nick;
    private List<String> admins = new ArrayList<String>();
    private List<String> channels = new ArrayList<String>();
    private List<Trigger> triggers = new ArrayList<Trigger>();

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * @return the admins
     */
    public List<String> getAdmins() {
        return admins;
    }

    /**
     * @param admins the admins to set
     */
    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    /**
     * @return the channels
     */
    public List<String> getChannels() {
        return channels;
    }

    /**
     * @param channels the channels to set
     */
    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    /**
     * @return the triggers
     */
    public List<Trigger> getTriggers() {
        return triggers;
    }

    /**
     * @param triggers the triggers to set
     */
    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }
}