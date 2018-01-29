package dsdsse.preferences;

import adf.app.StandardFonts;
import dsdsse.app.AppController;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created by Admin on 8/22/2016.
 */
public class PreferencesSetupPanel extends JPanel {

    private static final int THE_PANEL_WIDTH = 266;
    public static final Dimension THE_PANEL_SIZE = new Dimension(THE_PANEL_WIDTH, 1);

    private static final Color HEADER_BACKGROUND = new Color(0x0000AA);
    private static final Color LIGHT_GRAY_BACKGROUND = new Color(0xEEEEEE);

    private static final PreferencesSetupPanel preferencesSetupPanel = new PreferencesSetupPanel();

    public static PreferencesSetupPanel getInstance() {
        return preferencesSetupPanel;
    }

    //
    //   I n s t a n c e
    //

    private final JPanel titlePanel = new TitlePanel();
    private final JPanel buttonsPanel = new ButtonsPanel();
    private JPanel setupControlsHolder;
    private final JScrollPane scrollPane = new JScrollPane();

    PreferencesSetupPanel() {
        super(new BorderLayout());
        setBorder(new MatteBorder(0, 1, 0, 0, Color.GRAY));
        setPreferredSize(THE_PANEL_SIZE);
        initContent();
    }

    private void initContent() {

        scrollPane.setBackground(Color.GRAY);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.setupControlsHolder = new SetupContentHolder();
        setupControlsHolder.setOpaque(true);
        setupControlsHolder.setBackground(LIGHT_GRAY_BACKGROUND);
        scrollPane.getViewport().add(setupControlsHolder);


        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     *
     */
    private class TitlePanel extends JPanel {

        private final Dimension HEADER_SIZE = new Dimension(1, 22);
        private final Dimension BUTTON_SIZE = new Dimension(60, 21);
        private final JLabel headerLabel = new JLabel("Preferences Setup", JLabel.LEFT);
        private final JButton hideButton = new JButton("Hide");


        TitlePanel() {
            super(new GridBagLayout());
            setBackground(HEADER_BACKGROUND);
            setPreferredSize(HEADER_SIZE);
            hideButton.setPreferredSize(BUTTON_SIZE);
            hideButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
            hideButton.setBorder(null);
            hideButton.setFocusPainted(false);
            initLayout();
        }

        private void initLayout() {
            headerLabel.setOpaque(false);
            headerLabel.setForeground(Color.WHITE);
            add(headerLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));

            hideButton.addActionListener(e -> {
                AppController.getInstance().onUserClosesPreferencesSetup();
            });
            add(hideButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
    }

    private class ButtonsPanel extends JPanel {

        private static final int PANEL_HEIGHT = 15;
        private final Dimension PANEL_SIZE = new Dimension(1, PANEL_HEIGHT);

        private static final int BUTTON_WIDTH = 70;
        private static final int BUTTON_HEIGHT = 22;
        private final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        private final Color PANEL_BACKGROUND_COLOR = new Color(0xCCCCCC);
        private final Color BUTTON_FOREGROUND_COLOR = Color.BLACK;

//        private final JButton hideButton = new Adf3DButton("Hide");

        ButtonsPanel() {
            super(new GridBagLayout());
            setBackground(PANEL_BACKGROUND_COLOR);
            setMinimumSize(PANEL_SIZE);
            setMaximumSize(PANEL_SIZE);
            setPreferredSize(PANEL_SIZE);
            initLayout();
        }

        private void initLayout() {
//            hideButton.setMinimumSize(BUTTON_SIZE);
//            hideButton.setMaximumSize(BUTTON_SIZE);
//            hideButton.setPreferredSize(BUTTON_SIZE);
//            hideButton.setForeground(BUTTON_FOREGROUND_COLOR);
//            hideButton.setFocusPainted(false);
//            hideButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
//            hideButton.addActionListener(e -> {
//                AppController.getInstance().onUserClosesPreferencesSetup();
//            });

//            add(hideButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
//                    GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
        }
    }
}
