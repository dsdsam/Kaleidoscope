package mcln.model;

import mcln.palette.MclnState;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 1/10/14
 * Time: 7:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnModelEvent {

    public static enum EventType {INF_DIVIDER, PGM_INPUT, USR_INPUT, INFERENCE, EXTERNAL_INPUT}

    public static MclnModelEvent DIVIDER_EVENT = new MclnModelEvent(EventType.INF_DIVIDER);

    private final String source;
    private final EventType eventType;
    private final MclnState newMclnState;

    private MclnModelEvent(EventType eventType) {
        this("", EventType.INF_DIVIDER, null);
    }

    public MclnModelEvent(String uid, EventType eventType, MclnState newMclnState) {
        source = uid;
        this.eventType = eventType;
        this.newMclnState = newMclnState;
    }

    public EventType getInputType() {
        return eventType;
    }

    public String getSourceUID() {
        return source;
    }

    public MclnState getNewState() {
        return newMclnState;
    }

    public boolean isProgramInput() {
        return (eventType == EventType.PGM_INPUT);
    }

    public boolean isUserInput() {
        return (eventType == EventType.USR_INPUT);
    }

    public boolean isDivider() {
        return (eventType == EventType.INF_DIVIDER);
    }

    public String toString() {
        return "" + eventType + ", source=" + source + ",   " + ((newMclnState == null) ? "no state " : newMclnState.toString());
    }
}
