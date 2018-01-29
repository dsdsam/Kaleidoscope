package sem.mission.controlles.modelcontroller.actions;

import sem.appui.controller.BasicOperationRequest;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Mar 21, 2012
 * Time: 7:40:52 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ServiceExecutionListenerAdapter implements ServiceExecutionListener {

    private static final Logger logger = Logger.getLogger(ServiceExecutionListenerAdapter.class.getName());


    /**
     *
     */
    public ServiceExecutionListenerAdapter() {

    }

    /**
     * Called on service enqueued request. Should be overridden by extending class.
     *
     * @param operationRequest
     */
    protected void enqueued(BasicOperationRequest operationRequest) {
    }

    /**
     * Callback method that is called when action executin\on interrupted.
     *
     * @param operationRequest
     */
    protected void interrupted(BasicOperationRequest operationRequest) {
    }

    /**
     * Called on service response, when result is successful. Should be
     * overridden by extending class.
     *
     * @param operationRequest
     */
    protected void success(BasicOperationRequest operationRequest) {
    }

    /**
     * Called on service response, when result is failure. Should be overridden
     * by extending class.
     *
     * @param operationRequest
     */
    protected void failure(BasicOperationRequest operationRequest) {
    }

    /**
     * Called on service response, when timeout expired before response received.
     * Should be overridden by extending class.
     *
     * @param operationRequest
     */
    protected void timeout(BasicOperationRequest operationRequest) {
    }

    /**
     * this method should be overriden to get the server call result
     */
    private void done(BasicOperationRequest<?, ?> operationRequest) {


    }

    /**
     *
     */
    // protected void fireCacheEventInCallingThread( boolean applySorting ) {
    // CacheEvent event = new CacheEvent( this,
    // applySorting ? CacheEvent.CHANGE_DONE :
    // CacheEvent.CHANGE_DONE_DO_QUICK_UPDATE,
    // -1, null );
    // BufferedRequirementsBlotterTablePanel.getBufferedBlotterTableModel().onCacheChanged(event);
    // // Guaranteed to return a non-null array
    // // Object[] listeners = listenerList.getListenerList();
    // // // Process the listeners last to first, notifying
    // // // those that are interested in this event
    // // for (int i = listeners.length-2; i>=0; i-=2) {
    // // if (listeners[i]==ICacheListener.class) {
    // // ((ICacheListener)listeners[i+1]).onCacheChanged(event);
    // // }
    // // }
    // }

    /**
     * Called by executing server that should be invoked from exec method
     */
    synchronized public void executionDone(final BasicOperationRequest operationRequest) {

        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    executionDone(operationRequest);
                }
            });
        }

        switch (operationRequest.getOperationRequestStatus()) {
            case ENQUEUED:
                enqueued(operationRequest);
                break;

            case INTERRUPTED:
                interrupted(operationRequest);
                break;

            case SUCCESS:
                success(operationRequest);
                break;

            case FAILURE:
                failure(operationRequest);
                break;

            case TIMEOUT:
                logger.warning(operationRequest.toString());
                // RequestProcessorMessagePopup.showRequestTimeoutMessagePopup(
                // e.getMessage(), e.getRequestedOperation(), e.getTimeoutValue() );
                timeout(operationRequest);
                break;

            default:
                throw new RuntimeException("ServiceExecutionListenerAdapter.executionDone: "
                        + "executionStatus not set");
        }
    }

}
