/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * jDialogAddSeparationConstraint.java
 *
 * Created on Jun 6, 2011, 5:21:57 PM
 */

package newmotorbac.dialog;

import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JOptionPane;
import newmotorbac.util.OrbacPolicyContext;
import orbac.context.CContext;

/**
 *
 * @author fabien
 */
public class jDialogAddSeparationConstraint extends javax.swing.JDialog {

    public boolean canceled = false;
    // associated policy
    private OrbacPolicyContext thisContext;

    /** Creates new form jDialogAddSeparationConstraint */
    public jDialogAddSeparationConstraint(java.awt.Frame parent, boolean modal, OrbacPolicyContext thisContext) {
        super(parent, modal);
        initComponents();

        this.thisContext = thisContext;

        // select role separation constraint by default
        jComboBoxSeparationTypeItemStateChanged(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelConstraintType = new javax.swing.JLabel();
        jLabelFirstEntity = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabelSecondEntity = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxSeparationType = new javax.swing.JComboBox();
        jComboBoxFirstEntity = new javax.swing.JComboBox();
        jComboBoxSecondEntity = new javax.swing.JComboBox();
        jComboBoxFirstOrganization = new javax.swing.JComboBox();
        jComboBoxSecondOrganization = new javax.swing.JComboBox();
        jButtonCancel = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        setResizable(false);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(jDialogAddSeparationConstraint.class);
        jLabelConstraintType.setText(resourceMap.getString("jLabelConstraintType.text")); // NOI18N
        jLabelConstraintType.setName("jLabelConstraintType"); // NOI18N

        jLabelFirstEntity.setText(resourceMap.getString("jLabelFirstEntity.text")); // NOI18N
        jLabelFirstEntity.setName("jLabelFirstEntity"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabelSecondEntity.setText(resourceMap.getString("jLabelSecondEntity.text")); // NOI18N
        jLabelSecondEntity.setName("jLabelSecondEntity"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jComboBoxSeparationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "role separation constraint", "activity separation constraint", "view separation constraint", "context separation constraint" }));
        jComboBoxSeparationType.setName("jComboBoxSeparationType"); // NOI18N
        jComboBoxSeparationType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxSeparationTypeItemStateChanged(evt);
            }
        });

        jComboBoxFirstEntity.setName("jComboBoxFirstEntity"); // NOI18N
        jComboBoxFirstEntity.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxFirstEntityItemStateChanged(evt);
            }
        });

        jComboBoxSecondEntity.setName("jComboBoxSecondEntity"); // NOI18N
        jComboBoxSecondEntity.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxSecondEntityItemStateChanged(evt);
            }
        });

        jComboBoxFirstOrganization.setName("jComboBoxFirstOrganization"); // NOI18N

        jComboBoxSecondOrganization.setName("jComboBoxSecondOrganization"); // NOI18N

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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelConstraintType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxSeparationType, 0, 247, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelFirstEntity)
                        .addGap(91, 91, 91)
                        .addComponent(jComboBoxFirstEntity, 0, 247, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxFirstOrganization, 0, 247, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelSecondEntity)
                        .addGap(67, 67, 67)
                        .addComponent(jComboBoxSecondEntity, 0, 247, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxSecondOrganization, 0, 247, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                    .addComponent(jLabelConstraintType)
                    .addComponent(jComboBoxSeparationType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFirstEntity)
                    .addComponent(jComboBoxFirstEntity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxFirstOrganization, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSecondEntity)
                    .addComponent(jComboBoxSecondEntity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBoxSecondOrganization, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        canceled = false;
        // check if two different entities have been selected
        if ( (jComboBoxFirstEntity.getSelectedIndex() == jComboBoxSecondEntity.getSelectedIndex()) &&
             (jComboBoxFirstOrganization.getSelectedIndex() == jComboBoxSecondOrganization.getSelectedIndex()) )
        {
            JOptionPane.showMessageDialog(this, "You must select two different entities, or two different organizations if the same entity is selected");
            return;
        }
        dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        canceled = true;
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jComboBoxSeparationTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxSeparationTypeItemStateChanged
        // modify combo boxes content depending on the selected constraint type
        jComboBoxFirstEntity.removeAllItems();
        jComboBoxSecondEntity.removeAllItems();
        try
        {
            switch ( jComboBoxSeparationType.getSelectedIndex() )
            {
                case 0:// role
                    for ( String r : thisContext.thePolicy.GetRolesList(!thisContext.adorbacViewActive) )
                    {
                        jComboBoxFirstEntity.addItem(r);
                        jComboBoxSecondEntity.addItem(r);
                    }
                    break;
                case 1:// activity
                    for ( String r : thisContext.thePolicy.GetActivitiesList(!thisContext.adorbacViewActive) )
                    {
                        jComboBoxFirstEntity.addItem(r);
                        jComboBoxSecondEntity.addItem(r);
                    }
                    break;
                case 2:// view
                    for ( String r : thisContext.thePolicy.GetViewsList(!thisContext.adorbacViewActive) )
                    {
                        jComboBoxFirstEntity.addItem(r);
                        jComboBoxSecondEntity.addItem(r);
                    }
                    break;
                case 3:// context
                    Map<String, CContext> ctxs = thisContext.thePolicy.GetContexts();
                    for ( Entry<String, CContext> e : ctxs.entrySet() )
                    {
                        jComboBoxFirstEntity.addItem(e.getKey());
                        jComboBoxSecondEntity.addItem(e.getKey());
                    }
                    break;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jComboBoxSeparationTypeItemStateChanged

    private void jComboBoxFirstEntityItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxFirstEntityItemStateChanged
        // display organizations in which the entity is defined
        jComboBoxFirstOrganization.removeAllItems();
        try
        {
            switch ( jComboBoxSeparationType.getSelectedIndex() )
            {
                case 0:// role
                    for ( String o : thisContext.thePolicy.GetRoleAssociatedOrganizations( (String)jComboBoxFirstEntity.getSelectedItem() ) )
                        jComboBoxFirstOrganization.addItem(o);
                    break;
                case 1:// activity
                    for ( String o : thisContext.thePolicy.GetActivityAssociatedOrganizations( (String)jComboBoxFirstEntity.getSelectedItem() ) )
                        jComboBoxFirstOrganization.addItem(o);
                    break;
                case 2:// view
                    for ( String o : thisContext.thePolicy.GetViewAssociatedOrganizations( (String)jComboBoxFirstEntity.getSelectedItem() ) )
                        jComboBoxFirstOrganization.addItem(o);
                    break;
                case 3:// context
                    for ( String o : thisContext.thePolicy.GetOrganizationsList(thisContext.adorbacViewActive) )
                        jComboBoxFirstOrganization.addItem(o);
                    break;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jComboBoxFirstEntityItemStateChanged

    private void jComboBoxSecondEntityItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxSecondEntityItemStateChanged
        // display organizations in which the entity is defined
        jComboBoxSecondOrganization.removeAllItems();
        try
        {
            switch ( jComboBoxSeparationType.getSelectedIndex() )
            {
                case 0:// role
                    for ( String o : thisContext.thePolicy.GetRoleAssociatedOrganizations( (String)jComboBoxSecondEntity.getSelectedItem() ) )
                        jComboBoxSecondOrganization.addItem(o);
                    break;
                case 1:// activity
                    for ( String o : thisContext.thePolicy.GetActivityAssociatedOrganizations( (String)jComboBoxSecondEntity.getSelectedItem() ) )
                        jComboBoxSecondOrganization.addItem(o);
                    break;
                case 2:// view
                    for ( String o : thisContext.thePolicy.GetViewAssociatedOrganizations( (String)jComboBoxSecondEntity.getSelectedItem() ) )
                        jComboBoxSecondOrganization.addItem(o);
                    break;
                case 3:// context
                    for ( String o : thisContext.thePolicy.GetOrganizationsList(thisContext.adorbacViewActive) )
                        jComboBoxSecondOrganization.addItem(o);
                    break;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jComboBoxSecondEntityItemStateChanged

    public int GetSeparationType()
    {
        return jComboBoxSeparationType.getSelectedIndex();
    }
    public String GetFirstEntity()
    {
        return (String)jComboBoxFirstEntity.getSelectedItem();
    }
    public String GetSecondEntity()
    {
        return (String)jComboBoxSecondEntity.getSelectedItem();
    }
    public String GetFirstOrganization()
    {
        return (String)jComboBoxFirstOrganization.getSelectedItem();
    }
    public String GetSecondOrganization()
    {
        return (String)jComboBoxSecondOrganization.getSelectedItem();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JComboBox jComboBoxFirstEntity;
    private javax.swing.JComboBox jComboBoxFirstOrganization;
    private javax.swing.JComboBox jComboBoxSecondEntity;
    private javax.swing.JComboBox jComboBoxSecondOrganization;
    private javax.swing.JComboBox jComboBoxSeparationType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelConstraintType;
    private javax.swing.JLabel jLabelFirstEntity;
    private javax.swing.JLabel jLabelSecondEntity;
    // End of variables declaration//GEN-END:variables

}