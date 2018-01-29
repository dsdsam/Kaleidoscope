package sem.infrastructure.evdistributor;

import sem.mission.controlles.modelcontroller.actions.CallbackListener;
import sem.mission.controlles.modelcontroller.actions.ServiceExecutionListener;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Mar 6, 2012
 * Time: 7:50:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class SemEventDistributor extends EventDistributionSupport {

    //   E v e n t   G r o u p s

    public static enum EventGroup {
        MISSION_EVENT("Mission Event"),
        GUI_MOTION_ACTION_EVENT("Gui Motion Action Event");

        private final String id;
        private EventGroup(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
    }

    //   E v e n t s

    public static enum EventId {

        // Switch State Action Event
        POWER("Pover"),
        MANUAL_CONTROL("Manual Control"),
        PAUSE("Pause"),
        RESET_TO_INITIAL_POSITION("Reset Model"),
        MODEL_OPERATIONS_QUEUE_BRFORE_STATUS_CHANGE("Model Operations Queue Before State Change"),
        MODEL_OPERATIONS_QUEUE_AFTER_STATUS_CHANGE("Model Operations Queue After State Change"),

        // Motion Action Event
        CANCEL_OPERATION_EXECUTION("Cancel Operation Execution"),
        MOVE_FORWARD("Move Forward"),
        MOVE_BACKWARD("Move Backward"),
        OPERATION_MOVE_TO_THE_POINT("Move To The Point"),
        OPERATION_MOVE_FORWARD_NON_STOP("Move Foeward None Stop"),

        OPERATION_SPIN_TO_THE_POINT("Spin To The Point"),
        OPERATION_ROTATE_TO_THE_ANGLE("Rotate To The Angle"),
        OPERATION_SPIN_TO_THE_RIGHT("Spin To The Right"),
        OPERATION_SPIN_TO_THE_LEFT("Spin To The Left"),

        OPERATION_LEFT_TRACK_FORWARD("Left Track Moves Foeward"),
        OPERATION_RIGHT_TRACK_FORWARD("Left Track Moves Foeward"),
        OPERATION_LEFT_TRACK_BACKWARD("Left Track Moves Backward"),
        OPERATION_RIGHT_TRACK_BACKWARD("Left Track Moves Backward");

        private final String id;
        private EventId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
    }

    //   E v e n t   D i s t r i b u t  i o n   A P I

    public static void setPower(Component source, boolean status, CallbackListener callbackListener) {
        EventDistributionSupport.distributeEventsToListeners(EventGroup.MISSION_EVENT,
                SemEventDistributor.EventId.POWER, source, status, callbackListener);
    }

    public static void setManualControl(Component source, boolean status, CallbackListener callbackListener) {
        EventDistributionSupport.distributeEventsToListeners(EventGroup.MISSION_EVENT,
                SemEventDistributor.EventId.MANUAL_CONTROL, source, status, callbackListener);
    }

    public static void executeResetModel(Component source, boolean status, CallbackListener callbackListener) {
        EventDistributionSupport.distributeEventsToListeners(EventGroup.MISSION_EVENT,
                SemEventDistributor.EventId.RESET_TO_INITIAL_POSITION, source, status, callbackListener);
    }

    public static void setPause(Component source, boolean status, CallbackListener callbackListener) {
        EventDistributionSupport.distributeEventsToListeners(EventGroup.MISSION_EVENT,
                SemEventDistributor.EventId.PAUSE, source, status, callbackListener);
    }

    public static void distributeGuiStateModelEventToListeners(SemEventDistributor.EventGroup eventGroup,
                                                               SemEventDistributor.EventId eventId,
                                                               Component source, Object arguments,
                                                               ServiceExecutionListener serviceExecutionListener) {
        EventDistributionSupport.distributeEventsToListeners(eventGroup, eventId, source, arguments,
                serviceExecutionListener);
    }
}
