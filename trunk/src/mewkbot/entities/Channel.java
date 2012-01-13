package mewkbot.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mewes
 */
public class Channel {
    private String name = null;
    private boolean joined = false;
    private Map<String, User> users = new HashMap<String, User>();
    private Map<String, Double> commandCooldowns = new HashMap<String, Double>();
    
    /*
     * User Management
     */
    
    public void addUser(User user) {
        if (!this.users.containsKey(user.getNick())) {
            this.getUsers().put(user.getNick(), user);
        }
    }
    
    public void addUser(List<User> users) {
        for (User user : users) {
            this.addUser(user);  
        }
    }
    
    public User getUser(String nickname) throws Exception {
        if (!this.users.containsKey(nickname)) {
            throw new Exception("No such user exists.");
        }
        return this.getUsers().get(nickname);
    }
    
    public void removeUser(String nickname) {
        if (this.getUsers().containsKey(nickname)) {
            this.getUsers().remove(nickname);
        }
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
     * @return the joined
     */
    public boolean isJoined() {
        return joined;
    }

    /**
     * @param joined the joined to set
     */
    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    /**
     * @return the users
     */
    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    /**
     * @return the commandCooldowns
     */
    public Map<String, Double> getCommandCooldowns() {
        return commandCooldowns;
    }

    /**
     * @param commandCooldowns the commandCooldowns to set
     */
    public void setCommandCooldowns(Map<String, Double> commandCooldowns) {
        this.commandCooldowns = commandCooldowns;
    }
}
