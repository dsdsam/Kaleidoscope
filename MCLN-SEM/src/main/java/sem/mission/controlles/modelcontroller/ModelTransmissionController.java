package sem.mission.controlles.modelcontroller;

import sem.mission.explorer.model.SpaceExplorerModel;
import sem.appui.controller.ModelOperationRequest;
import sem.appui.controller.OperationExecutionListener;
import sem.appui.controls.ModelOperationRequestStateChangeListener;
import sem.appui.MissionTransmissionProvider;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Apr 21, 2013
 * Time: 10:21:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class ModelTransmissionController extends BasicOperationExecutionController {

    private final SpaceExplorerModel spaceExplorerModel;

    private ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest;
    private ModelMotionOperation modelTransmissionOperation = ModelMotionOperation.OPERATION_NONE;

    ModelTransmissionController(SpaceExplorerModel spaceExplorerModel) {
        this.spaceExplorerModel = spaceExplorerModel;
    }

    public void executeOperation(ModelOperationRequest<ModelMotionOperation, double[]> modelOperationRequest) {
        this.modelOperationRequest = modelOperationRequest;
        modelOperationRequest.setSourceBasicOperationExecutionController(this);
        ModelMotionOperation requestedOperation = modelOperationRequest.getRequestedOperation();
        modelTransmissionOperation = requestedOperation;

        MissionTransmissionProvider missionTransmissionProvider = MissionTransmissionProvider.getInstance();
        ModelOperationRequestStateChangeListener modelOperationRequestStateChangeListener =
                missionTransmissionProvider.getModelOperationRequestStateChangeListener();
        modelOperationRequestStateChangeListener.executionStarted(modelOperationRequest);
//        ModelController modelController = ModelController.getInstance();
//        modelController.addModelOperationRequestStateChangeListener(modelOperationRequestStateChangeListener);
        modelOperationRequest.setActualOperation(modelTransmissionOperation);
        requestExecutedSuccessfully = false;
    }

    private boolean requestExecutedSuccessfully = false;
    public boolean executeStep() {
        if (modelTransmissionOperation.equals(ModelMotionOperation.OPERATION_NONE)) {
            return true;
        }
//        System.out.println("Model Transmissiom Controller: executeStep");
        boolean requestExecuted = requestExecutedSuccessfully;
        if (requestExecuted) {
            requestExecutedSuccessfully = false;
            ModelOperationRequest returnedModelOperationRequest = modelOperationRequest;
            modelOperationRequest = null;
            modelTransmissionOperation = ModelMotionOperation.OPERATION_NONE;

            OperationExecutionListener operationExecutionListener = returnedModelOperationRequest.getOperationExecutionListener();
            if (operationExecutionListener != null) {
                returnedModelOperationRequest.setRequestExecutedSuccessfully();
                returnedModelOperationRequest.setActualOperation(ModelMotionOperation.OPERATION_NONE);
                operationExecutionListener.requestExecuted(returnedModelOperationRequest);
            }
        }
        return requestExecuted;
    }

    public boolean isOperationBeingExecuted() {
        return modelTransmissionOperation != ModelMotionOperation.OPERATION_NONE;
    }

    public void cancelCurrentOperation() {

    }

    public void stopOperationOnMissionStoped() {

    }

    private boolean transmitTelemetryPackage() {
        return true;
    }

    public void setRequestExecutedSuccessfully(){
        requestExecutedSuccessfully = true;
    }
}
