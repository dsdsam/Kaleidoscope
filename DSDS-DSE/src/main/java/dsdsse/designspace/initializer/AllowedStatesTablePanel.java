package dsdsse.designspace.initializer;

import adf.utils.StandardFonts;
import adf.ui.laf.button.Adf3DButton;
import mcln.model.MclnStatementState;
import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by Admin on 3/20/2016.
 */
final class AllowedStatesTablePanel extends JPanel {


    private static final int PANEL_HEIGHT = 100;
    private static final Dimension PANEL_SIZE = new Dimension(1, PANEL_HEIGHT);

    private static final int INFO_PANEL_HOLDER_HEIGHT = 30;
    private static final Dimension INFO_PANEL_HOLDER_SIZE = new Dimension(1, INFO_PANEL_HOLDER_HEIGHT);

    private static final int OK_BUTTON_WIDTH = 40;
    private static final int OK_BUTTON_HEIGHT = 20;
    private static final Dimension OK_BUTTON_SIZE = new Dimension(OK_BUTTON_WIDTH, OK_BUTTON_HEIGHT);

    private static final Dimension PROGRAM_CANDIDATE_STATE_VIEW_SIZE = new Dimension(40, 20);

    private static final Color PANEL_BACKGROUND = new Color(0xDEDEDE);
    private static final Color VIEWPORT_BACKGROUND = new Color(0xF7F7F7);
    private static final Color DISABLE_BACKGROUND = new Color(0x606060);

    private final String SELECTED_PALETTE_TEMPLATE = new StringBuilder().append("<html><p>").
            append("<font  color=\"#333333\" size=\"" + 2 + "\">").append("Palette:&nbsp;&nbsp;").append("</font>").
            append("<font  color=\"#0000AA\" size=\"" + 2 + "\">").append("\"$$$\"").append("</font>").
            append("</p></html>").toString();

    private final String SELECTED_ALLOWED_STATES_TEMPLATE = new StringBuilder().append("<html><p>").
            append("<font  color=\"#333333\" size=\"" + 2 + "\">").
            append("Allowed states selected:&nbsp;&nbsp;").append("</font>").
            append("<font  color=\"#0000AA\" size=\"" + 2 + "\">").append("$$$").append("</font>").
            append("</p></html>").toString();

    private final String INITIALIZATION_EXAMPLE = new StringBuilder().append("<html><p>").
            append("<font  color=\"#333333\" size=\"" + 2 + "\">").
            append("Initialization example:&nbsp;&nbsp;").append("</font>").
            append("<font  color=\"#0000AA\" size=\"" + 2 + "\">").append("[My car] [color] [is red]").append("</font>").
            append("</p></html>").toString();

    private final String CURRENT_INITIALIZATION = new StringBuilder().append("<html><p>").
            append("<font  color=\"#333333\" size=\"" + 2 + "\">").
            append("Current &nbsp;Interpretation:&nbsp;&nbsp;").append("</font>").
            append("<font  color=\"#0000AA\" size=\"" + 2 + "\">").append("$ ").append("</font>").
            append("</p></html>").toString();

    private final String CANDIDATE_STATE_SELECTED_TEXT = "Program candidate state selected:";
    private final String CANDIDATE_STATE_NOT_SELECTED_TEXT =
//            "Please click on the table row to select program candidate state";
    "Please pick up the program candidate state by clicking on the state column ";

    private final JLabel upperLabel = new JLabel();
    private final JLabel lowerLabel = new JLabel();
    private final JLabel candidateStateLabel = new JLabel();
    private final JLabel candidateStateViewLabel = new JLabel();

    private final InitAssistantController initAssistantController;
    private final InitAssistantDataModel initAssistantDataModel;


    private MclnStatePaletteTable mclnStatePaletteTable;
    private MclnStatePaletteTableModel mclnStatePaletteTableModel;

    private AllowedStatesTable allowedStatesTable;
    private AllowedStateTableModel allowedStateTableModel;

    //    private OneColumnTableModel<MclnState> currentTableModel;
    private JScrollPane scrollPanel;
    private final JViewport viewPort = null;

    private final JPanel messagePanel = new JPanel(new GridBagLayout());
    private final JLabel messageLabel = new JLabel();
    private final JButton messageButton = new Adf3DButton("OK");
    private final JPanel infoPanelHolder = new JPanel(new BorderLayout());
    private final JPanel infoPanel = new JPanel(new GridBagLayout());

//    private final StringBuilder stringBuilder = new StringBuilder();

    private Timer hideMessageTimer = new Timer(1000, (e) -> {
        clearMessage();
    });

    /**
     * Is used to update selected state statement
     */
    ListSelectionListener listSelectionListener = e -> {
        if (e.getValueIsAdjusting()) {
            return;
        }
        int selectedRowIndex = allowedStatesTable.getSelectedRow();
        if (selectedRowIndex < 0 || selectedRowIndex >= allowedStatesTable.getRowCount()) {
            return;
        }
        updateInterpretationStatement(selectedRowIndex);
    };

    private MouseAdapter mouseListener = new MouseAdapter() {
        public synchronized void mousePressed(MouseEvent e) {
            Point clickedPoint = e.getPoint();
            int clickedRow = allowedStatesTable.rowAtPoint(clickedPoint);
            int clickedColumn = allowedStatesTable.columnAtPoint(clickedPoint);
            processCellClicked(clickedRow, clickedColumn);
            e.consume();
        }
    };

    private final ControllerRequestListener controllerRequestListener =
            (InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel,
             InitAssistantController.PageNavigationRequest operation,
             InitAssistantController.InitializationPage page) -> {
                switch (operation) {
                    case InitialPage:
                        if (page == InitAssistantController.InitializationPage.PaletteAndAllowedStatesPage) {
                            scrollPanel.setViewportView(mclnStatePaletteTable);
                            initPaletteTableInfoPanel();
                        }
                        break;
                    case LeavingPage:
                        if (page == InitAssistantController.InitializationPage.PaletteAndAllowedStatesPage) {
                            mclnStatePaletteTable.clearSelection();
                        } else if (page == InitAssistantController.InitializationPage.InitInterpretationPage) {
                            if (allowedStatesTable.isEditing()) {
                                allowedStatesTable.getCellEditor().stopCellEditing();
                            }
                            allowedStatesTable.clearSelection();

                        } else if (page == InitAssistantController.InitializationPage.initInitialStatePage) {
                            allowedStatesTable.clearSelection();
                        } else if (page == InitAssistantController.InitializationPage.InitInputGeneratorPage) {
                            clearMessage();
                            allowedStatesTable.clearSelection();
                        } else if (page == InitAssistantController.InitializationPage.InitArcStatePage) {
                        }
                        break;

                    case NextPage:

                        allowedStateTableModel.setEditingAllowed(false);

                        if (page == InitAssistantController.InitializationPage.PaletteAndAllowedStatesPage) {
//                            mclnStatePaletteTable.clearSelection();
//                            allowedStatesTable.clearSelection();
                            scrollPanel.setViewportView(mclnStatePaletteTable);
                            initPaletteTableInfoPanel();

                        } else if (page == InitAssistantController.InitializationPage.InitInterpretationPage) {
//                            allowedStatesTable.clearSelection();

                            allowedStateTableModel = new AllowedStateTableModel(initAssistantDataModel);
                            allowedStatesTable = new AllowedStatesTable(allowedStateTableModel);
                            setupAllowedStatesTable(allowedStatesTable);

                            // updating table structure on palette type changed
//                            boolean processingPairsOfOppositeStates =
//                                    mclnStatePaletteTableModel.isPairsOfOppositeStatesPalette();
//                            allowedStateTableModel.setPaletteType(processingPairsOfOppositeStates);


                            // transferring selected Mcln Allowed States from MclnStatePaletteTableModel
                            // to InitAssistantDataModel and to AllowedStateTableModel
//                            List<MclnState> paletteSelectedAllowedStatesList =
//                                    mclnStatePaletteTableModel.getUpdatedAllowedStatesList();
//                            initAssistantDataModel.updateCurrentAllowedStatesListOnPaletItemSelected(paletteSelectedAllowedStatesList);
//
//                            // updating Allowed States Table data
//                            List<MclnStatementState> currentAllowedStatesList =
//                                    initAssistantDataModel.getCurrentAllowedStatesList();
//                            allowedStateTableModel.setSelectedAllowedMclnStates(currentAllowedStatesList);

                            allowedStateTableModel.setEditingAllowed(true);

                            scrollPanel.setViewportView(allowedStatesTable);
                            initAllowedStatesTableInfoPanel();

                        } else if (page == InitAssistantController.InitializationPage.initInitialStatePage) {
//                            allowedStatesTable.clearSelection();
                            List<MclnStatementState> currentAllowedStatesList =
                                    initAssistantDataModel.getCurrentAllowedStatesList();
                            allowedStateTableModel.setSelectedAllowedMclnStates(currentAllowedStatesList);
                            initAllowedStatesTableInfoPanel();
                        } else if (page == InitAssistantController.InitializationPage.InitInputGeneratorPage) {
//                            allowedStatesTable.clearSelection();
                            initAssistantDataModel.setProgramMclnStateCandidate(null);
                            initProgramInitializationInfoPanel();
                        } else if (page == InitAssistantController.InitializationPage.InitArcStatePage) {
                            System.out.println("AllowedStatesTablePanel InitArcStatePage");
                            scrollPanel.setViewportView(allowedStatesTable);
                        }
                        break;
                }


            };


    private final InitAssistantDataModelListener initAssistantDataModelListener = new InitAssistantDataModelListener() {

        @Override
        public void onInitAssistantDataModelChanged(InitAssistantDataModel initAssistantDataModel,
                                                    InitAssistantDataModel.AttributeID attributeID,
                                                    boolean initialized) {
            switch (attributeID) {
                case StatePaletteChosen:
                    List<MclnState> mclnStatesPaletteAsList = initAssistantDataModel.geMclnStatesPaletteAsList();
                    boolean[] selectedStates = initAssistantDataModel.getPaletteSelectedStates();
                    mclnStatePaletteTableModel.updateModelUpOnPaletteReselected(mclnStatesPaletteAsList, selectedStates);
                    mclnStatePaletteTable.initTable();
                    initPaletteTableInfoPanel();

//                    if (!initAssistantDataModel.isSelectedAllowedStatesListValid()) {
////                        initAssistantDataModel.fireAdfDataModelChanged(InitAssistantDataModel.AttributeID.AllowedStatesChoiceChanged, false);
//                        initAssistantDataModel.showInfoMessage(
//                                InitAssistantMessagesAndDialogs.MESSAGE_AVAILABLE_STATES_NOT_SELECTED);
//
//                    }

                    break;

                case ComponentName:
                case PropertyName:
//                    String componentName = initAssistantDataModel.getComponentName();
//                    System.out.println("AllowedStatesTablePanel - ComponentName = \"" + componentName + "\"");
//                    String propertyName = initAssistantDataModel.getPropertyName();
//                    System.out.println("AllowedStatesTablePanel - PropertyName = \"" + propertyName + "\"");
//                   String   interpretationText = buildStateInterpretation(componentName, propertyName);
//                    lowerValue.setText(interpretationText);
                    initAllowedStatesTableInfoPanel();
                    break;
                case AllowedStatementState:
                    initAllowedStatesTableInfoPanel();
                    break;
                case HasProgram:
                    System.out.println("AllowedStatesTablePanel - HasProgram " + attributeID);
                    break;
                case TimeDrivenGeneratorSelected:
                case RuleDrivenGeneratorSelected:
                    System.out.println("AllowedStatesTablePanel - Program " + attributeID);
                    initAssistantDataModel.setProgramMclnStateCandidate(null);
                    initProgramInitializationInfoPanel();
                    allowedStatesTable.clearSelection();
                    clearMessage();
                    break;
                case AllowedStatesChoiceChanged:
//                    initAssistantDataModel.getAllowedStatesList();
//                    allowedStateTableModel.setSelectedAllowedMclnStates(currentAllowedStatesList);
                    initPaletteTableInfoPanel();
                    break;
            }
        }

        @Override
        public void showMessage(String message) {
            String warningMessage = new StringBuilder().
                    append("<html>").
                    append("<font color=red>Warning:</font>").
                    append("<font color=\"#000000\">&nbsp;").append(message).append("</font>").
                    append("</html>").toString();

            messageLabel.setText(warningMessage);
            messageButton.setVisible(true);

            infoPanelHolder.removeAll();
            infoPanelHolder.add(messagePanel);
            infoPanelHolder.validate();
            infoPanelHolder.repaint();
        }

        @Override
        public void showAndHideMessage(String message) {
            String infoMessage = new StringBuilder().
                    append("<html>").
                    append("<font color=red>Info:</font>").
                    append("<font color=\"#000000\">&nbsp;").append(message).append("</font>").
                    append("</html>").toString();

            messageLabel.setText(infoMessage);
            messageButton.setVisible(false);

            infoPanelHolder.removeAll();
            infoPanelHolder.add(messagePanel);
            infoPanelHolder.validate();
            infoPanelHolder.repaint();
            hideMessageTimer.start();
        }
    };

    private final void clearMessage() {
        if (hideMessageTimer.isRunning()) {
            hideMessageTimer.stop();
        }
        messageLabel.setText("");
        infoPanelHolder.removeAll();
        infoPanelHolder.add(infoPanel);
        infoPanelHolder.validate();
        infoPanelHolder.repaint();
    }

    /**
     *
     */
    private void initPaletteTableInfoPanel() {

        infoPanel.removeAll();
        // upper label
        infoPanel.add(upperLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));

        // lower label
        infoPanel.add(lowerLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));

        String currentMclnStatesPaletteName = initAssistantDataModel.getMclnStatesPaletteName();
        String paletteName = SELECTED_PALETTE_TEMPLATE.replace("$$$", currentMclnStatesPaletteName);
        upperLabel.setText(paletteName);

        int selectedAllowedStatesNumber = mclnStatePaletteTableModel.getSelectedAllowedStatesNumber();
        String selectedStatesNumber =
                SELECTED_ALLOWED_STATES_TEMPLATE.replace("$$$", Integer.toString(selectedAllowedStatesNumber));
        lowerLabel.setText(selectedStatesNumber);

        infoPanelHolder.removeAll();
        infoPanelHolder.add(infoPanel);
        infoPanelHolder.validate();
        infoPanelHolder.repaint();
    }

    /**
     *
     */
    private final void initAllowedStatesTableInfoPanel() {

        infoPanel.removeAll();
        // upper label
        infoPanel.add(upperLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));

        // lower label
        infoPanel.add(lowerLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));

        upperLabel.setText(INITIALIZATION_EXAMPLE);

        infoPanelHolder.removeAll();
        infoPanelHolder.add(infoPanel);
        infoPanelHolder.validate();
        infoPanelHolder.repaint();

        int currentlySelectedRow = allowedStatesTable.getSelectedRow();
        updateInterpretationStatement(currentlySelectedRow);
    }

    /**
     *
     */
    private final void updateInterpretationStatement(int rowIndex) {

        String componentName = initAssistantDataModel.getComponentName();
        String propertyName = initAssistantDataModel.getPropertyName();
        String stateInterpretation = "";

        if (rowIndex >= 0) {
            MclnStatementState mclnStatementState =
                    initAssistantDataModel.getMclnStatementStateByIndex(rowIndex);
            if (mclnStatementState != null) {
                stateInterpretation = mclnStatementState.getStateInterpretation();
            } else {
                stateInterpretation = "(Not specified";
            }
            stateInterpretation = stateInterpretation.replace("$", "");

        }

        componentName = componentName.trim();
        propertyName = propertyName.trim();
        stateInterpretation.trim();

        if (componentName.length() == 0) {
            componentName = "(Not specified)";
        }

        String statement = componentName + " " + propertyName;

        if (stateInterpretation != null && stateInterpretation.contains("$")) {
            statement = stateInterpretation.replace("$", statement);
        } else {
            statement = statement + " " + stateInterpretation;
        }

        String currentInterpretation = CURRENT_INITIALIZATION;
        currentInterpretation = currentInterpretation.replace("$", statement);

        lowerLabel.setText(currentInterpretation);
        infoPanel.validate();
    }

    /**
     *
     */
    private final void initProgramInitializationInfoPanel() {

        infoPanel.removeAll();

        MclnStatementState programCandidateMclnStatementState = initAssistantDataModel.getProgramMclnStateCandidate();
        if (programCandidateMclnStatementState != null) {

            candidateStateLabel.setText(CANDIDATE_STATE_SELECTED_TEXT);

            candidateStateViewLabel.setPreferredSize(PROGRAM_CANDIDATE_STATE_VIEW_SIZE);
            candidateStateViewLabel.setMaximumSize(PROGRAM_CANDIDATE_STATE_VIEW_SIZE);
            MclnState mclnState = programCandidateMclnStatementState.getMclnState();
            int stateRGB = mclnState.getRGB();
            candidateStateViewLabel.setBackground(new Color(stateRGB));
            candidateStateViewLabel.setOpaque(true);
            clearMessage();
        } else {
            candidateStateLabel.setText(CANDIDATE_STATE_NOT_SELECTED_TEXT);
            candidateStateViewLabel.setOpaque(false);
        }

        infoPanel.add(candidateStateLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 10), 0, 0));
        infoPanel.add(candidateStateViewLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        infoPanelHolder.removeAll();
        infoPanelHolder.add(infoPanel);
        infoPanelHolder.validate();
        infoPanelHolder.repaint();
    }

    //   c r e a t i o n

    AllowedStatesTablePanel(InitAssistantController initAssistantController,
                            InitAssistantDataModel initAssistantDataModel) {
        super(new GridBagLayout());
        this.initAssistantController = initAssistantController;
        this.initAssistantDataModel = initAssistantDataModel;
        setBackground(PANEL_BACKGROUND);

        // init components

        // message panel
        messagePanel.setOpaque(true);
        messagePanel.setBackground(Color.YELLOW);
        messagePanel.setBorder(new MatteBorder(4, 0, 4, 0, PANEL_BACKGROUND));

        messageLabel.setFont(StandardFonts.FONT_MONOSPACED_BOLD_11);
        messageLabel.setOpaque(false);
        messageLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 3, 0, PANEL_BACKGROUND),
                BorderFactory.createEmptyBorder(0, 3, 0, 3)));

        messageButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);
        messageButton.setMinimumSize(OK_BUTTON_SIZE);
        messageButton.setMaximumSize(OK_BUTTON_SIZE);
        messageButton.setPreferredSize(OK_BUTTON_SIZE);
        messageButton.setBorder(null);
        messageButton.addActionListener((e) -> {
            clearMessage();
        });

        messagePanel.add(messageLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        messagePanel.add(messageButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 1), 0, 0));

        // info panel
        infoPanel.setOpaque(false);

        infoPanelHolder.setBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY));
        infoPanelHolder.setOpaque(false);
        infoPanelHolder.setMinimumSize(INFO_PANEL_HOLDER_SIZE);
        infoPanelHolder.setPreferredSize(INFO_PANEL_HOLDER_SIZE);
        infoPanelHolder.setMaximumSize(INFO_PANEL_HOLDER_SIZE);

        upperLabel.setFont(StandardFonts.FONT_HELVETICA_PLAIN_11);
        lowerLabel.setFont(StandardFonts.FONT_HELVETICA_PLAIN_11);
        candidateStateLabel.setFont(StandardFonts.FONT_HELVETICA_PLAIN_11);
//        candidateStateLabel.setForeground(new Color(0x666666));
        candidateStateLabel.setForeground(new Color(0x0000AA));
        candidateStateViewLabel.setOpaque(true);

        //
        // Creating Palette Table
        //

//        boolean thisIsPairsOfOppositeStatesPalette = initAssistantDataModel.isPairsOfOppositeStatesPalette();
//        if (thisIsPairsOfOppositeStatesPalette) {
//
//            List<MclnState> mclnStatesPaletteAsList = initAssistantDataModel.geMclnStatesPaletteAsList();
//            boolean[] selectedStates = initAssistantDataModel.getPaletteSelectedStates();
//            mclnStatePaletteTableModel = new MclnStatePaletteTableModel(initAssistantDataModel,
//                    mclnStatesPaletteAsList, selectedStates);
//
//        } else {
//
//            List<MclnState> mclnStatesPaletteAsList = initAssistantDataModel.geMclnStatesPaletteAsList();
//            boolean[] selectedStates = initAssistantDataModel.getPaletteSelectedStates();
//            mclnStatePaletteTableModel = new MclnStatePaletteTableModel(initAssistantDataModel,
//                    mclnStatesPaletteAsList, selectedStates);
//
//        }

        List<MclnState> mclnStatesPaletteAsList = initAssistantDataModel.geMclnStatesPaletteAsList();
        boolean[] selectedStates = initAssistantDataModel.getPaletteSelectedStates();
        mclnStatePaletteTableModel = new MclnStatePaletteTableModel(initAssistantDataModel, mclnStatesPaletteAsList,
                selectedStates);
        mclnStatePaletteTable = new MclnStatePaletteTable(mclnStatePaletteTableModel);

        //
        // Creating Allowed States Table
        //

        allowedStateTableModel = new AllowedStateTableModel(initAssistantDataModel);
        allowedStatesTable = new AllowedStatesTable(allowedStateTableModel);


        initAssistantController.addListener(controllerRequestListener);

        if (!initAssistantDataModel.isAssistantInitialized()) {
            scrollPanel = new JScrollPane();
            scrollPanel.setOpaque(false);
            initScrollPanel(scrollPanel);
        } else if (initAssistantDataModel.isInitializingProperty()) {
            scrollPanel = new JScrollPane(mclnStatePaletteTable);
            scrollPanel.setOpaque(false);
            initScrollPanel(scrollPanel);
            initPaletteTableInfoPanel();
//            allowedStatesTable.addMouseListener(mouseListener);
//            ListSelectionModel listSelectionModel = allowedStatesTable.getSelectionModel();
//            listSelectionModel.addListSelectionListener(listSelectionListener);
        } else {
            scrollPanel = new JScrollPane(allowedStatesTable);
            scrollPanel.setOpaque(false);
            initScrollPanel(scrollPanel);
            initAllowedStatesTableInfoPanel();
//            allowedStatesTable.addMouseListener(mouseListener);
        }

        add(infoPanelHolder, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(scrollPanel, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        setMinimumSize(PANEL_SIZE);
        setPreferredSize(PANEL_SIZE);

        initAssistantDataModel.addListener(initAssistantDataModelListener);

        setupAllowedStatesTable(allowedStatesTable);
    }

    private void setupAllowedStatesTable(AllowedStatesTable allowedStatesTable) {
        allowedStatesTable.addMouseListener(mouseListener);
        // this listener updates info panel
        ListSelectionModel listSelectionModel = allowedStatesTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(listSelectionListener);
    }

    /**
     * @param clickedRowIndex
     * @param clickedColumnIndex
     */
    private void processCellClicked(int clickedRowIndex, int clickedColumnIndex) {

        if (clickedRowIndex < 0 || clickedRowIndex >= allowedStatesTable.getRowCount()) {
            System.out.println("processCellClicked wrong index " + clickedRowIndex + "  " + clickedColumnIndex);
            return;
        }
        System.out.println("processCellClicked " + clickedRowIndex + "  " + clickedColumnIndex);

        boolean theRowIsEven = (clickedRowIndex % 2) == 0;

        // checking if the color column is clicked
        if (initAssistantDataModel.isPairsOfOppositeStatesPalette()) {
            if ((theRowIsEven && clickedColumnIndex != 1) || (!theRowIsEven && clickedColumnIndex != 2)) {
                return;
            }
        } else if (clickedColumnIndex != 1) {
            return;
        }

        MclnStatementState mclnStatementState = allowedStateTableModel.getRow(clickedRowIndex);

        if (initAssistantController.isCurrentStepInitInterpretationPage()) {
            return;
        } else if (initAssistantController.isCurrentStepInitialState()) {
            initAssistantDataModel.setInitialMclnState(mclnStatementState);
            return;
        } else if (initAssistantController.isCurrentStepInitInputGeneratingProgramPage()) {
            initAssistantDataModel.setProgramMclnStateCandidate(mclnStatementState);
            initProgramInitializationInfoPanel();
            return;
        } else if (initAssistantController.isCurrentStepInitArcState()) {
            initAssistantDataModel.setArcSelectedMclnState(mclnStatementState.getMclnState());
            initAllowedStatesTableInfoPanel();
            return;
        } else {
            allowedStatesTable.clearSelection();
        }
    }

    /**
     * @param scrollPanel
     */

    private final void initScrollPanel(JScrollPane scrollPanel) {
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollPanel.getViewport().setBackground(DISABLE_BACKGROUND);
        scrollPanel.setBackground(VIEWPORT_BACKGROUND);
        scrollPanel.getViewport().setBackground(VIEWPORT_BACKGROUND);
        scrollPanel.setBorder(null);


    }
}
