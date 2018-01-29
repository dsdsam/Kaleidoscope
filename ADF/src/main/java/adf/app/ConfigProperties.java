package adf.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Admin on 10/3/2016.
 */
public class ConfigProperties {

    public static final Logger logger = Logger.getLogger(ConfigProperties.class.getName());

    // Config file keys
    public static final String APP_TITLE_KEY = "app.title";

    private static Properties appConfigProps = new Properties();

    /**
     * Initializes configuration file key/value pairs
     * Used by DSDSSE and SE
     *
     * @param appPrefix
     * @param propertyFileName
     * @return
     */
    public static Properties initConfigProperties(String appPrefix, String propertyFileName) {
        appConfigProps.clear();
        String fileLocation = appPrefix + propertyFileName;
        logger.info("Config file classpath: " + fileLocation);
        InputStream inpStream = ConfigProperties.class.getResourceAsStream(fileLocation);
        if (inpStream == null) {
            logger.log(Level.SEVERE, ("Config file not found, inpStream is null, filename = " + fileLocation),
                    new IOException());
            return appConfigProps;
        }
        try {
            appConfigProps.load(inpStream);
        } catch (IOException e) {
            // default empty properties will be used
            logger.log(Level.SEVERE, ("Config properties not loaded, filename = " + fileLocation), e);
        }
        return appConfigProps;
    }

    /**
     * Used by DSDSSE and SE
     *
     * @param header
     * @param appConfigProps
     */
    public static void logConfigProperties(String header, Properties appConfigProps) {
        if (!logger.isLoggable(Level.INFO)) {
            return;
        }
        StringBuilder buf = new StringBuilder();
        buf.append("\n\n").append(header).append(": size = " + appConfigProps.size() + "\n");
        buf.append("========================================\n");
        Enumeration enumeration = appConfigProps.keys();
        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement();
            Object val = appConfigProps.get(key);
            buf.append("\t");
            buf.append(key);
            buf.append(" : ");
            buf.append(val);
            buf.append("\n");
        }
        buf.append("========================================\n");
        String msg = buf.toString();
        logger.log(Level.INFO, msg);
        return;
    }

    /**
     * @param key - the needed property key
     */
    public static String getStrConfigProperty(String key) {
        return appConfigProps.getProperty(key);
    }

    /**
     * @param key - the needed property key
     */
    public static String getStrConfigProperty(String key, String def) {
        return appConfigProps.getProperty(key, def);
    }

    /**
     *
     */
    public static int getIntConfigProperty(String key) {
        String value = getStrConfigProperty(key);
        return Integer.parseInt(value);
    }

}
