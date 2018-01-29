package adf.messageflyoutpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by u0180093 on 11/11/2016.
 */
class MainFrameFlyOutMessageManager {

    private static final Logger logger = Logger.getLogger(MainFrameFlyOutMessageManager.class.getName());

    private static FlyOutMessageManager flyOutMessageManager;

    private static void createFlyOutMessageManager() {
        if (flyOutMessageManager != null) {
            return;
        }
        JFrame frame = new JFrame();
        JLayeredPane layeredPane = frame.getLayeredPane();
        flyOutMessageManager = new FlyOutMessageManager(layeredPane);
    }

    /**
     * @param message
     */
    public static final synchronized void showAlertMessage(String message) {
        if (flyOutMessageManager == null) {
            createFlyOutMessageManager();
        }
        try {
            flyOutMessageManager.showAlertMessage(message);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in Fly Out Message Manager, while attempting to present message.", e);
        }
    }

    static {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent e) {
                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                    if (flyOutMessageManager != null) {
                        flyOutMessageManager.resetToHiddenStatus();
                    }
                    return;
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }
}
