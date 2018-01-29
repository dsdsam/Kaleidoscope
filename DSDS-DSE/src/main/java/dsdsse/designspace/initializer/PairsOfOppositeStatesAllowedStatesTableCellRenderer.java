package dsdsse.designspace.initializer;

import adf.app.StandardFonts;
import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by Admin on 7/7/2016.
 */
public class PairsOfOppositeStatesAllowedStatesTableCellRenderer  extends JLabel implements TableCellRenderer {

    private static final Color STATE_COLOR_BORDER_COLOR = Color.DARK_GRAY;
    private static final Font FONT_MONOSPACED_PLAIN_10 = StandardFonts.FONT_MONOSPACED_PLAIN_10;
    private static final Font FONT_DIALOG_PLAIN_11 = StandardFonts.FONT_DIALOG_PLAIN_11;

    private final static Color UNUSED_CELL_BACKGROUND = new Color(0xECECEC);

    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    JCheckBox cb = new JCheckBox();

    public PairsOfOppositeStatesAllowedStatesTableCellRenderer() {
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
        boolean theRowIsEven = (row % 2) == 0;

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(Color.black);
        }

        if (column == 0) {
            if(theRowIsEven) {
                int stateNumber = 1 + (row>>1);
                String strRowNumber = String.format("%03d", stateNumber);
                setText(strRowNumber);
            }


        } else if (column == 1) { // state color
            if(!theRowIsEven) {
                if (isSelected) {
                        setBackground(table.getSelectionBackground());
                } else {
                        setBackground(UNUSED_CELL_BACKGROUND);
                }
                return this;
            }
            MclnState mclnState = (MclnState)value;
            Integer intRGB =   mclnState.getRGB();
            Color borderColor = STATE_COLOR_BORDER_COLOR;
            Border border;

            if (isSelected) {
                if (intRGB.intValue() == -1) {
                    setBackground(table.getSelectionBackground());
                } else if (table.isEnabled()) {
                    setBackground(new Color(intRGB));
                } else {
                    setBackground(table.getBackground());
                }
                border = new CompoundBorder(BorderFactory.createMatteBorder(2, 30, 2, 30,
                        table.getSelectionBackground()), new LineBorder(borderColor));

            } else {
                if (intRGB.intValue() == -1) {
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
            if(theRowIsEven) {
                if (isSelected) {
                    setBackground(table.getSelectionBackground());
                } else {
                    setBackground(UNUSED_CELL_BACKGROUND);
                }
                return this;
            }
            MclnState oppositeMclnState = (MclnState)value;
            Integer intRGB =   oppositeMclnState.getRGB();
            Color borderColor = STATE_COLOR_BORDER_COLOR;
            Border border;

            if (isSelected) {
                if (intRGB.intValue() == -1) {
                    setBackground(table.getSelectionBackground());
                } else if (table.isEnabled()) {
                    setBackground(new Color(intRGB));
                } else {
                    setBackground(table.getBackground());
                }
                border = new CompoundBorder(BorderFactory.createMatteBorder(2, 30, 2, 30,
                        table.getSelectionBackground()), new LineBorder(borderColor));

            } else {
                if (intRGB.intValue() == -1) {
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

        } else if (column == 3) { // HEX color
            MclnState mclnState = (MclnState)value;
            String  hexColor =   mclnState.getHexColor();
//            mclnStatementState.getHexColor() + "/" + mclnStatementState.getOppositeMclnStatementState().getHexColor();
            setText(hexColor);
            return this;

        } else if (column == 4) {

            if (table instanceof MclnStatePaletteTable) {
                cb.setSelected((Boolean) value);
                return panel;
            }

            setBorder(new EmptyBorder(0, 5, 0, 0));
            setFont(FONT_DIALOG_PLAIN_11);
            setHorizontalAlignment(JLabel.LEFT);
            setText(value.toString());
            return this;
        }

        return this;
    }
}
