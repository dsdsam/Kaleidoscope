package sem.appui.controller;

import sem.mission.controlles.modelcontroller.actions.ServiceExecutionListener;
import sem.appui.controls.ViewRotationController;
import sem.appui.controls.ViewRotationOperation;
import sem.mission.controlles.modelcontroller.actions.CallbackListener;
import sem.infrastructure.evdistributor.EventDistributionAdapter;
import sem.infrastructure.evdistributor.SemEventDistributor;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 2, 2011
 * Time: 4:03:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppUIController {

    private static ViewRotationController viewRotationController = ViewRotationController.getInstance();

    private static boolean powerOn = false;


    /**
     *
     */
    private static EventDistributionAdapter missionEventDistributionListener = new EventDistributionAdapter<Boolean,
            CallbackListener>() {

        @Override
        public void onGuiStateModelEventFired(SemEventDistributor.EventId eventId, Component comp, Boolean powerStatus,
                                              CallbackListener callbackListener) {
            if (eventId == SemEventDistributor.EventId.POWER) {
                if (powerOn == powerStatus) {
                    return;
                }
                powerOn = powerStatus;
                if (powerOn) {
                    AppUIController.setPowerOn((ServiceExecutionListener) callbackListener);
                } else {
                    AppUIController.setPowerOff((ServiceExecutionListener) callbackListener);
                }
            }
        }
    };

    /**
     * Class initializer
     */
    public static void initialize() {
        SemEventDistributor.addEventDistributionListener(SemEventDistributor.EventGroup.MISSION_EVENT,
                missionEventDistributionListener);
    }

    /**
     *
     */
    private static OperationExecutionListener operationExecutionListener = new OperationExecutionListener() {
        public void requestExecuted(BasicOperationRequest basicOperationRequest) {
            BasicOperationRequest<ViewRotationOperation, Boolean>  missionOperationRequest = basicOperationRequest;
            ServiceExecutionListener serviceExecutionListener = missionOperationRequest.getServiceExecutionListener();
            if (serviceExecutionListener != null) {
                serviceExecutionListener.executionDone(missionOperationRequest);
            }
        }
    };

    private static void setPowerOn(ServiceExecutionListener serviceExecutionListener) {
        if (serviceExecutionListener != null) {
            BasicOperationRequest<ViewRotationOperation, Boolean> basicOperationRequest =
                    BasicOperationRequest.createBasicOperationRequest(ViewRotationOperation.NONE, Boolean.TRUE,
                            null, null);
            serviceExecutionListener.executionDone(basicOperationRequest);
        }
    }

    private static void setPowerOff(ServiceExecutionListener serviceExecutionListener) {
        MissionOperationRequest<ViewRotationOperation, Boolean> missionOperationRequest =
                MissionOperationRequest.createRequest(ViewRotationOperation.SET_UP_HOME_POSITION, Boolean.FALSE,
                        serviceExecutionListener, operationExecutionListener);
        AppUIController.viewRotationController.cancelAndExecuteOperation(missionOperationRequest);
    }

    public static void setViewToVantagePointPosition(ServiceExecutionListener serviceExecutionListener) {
        MissionOperationRequest<ViewRotationOperation, Boolean> missionOperationRequest =
                MissionOperationRequest.createRequest(ViewRotationOperation.SET_UP_VANTAGE_POINT_POSITION, (powerOn ? Boolean.TRUE : Boolean.FALSE),
                        serviceExecutionListener, operationExecutionListener);
        AppUIController.viewRotationController.executeOperation(missionOperationRequest);
    }

    public static void setViewToHomePosition(ServiceExecutionListener serviceExecutionListener) {
        MissionOperationRequest<ViewRotationOperation, Boolean> missionOperationRequest =
                MissionOperationRequest.createRequest(ViewRotationOperation.SET_UP_HOME_POSITION, (powerOn ? Boolean.TRUE : Boolean.FALSE),
                        serviceExecutionListener, operationExecutionListener);
        AppUIController.viewRotationController.executeOperation(missionOperationRequest);
    }

}
