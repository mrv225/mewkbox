package mewkbot;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import mewkbot.commands.*;
import mewkbot.listeners.*;
import mewkbot.entities.*;
import mewkbot.events.*;

/**
 *
 * @author Mewes
 */
public class MainFrame 
    extends 
        javax.swing.JFrame 
    implements 
        OnConfigChangeEventListener, 
        OnLogEventListener, 
        OnReceiveEventListener, 
        OnSendEventListener, 
        OnStartEventListener, 
        OnStopEventListener {

    Configuration configuration = null;
    IrcBot bot = null;
    Thread botThread = null;

    public MainFrame() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
          System.out.println("Error setting native LAF: " + e);
        }
        
        this.setIconImage(new javax.swing.ImageIcon(this.getClass().getResource("/mewkbot/resources/on.png")).getImage());
            
        this.initComponents();
        
        this.listEditorAdmins.addColumns(new String[] { "Name" });
        this.listEditorAdmins.setEntityName("Admin");
        
        this.listEditorChannels.addColumns(new String[] { "Name" });
        this.listEditorChannels.setEntityName("Channel");
        
        this.listEditorTriggers.addColumns(new String[] { "Channel", "Trigger", "Message" });
        this.listEditorTriggers.setEntityName("Trigger");
    }

    private void startBot() {
        this.botThread = new Thread(this.bot);
        this.botThread.start();
    }

    private void stopBot() {
        try {
            this.bot.disconnect();
            if (!this.botThread.isInterrupted()) {
                this.botThread.interrupt();
            }
        } catch (Exception e) {
            this.textLog.append("ERR: " + e.getMessage() + "\n");
        }
    }
    
    private void loadConfig() {
        try {
            FileInputStream is = new FileInputStream("config.xml");
            XMLDecoder e = new XMLDecoder(is);
            this.configuration = (Configuration) e.readObject();
            e.close();
        } catch (Exception e) {
            this.textLog.append("ERR: " + e.getMessage() + "\n");
            this.configuration = new Configuration();
        }
    }
    
    private void saveConfig() {
        try {
            FileOutputStream os = new FileOutputStream("config.xml");
            XMLEncoder e = new XMLEncoder(os);
            e.writeObject(this.configuration);
            e.flush();
            e.close();
        } catch (Exception e) {
            this.textLog.append("ERR: " + e.getMessage() + "\n");
        }
    }
    
    private void config2Gui() {
        this.textHost.setText(this.configuration.getHost());
        this.formattedTextPort.setValue(this.configuration.getPort());
        this.textPassword.setText(this.configuration.getPass());
        this.textNickname.setText(this.configuration.getNick());
        this.textUsername.setText(this.configuration.getName());
        
        // load admins
        List<String[]> rowsAdmin = new ArrayList<String[]>();
        for (String admin : this.configuration.getAdmins()) {
            rowsAdmin.add(new String[] { admin });
        }
        this.listEditorAdmins.addRows(rowsAdmin);
        
        
        // load channels
        List<String[]> rowsChannel = new ArrayList<String[]>();
        for (String channel : this.configuration.getChannels()) {
            rowsChannel.add(new String[] { channel });
        }
        this.listEditorChannels.addRows(rowsChannel);
        
        
        // load triggers
        List<String[]> rowsTrigger = new ArrayList<String[]>();
        for (Trigger trigger : this.configuration.getTriggers()) {
            rowsTrigger.add(new String[] { trigger.getChannel(), trigger.getTrigger(), trigger.getMessage() });
        }
        this.listEditorTriggers.addRows(rowsTrigger);
    }
    
    private void gui2Config() {
        // validate port
        Integer port = (Integer) this.formattedTextPort.getValue();
        if (port < 0 || port > 65535) {
            port = this.configuration.getPort();
            this.formattedTextPort.setValue(port);
        }
        
        this.configuration.setHost(this.textHost.getText());
        this.configuration.setPort(port);
        this.configuration.setPass(new String(this.textPassword.getPassword()));
        this.configuration.setNick(this.textNickname.getText());
        this.configuration.setName(this.textUsername.getText());
        
        // save admins
        List<String> admins = new ArrayList<String>();
        for (String[] row : this.listEditorAdmins.getRows()) {
            admins.add(row[0]);
        }
        this.configuration.setAdmins(admins);        
        
        // save channels
        List<String> channels = new ArrayList<String>();
        for (String[] row : this.listEditorChannels.getRows()) {
            channels.add(row[0]);
        }
        this.configuration.setChannels(channels);          
        
        // save triggers
        List<Trigger> triggers = new ArrayList<Trigger>();
        for (String[] row : this.listEditorTriggers.getRows()) {
            triggers.add(new Trigger(row[0], row[1], row[2]));
        }
        this.configuration.setTriggers(triggers);
    }
    

    @Override
    public void onConfigChangeEventOccurred(OnConfigChangeEvent evt) {
        this.gui2Config();
    }
    
    @Override
    public void onLogEventOccurred(OnLogEvent evt) {
        String data = evt.getData();
        if (data != null) {
            this.textLog.append("LOG: " + data.trim() + "\n");
        }
    }

    @Override
    public void onReceiveEventOccurred(OnReceiveEvent evt) {
        String data = "IN:  " + evt.getData().trim() + "\n";
        this.textLog.append(data);
    }

    @Override
    public void onSendEventOccurred(OnSendEvent evt) {
        String data = "OUT: " + evt.getData().trim() + "\n";
        this.textLog.append(data);
    }

    @Override
    public void onStartEventOccurred(OnStartEvent evt) {
        this.buttonStart.setEnabled(false);
        this.buttonStop.setEnabled(true);
        this.labelStatusIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mewkbot/resources/on.png")));
    }

    @Override
    public void onStopEventOccurred(OnStopEvent evt) {
        this.buttonStart.setEnabled(true);
        this.buttonStop.setEnabled(false);
        this.labelStatusIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mewkbot/resources/off.png")));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPaneMain = new javax.swing.JTabbedPane();
        scrollPaneLog = new javax.swing.JScrollPane();
        textLog = new javax.swing.JTextArea();
        tabPaneConfiguration = new javax.swing.JTabbedPane();
        panelServer = new javax.swing.JPanel();
        labelHost = new javax.swing.JLabel();
        textHost = new javax.swing.JTextField();
        labelPort = new javax.swing.JLabel();
        formattedTextPort = new javax.swing.JFormattedTextField();
        labelPassword = new javax.swing.JLabel();
        textPassword = new javax.swing.JPasswordField();
        panelUser = new javax.swing.JPanel();
        labelNickname = new javax.swing.JLabel();
        textNickname = new javax.swing.JTextField();
        labelUsername = new javax.swing.JLabel();
        textUsername = new javax.swing.JTextField();
        listEditorAdmins = new mewkbot.ListEditor();
        listEditorTriggers = new mewkbot.ListEditor();
        listEditorChannels = new mewkbot.ListEditor();
        buttonStart = new javax.swing.JButton();
        buttonStop = new javax.swing.JButton();
        labelStatusIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MewKBot");
        setIconImage(getIconImage());
        setMinimumSize(new java.awt.Dimension(300, 300));
        setName("mainFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        textLog.setColumns(20);
        textLog.setFont(new java.awt.Font("Courier New", 0, 13)); // NOI18N
        textLog.setRows(5);
        scrollPaneLog.setViewportView(textLog);

        tabPaneMain.addTab("Log", scrollPaneLog);

        labelHost.setText("Host:");

        labelPort.setText("Port:");

        formattedTextPort.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####"))));

        labelPassword.setText("Password:");

        javax.swing.GroupLayout panelServerLayout = new javax.swing.GroupLayout(panelServer);
        panelServer.setLayout(panelServerLayout);
        panelServerLayout.setHorizontalGroup(
            panelServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelHost)
                    .addComponent(labelPort)
                    .addComponent(labelPassword))
                .addGap(10, 10, 10)
                .addGroup(panelServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textHost, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(formattedTextPort, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(textPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelServerLayout.setVerticalGroup(
            panelServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelHost)
                    .addComponent(textHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPort)
                    .addComponent(formattedTextPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPassword))
                .addContainerGap(220, Short.MAX_VALUE))
        );

        tabPaneConfiguration.addTab("Server", panelServer);

        labelNickname.setText("Nickname:");

        labelUsername.setText("Username:");

        javax.swing.GroupLayout panelUserLayout = new javax.swing.GroupLayout(panelUser);
        panelUser.setLayout(panelUserLayout);
        panelUserLayout.setHorizontalGroup(
            panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelNickname)
                    .addComponent(labelUsername))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                    .addComponent(textNickname, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelUserLayout.setVerticalGroup(
            panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNickname)
                    .addComponent(textNickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelUsername)
                    .addComponent(textUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(251, Short.MAX_VALUE))
        );

        tabPaneConfiguration.addTab("User", panelUser);
        tabPaneConfiguration.addTab("Admins", listEditorAdmins);
        tabPaneConfiguration.addTab("Triggers", listEditorTriggers);
        tabPaneConfiguration.addTab("Channels", listEditorChannels);

        tabPaneMain.addTab("Config", tabPaneConfiguration);

        buttonStart.setText("Start");
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });

        buttonStop.setText("Stop");
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStopActionPerformed(evt);
            }
        });

        labelStatusIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mewkbot/resources/off.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStop)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
                        .addComponent(labelStatusIcon)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelStatusIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonStart, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
    this.buttonStart.setEnabled(false);
    this.buttonStop.setEnabled(true);
    this.startBot();
}//GEN-LAST:event_buttonStartActionPerformed

private void buttonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStopActionPerformed
    this.buttonStart.setEnabled(true);
    this.buttonStop.setEnabled(false);
    this.stopBot();
}//GEN-LAST:event_buttonStopActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    this.stopBot();
    this.gui2Config();
    this.saveConfig();
}//GEN-LAST:event_formWindowClosing

private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    this.loadConfig();
    this.config2Gui();

    this.bot = new IrcBot(this.configuration);

    this.bot.addBotCommand(new JoinCommand());
    this.bot.addBotCommand(new PartCommand());
    this.bot.addBotCommand(new QuitCommand());
    this.bot.addBotCommand(new RejoinCommand());
    this.bot.addBotCommand(new SayCommand());
    this.bot.addBotCommand(new ServerCommand());
    this.bot.addBotCommand(new SetCommand());
    this.bot.addBotCommand(new UnsetCommand());
    
    this.bot.addOnConfigChangeEventListener(this);
    this.bot.addOnLogEventListener(this);
    this.bot.addOnReceiveEventListener(this);
    this.bot.addOnSendEventListener(this);
    this.bot.addOnStartEventListener(this);
    this.bot.addOnStopEventListener(this);
}//GEN-LAST:event_formWindowOpened

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonStop;
    private javax.swing.JFormattedTextField formattedTextPort;
    private javax.swing.JLabel labelHost;
    private javax.swing.JLabel labelNickname;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelPort;
    private javax.swing.JLabel labelStatusIcon;
    private javax.swing.JLabel labelUsername;
    private mewkbot.ListEditor listEditorAdmins;
    private mewkbot.ListEditor listEditorChannels;
    private mewkbot.ListEditor listEditorTriggers;
    private javax.swing.JPanel panelServer;
    private javax.swing.JPanel panelUser;
    private javax.swing.JScrollPane scrollPaneLog;
    private javax.swing.JTabbedPane tabPaneConfiguration;
    private javax.swing.JTabbedPane tabPaneMain;
    private javax.swing.JTextField textHost;
    private javax.swing.JTextArea textLog;
    private javax.swing.JTextField textNickname;
    private javax.swing.JPasswordField textPassword;
    private javax.swing.JTextField textUsername;
    // End of variables declaration//GEN-END:variables

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}