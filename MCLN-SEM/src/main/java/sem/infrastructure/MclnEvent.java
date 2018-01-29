/**
 *
 */
package sem.infrastructure;

/**
 * @author Administrator
 */
public class MclnEvent {

    public static final int FRONT_EOS_SENSOR_ON = 1;
    public static final int FRONT_EOS_SENSOR_OFF = 2;
    public static final int REAR_EOS_SENSOR_ON = 3;
    public static final int REAR_EOS_SENSOR_OFF = 4;

    private int eventType;
    private String sourceId = "";

    public MclnEvent(int type, String sourceId) {
        this.eventType = type;
        this.sourceId = sourceId;
    }

    public boolean isFrontSpaceSensorOn() {
        return eventType == FRONT_EOS_SENSOR_ON;
    }

    public boolean isRearSpaceSensorOn() {
        return eventType == REAR_EOS_SENSOR_ON;
    }

    public int getEventType() {
        return eventType;
    }

    public String getSourceId() {
        return sourceId;
    }

    /**
     * @return the proposedState
     */
    private String eventTypeAsString(int type) {
        switch (type) {
            case FRONT_EOS_SENSOR_ON:
                return "LEFT EOW SENSOPR ON";
            case FRONT_EOS_SENSOR_OFF:
                return "LEFT EOW SENSOPR OFF";
            case REAR_EOS_SENSOR_ON:
                return "REAR_EOS_SENSOR_ON";
            case REAR_EOS_SENSOR_OFF:
                return "REAR_EOS_SENSOR_OFF";
//            case RIGHT_EOW_SENSOPR_ON:
//                return "RIGHT_EOW_SENSOPR_ON";
//            case RIGHT_EOW_SENSOPR_OFF:
//                return "RIGHT_EOW_SENSOPR_OFF";
            default:
                return "Event Type Unknown";
        }
    }

    public String toString() {
        return "Type: " + eventTypeAsString(eventType) + ", sourceId: " + sourceId;
    }

}
