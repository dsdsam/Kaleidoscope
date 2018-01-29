package led;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 4/18/13
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicLauncher {

    private static final String CONFIG_KEY_USER_NAME = "user.name";
    private static final String CONFIG_KEY_PASSWORD = "password";
    private static final String CONFIG_KEY_PANEL_WIDTH = "panel.width";
    private static final String CONFIG_KEY_PANEL_HEIGHT = "panel.height";
    private static final String CONFIG_KEY_LED_SIZE = "led.size";


    private Properties configProperties = new Properties();

    private String userName = "";
    private String password = "";
    private int panelWidth;
    private int panelHeight;
    private int ledSize;

    public boolean initProperties(String configFileName) {
        URL resourceURL = this.getClass().getResource(configFileName);
        if (resourceURL == null) {
            return false;
        }
        try {
            InputStream resourceInputStream = resourceURL.openStream();
            configProperties.load(resourceInputStream);
            configProperties.list(System.out);
        } catch (IOException e) {
            return false;
        }

        userName = configProperties.getProperty(CONFIG_KEY_USER_NAME);
        password = configProperties.getProperty(CONFIG_KEY_PASSWORD);

        String panelStrWidth = configProperties.getProperty(CONFIG_KEY_PANEL_WIDTH);
        panelWidth = stringToInt(panelStrWidth);
        String panelStrHeight = configProperties.getProperty(CONFIG_KEY_PANEL_HEIGHT);
        panelHeight = stringToInt(panelStrHeight);
        String ledStrSize = configProperties.getProperty(CONFIG_KEY_LED_SIZE);
        ledSize = stringToInt(ledStrSize);

        return true;
    }

    private int stringToInt(String strNumber) {
        int intNumber = 0;
        try {
            intNumber = Integer.parseInt(strNumber);
        } catch (NumberFormatException e) {

        }
        return intNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public int getLedSize() {
        return ledSize;
    }

    public String getProperty(String key, String defaultValue) {
        String value = (String) configProperties.get(key);
        return (value != null) ? value : defaultValue;
    }
}
