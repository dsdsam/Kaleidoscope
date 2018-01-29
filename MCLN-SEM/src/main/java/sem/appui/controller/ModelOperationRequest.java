package sem.appui.controller;

import sem.mission.controlles.modelcontroller.actions.ServiceExecutionListener;
import sem.mission.controlles.modelcontroller.ModelMotionOperation;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 13, 2011
 * Time: 8:22:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelOperationRequest<OT,DT> extends BasicOperationRequest<OT,DT> {

    public static final ModelOperationRequest createRequest(ModelMotionOperation operation, Object target,
                                                            ServiceExecutionListener serviceExecutionListener,
                                                            OperationExecutionListener operationExecutionListener) {
        return new ModelOperationRequest(operation, target, serviceExecutionListener, operationExecutionListener);
    }

    /**
     * @param operation
     * @param target
     * @param serviceExecutionListener
     * @param operationExecutionListener
     */
    private ModelOperationRequest(OT operation, DT target, ServiceExecutionListener serviceExecutionListener,
                                  OperationExecutionListener operationExecutionListener) {
        super(operation, target, serviceExecutionListener, operationExecutionListener);
    }

}
