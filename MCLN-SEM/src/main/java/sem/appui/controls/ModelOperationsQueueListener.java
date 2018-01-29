package sem.appui.controls;

import sem.mission.controlles.modelcontroller.ModelMotionOperation;
import sem.appui.controller.ModelOperationRequest;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Dec 22, 2011
 * Time: 12:24:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ModelOperationsQueueListener {

    public void modelOperationRequestEnqueued(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                              ModelOperationRequest[] modelOperationRequestsInTheQueue);

    public void modelOperationRequestDequeued(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                              ModelOperationRequest[] modelOperationRequestsInTheQueue);

    public void modelOperationRequestInterrupted(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                                 ModelOperationRequest[] modelOperationRequestsInTheQueue);

    public void modelOperationRequestExecuted(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest,
                                              ModelOperationRequest[] modelOperationRequestsInTheQueue);

    public void modelOperationsQueuePowerOn(boolean status, ModelOperationRequest[] modelOperationRequestsInTheQueue);

    public void modelOperationsQueueStatusOn(boolean status, ModelOperationRequest[] modelOperationRequestsInTheQueue);

    public void modelOperationsQueueCleard(ModelOperationRequest[] modelOperationRequestsInTheQueue);

}
