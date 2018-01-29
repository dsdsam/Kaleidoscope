package dsdsse.designspace.initializer;

import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Created by Admin on 5/15/2016.
 */
public class MclnStatePaletteTable extends AdfBasicTable {

    private static final int[] PAIRS_OF_OPPOSITE_STATES_COLUMN_WIDTHS = {30, 80, 80, 50, 150};
    private static final int[] COLUMN_WIDTHS = {30, 43, 66, 150};

    private static final Color TABLE_HEADER_BACKGROUND = InitAssistantUIColorScheme.TABLE_HEADER_BACKGROUND;
    private static final Color TABLE_HEADER_FOREGROUND = InitAssistantUIColorScheme.TABLE_HEADER_FOREGROUND;

    private static final AllowedStateTableCellRenderer allowedStateTableCellRenderer =
            new AllowedStateTableCellRenderer();

    private static final PairsOfOppositeStatesPaletteTableCellRenderer pairsOfOppositeStatesPaletteTableCellRenderer =
            new PairsOfOppositeStatesPaletteTableCellRenderer();

    private MclnStatePaletteTableModel mclnStatePaletteTableModel;
    private final MclnStatePaletteTableStateSelectionEditor mclnStatePaletteTableStateSelectionEditor;

    /**
     * @param mclnStatePaletteTableModel
     */
    MclnStatePaletteTable(MclnStatePaletteTableModel mclnStatePaletteTableModel) {
        super(mclnStatePaletteTableModel);
        this.mclnStatePaletteTableModel = mclnStatePaletteTableModel;
//        setModel(mclnStatePaletteTableModel);
        mclnStatePaletteTableStateSelectionEditor = new MclnStatePaletteTableStateSelectionEditor(this);
        initTable();
        addKeyListener(tableKeyListener);
    }

    /**
     *
     */
    final void initTable() {

        setFocusable(true);

        setCellSelectionEnabled(false);
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // header
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setBorder(null);
        getTableHeader().setFont(InitAssistantUIColorScheme.TABLE_HEADER_FONT);
        getTableHeader().setBackground(TABLE_HEADER_BACKGROUND);
        getTableHeader().setForeground(TABLE_HEADER_FOREGROUND);

        // table
        setBackground(Color.WHITE);
        setForeground(InitAssistantUIColorScheme.TABLE_FOREGROUND);

        setSelectionBackground(InitAssistantUIColorScheme.TABLE_SELECTION_BACKGROUND);
        setSelectionForeground(InitAssistantUIColorScheme.TABLE_FOREGROUND);

        // setting renderers
        if (mclnStatePaletteTableModel.isPairsOfOppositeStatesPalette()) {
            setColumnWidths(PAIRS_OF_OPPOSITE_STATES_COLUMN_WIDTHS);
            setDefaultRenderer(Integer.class, pairsOfOppositeStatesPaletteTableCellRenderer);
            setDefaultRenderer(MclnState.class, pairsOfOppositeStatesPaletteTableCellRenderer);
            setDefaultRenderer(String.class, pairsOfOppositeStatesPaletteTableCellRenderer);
            setDefaultEditor(Boolean.class, mclnStatePaletteTableStateSelectionEditor);
        } else {
            setColumnWidths(COLUMN_WIDTHS);
            setDefaultRenderer(Integer.class, allowedStateTableCellRenderer);
            setDefaultRenderer(MclnState.class, allowedStateTableCellRenderer);
            setDefaultRenderer(String.class, allowedStateTableCellRenderer);
        }
    }

    /**
     * @param columnWidths
     */
    private void setColumnWidths(int[] columnWidths) {
        TableColumnModel tableColumnModel = getColumnModel();
        for (int columnIndex = 0; columnIndex < columnWidths.length; columnIndex++) {
            TableColumn tableColumn = tableColumnModel.getColumn(columnIndex);
            tableColumn.setMinWidth(columnWidths[columnIndex]);
            if (columnIndex != 3 && columnIndex != 4 && columnIndex != 5) {
                tableColumn.setMaxWidth(columnWidths[columnIndex]);
            }
            tableColumn.setWidth(columnWidths[columnIndex]);
        }
    }
}
