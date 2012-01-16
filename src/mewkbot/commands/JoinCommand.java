package mewkbot.commands;

import mewkbot.ICommand;
import mewkbot.IrcBot;
import mewkbot.entities.Channel;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class JoinCommand implements ICommand {

    @Override
    public int getRequiredRoles() {
        return IrcBot.CMD_ADMIN;
    }
    
    @Override
    public String getName() {
        return "!join";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        if (user.isAdmin() && data.startsWith("#")) {
            client.log("join " + data);
            client.joinChannel(data);
        }

        return true;
    }
}