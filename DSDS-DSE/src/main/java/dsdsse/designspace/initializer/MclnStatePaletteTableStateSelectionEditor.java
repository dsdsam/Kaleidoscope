package dsdsse.designspace.initializer;


import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Admin on 5/15/2016.
 */
public class MclnStatePaletteTableStateSelectionEditor extends AbstractCellEditor implements TableCellEditor {

    private final JPanel panel = new JPanel(new GridBagLayout());
    private final JCheckBox mclnStateSelectionCheckBox = new JCheckBox() ;

    private final MclnStatePaletteTableModel mclnStatePaletteTableModel;
    private ItemListener checkBoxItemListener = (e) -> {
        System.out.println(""+(e.getStateChange() == ItemEvent.SELECTED)+"  "+e.getSource());
        MclnStatePaletteTableStateSelectionEditor.this.stopCellEditing();
    };

    public MclnStatePaletteTableStateSelectionEditor(MclnStatePaletteTable mclnStatePaletteTable) {
        mclnStatePaletteTableModel = (MclnStatePaletteTableModel) mclnStatePaletteTable.getModel();
        panel.setBorder(null);
        mclnStateSelectionCheckBox.setBorder(null);
        panel.add(mclnStateSelectionCheckBox, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 0, 0));
        mclnStateSelectionCheckBox.addItemListener(checkBoxItemListener);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        mclnStateSelectionCheckBox.setSelected((Boolean) value);
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return mclnStateSelectionCheckBox.isSelected();
    }
}
