package adf.preferences;

import java.io.*;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Admin on 11/9/2015.
 */
public class AdfUserPreferences {

    protected static final Logger logger = Logger.getLogger(AdfUserPreferences.class.getName());

    public static final String USER_HOME = "user.home";
    public static final String USER_DIR = "user.dir";

    // System properties
    public static final String USER_DATA_HOME = System.getProperty(USER_HOME);
    public static final String INSTALLATION_DIRECTORY = System.getProperty(USER_DIR);

    // Config property keys

    public static final String MAIN_FRAME_X_LOCATION = "main.frame.x.location";
    public static final String MAIN_FRAME_Y_LOCATION = "main.frame.y.location";

    public static final String MAIN_FRAME_WIDTH = "main.frame.width";
    public static final String MAIN_FRAME_HEIGHT = "main.frame.height";

    //    I n s t a n c e

    private Properties localUserPreferences = new Properties();

    public AdfUserPreferences(String fileName) {
        localUserPreferences.clear();
        localUserPreferences = loadPreferencesFile(USER_DATA_HOME, fileName, true);
        logProperties("User Preferences loaded", localUserPreferences);
    }

    /**
     * set Int preference
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setUserPreference(String key, int value) {
        return setUserPreference(key, String.valueOf(value));
    }

    /**
     * set boolean preference
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setBooleanPreference(String key, boolean value) {
        return setUserPreference(key, value ? "true" : "false");
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBooleanPreference(String key, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        try {
            String value = this.getProperty(key, defaultValue ? "true" : "false");
            booleanValue = Boolean.valueOf(value);
        } catch (Exception e) {

        }
        return booleanValue;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public boolean setUserPreference(String key, String value) {
        if (key == null) {
            logger.severe("key is null, not setting property");
            return false;
        }
        if (value == null) {
            logger.severe("value is null, not setting property");
            return false;
        }

        value = value.trim();
        localUserPreferences.put(key, value);
        return true;
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public int getIntProperty(String key, String defaultValue) {
        int intValue = 0;
        try {
            String value = this.getProperty(key, defaultValue);
            intValue = Integer.parseInt(value);
        } catch (Exception e) {

        }
        return intValue;
    }


    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        String value = this.getProperty(key);
        return (null != value) ? value : defaultValue;
    }

    /**
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return (String) localUserPreferences.get(key);
    }

    /**
     * @param dirPath
     * @param file
     * @param skipBlankValues
     * @return
     */
    private static Properties loadPreferencesFile(String dirPath, String file, boolean skipBlankValues) {
        Properties loadedPreferences = new Properties();
        File preferencesFile = new File(dirPath, file);
        try (FileInputStream inStream = new FileInputStream(preferencesFile)) {
            Properties p = new Properties();
            p.load(inStream);
//            logger.severe(file + " loaded from " + dirPath);
            Set keys = p.keySet();
            for (Object k : keys) {
                String key = (String) k;
                String value = (String) p.get(key);
                // ignore blanks or empty lines
                if (skipBlankValues) {
                    if (value != null && !value.trim().isEmpty()) {
                        loadedPreferences.setProperty(key, value);
                    } else {
                        logger.severe(String.format("Ignoring property load for key=[%s] value=[%s]", key, value));
                    }
                } else {
                    loadedPreferences.setProperty(key, value);
                }
            }
        } catch (Exception e) {
            logger.severe("Couldn't load " + file + " from " + dirPath + ": " + e.getMessage());
        }
        return loadedPreferences;
    }

    /**
     * @param header
     * @param properties
     */
    private static void logProperties(String header, Properties properties) {
        Set keys = properties.keySet();
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + header);
        sb.append("\nProperties as key-value pairs, entries size = " + keys.size());
        for (Object key : keys) {
            String value = (String) properties.get(key);
            sb.append("\n" + key + "  " + value);
        }
        sb.append("\n------\n");
        logger.severe(sb.toString());
    }


    /**
     *
     */
    public void saveUserPreferences(String fileName) {
        File preferencesFile = new File(USER_DATA_HOME, fileName);
        if (!preferencesFile.exists()) {
            preferencesFile.getParentFile().mkdirs();
        }
//        if (!preferencesFile.canWrite()) {
//            logger.severe("Cannot write to local user preferences to " + preferencesFile.getAbsolutePath());
//            return;
//        }

//        logger.info("Saving user preferences to " + preferencesFile.getAbsolutePath());
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(preferencesFile))) {
            this.localUserPreferences.store(out, "");
//            logProperties("Saved preferences", userPreferences);
        } catch (IOException ioe) {
            logProperties("Not saved preferences", localUserPreferences);
            logger.log(Level.WARNING, String.format("Couldn't write user preferences to %s!",
                    preferencesFile.getAbsolutePath()), ioe);
        }
    }
}
