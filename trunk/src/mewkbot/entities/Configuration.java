package mewkbot.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mewes
 */
public class Configuration {
    private String host;
    private Integer port;
    private String pass;
    private String altNickname;
    private String nickname;
    private List<String> admins = new ArrayList<String>();
    private List<String> channels = new ArrayList<String>();
    private List<Trigger> triggers = new ArrayList<Trigger>();
    private Map<String, Integer> commandCoolDowns = new HashMap<String, Integer>();
    
    private String minecraftServerHost;
    private Integer minecraftServerPort;

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
    public Integer getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(Integer port) {
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
     * @return the altNickname
     */
    public String getAltNickname() {
        return altNickname;
    }

    /**
     * @param altNickname the altNickname to set
     */
    public void setAltNickname(String altNickname) {
        this.altNickname = altNickname;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    /**
     * @return the minecraftServerHost
     */
    public String getMinecraftServerHost() {
        return minecraftServerHost;
    }

    /**
     * @param minecraftServerHost the minecraftServerHost to set
     */
    public void setMinecraftServerHost(String minecraftServerHost) {
        this.minecraftServerHost = minecraftServerHost;
    }

    /**
     * @return the minecraftServerPort
     */
    public Integer getMinecraftServerPort() {
        return minecraftServerPort;
    }

    /**
     * @param minecraftServerPort the minecraftServerPort to set
     */
    public void setMinecraftServerPort(Integer minecraftServerPort) {
        this.minecraftServerPort = minecraftServerPort;
    }

    /**
     * @return the commandCoolDowns
     */
    public Map<String, Integer> getCommandCoolDowns() {
        return commandCoolDowns;
    }

    /**
     * @param commandCoolDowns the commandCoolDowns to set
     */
    public void setCommandCoolDowns(Map<String, Integer> commandCoolDowns) {
        this.commandCoolDowns = commandCoolDowns;
    }
}