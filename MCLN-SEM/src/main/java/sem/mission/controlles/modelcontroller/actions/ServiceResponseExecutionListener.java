package sem.mission.controlles.modelcontroller.actions;

import sem.appui.controller.BasicOperationRequest;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Dec 26, 2011
 * Time: 12:06:01 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ServiceResponseExecutionListener implements ServiceExecutionListener {

    private static final Logger logger = Logger.getLogger(BasicAsynchronousAction.class.getName());
    private Object source = null;
    private JFrame frame;


    public ServiceResponseExecutionListener(JFrame frame) {
        this.frame = frame;
    }

    public ServiceResponseExecutionListener() {

    }

    /**
     *
     */
    protected void initialiseWaiting() {
    }

    /**
     * Called on server response, when result is successful. Should be
     * overridden by extending class.
     *
     * @param operationRequest
     */
    protected void success(BasicOperationRequest operationRequest) {
    }

    /**
     * Called on server response, when result is failure. Should be overridden
     * by extending class.
     *
     * @param operationRequest
     */
    protected void failure(BasicOperationRequest operationRequest) {

    }

    /**
     * Called on server response, when timeout expired before response received.
     * Should be overridden by extending class.
     *
     * @param operationRequest
     */
    protected void timeout(BasicOperationRequest operationRequest) {
    }

    /**
     * Called on server response, after the necessary processing is done to
     * finish the request if we need. Should be overridden by extending class.
     *
     * @param operationRequest
     */
    protected void postProcess(BasicOperationRequest operationRequest) {
    }

    /**
     * this method should be overriden to get the server call result
     */
    private void done(BasicOperationRequest operationRequest) {
        switch (operationRequest.getOperationRequestStatus()) {
            case SUCCESS:
                success(operationRequest);
                break;

            case FAILURE:
                failure(operationRequest);
                break;

            case TIMEOUT:
                logger.warning(operationRequest.toString());
                timeout(operationRequest);
                break;

            default:
                throw new RuntimeException("BasicAsynchronousServerAction.done: "
                        + "executionStatus not set");
        }
        postProcess(operationRequest);
    }


    /**
     * Called by executing server that should be invoked from exec method
     */
    public synchronized void executionDone(final BasicOperationRequest operationRequest) {
        if (SwingUtilities.isEventDispatchThread()) {
            done(operationRequest);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    done(operationRequest);
                }
            });
        }
    }
}
