package mewkbot.commands;

import java.util.Map.Entry;
import mewkbot.IrcBot;
import mewkbot.IrcBot.BotCommand;
import mewkbot.entities.Channel;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class RejoinCommand implements BotCommand {

    @Override
    public String getName() {
        return "!rejoin";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        if (user.isAdmin() && data.startsWith("#")) {
            client.log("rejoin all channels");
            for (Entry<String, Channel> channelEntry : client.getChannels().entrySet()) {
                client.joinChannel(channelEntry.getKey());
            }
        }

        return true;
    }
}