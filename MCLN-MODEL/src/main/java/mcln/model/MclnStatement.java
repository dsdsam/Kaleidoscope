package mcln.model;

import mcln.palette.BasicColorPalette;
import mcln.palette.MclnState;
import mcln.simulator.SimulatedStateChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 8/29/13
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnStatement extends MclnNode<MclnStatement, MclnCondition> {

    public static final String MCLN_STATEMENT_XML_TAG = "Mcln-Statement";
    public static final String MCLN_PALETTE_ATTRIBUTE_NAME = "mcln-states-palette-name";
    public static final String MCLN_GENERATOR_ATTRIBUTE_NAME = "has-input-generator";
    //  public static final String MCLN_STEP_PHASE_ATTRIBUTE_NAME = "phase";
    public static final String MCLN_STATEMENT_UID_TAG = "Statement-UID";
    public static final String MCLN_STATEMENT_INITIAL_STATE_TAG = "Initial-State";
    public static final String MCLN_STATEMENT_SUBJECT_TAG = "Subject";
    public static final String MCLN_STATEMENT_PROPERTY_NAME_TAG = "Property-Name";
    public static final String MCLN_STATEMENT_SIMULATION_LOG_TAG = "Simulation-Log";

    private static AvailableMclnStatementStates createDefaultAvailableStates() {

        BasicColorPalette mclnStatesPalette = BasicColorPalette.getInstance();
        MclnState defaultState00 = mclnStatesPalette.getMclnState(BasicColorPalette.CREATION_STATE_ID);
        MclnState defaultState01 = mclnStatesPalette.getMclnState(BasicColorPalette.RED_STATE_ID);
        MclnState defaultState02 = mclnStatesPalette.getMclnState(BasicColorPalette.GREEN_STATE_ID);
        MclnState defaultState03 = mclnStatesPalette.getMclnState(BasicColorPalette.BLUE_STATE_ID);
        MclnState defaultState04 = mclnStatesPalette.getMclnState(BasicColorPalette.CYAN_STATE_ID);
        MclnState defaultState05 = mclnStatesPalette.getMclnState(BasicColorPalette.MAGENTA_STATE_ID);
        MclnState defaultState06 = mclnStatesPalette.getMclnState(BasicColorPalette.YELLOW_STATE_ID);

        Object[][] defaultAvailableStatesData = {
                {defaultState00, "$ is in Default State"},
                {defaultState01, "$ is in Red State"},
                {defaultState02, "$ is in Green State"},
                {defaultState03, "$ is in Blue State"},
                {defaultState04, "$ is in Cyan State"},
                {defaultState05, "$ is in Magenta State"},
                {defaultState06, "$ is in Yellow State"}};

        AvailableMclnStatementStates defaultAvailableStates =
                AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                        defaultAvailableStatesData, defaultState00);

        return defaultAvailableStates;
    }

    //
    //    M c l n   S t a t e m e n t    I n s t a n c e
    //

    private MclnState calculatedSuggestedState = MclnState.EMPTY_STATE;
    private static final String DEFAULT_SUBJECT_TEXT = "Property Node";

    private AvailableMclnStatementStates availableMclnStatementStates;

    private String subject = DEFAULT_SUBJECT_TEXT;
    private boolean activator;
    private String propertyName = "";

    private InputSimulatingProgram inputSimulatingProgram;

    private MclnStatementState initialMclnStatementState;
    private MclnStatementState currentMclnStatementState;

    private boolean stateShouldBeLogged;

    private final MclnStatementStateHolder mclnStatementStateHolder;


    /**
     * Called from Mcln Project when Mcln Statement is created programmatically for Demo
     *
     * @param UID
     * @param cSysLocation
     * @param initialMclnState
     */
    MclnStatement(String UID, String subject, double[] cSysLocation, MclnState initialMclnState) {
        this(UID, subject, null, cSysLocation, initialMclnState, null);
    }

    /**
     * Called from Mcln Project when Mcln Statement is created programmatically for Demo
     *
     * @param UID
     * @param subject
     * @param availableMclnStatementStates
     * @param cSysLocation
     * @param initialMclnState
     */
    MclnStatement(String UID, String subject, AvailableMclnStatementStates availableMclnStatementStates,
                  double[] cSysLocation, MclnState initialMclnState) {
        this(UID, subject, availableMclnStatementStates, cSysLocation, initialMclnState, null);
    }

    /**
     * Called from Mcln Project to create statement
     * This should call Main Constructor
     *
     * @param uid
     * @param cSysLocation
     * @param initialState
     * @param inputSimulatingProgram
     */
    MclnStatement(String uid, String subject, AvailableMclnStatementStates availableMclnStatementStates,
                  double[] cSysLocation, MclnState initialState, InputSimulatingProgram inputSimulatingProgram) {
        super(uid, cSysLocation);
        mclnStatementStateHolder = new MclnStatementStateHolder(this);
        setSubject(subject);

        if (availableMclnStatementStates != null) {
            this.availableMclnStatementStates = availableMclnStatementStates;
            this.initialMclnStatementState = availableMclnStatementStates.getMclnStatementState(initialState.getStateID());
        } else {
            this.availableMclnStatementStates = MclnStatement.createDefaultAvailableStates();
            this.initialMclnStatementState = this.availableMclnStatementStates.getInitialMclnStatementState();
        }
        this.currentMclnStatementState = initialMclnStatementState;

        setInitialMclnState(initialState);
        setCurrentMclnState(initialState);

        this.inputSimulatingProgram = inputSimulatingProgram;
        if (inputSimulatingProgram != null) {
            inputSimulatingProgram.setMclnStatement(mclnStatementStateHolder);
        }
    }

    /**
     * This is the main Mcln Statement Constructor
     * Called from MclnRetriever
     *
     * @param uid
     * @param subject
     * @param availableMclnStatementStates
     * @param cSysLocation
     * @param initialMclnStatementState
     * @param inputSimulatingProgram
     */
    MclnStatement(String uid, String subject, String propertyName, boolean hasInputGenerator,
                  AvailableMclnStatementStates availableMclnStatementStates,
                  double[] cSysLocation, MclnStatementState initialMclnStatementState,
                  InputSimulatingProgram inputSimulatingProgram) {
        super(uid, cSysLocation);
        mclnStatementStateHolder = new MclnStatementStateHolder(this);
        setSubject(subject);
        this.propertyName = (propertyName != null && propertyName.trim().length() != 0) ? propertyName : "";

        this.availableMclnStatementStates = availableMclnStatementStates;
        this.initialMclnStatementState = initialMclnStatementState;
        this.currentMclnStatementState = initialMclnStatementState;

        setInitialMclnState(initialMclnStatementState.getMclnState());
        setCurrentMclnState(initialMclnStatementState.getMclnState());

        this.inputSimulatingProgram = inputSimulatingProgram;
        if (inputSimulatingProgram != null) {
            inputSimulatingProgram.setMclnStatement(mclnStatementStateHolder);
        }
    }

    /**
     * The method to save initialized by Initialization Assistant program
     *
     * @param programSteps
     * @param timeDrivenProgram
     */
    void updateInputGeneratingProgram(List<ProgramStep> programSteps, boolean timeDrivenProgram) {

        // program removed
        if (programSteps == null) {
            inputSimulatingProgram = null;
            return;
        }

        // program reset
        InputSimulatingProgram inputSimulatingProgram;
        if (timeDrivenProgram) {
            inputSimulatingProgram = new TimeDrivenProgram(programSteps);
        } else {
            inputSimulatingProgram = new StateDrivenProgram(programSteps);
        }
        this.inputSimulatingProgram = inputSimulatingProgram;
        inputSimulatingProgram.setMclnStatement(mclnStatementStateHolder);
    }

    void removeInputGeneratingProgram() {
        inputSimulatingProgram = null;
    }

    public String getMclnStatesPaletteName() {
        return availableMclnStatementStates.getMclnStatesPaletteName();
    }

    public boolean hasInputGeneratingProgram() {
        return inputSimulatingProgram != null;
    }

    public boolean theProgramHasPhase() {
        return inputSimulatingProgram.hasPhase();
    }

    public boolean isTimeDrivenProgram() {
        return inputSimulatingProgram instanceof TimeDrivenProgram;
    }

    public List<ProgramStepData> getProgramData() {
        return inputSimulatingProgram != null ? inputSimulatingProgram.getProgramStepsData() : new ArrayList();
    }

    @Override
    public double[] getCSysLocation() {
        return super.getCSysPoint();
    }

    @Override
    public void setCSysLocation(double[] cSysLocation) {
        super.setCSysPoint(cSysLocation);
    }

    @Override
    public void removeInputArc(MclnArc mclnArc) {
        super.removeInputArc(mclnArc);
    }

    @Override
    public void removeOutputArc(MclnArc mclnArc) {
        super.removeOutputArc(mclnArc);
    }

    public List<MclnStatementState> getPropertyStatesAsList() {
        return availableMclnStatementStates.getPropertyStatesAsList();
    }

    public AvailableMclnStatementStates getAvailableMclnStatementStates() {
        return availableMclnStatementStates;
    }

    public void setAvailableMclnStatementStates(AvailableMclnStatementStates availableMclnStatementStates) {
        this.availableMclnStatementStates = availableMclnStatementStates;
        this.initialMclnStatementState = availableMclnStatementStates.getInitialMclnStatementState();
        this.currentMclnStatementState = initialMclnStatementState;
    }

    public void setStateShouldBeLogged() {
        stateShouldBeLogged = true;
    }

    public void setSimulationLog(boolean value) {
        stateShouldBeLogged = value;
    }

    public boolean shouldStateBeLogged() {
        return stateShouldBeLogged;
    }

    public MclnStatementState getInitialMclnStatementState() {
        return initialMclnStatementState;
    }

    @Override
    public MclnState getInitialMclnState() {
        if (initialMclnStatementState != null) {
            return initialMclnStatementState.getMclnState();
        }
        return super.getInitialMclnState();
    }

    @Override
    public MclnState getCurrentMclnState() {
        return mclnStatementStateHolder.getCurrentMclnState();
    }

    MclnState getCurrentMclnStateForHolder() {
        if (currentMclnStatementState != null) {
            return currentMclnStatementState.getMclnState();
        }
        return super.getCurrentMclnState();
    }

    @Override
    void setCurrentMclnState(MclnState currentState) {
        mclnStatementStateHolder.setCurrentMclnState(currentState);
    }

    void setCurrentMclnStateByHolder(MclnState currentState) {
        if (availableMclnStatementStates != null) {
            this.currentMclnStatementState = availableMclnStatementStates.getMclnStatementState(currentState.getStateID());
        }
        super.setCurrentMclnState(currentState);

    }

    String getStatementStateAsString() {
        return availableMclnStatementStates != null ? currentMclnStatementState.toView() : "";
    }

    void setCurrentStatementState(MclnStatementState currentStatementState) {
        super.setCurrentMclnState(currentStatementState.getMclnState());
        this.currentMclnStatementState = currentStatementState;
    }

    public void setSubject(String subject) {
        this.subject = (subject != null && subject.trim().length() != 0) ? subject : DEFAULT_SUBJECT_TEXT;
        this.activator = this.subject.contains("Activator");
    }

    public String getSubject() {
        return subject;
    }

    public boolean isActivator() {
        return activator;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = (propertyName != null && propertyName.trim().length() != 0) ? propertyName : "";
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getStateInterpretation() {
        if (currentMclnStatementState != null) {
            String interpretation = currentMclnStatementState.getStateInterpretation();
            return interpretation;
        }
        return "";
    }

    /**
     * @return
     */
    public String getStatementText() {
        if (subject == null || subject.length() == 0) {
            return "There is no subject";
        }
        String statement = subject + "  " + propertyName;
        if (currentMclnStatementState != null) {
            String statementText = null;
            String interpretation = currentMclnStatementState.getStateInterpretation();
            int metaSymbolIndex = interpretation.indexOf("$");
            if (metaSymbolIndex >= 0) {
                statementText = interpretation.replace("$", statement);
            } else {
                statementText = statement + " " + currentMclnStatementState.getStateInterpretation();
            }
            return statementText;
        }
        String response = statement + " is " + getCurrentMclnState().getColorName();
        return response;
    }

    public String getCurrentMclnStatementState() {
        String statementState;
        if (currentMclnStatementState != null) {
            String interpretation = currentMclnStatementState.getStateInterpretation();
            int metaSymbolIndex = interpretation.indexOf("$");
            if (metaSymbolIndex >= 0) {
                statementState = interpretation.replace("$", "");
            } else {
                statementState = currentMclnStatementState.getStateInterpretation();
            }
        } else {
            statementState = " is " + getCurrentMclnState().getColorName();
        }
        return statementState;
    }

    //
    //   S i m u l a t i o n
    //

    InputGeneratorState initializeSimulation() {
        resetCurrentState();
        if (inputSimulatingProgram == null) {
            return null;
        }
        return inputSimulatingProgram.initializeSimulation();
    }

    void setSimulationStarted() {
        if (inputSimulatingProgram == null) {
            return;
        }
//        inputSimulatingProgram.startExecution();
    }

    void setSimulationStopped() {
        if (inputSimulatingProgram == null) {
            return;
        }
//        inputSimulatingProgram.stopExecution();
    }

    /**
     * used for simulating model behavior
     *
     * @return
     */
    String doInputSimulatingProgramStep() {
        if (inputSimulatingProgram == null) {
            return null;
        }
        boolean done = inputSimulatingProgram.executeStep();
        if (done) {
            return getUID() + ":" + getCurrentMclnState().toView();
        }
        return null;
    }

    MclnStatementState setNewCurrentMclnStateByStateAttribute(int stateID) {
        if (availableMclnStatementStates == null) {
            return null;
        }
        MclnStatementState newMclnStatementState = availableMclnStatementStates.getMclnStatementState(stateID);
        this.currentMclnStatementState = newMclnStatementState;
        MclnState newCurrentState = newMclnStatementState.getMclnState();
        super.setCurrentMclnState(newCurrentState);
        return newMclnStatementState;
    }


    public boolean hasOutputArcs() {
        return super.hasOutputArcs();
    }

    public String toString() {
        return getUID() + ", " + subject + ",  " + getCurrentMclnState().toString() + ", currentStatementState = " +
                currentMclnStatementState;
    }

    //
    //   X M L
    //

    public String toXml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(MCLN_STATEMENT_XML_TAG).append(" ").
                append(MCLN_PALETTE_ATTRIBUTE_NAME).append("=\"").
                append(availableMclnStatementStates.getMclnStatesPaletteName());
        if (hasInputGeneratingProgram()) {
            stringBuilder.append("\" ").append(MCLN_GENERATOR_ATTRIBUTE_NAME).append("=\"").append(hasInputGeneratingProgram());
        }
        stringBuilder.append("\">");

        stringBuilder.append("<").append(MCLN_STATEMENT_UID_TAG).append(">");
        stringBuilder.append(getUID());
        stringBuilder.append("</").append(MCLN_STATEMENT_UID_TAG).append(">");

        // UID
        locationToXml(stringBuilder);

        // available states
        availableMclnStatementStates.toXml(stringBuilder);

        // initial state
        stringBuilder.append("<").append(MCLN_STATEMENT_INITIAL_STATE_TAG).append(">");
        initialMclnStatementState.toXml(stringBuilder);
        stringBuilder.append("</").append(MCLN_STATEMENT_INITIAL_STATE_TAG).append(">");

        // subject
        stringBuilder.append("<").append(MCLN_STATEMENT_SUBJECT_TAG).append(">").
                append(subject).append("</").append(MCLN_STATEMENT_SUBJECT_TAG).append(">");

        // property name
        stringBuilder.append("<").append(MCLN_STATEMENT_PROPERTY_NAME_TAG).append(">").
                append(propertyName).append("</").append(MCLN_STATEMENT_PROPERTY_NAME_TAG).append(">");

        // execution log
        stringBuilder.append("<").append(MCLN_STATEMENT_SIMULATION_LOG_TAG).append(">").
                append(stateShouldBeLogged ? "true" : "false").
                append("</").append(MCLN_STATEMENT_SIMULATION_LOG_TAG).append(">");

        // input simulating program
        if (inputSimulatingProgram != null) {
            inputSimulatingProgram.toXML(stringBuilder);
        }

        stringBuilder.append("</").append(MCLN_STATEMENT_XML_TAG).append(">");
        return stringBuilder.toString();
    }

    private StringBuilder locationToXml(StringBuilder stringBuilder) {
        double[] cSysLocation = getCSysLocation();
        stringBuilder.append("<X-Location>").append(MclnStatement.doubleToFormattedString(cSysLocation[0])).
                append("</X-Location>");
        stringBuilder.append("<Y-Location>").append(MclnStatement.doubleToFormattedString(cSysLocation[1])).
                append("</Y-Location>");
        return stringBuilder;
    }

    public String toTooltip() {
        tooltipBuilder.delete(0, tooltipBuilder.length());

        tooltipBuilder.append("<HTML><BODY><font face=\"sanserif\" color=\"black\">")
                .append("<p align=\"center\">")
                .append("~ ~ ~&nbsp;&nbsp;&nbsp;S t a t e m e n t&nbsp;&nbsp;&nbsp;A t t r i b u t e s&nbsp;&nbsp;&nbsp;~ ~ ~<br><br>")
                .append("</p>")

                .append("UID = " + getUID()).append(",&nbsp;&nbsp;Subject:  \"").append(subject).append("\".<br>")
                .append("Current State = " + getCurrentMclnState().toString() + "<br>");

        tooltipBuilder.append("<br>");

        List<MclnArc<MclnCondition, MclnStatement>> inboundArcs = getInboundArcs();
        tooltipBuilder.append("Inp Arcs = " + inboundArcs.size() + "<br>");
        for (MclnArc<MclnCondition, MclnStatement> mclnArc : inboundArcs) {
            MclnCondition mclnCondition = mclnArc.getInpNode();
            tooltipBuilder.append("Inp condition = " + mclnCondition.toString() + "<br>");
        }
        tooltipBuilder.append("<br>");

        List<MclnArc<MclnStatement, MclnCondition>> outboundArcs = getOutboundArcs();
        tooltipBuilder.append("Out Arcs = " + outboundArcs.size() + "<br>");
        for (MclnArc<MclnStatement, MclnCondition> mclnArc : outboundArcs) {
            MclnCondition mclnCondition = mclnArc.getOutNode();
            tooltipBuilder.append("Out condition = " + mclnCondition.toString() + "<br>");
        }

        tooltipBuilder.append("</font></BODY></HTML>");
        return tooltipBuilder.toString();
    }

    public MclnState getCalculatedSuggestedState() {
        return calculatedSuggestedState;
    }

    public void setCalculatedSuggestedState(MclnState calculatedSuggestedState) {
        this.calculatedSuggestedState = calculatedSuggestedState;
    }

    /**
     * @return
     */
    @Override
    public String getOneLineInfoMessage() {
        oneLineMessageBuilder.delete(0, oneLineMessageBuilder.length());

        String trimmedSubject = subject.trim();
        MclnState mclnState = getCurrentMclnState();
        String interpretation = mclnState.getStateInterpretation().replace("$", trimmedSubject);

        if (isRuntimeInitializationUpdatedFlag()) {
            oneLineMessageBuilder.append("* ");
        }
        oneLineMessageBuilder.append("Node: Property [").
                append(" ID: " + getUID() + ", ").
                append(" Subject: \"" + trimmedSubject + "\" ").
                append(" State Interpretation: \"" + interpretation + "\"").
                append(" ]");
        return oneLineMessageBuilder.toString();
    }
}
