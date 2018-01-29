package sem.mission.controlles.modelcontroller;


import sem.mission.explorer.model.SpaceExplorerModel;
import sem.appui.controller.ModelOperationRequest;
import sem.appui.controller.OperationExecutionListener;
import sem.utils.math.MathUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 13, 2011
 * Time: 8:15:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelRotationControllerFirstVersion {

    private final int TURN_ANGLE = 10;

    private final int tickTime = 15;

    private final Executor operationExecutor = Executors.newSingleThreadExecutor();
    private final LinkedBlockingQueue<ModelOperationRequest> linkedBlockingQueue = new LinkedBlockingQueue();

//    private ViewRotationControllerListener viewRotationControllerListener;

    private final SpaceExplorerModel spaceExplorerModel;

//    private ModelRotatioOperation modelRotationOperation;
    private long targetAngle;

    private Object synchronizer = new Object();
    private boolean opedationInProgress;
    private ModelOperationRequest<ModelMotionOperation, Double> operationRequest = null;
    /**
     *
     */
    private final Runnable operationExecutionTask = new Runnable() {
        public void run() {
            while (true) {
                ModelOperationRequest localOperationRequest = null;
                try {
                    localOperationRequest = linkedBlockingQueue.take();
                } catch (InterruptedException e) {
//                    System.out.println();
                }
                synchronized (synchronizer) {
                    operationRequest = localOperationRequest;
//                    ModelRotatioOperation modelRotationOperation = (ModelRotatioOperation) operationRequest.getRequestedOperation();
                    double angle = operationRequest.getArgument().doubleValue();
                    opedationInProgress = true;
                    startOperation(angle);
                    while (opedationInProgress) {
                        try {
                            synchronizer.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    OperationExecutionListener operationExecutionListener = operationRequest.getOperationExecutionListener();
                    if (operationExecutionListener != null) {
                        operationRequest.setRequestExecutedSuccessfully();
                        operationExecutionListener.requestExecuted(operationRequest);
                    }
                    operationRequest = null;
                }
            }
        }

        public void cancel() {

        }
    };

    /**
     *
     */
//    private final Timer tickTimer = new Timer(tickTime, new ActionListener() {
//        public void executeMenuOperation(ActionEvent e) {
////            switch (modelRotationOperation) {
////                case OPERATION_LEFT_ROTATION:
////                case OPERATION_RIGHT_ROTATION:
////                    boolean rotationDone = rotate();
////                    if (rotationDone) {
////                        synchronized (synchronizer) {
////                            tickTimer.stop();
////                            opedationInProgress = false;
////                            synchronizer.notify();
////                        }
////                        TargetLocation targetLocation = new TargetLocation(20, 20);
//////                        ModelController.getInstance().onMotionTargetAssigned(ModelMotionOperation.OPERATION_MOVE_FORWARD, (double)20, null);
////                    }
////                    break;
////            }
//        }
//    });

    /**
     *
     */
    ModelRotationControllerFirstVersion(SpaceExplorerModel spaceExplorerModel) {
        this.spaceExplorerModel = spaceExplorerModel;
        operationExecutor.execute(operationExecutionTask);
    }

    /**
     * @param modelOperationRequest
     */
    public void executeOperation(ModelOperationRequest<ModelMotionOperation, Double> modelOperationRequest) {
        try {
            linkedBlockingQueue.put(modelOperationRequest);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException " + e.toString());
        }
    }

    public boolean executeStep() {
        return true;
    }

    public void stopOperation() {
        synchronized (synchronizer) {
//            tickTimer.stop();
            opedationInProgress = false;
            targetAngle = 0;
            linkedBlockingQueue.clear();
            synchronizer.notify();
        }
    }

    public long getTargetAngle() {
        return targetAngle;
    }

//    /**
//     * @param operationRequest
//     */
//    public void cancelAndExecuteOperation(MissionOperationRequest operationRequest) {
//        synchronized (synchronizer) {
//            linkedBlockingQueue.clear();
//            if (opedationInProgress) {
//                this.operationRequest = operationRequest;
//                ViewRotationOperation viewRotationOperation = (ViewRotationOperation) operationRequest.getRequestedOperation();
//                startOperation(viewRotationOperation);
//            } else {
//                executeOperation(operationRequest);
//            }
//        }
//    }


    private void startOperation(double angle) {

        this.targetAngle = MathUtils.doubleToLongWith2Dps(angle);
        double currentDirectionalAngle = spaceExplorerModel.getDirection();
        double delta = (angle - currentDirectionalAngle);
//        if (Math.abs(delta) < 180) {
//            if (delta > 0) {
//                modelRotationOperation = ModelRotatioOperation.OPERATION_LEFT_ROTATION;
//            } else {
//                modelRotationOperation = ModelRotatioOperation.OPERATION_RIGHT_ROTATION;
//            }
//        } else {
//            if (delta > 0) {
//                modelRotationOperation = ModelRotatioOperation.OPERATION_RIGHT_ROTATION;
//            } else {
//                modelRotationOperation = ModelRotatioOperation.OPERATION_LEFT_ROTATION;
//            }
//        }
//        if (!tickTimer.isRunning()) {
//            tickTimer.start();
//        }
    }

//    /**
//     * Adds a listener to the list that's notified each tick
//     *
//     * @param viewRotationControllerListener of the ViewRotationControllerListener
//     */
//    public synchronized void addViewRotationControllerListener(ViewRotationControllerListener viewRotationControllerListener) {
//        this.viewRotationControllerListener = viewRotationControllerListener;
//    }
//
//    /**
//     * Removes a listener from the list that's notified each tick
//     */
//    public synchronized void removeViewRotationControllerListener() {
//        viewRotationControllerListener = null;
//    }

    /**
     *
     *
     */
    private synchronized boolean rotate() {
//        System.out.println("Rotate to the angle, target angle = " + targetAngle + ", current angle = " + spaceExplorerModel.getLongDirection());
        if ((Math.abs((spaceExplorerModel.getLongDirection()) - Math.abs(targetAngle))) == 0) {
//            System.out.println("Rotate to the angle " + spaceExplorerModel.getDirection() + "=" + targetAngle);
            return true;
        }
//        if ((Math.abs((spaceExplorerModel.getDirection()) - Math.abs(targetAngle))) < 0.5) {
//            System.out.println("Rotate to the angle " + spaceExplorerModel.getDirection() + "=" + targetAngle);
//            return true;
//        }
//        switch (modelRotationOperation) {
//            case OPERATION_LEFT_ROTATION:
//                spaceExplorerModel.rotateModel(TURN_ANGLE);
//                break;
//            case OPERATION_RIGHT_ROTATION:
//                spaceExplorerModel.rotateModel(-TURN_ANGLE);
//                break;
//        }
        if ((Math.abs((spaceExplorerModel.getLongDirection()) - Math.abs(targetAngle))) == 0) {
//            System.out.println("Rotate to the angle " + spaceExplorerModel.getDirection() + "=" + targetAngle);
        }

        return false;
//        if (viewRotationControllerListener == null) {
//            return true;
//        }
//        boolean verticalRotationDone = viewRotationControllerListener.onRotateVertilally(verticalTargetValue, 1);
//        boolean horizontalRotationDone = viewRotationControllerListener.onRotateHorizontally(horizontalTargetValue, 3);
//        return verticalRotationDone && horizontalRotationDone;
    }
}
