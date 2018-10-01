package adf.flyout;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2/15/2017.
 */
final class FlyDownMessageHolderPanel extends RoundedCornersContainerPanel {

    private static final int MESSAGE_PANEL_WIDTH = 700;
    private static final int MESSAGE_PANEL_HEIGHT = 40;
    private static final Dimension MESSAGE_PANEL_SIZE = new Dimension(MESSAGE_PANEL_WIDTH, MESSAGE_PANEL_HEIGHT);

    private static final String htmlMessage = new StringBuilder().append("<html><p>").append("<center>").
            append("&nbsp;$&nbsp;<br><br>").append("</center>").append("</p></html>").toString();

    private JLabel messageLabel = new JLabel("", JLabel.CENTER);

    FlyDownMessageHolderPanel() {
        this(RoundedRectangle.ROUNDING_RADIUS_MEDIUM, RoundedRectangle.ROUND_BOTTOM_SIDE);
    }

    FlyDownMessageHolderPanel(int roundingRadius, int roundingPolicy) {
        super(roundingRadius, roundingPolicy);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 1));
        setLayout(new BorderLayout());
        setOpaque(false);

        messageLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        messageLabel.setOpaque(true);
        add(messageLabel, BorderLayout.CENTER);
    }

    void setMessage(FlyingMessage flyingMessage) {
        String messageText = flyingMessage.getMessageText();
        switch (flyingMessage.getMessageType()) {
            case FlyingMessage.ALERT:
                messageText = "Alert !   " + messageText;
                messageLabel.setBackground(new Color(0xFF0000));
                messageLabel.setForeground(new Color(0xFFFFFF));
                break;
            case FlyingMessage.WARNING:
                messageText = "Warning:   " + messageText;
                messageLabel.setBackground(new Color(0xFFBE7D));
                messageLabel.setForeground(new Color(0xFF0000));
                break;
            case FlyingMessage.INFO:
                messageText = "Info:   " + messageText;
                messageLabel.setBackground(new Color(0xFFFF00));
                messageLabel.setForeground(new Color(0xFF5000));
                break;
            default:
                messageLabel.setBackground(Color.BLACK);
                messageLabel.setForeground(Color.WHITE);
                messageText = "Invalid Message Type: " + flyingMessage.getMessageType();
        }
        messageLabel.setText(messageText);
        messageLabel.validate();
    }

    @Override
    public final Dimension getSize() {
        return MESSAGE_PANEL_SIZE;
    }

    @Override
    public final Dimension getPreferredSize() {
        return MESSAGE_PANEL_SIZE;
    }

    @Override
    public final Dimension getMinimumSize() {
        return MESSAGE_PANEL_SIZE;
    }

    @Override
    public final Dimension getMaximumSize() {
        return MESSAGE_PANEL_SIZE;
    }
}
