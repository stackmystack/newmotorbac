/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.dialog;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author fabien
 */
public class NewConcreteEntityTableModel extends DefaultTableModel
{
    // just to remove the warning
    static final long serialVersionUID = 0;

    NewConcreteEntityTableModel(Object[] columnNames, int rowCount)
    {
        super(columnNames, rowCount);
    }
    // make read-only except first column
    public boolean isCellEditable(int x, int y)
    {
        return (y > 0) ? false : true;
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    @SuppressWarnings("unchecked")
    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }
}
