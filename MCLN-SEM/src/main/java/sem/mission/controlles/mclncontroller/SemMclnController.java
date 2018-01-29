package sem.mission.controlles.mclncontroller;

import adf.onelinemessage.AdfOneLineMessageManager;
import mcln.model.*;
import mcln.simulator.SimulatedStateChangeListener;
import sem.appui.PredicatePanel;
import sem.appui.SubjectNamePanel;
import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.controlles.modelcontroller.ModelMotionOperation;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Admin on 11/11/2017.
 */
public final class SemMclnController {

    private static final int TICK_PHASE = 2000;

    private static final int TICK_INTERVAL = 1000;

    private static SemMclnController semMcLnController;

    static SemMclnController createInstance(MclnProject mclnProject) {
        semMcLnController = new SemMclnController(mclnProject);
        return semMcLnController;
    }

    public static SemMclnController getInstance() {
        return semMcLnController;
    }

    private static final String makeStateMessage(String propertyName, String stateInterpretation) {
        return propertyName + "  -  " + stateInterpretation;
    }

    // I n s t a n c e

    private final MclnProject mclnProject;
    private MclnModel mclnModel;

    private boolean simulationEnabled;
    private boolean simulationRunning;
    private boolean simulationPaused;


    private int ticksCounter;

    private final MclnModelPublicInterface mclnModelPublicInterface;
    private final ExternalEventListener externalEventListener;
    private final ExternalActionResponseProcessor externalActionResponseProcessor;

    ExternalActionRequestProcessor externalActionRequestProcessor = new ExternalActionRequestProcessor() {
        @Override
        public void processActionRequest(String effectorID, String subject, String propertyName, String stateInterpretation) {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(() -> {
                    externalActionRequestProcessor.processActionRequest(effectorID, subject, propertyName, stateInterpretation);
                    System.out.println();
                });
                return;
            }

            ModelController modelController = ModelController.getInstance();

            if (propertyName.contains("Action Move Backward")) {
                String operation = stateInterpretation != null ? stateInterpretation : "";
                if (!operation.equalsIgnoreCase("Request Start Moving Backward")) {
                    return;
                }
                String statement = subject + " / " + propertyName;
                AdfOneLineMessageManager.showInfoMessage(statement);

                SubjectNamePanel.getSingleton().setText(statement);
                PredicatePanel.getSingleton().setText(makeStateMessage(propertyName, stateInterpretation));

                double[] moveBackDistance = new double[]{-7};
                boolean acknowledged = modelController.
                        executeEmergencyMotionOperation(effectorID, ModelMotionOperation.OPERATION_MOVE_BACKWARD,
                                moveBackDistance, null);
                return;
            }

            if (effectorID.equalsIgnoreCase("S-0000029") && propertyName.contains("Action Move Forward")) {
                String operation = stateInterpretation != null ? stateInterpretation : "";
                if (!operation.equalsIgnoreCase("Request Start Moving Forward")) {
                    return;
                }
                String statement = subject + " / " + propertyName;
                AdfOneLineMessageManager.showInfoMessage(statement);

                SubjectNamePanel.getSingleton().setText(statement);
                PredicatePanel.getSingleton().setText(makeStateMessage(propertyName, stateInterpretation));

                double[] moveForwardDistance = new double[]{7};
                boolean acknowledged = modelController.
                        executeEmergencyMotionOperation(effectorID, ModelMotionOperation.OPERATION_MOVE_FORWARD,
                                moveForwardDistance, null);
                return;
            }

            if (propertyName.contains("Action Change Direction")) {
                String operation = stateInterpretation != null ? stateInterpretation : "";
                if (!operation.equalsIgnoreCase("Request Start Rotating")) {
                    return;
                }
                operation = operation.replace("$", "").trim();
                String statement = subject + " / " + propertyName;
                AdfOneLineMessageManager.showInfoMessage(statement);

                SubjectNamePanel.getSingleton().setText(statement);
                PredicatePanel.getSingleton().setText(makeStateMessage(propertyName, stateInterpretation));

                modelController.executeEmergencyMotionOperation(effectorID, ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE, null,
                        null);
                return;
            }

            if (propertyName.contains("Action Move Forward")) {
                String operation = stateInterpretation != null ? stateInterpretation : "";
                operation = operation.replace("$", "").trim();
                if (!operation.equalsIgnoreCase("Request Start Moving Forward")) {
                    return;
                }

                String statement = subject + " / " + propertyName;
                AdfOneLineMessageManager.showInfoMessage(statement);

                SubjectNamePanel.getSingleton().setText(statement);
                PredicatePanel.getSingleton().setText(makeStateMessage("Emergency resolved", stateInterpretation));

                modelController.executeMotionOperation(ModelMotionOperation.OPERATION_MOVE_FORWARD_NON_STOP, null, null);
                externalActionResponseProcessor.processActionResponse(effectorID, "done");
            }
        }
    };

    /**
     * McLN Controller Response
     *
     * @param effectorID
     * @param response
     */
    public void processActionResponse(String effectorID, String response) {
        externalActionResponseProcessor.processActionResponse(effectorID, response);
        MclnStatement mclnStatement = mclnModel.getMclnStatementByUID(effectorID);
        String propertyName = mclnStatement.getPropertyName();
        String stateInterpretation = mclnStatement.getStateInterpretation();
        PredicatePanel.getSingleton().setText(makeStateMessage(propertyName, stateInterpretation));
    }


    private final SimulatedStateChangeListener simulatedStateChangeListener = new SimulatedStateChangeListener() {
        @Override
        public void simulatedPropertyStateChanged(MclnStatement mclnStatement) {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(() -> {
                    simulatedPropertyStateChanged(mclnStatement);
                    System.out.println();
                });
                return;
            }

            String subject = mclnStatement.getSubject();
            if (subject.contains("Front Space Sensor") || subject.contains("Rear Space Sensor")) {
                String stateInterpretation = mclnStatement.getStateInterpretation();
                if (!stateInterpretation.contains("End of Space")) {
                    return;
                }
                String propertyName = mclnStatement.getPropertyName();
                String statement = subject + " / " + propertyName;
                SubjectNamePanel.getSingleton().setText(statement);
                PredicatePanel.getSingleton().setText(makeStateMessage(propertyName, stateInterpretation));
                return;
            }
        }
    };

    // Simulation Ticks generator

    private Timer noneSwingSimulationTimer = new Timer("MCLN Controller Timer", true);

    private TimerTask timerTask = new TimerTask() {
        public void run() {
            try {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                if (!(simulationEnabled && simulationRunning && !simulationPaused)) {
                    return;
                }
                // E x e c u t e   O n e   S i m u l a t i o n   S t e p
                ticksCounter++;
                mclnModelPublicInterface.executeOneSimulationStep();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private SemMclnController(MclnProject mclnProject) {
        this.mclnProject = mclnProject;
        this.mclnModel = mclnProject.getCurrentMclnModel();
        MclnModel currentMclnModel = mclnProject.getCurrentMclnModel();

        //   Connecting Mcln Controller to McLN Model
        mclnModelPublicInterface = new MclnModelPublicInterface();
        mclnModelPublicInterface.addMclnModelExternalActionProcessor(externalActionRequestProcessor);
        mclnModelPublicInterface.addStateChangeListener(simulatedStateChangeListener);
        externalEventListener = mclnModelPublicInterface.getMclnModelExternalEventListener();
        externalActionResponseProcessor = mclnModelPublicInterface.getExternalActionResponseProcessor();
        mclnModelPublicInterface.setMclnModel(currentMclnModel);
        mclnModelPublicInterface.startSimulation();

        noneSwingSimulationTimer.schedule(timerTask, TICK_PHASE, TICK_INTERVAL);

        setSimulationEnabled();
        setSimulationStarted();
    }


    public void processFrontTouchEvent(MclnModelEvent mclnModelEvent) {
        externalEventListener.processInputEvent(mclnModelEvent);
    }

    /**
     * S i m u l a t i o n   E n a b l e d
     */
    public void setSimulationEnabled() {
        simulationRunning = false;
        mclnModelPublicInterface.initializeSimulation();
        simulationEnabled = true;
    }

    /**
     * S i m u l a t i o n   D i s a b l e d
     */
    public void setSimulationDisabled() {
        simulationRunning = false;
        resetSimulation();
        simulationEnabled = false;
    }

    /**
     * S i m u l a t i o n   S t a r t e d
     */
    public void setSimulationStarted() {
        if (!simulationEnabled) {
            return;
        }
        simulationRunning = true;
        mclnModelPublicInterface.startSimulation();
    }

    /**
     * S i m u l a t i o n   S t o p p e d
     */
    public void setSimulationStopped() {
        if (!simulationEnabled) {
            return;
        }
        this.simulationRunning = false;
        mclnModelPublicInterface.stopSimulation();
    }

    public final void setSimulationPaused() {
        simulationPaused = true;
    }

    /**
     * S i m u l a t i o n   R e s u m e d
     */
    public void setSimulationResumed() {
        simulationPaused = false;
    }

    /**
     * S i m u l a t i o n   R e s e t
     */
    public void resetSimulation() {
        if (!simulationEnabled) {
            return;
        }
        this.simulationRunning = false;
        ticksCounter = 0;
//        AppStateModel.getInstance().updateSimulationTicks(ticksCounter);
        mclnModelPublicInterface.clearSimulation();
        mclnModelPublicInterface.resetSimulation();
        mclnModel.fireModelStateChanged();
        mclnModel.fireModelStateReset();
    }
}
