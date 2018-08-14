package dsdsse.dialogs.creation.project;

import adf.utils.StandardFonts;
import adf.ui.laf.button.Adf3DButton;

import javax.swing.*;
import java.awt.*;

public class ProjectSetupButtonsPanel extends JPanel {

    private static final int PANEL_HEIGHT = 36;
    private static final Dimension PANEL_SIZE = new Dimension(1, PANEL_HEIGHT);

    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 24;
    private static final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

    private static final Color PANEL_BACKGROUND_COLOR = new Color(0xCCCCCC);
    private static final Color BUTTON_FOREGROUND_COLOR = Color.BLACK;

    static final ProjectSetupButtonsPanel createButtonsPanel(Action cancelButtonAction, Action applyButtonAction) {
        return new ProjectSetupButtonsPanel(cancelButtonAction, applyButtonAction);
    }
    //
    //   L i s t e n e r s
    //

    private final JButton cancelButton = new Adf3DButton("Cancel");
    private final JButton saveButton = new Adf3DButton("Apply");

    private final Action cancelButtonAction;
    private final Action applyButtonAction;

    ProjectSetupButtonsPanel(Action cancelButtonAction, Action applyButtonAction) {
        super(new GridBagLayout());
        this.cancelButtonAction = cancelButtonAction;
        this.applyButtonAction = applyButtonAction;
        setBackground(PANEL_BACKGROUND_COLOR);
        setMinimumSize(PANEL_SIZE);
        setMaximumSize(PANEL_SIZE);
        initComponents();
    }

    private final void initComponents() {

        cancelButton.setMinimumSize(BUTTON_SIZE);
        cancelButton.setMaximumSize(BUTTON_SIZE);
        cancelButton.setPreferredSize(BUTTON_SIZE);
        cancelButton.setForeground(BUTTON_FOREGROUND_COLOR);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);
        cancelButton.addActionListener(cancelButtonAction);

//        saveButton.setEnabled(false);
        saveButton.setMinimumSize(BUTTON_SIZE);
        saveButton.setMaximumSize(BUTTON_SIZE);
        saveButton.setPreferredSize(BUTTON_SIZE);
        saveButton.setForeground(BUTTON_FOREGROUND_COLOR);
        saveButton.setFocusPainted(false);
        saveButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);
        saveButton.addActionListener(applyButtonAction);


        add(cancelButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 30, 0, 0), 0, 0));

        add(Box.createHorizontalStrut(40), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        add(saveButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 0, 0, 30), 0, 0));

    }

}
