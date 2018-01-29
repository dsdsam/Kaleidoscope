package mcln.model;

import mcln.palette.MclnState;

/**
 * Created by Admin on 7/30/2016.
 */
public class InputGeneratorState {

    boolean phase;
    boolean stepExecuted;
    int generatorStep;
    int stateCountDown;
    MclnState expectedState;

    int nextGeneratorStep;
    int nextStateCountDown;

    void setCurrentState(boolean phase, boolean stepExecuted, int generatorStep, int stateCountDown,
                         MclnState expectedState) {
        this.phase = phase;
        this.stepExecuted = stepExecuted;
        this.generatorStep = generatorStep;
        this.stateCountDown = stateCountDown;
        this.expectedState = expectedState;
    }

    void setNextState(int nextGeneratorStep, int nextStateCountDown) {
        this.nextGeneratorStep = nextGeneratorStep;
        this.nextStateCountDown = nextStateCountDown;
    }

    public boolean iaPhase() {
        return phase;
    }

    public boolean isStepExecuted() {
        return stepExecuted;
    }

    public int getStateCountDown() {
        return stateCountDown;
    }

    public int getGeneratorStep() {
        return generatorStep;
    }

    public int getNextGeneratorStep() {
        return nextGeneratorStep;
    }

    public int getNextStateCountDown() {
        return nextStateCountDown;
    }

    public MclnState getNewMclnState() {
        return expectedState;
    }

    public String toString() {
        return (new StringBuilder()).
                append("\nstepExecuted = ").append(stepExecuted).
                append("\nstateCountDown ").append(stateCountDown).
                append("\ngeneratorStep ").append(generatorStep).
                append("\nstepExecuted ").append((expectedState != null) ? expectedState.toString() : "").
                append("").toString();
    }
}