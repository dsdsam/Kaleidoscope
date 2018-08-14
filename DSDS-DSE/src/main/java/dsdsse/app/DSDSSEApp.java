/*
 * Created on Jul 31, 2005
 *
 */
package dsdsse.app;


import adf.app.AdfApp;
import adf.app.AdfEnv;
import adf.app.AppManifestAttributes;
import adf.app.ConfigProperties;
import adf.ui.tootippopup.AdfDetailedTooltipPopup;
import adf.utils.BuildUtils;
import dsdsse.help.HelpPanelHolder;
import dsdsse.preferences.DsdsseUserPreference;
import dsdsse.welcome.WelcomePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

/**
 * @author xpadmin
 */
public class DSDSSEApp extends AdfApp {

    private static final Dimension DEFAULT_FRAME_SIZE = new Dimension(1200, 740);
    public static Dimension DSE_INITIAL_AND_MINIMUM_SIZE = new Dimension(960, 700);

    private static final String CONFIG_FILE_LOCATION = "/dsdsse-resources/config/";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static String BASE_IMAGE_LOCATION = "/dsdsse-resources/images/app-icons/";
    public static String PREFIX = "/dsdsse-resources/images/splash/";

    private static Color DSDSSE_APP_BACKGROUND = new Color(236, 233, 216);

    private static DsdsseMainFrame dsdsseMainFrame;

    private static final WindowAdapter windowAdapter = new WindowAdapter() {

        /**
         * Invoked when a window is in the process of being closed.
         * The close operation can be overridden at this point.
         */
        @Override
        public void windowClosing(WindowEvent e) {
//            if (splash != null) {
//                splash.closeSplash();
//            }
            Rectangle appPlacementRectangle;
            if (isFullScreenSize()) {
                appPlacementRectangle = getAppDefaultPlacement();
            } else {
                appPlacementRectangle = dsdsseMainFrame.getBounds();
            }
            DsdsseUserPreference.setAppLocation(appPlacementRectangle);
            DsdsseUserPreference.saveUserPreferences();
            AppController.getInstance().userClosesApplicationWindow();
        }


        /**
         * Invoked when a window is iconified.
         */
        @Override
        public void windowIconified(WindowEvent e) {
//            splash.closeSplash();
        }

        /**
         * Invoked when a window state is changed.
         *
         * @since 1.4
         */
        @Override
        public void windowStateChanged(WindowEvent e) {
//            (e.getNewState() & JFrame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
        }

        private boolean isFullScreenSize() {
            boolean frameMaximized = (dsdsseMainFrame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle bounds = dsdsseMainFrame.getBounds();
            boolean fullSize = (Math.abs(screenSize.width - bounds.width) < 50) &&
                    (Math.abs(screenSize.height - bounds.height) < 50);
            return frameMaximized || fullSize;
        }

        private Rectangle getAppDefaultPlacement() {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int restOfScreenWidth = screenSize.width - DSE_INITIAL_AND_MINIMUM_SIZE.width;
            int restOfScreenHeight = screenSize.height - DSE_INITIAL_AND_MINIMUM_SIZE.height;
            int xLocation = restOfScreenWidth >> 1;
            int yLocation = restOfScreenHeight >> 1;
            return new Rectangle(xLocation, yLocation,
                    DSE_INITIAL_AND_MINIMUM_SIZE.width, DSE_INITIAL_AND_MINIMUM_SIZE.height);
        }
    };

    //
    //   C o n s t r u c t i n g
    //
    private static DSDSSEApp dsdsdsepp;

    public static final synchronized void createDSDSSEApp() {
        assert dsdsdsepp == null : "DSDSSEApp is a singleton and already created";
        dsdsdsepp = new DSDSSEApp();
    }

    private DSDSSEApp() {
    }

    @Override
    protected void appInitialization() {

        System.out.println(System.lineSeparator() + "Initializing DSDS DSE instance.");

        // init Help System
        HelpPanelHolder.getInstance();

        // Initializing application version from jar's manifest
        AppManifestAttributes.initialize();

        new DseEventQueue();

        super.appInitialization();
    }

    //
    //    C r e a t i o n   s t e p s
    //
    //    initConfig();
    //    initLocale();
    //    initFonts();
    //    initUIManager();
    //    createMainFrame();
    //    showSplash();
    //    initModel();
    //    initUI();
    //    initController();
    //    initRelations();
    //    showMainFrame();

    @Override
    protected void initConfig() {
        Properties dsdsseConfig = ConfigProperties.initConfigProperties(CONFIG_FILE_LOCATION, CONFIG_FILE_NAME);
        ConfigProperties.logConfigProperties("DSDS Development & Simulating Environment config properties", dsdsseConfig);
    }

    @Override
    protected void initUIManager() throws Exception {

        String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        UIManager.setLookAndFeel(lookAndFeel);

        String appTitle = ConfigProperties.getStrConfigProperty(ConfigProperties.APP_TITLE_KEY, "DSDSSE");
        System.getProperties().put(ConfigProperties.APP_TITLE_KEY, appTitle);

        UIManager.put("dsdsse.app.ui.main.panel.background", DSDSSE_APP_BACKGROUND);

        UIManager.put("Slider.majorTickLength", new Integer(3));
        UIManager.put("Slider.trackWidth", new Integer(10));
        UIManager.put("Slider.darkShadow", Color.BLACK);
        UIManager.put("Slider.shadow", Color.BLACK);
        UIManager.put("Slider.highlight", Color.BLACK);

//      ToolTip
        Color tooltipBackground = new Color(0xFFFFDD);
        UIManager.put("ToolTip.background", tooltipBackground);

        UIManager.put("RootPane.frameBorder",
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 1),
//                        BorderFactory.createCompoundBorder(
//                                BorderFactory.createLineBorder( Color.BLACK, 1 ),
//                                BorderFactory.createLineBorder( Color.ORANGE, 1 )
//                                ),
                        BorderFactory.createCompoundBorder(

                                BorderFactory.createRaisedBevelBorder(),
                                BorderFactory.createLoweredBevelBorder()

                        )
                )
        );
    }

    //   C r e a t i n g   M a i n   F r a m e

    @Override
    protected final void createMainFrame() {
        dsdsseMainFrame = DsdsseMainFrame.createMainFrame();
        AdfEnv.putMainFrame(dsdsseMainFrame);

        String initialTitle = DsdsseEnvironment.getAppInitialFrameTitle();
        dsdsseMainFrame.setTitle(initialTitle);
        ImageIcon imageIcon = BuildUtils.getImageIcon(BASE_IMAGE_LOCATION + "title-logo.png");
        if (imageIcon != null) {
            Image image = imageIcon.getImage();
            dsdsseMainFrame.setIconImage(image);
        }

        dsdsseMainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dsdsseMainFrame.addWindowListener(windowAdapter);

        // Setting Frame size and location

        Dimension frameSize = DsdsseUserPreference.getAppSize();

        if (frameSize.width == 0 || frameSize.height == 0) {
            // We are here when size is not yet stored
            int occupancyPercent = ConfigProperties.geIntProperty(ConfigProperties.OCCUPANCY_PERCENT_KEY, 0);
            boolean useOccupancyPercent = occupancyPercent != 0;
            if (useOccupancyPercent) {
                // setting size based on occupancy percent
                dsdsseMainFrame.initFrameSize(((double) occupancyPercent) / 100);
            } else {
                // setting default size
                dsdsseMainFrame.setSize(DEFAULT_FRAME_SIZE);
                dsdsseMainFrame.setPreferredSize(DEFAULT_FRAME_SIZE);
            }
            dsdsseMainFrame.setLocationRelativeTo(null);

        } else {
            // size was stored
            dsdsseMainFrame.setSize(frameSize);
            dsdsseMainFrame.setPreferredSize(frameSize);
            // set stored location
            Point xyLocation = DsdsseUserPreference.getAppLocation();
            dsdsseMainFrame.setLocation(xyLocation.x, xyLocation.y);
        }
        dsdsseMainFrame.setMinimumSize(DSE_INITIAL_AND_MINIMUM_SIZE);

        AdfDetailedTooltipPopup.initPopup(dsdsseMainFrame);
    }

    //   S h o w i n g   s p l a s h

    @Override
    protected final void showSplash() {
//        AdfDetailedTooltipPopup.initPopup(dsdsseMainFrame);
//        DsdsseSplash.showAboutPopup(DsdsseMainFrame.getInstance());
    }

    //   I n i t i a l i z i n g   M o d e l s

    @Override
    protected void initModel() {
    }

    //   I n i t i a l i z i n g   U I

    @Override
    protected void initUI() {
        DSDSSEAppBuilder dsdsseAppBuilder = new DSDSSEAppBuilder();
        dsdsseAppBuilder.build(dsdsseMainFrame);
    }

    //   I n i t i a l i z i n g   C o n t r o l l e r s

    @Override
    protected void initController() {
    }

    //  I n i t i a l i z i n g   C o m p o n e n t   R e l a t i o n s

    @Override
    protected final void initRelations() {
    }

    //   S h o w   M a i n   F r a m e

    @Override
    public void showMainFrame() {
        AdfEnv.getMainFrame().setVisible(true);
        if (DsdsseUserPreference.isWelcomePopupOn()) {
            WelcomePanel.showWelcomePanel(dsdsseMainFrame);
        }
    }

    //   S t a r t i n g   T h e   A p p l i c a t i o n

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DSDSSEApp.createDSDSSEApp();
            } catch (Throwable e) {
                System.out.println("DSDSSEApp.main: Throwable cought ! " + e.toString());
                System.out.println("DSDSSEApp.main: See Error Log for details.");
                e.printStackTrace();
            }
        });
    }
}
