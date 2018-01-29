package mcln.model;

import mcln.palette.MclnState;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 12/13/13
 * Time: 8:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class StateDrivenProgram extends InputSimulatingProgram {

    private MclnState expectedState;

    public StateDrivenProgram(List<ProgramStep> programSteps) {
        super(programSteps);
    }

    //
    //  The program initialization
    //

    @Override
    protected void initProgram() {
        int index = findProgramRuleForCurrentState();
        if (index < 0) {
            currentProgramStep = null;
            return;
        }
        setRuleToBeExecuted(index);
    }

    //
    // executing one tick
    //

    @Override
    protected InputGeneratorState testStep() {
        return doSimulatingStepTick();
    }

    @Override
    protected boolean executeStep() {
        return doSimulatingStepTick().isStepExecuted();
    }

    private InputGeneratorState doSimulatingStepTick() {
        // init current program rule for Property current state
        // if current program step is not yet initialized
        if (currentProgramStep == null) {
            int index = findProgramRuleForCurrentState();
            if (index < 0) {
                // rule not found - do nothing
                inputGeneratorState.setCurrentState(false,false, currentStepIndex, countDown, null);
                return inputGeneratorState;
            }

            setRuleToBeExecuted(index);
            inputGeneratorState.setCurrentState(false, false, currentStepIndex, countDown, expectedState);
            return inputGeneratorState;
        }

        // executing one tick
        MclnState generatedMclnState = currentProgramStep.getGeneratedState();
        if (countDown != 0) {
            countDown--;
            inputGeneratorState.setCurrentState(false, countDown == 0, currentStepIndex, countDown, generatedMclnState);
            if (countDown == 0) {
                // count down ended
                // reset Property current state
                mclnStatement.setCurrentMclnState(generatedMclnState);
                // clear the current rule
                // this will trigger search for the next rule
                clearCurrentStep();
            }
        }
        return inputGeneratorState;
    }

    private int findProgramRuleForCurrentState() {
        MclnState currentMclnState = mclnStatement.getCurrentMclnState();
        int size = programSteps.size();
        for (int i = 0; i < size; i++) {
            ProgramStep programStep = programSteps.get(i);
            if (programStep.getExpectedState() == currentMclnState) {
                return i;
            }
        }
        return -1;
    }

    private void clearCurrentStep() {
        currentProgramStep = null;
        currentStepIndex = -1;
        setCountDown(-1);
    }

    private void setRuleToBeExecuted(int stepIndex) {
        if (stepIndex >= programSteps.size()) {
            stepIndex = 0;
        }
        currentStepIndex = stepIndex;
        currentProgramStep = programSteps.get(currentStepIndex);
        expectedState = currentProgramStep.getExpectedState();
        int ticks = currentProgramStep.getTicks();
        setCountDown(ticks);
    }
}
