package mewkbot.commands;

import java.net.SocketTimeoutException;
import mewkbot.IrcBot;
import mewkbot.ICommand;
import mewkbot.MineQueryClient;
import mewkbot.entities.Channel;
import mewkbot.entities.User;

/**
 *
 * @author Mewes
 */
public class ServerCommand implements ICommand {

    @Override
    public String getName() {
        return "!server";
    }

    @Override
    public boolean run(User user, Channel channel, String data, IrcBot client) {
        client.log("trigger in " + channel.getName() + ": server");
        
        try {
            String[] infoArray = MineQueryClient.query("64.79.96.84", 25565, 1000);
            if (infoArray != null) {
                client.sendMessage(channel.getName(), "Server is online. (" + infoArray[1] + "/" + infoArray[2] + ")");
            } else {
                client.sendMessage(channel.getName(), "Server is offline.");
            }
        } catch (SocketTimeoutException e) {
            client.log(e.getMessage());
        }

        return true;
    }
}