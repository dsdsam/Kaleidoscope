package adf.ui.components.dialogs.undecorated;

import adf.utils.StandardFonts;
import adf.utils.BuildUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Admin on 4/1/2016.
 */
public class AdfUndecoratedDialogTitleBar extends JPanel {

    public static final int PANEL_HEIGHT = 30;
    private static final Dimension PANEL_SIZE = new Dimension(1, PANEL_HEIGHT);

    private static final float DARK_COLOR_PERCENT = 0.65f;

    private static final String CLOSE_BUTTON_ICON_NAME = "CloseUndecoratedDialogButton.png";
    private static final String CLOSE_BUTTON_TOOLTIP = "Close Dialog Button";

    private static JButton createCloseButton(String PREFIX, String iconName, String tipText,
                                             ActionListener closeButtonActionListener) {
        ImageIcon imageIcon = BuildUtils.getImageIcon(PREFIX + iconName);
        JButton button = new JButton(imageIcon);
        button.setFocusPainted(false);
        button.setToolTipText(tipText);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.addActionListener(closeButtonActionListener);
        button.setEnabled(true);
        button.setBorder(null);
        return button;
    }

    private final ActionListener closeButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (adfUndecoratedDialog != null) {
                if (!adfUndecoratedDialog.canTheDialogBeClosed()) {
                    return;
                }
                adfUndecoratedDialog.removeMouseListener(mouseListener);
                adfUndecoratedDialog.removeMouseMotionListener(mouseListener);
                adfUndecoratedDialog.fadeOutAndDestroy();
                adfUndecoratedDialog.removeWindowFocusListener(windowFocusListener);
            }
        }
    };

    private MouseAdapter mouseListener = new MouseAdapter() {

        private Point point = new Point();

        @Override
        public synchronized void mousePressed(MouseEvent e) {
            point.x = e.getX();
            point.y = e.getY();
            e.consume();
        }

        @Override
        public synchronized void mouseDragged(MouseEvent e) {
            int deltaX = e.getX() - point.x;
            int deltaY = e.getY() - point.y;
            Point currentLocation = adfUndecoratedDialog.getLocation();
            adfUndecoratedDialog.setLocation(currentLocation.x + deltaX, currentLocation.y + deltaY);
            e.consume();
        }
    };

    private final JLabel headerLabel = new JLabel("ADF Undecorated Window Header", JLabel.LEFT);
    private AdfUndecoratedDialog adfUndecoratedDialog;
    private final JPanel containerPanel;
    private Color titleBarBrightBackground;
    private Color titleBarDarkBackground = new Color(0xAAAAAA);

    // background switch on focus gained or lost
    private final WindowFocusListener windowFocusListener = new WindowFocusListener() {
        @Override
        public void windowGainedFocus(WindowEvent e) {
            containerPanel.setBackground(titleBarBrightBackground);
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
            containerPanel.setBackground(titleBarDarkBackground);
        }
    };

    public AdfUndecoratedDialogTitleBar(String closeButtonClassPath, String headerText, Color titleBarBackground, Color headerForeground) {
        super(new BorderLayout());
        this.titleBarBrightBackground = titleBarBackground;
//        this.titleBarDarkBackground = new Color(
//                titleBarBrightBackground.getRed() / 255f * DARK_COLOR_PERCENT,
//                titleBarBrightBackground.getGreen() / 255f * DARK_COLOR_PERCENT,
//                titleBarBrightBackground.getBlue() / 255f * DARK_COLOR_PERCENT);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        setPreferredSize(PANEL_SIZE);
        setMinimumSize(PANEL_SIZE);
        setMaximumSize(PANEL_SIZE);

        headerLabel.setOpaque(false);
        headerLabel.setText(headerText);
        headerLabel.setForeground(headerForeground);
        headerLabel.setFont(StandardFonts.FONT_HELVETICA_BOLD_11);

        containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setOpaque(true);
        containerPanel.setBackground(titleBarBrightBackground);

        JButton closeButton = createCloseButton(closeButtonClassPath, CLOSE_BUTTON_ICON_NAME,
                CLOSE_BUTTON_TOOLTIP, closeButtonActionListener);

        containerPanel.add(headerLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
        containerPanel.add(closeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 7), 0, 0));

        add(containerPanel, BorderLayout.CENTER);

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    protected void setHeaderText(String headerText) {
        headerLabel.setText(headerText);
    }

    void setAdfUndecoratedDialog(AdfUndecoratedDialog adfUndecoratedDialog) {
        this.adfUndecoratedDialog = adfUndecoratedDialog;
        adfUndecoratedDialog.addWindowFocusListener(windowFocusListener);

    }
}

