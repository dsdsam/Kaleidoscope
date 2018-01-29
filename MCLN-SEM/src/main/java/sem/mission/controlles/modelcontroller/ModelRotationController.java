package sem.mission.controlles.modelcontroller;

import sem.mission.explorer.model.SpaceExplorerModel;
import sem.appui.controller.ModelOperationRequest;
import sem.appui.controller.OperationExecutionListener;
import sem.utils.math.MathUtils;
import vw.valgebra.VAlgebra;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Nov 27, 2011
 * Time: 9:17:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelRotationController extends BasicOperationExecutionController {

    private final int TURN_ANGLE = 150;
    private final double BREAK_CRITERIA = TURN_ANGLE * 3;
    private final double DOUBLE_BREAK_CRITERIA = (TURN_ANGLE * 3) / 100;
    private final int LOW_SPEED_TURN_ANGLE = 10;
    private final int STOP_CRITERIA = 10; //LOW_SPEED_TURN_ANGLE / 2;

    private final SpaceExplorerModel spaceExplorerModel;

    private ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest;
    private ModelMotionOperation modelRotationOperation = ModelMotionOperation.OPERATION_NONE;
    private long targetAngle;
    private boolean targetAssigned;
    private int numberOfStepsToMove;
    private int stepCounter;

    private Object synchronizer = new Object();
    private boolean opedationInProgress;

    private int currentTurnAngle = TURN_ANGLE;
    private boolean lowSpeed = false;


    ModelRotationController(SpaceExplorerModel spaceExplorerModel) {
        this.spaceExplorerModel = spaceExplorerModel;
    }

    /**
     * @param modelOperationRequest
     */
    public void executeOperation(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest) {

        currentTurnAngle = TURN_ANGLE;
        lowSpeed = false;

        this.modelOperationRequest = modelOperationRequest;
        targetAssigned = modelOperationRequest.isArgumentAssigned();
        ModelMotionOperation requestedOperation = modelOperationRequest.getRequestedOperation();

        if (targetAssigned) {

            boolean spinToThePointRequested = requestedOperation == ModelMotionOperation.OPERATION_SPIN_TO_THE_POINT;
            boolean spinToTheAngleRequested = requestedOperation == ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE;

            double target = 0;
            if (spinToThePointRequested || spinToTheAngleRequested) {

                if (spinToThePointRequested) {
                    double[] targetLocation = modelOperationRequest.getArgument();
                    target = findDirectionAngleToTheTaggetPointFromCurrentLocation(targetLocation);
                } else if (spinToTheAngleRequested) {
                    target = modelOperationRequest.getArgument()[0];
                }

                this.targetAngle = MathUtils.doubleToLongWith2Dps(target);


                double currentDirectionalAngle = spaceExplorerModel.getDirection();
                double delta = (target - currentDirectionalAngle);
                double absDiff = Math.abs(delta);
                if (absDiff < DOUBLE_BREAK_CRITERIA) {
                    currentTurnAngle = LOW_SPEED_TURN_ANGLE;
                    lowSpeed = true;
                }
                if (Math.abs(delta) < 180) {
                    if (delta > 0) {
                        modelRotationOperation = ModelMotionOperation.OPERATION_SPIN_TO_THE_LEFT;
                    } else {
                        modelRotationOperation = ModelMotionOperation.OPERATION_SPIN_TO_THE_RIGHT;
                    }
                } else {
                    if (delta > 0) {
                        modelRotationOperation = ModelMotionOperation.OPERATION_SPIN_TO_THE_RIGHT;
                    } else {
                        modelRotationOperation = ModelMotionOperation.OPERATION_SPIN_TO_THE_LEFT;
                    }
                }

            } else {
                modelRotationOperation = requestedOperation;
            }

        } else {
            modelRotationOperation = requestedOperation;
            numberOfStepsToMove = 450;
            stepCounter = 0;
        }
        modelOperationRequest.setActualOperation(modelRotationOperation);
    }

    /**
     * @param worldTargetLocationVector
     * @return target angle
     */
    private double findDirectionAngleToTheTaggetPointFromCurrentLocation(double[] worldTargetLocationVector) {

        double[] currentLocationVector = spaceExplorerModel.getCurrentLocationVector();

        // find direction from current point to target point in world CSYS
        double[] selectedDirectionVector = new double[3];
        selectedDirectionVector = VAlgebra.subVec3(selectedDirectionVector, worldTargetLocationVector, currentLocationVector);
        double dx = selectedDirectionVector[0];
        double dy = selectedDirectionVector[1];
        double th = Math.atan2(dy, dx);
        if (th < 0) {
            th = 2 * Math.PI + th;
        }

        double targetAngle = th * (180 / Math.PI);
        targetAngle = Math.round(targetAngle);
//        System.out.println("findDirectionAngleToTheTaggetPointFromCurrentLocation " + targetAngle);
        return targetAngle;
    }

    public boolean isTargetDirectionAssigned() {
        return targetAssigned;
    }

    /**
     * @return target Angle as long
     */
    public long getTargetAngle() {
        return targetAngle;
    }

    /**
     * @return true when executed
     */
    public boolean isOperationBeingExecuted() {
        return modelRotationOperation != ModelMotionOperation.OPERATION_NONE;
    }

    /**
     * @return true when done
     */
//    @Override
    public boolean executeStep() {
        if (modelRotationOperation.equals(ModelMotionOperation.OPERATION_NONE)) {
            return true;
        }

        boolean targetReached = rotate();
        if (targetReached) {
            ModelOperationRequest returnedModelOperationRequest = modelOperationRequest;
            modelOperationRequest = null;
            modelRotationOperation = ModelMotionOperation.OPERATION_NONE;
//            targetAssigned = false;
//            targetAngle = 0;

            OperationExecutionListener operationExecutionListener = returnedModelOperationRequest.getOperationExecutionListener();
            if (operationExecutionListener != null) {
                returnedModelOperationRequest.setRequestExecutedSuccessfully();
                returnedModelOperationRequest.setActualOperation(ModelMotionOperation.OPERATION_NONE);
                operationExecutionListener.requestExecuted(returnedModelOperationRequest);
            }
        }
        return targetReached;
    }

    /**
     *
     *
     */


    private synchronized boolean rotate() {

        if (targetAssigned) {
            long absCurrentAngle = Math.abs(spaceExplorerModel.getLongDirection());
            long absTargetAngle = Math.abs(targetAngle);
            long diff = Math.abs(absCurrentAngle - absTargetAngle);
//            System.out.println("CurrentAngle  " + absCurrentAngle + "  diff " + diff);
//            if (diff == STOP_CRITERIA) {
//                return true;
//            }
            if (diff != currentTurnAngle) {
                if (diff < STOP_CRITERIA) {
                    return true;
                }
            }
            if (!lowSpeed && diff < BREAK_CRITERIA) {
                currentTurnAngle = LOW_SPEED_TURN_ANGLE;
                lowSpeed = true;
            }

        } else {
            if (stepCounter >= numberOfStepsToMove) {
                return true;
            }
            stepCounter++;
        }

        switch (modelRotationOperation) {
            case OPERATION_SPIN_TO_THE_LEFT:
                spaceExplorerModel.rotateModel(currentTurnAngle);
                break;
            case OPERATION_SPIN_TO_THE_RIGHT:
                spaceExplorerModel.rotateModel(-currentTurnAngle);
                break;
        }

        switch (modelRotationOperation) {
            case OPERATION_LEFT_TRACK_FORWARD:
                spaceExplorerModel.turnModel(-currentTurnAngle, -4.);
                break;
            case OPERATION_LEFT_TRACK_BACKWARD:
                spaceExplorerModel.turnModel(currentTurnAngle, -4.);
                break;
            case OPERATION_RIGHT_TRACK_FORWARD:
                spaceExplorerModel.turnModel(currentTurnAngle, 4.);
                break;
            case OPERATION_RIGHT_TRACK_BACKWARD:
                spaceExplorerModel.turnModel(-currentTurnAngle, 4.);
                break;
        }

        return false;
    }


    /**
     * Called when either GUI control wants the Mission to stop functioning:
     * Poser Status set Off, Execution Mode changed from Manusl to Auto, or Model Reset
     */
    public void stopOperationOnMissionStoped() {
//        System.out.println("stopOperation On MissionStoped " + modelRotationOperation);
        if (modelRotationOperation.equals(ModelMotionOperation.OPERATION_NONE)) {
            targetAssigned = false;
            targetAngle = 0;
            return;
        }

        ModelOperationRequest returnedModelOperationRequest = stopExecution();

        OperationExecutionListener operationExecutionListener = returnedModelOperationRequest.getOperationExecutionListener();
        if (operationExecutionListener != null) {
            returnedModelOperationRequest.setOperationInterrupted();
            operationExecutionListener.requestExecuted(returnedModelOperationRequest);
        }
    }

    /**
     * Called when either GUI control or MSLN wants to stop operation
     */
    public void cancelCurrentOperation() {
//        System.out.println("stopOperation " + modelRotationOperation);
        if (modelRotationOperation.equals(ModelMotionOperation.OPERATION_NONE)) {
            return;
        }

        ModelOperationRequest returnedModelOperationRequest = stopExecution();

        OperationExecutionListener operationExecutionListener = returnedModelOperationRequest.getOperationExecutionListener();
//        System.out.println("stopOperation operationExetutionListener " + operationExetutionListener);
        if (operationExecutionListener != null) {
            returnedModelOperationRequest.setOperationInterrupted();
            operationExecutionListener.requestExecuted(returnedModelOperationRequest);
        }
    }

    /**
     * @return ModelOperationRequest
     */
    private ModelOperationRequest stopExecution() {

        ModelOperationRequest returnedModelOperationRequest = modelOperationRequest;
        modelOperationRequest = null;
        modelRotationOperation = ModelMotionOperation.OPERATION_NONE;
        targetAssigned = false;
        targetAngle = 0;
        return returnedModelOperationRequest;

    }
}
