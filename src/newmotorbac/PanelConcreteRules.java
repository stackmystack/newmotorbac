/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanelConcreteRules.java
 *
 * Created on May 9, 2011, 10:55:10 AM
 */

package newmotorbac;

import newmotorbac.util.OrbacPolicyContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import orbac.exception.COrbacException;
import orbac.securityRules.CAdorbacConcretePermission;
import orbac.securityRules.CConcreteObligation;
import orbac.securityRules.CConcretePermission;
import orbac.securityRules.CConcreteProhibition;
import orbac.securityRules.CConcreteRule;

/**
 *
 * @author fabien
 */
public class PanelConcreteRules extends javax.swing.JPanel {
    // policy context
    private OrbacPolicyContext thisContext;
    // the custom table model instance
    private SecurityRulesTableModel securityRulesModel = new SecurityRulesTableModel() ;
    // table renderer
    SecurityRulesTableCellRenderer ruleListRenderer = new SecurityRulesTableCellRenderer();
    // icons used by the table renderer
    private ImageIcon imgActiveRule;
    private ImageIcon imgInactiveRule;
    private ImageIcon imgPreemptedRule;
    private ImageIcon imgFulfilled;
    private ImageIcon imgViolated;
    private ImageIcon imgError;

    /** Creates new form PanelConcreteRules */
    public PanelConcreteRules(OrbacPolicyContext thisContext) {
        initComponents();

        // store policy context
        this.thisContext = thisContext;

        // setup rule table
        jTableConcreteRules.setModel(securityRulesModel);
        jTableConcreteRules.setDefaultRenderer(Object.class, ruleListRenderer);

        // set table state variables to enable row selection
        jTableConcreteRules.setCellSelectionEnabled(false);
        jTableConcreteRules.setRowSelectionAllowed(true);
        jTableConcreteRules.setColumnSelectionAllowed(false);

        // create icons;
        URL url = NewMotorbacView.class.getResource("/newmotorbac/resources/active_rule.png");
        if( url != null )
            imgActiveRule = new ImageIcon(url);
        url = NewMotorbacView.class.getResource("/newmotorbac/resources/inactive_rule.png");
        if( url != null )
            imgInactiveRule = new ImageIcon(url);
        url = NewMotorbacView.class.getResource("/newmotorbac/resources/masked_rule.png");
        if( url != null )
            imgPreemptedRule = new ImageIcon(url);
        url = NewMotorbacView.class.getResource("/newmotorbac/resources/satisfied_obligation.png");
        if( url != null )
            imgFulfilled = new ImageIcon(url);
        url = NewMotorbacView.class.getResource("/newmotorbac/resources/violated_obligation.png");
        if( url != null )
            imgViolated = new ImageIcon(url);
        url = NewMotorbacView.class.getResource("/newmotorbac/resources/error.png");
        if( url != null )
            imgError = new ImageIcon(url);

        jCheckBoxPermissions.setSelected(true);
        jCheckBoxProhibitions.setSelected(true);
        jCheckBoxObligations.setSelected(true);

        // hide/show some controls depending on policy implementation
        jCheckBoxPermissions.setVisible(thisContext.thePolicy.IsPermissionImplemented());
        jCheckBoxProhibitions.setVisible(thisContext.thePolicy.IsProhibitionImplemented());
        jCheckBoxObligations.setVisible(thisContext.thePolicy.IsObligationImplemented());
        jCheckBoxAdorbacPermissions.setVisible(thisContext.thePolicy.IsAdorbacImplemented());
    }

    public void FillSecurityRulesTable()
    {
        Set<CConcretePermission> permissions = thisContext.thePolicy.GetConcretePermissions();
        Set<CConcreteProhibition> prohibitions = thisContext.thePolicy.GetConcreteProhibitions();
        Set<CConcreteObligation> obligations = thisContext.thePolicy.GetConcreteObligations();
        Set<CAdorbacConcretePermission> adorbacPermissions = null;
        try
        {
            adorbacPermissions = thisContext.thePolicy.GetAdorbacConcretePermissions();
        } 
        catch (COrbacException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // filters
        boolean filterSubjects, filterActions, filterObjects;
        // filter subjects
        filterSubjects = !jTextFieldSubjectFilter.getText().equals("");
        filterActions = !jTextFieldActionFilter.getText().equals("");
        filterObjects = !jTextFieldObjectFilter.getText().equals("");
        String sf = jTextFieldSubjectFilter.getText();
        String af = jTextFieldActionFilter.getText();
        String of = jTextFieldObjectFilter.getText();

        // process permissions
        Vector<CConcretePermission> tempPerm = new Vector<CConcretePermission>();
        Iterator<CConcretePermission> ipe = permissions.iterator();
        while ( ipe.hasNext() )
        {
            CConcretePermission perm = ipe.next();
            if ( filterSubjects || filterActions || filterObjects )
            {
                // check filters
                if ( filterSubjects && perm.GetSubject().contains(sf) == false ) continue;
                if ( filterActions && perm.GetAction().contains(af) == false ) continue;
                if ( filterObjects && perm.GetObject().contains(of) == false ) continue;
            }
            tempPerm.add(perm);
        }

        // process prohibitions
        Vector<CConcreteProhibition> tempProhib = new Vector<CConcreteProhibition>();
        Iterator<CConcreteProhibition> ipr = prohibitions.iterator();
        while ( ipr.hasNext() )
        {
            CConcreteProhibition prohib = ipr.next();
            if ( filterSubjects || filterActions || filterObjects )
            {
                // check filters
                if ( filterSubjects && prohib.GetSubject().contains(sf) == false ) continue;
                if ( filterActions && prohib.GetAction().contains(af) == false ) continue;
                if ( filterObjects && prohib.GetObject().contains(of) == false ) continue;
            }
            tempProhib.add(prohib);
        }

        // process obligations
        Vector<CConcreteObligation> tempOblig = new Vector<CConcreteObligation>();
        Iterator<CConcreteObligation> ipo = obligations.iterator();
        while ( ipo.hasNext() )
        {
            CConcreteObligation oblig = ipo.next();
            if ( filterSubjects || filterActions || filterObjects )
            {
                // check filters
                if ( filterSubjects && oblig.GetSubject().contains(sf) == false ) continue;
                if ( filterActions && oblig.GetAction().contains(af) == false ) continue;
                if ( filterObjects && oblig.GetObject().contains(of) == false ) continue;
            }
            tempOblig.add(oblig);
        }

        // process adorbac permissions
        Vector<CAdorbacConcretePermission> tempAdorbacPerm = new Vector<CAdorbacConcretePermission>();
        Iterator<CAdorbacConcretePermission> iape = adorbacPermissions.iterator();
        while ( iape.hasNext() )
        {
            CAdorbacConcretePermission perm = iape.next();
            if ( filterSubjects || filterActions || filterObjects )
            {
                // check filters
                if ( filterSubjects && perm.GetSubject().contains(sf) == false ) continue;
                if ( filterActions && perm.GetAction().contains(af) == false ) continue;
                if ( filterObjects && perm.GetObject().contains(of) == false ) continue;
            }
            tempAdorbacPerm.add(perm);
        }

        CConcretePermission[] permArray = new CConcretePermission[ tempPerm.size() ];
        CConcreteProhibition[] prohibArray = new CConcreteProhibition[ tempProhib.size() ];
        CConcreteObligation[] obligArray = new CConcreteObligation[ tempOblig.size() ];
        CAdorbacConcretePermission[] adorbacPermArray = new CAdorbacConcretePermission[ tempAdorbacPerm.size() ];

        int i = 0;
        Iterator<CConcretePermission> ipee = tempPerm.iterator();
        while ( ipee.hasNext() ) {
            permArray[i] = ipee.next();
            i++;
        }
        i = 0;
        Iterator<CConcreteProhibition> iprr = tempProhib.iterator();
        while ( iprr.hasNext() ) {
            prohibArray[i] = iprr.next();
            i++;
        }
        i = 0;
        Iterator<CConcreteObligation> io = tempOblig.iterator();
        while ( io.hasNext() ) {
            obligArray[i] = io.next();
            i++;
        }
        i = 0;
        Iterator<CAdorbacConcretePermission> iapee = tempAdorbacPerm.iterator();
        while ( iapee.hasNext() ) {
            adorbacPermArray[i] = iapee.next();
            i++;
        }
        securityRulesModel.SetData( permArray, prohibArray, obligArray, adorbacPermArray );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jCheckBoxPermissions = new javax.swing.JCheckBox();
        jCheckBoxProhibitions = new javax.swing.JCheckBox();
        jCheckBoxObligations = new javax.swing.JCheckBox();
        jCheckBoxAdorbacPermissions = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldSubjectFilter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldObjectFilter = new javax.swing.JTextField();
        jButtonFilterRules = new javax.swing.JButton();
        jTextFieldActionFilter = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableConcreteRules = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableConcreteRuleInfo = new javax.swing.JTable();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(500, 481));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(PanelConcreteRules.class);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jCheckBoxPermissions.setText(resourceMap.getString("jCheckBoxPermissions.text")); // NOI18N
        jCheckBoxPermissions.setName("jCheckBoxPermissions"); // NOI18N
        jCheckBoxPermissions.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxPermissionsStateChanged(evt);
            }
        });

        jCheckBoxProhibitions.setText(resourceMap.getString("jCheckBoxProhibitions.text")); // NOI18N
        jCheckBoxProhibitions.setName("jCheckBoxProhibitions"); // NOI18N
        jCheckBoxProhibitions.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxProhibitionsStateChanged(evt);
            }
        });

        jCheckBoxObligations.setText(resourceMap.getString("jCheckBoxObligations.text")); // NOI18N
        jCheckBoxObligations.setName("jCheckBoxObligations"); // NOI18N
        jCheckBoxObligations.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxObligationsStateChanged(evt);
            }
        });

        jCheckBoxAdorbacPermissions.setText(resourceMap.getString("jCheckBoxAdorbacPermissions.text")); // NOI18N
        jCheckBoxAdorbacPermissions.setName("jCheckBoxAdorbacPermissions"); // NOI18N
        jCheckBoxAdorbacPermissions.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxAdorbacPermissionsStateChanged(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextFieldSubjectFilter.setText(resourceMap.getString("jTextFieldSubjectFilter.text")); // NOI18N
        jTextFieldSubjectFilter.setMaximumSize(new java.awt.Dimension(155, 2147483647));
        jTextFieldSubjectFilter.setName("jTextFieldSubjectFilter"); // NOI18N
        jTextFieldSubjectFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldSubjectFilterKeyTyped(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jTextFieldObjectFilter.setText(resourceMap.getString("jTextFieldObjectFilter.text")); // NOI18N
        jTextFieldObjectFilter.setMaximumSize(new java.awt.Dimension(155, 2147483647));
        jTextFieldObjectFilter.setName("jTextFieldObjectFilter"); // NOI18N
        jTextFieldObjectFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldObjectFilterKeyTyped(evt);
            }
        });

        jButtonFilterRules.setIcon(resourceMap.getIcon("jButtonFilterRules.icon")); // NOI18N
        jButtonFilterRules.setText(resourceMap.getString("jButtonFilterRules.text")); // NOI18N
        jButtonFilterRules.setName("jButtonFilterRules"); // NOI18N
        jButtonFilterRules.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFilterRulesActionPerformed(evt);
            }
        });

        jTextFieldActionFilter.setMaximumSize(new java.awt.Dimension(155, 2147483647));
        jTextFieldActionFilter.setName("jTextFieldActionFilter"); // NOI18N
        jTextFieldActionFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldActionFilterKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSubjectFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldActionFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldObjectFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonFilterRules, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBoxPermissions)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxProhibitions)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxObligations)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxAdorbacPermissions)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxPermissions)
                    .addComponent(jCheckBoxProhibitions)
                    .addComponent(jCheckBoxObligations)
                    .addComponent(jCheckBoxAdorbacPermissions))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldSubjectFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldActionFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldObjectFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFilterRules, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableConcreteRules.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "State", "Subject", "Action", "Object"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableConcreteRules.setColumnSelectionAllowed(true);
        jTableConcreteRules.setName("jTableConcreteRules"); // NOI18N
        jTableConcreteRules.getTableHeader().setReorderingAllowed(false);
        jTableConcreteRules.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableConcreteRulesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableConcreteRules);
        jTableConcreteRules.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableConcreteRules.getColumnModel().getColumn(0).setResizable(false);
        jTableConcreteRules.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTableConcreteRules.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableConcreteRules.columnModel.title0")); // NOI18N
        jTableConcreteRules.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTableConcreteRules.columnModel.title1")); // NOI18N
        jTableConcreteRules.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTableConcreteRules.columnModel.title2")); // NOI18N
        jTableConcreteRules.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTableConcreteRules.columnModel.title3")); // NOI18N

        jSplitPane1.setTopComponent(jScrollPane1);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTableConcreteRuleInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "property", "value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableConcreteRuleInfo.setName("jTableConcreteRuleInfo"); // NOI18N
        jTableConcreteRuleInfo.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableConcreteRuleInfo);
        jTableConcreteRuleInfo.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableConcreteRuleInfo.columnModel.title0")); // NOI18N
        jTableConcreteRuleInfo.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTableConcreteRuleInfo.columnModel.title1")); // NOI18N

        jSplitPane1.setRightComponent(jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxPermissionsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxPermissionsStateChanged
        // update display
        FillSecurityRulesTable();
    }//GEN-LAST:event_jCheckBoxPermissionsStateChanged

    private void jCheckBoxProhibitionsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxProhibitionsStateChanged
        // update display
        FillSecurityRulesTable();
    }//GEN-LAST:event_jCheckBoxProhibitionsStateChanged

    private void jCheckBoxObligationsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxObligationsStateChanged
        // update display
        FillSecurityRulesTable();
    }//GEN-LAST:event_jCheckBoxObligationsStateChanged

    private void jCheckBoxAdorbacPermissionsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxAdorbacPermissionsStateChanged
        // update display
        FillSecurityRulesTable();
    }//GEN-LAST:event_jCheckBoxAdorbacPermissionsStateChanged

    private void jButtonFilterRulesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFilterRulesActionPerformed
        // update display
        FillSecurityRulesTable();
    }//GEN-LAST:event_jButtonFilterRulesActionPerformed

    private void jTextFieldSubjectFilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSubjectFilterKeyTyped
        // update display
        FillSecurityRulesTable();
    }//GEN-LAST:event_jTextFieldSubjectFilterKeyTyped

    private void jTextFieldActionFilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldActionFilterKeyTyped
        // update display
        FillSecurityRulesTable();
    }//GEN-LAST:event_jTextFieldActionFilterKeyTyped

    private void jTextFieldObjectFilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldObjectFilterKeyTyped
        // update display
        FillSecurityRulesTable();
    }//GEN-LAST:event_jTextFieldObjectFilterKeyTyped

    private void jTableConcreteRulesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableConcreteRulesMouseClicked
        // display information about the selected rule
        Point p = evt.getPoint();
        int row = jTableConcreteRules.rowAtPoint(p);
        CConcreteRule rule = securityRulesModel.GetData(row);

        // create new model
        DefaultTableModel infosModel = new DefaultTableModel();
        infosModel.addColumn("property");
        infosModel.addColumn("value");
        String attributeRow[] = new String[2];

        attributeRow[0] = "infered from";
        attributeRow[1] = rule.GetName();
        infosModel.addRow(attributeRow);

        attributeRow[0] = "organizations";
        Iterator<String> is = rule.GetOrganizations().iterator();
        attributeRow[1] = "";
        while ( is.hasNext() )
        {
            attributeRow[1] += is.next();
            if ( is.hasNext() )
                attributeRow[1] += ", ";
        }   
        infosModel.addRow(attributeRow);

        attributeRow[0] = "context";
        attributeRow[1] = rule.GetContext();
        infosModel.addRow(attributeRow);

        attributeRow[0] = "activation date";
        Date d = rule.GetLastActivationDate();
        attributeRow[1] = d != null ? d.toString() : "not yet activated";
        infosModel.addRow(attributeRow);

        if ( rule instanceof CConcreteObligation )
        {
            CConcreteObligation o = (CConcreteObligation)rule;
            attributeRow[0] = "violation context";
            attributeRow[1] = o.GetViolationContext();
            infosModel.addRow(attributeRow);
            attributeRow[0] = "fullfilment date";
            d = o.GetLastFulfillmentDate();
            attributeRow[1] = d != null ? d.toString() : "not yet fullfiled";
            infosModel.addRow(attributeRow);
            attributeRow[0] = "violation date";
            d = o.GetLastViolationDate();
            attributeRow[1] = d != null ? d.toString() : "not yet violated";
            infosModel.addRow(attributeRow);
        }
        jTableConcreteRuleInfo.setModel(infosModel);
    }//GEN-LAST:event_jTableConcreteRulesMouseClicked

    // called by the simulation tab when clicking on the update button
    public void UpdateDisplay()
    {
        // update display
        FillSecurityRulesTable();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonFilterRules;
    private javax.swing.JCheckBox jCheckBoxAdorbacPermissions;
    private javax.swing.JCheckBox jCheckBoxObligations;
    private javax.swing.JCheckBox jCheckBoxPermissions;
    private javax.swing.JCheckBox jCheckBoxProhibitions;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTableConcreteRuleInfo;
    private javax.swing.JTable jTableConcreteRules;
    private javax.swing.JTextField jTextFieldActionFilter;
    private javax.swing.JTextField jTextFieldObjectFilter;
    private javax.swing.JTextField jTextFieldSubjectFilter;
    // End of variables declaration//GEN-END:variables


    // custom table model
    class SecurityRulesTableModel extends AbstractTableModel
    {
        /**
         *
         */
        private static final long serialVersionUID = 5397902197017240144L;

        // columns and data
        private String[] columnNames = {"state", "subject", "action", "object"};
        private CConcretePermission[] dataPerm;
        private CConcreteProhibition[] dataProhib;
        private CConcreteObligation[] dataOblig;
        private CAdorbacConcretePermission[] dataAdorbacPerm;

        public void SetData(CConcretePermission[] dataPerm,
                            CConcreteProhibition[] dataProhib,
                            CConcreteObligation[] dataOblig,
                            CAdorbacConcretePermission[] dataAdorbacPerm)
        {
            this.dataPerm = dataPerm;
            this.dataProhib = dataProhib;
            this.dataOblig = dataOblig;
            this.dataAdorbacPerm = dataAdorbacPerm;
            fireTableDataChanged();
        }
        public CConcreteRule GetData(int index)
        {
            // return in order: permissions, prohibitions, obligations, adorbac permissions
            if ( jCheckBoxPermissions.isSelected() )
            {
                if ( index < dataPerm.length ) return (CConcreteRule)dataPerm[index];
            }
            if ( jCheckBoxProhibitions.isSelected() )
            {
                int previousRules = 0;
                if ( jCheckBoxPermissions.isSelected() ) previousRules += dataPerm.length;
                if ( (index - previousRules) < dataProhib.length ) return (CConcreteRule)dataProhib[index - previousRules];
            }
            if ( jCheckBoxObligations.isSelected() )
            {
                int previousRules = 0;
                if ( jCheckBoxPermissions.isSelected() ) previousRules += dataPerm.length;
                if ( jCheckBoxProhibitions.isSelected() ) previousRules += dataProhib.length;
                if ( (index - previousRules) < dataOblig.length ) return (CConcreteRule)dataOblig[index - previousRules];
            }
            if ( jCheckBoxAdorbacPermissions.isSelected() )
            {
                int previousRules = 0;
                if ( jCheckBoxPermissions.isSelected() ) previousRules += dataPerm.length;
                if ( jCheckBoxProhibitions.isSelected() ) previousRules += dataProhib.length;
                if ( jCheckBoxObligations.isSelected() ) previousRules += dataOblig.length;
                if ( (index - previousRules) < dataAdorbacPerm.length ) return (CConcreteRule)dataAdorbacPerm[index - previousRules];
            }
            return null;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount()
        {
            // do not fill table if no policy is currently selected
            if ( (dataPerm == null) || (dataProhib == null) || (dataOblig == null) || (dataAdorbacPerm == null) ) return 0;

            // the number of returned rows depends on the selected security rules type
            int total = 0;
            if ( jCheckBoxPermissions.isSelected() ) total += dataPerm.length;
            if ( jCheckBoxProhibitions.isSelected() ) total += dataProhib.length;
            if ( jCheckBoxObligations.isSelected() ) total += dataOblig.length;
            if ( jCheckBoxAdorbacPermissions.isSelected() ) total += dataAdorbacPerm.length;
            return total;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col)
        {
            CConcreteRule rule = GetData(row);
            switch ( col )
            {
                case 0:
                    // state, depends on rule type
                    try
                    {
                        if ( rule instanceof CConcretePermission )
                        {
                            CConcretePermission perm = (CConcretePermission)rule;
                            // first check if rule is active
                            if ( perm.IsActive() == false ) return "inactive";
                            else if ( perm.IsPreempted() != null ) return "preempted";
                            else return "active";
                        }
                        else if ( rule instanceof CConcreteProhibition )
                        {
                            CConcreteProhibition prohib = (CConcreteProhibition)rule;
                            // first check if rule is active
                            if ( prohib.IsActive() == false ) return "inactive";
                            else if ( prohib.IsPreempted() != null ) return "preempted";
                            else return "active";
                        }
                        else if ( rule instanceof CConcreteObligation )
                        {
                            CConcreteObligation oblig = (CConcreteObligation)rule;

                            // first check if rule is violated
                            if ( oblig.IsViolated() ) return "violated";
                            else if ( oblig.IsFulfilled() ) return "fulfilled";
                            else if ( oblig.IsActive() ) return "active";
                            else return "inactive";
                        }
                        else if ( rule instanceof CAdorbacConcretePermission )
                        {
                            CAdorbacConcretePermission aperm = (CAdorbacConcretePermission)rule;
                            // first check if rule is violated
                            // first check if rule is active
                            if ( aperm.IsActive() == false ) return "inactive";
                            else return "active";
                        }
                    }
                    catch (COrbacException e)
                    {
                        System.out.println("while evaluating rule state:" + e);
                        e.printStackTrace();
                        return "error during evaluation";
                    }
                case 1:
                    // subject
                    return rule.GetSubject();
                case 2:
                    // action
                    return rule.GetAction();
                case 3:
                    // object
                    return rule.GetObject();
                default:
                    return "wazaaa";
            }
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
            public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    public class SecurityRulesTableCellRenderer extends DefaultTableCellRenderer
    {
    	// just to remove the warning
    	static final long serialVersionUID = 0;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            TableModel m = table.getModel();
            Color cellColor = Color.WHITE;

            // rule type
            CConcreteRule rule = securityRulesModel.GetData(row);
            if ( rule instanceof CConcretePermission )
                cellColor = new Color(0.7f, 1.0f, 0.7f);
            else if ( rule instanceof CConcreteProhibition )
                cellColor = new Color(1.0f, 0.7f, 0.7f);
            else if ( rule instanceof CConcreteObligation )
                cellColor = new Color(0.7f, 0.7f, 1.0f);
            else if ( rule instanceof CAdorbacConcretePermission )
                cellColor = new Color(0.8f, 1.0f, 0.7f);
            
            // rule state
            setIcon(null);
            String state = (String)m.getValueAt(row, 0);
            if ( state.equals("active") )
            {
                // set cell on
            	if ( column == 0 ) setIcon(imgActiveRule);
                cell.setBackground( cellColor );
                cell.setForeground( Color.BLACK );
            }
            else if ( state.equals("inactive") )
            {
                // set cell light
            	if ( column == 0 ) setIcon(imgInactiveRule);
                cell.setBackground( cellColor );
                cell.setForeground( Color.GRAY );
            }
            else if ( state.equals("preempted") )
            {
                // preempted rule
            	if ( column == 0 ) setIcon(imgPreemptedRule);
                cell.setBackground( cellColor );
                cell.setForeground( Color.GRAY );
            }
            else if ( state.equals("fulfilled") )
            {
            	if ( column == 0 ) setIcon(imgFulfilled);
                // set cell light
                cell.setBackground( cellColor );
                cell.setForeground( Color.BLACK );
            }
            else if ( state.equals("violated") )
            {
            	if ( column == 0 ) setIcon(imgViolated);
                // set cell light
                cell.setBackground( cellColor );
                cell.setForeground( Color.RED );
            }
            else if ( state.equals("error during evaluation") )
            {
            	if ( column == 0 ) setIcon(imgError);
                // set cell light
                cell.setBackground( Color.GRAY );
                cell.setForeground( Color.RED );
            }
            return cell;
        }
    }
}
