package sem.mission.controlles.modelcontroller.interfaces;

import sem.mission.controlles.modelcontroller.actions.ServiceExecutionEvent;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Nov 2, 2011
 * Time: 9:15:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicOperationDoneListener {

    /**
     * Called on server response, when result is successful. Should be
     * overridden by extending class.
     *
     * @param e
     */
    void success(ServiceExecutionEvent e) {
    }

    /**
     * Called on server response, when result is failure. Should be overridden
     * by extending class.
     *
     * @param e
     */
    void failure(ServiceExecutionEvent e) {

    }

    /**
     * Called on server response, when timeout expired before response received.
     * Should be overridden by extending class.
     *
     * @param e
     */
    void timeout(ServiceExecutionEvent e) {
    }

    private void done(ServiceExecutionEvent e) {
        switch (e.getExecutionStatus()) {
            case ServiceExecutionEvent.SUCCESS:
                success(e);
                break;

            case ServiceExecutionEvent.FAILURE:
                failure(e);
                break;

            case ServiceExecutionEvent.TIMEOUT:
                timeout(e);
                break;

            default:
                throw new RuntimeException("Execution Status Not Set");
        }
    }


    synchronized public void executionDone(final ServiceExecutionEvent e) {

        if (SwingUtilities.isEventDispatchThread()) {
            done(e);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    done(e);
                }
            });
        }
    }

}
