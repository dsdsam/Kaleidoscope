package sem.appui.controller;

import sem.infrastructure.OperationRequestStatus;
import sem.mission.controlles.modelcontroller.BasicOperationExecutionController;
import sem.mission.controlles.modelcontroller.actions.ServiceExecutionListener;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Nov 2, 2011
 * Time: 10:32:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicOperationRequest<OT,DT> {

    public static final BasicOperationRequest createBasicOperationRequest(Enum operation, Object argument,
                                                                          ServiceExecutionListener serviceExecutionListener,
                                                                          OperationExecutionListener operationExecutionListener) {
        return new BasicOperationRequest(operation, argument, serviceExecutionListener, operationExecutionListener);
    }

    protected static long instanceCnt;

    private long requestID;
    private OT operation;
    private OT actualOperation;
    private DT argument;
    private BasicOperationExecutionController sourceBasicOperationExecutionController;
    private ServiceExecutionListener serviceExecutionListener;
    OperationExecutionListener operationExecutionListener;

    // response
    private OperationRequestStatus operationRequestStatus = OperationRequestStatus.SUCCESS;
    private boolean executingFromQueue;

    BasicOperationRequest(OT operation, DT argument,
                          ServiceExecutionListener serviceExecutionListener,
                          OperationExecutionListener operationExecutionListener) {
        requestID = ++instanceCnt;
        this.operation = operation;
        this.argument = argument;
        this.serviceExecutionListener = serviceExecutionListener;
        this.operationExecutionListener = operationExecutionListener;
    }

    public BasicOperationExecutionController getSourceBasicOperationExecutionController() {
        return sourceBasicOperationExecutionController;
    }

    public void setSourceBasicOperationExecutionController(BasicOperationExecutionController sourceBasicOperationExecutionController) {
        this.sourceBasicOperationExecutionController = sourceBasicOperationExecutionController;
    }

    public OT getActualOperation() {
        return actualOperation;
    }

    public void setActualOperation(OT actualOperation) {
        this.actualOperation = actualOperation;
    }

    public long getRequestID() {
        return requestID;
    }

    public OT getRequestedOperation() {
        return operation;
    }

    public boolean isArgumentAssigned() {
        return argument != null;
    }

    public  void setArgument(DT argument) {
        this.argument = argument;
    }

    public DT getArgument() {
        return argument;
    }

    public ServiceExecutionListener getServiceExecutionListener() {
        return serviceExecutionListener;
    }

    public OperationExecutionListener getOperationExecutionListener() {
        return operationExecutionListener;
    }


    public OperationRequestStatus getOperationRequestStatus() {
        return operationRequestStatus;
    }

    public boolean isRequestEnqueued() {
        return operationRequestStatus == OperationRequestStatus.ENQUEUED;
    }

    public void setRequestEnqueued() {
        this.operationRequestStatus = OperationRequestStatus.ENQUEUED;
        executingFromQueue = true;
    }

    public boolean isExecutingFromQueue() {
        return executingFromQueue;
    }

    public boolean isRequestExecutedSuccessfully() {
        return operationRequestStatus == OperationRequestStatus.SUCCESS;
    }

    public void setRequestExecutedSuccessfully() {
        this.operationRequestStatus = OperationRequestStatus.SUCCESS;
    }

    public boolean isRequestFailed() {
        return operationRequestStatus == OperationRequestStatus.FAILURE;
    }

    public void setRequestFailed() {
        this.operationRequestStatus = OperationRequestStatus.FAILURE;
    }

    public boolean isTimeout() {
        return operationRequestStatus == OperationRequestStatus.TIMEOUT;
    }

    public void setTimeout() {
        this.operationRequestStatus = OperationRequestStatus.TIMEOUT;
    }

    public boolean isOperationInterrupted() {
        return operationRequestStatus == OperationRequestStatus.INTERRUPTED;
    }

    public void setOperationInterrupted() {
        this.operationRequestStatus = OperationRequestStatus.INTERRUPTED;
    }

    public String toString() {
        return "Operation Request: ID= " + requestID + ", operation = " + operation + ", status = " +
                operationRequestStatus+", executed from queue = "+executingFromQueue;
    }
}
