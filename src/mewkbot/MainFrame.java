package mewkbot;

import java.beans.DefaultPersistenceDelegate;
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
public class MainFrame extends javax.swing.JFrame implements OnLogEventListener, OnReceiveEventListener, OnSendEventListener, OnStartEventListener, OnStopEventListener {

    Configuration configuration = null;
    IrcBot bot = null;
    Thread botThread = null;

    public MainFrame() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
          System.out.println("Error setting native LAF: " + e);
        }
        
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/mewkbot/resources/on.png")).getImage());
            
        initComponents();
        
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
            textLog.append("ERR: " + e.getMessage() + "\n");
        }
    }
    
    private void loadConfig() {
        try {
            FileInputStream is = new FileInputStream("config.xml");
            XMLDecoder e = new XMLDecoder(is);
            this.configuration = (Configuration) e.readObject();
            e.close();
        } catch (Exception e) {
            textLog.append("ERR: " + e.getMessage() + "\n");
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
            textLog.append("ERR: " + e.getMessage() + "\n");
        }
    }
    
    private void config2Gui() {
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
        
    }
    
    @Override
    public void onLogEventOccurred(OnLogEvent evt) {
        String data = evt.getData();
        if (data != null) {
            textLog.append("LOG: " + data.trim() + "\n");
        }
    }

    @Override
    public void onReceiveEventOccurred(OnReceiveEvent evt) {
        String data = "IN:  " + evt.getData().trim() + "\n";
        textLog.append(data);
    }

    @Override
    public void onSendEventOccurred(OnSendEvent evt) {
        String data = "OUT: " + evt.getData().trim() + "\n";
        textLog.append(data);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        textLog = new javax.swing.JTextArea();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        listEditorChannels = new mewkbot.ListEditor();
        jPanel4 = new javax.swing.JPanel();
        listEditorTriggers = new mewkbot.ListEditor();
        jPanel5 = new javax.swing.JPanel();
        listEditorAdmins = new mewkbot.ListEditor();
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
        jScrollPane1.setViewportView(textLog);

        jTabbedPane1.addTab("Log", jScrollPane1);

        jLabel1.setText("Host:");

        jLabel2.setText("Port:");

        jLabel3.setText("Password:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(220, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Server", jPanel1);

        jLabel4.setText("Nickname:");

        jLabel5.setText("Username:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(251, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("User", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listEditorChannels, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listEditorChannels, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Channels", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listEditorTriggers, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listEditorTriggers, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Triggers", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listEditorAdmins, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listEditorAdmins, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Admins", jPanel5);

        jTabbedPane1.addTab("Config", jTabbedPane2);

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
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
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
    
    this.bot.addOnLogEventListener(this);
    this.bot.addOnReceiveEventListener(this);
    this.bot.addOnSendEventListener(this);
    this.bot.addOnStartEventListener(this);
    this.bot.addOnStopEventListener(this);
}//GEN-LAST:event_formWindowOpened

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonStop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JLabel labelStatusIcon;
    private mewkbot.ListEditor listEditorAdmins;
    private mewkbot.ListEditor listEditorChannels;
    private mewkbot.ListEditor listEditorTriggers;
    private javax.swing.JTextArea textLog;
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