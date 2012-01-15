package mewkbot;

import mewkbot.entities.Channel;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public interface ICommand {
    public int getRequiredRoles();
    public String getName();
    public boolean run(User user, Channel channel, String data, IrcBot client);
}