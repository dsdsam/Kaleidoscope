package adf.flyout;

import adf.app.AdfEnv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFrameFlyingMessageManager {

    private static final Logger logger = Logger.getLogger(MainFrameFlyingMessageManager.class.getName());

    private static FlyOutMessageManager flyOutMessageManager;
    private static FlyDownMessageManager flyDownMessageManager;

    private static void createFlyOutMessageManager() {
        if (flyOutMessageManager != null) {
            return;
        }
        JFrame mainFrame = AdfEnv.getMainFrame();
        flyOutMessageManager = new FlyOutMessageManager(mainFrame);
    }

    private static void createFlyDownMessageManager() {
        if (flyDownMessageManager != null) {
            return;
        }
        JFrame mainFrame = AdfEnv.getMainFrame();
        flyDownMessageManager = new FlyDownMessageManager(mainFrame);
    }

    /**
     * @param message
     */
    public static final synchronized void showFlyOutAlertMessage(String message) {
        if (flyOutMessageManager == null) {
            createFlyOutMessageManager();
        }
        try {
            flyOutMessageManager.showAlertMessage(message);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in Fly Out Message Manager, while attempting to present message.", e);
        }
    }

    /**
     * @param message
     */
    public static final synchronized void showFlyDownAlertMessage(String message) {
        if (flyDownMessageManager == null) {
            createFlyDownMessageManager();
        }
        try {
            flyDownMessageManager.showAlertMessage(message);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in Fly Down Message Manager, while attempting to present message.", e);
        }
    }

    public static final synchronized void showFlyDownWarningMessage(String message) {
        if (flyDownMessageManager == null) {
            createFlyDownMessageManager();
        }
        try {
            flyDownMessageManager.showWarningMessage(message);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in Fly Down Message Manager, while attempting to present message.", e);
        }
    }

    /**
     * @param message
     */
    public static final synchronized void showFlyDownInfoMessage(String message) {
        if (flyDownMessageManager == null) {
            createFlyDownMessageManager();
        }
        try {
            flyDownMessageManager.showInfoMessage(message);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in Fly Down Message Manager, while attempting to present message.", e);
        }
    }

    static {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent e) {
                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                    if (flyOutMessageManager != null) {
                        if (flyOutMessageManager.resetToHiddenStatus()) {
                            ((MouseEvent) e).consume();
                        }
                    }
                    if (flyDownMessageManager != null) {
                        if (flyDownMessageManager.resetToHiddenStatus()) {
                            ((MouseEvent) e).consume();
                        }
                    }
                    return;
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }
}
