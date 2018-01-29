package sem.mission.controlles.modelcontroller.actions;

import sem.appui.controls.TwoStateRoundedCornersButton;
import sem.appui.controls.MoveButtonPanel;
import sem.appui.controller.BasicOperationRequest;
import sem.infrastructure.evdistributor.SemEventDistributor;
import sem.infrastructure.OperationRequestStatus;
import sem.mission.controlles.modelcontroller.ModelMotionOperation;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Mar 16, 2012
 * Time: 7:38:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class MotionButtonAction extends AsynchronousAction {

    private final MoveButtonPanel moveButtonPanel;
    private final TwoStateRoundedCornersButton button;
    private boolean buttonpressed;

    public MotionButtonAction(MoveButtonPanel moveButtonPanel,TwoStateRoundedCornersButton button) {
        this.moveButtonPanel = moveButtonPanel;
        this.button = button;
    }

    /**
     * Checks out if the action allowed to be executed.
     *
     * @return boolean
     */
    @Override
    boolean isAllowed() {
        button.setEnabled(true);
        button.setSecondStateCurrent();
        return true;
    }

    /**
     * Called when action execution is alloed.
     */
    @Override
    void exec() {
        buttonpressed = button.isInSecondState();
//        button.setEnabled(false);
//        Long distanceToTheTargetLocation = new Long(10);
        double[] distanceToTheTargetLocation = new double[]{10};
        SemEventDistributor.distributeGuiStateModelEventToListeners(
                SemEventDistributor.EventGroup.GUI_MOTION_ACTION_EVENT,
                SemEventDistributor.EventId.MOVE_FORWARD,
                button, distanceToTheTargetLocation, this);
    }

    /**
     * Callback methof that is called when action executed successfully.
     *
     * @param operationRequest
     */
    @Override
    void success(BasicOperationRequest operationRequest) {
         OperationRequestStatus operationRequestStatus = operationRequest.getOperationRequestStatus();
            ModelMotionOperation requestedOperation = (ModelMotionOperation) operationRequest.getRequestedOperation();
            System.out.println("MoveButtonPanel: Operation " + requestedOperation + " Done with status = " + operationRequestStatus);

//            cancelAllOperation();

//        button.setFirstStateCurrent();
    }

    /**
     * Callback methof that is called when action executed interrupted.
     *
     * @param operationRequest
     */
    @Override
    void interrupted(BasicOperationRequest operationRequest) {
//        MclnSimulator.getInstance().processPowerOnOff(powerOn);
//        ModelController.getInstance().onIgnition(currentOperationIsStart);
        button.setEnabled(true);
//        System.out.println("ActionPowerOnOff  Success, power On " + powerOn);
    }

    /**
     * Callback methof that is called when action execution failed.
     *
     * @param operationRequest
     */
    @Override
    void failure(BasicOperationRequest operationRequest) {

    }

    /**
     * Callback methof that is called when action not execution withing given time.
     *
     * @param operationRequest
     */
    @Override
    void timeout(BasicOperationRequest operationRequest) {
    }
}
