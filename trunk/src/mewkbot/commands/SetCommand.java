package mewkbot.commands;

import mewkbot.IrcBot;
import mewkbot.ICommand;
import mewkbot.entities.Channel;
import mewkbot.entities.Trigger;
import mewkbot.entities.User;
import mewkbot.events.OnConfigChangeEvent;

/**
 *
 * @author Mewes
 */
public class SetCommand implements ICommand {
    
   @Override
    public int getRequiredRoles() {
        return IrcBot.CMD_ADMIN | IrcBot.CMD_OPERATOR;
    }
   
    @Override
    public String getName() {
        return "!set";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        if ((user.isAdmin() || user.isOperator()) && data != null) {
            String message = data.substring(data.indexOf(" ")).trim();
            String name = "!" + data.substring(0, data.indexOf(" ")).trim();

            client.log("set trigger in " + channel.getName() + ": " + name + " = " + message);

            boolean _replaced = false;
            for (int i = 0; i < client.getConfig().getTriggers().size(); i++) {
                if (client.getConfig().getTriggers().get(i).getTrigger().equalsIgnoreCase(name)) {
                    client.getConfig().getTriggers().get(i).setMessage(message);
                    _replaced = true;
                    break;
                }
            }

            if (!_replaced) {
                client.getConfig().getTriggers().add(new Trigger(channel.getName(), name, message, 0));
                
            }
            
            client.fireOnConfigChangeEvent(new OnConfigChangeEvent(client));
        }
        
        return true;
    }
}