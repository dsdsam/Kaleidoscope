/*
 * Created on Aug 20, 2005
 *
 */
package sem.mission.controlles.modelcontroller;

import mcln.model.MclnModelEvent;
import sem.app.SemAppStateModel;
import sem.appui.controller.BasicOperationRequest;
import sem.appui.controller.ModelOperationRequest;
import sem.appui.controller.OperationExecutionListener;
import sem.appui.controls.DirectionalNavigatonPanel;
import sem.appui.controls.ModelOperationRequestStateChangeListener;
import sem.infrastructure.MclnEvent;
import sem.infrastructure.SemConstants;
import sem.infrastructure.evdistributor.EventDistributionAdapter;
import sem.infrastructure.evdistributor.SemEventDistributor;
import sem.mission.controlles.mclncontroller.SemMclnController;
import sem.mission.controlles.modelcontroller.actions.CallbackListener;
import sem.mission.controlles.modelcontroller.actions.ServiceExecutionListener;
import sem.mission.controlles.modelcontroller.interfaces.ModelSensor;
import sem.mission.explorer.model.SpaceExplorerModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xpadmin
 */
public class ModelController implements SemConstants, SeModelConstants {


    private SpaceExplorerModel spaceExplorerModel;

    boolean engineStarted;

    boolean pause;

    private ModelOperationRequestQueue modelOperationRequestQueue;
    private ModelOperationExecutor modelOperationExecutor;
    private List<ModelSensor> sensors = new ArrayList();
    private double angle = 360;

    private static ModelController modelController;

    private ModelOperationRequest<ModelMotionOperation, double[]> currentlyExecutedModelMotionOperationRequest;

    private final List<ModelOperationRequestStateChangeListener> modelOperationRequestStateChangeListeners = new ArrayList();


    /**
     * @param spaceExplorerModel
     */
    public static synchronized ModelController createSingleInstance(SpaceExplorerModel spaceExplorerModel) {
        if (modelController != null) {
            throw new RuntimeException("ModelController instance alredy created");
        }
        modelController = new ModelController(spaceExplorerModel);
        return modelController;
    }

    /**
     * @return ModelController
     */
    public static ModelController getInstance() {
        if (modelController == null) {
            throw new RuntimeException("ModelController instance not created yet");
        }
        return modelController;
    }

    public boolean powerOn;
    private boolean mclnControlledBehavior = true;

    /**
     * listener of Power On/Off switch
     */
    private EventDistributionAdapter missionEventDistributionListener =
            new EventDistributionAdapter<Boolean,
                    CallbackListener>() {

                @Override
                public void onGuiStateModelEventFired(SemEventDistributor.EventId eventId, Component comp, Boolean argument,
                                                      CallbackListener callbackListener) {

                    switch (eventId) {
                /*
                    On switching the power ON first time after launch,
                    The Mission is pre-set to McLN controlled mode.
                    Then Explorer starts moving forward until it reaches the end of the space.
                    Then McLN Controller receives End of Space (EoS) event and starts
                    controlling the Explorer.

                    When the power is set ON after it was shutdown the system
                    is in the control mode that was before shutdown. User can
                    switch mode back and forth as desired.

                    On switching power off, the Mission is reset to its initial state:
                    1) Current operation is interrupted.
                    The control mode is not changed.
                 */
                        case POWER:
                            SemAppStateModel.setPowerON(argument);
                            onPauseResume(true);
                            boolean localPowerOn = argument;
                            ModelController.this.powerOn = localPowerOn;

                            // reset queue
                            modelOperationRequestQueue.setQueuePowerOn(powerOn);

                            if (powerOn) {
                                // Power up
                                engineStarted = powerOn;
                                // Start moving forward, if in McLN Control mode
                                if (mclnControlledBehavior) {
                                    startMovingWanderingAround();
                                }
                            } else {
                                // Power Shutdown
                                engineStarted = powerOn;
                                modelOperationExecutor.stopOperationOnMissionStopped();
                                SemMclnController.getInstance().resetSimulation();
                                spaceExplorerModel.resetModel();
                            }

                            touchCounter = 0;
                            onPauseResume(false);
                            break;


                        case MANUAL_CONTROL:
                    /*
                       User can switch to Manual Control and back to McLN Control mode.

                       When switching to Manual Control mode:
                       Model stays in its current position.
                       McLN Controller is reset.

                       When switching to McLN Control mode everything is started from initial position, so:
                       Queue is cleaned if it is ON.
                       Current operation is interrupted.
                       Model is reset to initial position.
                     */
                            onPauseResume(true);
                            boolean autoManualControlState = argument;
                            mclnControlledBehavior = autoManualControlState;

                            if (mclnControlledBehavior) {
                                // Switching to McLN Control mode

                                // Clear queue
                                modelOperationRequestQueue.setQueuePowerOn(false);
                                modelOperationRequestQueue.setQueuePowerOn(true);
                                // Stopping current operation
                                modelOperationExecutor.stopOperationOnMissionStopped();
                                // Resetting Explorer to initial state
                                spaceExplorerModel.resetModel();
                                touchCounter = 0;
                                // Start moving forward
                                startMovingWanderingAround();

                            } else {
                                // Switching to Manual Control mode

                                // What is this ?
                                if (modelOperationRequestQueue.isQueueOn()) {
                                    modelOperationRequestQueue.setQueuePowerOn(true);
                                }
                                // Stopping current operation
                                modelOperationExecutor.stopOperationOnMissionStopped();
                                // Resetting McLN to initial state
                                SemMclnController.getInstance().resetSimulation();
                            }
                            onPauseResume(false);
                            break;


                        case RESET_TO_INITIAL_POSITION:
                            onPauseResume(true);

                            if (modelOperationRequestQueue.isQueueOn()) {
                                modelOperationRequestQueue.resetQueue();
                            }
                            // Stopping current operation
                            modelOperationExecutor.stopOperationOnMissionStopped();

                            if (mclnControlledBehavior) {
                                // Resetting while in McLN Control mode

                                // Resetting McLN to initial state
                                SemMclnController.getInstance().resetSimulation();
                                // Resetting Explorer to initial state
                                spaceExplorerModel.resetModel();
                                // Start moving forward
                                startMovingWanderingAround();
                            } else {
                                // Resetting while in Manual Control mode

                                // Resetting Explorer to initial state
                                spaceExplorerModel.resetModel();
                            }

                            touchCounter = 0;
                            onPauseResume(false);
                            break;
                    }
                }
            };

    /**
     * Starts McLN controlled Demo process
     */
    private void startMovingWanderingAround() {
//        executeMotionOperation(ModelMotionOperation.OPERATION_MOVE_FORWARD_NON_STOP, null, null);
        SemMclnController.getInstance().setSimulationStarted();
        for (int i = 0; i < 3; i++) {
//            ModelOperationRequest<ModelMotionOperation, double[]> modelMotionOperationRequest =
//                    executeModelMotionOperation(ModelMotionOperation.OPERATION_MOVE_FORWARD, new double[]{10.}, null);

            ModelOperationRequest<ModelMotionOperation, double[]> modelMotionOperationRequest =
                    executeModelMotionOperation(ModelMotionOperation.OPERATION_MOVE_BACKWARD, new double[]{-10.}, null);

//        ModelOperationRequest<ModelMotionOperation, double[]> modelMotionOperationRequest =
//                ModelOperationRequest.createRequest(operation, targetValue, serviceExecutionListener, operationExecutionListener);
            modelMotionOperationRequest.setRequestEnqueued();
            modelOperationRequestQueue.enqueueModelMotionOperationRequest(modelMotionOperationRequest);
        }
    }

    /**
     * //////////////////////////////////////////////////////////////////////////////////////////////////////
     * //                     Motion   Event   Distribution  Listener
     * //////////////////////////////////////////////////////////////////////////////////////////////////////
     */
    private EventDistributionAdapter motionEventDistributionListener = new EventDistributionAdapter<Object, ServiceExecutionListener>() {

        public void onGuiStateModelEventFired(SemEventDistributor.EventId eventId, Component comp, Object argument,
                                              ServiceExecutionListener serviceExecutionListener) {
            ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest = null;
            switch (eventId) {
                case CANCEL_OPERATION_EXECUTION:
                    if (modelOperationRequestQueue.isQueueOn()) {
                        modelOperationRequestQueue.resetQueue();
                    }
                    cancelCurrentOperation();
                    return;
                case MOVE_FORWARD:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_MOVE_FORWARD,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case MOVE_BACKWARD:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_MOVE_BACKWARD,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_MOVE_TO_THE_POINT:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_MOVE_TO_THE_POINT,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_MOVE_FORWARD_NON_STOP:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_MOVE_FORWARD_NON_STOP,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_SPIN_TO_THE_POINT:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_SPIN_TO_THE_POINT,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_ROTATE_TO_THE_ANGLE:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_SPIN_TO_THE_RIGHT:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_SPIN_TO_THE_RIGHT,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_SPIN_TO_THE_LEFT:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_SPIN_TO_THE_LEFT,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_LEFT_TRACK_FORWARD:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_LEFT_TRACK_FORWARD,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_RIGHT_TRACK_FORWARD:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_RIGHT_TRACK_FORWARD,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_LEFT_TRACK_BACKWARD:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_LEFT_TRACK_BACKWARD,
                            (double[]) argument, serviceExecutionListener);
                    break;
                case OPERATION_RIGHT_TRACK_BACKWARD:
                    modelOperationRequest = executeModelMotionOperation(ModelMotionOperation.OPERATION_RIGHT_TRACK_BACKWARD,
                            (double[]) argument, serviceExecutionListener);
                    break;
            }
            if (modelOperationRequest.isExecutingFromQueue() && serviceExecutionListener != null) {
                serviceExecutionListener.executionDone(modelOperationRequest);
            }
        }
    };

    /**
     *
     */
    private OperationExecutionListener emergencyExecutionListener = new OperationExecutionListener() {

        public void requestExecuted(BasicOperationRequest<?, ?> operationRequest) {


            currentlyExecutedModelMotionOperationRequest = null;
            ModelMotionOperation requestedOperation = (ModelMotionOperation) operationRequest.getRequestedOperation();

//            System.out.println("E m e r g e n c y ExetutionListener.requestExecuted(OperationEcetutionListener): operation = " + requestedOperation +
//                    " accomplished with status = " + operationRequest.getOperationRequestStatus() +
//                    " , Service listener = " + operationRequest.getServiceExecutionListener());

            /**
             * This is the mechanism to inform control that current operation is complete
             * an target should be cleand
             */
            fireModelOperationRequestExecutionCompleted(operationRequest);

            if (operationRequest.isExecutingFromQueue()) {
//                if (!operationRequest.isOperationInterrupted()) {
//                    modelOperationRequestQueue.requestExecutionInterrupted();
//                } else {
                modelOperationRequestQueue.requestExecuted();
//                }
//                return;
            } else {
                if (operationRequest.isOperationInterrupted()) {
                    return;
                }
            }

            if (requestedOperation == ModelMotionOperation.OPERATION_MOVE_FORWARD) {
                SemMclnController.getInstance().processActionResponse(effectorID, "done");
            } else if (requestedOperation == ModelMotionOperation.OPERATION_MOVE_BACKWARD) {
                SemMclnController.getInstance().processActionResponse(effectorID, "done");
//                angle = generateNewDirectionAngle(angle);
////                System.out.println("E m e r g e n c y    angle " + angle);
//                executeEmergencyMotionOperation(ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE, new double[]{angle},
//                        null);

            } else if (requestedOperation == ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE) {
                SemMclnController.getInstance().processActionResponse(effectorID, "done");
//                executeMotionOperation(ModelMotionOperation.OPERATION_MOVE_FORWARD_NON_STOP, null, null);
            }
        }
    };

    /**
     *
     */
    private OperationExecutionListener operationExecutionListener = new OperationExecutionListener() {

        public void requestExecuted(BasicOperationRequest<?, ?> operationRequest) {

            currentlyExecutedModelMotionOperationRequest = null;

//            ModelMotionOperation requestedOperation = operationRequest.getRequestedOperation();
//            OperationRequestStatus operationRequestStatus = operationRequest.getOperationRequestStatus();
//            System.out.println("ModelController.requestExecuted(OperationEcetutionListener): operation = " + requestedOperation +
//                    " accomplished with status = " + operationRequestStatus + " , Service listener = " + serviceExecutionListener);

            fireModelOperationRequestExecutionCompleted(operationRequest);

            if (operationRequest.isExecutingFromQueue()) {
                if (operationRequest.isOperationInterrupted()) {
                    // we have to release semaphor
                    modelOperationRequestQueue.requestExecutionInterrupted();
                } else {
                    modelOperationRequestQueue.requestExecuted();
                }

                // we return here for not to call listener
                // it is from queue. It might be many same operations in the queue
                return;

            }
            ServiceExecutionListener serviceExecutionListener = operationRequest.getServiceExecutionListener();
            if (serviceExecutionListener != null) {
                serviceExecutionListener.executionDone(operationRequest);
            }

        }
    };


    /**
     * M o d e l    C o n t r o l l e r
     *
     * @param spaceExplorerModel
     */
    private ModelController(SpaceExplorerModel spaceExplorerModel) {
        this.spaceExplorerModel = spaceExplorerModel;

        modelOperationRequestQueue = new ModelOperationRequestQueue(this);
        modelOperationExecutor = new ModelOperationExecutor(this, spaceExplorerModel);

        sensors = spaceExplorerModel.getAllSensors();
        SemEventDistributor.addEventDistributionListener(SemEventDistributor.EventGroup.MISSION_EVENT,
                missionEventDistributionListener);
        SemEventDistributor.addEventDistributionListener(SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                motionEventDistributionListener);

    }

    /**
     * @return true when modelOperationRequestQueue is On
     */
    public boolean isQueueOn() {
        return modelOperationRequestQueue.isQueueOn();
    }

    /**
     * @param modelOperationRequestStateChangeListener
     */
    public void addModelOperationRequestStateChangeListener(ModelOperationRequestStateChangeListener
                                                                    modelOperationRequestStateChangeListener) {
        modelOperationRequestStateChangeListeners.add(modelOperationRequestStateChangeListener);
    }

    public ModelOperationRequestQueue getModelOperationRequestQueue() {
        return modelOperationRequestQueue;
    }

    /**
     * @return SpaceExplorerModel
     */
    public SpaceExplorerModel getSpaceExplorerModel() {
        return spaceExplorerModel;
    }


    /**
     * The method is intended to inform Model Controller about events in the model
     *
     * @param seModelEvent
     */
    public void enqueueSeModelEvent(SeModelEvent seModelEvent) {
        System.out.println(" enqueueSeModelEvent " + seModelEvent);
    }

    /**
     * Called when the application is closed. Stops the model activity.
     */
    public void shutDown() {
//        timer.cancel();
//        modelOperationExecutor.animationTickTimer.stop();
    }

    // ***************************************************************
    //   Model Operations API
    // ***************************************************************
    private String effectorID;

    public boolean executeEmergencyMotionOperation(String effectorID, ModelMotionOperation operation, double[] targetValue,
                                                   ServiceExecutionListener serviceExecutionListener) {
        this.effectorID = effectorID;
        boolean result = executeEmergencyMotionOperation(operation, targetValue, serviceExecutionListener);
        return result;
    }

    public boolean executeEmergencyMotionOperation(ModelMotionOperation operation, double[] targetValue,
                                                   ServiceExecutionListener serviceExecutionListener) {

        if (modelOperationRequestQueue.isQueueOn()) {

            if (operation == ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE) {
                angle = generateNewDirectionAngle(angle);
                targetValue = new double[]{angle};
//            System.out.println("E m e r g e n c y    angle " + angle);
                if (modelOperationRequestQueue.isQueueOn()) {
                    // activate transmitssion to orbital station
                    if (touchCounter >= 5) {
                        touchCounter = 0;
                        double[] transmissionArgument = new double[]{-10};
                        ModelOperationRequest<ModelMotionOperation, double[]> transmissionRequest =
                                ModelOperationRequest.createRequest(ModelMotionOperation.OPERATION_TRANSMISSION,
                                        transmissionArgument, null, emergencyExecutionListener);
                        transmissionRequest.setRequestEnqueued();
                        modelOperationRequestQueue.enqueueModelMotionOperationRequest(transmissionRequest);
                    }
                }

            }

            ModelOperationRequest<ModelMotionOperation, double[]> modelMotionOperationRequest =
                    ModelOperationRequest.createRequest(operation, targetValue, null, emergencyExecutionListener);
            modelMotionOperationRequest.setRequestEnqueued();
            modelOperationRequestQueue.enqueueModelMotionOperationRequest(modelMotionOperationRequest);

//          // activate transmitssion to orbital station
//            ModelOperationRequest<ModelMotionOperation, double[]> transmissionRequest =
//                    ModelOperationRequest.createRequest(ModelMotionOperation.OPERATION_TRANSMISSION,
//                            targetValue, null, emergencyExecutionListener);
//            transmissionRequest.setRequestEnqueued();
//            modelOperationRequestQueue.enqueueModelMotionOperationRequest(transmissionRequest);
////        executeEmergencyMotionOperation(ModelMotionOperation.OPERATION_TRANSMISSION, moveBackDistance, null);
            return true;
        } else {

            if (operation == ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE) {
                angle = generateNewDirectionAngle(angle);
                targetValue = new double[]{angle};
//            System.out.println("E m e r g e n c y    angle " + angle);
            }

            ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest =
                    ModelOperationRequest.createRequest(operation, targetValue, serviceExecutionListener,
                            emergencyExecutionListener);
            processMotionOperationRequest(modelOperationRequest);
            return false;
        }

    }

    /**
     * Places Model Motion Operation Request to Motion Operations Queue
     *
     * @param operation
     * @param targetValue
     * @param serviceExecutionListener
     * @return true when request enqueued
     */
    private ModelOperationRequest<ModelMotionOperation, double[]>
    executeModelMotionOperation(ModelMotionOperation operation, double[] targetValue,
                                ServiceExecutionListener serviceExecutionListener) {

        ModelOperationRequest<ModelMotionOperation, double[]> modelMotionOperationRequest;
        if (modelOperationRequestQueue.isQueueOn()) {
            modelMotionOperationRequest = ModelOperationRequest.createRequest(operation, targetValue,
                    null, operationExecutionListener);
            modelMotionOperationRequest.setRequestEnqueued();
            modelOperationRequestQueue.enqueueModelMotionOperationRequest(modelMotionOperationRequest);
        } else {
            modelOperationExecutor.cancelCurrentOperation();
            modelMotionOperationRequest = ModelOperationRequest.createRequest(operation, targetValue,
                    serviceExecutionListener, operationExecutionListener);
            processMotionOperationRequest(modelMotionOperationRequest);
        }
        return modelMotionOperationRequest;

    }

    /**
     * The method is called from Manual Control Panel.
     * But not from the movement buttons
     * It is also called from Mcln Controller to start Move Forward operation.
     * Currently Operation Queue is always ON.
     * - 11.27.2017
     * <p>
     * Places Model Motion Operation Request to Motion Operations Queue
     *
     * @param operation
     * @param targetValue
     * @param serviceExecutionListener
     * @return true when request enqueued
     */
    public boolean executeMotionOperation(ModelMotionOperation operation, double[] targetValue,
                                          ServiceExecutionListener serviceExecutionListener) {

        if (modelOperationRequestQueue.isQueueOn()) {  // was false
            ModelOperationRequest<ModelMotionOperation, double[]> modelMotionOperationRequest =
                    ModelOperationRequest.createRequest(operation, targetValue, serviceExecutionListener, operationExecutionListener);
            modelMotionOperationRequest.setRequestEnqueued();
            modelOperationRequestQueue.enqueueModelMotionOperationRequest(modelMotionOperationRequest);
            return true;
        } else {
            modelOperationExecutor.cancelCurrentOperation();
            ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest =
                    ModelOperationRequest.createRequest(operation, targetValue, serviceExecutionListener,
                            operationExecutionListener);
            processMotionOperationRequest(modelOperationRequest);
            return false;
        }

    }

    private boolean currentlyExcecutedOperationIsSpinToThePoint() {
        if (currentlyExecutedModelMotionOperationRequest == null) {
            return false;
        }
        ModelMotionOperation modelMotionOperation = currentlyExecutedModelMotionOperationRequest.getRequestedOperation();
        if (modelMotionOperation == ModelMotionOperation.OPERATION_SPIN_TO_THE_POINT) {
            return true;
        }
        return false;
    }


    /**
     * Executes Model Motion Operation Request
     *
     * @param modelMotionOperationRequest
     */
    void processMotionOperationRequest(ModelOperationRequest<ModelMotionOperation, double[]> modelMotionOperationRequest) {
//        System.out.println("ModeController(from Queue).processMotionOperationRequest: operation = " +
//                modelMotionOperationRequest.getRequestedOperation() +
//                " accomplished with status = " + modelMotionOperationRequest.getOperationRequestStatus() +
//                " , Service listener = " + modelMotionOperationRequest.getServiceExecutionListener());

        currentlyExecutedModelMotionOperationRequest = modelMotionOperationRequest;

        ModelMotionOperation operation = modelMotionOperationRequest.getRequestedOperation();
        if (operation.isRotationOperation(operation)) {
            modelOperationExecutor.modelRotationController.executeOperation(modelMotionOperationRequest);
        } else if (operation.isMoveOperation(operation)) {
            modelOperationExecutor.modelMotionController.executeOperation(modelMotionOperationRequest);
        } else if (operation.isTransmissionOperation()) {
            double xLocation = spaceExplorerModel.getCurX();
            double yLocation = spaceExplorerModel.getCurY();
            double[] currentLocation = {xLocation, yLocation};
            modelMotionOperationRequest.setRequestFailed();
            modelMotionOperationRequest.setArgument(currentLocation);
            modelOperationExecutor.modelTransmissionController.executeOperation(modelMotionOperationRequest);
        }
        fireModelOperationRequestExecutionStarted(modelMotionOperationRequest);
    }

    public boolean isTargetDirectionAssigned() {
        return modelOperationExecutor.modelRotationController.isTargetDirectionAssigned();
    }

    /**
     *
     *
     */

    // ****************************************************************
    // B u t t o n A c t i o n s A P I
    // ****************************************************************

    /**
     * P o w e r   O n / Off
     */
    public void onIgnition(boolean powerOn) {

    }

    public double getTargetDirection() {
        return modelOperationExecutor.modelRotationController.getTargetAngle();
    }


    public boolean isTargetLocationAssigned() {
        return modelOperationExecutor.modelMotionController.isTargetLocationAssigned();
    }

    /**
     *
     */
    public ModelController() {
        super();
    }

    /**
     *
     */
    public void onPauseResume(boolean pause) {
        this.pause = pause;
    }

    int touchCounter;

    /**
     * @param mclnEvent
     */
    public void processSensorEvent(MclnEvent mclnEvent) {
        if (mclnEvent.isFrontSpaceSensorOn()) {
            touchCounter++;
        }

        // stop everything before sending event to McLN Controller
        if (modelOperationRequestQueue.isQueueOn()) {
            modelOperationRequestQueue.discardEnqueuedElements();
        }
        cancelCurrentOperation();

        if (mclnControlledBehavior) {
            /*
               Send to McLN Controller McLN Input Event if
               model behavior is controlled by McLN Controller
             */
            String sensorInputID;
            if (mclnEvent.isFrontSpaceSensorOn()) {
                sensorInputID = "S-0000036";
            } else if (mclnEvent.isRearSpaceSensorOn()) {
                sensorInputID = "S-0000035";
            } else {
                return;
            }
            MclnModelEvent mclnModelEvent = new MclnModelEvent(sensorInputID, MclnModelEvent.EventType.EXTERNAL_INPUT, null);
            SemMclnController.getInstance().processFrontTouchEvent(mclnModelEvent);
        }

        if (true) {
            return;
        }

        double[] moveBackDistance = new double[]{-4};
        executeEmergencyMotionOperation(ModelMotionOperation.OPERATION_MOVE_BACKWARD, moveBackDistance, null);


        if (modelOperationRequestQueue.isQueueOn()) {
            // activate transmitssion to orbital station
//            if (touchCounter >= 5) {
//                touchCounter = 0;
//                ModelOperationRequest<ModelMotionOperation, double[]> transmissionRequest =
//                        ModelOperationRequest.createRequest(ModelMotionOperation.OPERATION_TRANSMISSION,
//                                moveBackDistance, null, emergencyExecutionListener);
//                transmissionRequest.setRequestEnqueued();
//                modelOperationRequestQueue.enqueueModelMotionOperationRequest(transmissionRequest);
//            }
            moveBackDistance = new double[]{-7};
            executeEmergencyMotionOperation(ModelMotionOperation.OPERATION_MOVE_BACKWARD, moveBackDistance, null);


            angle = generateNewDirectionAngle(angle);
//            System.out.println("E m e r g e n c y    angle " + angle);
            executeEmergencyMotionOperation(ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE, new double[]{angle},
                    null);
            executeMotionOperation(ModelMotionOperation.OPERATION_MOVE_FORWARD_NON_STOP, null, null);
        } else {
            moveBackDistance = new double[]{-7};
            executeEmergencyMotionOperation(ModelMotionOperation.OPERATION_MOVE_BACKWARD, moveBackDistance, null);
        }

//        MclnSimulator.getInstance().enqueueEvent(mclnEvent);
    }

    /**
     * @param angle
     * @return new angle
     */
    private double generateNewDirectionAngle(double angle) {

        double deltaAngle = Math.random() * 90;
        if (deltaAngle < 30) {
            deltaAngle = 30;
        }
        angle = angle - deltaAngle;
        if (angle < 0) {
            angle = 360 + angle;
        }

        angle = Math.round(angle);

        if (Math.abs(0 - angle) < DirectionalNavigatonPanel.EPS ||
                Math.abs(360 - angle) < DirectionalNavigatonPanel.EPS) {
            angle = 0;
        }

        return angle;
    }

    /**
     * Called when Stop Button  pressed  or  Behavior controlles changes  model behavior.
     * For instance, when senser signsls that continuing current operation is impossible.
     * We just Interrupt current operation regarless if it is executed from queue or not.
     */
    private void cancelCurrentOperation() {
        modelOperationExecutor.cancelCurrentOperation();
    }

    /**
     * Call when user activates new operation by clicking GUI control
     * If Queue is On the new operation does not interrupt current operation
     * Instead the new operation is placed to the end of the queue.
     * If Queue is Off the current operation is canceled and new will we started
     */
    public void stopAllOperation() {
        if (modelOperationRequestQueue.isQueueOn()) {
            return;
        }
        modelOperationExecutor.cancelCurrentOperation();
    }

    /**
     * @param modelOperationRequest
     */
    private void fireModelOperationRequestExecutionStarted(BasicOperationRequest modelOperationRequest) {
        for (ModelOperationRequestStateChangeListener modelRequestStateChangeListener :
                modelOperationRequestStateChangeListeners) {
            modelRequestStateChangeListener.executionStarted(modelOperationRequest);
        }
    }

    /**
     * @param modelOperationRequest
     */
    private void fireModelOperationRequestExecutionCompleted(BasicOperationRequest modelOperationRequest) {
        for (ModelOperationRequestStateChangeListener modelRequestStateChangeListener :
                modelOperationRequestStateChangeListeners) {
            modelRequestStateChangeListener.executionCompleted(modelOperationRequest);
        }
    }
}
