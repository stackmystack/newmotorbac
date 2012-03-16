/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanelAbstractConflicts.java
 *
 * Created on May 5, 2011, 5:36:23 PM
 */

package newmotorbac;

import newmotorbac.util.OrbacPolicyContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import orbac.AbstractOrbacPolicy;
import orbac.conflict.CAbstractConflict;
import orbac.exception.COrbacException;
import orbac.exception.CViolatedConstraintException;
import orbac.securityRules.CAbstractRule;

/**
 *
 * @author fabien
 */
public class PanelAbstractConflicts extends javax.swing.JPanel implements ActionListener {
    // policy context
    OrbacPolicyContext thisContext;
    // the popup menu items
    private JPopupMenu      popupMenu = new JPopupMenu();
    private MouseListener   popupListener = new PopupListener();
    private JMenuItem       titleMenuItem = new JMenuItem("Solve conflict");
    private JMenuItem       separateRolesMenuItem = new JMenuItem("Separate roles");
    private JMenuItem       separateActivitiesMenuItem = new JMenuItem("Separate activities");
    private JMenuItem       separateViewsMenuItem = new JMenuItem("Separate views");
    private JMenuItem       separateContextsMenuItem = new JMenuItem("Separate contexts");
    private JMenuItem       setPriorityMenuItem1 = new JMenuItem("Set priority");
    private JMenuItem       setPriorityMenuItem2 = new JMenuItem("Set priority");

    /** Creates new form PanelAbstractConflicts */
    public PanelAbstractConflicts(OrbacPolicyContext thisContext) {
        initComponents();

        // store context
        this.thisContext = thisContext;

        // set custom table renderer
        jTableAbstractConflicts.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        // configure popup menu
        popupMenu.add(titleMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(separateRolesMenuItem);
        popupMenu.add(separateActivitiesMenuItem);
        popupMenu.add(separateViewsMenuItem);
        popupMenu.add(separateContextsMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(setPriorityMenuItem1);
        popupMenu.add(setPriorityMenuItem2);
        separateRolesMenuItem.addActionListener(this);
        separateActivitiesMenuItem.addActionListener(this);
        separateViewsMenuItem.addActionListener(this);
        separateContextsMenuItem.addActionListener(this);
        setPriorityMenuItem1.addActionListener(this);
        setPriorityMenuItem2.addActionListener(this);

        // set table state variables to enable row selection
        jTableAbstractConflicts.setCellSelectionEnabled(false);
        jTableAbstractConflicts.setRowSelectionAllowed(true);
        jTableAbstractConflicts.setColumnSelectionAllowed(false);

        // add menu to table
        jTableAbstractConflicts.addMouseListener(popupListener);
        jTableAbstractConflicts.setRowSelectionAllowed(true);
    }

    public void UpdateConflictTable()
    {
        try
        {
            DefaultTableModel model = (DefaultTableModel)jTableAbstractConflicts.getModel();
            model.setRowCount(0);
            // get conflicts
            Set<CAbstractConflict> conflicts = thisContext.thePolicy.GetAbstractConflicts();

            // display conflicts
            Iterator<CAbstractConflict> ic = conflicts.iterator();
            while ( ic.hasNext() )
            {
                CAbstractConflict c = ic.next();
                String row[] = new String[7];
                CAbstractRule rel;

                rel = c.GetFirstRule();
                row[0] = rel.GetName();
                if ( rel.GetType() == AbstractOrbacPolicy.TYPE_PERMISSION ) row[1] = "permission";
                else if ( rel.GetType() == AbstractOrbacPolicy.TYPE_PROHIBITION ) row[1] = "prohibition";
                else if ( rel.GetType() == AbstractOrbacPolicy.TYPE_OBLIGATION ) row[1] = "obligation";
                row[2] = rel.GetOrganization();
                row[3] = rel.GetRole();
                row[4] = rel.GetActivity();
                row[5] = rel.GetView();
                row[6] = rel.GetContext();
                model.addRow(row);

                rel = c.GetSecondRule();
                row[0] = rel.GetName();
                if ( rel.GetType() == AbstractOrbacPolicy.TYPE_PERMISSION ) row[1] = "permission";
                else if ( rel.GetType() == AbstractOrbacPolicy.TYPE_PROHIBITION ) row[1] = "prohibition";
                else if ( rel.GetType() == AbstractOrbacPolicy.TYPE_OBLIGATION ) row[1] = "obligation";
                row[2] = rel.GetOrganization();
                row[3] = rel.GetRole();
                row[4] = rel.GetActivity();
                row[5] = rel.GetView();
                row[6] = rel.GetContext();
                model.addRow(row);
            }

            // update status bar
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent evt)
    {
        if ( evt.getSource() instanceof JMenuItem )
        {
            JMenuItem item = (JMenuItem)evt.getSource();
            DefaultTableModel model = (DefaultTableModel)jTableAbstractConflicts.getModel();

            int base = (clickedRow / 2) * 2;
            String org1 = (String)model.getValueAt(base, 2);
            String org2 = (String)model.getValueAt(base + 1, 2);

            // create separation constraint or rule priority
            try
            {
                if ( item == separateRolesMenuItem )
                {
                    String role1 = (String)model.getValueAt(base, 3);
                    String role2 = (String)model.getValueAt(base + 1, 3);
                    thisContext.thePolicy.AddRoleSeparationConstraint(role1, role2, org1, org2);
                }
                else if ( item == separateActivitiesMenuItem )
                {
                    String activity1 = (String)model.getValueAt(base, 4);
                    String activity2 = (String)model.getValueAt(base + 1, 4);
                    thisContext.thePolicy.AddActivitySeparationConstraint(activity1, activity2, org1, org2);
                }
                else if ( item == separateViewsMenuItem )
                {
                    String view1 = (String)model.getValueAt(base, 5);
                    String view2 = (String)model.getValueAt(base + 1, 5);
                    thisContext.thePolicy.AddViewSeparationConstraint(view1, view2, org1, org2);
                }
                else if ( item == separateContextsMenuItem )
                {
                    String context1 = (String)model.getValueAt(base, 6);
                    String context2 = (String)model.getValueAt(base + 1, 6);
                    thisContext.thePolicy.AddContextSeparationConstraint(context1, context2, org1, org2);
                }
                else if ( item == setPriorityMenuItem1 )
                {
                    String ruleName1 = (String)model.getValueAt(base, 0);
                    String ruleName2 = (String)model.getValueAt(base + 1, 0);
                    thisContext.thePolicy.SetRule1AboveRule2(ruleName1, ruleName2, org1, org2);
                }
                else if ( item == setPriorityMenuItem2 )
                {
                    String ruleName1 = (String)model.getValueAt(base, 0);
                    String ruleName2 = (String)model.getValueAt(base + 1, 0);
                    thisContext.thePolicy.SetRule1AboveRule2(ruleName2, ruleName1, org2, org1);
                }

                // store policy in undo/redo stack
                thisContext.panelPolicy.PushPolicy();
            }
            catch ( CViolatedConstraintException e )
            {
                // the new separation constraint is violated
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
            catch (COrbacException e)
            {
                // if such an exception is caught the constraint addition failed, do not
                // save modified policy in history
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
            finally
            {
                // refresh abstract conflicts and concrete conflicts
                UpdateConflictTable();
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

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAbstractConflicts = new javax.swing.JTable();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(PanelAbstractConflicts.class);
        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableAbstractConflicts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rule name", "Type", "Organization", "Role", "Activity", "View", "Context"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAbstractConflicts.setColumnSelectionAllowed(true);
        jTableAbstractConflicts.setName("jTableAbstractConflicts"); // NOI18N
        jTableAbstractConflicts.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTableAbstractConflicts);
        jTableAbstractConflicts.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jTableAbstractConflicts.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableAbstractConflicts.columnModel.title0")); // NOI18N
        jTableAbstractConflicts.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTableAbstractConflicts.columnModel.title1")); // NOI18N
        jTableAbstractConflicts.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTableAbstractConflicts.columnModel.title2")); // NOI18N
        jTableAbstractConflicts.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTableAbstractConflicts.columnModel.title3")); // NOI18N
        jTableAbstractConflicts.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jTableAbstractConflicts.columnModel.title4")); // NOI18N
        jTableAbstractConflicts.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("jTableAbstractConflicts.columnModel.title5")); // NOI18N
        jTableAbstractConflicts.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("jTableAbstractConflicts.columnModel.title6")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(181, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        UpdateConflictTable();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableAbstractConflicts;
    // End of variables declaration//GEN-END:variables

    public class CustomTableCellRenderer extends DefaultTableCellRenderer
    {
    	// just to remove the warning
    	static final long serialVersionUID = 0;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if( value instanceof String )
            {
            	if ( (row % 4) == 0 || (row % 4) == 1 )
                	cell.setBackground( new Color(0.8f, 0.8f, 0.5f) );
                else if ( (row % 4) == 2 || (row % 4) == 3 )
                	cell.setBackground( new Color(0.9f, 0.9f, 0.7f) );
            }
            return cell;
        }
    }

    // popup menu mouse handler
    private int clickedRow;
    class PopupListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
        public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }

        private void maybeShowPopup(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {
                // store which row has been right-clicked
                clickedRow = jTableAbstractConflicts.rowAtPoint(e.getPoint());
                DefaultTableModel model = (DefaultTableModel)jTableAbstractConflicts.getModel();

                // refresh menu and then display it
                int base = (clickedRow / 2) * 2;
		jTableAbstractConflicts.setRowSelectionInterval(base, base + 1);
		String ruleName1 = (String)model.getValueAt(base, 0);
		String ruleName2 = (String)model.getValueAt(base + 1, 0);
		String role1 = (String)model.getValueAt(base, 3);
		String role2 = (String)model.getValueAt(base + 1, 3);
		String activity1 = (String)model.getValueAt(base, 4);
		String activity2 = (String)model.getValueAt(base + 1, 4);
		String view1 = (String)model.getValueAt(base, 5);
		String view2 = (String)model.getValueAt(base + 1, 5);
		String context1 = (String)model.getValueAt(base, 6);
		String context2 = (String)model.getValueAt(base + 1, 6);

		// modify menu title and items
		titleMenuItem.setText("Solve conflict between " + ruleName1 + " and " + ruleName2);
		separateRolesMenuItem.setVisible( role1.equals(role2) == false );
		separateActivitiesMenuItem.setVisible( activity1.equals(activity2) == false );
		separateViewsMenuItem.setVisible( view1.equals(view2) == false );
		separateContextsMenuItem.setVisible( context1.equals(context2) == false );
		separateRolesMenuItem.setText("separate roles " + role1 + " and " + role2);
		separateActivitiesMenuItem.setText("separate activities " + activity1 + " and " + activity2);
		separateViewsMenuItem.setText("separate views " + view1 + " and " + view2);
		separateContextsMenuItem.setText("separate contexts " + context1 + " and " + context2);
		setPriorityMenuItem2.setText("set priority " + ruleName1 + " > " + ruleName2);
		setPriorityMenuItem1.setText("set priority " + ruleName1 + " < " + ruleName2);

                // display the menu
        	popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}
