/*
 * NewMotorbacView.java
 */
package newmotorbac;

import newmotorbac.util.ExampleFileFilter;
import newmotorbac.dialog.jDialogCreatePolicy;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import orbac.COrbacCore;
import orbac.OrbacPolicyFactory;
import orbac.exception.COrbacException;
import org.jdesktop.application.Application.ExitListener;

/**
 * The application's main frame.
 */
public class NewMotorbacView extends FrameView {

    // orbac related members
    // the motorbac core
    private COrbacCore core;
    // path to the application directory
    private String workingDirectory = "";

    public NewMotorbacView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // change the exit event listener so that the default behaviour is changed
        app.addExitListener(new ExitListener() {

            @Override
            public boolean canExit(EventObject e) {
                return Exit();
            }

            @Override
            public void willExit(EventObject e) {
            }
        });

        System.out.println("Starting MotOrBAC 2");

        // get working directory
        File file = new File(".");
        String absPath = "";
        try {
            absPath = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        workingDirectory = absPath;
        System.out.println("MotOrBAC 2 directory:" + workingDirectory);
        // remove dot at the end of the path
        absPath += File.separatorChar + "plugins";
        // check if the plugin directory exists, if not we create it
        File pluginDir = new File(absPath);
        if (pluginDir.exists() == false) {
            // create directory
            System.out.println("\tplug-in directory does not exit, creating it");
            pluginDir.mkdir();
        }
        // initialize OrBAC core
        core = COrbacCore.GetTheInstance(absPath);
        
        // set application icon
        URL imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/orbee.png");

        if (imgURL != null) {
            ImageIcon img = new ImageIcon(imgURL);
            this.getFrame().setIconImage(img.getImage());
        }

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        // disable controls related to policy state since no policy exist yet
        SetPolicyStateReleatedButtonsStates(false);

        System.out.println("MotOrBAC 2 initialized");
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
            aboutBox = new NewMotorbacAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        NewMotorbacApp.getApplication().show(aboutBox);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jTabbedPanePolicies = new javax.swing.JTabbedPane();
        jToolBarControls = new javax.swing.JToolBar();
        jButtonNewPolicy = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonLoad = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAs = new javax.swing.JButton();
        jButtonUndo = new javax.swing.JButton();
        jButtonRedo = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButtonEnableAdorbac = new javax.swing.JButton();
        jButtonChangeAdorbacUser = new javax.swing.JButton();
        jButtonChangeAdorbacUserPassword = new javax.swing.JButton();
        jButtonDelegateRole = new javax.swing.JButton();
        jButtonDelegateRule = new javax.swing.JButton();
        jButtonAdorbacView = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItemNew = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItemProperties = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem jMenuItemExit = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemUndo = new javax.swing.JMenuItem();
        jMenuItemRedo = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemPlugins = new javax.swing.JMenuItem();
        jMenuItemClass = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        jMenuItemOnlineManual = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMenuItemAbout = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabelAdorbacUser = new javax.swing.JLabel();
        jLabelConflicts = new javax.swing.JLabel();

        mainPanel.setMinimumSize(new java.awt.Dimension(700, 400));
        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(NewMotorbacView.class);
        jTabbedPanePolicies.setToolTipText(resourceMap.getString("jTabbedPanePolicies.toolTipText")); // NOI18N
        jTabbedPanePolicies.setName("jTabbedPanePolicies"); // NOI18N
        jTabbedPanePolicies.setPreferredSize(new java.awt.Dimension(700, 500));
        jTabbedPanePolicies.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPanePoliciesStateChanged(evt);
            }
        });

        jToolBarControls.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBarControls.setRollover(true);
        jToolBarControls.setName("jToolBarControls"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getActionMap(NewMotorbacView.class, this);
        jButtonNewPolicy.setAction(actionMap.get("createPolicy")); // NOI18N
        jButtonNewPolicy.setIcon(resourceMap.getIcon("jButtonNewPolicy.icon")); // NOI18N
        jButtonNewPolicy.setText(resourceMap.getString("jButtonNewPolicy.text")); // NOI18N
        jButtonNewPolicy.setToolTipText(resourceMap.getString("jButtonNewPolicy.toolTipText")); // NOI18N
        jButtonNewPolicy.setFocusable(false);
        jButtonNewPolicy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNewPolicy.setName("jButtonNewPolicy"); // NOI18N
        jButtonNewPolicy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarControls.add(jButtonNewPolicy);

        jButtonClose.setAction(actionMap.get("ClosePolicy")); // NOI18N
        jButtonClose.setIcon(resourceMap.getIcon("jButtonClose.icon")); // NOI18N
        jButtonClose.setText(resourceMap.getString("jButtonClose.text")); // NOI18N
        jButtonClose.setToolTipText(resourceMap.getString("jButtonClose.toolTipText")); // NOI18N
        jButtonClose.setFocusable(false);
        jButtonClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClose.setName("jButtonClose"); // NOI18N
        jButtonClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarControls.add(jButtonClose);

        jButtonLoad.setAction(actionMap.get("openPolicy")); // NOI18N
        jButtonLoad.setIcon(resourceMap.getIcon("jButtonLoad.icon")); // NOI18N
        jButtonLoad.setText(resourceMap.getString("jButtonLoad.text")); // NOI18N
        jButtonLoad.setToolTipText(resourceMap.getString("jButtonLoad.toolTipText")); // NOI18N
        jButtonLoad.setFocusable(false);
        jButtonLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLoad.setName("jButtonLoad"); // NOI18N
        jButtonLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarControls.add(jButtonLoad);

        jButtonSave.setAction(actionMap.get("SavePolicy")); // NOI18N
        jButtonSave.setIcon(resourceMap.getIcon("jButtonSave.icon")); // NOI18N
        jButtonSave.setToolTipText(resourceMap.getString("jButtonSave.toolTipText")); // NOI18N
        jButtonSave.setFocusable(false);
        jButtonSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSave.setName("jButtonSave"); // NOI18N
        jButtonSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarControls.add(jButtonSave);

        jButtonSaveAs.setAction(actionMap.get("SavePolicyAs")); // NOI18N
        jButtonSaveAs.setIcon(resourceMap.getIcon("jButtonSaveAs.icon")); // NOI18N
        jButtonSaveAs.setToolTipText(resourceMap.getString("jButtonSaveAs.toolTipText")); // NOI18N
        jButtonSaveAs.setFocusable(false);
        jButtonSaveAs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSaveAs.setName("jButtonSaveAs"); // NOI18N
        jButtonSaveAs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarControls.add(jButtonSaveAs);

        jButtonUndo.setAction(actionMap.get("Undo")); // NOI18N
        jButtonUndo.setIcon(resourceMap.getIcon("jButtonUndo.icon")); // NOI18N
        jButtonUndo.setToolTipText(resourceMap.getString("jButtonUndo.toolTipText")); // NOI18N
        jButtonUndo.setFocusable(false);
        jButtonUndo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonUndo.setName("jButtonUndo"); // NOI18N
        jButtonUndo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarControls.add(jButtonUndo);

        jButtonRedo.setAction(actionMap.get("Redo")); // NOI18N
        jButtonRedo.setIcon(resourceMap.getIcon("jButtonRedo.icon")); // NOI18N
        jButtonRedo.setToolTipText(resourceMap.getString("jButtonRedo.toolTipText")); // NOI18N
        jButtonRedo.setFocusable(false);
        jButtonRedo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRedo.setName("jButtonRedo"); // NOI18N
        jButtonRedo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarControls.add(jButtonRedo);

        jButtonRefresh.setIcon(resourceMap.getIcon("jButtonRefresh.icon")); // NOI18N
        jButtonRefresh.setToolTipText(resourceMap.getString("jButtonRefresh.toolTipText")); // NOI18N
        jButtonRefresh.setEnabled(false);
        jButtonRefresh.setFocusable(false);
        jButtonRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRefresh.setName("jButtonRefresh"); // NOI18N
        jButtonRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });
        jToolBarControls.add(jButtonRefresh);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBarControls.add(jSeparator5);

        jButtonEnableAdorbac.setIcon(resourceMap.getIcon("jButtonEnableAdorbac.icon")); // NOI18N
        jButtonEnableAdorbac.setToolTipText(resourceMap.getString("jButtonEnableAdorbac.toolTipText")); // NOI18N
        jButtonEnableAdorbac.setEnabled(false);
        jButtonEnableAdorbac.setFocusable(false);
        jButtonEnableAdorbac.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEnableAdorbac.setName("jButtonEnableAdorbac"); // NOI18N
        jButtonEnableAdorbac.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEnableAdorbac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnableAdorbacActionPerformed(evt);
            }
        });
        jToolBarControls.add(jButtonEnableAdorbac);

        jButtonChangeAdorbacUser.setIcon(resourceMap.getIcon("jButtonChangeAdorbacUser.icon")); // NOI18N
        jButtonChangeAdorbacUser.setToolTipText(resourceMap.getString("jButtonChangeAdorbacUser.toolTipText")); // NOI18N
        jButtonChangeAdorbacUser.setEnabled(false);
        jButtonChangeAdorbacUser.setFocusable(false);
        jButtonChangeAdorbacUser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonChangeAdorbacUser.setName("jButtonChangeAdorbacUser"); // NOI18N
        jButtonChangeAdorbacUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarControls.add(jButtonChangeAdorbacUser);

        jButtonChangeAdorbacUserPassword.setIcon(resourceMap.getIcon("jButtonChangeAdorbacUserPassword.icon")); // NOI18N
        jButtonChangeAdorbacUserPassword.setToolTipText(resourceMap.getString("jButtonChangeAdorbacUserPassword.toolTipText")); // NOI18N
        jButtonChangeAdorbacUserPassword.setEnabled(false);
        jButtonChangeAdorbacUserPassword.setFocusable(false);
        jButtonChangeAdorbacUserPassword.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonChangeAdorbacUserPassword.setName("jButtonChangeAdorbacUserPassword"); // NOI18N
        jButtonChangeAdorbacUserPassword.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarControls.add(jButtonChangeAdorbacUserPassword);

        jButtonDelegateRole.setIcon(resourceMap.getIcon("jButtonDelegateRole.icon")); // NOI18N
        jButtonDelegateRole.setToolTipText(resourceMap.getString("jButtonDelegateRole.toolTipText")); // NOI18N
        jButtonDelegateRole.setEnabled(false);
        jButtonDelegateRole.setFocusable(false);
        jButtonDelegateRole.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDelegateRole.setName("jButtonDelegateRole"); // NOI18N
        jButtonDelegateRole.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDelegateRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelegateRoleActionPerformed(evt);
            }
        });
        jToolBarControls.add(jButtonDelegateRole);

        jButtonDelegateRule.setIcon(resourceMap.getIcon("jButtonDelegateRule.icon")); // NOI18N
        jButtonDelegateRule.setToolTipText(resourceMap.getString("jButtonDelegateRule.toolTipText")); // NOI18N
        jButtonDelegateRule.setEnabled(false);
        jButtonDelegateRule.setFocusable(false);
        jButtonDelegateRule.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDelegateRule.setName("jButtonDelegateRule"); // NOI18N
        jButtonDelegateRule.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDelegateRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelegateRuleActionPerformed(evt);
            }
        });
        jToolBarControls.add(jButtonDelegateRule);

        jButtonAdorbacView.setIcon(resourceMap.getIcon("jButtonAdorbacView.icon")); // NOI18N
        jButtonAdorbacView.setToolTipText(resourceMap.getString("jButtonAdorbacView.toolTipText")); // NOI18N
        jButtonAdorbacView.setEnabled(false);
        jButtonAdorbacView.setFocusable(false);
        jButtonAdorbacView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAdorbacView.setName("jButtonAdorbacView"); // NOI18N
        jButtonAdorbacView.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAdorbacView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdorbacViewActionPerformed(evt);
            }
        });
        jToolBarControls.add(jButtonAdorbacView);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBarControls, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPanePolicies, javax.swing.GroupLayout.DEFAULT_SIZE, 957, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBarControls, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
            .addComponent(jTabbedPanePolicies, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setAction(actionMap.get("SavePolicyAs")); // NOI18N
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setToolTipText(resourceMap.getString("fileMenu.toolTipText")); // NOI18N

        jMenuItemNew.setAction(actionMap.get("createPolicy")); // NOI18N
        jMenuItemNew.setText(resourceMap.getString("jMenuItemNew.text")); // NOI18N
        jMenuItemNew.setToolTipText(resourceMap.getString("jMenuItemNew.toolTipText")); // NOI18N
        jMenuItemNew.setName("jMenuItemNew"); // NOI18N
        fileMenu.add(jMenuItemNew);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        jMenuItemClose.setAction(actionMap.get("ClosePolicy")); // NOI18N
        jMenuItemClose.setText(resourceMap.getString("jMenuItemClose.text")); // NOI18N
        jMenuItemClose.setToolTipText(resourceMap.getString("jMenuItemClose.toolTipText")); // NOI18N
        fileMenu.add(jMenuItemClose);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        jMenuItemOpen.setAction(actionMap.get("openPolicy")); // NOI18N
        jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpen.setText(resourceMap.getString("jMenuItemOpen.text")); // NOI18N
        jMenuItemOpen.setToolTipText(resourceMap.getString("jMenuItemOpen.toolTipText")); // NOI18N
        jMenuItemOpen.setName("jMenuItemOpen"); // NOI18N
        fileMenu.add(jMenuItemOpen);

        jMenuItemSave.setAction(actionMap.get("SavePolicy")); // NOI18N
        jMenuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSave.setText(resourceMap.getString("jMenuItemSave.text")); // NOI18N
        jMenuItemSave.setToolTipText(resourceMap.getString("jMenuItemSave.toolTipText")); // NOI18N
        fileMenu.add(jMenuItemSave);

        jMenuItemSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSaveAs.setText(resourceMap.getString("jMenuItemSaveAs.text")); // NOI18N
        jMenuItemSaveAs.setToolTipText(resourceMap.getString("jMenuItemSaveAs.toolTipText")); // NOI18N
        jMenuItemSaveAs.setName("jMenuItemSaveAs"); // NOI18N
        fileMenu.add(jMenuItemSaveAs);

        jSeparator4.setName("jSeparator4"); // NOI18N
        fileMenu.add(jSeparator4);

        jMenuItemProperties.setText(resourceMap.getString("jMenuItemProperties.text")); // NOI18N
        jMenuItemProperties.setToolTipText(resourceMap.getString("jMenuItemProperties.toolTipText")); // NOI18N
        jMenuItemProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPropertiesActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItemProperties);

        jSeparator3.setName("jSeparator3"); // NOI18N
        fileMenu.add(jSeparator3);

        jMenuItemExit.setAction(actionMap.get("quit")); // NOI18N
        jMenuItemExit.setText(resourceMap.getString("jMenuItemExit.text")); // NOI18N
        jMenuItemExit.setName("jMenuItemExit"); // NOI18N
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItemExit);

        menuBar.add(fileMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N

        jMenuItemUndo.setAction(actionMap.get("Undo")); // NOI18N
        jMenuItemUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemUndo.setText(resourceMap.getString("jMenuItemUndo.text")); // NOI18N
        jMenuItemUndo.setName("jMenuItemUndo"); // NOI18N
        jMenu1.add(jMenuItemUndo);

        jMenuItemRedo.setAction(actionMap.get("Redo")); // NOI18N
        jMenuItemRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRedo.setText(resourceMap.getString("jMenuItemRedo.text")); // NOI18N
        jMenu1.add(jMenuItemRedo);

        menuBar.add(jMenu1);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N

        jMenuItemPlugins.setAction(actionMap.get("ShowPluginDialog")); // NOI18N
        jMenuItemPlugins.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemPlugins.setText(resourceMap.getString("jMenuItemPlugins.text")); // NOI18N
        jMenu2.add(jMenuItemPlugins);

        jMenuItemClass.setAction(actionMap.get("ShowClassEditor")); // NOI18N
        jMenuItemClass.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClass.setText(resourceMap.getString("jMenuItemClass.text")); // NOI18N
        jMenuItemClass.setName("jMenuItemClass"); // NOI18N
        jMenu2.add(jMenuItemClass);

        menuBar.add(jMenu2);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N

        jMenuItemOnlineManual.setText(resourceMap.getString("jMenuItemOnlineManual.text")); // NOI18N
        jMenuItemOnlineManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOnlineManualActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItemOnlineManual);

        jMenuItemAbout.setAction(actionMap.get("showAboutBox")); // NOI18N
        jMenuItemAbout.setName("jMenuItemAbout"); // NOI18N
        helpMenu.add(jMenuItemAbout);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        jLabelAdorbacUser.setText(resourceMap.getString("jLabelAdorbacUser.text")); // NOI18N
        jLabelAdorbacUser.setName("jLabelAdorbacUser"); // NOI18N

        jLabelConflicts.setText(resourceMap.getString("jLabelConflicts.text")); // NOI18N
        jLabelConflicts.setName("jLabelConflicts"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusMessageLabel)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addComponent(jLabelAdorbacUser)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelConflicts, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusAnimationLabel)))
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusMessageLabel)
                            .addComponent(statusAnimationLabel)
                            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelAdorbacUser)
                        .addComponent(jLabelConflicts, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAdorbacViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdorbacViewActionPerformed
        // toggle adorbac display state for currently selected policy
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.ToggleAdorbacDisplay();
        RefreshToolbar(selectedPolicyTab);
        RefreshMenuItems(selectedPolicyTab);
    }//GEN-LAST:event_jButtonAdorbacViewActionPerformed

    private void jTabbedPanePoliciesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPanePoliciesStateChanged
        // refresh toolbar according to selected policy state
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        if (selectedPolicyTab == null) {
            // no policy, put the GUI in the same state than after startup
            SetPolicyStateReleatedButtonsStates(false);
        } else {
            RefreshToolbar(selectedPolicyTab);
            RefreshMenuItems(selectedPolicyTab);
        }
    }//GEN-LAST:event_jTabbedPanePoliciesStateChanged

    private void jButtonEnableAdorbacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnableAdorbacActionPerformed
        // toggle adorbac state for currently selected policy
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.ToggleAdorbac();
        RefreshToolbar(selectedPolicyTab);
    }//GEN-LAST:event_jButtonEnableAdorbacActionPerformed

    private void jMenuItemPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPropertiesActionPerformed
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.DisplayInformation();
    }//GEN-LAST:event_jMenuItemPropertiesActionPerformed

    private void jButtonDelegateRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelegateRoleActionPerformed
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.RoleDelegation();
    }//GEN-LAST:event_jButtonDelegateRoleActionPerformed

    private void jButtonDelegateRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelegateRuleActionPerformed
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.RuleDelegation();
    }//GEN-LAST:event_jButtonDelegateRuleActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.UpdatePolicyDisplay();
        RefreshToolbar(selectedPolicyTab);
        RefreshMenuItems(selectedPolicyTab);
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jMenuItemOnlineManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOnlineManualActionPerformed
        if (!java.awt.Desktop.isDesktopSupported()) {
            System.err.println("Desktop is not supported, cannot open online manual");
            System.err.println("please try to update your java environment to 1.6 or higher");
            JOptionPane.showMessageDialog(getFrame(), "Desktop is not supported, cannot open online manual. "
                    + "please try to update your java environment to 1.6 or higher", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            System.err.println("Desktop doesn't support the browse action");
            JOptionPane.showMessageDialog(getFrame(), "Desktop doesn't support the browse action", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            java.net.URI uri = new java.net.URI("http://motorbac.sourceforge.net/user_manual/index.html");
            desktop.browse(uri);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItemOnlineManualActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private boolean SavePolicy(PanelPolicy policyPanel) {
        boolean result = false;
        if (policyPanel.GetPolicyPath() == null) {
            result = SavePolicyAs(policyPanel);
        } else {
            result = policyPanel.SavePolicy();
        }
        // refresh undo/redo and save controls
        RefreshMenuItems(policyPanel);
        RefreshToolbar(policyPanel);
        return result;
    }

    private boolean SavePolicyAs(PanelPolicy policyPanel) {
        try {
            // create the file chooser and set the title
            JFileChooser chooser = new JFileChooser(workingDirectory);
            chooser.setDialogTitle("Save policy");
            // set the file extension type that will be displayed according to the policy type
            OrbacPolicyFactory f = core.GetFactory(policyPanel.GetPolicy().getClass());

            chooser.addChoosableFileFilter(new ExampleFileFilter(f.GetFileExtension(), f.GetPolicyType() + " policy files"));

            // get the result of the file chooser
            if (chooser.showSaveDialog(getFrame()) == JFileChooser.CANCEL_OPTION) {
                return false;
            }
            // get the selected file
            File file = chooser.getSelectedFile();

            // save policy
            policyPanel.SavePolicy(file.getAbsolutePath());
            // refresh undo/redo and save controls
            RefreshMenuItems(policyPanel);
            RefreshToolbar(policyPanel);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return true;
    }

    // set menu items state according to given policy tab
    public void RefreshMenuItems(PanelPolicy selectedPolicyTab) {
        jMenuItemClass.setEnabled(true);
        jMenuItemClose.setEnabled(true);
        jMenuItemPlugins.setEnabled(true);
        jMenuItemProperties.setEnabled(true);
        jMenuItemRedo.setEnabled(selectedPolicyTab.CanRedo());
        jMenuItemSave.setEnabled(selectedPolicyTab.CanBeSaved());
        jMenuItemSaveAs.setEnabled(true);
        jMenuItemUndo.setEnabled(selectedPolicyTab.CanUndo());
    }
    // set toolbar buttons state according to given policy tab

    public void RefreshToolbar(PanelPolicy selectedPolicyTab) {
        // hide/show adorbac control depending on policy implementation
        jButtonAdorbacView.setVisible(selectedPolicyTab.GetPolicy().IsAdorbacImplemented());
        jButtonChangeAdorbacUser.setVisible(selectedPolicyTab.GetPolicy().IsAdorbacImplemented());
        jButtonChangeAdorbacUserPassword.setVisible(selectedPolicyTab.GetPolicy().IsAdorbacImplemented());
        jButtonDelegateRole.setVisible(selectedPolicyTab.GetPolicy().IsAdorbacImplemented());
        jButtonDelegateRule.setVisible(selectedPolicyTab.GetPolicy().IsAdorbacImplemented());
        jButtonEnableAdorbac.setVisible(selectedPolicyTab.GetPolicy().IsAdorbacImplemented());
        jSeparator5.setVisible(selectedPolicyTab.GetPolicy().IsAdorbacImplemented());

        // set adorbac button icon
        URL imgURL = null;
        if (selectedPolicyTab.GetAdorbacState()) {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/adorbac_enabled.png");
        } else {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/adorbac_disabled.png");
        }
        if (imgURL != null) {
            ImageIcon img = new ImageIcon(imgURL);
            jButtonEnableAdorbac.setIcon(img);
        }
        // set other buttons state
        jButtonChangeAdorbacUser.setEnabled(selectedPolicyTab.GetAdorbacState());
        jButtonChangeAdorbacUserPassword.setEnabled(selectedPolicyTab.GetAdorbacState());
        jButtonDelegateRole.setEnabled(selectedPolicyTab.GetAdorbacState());
        jButtonDelegateRule.setEnabled(selectedPolicyTab.GetAdorbacState());
        // buttons in the toolbar
        jButtonRedo.setEnabled(selectedPolicyTab.CanRedo());
        jButtonSave.setEnabled(selectedPolicyTab.CanBeSaved());
        jButtonSaveAs.setEnabled(true);
        jButtonClose.setEnabled(true);
        jButtonUndo.setEnabled(selectedPolicyTab.CanUndo());
        jButtonRefresh.setEnabled(true);
        // entries in the menu
        jMenuItemClass.setEnabled(true);
        jMenuItemClose.setEnabled(true);
        jMenuItemPlugins.setEnabled(true);
        jMenuItemProperties.setEnabled(true);
        jMenuItemRedo.setEnabled(selectedPolicyTab.CanRedo());
        jMenuItemSave.setEnabled(selectedPolicyTab.CanBeSaved());
        jMenuItemSaveAs.setEnabled(true);
        jMenuItemUndo.setEnabled(selectedPolicyTab.CanUndo());
        // display current adorbac user if any
        if (selectedPolicyTab.GetAdorbacState()) {
            jLabelAdorbacUser.setText("Current AdOrBAC user: " + selectedPolicyTab.GetAdorbacUser());
        } else {
            jLabelAdorbacUser.setText("No current AdOrBAC user: AdOrBAC policy disabled");
        }

        // set adorbac display button icon
        if (selectedPolicyTab.GetAdorbacDisplayState()) {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/adorbac_view_enabled.png");
        } else {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/adorbac_view_disabled.png");
        }
        if (imgURL != null) {
            ImageIcon img = new ImageIcon(imgURL);
            jButtonAdorbacView.setIcon(img);
        }

        // display conflicts
        int abstractConflictNumber = selectedPolicyTab.GetAbstractConflictsNumber();
        int concreteConflictNumber = selectedPolicyTab.GetConcreteConflictsNumber();

        String infoText = "";
        if (abstractConflictNumber < 0) {
            infoText += "abstract conflicts: NA";
        } else {
            infoText += abstractConflictNumber + " abstract conflict" + ((abstractConflictNumber > 1) ? "s" : "");
        }
        if (concreteConflictNumber < 0) {
            infoText += " | concrete conflicts: NA";
        } else {
            infoText += " | " + concreteConflictNumber + " concrete conflict" + ((concreteConflictNumber > 1) ? "s" : "");
        }
        // add number of abstract and concrete rules
        infoText += " | " + selectedPolicyTab.GetConcretePermissionNumber() + " cpe, " + selectedPolicyTab.GetAbstractPermissionNumber() + " ape";
        infoText += " | " + selectedPolicyTab.GetConcreteProhibitionNumber() + " cpr, " + selectedPolicyTab.GetAbstractProhibitionNumber() + " apr";
        infoText += " | " + selectedPolicyTab.GetConcreteObligationNumber() + " co, " + selectedPolicyTab.GetAbstractObligationNumber() + "ao";
        infoText += " | " + selectedPolicyTab.GetConcreteAdorbacPermissionNumber() + " acp, " + selectedPolicyTab.GetAbstractAdorbacPermissionNumber() + "aap";
        jLabelConflicts.setText(infoText);

        if (concreteConflictNumber > 0) {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/concrete_conflict.png");
            jLabelConflicts.setIcon(new ImageIcon(imgURL));
        } else if (abstractConflictNumber > 0) {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/abstract_conflict.png");
            jLabelConflicts.setIcon(new ImageIcon(imgURL));
        } else {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/no_conflicts.png");
            jLabelConflicts.setIcon(new ImageIcon(imgURL));
        }


        // refresh tabs depending on the policy state. If a policy needs to be saved, display a little icon
        imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/document-save_small.png");
        for (int i = 0; i < jTabbedPanePolicies.getTabCount(); i++) {
            PanelPolicy p = (PanelPolicy) jTabbedPanePolicies.getComponentAt(i);
            if (p == null) {
                continue;
            }

            if (p.CanBeSaved()) {
                jTabbedPanePolicies.setIconAt(i, new ImageIcon(imgURL));
            } else {
                jTabbedPanePolicies.setIconAt(i, null);
            }
        }
    }

    @Action
    public void createPolicy() {
        // display modal dialog box
        JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
        jDialogCreatePolicy newPolicyDialogBox = new jDialogCreatePolicy(mainFrame, true);
        newPolicyDialogBox.setLocationRelativeTo(mainFrame);
        NewMotorbacApp.getApplication().show(newPolicyDialogBox);
        if (newPolicyDialogBox.canceled) {
            return;
        }

        // now create policy, this will be done in the PanelPolicy class constructor
        // create a new policy panel and add it to the main tabbed pane
        try {
            PanelPolicy newPolicyPanel = new PanelPolicy(newPolicyDialogBox, this);
            jTabbedPanePolicies.addTab(newPolicyDialogBox.jTextFieldPolicyName.getText(), newPolicyPanel);
            jTabbedPanePolicies.setSelectedComponent(newPolicyPanel);
            // modify GUI state
            SetPolicyStateReleatedButtonsStates(true);
            RefreshToolbar(newPolicyPanel);
            RefreshMenuItems(newPolicyPanel);
        } catch (COrbacException e) {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(getFrame(), e, "Error while creating new policy", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Action
    public void openPolicy() {
        // create the file chooser and set the title
        JFileChooser chooser = new JFileChooser(workingDirectory);
        chooser.setDialogTitle("Load policy");

        // get the file extension types that will be displayed
        Set<String> extensions = core.GetSupportedPolicyFileExtensions();
        Set<String> types = core.GetSupportedPolicyTypes();
        String st = "Supported policy implementations: ";
        Iterator<String> it = types.iterator();
        while (it.hasNext()) {
            st += it.next();
            if (it.hasNext()) {
                st += ", ";
            }
        }
        String[] typesArray = (String[]) extensions.toArray(new String[extensions.size()]);
        chooser.addChoosableFileFilter(new ExampleFileFilter(typesArray, st));

        // get the result of the file chooser
        if (chooser.showOpenDialog(getFrame()) == JFileChooser.CANCEL_OPTION) {
            return;
        }
        // get the selected file
        File file = chooser.getSelectedFile();

        // now load policy, this will be done in the PanelPolicy class constructor
        // create a new policy panel and add it to the main tabbed pane
        try {
            PanelPolicy newPolicyPanel = new PanelPolicy(file, this);
            jTabbedPanePolicies.addTab(newPolicyPanel.GetPolicy().GetName(), newPolicyPanel);
            jTabbedPanePolicies.setSelectedComponent(newPolicyPanel);
            // modify GUI state
            SetPolicyStateReleatedButtonsStates(true);
            RefreshToolbar(newPolicyPanel);
            RefreshMenuItems(newPolicyPanel);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(getFrame(), e, "Error while creating new policy", JOptionPane.WARNING_MESSAGE);
        }
    }

    // disable/enable buttons related to policy state
    private void SetPolicyStateReleatedButtonsStates(boolean state) {
        // buttons
        jButtonAdorbacView.setEnabled(state);
        jButtonChangeAdorbacUser.setEnabled(state);
        jButtonChangeAdorbacUserPassword.setEnabled(state);
        jButtonDelegateRole.setEnabled(state);
        jButtonDelegateRule.setEnabled(state);
        jButtonEnableAdorbac.setEnabled(state);
        jButtonRedo.setEnabled(state);
        jButtonSave.setEnabled(state);
        jButtonSaveAs.setEnabled(state);
        jButtonClose.setEnabled(state);
        jButtonUndo.setEnabled(state);
        jLabelAdorbacUser.setText("No loaded policy");
        // disable entries in the menu
        jMenuItemClass.setEnabled(state);
        jMenuItemClose.setEnabled(state);
        jMenuItemPlugins.setEnabled(state);
        jMenuItemProperties.setEnabled(state);
        jMenuItemRedo.setEnabled(state);
        jMenuItemSave.setEnabled(state);
        jMenuItemSaveAs.setEnabled(state);
        jMenuItemUndo.setEnabled(state);
    }

    @Action
    public void ShowPluginDialog() {
        // tell currently selected policy tab to display the plug-ins dialog box
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.ShowPluginDialog();
    }

    @Action
    public void ShowClassEditor() {
        // tell currently selected policy tab to display the plug-ins dialog box
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.ShowClassEditor();
    }

    @Action
    public void Undo() {
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.Undo();
        selectedPolicyTab.UpdatePolicyDisplay();
    }

    @Action
    public void Redo() {
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        selectedPolicyTab.Redo();
        selectedPolicyTab.UpdatePolicyDisplay();
    }

    @Action
    public void SavePolicy() {
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        SavePolicy(selectedPolicyTab);
    }

    @Action
    public void SavePolicyAs() {
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        SavePolicyAs(selectedPolicyTab);
    }

    @Action
    public void ClosePolicy() {
        // ask the user if he wants to save the policy before closing
        PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
        ClosePolicy(selectedPolicyTab);
    }

    // return true if the closing operation has been cancelled
    private boolean ClosePolicy(PanelPolicy selectedPolicyTab) {
        boolean policyClosingCancelled = false;
        // ask the user if he wants to save the policy before closing
        int tabIndex = jTabbedPanePolicies.indexOfComponent(selectedPolicyTab);

        if (selectedPolicyTab.CanBeSaved()) {
            String choices[] = {"Save and close", "Do not save and close", "Cancel"};
            int ret = JOptionPane.showOptionDialog(getFrame(),
                    "Policy " + selectedPolicyTab.GetPolicy().GetName() + " has been modified since last save. Do you want to save it?",
                    "Confirm modified policy closing",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    choices,
                    choices[0]);// default button

            switch (ret) {
                case 0: {
                    // will ask for a file path if no file is yet associated
                    SavePolicy(selectedPolicyTab);
                    selectedPolicyTab.ClosePolicy();
                    // remove policy tab
                    jTabbedPanePolicies.remove(selectedPolicyTab);
                }
                case 1: {
                    // close policy
                    selectedPolicyTab.ClosePolicy();
                    // remove policy tab
                    jTabbedPanePolicies.remove(selectedPolicyTab);
                }
                break;
                case 2:
                    // nothing to do
                    policyClosingCancelled = true;
                    break;
            }
        } else {
            // the policy has already been saved or is empty, just close it
            String choices[] = {"OK", "Cancel"};
            int ret = JOptionPane.showOptionDialog(getFrame(),
                    "Are you sure you want to close policy " + selectedPolicyTab.GetPolicy().GetName() + " ?",
                    "Confirm policy closing",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    choices,
                    choices[0]);// default button
            switch (ret) {
                case 0:
                    selectedPolicyTab.ClosePolicy();
                    // remove policy tab
                    jTabbedPanePolicies.remove(selectedPolicyTab);
                    break;
                case 1:
                    // nothing to do
                    break;
            }
        }

        // we must now update the gui according to the number of remaining tabs
        if (jTabbedPanePolicies.getTabCount() == 0) {
            // no more open policies, put the GUI into the same state as just after starting
            SetPolicyStateReleatedButtonsStates(false);
        } else {
            // select the previous tab if there was any
            if (jTabbedPanePolicies.getTabCount() > 1) {
                if (tabIndex > 0) {
                    tabIndex--;
                    jTabbedPanePolicies.setSelectedIndex(tabIndex);
                } else if (tabIndex == 0) {
                    jTabbedPanePolicies.setSelectedIndex(0);
                }
            }
            // refresh gui
            selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getSelectedComponent();
            RefreshToolbar(selectedPolicyTab);
            RefreshMenuItems(selectedPolicyTab);
        }

        return policyClosingCancelled;
    }

    // called when the exit button or the exit menu item are clicked
    private boolean Exit() {
        // first check if some policies should be saved because they have been modified since last save
        for (int i = 0; i < jTabbedPanePolicies.getTabCount(); i++) {
            // get tab
            PanelPolicy selectedPolicyTab = (PanelPolicy) jTabbedPanePolicies.getComponentAt(i);
            if (selectedPolicyTab.CanBeSaved()) {
                if (ClosePolicy(selectedPolicyTab)) {
                    // the user cancelled the closing operation
                    return false;
                }
            }
        }
        return true;
    }

    @Action
    public void Quit() {
        // called by the menu
        Exit();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdorbacView;
    private javax.swing.JButton jButtonChangeAdorbacUser;
    private javax.swing.JButton jButtonChangeAdorbacUserPassword;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonDelegateRole;
    private javax.swing.JButton jButtonDelegateRule;
    private javax.swing.JButton jButtonEnableAdorbac;
    private javax.swing.JButton jButtonLoad;
    private javax.swing.JButton jButtonNewPolicy;
    private javax.swing.JButton jButtonRedo;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSaveAs;
    private javax.swing.JButton jButtonUndo;
    public javax.swing.JLabel jLabelAdorbacUser;
    private javax.swing.JLabel jLabelConflicts;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItemClass;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemNew;
    private javax.swing.JMenuItem jMenuItemOnlineManual;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemPlugins;
    private javax.swing.JMenuItem jMenuItemProperties;
    private javax.swing.JMenuItem jMenuItemRedo;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JMenuItem jMenuItemUndo;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPanePolicies;
    public javax.swing.JToolBar jToolBarControls;
    private javax.swing.JPanel mainPanel;
    public javax.swing.JMenuBar menuBar;
    public javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}