package mcln.model;

import mcln.palette.MclnState;

/**
 * Created by Admin on 6/14/2016.
 */
public class ProgramStepData implements Cloneable {

    private boolean timeDrivenProgramData;
    private boolean phase;

    private MclnState conditionState;

    private int ticks;

    private double randomNoiseRange;

    private double randomNoisePercent;

    private MclnState generatedState;

    public ProgramStepData() {
        this.timeDrivenProgramData = false;
        this.conditionState = null;
        this.ticks = -1;
        this.generatedState = null;
    }

    public ProgramStepData(boolean phase) {
        this.timeDrivenProgramData = true;
        this.phase = phase;
        this.conditionState = null;
        this.ticks = -1;
        this.generatedState = null;
    }

    public ProgramStepData(boolean phase, int ticksCounter, MclnState generatedState) {
        this.timeDrivenProgramData = true;
        this.phase = phase;
        this.ticks = ticksCounter;
        this.generatedState = generatedState;
    }

    ProgramStepData(boolean phase, MclnState conditionState, int ticksCounter, MclnState generatedState) {
        this.phase = phase;
        this.conditionState = conditionState;
        this.ticks = ticksCounter;
        this.generatedState = generatedState;
    }

    @Override
    public ProgramStepData clone() {
        try {
            return (ProgramStepData) super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return null;
    }

//    public ProgramStepData(Object[] step) {
//        this.conditionState = (MclnState) step[0];
//        this.ticks = (Integer) step[1];
//        this.generatedState = (MclnState) step[2];
//    }

    public ProgramStep toProgramStep() {
        ProgramStep programStep;
        if (timeDrivenProgramData) {
            programStep = new TimeProgramStep(phase, ticks, generatedState);
        } else {
            programStep = new StateProgramStep(conditionState, ticks, generatedState);
        }
        return programStep;
    }

    public boolean isPhase() {
        return phase;
    }

    public MclnState getConditionState() {
        return conditionState;
    }

    public void setConditionState(MclnState condition) {
        this.conditionState = condition;
    }

    public int getTicksCounter() {
        return ticks;
    }

    public void setTicks(String strTicks) {
        this.ticks = stringTicksToInt(strTicks);
    }

    public MclnState getGeneratedState() {
        return generatedState;
    }

    public void setGeneratedState(MclnState state) {
        this.generatedState = state;
    }

    private int stringTicksToInt(String strTicks) {
        int ticks = -1;
        try {
            ticks = Integer.valueOf(strTicks);
        } finally {
            return ticks;
        }
    }

    public boolean isEqual(ProgramStepData otherProgramStepData) {
        boolean equals;
        if (timeDrivenProgramData != otherProgramStepData.timeDrivenProgramData) {
            return false;
        }
        if (timeDrivenProgramData) {
            equals = (phase == otherProgramStepData.phase) && (ticks == otherProgramStepData.ticks) &&
                    (generatedState == otherProgramStepData.generatedState);
        } else {
            equals = (conditionState == otherProgramStepData.conditionState) && (ticks == otherProgramStepData.ticks) &&
                    (generatedState == otherProgramStepData.generatedState);
        }
        return equals;
    }

}

