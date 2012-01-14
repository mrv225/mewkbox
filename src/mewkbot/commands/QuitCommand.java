package mewkbot.commands;

import mewkbot.IrcBot;
import mewkbot.IrcBot.BotCommand;
import mewkbot.entities.Channel;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class QuitCommand implements BotCommand {

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