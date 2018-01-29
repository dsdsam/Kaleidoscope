package dsdsse.designspace.initializer;

import mcln.model.MclnStatementState;
import mcln.model.ProgramStepData;
import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

/**
 * Created by Admin on 5/9/2016.
 */
final class RuleDrivenProgramTablePanel<T> extends AbstractProgramTablePanel<RuleDrivenProgramTablePanel> {

    private static String COLUMN_0_HEADER = "Rule N";
    private static String COLUMN_1_HEADER = "Cond";
    private static String COLUMN_2_HEADER = "Ticks";
    private static String COLUMN_3_HEADER = "State";

    private static final String[] RULE_DRIVEN_PROGRAM_COLUMN_NAMES =
            {COLUMN_0_HEADER, COLUMN_1_HEADER, COLUMN_2_HEADER, COLUMN_3_HEADER};

    private static final int[] RULE_DRIVEN_PROGRAM_COLUMN_WIDTH =
            {45, 42, 41, 42};

    private StateDrivenProgramCellRenderer stateDrivenProgramCellRenderer = new StateDrivenProgramCellRenderer(true);
    private Color bgColor = Color.white;

    private final StateDrivenProgramTableModel<ProgramStepData> stateDrivenProgramTableModel;

    public RuleDrivenProgramTablePanel(InitAssistantDataModel initAssistantDataModel) {
        super(initAssistantDataModel, RULE_DRIVEN_PROGRAM_COLUMN_WIDTH,
                new StateDrivenProgramTableModel<ProgramStepData>(RULE_DRIVEN_PROGRAM_COLUMN_NAMES,
                        initAssistantDataModel));
        this.stateDrivenProgramTableModel = (StateDrivenProgramTableModel) oneColumnTableModel;
        initContexts(stateDrivenProgramCellRenderer);
    }

    void setData(java.util.List<ProgramStepData> programSteps) {
        stateDrivenProgramTableModel.setData(programSteps);
    }

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
        numberColumn.setPreferredWidth(RULE_DRIVEN_PROGRAM_COLUMN_WIDTH[0]);
        numberColumn.setCellRenderer(stateDrivenProgramCellRenderer);

//      "Condition" column.
        TableColumn conditionColumn = table.getColumn(COLUMN_1_HEADER);
        headerRenderer = conditionColumn.getHeaderRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer)
            ((DefaultTableCellRenderer) headerRenderer).setToolTipText
                    ("The is to be set, when ticks are expired.");
        conditionColumn.setPreferredWidth(RULE_DRIVEN_PROGRAM_COLUMN_WIDTH[1]);
        conditionColumn.setCellRenderer(stateDrivenProgramCellRenderer);

        // "Ticks" column.
        TableColumn ticksColumn = table.getColumn(COLUMN_2_HEADER);
        headerRenderer = ticksColumn.getHeaderRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer) headerRenderer).setToolTipText
                    ("The number of ticks to pass before new state is set.");
        }
       /*
           DefaultTableCellRenderer numberColumnRenderer = new DefaultTableCellRenderer() {
           public void setValue(Object value) {
         // int cellValue = (value instanceof String) ? ((String)value).intValue() : 0;
        //  setForeground((cellValue > 30) ? Color.black : Color.red);
        //  setText((value == null) ? "" : value.toString());
            setBackground(Color.red);
          }
           };
           numberColumnRenderer.setHorizontalAlignment(JLabel.RIGHT);
           ticksColumn.setCellRenderer(numberColumnRenderer);
       */

        StringCellRenderer strCellRenderer = new StringCellRenderer(false);
        strCellRenderer.setToolTipText("Click for to edit ticks value.");
        strCellRenderer.setHorizontalAlignment(JLabel.RIGHT);
        ticksColumn.setCellRenderer(strCellRenderer);
//
//        cellTextField = new JTextField( new TicksInputDocument(),
//                                        null, 3 );
//        Font font = cellTextField.getFont();
//        cellTextField.setFont( new Font( font.getName(), font.BOLD, 11 ));
//        cellTextField.setHorizontalAlignment(JTextField.CENTER);
////        cellTextField.setMargin( new Insets( 3, 3, 3, 3 ));
//        ticksColumn.setCellEditor( new DefaultCellEditor( cellTextField ) );
        ticksColumn.setPreferredWidth(RULE_DRIVEN_PROGRAM_COLUMN_WIDTH[2]);

        cellTextField = new JTextField(new TicksInputDocument(1, "Rule"), null, 3);
        Font font = cellTextField.getFont();
        cellTextField.setFont(new Font(font.getName(), font.BOLD, 11));
        cellTextField.setHorizontalAlignment(JTextField.CENTER);
//        cellTextField.setMargin( new Insets( 3, 3, 3, 3 ));
        ticksColumn.setCellEditor(new DefaultCellEditor(cellTextField));

//      "State" column.
        TableColumn stateColumn = table.getColumn(COLUMN_3_HEADER);
        headerRenderer = stateColumn.getHeaderRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer)
            ((DefaultTableCellRenderer) headerRenderer).setToolTipText
                    ("The is to be set, when ticks are expired.");
        stateColumn.setPreferredWidth(RULE_DRIVEN_PROGRAM_COLUMN_WIDTH[3]);
        stateColumn.setCellRenderer(stateDrivenProgramCellRenderer);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row < 0 || col < 0) {
                    return;
                }
                if (col != 1 && col != 3) {
                    return;
                }

                MclnStatementState mclnStatementState = initAssistantDataModel.getProgramMclnStateCandidate();
                if (mclnStatementState == null) {
                    System.out.println("Candidate State is not selected");
                    initAssistantDataModel.showMessage("Candidate State is not selected");
                    return;
                }

                System.out.println(" row " + row + "  col " + col + "  candidate " + mclnStatementState);
                ProgramStepData programStepData = stateDrivenProgramTableModel.getRow(row);
                MclnState mclnState = mclnStatementState.getMclnState();
                if (col == 1) {
                    programStepData.setConditionState(mclnState);
                    initAssistantDataModel.fireProgramRowChanged();
                }
                if (col == 3) {
                    programStepData.setGeneratedState(mclnState);
                    initAssistantDataModel.fireProgramRowChanged();
                }

                table.repaint();
            }
        });

    }

    @Override
    void appendEmptyRow() {
        ProgramStepData programStepData = createEmptyRow();
        oneColumnTableModel.appendRowAndFireEvent(programStepData);
        java.util.List<ProgramStepData> currentProgramStatesDataList = oneColumnTableModel.getData();
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
        ProgramStepData programStep = new ProgramStepData( );
        return programStep;
    }

    /**
     * @author Administrator
     */
    private class StateDrivenProgramCellRenderer extends JLabel implements TableCellRenderer {

        private Border border = null;
        private boolean isBordered = true;

        public StateDrivenProgramCellRenderer(boolean isBordered) {
            super();
            this.isBordered = isBordered;
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object state, boolean isSelected,
                                                       boolean hasFocus, int row, int col) {

            switch (col) {
                case 0:
                    processNumberColumn(table, state, isSelected);
                    break;
                case 1:
                    processStateColumn(this, table, state, isSelected);
                    break;
//                case 2:
//                    processNumberColumn(table, state, isSelected);
//                    break;
                case 3:
                    processStateColumn(this, table, state, isSelected);
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


    }


    //        public void processNumberColumn(JTable table, int row, Object state,
//                boolean isSelected) {
//            /*
//             * if (row == 0 && curInpSimProg.isSelectedProgramTimeDrivenProgram() &&
//             * !phaseNeeded) { setBackground( Color.lightGray );
//             * setBorder(null); setForeground( Color.black ); setText("");
//             * return; }
//             */
//            Integer intState;
//            int numValue;
//            String strValue = "";
//
//            if (row == 0 && curInpSimProg.isSelectedProgramTimeDrivenProgram()) {
//                strValue = "Phase:";
//
//                if (!phaseNeeded) {
//                    setBackground(Color.lightGray);
//                    setForeground(Color.gray);
//                } else {
//                    setBackground(Color.lightGray);
//                    setForeground(Color.black);
//                }
//
//                setBorder(null);
//                setText(strValue);
//                Font font = getFont();
//                setFont(new Font(font.getName(), font.BOLD, 10));
//                setHorizontalAlignment(JLabel.CENTER);
//                return;
//                // System.out.println("Object =" + state.getClass().getName());
//            } else {
//                intState = (Integer) state;
//                numValue = intState.intValue();
//
//                if (numValue == -1)
//                    strValue = "";
//                else {
//                    strValue = // new String( ""+(numValue) );
//                    new String(
//                            ""
//                                    + (numValue + (curInpSimProg
//                                            .isSelectedProgramTimeDrivenProgram() ? 0 : 1)));
//                    while (strValue.length() < 3)
//                        strValue = new String("0" + strValue);
//                }
//            }
//
//            // System.out.println("FIRST COLOMN");
//            setBorder(null);
//            if (isSelected && !curInpSimProg.isRuleDrivenProgram()) {
//                setBackground(table.getSelectionBackground());
//                setForeground(table.getSelectionForeground());
//            } else {
//                setBackground(table.getBackground());
//                setForeground(Color.black);
//            }
//
//            setText(strValue);
//            Font font = getFont();
//            setFont(new Font(font.getName(), font.PLAIN, font.getSize()));
//            setHorizontalAlignment(JLabel.CENTER);
//        }
//
//        // -----------------------------------------------------------------
//        public void processStateColumn(JTable table, int row, Object state,
//                boolean isSelected) {
//            Integer intState;
//            int numValue;
//            String strValue = "";
//
//            intState = (Integer) state;
//            numValue = intState.intValue();
//
//            Color conturColor = Color.darkGray;
//            if (isSelected) {
//                if (numValue == -1) {
//                    setBackground(table.getSelectionBackground());
//                    conturColor = table.getSelectionBackground();
//                } else
//                    setBackground(new Color(numValue));
//
//                border = new CompoundBorder(BorderFactory.createMatteBorder(2,
//                        10, 2, 10, table.getSelectionBackground()),
//                        new LineBorder(conturColor));
//                setBorder(border);
//            } else {
//                if (numValue == -1) {
//                    setBackground(bgColor);
//                    conturColor = bgColor;
//                } else
//                    setBackground(new Color(numValue));
//
//                border = new CompoundBorder(BorderFactory.createMatteBorder(2,
//                        10, 2, 10, bgColor), new LineBorder(conturColor));
//                setBorder(border);
//            }
//            if (row == 0 && curInpSimProg.isSelectedProgramTimeDrivenProgram() && !phaseNeeded) {
//                setBackground(Color.lightGray);
//                setBorder(null);
//                setForeground(Color.black);
//            }
//            setText("");
//        }
//
//        //-----------------------------------------------------------------
//        /*
//         public Component getStateDrivenProgramCellRendererComponent(
//         JTable table, Object state,
//         boolean isSelected, boolean hasFocus,
//         int row, int column )
//         {
//         if ( column == 0 )
//         processNumberColumn( table, row, state, isSelected );
//
//         if ( column == 1 )
//         processStateColumn( table, row, state, isSelected );
//
//         if ( column == 3 )
//         processStateColumn( table, row, state, isSelected );
//
//         return this;
//         }
//         */
//        //-----------------------------------------------------------------


}
