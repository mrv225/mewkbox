package mewkbot.commands;

import java.util.List;
import mewkbot.ICommand;
import mewkbot.IrcBot;
import mewkbot.entities.Channel;
import mewkbot.entities.Trigger;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class DefaultCommand implements ICommand {

    @Override
    public int getRequiredRoles() {
        return IrcBot.CMD_ADMIN | IrcBot.CMD_OPERATOR | IrcBot.CMD_USER;
    }
    
    @Override
    public String getName() {
        return "*";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        //TODO Cooldown
        if (data.startsWith("!") && channel != null) {
            List<Trigger> triggers = client.getConfig().getTriggers();
            for (int i = 0; i < triggers.size(); i++) {
                Trigger trigger = triggers.get(i);
                if (trigger.getTrigger().equals(data) && (trigger.getChannel().contains(channel.getName()) || trigger.getChannel().isEmpty())) {
                    client.log("trigger in " + channel.getName() + ": " + data);
                    client.sendMessage(channel.getName(), triggers.get(i).getMessage());
                    break;
                }
            }
        }

        return true;
    }
}