/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanelAbstractEntity.java
 *
 * Created on May 5, 2011, 4:57:12 PM
 */
package newmotorbac;

import newmotorbac.util.SimpleCellRenderer;
import newmotorbac.util.OrbacPolicyContext;
import newmotorbac.dialog.jDialogAddAbstractEntity;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import newmotorbac.dialog.jDialogCreateAbstractRule;
import orbac.AbstractOrbacPolicy;
import orbac.abstractEntities.CActivityAssignment;
import orbac.abstractEntities.CRoleAssignment;
import orbac.abstractEntities.CViewAssignment;
import orbac.conflict.CSeparationConstraint;
import orbac.exception.COrbacException;
import orbac.exception.CViolatedConstraintException;
import orbac.exception.CViolatedEntityDefinitionException;

/**
 *
 * @author fabien
 */
public class PanelAbstractEntity extends JPanel implements ActionListener {
    // policy context

    OrbacPolicyContext thisContext;
    // entity type displayed by this instance
    int entityType;
    // the organization tree contextual menu
    private JPopupMenu popupMenu = new JPopupMenu();
    private MouseListener popupListener = new PopupListener(this);
    private JMenuItem addEntityMenuItem;
    private JMenuItem editEntityMenuItem;
    private JMenuItem deleteEntityMenuItem;
    private JMenu assignClass;
    private MyTableModel tableModel = new MyTableModel();
    // used to have a more compact code
    String[] entityTypes = {"role", "activity", "view"};
    String[] prefixEntityTypes = {"a role", "an activity", "a view"};

    /**
     * Creates new form PanelAbstractEntity
     */
    public PanelAbstractEntity(OrbacPolicyContext thisContext, int entityType) {
        initComponents();
        
        assignClass = new JMenu("Assign class");
        assignClass.addActionListener(this);

        // setup tree transfer handler for drag and drop
        jTreeEntityHierarchy.setTransferHandler(new ToTransferHandler(TransferHandler.COPY));
        
        // setup tree renderer
        URL url1 = NewMotorbacView.class.getResource("/newmotorbac/resources/" + entityTypes[entityType] + "_new.png");
        if (url1 != null) {
            ImageIcon img1 = new ImageIcon(url1);
            jTreeEntityHierarchy.setCellRenderer(new SimpleCellRenderer(img1, img1));
        }

        // setup interface
        DefaultTableModel m = (DefaultTableModel) jTableAssignments.getModel();
        TitledBorder b = (TitledBorder) jPanelAssignments.getBorder();
        switch (entityType) {
            case AbstractOrbacPolicy.TYPE_ROLE:
                b.setTitle("Subject assignments");
                String[] sc = {"Empowered subject", "Organization"};
                m.setColumnIdentifiers(sc);
                break;
            case AbstractOrbacPolicy.TYPE_ACTIVITY:
                b.setTitle("Action assignments");
                String[] ac = {"Considered action", "Organization"};
                m.setColumnIdentifiers(ac);
                break;
            case AbstractOrbacPolicy.TYPE_VIEW:
                b.setTitle("Objects assignments");
                String[] vc = {"Used object", "Organization"};
                m.setColumnIdentifiers(vc);
                break;
        }
        // display a message in the information area to tell the user to select an entity
        //jSplitPane1.setBottomComponent(new JLabel("Select " + prefixEntityTypes[entityType]));

        // set selection model
        jTreeEntityHierarchy.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // configure popup menu
        addEntityMenuItem = new JMenuItem("add " + entityTypes[entityType]);
        editEntityMenuItem = new JMenuItem("edit " + entityTypes[entityType]);
        deleteEntityMenuItem = new JMenuItem("delete " + entityTypes[entityType]);
        popupMenu.add(addEntityMenuItem);
        popupMenu.add(editEntityMenuItem);
        popupMenu.add(deleteEntityMenuItem);
        popupMenu.add(assignClass);
        addEntityMenuItem.addActionListener(this);
        editEntityMenuItem.addActionListener(this);
        deleteEntityMenuItem.addActionListener(this);

        // add menu to tree
        jTreeEntityHierarchy.addMouseListener(popupListener);

        // store context
        this.thisContext = thisContext;
        this.entityType = entityType;
        // update hierarchy
        UpdateHierarchy();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() instanceof JMenuItem) {
            JMenuItem source = (JMenuItem) evt.getSource();

            // get selected abstract entity in the tree
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeEntityHierarchy.getLastSelectedPathComponent();
            String selectedEntity = (node == null) ? null : node.toString();
            
            if(selectedEntity == null) {
                return;
            }
            try {
                if (source == addEntityMenuItem) {
                    switch (entityType) {
                        case AbstractOrbacPolicy.TYPE_ROLE:
                            AddRole(selectedEntity);
                            break;
                        case AbstractOrbacPolicy.TYPE_ACTIVITY:
                            AddActivity(selectedEntity);
                            break;
                        case AbstractOrbacPolicy.TYPE_VIEW:
                            AddView(selectedEntity);
                            break;
                    }
                } else if (source == editEntityMenuItem) {
                    // display the same dialog than when creating a new abstract entity
                    String oldName = selectedEntity;
                    Set<String> entities = null;
                    Set<String> superEntities = null;
                    switch (entityType) {
                        case AbstractOrbacPolicy.TYPE_ROLE:
                            entities = thisContext.thePolicy.GetRolesList(!thisContext.adorbacViewActive);
                            superEntities = thisContext.thePolicy.GetSuperRoles(thisContext.currentOrganization, selectedEntity);
                            break;
                        case AbstractOrbacPolicy.TYPE_ACTIVITY:
                            entities = thisContext.thePolicy.GetActivitiesList(!thisContext.adorbacViewActive);
                            superEntities = thisContext.thePolicy.GetSuperActivities(thisContext.currentOrganization, selectedEntity);
                            break;
                        case AbstractOrbacPolicy.TYPE_VIEW:
                            entities = thisContext.thePolicy.GetViewsList(!thisContext.adorbacViewActive);
                            superEntities = thisContext.thePolicy.GetSuperViews(thisContext.currentOrganization, selectedEntity);
                            break;
                    }
                    // the list includes the current entity itself, remove it
                    entities.remove(selectedEntity);
                    // the list includes the current entity itself, remove it
                    superEntities.remove(selectedEntity);

                    // display dialog
                    jDialogAddAbstractEntity editEntity = new jDialogAddAbstractEntity(findActiveFrame(), entityTypes[entityType], selectedEntity, entities, superEntities, true);
                    editEntity.setLocationRelativeTo(findActiveFrame());
                    NewMotorbacApp.getApplication().show(editEntity);

                    // get result if not cancelled
                    if (editEntity.canceled == false) {
                        // change entity name
                        if (oldName.equals(editEntity.GetEntityName()) == false) {
                            switch (entityType) {
                                case AbstractOrbacPolicy.TYPE_ROLE:
                                    thisContext.thePolicy.RenameRole(oldName, editEntity.GetEntityName());
                                    break;
                                case AbstractOrbacPolicy.TYPE_ACTIVITY:
                                    thisContext.thePolicy.RenameActivity(oldName, editEntity.GetEntityName());
                                    break;
                                case AbstractOrbacPolicy.TYPE_VIEW:
                                    thisContext.thePolicy.RenameView(oldName, editEntity.GetEntityName());
                                    break;
                            }
                            selectedEntity = editEntity.GetEntityName();
                        }

                        // change hierarchy
                        Set<String> nse = editEntity.GetSuperEntities();
                        // first check for removed super entities
                        Vector<String> entitiesToRemove = new Vector<String>();
                        for (String ce : superEntities) {
                            if (nse.contains(ce) == false) {
                                entitiesToRemove.add(ce);
                            }
                        }
                        // check for added super entities
                        Vector<String> entitiesToAdd = new Vector<String>();
                        for (String ce : nse) {
                            if (superEntities.contains(ce) == false) {
                                entitiesToAdd.add(ce);
                            }
                        }
                        // now modify the hierarchy
                        switch (entityType) {
                            case AbstractOrbacPolicy.TYPE_ROLE:
                                for (int i = 0; i < entitiesToRemove.size(); i++) {
                                    thisContext.thePolicy.DeleteRoleHierarchy(selectedEntity, entitiesToRemove.elementAt(i), thisContext.currentOrganization);
                                }
                                for (int i = 0; i < entitiesToAdd.size(); i++) {
                                    thisContext.thePolicy.CreateRoleHierarchy(selectedEntity, entitiesToAdd.elementAt(i), thisContext.currentOrganization);
                                }
                                break;
                            case AbstractOrbacPolicy.TYPE_ACTIVITY:
                                for (int i = 0; i < entitiesToRemove.size(); i++) {
                                    thisContext.thePolicy.DeleteActivityHierarchy(selectedEntity, entitiesToRemove.elementAt(i), thisContext.currentOrganization);
                                }
                                for (int i = 0; i < entitiesToAdd.size(); i++) {
                                    thisContext.thePolicy.CreateActivityHierarchy(selectedEntity, entitiesToAdd.elementAt(i), thisContext.currentOrganization);
                                }
                                break;
                            case AbstractOrbacPolicy.TYPE_VIEW:
                                for (int i = 0; i < entitiesToRemove.size(); i++) {
                                    thisContext.thePolicy.DeleteViewHierarchy(selectedEntity, entitiesToRemove.elementAt(i), thisContext.currentOrganization);
                                }
                                for (int i = 0; i < entitiesToAdd.size(); i++) {
                                    thisContext.thePolicy.CreateViewHierarchy(selectedEntity, entitiesToAdd.elementAt(i), thisContext.currentOrganization);
                                }
                                break;
                        }

                        // refresh entity tree
                        UpdateHierarchy();
                    }
                } else if (source == deleteEntityMenuItem) {
                    switch (entityType) {
                        case AbstractOrbacPolicy.TYPE_ROLE:
                            thisContext.thePolicy.RemoveRoleFromOrg(selectedEntity, thisContext.currentOrganization);
                            break;
                        case AbstractOrbacPolicy.TYPE_ACTIVITY:
                            thisContext.thePolicy.RemoveActivityFromOrg(selectedEntity, thisContext.currentOrganization);
                            break;
                        case AbstractOrbacPolicy.TYPE_VIEW:
                            thisContext.thePolicy.RemoveViewFromOrg(selectedEntity, thisContext.currentOrganization);
                            break;
                    }
                } else if (source.getParent() == assignClass.getPopupMenu()) {
                    try {
                        thisContext.thePolicy.AssignAbstractEntityToClass(selectedEntity, source.getText());

                        // refresh selected entity information
                        RefreshSelectedEntityInformation();

                        // save the modified policy in history if operation is successful
                        thisContext.panelPolicy.PushPolicy();
                    } catch (COrbacException e) {
                        System.out.println("actionPerformed():" + e);
                        e.printStackTrace();
                    }
                }

                // push policy on undo/redo stack
                thisContext.panelPolicy.PushPolicy();
            } catch (COrbacException e) {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
            }
        }
    }

    public void UpdateHierarchy() {
        DefaultMutableTreeNode root = null;
        try {
            if (thisContext.currentOrganization == null) {
                // get all entities without hierarchies
                Set<String> list = null;
                switch (entityType) {
                    case AbstractOrbacPolicy.TYPE_ROLE:
                        list = thisContext.thePolicy.GetRolesList(!thisContext.adorbacViewActive);
                        // build tree
                        root = new DefaultMutableTreeNode("All roles (no hierarchy)");
                        break;
                    case AbstractOrbacPolicy.TYPE_ACTIVITY:
                        list = thisContext.thePolicy.GetActivitiesList(!thisContext.adorbacViewActive);
                        // build tree
                        root = new DefaultMutableTreeNode("All activities (no hierarchy)");
                        break;
                    case AbstractOrbacPolicy.TYPE_VIEW:
                        list = thisContext.thePolicy.GetViewsList(!thisContext.adorbacViewActive);
                        // build tree
                        root = new DefaultMutableTreeNode("All views (no hierarchy)");
                        break;
                }
                Iterator<String> il = list.iterator();
                while (il.hasNext()) {
                    DefaultMutableTreeNode subEntityNode = new DefaultMutableTreeNode(il.next());

                    root.add(subEntityNode);
                }
                jTreeEntityHierarchy.setModel(new DefaultTreeModel(root));
            } else {
                // build tree
                switch (entityType) {
                    case AbstractOrbacPolicy.TYPE_ROLE:
                        // build tree
                        root = new DefaultMutableTreeNode("All roles");
                        thisContext.thePolicy.GetAssociatedRolesHierarchy(root,
                                thisContext.currentOrganization,
                                !thisContext.adorbacViewActive);
                        break;
                    case AbstractOrbacPolicy.TYPE_ACTIVITY:
                        // build tree
                        root = new DefaultMutableTreeNode("All activities");
                        thisContext.thePolicy.GetAssociatedActivitiesHierarchy(root,
                                thisContext.currentOrganization,
                                !thisContext.adorbacViewActive);
                        break;
                    case AbstractOrbacPolicy.TYPE_VIEW:
                        // build tree
                        root = new DefaultMutableTreeNode("All views");
                        thisContext.thePolicy.GetAssociatedViewsHierarchy(root,
                                thisContext.currentOrganization,
                                !thisContext.adorbacViewActive);
                        break;
                }
                jTreeEntityHierarchy.setModel(new DefaultTreeModel(root));
                expandAll(jTreeEntityHierarchy, true);
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeEntityHierarchy = new javax.swing.JTree();
        jPanelAssignments = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableAssignments = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        attributesTable = new javax.swing.JTable();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(200, 124));
        setLayout(new java.awt.GridLayout(1, 3, 1, 0));

        jScrollPane1.setMinimumSize(new java.awt.Dimension(250, 25));
        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 402));

        jTreeEntityHierarchy.setName("jTreeEntityHierarchy"); // NOI18N
        jTreeEntityHierarchy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTreeEntityHierarchyMouseClicked(evt);
            }
        });
        jTreeEntityHierarchy.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeEntityHierarchyValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTreeEntityHierarchy);

        add(jScrollPane1);

        jPanelAssignments.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanelAssignments.setName("jPanelAssignments"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTableAssignments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Assigned entity", "Organization"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAssignments.setName("jTableAssignments"); // NOI18N
        jTableAssignments.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableAssignments);

        javax.swing.GroupLayout jPanelAssignmentsLayout = new javax.swing.GroupLayout(jPanelAssignments);
        jPanelAssignments.setLayout(jPanelAssignmentsLayout);
        jPanelAssignmentsLayout.setHorizontalGroup(
            jPanelAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 329, Short.MAX_VALUE)
            .addGroup(jPanelAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelAssignmentsLayout.createSequentialGroup()
                    .addGap(0, 164, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 165, Short.MAX_VALUE)))
        );
        jPanelAssignmentsLayout.setVerticalGroup(
            jPanelAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
            .addGroup(jPanelAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelAssignmentsLayout.createSequentialGroup()
                    .addGap(0, 60, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 60, Short.MAX_VALUE)))
        );

        add(jPanelAssignments);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        attributesTable.setModel(tableModel);
        attributesTable.setName("attributesTable"); // NOI18N
        jScrollPane3.setViewportView(attributesTable);

        add(jScrollPane3);
    }// </editor-fold>//GEN-END:initComponents

    private void jTreeEntityHierarchyValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeEntityHierarchyValueChanged
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeEntityHierarchy.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        if (node == jTreeEntityHierarchy.getModel().getRoot()) {
            // nothing to do, just display a message in the information area to tell the user to select an entity
            //jSplitPane1.setBottomComponent(new JLabel("Select " + prefixEntityTypes[entityType]));
        } else {
            // restore list if necessary
            //jSplitPane1.setBottomComponent(jPanelAssignments);

            // display info related to the selected entity
            String abstractEntity = node.getUserObject().toString();
            DisplayEntityInformation(abstractEntity);
        }
    }//GEN-LAST:event_jTreeEntityHierarchyValueChanged

    private void jTreeEntityHierarchyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTreeEntityHierarchyMouseClicked

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeEntityHierarchy.getLastSelectedPathComponent();
        if (node != null) {
            try {
                Map<String, String> membersValues = thisContext.thePolicy.GetAbstractEntityClassMembersAndValues(node.toString());
                if (membersValues != null) {
                    tableModel.SetData(membersValues, node.toString());
                }
            } catch (COrbacException ex) {
                Logger.getLogger(PanelAbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTreeEntityHierarchyMouseClicked

    // display information about the selected node in the tree
    public void RefreshSelectedEntityInformation() {
        // display info related to the selected entity
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeEntityHierarchy.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        String abstractEntity = node.getUserObject().toString();
        DisplayEntityInformation(abstractEntity);
    }
    // display the given entity information in the table

    public void DisplayEntityInformation(String entity) {
        // display entity assignments related to the given entity
        DefaultTableModel m = (DefaultTableModel) jTableAssignments.getModel();
        m.setRowCount(0);
        try {
            switch (entityType) {
                case AbstractOrbacPolicy.TYPE_ROLE:
                    HashSet<CRoleAssignment> ra = thisContext.thePolicy.GetRoleAssignments(entity);
                    // fill model
                    for (CRoleAssignment rai : ra) {
                        String[] data = new String[2];
                        data[0] = rai.GetSubject();
                        data[1] = rai.GetOrganization();
                        m.addRow(data);
                    }
                    break;
                case AbstractOrbacPolicy.TYPE_ACTIVITY:
                    HashSet<CActivityAssignment> aa = thisContext.thePolicy.GetActivityAssignments(entity);
                    // fill model
                    for (CActivityAssignment aai : aa) {
                        String[] data = new String[2];
                        data[0] = aai.GetAction();
                        data[1] = aai.GetOrganization();
                        m.addRow(data);
                    }
                    break;
                case AbstractOrbacPolicy.TYPE_VIEW:
                    HashSet<CViewAssignment> va = thisContext.thePolicy.GetViewAssignments(entity);
                    // fill model
                    for (CViewAssignment vai : va) {
                        String[] data = new String[2];
                        data[0] = vai.GetObject();
                        data[1] = vai.GetOrganization();
                        m.addRow(data);
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    // add a role
    private void AddRole(String superEntity) throws COrbacException {
        // display modal dialog box
        Set<String> roles = thisContext.thePolicy.GetRolesList(!thisContext.adorbacViewActive);
        jDialogAddAbstractEntity createRole = new jDialogAddAbstractEntity(findActiveFrame(), "role", roles, superEntity, thisContext.currentOrganization, true);
        createRole.setLocationRelativeTo(findActiveFrame());
        NewMotorbacApp.getApplication().show(createRole);
        // get result if not cancelled
        if (createRole.canceled == false) {
            // create role
            thisContext.thePolicy.CreateRoleAndInsertIntoOrg(createRole.GetEntityName(), thisContext.currentOrganization);
            // create hierarchy if necessary
            for (String superRole : createRole.GetSuperEntities()) {
                thisContext.thePolicy.CreateRoleHierarchy(createRole.GetEntityName(), superRole, thisContext.currentOrganization);
            }

            // refresh abstract entities trees
            UpdateHierarchy();
        }
    }
    // add an activity

    private void AddActivity(String superEntity) throws COrbacException {
        // display modal dialog box
        Set<String> activities = thisContext.thePolicy.GetActivitiesList(!thisContext.adorbacViewActive);
        jDialogAddAbstractEntity createActivity = new jDialogAddAbstractEntity(findActiveFrame(), "activity", activities, superEntity, thisContext.currentOrganization, true);
        createActivity.setLocationRelativeTo(findActiveFrame());
        NewMotorbacApp.getApplication().show(createActivity);
        // get result if not cancelled
        if (createActivity.canceled == false) {
            // create activity
            thisContext.thePolicy.CreateActivityAndInsertIntoOrg(createActivity.GetEntityName(), thisContext.currentOrganization);
            // create hierarchy if necessary
            for (String superActivity : createActivity.GetSuperEntities()) {
                thisContext.thePolicy.CreateActivityHierarchy(createActivity.GetEntityName(), superActivity, thisContext.currentOrganization);
            }

            // refresh abstract entities trees
            UpdateHierarchy();
        }
    }
    // add a view

    private void AddView(String superEntity) throws COrbacException {
        // display modal dialog box
        Set<String> views = thisContext.thePolicy.GetViewsList(!thisContext.adorbacViewActive);
        jDialogAddAbstractEntity createView = new jDialogAddAbstractEntity(findActiveFrame(), "view", views, superEntity, thisContext.currentOrganization, true);
        createView.setLocationRelativeTo(findActiveFrame());
        NewMotorbacApp.getApplication().show(createView);
        // get result if not cancelled
        if (createView.canceled == false) {
            // create view
            thisContext.thePolicy.CreateViewAndInsertIntoOrg(createView.GetEntityName(), thisContext.currentOrganization);
            // create hierarchy if necessary
            for (String superView : createView.GetSuperEntities()) {
                thisContext.thePolicy.CreateViewHierarchy(createView.GetEntityName(), superView, thisContext.currentOrganization);
            }

            // refresh abstract entities trees
            UpdateHierarchy();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable attributesTable;
    private javax.swing.JPanel jPanelAssignments;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableAssignments;
    private javax.swing.JTree jTreeEntityHierarchy;
    // End of variables declaration//GEN-END:variables

    // abstract entities specific drag and drop handler
    class ToTransferHandler extends TransferHandler {

        private static final long serialVersionUID = 1L;
        int action;
        // to know which icon from the toolbox has been dragged
        boolean toolBoxItemDropped = false;
        boolean toolBoxRuleDropped = false;
        int draggedRuleType;
        // multiple selection drag and drop support
        String[] entities;
        boolean multipleConcreteEntitiesDropped = false;

        public ToTransferHandler(int action) {
            this.action = action;
        }

        public boolean canImport(TransferHandler.TransferSupport support) {
            boolean dragOk = false;
            toolBoxItemDropped = false;
            toolBoxRuleDropped = false;
            multipleConcreteEntitiesDropped = false;

            // we only support drops (not clipboard paste)
            if (!support.isDrop()) {
                return false;
            }

            // we only import Strings
            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }

            // if no organization is selected then forbid the drag and drop
            if (thisContext.currentOrganization == null) {
                return false;
            }

            try {
                // get dragged entity
                String draggedEntity = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);

                // check for multiple selection drag and drop
                if (draggedEntity.contains("\n")) {
                    // get entities
                    entities = Pattern.compile("\n").split(draggedEntity);
                    multipleConcreteEntitiesDropped = true;
                    dragOk = true;
                } // check if an abstract entity icon is dropped to create a new entity
                else if (draggedEntity.contains("role_new.png") || draggedEntity.contains("activity_new.png") || draggedEntity.contains("view_new.png")) {
                    // a toolbar icon is beeing dragged
                    if ((draggedEntity.contains("role_new.png") && entityType == AbstractOrbacPolicy.TYPE_ROLE)
                            || (draggedEntity.contains("activity_new.png") && entityType == AbstractOrbacPolicy.TYPE_ACTIVITY)
                            || (draggedEntity.contains("view_new.png") && entityType == AbstractOrbacPolicy.TYPE_VIEW)) {
                        toolBoxItemDropped = true;
                        dragOk = true;
                    }
                } // check if an abstract rule icon is dropped to create a new abstract rule
                else if (draggedEntity.contains("rule_permission.png") || draggedEntity.contains("rule_prohibition.png") || draggedEntity.contains("rule_obligation.png")) {
                    if (draggedEntity.contains("rule_permission.png")) {
                        draggedRuleType = AbstractOrbacPolicy.TYPE_PERMISSION;
                    } else if (draggedEntity.contains("rule_prohibition.png")) {
                        draggedRuleType = AbstractOrbacPolicy.TYPE_PROHIBITION;
                    } else if (draggedEntity.contains("rule_obligation.png")) {
                        draggedRuleType = AbstractOrbacPolicy.TYPE_OBLIGATION;
                    }
                    toolBoxRuleDropped = true;
                    dragOk = true;
                } else {
                    // check the dragged item type to only authorize drop on abstract entities
                    // corresponding to the dragged concrete entity
                    Set<String> concreteEntities = null;
                    switch (entityType) {
                        case AbstractOrbacPolicy.TYPE_ROLE:
                            // the dragged entity must be a subject
                            concreteEntities = thisContext.thePolicy.GetSubjects();
                            break;
                        case AbstractOrbacPolicy.TYPE_ACTIVITY:
                            // the dragged entity must be an action
                            concreteEntities = thisContext.thePolicy.GetActions();
                            break;
                        case AbstractOrbacPolicy.TYPE_VIEW:
                            // the dragged entity must be a subject or an object
                            concreteEntities = thisContext.thePolicy.GetSubjects();
                            concreteEntities.addAll(thisContext.thePolicy.GetObjects());
                            break;
                    }

                    dragOk = concreteEntities.contains(draggedEntity);
                }

            } catch (UnsupportedFlavorException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            } catch (Exception e) {
                return false;
            }

            boolean actionSupported = (action & support.getSourceDropActions()) == action;
            if (actionSupported && dragOk) {
                support.setDropAction(action);
                return true;
            }

            return false;
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            // if we can't handle the import, say so
            if (!canImport(support)) {
                return false;
            }

            // fetch the drop location
            JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
            // get the abstract entity
            String abstractEntity = dl.getPath().getLastPathComponent().toString();

            try {
                if (multipleConcreteEntitiesDropped) {
                    for (int i = 0; i < entities.length; i++) {
                        switch (entityType) {
                            case AbstractOrbacPolicy.TYPE_ROLE:
                                thisContext.thePolicy.Empower(thisContext.currentOrganization, entities[i], abstractEntity);
                                break;
                            case AbstractOrbacPolicy.TYPE_ACTIVITY:
                                thisContext.thePolicy.Consider(thisContext.currentOrganization, entities[i], abstractEntity);
                                break;
                            case AbstractOrbacPolicy.TYPE_VIEW:
                                thisContext.thePolicy.Use(thisContext.currentOrganization, entities[i], abstractEntity);
                                break;
                        }
                    }
                    // refresh abstract entity panel
                    DisplayEntityInformation(abstractEntity);
                } else if (toolBoxItemDropped) {
                    // first check if the tool has been dropped on an existing abstract entity or
                    // the root of the tree
                    if (abstractEntity.contains("All ")) {
                        abstractEntity = null;
                    }
                    // create selected abstract entity
                    if (entityType == AbstractOrbacPolicy.TYPE_ROLE) {
                        AddRole(abstractEntity);
                    } else if (entityType == AbstractOrbacPolicy.TYPE_ACTIVITY) {
                        AddActivity(abstractEntity);
                    } else if (entityType == AbstractOrbacPolicy.TYPE_VIEW) {
                        AddView(abstractEntity);
                    }
                } else if (toolBoxRuleDropped) {
                    jDialogCreateAbstractRule createRule = null;
                    switch (draggedRuleType) {
                        case AbstractOrbacPolicy.TYPE_PERMISSION:
                            createRule = new jDialogCreateAbstractRule(findActiveFrame(), true,
                                    thisContext.currentOrganization,
                                    abstractEntity, null, null,
                                    thisContext.thePolicy,
                                    !thisContext.adorbacViewActive,
                                    AbstractOrbacPolicy.TYPE_PERMISSION);
                            createRule.setLocationRelativeTo(findActiveFrame());
                            NewMotorbacApp.getApplication().show(createRule);

                            if (createRule.canceled) {
                                return false;
                            }

                            // create the rule
                            thisContext.thePolicy.AbstractPermission(thisContext.currentOrganization,
                                    createRule.GetFirstEntity(),
                                    createRule.GetSecondEntity(),
                                    createRule.GetThirdEntity(),
                                    createRule.GetContext(),
                                    createRule.GetRuleName());
                            break;
                        case AbstractOrbacPolicy.TYPE_PROHIBITION:
                            createRule = new jDialogCreateAbstractRule(findActiveFrame(), true,
                                    thisContext.currentOrganization,
                                    null, abstractEntity, null,
                                    thisContext.thePolicy,
                                    !thisContext.adorbacViewActive,
                                    AbstractOrbacPolicy.TYPE_PROHIBITION);
                            createRule.setLocationRelativeTo(findActiveFrame());
                            NewMotorbacApp.getApplication().show(createRule);

                            if (createRule.canceled) {
                                return false;
                            }

                            // create the rule
                            thisContext.thePolicy.AbstractProhibition(thisContext.currentOrganization,
                                    createRule.GetFirstEntity(),
                                    createRule.GetSecondEntity(),
                                    createRule.GetThirdEntity(),
                                    createRule.GetContext(),
                                    createRule.GetRuleName());
                            break;
                        case AbstractOrbacPolicy.TYPE_OBLIGATION:
                            createRule = new jDialogCreateAbstractRule(findActiveFrame(), true,
                                    thisContext.currentOrganization,
                                    null, null, abstractEntity,
                                    thisContext.thePolicy,
                                    !thisContext.adorbacViewActive,
                                    AbstractOrbacPolicy.TYPE_OBLIGATION);
                            createRule.setLocationRelativeTo(findActiveFrame());
                            NewMotorbacApp.getApplication().show(createRule);

                            if (createRule.canceled) {
                                return false;
                            }

                            // create the rule
                            thisContext.thePolicy.AbstractObligation(thisContext.currentOrganization,
                                    createRule.GetFirstEntity(),
                                    createRule.GetSecondEntity(),
                                    createRule.GetThirdEntity(),
                                    createRule.GetContext(),
                                    createRule.GetViolationContext(),
                                    createRule.GetRuleName());
                            break;
                    }

                    // update abstract rule tab
                    thisContext.panelAbstractRules.UpdateTabs();
                } else {
                    // get the concrete entity and bail if this fails
                    String concreteEntity = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    // all ok, we assoeciate the conrete entity and the abstract entity and refresh
                    switch (entityType) {
                        case AbstractOrbacPolicy.TYPE_ROLE:
                            thisContext.thePolicy.Empower(thisContext.currentOrganization, concreteEntity, abstractEntity);
                            thisContext.panelSubjects.SetSelectedEntity(concreteEntity);
                            break;
                        case AbstractOrbacPolicy.TYPE_ACTIVITY:
                            thisContext.thePolicy.Consider(thisContext.currentOrganization, concreteEntity, abstractEntity);
                            thisContext.panelActions.SetSelectedEntity(concreteEntity);
                            break;
                        case AbstractOrbacPolicy.TYPE_VIEW:
                            thisContext.thePolicy.Use(thisContext.currentOrganization, concreteEntity, abstractEntity);
                            thisContext.panelObjects.UpdateEntityList();
                            thisContext.panelObjects.SetSelectedEntity(concreteEntity);
                            break;
                    }
                    // refresh abstract entity panel
                    DisplayEntityInformation(abstractEntity);
                }

                // push policy on undo/redo stack
                thisContext.panelPolicy.PushPolicy();
            } catch (UnsupportedFlavorException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            } catch (CViolatedEntityDefinitionException vee) {
                // violated entity definition
                JOptionPane.showMessageDialog(null, vee.getMessage());
                return false;
            } catch (CViolatedConstraintException vce) {
                // violated separation constraint
                String mess = "";
                mess += vce.getMessage() + ":\n";
                Iterator<?> isc = vce.GetInformation().iterator();
                while (isc.hasNext()) {
                    CSeparationConstraint sc = (CSeparationConstraint) isc.next();
                    mess += sc.GetFirstEntity() + " is separated with " + sc.GetSecondEntity() + "\n";
                }
                JOptionPane.showMessageDialog(null, mess);
                return false;
            } catch (COrbacException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage());
                return false;
            }

            return true;
        }
    }

    // If expand is true, expands all nodes in the tree
    // otherwise, collapses all nodes in the tree
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
    // helper function to find a frame object to display a dialog box for example

    private Frame findActiveFrame() {
        Frame[] frames = JFrame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            Frame frame = frames[i];
            if (frame.isVisible()) {
                return frame;
            }
        }
        return null;
    }
    // popup menu mouse handler

    class PopupListener extends MouseAdapter {

        private ActionListener parent;

        public PopupListener(ActionListener parent) {
            this.parent = parent;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                // the event source is the organization tree
                TreePath tp = jTreeEntityHierarchy.getPathForLocation(e.getX(), e.getY());
                if (tp == null) {
                    return;
                }
                DefaultMutableTreeNode n = (DefaultMutableTreeNode) tp.getLastPathComponent();
                jTreeEntityHierarchy.setSelectionPath(jTreeEntityHierarchy.getPathForLocation(e.getX(), e.getY()));

                // disable some menu entries if the abstract entity tree root is selected
                editEntityMenuItem.setEnabled(!n.toString().contains("All "));
                deleteEntityMenuItem.setEnabled(!n.toString().contains("All "));


                // possibly multiple selections
                int[] selectedItems = jTreeEntityHierarchy.getSelectionRows();
                if (selectedItems.length > 0 && thisContext.currentOrganization != null) {
                    try {
                        // generate menu for multiple items
                        assignClass.removeAll();
                        Set<String> classes = thisContext.thePolicy.GetAbstractClassesList();
                        for (String c : classes) {
                            Set<String> instances = thisContext.thePolicy.GetAbstractClassInstancesNames(c);
                            if (instances != null && instances.contains(((DefaultMutableTreeNode) jTreeEntityHierarchy.getLastSelectedPathComponent()).toString())) {
                                continue;
                            }
                            JMenuItem mi = new JMenuItem(c);
                            mi.addActionListener(this.parent);
                            assignClass.add(mi);
                        }
                    } catch (COrbacException ex) {
                        Logger.getLogger(PanelAbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    // implement custom table model for class attributes edition
    class MyTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;
        // columns and data
        private String[] columnNames = {"Attribute", "Value"};
        private String[] attributesNames = new String[0];
        private String[] attributesValues = new String[0];
        // concrete entity associated with this data
        private String associatedEntity = "";
        
        public void SetData(Map<String, String> dataMap, String associatedEntity) {
            // store entity name
            this.associatedEntity = associatedEntity;
            // allocate memory
            attributesNames = new String[dataMap.size()];
            attributesValues = new String[dataMap.size()];
            // copy content
            Set< Map.Entry<String, String>> es = dataMap.entrySet();
            Iterator< Map.Entry<String, String>> ies = es.iterator();
            int i = 0;
            while (ies.hasNext()) {
                Map.Entry<String, String> e = ies.next();
                attributesNames[i] = e.getKey();
                attributesValues[i] = e.getValue();
                i++;
            }
            fireTableDataChanged();
        }

        public void clear() {
            HashMap<String, String> dummyMap = new HashMap<String, String>();
            SetData(dummyMap, "dummy");
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return attributesValues.length;
        }

        public boolean isCellEditable(int row, int col) {
            // only attributes values can be edited
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        public Object getValueAt(int arg0, int arg1) {
            if (arg1 == 0) {
                // attribute name
                return attributesNames[arg0];
            } else {
                // attribute value
                return attributesValues[arg0];
            }
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public void setValueAt(Object value, int row, int col) {
            if (col == 0) {
                // attribute name, should never happen
            } else {
                // attribute value
                attributesValues[row] = (String) value;
                // reflect change in policy
                try {
                    thisContext.thePolicy.SetConcreteClassInstanceMemberValue(associatedEntity, attributesNames[row], attributesValues[row]);
                } catch (COrbacException e) {
                    e.printStackTrace();
                }
            }
            fireTableCellUpdated(row, col);
        }
    }
}
