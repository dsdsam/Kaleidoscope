package dsdsse.designspace.initializer;

import adf.utils.StandardFonts;
import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by Admin on 5/30/2016.
 */
public class PairsOfOppositeStatesPaletteTableCellRenderer extends JLabel implements TableCellRenderer {

    private static final Color STATE_COLOR_BORDER_COLOR = Color.DARK_GRAY;
    private static final Font FONT_MONOSPACED_PLAIN_10 = StandardFonts.FONT_MONOSPACED_PLAIN_10;
    private static final Font FONT_DIALOG_PLAIN_11 = StandardFonts.FONT_DIALOG_PLAIN_11;

    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    JCheckBox cb = new JCheckBox();

    public PairsOfOppositeStatesPaletteTableCellRenderer() {
        setOpaque(true);
        panel.setBorder(null);
        panel.add(cb);
    }

    //     -----------------------------------------------------------------
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {


        setBorder(null);
        setFont(FONT_MONOSPACED_PLAIN_10);
        setHorizontalAlignment(JLabel.CENTER);
        setText("");

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(Color.black);
        }

        if (column == 0) {
            Integer intState = (Integer) value;
            String strRowNumber = String.format("%03d", intState);
            setText(strRowNumber);
            return this;

        } else if (column == 1) { // state color
            MclnState mclnState = (MclnState)value;
            int intRGB =   mclnState.getRGB();
            Color borderColor = STATE_COLOR_BORDER_COLOR;
            Border border;

            if (isSelected) {
                if (intRGB == -1) {
                    setBackground(table.getSelectionBackground());
                } else if (table.isEnabled()) {
                    setBackground(new Color(intRGB));
                } else {
                    setBackground(table.getBackground());
                }
                border = new CompoundBorder(BorderFactory.createMatteBorder(2, 30, 2, 30,
                        table.getSelectionBackground()), new LineBorder(borderColor));

            } else {
                if (intRGB == -1) {
                    setBackground(table.getBackground());
                    borderColor = table.getBackground();
                } else if (table.isEnabled()) {
                    setBackground(new Color(intRGB));
                } else {
                    setBackground(table.getBackground());
                }
                border = new CompoundBorder(BorderFactory.createMatteBorder(2, 30, 2, 30,
                        table.getBackground()), new LineBorder(borderColor));
            }

            setBorder(border);
            return this;

        } else if (column == 2) {
            MclnState oppositeMclnState = (MclnState)value;
            int intRGB =   oppositeMclnState.getRGB();
            Color borderColor = STATE_COLOR_BORDER_COLOR;
            Border border;

            if (isSelected) {
                if (intRGB == -1) {
                    setBackground(table.getSelectionBackground());
                } else if (table.isEnabled()) {
                    setBackground(new Color(intRGB));
                } else {
                    setBackground(table.getBackground());
                }
                border = new CompoundBorder(BorderFactory.createMatteBorder(2, 30, 2, 30,
                        table.getSelectionBackground()), new LineBorder(borderColor));

            } else {
                if (intRGB == -1) {
                    setBackground(table.getBackground());
                    borderColor = table.getBackground();
                } else if (table.isEnabled()) {
                    setBackground(new Color(intRGB));
                } else {
                    setBackground(table.getBackground());
                }
                border = new CompoundBorder(BorderFactory.createMatteBorder(2, 30, 2, 30,
                        table.getBackground()), new LineBorder(borderColor));
            }

            setBorder(border);
            return this;

        } else if (column == 3) { // RGB code
            setText((String)value);
            return this;

        }
        return this;
    }
}
