package sem.mission.controlles.modelcontroller.actions;

import sem.appui.controller.BasicOperationRequest;

import javax.swing.*;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 28, 2011
 * Time: 8:48:46 PM
 * <p/>
 * This Class is intended to be used with menu items and buttons instead
 * of Action. The behavior of the class is following.
 * <p/>
 * Its executeMenuOperation() method is called when the menu item or the
 * button is clicked. The method calls isAllowed() method to click if
 * the action is allowed at the moment. The isAllowed method is empty
 * and should be overridden if necessary. Then exec() method is called.
 * This method does the operation and definitely should be overridden.
 * Hence, it is abstract. The overriding method should call one of the
 * operations provided by RequestProcessor. As one (the first) argument
 * of the call it should provide "this" as the ServiceExecutionListener.
 * So, when operation is accomplished by server, it will call this
 * class's executionDone method with ServiceExecutionEvent e. Method
 * executionDone in turn will enable the action and check the status of
 * the response. For timeout it will call method:
 * timeout(ServiceExecutionEvent e). For general failure to accomplish
 * the operation method: failure(ServiceExecutionEvent e). For the case
 * the opertaion executed metod: success(ServiceExecutionEvent e).
 * <p/>
 * All these tree methods can be overridden by extending class if
 * necessary.
 */
@SuppressWarnings("serial")
abstract class AsynchronousAction extends AbstractAction implements ServiceExecutionListener {

    private static final Logger logger = Logger.getLogger(AsynchronousAction.class.getName());
    private Object source = null;
    private JFrame frame;

    /**
     * @param name
     */
    public AsynchronousAction(String name) {
        this(null, name);
    }

    public AsynchronousAction(JFrame frame, String name) {
        super(name);
        this.frame = frame;
    }

    public AsynchronousAction() {

    }

    /**
     * Checks out if the action allowed. Should be overridden by extending
     * class.
     *
     * @return boolean
     */
    boolean isAllowed() {
        return true;
    }

    /**
     * The overriding method should invoke server and provide "this" as a
     * listener for the server response.
     *
     */
    //abstract protected void exec();

    /**
     * The overriding method should invoke server and provide "this" as a
     * listener for the server response.
     */
    abstract void exec();

    /**
     * Called on server response, when result is successful. Should be
     * overridden by extending class.
     *
     * @param operationRequest
     */
    void success(BasicOperationRequest operationRequest) {
    }

    /**
     * Called on server response, when result is failure. Should be overridden
     * by extending class.
     *
     * @param operationRequest
     */
    void failure(BasicOperationRequest operationRequest) {
    }

    /**
     * Callback methof that is called when action executed interrupted.
     *
     * @param operationRequest
     */
    void interrupted(BasicOperationRequest operationRequest) {
    }

    /**
     * Called on server response, when timeout expired before response received.
     * Should be overridden by extending class.
     *
     * @param operationRequest
     */
    void timeout(BasicOperationRequest operationRequest) {
    }


    /**
     * this method should be overriden to get the server call result
     */
    private void done(BasicOperationRequest operationRequest) {
        setEnabled(true);
        switch (operationRequest.getOperationRequestStatus()) {
            case SUCCESS:
            case ENQUEUED:
                success(operationRequest);
                break;

            case FAILURE:

//            RequestProcessorLogger.logServiceExecutionEvent(e);
//
//            if (e.getRequestError() != null) {
//                RequestProcessorMessagePopupUtils
//                        .showRequestFailureMessagePopup(e.getRequestError());
//            } else {
//                String header = "Server ERROR on Operation: \""
//                        + e.getRequestedOperation() + "\"";
//                RequestProcessorMessagePopupUtils
//                        .showRequestFailureMessagePopup(header, ""
//                                + e.getMessage() + "  ");
//            }

                failure(operationRequest);
                break;

            case TIMEOUT:
                logger.warning(operationRequest.toString());
                // RequestProcessorMessagePopup.showRequestTimeoutMessagePopup(
                // e.getMessage(), e.getRequestedOperation(), e.getTimeoutValue() );
                timeout(operationRequest);
                break;

            default:
                throw new RuntimeException("BasicAsynchronousServerAction.done: "
                        + "executionStatus not set");
        }
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
        // fireCacheEventInCallingThread( applySorting );
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                done(operationRequest);
            }
        });
    }

    /**
     * Action Event listener implementation. Called when the action is to be
     * applied.
     */
    public void actionPerformed(ActionEvent event) {
        String actionCommand = null;
        AsynchronousAction listener = null;
        if (event != null) {
            source = event.getSource();
            actionCommand = event.getActionCommand();
        }
        try {
            if (!isAllowed()) {
                return;
            }
            setEnabled(false);
            exec();
//            if (listener == null) {
//                if (!isAllowed()) {
//                    return;
//                }
//                setEnabled(false);
//                exec();
//            } else {
//                if (!listener.isAllowed()) {
//                    return;
//                }
//                setEnabled(false);
//                listener.exec();
//            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            String exception = e.getClass().getName();
            exception = exception.substring(exception.lastIndexOf(".") + 1);
            String text = "Gui Action cought Exception: \"" + exception
                    + "\"   ";
//            RequestProcessorMessagePopupUtils
//                    .showRequestFailureMessagePopup("Exception !", text);
        }
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
