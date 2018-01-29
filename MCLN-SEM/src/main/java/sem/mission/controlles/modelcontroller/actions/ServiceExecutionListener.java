package sem.mission.controlles.modelcontroller.actions;

import sem.appui.controller.BasicOperationRequest;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 28, 2011
 * Time: 8:53:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ServiceExecutionListener extends CallbackListener {
    /**
     *
     * @param operationRequest
     */
    public void executionDone( BasicOperationRequest<?,?> operationRequest );
}
