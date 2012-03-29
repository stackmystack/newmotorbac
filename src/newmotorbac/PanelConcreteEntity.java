/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanelConcreteEntity.java
 *
 * Created on May 5, 2011, 4:05:14 PM
 */

package newmotorbac;

import newmotorbac.util.OrbacPolicyContext;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import newmotorbac.dialog.jDialogAddConcreteEntity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.AbstractTableModel;
import orbac.AbstractOrbacPolicy;
import orbac.concreteEntities.CConcreteEntityAssignement;
import orbac.conflict.CSeparationConstraint;
import orbac.exception.CInvalidResourceNameException;
import orbac.exception.COrbacException;
import orbac.exception.CViolatedConstraintException;
import orbac.exception.CViolatedEntityDefinitionException;

/**
 *
 * @author fabien
 */
public class PanelConcreteEntity extends javax.swing.JPanel implements ActionListener {
    // policy context
    OrbacPolicyContext thisContext;
    // entity type displayed by this instance
    int entityType;
    // table model
    MyTableModel tableModel = new MyTableModel();

    String[] entityTypes = {"subject", "action", "object"};

    // contextual menu// the popup menu items
    private JPopupMenu popupMenu = new JPopupMenu();
    private MouseListener popupListener = new PopupListener();
    private JMenu assignSubMenu = new JMenu("");
    private JMenu revokeSubMenu = new JMenu("");
    private JMenu assignClassSubMenu = new JMenu("");
    private JMenu revokeClassSubMenu = new JMenu("");
    private JMenuItem noOrgSelectedMenu = new JMenuItem("no organization selected");

    /** Creates new form PanelConcreteEntity */
    public PanelConcreteEntity(OrbacPolicyContext thisContext, int entityType) {
        initComponents();

        // store context
        this.thisContext = thisContext;
        this.entityType = entityType;

        // set table model
        jTableEntityMembers.setModel(tableModel);

        // add menu
        jListEntities.addMouseListener(popupListener);
        popupMenu.add(assignSubMenu);
        popupMenu.add(revokeSubMenu);
        popupMenu.add(assignClassSubMenu);
        popupMenu.add(revokeClassSubMenu);
        assignSubMenu.addActionListener(this);
        revokeSubMenu.addActionListener(this);
        assignClassSubMenu.addActionListener(this);
        revokeClassSubMenu.addActionListener(this);

        // set menu text
        assignClassSubMenu.setText("assign class");
        revokeClassSubMenu.setText("unassign class");
        switch ( entityType )
        {
            case AbstractOrbacPolicy.TYPE_SUBJECT:
                assignSubMenu.setText("empower");
                revokeSubMenu.setText("revoke");
                break;
            case AbstractOrbacPolicy.TYPE_ACTION:
                assignSubMenu.setText("consider");
                revokeSubMenu.setText("unconsider");
                break;
            case AbstractOrbacPolicy.TYPE_OBJECT:
                assignSubMenu.setText("use");
                revokeSubMenu.setText("unuse");
                break;
        }
    }

    // tell to display some given entity information
    public void SetSelectedEntity(String entity)
    {
        jListEntities.setSelectedValue(entity, true);
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt)
    {
        if ( evt.getSource() instanceof JMenuItem )
        {
            JMenuItem source = (JMenuItem)evt.getSource();

            if ( source.getParent() == assignSubMenu.getPopupMenu() )
            {
                try
                {
                    switch ( entityType )
                    {
                        case AbstractOrbacPolicy.TYPE_SUBJECT:
                            thisContext.thePolicy.Empower(thisContext.currentOrganization, selectedEntity, source.getText());
                            thisContext.panelRoles.RefreshSelectedEntityInformation();
                            break;
                        case AbstractOrbacPolicy.TYPE_ACTION:
                            thisContext.thePolicy.Consider(thisContext.currentOrganization, selectedEntity, source.getText());
                            thisContext.panelActivities.RefreshSelectedEntityInformation();
                            break;
                        case AbstractOrbacPolicy.TYPE_OBJECT:
                            thisContext.thePolicy.Use(thisContext.currentOrganization, selectedEntity, source.getText());
                            thisContext.panelViews.RefreshSelectedEntityInformation();
                            break;
                    }
                    // refresh menu
                    RefreshMenu();
                    // refresh selected entity information
                    RefreshSelectedEntityInformation();
                    
                    // save the modified policy in history if operation is successful
                    thisContext.panelPolicy.PushPolicy();
                }
                catch ( CViolatedEntityDefinitionException vee )
                {
                    // violated entity definition
                    JOptionPane.showMessageDialog(findActiveFrame(), vee.getMessage());
                    System.out.println("CSubjectTab::actionPerformed():" + vee);
                }
                catch ( CViolatedConstraintException vce )
                {
                    // violated separation constraint
                    String mess = "";
                    mess += vce.getMessage() + ":\n";
                    Iterator<?> isc = vce.GetInformation().iterator();
                    while ( isc.hasNext() )
                    {
                            CSeparationConstraint sc = (CSeparationConstraint)isc.next();
                            mess += sc.GetFirstEntity() + " is separated with " + sc.GetSecondEntity() + "\n";
                    }
                    JOptionPane.showMessageDialog(findActiveFrame(), mess);
                    System.out.println("CSubjectTab::actionPerformed():" + vce);
                }
                catch ( COrbacException e )
                {
                    JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
                    System.out.println("CSubjectTab::actionPerformed():" + e);
                    e.printStackTrace();
                }
            }
            else if ( source.getParent() == assignClassSubMenu.getPopupMenu() )
            {
                try
                {
                    thisContext.thePolicy.AssignConcreteEntityToClass(selectedEntity, source.getText());

                    // refresh selected entity information
                    RefreshSelectedEntityInformation();

                    // save the modified policy in history if operation is successful
                    thisContext.panelPolicy.PushPolicy();
                }
                catch (COrbacException e)
                {
                    System.out.println("actionPerformed():" + e);
                    e.printStackTrace();
                }
            }
            else if ( source.getParent() == revokeSubMenu.getPopupMenu() )
            {
                try
                {
                    switch ( entityType )
                    {
                        case AbstractOrbacPolicy.TYPE_SUBJECT:
                            thisContext.thePolicy.UnEmpower(thisContext.currentOrganization, selectedEntity, source.getText());
                            thisContext.panelRoles.RefreshSelectedEntityInformation();
                            break;
                        case AbstractOrbacPolicy.TYPE_ACTION:
                            thisContext.thePolicy.UnConsider(thisContext.currentOrganization, selectedEntity, source.getText());
                            thisContext.panelActivities.RefreshSelectedEntityInformation();
                            break;
                        case AbstractOrbacPolicy.TYPE_OBJECT:
                            thisContext.thePolicy.UnUse(thisContext.currentOrganization, selectedEntity, source.getText());
                            thisContext.panelViews.RefreshSelectedEntityInformation();
                            break;
                    }
                    // refresh menu
                    RefreshMenu();
                    // refresh selected entity information
                    RefreshSelectedEntityInformation();

                    // save the modified policy in history if operation is successful
                    thisContext.panelPolicy.PushPolicy();
                }
                catch (COrbacException e)
                {
                    JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
                    System.out.println("actionPerformed():" + e);
                    e.printStackTrace();
                }
            }
            else if ( source.getParent() == revokeClassSubMenu.getPopupMenu() )
            {
                try
                {
                    thisContext.thePolicy.UnassignConcreteEntityToClass(selectedEntity, source.getText());
                    
                    // refresh selected entity information
                    RefreshSelectedEntityInformation();

                    // save the modified policy in history if operation is successful
                    thisContext.panelPolicy.PushPolicy();
                }
                catch (COrbacException e)
                {
                    System.out.println("actionPerformed():" + e);
                    e.printStackTrace();
                }
            }
        }
    }

    // update the concrete enity list
    public void UpdateEntityList()
    {
        Set<String> entities = null;
        try
        {
            // get entities
            switch ( entityType )
            {
                case AbstractOrbacPolicy.TYPE_SUBJECT:
                    entities = thisContext.thePolicy.GetSubjects(!thisContext.adorbacViewActive);
                    break;
                case AbstractOrbacPolicy.TYPE_ACTION:
                    entities = thisContext.thePolicy.GetActions(!thisContext.adorbacViewActive);
                    break;
                case AbstractOrbacPolicy.TYPE_OBJECT:
                    entities = thisContext.thePolicy.GetObjects(!thisContext.adorbacViewActive);
                    break;
            }
            // display them
            DefaultListModel m = new DefaultListModel();
            for ( String s : entities )
                m.addElement(s);
            jListEntities.setModel(m);
        }
        catch ( COrbacException e )
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

        jButtonAddEntity = new javax.swing.JButton();
        jButtonDeleteEntity = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListEntities = new javax.swing.JList();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaEntityInfo = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableEntityMembers = new javax.swing.JTable();
        jButtonEditEntity = new javax.swing.JButton();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(400, 200));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(PanelConcreteEntity.class);
        jButtonAddEntity.setText(resourceMap.getString("jButtonAddEntity.text")); // NOI18N
        jButtonAddEntity.setName("jButtonAddEntity"); // NOI18N
        jButtonAddEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddEntityActionPerformed(evt);
            }
        });

        jButtonDeleteEntity.setText(resourceMap.getString("jButtonDeleteEntity.text")); // NOI18N
        jButtonDeleteEntity.setName("jButtonDeleteEntity"); // NOI18N
        jButtonDeleteEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteEntityActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jScrollPane1.border.title"))); // NOI18N
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jListEntities.setDragEnabled(true);
        jListEntities.setMaximumSize(new java.awt.Dimension(100, 140));
        jListEntities.setName("jListEntities"); // NOI18N
        jListEntities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListEntitiesMouseClicked(evt);
            }
        });
        jListEntities.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListEntitiesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListEntities);

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTextAreaEntityInfo.setColumns(20);
        jTextAreaEntityInfo.setRows(5);
        jTextAreaEntityInfo.setName("jTextAreaEntityInfo"); // NOI18N
        jScrollPane2.setViewportView(jTextAreaEntityInfo);

        jSplitPane1.setTopComponent(jScrollPane2);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTableEntityMembers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attribute", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableEntityMembers.setName("jTableEntityMembers"); // NOI18N
        jScrollPane3.setViewportView(jTableEntityMembers);

        jSplitPane1.setRightComponent(jScrollPane3);

        jButtonEditEntity.setText(resourceMap.getString("jButtonEditEntity.text")); // NOI18N
        jButtonEditEntity.setName("jButtonEditEntity"); // NOI18N
        jButtonEditEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditEntityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAddEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEditEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAddEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDeleteEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonEditEntity, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddEntityActionPerformed
        try
        {
            // get orbac classes
            Set<String> classes = thisContext.thePolicy.GetClassesList();
            
            // get new entity name and the classes it instanciates
            JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
            jDialogAddConcreteEntity dialogBox = new jDialogAddConcreteEntity(mainFrame, classes, entityTypes[entityType], true);
            dialogBox.setLocationRelativeTo(mainFrame);
            NewMotorbacApp.getApplication().show(dialogBox);

            // create entity if requested
            if ( dialogBox.canceled == false )
            {
                String entityName = dialogBox.GetEntityName();
                switch ( entityType )
                {
                    case AbstractOrbacPolicy.TYPE_SUBJECT:
                        thisContext.thePolicy.AddSubject( entityName );
                        break;
                    case AbstractOrbacPolicy.TYPE_ACTION:
                        thisContext.thePolicy.AddAction( entityName );
                        break;
                    case AbstractOrbacPolicy.TYPE_OBJECT:
                        thisContext.thePolicy.AddObject( entityName );
                        break;
                }
                // add classes
                Set<String> entityClasses = thisContext.thePolicy.GetConcreteEntityClasses( entityName );
                Set<String> selectedClasses = dialogBox.GetInstanciatedClasses();
                // add selected classes
                for ( String c : selectedClasses )
                    thisContext.thePolicy.AssignConcreteEntityToClass(entityName, c);
            }
            // update list
            UpdateEntityList();
            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
            
            // if the subject tab is being updated, update also the object tab and vice-versa
            if ( entityType == AbstractOrbacPolicy.TYPE_SUBJECT )
                thisContext.panelObjects.UpdateEntityList();
            else if(entityType == AbstractOrbacPolicy.TYPE_OBJECT)
                thisContext.panelSubjects.UpdateEntityList();
        }
        catch ( COrbacException e )
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonAddEntityActionPerformed

    private void jButtonDeleteEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteEntityActionPerformed
        try
        {
            // get selected entity
            String entityName = (String)jListEntities.getSelectedValue();
            switch ( entityType )
            {
                case AbstractOrbacPolicy.TYPE_SUBJECT:
                    thisContext.thePolicy.DeleteSubject( entityName );
                    break;
                case AbstractOrbacPolicy.TYPE_ACTION:
                    thisContext.thePolicy.DeleteAction( entityName );
                    break;
                case AbstractOrbacPolicy.TYPE_OBJECT:
                    thisContext.thePolicy.DeleteObject( entityName );
                    break;
            }
            // update list
            UpdateEntityList();
            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();

            // if the subject tab is being updated, update also the object tab and vice-versa
            if ( entityType == AbstractOrbacPolicy.TYPE_SUBJECT )
                thisContext.panelObjects.UpdateEntityList();
            else if(entityType == AbstractOrbacPolicy.TYPE_OBJECT)
                thisContext.panelSubjects.UpdateEntityList();
        }
        catch ( COrbacException e )
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonDeleteEntityActionPerformed

    private void jButtonEditEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditEntityActionPerformed
        // display the same dialog box as for the entity creation
        try
        {
            // get selected entity
            String entityName = (String)jListEntities.getSelectedValue();
            // get selected entity classes
            Set<String> entityClasses = thisContext.thePolicy.GetConcreteEntityClasses( entityName );
            // get orbac classes
            Set<String> classes = thisContext.thePolicy.GetClassesList();

            // get modified entity name and the classes it instanciates
            JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
            jDialogAddConcreteEntity dialogBox = new jDialogAddConcreteEntity(mainFrame, entityName, classes, entityClasses, true);
            dialogBox.setLocationRelativeTo(mainFrame);
            NewMotorbacApp.getApplication().show(dialogBox);

            // modify entity if necessary
            if ( dialogBox.canceled == false )
            {
                String newEntityName = dialogBox.GetEntityName();
                if ( newEntityName.equals(entityName) == false )
                    thisContext.thePolicy.RenameObject(entityName, newEntityName);
                
                Set<String> selectedClasses = dialogBox.GetInstanciatedClasses();
                // remove unselected classes
                for ( String c : entityClasses )
                {
                    if ( selectedClasses.contains(c) == false)
                        thisContext.thePolicy.UnassignConcreteEntityToClass(entityName, c);
                }
                // add selected classes
                for ( String c : selectedClasses )
                {
                    if ( entityClasses.contains(c) == false)
                        thisContext.thePolicy.AssignConcreteEntityToClass(entityName, c);
                }
            }
            // update list
            UpdateEntityList();
            // push policy on undo/redo stack
            thisContext.panelPolicy.PushPolicy();
            
            // if the subject tab is being updated, update also the object tab and vice-versa
            if ( entityType == AbstractOrbacPolicy.TYPE_SUBJECT )
                thisContext.panelObjects.UpdateEntityList();
            else if(entityType == AbstractOrbacPolicy.TYPE_OBJECT)
                thisContext.panelSubjects.UpdateEntityList();
        }
        catch ( COrbacException e )
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonEditEntityActionPerformed

    String[] abstractLink = {"Empowered in:", "Considered as:", "Used in:"};
    String[] abstractType = {"role", "activity", "view"};

    private void jListEntitiesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListEntitiesValueChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jListEntitiesValueChanged

    private void jListEntitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListEntitiesMouseClicked
        // TODO add your handling code here:
        int[] selectedItems = jListEntities.getSelectedIndices();

        if ( selectedItems.length == 1 )
        {
            // display selected entity information
            selectedEntity = (String)jListEntities.getModel().getElementAt(selectedItems[0]);
            RefreshSelectedEntityInformation();
        }
        else
        {
            jTextAreaEntityInfo.setText("Multiple selection, information unavailable");
            selectedEntity = null;
        }

        // refresh menu
        RefreshMenu();
    }//GEN-LAST:event_jListEntitiesMouseClicked

    public void RefreshSelectedEntityInformation()
    {
        String info = abstractLink[entityType];
        try
        {
            // display abstract entities to which the entity is associated
            Vector<CConcreteEntityAssignement> abstractEntities = null;
            switch ( entityType )
            {
                case AbstractOrbacPolicy.TYPE_SUBJECT:
                    abstractEntities = thisContext.thePolicy.GetRolesForSubject(selectedEntity);
                    break;
                case AbstractOrbacPolicy.TYPE_ACTION:
                    abstractEntities = thisContext.thePolicy.GetActivitiesForAction(selectedEntity);
                    break;
                case AbstractOrbacPolicy.TYPE_OBJECT:
                    abstractEntities = thisContext.thePolicy.GetViewsForObject(selectedEntity);
                    break;
            }
            Iterator<CConcreteEntityAssignement> ir = abstractEntities.iterator();
            if ( ir.hasNext() == false ) info += "  no " + abstractType[entityType] + "\n";
            else
            {
                while ( ir.hasNext() )
                {
                    CConcreteEntityAssignement ass = ir.next();
                    info += "  " + ass.GetAbstractEntity() + " in organization " + ass.GetOrganization() + "\n";
                }
            }
            Set<String> classes = thisContext.thePolicy.GetConcreteEntityClasses(selectedEntity);
            if ( classes.size() == 0 ) info +=  "Instanciates no classes";
            else
            {
                info += "Instanciate the following classes: ";
                Iterator<String> ic = classes.iterator();
                while ( ic.hasNext() )
                {
                    info += ic.next();
                    if ( ic.hasNext() ) info += ", ";
                }
            }

            jTextAreaEntityInfo.setText(info);

            // display class members
            Map<String, String> membersValues = thisContext.thePolicy.GetConcreteEntityClassMembersAndValues(selectedEntity);
            tableModel.SetData( membersValues, selectedEntity );

        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddEntity;
    private javax.swing.JButton jButtonDeleteEntity;
    private javax.swing.JButton jButtonEditEntity;
    private javax.swing.JList jListEntities;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTableEntityMembers;
    private javax.swing.JTextArea jTextAreaEntityInfo;
    // End of variables declaration//GEN-END:variables

    // implement custom table model for class attributes edition
    class MyTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;

        // columns and data
        private String[] columnNames = {"Attribute", "Value"};
        private String[] attributesNames = new String[0];
        private String[] attributesValues = new String[0];
        // concrete entity associated with this data
        private String associatedEntity = "";

        public void SetData(Map<String, String> dataMap, String associatedEntity)
        {
            // store entity name
            this.associatedEntity = associatedEntity;
            // allocate memory
            attributesNames = new String[dataMap.size()];
            attributesValues = new String[dataMap.size()];
            // copy content
            Set< Entry<String, String> > es = dataMap.entrySet();
            Iterator< Entry<String, String> > ies = es.iterator();
            int i = 0;
            while ( ies.hasNext() )
            {
                Entry<String, String> e = ies.next();
                attributesNames[i] = e.getKey();
                attributesValues[i] = e.getValue();
                i++;
            }
            fireTableDataChanged();
        }
        public void clear()
        {
            HashMap<String, String> dummyMap = new HashMap<String, String>();
            SetData(dummyMap, "dummy");
        }

        @Override
        public int getColumnCount()
        {
            return 2;
        }

        @Override
        public int getRowCount()
        {
            return attributesValues.length;
        }

        public boolean isCellEditable(int row, int col)
        {
            // only attributes values can be edited
            if (col < 1) return false;
            else return true;
        }

        @Override
        public Object getValueAt(int arg0, int arg1)
        {
            if ( arg1 == 0 )
            {
                // attribute name
                return attributesNames[arg0];
            }
            else
            {
                // attribute value
                return attributesValues[arg0];
            }
        }
        public String getColumnName(int col)
        {
            return columnNames[col];
        }

        public void setValueAt(Object value, int row, int col)
        {
            if ( col == 0 )
            {
                // attribute name, should never happen
            }
            else
            {
                // attribute value
                attributesValues[row] = (String)value;
                // reflect change in policy
                try
                {
                    thisContext.thePolicy.SetClassInstanceMemberValue(associatedEntity, attributesNames[row], attributesValues[row]);
                }
                catch (COrbacException e)
                {
                    e.printStackTrace();
                }
            }
            fireTableCellUpdated(row, col);
        }
    }


    public void RefreshMenu()
    {
        // check if an entity has been selected
        if ( selectedEntity == null )
        {
            // possibly multiple selections
            int[] selectedItems = jListEntities.getSelectedIndices();

            if ( selectedItems.length > 0 && thisContext.currentOrganization != null)
            {
                // generate menu for multiple items
                popupMenu.removeAll();
                popupMenu.add(assignSubMenu);
                popupMenu.add(assignClassSubMenu);
                
                // build abstract entity list
                assignSubMenu.removeAll();
                Set<String> abstractEntities = new HashSet<String>();

                try
                {
                    switch ( entityType )
                    {
                        case AbstractOrbacPolicy.TYPE_SUBJECT:
                            abstractEntities = thisContext.thePolicy.GetRoles(thisContext.currentOrganization, !thisContext.adorbacViewActive);
                            break;
                        case AbstractOrbacPolicy.TYPE_ACTION:
                            abstractEntities = thisContext.thePolicy.GetActivities(thisContext.currentOrganization, !thisContext.adorbacViewActive);
                            break;
                        case AbstractOrbacPolicy.TYPE_OBJECT:
                            abstractEntities = thisContext.thePolicy.GetViews(thisContext.currentOrganization, !thisContext.adorbacViewActive);
                            break;
                    }
                    Iterator<String> ir = abstractEntities.iterator();
                    while ( ir.hasNext() )
                    {
                        JMenuItem mi = new JMenuItem( (String)ir.next() );
                        mi.addActionListener(this);
                        assignSubMenu.add(mi);
                    }
                    // build class list
                    assignClassSubMenu.removeAll();
                    Set<String> classes = thisContext.thePolicy.GetClassesList();
                    for ( String c : classes )
                    {
                        JMenuItem mi = new JMenuItem( c );
                        mi.addActionListener(this);
                        assignClassSubMenu.add(mi);
                    }
                }
                catch (COrbacException e)
                {
                    System.out.println("RefreshMenu: " + e);
                    e.printStackTrace();
                }
            }
            return;
        }
        if ( thisContext.currentOrganization == null )
        {
            popupMenu.removeAll();
            popupMenu.add(noOrgSelectedMenu);
            return;
        }
        else
        {
            popupMenu.removeAll();
            popupMenu.add(assignSubMenu);
            popupMenu.add(revokeSubMenu);
            popupMenu.add(assignClassSubMenu);
            popupMenu.add(revokeClassSubMenu);
        }
        try
        {
            // build abstract entity list
            assignSubMenu.removeAll();
            Set<String> abstractEntities = new HashSet<String>();

            switch ( entityType )
            {
                case AbstractOrbacPolicy.TYPE_SUBJECT:
                    abstractEntities = thisContext.thePolicy.GetRoles(thisContext.currentOrganization, !thisContext.adorbacViewActive);
                    break;
                case AbstractOrbacPolicy.TYPE_ACTION:
                    abstractEntities = thisContext.thePolicy.GetActivities(thisContext.currentOrganization, !thisContext.adorbacViewActive);
                    break;
                case AbstractOrbacPolicy.TYPE_OBJECT:
                    abstractEntities = thisContext.thePolicy.GetViews(thisContext.currentOrganization, !thisContext.adorbacViewActive);
                    break;
            }
            Iterator<String> ir = abstractEntities.iterator();
            while ( ir.hasNext() )
            {
                JMenuItem mi = new JMenuItem( (String)ir.next() );
                mi.addActionListener(this);
                assignSubMenu.add(mi);
            }
            if ( selectedEntity != null )
            {
                // build abstract entity list to which the concrete entity is assigned
                revokeSubMenu.removeAll();
                Set<String> assignments = null;
                switch ( entityType )
                {
                    case AbstractOrbacPolicy.TYPE_SUBJECT:
                        assignments = thisContext.thePolicy.GetRolesForSubjectInOrganization(thisContext.currentOrganization, selectedEntity);
                        break;
                    case AbstractOrbacPolicy.TYPE_ACTION:
                        assignments = thisContext.thePolicy.GetActivitiesForActionInOrganization(thisContext.currentOrganization, selectedEntity);
                        break;
                    case AbstractOrbacPolicy.TYPE_OBJECT:
                        assignments = thisContext.thePolicy.GetViewsForObjectInOrganization(thisContext.currentOrganization, selectedEntity);
                        break;
                }
                for ( String ae : assignments )
                {
                    JMenuItem mi = new JMenuItem( ae );
                    mi.addActionListener(this);
                    revokeSubMenu.add(mi);
                }
                // build class list to which the entity is associated
                revokeClassSubMenu.removeAll();
                Set<String> assignedClasses = thisContext.thePolicy.GetConcreteEntityClasses(selectedEntity);
                for ( String c : assignedClasses )
                {
                    JMenuItem mi = new JMenuItem( c );
                    mi.addActionListener(this);
                    revokeClassSubMenu.add(mi);
                }
            }
            // build class list
            assignClassSubMenu.removeAll();
            Set<String> classes = thisContext.thePolicy.GetClassesList();
            for ( String c : classes )
            {
                JMenuItem mi = new JMenuItem( c );
                mi.addActionListener(this);
                assignClassSubMenu.add(mi);
            }
        }
        catch (CInvalidResourceNameException e)
        {
        }
        catch (COrbacException e)
        {
            System.out.println("RefreshMenu: " + e);
            e.printStackTrace();
        }
    }

    // popup menu mouse handler
    private String selectedEntity;
    class PopupListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
        public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }

        private void maybeShowPopup(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {
                // check for multiple selections
                int[] selectedItems = jListEntities.getSelectedIndices();

                if ( selectedItems.length == 1 )
                {
                    int index = jListEntities.locationToIndex(e.getPoint());
                    jListEntities.setSelectedIndex(index);
                }

                // display the menu
        	popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
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
