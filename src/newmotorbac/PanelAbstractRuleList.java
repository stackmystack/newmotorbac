/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanelAbstractRules.java
 *
 * Created on May 5, 2011, 5:25:13 PM
 */

package newmotorbac;

import newmotorbac.util.OrbacPolicyContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import newmotorbac.dialog.jDialogCreateAbstractRule;
import orbac.AbstractOrbacPolicy;
import orbac.exception.COrbacException;
import orbac.securityRules.CAbstractRule;

/**
 *
 * @author fabien
 */
public class PanelAbstractRuleList extends javax.swing.JPanel {
    // policy context
    OrbacPolicyContext thisContext;
    int ruleType;
    // renderer
    private AbstractRulesTableCellRenderer inheritedRulesRenderer = new AbstractRulesTableCellRenderer();

    String[] rulesTypesStr = {"Permissions", "Prohibitions", "Obligations"};

    /** Creates new form PanelAbstractRules */
    public PanelAbstractRuleList(OrbacPolicyContext thisContext, int ruleType) {
        initComponents();

        // store context
        this.thisContext = thisContext;
        this.ruleType = ruleType;

        // set table state variables to enable row selection
        jTableRules.setCellSelectionEnabled(false);
        jTableRules.setRowSelectionAllowed(true);
        jTableRules.setColumnSelectionAllowed(false);
        // set table renderer
        jTableRules.setDefaultRenderer(Object.class, inheritedRulesRenderer);
        
        // no rule is selected
        jButtonAddRule.setEnabled(false);
        jButtonDeleteRule.setEnabled(false);
        jButtonEditRule.setEnabled(false);
        
        // update rules
        UpdateRules();
    }

    public void UpdateRules()
    {
        if ( thisContext.thePolicy == null )
            return;
        try
        {
            DefaultTableModel model = (DefaultTableModel)jTableRules.getModel();
            model.setRowCount(0);

            // if the current organization is null, it means we display all rules
            if ( thisContext.currentOrganization != null )
            {
                // compute set of organization for which rules must be retrieved
                Set<String> superOrgs = new HashSet<String>();
                if ( jCheckBoxHideInheritedRules.isSelected() == false)
                    superOrgs.addAll( thisContext.thePolicy.GetSuperOrganizations(thisContext.currentOrganization) );
                superOrgs.add(thisContext.currentOrganization);

                // get rules
                Iterator<String> iso = superOrgs.iterator();
                Vector<CAbstractRule> allRules = new Vector<CAbstractRule>();
                while ( iso.hasNext() )
                {
                    String currOrg = (String)iso.next();
                    switch (ruleType)
                    {
                        case AbstractOrbacPolicy.TYPE_PERMISSION:
                            if ( thisContext.adorbacViewActive )
                                allRules.addAll( thisContext.thePolicy.GetAdorbacAbstractPermissions(currOrg) );
                            else
                                allRules.addAll( thisContext.thePolicy.GetAbstractPermissions(currOrg) );
                            break;
                        case AbstractOrbacPolicy.TYPE_PROHIBITION:
                            if ( !thisContext.adorbacViewActive )
                                allRules.addAll( thisContext.thePolicy.GetAbstractProhibitions(currOrg) );
                            break;
                        case AbstractOrbacPolicy.TYPE_OBLIGATION:
                            if ( !thisContext.adorbacViewActive )
                                allRules.addAll( thisContext.thePolicy.GetAbstractObligations(currOrg) );
                            break;
                    }
                }

                Iterator<CAbstractRule> iar = allRules.iterator();
                // display number of rules in tab name
                //JTabbedPane parent = (JTabbedPane)getParent();
                //parent.setTitleAt(0, rulesTypesStr[ruleType] + " (" + allRules.size() + ")");
                while ( iar.hasNext() )
                {
                    CAbstractRule r = iar.next();
                    // fill table
                    String row[] = new String[6];
                    row[0] = r.GetName();
                    row[1] = r.GetRole();
                    row[2] = r.GetActivity();
                    row[3] = r.GetView();
                    row[4] = r.GetContext();
                    row[5] = r.GetViolationContext() == null ? "" : r.GetViolationContext();
                    model.addRow(row);
                }
            }
            else
            {
                // get rules for all organizations
                HashSet<CAbstractRule> rules = new HashSet<CAbstractRule>();
                switch (ruleType)
                {
                    case AbstractOrbacPolicy.TYPE_PERMISSION:
                        if ( thisContext.adorbacViewActive )
                            rules = thisContext.thePolicy.GetAdorbacAbstractPermissions();
                        else
                            rules = thisContext.thePolicy.GetAbstractPermissions();
                        break;
                    case AbstractOrbacPolicy.TYPE_PROHIBITION:
                        if ( !thisContext.adorbacViewActive )
                            rules = thisContext.thePolicy.GetAbstractProhibitions();
                        break;
                    case AbstractOrbacPolicy.TYPE_OBLIGATION:
                        if ( !thisContext.adorbacViewActive )
                            rules = thisContext.thePolicy.GetAbstractObligations();
                        break;
                }

                Iterator<CAbstractRule> iar = rules.iterator();
                // display number of rules in tab name
                //JTabbedPane parent = (JTabbedPane)getParent();
                //parent.setTitleAt(0, "All " + rulesTypesStr[ruleType] + " (" + rules.size() + ")");
                while ( iar.hasNext() )
                {
                    CAbstractRule r = iar.next();
                    // fill table
                    String row[] = new String[6];
                    row[0] = r.GetName();
                    row[1] = r.GetRole();
                    row[2] = r.GetActivity();
                    row[3] = r.GetView();
                    row[4] = r.GetContext();
                    row[5] = r.GetViolationContext() == null ? "" : r.GetViolationContext();
                    model.addRow(row);
                }
            }

            // disable buttons if no rules are shown
            if ( model.getRowCount() == 0 )
            {
                jButtonDeleteRule.setEnabled(false);
                jButtonEditRule.setEnabled(false);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
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

        jButtonAddRule = new javax.swing.JButton();
        jButtonDeleteRule = new javax.swing.JButton();
        jButtonEditRule = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRules = new javax.swing.JTable();
        jCheckBoxHideInheritedRules = new javax.swing.JCheckBox();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(100, 132));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(PanelAbstractRuleList.class);
        jButtonAddRule.setText(resourceMap.getString("jButtonAddRule.text")); // NOI18N
        jButtonAddRule.setName("jButtonAddRule"); // NOI18N
        jButtonAddRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddRuleActionPerformed(evt);
            }
        });

        jButtonDeleteRule.setText(resourceMap.getString("jButtonDeleteRule.text")); // NOI18N
        jButtonDeleteRule.setName("jButtonDeleteRule"); // NOI18N
        jButtonDeleteRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteRuleActionPerformed(evt);
            }
        });

        jButtonEditRule.setText(resourceMap.getString("jButtonEditRule.text")); // NOI18N
        jButtonEditRule.setName("jButtonEditRule"); // NOI18N
        jButtonEditRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditRuleActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableRules.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rule name", "Role", "Activity", "View", "Context", "Violation context"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableRules.setColumnSelectionAllowed(true);
        jTableRules.setName("jTableRules"); // NOI18N
        jTableRules.getTableHeader().setReorderingAllowed(false);
        jTableRules.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRulesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableRules);
        jTableRules.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableRules.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableRules.columnModel.title0")); // NOI18N
        jTableRules.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTableRules.columnModel.title1")); // NOI18N
        jTableRules.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTableRules.columnModel.title2")); // NOI18N
        jTableRules.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTableRules.columnModel.title3")); // NOI18N
        jTableRules.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jTableRules.columnModel.title4")); // NOI18N
        jTableRules.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("jTableRules.columnModel.title5")); // NOI18N

        jCheckBoxHideInheritedRules.setSelected(true);
        jCheckBoxHideInheritedRules.setText(resourceMap.getString("jCheckBoxHideInheritedRules.text")); // NOI18N
        jCheckBoxHideInheritedRules.setName("jCheckBoxHideInheritedRules"); // NOI18N
        jCheckBoxHideInheritedRules.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxHideInheritedRulesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonAddRule)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDeleteRule)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEditRule)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxHideInheritedRules)
                .addContainerGap(39, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAddRule, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDeleteRule, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonEditRule, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxHideInheritedRules))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxHideInheritedRulesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxHideInheritedRulesActionPerformed
        UpdateRules();
    }//GEN-LAST:event_jCheckBoxHideInheritedRulesActionPerformed

    private void jButtonAddRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddRuleActionPerformed
        // action depends on tab type
        switch (ruleType)
        {
            case AbstractOrbacPolicy.TYPE_PERMISSION:
                AddAbstractPermission();
                break;
            case AbstractOrbacPolicy.TYPE_PROHIBITION:
                AddAbstractProhibition();
                break;
            case AbstractOrbacPolicy.TYPE_OBLIGATION:
                AddAbstractObligation();
                break;
        }
    }//GEN-LAST:event_jButtonAddRuleActionPerformed

    private void jButtonDeleteRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteRuleActionPerformed
        // get rule parameters and delete it
        DefaultTableModel model = (DefaultTableModel)jTableRules.getModel();
        int row = jTableRules.getSelectedRow();
        if ( row == -1 ) return;
        String name = (String)model.getValueAt(row, 0);
        String role = (String)model.getValueAt(row, 1);
        String activity = (String)model.getValueAt(row, 2);
        String view = (String)model.getValueAt(row, 3);
        String context = (String)model.getValueAt(row, 4);
        String violationContext = (String)model.getValueAt(row, 5);
        try
        {
            // action depends on tab type
            switch (ruleType)
            {
                case AbstractOrbacPolicy.TYPE_PERMISSION:
                    if ( thisContext.thePolicy.IsAdorbacEnabled() )
                        thisContext.thePolicy.RemoveAdorbacLicense(name, thisContext.currentOrganization);
                    else
                        thisContext.thePolicy.DeleteAbstractPermission(name, thisContext.currentOrganization, role, activity, view, context);
                    break;
                case AbstractOrbacPolicy.TYPE_PROHIBITION:
                    thisContext.thePolicy.DeleteAbstractProhibition(name, thisContext.currentOrganization, role, activity, view, context);
                    break;
                case AbstractOrbacPolicy.TYPE_OBLIGATION:
                    thisContext.thePolicy.DeleteAbstractObligation(name, thisContext.currentOrganization, role, activity, view, context, violationContext);
                    break;
            }

            jButtonDeleteRule.setEnabled(false);
            jButtonEditRule.setEnabled(false);

            // refresh abstract rule panel
            UpdateRules();
            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
        }
        catch ( COrbacException e )
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonDeleteRuleActionPerformed

    private void jButtonEditRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditRuleActionPerformed
        // get selected rule parameters
        int row = jTableRules.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel)jTableRules.getModel();
        // if no selection return
        if ( row == -1 ) return;
        String name = (String)model.getValueAt(row, 0);
        String role = (String)model.getValueAt(row, 1);
        String activity = (String)model.getValueAt(row, 2);
        String view = (String)model.getValueAt(row, 3);
        String context = (String)model.getValueAt(row, 4);
        String violationContext = (String)model.getValueAt(row, 5);

        // open dialog box
        jDialogCreateAbstractRule editRule = new jDialogCreateAbstractRule(findActiveFrame(), true,
                                                                             thisContext.currentOrganization,
                                                                             role, activity, view, context, violationContext, name,
                                                                             thisContext.thePolicy,
                                                                             !thisContext.adorbacViewActive,
                                                                             ruleType);
        editRule.setLocationRelativeTo(findActiveFrame());
        NewMotorbacApp.getApplication().show(editRule);

        if ( editRule.canceled )
            return;

        try
        {
            boolean modifiedRule = false;
            // modify the rule if necessary
            if ( name.equals(editRule.GetRuleName()) == false ||
                 role.equals(editRule.GetFirstEntity()) == false ||
                 activity.equals(editRule.GetSecondEntity()) == false ||
                 view.equals(editRule.GetThirdEntity()) == false ||
                 context.equals(editRule.GetContext()) == false )
                modifiedRule = true;
            // only valid for obligations
            if ( ruleType == AbstractOrbacPolicy.TYPE_OBLIGATION &&
                 violationContext.equals(editRule.GetViolationContext()) == false)
                modifiedRule = true;

            if ( modifiedRule )
            {
                CAbstractRule oldRule, newRule;
                // modify the rule
                switch (ruleType)
                {
                    case AbstractOrbacPolicy.TYPE_PERMISSION:
                        oldRule = thisContext.thePolicy.GetAbstractPermission(name);
                        newRule = new CAbstractRule(thisContext.currentOrganization, 
                                                    editRule.GetFirstEntity(), 
                                                    editRule.GetSecondEntity(),
                                                    editRule.GetThirdEntity(), 
                                                    editRule.GetRuleName(), 
                                                    editRule.GetContext(), 
                                                    ruleType, 
                                                    thisContext.thePolicy);
                        thisContext.thePolicy.ModifyAbstractPermission(oldRule, newRule);
                        break;
                    case AbstractOrbacPolicy.TYPE_PROHIBITION:
                        oldRule = thisContext.thePolicy.GetAbstractProhibition(name);
                        newRule = new CAbstractRule(thisContext.currentOrganization, 
                                                    editRule.GetFirstEntity(), 
                                                    editRule.GetSecondEntity(), 
                                                    editRule.GetThirdEntity(), 
                                                    editRule.GetRuleName(), 
                                                    editRule.GetContext(), 
                                                    ruleType, 
                                                    thisContext.thePolicy);
                        thisContext.thePolicy.ModifyAbstractProhibition(oldRule, newRule);
                        break;
                    case AbstractOrbacPolicy.TYPE_OBLIGATION:
                        oldRule = thisContext.thePolicy.GetAbstractObligation(name);
                        newRule = new CAbstractRule(thisContext.currentOrganization, 
                                                    editRule.GetFirstEntity(), 
                                                    editRule.GetSecondEntity(), 
                                                    editRule.GetThirdEntity(), 
                                                    editRule.GetRuleName(), 
                                                    editRule.GetContext(), 
                                                    editRule.GetViolationContext(), 
                                                    oldRule.GetObligator(), 
                                                    ruleType, 
                                                    thisContext.thePolicy);
                        thisContext.thePolicy.ModifyAbstractObligation(oldRule, newRule);
                        break;
                }

                // refresh abstract rule panel
                UpdateRules();
                // push policy on undo/redo stack
                thisContext.panelPolicy.PushPolicy();
            }
        }
        catch (COrbacException e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
        }
    }//GEN-LAST:event_jButtonEditRuleActionPerformed

    private void jTableRulesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRulesMouseClicked
        // get selected row
        DefaultTableModel model = (DefaultTableModel)jTableRules.getModel();
        Point p = evt.getPoint();
        int row = jTableRules.rowAtPoint(p);
        // get rule name at row
        String ruleName = (String)model.getValueAt(row, 0);

        // set buttons state according to table selection
        // inherited rules cannot be edited
        jButtonDeleteRule.setEnabled( !InheritedRule(ruleName, thisContext.currentOrganization) );
        jButtonEditRule.setEnabled( !InheritedRule(ruleName, thisContext.currentOrganization) );
    }//GEN-LAST:event_jTableRulesMouseClicked

    // return true if the given rule is inherited in the given organization
    public boolean InheritedRule(String ruleName, String organization)
    {
        boolean result = false;
        try
        {
            // rule name
            CAbstractRule rule = null;
            switch (ruleType)
            {
                case AbstractOrbacPolicy.TYPE_PERMISSION:
                    rule = thisContext.thePolicy.GetAbstractPermission(ruleName);
                    break;
                case AbstractOrbacPolicy.TYPE_PROHIBITION:
                    rule = thisContext.thePolicy.GetAbstractProhibition(ruleName);
                    break;
                case AbstractOrbacPolicy.TYPE_OBLIGATION:
                    rule = thisContext.thePolicy.GetAbstractObligation(ruleName);
                    break;
            }
            result = !rule.GetOrganization().equals(thisContext.currentOrganization);
        }
        catch (COrbacException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
        return result;
    }

    // called when the current organization has changed
    public void SelectedOrganizationHasChanged()
    {
        // if no organization is selected, we disable the buttons
        jButtonAddRule.setEnabled( thisContext.currentOrganization != null );
    }

    // public methods to add abstract rules, used by the drag and drop code
    public void AddAbstractPermission()
    {
        // open dialog box
        jDialogCreateAbstractRule createRule = new jDialogCreateAbstractRule(findActiveFrame(), true,
                                                                             thisContext.currentOrganization,
                                                                             null, null, null,
                                                                             thisContext.thePolicy,
                                                                             !thisContext.adorbacViewActive,
                                                                             AbstractOrbacPolicy.TYPE_PERMISSION);
        createRule.setLocationRelativeTo(findActiveFrame());
        NewMotorbacApp.getApplication().show(createRule);

        if ( createRule.canceled )
            return;

        try
        {
            // create the rule
            thisContext.thePolicy.AbstractPermission(thisContext.currentOrganization,
                                                     createRule.GetFirstEntity(),
                                                     createRule.GetSecondEntity(),
                                                     createRule.GetThirdEntity(),
                                                     createRule.GetContext(),
                                                     createRule.GetRuleName());

            // refresh abstract rule panel
            UpdateRules();
            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
        }
        catch (COrbacException e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
        }
    }
    public void AddAbstractProhibition()
    {
        // open dialog box
        jDialogCreateAbstractRule createRule = new jDialogCreateAbstractRule(findActiveFrame(), true,
                                                                             thisContext.currentOrganization,
                                                                             null, null, null,
                                                                             thisContext.thePolicy,
                                                                             !thisContext.adorbacViewActive,
                                                                             AbstractOrbacPolicy.TYPE_PROHIBITION);
        createRule.setLocationRelativeTo(findActiveFrame());
        NewMotorbacApp.getApplication().show(createRule);

        if ( createRule.canceled )
            return;

        try
        {
            // create the rule
            thisContext.thePolicy.AbstractProhibition(thisContext.currentOrganization,
                                                     createRule.GetFirstEntity(),
                                                     createRule.GetSecondEntity(),
                                                     createRule.GetThirdEntity(),
                                                     createRule.GetContext(),
                                                     createRule.GetRuleName());

            // refresh abstract rule panel
            UpdateRules();
            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
        }
        catch (COrbacException e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
        }
    }
    public void AddAbstractObligation()
    {
        // open dialog box
        jDialogCreateAbstractRule createRule = new jDialogCreateAbstractRule(findActiveFrame(), true,
                                                                             thisContext.currentOrganization,
                                                                             null, null, null,
                                                                             thisContext.thePolicy,
                                                                             !thisContext.adorbacViewActive,
                                                                             AbstractOrbacPolicy.TYPE_OBLIGATION);
        createRule.setLocationRelativeTo(findActiveFrame());
        NewMotorbacApp.getApplication().show(createRule);

        if ( createRule.canceled )
            return;

        try
        {
            // create the rule
            thisContext.thePolicy.AbstractObligation(thisContext.currentOrganization,
                                                     createRule.GetFirstEntity(),
                                                     createRule.GetSecondEntity(),
                                                     createRule.GetThirdEntity(),
                                                     createRule.GetContext(),
                                                     createRule.GetViolationContext(),
                                                     createRule.GetRuleName());

            // refresh abstract rule panel
            UpdateRules();
            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
        }
        catch (COrbacException e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddRule;
    private javax.swing.JButton jButtonDeleteRule;
    private javax.swing.JButton jButtonEditRule;
    private javax.swing.JCheckBox jCheckBoxHideInheritedRules;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableRules;
    // End of variables declaration//GEN-END:variables

    // helper function to find a frame object to display a dialog box for example
    private Frame findActiveFrame()
    {
        Frame[] frames = JFrame.getFrames();
        for (int i = 0; i < frames.length; i++)
        {
            Frame frame = frames[i];
            if (frame.isVisible())
            {
                return frame;
            }
        }
        return null;
    }
    
    // custom renderer to differenciate inherited abstract rules
    public class AbstractRulesTableCellRenderer extends DefaultTableCellRenderer
    {
    	// just to remove the warning
    	static final long serialVersionUID = 0;
        private Color inheritedColor = new Color(0.9f, 0.9f, 1.0f);

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // based on the rule name, check if the currently renderered rule has been defined in the currently selected organization
            if ( column == 0 )
            {
                try
                {
                    // rule name
                    String ruleName = (String)value;
                    CAbstractRule rule = null;
                    switch (ruleType)
                    {
                        case AbstractOrbacPolicy.TYPE_PERMISSION:
                            rule = thisContext.thePolicy.GetAbstractPermission(ruleName);
                            break;
                        case AbstractOrbacPolicy.TYPE_PROHIBITION:
                            rule = thisContext.thePolicy.GetAbstractProhibition(ruleName);
                            break;
                        case AbstractOrbacPolicy.TYPE_OBLIGATION:
                            rule = thisContext.thePolicy.GetAbstractObligation(ruleName);
                            break;
                    }
                    if ( rule.GetOrganization().equals(thisContext.currentOrganization) == false )
                        cell.setBackground( inheritedColor );
                    else cell.setBackground( Color.white );
                }
                catch (COrbacException e)
                {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
            return cell;
        }
    }
}
