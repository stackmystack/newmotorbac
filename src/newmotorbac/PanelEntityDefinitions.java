/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanelEntityDefinitions.java
 *
 * Created on May 5, 2011, 5:49:53 PM
 */

package newmotorbac;

import newmotorbac.util.OrbacPolicyContext;
import com.Ostermiller.Syntax.HighlightedDocument;
import java.awt.Frame;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import newmotorbac.dialog.jDialogAddEntityDefinition;
import newmotorbac.dialog.jDialogEditEntityDefinition;
import orbac.abstractEntities.CEntityDefinition;
import orbac.abstractEntities.CThreatEntityDefinition;
import orbac.exception.COrbacException;

/**
 *
 * @author fabien
 */
public class PanelEntityDefinitions extends javax.swing.JPanel {
    // policy context
    OrbacPolicyContext thisContext;
    // syntax highlighter
    private HighlightedDocument docPane = new HighlightedDocument();
    // currently selected definition
    private String selectedEntityDef;

    // the list of context definition editors that are open
    // the map key is the entity definition name concatenated with the organization in which the definition is edited
    HashMap<String, jDialogEditEntityDefinition> editors = new HashMap<String, jDialogEditEntityDefinition>();

    /** Creates new form PanelEntityDefinitions */
    public PanelEntityDefinitions(OrbacPolicyContext thisContext) {
        initComponents();

        // store context
        this.thisContext = thisContext;

        thisContext.panelEntityDefinitions = this;

        // set syntax highlightning object
        jTextPaneDefinition.setDocument(docPane);

        // disable some buttons
        jButtonDeleteEntityDefinition.setEnabled(false);
        jButtonAddDefinition.setEnabled(false);
        jButtonDeleteDefinition.setEnabled(false);
        jButtonEditDefinition.setEnabled(false);

        // set table state variables to enable row selection
        jTableEntityDefinitions.setCellSelectionEnabled(false);
        jTableEntityDefinitions.setRowSelectionAllowed(true);
        jTableEntityDefinitions.setColumnSelectionAllowed(false);

        UpdateEntityDefinitionTable();
    }
    
    public void CloseContextEditors()
    {
        for ( Entry<String, jDialogEditEntityDefinition> e : editors.entrySet() )
            if ( e.getValue() != null )
                e.getValue().dispose();
    }

    public void UpdateEntityDefinitionTable()
    {
        // clear table
        DefaultTableModel model = (DefaultTableModel)jTableEntityDefinitions.getModel();
        model.setNumRows(0);

        try
        {
            HashSet<CEntityDefinition> defs = thisContext.thePolicy.GetRoleDefinitionsVector();
            Iterator<CEntityDefinition> icd = defs.iterator();
            while ( icd.hasNext() )
            {
                String row[] = new String[3];
                CEntityDefinition cd = icd.next();
                if ( cd instanceof CThreatEntityDefinition )
                {
                    row[0] = cd.GetName();
                    row[1] = "threat role definition";
                    row[2] = "dynamic organization";
                }
                else
                {
                    row[0] = cd.GetName();
                    row[1] = "role definition";
                    row[2] = cd.GetAbstractEntity();
                }
                model.addRow(row);
            }

            defs = thisContext.thePolicy.GetActivityDefinitionsVector();
            icd = defs.iterator();
            while ( icd.hasNext() )
            {
                String row[] = new String[3];
                CEntityDefinition cd = icd.next();
                if ( cd instanceof CThreatEntityDefinition )
                {
                    row[0] = cd.GetName();
                    row[1] = "threat activity definition";
                    row[2] = "dynamic organization";
                }
                else
                {
                    row[0] = cd.GetName();
                    row[1] = "activity definition";
                    row[2] = cd.GetAbstractEntity();
                }
                model.addRow(row);
            }

            defs = thisContext.thePolicy.GetViewDefinitionsVector();
            icd = defs.iterator();
            while ( icd.hasNext() )
            {
                String row[] = new String[3];
                CEntityDefinition cd = icd.next();
                if ( cd instanceof CThreatEntityDefinition )
                {
                    row[0] = cd.GetName();
                    row[1] = "threat view definition";
                    row[2] = "dynamic organization";
                }
                else
                {
                    row[0] = cd.GetName();
                    row[1] = "view definition";
                    row[2] = cd.GetAbstractEntity();
                }
                model.addRow(row);
            }
        }
        catch (COrbacException e)
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

        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableEntityDefinitions = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDefinitions = new javax.swing.JTable();
        jButtonAddDefinition = new javax.swing.JButton();
        jButtonDeleteDefinition = new javax.swing.JButton();
        jButtonEditDefinition = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPaneDefinition = new javax.swing.JTextPane();
        jButtonDeleteEntityDefinition = new javax.swing.JButton();
        jButtonAddEntityDefinition = new javax.swing.JButton();
        jButtonApplyEntityDefinition = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableEntityDefinitions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Type", "Abstract entity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableEntityDefinitions.setName("jTableEntityDefinitions"); // NOI18N
        jTableEntityDefinitions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableEntityDefinitionsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableEntityDefinitions);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(PanelEntityDefinitions.class);
        jTableEntityDefinitions.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableEntityDefinitions.columnModel.title0")); // NOI18N
        jTableEntityDefinitions.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTableEntityDefinitions.columnModel.title1")); // NOI18N
        jTableEntityDefinitions.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTableEntityDefinitions.columnModel.title2")); // NOI18N

        jSplitPane1.setTopComponent(jScrollPane1);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(456, 117));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTableDefinitions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Definitions"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDefinitions.setName("jTableDefinitions"); // NOI18N
        jTableDefinitions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDefinitionsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDefinitions);
        jTableDefinitions.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableDefinitions.columnModel.title0")); // NOI18N

        jButtonAddDefinition.setText(resourceMap.getString("jButtonAddDefinition.text")); // NOI18N
        jButtonAddDefinition.setName("jButtonAddDefinition"); // NOI18N
        jButtonAddDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddDefinitionActionPerformed(evt);
            }
        });

        jButtonDeleteDefinition.setText(resourceMap.getString("jButtonDeleteDefinition.text")); // NOI18N
        jButtonDeleteDefinition.setName("jButtonDeleteDefinition"); // NOI18N
        jButtonDeleteDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteDefinitionActionPerformed(evt);
            }
        });

        jButtonEditDefinition.setText(resourceMap.getString("jButtonEditDefinition.text")); // NOI18N
        jButtonEditDefinition.setName("jButtonEditDefinition"); // NOI18N
        jButtonEditDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditDefinitionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonAddDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEditDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAddDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(jButtonDeleteDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(jButtonEditDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
        );

        jSplitPane1.setBottomComponent(jPanel1);

        jSplitPane2.setLeftComponent(jSplitPane1);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTextPaneDefinition.setName("jTextPaneDefinition"); // NOI18N
        jScrollPane3.setViewportView(jTextPaneDefinition);

        jSplitPane2.setRightComponent(jScrollPane3);

        jButtonDeleteEntityDefinition.setText(resourceMap.getString("jButtonDeleteEntityDefinition.text")); // NOI18N
        jButtonDeleteEntityDefinition.setName("jButtonDeleteEntityDefinition"); // NOI18N
        jButtonDeleteEntityDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteEntityDefinitionActionPerformed(evt);
            }
        });

        jButtonAddEntityDefinition.setText(resourceMap.getString("jButtonAddEntityDefinition.text")); // NOI18N
        jButtonAddEntityDefinition.setName("jButtonAddEntityDefinition"); // NOI18N
        jButtonAddEntityDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddEntityDefinitionActionPerformed(evt);
            }
        });

        jButtonApplyEntityDefinition.setText(resourceMap.getString("jButtonApplyEntityDefinition.text")); // NOI18N
        jButtonApplyEntityDefinition.setName("jButtonApplyEntityDefinition"); // NOI18N
        jButtonApplyEntityDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyEntityDefinitionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonAddEntityDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDeleteEntityDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonApplyEntityDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(122, Short.MAX_VALUE))
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAddEntityDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDeleteEntityDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonApplyEntityDefinition, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void DisplayEntityDefinitions(String entityDef)
    {
        // empty table
        DefaultTableModel modelDefinition = (DefaultTableModel)jTableDefinitions.getModel();
        modelDefinition.setNumRows(0);
        String rowContent[] = new String[2];
        CEntityDefinition ed = null;
        try
        {
            ed = thisContext.thePolicy.GetEntityDefinition(entityDef);
            HashMap<String, String> definitions = ed.GetDefinitions();

            Set< Entry<String, String> > keys = definitions.entrySet();
            Iterator< Entry<String, String> > it = keys.iterator();
            while ( it.hasNext() )
            {
                Entry<String, String> e = it.next();
                String key = (String)e.getKey();
                rowContent[0] = key;
                modelDefinition.addRow(rowContent);
            }
        }
        catch ( Exception e )
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        finally
        {
            // if there is some definitions to display, select the first one in the lower table
            // and activate/desactivate some buttons
            jButtonDeleteDefinition.setEnabled(modelDefinition.getRowCount() > 0);
            jButtonEditDefinition.setEnabled(modelDefinition.getRowCount() > 0);
            // the add definition button is activated only if their is no definition
            // for the currently selected organization or if no definition exist
            jButtonAddDefinition.setEnabled(modelDefinition.getRowCount() == 0);

            if ( modelDefinition.getRowCount() > 0 )
            {
                jTableDefinitions.setRowSelectionInterval(0, 0);
                // display it
                String org = (String)modelDefinition.getValueAt(0, 0);

                // display definition
                try
                {
                    String def = ed.GetDefinition(org);
                    // display definition
                    jTextPaneDefinition.setText(def);
                }
                catch (COrbacException e)
                {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }

            // this will update the buttons state
            SelectedOrganizationHasChanged();
        }
    }

    private void jButtonAddDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddDefinitionActionPerformed
        // check if an organization is selected
        if ( thisContext.currentOrganization == null )
            JOptionPane.showMessageDialog(this, "No organization selected, please select the organization in the organization tree "
                    + "in which you want to add an entity definition definition");
        else {
            // first check if an editor is already open
            jDialogEditEntityDefinition editor = editors.get(selectedEntityDef + thisContext.currentOrganization);
            if ( editor == null ) {
                // if not display dialog box
                jDialogEditEntityDefinition createDef = new jDialogEditEntityDefinition(findActiveFrame(), false, "",
                                                                                                          selectedEntityDef,
                                                                                                          thisContext.currentOrganization,
                                                                                                          thisContext.thePolicy,
                                                                                                          this);
                createDef.setLocationRelativeTo(findActiveFrame());
                NewMotorbacApp.getApplication().show(createDef);
                // store editor in map
                editors.put(selectedEntityDef + thisContext.currentOrganization, createDef);
            } else editor.toFront();
        }
}//GEN-LAST:event_jButtonAddDefinitionActionPerformed

    private void jButtonDeleteDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteDefinitionActionPerformed
        try {
            // get selected entity definition
            int rowEntityDef = jTableEntityDefinitions.getSelectedRow();
            int rowDef = jTableDefinitions.getSelectedRow();
            if ( rowDef == -1 && rowEntityDef == -1 ) return;
            String entityDef = (String)jTableEntityDefinitions.getModel().getValueAt(rowEntityDef, 0);
            String org = (String)jTableDefinitions.getModel().getValueAt(rowDef, 0);
            // delete selected entity definition definition
            thisContext.thePolicy.DeleteEntityDefinitionDefinition(entityDef, org);

            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
            // refresh display
            UpdateEntityDefinitionTable();
            // reselect the previously selected definition
            jTableEntityDefinitions.getSelectionModel().setSelectionInterval(rowEntityDef, rowEntityDef);

            // display definitions for this entity definition
            DisplayEntityDefinitions(selectedEntityDef);
        } catch (COrbacException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(this, e);
            e.printStackTrace();
        }
}//GEN-LAST:event_jButtonDeleteDefinitionActionPerformed

    private void jButtonEditDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditDefinitionActionPerformed
        // retrieve definition organization
        int rowIndex = jTableDefinitions.getSelectedRow();
        String org = (String)jTableDefinitions.getValueAt(rowIndex, 0);
        // first check if an editor is already open
        jDialogEditEntityDefinition editor = editors.get(selectedEntityDef + org);
        if ( editor == null ) {
            // if not display dialog box
            jDialogEditEntityDefinition createDef = new jDialogEditEntityDefinition(findActiveFrame(), false, jTextPaneDefinition.getText(),
                                                                                                      selectedEntityDef,
                                                                                                      org,
                                                                                                      thisContext.thePolicy,
                                                                                                      this);
            createDef.setLocationRelativeTo(findActiveFrame());
            NewMotorbacApp.getApplication().show(createDef);
            // store editor in map
            editors.put(selectedEntityDef + org, createDef);
        } else editor.toFront();
}//GEN-LAST:event_jButtonEditDefinitionActionPerformed

    private void jButtonAddEntityDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddEntityDefinitionActionPerformed
     	// ask entity definition name and type
        jDialogAddEntityDefinition createEntityDef = new jDialogAddEntityDefinition(findActiveFrame(), true, thisContext.thePolicy);
        createEntityDef.setLocationRelativeTo(findActiveFrame());
        NewMotorbacApp.getApplication().show(createEntityDef);
        if ( createEntityDef.canceled == true ) return;
        try
        {
            // create entity definition
            thisContext.thePolicy.CreateEntityDefinition(createEntityDef.GetEntityDefinitionName(),
                                                         createEntityDef.GetEntityName(),
                                                         createEntityDef.GetEntityDefinitionType());

            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
            // update entity definition table
            UpdateEntityDefinitionTable();
        }
        catch ( COrbacException e )
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
        }
    }//GEN-LAST:event_jButtonAddEntityDefinitionActionPerformed

    private void jButtonDeleteEntityDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteEntityDefinitionActionPerformed
        try
    	{
            DefaultTableModel model = (DefaultTableModel)jTableEntityDefinitions.getModel();
            // get selected definition
            int row = jTableEntityDefinitions.getSelectedRow();
            if ( row == -1 ) return;
            String edn = (String)model.getValueAt(row, 0);
            // delete selected entity definition
            thisContext.thePolicy.DeleteEntityDefinition(edn);

            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
            // update entity definition table
            UpdateEntityDefinitionTable();
    	}
        catch (COrbacException e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonDeleteEntityDefinitionActionPerformed

    private void jButtonApplyEntityDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyEntityDefinitionActionPerformed
        try
        {
            thisContext.thePolicy.ApplyRoleDefinitions();
            thisContext.thePolicy.ApplyActivityDefinitions();
            thisContext.thePolicy.ApplyViewDefinitions();
        }
        catch (COrbacException e)
        {
            // if an exception is caught we display it
            String mess = "";
            mess += e.getMessage() + ":\n";
            Iterator<?> isc = e.GetInformation().iterator();
            while ( isc.hasNext() )
            {
                String ve = (String)isc.next();
                mess += ve + "\n";
            }
            JOptionPane.showMessageDialog(this, mess);
            System.out.println(e);
        }
        finally
        {
            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
        }
    }//GEN-LAST:event_jButtonApplyEntityDefinitionActionPerformed

    private void jTableEntityDefinitionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableEntityDefinitionsMouseClicked
        // get selected row
        DefaultTableModel model = (DefaultTableModel)jTableEntityDefinitions.getModel();
        if ( model.getRowCount() == 0 )
            return;
        DefaultTableModel modelDefinition = (DefaultTableModel)jTableDefinitions.getModel();
        Point p = evt.getPoint();
        int row = jTableEntityDefinitions.rowAtPoint(p);
        selectedEntityDef = (String)model.getValueAt(row, 0);

        // enable entity definition deletion button
        jButtonDeleteEntityDefinition.setEnabled(true);

        // display definitions for this entity definition
        DisplayEntityDefinitions(selectedEntityDef);
    }//GEN-LAST:event_jTableEntityDefinitionsMouseClicked

    private void jTableDefinitionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDefinitionsMouseClicked
        // get organization name to display its definition in the text area
        if ( selectedEntityDef == null ) return;
        DefaultTableModel modelDefinition = (DefaultTableModel)jTableDefinitions.getModel();
        Point p = evt.getPoint();
        int row = jTableDefinitions.rowAtPoint(p);
        String org = (String)modelDefinition.getValueAt(row, 0);

        // get definition and display it
        try
        {
            CEntityDefinition ed = thisContext.thePolicy.GetEntityDefinition(selectedEntityDef);
            // display definition
            jTextPaneDefinition.setText(ed.GetDefinition(org));
        }
        catch (COrbacException e)
        {
            System.err.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTableDefinitionsMouseClicked

    public void EntityDefinitionEditorClosing(String entityDefinition, String org)
    {
        // remove dialog instance from the map
        editors.put(entityDefinition + org, null);
    }

    // called when the current organization has changed
    public void SelectedOrganizationHasChanged()
    {
        // if the currently selected organization appears in the definition table, disable button
        boolean found = false;
        DefaultTableModel modelDefinition = (DefaultTableModel)jTableDefinitions.getModel();
        Vector<Vector<String>> data = modelDefinition.getDataVector();
        for ( Vector<String> row : data )
        {
            // only one column
            if ( row.contains(thisContext.currentOrganization) )
            {
                found = true;
                break;
            }
        }
        jButtonAddDefinition.setEnabled( !found );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddDefinition;
    private javax.swing.JButton jButtonAddEntityDefinition;
    private javax.swing.JButton jButtonApplyEntityDefinition;
    private javax.swing.JButton jButtonDeleteDefinition;
    private javax.swing.JButton jButtonDeleteEntityDefinition;
    private javax.swing.JButton jButtonEditDefinition;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTableDefinitions;
    private javax.swing.JTable jTableEntityDefinitions;
    private javax.swing.JTextPane jTextPaneDefinition;
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
}
