/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author fabien
 */
public class SimpleCellRenderer extends JLabel implements TreeCellRenderer
{
    // just to remove the warning
    static final long serialVersionUID = 0;

    private ImageIcon icon = null;
    private ImageIcon iconSel = null;
    public SimpleCellRenderer(ImageIcon icon, ImageIcon iconSel)
    {
        this.icon = icon;
        this.iconSel = iconSel;
    }
    public Component getTreeCellRendererComponent(JTree tree,
                                                Object value, boolean sel, boolean expanded, boolean leaf,
                                                int row, boolean hasFocus)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        String nodeName = obj.toString();
        // set the text
        if ( sel )
        {
            setIcon(iconSel);
            setForeground(Color.BLACK);
        }
        else
        {
            setIcon(icon);
            setForeground(new Color(0.5f, 0.5f, 0.5f));
        }
        setText(nodeName);
        return this;
     }
}