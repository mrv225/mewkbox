package mewkbot.commands;

import mewkbot.IrcBot;
import mewkbot.IrcBot.BotCommand;
import mewkbot.entities.Channel;
import mewkbot.entities.Trigger;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class UnsetCommand implements BotCommand {

    @Override
    public String getName() {
        return "!unset";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        if ((user.isAdmin() || user.isOperator()) && data != null) {
            for (int i = 0; i < client.getConfig().getTriggers().size(); i++) {
                if (client.getConfig().getTriggers().get(i).getTrigger().equalsIgnoreCase("!" + data)) {
                    client.log("unset trigger in " + channel.getName() + ": " + data);
                    client.getConfig().getTriggers().remove(i);
                    break;
                }
            }
        }
        
        return true;
    }
}