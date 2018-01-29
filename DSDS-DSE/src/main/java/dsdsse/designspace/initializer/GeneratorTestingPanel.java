package dsdsse.designspace.initializer;

import adf.app.StandardFonts;
import adf.ui.laf.button.Adf3DButton;
import mcln.model.*;
import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Admin on 7/23/2016.
 */
class GeneratorTestingPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(GeneratorTestingPanel.class.getName());

    private static final int TICKS_PANEL_HEIGHT = 20;
    private static final Dimension TICKS_PANEL_SIZE = new Dimension(1, TICKS_PANEL_HEIGHT);
    private static final Dimension TICKS_VALUE_SIZE = new Dimension(18, 18);

    //    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 19;
    private static final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
    private static final int EXEC_RULE_BUTTON_WIDTH = 140;
    private static final int EXEC_RULE_BUTTON_HEIGHT = 19;
    private static final Dimension EXEC_RULE_BUTTON_SIZE = new Dimension(EXEC_RULE_BUTTON_WIDTH, EXEC_RULE_BUTTON_HEIGHT);

    private static final String START_GENERATING_OPERATION = "Start";
    private static final String STOP_GENERATING_OPERATION = "Stop";
    private static final String PAUSE_GENERATING_OPERATION = "Pause";
    private static final String RESUME_GENERATING_OPERATION = "Resume";
    private static final String RESET_GENERATING_OPERATION = "Reset";
    private static final String ONE_TICK_OPERATION = "Do Tick";
    private static final String FIND_A_RULE_OPERATION = "Find Applicable Rule";
    private static final String EXECUTE_THE_RULE_TICK_OPERATION = "Do Tick";
//    private static final Color START_GENERATING_COLOR = new Color(0x006600);
//    private static final Color STOP_GENERATING_COLOR = new Color(0xDD0000);

    private static final Color START_GENERATING_COLOR = new Color(0x73A901);
    private static final Color EXEC_RULE_GENERATING_COLOR = new Color(0x006600);
    private static final Color STOP_GENERATING_COLOR = new Color(0xFF3C00);
    private static final Color PAUSE_RESUME_BUTTON_COLOR = new Color(0xCC00CC);
    private static final Color ONE_TICK_BUTTON_COLOR = new Color(0xBE8B3E);
    private static final Color RESET_BUTTON_COLOR = new Color(0x2060DF);
    private static final Color DISABLED_BUTTON_COLOR = new Color(0xCCCCCC);

    private static final String TIME_DRIVEN_GENERATOR_INSTRUCTION = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#780000; text-align:justify; ").
            append(" width:200px; \">").
            append("TO EXECUTE TEST:<br>").
            append(" Click \"Execute Tick\" button to execute one tick at a time.").
            append(" Alternatively, click \"Start Generator\" button to start executing all program").
            append(" steps cyclically.<br>").
            append("</div></html>").toString();

    private static final String RULE_DRIVEN_GENERATOR_INSTRUCTION = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#780000; text-align:justify; ").
            append(" width:200px; \">").
            append("TO EXECUTE TEST:<br>").
            append(" 1) Click \"Start Generator\" button to start the generator.<br>").
            append(" 2) Click right mouse button on the Property Area").
            append("  to open Property Available State popup menu.<br>").
            append(" 3) Pick up the state you would like to test.<br>").
            append("</div></html>").toString();

    private static final String CURRENT_STATE_INTERPRETATION_TEMPLATE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#666666; text-align:justify; ").
            append(" width:200px; \">").
            append("&nbsp;&nbsp;Current State<b>:</b>").
            append("<font size=\"8pt\" color=\"#0000A0\">&nbsp;&nbsp;<b>1$ 2$ 3$</b></font>").
            append("</div></html>").toString();

    private final JLabel timeDrivenGeneratorNoteLabel = new JLabel(TIME_DRIVEN_GENERATOR_INSTRUCTION);
    private final JLabel ruleDrivenGeneratorNoteLabel = new JLabel(RULE_DRIVEN_GENERATOR_INSTRUCTION);
    private final JLabel currentStateInterpretationLabel = new JLabel(CURRENT_STATE_INTERPRETATION_TEMPLATE);

    private final InitAssistantDataModelListener initAssistantDataModelListener =
            (InitAssistantDataModel initAssistantDataModel,
             InitAssistantDataModel.AttributeID attributeID, boolean initialized) -> {
                switch (attributeID) {
                    case TimeDrivenGeneratorSelected:
                    case RuleDrivenGeneratorSelected:
                        logger.info("Generator Testing Panel data model listener:  Build And Show Generator Test Panel " + attributeID);
                        buildAndShowGeneratorTestPanel();
                        break;
                }
            };

    private final InitAssistantController initAssistantController;
    private final InitAssistantDataModel initAssistantDataModel;

    private PropertyTestingPanel propertyTestingPanel;

    //    private static final Color TEXT_COLOR = new Color(0x777777);
    private static final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private static final Color NUMBER_COLOR = new Color(0x0000DD);
    private final JLabel stepsLabel = new JLabel("Step:", JLabel.RIGHT);
    private final JLabel stepsValue = new JLabel("", JLabel.RIGHT);
    private final JLabel ticksLabel = new JLabel("Tick:", JLabel.RIGHT);
    private final JLabel ticksValue = new JLabel("", JLabel.RIGHT);

    private final String NO_RULE_TEXT = "No applicable rule found for the state";
    private final String THE_RULE_EXECUTED_TEXT = "The rule executed";
    private final JLabel ruleLabel = new JLabel("", JLabel.CENTER);
    private final JLabel ruleNumberLabel = new JLabel("002", JLabel.LEFT);
    private final JLabel ruleTickLabel = new JLabel("", JLabel.LEFT);
    private final JLabel simulationStatusLabel = new JLabel("", JLabel.LEFT);

    private final JButton startTDGButton = new Adf3DButton(START_GENERATING_OPERATION);
    private final JButton pauseResumeTDGButton = new Adf3DButton(PAUSE_GENERATING_OPERATION);
    private final JButton oneTickTDGButton = new Adf3DButton(ONE_TICK_OPERATION);
    private final JButton resetTDGButton = new Adf3DButton(RESET_GENERATING_OPERATION);

    private final JButton startRDGButton = new Adf3DButton(FIND_A_RULE_OPERATION);

    private boolean testingTimeDrivenGenerator;
    private boolean simulationIsRunning;

    //    private MclnStatement mclnStatement;
    private ProgramTester programTester;
    private final String componentName;
    private final String propertyName;


    private final Timer simulationTickTimer = new Timer(1000, (e) -> {
        if (!simulationIsRunning) {
            return;
        }
//        System.out.println("Tick ...");
        if (testingTimeDrivenGenerator) {
            simulateTimeDrivenGenerator();
        } else {
            simulateRuleDrivenGenerator();
        }

    });

    /**
     * @param initAssistantController
     * @param initAssistantDataModel
     */
    GeneratorTestingPanel(InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel) {
        super(new BorderLayout());

        this.initAssistantController = initAssistantController;
        this.initAssistantDataModel = initAssistantDataModel;

        componentName = initAssistantDataModel.getComponentName();
        propertyName = initAssistantDataModel.getPropertyName();

//        startTDGButton.setBorder(null);
        startTDGButton.setBorder(new EmptyBorder(0, 0, 2, 0));
        startTDGButton.setMinimumSize(BUTTON_SIZE);
        startTDGButton.setMaximumSize(BUTTON_SIZE);
        startTDGButton.setPreferredSize(BUTTON_SIZE);

        startTDGButton.setFocusPainted(false);
        startTDGButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);

        pauseResumeTDGButton.setEnabled(true);
        startTDGButton.setBackground(START_GENERATING_COLOR);
        startTDGButton.setForeground(Color.WHITE);

        startTDGButton.addActionListener(e -> {
            if (e.getActionCommand().equals(START_GENERATING_OPERATION)) {
//                InputGeneratorState inputGeneratorState = programTester.initSimulationTest();
//                if (initAssistantDataModel.isSelectedProgramTimeDrivenProgram()) {
                doSimulationStatusTransition(START_GENERATING_OPERATION);
//                    updateTimeDrivenTicksDisplayPanel(inputGeneratorState);
//                } else {
//                    updateRuleDrivenTicksDisplayPanel(inputGeneratorState);
//                }

//                if (initAssistantDataModel.isSelectedProgramTimeDrivenProgram()) {
//                    oneTickTDGButton.setEnabled(false);
//                }
            } else {

//                if (initAssistantDataModel.isSelectedProgramTimeDrivenProgram()) {
                doSimulationStatusTransition(STOP_GENERATING_OPERATION);
//                }
            }
        });


//        pauseResumeTDGButton.setBorder(null);
        pauseResumeTDGButton.setBorder(new EmptyBorder(0, 0, 2, 0));
        pauseResumeTDGButton.setMinimumSize(BUTTON_SIZE);
        pauseResumeTDGButton.setMaximumSize(BUTTON_SIZE);
        pauseResumeTDGButton.setPreferredSize(BUTTON_SIZE);

        pauseResumeTDGButton.setFocusPainted(false);
        pauseResumeTDGButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);

        pauseResumeTDGButton.setEnabled(false);
        pauseResumeTDGButton.setBackground(DISABLED_BUTTON_COLOR);
        pauseResumeTDGButton.setForeground(Color.WHITE);

        pauseResumeTDGButton.addActionListener(e -> {
            doSimulationStatusTransition(e.getActionCommand());
        });


//        oneTickTDGButton.setBorder(null);
        oneTickTDGButton.setBorder(new EmptyBorder(0, 0, 2, 0));
        oneTickTDGButton.setMinimumSize(BUTTON_SIZE);
        oneTickTDGButton.setMaximumSize(BUTTON_SIZE);
        oneTickTDGButton.setPreferredSize(BUTTON_SIZE);

        oneTickTDGButton.setFocusPainted(false);
        oneTickTDGButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);


        oneTickTDGButton.setEnabled(true);
        oneTickTDGButton.setBackground(ONE_TICK_BUTTON_COLOR);
        oneTickTDGButton.setForeground(Color.WHITE);

        oneTickTDGButton.addActionListener(e -> {

            doSimulationStatusTransition(e.getActionCommand());
//            InputGeneratorState inputGeneratorState = MclnModelPublicInterface.initSimulationTest(mclnStatement);
//            if (initAssistantDataModel.isSelectedProgramTimeDrivenProgram()) {
//                updateTimeDrivenTicksDisplayPanel(inputGeneratorState);
//            }
//            if (e.getActionCommand().equals(START_GENERATING_OPERATION)) {
//                startSimulation();
//            } else {
//                stopSimulation();
//            }
        });

//        resetTDGButton.setBorder(null);
        resetTDGButton.setBorder(new EmptyBorder(0, 0, 2, 0));
        resetTDGButton.setMinimumSize(BUTTON_SIZE);
        resetTDGButton.setMaximumSize(BUTTON_SIZE);
        resetTDGButton.setPreferredSize(BUTTON_SIZE);

        resetTDGButton.setFocusPainted(false);
        resetTDGButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);

        resetTDGButton.setEnabled(false);
        resetTDGButton.setBackground(DISABLED_BUTTON_COLOR);
        resetTDGButton.setForeground(Color.WHITE);

        resetTDGButton.addActionListener(e -> {
            doSimulationStatusTransition(e.getActionCommand());
        });

        simulationStatusLabel.setText(" Generator Initialized");

        // start/stop Rule Driven Generator button
//        startRDGButton.setBorder(null);
        startRDGButton.setBorder(new EmptyBorder(0, 0, 1, 0));
        startRDGButton.setMinimumSize(EXEC_RULE_BUTTON_SIZE);
        startRDGButton.setMaximumSize(EXEC_RULE_BUTTON_SIZE);
        startRDGButton.setPreferredSize(EXEC_RULE_BUTTON_SIZE);

        startRDGButton.setForeground(EXEC_RULE_GENERATING_COLOR);
        startRDGButton.setFocusPainted(false);
        startRDGButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);

        startRDGButton.addActionListener(e -> {
            simulateRuleDrivenGenerator();

//            if (e.getActionCommand().equals(START_GENERATING_OPERATION)) {
//                InputGeneratorState inputGeneratorState = programTester.initSimulationTest();
//                updateRuleDrivenTicksDisplayPanel(inputGeneratorState);
//                startSimulation();
//            } else {
//                stopSimulation();
//            }
        });
    }

    /**
     * this method is called:
     * 1) When to Start Testing Button on Program Setup Panel has clicked
     * 2) When Generator type changed
     */

    void buildAndShowGeneratorTestPanel() {
        System.out.println("Generator Testing Panel: method Build And Show Generator Test Panel ");
        // Stop simulation if currently running
        if (programTester != null) {
            destroyGeneratorTestingPanel();
        }

        System.out.println("Generator Testing Panel: method Build And Show Generator Test Panel after destroy");

        // build new GeneratorTestPanel

        programTester = preparePropertyForTesting();
        if (programTester == null) {
            return;
        }

        if (initAssistantDataModel.isSelectedProgramTimeDrivenProgram()) {
            testingTimeDrivenGenerator = true;
            JPanel tickDisplayPanel = new TimeDrivenGeneratorStateDisplayPanel();
            initGeneratorTestContent(tickDisplayPanel, timeDrivenGeneratorNoteLabel);
            InputGeneratorState inputGeneratorState = programTester.initSimulationTest();
            updateTimeDrivenTicksDisplayPanel(programTester.theProgramHasPhase(), inputGeneratorState.getGeneratorStep(),
                    inputGeneratorState.getStateCountDown());
        } else {
            testingTimeDrivenGenerator = false;
            JPanel tickDisplayPanel = new RuleDrivenGeneratorStateDisplayPanel();
            initGeneratorTestContent(tickDisplayPanel, ruleDrivenGeneratorNoteLabel);
        }

        prepareSimulation();

        initAssistantDataModel.addListener(initAssistantDataModelListener);
    }

    private void prepareSimulation() {
        skipNextTick = false;
        savedInputGeneratorState = null;
        MclnStatementState initialMclnStatementState = initAssistantDataModel.getInitialMclnStatementState();
        Color itemColor = new Color(initialMclnStatementState.getRGB());
        propertyTestingPanel.setPropertyState(itemColor);
        updateCurrentStateInterpretationDisplay(initialMclnStatementState.getStateID());
    }

    /**
     * this method is called:
     * 1) When to Stop Testing Button clicked
     * 2) When Generator type changed at the time the Generator
     * Testing Panel is open (Start button clicked)
     * 3) When Prev button is clicked to switch to State Initialization Page
     */
    void destroyGeneratorTestingPanel() {
        logger.info("Destroy Generator Testing Panel ");
        stopSimulation();
        removeAll();
        initAssistantDataModel.removeListener(initAssistantDataModelListener);
        if(propertyTestingPanel != null) {
            propertyTestingPanel.destroyContents();
        }
        propertyTestingPanel = null;
        programTester = null;
        startTDGButton.setText(START_GENERATING_OPERATION);
        startRDGButton.setText(FIND_A_RULE_OPERATION);
        pauseResumeTDGButton.setText(PAUSE_GENERATING_OPERATION);
    }

    //
    //   P r e p a r a t i o n   f o r   t e s t i n g
    //

    private ProgramTester preparePropertyForTesting() {

//        MclnStatement mclnStatement = initAssistantDataModel.getMclnProperty();

        // updating Input Generating Program

        boolean propertyHasProgram = initAssistantDataModel.propertyHasProgram();
        if (!propertyHasProgram) {
            // repainting Property view
//            mclnGraphViewStatement.repaintPropertyUponInitialization();
            return null;
        }

        java.util.List<ProgramStepData> programStepData = initAssistantDataModel.getSelectedProgramSteps();
        if (programStepData == null) {
            initAssistantDataModel.showInfoMessage("Program steps not created");
            return null;
        }

        boolean programHasPhase = initAssistantDataModel.getSelectedProgramHasPhase();
//        MclnProject.updateInputGeneratingProgram(mclnStatement, ProgramHasPhase,
//                initAssistantDataModel.isSelectedProgramTimeDrivenProgram(), programStepData);
        MclnStatementState initialMclnStatementState = initAssistantDataModel.getInitialMclnStatementState();
        programTester = ProgramTester.createProgramTester(initialMclnStatementState.getMclnState(), programHasPhase,
                initAssistantDataModel.isSelectedProgramTimeDrivenProgram(), programStepData);

        return programTester;
    }

    /**
     * @param instructionLabel
     */
    private final void initGeneratorTestContent(JPanel tickDisplayPanel, JLabel instructionLabel) {

        removeAll();

//        tickDisplayPanel.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(tickDisplayPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(true);
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        propertyTestingPanel = new PropertyTestingPanel(this, initAssistantDataModel);
        centerPanel.add(propertyTestingPanel, BorderLayout.CENTER);

        simulationStatusLabel.setOpaque(false);
        simulationStatusLabel.setPreferredSize(TICKS_PANEL_SIZE);
        simulationStatusLabel.setForeground(Color.LIGHT_GRAY);
        centerPanel.add(simulationStatusLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(new EmptyBorder(5, 10, 10, 10));

        buttonPanel.add(instructionLabel, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

        currentStateInterpretationLabel.setBorder(new MatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
        currentStateInterpretationLabel.setOpaque(true);
        currentStateInterpretationLabel.setBackground(Color.WHITE);
        buttonPanel.add(currentStateInterpretationLabel, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

        if (initAssistantDataModel.isSelectedProgramTimeDrivenProgram()) {

            buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

            buttonPanel.add(startTDGButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 1), 0, 0));

            buttonPanel.add(pauseResumeTDGButton, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 1, 0, 1), 0, 0));

            buttonPanel.add(oneTickTDGButton, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 1, 0, 1), 0, 0));

            buttonPanel.add(resetTDGButton, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 0, 0));

            buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(5, 2, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        } else {
            buttonPanel.add(startRDGButton, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //
    //   S i m u l a t i o n
    //

    private void startSimulation() {
        simulationIsRunning = true;
        simulationTickTimer.start();
    }

    private void stopSimulation() {
        simulationIsRunning = false;
        if (simulationTickTimer.isRunning()) {
            simulationTickTimer.stop();
        }
//        startTDGButton.setText(START_GENERATING_OPERATION);
//        startTDGButton.setForeground(START_GENERATING_COLOR);
        startRDGButton.setText(FIND_A_RULE_OPERATION);
        startRDGButton.setForeground(START_GENERATING_COLOR);
    }

    private enum SimulationStatus {Initial, Started, Paused, TickByTick}

    private SimulationStatus currentSimulationStatus = SimulationStatus.Initial;

    private void doSimulationStatusTransition(String operation) {
        if (operation.equals(START_GENERATING_OPERATION)) {

            startTDGButton.setText(STOP_GENERATING_OPERATION);

            startTDGButton.setEnabled(true);
            startTDGButton.setBackground(STOP_GENERATING_COLOR);

            pauseResumeTDGButton.setEnabled(true);
            pauseResumeTDGButton.setBackground(PAUSE_RESUME_BUTTON_COLOR);

            oneTickTDGButton.setEnabled(false);
            oneTickTDGButton.setBackground(DISABLED_BUTTON_COLOR);

            resetTDGButton.setEnabled(true);
            resetTDGButton.setBackground(RESET_BUTTON_COLOR);

            simulationStatusLabel.setText(" Generator started");

            startSimulation();

        } else if (operation.equals(STOP_GENERATING_OPERATION)) {
            stopSimulation();
            startTDGButton.setEnabled(true);
            startTDGButton.setBackground(START_GENERATING_COLOR);
            startTDGButton.setText(START_GENERATING_OPERATION);

            pauseResumeTDGButton.setEnabled(false);
            pauseResumeTDGButton.setBackground(DISABLED_BUTTON_COLOR);

            oneTickTDGButton.setEnabled(true);
            oneTickTDGButton.setBackground(ONE_TICK_BUTTON_COLOR);

            resetTDGButton.setEnabled(true);
            resetTDGButton.setBackground(RESET_BUTTON_COLOR);

            simulationStatusLabel.setText(" Generator stopped");

        } else if (operation.equals(PAUSE_GENERATING_OPERATION)) {
            simulationIsRunning = false;
            pauseResumeTDGButton.setText(RESUME_GENERATING_OPERATION);

            startTDGButton.setEnabled(false);
            startTDGButton.setBackground(DISABLED_BUTTON_COLOR);

            pauseResumeTDGButton.setEnabled(true);
            pauseResumeTDGButton.setBackground(PAUSE_RESUME_BUTTON_COLOR);

            oneTickTDGButton.setEnabled(true);
            oneTickTDGButton.setBackground(ONE_TICK_BUTTON_COLOR);

            resetTDGButton.setEnabled(true);
            resetTDGButton.setBackground(RESET_BUTTON_COLOR);

            simulationStatusLabel.setText(" Generator paused");

        } else if (operation.equals(RESUME_GENERATING_OPERATION)) {

            startTDGButton.setEnabled(true);
            startTDGButton.setBackground(STOP_GENERATING_COLOR);

            pauseResumeTDGButton.setText(PAUSE_GENERATING_OPERATION);
            pauseResumeTDGButton.setEnabled(true);
            pauseResumeTDGButton.setBackground(PAUSE_RESUME_BUTTON_COLOR);

            oneTickTDGButton.setEnabled(false);
            oneTickTDGButton.setBackground(DISABLED_BUTTON_COLOR);

            resetTDGButton.setEnabled(true);
            resetTDGButton.setBackground(RESET_BUTTON_COLOR);

            simulationStatusLabel.setText(" Generator resumed");

            simulationIsRunning = true;

        } else if (operation.equals(ONE_TICK_OPERATION)) {

//            InputGeneratorState inputGeneratorState = programTester.initSimulationTest();
            simulateTimeDrivenGenerator();
//            updateTimeDrivenTicksDisplayPanel(inputGeneratorState);

            if (!skipNextTick) {
                simulationStatusLabel.setText(" Tick executed");
            } else {
                simulationStatusLabel.setText(" Step executed");
            }

            resetTDGButton.setEnabled(true);
            resetTDGButton.setBackground(RESET_BUTTON_COLOR);

        } else if (operation.equals(RESET_GENERATING_OPERATION)) {

            stopSimulation();
            prepareSimulation();
            InputGeneratorState inputGeneratorState = programTester.initSimulationTest();

            updateTimeDrivenTicksDisplayPanel(programTester.theProgramHasPhase(), inputGeneratorState.getGeneratorStep(),
                    inputGeneratorState.getStateCountDown());

            startTDGButton.setEnabled(true);
            startTDGButton.setBackground(START_GENERATING_COLOR);
            startTDGButton.setText(START_GENERATING_OPERATION);

            pauseResumeTDGButton.setEnabled(false);
            pauseResumeTDGButton.setBackground(DISABLED_BUTTON_COLOR);
            pauseResumeTDGButton.setText(PAUSE_GENERATING_OPERATION);

            oneTickTDGButton.setEnabled(true);
            oneTickTDGButton.setBackground(ONE_TICK_BUTTON_COLOR);

//            resetTDGButton.setEnabled(true);
//            resetTDGButton.setBackground(RESET_BUTTON_COLOR);

            resetTDGButton.setEnabled(false);
            resetTDGButton.setBackground(DISABLED_BUTTON_COLOR);

            simulationStatusLabel.setText(" Generator reinitialized");

        }
    }

    //
    //   Testing Time Driven Generator
    //

    private boolean skipNextTick;
    private InputGeneratorState savedInputGeneratorState;

    private void simulateTimeDrivenGenerator() {

        System.out.println("simulate Time Driven Generator\n");
        if (skipNextTick) {
            updateTimeDrivenTicksDisplayPanel(programTester.theProgramHasPhase(),
                    savedInputGeneratorState.getNextGeneratorStep(),
                    savedInputGeneratorState.getNextStateCountDown());
            skipNextTick = false;
            savedInputGeneratorState = null;
            return;
        }
        InputGeneratorState inputGeneratorState = programTester.testInputSimulatingProgramStep();
//        InputGeneratorState inputGeneratorState = MclnModelPublicInterface.testOneSimulationStepExecution(mclnStatement);
        if (inputGeneratorState == null) {
            return;
        }

        System.out.println("\nProperty Testing Panel: InputGeneratorState = " + inputGeneratorState.toString());

        updateTimeDrivenTicksDisplayPanel(programTester.theProgramHasPhase(), inputGeneratorState.getGeneratorStep(),
                inputGeneratorState.getStateCountDown());

        if (!inputGeneratorState.isStepExecuted()) {
            simulationStatusLabel.setText(" Tick executed");
            return;
        }

        MclnState mclnState = inputGeneratorState.getNewMclnState();
        Color stateColor = new Color(mclnState.getRGB());
        propertyTestingPanel.setPropertyState(stateColor);
        updateCurrentStateInterpretationDisplay(mclnState.getStateID());
        skipNextTick = true;
        this.savedInputGeneratorState = inputGeneratorState;
        simulationStatusLabel.setText(" Step executed");

    }

    /**
     * @param generatorStep
     * @param stateCountDown
     */
    void updateTimeDrivenTicksDisplayPanel(boolean phase, int generatorStep, int stateCountDown) {
        String stepDisplay = "";
        int currentStep = phase ? generatorStep : generatorStep + 1;
        if(currentStep == 0){
            stepsLabel.setText("Phase:");
        }else{
            stepsLabel.setText("Step:");
              stepDisplay = String.format("%2d", currentStep);
        }

        stepsValue.setText(stepDisplay);
        String ticksDisplay = String.format("%2d", stateCountDown);
        ticksValue.setText(ticksDisplay);
    }

    //
    //   Testing Rule Driven Generator
    //

    private void simulateRuleDrivenGenerator() {

        System.out.println("simulate Rule Driven Generator\n");

        simulationStatusLabel.setText("");

        InputGeneratorState inputGeneratorState = programTester.testInputSimulatingProgramStep();
        if (inputGeneratorState == null ||
                inputGeneratorState.getGeneratorStep() < 0 || inputGeneratorState.getStateCountDown() < 0) {
            // rule not found
            updateRuleDrivenTicksDisplayPanel(null);
            startRDGButton.setText(FIND_A_RULE_OPERATION);
            return;
        }


        updateRuleDrivenTicksDisplayPanel(inputGeneratorState);

        if (!inputGeneratorState.isStepExecuted()) {
            startRDGButton.setText(EXECUTE_THE_RULE_TICK_OPERATION);
            simulationStatusLabel.setText(" Rule found");
            return;
        }
        MclnState mclnState = inputGeneratorState.getNewMclnState();
        Color itemColor = new Color(mclnState.getRGB());
        propertyTestingPanel.setPropertyState(itemColor);
        startRDGButton.setText(FIND_A_RULE_OPERATION);
    }

    private void updateRuleDrivenTicksDisplayPanel(InputGeneratorState inputGeneratorState) {

        if (inputGeneratorState == null ||
                inputGeneratorState.getGeneratorStep() < 0 || inputGeneratorState.getStateCountDown() < 0) {
            ruleLabel.setText(NO_RULE_TEXT);
            return;
        }

        int ruleIndex = inputGeneratorState.getGeneratorStep();
        int tickCountDown = inputGeneratorState.getStateCountDown();

        if (tickCountDown == 0) {
            ruleLabel.setText(THE_RULE_EXECUTED_TEXT);
            return;
        }

        String ruleNumber = String.format("%03d", ruleIndex + 1);
        String ticksLeft = String.format("%02d", tickCountDown);


//        MclnState generatedState = inputGeneratorState.getNewMclnState();
//        String test = "Executing rule " + ruleNumber + " in " + ticksLeft + " ticks";

        String executingRuleText = new StringBuilder().
                append("<html>").
                append("<div style=\"font-weight: bold;\"").
                append("<font size=\"11pt\" color=\"#777777\">Executing rule</font>").append("&nbsp;&nbsp;").
                append("<font size=\"12pt\" color=\"#0000DD\"><b>").append(ruleNumber).append("</b></font>&nbsp;&nbsp;").
                append("<font size=\"11pt\" color=\"#777777\">in</font>").append("&nbsp;&nbsp;").
                append("<font size=\"12pt\" color=\"#0000DD\"><b>").append(ticksLeft).append("</b></font>&nbsp;&nbsp;").
                append("<font size=\"11pt\" color=\"#777777\">ticks</font>").append("</<div></html>").toString();

        ruleLabel.setText(executingRuleText);

//        if (inputGeneratorState.isStepExecuted()) {
//            ruleLabel.setText(NO_RULE_TEXT);
//            updateCurrentStateInterpretationDisplay(generatedState.getStateID());
//            return;
//        }
    }

    /**
     * @param mclnStateID
     */
    private void updateCurrentStateInterpretationDisplay(Integer mclnStateID) {
        MclnStatementState mclnStatementState = initAssistantDataModel.getMclnStatementStateByMclnStateID(mclnStateID);
        String stateInterpretation = mclnStatementState.getStateInterpretation();
        stateInterpretation = stateInterpretation != null ? stateInterpretation : "";
        stateInterpretation = stateInterpretation.replace("$", "");
        stateInterpretation = stateInterpretation.replace(".", "").concat(".");

        String fullInterpretation = CURRENT_STATE_INTERPRETATION_TEMPLATE;
        fullInterpretation = fullInterpretation.replace("1$", componentName);
        fullInterpretation = fullInterpretation.replace("2$", propertyName);
        fullInterpretation = fullInterpretation.replace("3$", stateInterpretation);

        currentStateInterpretationLabel.setText(fullInterpretation);
    }

    /**
     * @param selectedStateID
     */
    void updateMclnStatementStateFromPopup(int selectedStateID) {
        List<MclnStatementState> allowedStatesList = initAssistantDataModel.getCurrentAllowedStatesList();
        for (MclnStatementState mclnStatementState : allowedStatesList) {
            int allowedStateID = mclnStatementState.getStateID();
            if (allowedStateID != selectedStateID) {
                continue;
            }
            MclnState mclnState = mclnStatementState.getMclnState();
            programTester.setTestingCurrentMclnState(mclnState);

            Color itemColor = new Color(mclnState.getRGB());
            propertyTestingPanel.setPropertyState(itemColor);
        }
    }

    /**
     * c l a s s :   T i m e   D r i v e n   G e n e r a t o r   S t a t e   D i s p l a y   P a n e l
     */
    private class TimeDrivenGeneratorStateDisplayPanel extends JPanel {

        TimeDrivenGeneratorStateDisplayPanel() {
            super(new GridBagLayout());
            initContext();
        }

        private void initContext() {

            setPreferredSize(TICKS_PANEL_SIZE);
            setBackground(Color.WHITE);
            setOpaque(true);

            stepsLabel.setForeground(TEXT_COLOR);
//            stepsLabel.setOpaque(true);
//            stepsLabel.setBackground(Color.RED);

            stepsValue.setForeground(NUMBER_COLOR);
            stepsValue.setFont(StandardFonts.FONT_HELVETICA_BOLD_12);
            stepsValue.setPreferredSize(TICKS_VALUE_SIZE);
//            stepsValue.setOpaque(true);
//            stepsValue.setBackground(Color.GREEN);

            ticksLabel.setForeground(TEXT_COLOR);
            ticksValue.setForeground(NUMBER_COLOR);
            ticksValue.setFont(StandardFonts.FONT_HELVETICA_BOLD_12);
            ticksValue.setPreferredSize(TICKS_VALUE_SIZE);

            add(stepsLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 0, 0, 5), 0, 0));
            add(stepsValue, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));

            add(ticksLabel, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 0, 0, 5), 0, 0));
            add(ticksValue, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));
        }
    }

    /**
     * c l a s s :   R u l e   D r i v e n   G e n e r a t o r   S t a t e   D i s p l a y   P a n e l
     */
    private class RuleDrivenGeneratorStateDisplayPanel extends JPanel {

        RuleDrivenGeneratorStateDisplayPanel() {
            super(new GridBagLayout());
            initContext();
        }

        private void initContext() {

            setPreferredSize(TICKS_PANEL_SIZE);
            setBackground(Color.WHITE);
            setOpaque(true);

            ruleLabel.setForeground(TEXT_COLOR);

            add(ruleLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
    }
}
