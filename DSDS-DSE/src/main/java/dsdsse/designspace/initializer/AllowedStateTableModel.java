package dsdsse.designspace.initializer;

import adf.ui.components.tables.OneColumnTableModel;
import mcln.model.MclnStatementState;

import java.util.List;

/**
 * Created by Admin on 3/18/2016.
 */
class AllowedStateTableModel extends OneColumnTableModel<MclnStatementState> {

    private static final String[] PAIRS_OF_OPPOSITE_PALETTE_HEADER_NAMES =
            {"Num.", "State", "Opposite State", "RGB Values", "State Interpretation   ( editable column )"};
    private static final String[] HEADER_NAMES =
            {"Num.", "State", "RGB Value", "State Interpretation   ( editable column )"};

    private boolean processingPairsOfOppositeStates;
    private final InitAssistantDataModel initAssistantDataModel;

    public AllowedStateTableModel(InitAssistantDataModel initAssistantDataModel) {
        super(initAssistantDataModel.getCurrentAllowedStatesList());
        this.initAssistantDataModel = initAssistantDataModel;
        boolean processingPairsOfOppositeStates = this.initAssistantDataModel.isPairsOfOppositeStatesPalette();
        initPaletteTypeRelatedData(processingPairsOfOppositeStates);
    }

//    void setPaletteType(boolean processingPairsOfOppositeStates) {
//        initPaletteTypeRelatedData(processingPairsOfOppositeStates);
//        fireTableStructureChanged();
//    }

    private void initPaletteTypeRelatedData(boolean processingPairsOfOppositeStates) {
        this.processingPairsOfOppositeStates = processingPairsOfOppositeStates;
        if (processingPairsOfOppositeStates) {
            setColumnNames(PAIRS_OF_OPPOSITE_PALETTE_HEADER_NAMES);
        } else {
            setColumnNames(HEADER_NAMES);
        }
    }

    public boolean isPairsOfOppositeStatesPalette() {
        return processingPairsOfOppositeStates;
    }

    void setSelectedAllowedMclnStates(List<MclnStatementState> allowedMclnStatesList) {
        setData(allowedMclnStatesList);
    }

    @Override
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        if (!isEditingAllowed()) {
            return false;
        } else {
            return ((col < 3) ? false : true);
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        MclnStatementState mclnStatementState = getRow(row);

        if (!processingPairsOfOppositeStates) {

            switch (col) {
                case 0: // row number
                    return row + 1;
                case 1: // Color
                    return mclnStatementState.getMclnState();
                case 2: // RGB value
                    return mclnStatementState.getHexColor();
                case 3: // Object
                    return mclnStatementState.getStateInterpretation();
                default:
                    throw new RuntimeException(this.getClass().getName() + ".initTableModel" + "Wrong index");
            }

        } else {

            switch (col) {
                case 0: // row number
                    return row + 1;
                case 1: // Color
                    return mclnStatementState.getMclnState();
                case 2: // opposite color
                    return mclnStatementState.getMclnState();
                case 3: // opposite color
                    return mclnStatementState.getMclnState();
                case 4: // row selected flag
                    return mclnStatementState.getStateInterpretation();
                default:
                    throw new RuntimeException(this.getClass().getName()
                            + ".initTableModel" + "Wrong index");
            }
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (value == null) {
            throw new RuntimeException(this.getClass().getName() + ".setValueAt" + "value is null");
        }
        MclnStatementState mclnStatementState = getRow(row);
        switch (col) {
            case 3:
                System.out.println("new interpretation \"" + value + "\n");
                mclnStatementState.setInterpretation((String) value);
                initAssistantDataModel.allowedStatementStatesUpdated();
                break;
            default:
                throw new RuntimeException(this.getClass().getName() + ".initTableModel" + "Wrong index " + col);
        }
    }

    @Override
    public MclnStatementState getRow(int rowIndex) {
        MclnStatementState mclnState = super.getRow(rowIndex);
        return mclnState;
    }
};

