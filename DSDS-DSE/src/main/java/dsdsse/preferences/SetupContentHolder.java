package dsdsse.preferences;

import adf.app.StandardFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 8/23/2016.
 */
public class SetupContentHolder extends JPanel {

    // Sec 000
    private static final String SECTION_000_KEY = "Section 000";
    private static final String SECTION_000_VAL = "Welcome Popup";
    private static final String PREF_WELCOME_POPUP_LBL = " -  Welcome Popup On";

    // Sec 001
    private static final String SECTION_001_KEY = "Section 001";
    private static final String SECTION_001_VAL = "McLN Display";
    private static final String PREF_VIEW_STYLE_LBL = " -  Draw \"Property\" as 3D ball";

    // Sec 002
    private static final String SECTION_002_KEY = "Section 002";
    private static final String SECTION_002_VAL = "Design Space";
    private static final String PREF_GRID_VISIBLE_LBL = " -  Grid Visible";
    private static final String PREF_AXES_VISIBLE_LBL = " -  Axes Visible";
    private static final String PREF_PS_VISIBLE_LBL = " -  Project Space Rectangle Visible";

    // Sec 003
    private static final String SECTION_003_KEY = "Section 003";
    private static final String SECTION_003_VAL = "Initialization Assistant";
    private static final String PREF_IA_EMBEDDED_LBL = " -  Initialization Assistant Embedded";

    // Sec 004
    private static final String SECTION_004_KEY = "Section 004";
    private static final String SECTION_004_VAL = "Simulation Trace Log";

    // Sec 005
    private static final String SECTION_005_KEY = "Section 005";
    private static final String SECTION_005_VAL = "Modification Cancellation Policy";

    private static final String[][][] setupPreferencesStructure = {
            {
                    {SECTION_000_KEY, SECTION_000_VAL},
                    {DsdsseUserPreference.PREF_WELCOME_POPUP_STATE_KEY, PREF_WELCOME_POPUP_LBL,}
            },
            {
                    {SECTION_001_KEY, SECTION_001_VAL},
                    {
                            DsdsseUserPreference.PREF_VIEW_STYLE_KEY, PREF_VIEW_STYLE_LBL,
                            DsdsseUserPreference.PREF_PROPERTY_VIEW_SIZE_KEY, PropertyViewDisplaySizePrefPanel.class.getName(),
                    }
            },
            {
                    {SECTION_002_KEY, SECTION_002_VAL},
                    {
                            DsdsseUserPreference.PREF_GRID_VISIBLE_KEY, PREF_GRID_VISIBLE_LBL,
                            DsdsseUserPreference.PREF_AXES_VISIBLE_KEY, PREF_AXES_VISIBLE_LBL,
                            DsdsseUserPreference.PREF_PS_VISIBLE_KEY, PREF_PS_VISIBLE_LBL,
                    },
            },
            {
                    {SECTION_003_KEY, SECTION_003_VAL},
                    {DsdsseUserPreference.PREF_IA_EMBEDDED_KEY, PREF_IA_EMBEDDED_LBL},
            },
            {
                    {SECTION_004_KEY, SECTION_004_VAL},
                    {DsdsseUserPreference.PREF_STATE_DISPLAY_SIZE_KEY, PropertyStateDisplaySizePrefPanel.class.getName(),}
            },
            {
                    {SECTION_005_KEY, SECTION_005_VAL},
                    {DsdsseUserPreference.PREF_CANCELLATION_POLICY_KEY, PropertyCancellationPolicyPrefPanel.class.getName(),}
            },

    };

    // needed for debugging
    private static void printPreferencesStructure(String[][][] setupPreferencesStructure) {
        for (int i = 0; i < setupPreferencesStructure.length; i++) {
            System.out.println("");

            String[][] section = setupPreferencesStructure[i];
            String sectionKey = section[0][0];
            String sectionLbl = section[0][1];
            System.out.println("#");
            System.out.println("#  Section Name: " + sectionLbl);
            System.out.println("#");

            String[] sectionAttributes = section[1];
            System.out.println("");
            for (int j = 0; j < sectionAttributes.length; j += 2) {
                String attrKey = sectionAttributes[j];
                String attrVal = sectionAttributes[j + 1];
                System.out.println(attrKey + " = " + attrVal);
            }
        }
    }

    private static final Dimension CHECKBOX_SIZE = new Dimension(1, 14);
    private static final Color SECTION_LABEL_BACKGROUND_COLOR = new Color(0xCDC77B);

    private static final Font CHECKBOX_TEXT_FONT = StandardFonts.FONT_HELVETICA_PLAIN_12;
    private static final Font SECTION_LABEL_FONT = StandardFonts.FONT_DIALOG_BOLD_11;

    private static final Map<String, String> preferenceKeyToPreferenceLabel = new HashMap();

    private PropertyViewDisplaySizePrefPanel propertyViewDisplaySizePrefPanel;
    private PropertyStateDisplaySizePrefPanel propertyStateDisplaySizePrefPanel;
    private PropertyCancellationPolicyPrefPanel propertyCancellationPolicyPrefPanel;


    /**
     *
     */
    SetupContentHolder() {
        super(new GridBagLayout());
        setBorder(new EmptyBorder(10, 0, 10, 0));
        buildMaps(setupPreferencesStructure);
        initLayout();
    }

    /**
     * @param setupPreferencesStructure
     */
    private void buildMaps(String[][][] setupPreferencesStructure) {
        preferenceKeyToPreferenceLabel.clear();
        for (int i = 0; i < setupPreferencesStructure.length; i++) {
            String[][] section = setupPreferencesStructure[i];
            String sectionKey = section[0][0];
            String sectionLbl = section[0][1];
            preferenceKeyToPreferenceLabel.put(sectionKey, sectionLbl);
            String[] sectionAttributes = section[1];
            for (int j = 0; j < sectionAttributes.length; j += 2) {
                String prefsKey = sectionAttributes[j];
                String prefsLbl = sectionAttributes[j + 1];
                preferenceKeyToPreferenceLabel.put(prefsKey, prefsLbl);
            }
        }
    }

    /**
     *
     */
    private void initLayout() {
        int rowCnt = 0;
        for (int i = 0; i < setupPreferencesStructure.length; i++) {
            String[][] section = setupPreferencesStructure[i];
            String sectionKey = section[0][0];
            String sectionLblTxt = preferenceKeyToPreferenceLabel.get(sectionKey);
            JLabel sectionLabel = new JLabel(sectionLblTxt, JLabel.CENTER);
            sectionLabel.setBorder(new EmptyBorder(1, 0, 1, 0));
            sectionLabel.setFont(SECTION_LABEL_FONT);
            sectionLabel.setOpaque(true);
            sectionLabel.setBackground(SECTION_LABEL_BACKGROUND_COLOR);
            sectionLabel.setForeground(Color.DARK_GRAY);
            add(sectionLabel, new GridBagConstraints(0, rowCnt++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 6, 5, 6), 0, 0));

            String[] sectionAttributes = section[1];
            for (int j = 0; j < sectionAttributes.length; j += 2) {
                String prefsKey = sectionAttributes[j];
                String prefsLbl = preferenceKeyToPreferenceLabel.get(prefsKey);

                if (prefsKey.equals(DsdsseUserPreference.PREF_PROPERTY_VIEW_SIZE_KEY)) {
                    try {
                        Class<?> clazz = Class.forName(prefsLbl);
                        propertyViewDisplaySizePrefPanel = (PropertyViewDisplaySizePrefPanel) clazz.newInstance();
                        add(propertyViewDisplaySizePrefPanel, new GridBagConstraints(0, rowCnt++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                GridBagConstraints.HORIZONTAL, new Insets(0, 20, 5, 20), 0, 0));
                    } catch (Exception e) {
                        continue;
                    }
                } else if (prefsKey.equals(DsdsseUserPreference.PREF_STATE_DISPLAY_SIZE_KEY)) {
                    try {
                        Class<?> clazz = Class.forName(prefsLbl);
                        propertyStateDisplaySizePrefPanel = (PropertyStateDisplaySizePrefPanel) clazz.newInstance();
                        add(propertyStateDisplaySizePrefPanel, new GridBagConstraints(0, rowCnt++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                GridBagConstraints.HORIZONTAL, new Insets(0, 20, 5, 20), 0, 0));
                    } catch (Exception e) {
                        continue;
                    }
                } else if (prefsKey.equals(DsdsseUserPreference.PREF_CANCELLATION_POLICY_KEY)) {
                    try {
                        Class<?> clazz = Class.forName(prefsLbl);
                        propertyCancellationPolicyPrefPanel = (PropertyCancellationPolicyPrefPanel) clazz.newInstance();
                        add(propertyCancellationPolicyPrefPanel, new GridBagConstraints(0, rowCnt++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                GridBagConstraints.HORIZONTAL, new Insets(0, 20, 5, 20), 0, 0));
                    } catch (Exception e) {
                        continue;
                    }
                } else {
                    boolean prefsValue = DsdsseUserPreference.getBooleanPreference(prefsKey, true);
                    JCheckBox checkBox = new SetupCheckBox(prefsLbl, prefsValue, prefsKey);
                    checkBox.setPreferredSize(CHECKBOX_SIZE);
                    checkBox.setOpaque(false);
                    checkBox.setFocusPainted(false);
                    checkBox.setFont(CHECKBOX_TEXT_FONT);
                    add(checkBox, new GridBagConstraints(0, rowCnt++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(0, 20, 5, 20), 0, 0));
                }
            }
        }
        JLabel line = new JLabel();
        line.setOpaque(true);
        line.setBackground(SECTION_LABEL_BACKGROUND_COLOR);
        line.setPreferredSize(new Dimension(1, 2));
        add(line, new GridBagConstraints(0, rowCnt++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 20, 5, 20), 0, 0));

        //   Space Holder
        add(Box.createVerticalGlue(), new GridBagConstraints(0, rowCnt, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     *
     */
    private class SetupCheckBox extends JCheckBox {
        SetupCheckBox(String text, boolean selected, String prefsKey) {
            super(text, selected);
            this.addItemListener((e) -> {
                boolean status = e.getStateChange() == ItemEvent.SELECTED;
                System.out.println("status " + status + "  " + prefsKey);
                DsdsseUserPreference.setBooleanPreference(prefsKey, status);
            });
        }
    }
}
