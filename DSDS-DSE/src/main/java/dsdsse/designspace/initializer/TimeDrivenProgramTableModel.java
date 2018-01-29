package dsdsse.designspace.initializer;

import adf.ui.components.tables.OneColumnTableModel;
import mcln.model.ProgramStepData;
import mcln.palette.MclnState;

import java.util.ArrayList;

/**
 * Created by Admin on 5/9/2016.
 */
final class TimeDrivenProgramTableModel<T> extends OneColumnTableModel<T> {

    private final InitAssistantDataModel initAssistantDataModel;

    public TimeDrivenProgramTableModel(String[] columnNames, InitAssistantDataModel initAssistantDataModel) {
        super(columnNames, new ArrayList());
        this.initAssistantDataModel = initAssistantDataModel;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(rowIndex == 0 && !initAssistantDataModel.getSelectedProgramHasPhase()){
            return false;
        }

        if (columnIndex == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void appendRowAndFireEvent(T obj) {
        appendRow(obj);
        int rowIndex = this.getRowCount() - 1;
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    @Override
    public void insertRowAndFireEvent(T obj, int rowIndex) {
        super.insertRow(obj, rowIndex);
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    @Override
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
            case 1:
                programStepData.setTicks((String) value);
                initAssistantDataModel.fireProgramRowChanged();
                break;
            default:
                throw new RuntimeException(this.getClass().getName() + ".setValueAt" + "Wrong index");
        }
    }

    public Object getValueAt(int row, int col) {
        T programStep = getRow(row);
        switch (col) {
            case 0:
                return row;
            case 1:
                int ticksCounter = ((ProgramStepData) programStep).getTicksCounter();
                return ticksCounter;
            case 2:
                MclnState generatedState = ((ProgramStepData) programStep).getGeneratedState();
                return generatedState;
            default:
                throw new RuntimeException(this.getClass().getName() + ".getValueAt" + "Wrong index");
        }
    }
}
