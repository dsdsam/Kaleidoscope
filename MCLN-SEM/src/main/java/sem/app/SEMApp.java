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

    private static final double OCCUPANCY_PERCENT = 0.80;
    private static final double LOCATION_PERCENT = 0.40;

    private static final String CONFIG_FILE_DIR_CLASS_PATH = "/sem-resources/config/";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static String ICONS_DIR_CLASS_PATH = "/sem-resources/images/app-icons/";
    public static String SPLASH_DIR_CLASS_PATH = "/sem-resources/images/splash/";


    //    private static Color SE_APP_BACKGROUND = new Color(236, 233, 216);
    private static Color SE_APP_BACKGROUND = new Color(236, 233, 216);

    private static SEMainFrame seMainFrame;

    //   I n s t a n c e

    private SpaceExplorerModel spaceExplorerModel;
    private SEMainPanel seMainPanel;
    private static Splash splash;

    private static final ComponentListener frameComponentListener = new ComponentListener() {
        /**
         * Invoked when the component's size changes.
         */
        public void componentResized(ComponentEvent e) {
            if (seMainFrame != null && seMainFrame.isVisible() && splash != null) {
//                splash.frameBoundsChanged(e.getComponent().getBounds());
            }
        }

        /**
         * Invoked when the component's position changes.
         */
        public void componentMoved(ComponentEvent e) {
            if (seMainFrame != null && seMainFrame.isVisible() && splash != null) {
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
    private Object lock;

    //
    //   C o n s t r u c t i n g
    //

    // made public to be accessible from Super Module
    public SEMApp() {
        super(false); // false means SE does not use ADF config properties
    }

    @Override
    protected void appInitialization() {

        // Initializing application version from jar's manifest
        AppManifestAttributes.initialize();

        try {

            Properties seConfigProperties = ConfigProperties.initConfigProperties(CONFIG_FILE_DIR_CLASS_PATH, CONFIG_FILE_NAME);
            ConfigProperties.logConfigProperties("Space Exploration Mission config properties", seConfigProperties);

            // application creation steps
            initUIManager();
            createMainFrame();
            showSplash();
            initController();
            initModel();
            initUI();

            ControllersBuilder.buildMclnProject();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //
    //   C r e a t i o n   s t e p s
    //

    protected void initUIManager() {
        String header = ConfigProperties.getStrConfigProperty(ConfigProperties.APP_TITLE_KEY, "SE");
        System.getProperties().put(ConfigProperties.APP_TITLE_KEY, header);

        UIManager.put(MAIN_PANEL_BACKGROUND, SE_APP_BACKGROUND);

        UIManager.put("Slider.majorTickLength", new Integer(3));
        UIManager.put("Slider.trackWidth", new Integer(10));
        UIManager.put("Slider.darkShadow", Color.BLACK);
        UIManager.put("Slider.shadow", Color.BLACK);
        UIManager.put("Slider.highlight", Color.BLACK);
        // UIManager.put("Slider.foreground", Color.BLACK );

//        UIManager.put("Slider.foreground", Color.BLUE );
//        Slider.majorTickLength

        UIManager.put("Slider.horizontalThumbIcon", new SEHorizontalSliderThumbIcon());
        UIManager.put("Slider.verticalThumbIcon", new SEVerticalSliderThumbIcon());
        /*
        Enumeration en = UIManager.getDefaults().keys();
        while(en.hasMoreElements()){
            Object ff = en.nextElement();
            System.out.println(""+ff+"  "+UIManager.getDefaults().get(ff));

        }
        */
/*
        trackWidth = ((Integer)UIManager.get( "Slider.trackWidth" )).intValue();
        tickLength = ((Integer)UIManager.get( "Slider.majorTickLength" )).intValue();
        horizThumbIcon = UIManager.getIcon( "Slider.horizontalThumbIcon" );
        vertThumbIcon = UIManager.getIcon( "Slider.verticalThumbIcon" );

    super.installUI( c );

        thumbColor = UIManager.getColor("Slider.thumb");
        highlightColor = UIManager.getColor("Slider.highlight");
        darkShadowColor = UIManager.getColor("Slider.darkShadow");

         highlightColor = UIManager.getColor("Slider.highlight");

        shadowColor = UIManager.getColor("Slider.shadow");
        focusColor = UIManager.getColor("Slider.focus");

*/
//      ToolTip
//        Color tooltipBackground = new Color(0xFFFFDD);
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

        /*
        UIManager.put( "RootPane.frameBorder",
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLoweredBevelBorder(),
                        BorderFactory.createRaisedBevelBorder()

                )

        );
        */
        /*
        UIManager.put( "RootPane.frameBorder",
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder( Color.BLACK, 1 ),
                                BorderFactory.createLineBorder( Color.ORANGE, 1 )
                                ),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder( Color.WHITE, 1 ),
                                BorderFactory.createLineBorder( Color.ORANGE, 1 )
                                )
                )

                );
        */

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

    private final void createMainFrame() {
        seMainFrame = SEMainFrame.createMainFrame();
        AdfEnv.putMainFrame(seMainFrame);
        seMainFrame.addWindowListener(windowAdapter);
    }

    //   S h o w i n g   s p l a s h

    protected void showSplash() {
        seMainFrame = SEMainFrame.getInstance();
        Dimension restOfScreen = seMainFrame.initMainFrame(OCCUPANCY_PERCENT);
        String splashProperty = ConfigProperties.getStrConfigProperty("ui.splash", "false");
        if (splashProperty.equalsIgnoreCase("true")) {
            ImageIcon backgroundImageIcon = BuildUtils.getImageIcon(SPLASH_DIR_CLASS_PATH + "darck splash.png");
            ImageIcon darkcloseButtonIcon = BuildUtils.getImageIcon(SPLASH_DIR_CLASS_PATH + "close5.gif");
            ImageIcon brightcloseButtonIcon = BuildUtils.getImageIcon(SPLASH_DIR_CLASS_PATH + "close25.gif");
            String appVersionProperty = AppManifestAttributes.getAppVersion();
            appVersionProperty = "Version:  " + appVersionProperty;
            String appBuiltDateProperty = AppManifestAttributes.getAppBuiltDate();
            appBuiltDateProperty = "Built on:  " + appBuiltDateProperty;
            splash = new sem.appui.splash.Splash(seMainFrame, appVersionProperty, appBuiltDateProperty, backgroundImageIcon,
                    darkcloseButtonIcon, brightcloseButtonIcon);
            splash.setVisible(true);
            splash.toFront();
        }
        AdfEnv.put(AdfEnv.SPLASH_WINDOW_KEY, splash);
    }

    public void initController() {
    }

    public void initModel() {
        spaceExplorerModel = SpaceExplorerModel.getInstance();
    }

    public void initUI() {
        ModelController.createSingleInstance(spaceExplorerModel);
        AppUIController.initialize();
        seMainPanel = new SEMainPanel();

        seMainFrame.init(seMainPanel);
        seMainPanel.buildModelRepresentation(spaceExplorerModel);

        // new stuff
        ControllersBuilder.buildControllers();
        showMainFrame();
        seMainPanel.addComponentListener(frameComponentListener);
    }

    //   S t a r t i n g   T h e   A p p l i c a t i o n

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                UIManager.setLookAndFeel(lookAndFeel);
                SEMApp app = new SEMApp();
            } catch (Throwable e) {
                System.out.println("SEApp.main: Throwable cought ! " + e.toString());
                System.out.println("SEApp.main: See Error Log for details.");
                e.printStackTrace();
            }
        });
    }
}

