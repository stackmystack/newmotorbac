/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JDialogCreatePolicy.java
 *
 * Created on May 9, 2011, 7:08:17 PM
 */

package newmotorbac.dialog;

import javax.swing.JOptionPane;
import orbac.COrbacCore;
import orbac.exception.COrbacException;

/**
 *
 * @author fabien
 */
public class jDialogCreatePolicy extends javax.swing.JDialog {

    public boolean canceled = false;
    /** Creates new form JDialogCreatePolicy */
    public jDialogCreatePolicy(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        // get the orbac core instance
        COrbacCore core = COrbacCore.GetTheInstance();
        // fill combo box
        for ( String c : core.GetSupportedPolicyTypes() )
        {
            jComboBoxImplementation.addItem(c);
        }
        jComboBoxImplementation.setSelectedIndex(0);

        getRootPane().setDefaultButton(jButtonOk);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabelPolicyName = new javax.swing.JLabel();
        jTextFieldPolicyName = new javax.swing.JTextField();
        jComboBoxImplementation = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaImplementationInfos = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jCheckBoxManageRoleView = new javax.swing.JCheckBox();
        jCheckBoxManageActivityView = new javax.swing.JCheckBox();
        jCheckBoxManageViewView = new javax.swing.JCheckBox();
        jCheckBoxManageOrganizationView = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jCheckBoxManageRoleAssignmentView = new javax.swing.JCheckBox();
        jCheckBoxManageActivityAssignmentView = new javax.swing.JCheckBox();
        jCheckBoxManageViewAssignmentView = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jCheckBoxManageRoleHierarchyView = new javax.swing.JCheckBox();
        jCheckBoxManageActivityHierarchyView = new javax.swing.JCheckBox();
        jCheckBoxManageViewHierarchyView = new javax.swing.JCheckBox();
        jCheckBoxManageOrganizationHierarchyView = new javax.swing.JCheckBox();
        jCheckBoxManageLicenseView = new javax.swing.JCheckBox();
        jCheckBoxManageLicenseDelegationView = new javax.swing.JCheckBox();
        jCheckBoxManageLicenseTransferView = new javax.swing.JCheckBox();
        jCheckBoxManageGrantOptionView = new javax.swing.JCheckBox();
        jCheckBoxManageRoleDelegationView = new javax.swing.JCheckBox();
        jCheckBoxManageRoleTransferView = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        setResizable(false);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(jDialogCreatePolicy.class);
        jButtonOk.setText(resourceMap.getString("jButtonOk.text")); // NOI18N
        jButtonOk.setName("jButtonOk"); // NOI18N
        jButtonOk.setSelected(true);
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        jButtonCancel.setText(resourceMap.getString("jButtonCancel.text")); // NOI18N
        jButtonCancel.setName("jButtonCancel"); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jLabelPolicyName.setText(resourceMap.getString("jLabelPolicyName.text")); // NOI18N
        jLabelPolicyName.setName("jLabelPolicyName"); // NOI18N

        jTextFieldPolicyName.setName("jTextFieldPolicyName"); // NOI18N

        jComboBoxImplementation.setName("jComboBoxImplementation"); // NOI18N
        jComboBoxImplementation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxImplementationItemStateChanged(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel8.border.title"))); // NOI18N
        jPanel8.setName("jPanel8"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextAreaImplementationInfos.setColumns(1);
        jTextAreaImplementationInfos.setEditable(false);
        jTextAreaImplementationInfos.setLineWrap(true);
        jTextAreaImplementationInfos.setRows(1);
        jTextAreaImplementationInfos.setName("jTextAreaImplementationInfos"); // NOI18N
        jScrollPane1.setViewportView(jTextAreaImplementationInfos);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
        );

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jScrollPane2.border.title"))); // NOI18N
        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jPanel9.setMaximumSize(new java.awt.Dimension(353, 742));
        jPanel9.setMinimumSize(new java.awt.Dimension(353, 742));
        jPanel9.setName("jPanel9"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        jCheckBoxManageRoleView.setText(resourceMap.getString("jCheckBoxManageRoleView.text")); // NOI18N
        jCheckBoxManageRoleView.setName("jCheckBoxManageRoleView"); // NOI18N

        jCheckBoxManageActivityView.setText(resourceMap.getString("jCheckBoxManageActivityView.text")); // NOI18N
        jCheckBoxManageActivityView.setName("jCheckBoxManageActivityView"); // NOI18N

        jCheckBoxManageViewView.setText(resourceMap.getString("jCheckBoxManageViewView.text")); // NOI18N
        jCheckBoxManageViewView.setName("jCheckBoxManageViewView"); // NOI18N

        jCheckBoxManageOrganizationView.setText(resourceMap.getString("jCheckBoxManageOrganizationView.text")); // NOI18N
        jCheckBoxManageOrganizationView.setName("jCheckBoxManageOrganizationView"); // NOI18N

        jSeparator2.setName("jSeparator2"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jCheckBoxManageRoleAssignmentView.setText(resourceMap.getString("jCheckBoxManageRoleAssignmentView.text")); // NOI18N
        jCheckBoxManageRoleAssignmentView.setName("jCheckBoxManageRoleAssignmentView"); // NOI18N

        jCheckBoxManageActivityAssignmentView.setText(resourceMap.getString("jCheckBoxManageActivityAssignmentView.text")); // NOI18N
        jCheckBoxManageActivityAssignmentView.setName("jCheckBoxManageActivityAssignmentView"); // NOI18N

        jCheckBoxManageViewAssignmentView.setText(resourceMap.getString("jCheckBoxManageViewAssignmentView.text")); // NOI18N
        jCheckBoxManageViewAssignmentView.setName("jCheckBoxManageViewAssignmentView"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jSeparator3.setName("jSeparator3"); // NOI18N

        jCheckBoxManageRoleHierarchyView.setText(resourceMap.getString("jCheckBoxManageRoleHierarchyView.text")); // NOI18N
        jCheckBoxManageRoleHierarchyView.setName("jCheckBoxManageRoleHierarchyView"); // NOI18N

        jCheckBoxManageActivityHierarchyView.setText(resourceMap.getString("jCheckBoxManageActivityHierarchyView.text")); // NOI18N
        jCheckBoxManageActivityHierarchyView.setName("jCheckBoxManageActivityHierarchyView"); // NOI18N

        jCheckBoxManageViewHierarchyView.setText(resourceMap.getString("jCheckBoxManageViewHierarchyView.text")); // NOI18N
        jCheckBoxManageViewHierarchyView.setName("jCheckBoxManageViewHierarchyView"); // NOI18N

        jCheckBoxManageOrganizationHierarchyView.setText(resourceMap.getString("jCheckBoxManageOrganizationHierarchyView.text")); // NOI18N
        jCheckBoxManageOrganizationHierarchyView.setName("jCheckBoxManageOrganizationHierarchyView"); // NOI18N

        jCheckBoxManageLicenseView.setSelected(true);
        jCheckBoxManageLicenseView.setText(resourceMap.getString("jCheckBoxManageLicenseView.text")); // NOI18N
        jCheckBoxManageLicenseView.setName("jCheckBoxManageLicenseView"); // NOI18N
        jCheckBoxManageLicenseView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxManageLicenseViewActionPerformed(evt);
            }
        });

        jCheckBoxManageLicenseDelegationView.setText(resourceMap.getString("jCheckBoxManageLicenseDelegationView.text")); // NOI18N
        jCheckBoxManageLicenseDelegationView.setName("jCheckBoxManageLicenseDelegationView"); // NOI18N

        jCheckBoxManageLicenseTransferView.setText(resourceMap.getString("jCheckBoxManageLicenseTransferView.text")); // NOI18N
        jCheckBoxManageLicenseTransferView.setName("jCheckBoxManageLicenseTransferView"); // NOI18N

        jCheckBoxManageGrantOptionView.setText(resourceMap.getString("jCheckBoxManageGrantOptionView.text")); // NOI18N
        jCheckBoxManageGrantOptionView.setName("jCheckBoxManageGrantOptionView"); // NOI18N

        jCheckBoxManageRoleDelegationView.setText(resourceMap.getString("jCheckBoxManageRoleDelegationView.text")); // NOI18N
        jCheckBoxManageRoleDelegationView.setName("jCheckBoxManageRoleDelegationView"); // NOI18N

        jCheckBoxManageRoleTransferView.setText(resourceMap.getString("jCheckBoxManageRoleTransferView.text")); // NOI18N
        jCheckBoxManageRoleTransferView.setName("jCheckBoxManageRoleTransferView"); // NOI18N

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jSeparator5.setName("jSeparator5"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                    .addComponent(jCheckBoxManageRoleView)
                    .addComponent(jCheckBoxManageActivityView)
                    .addComponent(jCheckBoxManageViewView)
                    .addComponent(jCheckBoxManageOrganizationView)
                    .addComponent(jLabel3)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                    .addComponent(jCheckBoxManageRoleAssignmentView)
                    .addComponent(jCheckBoxManageActivityAssignmentView)
                    .addComponent(jCheckBoxManageViewAssignmentView)
                    .addComponent(jLabel4)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                    .addComponent(jCheckBoxManageRoleHierarchyView)
                    .addComponent(jCheckBoxManageActivityHierarchyView)
                    .addComponent(jCheckBoxManageViewHierarchyView)
                    .addComponent(jCheckBoxManageOrganizationHierarchyView)
                    .addComponent(jLabel5)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                    .addComponent(jCheckBoxManageLicenseView)
                    .addComponent(jLabel6)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                    .addComponent(jCheckBoxManageRoleDelegationView)
                    .addComponent(jCheckBoxManageRoleTransferView)
                    .addComponent(jCheckBoxManageLicenseDelegationView)
                    .addComponent(jCheckBoxManageLicenseTransferView)
                    .addComponent(jCheckBoxManageGrantOptionView))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageRoleView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageActivityView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageViewView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageOrganizationView)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageRoleAssignmentView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageActivityAssignmentView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageViewAssignmentView)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageRoleHierarchyView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageActivityHierarchyView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageViewHierarchyView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageOrganizationHierarchyView)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageLicenseView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageLicenseDelegationView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageLicenseTransferView)
                .addGap(8, 8, 8)
                .addComponent(jCheckBoxManageGrantOptionView)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageRoleDelegationView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxManageRoleTransferView)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancel))
                    .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1)
                                .addComponent(jLabelPolicyName))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jComboBoxImplementation, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldPolicyName, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPolicyName)
                    .addComponent(jTextFieldPolicyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxImplementation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
    	// check if a policy name has been specified
    	if ( jTextFieldPolicyName.getText().equals("") )
    	{
            JOptionPane.showMessageDialog(this, "You must specify a policy name");
            return;
    	}
        // check that a policy implementation has been chosen
        if ( jComboBoxImplementation.getSelectedIndex() < 0 )
    	{
            JOptionPane.showMessageDialog(this, "You must choose a policy implementation");
            return;
    	}
        dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        canceled = true;
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jCheckBoxManageLicenseViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxManageLicenseViewActionPerformed
        jCheckBoxManageLicenseView.setSelected(true);
    }//GEN-LAST:event_jCheckBoxManageLicenseViewActionPerformed

    private void jComboBoxImplementationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxImplementationItemStateChanged
        // get selected factory type
        String selectedType = (String)jComboBoxImplementation.getSelectedItem();
        // display information absage depuis queout the selected factory in the textarea
        COrbacCore core = COrbacCore.GetTheInstance();
        try
        {
            jTextAreaImplementationInfos.setText( core.GetPolicyFactoryInformation(selectedType) );
        }
        catch ( COrbacException e )
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jComboBoxImplementationItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    public javax.swing.JCheckBox jCheckBoxManageActivityAssignmentView;
    public javax.swing.JCheckBox jCheckBoxManageActivityHierarchyView;
    public javax.swing.JCheckBox jCheckBoxManageActivityView;
    public javax.swing.JCheckBox jCheckBoxManageGrantOptionView;
    public javax.swing.JCheckBox jCheckBoxManageLicenseDelegationView;
    public javax.swing.JCheckBox jCheckBoxManageLicenseTransferView;
    public javax.swing.JCheckBox jCheckBoxManageLicenseView;
    public javax.swing.JCheckBox jCheckBoxManageOrganizationHierarchyView;
    public javax.swing.JCheckBox jCheckBoxManageOrganizationView;
    public javax.swing.JCheckBox jCheckBoxManageRoleAssignmentView;
    public javax.swing.JCheckBox jCheckBoxManageRoleDelegationView;
    public javax.swing.JCheckBox jCheckBoxManageRoleHierarchyView;
    public javax.swing.JCheckBox jCheckBoxManageRoleTransferView;
    public javax.swing.JCheckBox jCheckBoxManageRoleView;
    public javax.swing.JCheckBox jCheckBoxManageViewAssignmentView;
    public javax.swing.JCheckBox jCheckBoxManageViewHierarchyView;
    public javax.swing.JCheckBox jCheckBoxManageViewView;
    public javax.swing.JComboBox jComboBoxImplementation;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelPolicyName;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextArea jTextAreaImplementationInfos;
    public javax.swing.JTextField jTextFieldPolicyName;
    // End of variables declaration//GEN-END:variables

}
