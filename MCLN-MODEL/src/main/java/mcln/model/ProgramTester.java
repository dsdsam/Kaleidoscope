package mcln.model;

import mcln.palette.MclnState;

import java.util.List;

/**
 * Created by Admin on 7/31/2016.
 */
public class ProgramTester extends MclnStatementStateHolder {

    public static ProgramTester createProgramTester(MclnState mclnState, boolean theProgramHasPhase,
                                                    boolean timeDrivenProgram,
                                                    List<ProgramStepData> programStepDataList) {
        return new ProgramTester(mclnState, theProgramHasPhase, timeDrivenProgram, programStepDataList);
    }

    //
    //   I n s t a n c e
    //

    private MclnState mclnState;

    private boolean theProgramHasPhase;
    private boolean timeDrivenProgram;
    private List<ProgramStepData> programStepDataList;

    private boolean hasInputGenerator;
    private InputSimulatingProgram inputSimulatingProgram;

    private ProgramTester(MclnState mclnState, boolean theProgramHasPhase, boolean timeDrivenProgram,
                          List<ProgramStepData> programStepDataList) {
        this.theProgramHasPhase = theProgramHasPhase;
        this.timeDrivenProgram = timeDrivenProgram;
        this.programStepDataList = programStepDataList;

        List<ProgramStep> programSteps = MclnModelFactory.mclnProgramDataToProgramStates(theProgramHasPhase,
                programStepDataList);

        if (programSteps == null) {
            inputSimulatingProgram = null;
            return;
        }

        setCurrentMclnState(mclnState);

        // program reset
        InputSimulatingProgram inputSimulatingProgram;
        if (timeDrivenProgram) {
            inputSimulatingProgram = new TimeDrivenProgram(programSteps);
        } else {
            inputSimulatingProgram = new StateDrivenProgram(programSteps);
        }
        this.inputSimulatingProgram = inputSimulatingProgram;
        inputSimulatingProgram.setMclnStatement(this);
    }

    public boolean theProgramHasPhase() {
        return theProgramHasPhase;
    }

    /**
     * @return
     */
    public InputGeneratorState initSimulationTest() {
        if (inputSimulatingProgram == null) {
            return null;
        }
        return inputSimulatingProgram.initializeSimulation();
    }

    /**
     * used for testing generating program at initialization time
     *
     * @return
     */
    public InputGeneratorState testInputSimulatingProgramStep() {
        if (inputSimulatingProgram == null) {
            return null;
        }
        InputGeneratorState inputGeneratorState = inputSimulatingProgram.testStep();
        return inputGeneratorState;
    }

    public void setTestingCurrentMclnState(MclnState mclnState) {
        setCurrentMclnState(mclnState);
    }

}
