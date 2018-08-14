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
final class PropertyViewDisplaySizePrefPanel extends JPanel {

    public static final String SMALL_VALUE = "Small";
    public static final String MEDIUM_VALUE = "Medium";
    public static final String LARGE_VALUE = "Large";
    public static final String XLARGE_VALUE = "Extra Large";

    private static final Color FOREGROUND = new Color(0x333333);
    private static final Font RADIO_BUTTON_FONT = StandardFonts.FONT_DIALOG_PLAIN_11;
    private static final Border border = new EmptyBorder(0, 0, 0, 0);
    private static final ButtonGroup btnGroup = new ButtonGroup();

    private static final String SMALL_ITEM = " -  Small";
    private static final String MEDIUM_ITEM = " -  Medium";
    private static final String LARGE_ITEM = " -  Large";
    private static final String XLARGE_ITEM = " -  Extra Large";

    private static final String PROPERTY_BALL_SIZE =
            "<html><p><b>Property view display size:</p></html";

    private final static Map<String, String> propertyItemToBallSizes = new HashMap<>();
    private final static Map<String, String> propertySizeToItem = new HashMap<>();
    private static String selectedSize =
            DsdsseUserPreference.getStringPreference(DsdsseUserPreference.PREF_PROPERTY_VIEW_SIZE_KEY,
                    PropertyViewDisplaySizePrefPanel.MEDIUM_VALUE);


    static {


        propertyItemToBallSizes.put(SMALL_ITEM, SMALL_VALUE);
        propertyItemToBallSizes.put(MEDIUM_ITEM, MEDIUM_VALUE);
        propertyItemToBallSizes.put(LARGE_ITEM, LARGE_VALUE);
        propertyItemToBallSizes.put(XLARGE_ITEM, XLARGE_VALUE);
    }

    private final JLabel propertyBallSizeLabel = new JLabel(PROPERTY_BALL_SIZE);
    private final JRadioButton small = new SetupRadioButton(SMALL_ITEM, SMALL_VALUE.equals(selectedSize));
    private final JRadioButton medium = new SetupRadioButton(MEDIUM_ITEM, MEDIUM_VALUE.equals(selectedSize));
    private final JRadioButton large = new SetupRadioButton(LARGE_ITEM, LARGE_VALUE.equals(selectedSize));
    private final JRadioButton extraLarge = new SetupRadioButton(XLARGE_ITEM, XLARGE_VALUE.equals(selectedSize));

    PropertyViewDisplaySizePrefPanel() {
        super(new GridBagLayout());
        init();
    }

    /**
     *
     */
    private final void init() {

        propertyBallSizeLabel.setFont(StandardFonts.FONT_HELVETICA_PLAIN_12);
        propertyBallSizeLabel.setBorder(new EmptyBorder(2, 0, 4, 0));
        propertyBallSizeLabel.setForeground(Color.DARK_GRAY);

        add(propertyBallSizeLabel, new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

        add(small, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
        add(medium, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));

        add(large, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 15, 0, 0), 0, 0));
        add(extraLarge, new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 15, 0, 0), 0, 0));

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
                String selectedItem = propertyItemToBallSizes.get(key);
                System.out.println("SELECTED " + selectedItem);
                DsdsseUserPreference.setStringPreference(DsdsseUserPreference.PREF_PROPERTY_VIEW_SIZE_KEY, selectedItem);
            });
        }
    }
}
