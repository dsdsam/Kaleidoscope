package dsdsse.designspace.initializer;

import adf.ui.components.tables.OneColumnTableModel;
import mcln.model.ProgramStepData;

import java.util.ArrayList;

/**
 * Created by Admin on 5/9/2016.
 */
final class StateDrivenProgramTableModel<T> extends OneColumnTableModel<T> {

    private final InitAssistantDataModel initAssistantDataModel;

    public StateDrivenProgramTableModel(String[] columnNames, InitAssistantDataModel initAssistantDataModel) {
        super(columnNames, new ArrayList());
        this.initAssistantDataModel = initAssistantDataModel;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 2) {
            return true;
        }
        return false;
    }

    public void appendRowAndFireEvent(T obj) {
        appendRow(obj);
        int rowIndex = this.getRowCount() - 1;
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    public void insertRowAndFireEvent(T obj, int rowIndex) {
        super.insertRow(obj, rowIndex);
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    public void removeRowAndFireEvent(int rowIndex) {
        removeRow(rowIndex);
        this.fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void setValueAt(Object value, int row, int col) {
        if (value == null) {
            throw new RuntimeException(this.getClass().getName() + ".setValueAt" + "value is null");
        }
        ProgramStepData programStepData = (ProgramStepData) getRow(row);
        switch (col) {
            case 2:
                programStepData.setTicks((String) value);
                initAssistantDataModel.fireProgramRowChanged();
                break;
            default:
                throw new RuntimeException(this.getClass().getName() + ".setValueAt" + "Wrong index");
        }
    }

    public Object getValueAt(int row, int col) {
        ProgramStepData programStepData = (ProgramStepData) getRow(row);
        switch (col) {
            case 0:
                return row + 1;
            case 1:
                return programStepData.getConditionState();
            case 2:
                return programStepData.getTicksCounter();
            case 3:
                return programStepData.getGeneratedState();
            default:
                throw new RuntimeException(this.getClass().getName() + ".getValueAt" + "Wrong index");
        }
    }
}

