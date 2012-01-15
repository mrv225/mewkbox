package mewkbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.EventListenerList;
import mewkbot.entities.Channel;
import mewkbot.entities.Configuration;
import mewkbot.entities.User;
import mewkbot.events.OnConfigChangeEvent;
import mewkbot.events.OnLogEvent;
import mewkbot.events.OnReceiveEvent;
import mewkbot.events.OnSendEvent;
import mewkbot.events.OnStartEvent;
import mewkbot.events.OnStopEvent;
import mewkbot.listeners.OnConfigChangeEventListener;
import mewkbot.listeners.OnLogEventListener;
import mewkbot.listeners.OnReceiveEventListener;
import mewkbot.listeners.OnSendEventListener;
import mewkbot.listeners.OnStartEventListener;
import mewkbot.listeners.OnStopEventListener;

/**
 *
 * @author Mewes
 */
public class IrcBot implements Runnable {

    public static final String RPL_WELCOME = "001";
    public static final String RPL_NAMREPLY = "353";
    //TODO
    public static final String ERR_NICKNAMEINUSE = "433";
    
    private Configuration config;
    private Map<String, Channel> channels = new HashMap<String, Channel>();
    private Map<String, ICommand> commands = new HashMap<String, ICommand>();
    
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public IrcBot(Configuration config) {
        this.config = config;
    }
    
    @Override
    public void run() {
        this.channels = new HashMap<String, Channel>();
        
        // connect
        try {
            this.connect();
            this.login();
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
                    // fire event
                    this.fireOnReceiveEvent(new OnReceiveEvent(this, data));

                    String[] dataParts = data.split(" ", 4);

                    // PING
                    if ("PING".equals(dataParts[0])) {
                        this.sendData("PONG", dataParts[1]);
                    } else {
                        String nickname = dataParts.length > 0 ? dataParts[0].trim() : null;
                        String command = dataParts.length > 1 ? dataParts[1].trim() : null;
                        String target = dataParts.length > 2 ? dataParts[2].trim() : null;
                        String content = dataParts.length > 3 ? (!dataParts[3].isEmpty() ? dataParts[3].substring(1).trim() : null) : null;

                        if ("JOIN".equals(command)) {
                            this.handleJoin(nickname, command, target, content);
                        } else if ("MODE".equals(command)) {
                            this.handleMode(nickname, command, target, content);
                        } else if ("PART".equals(command)) {
                            this.handlePart(nickname, command, target, content);
                        } else if ("PRIVMSG".equals(command)) {
                            this.handlePrivMsg(nickname, command, target, content);
                        } else if (RPL_NAMREPLY.equals(command)) {
                            this.handleRplNamereply(nickname, command, target, content);
                        } else if (RPL_WELCOME.equals(command)) {
                            this.handleRplWelcome(nickname, command, target, content);
                        }
                    }
                }
            } catch (IOException e) {
                this.log(e.getMessage());
                _continue = false;
            } catch (Exception e) {
                this.log(e.getMessage());
            }
        }

        // disconnect
        try {
            this.disconnect();
        } catch (IOException e) {
            this.log(e.getMessage());
        }
    }
    
    /*
     * Connection Handling
     */
    
    private void connect() throws UnknownHostException, IOException {
        this.socket = new Socket(this.getConfig().getHost(), this.getConfig().getPort());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void disconnect() throws IOException {
        if (!this.socket.isClosed()) {
            this.sendData("QUIT");
            
            try {
                this.out.close();
            } catch (Exception e) {
                this.log(e.getMessage());
            } finally {
                try {
                    this.in.close();
                } catch (Exception e) {
                    this.log(e.getMessage());
                } finally {
                    try {
                        this.socket.close();
                    } catch (Exception e) {
                        this.log(e.getMessage());
                    }
                }
            }
            
            this.fireOnStopEvent(new OnStopEvent(this));
        }
    }

    public void login() {
        if (!this.getConfig().getPass().trim().equals("")) {
            this.sendData("PASS", this.getConfig().getPass());
        }

        // 0 = default, 8 = invisible
        this.sendData("USER", this.getConfig().getNick() + " 0 * :" + this.getConfig().getName());
        this.sendData("NICK", this.getConfig().getNick());
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

    /*
     * IRC Command Handler
     */
    
    //<editor-fold defaultstate="collapsed" desc="IRC Command Handler">
    private boolean handleJoin(String nickname, String command, String target, String content) {
        try {
            String name = nickname.substring(1, nickname.indexOf("!"));
            
            User user = new User();
            user.setNick(name);
            
            Channel channel = this.getChannels().get(target);
            channel.addUser(user);
        } catch (Exception e) {
            this.log(e.getMessage());
        }
        
        return true;
    }
    
    private boolean handleMode(String nickname, String command, String target, String content) {
        try {
            if (content.indexOf(" ") > 0) {
                String mode = content.substring(0, content.indexOf(" ")).trim();
                String name = content.substring(content.indexOf(" ") + 1).trim();

                Channel channel = this.getChannels().get(target);

                // add OP
                if ("+o".equals(mode)) {
                    channel.getUser(name).setOperator(true);
                }

                // remove OP
                else if ("-o".equals(mode)) {
                    channel.getUser(name).setOperator(false);
                }
            }
        } catch (Exception e) {
            this.log(e.getMessage());
        }
        
        return true;
    }
    
    private boolean handlePart(String nickname, String command, String target, String content) {
        String name = nickname.substring(1, nickname.indexOf("!"));
        
        Channel channel = this.getChannels().get(target);
        channel.removeUser(name);
        
        return true;
    }
    
    private boolean handlePrivMsg(String nickname, String command, String target, String content) {
        boolean _continue = true;
        
        try {
            Channel channel = this.getChannels().get(target);
            
            if (content != null & !content.isEmpty()) {
                String[] messageParts = content.split(" ", 2);
                String botCommand = messageParts.length > 0 ? messageParts[0].trim() : null;
                String botParameter = messageParts.length > 1 ? messageParts[1].trim() : null;
                String name = nickname.substring(1, nickname.indexOf("!"));
                
                User user = null;
                if (channel != null) {
                    user = channel.getUser(name);
                } else {
                    user = new User();
                    user.setNick(name);
                }
                
                // lookup command
                ICommand botCommandInstance = this.commands.get(botCommand);
                if (botCommandInstance != null) {
                    _continue = botCommandInstance.run(user, channel, botParameter, this);
                } else {
                    // default command
                    if (botCommand.startsWith("!") && channel != null) {
                        for (int i = 0; i < this.getConfig().getTriggers().size(); i++) {
                            if (this.getConfig().getTriggers().get(i).getTrigger().equalsIgnoreCase(botCommand)) {
                                this.log("trigger in " + channel.getName() + ": " + botCommand);
                                this.sendMessage(channel.getName(), this.getConfig().getTriggers().get(i).getMessage());
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            this.log(e.getMessage());
        }
        
        return _continue;
    }
    
    private boolean handleRplNamereply(String nickname, String command, String target, String content) {
        try {
            String channelName = content.substring(0, content.indexOf(":")).trim();
            String namelist = content.substring(content.indexOf(":") + 1).trim();
            String[] names = namelist.split(" ");
            
            Channel channel = this.getChannels().get(channelName);
            channel.setJoined(true);
            
            // reset users
            channel.setUsers(new HashMap<String, User>());
            
            // add users
            for (String _nickname : names) {
                User user = new User();
                if (_nickname.startsWith("@")) {
                    user.setNick(_nickname.substring(1));
                    user.setOperator(true);
                } else {
                    user.setNick(_nickname);
                }
                channel.addUser(user);
            }
        } catch (Exception e) {
            this.log(e.getMessage());
        }
        
        return true;
    }
    
    private boolean handleRplWelcome(String nickname, String command, String target, String content) {
        for (String channel : this.getConfig().getChannels()) {
            this.joinChannel(channel);
        }
        
        return true;
    }
    //</editor-fold>
    
    /*
     * Misc
     */
    
    public void log(String string) {
        this.fireOnLogEvent(new OnLogEvent(this, string));
    }

    public boolean isAdmin(String name) {
        return this.getConfig().getAdmins().contains(name);
    }
    
    public void addBotCommand(ICommand botCommand) {
        this.commands.put(botCommand.getName(), botCommand);
    }
    
    /*
     * Channel Management
     */
    
    public void joinChannel(String name) {
        Channel channel = null;
        
        if (!this.channels.containsKey(name)) {
            channel = new Channel();
            channel.setName(name);
            this.getChannels().put(name, channel);
            
            if (!this.config.getChannels().contains(name)) {
                this.config.getChannels().add(name);
                this.fireOnConfigChangeEvent(new OnConfigChangeEvent(this));
            }
        } else {
            channel = this.getChannels().get(name);
        }
        
        this.sendData("JOIN", channel.getName());
    }

    public void partChannel(String name) {
        if (this.getChannels().containsKey(name)) {
            this.getChannels().remove(name);
            
            if (this.config.getChannels().contains(name)) {
                this.config.getChannels().remove(name);
                this.fireOnConfigChangeEvent(new OnConfigChangeEvent(this));
            }
        }
        
        this.sendData("PART", name);
    }
    
    /*
     * Getter
     */
        
    //<editor-fold defaultstate="collapsed" desc="Getter">
    /**
     * @return the channels
     */
    public Map<String, Channel> getChannels() {
        return channels;
    }
    
    /**
     * @return the config
     */
    public Configuration getConfig() {
        return config;
    }
    //</editor-fold>
    
    /*
     * Events
     */
    
    //<editor-fold defaultstate="collapsed" desc="OnConfigChangeEvent">
    /*
     * OnConfigChangeEvent
     */
       
    protected EventListenerList onConfigChangeEventListenerList = new EventListenerList();
    
    public void addOnConfigChangeEventListener(OnConfigChangeEventListener listener) {
        this.onConfigChangeEventListenerList.add(OnConfigChangeEventListener.class, listener);
    }
    
    public void removeOnConfigChangeEventListener(OnConfigChangeEventListener listener) {
        this.onConfigChangeEventListenerList.remove(OnConfigChangeEventListener.class, listener);
    }
    
    public void fireOnConfigChangeEvent(OnConfigChangeEvent evt) {
        Object[] listeners = this.onConfigChangeEventListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == OnConfigChangeEventListener.class) {
                ((OnConfigChangeEventListener) listeners[i + 1]).onConfigChangeEventOccurred(evt);
            }
        }
    }
    //</editor-fold>    
    
    //<editor-fold defaultstate="collapsed" desc="OnLogEvent">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="OnReceiveEvent">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="OnSendEvent">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="OnStartEvent">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="OnStopEvent">
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
    //</editor-fold>
}