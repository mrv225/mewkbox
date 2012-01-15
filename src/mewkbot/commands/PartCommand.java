package mewkbot.commands;

import mewkbot.IrcBot;
import mewkbot.ICommand;
import mewkbot.entities.Channel;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class PartCommand implements ICommand {
    
   @Override
    public int getRequiredRoles() {
        return IrcBot.CMD_ADMIN;
    }
   
    @Override
    public String getName() {
        return "!part";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        if (user.isAdmin() && data.startsWith("#")) {
            client.log("part " + data);
            client.partChannel(data);
        }

        return true;
    }
}