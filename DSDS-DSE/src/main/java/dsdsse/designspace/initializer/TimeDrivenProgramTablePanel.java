package dsdsse.designspace.initializer;

import mcln.model.MclnStatementState;
import mcln.model.ProgramStepData;
import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;


/**
 * Created by Admin on 5/9/2016.
 */
final class TimeDrivenProgramTablePanel<T> extends AbstractProgramTablePanel<TimeDrivenProgramTablePanel> {

    private static String COLUMN_0_HEADER = "Step N";
    private static String COLUMN_1_HEADER = "Ticks";
    private static String COLUMN_2_HEADER = "State";
    private static final String[] TIME_DRIVEN_PROGRAM_COLUMN_NAMES =
            {COLUMN_0_HEADER, COLUMN_1_HEADER, COLUMN_2_HEADER};

    private static final int[] TIME_DRIVEN_PROGRAM_COLUMN_WIDTH =
            {20, 40, 20};

    private TimeDrivenProgramCellRenderer timeDrivenProgramCellRenderer = new TimeDrivenProgramCellRenderer(true);
    private Color bgColor = Color.WHITE;

    private final TimeDrivenProgramTableModel<ProgramStepData> timeDrivenProgramTableModel;

    //   C r e a t i o n

    TimeDrivenProgramTablePanel(InitAssistantDataModel initAssistantDataModel) {
        super(initAssistantDataModel, TIME_DRIVEN_PROGRAM_COLUMN_WIDTH,
                new TimeDrivenProgramTableModel<ProgramStepData>(TIME_DRIVEN_PROGRAM_COLUMN_NAMES,
                        initAssistantDataModel));
        this.timeDrivenProgramTableModel = (TimeDrivenProgramTableModel) oneColumnTableModel;
        initContexts(timeDrivenProgramCellRenderer);
    }

    void setData(List<ProgramStepData> programSteps) {
        timeDrivenProgramTableModel.setData(programSteps);
    }

    @Override
    protected void initContexts(TableCellRenderer tableCellRenderer) {

        super.initContexts(tableCellRenderer);

        // Set the "Num" column.
        TableColumn numberColumn;
        TableCellRenderer headerRenderer;

        numberColumn = table.getColumn(COLUMN_0_HEADER);
        headerRenderer = numberColumn.getHeaderRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer) headerRenderer).setToolTipText("The number of the program step");
        }
        numberColumn.setPreferredWidth(TIME_DRIVEN_PROGRAM_COLUMN_WIDTH[0]);
        numberColumn.setCellRenderer(timeDrivenProgramCellRenderer);

        // "Ticks" column.
        TableColumn ticksColumn = table.getColumn(COLUMN_1_HEADER);
        headerRenderer = ticksColumn.getHeaderRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer) headerRenderer).
                    setToolTipText("The number of ticks to pass before new state is set.");
        }

        StringCellRenderer strCellRenderer = new StringCellRenderer(false);
        strCellRenderer.setToolTipText("Click for to edit ticks value.");
        strCellRenderer.setHorizontalAlignment(JLabel.RIGHT);
        ticksColumn.setCellRenderer(strCellRenderer);
        ticksColumn.setPreferredWidth(TIME_DRIVEN_PROGRAM_COLUMN_WIDTH[1]);
//
        cellTextField = new JTextField(new TicksInputDocument(1, "Time"), null, 3);
        Font font = cellTextField.getFont();
        cellTextField.setFont(new Font(font.getName(), font.BOLD, 11));
        cellTextField.setHorizontalAlignment(JTextField.CENTER);
//        cellTextField.setMargin( new Insets( 3, 3, 3, 3 ));
        ticksColumn.setCellEditor(new DefaultCellEditor(cellTextField));

//      "State" column.
        TableColumn stateColumn = table.getColumn(COLUMN_2_HEADER);
        headerRenderer = stateColumn.getHeaderRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer)
            ((DefaultTableCellRenderer) headerRenderer).setToolTipText
                    ("The is to be set, when ticks are expired.");
        stateColumn.setPreferredWidth(TIME_DRIVEN_PROGRAM_COLUMN_WIDTH[2]);
        stateColumn.setCellRenderer(timeDrivenProgramCellRenderer);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row < 0 || col < 0) {
                    return;
                }
                if (col != 2) {
                    return;
                }

                if (row == 0 && !initAssistantDataModel.getSelectedProgramHasPhase()) {
                    return;
                }

                MclnStatementState mclnStatementState = initAssistantDataModel.getProgramMclnStateCandidate();
                if (mclnStatementState == null) {
                    System.out.println("Candidate State is not selected");
                    initAssistantDataModel.showMessage("Candidate State is not selected");
                    return;
                }

                System.out.println(" row " + row + "  col " + col + "  candidate " + mclnStatementState);
                ProgramStepData programStepData = timeDrivenProgramTableModel.getRow(row);
                MclnState mclnState = mclnStatementState.getMclnState();
                programStepData.setGeneratedState(mclnState);
                initAssistantDataModel.fireProgramRowChanged();
                table.repaint();
            }
        });
    }

    @Override
    void appendEmptyRow() {
        int nRows = oneColumnTableModel.getRowCount();
        ProgramStepData programStepData;
        if (nRows == 0) {
            programStepData = new ProgramStepData(true);
        } else {
            programStepData = createEmptyRow();
        }
        oneColumnTableModel.appendRowAndFireEvent(programStepData);
        List<ProgramStepData> currentProgramStatesDataList = oneColumnTableModel.getData();
        initAssistantDataModel.setSelectedProgramSteps(currentProgramStatesDataList);
    }

    @Override
    void insertEmptyRow() {
        Object defaultRow = createEmptyRow();
        int ind;

//        stopEditing();

        if (oneColumnTableModel.getRowCount() <= 0) {
            ind = 0;
        } else {
            ind = table.getSelectedRow();
        }

        if (ind < 0) {
            ind = 0;
        }

        oneColumnTableModel.insertRowAndFireEvent(defaultRow, ind);
        List<ProgramStepData> currentProgramStatesDataList = oneColumnTableModel.getData();
        initAssistantDataModel.setSelectedProgramSteps(currentProgramStatesDataList);
    }

    @Override
    void removeRow() {

//      stopEditing();

        if (oneColumnTableModel.getRowCount() <= 0) {
            return;
        }

        int ind = table.getSelectedRow();
        if (ind < 0) {
            ind = oneColumnTableModel.getRowCount() - 1;
        }

        oneColumnTableModel.removeRowAndFireEvent(ind);
        List<ProgramStepData> currentProgramStatesDataList = oneColumnTableModel.getData();
        initAssistantDataModel.setSelectedProgramSteps(currentProgramStatesDataList);
    }

    private ProgramStepData createEmptyRow() {
        ProgramStepData programStep = new ProgramStepData(false, -1, null);
        return programStep;
    }

    /**
     * @author Administrator
     */
    private class TimeDrivenProgramCellRenderer extends JLabel implements TableCellRenderer {


        private Border border = null;
        private boolean isBordered = true;


        public TimeDrivenProgramCellRenderer(boolean isBordered) {
            super();
            this.isBordered = isBordered;
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int col) {

            setBorder(null);

            if (row == 0) {
                switch (col) {
                    case 0:
                        String strValue = "Phase:";
                        if (!isPhaseEnabled()) {
                            setBackground(PHASE_DISABLE_BACKGROUND);
                            setForeground(Color.BLACK);

                        } else if (isSelected) {
                            setBackground(table.getSelectionBackground());
                            setForeground(table.getSelectionForeground());
                        } else {
                            setBackground(PHASE_COLUMN_ONE_ENABLED_BACKGROUND);
                            setForeground(Color.BLACK);
                        }

                        setText(strValue);
                        Font font = getFont();
                        setFont(new Font(font.getName(), font.BOLD, 10));
                        setHorizontalAlignment(JLabel.CENTER);
                        break;

                    case 2:
                        if (isPhaseEnabled()) {
                            if (value == null) {
                                // processing empty row
                                if (isSelected) {
                                    setBackground(table.getSelectionBackground());
                                    setForeground(table.getSelectionForeground());
                                } else {
                                    setBackground(PHASE_ROW_ENABLED_BACKGROUND);
                                    setForeground(Color.black);

//                                    setBackground(table.getBackground());
//                                    setForeground(Color.black);
                                }
                            } else {

                                int stateValue = ((MclnState) value).getRGB();
                                Color stateColor = new Color(stateValue);
                                Color contourColor = Color.DARK_GRAY;

                                setBackground(stateColor);
                                if (isSelected) {
                                    border = new CompoundBorder(BorderFactory.createMatteBorder(2, 10, 2, 10,
                                            table.getSelectionBackground()), new LineBorder(contourColor));
                                    setBorder(border);
                                } else {
                                    border = new CompoundBorder(BorderFactory.createMatteBorder(2, 10, 2, 10,
                                            PHASE_ROW_ENABLED_BACKGROUND), new LineBorder(contourColor));
                                    setBorder(border);
                                }
                            }

                        } else {
                            setBackground(PHASE_DISABLE_BACKGROUND);
                            setBorder(null);
                        }
                        setText("");
                        break;
                }
                return this;
            }

            // other rows

            switch (col) {
                case 0:
                    processNumberColumn(table, value, isSelected);
                    break;
                case 2:
                    processStateColumn(this, table, value, isSelected);
                    break;
            }
            return this;
        }

        /**
         * @param table
         * @param value
         * @param isSelected
         */
        private void processNumberColumn(JTable table, Object value, boolean isSelected) {
            setBorder(null);
            int numValue = ((Integer) value).intValue();
            String strRowNumber = String.format("%03d", numValue);

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(Color.black);
            }

            setText(strRowNumber);
            setFont(PLAIN_FONT);
            setHorizontalAlignment(JLabel.CENTER);
        }

        /**
         * @param table
         * @param value
         * @param isSelected
         */
//        private void processStateColumn(JTable table, Object value, boolean isSelected) {
//
//            if(value == null){
//                // processing empty row
//                if (isSelected) {
//                    setBackground(table.getSelectionBackground());
//                    setForeground(table.getSelectionForeground());
//                } else {
//                    setBackground(table.getBackground());
//                    setForeground(Color.black);
//                }
//            }else {
//                int stateValue = ((MclnState) value).getRGB();
//                Color stateColor = new Color(stateValue);
//                Color contourColor = Color.DARK_GRAY;
//
//                setBackground(stateColor);
//
//                if (isSelected) {
//                    border = new CompoundBorder(BorderFactory.createMatteBorder(2, 10, 2, 10,
//                            table.getSelectionBackground()), new LineBorder(contourColor));
//                    setBorder(border);
//                } else {
//                    border = new CompoundBorder(BorderFactory.createMatteBorder(2, 10, 2, 10, bgColor),
//                            new LineBorder(contourColor));
//                    setBorder(border);
//                }
//            }
//            setText("");
//        }
    }
}
