package dsdsse.dialogs.creation.project;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class ValueEntryPanel extends JPanel {

    private static final int VALUE_ENTRY_FIELD_WIDTH = 120;
    private static final int VALUE_ENTRY_FIELD_HEIGHT = 25;
    private static final Dimension VALUE_ENTRY_FIELD_SIZE =
            new Dimension(VALUE_ENTRY_FIELD_WIDTH, VALUE_ENTRY_FIELD_HEIGHT);

    private static final Color ENTRY_FIELD_BACKGROUND = ProjectAttributesSetupMainPanel.ENTRY_FIELD_BACKGROUND;

    static final ValueEntryPanel createValueEntryPanel(String labelText) {
        return new ValueEntryPanel(labelText);
    }

    private final JLabel valueEntryLabel = new JLabel();
    private final JTextField valueEntryField = new JTextField();

    ValueEntryPanel(String labelText) {
        super(new GridBagLayout());
        Border textFieldBevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.LIGHT_GRAY,
                Color.GRAY, Color.LIGHT_GRAY);
        Border textFieldInnerBorder = new EmptyBorder(0, 2, 0, 2);
        Border textFieldBorder = BorderFactory.createCompoundBorder(textFieldBevelBorder, textFieldInnerBorder);
        valueEntryField.setBorder(textFieldBorder);
        valueEntryLabel.setText(labelText);
        initContent();
    }

    final void setValue(String text) {
        valueEntryField.setText(text);
    }

    final String getValue() {
        return valueEntryField.getText();
    }

    private final void initContent() {

        valueEntryLabel.setPreferredSize(VALUE_ENTRY_FIELD_SIZE);
        valueEntryLabel.setMinimumSize(VALUE_ENTRY_FIELD_SIZE);
        valueEntryLabel.setMaximumSize(VALUE_ENTRY_FIELD_SIZE);

        valueEntryField.setPreferredSize(VALUE_ENTRY_FIELD_SIZE);
        valueEntryField.setMinimumSize(VALUE_ENTRY_FIELD_SIZE);
        valueEntryField.setMaximumSize(VALUE_ENTRY_FIELD_SIZE);
        valueEntryField.setBackground(ENTRY_FIELD_BACKGROUND);
        valueEntryField.setOpaque(true);

        add(valueEntryLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 10, 0, 10), 0, 0));

        add(valueEntryField, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 10, 0, 10), 0, 0));
    }
}
