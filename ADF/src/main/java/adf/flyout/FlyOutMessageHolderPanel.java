package adf.flyout;

import adf.utils.BuildUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created on 11/9/2016.
 */
class FlyOutMessageHolderPanel extends RoundedCornersContainerPanel {

    private static ImageIcon attentionSignIcon = BuildUtils.getAdfIconFromClassPath("attention-sign.png");

    private static final int ATTENTION_SIGN_PANEL_WIDTH = 50;
    private static final int ALERT_MESSAGE_WIDTH = 70 * 5;
    private static final int ALERT_MESSAGE_HEIGHT = 140;
    private static final Dimension ALERT_MESSAGE_SIZE = new Dimension(ALERT_MESSAGE_WIDTH, ALERT_MESSAGE_HEIGHT);

    private static final int ALERT_PANEL_WIDTH = ATTENTION_SIGN_PANEL_WIDTH + ALERT_MESSAGE_WIDTH;
    private static final int ALERT_PANEL_HEIGHT = 140;
    private static final Dimension ALERT_PANEL_SIZE = new Dimension(ALERT_PANEL_WIDTH, ALERT_PANEL_HEIGHT);

    private static final Color MESSAGE_BACKGROUND = new Color(0xFEFECC);
    private static final Color ALERT_SIGN_PANEL_BACKGROUND = MESSAGE_BACKGROUND;

    private static final String htmlMessage = new StringBuilder().append("<html><p>").
            append("<center>").append("<font color=\"#020080\">").append("&nbsp;$&nbsp;<br><br>").
            append("</font>").append("</center>").append("</p></html>").toString();

    private JLabel messageLabel = new JLabel("", JLabel.CENTER);
    private JLabel attentionSignLabel = new JLabel("", JLabel.CENTER);

    FlyOutMessageHolderPanel() {
        this(RoundedRectangle.ROUNDING_RADIUS_MEDIUM, RoundedRectangle.ROUND_ALL);
    }

    FlyOutMessageHolderPanel(int roundingRadiys, int roundingPolicy) {
        super(roundingRadiys, roundingPolicy);
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 0));
        setLayout(new BorderLayout());
        setOpaque(false);
        setBackground(ALERT_SIGN_PANEL_BACKGROUND);

        JLabel titlePanel = new JLabel("Alert  Message", JLabel.LEFT);
        titlePanel.setBorder(new EmptyBorder(0, 16, 0, 0));
        titlePanel.setPreferredSize(new Dimension(1, 20));
        titlePanel.setOpaque(true);
        titlePanel.setBackground(new Color(220, 110, 0));
        titlePanel.setForeground(Color.WHITE);
        add(titlePanel, BorderLayout.NORTH);

        attentionSignLabel.setIcon(attentionSignIcon);
        attentionSignLabel.setOpaque(true);
        attentionSignLabel.setBorder(null);

        JPanel attentionSignPanel = new JPanel(new GridBagLayout());
        attentionSignPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        attentionSignPanel.setOpaque(false);
        attentionSignPanel.setPreferredSize(new Dimension(ATTENTION_SIGN_PANEL_WIDTH, 60));
        attentionSignPanel.setMaximumSize(new Dimension(ATTENTION_SIGN_PANEL_WIDTH, 60));
        attentionSignPanel.setMinimumSize(new Dimension(ATTENTION_SIGN_PANEL_WIDTH, 60));

        attentionSignPanel.add(attentionSignLabel,
                new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0,
                        0));
        add(attentionSignPanel, BorderLayout.WEST);

        messageLabel.setPreferredSize(ALERT_MESSAGE_SIZE);
        messageLabel.setMaximumSize(ALERT_MESSAGE_SIZE);
        messageLabel.setMinimumSize(new Dimension(200, ALERT_MESSAGE_HEIGHT));

        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        messageLabel.setFont(new Font("Ariel", Font.PLAIN, 14));
        messageLabel.setBackground(MESSAGE_BACKGROUND);
        messageLabel.setOpaque(true);
        add(messageLabel, BorderLayout.CENTER);
    }

    void setMessage(String message) {
        String messageToShow = htmlMessage.replace("$", message);
        messageLabel.setText(messageToShow);
        messageLabel.validate();
    }

    @Override
    public final Dimension getSize() {
        return ALERT_PANEL_SIZE;
    }

    @Override
    public final Dimension getPreferredSize() {
        return ALERT_PANEL_SIZE;
    }

    @Override
    public final Dimension getMinimumSize() {
        return ALERT_PANEL_SIZE;
    }

    @Override
    public final Dimension getMaximumSize() {
        return ALERT_PANEL_SIZE;
    }
}
