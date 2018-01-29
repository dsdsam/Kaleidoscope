package sem.appui.controller;

import sem.mission.controlles.modelcontroller.actions.ServiceExecutionListener;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 2, 2011
 * Time: 4:15:05 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MissionOperationRequest<OT,DT> extends BasicOperationRequest<OT,DT> {


    public static final MissionOperationRequest createRequest(Enum operation, Object argument,
                                                              ServiceExecutionListener serviceExecutionListener,
                                                              OperationExecutionListener operationExecutionListener) {
        return new MissionOperationRequest(operation, argument, serviceExecutionListener, operationExecutionListener);
    }


    /**
     * @param operation
     * @param serviceExecutionListener
     * @param operationExecutionListener
     */
    private MissionOperationRequest(OT operation, DT argument, ServiceExecutionListener serviceExecutionListener,
                                    OperationExecutionListener operationExecutionListener) {
        super(operation, argument, serviceExecutionListener, operationExecutionListener);
    }

}
