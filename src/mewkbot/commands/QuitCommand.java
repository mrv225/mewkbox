package mewkbot.commands;

import mewkbot.IrcBot;
import mewkbot.ICommand;
import mewkbot.entities.Channel;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class QuitCommand implements ICommand {
   
    @Override
    public int getRequiredRoles() {
        return IrcBot.CMD_ADMIN;
    }
   
    @Override
    public String getName() {
        return "!quit";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        if (user.isAdmin()) {
            client.log("quit");
            return false;
        }
        return true;
    }
}