package sem.mission.controlles.modelcontroller.actions;

import sem.appui.controls.TwoStateRoundedCornersButton;
import sem.appui.controller.AppUIController;
import sem.appui.controller.BasicOperationRequest;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 28, 2011
 * Time: 9:00:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class SetVantagePpointAction extends BasicAsynchronousAction {

    private TwoStateRoundedCornersButton button;

    public SetVantagePpointAction(TwoStateRoundedCornersButton button) {
        this.button = button;
    }

    /**
     * Checks out if the action allowed. Should be overridden by extending
     * class.
     *
     * @return boolean
     */
    @Override
    boolean isAllowed() {
        return true;
    }

    /**
     * The overriding method should invoke server and provide "this" as a
     * listener for the server response.
     */
    @Override
    void exec() {
        button.setEnabled(false);
        if (button.isInSecondState()) {
            AppUIController.setViewToVantagePointPosition(this);
//            ViewRotationController.getInstance().setOperation(ViewRotationOperation.SET_UP_VANTAGE_POINT_POSITION);
        } else {
            AppUIController.setViewToHomePosition(this);
//            ViewRotationController.getInstance().setOperation(ViewRotationOperation.SET_UP_HOME_POSITION);
        }

    }

    /**
     * Called on server response, when result is successful. Should be
     * overridden by extending class.
     *
     * @param operationRequest
     */
    @Override
    void success(BasicOperationRequest operationRequest) {
          boolean powerIsOn = ((Boolean)operationRequest.getArgument());
//        System.out.println("SetVantagePpointAction  Success, power On "+powerOn);

         button.setEnabled(powerIsOn);
    }

    /**
     * Called on server response, when result is failure. Should be overridden
     * by extending class.
     *
     * @param operationRequest
     */
    @Override
    void failure(BasicOperationRequest operationRequest) {

    }

    /**
     * Called on server response, when timeout expired before response received.
     * Should be overridden by extending class.
     *
     * @param operationRequest
     */
    @Override
    void timeout(BasicOperationRequest operationRequest) {
    }
}
