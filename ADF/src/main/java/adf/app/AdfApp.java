package adf.app;

import adf.mainframe.AdfMainFrame;
import adf.mainframe.MainPanel;
import adf.menu.AdfMenuAndToolbarBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Properties;

public class AdfApp extends AdfEnv {

    private static final String CONFIG_FILE_LOCATION = "/adf-resources/config/";
    private static final String CONFIG_FILE_NAME = "config.properties";
    public static final boolean USE_CONFIG_FILE = false;

    private static final Color APP_BACKGROUND = new Color(236, 233, 216);

    //
    //   I n s t a n c e
    //

    protected boolean useConfigFile;

    //  C o n s t r u c t i n g

    protected AdfApp() {
        this(USE_CONFIG_FILE);
    }

    public AdfApp(boolean useConfigFile) {
        this.useConfigFile = useConfigFile;
        appInitialization();
    }

    /**
     *
     */
    protected void appInitialization() {
        try {
            // application initialization steps
            initConfig();
            initLocale();
            initFonts();
            initUIManager();
            createMainFrame();
            showSplash();
            initModel();
            initUI();
            initController();
            initRelations();
            showMainFrame();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //
    //   I n i t i a l i z a t i o n   s t e p s
    //

    protected void initConfig() {
        if (useConfigFile) {
            Properties adfConfig = ConfigProperties.initConfigProperties(CONFIG_FILE_LOCATION, CONFIG_FILE_NAME);
            ConfigProperties.logConfigProperties("ADF config properties", adfConfig);
        }
    }

    protected void initLocale() {
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
    }

    protected void initFonts() {
        AdfEnv.loadAndRegisterFont("calibrib.ttf");
        AdfEnv.loadAndRegisterFont("calibri.ttf");
        AdfEnv.loadAndRegisterFont("roboto/Roboto-Italic.ttf");
        System.setProperty("java.util.prefs.PreferencesFactory", "adf.utils.preferences.AppUserPreferencesFactory");
    }

    protected void initUIManager() throws Exception {

        String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        UIManager.setLookAndFeel(lookAndFeel);

        String appTitle = ConfigProperties.getStrConfigProperty(ConfigProperties.APP_TITLE_KEY, "ADF");
        System.getProperties().put(ConfigProperties.APP_TITLE_KEY, appTitle);
        UIManager.put(MAIN_PANEL_BACKGROUND, APP_BACKGROUND);
    }


    protected void createMainFrame() {
        AdfMainFrame adfMainFrame = AdfMainFrame.createMainFrame();
        AdfEnv.putMainFrame(adfMainFrame);
    }

    protected void showSplash() {
    }

    protected void initModel() {
    }

    protected void initUI() {
        AdfMainFrame adfMainFrame = (AdfMainFrame) AdfEnv.getMainFrame();

        AdfMenuAndToolbarBuilder adfMenuAndToolbarBuilder = AdfMenuAndToolbarBuilder.getInstance();
        JMenuBar menuBar = adfMenuAndToolbarBuilder.buildMenuBur();
        adfMainFrame.setJMenuBar(menuBar);

        JPanel panel = new MainPanel();
        adfMainFrame.getContentPane().add(panel);
        adfMainFrame.initMainFrame();
        adfMainFrame.initFrameSize(0.60);
        showMainFrame();
    }

    protected void initController() {
    }

    protected void initRelations() {
    }

    protected void showMainFrame() {
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
                boolean useConfigFile = true;
                new AdfApp(useConfigFile);
            } catch (Throwable e) {
                System.out.println("AdfApp.main: Throwable caught !");
                System.out.println("AdfApp.main: See Error Log for details.");
                e.printStackTrace();
            }
        });
    }
}
