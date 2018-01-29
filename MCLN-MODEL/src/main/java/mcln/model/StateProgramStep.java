package mcln.model;

import mcln.palette.MclnState;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 12/13/13
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class StateProgramStep extends ProgramStep {

    public StateProgramStep(MclnState expectedState, int ticks, MclnState generatedState) {
        super(expectedState, ticks, generatedState);
    }

    @Override
    public Object[] toIntArray() {
        MclnState expectedState = getExpectedState();
        MclnState generatedState = getGeneratedState();
        Object[] intArray = {expectedState, getTicksAsItIs(), generatedState};
        return intArray;
    }

    public ProgramStepData toProgramStepData() {
        return new ProgramStepData(isPhase(), getExpectedState(), getTicks(), getGeneratedState());
    }

    @Override
    StringBuilder toXML(StringBuilder stringBuilder) {
//        stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(MCLN_PROGRAM_STEP_TAG).append(">");

        getExpectedState().toXml(stringBuilder, MCLN_EXPECTED_STATE_TAG);

        ticksToXml(stringBuilder);

//        stringBuilder.append("<").append(MCLN_TICKS_TAG).append(">");
//        stringBuilder.append(getTicks());
//        stringBuilder.append("</").append(MCLN_TICKS_TAG).append(">");

        getGeneratedState().toXml(stringBuilder, MCLN_GENERATED_STATE_TAG);

        stringBuilder.append("</").append(MCLN_PROGRAM_STEP_TAG).append(">");
        return stringBuilder;
    }
}
