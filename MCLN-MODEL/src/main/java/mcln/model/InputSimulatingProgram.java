package mcln.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 12/13/13
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class InputSimulatingProgram {

    static final String MCLN_INPUT_SIMULATING_PROGRAM_TAG = "Input-Simulating-Program";

    protected MclnStatementStateHolder mclnStatement;

    private final boolean hasPhase;
    protected int initialCountdown;
    protected int countDown;

    final List<ProgramStep> programSteps;
    ProgramStep currentProgramStep;
    protected int currentStepIndex;

    // InputGeneratorState is used by Initialization Assistant
    protected final InputGeneratorState inputGeneratorState = new InputGeneratorState();

    //
    //   C r e a t i o n
    //

    InputSimulatingProgram(List<ProgramStep> programSteps) {
        this.programSteps = programSteps;
        boolean tmpHasPhase = false;
        for (ProgramStep programStep : programSteps) {
            try {
//                System.out.println("InputSimulatingProgram  Program Step " + programStep.toString());
                tmpHasPhase |= programStep.isPhase();
            } catch (Exception e) {
                System.out.println("InputSimulatingProgram  Program Step ");
            }
        }
        hasPhase = tmpHasPhase;
    }

    /**
     * This is the program API
     *
     * @param mclnStatementStateHolder
     */
    protected void setMclnStatement(MclnStatementStateHolder mclnStatementStateHolder) {
        this.mclnStatement = mclnStatementStateHolder;
    }

    /**
     * This is the program API
     *
     * @return
     */
    public boolean hasPhase() {
        return hasPhase;
    }

    /**
     * This is the program API
     * Used by Initialization Assistant
     *
     * @return
     */
    public List<ProgramStepData> getProgramStepsData() {
        List<ProgramStepData> programStepDataList = new ArrayList();
        for (ProgramStep programStep : programSteps) {
            programStepDataList.add(programStep.toProgramStepData());
        }
        return programStepDataList;
    }

    //
    //  The program initialization and execution
    //

    /**
     * This is the program API
     */
    InputGeneratorState initializeSimulation() {
        initProgram();
        inputGeneratorState.setCurrentState(currentProgramStep != null ? currentProgramStep.isPhase() : false, false,
                currentStepIndex, countDown, currentProgramStep != null ? currentProgramStep.getGeneratedState() : null);
        return inputGeneratorState;
    }

    /**
     * This is the program API
     */
    void resetExecution() {
        initializeSimulation();
    }

    protected abstract void initProgram();

    //
    // executing one tick
    //

    /**
     * This is the program API
     */
    protected abstract InputGeneratorState testStep();

    /**
     * This is the program API
     */
    protected abstract boolean executeStep();

    /**
     * setting new step upon current step executed
     *
     * @param countDown
     */
    protected void setCountDown(int countDown) {
        initialCountdown = countDown;
        this.countDown = countDown;
    }

    //
    //   Converting to external representation
    //
    @Override
    public String toString() {
        return toXML(new StringBuilder()).toString();
    }

    /**
     * @param stringBuilder
     * @return
     */
    StringBuilder toXML(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_INPUT_SIMULATING_PROGRAM_TAG).append(" ");
        stringBuilder.append("name=\"").append(getClass().getSimpleName()).append("\">");

        for (ProgramStep programStep : programSteps) {
            programStep.toXML(stringBuilder);
        }
        stringBuilder.append("</").append(MCLN_INPUT_SIMULATING_PROGRAM_TAG).append(">");
        return stringBuilder;
    }
}
