package dsdsse.designspace.executor;

import dsdsse.app.AppStateModel;
import mcln.model.MclnModel;
import mcln.model.MclnModelPublicInterface;
import mcln.model.MclnStatement;
import mcln.palette.MclnState;
import mcln.simulator.SimulatedStateChangeListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 31, 2013
 * Time: 9:47:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnSimulationController {

    private static final int TICK_PHASE = 2000;

    private static final int TICK_INTERVAL = 1000;

    private final Logger logger = Logger.getLogger(MclnSimulationController.class.getName());


    private static MclnSimulationController mclnSimulationController = new MclnSimulationController();

    public static MclnSimulationController getInstance() {
        return mclnSimulationController;
    }

    /**
     * Method called when user selects Property State from popup menu
     *
     * @param uid
     * @param userProvidedMclnStat
     */
    public static final void processRMBPopupMenuUserInput(String uid, MclnState userProvidedMclnStat) {
        mclnSimulationController.mclnModelPublicInterface.
                processUserProvidedRuntimePropertyState(uid, userProvidedMclnStat);
    }

    //
    //   i n s t a n c e
    //

    private MclnModelPublicInterface mclnModelPublicInterface;

    private Timer noneSwingSimulationTimer = new Timer("MCLN Controller Timer", true);

    private TimerTask timerTask = new TimerTask() {
        public void run() {
            try {
//             System.out.println("Tick! "+new Date().toString());
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                if (!(simulationEnabled && simulationRunning && !simulationPaused)) {
                    return;
                }

                ticksCounter++;
                AppStateModel.getInstance().updateSimulationTicks(ticksCounter);
                String advancedStateRecord = mclnModelPublicInterface.executeOneSimulationStep();

                if (advancedStateRecord == null || advancedStateRecord.length() == 0) {
//                    System.out.println("\n\n*************************");
//                    System.out.println("Simulation State Change Rerecord is empty");
//                    System.out.println("*************************\n");
                } else {
//                    System.out.println("\n\n*************************");
//                    System.out.println("MclnSimulationController: advanced state = \"" + advancedStateRecord + "\"");
//                    System.out.println("*************************");
                    String[] statementStates = advancedStateRecord.split("#");
//                    System.out.println("MclnSimulationController: Statement States = " + statementStates.length);
//                    for (int i = 0; i < statementStates.length; i++) {
//                        System.out.println("MclnSimulationController: statementState" + i + ",  " + statementStates[i]);
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private final SimulatedStateChangeListener simulatedStateChangeListener = new SimulatedStateChangeListener() {
        @Override
        public void simulatedPropertyStateChanged(MclnStatement mclnStatement) {
            if (!mclnStatement.shouldStateBeLogged()) {
                return;
            }
//            System.out.println("Mcln Simulation Controller/SimulatedStateChangeListener.simulatedPropertyStateChanged: " + mclnStatement.toString());
//            final long time = System.currentTimeMillis();
//            final String uid = mclnStatement.getUID();
//            final String statementText = mclnStatement.getStatementText();
//            final MclnState mclnState = mclnStatement.getCurrentMclnState();
        }
    };

    private boolean simulationEnabled;
    private boolean simulationRunning;
    private boolean simulationPaused;
    private MclnModel mclnModel;
    // Flag makes model initial state recorded to Trace Log
    private boolean recordModelInitialState = true;

    private int ticksCounter;

    private MclnSimulationController() {
        mclnModelPublicInterface = new MclnModelPublicInterface();
        mclnModelPublicInterface.addStateChangeListener(simulatedStateChangeListener);
        noneSwingSimulationTimer.schedule(timerTask, TICK_PHASE, TICK_INTERVAL);
    }


    public void clearSimulation() {
        simulationEnabled = false;
        simulationRunning = false;
        mclnModel.setSimulationEnabled(simulationEnabled);
        mclnModel.setSimulationRunning(simulationRunning);
        mclnModelPublicInterface.clearSimulation();
        ticksCounter = 0;
        AppStateModel.getInstance().updateSimulationTicks(ticksCounter);
    }

    public void setMclnModel(MclnModel mclnModel) {
        assert mclnModel != null : "provided MCLN model is null";
        this.mclnModel = mclnModel;
        mclnModelPublicInterface.setMclnModel(mclnModel);
//        if (mclnModel == null) {
//            System.out.println("\n\nSimulation Initialized with model set as null\n\n");
//        } else {
//            System.out.println("\n\nSimulation Initialized with model: " + mclnModel.getModelName() + "\n\n");
//        }
    }

    /**
     * S i m u l a t i o n   E n a b l e d
     */
    public void setSimulationEnabled() {
        simulationRunning = false;
        mclnModel.setSimulationRunning(simulationRunning);
        mclnModelPublicInterface.initializeSimulation();
        simulationEnabled = true;
        mclnModel.setSimulationEnabled(simulationEnabled);
        recordModelInitialState = true;
    }

    /**
     * S i m u l a t i o n   D i s a b l e d
     */
    public void setSimulationDisabled() {
        simulationRunning = false;
        mclnModel.setSimulationRunning(simulationRunning);
        setSimulationReset();
        simulationEnabled = false;
        mclnModel.setSimulationEnabled(simulationEnabled);
        recordModelInitialState = false;
    }

    /**
     * S i m u l a t i o n   S t a r t e d
     */
    public void setSimulationStarted() {
        if (!simulationEnabled) {
            return;
        }
        if (recordModelInitialState) {
            // called to record initial state to Trace History
            mclnModel.fireSimulationStepExecuted();
            recordModelInitialState = false;
        }
        simulationRunning = true;
        mclnModel.setSimulationRunning(simulationRunning);

        mclnModelPublicInterface.startSimulation();
        mclnModel.fireModelStateChanged();
    }

    /**
     * S i m u l a t i o n   S t o p p e d
     */
    public void setSimulationStopped() {
        if (!simulationEnabled) {
            return;
        }
        this.simulationRunning = false;
        mclnModel.setSimulationRunning(simulationRunning);
        mclnModelPublicInterface.stopSimulation();
    }

    public final void setSimulationPaused() {
        simulationPaused = true;
        mclnModel.setSimulationPaused(simulationPaused);
    }

    /**
     * S i m u l a t i o n   R e s u m e d
     */
    public void setSimulationResumed() {
        simulationPaused = false;
        mclnModel.setSimulationPaused(simulationPaused);
    }

    /**
     * E x e c u t e   O n e   S i m u l a t i o n   S t e p
     */
    public void executeOneSimulationStep() {
        if (!simulationEnabled) {
            return;
        }
        if (simulationRunning) {
            return;
        }
        if (recordModelInitialState) {
            // called to record initial state to Trace History
            mclnModel.fireSimulationStepExecuted();
            recordModelInitialState = false;
        }
        ticksCounter++;
        AppStateModel.getInstance().updateSimulationTicks(ticksCounter);
        mclnModelPublicInterface.executeOneSimulationStep();
    }

    /**
     * S i m u l a t i o n   R e s e t
     */
    public void setSimulationReset() {
        if (!simulationEnabled) {
            return;
        }
        recordModelInitialState = true;
        this.simulationRunning = false;
        mclnModel.setSimulationRunning(simulationRunning);
        ticksCounter = 0;
        AppStateModel.getInstance().updateSimulationTicks(ticksCounter);

        // following two call go to Simulating Engine
        // No listeners is called during this calls
        mclnModelPublicInterface.clearSimulation();
        mclnModelPublicInterface.resetSimulation();  // this call updates McLN MOdel

        // The following two methods are called after model
        // updated, they invoke MclnModelSimulationListener
        mclnModel.fireModelStateChanged();
        mclnModel.fireModelStateReset();
    }
}
