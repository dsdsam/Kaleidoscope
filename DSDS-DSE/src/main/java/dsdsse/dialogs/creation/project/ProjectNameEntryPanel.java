package dsdsse.dialogs.creation.project;

import adf.ui.UiUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ProjectNameEntryPanel extends JPanel {

    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 60;
    private static final Dimension PANEL_SIZE = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);

    private static final String TITLE = "   Project Name   ";
    private static final Color TITLED_BORDER_TEXT_COLOR = new Color(0x0000AA);
    private static final Color TITLED_BORDER_LINE_COLOR = new Color(0x0000AA);

    private static final int VALUE_ENTRY_FIELD_WIDTH = 300;
    private static final int VALUE_ENTRY_FIELD_HEIGHT = 25;
    private static final Dimension VALUE_ENTRY_FIELD_SIZE =
            new Dimension(VALUE_ENTRY_FIELD_WIDTH, VALUE_ENTRY_FIELD_HEIGHT);

    private static final Color ENTRY_FIELD_BACKGROUND = ProjectAttributesSetupMainPanel.ENTRY_FIELD_BACKGROUND;

    static final ProjectNameEntryPanel createProjectNameEntryPanel() {
        return new ProjectNameEntryPanel();
    }

    private final JTextField valueEntryField = new JTextField();

    ProjectNameEntryPanel() {
        super(new GridBagLayout());
        Border outerBorder = new EmptyBorder(3, 3, 3, 3);
        Border titledBorder = UiUtils.createTitledBorder(0, 10, 5, 10,
                TITLED_BORDER_LINE_COLOR, TITLE, TITLED_BORDER_TEXT_COLOR, TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION);
        Border border = BorderFactory.createCompoundBorder(outerBorder, titledBorder);
        setBorder(border);

        Border textFieldBevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.LIGHT_GRAY,
                Color.GRAY, Color.LIGHT_GRAY);
        Border textFieldInnerBorder = new EmptyBorder(0, 2, 0, 2);
        Border textFieldBorder = BorderFactory.createCompoundBorder(textFieldBevelBorder, textFieldInnerBorder);
        valueEntryField.setBorder(textFieldBorder);
        initContent();
    }

    private final void initContent() {

        setPreferredSize(PANEL_SIZE);
        setMinimumSize(PANEL_SIZE);
        setMaximumSize(PANEL_SIZE);

        valueEntryField.setPreferredSize(VALUE_ENTRY_FIELD_SIZE);
        valueEntryField.setMinimumSize(VALUE_ENTRY_FIELD_SIZE);
        valueEntryField.setMaximumSize(VALUE_ENTRY_FIELD_SIZE);
        valueEntryField.setBackground(ENTRY_FIELD_BACKGROUND);
        valueEntryField.setOpaque(true);

        add(valueEntryField, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 20, 0, 20), 0, 0));
    }

    public final void setProjectName(String projectName) {
        projectName = projectName != null ? projectName.trim() : "";
        valueEntryField.setText(projectName);
    }

    final String getProjectName() {
        String projectName = valueEntryField.getText();
        return projectName.trim();
    }
}
