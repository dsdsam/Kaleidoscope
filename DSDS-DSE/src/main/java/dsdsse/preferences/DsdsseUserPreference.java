package dsdsse.preferences;

import adf.preferences.AdfPreferenceGroupsSupport;
import adf.preferences.AdfUserPreferences;
import adf.preferences.GroupChangeListener;

import java.awt.*;

/**
 * Created by Admin on 11/9/2015.
 */
public class DsdsseUserPreference extends AdfPreferenceGroupsSupport {

    private final static String USER_PREFERENCES_FILE_NAME = "AppData/DSDSSE/user.properties";

    // Sec 000
    public static final String PREF_WELCOME_POPUP_STATE_KEY = "WelcomePopupState";
    // Sec 001
    public static final String PREF_VIEW_STYLE_KEY = "PreferenceViewStyle";
    public static final String PREF_PROPERTY_VIEW_SIZE_KEY = "PropertyViewSizePrefKey";
    // Sec 002
    public static final String PREF_GRID_VISIBLE_KEY = "Grid Visible";
    public static final String PREF_AXES_VISIBLE_KEY = "Axes Visible";
    public static final String PREF_PS_VISIBLE_KEY = "Project Space Visible";
    // Sec 003
    public static final String PREF_IA_EMBEDDED_KEY = "IA Embedded";
    // Sec 004
    public static final String PREF_STATE_DISPLAY_SIZE_KEY = "State display size";
    // Sec 005
    public static final String PREF_CANCELLATION_POLICY_KEY = "Cancellation policy";

    private static DsdsseUserPreference dsdsseUserPreferences = new DsdsseUserPreference();
    private static AdfUserPreferences adfUserPreferences;

    public static synchronized DsdsseUserPreference getInstance() {
        return dsdsseUserPreferences;
    }

    private static boolean axesVisibleRunTimePreference;
    private static boolean projectSpaceDetailsVisibleRunTimePreference;
    private static boolean gridVisibleRunTimePreference;

    public static void toggleProjectSpaceAxesVisibility() {
        axesVisibleRunTimePreference ^= true;
    }

    public static void toggleProjectSpaceDetailsVisible() {
        projectSpaceDetailsVisibleRunTimePreference ^= true;
    }

    public static void toggleGridVisibility() {
        gridVisibleRunTimePreference ^= true;
    }

    private DsdsseUserPreference() {
        super(GroupID.class);
        adfUserPreferences = new AdfUserPreferences(USER_PREFERENCES_FILE_NAME);
        dsdsseUserPreferences = this;
    }

    //
    //   Static API
    //

    public static final void setAppLocation(Rectangle appViewRectangle) {
        adfUserPreferences.setUserPreference(AdfUserPreferences.MAIN_FRAME_X_LOCATION, appViewRectangle.x);
        adfUserPreferences.setUserPreference(AdfUserPreferences.MAIN_FRAME_Y_LOCATION, appViewRectangle.y);
        adfUserPreferences.saveUserPreferences(USER_PREFERENCES_FILE_NAME);

        adfUserPreferences.setUserPreference(AdfUserPreferences.MAIN_FRAME_WIDTH, appViewRectangle.width);
        adfUserPreferences.setUserPreference(AdfUserPreferences.MAIN_FRAME_HEIGHT, appViewRectangle.height);
        adfUserPreferences.saveUserPreferences(USER_PREFERENCES_FILE_NAME);
    }

    public static final Point getAppLocation() {
        int xLocation = adfUserPreferences.getIntProperty(AdfUserPreferences.MAIN_FRAME_X_LOCATION, "0");
        int yLocation = adfUserPreferences.getIntProperty(AdfUserPreferences.MAIN_FRAME_Y_LOCATION, "0");
        return new Point(xLocation, yLocation);
    }

    public static final Dimension getAppSize() {
        int frameWidth = adfUserPreferences.getIntProperty(AdfUserPreferences.MAIN_FRAME_WIDTH, "0");
        int frameHeight = adfUserPreferences.getIntProperty(AdfUserPreferences.MAIN_FRAME_HEIGHT, "0");
        return new Dimension(frameWidth, frameHeight);
    }

    public static final boolean isPropertyView3D() {
        boolean propertyViewIs3D = adfUserPreferences.getBooleanPreference(PREF_VIEW_STYLE_KEY, true);
        return propertyViewIs3D;
    }

    public static final int getPropertyBallSize() {
        String propertyBallSize = adfUserPreferences.getProperty(PREF_PROPERTY_VIEW_SIZE_KEY,
                PropertyViewDisplaySizePrefPanel.MEDIUM_VALUE);
        switch (propertyBallSize) {
            case PropertyViewDisplaySizePrefPanel.SMALL_VALUE:
                return 7;
            case PropertyViewDisplaySizePrefPanel.MEDIUM_VALUE:
                return 9;
            case PropertyViewDisplaySizePrefPanel.LARGE_VALUE:
                return 11;
            case PropertyViewDisplaySizePrefPanel.XLARGE_VALUE:
                return 13;
        }
        return 9;
    }

    public static final boolean isWelcomePopupOn() {
        boolean gridIsVisible = adfUserPreferences.getBooleanPreference(PREF_WELCOME_POPUP_STATE_KEY, true);
        return gridIsVisible;
    }

    public static final boolean isGriVisible() {
        boolean gridIsVisible = adfUserPreferences.getBooleanPreference(PREF_GRID_VISIBLE_KEY, true);
        return gridIsVisible ? gridIsVisible && !gridVisibleRunTimePreference :
                !gridIsVisible && gridVisibleRunTimePreference;
    }

    public static final boolean areAxesVisible() {
        boolean axesAreVisible = adfUserPreferences.getBooleanPreference(PREF_AXES_VISIBLE_KEY, true);
        return axesAreVisible ? axesAreVisible && !axesVisibleRunTimePreference :
                !axesAreVisible && axesVisibleRunTimePreference;
    }

    public static final boolean isProjectSpaceRectangleVisible() {
        boolean psRectangleVisible = adfUserPreferences.getBooleanPreference(PREF_PS_VISIBLE_KEY, true);
        return psRectangleVisible ? psRectangleVisible && !projectSpaceDetailsVisibleRunTimePreference :
                !psRectangleVisible && projectSpaceDetailsVisibleRunTimePreference;
    }

    public static final boolean isInitAssistantEmbedded() {
        boolean iaIsEmbedded = adfUserPreferences.getBooleanPreference(PREF_IA_EMBEDDED_KEY, true);
        return iaIsEmbedded;
    }

    public static final int getPropertyStateDisplaySize() {
        String propertyStateDisplaySize = adfUserPreferences.getProperty(PREF_STATE_DISPLAY_SIZE_KEY,
                PropertyStateDisplaySizePrefPanel.LARGE_VALUE);
        switch (propertyStateDisplaySize) {
            case PropertyStateDisplaySizePrefPanel.SMALL_VALUE:
                return 4;
            case PropertyStateDisplaySizePrefPanel.MEDIUM_VALUE:
                return 6;
            case PropertyStateDisplaySizePrefPanel.LARGE_VALUE:
                return 8;
            case PropertyStateDisplaySizePrefPanel.XLARGE_VALUE:
                return 10;
        }
        return 6;
    }

    public static final boolean isUserPreferenceConfirmationPolicy() {
        String selectedCancellationPolicy = adfUserPreferences.getProperty(PREF_CANCELLATION_POLICY_KEY,
                PropertyCancellationPolicyPrefPanel.CONFIRMATION_VALUE);
        boolean selectedPolicyIsConfirmation =
                selectedCancellationPolicy.equalsIgnoreCase(PropertyCancellationPolicyPrefPanel.CONFIRMATION_VALUE);
        return selectedPolicyIsConfirmation;
    }

    public static final boolean isUserPreferenceSilentCancellationPolicy() {
        String selectedSilentCancellationPolicy = adfUserPreferences.getProperty(PREF_CANCELLATION_POLICY_KEY,
                PropertyCancellationPolicyPrefPanel.SILENT_VALUE);
        boolean selectedPolicyIsSilentCancellation =
                selectedSilentCancellationPolicy.equalsIgnoreCase(PropertyCancellationPolicyPrefPanel.SILENT_VALUE);
        return selectedPolicyIsSilentCancellation;
    }

    /**
     * @param preferenceName
     * @param preferenceValue
     */
    public static final void setStringPreference(String preferenceName, String preferenceValue) {
        adfUserPreferences.setUserPreference(preferenceName, preferenceValue);
        adfUserPreferences.saveUserPreferences(USER_PREFERENCES_FILE_NAME);
        dsdsseUserPreferences.firePreferenceChanged(preferenceName);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static final String getStringPreference(String key, String defaultValue) {
        String retrievedValue = defaultValue;
        try {
            retrievedValue = adfUserPreferences.getProperty(key, defaultValue);
        } catch (Exception e) {

        }
        return retrievedValue;
    }


    /**
     * set boolean preference
     *
     * @param preferenceName
     * @param preferenceValue
     * @return
     */
    public static final void setBooleanPreference(String preferenceName, boolean preferenceValue) {
        adfUserPreferences.setUserPreference(preferenceName, preferenceValue ? "true" : "false");
        adfUserPreferences.saveUserPreferences(USER_PREFERENCES_FILE_NAME);
        dsdsseUserPreferences.firePreferenceChanged(preferenceName);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static final boolean getBooleanPreference(String key, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        try {
            String value = adfUserPreferences.getProperty(key, defaultValue ? "true" : "false");
            booleanValue = Boolean.valueOf(value);
        } catch (Exception e) {

        }
        return booleanValue;
    }

    public static final void saveUserPreferences() {
        adfUserPreferences.saveUserPreferences(USER_PREFERENCES_FILE_NAME);
    }

    public void addGroupChangeListener(String pref, Enum groupID, GroupChangeListener groupChangeListener) {
        super.addGroupChangeListener(pref, groupID, groupChangeListener);
    }

    public void removeGroupChangeListener(Enum groupID, GroupChangeListener groupChangeListener) {
        super.removeGroupChangeListener(groupID, groupChangeListener);
    }

    public void firePreferenceChanged(String preferenceName) {
        super.firePreferenceChanged(preferenceName);
    }

}
