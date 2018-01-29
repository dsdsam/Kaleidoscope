package dsdsse.printing;

import adf.app.StandardFonts;
import adf.ui.laf.button.Adf3DButton;
import adf.utils.BuildUtils;
import dsdsse.app.AppController;
import dsdsse.designspace.DesignSpaceView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Admin on 9/15/2016.
 */
public class PrintViewButtonPanel extends JPanel {

    private static final int PANEL_WIDTH = 140;
    public static final Dimension PANEL_SIZE = new Dimension(PANEL_WIDTH, 1);

    private static final Color LIGHT_GRAY_BACKGROUND = new Color(0xEEEEEE);
    private static final Color LIGHT_BLUE_BACKGROUND = new Color(0xD6EAFF);

    private static final int BUTTON_WIDTH = 110;
    private static final int BUTTON_HEIGHT = 22;
    private final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

    private final Color PANEL_BACKGROUND_COLOR = new Color(0xE7E7E7);
    private final Color BUTTON_FOREGROUND_COLOR = Color.BLACK;

    private final JLabel titleLabel = new JLabel("Print Preview", JLabel.CENTER);
    private final String PORTRAIT_TEXT = " - Portrait";
    private final String LANDSCAPE_TEXT = " - Landscape";

    // I n s t a n c e

    private final MclnPrintPreviewPanel mclnPrintPreviewPanel;

    private final JLabel orientationLabel = new JLabel("Orientation:");
    private final JRadioButton portraitButton = new JRadioButton(PORTRAIT_TEXT);
    private final JRadioButton landscapeButton = new JRadioButton(LANDSCAPE_TEXT);
    private final JButton printButton = new Adf3DButton("Print");
    private final JButton cancelButton = new Adf3DButton("Cancel");

    public PrintViewButtonPanel(MclnPrintPreviewPanel mclnPrintPreviewPanel) {
        super(new GridBagLayout());

        this.mclnPrintPreviewPanel = mclnPrintPreviewPanel;

        setOpaque(true);
        setBackground(PANEL_BACKGROUND_COLOR);

        setMinimumSize(PANEL_SIZE);
        setMaximumSize(PANEL_SIZE);
        setPreferredSize(PANEL_SIZE);

        Border marginBorder = new EmptyBorder(15, 10, 10, 10);
        Border matteBorder = new MatteBorder(0, 0, 0, 1, Color.GRAY);
        setBorder(BorderFactory.createCompoundBorder(matteBorder, marginBorder));
        initContent();
    }

    /**
     *
     */
    private final void initContent() {

        orientationLabel.setOpaque(false);

        titleLabel.setFont(StandardFonts.FONT_HELVETICA_BOLD_14);
        titleLabel.setForeground(new Color(0x0000DD));

        BuildUtils.resetComponentFont(portraitButton, Font.PLAIN, 12);
        portraitButton.setForeground(Color.BLACK);
        portraitButton.setOpaque(false);
        portraitButton.setMnemonic(KeyEvent.VK_P);
        portraitButton.setFocusPainted(false);
        portraitButton.addItemListener(e -> {
            mclnPrintPreviewPanel.setOrientation(MclnPrintPreviewPanel.Orientation.PORTRAIT);
            DesignSpaceView.getInstance().validate();
            DesignSpaceView.getInstance().repaint();
        });
//        portraitButton.addActionListener(generatorTypeSelectionAction);


        BuildUtils.resetComponentFont(landscapeButton, Font.PLAIN, 12);
        landscapeButton.setForeground(Color.BLACK);
        landscapeButton.setOpaque(false);
        landscapeButton.setMnemonic(KeyEvent.VK_L);
        landscapeButton.setFocusPainted(false);
        landscapeButton.addItemListener(e -> {
            mclnPrintPreviewPanel.setOrientation(MclnPrintPreviewPanel.Orientation.LANDSCAPE);
            DesignSpaceView.getInstance().validate();
            DesignSpaceView.getInstance().repaint();
        });
//        landscapeButton.addActionListener(generatorTypeSelectionAction);

        portraitButton.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(portraitButton);
        group.add(landscapeButton);

        printButton.setMinimumSize(BUTTON_SIZE);
        printButton.setMaximumSize(BUTTON_SIZE);
        printButton.setPreferredSize(BUTTON_SIZE);
        printButton.setForeground(BUTTON_FOREGROUND_COLOR);
        printButton.setFocusPainted(false);
        printButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
        printButton.addActionListener(e -> {
            mclnPrintPreviewPanel.printMcLNGraphView();
        });

        cancelButton.setMinimumSize(BUTTON_SIZE);
        cancelButton.setMaximumSize(BUTTON_SIZE);
        cancelButton.setPreferredSize(BUTTON_SIZE);
        cancelButton.setForeground(BUTTON_FOREGROUND_COLOR);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
        cancelButton.addActionListener(e -> {
            AppController.getInstance().onUserClosesPrintPreviewContent();
        });

        add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 15, 0), 0, 0));

        add(orientationLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));

        add(portraitButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));

        add(landscapeButton, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));


        add(printButton, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 0, 0));

        add(cancelButton, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0));

        add(Box.createVerticalGlue(), new GridBagConstraints(0, 6, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    }

}
