package mewkbot.commands;

import mewkbot.IrcBot;
import mewkbot.IrcBot.BotCommand;
import mewkbot.entities.Channel;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class SayCommand implements BotCommand {

    @Override
    public String getName() {
        return "!say";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        if ((user.isAdmin() || user.isOperator()) && data != null) {
            client.sendMessage(channel.getName(), data);
            client.log("say in " + channel.getName() + ": " + data);
        }
        
        return true;
    }
    
}