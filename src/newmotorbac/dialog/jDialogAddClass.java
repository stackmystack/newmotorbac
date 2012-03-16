/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JDialogAddConcreteEntity.java
 *
 * Created on May 10, 2011, 7:07:24 PM
 */

package newmotorbac.dialog;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author fabien
 */
public class jDialogAddClass extends javax.swing.JDialog {

    public boolean canceled = false;
    protected Object headers[] = {"subclass of", "class"};
    NewClassTableModel model = new NewClassTableModel(headers, 0);

    /** Creates new form JDialogAddConcreteEntity */
    public jDialogAddClass(java.awt.Frame parent, Set<String> classes, boolean modal) {
        super(parent, modal);
        initComponents();

        // set title
        setTitle("Create new class");

        // fill table
        jTableClasses.setModel(model);
        for ( String c : classes )
        {
            Object[] row = new Object[2];
            row[0] = false;
            row[1] = c;
            model.addRow(row);
        }

         // set class selection column width
        jTableClasses.getColumn("subclass of").setMinWidth(100);
        jTableClasses.getColumn("subclass of").setMaxWidth(100);

        getRootPane().setDefaultButton(jButtonOk);
    }

    // constructor used when editing an entity
    public jDialogAddClass(java.awt.Frame parent, String className, Set<String> classes, Set<String> superClasses, boolean modal) {
        super(parent, modal);
        initComponents();

        jTextFieldClassName.setText(className);

        // fill table and select classes
        jTableClasses.setModel(model);
        for ( String c : classes )
        {
            Object[] row = new Object[2];
            row[0] = superClasses.contains(c);
            row[1] = c;
            model.addRow(row);
        }

         // set class selection column width
        jTableClasses.getColumn("subclass of").setMinWidth(100);
        jTableClasses.getColumn("subclass of").setMaxWidth(100);
    }

    public Set<String> GetSuperClasses()
    {
        HashSet<String> classes = new HashSet<String>();
        for ( int i = 0; i < model.getRowCount(); i++)
        {
            Boolean b = (Boolean)model.getValueAt(i, 0);
            if ( b )
                classes.add((String)model.getValueAt(i, 1));
        }
        return classes;
    }
    public String GetClassName()
    {
        return jTextFieldClassName.getText();
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
        jTextFieldClassName = new javax.swing.JTextField();
        jButtonCancel = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableClasses = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Form"); // NOI18N
        setResizable(false);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newmotorbac.NewMotorbacApp.class).getContext().getResourceMap(jDialogAddClass.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jTextFieldClassName.setText(resourceMap.getString("jTextFieldClassName.text")); // NOI18N
        jTextFieldClassName.setName("jTextFieldClassName"); // NOI18N

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

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableClasses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "subclass of", "class"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class
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
        jTableClasses.setName("jTableClasses"); // NOI18N
        jTableClasses.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTableClasses);
        jTableClasses.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTableClasses.getColumnModel().getColumn(0).setMaxWidth(100);
        jTableClasses.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableClasses.columnModel.title0")); // NOI18N
        jTableClasses.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTableClasses.columnModel.title1")); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldClassName, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancel))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldClassName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        // check if an entity name has been specified
    	if ( jTextFieldClassName.getText().equals("") )
    	{
            JOptionPane.showMessageDialog(this, "You must specify a class name");
            return;
    	}
        dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        canceled = true;
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableClasses;
    private javax.swing.JTextField jTextFieldClassName;
    // End of variables declaration//GEN-END:variables

}