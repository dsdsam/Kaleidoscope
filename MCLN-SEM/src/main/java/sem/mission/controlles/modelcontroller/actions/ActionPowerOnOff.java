package sem.mission.controlles.modelcontroller.actions;

import adf.app.AdfEnv;
import sem.appui.MissionControlCenterPanel;
import sem.appui.controller.BasicOperationRequest;
import sem.appui.controls.TwoStateRoundedCornersButton;
import sem.appui.splash.Splash;
import sem.infrastructure.evdistributor.SemEventDistributor;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 1, 2011
 * Time: 7:08:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionPowerOnOff extends BasicAsynchronousAction {

    private final TwoStateRoundedCornersButton button;
    private final MissionControlCenterPanel missionControlCenterPanel;

    public ActionPowerOnOff(MissionControlCenterPanel missionControlCenterPanel, TwoStateRoundedCornersButton button) {
        this.missionControlCenterPanel = missionControlCenterPanel;
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
        boolean powerOn = button.isInSecondState();
        button.setEnabled(false);
        if (powerOn) {
            Splash splash = (Splash) AdfEnv.get(AdfEnv.SPLASH_WINDOW_KEY);
            if (splash != null) {
                splash.closeSplash();
            }
        }
        SemEventDistributor.setPower(button, (powerOn ? Boolean.TRUE : Boolean.FALSE), this);
    }

    /**
     * Called on server response, when result is successful. Should be
     * overridden by extending class.
     *
     * @param operationRequest
     */
    @Override
    void success(BasicOperationRequest operationRequest) {
        button.setEnabled(true);
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
