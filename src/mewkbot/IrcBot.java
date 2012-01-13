package mewkbot;

import mewkbot.entities.User;
import mewkbot.entities.Trigger;
import mewkbot.entities.Configuration;
import mewkbot.events.OnReceiveEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.event.EventListenerList;
import mewkbot.entities.Channel;
import mewkbot.events.OnLogEvent;
import mewkbot.events.OnSendEvent;
import mewkbot.events.OnStartEvent;
import mewkbot.events.OnStopEvent;

/**
 *
 * @author Mewes
 */
public class IrcBot implements Runnable {

    public interface OnLogEventListener extends EventListener {

        public void onLogEventOccurred(OnLogEvent evt);
    }

    public interface OnReceiveEventListener extends EventListener {

        public void onReceiveEventOccurred(OnReceiveEvent evt);
    }

    public interface OnSendEventListener extends EventListener {

        public void onSendEventOccurred(OnSendEvent evt);
    }

    public interface OnStartEventListener extends EventListener {

        public void onStartEventOccurred(OnStartEvent evt);
    }

    public interface OnStopEventListener extends EventListener {

        public void onStopEventOccurred(OnStopEvent evt);
    }
    public static final String RPL_NAMREPLY = "353";
    protected Configuration config;
    private Map<String, Channel> channels = new HashMap<String, Channel>();
    
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public IrcBot(Configuration config) {
        this.config = config;
    }

    public void connect() throws UnknownHostException, IOException {
        this.socket = new Socket(this.config.getHost(), this.config.getPort());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void disconnect() throws IOException {
        this.sendData("QUIT");
        try {
            this.out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                this.in.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    this.socket.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void login() {
        if (this.config.getPass() != null) {
            this.sendData("PASS", this.config.getPass());
        }

        // 0 = default, 8 = invisible
        this.sendData("USER", this.config.getNick() + " 0 * :" + this.config.getName());
        this.sendData("NICK", this.config.getNick());
    }

    @Override
    public void run() {
        // connect
        try {
            this.connect();
            this.login();
            for (String channel : this.config.getChannels()) {
                this.joinChannel(channel);
            }
        } catch (Exception e) {
            this.log(e.getMessage());
        }
        this.fireOnStartEvent(new OnStartEvent(this));

        // main loop
        boolean _continue = true;
        while (_continue && !Thread.interrupted()) {
            try {
                String data = this.in.readLine();

                if (data != null) {
                    String[] dataParts = data.split(" ", 4);

                    // PING
                    if ("PING".equals(dataParts[0])) {
                        this.sendData("PONG", dataParts[1]);
                    } else {
                        String nickname = dataParts.length > 0 ? dataParts[0].trim() : null;
                        String command = dataParts.length > 1 ? dataParts[1].trim() : null;
                        String target = dataParts.length > 2 ? dataParts[2].trim() : null;
                        String content = dataParts.length > 3 ? dataParts[3].substring(1).trim() : null;

                        // PRIVMSG
                        if ("PRIVMSG".equals(command)) {
                            Channel channel = this.channels.get(target);
                            User user = channel.getUser(nickname);
                            
                            if (content != null & !content.isEmpty()) {
                                String[] messageParts = content.split(" ", 2);
                                String botCommand = messageParts.length > 0 ? messageParts[0].trim() : null;
                                String botParameter = messageParts.length > 1 ? messageParts[1].trim() : null;
                                String name = nickname.substring(1, nickname.indexOf("!"));

                                /*
                                 * Admin commands
                                 */
                                if (this.isAdmin(name)) {
                                    if ("!join".equalsIgnoreCase(botCommand)) {
                                        if (botParameter != null && botParameter.startsWith("#")) {
                                            this.joinChannel(botParameter);
                                            this.log("join " + botParameter);
                                        }
                                    } else if ("!rejoin".equalsIgnoreCase(botCommand)) {
                                        for (Entry<String, Channel> channelEntry : this.channels.entrySet()) {
                                            this.joinChannel(channelEntry.getKey());
                                        }
                                        this.log("rejoin all channels");
                                    } else if ("!part".equalsIgnoreCase(botCommand)) {
                                        if (botParameter != null && botParameter.startsWith("#")) {
                                            this.partChannel(botParameter);
                                            this.log("part " + botParameter);
                                        }
                                    } else if ("!shutdown".equalsIgnoreCase(botCommand)) {
                                        this.log("shutdown");
                                        _continue = false;
                                    }
                                }

                                /*
                                 * Operator commands
                                 */
                                if (this.isAdmin(name) || user.isOperator()) {
                                    if ("!say".equalsIgnoreCase(botCommand)) {
                                        if (botParameter != null) {
                                            this.sendMessage(target, botParameter);
                                            this.log("say in " + target + ": " + botParameter);
                                        }
                                    }
                                    if ("!set".equalsIgnoreCase(botCommand)) {
                                        // TODO: Overwrite

                                        String botParameter2 = botParameter.substring(botParameter.indexOf(" ")).trim();
                                        botParameter = botParameter.substring(0, botParameter.indexOf(" ")).trim();

                                        boolean _replaced = false;
                                        for (int i = 0; i < this.config.getTriggers().size(); i++) {
                                            if (this.config.getTriggers().get(i).getTrigger().equalsIgnoreCase("!" + botParameter)) {
                                                this.config.getTriggers().get(i).setMessage(botParameter2);
                                                _replaced = true;
                                                break;
                                            }
                                        }

                                        if (!_replaced) {
                                            this.config.getTriggers().add(new Trigger(target, "!" + botParameter, botParameter2));
                                        }
                                        this.log("set trigger in " + target + ": " + botParameter + " = " + botParameter2);
                                    }
                                    if ("!unset".equalsIgnoreCase(botCommand)) {
                                        for (int i = 0; i < this.config.getTriggers().size(); i++) {
                                            if (this.config.getTriggers().get(i).getTrigger().equalsIgnoreCase("!" + botParameter)) {
                                                this.config.getTriggers().remove(i);
                                                this.log("unset trigger in " + target + ": " + botParameter);
                                                break;
                                            }
                                        }
                                    }
                                }

                                /*
                                 * User triggers
                                 */

                                if ("!server".equalsIgnoreCase(botCommand)) {
                                    String[] infoArray = MineQueryClient.query("seegurke.endoftheinternet.org", 25565, 1000);
                                    if (infoArray != null) {
                                        this.sendMessage(target, "Server is online. ("+infoArray[1]+"/"+infoArray[2]+")");
                                    } else {
                                        this.sendMessage(target, "Server is offline.");
                                    }
                                    this.log("trigger in " + target + ": server");
                                }

                                if (botCommand.startsWith("!") && target.startsWith("#")) {
                                    for (int i = 0; i < this.config.getTriggers().size(); i++) {
                                        if (this.config.getTriggers().get(i).getTrigger().equalsIgnoreCase(botCommand)) {
                                            this.sendMessage(target, this.config.getTriggers().get(i).getMessage());
                                            this.log("trigger in " + target + ": " + botCommand);
                                            break;
                                        }
                                    }
                                }
                            }
                        } // MODE
                        else if ("MODE".equals(command)) {
                            try {
                                String mode = content.substring(0, content.indexOf(" ")).trim();
                                String name = content.substring(content.indexOf(" ") + 1).trim();
                                
                                Channel channel = this.channels.get(target);

                                // add OP
                                if ("+o".equals(mode)) {
                                    channel.getUser(name).setOperator(true);
                                } 

                                // remove OP
                                else if ("-o".equals(mode)) {
                                    channel.getUser(name).setOperator(false);
                                }
                            } catch (Exception e2) {
                                this.log(e2.getMessage());
                            }
                        } // NAMELIST
                        else if (RPL_NAMREPLY.equals(command)) {
                            try {
                                String channelName = content.substring(1, content.indexOf(":")).trim();
                                String namelist = content.substring(content.indexOf(":") + 1).trim();
                                String[] names = namelist.split(" ");

                                Channel channel = this.channels.get(channelName);
                                channel.setJoined(true);
                                
                                // reset users
                                channel.setUsers(new HashMap<String, User>());

                                // add users
                                for (String _nickname : names) {
                                    User user = new User();
                                    user.setNick(_nickname);
                                    user.setOperator(_nickname.startsWith("@"));
                                    channel.addUser(user);
                                }
                            } catch (Exception e2) {
                                this.log(e2.getMessage());
                            }
                        }
                    }

                    this.fireOnReceiveEvent(new OnReceiveEvent(this, data));
                }
            } catch (IOException e) {
                this.log(e.getMessage());
                _continue = false;
            } catch (Exception e) {
                this.log(e.getMessage());
            }

            if (Thread.currentThread().isInterrupted()) {
                _continue = false;
            }
        }

        // disconnect
        try {
            this.disconnect();
        } catch (IOException e) {
            this.log(e.getMessage());
        }
        this.fireOnStopEvent(new OnStopEvent(this));
    }

    public void sendData(String cmd, String data) {
        this.sendData(cmd.trim() + " " + data.trim());
    }

    public void sendData(String data) {
        this.out.print(data.trim() + "\r\n");
        this.out.flush();
        this.fireOnSendEvent(new OnSendEvent(this, data));
    }

    public void sendMessage(String target, String message) {
        this.sendData("PRIVMSG", target.trim() + " :" + message.trim());
    }

    public void log(String string) {
        this.fireOnLogEvent(new OnLogEvent(this, string));
    }

    /*
     * Channel Management
     */
    public void joinChannel(String name) {
        Channel channel = null;
        if (!this.channels.containsKey(name)) {
            channel = new Channel();
            channel.setName(name);
            this.channels.put(name, channel);
        } else {
            channel = this.channels.get(name);
        }
        this.sendData("JOIN", channel.getName());
    }

    public void partChannel(String name) {
        if (this.channels.containsKey(name)) {
            this.channels.remove(name);
        }
        this.sendData("PART", name);
    }

    public boolean isAdmin(String name) {
        return this.config.getAdmins().contains(name);
    }
    /*
     * OnLogEvent
     */
    protected EventListenerList onLogListenerList = new EventListenerList();

    public void addOnLogEventListener(OnLogEventListener listener) {
        this.onLogListenerList.add(OnLogEventListener.class, listener);
    }

    public void removeOnLogEventListener(OnLogEventListener listener) {
        this.onLogListenerList.remove(OnLogEventListener.class, listener);
    }

    private void fireOnLogEvent(OnLogEvent evt) {
        Object[] listeners = this.onLogListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == OnLogEventListener.class) {
                ((OnLogEventListener) listeners[i + 1]).onLogEventOccurred(evt);
            }
        }
    }
    /*
     * OnReceiveEvent
     */
    protected EventListenerList onReceiveListenerList = new EventListenerList();

    public void addOnReceiveEventListener(OnReceiveEventListener listener) {
        this.onReceiveListenerList.add(OnReceiveEventListener.class, listener);
    }

    public void removeOnReceiveEventListener(OnReceiveEventListener listener) {
        this.onReceiveListenerList.remove(OnReceiveEventListener.class, listener);
    }

    private void fireOnReceiveEvent(OnReceiveEvent evt) {
        Object[] listeners = this.onReceiveListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == OnReceiveEventListener.class) {
                ((OnReceiveEventListener) listeners[i + 1]).onReceiveEventOccurred(evt);
            }
        }
    }
    /*
     * OnSendEvent
     */
    protected EventListenerList onSendEventListenerList = new EventListenerList();

    public void addOnSendEventListener(OnSendEventListener listener) {
        this.onSendEventListenerList.add(OnSendEventListener.class, listener);
    }

    public void removeOnSendEventListener(OnSendEventListener listener) {
        this.onSendEventListenerList.remove(OnSendEventListener.class, listener);
    }

    private void fireOnSendEvent(OnSendEvent evt) {
        Object[] listeners = this.onSendEventListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == OnSendEventListener.class) {
                ((OnSendEventListener) listeners[i + 1]).onSendEventOccurred(evt);
            }
        }
    }
    /*
     * OnStartEvent
     */
    protected EventListenerList onStartEventListenerList = new EventListenerList();

    public void addOnStartEventListener(OnStartEventListener listener) {
        this.onStartEventListenerList.add(OnStartEventListener.class, listener);
    }

    public void removeOnStartEventListener(OnStartEventListener listener) {
        this.onStartEventListenerList.remove(OnStartEventListener.class, listener);
    }

    private void fireOnStartEvent(OnStartEvent evt) {
        Object[] listeners = this.onStartEventListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == OnStartEventListener.class) {
                ((OnStartEventListener) listeners[i + 1]).onStartEventOccurred(evt);
            }
        }
    }
    /*
     * OnStopEvent
     */
    protected EventListenerList onStopEventListenerList = new EventListenerList();

    public void addOnStopEventListener(OnStopEventListener listener) {
        this.onStopEventListenerList.add(OnStopEventListener.class, listener);
    }

    public void removeOnStopEventListener(OnStopEventListener listener) {
        this.onStopEventListenerList.remove(OnStopEventListener.class, listener);
    }

    private void fireOnStopEvent(OnStopEvent evt) {
        Object[] listeners = this.onStopEventListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == OnStopEventListener.class) {
                ((OnStopEventListener) listeners[i + 1]).onStopEventOccurred(evt);
            }
        }
    }
}
