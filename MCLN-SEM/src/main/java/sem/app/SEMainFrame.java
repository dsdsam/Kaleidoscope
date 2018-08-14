/*
 * Created on Jul 31, 2005
 *
 */
package sem.app;

import adf.app.AdfMessagesAndDialogs;
import adf.app.AppManifestAttributes;
import adf.app.ConfigProperties;
import adf.mainframe.AdfMainFrame;
import adf.utils.BuildUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author xpadmin
 */
public class SEMainFrame extends AdfMainFrame {

    private static final Logger logger = Logger.getLogger(SEMainFrame.class.getName());

    public static final String MAIN_PANEL_SE = "MAIN_PANEL_SE";
    public static final String MAIN_PANEL_FE = "MAIN_PANEL_FE";
    public static final String MAIN_PANEL_RE = "MAIN_PANEL_RE";

    private static SEMainFrame semMainFrame;

    public static final synchronized SEMainFrame createMainFrame(){
        assert semMainFrame == null : "SEM Main Frame is a singleton and already created";
        return semMainFrame = new SEMainFrame();
    }

    public static final SEMainFrame getInstance() {
        assert semMainFrame != null : "SEM Main Frame is a singleton and not yet created";
        return semMainFrame;
    }

    //
    //   I n s t a n c e
    //

    private CardLayout cardLayout = new CardLayout();
    private Container contentPane;
    private Map panelMap = new HashMap();

    private KeyboardFocusManager newManager = new DefaultKeyboardFocusManager() {
        public boolean dispatchEvent(AWTEvent e) {
            processHit(e);
            return super.dispatchEvent(e);
        }
    };

    public void processHit(AWTEvent e) {
        if (!(e instanceof KeyEvent)) {
            return;
        }
        KeyEvent keyEvent = (KeyEvent) e;
        if (keyEvent.getKeyCode() == KeyEvent.VK_F1 &&
                keyEvent.getID() == KeyEvent.KEY_PRESSED) {
            System.out.println("F1 pressed");
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F2 &&
                keyEvent.getID() == KeyEvent.KEY_PRESSED) {

        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F3 &&
                keyEvent.getID() == KeyEvent.KEY_PRESSED) {
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F4 &&
                keyEvent.getID() == KeyEvent.KEY_PRESSED) {
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F5 &&
                keyEvent.getID() == KeyEvent.KEY_PRESSED) {
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F6 &&
                keyEvent.getID() == KeyEvent.KEY_PRESSED) {
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F7 &&
                keyEvent.getID() == KeyEvent.KEY_PRESSED) {
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F8 &&
                keyEvent.getID() == KeyEvent.KEY_PRESSED) {
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F9 &&
                keyEvent.getID() == KeyEvent.KEY_PRESSED) {
        }

    }

    private SEMainFrame() {
        AdfMessagesAndDialogs.setMainFrame(semMainFrame);
        String allowUserKeys = System.getProperty("allow.user.keys");
        if (allowUserKeys != null && allowUserKeys.equalsIgnoreCase("true")) {
            KeyboardFocusManager.setCurrentKeyboardFocusManager(newManager);
        }
    }

    public void setSemMainPanel(JPanel seMainPanel) {
        contentPane = getContentPane();
        this.getRootPane().setBorder(null);
        contentPane.setLayout(cardLayout);
        contentPane.add(seMainPanel, MAIN_PANEL_SE);
        panelMap.put(MAIN_PANEL_SE, seMainPanel);
    }
}
