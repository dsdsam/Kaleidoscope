package sem.appui.components.panes.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

import adf.csys.model.ModelTreeNode;
import adf.ui.components.borders.AlignedRoundedBorder;

public class ModelTreeRenderer extends JPanel implements TreeCellRenderer {

    private final ImageIcon redIcon = (ImageIcon) UIManager.get("ModelTreeRenderer.redIcon");
    private final ImageIcon blackIcon = (ImageIcon) UIManager.get("ModelTreeRenderer.blackIcon");
    private final ImageIcon yellowIcon = (ImageIcon) UIManager.get("ModelTreeRenderer.yellowIcon");

    private final Color borderColor = new Color(0x008800);
    private final Color highlightBorderColor = new Color(0xFF3300);

    private final AlignedRoundedBorder alignedRoundedBorder = new AlignedRoundedBorder(2, 4,
            AlignedRoundedBorder.ROUNDING_LEFT, new Color(0x333333), borderColor);

    private JLabel label = new JLabel();

    private int fontSize = 11;
    private Font FONT_ARIAL_PLAIN = new Font("Arial", Font.PLAIN, fontSize);

    public ModelTreeRenderer() {
        super(new BorderLayout());

        setBorder(alignedRoundedBorder);
        setOpaque(false);
        setBackground(Color.black);
        setFont(FONT_ARIAL_PLAIN);

        label.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 3));
        label.setFont(FONT_ARIAL_PLAIN);
        label.setBackground(new Color(0x070707));
        add(label);
    }

    /**
     * 
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf, int row,
                                                  boolean hasFocus) {
        if (selected) {
            alignedRoundedBorder.setInnerBorderColor(highlightBorderColor);
        } else {
            alignedRoundedBorder.setInnerBorderColor(borderColor);
        }

        if (! (value instanceof ModelTreeNode)) {
            throw new RuntimeException(
                    "SeTreeRenderer.getTreeCellRendererComponent: value is not TreeNode");
        }


        ModelTreeNode modelTreeNode = (ModelTreeNode) value;
        String text = modelTreeNode.getName();

        label.setText(text);


        label.setForeground(new Color(170, 170, 170));
        if (leaf) {
            label.setIcon(yellowIcon);
        } else {
            label.setIcon(redIcon);
            setForeground(new Color(130, 130, 0));
        }

        return this;
    }
}
