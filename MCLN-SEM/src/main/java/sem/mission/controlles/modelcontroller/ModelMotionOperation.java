package sem.mission.controlles.modelcontroller;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 27, 2011
 * Time: 7:53:43 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ModelMotionOperation {

    OPERATION_NONE,

    OPERATION_MOVE_TO_THE_POINT,

    OPERATION_MOVE_FORWARD_NON_STOP,

    OPERATION_MOVE_FORWARD,
    OPERATION_MOVE_BACKWARD,

    OPERATION_SPIN_TO_THE_POINT,
    OPERATION_ROTATE_TO_THE_ANGLE,
    OPERATION_SPIN_TO_THE_RIGHT,
    OPERATION_SPIN_TO_THE_LEFT,

    OPERATION_LEFT_TRACK_FORWARD,
    OPERATION_RIGHT_TRACK_FORWARD,
    OPERATION_LEFT_TRACK_BACKWARD,
    OPERATION_RIGHT_TRACK_BACKWARD,

    OPERATION_TRANSMISSION;

    public boolean isRotationOperation(ModelMotionOperation operation) {
        switch (this) {
            case OPERATION_SPIN_TO_THE_POINT:
            case OPERATION_ROTATE_TO_THE_ANGLE:
            case OPERATION_SPIN_TO_THE_RIGHT:
            case OPERATION_SPIN_TO_THE_LEFT:

            case OPERATION_LEFT_TRACK_FORWARD:
            case OPERATION_RIGHT_TRACK_FORWARD:
            case OPERATION_LEFT_TRACK_BACKWARD:
            case OPERATION_RIGHT_TRACK_BACKWARD:
                return true;
            default:
                return false;
        }
    }

    public boolean isMoveOperation(ModelMotionOperation operation) {
        switch (this) {
            case OPERATION_MOVE_TO_THE_POINT:
            case OPERATION_MOVE_FORWARD_NON_STOP:
            case OPERATION_MOVE_FORWARD:
            case OPERATION_MOVE_BACKWARD:
                return true;
            default:
                return false;
        }
    }

    public boolean isTransmissionOperation() {
        return this == OPERATION_TRANSMISSION;
    }
}

