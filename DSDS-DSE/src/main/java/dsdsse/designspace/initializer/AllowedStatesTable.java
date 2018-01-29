package dsdsse.designspace.initializer;

import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Created by Admin on 3/19/2016.
 */
final class AllowedStatesTable extends AdfBasicTable {

    private static final int[] PAIRS_OF_OPPOSITE_STATES_COLUMN_WIDTHS = {30, 80, 80, 50, 150};
    private static final int[] COLUMN_WIDTHS = {30, 43, 66, 150};

    private static final Color TABLE_HEADER_BACKGROUND = InitAssistantUIColorScheme.TABLE_HEADER_BACKGROUND;
    private static final Color TABLE_HEADER_FOREGROUND = InitAssistantUIColorScheme.TABLE_HEADER_FOREGROUND;

    private static final AllowedStateTableCellRenderer allowedStateTableCellRenderer =
            new AllowedStateTableCellRenderer();

    private static final PairsOfOppositeStatesAllowedStatesTableCellRenderer
            pairsOfOppositeStatesAllowedStatesTableCellRenderer =
            new PairsOfOppositeStatesAllowedStatesTableCellRenderer();

    private final AllowedStateTableModel allowedStateTableModel;

//    private final TableModelListener tableModelListener = e -> {
//        super.tableChanged(e);
//        // recognizing Structure Changed Event
////        if (e.getType() == 0 && e.getColumn() == -1 && e.getFirstRow() == -1 && e.getLastRow() == -1) {
//            initTable();
////        }
//    };


    AllowedStatesTable(AllowedStateTableModel allowedStateTableModel) {
        super(allowedStateTableModel);
        this.allowedStateTableModel = allowedStateTableModel;
        initTable();
        addKeyListener(tableKeyListener);
//        allowedStateTableModel.addTableModelListener(tableModelListener);
    }
//
//    @Override
//    public void tableChanged(TableModelEvent e) {
//        super.tableChanged(e);
//        initTable();
//        System.out.println();
//    }

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

        // width and renderers
        if (allowedStateTableModel.isPairsOfOppositeStatesPalette()) {
            setColumnWidths( PAIRS_OF_OPPOSITE_STATES_COLUMN_WIDTHS);
            setDefaultRenderer(Integer.class, pairsOfOppositeStatesAllowedStatesTableCellRenderer);
            setDefaultRenderer(MclnState.class, pairsOfOppositeStatesAllowedStatesTableCellRenderer);
            setDefaultRenderer(String.class, pairsOfOppositeStatesAllowedStatesTableCellRenderer);
        } else {
            setColumnWidths( COLUMN_WIDTHS);
            setDefaultRenderer(Integer.class, allowedStateTableCellRenderer);
            setDefaultRenderer(MclnState.class, allowedStateTableCellRenderer);
            setDefaultRenderer(String.class, allowedStateTableCellRenderer);
        }
    }


    private void setColumnWidths( int[] columnWidths) {
        int columnCount = getColumnCount();
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
