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

    private static SEMainFrame seMainFrame;

    /**
     * @return
     */
    public static SEMainFrame createMainFrame() {
        if (seMainFrame != null) {
            logger.severe("SEMainFrame instance already created !!!");
            return seMainFrame;
        }
        return new SEMainFrame();
    }

    /**
     * @return
     */
    public static SEMainFrame getInstance() {
        return seMainFrame;
    }

    //   I n s t a n c e

    private CardLayout cardLayout = new CardLayout();
    private Container contentPane;
    private Map panelMap = new HashMap();


    // =========    C o n s t r u c t o r s    ====================

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
        seMainFrame = this;
        AdfMessagesAndDialogs.setMainFrame(seMainFrame);
        String allowUserKeys = System.getProperty("allow.user.keys");
        if (allowUserKeys != null && allowUserKeys.equalsIgnoreCase("true")) {
            KeyboardFocusManager.setCurrentKeyboardFocusManager(newManager);
        }
    }

    public void init(JPanel seMainPanel) {
        contentPane = getContentPane();
        this.getRootPane().setBorder(null);
        contentPane.setLayout(cardLayout);
        contentPane.add(seMainPanel, MAIN_PANEL_SE);
        panelMap.put(MAIN_PANEL_SE, seMainPanel);


    }

    @Override
    public void setVisible(boolean state) {
        Dimension size = getSize();
        super.setVisible(state);
    }


    public Dimension initMainFrame(double percent) {
        setTitle(System.getProperty(ConfigProperties.APP_TITLE_KEY) + " - " + AppManifestAttributes.getAppVersion());
        Image image = BuildUtils.getImageIcon(sem.app.SEMApp.ICONS_DIR_CLASS_PATH + "title-logo.png").getImage();
        if (image != null) {
            setIconImage(image);
        }

        // set Main Frame size
        Toolkit tk = getToolkit();
        Dimension screenSize = tk.getScreenSize();
//       System.out.println( "curSize.width = " + curSize.width );
//       System.out.println( "curSize.height = " + curSize.height );

//        if (screenSize.width == 1024 && screenSize.height == 768) {
//            curSize.width = screenSize.width;
//            curSize.height = screenSize.height;
//            pack();
//            setSize(curSize);
//            this.setMinimumSize(new Dimension(300, 500));
//            return new Dimension(0, 0);
//        }
//
//        if (screenSize.width > 640) {
////            int height = (int) (((float) screenSize.height) * percent);
////            int portion = height / 3;
//            int width = 1024;
//            int portion = width / 4;
//            curSize.width = (portion * 4);
//            curSize.height = portion * 3;// - 60;
//        }
//                    int width = 580;
//            int portion = width / 4;
//            curSize.width = (portion * 4);
//            curSize.height = portion * 3;// - 60;

//        curSize.width = 1024;
//        curSize.height = 840;

//       System.out.println( "curSize.width = " + curSize.width );
//       System.out.println( "curSize.height = " + curSize.height );
//       initTest();

//        pack();
//        setSize(curSize.width, curSize.height);

        Dimension minSize = new Dimension(900, 740);
        Dimension preferredSize = new Dimension(1200, 860);
        preferredSize = new Dimension(1200, 740);
        setSize(preferredSize);
        setPreferredSize(preferredSize);
        this.setMinimumSize(minSize);

        return new Dimension(screenSize.width - curSize.width, screenSize.height - curSize.height);
    }
}
