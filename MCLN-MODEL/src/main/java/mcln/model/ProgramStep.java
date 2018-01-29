package mcln.model;

import mcln.palette.MclnState;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 12/13/13
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class ProgramStep {

    public static final String MCLN_PROGRAM_STEP_TAG = "Program-Step";
    public static final String MCLN_PROGRAM_ATTRIBUTE_IS_PHASE_KEY = "is-phase";
    public static final String MCLN_EXPECTED_STATE_TAG = "Expected-State";
    public static final String MCLN_TICKS_TAG = "Ticks";
    public static final String MCLN_GENERATED_STATE_TAG = "Generated-State";

    private boolean phase;
    private final MclnState expectedState;
    private final int ticks;
    private final MclnState generatedState;

    ProgramStep(boolean phase, int ticks, MclnState generatedState) {
        this(phase, null, ticks, generatedState);
    }

    ProgramStep(MclnState expectedState, int ticks, MclnState generatedState) {
        this(false, expectedState, ticks, generatedState);
    }

    private ProgramStep(boolean phase, MclnState expectedState, int ticks, MclnState generatedState) {
        this.phase = phase;
        this.expectedState = expectedState;
        this.ticks = ticks;
        this.generatedState = generatedState;
    }

    public boolean isPhase() {
        return phase;
    }

    abstract public Object[] toIntArray();

    abstract public ProgramStepData toProgramStepData();

    int getTicksAsItIs() {
        return ticks;
    }

    /**
     * @return the conditionState
     */
    MclnState getExpectedState() {
        return expectedState;
    }


    /**
     * @return the ticks
     */
    int getTicks() {
        if (ticks >= 0) {
            return ticks;
        }
        double absTicks = Math.abs(ticks);
//        int randomTicks = (int)(3) + 1 + (int)(Math.random()* absTicks);
        int randomTicks = (int) (2 + (int) (Math.random() * absTicks));
        return randomTicks;
    }

    /**
     * @return the state
     */
    MclnState getGeneratedState() {
        return generatedState;
    }

    public String toString() {
        return toXML(new StringBuilder()).toString();
    }

    StringBuilder ticksToXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_TICKS_TAG).append(">");
        stringBuilder.append(ticks);
        stringBuilder.append("</").append(MCLN_TICKS_TAG).append(">");
        return stringBuilder;
    }

    abstract StringBuilder toXML(StringBuilder stringBuilder);

}
