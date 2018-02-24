package dsdsse.dialogs.creation.project;

import adf.app.StandardFonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;

final class EntryChosePanel extends JPanel {

    private static final int PANEL_WIDTH = 1;
    private static final int PANEL_HEIGHT = 40;
    private static final Dimension PANEL_SIZE = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);

    private static final String CHOICE_WIDTH_TEXT = "Set rectangle's width and height (rectangle will be centered).";
    private static final String CHOICE_CORNER_TEXT =
            "Set rectangle's upper left and lower right corners X and Y coordinates.";


    //
    //   I n s t a n c e
    //

    private final ButtonGroup btnGroup = new ButtonGroup();
    private static final Color FOREGROUND = new Color(0x333333);
    private static final Font RADIO_BUTTON_FONT = StandardFonts.FONT_DIALOG_PLAIN_11;
    private static final Border border = new EmptyBorder(0, 10, 0, 0);

    private final JPanel cardPanel;
    private String currentChoice = ProjectSpaceSetupPanel.INIT_SIZE_ID;

    EntryChosePanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        setLayout(new GridBagLayout());
        setOpaque(true);
        setPreferredSize(PANEL_SIZE);
        setMinimumSize(PANEL_SIZE);
        init();
    }

    private void init() {
        SetupRadioButton optionXYRadioButton = new SetupRadioButton(CHOICE_WIDTH_TEXT, true);
        SetupRadioButton optionWHRadioButton = new SetupRadioButton(CHOICE_CORNER_TEXT, false);

        add(optionXYRadioButton, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(2, 5, 0, 0), 0, 0));
        add(optionWHRadioButton, new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(2, 5, 0, 0), 0, 0));
    }

    /**
     *
     */
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
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                String key = ((JRadioButton) e.getItem()).getText();
                if (key.equalsIgnoreCase(CHOICE_WIDTH_TEXT)) {
                    cardLayout.show(cardPanel, ProjectSpaceSetupPanel.INIT_SIZE_ID);
                    currentChoice = ProjectSpaceSetupPanel.INIT_SIZE_ID;
                } else {
                    cardLayout.show(cardPanel, ProjectSpaceSetupPanel.INIT_CORNERS_ID);
                    currentChoice = ProjectSpaceSetupPanel.INIT_CORNERS_ID;
                }
            });
        }
    }

    final boolean isActivePanelWidthAndHeight() {
        return currentChoice == ProjectSpaceSetupPanel.INIT_SIZE_ID;
    }
}
