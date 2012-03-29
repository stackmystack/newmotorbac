/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanelPolicy.java
 *
 * Created on May 5, 2011, 3:53:03 PM
 */

package newmotorbac;

import newmotorbac.util.SimpleCellRenderer;
import newmotorbac.util.RoleRevocationResult;
import newmotorbac.util.RuleRevocationResult;
import newmotorbac.util.RoleDelegationResult;
import newmotorbac.util.OrbacPolicyContext;
import newmotorbac.util.RuleDelegationResult;
import java.awt.Frame;
import newmotorbac.dialog.jDialogCreatePolicy;
import newmotorbac.dialog.jDialogAddAbstractEntity;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import newmotorbac.dialog.JDialogExportPolicy;
import newmotorbac.dialog.jDialogClassEditor;
import newmotorbac.dialog.jDialogDefinePassword;
import newmotorbac.dialog.jDialogLoadPolicy;
import newmotorbac.dialog.jDialogPlugins;
import newmotorbac.dialog.jDialogProperties;
import newmotorbac.dialog.jDialogRoleDelegation;
import newmotorbac.dialog.jDialogRuleDelegation;
import newmotorbac.dialog.jDialogUserAndPassword;
import newmotorbac.undoredo.PolicyHistoric;
import orbac.AbstractOrbacPolicy;
import orbac.COrbacCore;
import orbac.conflict.CConcreteConflict;
import orbac.exception.CInvalidUserException;
import orbac.exception.CNoCurrentUserSetException;
import orbac.exception.COrbacException;
import orbac.exception.CPasswordNotFoundException;

/**
 *
 * @author fabien
 */
public class PanelPolicy extends javax.swing.JPanel implements ActionListener {
    // GUI policy context
    OrbacPolicyContext thisContext;

    // GUI components
    private PanelAbstractEntities   panelAbstractEntities;
    private PanelContexts           panelContexts;
    private PanelAbstractRules      panelAbstractRules;
    private PanelConflicts          panelConflicts;
    private PanelEntityDefinitions  panelEntityDefinitions;
    private PanelSimulation         panelSimulation;
    private PanelConcreteEntity     panelSubjects, panelActions, panelObjects;
    // the organization tree contextual menu
    private JPopupMenu              popupMenu = new JPopupMenu();
    private MouseListener           popupListener = new PopupListener();
    private JMenuItem               addOrgMenuItem = new JMenuItem("add organization");
    private JMenuItem               editOrgMenuItem = new JMenuItem("edit organization");
    private JMenuItem               deleteOrgMenuItem = new JMenuItem("delete organization");
    // parent GUI
    private NewMotorbacView         parentGui;
    // associated plug-in dialog box
    private jDialogPlugins          pluginDiag;
    // associated class editor dialog box
    private jDialogClassEditor      classEditor;

    // undo/redo stack implementation
    private PolicyHistoric          undoRedoStack = new PolicyHistoric();

    /** Creates new form PanelPolicy */
    public PanelPolicy(jDialogCreatePolicy policyCreationData, NewMotorbacView parentGui) throws COrbacException {
        initComponents();

        this.parentGui = parentGui;

        // create policy then create a OrbacPolicyContext to instanciate other parts of the GUI
        AbstractOrbacPolicy policy = null;
        try
        {
            policy = COrbacCore.GetTheInstance().CreatePolicy( policyCreationData.jTextFieldPolicyName.getText(), (String)policyCreationData.jComboBoxImplementation.getSelectedItem() );
        }
        catch ( Exception e )
        {
            throw new COrbacException("Error loading policy: " + e.getMessage());
        }
        thisContext = new OrbacPolicyContext();
        thisContext.thePolicy = policy;
        thisContext.panelPolicy = this;

        // add modification listener
        //policy.AddPolicyRDFModelModificationListener(new JenaModelModificationListener());
        // add the motorbac GUI as a policy modification listener
        //policy.AddPolicyModificationListener(this);
        // add the motorbac GUI as a policy inference listener
        //policy.AddPolicyInferenceListener(this);

        // add requested adorbac permissions
        // organization view
        if ( policyCreationData.jCheckBoxManageOrganizationView.isSelected() )
        {
            System.out.println("adding adorbac rule: organization view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_org_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.abstractEntityViewName[ AbstractOrbacPolicy.TYPE_ORGANIZATION ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // role view
        if ( policyCreationData.jCheckBoxManageRoleView.isSelected() )
        {
            System.out.println("adding adorbac rule: role view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_role_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.abstractEntityViewName[ AbstractOrbacPolicy.TYPE_ROLE ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // activity view
        if ( policyCreationData.jCheckBoxManageActivityView.isSelected() )
        {
            System.out.println("adding adorbac rule: activity view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_activity_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.abstractEntityViewName[ AbstractOrbacPolicy.TYPE_ACTIVITY ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // view view
        if ( policyCreationData.jCheckBoxManageViewView.isSelected() )
        {
            System.out.println("adding adorbac rule: view view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_view_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.abstractEntityViewName[ AbstractOrbacPolicy.TYPE_VIEW ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // role assignment view
        if ( policyCreationData.jCheckBoxManageRoleAssignmentView.isSelected() )
        {
            System.out.println("adding adorbac rule: role assignment view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_role_assignment_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.assignementViewName[ AbstractOrbacPolicy.TYPE_ROLE ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // activity assignment view
        if ( policyCreationData.jCheckBoxManageActivityAssignmentView.isSelected() )
        {
            System.out.println("adding adorbac rule: activity assignment view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_activity_assignment_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.assignementViewName[ AbstractOrbacPolicy.TYPE_ACTIVITY ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // view assignment view
        if ( policyCreationData.jCheckBoxManageViewAssignmentView.isSelected() )
        {
            System.out.println("adding adorbac rule: view assignment view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_view_assignment_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.assignementViewName[ AbstractOrbacPolicy.TYPE_VIEW ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // role hierarchy view
        if ( policyCreationData.jCheckBoxManageRoleHierarchyView.isSelected() )
        {
            System.out.println("adding adorbac rule: role hierarchy view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_role_hierarchy_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.hierarchyViewName[ AbstractOrbacPolicy.TYPE_ROLE ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // activity hierarchy view
        if ( policyCreationData.jCheckBoxManageActivityHierarchyView.isSelected() )
        {
            System.out.println("adding adorbac rule: activity hierarchy view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_activity_hierarchy_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.hierarchyViewName[ AbstractOrbacPolicy.TYPE_ACTIVITY ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // view hierarchy view
        if ( policyCreationData.jCheckBoxManageViewHierarchyView.isSelected() )
        {
            System.out.println("adding adorbac rule: view hierarchy view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_view_hierarchy_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.hierarchyViewName[ AbstractOrbacPolicy.TYPE_VIEW ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // organization hierarchy view
        if ( policyCreationData.jCheckBoxManageOrganizationHierarchyView.isSelected() )
        {
            System.out.println("adding adorbac rule: organization hierarchy view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_organization_hierarchy_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.hierarchyViewName[ AbstractOrbacPolicy.TYPE_ORGANIZATION ],
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // license delegation view
        if ( policyCreationData.jCheckBoxManageLicenseDelegationView.isSelected() )
        {
            System.out.println("adding adorbac rule: license delegation view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_license_delegation_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.adorbacLicenceDelegationView,
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // license transfer view
        if ( policyCreationData.jCheckBoxManageLicenseTransferView.isSelected() )
        {
            System.out.println("adding adorbac rule: license transfer view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_license_transfer_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.adorbacLicenceTransferView,
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // grant option view
        if ( policyCreationData.jCheckBoxManageGrantOptionView.isSelected() )
        {
            System.out.println("adding adorbac rule: grant option view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_grant_option_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.adorbacGrantOptionView,
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // role delegation view
        if ( policyCreationData.jCheckBoxManageRoleDelegationView.isSelected() )
        {
            System.out.println("adding adorbac rule: role delegation view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_role_delegation_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.adorbacRoleDelegationView,
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }
        // role transfer view
        if ( policyCreationData.jCheckBoxManageRoleTransferView.isSelected() )
        {
            System.out.println("adding adorbac rule: role transfer view");
            policy.AddAdorbacLicense(AbstractOrbacPolicy.adorbacLicenseView,
                                     "admin_manage_role_transfer_view",
                                     AbstractOrbacPolicy.adorbacOrgNameStr,
                                     "admin",
                                     AbstractOrbacPolicy.adorbacManageActivity,
                                     AbstractOrbacPolicy.adorbacRoleTransferView,
                                     AbstractOrbacPolicy.defaultContext);
            System.out.println("adorbac rule added");
        }

        // create GUI context and other components
        CreateGuiComponents();

        // display policy data
        UpdateOrganizationTree();
        panelSubjects.UpdateEntityList();
        panelActions.UpdateEntityList();
        panelObjects.UpdateEntityList();

        // initialize undo/redo stack
        try
        {
            undoRedoStack.InitializeStack(policy.GetPolicyAsString());
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public PanelPolicy(File policyFile, NewMotorbacView parentGui) throws COrbacException
    {
        initComponents();

        this.parentGui = parentGui;

        // load the policy then create a OrbacPolicyContext to instanciate other parts of the GUI
        // display the loading dialog box

        // display modal dialog box
        JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
        jDialogLoadPolicy loadPolicyDialogBox = new jDialogLoadPolicy(mainFrame, policyFile, true);
        loadPolicyDialogBox.setLocationRelativeTo(mainFrame);
        NewMotorbacApp.getApplication().show(loadPolicyDialogBox);

        thisContext = new OrbacPolicyContext();
        thisContext.thePolicy = loadPolicyDialogBox.GetPolicy();
        thisContext.path = policyFile.getAbsolutePath();
        if ( thisContext.thePolicy == null )
            throw new COrbacException("Error loading policy \"" + policyFile + "\"");
        thisContext.panelPolicy = this;

        // create GUI context and other components
        CreateGuiComponents();

        // display policy data
        UpdateOrganizationTree();
        panelSubjects.UpdateEntityList();
        panelActions.UpdateEntityList();
        panelObjects.UpdateEntityList();

        // initialize undo/redo stack
        try
        {
            undoRedoStack.InitializeStack(thisContext.thePolicy.GetPolicyAsString());
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void CreateGuiComponents()
    {
        panelAbstractEntities = new PanelAbstractEntities(thisContext);
        panelContexts = new PanelContexts(thisContext);
        panelAbstractRules = new PanelAbstractRules(thisContext);
        panelConflicts = new PanelConflicts(thisContext);
        panelEntityDefinitions = new PanelEntityDefinitions(thisContext);
        panelSimulation = new PanelSimulation(thisContext);
        panelSubjects = new PanelConcreteEntity(thisContext, AbstractOrbacPolicy.TYPE_SUBJECT);
        panelActions = new PanelConcreteEntity(thisContext, AbstractOrbacPolicy.TYPE_ACTION);
        panelObjects = new PanelConcreteEntity(thisContext, AbstractOrbacPolicy.TYPE_OBJECT);
        // add them, depending on policy implementation
        jTabbedPaneAbstractPolicy.addTab("Abstract entities", panelAbstractEntities);
        jTabbedPaneAbstractPolicy.addTab("Contexts", panelContexts);
        jTabbedPaneAbstractPolicy.addTab("Abstract rules", panelAbstractRules);
        jTabbedPaneAbstractPolicy.addTab("Conflicts", panelConflicts);
        if ( thisContext.thePolicy.IsEntityDefinitionImplemented() )
            jTabbedPaneAbstractPolicy.addTab("Entity definitions", panelEntityDefinitions);
        jTabbedPaneAbstractPolicy.addTab("Concrete policy simulation", panelSimulation);
        jTabbedPaneConcreteEntities.addTab("Subjects", panelSubjects);
        jTabbedPaneConcreteEntities.addTab("Actions", panelActions);
        jTabbedPaneConcreteEntities.addTab("Objects", panelObjects);
        // store some of them in context
        thisContext.panelSubjects = panelSubjects;
        thisContext.panelActions = panelActions;
        thisContext.panelObjects = panelObjects;

        // configure popup menu
        popupMenu.add(addOrgMenuItem);
        popupMenu.add(editOrgMenuItem);
        popupMenu.add(deleteOrgMenuItem);
        addOrgMenuItem.addActionListener(this);
        editOrgMenuItem.addActionListener(this);
        deleteOrgMenuItem.addActionListener(this);

        DefaultListModel m = new DefaultListModel();
        jListTools.setModel(m);
        URL imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/organization_new.png");
        if( imgURL != null ) {
            ImageIcon img = new ImageIcon(imgURL);
            m.addElement(img);
        }
        imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/role_new.png");
        if( imgURL != null ) {
            ImageIcon img = new ImageIcon(imgURL);
            m.addElement(img);
        }
        imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/activity_new.png");
        if( imgURL != null ) {
            ImageIcon img = new ImageIcon(imgURL);
            m.addElement(img);
        }
        imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/view_new.png");
        if( imgURL != null ) {
            ImageIcon img = new ImageIcon(imgURL);
            m.addElement(img);
        }
        m = new DefaultListModel();
        jListRules.setModel(m);

        if ( thisContext.thePolicy.IsPermissionImplemented() )
        {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/rule_permission.png");
            if( imgURL != null ) {
                ImageIcon img = new ImageIcon(imgURL);
                m.addElement(img);
            }
        }
        if ( thisContext.thePolicy.IsProhibitionImplemented() )
        {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/rule_prohibition.png");
            if( imgURL != null ) {
                ImageIcon img = new ImageIcon(imgURL);
                m.addElement(img);
            }
        }
        if ( thisContext.thePolicy.IsObligationImplemented() )
        {
            imgURL = NewMotorbacView.class.getResource("/newmotorbac/resources/rule_obligation.png");
            if( imgURL != null ) {
                ImageIcon img = new ImageIcon(imgURL);
                m.addElement(img);
            }
        }
        jListRules.setToolTipText("Drag and drop a rule type over the organization tree to create a new abstract rule");

        // setup organization tree renderer
        URL url1 = NewMotorbacView.class.getResource("/newmotorbac/resources/organization_new.png");
        if( url1 != null )
        {
            ImageIcon img1 = new ImageIcon(url1);
            jTreeOrganizations.setCellRenderer(new SimpleCellRenderer(img1, img1));
        }

        // add organization tree drag and drop handler
        jTreeOrganizations.setTransferHandler(new ToTransferHandler(TransferHandler.COPY));
        // set selection model
        jTreeOrganizations.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // add menu to tree
        jTreeOrganizations.addMouseListener(popupListener);
    }

    public void UpdatePolicyDisplay()
    {
        // update all controls
        UpdateOrganizationTree();
        panelAbstractEntities.UpdateTabs();
        panelContexts.UpdateContextTable();
        panelAbstractRules.UpdateTabs();
        panelEntityDefinitions.UpdateEntityDefinitionTable();
        panelConflicts.UpdateTabs();
        panelSubjects.UpdateEntityList();
        panelActions.UpdateEntityList();
        panelObjects.UpdateEntityList();
    }

    private void UpdateOrganizationTree()
    {
        DefaultMutableTreeNode rootOrg = new DefaultMutableTreeNode("All organizations");
        try
        {
            thisContext.thePolicy.GetOrganizationsHierarchy(rootOrg, !thisContext.adorbacViewActive);
            //rootOrg = thisContext.thePolicy.GetOrganizationsHierarchy(!thisContext.adorbacViewActive);
        }
        catch (COrbacException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
        jTreeOrganizations.setModel(new DefaultTreeModel(rootOrg));
        expandAll(jTreeOrganizations, true);
    }

    public void actionPerformed(java.awt.event.ActionEvent evt)
    {
        if ( evt.getSource() instanceof JMenuItem )
        {
            JMenuItem source = (JMenuItem)evt.getSource();

            try
            {
                if ( source == addOrgMenuItem )
                {
                    // display modal dialog box
                    Set<String> orgs = thisContext.thePolicy.GetOrganizationsList(thisContext.adorbacViewActive);
                    String superOrg = (thisContext.currentOrganization == null) ? AbstractOrbacPolicy.adorbacOrgNameStr : thisContext.currentOrganization;
                    jDialogAddAbstractEntity createOrg = new jDialogAddAbstractEntity(findActiveFrame(), "organization", orgs, superOrg, superOrg, true);
                    createOrg.setLocationRelativeTo(findActiveFrame());
                    NewMotorbacApp.getApplication().show(createOrg);
                    // get result if not cancelled
                    if ( createOrg.canceled == false )
                    {
                        // create organization
                        thisContext.thePolicy.CreateOrganization(createOrg.GetEntityName());
                        // create hierarchy if necessary
                        for ( String so : createOrg.GetSuperEntities() )
                            thisContext.thePolicy.CreateOrganizationHierarchy(createOrg.GetEntityName(), so);

                        // refresh organization tree
                        UpdateOrganizationTree();
                        // push policy on undo/redo stack
                        PushPolicy();
                    }
                }
                else if( source == editOrgMenuItem )
                {
                    // display the same dialog than when creating a new organization
                    String oldName = thisContext.currentOrganization;
                    Set<String> orgs = thisContext.thePolicy.GetOrganizationsList(!thisContext.adorbacViewActive);
                    // the list includes the current organization itself, remove it
                    orgs.remove(thisContext.currentOrganization);
                    Set<String> superOrgs = thisContext.thePolicy.GetDirectSuperOrganizations(thisContext.currentOrganization);
                    // the list includes the current organization itself, remove it
                    superOrgs.remove(thisContext.currentOrganization);
                    // remove adorbac super org
                    superOrgs.remove(AbstractOrbacPolicy.adorbacOrgNameStr);

                    // display dialog
                    jDialogAddAbstractEntity editOrg = new jDialogAddAbstractEntity(findActiveFrame(), "organization", thisContext.currentOrganization, orgs, superOrgs, true);
                    editOrg.setLocationRelativeTo(findActiveFrame());
                    NewMotorbacApp.getApplication().show(editOrg);

                    // get result if not cancelled
                    if ( editOrg.canceled == false )
                    {
                        // change organization name
                        if ( oldName.equals( editOrg.GetEntityName() ) == false )
                        {
                            thisContext.thePolicy.RenameOrganization(oldName, editOrg.GetEntityName());
                            thisContext.currentOrganization = editOrg.GetEntityName();
                        }

                        // change hierarchy
                        Set<String> nse = editOrg.GetSuperEntities();
                        // first check for removed super entities
                        Vector<String> entitiesToRemove = new Vector<String>();
                        for ( String ce : superOrgs )
                        {
                            if ( nse.contains(ce) == false )
                                entitiesToRemove.add(ce);
                        }
                        // check for added super entities
                        Vector<String> entitiesToAdd = new Vector<String>();
                        for ( String ce : nse )
                        {
                            if ( superOrgs.contains(ce) == false )
                                entitiesToAdd.add(ce);
                        }
                        // now modify the hierarchy
                        for (int i = 0; i < entitiesToRemove.size(); i++)
                            thisContext.thePolicy.DeleteOrganizationHierarchy(thisContext.currentOrganization, entitiesToRemove.elementAt(i));
                        for (int i = 0; i < entitiesToAdd.size(); i++)
                            thisContext.thePolicy.CreateOrganizationHierarchy(thisContext.currentOrganization, entitiesToAdd.elementAt(i));

                        // refresh organization tree
                        UpdateOrganizationTree();
                        // push policy on undo/redo stack
                        PushPolicy();
                    }
                }
                else if( source == deleteOrgMenuItem )
                {
                    // warn user
                    int n = JOptionPane.showConfirmDialog(findActiveFrame(), "Deleting organization " + thisContext.currentOrganization + " will destroy all abstract entities defined in it. Confirm deletion?",
                                                          "Delete organization", JOptionPane.YES_NO_OPTION);
                    if ( n == JOptionPane.YES_OPTION )
                    {
                        thisContext.thePolicy.DeleteOrganization(thisContext.currentOrganization);
                        
                        // refresh organization tree
                        UpdateOrganizationTree();
                        // push policy on undo/redo stack
                        PushPolicy();
                    }
                }
            }
            catch ( COrbacException e )
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
            }
            catch ( Exception e )
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
            }
        }
    }

    // called by main window to display the plug-in dialog box
    public void ShowPluginDialog()
    {
        if ( pluginDiag == null )
        {
            pluginDiag = new jDialogPlugins(findActiveFrame(), true, thisContext.thePolicy, thisContext.path);
            pluginDiag.setLocationRelativeTo(findActiveFrame());
        }
        NewMotorbacApp.getApplication().show(pluginDiag);
    }
    // called by main window to display the class editor
    public void ShowClassEditor()
    {
        if ( classEditor == null )
        {
            classEditor = new jDialogClassEditor(findActiveFrame(), true, thisContext);
            classEditor.setLocationRelativeTo(findActiveFrame());
        }
        NewMotorbacApp.getApplication().show(classEditor);
    }
    // called by the class editor when closing
    public void ClassEditorClosing()
    {
        classEditor = null;
    }

    public void DisplayInformation()
    {
        JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
        jDialogProperties properties = new jDialogProperties(mainFrame, true, thisContext);
        properties.setLocationRelativeTo(mainFrame);
        NewMotorbacApp.getApplication().show(properties);

        if ( !properties.HasBeenCancelled() )
        {
            // modify some policy properties
            if ( thisContext.thePolicy.GetVersion() != properties.GetVersion() ||
                 thisContext.thePolicy.GetInformation().equals(properties.GetInformation()) == false )
            {
                thisContext.thePolicy.SetVersion(properties.GetVersion());
                thisContext.thePolicy.SetInformation(properties.GetInformation());
                
                // push policy on undo/redo stack
                PushPolicy();
            }
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
        jTreeOrganizations = new javax.swing.JTree();
        jSplitPane3 = new javax.swing.JSplitPane();
        jTabbedPaneAbstractPolicy = new javax.swing.JTabbedPane();
        jToolBarControls = new javax.swing.JToolBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListTools = new javax.swing.JList();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListRules = new javax.swing.JList();
        jTabbedPaneConcreteEntities = new javax.swing.JTabbedPane();

        setName("Form"); // NOI18N
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(100, 100));

        jSplitPane2.setDividerLocation(300);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setName("jSplitPane2"); // NOI18N
        jSplitPane2.setOneTouchExpandable(true);

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setLastDividerLocation(200);
        jSplitPane1.setName("jSplitPane1"); // NOI18N
        jSplitPane1.setPreferredSize(new java.awt.Dimension(165, 100));

        jScrollPane1.setMinimumSize(new java.awt.Dimension(150, 25));
        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(95, 20));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(PanelPolicy.class);
        jTreeOrganizations.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jTreeOrganizations.border.title"))); // NOI18N
        jTreeOrganizations.setMinimumSize(new java.awt.Dimension(95, 50));
        jTreeOrganizations.setName("jTreeOrganizations"); // NOI18N
        jTreeOrganizations.setPreferredSize(new java.awt.Dimension(93, 50));
        jTreeOrganizations.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeOrganizationsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTreeOrganizations);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jSplitPane3.setDividerLocation(38);
        jSplitPane3.setDividerSize(0);
        jSplitPane3.setEnabled(false);
        jSplitPane3.setName("jSplitPane3"); // NOI18N

        jTabbedPaneAbstractPolicy.setName("jTabbedPaneAbstractPolicy"); // NOI18N
        jSplitPane3.setRightComponent(jTabbedPaneAbstractPolicy);

        jToolBarControls.setFloatable(false);
        jToolBarControls.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBarControls.setRollover(true);
        jToolBarControls.setMaximumSize(new java.awt.Dimension(38, 32767));
        jToolBarControls.setMinimumSize(new java.awt.Dimension(30, 100));
        jToolBarControls.setName("jToolBarControls"); // NOI18N
        jToolBarControls.setPreferredSize(new java.awt.Dimension(30, 25));

        jScrollPane2.setMaximumSize(new java.awt.Dimension(32767, 100));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(35, 100));
        jScrollPane2.setName("jScrollPane2"); // NOI18N
        jScrollPane2.setPreferredSize(new java.awt.Dimension(35, 110));

        jListTools.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListTools.setToolTipText(resourceMap.getString("jListTools.toolTipText")); // NOI18N
        jListTools.setDragEnabled(true);
        jListTools.setMaximumSize(new java.awt.Dimension(50, 100));
        jListTools.setMinimumSize(new java.awt.Dimension(25, 100));
        jListTools.setName("jListTools"); // NOI18N
        jListTools.setPreferredSize(new java.awt.Dimension(25, 10));
        jScrollPane2.setViewportView(jListTools);

        jToolBarControls.add(jScrollPane2);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBarControls.add(jSeparator1);

        jScrollPane3.setMaximumSize(new java.awt.Dimension(32767, 100));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(40, 100));
        jScrollPane3.setName("jScrollPane3"); // NOI18N
        jScrollPane3.setPreferredSize(new java.awt.Dimension(40, 100));

        jListRules.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListRules.setDragEnabled(true);
        jListRules.setMaximumSize(new java.awt.Dimension(25, 70));
        jListRules.setMinimumSize(new java.awt.Dimension(25, 70));
        jListRules.setName("jListRules"); // NOI18N
        jListRules.setPreferredSize(new java.awt.Dimension(25, 70));
        jScrollPane3.setViewportView(jListRules);

        jToolBarControls.add(jScrollPane3);

        jSplitPane3.setLeftComponent(jToolBarControls);

        jSplitPane1.setRightComponent(jSplitPane3);

        jSplitPane2.setTopComponent(jSplitPane1);

        jTabbedPaneConcreteEntities.setName("jTabbedPaneConcreteEntities"); // NOI18N
        jSplitPane2.setRightComponent(jTabbedPaneConcreteEntities);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTreeOrganizationsValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeOrganizationsValueChanged
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTreeOrganizations.getLastSelectedPathComponent();
        if ( node == null ) return;
        if ( node == jTreeOrganizations.getModel().getRoot() )
        {
            // display all abstract entities
            thisContext.currentOrganization = null;
        }
        else
        {
            thisContext.currentOrganization = node.getUserObject().toString();
        }
        System.out.println("current org: " + thisContext.currentOrganization);

        // update interface to display information relevant to this organization
        panelAbstractRules.UpdateTabs();
        panelAbstractEntities.UpdateTabs();
        panelEntityDefinitions.SelectedOrganizationHasChanged();
        panelAbstractRules.SelectedOrganizationHasChanged();
        panelSubjects.RefreshMenu();
        panelActions.RefreshMenu();
        panelObjects.RefreshMenu();
    }//GEN-LAST:event_jTreeOrganizationsValueChanged

    // returns the associated policy
    public AbstractOrbacPolicy GetPolicy()
    {
        return thisContext.thePolicy;
    }
    // returns the associated policy path
    public String GetPolicyPath()
    {
        return thisContext.path;
    }
    // save the policy associated with this tab to the specified path
    // return false if saving failed
    public boolean SavePolicy(String path)
    {
        // ask policy object to write data at the specified path
        try
        {
            // save policy
            String[] options = { "RDF/XML-ABBREV" };
            thisContext.thePolicy.WritePolicyFile(path, options);
            // record given path if successful
            thisContext.path = path;
            // store in undo/redo stack the position at which the policy has been saved
            undoRedoStack.AssociatedPolicyHasBeenSaved();
        }
        catch (IOException e)
        {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }
    // save the policy associated with this tab to the recorded path
    // return false if saving failed
    public boolean SavePolicy()
    {
        // ask policy object to write data at the recorded path
        try
        {
            // save policy
            String[] options = { "RDF/XML-ABBREV" };
            thisContext.thePolicy.WritePolicyFile(thisContext.path, options);
            // store in undo/redo stack the position at which the policy has been saved
            undoRedoStack.AssociatedPolicyHasBeenSaved();
        }
        catch (IOException e)
        {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void ExportPolicy()
    {
        // display modal dialog box
        JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
        JDialogExportPolicy newPolicyDialogBox = new JDialogExportPolicy(mainFrame, true, thisContext.thePolicy);
        newPolicyDialogBox.setLocationRelativeTo(mainFrame);
        NewMotorbacApp.getApplication().show(newPolicyDialogBox);
    }
    
    // called before closing the tab
    public void ClosePolicy()
    {
        // we must close any open window related to this policy
        // context editors
        thisContext.panelContexts.CloseContextEditors();
        // entity definition editors
        thisContext.panelEntityDefinitions.CloseContextEditors();
    }

    // toggle adorbac policy for the associated policy
    public boolean ToggleAdorbac()
    {
        try
        {
            // switch state
            thisContext.thePolicy.SetAdorbacEnabled( !thisContext.thePolicy.IsAdorbacEnabled() );
        }
        catch ( CNoCurrentUserSetException e )
        {
            // if an exception is raised it means that the adorbac user is not set
            // open a dialog box to enter the user name and password
            // display modal dialog box
            JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
            jDialogUserAndPassword dialog = new jDialogUserAndPassword(mainFrame, true);
            dialog.setLocationRelativeTo(mainFrame);
            NewMotorbacApp.getApplication().show(dialog);
            if ( dialog.HasBeenCancelled() == false )
            {
                try
                {
                    thisContext.thePolicy.SetCurrentUser(dialog.GetUserName(), dialog.GetUserPassword());
                    // activate adorbac if login successful
                    thisContext.thePolicy.SetAdorbacEnabled(true);
                }
                catch ( CInvalidUserException iue )
                {
                    // the user does not exist in the policy
                    JOptionPane.showMessageDialog(findActiveFrame(), "Invalid user \"" + iue.GetInformation().elementAt(0) + "\"");
                }
                catch ( CPasswordNotFoundException pnfe )
                {
                    // adorbac user password is not set
                    // open a dialog box to set the password
                    // display modal dialog box
                    jDialogDefinePassword dialogPass = new jDialogDefinePassword(findActiveFrame(), dialog.GetUserName(), true);
                    dialogPass.setLocationRelativeTo(mainFrame);
                    NewMotorbacApp.getApplication().show(dialogPass);
                    if ( dialog.HasBeenCancelled() == false )
                    {
                        try
                        {
                            // store password
                            // old password is not defined
                            thisContext.thePolicy.SetUserPassword(dialog.GetUserName(), "", dialogPass.GetUserPassword());
                            thisContext.thePolicy.SetCurrentUser(dialog.GetUserName(), dialogPass.GetUserPassword());
                            // activate adorbac
                            thisContext.thePolicy.SetAdorbacEnabled(true);

                            // push policy on undo/redo stack
                            PushPolicy();
                        }
                        catch ( COrbacException ee )
                        {
                            JOptionPane.showMessageDialog(findActiveFrame(), ee.getMessage());
                            ee.printStackTrace();
                        }
                    }
                    else
                    {
                        // no password has been defined, we cancel the switch to adorbac mode
                        try
                        {
                            thisContext.thePolicy.SetAdorbacEnabled(false);
                        }
                        catch ( COrbacException ee )
                        {
                            JOptionPane.showMessageDialog(findActiveFrame(), ee.getMessage());
                            ee.printStackTrace();
                        }
                    }
                }
                catch ( COrbacException le )
                {
                    JOptionPane.showMessageDialog(findActiveFrame(), le.getMessage());
                    le.printStackTrace();
                }
            }
        }
        finally
        {
            // refresh affected GUI components
            panelAbstractEntities.UpdateTabs();
            panelAbstractRules.UpdateTabs();
            panelSubjects.UpdateEntityList();
            panelActions.UpdateEntityList();
            panelObjects.UpdateEntityList();
        }
        
        return thisContext.thePolicy.IsAdorbacEnabled();
    }
    // return adorbac state for the associated policy
    public boolean GetAdorbacState()
    {
        return thisContext.thePolicy.IsAdorbacEnabled();
    }
    // return adorbac user for the associated policy
    public String GetAdorbacUser()
    {
        try
        {
            return thisContext.thePolicy.GetCurrentUser();
        }
        catch ( COrbacException e )
        {
            System.out.println(e);
            e.printStackTrace();
        }
        return "none";
    }

    // method related to editing states
    public boolean EmptyHistoric()
    {
        return undoRedoStack.IsEmpty();
    }
    public boolean CanBeSaved()
    {
        // a policy can be saved if it has been modified
        return undoRedoStack.IsModified();
    }
    public boolean CanUndo()
    {
        return undoRedoStack.CanUndo();
    }
    public boolean CanRedo()
    {
        return undoRedoStack.CanRedo();
    }
    public void PushPolicy()
    {
        try
        {
            undoRedoStack.PushPolicy(thisContext.thePolicy.GetPolicyAsString());
            // refresh toolbar and menu
            parentGui.RefreshMenuItems(this);
            parentGui.RefreshToolbar(this);
        }
        catch ( Exception e )
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    public void Undo()
    {
        String policyAsString = undoRedoStack.RetrievePreviousPolicy();
        try
        {
            thisContext.thePolicy.ReadPolicyFromString(policyAsString);
            // refresh toolbar and menu
            parentGui.RefreshMenuItems(this);
            parentGui.RefreshToolbar(this);
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    public void Redo()
    {
        String policyAsString = undoRedoStack.RetrieveNextPolicy();
        try
        {
            thisContext.thePolicy.ReadPolicyFromString(policyAsString);
            // refresh toolbar and menu
            parentGui.RefreshMenuItems(this);
            parentGui.RefreshToolbar(this);
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    // toggle the display of adorbac objects
    public boolean ToggleAdorbacDisplay()
    {
        thisContext.adorbacViewActive = !thisContext.adorbacViewActive;
        // refresh affected GUI components
        panelAbstractEntities.UpdateTabs();
        panelAbstractRules.UpdateTabs();
        panelSubjects.UpdateEntityList();
        panelActions.UpdateEntityList();
        panelObjects.UpdateEntityList();
        UpdateOrganizationTree();
        return thisContext.adorbacViewActive;
    }
    // return adorbac display state for the associated policy
    public boolean GetAdorbacDisplayState()
    {
        return thisContext.adorbacViewActive;
    }

    // return information about conflicts
    public int GetAbstractConflictsNumber()
    {
        int n = 0;
        try {
            n = thisContext.thePolicy.GetAbstractConflicts().size();
        }
        catch ( COrbacException e ) {}
        return n;
    }
    public int GetConcreteConflictsNumber()
    {
        int n = 0;
        try {
            Set<CConcreteConflict> conflicts = thisContext.thePolicy.GetConcreteConflicts();
            n = conflicts.size();
        }
        catch ( COrbacException e ) {}
        return n;
    }
    public int GetConcretePermissionNumber()
    {
        return thisContext.thePolicy.GetConcretePermissions().size();
    }
    public int GetAbstractPermissionNumber()
    {
        try
        {
            return thisContext.thePolicy.GetAbstractPermissions().size();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }
    public int GetConcreteProhibitionNumber()
    {
        return thisContext.thePolicy.GetConcreteProhibitions().size();
    }
    public int GetAbstractProhibitionNumber()
    {
        try
        {
            return thisContext.thePolicy.GetAbstractProhibitions().size();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }
    public int GetConcreteObligationNumber()
    {
        return thisContext.thePolicy.GetConcreteObligations().size();
    }
    public int GetAbstractObligationNumber()
    {
        try
        {
            return thisContext.thePolicy.GetAbstractObligations().size();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }
    public int GetConcreteAdorbacPermissionNumber()
    {
        try
        {
            return thisContext.thePolicy.GetAdorbacConcretePermissions().size();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }
    public int GetAbstractAdorbacPermissionNumber()
    {
        try
        {
            return thisContext.thePolicy.GetAdorbacAbstractPermissions().size();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    // If expand is true, expands all nodes in the tree
    // otherwise, collapses all nodes in the tree
    public void expandAll(JTree tree, boolean expand)
    {
        TreeNode root = (TreeNode)tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
    private void expandAll(JTree tree, TreePath parent, boolean expand)
    {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0)
        {
            for (Enumeration<?> e=node.children(); e.hasMoreElements(); )
            {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) tree.expandPath(parent);
        else tree.collapsePath(parent);
    }

    // delegation
    public void RuleDelegation()
    {
        // display modal dialog box
        JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
        jDialogRuleDelegation dialog = new jDialogRuleDelegation(mainFrame, true, thisContext);
        dialog.setLocationRelativeTo(mainFrame);
        NewMotorbacApp.getApplication().show(dialog);
        if ( dialog.HasBeenCancelled() == false )
        {
            // catch all exceptions to display a message after operation
            class DelegationError
            {
                public DelegationError(COrbacException exception, String license)
                {
                    this.exception = exception;
                    this.license = license;
                }
                public COrbacException exception;
                public String license;
            }
            Vector<DelegationError> errors = new Vector<DelegationError>();
            // for each delegation request we make a call to the api
            Vector<RuleDelegationResult> r = dialog.GetDelegatedRules();
            Iterator<RuleDelegationResult> ir = r.iterator();
            while ( ir.hasNext() )
            {
                RuleDelegationResult dr = ir.next();
                try
                {
                    if ( dr.obligation )
                        thisContext.thePolicy.DelegateObligation(thisContext.currentOrganization, dr.grantee, dr.ruleName, null, null, dr.transfert);
                    else
                        thisContext.thePolicy.DelegatePermission(thisContext.currentOrganization, dr.grantee, dr.ruleName, null, null, dr.transfert);
                }
                catch ( COrbacException e )
                {
                    errors.add(new DelegationError(e, dr.ruleName));
                }
            }
            // for each revocation request we make a call to the api
            Vector<RuleRevocationResult> rr = dialog.GetRevokedRules();
            Iterator<RuleRevocationResult> irr = rr.iterator();
            while ( irr.hasNext() )
            {
                RuleRevocationResult dr = irr.next();
                try
                {
                    if ( dr.obligation )
                        thisContext.thePolicy.RevokeDelegatedObligation(dr.ruleName, dr.cascade, dr.strong);
                    else
                        thisContext.thePolicy.RevokeDelegatedPermission(dr.ruleName, dr.cascade, dr.strong);
                }
                catch ( COrbacException e )
                {
                    errors.add(new DelegationError(e, dr.ruleName));
                }
            }
            // build error message if necessary
            if ( errors.size() != 0 )
            {
                String errorMessage = (errors.size() == r.size()) ? "None of the permissions were delegated: \n" : "Some of the permissions could not be delegated: \n";
                Iterator<DelegationError> ie = errors.iterator();
                while ( ie.hasNext() )
                {
                    DelegationError de = ie.next();
                    errorMessage += "license " + de.license + ": " + de.exception.getMessage() + "\n";
                }
                // display it
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
            }
        }
    }

    public void RoleDelegation()
    {
        // display modal dialog box
        JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
        jDialogRoleDelegation dialog = new jDialogRoleDelegation(mainFrame, true, thisContext);
        dialog.setLocationRelativeTo(mainFrame);
        NewMotorbacApp.getApplication().show(dialog);
        if ( dialog.HasBeenCancelled() == false )
        {
            // catch all exceptions to display a message after operation
            class DelegationError
            {
                public DelegationError(COrbacException exception, String ra)
                {
                    this.exception = exception;
                    this.ra = ra;
                }
                public COrbacException exception;
                public String ra;
            }
            Vector<DelegationError> errors = new Vector<DelegationError>();
            // for each delegation request we make a call to the api
            Vector<RoleDelegationResult> r = dialog.GetDelegatedRoles();
            Iterator<RoleDelegationResult> ir = r.iterator();
            while ( ir.hasNext() )
            {
                RoleDelegationResult dr = ir.next();
                try
                {
                    thisContext.thePolicy.DelegateRole(thisContext.currentOrganization, dr.grantee, dr.roleAssignmentName, dr.transfert);
                }
                catch ( COrbacException e )
                {
                    errors.add(new DelegationError(e, dr.roleAssignmentName));
                }
            }
            // for each revocation request we make a call to the api
            Vector<RoleRevocationResult> rr = dialog.GetRevokedRoles();
            Iterator<RoleRevocationResult> irr = rr.iterator();
            while ( irr.hasNext() )
            {
                RoleRevocationResult dr = irr.next();
                try
                {
                    thisContext.thePolicy.RevokeDelegatedRole(dr.roleAssignmentName, dr.cascade, dr.strong);
                }
                catch ( COrbacException e )
                {
                    errors.add(new DelegationError(e, dr.roleAssignmentName));
                }
            }
            // build error message if necessary
            if ( errors.size() != 0 )
            {
                String errorMessage = (errors.size() == r.size()) ? "None of the roles were delegated: \n" : "Some of the roles could not be delegated: \n";
                Iterator<DelegationError> ie = errors.iterator();
                while ( ie.hasNext() )
                {
                    DelegationError de = ie.next();
                    errorMessage += "role assignment " + de.ra + ": " + de.exception.getMessage() + "\n";
                }
                // display it
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
            }
        }        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jListRules;
    private javax.swing.JList jListTools;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPaneAbstractPolicy;
    private javax.swing.JTabbedPane jTabbedPaneConcreteEntities;
    private javax.swing.JToolBar jToolBarControls;
    private javax.swing.JTree jTreeOrganizations;
    // End of variables declaration//GEN-END:variables

    // popup menu mouse handler
    class PopupListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
        public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
        
        private void maybeShowPopup(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {
                // the event source is the organization tree
                TreePath tp = jTreeOrganizations.getPathForLocation(e.getX(), e.getY());
                if ( tp == null ) return;
                DefaultMutableTreeNode n = (DefaultMutableTreeNode)tp.getLastPathComponent();
		jTreeOrganizations.setSelectionPath(jTreeOrganizations.getPathForLocation(e.getX(), e.getY()));

                // disable some menu entries if the organization tree root is selected
                editOrgMenuItem.setEnabled( !n.toString().contains("All ") );
                deleteOrgMenuItem.setEnabled( !n.toString().contains("All ") );
                
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    // organization tree drag and drop handler
    class ToTransferHandler extends TransferHandler
    {
        private static final long serialVersionUID = 1L;
        int action;

        public ToTransferHandler(int action) {
            this.action = action;
        }

        public boolean canImport(TransferHandler.TransferSupport support)
        {
            // we only support drops (not clipboard paste)
            if ( !support.isDrop() )
            {
                return false;
            }

            // we only import Strings
            if ( !support.isDataFlavorSupported(DataFlavor.stringFlavor) )
            {
                return false;
            }

            // the action taken depends on the object name
            try
            {
                String data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);

                // first process drag and drop from the abstract entity toolbar
                if( data.contains("resources") && data.contains(".png") )
                {
                    return true;
                }
                // nothing else can be drag and dropped
                else return false;
            } 
            catch (IOException ex)
            {
                Logger.getLogger(PanelPolicy.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (UnsupportedFlavorException e)
            {
                return false;
            }

            boolean actionSupported = (action & support.getSourceDropActions()) == action;
            if (actionSupported)
            {
                support.setDropAction(action);
                return true;
            }

            return false;
        }

        public boolean importData(TransferHandler.TransferSupport support)
        {
            // if we can't handle the import, say so
            if (!canImport(support))
            {
                return false;
            }

            // fetch the drop location
            JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
            // get the organization
            String organization = dl.getPath().getLastPathComponent().toString();

            // if the organization tree root is targeted then the operation will be issued in the adorbac organization
            if ( organization.equals("All organizations") )
                organization = thisContext.thePolicy.adorbacOrgNameStr;

            // fetch the dragged tool
            String tool;
            try
            {
            	tool = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                // all ok, we do the job
                JFrame mainFrame = NewMotorbacApp.getApplication().getMainFrame();
                if ( tool.contains("organization") )
                {
                    // display modal dialog box
                    Set<String> orgs = thisContext.thePolicy.GetOrganizationsList(thisContext.adorbacViewActive);
                    jDialogAddAbstractEntity createOrg = new jDialogAddAbstractEntity(mainFrame, "organization", orgs, organization, organization, true);
                    createOrg.setLocationRelativeTo(mainFrame);
                    NewMotorbacApp.getApplication().show(createOrg);
                    // get result if not cancelled
                    if ( createOrg.canceled == false )
                    {
                        // create organization
                        thisContext.thePolicy.CreateOrganization(createOrg.GetEntityName());
                        // create hierarchy if necessary
                        for ( String superOrg : createOrg.GetSuperEntities() )
                            thisContext.thePolicy.CreateOrganizationHierarchy(createOrg.GetEntityName(), superOrg);

                        // refresh organization tree
                        UpdateOrganizationTree();
                        // push policy on undo/redo stack
                        PushPolicy();
                    }
                }
                else if ( tool.contains("role") )
                {
                    // display modal dialog box
                    Set<String> roles = thisContext.thePolicy.GetRolesList(thisContext.adorbacViewActive);
                    jDialogAddAbstractEntity createRole = new jDialogAddAbstractEntity(mainFrame, "role", roles, null, organization, true);
                    createRole.setLocationRelativeTo(mainFrame);
                    NewMotorbacApp.getApplication().show(createRole);
                    // get result if not cancelled
                    if ( createRole.canceled == false )
                    {
                        // create role
                        thisContext.thePolicy.CreateRoleAndInsertIntoOrg(createRole.GetEntityName(), thisContext.currentOrganization);
                        // create hierarchy if necessary
                        for ( String superRole : createRole.GetSuperEntities() )
                            thisContext.thePolicy.CreateRoleHierarchy(createRole.GetEntityName(), superRole, thisContext.currentOrganization);

                        // refresh abstract entities trees
                        panelAbstractEntities.UpdateTabs();
                        // push policy on undo/redo stack
                        PushPolicy();
                    }
                }
                else if ( tool.contains("activity") )
                {
                    // display modal dialog box
                    Set<String> activities = thisContext.thePolicy.GetActivitiesList(thisContext.adorbacViewActive);
                    jDialogAddAbstractEntity createActivity = new jDialogAddAbstractEntity(mainFrame, "activity", activities, null, organization, true);
                    createActivity.setLocationRelativeTo(mainFrame);
                    NewMotorbacApp.getApplication().show(createActivity);
                    // get result if not cancelled
                    if ( createActivity.canceled == false )
                    {
                        // create activity
                        thisContext.thePolicy.CreateActivityAndInsertIntoOrg(createActivity.GetEntityName(), thisContext.currentOrganization);
                        // create hierarchy if necessary
                        for ( String superActivity : createActivity.GetSuperEntities() )
                            thisContext.thePolicy.CreateActivityHierarchy(createActivity.GetEntityName(), superActivity, thisContext.currentOrganization);

                        // refresh abstract entities trees
                        panelAbstractEntities.UpdateTabs();
                        // push policy on undo/redo stack
                        PushPolicy();
                    }
                }
                else if ( tool.contains("view") )
                {
                    // display modal dialog box
                    Set<String> views = thisContext.thePolicy.GetViewsList(thisContext.adorbacViewActive);
                    jDialogAddAbstractEntity createView = new jDialogAddAbstractEntity(mainFrame, "view", views, null, organization, true);
                    createView.setLocationRelativeTo(mainFrame);
                    NewMotorbacApp.getApplication().show(createView);
                    // get result if not cancelled
                    if ( createView.canceled == false )
                    {
                        // create view
                        thisContext.thePolicy.CreateViewAndInsertIntoOrg(createView.GetEntityName(), thisContext.currentOrganization);
                        // create hierarchy if necessary
                        for ( String superView : createView.GetSuperEntities() )
                            thisContext.thePolicy.CreateViewHierarchy(createView.GetEntityName(), superView, thisContext.currentOrganization);

                        // refresh abstract entities trees
                        panelAbstractEntities.UpdateTabs();
                        // push policy on undo/redo stack
                        PushPolicy();
                    }
                }
                else if ( tool.contains("permission") )
                {
                    if ( thisContext.currentOrganization == null )
                    {
                        JOptionPane.showMessageDialog(findActiveFrame(), "Please drag and drop the permission icon on an organization name");
                        return false;
                    }
                    thisContext.panelPermissions.AddAbstractPermission();
                }
                else if ( tool.contains("prohibition") )
                {

                    if ( thisContext.currentOrganization == null )
                    {
                        JOptionPane.showMessageDialog(findActiveFrame(), "Please drag and drop the prohibition icon on an organization name");
                        return false;
                    }
                    thisContext.panelPermissions.AddAbstractProhibition();
                }
                else if ( tool.contains("obligation") )
                {

                    if ( thisContext.currentOrganization == null )
                    {
                        JOptionPane.showMessageDialog(findActiveFrame(), "Please drag and drop the obligation icon on an organization name");
                        return false;
                    }
                    thisContext.panelPermissions.AddAbstractObligation();
                }
            }
            catch (UnsupportedFlavorException e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
                return false;
            }
            catch (COrbacException e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
                return false;
            }
            catch (Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(findActiveFrame(), e.getMessage());
                return false;
            }

            return true;
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
