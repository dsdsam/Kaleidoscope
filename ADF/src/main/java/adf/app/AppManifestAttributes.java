package adf.app;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Logger;

public class AppManifestAttributes {

    private static final Logger logger = Logger.getLogger(AppManifestAttributes.class.getName());

    private static final String APP_VERSION_KEY = "Application-Version";
    private static final String APP_BUILD_DATE_KEY = "Built-Date";

    private static String APPLICATION_ATTRIBUTE_NOT_INITIALIZED = "Not Initialized";
    private static String APPLICATION_VERSION_DEVELOPMENT = "Development";
    private static String BUILD_DATE_NOT_DEFINED = "Not defined";

    private static Manifest manifest;
    private static Attributes attributes;
    private static boolean initialized;

    public static final void initialize() {
        assert !initialized : "Application Manifest already initialized";
        try {

            String className = AppManifestAttributes.class.getName();
            className = className.replace(".", "/") + ".class";
            URL resourceURL = AppManifestAttributes.class.getClassLoader().getResource(className);
            URLConnection openConnection = resourceURL.openConnection();

            if (!(openConnection instanceof JarURLConnection)) {
                logger.severe("Application started not from Jar. Manifest not retrieved.");
                return;
            }

            if (openConnection instanceof JarURLConnection) {
                JarURLConnection jarConnection = (JarURLConnection) openConnection;
                manifest = jarConnection.getManifest();
                if (manifest == null) {
                    logger.severe("Application Manifest not retrieved.");
                    return;
                }
                attributes = manifest.getMainAttributes();
                if (attributes == null) {
                    logger.severe("Application Manifest Attributes not retrieved.");
                    return;
                }
                logger.severe("Application Manifest retrieved: " + manifest);
                initialized = true;
            }

        } catch (Throwable e) {
            logger.severe("Cannot read Application Manifest.");
        }
    }

    public static final String getAppVersion() {
        if (!initialized) {
            return APPLICATION_VERSION_DEVELOPMENT;
        }
        String appVersion = attributes.getValue(APP_VERSION_KEY);
        appVersion = (appVersion != null && !appVersion.isEmpty()) ? appVersion : APPLICATION_ATTRIBUTE_NOT_INITIALIZED;
        return appVersion;
    }

    public static final String getAppBuiltDate() {
        if (!initialized) {
            return BUILD_DATE_NOT_DEFINED;
        }
        String buildDate = attributes.getValue(APP_BUILD_DATE_KEY);
        buildDate = (buildDate != null && !buildDate.isEmpty()) ? buildDate : APPLICATION_ATTRIBUTE_NOT_INITIALIZED;
        return buildDate;
    }
}
