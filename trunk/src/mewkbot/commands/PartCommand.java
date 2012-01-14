package mewkbot.commands;

import mewkbot.IrcBot;
import mewkbot.IrcBot.BotCommand;
import mewkbot.entities.Channel;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class PartCommand implements BotCommand {

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