package mcln.model;

import mcln.palette.MclnState;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 12/13/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeProgramStep extends ProgramStep {

    public TimeProgramStep(int ticks, MclnState state) {
        this(false, ticks, state);
    }

    public TimeProgramStep(boolean phase, int ticks, MclnState state) {
        super(phase, ticks, state);
    }

    @Override
    public Object[] toIntArray() {
        MclnState generatedState = getGeneratedState();
        Object[] intArray = {null, getTicksAsItIs(), generatedState};
        return intArray;
    }

    public ProgramStepData toProgramStepData() {
        return new ProgramStepData(isPhase(), getTicks(), getGeneratedState());
    }

    @Override
    StringBuilder toXML(StringBuilder stringBuilder) {

        stringBuilder.append("<").append(MCLN_PROGRAM_STEP_TAG);
        if (isPhase()) {
            stringBuilder.append(" ").append(MCLN_PROGRAM_ATTRIBUTE_IS_PHASE_KEY).append("=\"").append(isPhase()).append("\"");
        }
        stringBuilder.append(">");

        ticksToXml(stringBuilder);

//        stringBuilder.append("<").append(MCLN_TICKS_TAG).append(">");
//        stringBuilder.append(getTicks());
//        stringBuilder.append("</").append(MCLN_TICKS_TAG).append(">");

        getGeneratedState().toXml(stringBuilder, MCLN_GENERATED_STATE_TAG);

        stringBuilder.append("</").append(MCLN_PROGRAM_STEP_TAG).append(">");
        return stringBuilder;
    }
}
