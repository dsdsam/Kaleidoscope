package sem.appui.controls;

import sem.appui.controller.BasicOperationRequest;
import sem.mission.controlles.modelcontroller.ModelMotionOperation;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Dec 25, 2011
 * Time: 1:03:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ModelOperationRequestStateChangeListener {

    public void executionStarted(BasicOperationRequest<ModelMotionOperation, double[]> modelOperationRequest);

    public void executionCompleted(BasicOperationRequest<ModelMotionOperation, double[]> modelOperationRequest);

}
