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
import adf.utils.WaitingSignPopup;
import dsdsse.help.HelpPanelHolder;
import dsdsse.preferences.DsdsseUserPreference;
import dsdsse.welcome.WelcomePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.Properties;

/**
 * @author xpadmin
 */
public class DSDSSEApp extends AdfApp {

    private static final double OCCUPANCY_PERCENT = 0.80;
    private static final double LOCATION_PERCENT = 0.40;

    private static final String CONFIG_FILE_LOCATION = "/dsdsse-resources/config/";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static String BASE_IMAGE_LOCATION = "/dsdsse-resources/images/app-icons/";
    public static String PREFIX = "/dsdsse-resources/images/splash/";


    public static final Dimension DSE_INITIAL_AND_MINIMUM_SIZE = new Dimension(1100, 820);
//    public static Dimension DSE_INITIAL_AND_MINIMUM_SIZE = new Dimension(700, 400);

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
    private Object lock;

    //
    //   C o n s t r u c t i n g
    //

    // made public to be accessible from Super Module
    public DSDSSEApp() {
        super(false); // false means DSDSSE does not use ADF config properties
    }

    @Override
    protected void appInitialization() {

        System.out.println(System.lineSeparator() + "Initializing DSDS DSE instance.");

        Locale initialLocale = Locale.getDefault();
        System.out.println();
        System.out.println("Initial Locale: Locale Name = \"" + initialLocale.getDisplayName() + "\"");
        System.out.println("Initial Locale: Country = \"" + initialLocale.getDisplayCountry() + "\"");
        System.out.println("Initial Locale: Language = \"" + initialLocale.getDisplayLanguage() + "\"");

        Locale.setDefault(Locale.US);
        Locale installedLocale = Locale.getDefault();
        System.out.println();
        System.out.println("Installed Locale: Locale Name = \"" + installedLocale.getDisplayName() + "\"");
        System.out.println("Installed Locale: Country = \"" + installedLocale.getDisplayCountry() + "\"");
        System.out.println("Installed Locale: Language = \"" + installedLocale.getDisplayLanguage() + "\"");
        System.out.println();

        AdfEnv.loadAndRegisterFont("calibrib.ttf");
        AdfEnv.loadAndRegisterFont("calibri.ttf");
        AdfEnv.loadAndRegisterFont("roboto/Roboto-Italic.ttf");
        System.setProperty("java.util.prefs.PreferencesFactory", "adf.utils.preferences.AppUserPreferencesFactory");

        // init Help System
        // DsdsseEnvironment.createQuickHelpPanel();
        HelpPanelHolder.getInstance();

        // Initializing application version from jar's manifest
        AppManifestAttributes.initialize();

        try {
            new DseEventQueue();
            Properties dsdsseConfig = ConfigProperties.initConfigProperties(CONFIG_FILE_LOCATION, CONFIG_FILE_NAME);
            ConfigProperties.logConfigProperties("DSDS Simulating Environment config properties", dsdsseConfig);

            // application creation steps
            initUIManager();
            createMainFrame();

            showSplash();
            initController();
            initModel();
            initUI();
            initRelations();
            showWelcomePanel();
            WaitingSignPopup waitingSign = new WaitingSignPopup(DsdsseMainFrame.getInstance());

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //
    //   C r e a t i o n   s t e p s
    //

    private void initUIManager() {
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
//        Color tooltipBackground = new Color(0xAAFFFF);
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

    private final void createMainFrame() {
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

        Dimension frameSize = DsdsseUserPreference.getAppSize();
        dsdsseMainFrame.setSize(frameSize);
        int frameWidthDefault = 0;
        int frameHeightDefault = 0;

        int xLocation = 0;
        int yLocation = 0;
        if (frameSize.width == frameWidthDefault && frameSize.height == frameHeightDefault) {
            // We are here when size is not yet stored
            Dimension restOfScreen = dsdsseMainFrame.initMainFrame(OCCUPANCY_PERCENT);
            if (restOfScreen.width == 0 && restOfScreen.height == 0) {
                // full screen case
                dsdsseMainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                boolean placeInTheMiddleOfTheScreen = true;

                if (placeInTheMiddleOfTheScreen) {
                    xLocation = restOfScreen.width >> 1;
                    yLocation = restOfScreen.height >> 1;
                } else {
                    xLocation = (int) (restOfScreen.width * LOCATION_PERCENT);
                    yLocation = (int) (restOfScreen.height * LOCATION_PERCENT);
                }
            }

        } else {
            // size was stored
            Point xyLocation = DsdsseUserPreference.getAppLocation();
            xLocation = xyLocation.x;
            yLocation = xyLocation.y;
        }

        dsdsseMainFrame.setLocation(xLocation, yLocation);
        dsdsseMainFrame.setMinimumSize(DSE_INITIAL_AND_MINIMUM_SIZE);
    }

    //   S h o w i n g   s p l a s h

    private final void showSplash() {
        AdfDetailedTooltipPopup.initPopup(dsdsseMainFrame);
    }


    //   I n i t i a l i z i n g   C o n t r o l l e r s

    private void initController() {
    }

    //   I n i t i a l i z i n g   M o d e l s

    private void initModel() {
//        spaceExplorerModel = SpaceExplorerModel.getInstance();
    }

    //   I n i t i a l i z i n g   U I

    private void initUI() {
        DSDSSEAppBuilder dsdsseAppBuilder = new DSDSSEAppBuilder();
        dsdsseAppBuilder.build(dsdsseMainFrame);
        showMainFrame();
    }

    //  I n i t i a l i z i n g   C o m p o n e n t   R e l a t i o n s

    private final void initRelations() {
    }

    private final void showWelcomePanel() {
        if (DsdsseUserPreference.isWelcomePopupOn()) {
            WelcomePanel.showWelcomePanel(dsdsseMainFrame);
        }
    }

    //   S t a r t i n g   T h e   A p p l i c a t i o n

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                UIManager.setLookAndFeel(lookAndFeel);
                DSDSSEApp app = new DSDSSEApp();
            } catch (Throwable e) {
                System.out.println("DSDSSEApp.main: Throwable cought ! " + e.toString());
                System.out.println("DSDSSEApp.main: See Error Log for details.");
                e.printStackTrace();
            }
        });
    }
}
