package dsdsse.designspace.initializer;

import adf.ui.components.tables.OneColumnTableModel;
import mcln.palette.MclnState;
import mcln.palette.MclnStatesNewPalette;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 5/15/2016.
 */
class MclnStatePaletteTableModel extends OneColumnTableModel {

    private static final String[] PAIRS_OF_OPPOSITE_PALETTE_HEADER_NAMES =
            {"Num.", "State", "Opposite State", "RGB Values", "Selected States"};
    private static final String[] PALETTE_HEADER_NAMES = {"Num.", "State", "RGB Value", "Selected States"};

    private List<MclnState> mclnStatesPaletteAsList;
    private MclnStatesNewPalette mclnStatesPalette;
    private boolean thisIsPairsOfOppositeStatesPalette;
    private final InitAssistantDataModel initAssistantDataModel;
    private boolean[] selectedStates;

    MclnStatePaletteTableModel(InitAssistantDataModel initAssistantDataModel,
                               List<MclnState> mclnStatesPaletteAsList, boolean[] selectedStates) {
        super(mclnStatesPaletteAsList);
        this.initAssistantDataModel = initAssistantDataModel;
        initModel(mclnStatesPaletteAsList, selectedStates);
    }

    void updateModelUpOnPaletteReselected(List<MclnState> mclnStatesPaletteAsList, boolean[] selectedStates) {
        initModel(mclnStatesPaletteAsList, selectedStates);
        fireTableStructureChanged(); // this will recreate columns
        setData(mclnStatesPaletteAsList);
    }

    private final void initModel(List<MclnState> mclnStatesPaletteAsList, boolean[] selectedStates) {
        this.mclnStatesPaletteAsList = mclnStatesPaletteAsList;
        thisIsPairsOfOppositeStatesPalette = this.initAssistantDataModel.isPairsOfOppositeStatesPalette();
        if (thisIsPairsOfOppositeStatesPalette) {
            setColumnNames(PAIRS_OF_OPPOSITE_PALETTE_HEADER_NAMES);
        } else {
            setColumnNames(PALETTE_HEADER_NAMES);
        }
        this.selectedStates = selectedStates;
    }


    public boolean isPairsOfOppositeStatesPalette() {
        return thisIsPairsOfOppositeStatesPalette;
    }

    /**
     * Called each time when user selects checkbox palette item
     * to update Mcln Property Allowed States list
     *
     * @return
     */
    private void processAllowedStatesSelectionChanged() {
        List<MclnState> paletteSelectedAllowedStatesList = getUpdatedAllowedStatesList();
        initAssistantDataModel.updateCurrentAllowedStatesList(paletteSelectedAllowedStatesList);
    }

    /**
     * Called from processAllowedStatesSelectionChanged after user clicked check box or
     * when page changed from Selection Allowed Stated List to page Allowed State Table
     *
     * @return
     */
    List<MclnState> getUpdatedAllowedStatesList() {
        List<MclnState> selectedAllowedMclnStates = new ArrayList();
        for (int i = 0; i < selectedStates.length; i++) {
            if (selectedStates[i]) {
                MclnState paletteSelectedMclnState = mclnStatesPaletteAsList.get(i);
                selectedAllowedMclnStates.add(paletteSelectedMclnState);
            }
        }
        return selectedAllowedMclnStates;
    }

    int getSelectedAllowedStatesNumber() {
        int selectedAllowedStatesNumber = 0;
        for (int i = 0; i < selectedStates.length; i++) {
            if (selectedStates[i]) {
                selectedAllowedStatesNumber++;
            }
        }
        return selectedAllowedStatesNumber;
    }

    @Override
    public Class getColumnClass(int column) {
        if (thisIsPairsOfOppositeStatesPalette) {
            if ((column < 4)) {
                return getValueAt(0, column).getClass();
            } else {
                return Boolean.TRUE.getClass();
            }
        } else {
            if ((column < 3)) {
                return getValueAt(0, column).getClass();
            } else {
                return Boolean.TRUE.getClass();
            }
        }
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
        MclnState mclnState = getRow(row);

        if (!thisIsPairsOfOppositeStatesPalette) {

            switch (col) {
                case 0: // row number
                    return row + 1;
                case 1: // Color
                    return mclnState;
                case 2: // RGB value
                    return mclnState.getHexColor();
                case 3: // Object
                    return new Boolean(selectedStates[row]);
                default:
                    throw new RuntimeException(this.getClass().getName()
                            + ".initTableModel" + "Wrong index");
            }

        } else {

            switch (col) {
                case 0: // row number
                    return row + 1;
                case 1: // Color
                    return mclnState;
                case 2: // opposite color
                    return mclnState.getOppositeMclnState();
                case 3: // opposite color
                    return mclnState.getHexColor() + "/" + mclnState.getOppositeMclnState().getHexColor();
                case 4: // row selected flag
                    return new Boolean(selectedStates[row]);
                default:
                    throw new RuntimeException(this.getClass().getName()
                            + ".initTableModel" + "Wrong column index "+col);
            }
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (value == null) {
            throw new RuntimeException(this.getClass().getName() + ".setValueAt" + "value is null");
        }

        if (!thisIsPairsOfOppositeStatesPalette) {

            switch (col) {
                case 3:
                    System.out.println(" Selected row index " + row +",  value "+value);
                    selectedStates[row] = (boolean) value;
                    processAllowedStatesSelectionChanged();
                    break;
                default:
                    throw new RuntimeException(this.getClass().getName()
                            + ".initTableModel" + "Wrong index " + col);
            }

        } else{

            switch (col) {
                case 4:
                    System.out.println(" Selected row index " + row);
                    selectedStates[row] = (boolean) value;
                    processAllowedStatesSelectionChanged();
                    break;
                default:
                    throw new RuntimeException(this.getClass().getName()
                            + ".initTableModel" + "Wrong index " + col);
            }
        }

        fireTableCellUpdated(row, col);
    }

    @Override
    public MclnState getRow(int rowIndex) {
        MclnState mclnState = (MclnState) super.getRow(rowIndex);
        return mclnState;
    }
};

