package dsdsse.preferences;

import adf.utils.StandardFonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 10/5/2016.
 */
final class PropertyStateDisplaySizePrefPanel extends JPanel {

    public static final String SMALL_VALUE = "Small";
    public static final String MEDIUM_VALUE = "Medium";
    public static final String LARGE_VALUE = "Large";
    public static final String XLARGE_VALUE = "Extra Large";

    private static final Color FOREGROUND = new Color(0x333333);
    private static final Font RADIO_BUTTON_FONT = StandardFonts.FONT_DIALOG_PLAIN_11;
    private static final Border border = new EmptyBorder(3, 0, 3, 0);
    private static final ButtonGroup btnGroup = new ButtonGroup();

    private static final String SMALL_ITEM = " -  Tiny";
    private static final String MEDIUM_ITEM = " -  Small";
    private static final String LARGE_ITEM = " -  Medium";
    private static final String XLARGE_ITEM = " -  Large";

    private static final String PROPERTY_STATE_DISPLAY_SIZE =
            "<html><p><b>Property State Display Size:</p></html";

    private final static Map<String, String> propertyItemToStateDisplaySizeSizes = new HashMap<>();
    private final static Map<String, String> propertySizeToItem = new HashMap<>();
    private static String selectedSize =
            DsdsseUserPreference.getStringPreference(DsdsseUserPreference.PREF_STATE_DISPLAY_SIZE_KEY,
                    PropertyStateDisplaySizePrefPanel.LARGE_VALUE);


    static {
        propertyItemToStateDisplaySizeSizes.put(SMALL_ITEM, SMALL_VALUE);
        propertyItemToStateDisplaySizeSizes.put(MEDIUM_ITEM, MEDIUM_VALUE);
        propertyItemToStateDisplaySizeSizes.put(LARGE_ITEM, LARGE_VALUE);
        propertyItemToStateDisplaySizeSizes.put(XLARGE_ITEM, XLARGE_VALUE);
    }

    private final JLabel propertyStateDisplaySizeLabel = new JLabel(PROPERTY_STATE_DISPLAY_SIZE);
    private final JRadioButton small = new SetupRadioButton(SMALL_ITEM, SMALL_VALUE.equals(selectedSize));
    private final JRadioButton medium = new SetupRadioButton(MEDIUM_ITEM, MEDIUM_VALUE.equals(selectedSize));
    private final JRadioButton large = new SetupRadioButton(LARGE_ITEM, LARGE_VALUE.equals(selectedSize));
    private final JRadioButton extraLarge = new SetupRadioButton(XLARGE_ITEM, XLARGE_VALUE.equals(selectedSize));

    PropertyStateDisplaySizePrefPanel() {
        super(new GridBagLayout());
        init();
    }

    /**
     *
     */
    private final void init() {
        propertyStateDisplaySizeLabel.setFont(StandardFonts.FONT_HELVETICA_PLAIN_12);
        propertyStateDisplaySizeLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        propertyStateDisplaySizeLabel.setForeground(Color.DARK_GRAY);

        add(propertyStateDisplaySizeLabel, new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

        add(small, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
        add(medium, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));

        add(large, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
        add(extraLarge, new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));

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
                String selectedItem = propertyItemToStateDisplaySizeSizes.get(key);
                System.out.println("SELECTED " + selectedItem);
                DsdsseUserPreference.setStringPreference(DsdsseUserPreference.PREF_STATE_DISPLAY_SIZE_KEY, selectedItem);
            });
        }
    }
}
