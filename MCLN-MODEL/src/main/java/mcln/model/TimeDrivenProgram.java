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
class TimeDrivenProgram extends InputSimulatingProgram {


    public TimeDrivenProgram(List<ProgramStep> programSteps) {
        super(programSteps);
    }

    //
    //  The program initialization
    //

    @Override
    protected void initProgram() {
        // with or without phase program is started from step 0
        setStepToBeExecuted(0);
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
        MclnState mclnState = currentProgramStep.getGeneratedState();
        if (countDown != 0) {
            countDown--;
            inputGeneratorState.setCurrentState(currentProgramStep != null ? currentProgramStep.isPhase() : false,
                    countDown == 0, currentStepIndex, countDown, mclnState);
            if (countDown == 0) {
                // count down ended
                // reset Property current state
                MclnState generatedMclnState = currentProgramStep.getGeneratedState();
                mclnStatement.setCurrentMclnState(generatedMclnState);
                // advance state
                setStepToBeExecuted(++currentStepIndex);
                inputGeneratorState.setNextState(currentStepIndex, countDown);
            }
        }
        return inputGeneratorState;
    }

    private void setStepToBeExecuted(int stepIndex) {
        if (stepIndex >= programSteps.size()) {
            stepIndex = hasPhase()? 1 : 0;
        }
        currentStepIndex = stepIndex;
        currentProgramStep = programSteps.get(currentStepIndex);
        int ticks = currentProgramStep.getTicks();
        setCountDown(ticks);
    }
}
