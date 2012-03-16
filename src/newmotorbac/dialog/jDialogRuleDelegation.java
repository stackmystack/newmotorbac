/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * jDialogRuleDelegation.java
 *
 * Created on 25 août 2011, 15:04:56
 */

package newmotorbac.dialog;

import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import newmotorbac.util.OrbacPolicyContext;
import newmotorbac.util.RuleDelegationParameters;
import newmotorbac.util.RuleDelegationResult;
import newmotorbac.util.RuleRevocationParameters;
import newmotorbac.util.RuleRevocationResult;
import orbac.context.CContext;
import orbac.exception.COrbacException;
import orbac.securityRules.CAbstractRule;

/**
 *
 * @author fabien
 */
public class jDialogRuleDelegation extends javax.swing.JDialog {
    // policy context
    OrbacPolicyContext thisContext;

    public boolean canceled = false;

    // delegation state variables
    // the rule table listing the rules for the current user
    protected String headersDelegation[] = {"delegate", "rule name", "organization", "role", "activity", "view", "context", "violation context"};
    // table data
    private Object tableDataDelegation[][] = null;
    // table renderer and model
    private MyTableCellRenderer ruleListRendererDelegation = new MyTableCellRenderer();
    private MyDelegationTableModel modelDelegation;
    // number of permissions, permissions are displayed first in the table
    private int numberOfPermissionsDelegation = 0;
    // rule delegation parameters cache
    private RuleDelegationParameters ruleDelegationCache[] = null;

    // revocaton state variables
    // the rule table listing the delegated rules for the current user
    protected String headersRevocation[] = {"revoke", "rule name", "organization", "delegatee", "activity", "view", "context", "violation context"};
    // table data
    private Object tableDataRevocation[][] = null;
    // table renderer and model
    private MyTableCellRenderer ruleListRendererRevocation = new MyTableCellRenderer();
    private MyRevocationTableModel modelRevocation;
    // number of permissions, permissions are displayed first in the table
    private int numberOfPermissionsRevocation = 0;
    // rule delegation parameters cache
    private RuleRevocationParameters ruleRevocationCache[] = null;

    /** Creates new form jDialogRuleDelegation */
    public jDialogRuleDelegation(java.awt.Frame parent, boolean modal, OrbacPolicyContext thisContext) {
        super(parent, modal);
        initComponents();

        this.thisContext = thisContext;

        // set title
        setTitle("Manage rule delegation");

        // setup delegation controls
        SetupDelegationTable();
        // setup revocation controls
        SetupRevocationTable();
    }

    private void SetupRevocationTable()
    {
        // build table data
        // get the abstract permissions for the current adorbac user
        int rulesTotalNumber = 0;
        try
        {
            // get delegated abstract permission AND delegated abstract obligations
            HashSet<CAbstractRule> permissions = thisContext.thePolicy.GetCurrentAdorbacUserDelegatedPermissions();
            HashSet<CAbstractRule> obligations = thisContext.thePolicy.GetCurrentAdorbacUserDelegatedObligations();
            tableDataRevocation = new Object[ permissions.size() + obligations.size() ][8];
            numberOfPermissionsRevocation = permissions.size();

            for ( CAbstractRule ar : permissions )
            {
                tableDataRevocation[rulesTotalNumber][0] = false;
                tableDataRevocation[rulesTotalNumber][1] = ar.GetName();
                tableDataRevocation[rulesTotalNumber][2] = ar.GetOrganization();
                tableDataRevocation[rulesTotalNumber][3] = ar.GetRole();
                tableDataRevocation[rulesTotalNumber][4] = ar.GetActivity();
                tableDataRevocation[rulesTotalNumber][5] = ar.GetView();
                tableDataRevocation[rulesTotalNumber][6] = ar.GetContext();
                tableDataRevocation[rulesTotalNumber][7] = "null";

                rulesTotalNumber++;
            }

            for ( CAbstractRule ar : obligations )
            {
                tableDataRevocation[rulesTotalNumber][0] = false;
                tableDataRevocation[rulesTotalNumber][1] = ar.GetName();
                tableDataRevocation[rulesTotalNumber][2] = ar.GetOrganization();
                tableDataRevocation[rulesTotalNumber][3] = ar.GetRole();
                tableDataRevocation[rulesTotalNumber][4] = ar.GetActivity();
                tableDataRevocation[rulesTotalNumber][5] = ar.GetView();
                tableDataRevocation[rulesTotalNumber][6] = ar.GetContext();
                tableDataRevocation[rulesTotalNumber][7] = ar.GetViolationContext();

                rulesTotalNumber++;
            }
        }
        catch (COrbacException e)
        {
            System.out.println("Error building the current adorbac user set of abstract rules");
            e.printStackTrace();
        }

        // set up state cache to record delegation parameters for each rule
        ruleRevocationCache = new RuleRevocationParameters[rulesTotalNumber];
        // for each rule setup the parameter cache
        for ( int i = 0; i < rulesTotalNumber; i++ )
        {
            ruleRevocationCache[i] = new RuleRevocationParameters();
            ruleRevocationCache[i].obligation = (i > numberOfPermissionsRevocation);// check if processing an obligation
        }

        // setup renderer
	jTableRevocation.setDefaultRenderer(Object.class, ruleListRendererRevocation);
        // setup model
	modelRevocation = new MyRevocationTableModel(tableDataRevocation, headersRevocation);
        jTableRevocation.setModel(modelRevocation);
    }

    // return all the chosen permissions to delegate and the associated parameters
    public Vector<RuleRevocationResult> GetRevokedRules()
    {
        Vector<RuleRevocationResult> rules = new Vector<RuleRevocationResult>();
        for ( int i = 0; i < ruleRevocationCache.length; i++ )
        {
            if ( (Boolean)jTableRevocation.getModel().getValueAt(i, 0) == false ) continue;
            RuleRevocationResult rr = new RuleRevocationResult();
            rr.ruleName = (String)tableDataRevocation[i][1];
            rr.cascade = ruleRevocationCache[i].cascade;
            rr.strong = ruleRevocationCache[i].strong;
            rr.obligation = ruleRevocationCache[i].obligation;

            rules.add(rr);
        }
        return rules;
    }
    
    private void SetupDelegationTable()
    {
        // build table data
        // get the abstract permissions for the current adorbac user
        int rulesTotalNumber = 0;
        try
        {
            // get abstract permission AND adorbac abstract permissions
            HashSet<CAbstractRule> permissions = thisContext.thePolicy.GetCurrentAdorbacUserAbstractPermissions();
            HashSet<CAbstractRule> obligations = thisContext.thePolicy.GetCurrentAdorbacUserAbstractObligations();
            tableDataDelegation = new Object[ permissions.size() + obligations.size() ][8];
            numberOfPermissionsDelegation = permissions.size();

            for ( CAbstractRule ar : permissions )
            {
                tableDataDelegation[rulesTotalNumber][0] = false;
                tableDataDelegation[rulesTotalNumber][1] = ar.GetName();
                tableDataDelegation[rulesTotalNumber][2] = ar.GetOrganization();
                tableDataDelegation[rulesTotalNumber][3] = ar.GetRole();
                tableDataDelegation[rulesTotalNumber][4] = ar.GetActivity();
                tableDataDelegation[rulesTotalNumber][5] = ar.GetView();
                tableDataDelegation[rulesTotalNumber][6] = ar.GetContext();
                tableDataDelegation[rulesTotalNumber][7] = "null";

                rulesTotalNumber++;
            }

            for ( CAbstractRule ar : obligations )
            {
                tableDataDelegation[rulesTotalNumber][0] = false;
                tableDataDelegation[rulesTotalNumber][1] = ar.GetName();
                tableDataDelegation[rulesTotalNumber][2] = ar.GetOrganization();
                tableDataDelegation[rulesTotalNumber][3] = ar.GetRole();
                tableDataDelegation[rulesTotalNumber][4] = ar.GetActivity();
                tableDataDelegation[rulesTotalNumber][5] = ar.GetView();
                tableDataDelegation[rulesTotalNumber][6] = ar.GetContext();
                tableDataDelegation[rulesTotalNumber][7] = ar.GetViolationContext();

                rulesTotalNumber++;
            }

            // fill context list
            Map<String, CContext> ctxs = thisContext.thePolicy.GetContexts();
            Set< Entry<String, CContext> > ctxSet = ctxs.entrySet();
            Iterator< Entry<String, CContext> > ies = ctxSet.iterator();
            while ( ies.hasNext() )
            {
                Entry<String, CContext> e = ies.next();
                jComboBoxContext.addItem(e.getValue().GetName());
            }
            // fill grantee list
            Set<String> subjects = thisContext.thePolicy.GetSubjects();
            Iterator<String> is = subjects.iterator();
            while ( is.hasNext() )
            {
                String s = is.next();
                jComboBoxGrantee.addItem(s);
            }
        }
        catch (COrbacException e)
        {
            System.out.println("Error building the current adorbac user set of abstract rules");
            e.printStackTrace();
        }

        // set up state cache to record delegation parameters for each rule
        ruleDelegationCache = new RuleDelegationParameters[rulesTotalNumber];
        // for each rule setup the parameter cache
        for ( int i = 0; i < rulesTotalNumber; i++ )
        {
            ruleDelegationCache[i] = new RuleDelegationParameters();
            ruleDelegationCache[i].grantee = -1;
            ruleDelegationCache[i].transfert = false;
            ruleDelegationCache[i].grantOption = false;
            ruleDelegationCache[i].ctxOption = -1;
            ruleDelegationCache[i].steps = 1;
            ruleDelegationCache[i].obligation = (i > numberOfPermissionsDelegation);// check if processing an obligation
        }

        // setup renderer
	jTableDelegation.setDefaultRenderer(Object.class, ruleListRendererDelegation);
        // setup model
	modelDelegation = new MyDelegationTableModel(tableDataDelegation, headersDelegation);
        jTableDelegation.setModel(modelDelegation);
    }

    // return all the chosen permissions to delegate and the associated parameters
    public Vector<RuleDelegationResult> GetDelegatedRules()
    {
        Vector<RuleDelegationResult> rules = new Vector<RuleDelegationResult>();
        for ( int i = 0; i < ruleDelegationCache.length; i++ )
        {
            if ( (Boolean)jTableDelegation.getModel().getValueAt(i, 0) == false ) continue;
            RuleDelegationResult dr = new RuleDelegationResult();
            dr.ruleName = (String)tableDataDelegation[i][1];
            dr.grantee = (String)jComboBoxGrantee.getItemAt(ruleDelegationCache[i].grantee);
            dr.transfert = ruleDelegationCache[i].transfert;
            dr.grantOption = ruleDelegationCache[i].grantOption;
            dr.contextOption = (String)jComboBoxContext.getItemAt(ruleDelegationCache[i].ctxOption);
            dr.steps = ruleDelegationCache[i].steps;
            dr.obligation = ruleDelegationCache[i].obligation;

            rules.add(dr);
        }
        return rules;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelDelegation = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDelegation = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabelGrantee = new javax.swing.JLabel();
        jComboBoxGrantee = new javax.swing.JComboBox();
        jLabelGrantContext = new javax.swing.JLabel();
        jComboBoxContext = new javax.swing.JComboBox();
        jCheckBoxTransfert = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jCheckBoxWithGrant = new javax.swing.JCheckBox();
        jLabelSteps = new javax.swing.JLabel();
        jSpinnerSteps = new javax.swing.JSpinner();
        jPanelRevocation = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableRevocation = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxCascade = new javax.swing.JCheckBox();
        jCheckBoxStrong = new javax.swing.JCheckBox();
        jButtonCancel = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(640, 400));
        setName("Form"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanelDelegation.setName("jPanelDelegation"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableDelegation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableDelegation.setName("jTableDelegation"); // NOI18N
        jTableDelegation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDelegationMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableDelegation);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(jDialogRuleDelegation.class);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jLabelGrantee.setText(resourceMap.getString("jLabelGrantee.text")); // NOI18N
        jLabelGrantee.setEnabled(false);
        jLabelGrantee.setName("jLabelGrantee"); // NOI18N

        jComboBoxGrantee.setEnabled(false);
        jComboBoxGrantee.setName("jComboBoxGrantee"); // NOI18N

        jLabelGrantContext.setText(resourceMap.getString("jLabelGrantContext.text")); // NOI18N
        jLabelGrantContext.setEnabled(false);
        jLabelGrantContext.setName("jLabelGrantContext"); // NOI18N

        jComboBoxContext.setName("jComboBoxContext"); // NOI18N

        jCheckBoxTransfert.setText(resourceMap.getString("jCheckBoxTransfert.text")); // NOI18N
        jCheckBoxTransfert.setEnabled(false);
        jCheckBoxTransfert.setName("jCheckBoxTransfert"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        jCheckBoxWithGrant.setText(resourceMap.getString("jCheckBoxWithGrant.text")); // NOI18N
        jCheckBoxWithGrant.setEnabled(false);
        jCheckBoxWithGrant.setName("jCheckBoxWithGrant"); // NOI18N
        jCheckBoxWithGrant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxWithGrantActionPerformed(evt);
            }
        });

        jLabelSteps.setText(resourceMap.getString("jLabelSteps.text")); // NOI18N
        jLabelSteps.setEnabled(false);
        jLabelSteps.setName("jLabelSteps"); // NOI18N

        jSpinnerSteps.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinnerSteps.setEnabled(false);
        jSpinnerSteps.setName("jSpinnerSteps"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelGrantContext)
                            .addComponent(jLabelGrantee))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxGrantee, 0, 140, Short.MAX_VALUE)
                            .addComponent(jComboBoxContext, 0, 140, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBoxTransfert)
                        .addGap(153, 153, 153))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelSteps)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinnerSteps))
                            .addComponent(jCheckBoxWithGrant, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(419, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelGrantee)
                    .addComponent(jComboBoxGrantee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxTransfert))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelGrantContext)
                    .addComponent(jComboBoxContext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxWithGrant)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSteps)
                    .addComponent(jSpinnerSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelDelegationLayout = new javax.swing.GroupLayout(jPanelDelegation);
        jPanelDelegation.setLayout(jPanelDelegationLayout);
        jPanelDelegationLayout.setHorizontalGroup(
            jPanelDelegationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDelegationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDelegationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelDelegationLayout.setVerticalGroup(
            jPanelDelegationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDelegationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanelDelegation.TabConstraints.tabTitle"), jPanelDelegation); // NOI18N

        jPanelRevocation.setName("jPanelRevocation"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTableRevocation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableRevocation.setName("jTableRevocation"); // NOI18N
        jTableRevocation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRevocationMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableRevocation);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jCheckBoxCascade.setText(resourceMap.getString("jCheckBoxCascade.text")); // NOI18N
        jCheckBoxCascade.setEnabled(false);
        jCheckBoxCascade.setName("jCheckBoxCascade"); // NOI18N

        jCheckBoxStrong.setText(resourceMap.getString("jCheckBoxStrong.text")); // NOI18N
        jCheckBoxStrong.setEnabled(false);
        jCheckBoxStrong.setName("jCheckBoxStrong"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxCascade)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxStrong)
                .addContainerGap(240, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jCheckBoxCascade)
                .addComponent(jCheckBoxStrong))
        );

        javax.swing.GroupLayout jPanelRevocationLayout = new javax.swing.GroupLayout(jPanelRevocation);
        jPanelRevocation.setLayout(jPanelRevocationLayout);
        jPanelRevocationLayout.setHorizontalGroup(
            jPanelRevocationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRevocationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRevocationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelRevocationLayout.setVerticalGroup(
            jPanelRevocationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelRevocationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanelRevocation.TabConstraints.tabTitle"), jPanelRevocation); // NOI18N

        jButtonCancel.setText(resourceMap.getString("jButtonCancel.text")); // NOI18N
        jButtonCancel.setName("jButtonCancel"); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonOk.setText(resourceMap.getString("jButtonOk.text")); // NOI18N
        jButtonOk.setName("jButtonOk"); // NOI18N
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOk)
                    .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        canceled = false;
        
        // store currently selected rule parameters in the rule delegation cache
        ruleDelegationCache[lastRuleIndexDelegation].grantee = jComboBoxGrantee.getSelectedIndex();
        ruleDelegationCache[lastRuleIndexDelegation].transfert = jCheckBoxTransfert.isSelected();
        ruleDelegationCache[lastRuleIndexDelegation].grantOption = jCheckBoxWithGrant.isSelected();
        ruleDelegationCache[lastRuleIndexDelegation].ctxOption = jComboBoxContext.getSelectedIndex();
        ruleDelegationCache[lastRuleIndexDelegation].steps = (Integer)jSpinnerSteps.getValue();

        dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        canceled = true;
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jCheckBoxWithGrantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxWithGrantActionPerformed
        if ( jCheckBoxWithGrant.isSelected() )
        {
            jLabelSteps.setEnabled(true);
            jSpinnerSteps.setEnabled(true);
        }
        else
        {
            jLabelSteps.setEnabled(false);
            jSpinnerSteps.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBoxWithGrantActionPerformed

    private void jTableDelegationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDelegationMouseClicked
        // get delegation state for selected rule and update interface
        Boolean b = (Boolean)jTableDelegation.getValueAt(jTableDelegation.getSelectedRow(), 0);
        SetupDelegationControls(b, jTableDelegation.getSelectedRow());
    }//GEN-LAST:event_jTableDelegationMouseClicked

    private void jTableRevocationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRevocationMouseClicked
        // get delegation state for selected rule and update interface
        Boolean b = (Boolean)jTableRevocation.getValueAt(jTableRevocation.getSelectedRow(), 0);
        SetupRevocationControls(b, jTableRevocation.getSelectedRow());
    }//GEN-LAST:event_jTableRevocationMouseClicked

    public boolean HasBeenCancelled()
    {
        return canceled;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JCheckBox jCheckBoxCascade;
    private javax.swing.JCheckBox jCheckBoxStrong;
    private javax.swing.JCheckBox jCheckBoxTransfert;
    private javax.swing.JCheckBox jCheckBoxWithGrant;
    private javax.swing.JComboBox jComboBoxContext;
    private javax.swing.JComboBox jComboBoxGrantee;
    private javax.swing.JLabel jLabelGrantContext;
    private javax.swing.JLabel jLabelGrantee;
    private javax.swing.JLabel jLabelSteps;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelDelegation;
    private javax.swing.JPanel jPanelRevocation;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinnerSteps;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableDelegation;
    private javax.swing.JTable jTableRevocation;
    // End of variables declaration//GEN-END:variables


    // custom table class
    class MyDelegationTableModel extends DefaultTableModel
    {
    	// just to remove the warning
    	static final long serialVersionUID = 0;

        public MyDelegationTableModel(Object[][] data, String[] headers)
        {
            super(data, headers);
        }

		// make read-only except first column
        public boolean isCellEditable(int x, int y)
        {
            return (y > 0) ? false : true;
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        @SuppressWarnings("unchecked")
        public Class getColumnClass(int c)
        {
            return getValueAt(0, c).getClass();
        }

        public void setValueAt(Object value, int row, int col)
        {
            super.setValueAt(value, row, col);

            // if the value is set to true, we enable the delegation controls
            if ( value instanceof Boolean )
            {
                Boolean bVal = (Boolean)value;
                // update delegation controls according to selected row
                SetupDelegationControls(bVal, row);
            }
        }
    };

    class MyRevocationTableModel extends DefaultTableModel
    {
    	// just to remove the warning
    	static final long serialVersionUID = 0;

        public MyRevocationTableModel(Object[][] data, String[] headers)
        {
            super(data, headers);
        }

		// make read-only except first column
        public boolean isCellEditable(int x, int y)
        {
            return (y > 0) ? false : true;
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        @SuppressWarnings("unchecked")
        public Class getColumnClass(int c)
        {
            return getValueAt(0, c).getClass();
        }

        public void setValueAt(Object value, int row, int col)
        {
            super.setValueAt(value, row, col);
        }
    };

    // last rule index used to update interface
    private static int lastRuleIndexDelegation = -1;
    private void SetupDelegationControls(boolean state, int ruleIndex)
    {
        if ( (lastRuleIndexDelegation != ruleIndex) && (lastRuleIndexDelegation != -1) )
        {
            // store controls state for last selected rule since another rule
            // is being selected
            ruleDelegationCache[lastRuleIndexDelegation].grantee = jComboBoxGrantee.getSelectedIndex();
            ruleDelegationCache[lastRuleIndexDelegation].transfert = jCheckBoxTransfert.isSelected();
            ruleDelegationCache[lastRuleIndexDelegation].grantOption = jCheckBoxWithGrant.isSelected();
            ruleDelegationCache[lastRuleIndexDelegation].ctxOption = jComboBoxContext.getSelectedIndex();
            ruleDelegationCache[lastRuleIndexDelegation].steps = (Integer)jSpinnerSteps.getValue();
        }
        jComboBoxGrantee.setSelectedIndex(ruleDelegationCache[ruleIndex].grantee);
        jCheckBoxTransfert.setSelected(ruleDelegationCache[ruleIndex].transfert);
        jCheckBoxWithGrant.setSelected(ruleDelegationCache[ruleIndex].grantOption);
        jComboBoxContext.setSelectedIndex(ruleDelegationCache[ruleIndex].ctxOption);
        jSpinnerSteps.setValue(ruleDelegationCache[ruleIndex].steps);
        if ( state )
        {
            // setup parameters
            jLabelGrantee.setEnabled(true);
            jComboBoxGrantee.setEnabled(true);
            jCheckBoxTransfert.setEnabled(true);
            jCheckBoxWithGrant.setEnabled(true);
            jLabelGrantContext.setEnabled(true);
            jComboBoxContext.setEnabled(true);
            // last setup for grant options
            if ( jCheckBoxWithGrant.isSelected() )
            {
                jLabelSteps.setEnabled(true);
                jSpinnerSteps.setEnabled(true);
            }
            else
            {
                jLabelSteps.setEnabled(false);
                jSpinnerSteps.setEnabled(false);
            }
        }
        else
        {
            // disable all controls
            jLabelGrantee.setEnabled(false);
            jComboBoxGrantee.setEnabled(false);
            jCheckBoxTransfert.setEnabled(false);
            jCheckBoxWithGrant.setEnabled(false);
            jLabelSteps.setEnabled(false);
            jSpinnerSteps.setEnabled(false);
            jLabelGrantContext.setEnabled(false);
            jComboBoxContext.setEnabled(false);
        }
        lastRuleIndexDelegation = ruleIndex;
    }

    // last rule index used to update interface
    private static int lastRuleIndexRevocation = -1;
    private void SetupRevocationControls(boolean state, int ruleIndex)
    {
        if ( (lastRuleIndexRevocation != ruleIndex) && (lastRuleIndexRevocation != -1) )
        {
            // store controls state for last selected rule since another rule
            // is being selected
            ruleRevocationCache[lastRuleIndexRevocation].cascade = jCheckBoxCascade.isSelected();
            ruleRevocationCache[lastRuleIndexRevocation].strong = jCheckBoxStrong.isSelected();
        }
        jCheckBoxCascade.setSelected(ruleRevocationCache[ruleIndex].cascade);
        jCheckBoxStrong.setSelected(ruleRevocationCache[ruleIndex].strong);

        // setup parameters
        jCheckBoxCascade.setEnabled(state);
        jCheckBoxStrong.setEnabled(state);

        lastRuleIndexRevocation = ruleIndex;
    }

    // common to the two tables
    public class MyTableCellRenderer extends DefaultTableCellRenderer
    {
    	// just to remove the warning
    	static final long serialVersionUID = 0;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            TableModel m = table.getModel();
            Color cellColor = Color.WHITE;
            String type = (String)m.getValueAt(row, 7);

            if ( type.equals("null") )
            	cellColor = new Color(0.8f, 1.0f, 0.7f);
            else
            	cellColor = new Color(0.7f, 0.7f, 1.0f);

            cell.setBackground( cellColor );

            return cell;
        }
    };
}
