/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JDialog.java
 *
 * Created on May 30, 2011, 3:26:05 PM
 */

package newmotorbac.dialog;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JOptionPane;
import orbac.AbstractOrbacPolicy;
import orbac.context.CContext;

/**
 *
 * @author fabien
 */
public class jDialogCreateAbstractRule extends javax.swing.JDialog {

    public boolean canceled = false;
    // the processed policy
    AbstractOrbacPolicy policy;
    boolean adorbacViewEnabled;

    /** Creates new form JDialog */
    public jDialogCreateAbstractRule(java.awt.Frame parent, boolean modal, String organization,
                                                                           String role,
                                                                           String activity,
                                                                           String view,
                                                                           AbstractOrbacPolicy policy,
                                                                           boolean adorbacViewEnabled,
                                                                           int ruleType) {
        super(parent, modal);
        initComponents();

        this.policy = policy;
        this.adorbacViewEnabled = adorbacViewEnabled;

        switch ( ruleType )
        {
            case AbstractOrbacPolicy.TYPE_PERMISSION:
                setTitle("Create a new abstract permission");
                // no violation context for a permission
                jComboBoxViolationContext.setVisible(false);
                jLabelViol1.setVisible(false);
                jLabelViol2.setVisible(false);
                jLabelViol3.setVisible(false);
                break;
            case AbstractOrbacPolicy.TYPE_PROHIBITION:
                setTitle("Create a new abstract prohibition");
                // no violation context for a prohibition
                jComboBoxViolationContext.setVisible(false);
                jLabelViol1.setVisible(false);
                jLabelViol2.setVisible(false);
                jLabelViol3.setVisible(false);
                break;
            case AbstractOrbacPolicy.TYPE_OBLIGATION:
                setTitle("Create a new abstract obligation");
                break;
        }
        // display the organization name
        jLabelOrg.setText(organization);

        // fill combo boxes
        FillRoleCB(false);
        FillActivityCB(false);
        FillViewCB(false);
        try
        {
            Map<String, CContext> ctxs = policy.GetContexts();
            for ( Entry<String, CContext> e : ctxs.entrySet() )
            {
                jComboBoxContext.addItem(e.getKey());
                jComboBoxViolationContext.addItem(e.getKey());
            }
        }
        catch ( Exception e ) {}

        if ( jComboBoxFirstEntity.getItemCount() > 0 ) jComboBoxFirstEntity.setSelectedIndex(0);
        if ( jComboBoxSecondEntity.getItemCount() > 0 ) jComboBoxSecondEntity.setSelectedIndex(0);
        if ( jComboBoxThirdEntity.getItemCount() > 0 ) jComboBoxThirdEntity.setSelectedIndex(0);

        if ( role != null ) jComboBoxFirstEntity.setSelectedItem(role);
        if ( activity != null ) jComboBoxSecondEntity.setSelectedItem(activity);
        if ( view != null ) jComboBoxThirdEntity.setSelectedItem(view);
    }

    // constructor used when editing a rule
    public jDialogCreateAbstractRule(java.awt.Frame parent, boolean modal, String organization,
                                                                           String role,
                                                                           String activity,
                                                                           String view,
                                                                           String context,
                                                                           String violationContext,
                                                                           String ruleName,
                                                                           AbstractOrbacPolicy policy,
                                                                           boolean adorbacViewEnabled,
                                                                           int ruleType) {
        super(parent, modal);
        initComponents();

        this.policy = policy;
        this.adorbacViewEnabled = adorbacViewEnabled;

        switch ( ruleType )
        {
            case AbstractOrbacPolicy.TYPE_PERMISSION:
                setTitle("Edit an abstract permission");
                // no violation context for a permission
                jComboBoxViolationContext.setVisible(false);
                jLabelViol1.setVisible(false);
                jLabelViol2.setVisible(false);
                jLabelViol3.setVisible(false);
                break;
            case AbstractOrbacPolicy.TYPE_PROHIBITION:
                setTitle("Edit an abstract prohibition");
                // no violation context for a prohibition
                jComboBoxViolationContext.setVisible(false);
                jLabelViol1.setVisible(false);
                jLabelViol2.setVisible(false);
                jLabelViol3.setVisible(false);
                break;
            case AbstractOrbacPolicy.TYPE_OBLIGATION:
                setTitle("Edit an abstract obligation");
                break;
        }
        // display the organization name
        jLabelOrg.setText(organization);
        // display rule name
        jTextFieldRuleName.setText(ruleName);

        // fill combo boxes
        FillRoleCB(false);
        FillActivityCB(false);
        FillViewCB(false);
        try
        {
            Map<String, CContext> ctxs = policy.GetContexts();
            for ( Entry<String, CContext> e : ctxs.entrySet() )
            {
                jComboBoxContext.addItem(e.getKey());
                jComboBoxViolationContext.addItem(e.getKey());
            }
        }
        catch ( Exception e ) {}

        if ( jComboBoxFirstEntity.getItemCount() > 0 ) jComboBoxFirstEntity.setSelectedIndex(0);
        if ( jComboBoxSecondEntity.getItemCount() > 0 ) jComboBoxSecondEntity.setSelectedIndex(0);
        if ( jComboBoxThirdEntity.getItemCount() > 0 ) jComboBoxThirdEntity.setSelectedIndex(0);

        // select rule parameters
        jComboBoxFirstEntity.setSelectedItem(role);
        jComboBoxSecondEntity.setSelectedItem(activity);
        jComboBoxThirdEntity.setSelectedItem(view);
        jComboBoxContext.setSelectedItem(context);
        jComboBoxViolationContext.setSelectedItem(violationContext);
    }

    public String GetFirstEntity()
    {
        return (String)jComboBoxFirstEntity.getSelectedItem();
    }
    public String GetSecondEntity()
    {
        return (String)jComboBoxSecondEntity.getSelectedItem();
    }
    public String GetThirdEntity()
    {
        return (String)jComboBoxThirdEntity.getSelectedItem();
    }
    public String GetContext()
    {
        return (String)jComboBoxContext.getSelectedItem();
    }
    public String GetViolationContext()
    {
        return (String)jComboBoxViolationContext.getSelectedItem();
    }
    public String GetRuleName()
    {
        return (String)jTextFieldRuleName.getText();
    }

    private void FillRoleCB(boolean displayConcreteEntities)
    {
        jComboBoxFirstEntity.removeAllItems();
        try
        {
            if ( displayConcreteEntities )
            {
                 Set<String> subjects = policy.GetSubjects();
                for ( String s : subjects )
                    jComboBoxFirstEntity.addItem(s);
            }
            else
            {
                 Set<String> roles = policy.GetRolesList(adorbacViewEnabled);
                for ( String r : roles )
                    jComboBoxFirstEntity.addItem(r);
            }
        }
        catch ( Exception e )
        {
            System.out.print(e);
        }
    }
    private void FillActivityCB(boolean displayConcreteEntities)
    {
        jComboBoxSecondEntity.removeAllItems();
        try
        {
            if ( displayConcreteEntities )
            {
                 Set<String> actions = policy.GetActions();
                for ( String a : actions )
                    jComboBoxSecondEntity.addItem(a);
            }
            else
            {
                 Set<String> activities = policy.GetActivitiesList(adorbacViewEnabled);
                for ( String a : activities )
                    jComboBoxSecondEntity.addItem(a);
            }
        }
        catch ( Exception e )
        {
            System.out.print(e);
        }
    }
    private void FillViewCB(boolean displayConcreteEntities)
    {
        jComboBoxThirdEntity.removeAllItems();
        try
        {
            if ( displayConcreteEntities )
            {
                 Set<String> objects = policy.GetObjects();
                for ( String o : objects )
                    jComboBoxThirdEntity.addItem(o);
            }
            else
            {
                 Set<String> views = policy.GetViewsList(adorbacViewEnabled);
                for ( String v : views )
                    jComboBoxThirdEntity.addItem(v);
            }
        }
        catch ( Exception e )
        {
            System.out.print(e);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextFieldRuleName = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jComboBoxRoleSubject = new javax.swing.JComboBox();
        jComboBoxActivityAction = new javax.swing.JComboBox();
        jComboBoxViewObject = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxFirstEntity = new javax.swing.JComboBox();
        jComboBoxSecondEntity = new javax.swing.JComboBox();
        jComboBoxThirdEntity = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxContext = new javax.swing.JComboBox();
        jLabelViol1 = new javax.swing.JLabel();
        jComboBoxViolationContext = new javax.swing.JComboBox();
        jLabelViol2 = new javax.swing.JLabel();
        jLabelViol3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelOrg = new javax.swing.JLabel();
        jButtonCancel = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        setResizable(false);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(jDialogCreateAbstractRule.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jTextFieldRuleName.setText(resourceMap.getString("jTextFieldRuleName.text")); // NOI18N
        jTextFieldRuleName.setName("jTextFieldRuleName"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jComboBoxRoleSubject.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "role", "subject" }));
        jComboBoxRoleSubject.setName("jComboBoxRoleSubject"); // NOI18N
        jComboBoxRoleSubject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxRoleSubjectItemStateChanged(evt);
            }
        });

        jComboBoxActivityAction.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "activity", "action" }));
        jComboBoxActivityAction.setName("jComboBoxActivityAction"); // NOI18N
        jComboBoxActivityAction.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxActivityActionItemStateChanged(evt);
            }
        });

        jComboBoxViewObject.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "view", "object" }));
        jComboBoxViewObject.setName("jComboBoxViewObject"); // NOI18N
        jComboBoxViewObject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxViewObjectItemStateChanged(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jComboBoxFirstEntity.setName("jComboBoxFirstEntity"); // NOI18N

        jComboBoxSecondEntity.setName("jComboBoxSecondEntity"); // NOI18N

        jComboBoxThirdEntity.setName("jComboBoxThirdEntity"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jComboBoxContext.setName("jComboBoxContext"); // NOI18N

        jLabelViol1.setText(resourceMap.getString("jLabelViol1.text")); // NOI18N
        jLabelViol1.setName("jLabelViol1"); // NOI18N

        jComboBoxViolationContext.setName("jComboBoxViolationContext"); // NOI18N

        jLabelViol2.setText(resourceMap.getString("jLabelViol2.text")); // NOI18N
        jLabelViol2.setName("jLabelViol2"); // NOI18N

        jLabelViol3.setText(resourceMap.getString("jLabelViol3.text")); // NOI18N
        jLabelViol3.setName("jLabelViol3"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jLabelOrg.setText(resourceMap.getString("jLabelOrg.text")); // NOI18N
        jLabelOrg.setName("jLabelOrg"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBoxRoleSubject, 0, 106, Short.MAX_VALUE)
                        .addComponent(jComboBoxActivityAction, javax.swing.GroupLayout.Alignment.TRAILING, 0, 106, Short.MAX_VALUE)
                        .addComponent(jComboBoxViewObject, 0, 106, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5)
                        .addComponent(jLabelViol2)
                        .addComponent(jLabelViol1)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabelOrg, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxFirstEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxSecondEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxThirdEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxContext, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelViol3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxViolationContext, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabelOrg, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBoxRoleSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxFirstEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxActivityAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxSecondEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxViewObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBoxThirdEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxContext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabelViol2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelViol1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelViol3)
                            .addComponent(jComboBoxViolationContext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, 0, 396, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldRuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldRuleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonOk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxRoleSubjectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxRoleSubjectItemStateChanged
        // modify subject/role combo box
        FillRoleCB( jComboBoxRoleSubject.getSelectedIndex() != 0 );
    }//GEN-LAST:event_jComboBoxRoleSubjectItemStateChanged

    private void jComboBoxActivityActionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxActivityActionItemStateChanged
        // modify action/activity combo box
        FillActivityCB( jComboBoxActivityAction.getSelectedIndex() != 0 );
    }//GEN-LAST:event_jComboBoxActivityActionItemStateChanged

    private void jComboBoxViewObjectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxViewObjectItemStateChanged
        // modify object/view combo box
        FillViewCB( jComboBoxViewObject.getSelectedIndex() != 0 );
    }//GEN-LAST:event_jComboBoxViewObjectItemStateChanged

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        canceled = true;
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        // check if a rule name has been specified
    	if ( jTextFieldRuleName.getText().equals("") )
    	{
            JOptionPane.showMessageDialog(this, "You must specify a rule name");
            return;
    	}
        dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JComboBox jComboBoxActivityAction;
    private javax.swing.JComboBox jComboBoxContext;
    private javax.swing.JComboBox jComboBoxFirstEntity;
    private javax.swing.JComboBox jComboBoxRoleSubject;
    private javax.swing.JComboBox jComboBoxSecondEntity;
    private javax.swing.JComboBox jComboBoxThirdEntity;
    private javax.swing.JComboBox jComboBoxViewObject;
    private javax.swing.JComboBox jComboBoxViolationContext;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelOrg;
    private javax.swing.JLabel jLabelViol1;
    private javax.swing.JLabel jLabelViol2;
    private javax.swing.JLabel jLabelViol3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldRuleName;
    // End of variables declaration//GEN-END:variables

}
