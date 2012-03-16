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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import newmotorbac.util.OrbacPolicyContext;
import newmotorbac.util.RoleDelegationParameters;
import newmotorbac.util.RoleDelegationResult;
import newmotorbac.util.RoleRevocationParameters;
import newmotorbac.util.RoleRevocationResult;
import orbac.abstractEntities.CRoleAssignment;
import orbac.exception.COrbacException;

/**
 *
 * @author fabien
 */
public class jDialogRoleDelegation extends javax.swing.JDialog {
    // policy context
    OrbacPolicyContext thisContext;

    public boolean canceled = false;

    // delegation state variables
    // the role table listing the roles for the current user
    protected String headersDelegation[] = {"delegate", "role", "role assignment object"};
    // table data
    private Object tableDataDelegation[][] = null;
    // table model
    private MyDelegationTableModel modelDelegation;
    // rule delegation parameters cache
    private RoleDelegationParameters roleDelegationCache[] = null;

    // revocaton state variables
    // the role table listing the delegated roles for the current user
    protected String headersRevocation[] = {"revoke", "grantee", "role"};
    // table data
    private Object tableDataRevocation[][] = null;
    // number of delegated roles
    private int delegatedRolesNumber;
    // table model
    private MyRevocationTableModel modelRevocation;
    // rule delegation parameters cache
    private RoleRevocationParameters roleRevocationCache[] = null;

    /** Creates new form jDialogRuleDelegation */
    public jDialogRoleDelegation(java.awt.Frame parent, boolean modal, OrbacPolicyContext thisContext) {
        super(parent, modal);
        initComponents();

        this.thisContext = thisContext;

        // set title
        setTitle("Manage role delegation");

        // setup delegation controls
        SetupDelegationTable();
        // setup revocation controls
        SetupRevocationTable();
    }

    private void SetupRevocationTable()
    {
        // build table data
        // get the roles for the current adorbac user
        int rolesTotalNumber = 0;
        try
        {
            HashMap<String, CRoleAssignment> roles = thisContext.thePolicy.GetCurrentAdorbacUserDelegatedRoles();
            // count the number of role assignment related to the current adorbac user
            Set<Entry<String, CRoleAssignment>> es = roles.entrySet();
            tableDataRevocation = new Object[roles.size()][3];
            delegatedRolesNumber = 0;

            for ( Entry<String, CRoleAssignment> rae : es )
            {
                CRoleAssignment	ra = rae.getValue();
                tableDataRevocation[delegatedRolesNumber][0] = false;
                tableDataRevocation[delegatedRolesNumber][1] = ra.GetRole();
                tableDataRevocation[delegatedRolesNumber][2] = rae.getKey();

                delegatedRolesNumber++;
            }
        }
        catch (COrbacException e)
        {
            System.out.println("Error building the current adorbac user set of delegated roles");
            e.printStackTrace();
        }

        // set up state cache to record delegation parameters for each rule
        roleRevocationCache = new RoleRevocationParameters[rolesTotalNumber];
        // for each rule setup the parameter cache
        for ( int i = 0; i < rolesTotalNumber; i++ )
            roleRevocationCache[i] = new RoleRevocationParameters();

        // setup model
	modelRevocation = new MyRevocationTableModel(tableDataRevocation, headersRevocation);
        jTableRevocation.setModel(modelRevocation);
    }

    // return all the chosen permissions to delegate and the associated parameters
    public Vector<RoleRevocationResult> GetRevokedRoles()
    {
        Vector<RoleRevocationResult> roles = new Vector<RoleRevocationResult>();
        for ( int i = 0; i < delegatedRolesNumber; i++ )
        {
            if ( (Boolean)jTableRevocation.getModel().getValueAt(i, 0) == false ) continue;
            RoleRevocationResult rr = new RoleRevocationResult();
            rr.roleAssignmentName = (String)jTableRevocation.getModel().getValueAt(i, 2);
            rr.cascade = roleRevocationCache[i].cascade;
            rr.strong = roleRevocationCache[i].strong;
            roles.add(rr);
        }
        return roles;
    }
    
    private void SetupDelegationTable()
    {
        // build table data
        // get the roles for the current adorbac user
        try
        {
            HashMap<String, CRoleAssignment> roles = thisContext.thePolicy.GetCurrentAdorbacUserRoleAssignments();
            // count the number of role assignment related to the current adorbac user
            Set<Entry<String, CRoleAssignment>> es = roles.entrySet();
            tableDataDelegation = new Object[roles.size()][3];
            int i = 0;

            for ( Entry<String, CRoleAssignment> rae : es )
            {
                CRoleAssignment	ra = rae.getValue();
                tableDataDelegation[i][0] = false;
                tableDataDelegation[i][1] = ra.GetRole();
                tableDataDelegation[i][2] = rae.getKey();

                i++;
            }

            // fill subjects list
            for ( String s : thisContext.thePolicy.GetSubjects() )
                jComboBoxGrantee.addItem(s);
        }
        catch (COrbacException e)
        {
            System.out.println("Error building the current adorbac user set of roles");
            e.printStackTrace();
        }

        // set up state cache to record delegation parameters for each rule
        roleDelegationCache = new RoleDelegationParameters[tableDataDelegation.length];
        // for each rule setup the parameter cache
        for ( int i = 0; i < tableDataDelegation.length; i++ )
        {
            roleDelegationCache[i] = new RoleDelegationParameters();
            roleDelegationCache[i].grantee = -1;
            roleDelegationCache[i].transfert = false;
            roleDelegationCache[i].roleAssignmentName = "na";
            roleDelegationCache[i].grantOption = false;
            roleDelegationCache[i].steps = 1;
        }

        // setup model
	modelDelegation = new MyDelegationTableModel(tableDataDelegation, headersDelegation);
        jTableDelegation.setModel(modelDelegation);
    }

    // return all the chosen permissions to delegate and the associated parameters
    public Vector<RoleDelegationResult> GetDelegatedRoles()
    {
        Vector<RoleDelegationResult> roles = new Vector<RoleDelegationResult>();
        for ( int i = 0; i < roleDelegationCache.length; i++ )
        {
            if ( (Boolean)jTableDelegation.getModel().getValueAt(i, 0) == false ) continue;
            RoleDelegationResult dr = new RoleDelegationResult();
            dr.roleAssignmentName = (String)tableDataDelegation[i][2];
            dr.grantee = (String)jComboBoxGrantee.getItemAt(roleDelegationCache[i].grantee);
            dr.transfert = roleDelegationCache[i].transfert;

            roles.add(dr);
        }
        return roles;
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

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(jDialogRoleDelegation.class);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jLabelGrantee.setText(resourceMap.getString("jLabelGrantee.text")); // NOI18N
        jLabelGrantee.setEnabled(false);
        jLabelGrantee.setName("jLabelGrantee"); // NOI18N

        jComboBoxGrantee.setEnabled(false);
        jComboBoxGrantee.setName("jComboBoxGrantee"); // NOI18N

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
                        .addComponent(jLabelGrantee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxGrantee, 0, 154, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBoxTransfert)
                        .addGap(193, 193, 193))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCheckBoxWithGrant)
                        .addContainerGap(412, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelSteps)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerSteps, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                        .addGap(443, 443, 443))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelGrantee)
                    .addComponent(jComboBoxGrantee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxTransfert))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxWithGrant)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSteps)
                    .addComponent(jSpinnerSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanelDelegationLayout = new javax.swing.GroupLayout(jPanelDelegation);
        jPanelDelegation.setLayout(jPanelDelegationLayout);
        jPanelDelegationLayout.setHorizontalGroup(
            jPanelDelegationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDelegationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDelegationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelDelegationLayout.setVerticalGroup(
            jPanelDelegationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDelegationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
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
                .addContainerGap(233, Short.MAX_VALUE))
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelRevocationLayout.setVerticalGroup(
            jPanelRevocationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelRevocationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
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
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        canceled = false;

        // store currently selected role parameters in the role delegation cache
        roleDelegationCache[lastRoleIndexDelegation].grantee = jComboBoxGrantee.getSelectedIndex();
        roleDelegationCache[lastRoleIndexDelegation].transfert = jCheckBoxTransfert.isSelected();
        roleDelegationCache[lastRoleIndexDelegation].roleAssignmentName = (String)jTableDelegation.getValueAt(jTableDelegation.getSelectedRow(), 2);
        roleDelegationCache[lastRoleIndexDelegation].grantOption = jCheckBoxWithGrant.isSelected();
        roleDelegationCache[lastRoleIndexDelegation].steps = (Integer)jSpinnerSteps.getValue();

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
    private javax.swing.JComboBox jComboBoxGrantee;
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
    private static int lastRoleIndexDelegation = -1;
    private void SetupDelegationControls(boolean state, int roleIndex)
    {
        if ( (lastRoleIndexDelegation != roleIndex) && (lastRoleIndexDelegation != -1) )
        {
            // store controls state for last selected role since another role
            // is being selected
            roleDelegationCache[lastRoleIndexDelegation].grantee = jComboBoxGrantee.getSelectedIndex();
            roleDelegationCache[lastRoleIndexDelegation].transfert = jCheckBoxTransfert.isSelected();
            roleDelegationCache[lastRoleIndexDelegation].roleAssignmentName = (String)jTableDelegation.getValueAt(jTableDelegation.getSelectedRow(), 2);
            roleDelegationCache[lastRoleIndexDelegation].grantOption = jCheckBoxWithGrant.isSelected();
            roleDelegationCache[lastRoleIndexDelegation].steps = (Integer)jSpinnerSteps.getValue();
        }
        jComboBoxGrantee.setSelectedIndex(roleDelegationCache[roleIndex].grantee);
        jCheckBoxTransfert.setSelected(roleDelegationCache[roleIndex].transfert);
        jCheckBoxWithGrant.setSelected(roleDelegationCache[roleIndex].grantOption);
        jSpinnerSteps.setValue(roleDelegationCache[roleIndex].steps);
        
        // setup parameters
        jLabelGrantee.setEnabled(state);
        jComboBoxGrantee.setEnabled(state);
        jCheckBoxTransfert.setEnabled(state);
        jCheckBoxWithGrant.setEnabled(state);

        // last setup for grant options
        if ( state && jCheckBoxWithGrant.isSelected() )
        {
            jLabelSteps.setEnabled(true);
            jSpinnerSteps.setEnabled(true);
        }
        else
        {
            jLabelSteps.setEnabled(false);
            jSpinnerSteps.setEnabled(false);
        }

        lastRoleIndexDelegation = roleIndex;
    }

    // last rule index used to update interface
    private static int lastRuleIndexRevocation = -1;
    private void SetupRevocationControls(boolean state, int roleIndex)
    {
        if ( (lastRuleIndexRevocation != roleIndex) && (lastRuleIndexRevocation != -1) )
        {
            // store controls state for last selected rule since another rule
            // is being selected
            roleRevocationCache[lastRuleIndexRevocation].cascade = jCheckBoxCascade.isSelected();
            roleRevocationCache[lastRuleIndexRevocation].strong = jCheckBoxStrong.isSelected();
        }
        jCheckBoxCascade.setSelected(roleRevocationCache[roleIndex].cascade);
        jCheckBoxStrong.setSelected(roleRevocationCache[roleIndex].strong);

        // setup parameters
        jCheckBoxCascade.setEnabled(state);
        jCheckBoxStrong.setEnabled(state);

        lastRuleIndexRevocation = roleIndex;
    }
}
