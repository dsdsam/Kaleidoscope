package mcln.model;

import mcln.palette.MclnState;

import java.util.ArrayList;
import java.util.List;

/**
 * Static factory class that has methods to create
 * classes: MclnStatement, MclnCondition, and MclnArc
 * <p>
 * Created by Admin on 1/16/2016.
 */
public final class MclnModelFactory {

    //
    //  C r e a t i n g   S t a t e m e n t
    //

    /**
     * Called when Mcln Statement is created by Editor
     *
     * @param cSysLocation
     * @return
     */
//    static synchronized final MclnStatement createMclnStatement(String UID, String subject, double[] cSysLocation) {
//        MclnStatement mclnStatement = createMclnStatement(UID, subject, cSysLocation,
//                ThreeShadesConfettiPalette.MCLN_CREATION_STATE);
//        mclnStatement.setSubject(subject);
//        return mclnStatement;
//    }

    /**
     * Called when Mcln Statement is created programmatically for Demo
     *
     * @param subject
     * @param cSysLocation
     * @param initialState
     * @return
     */
    static synchronized final MclnStatement createMclnStatement(String UID, String subject, double[] cSysLocation,
                                                                MclnState initialState) {
        MclnStatement mclnStatement = new MclnStatement(UID, subject, cSysLocation, initialState);
        return mclnStatement;
    }

    /**
     * Called when Mcln Statement is created programmatically for Demo
     *
     * @param subject
     * @param availableMclnStatementStates
     * @param cSysLocation
     * @param initialState
     * @return
     */
    static synchronized final MclnStatement createMclnStatement(
            String UID, String subject, AvailableMclnStatementStates availableMclnStatementStates,
            double[] cSysLocation, MclnState initialState) {
        MclnStatement mclnStatement = new MclnStatement(UID, subject, availableMclnStatementStates, cSysLocation,
                initialState);
        return mclnStatement;
    }

    /**
     * Called when Mcln Statement is created programmatically with Time Driven Input Simulating Program
     *
     * @param subject
     * @param cSysLocation
     * @param initialState
     * @param inputSimulatingProgramData
     * @return
     */
    static synchronized final MclnStatement createMclnStatementWithTimeDrivenProgram(
            String UID, String subject, AvailableMclnStatementStates availableMclnStatementStates,
            double[] cSysLocation, MclnState initialState, Object[][] inputSimulatingProgramData) {

        List<ProgramStep> programSteps = new ArrayList();
        for (int i = 0; i < inputSimulatingProgramData.length; i++) {
            Object[] programData = inputSimulatingProgramData[i];
            int ticks = (int) programData[0];
            MclnState mclnState = (MclnState) programData[1];
            TimeProgramStep timeProgramStep = new TimeProgramStep(ticks, mclnState);
            programSteps.add(timeProgramStep);
        }
        InputSimulatingProgram inputSimulatingProgram = new TimeDrivenProgram(programSteps);
        MclnStatement mclnStatement = new MclnStatement(UID, subject, availableMclnStatementStates, cSysLocation,
                initialState, inputSimulatingProgram);

        return mclnStatement;
    }

    static List<ProgramStep> mclnProgramDataToProgramStates(boolean programHasPhase,
                                                            List<ProgramStepData> programStepDataList) {
        List<ProgramStep> programSteps = new ArrayList();
        for (ProgramStepData programStepData : programStepDataList) {
            if (!programHasPhase && programStepData.isPhase()) {
                continue;
            }
            programSteps.add(programStepData.toProgramStep());
        }
        return programSteps;
    }

    /**
     * Called when Mcln Statement is created programmatically with State Driven Input Simulating Program
     *
     * @param subject
     * @param availableMclnStatementStates
     * @param cSysLocation
     * @param initialState
     * @param inputSimulatingProgramData
     * @return
     */
    static synchronized final MclnStatement createMclnStatementWithStateDrivenProgram(
            String UID, String subject, AvailableMclnStatementStates availableMclnStatementStates,
            double[] cSysLocation, MclnState initialState, Object[][] inputSimulatingProgramData) {

        List<ProgramStep> stateDrivenProgramSteps = new ArrayList();
        for (int i = 0; i < inputSimulatingProgramData.length; i++) {
            Object[] programData = inputSimulatingProgramData[i];
            MclnState expectedMclnState = (MclnState) programData[0];
            int ticks = (int) programData[1];
            MclnState generatedMclnState = (MclnState) programData[2];
            StateProgramStep stateProgramStep = new StateProgramStep(expectedMclnState, ticks, generatedMclnState);
            stateDrivenProgramSteps.add(stateProgramStep);
        }

        InputSimulatingProgram inputSimulatingProgram = new StateDrivenProgram(stateDrivenProgramSteps);
        MclnStatement mclnStatement = new MclnStatement(UID, subject, availableMclnStatementStates,
                cSysLocation, initialState, inputSimulatingProgram);
        return mclnStatement;
    }
}
