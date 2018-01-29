package sem.appui.controls;

import sem.appui.ViewRotationControllerListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import sem.appui.controller.MissionOperationRequest;
import sem.appui.controller.OperationExecutionListener;
import com.sun.jmx.snmp.tasks.Task;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 17, 2011
 * Time: 2:07:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewRotationController {


    private static ViewRotationController viewRotationController = new ViewRotationController();

    public static ViewRotationController getInstance() {
        return viewRotationController;
    }

    private final int tickTime = 20;

    private final Executor operationExecutor = Executors.newSingleThreadExecutor();
    private final LinkedBlockingQueue<MissionOperationRequest> linkedBlockingQueue = new LinkedBlockingQueue();

    private ViewRotationControllerListener viewRotationControllerListener;
    private ViewRotationOperation viewRotationOperation;

    private Object synchronizer = new Object();
    private boolean opedationInProgress;
    private MissionOperationRequest missionOperationRequest = null;
    /**
     *
     */
    private final Runnable operationExecutionTask = new Task() {
        public void run() {
            while (true) {
                MissionOperationRequest localMissionOperationRequest = null;
                try {
                    localMissionOperationRequest = linkedBlockingQueue.take();
                } catch (InterruptedException e) {
//                    System.out.println();
                }
                synchronized (synchronizer) {
                    missionOperationRequest = localMissionOperationRequest;
                    ViewRotationOperation viewRotationOperation = (ViewRotationOperation) missionOperationRequest.getRequestedOperation();
                    opedationInProgress = true;
                    setOperation(viewRotationOperation);
                    while (opedationInProgress) {
                        try {
                            synchronizer.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    OperationExecutionListener operationExecutionListener = missionOperationRequest.getOperationExecutionListener();
                    if (operationExecutionListener != null) {
                        operationExecutionListener.requestExecuted(missionOperationRequest);
                    }
                    missionOperationRequest = null;
                }
            }
        }

        public void cancel() {

        }
    };

    /**
     *
     */
    private final Timer tickTimer = new Timer(tickTime, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            switch (viewRotationOperation) {
                case SET_UP_HOME_POSITION:
                    setupHomePosition();
                    break;
                case SET_UP_VANTAGE_POINT_POSITION:
                    setupVantagePointPosition();
                    break;
            }
        }
    });

    /**
     *
     */
    private ViewRotationController() {
        operationExecutor.execute(operationExecutionTask);
    }

    /**
     * @param missionOperationRequest
     */
    public void cancelAndExecuteOperation(MissionOperationRequest missionOperationRequest) {
        synchronized (synchronizer) {
            linkedBlockingQueue.clear();
            if (opedationInProgress) {
                this.missionOperationRequest = missionOperationRequest;
                ViewRotationOperation viewRotationOperation = (ViewRotationOperation) missionOperationRequest.getRequestedOperation();
                setOperation(viewRotationOperation);
            } else {
                executeOperation(missionOperationRequest);
            }
        }
    }

    /**
     * @param missionOperationRequest
     */
    public void executeOperation(MissionOperationRequest missionOperationRequest) {
        try {
            linkedBlockingQueue.put(missionOperationRequest);
        } catch (InterruptedException e) {
            System.out.println("");
        }
    }

    private void setOperation(ViewRotationOperation viewRotationOperation) {
        this.viewRotationOperation = viewRotationOperation;
        switch (viewRotationOperation) {
            case SET_UP_HOME_POSITION:
            case SET_UP_VANTAGE_POINT_POSITION:
                if (!tickTimer.isRunning()) {
                    tickTimer.start();
                }
                break;
            default:
                throw new RuntimeException("Unknown operation: " + viewRotationOperation);
        }
    }

    /**
     *
     */
    private void setupHomePosition() {
        boolean rotationDone = rotate(0, 0);
        if (rotationDone) {

            synchronized (synchronizer) {
                tickTimer.stop();
                opedationInProgress = false;
                synchronizer.notify();
            }
        }
    }

    /**
     *
     */
    private void setupVantagePointPosition() {
        boolean rotationDone = rotate(65, 155);
        if (rotationDone) {
            tickTimer.stop();
            opedationInProgress = false;
            synchronized (synchronizer) {
                synchronizer.notify();
            }
        }
    }

    /**
     * Adds a listener to the list that's notified each tick
     *
     * @param viewRotationControllerListener of the ViewRotationControllerListener
     */
    public synchronized void addViewRotationControllerListener(ViewRotationControllerListener viewRotationControllerListener) {
        this.viewRotationControllerListener = viewRotationControllerListener;
    }

    /**
     * Removes a listener from the list that's notified each tick
     */
    public synchronized void removeViewRotationControllerListener() {
        viewRotationControllerListener = null;
    }

    /**
     *
     *
     */
    private boolean rotate(int verticalTargetValue, int horizontalTargetValue) {
        if (viewRotationControllerListener == null) {
            return true;
        }
        boolean verticalRotationDone = viewRotationControllerListener.onRotateVertilally(verticalTargetValue, 1);
        boolean horizontalRotationDone = viewRotationControllerListener.onRotateHorizontally(horizontalTargetValue, 3);
        return verticalRotationDone && horizontalRotationDone;
    }

}
