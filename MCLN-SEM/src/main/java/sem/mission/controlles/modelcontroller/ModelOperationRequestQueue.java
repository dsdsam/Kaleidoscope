package sem.mission.controlles.modelcontroller;

import com.sun.jmx.snmp.tasks.Task;
import sem.appui.controls.ModelOperationsQueueListener;
import sem.appui.controller.ModelOperationRequest;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Nov 27, 2011
 * Time: 6:38:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelOperationRequestQueue {

    private static class MySemaphore extends Semaphore {

        public MySemaphore(int permits, boolean fair) {
            super(permits, fair);
        }

        public Collection<Thread> getQueuedThreads() {
            return super.getQueuedThreads();
        }
    }

    private ModelOperationRequest[] EMPTY_MODEL_OPERATION_REQUESTS = new ModelOperationRequest[0];

    private final Executor operationExecutor = Executors.newSingleThreadExecutor();
    private final LinkedBlockingQueue<ModelOperationRequest> linkedBlockingQueue = new LinkedBlockingQueue();
    private final MySemaphore available = new MySemaphore(1, true);
//    private final Semaphore blocker = new Semaphore(1, true);

    private Object synchronizer = new Object();
    private boolean opedationInProgress;
    private ModelOperationRequest<ModelMotionOperation, double[]> operationRequest = null;
    private final List<ModelOperationsQueueListener> modelRequestQueueListeners = new ArrayList();

    private ModelController modelController;
//    ModelOperationRequest dequeuedOperationRequest = null;

    private boolean powerOn = false;

    private boolean queueOn = true;

    private boolean engaged = powerOn & queueOn;


    private final Runnable operationExecutionTask = new Task() {
        public void run() {
            while (true) {

//                ModelOperationRequest dequeuedOperationRequest = null;
                try {

//                    System.out.println("Ready 1 to take from Queue: sym avail = " + available.availablePermits());
                    available.acquire();
//                    blocker.release();

//                    System.out.println("Ready 2 to take from Queue: sym avail = " + available.availablePermits());
                    ModelOperationRequest dequeuedOperationRequest = linkedBlockingQueue.take();
                    synchronized (synchronizer) {
                        /*
                           If power is On and queue is On => start execution.
                           If while thread was waiting permission and request power was set Off =>
                           discard request and permit waiting next request.
                        */
                        if (isEngaged()) {
                            operationRequest = dequeuedOperationRequest;
                            OperationExecutor operationExecutor = new OperationExecutor(operationRequest);
                            SwingUtilities.invokeLater(operationExecutor);

                        } else if (available.availablePermits() == 0) {
                            available.release();
                        }
                    }
//                    System.out.println("Ready 3 to take from Queue: sym avail = " + available.availablePermits());

//                    blocker.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                System.out.println("ModelOperationRequestQueue.Task:  dequeuedOperationRequest " + dequeuedOperationRequest.toString() + ", queue length = " + linkedBlockingQueue.toArray().length);

//                final ModelOperationRequest argument = dequeuedOperationRequest;

//                Runnable runnable = new Runnable() {
//                    public void run() {
//                        if (!isEngaged()) {
//                            return;
//                        }
//
//                        operationRequest = dequeuedOperationRequest;
//                        System.out.println("Exec ModelOperationRequestQueue.Task:  dequeuedOperationRequest " + dequeuedOperationRequest.toString() + ", queue length = " + linkedBlockingQueue.toArray().length);
////                        operationRequest = dequeuedOperationRequest;
//                        ModelOperationRequest argument = dequeuedOperationRequest;
//                        modelController.processMotionOperationRequest(argument);
//
//                        ModelOperationRequest[] modelOperationRequests = getQueueContent();
//                        fireModelOperationRequestDequeued(argument, modelOperationRequests);
//                    }
//                };

//                if (SwingUtilities.isEventDispatchThread()) {
//                    runnable.run();
//                } else {
//                    SwingUtilities.invokeLater(runnable);
//                }
//                blocker.release();
            }

        }

        public void cancel() {

        }
    };

    /**
     * Executes Operation in EDT
     */
    private class OperationExecutor implements Runnable {

        private ModelOperationRequest operationRequest;

        private OperationExecutor(ModelOperationRequest operationRequest) {
            this.operationRequest = operationRequest;
        }

        public void run() {
            synchronized (synchronizer) {
                /*
                  If after request was placed into EDT either power is Off or queue is Off => do nothing
                  because while resetting power or queue status request was cleaned up.
                  Otherwise start executing request. Nothing bad will happen while we are in synchronized
                  block.
                */
                if (!isEngaged()) {
                    return;
                }

//                System.out.println("Exec ModelOperationRequestQueue.Task:  dequeuedOperationRequest " + operationRequest.toString() + ", queue length = " + linkedBlockingQueue.toArray().length);
//                        operationRequest = dequeuedOperationRequest;
                ModelOperationRequest argument = operationRequest;
                modelController.processMotionOperationRequest(argument);

                ModelOperationRequest[] modelOperationRequests = getQueueContent();
                fireModelOperationRequestDequeued(argument, modelOperationRequests);
            }
        }
    }


    /**
     * M o d e l   O p e r a t i o n   R e q u e s t   Q u e u e
     *
     * @param modelController
     */
    ModelOperationRequestQueue(ModelController modelController) {
        this.modelController = modelController;
        operationExecutor.execute(operationExecutionTask);
        modelOperationRequestQueue = this;
    }

    static ModelOperationRequestQueue modelOperationRequestQueue;

    public static ModelOperationRequestQueue getModelOperationRequestQueue() {
        return modelOperationRequestQueue;
    }

    public void checkIfQueueOK() {
        if (available.availablePermits() == 0) {
            System.out.println();
            available.release();
        }
    }

    /**
     * @return true when power is On amd queue is On
     */
    private boolean isEngaged() {
        return engaged;
    }

    /**
     * @return power status
     */
    public boolean isPowerOn() {
        return powerOn;
    }

    /**
     * @return true when queue is On
     */
    public boolean isQueueOn() {
        return queueOn;
    }

    /**
     * @return true when empty
     */
    public boolean isEmpty() {
        return linkedBlockingQueue.isEmpty();
    }

    /**
     * @return number of enqueued elements
     */
    public int getQueueSize() {
        return linkedBlockingQueue.size();
    }

    /**
     * @param status
     */
    public void setQueuePowerOn(boolean status) {

        synchronized (synchronizer) {

            this.powerOn = status;
            engaged = powerOn & queueOn;
            if (!powerOn) {
                clearQueue();
                if (operationRequest != null) {
                    interruptRequestExecution();
                }
            }
            ModelOperationRequest[] modelOperationRequests = getQueueContent();
            fireModelOperationsQueuePowerOn(powerOn, modelOperationRequests);
        }
    }

    /**
     * @param status
     */
    public void setQueueOn(boolean status) {

        synchronized (synchronizer) {

            this.queueOn = status;
            engaged = powerOn & queueOn;
            if (!queueOn) {
                clearQueue();
                if (operationRequest != null) {
                    interruptRequestExecution();
                }
            }
            ModelOperationRequest[] modelOperationRequests = getQueueContent();
            fireModelOperationsQueueOn(queueOn, modelOperationRequests);
        }
    }

    /**
     *
     */
    void resetQueue() {
        synchronized (synchronizer) {
            if (queueOn) {
                clearQueue();
                if (operationRequest != null) {
                    interruptRequestExecution();
                }
            }
            ModelOperationRequest[] modelOperationRequests = getQueueContent();
            fireModelOperationsQueueOn(queueOn, modelOperationRequests);
        }
    }

    /**
     *
     */
    public void discardEnqueuedElements() {
        if (!queueOn) {
            return;
        }
        clearQueue();
    }

    /**
     * @param modelOperationsQueueListener
     */
    public void addModelOperationRequestQueueListener(ModelOperationsQueueListener modelOperationsQueueListener) {
        modelRequestQueueListeners.add(modelOperationsQueueListener);
    }

    /**
     * @param modelOperationRequest
     */
    public void enqueueModelMotionOperationRequest(ModelOperationRequest<ModelMotionOperation, double[]>
                                                           modelOperationRequest) {
        try {
//            System.out.println("\n\n 1. ModelOperationRequestQueue.enqueueModelMotionOperationRequest: operation = " +
//                    modelOperationRequest.getRequestedOperation() +
//                    ", enqueued,   status = " + modelOperationRequest.getOperationRequestStatus() +
//                    " , Service listener = " + modelOperationRequest.getServiceExecutionListener() +
//                    ",  q-size = " + linkedBlockingQueue.toArray().length);

            linkedBlockingQueue.put(modelOperationRequest);

//            System.out.println("2. ModelOperationRequestQueue.enqueueModelMotionOperationRequest: operation = " +
//                    modelOperationRequest.getRequestedOperation() +
//                    ", enqueued,  status = " + modelOperationRequest.getOperationRequestStatus() +
//                    " , Service listener = " + modelOperationRequest.getServiceExecutionListener() +
//                    ",  q-size = " + linkedBlockingQueue.toArray().length);

            ModelOperationRequest[] modelOperationRequests = getQueueContent();
            fireModelOperationRequestEnqueued(modelOperationRequest, modelOperationRequests);

        } catch (InterruptedException e) {
            System.out.println("InterruptedException " + e.toString());
        }
    }

//     public void enqueueModelOperationRequest(ModelOperationRequest<OperationTransmission, double[]>
//            modelOperationRequest) {
//        try {
////            System.out.println("\n\n 1. ModelOperationRequestQueue.enqueueModelMotionOperationRequest: operation = " +
////                    modelOperationRequest.getRequestedOperation() +
////                    ", enqueued,   status = " + modelOperationRequest.getOperationRequestStatus() +
////                    " , Service listener = " + modelOperationRequest.getServiceExecutionListener() +
////                    ",  q-size = " + linkedBlockingQueue.toArray().length);
//
//            linkedBlockingQueue.put(modelOperationRequest);
//
////            System.out.println("2. ModelOperationRequestQueue.enqueueModelMotionOperationRequest: operation = " +
////                    modelOperationRequest.getRequestedOperation() +
////                    ", enqueued,  status = " + modelOperationRequest.getOperationRequestStatus() +
////                    " , Service listener = " + modelOperationRequest.getServiceExecutionListener() +
////                    ",  q-size = " + linkedBlockingQueue.toArray().length);
//
//            ModelOperationRequest[] modelOperationRequests = getQueueContent();
//            fireModelOperationRequestEnqueued(modelOperationRequest, modelOperationRequests);
//
//        } catch (InterruptedException e) {
//            System.out.println("InterruptedException " + e.toString());
//        }
//    }

    /**
     *
     */
    public void requestExecuted() {
//        System.out.println("requestExecuted 0 to take from Queue: sym avail = " + available.availablePermits() + "  request exists " + (operationRequest != null));
//        Thread.dumpStack();
        synchronized (synchronizer) {
            if (operationRequest != null) {
//            System.out.println("ModelOperationRequestQueue.requestExecuted: operation = " +
//                    operationRequest.getRequestedOperation() +
//                    " accomplished with status = " + operationRequest.getOperationRequestStatus() +
//                    " , Service listener = " + operationRequest.getServiceExecutionListener());
                ModelOperationRequest[] modelOperationRequests = getQueueContent();
                fireModelOperationRequestExecuted(operationRequest, modelOperationRequests);

                clearRequest("requestExecuted");

                if (available.availablePermits() == 0) {
//                    System.out.println("requestExecuted 1 to take from Queue: sym avail = " + available.availablePermits());
                    available.release();
//                    System.out.println("requestExecuted 2 to take from Queue: sym avail = " + available.availablePermits());
                }
            }
        }
    }

    /**
     *
     */
    public void requestExecutionInterrupted() {
        if (operationRequest == null) {
            return;
        }
        ModelOperationRequest[] modelOperationRequests = getQueueContent();
        fireModelOperationRequestExecuted(operationRequest, modelOperationRequests);

        clearRequest("requestExecutionInterrupted");

        if (available.availablePermits() == 0) {
            available.release();
        }
    }

    /**
     *
     */
    private void interruptRequestExecution() {

        ModelOperationRequest[] modelOperationRequests = getQueueContent();
        fireModelOperationRequestExecuted(operationRequest, modelOperationRequests);

        clearRequest("interruptRequestExecution");

        if (available.availablePermits() == 0) {
            available.release();
        }
    }

    /**
     * @param from
     */
    private void clearRequest(String from) {
        operationRequest = null;
    }

    /**
     *
     */
    private void clearQueue() {
        synchronized (synchronizer) {
            linkedBlockingQueue.clear();
            ModelOperationRequest[] modelOperationRequests = getQueueContent();
            fireModelOperationRequestQueueCleard(modelOperationRequests);
        }
    }


    private ModelOperationRequest[] getQueueContent() {
        return linkedBlockingQueue.toArray(EMPTY_MODEL_OPERATION_REQUESTS);
    }


    private void fireModelOperationRequestEnqueued(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                                   ModelOperationRequest[] modelOperationRequestsInTheQueue) {
        for (ModelOperationsQueueListener modelOperationsQueueListener : modelRequestQueueListeners) {
            modelOperationsQueueListener.modelOperationRequestEnqueued(modelOperationRequest,
                    modelOperationRequestsInTheQueue);
        }
    }

    private void fireModelOperationRequestDequeued(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                                   ModelOperationRequest[] modelOperationRequestsInTheQueue) {
        for (ModelOperationsQueueListener modelOperationsQueueListener : modelRequestQueueListeners) {
            modelOperationsQueueListener.modelOperationRequestDequeued(modelOperationRequest,
                    modelOperationRequestsInTheQueue);
        }
    }

    private void fireModelOperationRequestInterrupted(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                                      ModelOperationRequest[] modelOperationRequestsInTheQueue) {
        for (ModelOperationsQueueListener modelOperationsQueueListener : modelRequestQueueListeners) {
            modelOperationsQueueListener.modelOperationRequestInterrupted(modelOperationRequest,
                    modelOperationRequestsInTheQueue);
        }
    }

    private void fireModelOperationRequestExecuted(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                                   ModelOperationRequest[] modelOperationRequestsInTheQueue) {
        for (ModelOperationsQueueListener modelOperationsQueueListener : modelRequestQueueListeners) {
            modelOperationsQueueListener.modelOperationRequestExecuted(modelOperationRequest,
                    modelOperationRequestsInTheQueue);
        }
    }

    private void fireModelOperationsQueuePowerOn(boolean status, ModelOperationRequest[] modelOperationRequestsInTheQueue) {
        for (ModelOperationsQueueListener modelOperationsQueueListener : modelRequestQueueListeners) {
            modelOperationsQueueListener.modelOperationsQueuePowerOn(status, modelOperationRequestsInTheQueue);
        }
    }

    private void fireModelOperationsQueueOn(boolean status, ModelOperationRequest[] modelOperationRequestsInTheQueue) {
        for (ModelOperationsQueueListener modelOperationsQueueListener : modelRequestQueueListeners) {
            modelOperationsQueueListener.modelOperationsQueueStatusOn(status, modelOperationRequestsInTheQueue);
        }
    }

    private void fireModelOperationRequestQueueCleard(ModelOperationRequest[] modelOperationRequestsInTheQueue) {
        for (ModelOperationsQueueListener modelOperationsQueueListener : modelRequestQueueListeners) {
            modelOperationsQueueListener.modelOperationsQueueCleard(modelOperationRequestsInTheQueue);
        }
    }
}
