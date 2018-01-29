package dsdsse.preferences;

import adf.app.StandardFonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 2/23/2017.
 */
public class PropertyCancellationPolicyPrefPanel extends JPanel {

    public static final String SILENT_VALUE = "Silent";
    public static final String CONFIRMATION_VALUE = "Confirmation";

    private static final Color FOREGROUND = new Color(0x333333);
    private static final Font RADIO_BUTTON_FONT = StandardFonts.FONT_DIALOG_PLAIN_11;
    private static final Border border = new EmptyBorder(0, 0, 0, 0);
    private static final ButtonGroup btnGroup = new ButtonGroup();

    private static final String SILENT_ITEM = " -  Silent cancellation";
    private static final String CONFIRMATION_ITEM = " -  User confirmation required";

    private final static Map<String, String> propertyItemToSelectedOption = new HashMap<>();
    private final static Map<String, String> propertySizeToItem = new HashMap<>();
    private static String selectedOption =
            DsdsseUserPreference.getStringPreference(DsdsseUserPreference.PREF_CANCELLATION_POLICY_KEY,
                    PropertyCancellationPolicyPrefPanel.CONFIRMATION_VALUE);
    static {
        propertyItemToSelectedOption.put(SILENT_ITEM, SILENT_VALUE);
        propertyItemToSelectedOption.put(CONFIRMATION_ITEM, CONFIRMATION_VALUE);
    }

    private final JRadioButton silent = new SetupRadioButton(SILENT_ITEM, SILENT_VALUE.equals(selectedOption));
    private final JRadioButton confirmation = new SetupRadioButton(CONFIRMATION_ITEM, CONFIRMATION_VALUE.equals(selectedOption));

    PropertyCancellationPolicyPrefPanel() {
        super(new GridBagLayout());
        init();
    }

    /**
     *
     */
    private final void init() {
        add(silent, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 5, 0, 0), 0, 0));
        add(confirmation, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 5, 0, 0), 0, 0));
        setOpaque(false);
    }

    private final class SetupRadioButton extends JRadioButton {

        SetupRadioButton(String text, boolean selected) {
            super(text);
            btnGroup.add(this);
            setBorder(border);
            setFocusPainted(false);
            setFont(RADIO_BUTTON_FONT);
            setForeground(FOREGROUND);
            setOpaque(false);
            setSelected(selected);
            addItemListener((e) -> {
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }
                String key = ((JRadioButton) e.getItem()).getText();
                String selectedOption = propertyItemToSelectedOption.get(key);
                System.out.println("SELECTED " + selectedOption);
                DsdsseUserPreference.setStringPreference(DsdsseUserPreference.PREF_CANCELLATION_POLICY_KEY, selectedOption);
            });
        }
    }
}
