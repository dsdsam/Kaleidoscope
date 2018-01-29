package sem.mission.controlles.modelcontroller;

import sem.mission.explorer.model.SpaceExplorerModel;
import sem.appui.controller.ModelOperationRequest;
import sem.appui.controller.OperationExecutionListener;
import vw.valgebra.VAlgebra;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 20, 2011
 * Time: 8:12:02 PM
 * To change this template use File | Settings | File Templates.
 */
 class ModelMotionController extends BasicOperationExecutionController {

    private final double MOTION_STEP = 0.30;
    private final double BREAK_CRITERIA = MOTION_STEP * 10;
    private final double SLOW_MOTION_STEP = 0.10;
    private final double STOP_CRITERIA = SLOW_MOTION_STEP / 2;

    private final SpaceExplorerModel spaceExplorerModel;

    private ModelOperationRequest modelOperationRequest;
    private ModelMotionOperation modelMotionOperation = ModelMotionOperation.OPERATION_NONE;

    private boolean targetAssigned;
    private TargetPositionLocation targetPositionLocation;
    private double distanceToTheTarget;
    private int numberOfStepsToMove;
    private int stepCounter;

    private double currentMotionStep = MOTION_STEP;
    private boolean lowSpeed = false;

    /**
     *
     */
    ModelMotionController(SpaceExplorerModel spaceExplorerModel) {
        this.spaceExplorerModel = spaceExplorerModel;
    }

    /**
     * @param modelOperationRequest
     */
    void executeOperation(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest) {

        currentMotionStep = MOTION_STEP;
//        currentMotionStep = 0;
        lowSpeed = false;

        distanceToTheTarget = 0;
        this.modelOperationRequest = modelOperationRequest;
        targetAssigned = modelOperationRequest.isArgumentAssigned();
        ModelMotionOperation requestedOperation = modelOperationRequest.getRequestedOperation();
        modelOperationRequest.setActualOperation(requestedOperation);

        numberOfStepsToMove = 0;
        stepCounter = 0;

        if (targetAssigned) {
            double distance = 0;

            boolean moveToThePointRequested = requestedOperation == ModelMotionOperation.OPERATION_MOVE_TO_THE_POINT;
            if (moveToThePointRequested) {
                requestedOperation = ModelMotionOperation.OPERATION_MOVE_FORWARD;
                double[] targetLocation = modelOperationRequest.getArgument();
                distance = findDistanceFromCurrentLocationToTheTargetLocation(targetLocation);
            } else {
                distance = modelOperationRequest.getArgument()[0];
            }

            if (Math.abs(distance) < BREAK_CRITERIA) {
                currentMotionStep = SLOW_MOTION_STEP;
                lowSpeed = true;
            }

            double[] currentDirectionVector = spaceExplorerModel.getCurrentDirectionVector();
            double[] currentLocationVector = spaceExplorerModel.getCurrentLocationVector();

            double[] targetLocationVector = new double[3];
            VAlgebra.LinCom3(targetLocationVector, 1, currentLocationVector, distance, currentDirectionVector);
            this.targetPositionLocation = new TargetPositionLocation(targetLocationVector[0], targetLocationVector[1]);
            distanceToTheTarget = targetPositionLocation.getDistance();

            this.modelMotionOperation = requestedOperation;

        } else if (requestedOperation == ModelMotionOperation.OPERATION_MOVE_FORWARD_NON_STOP) {
            this.targetPositionLocation = new TargetPositionLocation(-1, -1);
            this.modelMotionOperation = ModelMotionOperation.OPERATION_MOVE_FORWARD;
        } else {
            this.targetPositionLocation = new TargetPositionLocation(-1, -1);
            this.modelMotionOperation = requestedOperation;
            numberOfStepsToMove = 200;
            stepCounter = 0;
        }

    }

    /**
     * @param worldTargetpoint
     * @return distance from current location to the target point
     */
    private double findDistanceFromCurrentLocationToTheTargetLocation(double[] worldTargetpoint) {

        double[] currentLocationVector = spaceExplorerModel.getCurrentLocationVector();

        // find direction from current point to target point in world CSYS
        double[] selectedDirectionVector = new double[3];
        selectedDirectionVector = VAlgebra.subVec3(selectedDirectionVector, worldTargetpoint, currentLocationVector);
        double selectedMoveingDistance = VAlgebra.vec2Len(selectedDirectionVector);
        return selectedMoveingDistance;
    }


    public boolean isTargetLocationAssigned() {
        return targetAssigned;
    }

//    public TargetLocation getTargetLocation() {
//        return targetLocation;
//    }

    /**
     * @return true when executed
     */
    public boolean isOperationBeingExecuted() {
        return modelMotionOperation != ModelMotionOperation.OPERATION_NONE;
    }

    /**
     *
     */
    @Override
    public boolean executeStep() {
        if (modelMotionOperation.equals(ModelMotionOperation.OPERATION_NONE)) {
//            System.out.println("NOP");
            return true;
        }

        boolean targetReached = move();
        if (targetReached) {
            ModelOperationRequest returnedModelOperationRequest = modelOperationRequest;
            modelMotionOperation = ModelMotionOperation.OPERATION_NONE;

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
    private synchronized boolean move() {
        if (targetAssigned) {
            double[] targetLocationVector = targetPositionLocation.getVector();
            double currentDistanceToTheTargetLocation = findDistanceFromCurrentLocationToTheTargetLocation(targetLocationVector);
//            System.out.println("currentMotionStep  " + currentMotionStep);
//            double[] currentLocationVector = spaceExplorerModel.getCurrentLocationVector();
//            double distanceToCurrentLocation = Math.sqrt(currentLocationVector[0] * currentLocationVector[0] +
//                    currentLocationVector[1] * currentLocationVector[1]);
            double absDelta = currentDistanceToTheTargetLocation;//Math.abs(distanceToTheTarget - distanceToCurrentLocation);
//              System.out.println("currentMotionStep  " + currentMotionStep);
            if (absDelta < STOP_CRITERIA) {
                return true;
            }
            if (!lowSpeed && absDelta < BREAK_CRITERIA) {
                currentMotionStep = SLOW_MOTION_STEP;
                lowSpeed = true;

            }


        } else if (numberOfStepsToMove != 0) {

            if (stepCounter >= numberOfStepsToMove) {
                return true;
            }
            stepCounter++;
        }

        switch (modelMotionOperation) {
            case OPERATION_NONE:
                break;
            case OPERATION_MOVE_FORWARD:
                spaceExplorerModel.moveModel(currentMotionStep);
                break;
            case OPERATION_MOVE_BACKWARD:
                spaceExplorerModel.moveModel(-currentMotionStep);
                break;
        }

        return false;
    }

    /**
     * Called when either GUI control wants the Mission to stop functioning:
     * Poser Status set Off, Execution Mode changed from Manusl to Auto, or Model Reset
     */
    public void stopOperationOnMissionStoped() {
        if (modelMotionOperation.equals(ModelMotionOperation.OPERATION_NONE)) {
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
     *  fron\m Stop and MCLN
     */
    public void cancelCurrentOperation() {

        if (modelMotionOperation.equals(ModelMotionOperation.OPERATION_NONE)) {
            return;
        }
        ModelOperationRequest returnedModelOperationRequest = stopExecution();

        OperationExecutionListener operationExecutionListener = returnedModelOperationRequest.getOperationExecutionListener();
        if (operationExecutionListener != null) {
            operationExecutionListener.requestExecuted(returnedModelOperationRequest);
        }
    }

    /**
     * @return ModelOperationRequest
     */
    private ModelOperationRequest stopExecution() {
        ModelOperationRequest returnedModelOperationRequest = modelOperationRequest;
        modelOperationRequest = null;
        modelMotionOperation = ModelMotionOperation.OPERATION_NONE;
        targetPositionLocation = new TargetPositionLocation();
        return returnedModelOperationRequest;
    }

}
