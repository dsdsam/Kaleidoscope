package dsdsse.dialogs.creation.project;

import mcln.model.MclnDoubleRectangle;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

final class ProjectSizeEntryPanel extends JPanel {

    private static final int PROJECT_SIZE_ENTRY_PANEL_WIDTH = 400;
    private static final int PROJECT_SIZE_ENTRY_PANEL_HEIGHT = 110;
    private static final Dimension PROJECT_SIZE_ENTRY_PANEL_SIZE =
            new Dimension(PROJECT_SIZE_ENTRY_PANEL_WIDTH, PROJECT_SIZE_ENTRY_PANEL_HEIGHT);

    private static final int LABEL_WIDTH = 40;
    private static final int LABEL_HEIGHT = 25;
    private static final Dimension LABEL_SIZE = new Dimension(LABEL_WIDTH, LABEL_HEIGHT);


    private static final int TEXT_ENTRY_FIELD_WIDTH = 180;
    private static final int TEXT_ENTRY_FIELD_HEIGHT = 25;
    private static final Dimension TEXT_ENTRY_FIELD_SIZE =
            new Dimension(TEXT_ENTRY_FIELD_WIDTH, TEXT_ENTRY_FIELD_HEIGHT);

    private static final Color ENTRY_FIELD_BACKGROUND = ProjectAttributesSetupMainPanel.ENTRY_FIELD_BACKGROUND;

    static final ProjectSizeEntryPanel createProjectSizeEntryPanel() {
        return new ProjectSizeEntryPanel();
    }

    private final JLabel widthEntryLabel = new JLabel("Width:");
    private final JTextField widthEntryField = new JTextField();

    private final JLabel heightEntryLabel = new JLabel("Height:");
    private final JTextField heightEntryField = new JTextField();

    private ProjectSizeEntryPanel() {
        super(new GridBagLayout());
        setOpaque(false);
        setPreferredSize(PROJECT_SIZE_ENTRY_PANEL_SIZE);
        setMinimumSize(PROJECT_SIZE_ENTRY_PANEL_SIZE);
        setMaximumSize(PROJECT_SIZE_ENTRY_PANEL_SIZE);

        Border widthTextFieldBevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.LIGHT_GRAY,
                Color.GRAY, Color.LIGHT_GRAY);
        Border widthTextFieldInnerBorder = new EmptyBorder(0, 2, 0, 2);
        Border widthTextFieldBorder = BorderFactory.createCompoundBorder(widthTextFieldBevelBorder, widthTextFieldInnerBorder);
        widthEntryField.setBorder(widthTextFieldBorder);
        widthEntryField.setHorizontalAlignment(JTextField.RIGHT);

        Border heightTextFieldBevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.LIGHT_GRAY,
                Color.GRAY, Color.LIGHT_GRAY);
        Border heightTextFieldInnerBorder = new EmptyBorder(0, 2, 0, 2);
        Border heightTextFieldBorder = BorderFactory.createCompoundBorder(heightTextFieldBevelBorder, heightTextFieldInnerBorder);
        heightEntryField.setBorder(heightTextFieldBorder);
        heightEntryField.setHorizontalAlignment(JTextField.RIGHT);

        initContext();
    }

    private final void initContext() {

        widthEntryLabel.setPreferredSize(LABEL_SIZE);
        widthEntryLabel.setMinimumSize(LABEL_SIZE);
        widthEntryLabel.setMaximumSize(LABEL_SIZE);

        widthEntryField.setPreferredSize(TEXT_ENTRY_FIELD_SIZE);
        widthEntryField.setMinimumSize(TEXT_ENTRY_FIELD_SIZE);
        widthEntryField.setMaximumSize(TEXT_ENTRY_FIELD_SIZE);
        widthEntryField.setBackground(ENTRY_FIELD_BACKGROUND);
        widthEntryField.setOpaque(true);

        heightEntryLabel.setPreferredSize(LABEL_SIZE);
        heightEntryLabel.setMinimumSize(LABEL_SIZE);
        heightEntryLabel.setMaximumSize(LABEL_SIZE);

        heightEntryField.setPreferredSize(TEXT_ENTRY_FIELD_SIZE);
        heightEntryField.setMinimumSize(TEXT_ENTRY_FIELD_SIZE);
        heightEntryField.setMaximumSize(TEXT_ENTRY_FIELD_SIZE);
        heightEntryField.setBackground(ENTRY_FIELD_BACKGROUND);
        heightEntryField.setOpaque(true);

        add(widthEntryLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(20, 40, 0, 0), 0, 0));

        add(widthEntryField, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 0, 0));


        add(Box.createGlue(), new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));


        add(heightEntryLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 40, 20, 0), 0, 0));

        add(heightEntryField, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 20, 0), 0, 0));
    }

    public final void setProjectSpaceRectangle(MclnDoubleRectangle mclnDoubleRectangle) {
        widthEntryField.setText("" + mclnDoubleRectangle.getWidth());
        heightEntryField.setText("" + mclnDoubleRectangle.getHeight());
    }


    final MclnDoubleRectangle getProjectSpaceRectangle() {
        MclnDoubleRectangle mclnDoubleRectangle = new MclnDoubleRectangle(0, 0, 0, 0);
        ;
        String strWidth = widthEntryField.getText();
        String strHeight = heightEntryField.getText();
        double width = stringToDouble(strWidth);
        double height = stringToDouble(strHeight);
        if (Double.isNaN(width) || Double.isNaN(height)) {
            return mclnDoubleRectangle;
        }
        double x = -width / 2;
        double y = height / 2;
        mclnDoubleRectangle = new MclnDoubleRectangle(x, y, width, height);
        return mclnDoubleRectangle;
    }

    private final double stringToDouble(String text) {
        double doubleValue = Double.NaN;
        try {
            doubleValue = Double.parseDouble(text);
        } finally {
            return doubleValue;
        }
    }
}
