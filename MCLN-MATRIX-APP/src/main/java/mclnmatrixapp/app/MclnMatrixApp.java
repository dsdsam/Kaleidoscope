package mclnmatrixapp.app;

import adf.app.AdfApp;
import adf.app.AdfEnv;
import adf.app.ConfigProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

/**
 * @author xpadmin
 */
public class MclnMatrixApp extends AdfApp {

    private static final Dimension DEFAULT_FRAME_SIZE = new Dimension(1200, 700);
    private static final Dimension FRAME_MINIMUM_SIZE = new Dimension(800, 400);

    private static final String CONFIG_FILE_LOCATION = "/mclnmatrixapp-resources/config/";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static String BASE_IMAGE_LOCATION = "/dsdsse-resources/images/app-icons/";
    public static String PREFIX = "/dsdsse-resources/images/splash/";

    private static final String FRAME_TITLE_KEY = "app.title";

    private static Color MCLN_MATRIX_APP_BACKGROUND = new Color(236, 233, 216);

    private static McLNMatrixFrame mclnMatrixFrame;

    private static final WindowAdapter windowAdapter = new WindowAdapter() {

        /**
         * Invoked when a window is in the process of being closed.
         * Like:
         * a) save frame size
         * b) save frame location
         * c) call application facility responsible for
         *    graceful exit.
         *
         * The close operation can be overridden at this point.
         */
        @Override
        public void windowClosing(WindowEvent e) {
        }


        /**
         * Invoked when a window is iconified.
         */
        @Override
        public void windowIconified(WindowEvent e) {
        }

        /**
         * Invoked when a window state is changed.
         *
         * @since 1.4
         */
        @Override
        public void windowStateChanged(WindowEvent e) {
        }
    };

    //
    //   C o n s t r u c t i n g
    //

    // made public to be accessible from Super Module
    private MclnMatrixApp() {
        super(false); // false means DSDSSE does not use ADF config properties
    }

    @Override
    protected void appInitialization() {
        System.out.println(System.lineSeparator() + "Initializing DSDS DSE instance.");
        super.appInitialization();
    }

    //
    //   C r e a t i o n   s t e p s
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
        Properties matrixConfig = ConfigProperties.initConfigProperties(CONFIG_FILE_LOCATION, CONFIG_FILE_NAME);
        ConfigProperties.logConfigProperties("McLN MATRIX config properties", matrixConfig);
    }

    @Override
    protected final void initUIManager() throws Exception {

        String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        UIManager.setLookAndFeel(lookAndFeel);

        String appTitle = ConfigProperties.getStrConfigProperty(ConfigProperties.APP_TITLE_KEY, "MATRIX");
        System.getProperties().put(ConfigProperties.APP_TITLE_KEY, appTitle);

        UIManager.put("mclnmatrixapp.ui.main.panel.background", MCLN_MATRIX_APP_BACKGROUND);

        Color yellowSelBg = new Color(0xFFFFDD);
        UIManager.put("ToolTip.background", yellowSelBg);

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
        mclnMatrixFrame = McLNMatrixFrame.createMainFrame();

        AdfEnv.putMainFrame(mclnMatrixFrame);

        String initialTitle = ConfigProperties.getStrConfigProperty(FRAME_TITLE_KEY, "Mcln Matrix App");
        mclnMatrixFrame.setTitle(initialTitle);

        mclnMatrixFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mclnMatrixFrame.addWindowListener(windowAdapter);

        // Setting Frame size and location

        int occupancyPercent = ConfigProperties.geIntProperty(ConfigProperties.OCCUPANCY_PERCENT_KEY, 50);
        boolean useOccupancyPercent = occupancyPercent != 0;
        if (useOccupancyPercent) {
            mclnMatrixFrame.initFrameSize(((double) occupancyPercent) / 100);
        } else {
            mclnMatrixFrame.setSize(DEFAULT_FRAME_SIZE);
            mclnMatrixFrame.setPreferredSize(DEFAULT_FRAME_SIZE);
        }
        mclnMatrixFrame.setMinimumSize(FRAME_MINIMUM_SIZE);
        mclnMatrixFrame.setLocationRelativeTo(null);
    }

    //   S h o w i n g   s p l a s h

    @Override
    protected final void showSplash() {
    }

    //   I n i t i a l i z i n g   M o d e l s

    @Override
    protected void initModel() {
    }

    //   I n i t i a l i z i n g   U I

    @Override
    protected void initUI() {
        AppMatrixViewMainPanel appMatrixViewMainPanel = AppMatrixViewMainPanel.createInstance(false);
        mclnMatrixFrame.getContentPane().add(appMatrixViewMainPanel);
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
    }

    //
    //   S t a r t i n g   T h e   A p p l i c a t i o n
    //

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MclnMatrixApp();
            } catch (Throwable e) {
                System.out.println("MclnMatrixApp.main: Throwable caught !");
                System.out.println("MclnMatrixApp.main: See Error Log for details.");
                e.printStackTrace();
            }
        });
    }
}
