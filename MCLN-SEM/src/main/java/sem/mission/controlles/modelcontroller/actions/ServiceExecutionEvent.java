package sem.mission.controlles.modelcontroller.actions;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 28, 2011
 * Time: 8:53:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceExecutionEvent {

    public static final int NONE = 0;
    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;
    public static final int TIMEOUT = 3;


    public static final ServiceExecutionEvent createSucessEvent(Enum requestedOperation, boolean powerOn) {
        return new ServiceExecutionEvent(requestedOperation, SUCCESS, powerOn);
    }

    public static final ServiceExecutionEvent createFalueEvent(Enum requestedOperation, boolean powerOn) {
        return new ServiceExecutionEvent(requestedOperation, FAILURE, powerOn);
    }

    public static final ServiceExecutionEvent createTimeoutEvent(Enum requestedOperation, boolean powerOn) {
        return new ServiceExecutionEvent(requestedOperation, TIMEOUT, powerOn);
    }

    private Enum requestedOperation;
    private int executionStatus;
    private String message;
    private int timeoutValue;
    private boolean powerOn;

    private ServiceExecutionEvent(Enum requestedOperation, int executionStatus, boolean powerOn) {
        this.requestedOperation = requestedOperation;
        this.executionStatus = executionStatus;
        this.powerOn = powerOn;
    }

    public Enum getRequestedOperation() {
        return requestedOperation;
    }

    public int getExecutionStatus() {
        return executionStatus;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    /**
     * @return boolean
     */
    public boolean isSuccess() {
        switch (executionStatus) {
            case SUCCESS:
                return true;
            case FAILURE:
            case TIMEOUT:
                return false;
            default:
                throw new RuntimeException("ServiceExecutionEvent.isSuccess: " +
                        "executionStatus not set");
        }
    }


    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * @return Returns the timeoutValue.
     */
    public int getTimeoutValue() {
        return timeoutValue;
    }

    /**
     * @param timeoutValue The timeoutValue to set.
     */
    public void setTimeoutValue(int timeoutValue) {
        this.timeoutValue = timeoutValue;
    }

    /**
     *
     */
    public String toString() {
        return "Requested Operation: " + requestedOperation + ", executionStatus: " + executionStatus;
    }

}
