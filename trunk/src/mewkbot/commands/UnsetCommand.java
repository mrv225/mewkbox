package mewkbot.commands;

import mewkbot.IrcBot;
import mewkbot.ICommand;
import mewkbot.entities.Channel;
import mewkbot.entities.User;
import mewkbot.events.OnConfigChangeEvent;

/**
 *
 * @author Mewes
 */
public class UnsetCommand implements ICommand {

   @Override
    public int getRequiredRoles() {
        return IrcBot.CMD_ADMIN | IrcBot.CMD_OPERATOR;
    }    
    
    @Override
    public String getName() {
        return "!unset";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        if ((user.isAdmin() || user.isOperator()) && data != null) {
            boolean _unsetted = false;
            
            for (int i = 0; i < client.getConfig().getTriggers().size(); i++) {
                if (client.getConfig().getTriggers().get(i).getTrigger().equalsIgnoreCase("!" + data)) {
                    client.log("unset trigger in " + channel.getName() + ": " + data);
                    client.getConfig().getTriggers().remove(i);
                    _unsetted = true;
                    break;
                }
            }
            
            if (_unsetted) {
                client.fireOnConfigChangeEvent(new OnConfigChangeEvent(client));
            }
        }
        
        return true;
    }
}