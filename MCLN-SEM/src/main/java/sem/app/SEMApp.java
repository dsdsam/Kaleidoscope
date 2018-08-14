package sem.app;

import adf.app.AdfApp;
import adf.app.AdfEnv;
import adf.app.AppManifestAttributes;
import adf.app.ConfigProperties;
import adf.utils.BuildUtils;
import sem.appui.SEMainPanel;
import sem.appui.components.SEHorizontalSliderThumbIcon;
import sem.appui.components.SEVerticalSliderThumbIcon;
import sem.appui.controller.AppUIController;
import sem.appui.splash.Splash;
import sem.mission.controlles.mclncontroller.ControllersBuilder;
import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.explorer.model.SpaceExplorerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

/**
 * Created by Admin on 11/11/2017.
 */
public class SEMApp extends AdfApp {

    private static final Dimension DEFAULT_FRAME_SIZE = new Dimension(1100, 740);
    private static final Dimension FRAME_MINIMUM_SIZE = new Dimension(960, 700);

    private static final String CONFIG_FILE_LOCATION = "/sem-resources/config/";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static String ICONS_DIR_CLASS_PATH = "/sem-resources/images/app-icons/";
    public static String SPLASH_DIR_CLASS_PATH = "/sem-resources/images/splash/";

    //    private static Color SE_APP_BACKGROUND = new Color(236, 233, 216);
    private static Color SE_APP_BACKGROUND = new Color(236, 233, 216);

    private static SEMainFrame semMainFrame;

    //   I n s t a n c e

    private SpaceExplorerModel spaceExplorerModel;
    private SEMainPanel seMainPanel;
    private static Splash splash;

    private static final ComponentListener frameComponentListener = new ComponentListener() {
        /**
         * Invoked when the component's size changes.
         */
        public void componentResized(ComponentEvent e) {
            if (semMainFrame != null && semMainFrame.isVisible() && splash != null) {
//                splash.frameBoundsChanged(e.getComponent().getBounds());
            }
        }

        /**
         * Invoked when the component's position changes.
         */
        public void componentMoved(ComponentEvent e) {
            if (semMainFrame != null && semMainFrame.isVisible() && splash != null) {
//                splash.frameBoundsChanged(e.getComponent().getBounds());
            }
        }

        /**
         * Invoked when the component has been made visible.
         */
        public void componentShown(ComponentEvent e) {
        }

        /**
         * Invoked when the component has been made invisible.
         */
        public void componentHidden(ComponentEvent e) {
//            splash.closeSplash();
        }
    };

    private static final WindowAdapter windowAdapter = new WindowAdapter() {

        /**
         * Invoked when a window is in the process of being closed.
         * The close operation can be overridden at this point.
         */
        @Override
        public void windowClosing(WindowEvent e) {
            if (splash != null) {
                splash.closeSplash();
            }
            System.exit(0);
        }

        /**
         * Invoked when a window is iconified.
         */
        @Override
        public void windowIconified(WindowEvent e) {
            splash.closeSplash();
        }

        /**
         * Invoked when a window state is changed.
         *
         * @since 1.4
         */
        @Override
        public void windowStateChanged(WindowEvent e) {
            if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                System.out.println("maximized");
            }
        }


    };

    //
    //   C o n s t r u c t i n g
    //

    private static SEMApp semApp;

    public static final synchronized void createSEMApp() {
        assert semApp == null : "SEMApp is a singleton and already created";
        semApp = new SEMApp();
    }

    private SEMApp() {
    }

    @Override
    protected void appInitialization() {

        System.out.println(System.lineSeparator() + "Initializing Space Exploration Mission instance.");

        // Initializing application version from jar's manifest
        AppManifestAttributes.initialize();

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
        Properties semConfigProperties = ConfigProperties.initConfigProperties(CONFIG_FILE_LOCATION, CONFIG_FILE_NAME);
        ConfigProperties.logConfigProperties("Space Exploration Mission config properties", semConfigProperties);

    }

    @Override
    protected void initUIManager() throws Exception {

        String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        UIManager.setLookAndFeel(lookAndFeel);

        String appTitle = ConfigProperties.getStrConfigProperty(ConfigProperties.APP_TITLE_KEY) + " - " + AppManifestAttributes.getAppVersion();
        System.getProperties().put(ConfigProperties.APP_TITLE_KEY, appTitle);

        UIManager.put(MAIN_PANEL_BACKGROUND, SE_APP_BACKGROUND);

        UIManager.put("Slider.majorTickLength", new Integer(3));
        UIManager.put("Slider.trackWidth", new Integer(10));
        UIManager.put("Slider.darkShadow", Color.BLACK);
        UIManager.put("Slider.shadow", Color.BLACK);
        UIManager.put("Slider.highlight", Color.BLACK);

        UIManager.put("Slider.horizontalThumbIcon", new SEHorizontalSliderThumbIcon());
        UIManager.put("Slider.verticalThumbIcon", new SEVerticalSliderThumbIcon());
        /*
        Enumeration en = UIManager.getDefaults().keys();
        while(en.hasMoreElements()){
            Object ff = en.nextElement();
            System.out.println(""+ff+"  "+UIManager.getDefaults().get(ff));

        }
        */

        //  ToolTip
        Color tooltipBackground = new Color(0xAAFFFF);
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

        // activeBackground
        UIManager.put("adfActiveCaption", Color.RED);
        // activeForeground
        UIManager.put("adfActiveCaptionText", Color.BLUE);
        // activeShadow
        UIManager.put("AdfActiveCaptionBorder", Color.RED);

        UIManager.put("ModelTreeRenderer.redIcon", BuildUtils.getImageIcon(ICONS_DIR_CLASS_PATH + "tree_red.gif"));
        UIManager.put("ModelTreeRenderer.blackIcon", BuildUtils.getImageIcon(ICONS_DIR_CLASS_PATH + "tree_black.gif"));
        UIManager.put("ModelTreeRenderer.yellowIcon", BuildUtils.getImageIcon(ICONS_DIR_CLASS_PATH + "tree_yellow.gif"));

    }

    //   C r e a t i n g   M a i n   F r a m e

    @Override
    protected final void createMainFrame() {
        semMainFrame = SEMainFrame.createMainFrame();
        AdfEnv.putMainFrame(semMainFrame);

        String initialTitle = (String) System.getProperties().get(ConfigProperties.APP_TITLE_KEY);
        semMainFrame.setTitle(initialTitle);
        ImageIcon imageIcon = BuildUtils.getImageIcon(ICONS_DIR_CLASS_PATH + "title-logo.png");
        if (imageIcon != null) {
            Image image = imageIcon.getImage();
            semMainFrame.setIconImage(image);
        }

        semMainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        semMainFrame.addWindowListener(windowAdapter);

        // Setting Frame size and location

        int occupancyPercent = ConfigProperties.geIntProperty(ConfigProperties.OCCUPANCY_PERCENT_KEY, 0);
        boolean useOccupancyPercent = occupancyPercent != 0;
        if (useOccupancyPercent) {
            semMainFrame.initFrameSize(((double) occupancyPercent) / 100);
        } else {
            semMainFrame.setSize(DEFAULT_FRAME_SIZE);
            semMainFrame.setPreferredSize(DEFAULT_FRAME_SIZE);
        }
        semMainFrame.setMinimumSize(FRAME_MINIMUM_SIZE);
//        semMainFrame.setLocationRelativeTo(null);
        semMainFrame.setLocation(50,50);
    }

    //   S h o w i n g   s p l a s h

    @Override
    protected void showSplash() {
        semMainFrame = SEMainFrame.getInstance();

        String splashProperty = ConfigProperties.getStrConfigProperty("ui.splash", "false");
        if (splashProperty.equalsIgnoreCase("true")) {
            ImageIcon backgroundImageIcon = BuildUtils.getImageIcon(SPLASH_DIR_CLASS_PATH + "darck splash.png");
            ImageIcon darkcloseButtonIcon = BuildUtils.getImageIcon(SPLASH_DIR_CLASS_PATH + "close5.gif");
            ImageIcon brightcloseButtonIcon = BuildUtils.getImageIcon(SPLASH_DIR_CLASS_PATH + "close25.gif");
            String appVersionProperty = AppManifestAttributes.getAppVersion();
            appVersionProperty = "Version:  " + appVersionProperty;
            String appBuiltDateProperty = AppManifestAttributes.getAppBuiltDate();
            appBuiltDateProperty = "Built on:  " + appBuiltDateProperty;
            splash = new sem.appui.splash.Splash(semMainFrame, appVersionProperty, appBuiltDateProperty, backgroundImageIcon,
                    darkcloseButtonIcon, brightcloseButtonIcon);
            splash.setVisible(true);
            splash.toFront();
        }
        AdfEnv.put(AdfEnv.SPLASH_WINDOW_KEY, splash);
    }

    @Override
    protected void initModel() {
        spaceExplorerModel = SpaceExplorerModel.getInstance();
    }

    @Override
    protected void initUI() {
        ModelController.createSingleInstance(spaceExplorerModel);
        AppUIController.initialize();
        seMainPanel = new SEMainPanel();

        semMainFrame.setSemMainPanel(seMainPanel);
        seMainPanel.buildModelRepresentation(spaceExplorerModel);

        // new stuff
        ControllersBuilder.buildControllers();
        showMainFrame();
        seMainPanel.addComponentListener(frameComponentListener);
    }

    @Override
    protected void initController() {
        ControllersBuilder.buildMclnProject();
    }

    @Override
    protected final void initRelations() {
    }

    //   S h o w   M a i n   F r a m e

    @Override
    public void showMainFrame() {
        AdfEnv.getMainFrame().setVisible(true);
    }


    //   S t a r t i n g   T h e   A p p l i c a t i o n

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                SEMApp.createSEMApp();
            } catch (Throwable e) {
                System.out.println("SEApp.main: Throwable cought ! " + e.toString());
                System.out.println("SEApp.main: See Error Log for details.");
                e.printStackTrace();
            }
        });
    }
}

