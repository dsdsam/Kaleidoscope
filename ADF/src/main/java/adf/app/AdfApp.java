package adf.app;

import java.awt.Color;
import java.util.Properties;

import javax.swing.*;

import adf.mainframe.AdfMainFrame;
import adf.mainframe.MainPanel;
import adf.menu.AdfMenuAndToolbarBuilder;

public class AdfApp extends AdfEnv {

    private static final String CONFIG_FILE_LOCATION = "/adf-resources/config/";
    private static final String CONFIG_FILE_NAME = "config.properties";

    private static final Color APP_BACKGROUND = new Color(236, 233, 216);
    public static final boolean USE_CONFIG_FILE = true;

    //
    //   I n s t a n c e
    //

    protected boolean useConfigFile;
    private AdfEventQueue adfEventQueue;


    //  C o n s t r u c t i n g

    public AdfApp() {
        this(USE_CONFIG_FILE);
    }

    public AdfApp(boolean useConfigFile) {
        this.useConfigFile = useConfigFile;
        if (useConfigFile) {
            Properties adfConfig = ConfigProperties.initConfigProperties(CONFIG_FILE_LOCATION, CONFIG_FILE_NAME);
            ConfigProperties.logConfigProperties("ADF config properties", adfConfig);
        }
        appInitialization();
    }

    /**
     *
     */
    protected void appInitialization() {
        try {
            // application creation steps
            initUIManager();
            createMainFrame();
            showSplash();
            initController();
            initModel();
            initUI();
            initRelations();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //   C r e a t i o n   s t e p s

    /**
     *
     */
    private final void initUIManager() {
        String appTitle = ConfigProperties.getStrConfigProperty(ConfigProperties.APP_TITLE_KEY, "ADF");
        System.getProperties().put(ConfigProperties.APP_TITLE_KEY, appTitle);

        UIManager.put(MAIN_PANEL_BACKGROUND, APP_BACKGROUND);
    }

    /**
     *
     */
    private final void createMainFrame() {
        AdfMainFrame adfMainFrame = new AdfMainFrame();
        AdfEnv.putMainFrame(adfMainFrame);
    }

    /**
     *
     */
    private final void showSplash() {
    }

    private final void initController() {
    }

    /**
     *
     */
    private final void initModel() {
    }

    /**
     *
     */
    private final void initUI() {
        AdfMainFrame adfMainFrame = (AdfMainFrame) AdfEnv.getMainFrame();

        AdfMenuAndToolbarBuilder adfMenuAndToolbarBuilder = AdfMenuAndToolbarBuilder.getInstance();
        JMenuBar menuBar = adfMenuAndToolbarBuilder.buildMenuBur();
        adfMainFrame.setJMenuBar(menuBar);

        JPanel panel = new MainPanel();
        adfMainFrame.getContentPane().add(panel);
        adfMainFrame.initMainFrame(0.85);
        showMainFrame();
    }

    /**
     *
     */
    private final void initRelations() {
    }

    /**
     *
     */
    protected final void showMainFrame() {
        AdfEnv.getMainFrame().setVisible(true);
    }

    /**
     *
     */
    public void destroyApp() {
        AdfEnv.getMainFrame().dispose();
        AdfEnv.removeAllProperties();
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                UIManager.setLookAndFeel(lookAndFeel);
                boolean useConfigFile = true;
                AdfApp app = new AdfApp(useConfigFile);
            } catch (Throwable e) {
                System.out.println("AdfApp.main: Throwable cought !");
                System.out.println("AdfApp.main: See Error Log for details.");
                e.printStackTrace();
            }
        });
    }
}
