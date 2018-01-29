package dsdsse.messageflydownpanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Admin on 5/30/2017.
 */
class MessageFlyDownHolderPanel extends MessageFlyDownRoundedCornersPanel {

    private static final int ALERT_PANEL_WIDTH = 400;
    private static final int ALERT_PANEL_HEIGHT = 40;
    private static final Dimension ALERT_PANEL_SIZE = new Dimension(ALERT_PANEL_WIDTH, ALERT_PANEL_HEIGHT);

    private static final Color INFO_MESSAGE_BACKGROUND = Color.YELLOW;
    private static final Color ALERT_MESSAGE_BACKGROUND = Color.RED;//new Color(0xFF5555);
    private static final Color ALERT_SIGN_HEADER_BACKGROUND = Color.RED;//Color.BLUE;

    private static final String templateMessage = new StringBuilder().append("<html><p>").
            append("<center>").append("&nbsp;$&nbsp;<br><br>").
            append("</center>").append("</p></html>").toString();

    private JLabel messageLabel = new JLabel("", JLabel.CENTER);
    private JLabel attentionSignLabel = new JLabel("", JLabel.CENTER);

    MessageFlyDownHolderPanel() {
        this(RoundedRectangle.ROUND_BOTTOM_SIDE);
    }

    MessageFlyDownHolderPanel(int roundingPolicy) {
        super(roundingPolicy);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 1));
        setLayout(new BorderLayout());
        this.setOpaque(false);
//        this.setBackground(ALERT_SIGN_PANEL_BACKGROUND);
//        this.setBackground(new Color(0xFF0000));

        JLabel titlePanel = new JLabel("Alert  Message", JLabel.LEFT);
        titlePanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        titlePanel.setPreferredSize(new Dimension(1, 24));
        titlePanel.setOpaque(true);
        titlePanel.setBackground(ALERT_SIGN_HEADER_BACKGROUND);
//        titlePanel.setBackground(new Color(220, 10, 0));
        titlePanel.setForeground(Color.WHITE);
//        add(titlePanel, BorderLayout.NORTH);

//        attentionSignLabel.setIcon(attentionSignIcon);
//        attentionSignLabel.setOpaque(true);
//        attentionSignLabel.setBorder(null);
//
//        JPanel attentionSignPanel = new JPanel(new GridBagLayout());
//        attentionSignPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
//        attentionSignPanel.setOpaque(false);
//        attentionSignPanel.setPreferredSize(new Dimension(ATTENTION_SIGN_PANEL_WIDTH, 60));
//        attentionSignPanel.setMaximumSize(new Dimension(ATTENTION_SIGN_PANEL_WIDTH, 60));
//        attentionSignPanel.setMinimumSize(new Dimension(ATTENTION_SIGN_PANEL_WIDTH, 60));
//
//        attentionSignPanel.add(attentionSignLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
//                GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
//        add(attentionSignPanel, BorderLayout.WEST);

//        messageLabel.setPreferredSize(ALERT_MESSAGE_SIZE);
//        messageLabel.setMaximumSize(ALERT_MESSAGE_SIZE);
//        messageLabel.setMinimumSize(new Dimension(200, ALERT_MESSAGE_HEIGHT));

//        messageLabel.setBorder(new MatteBorder(10, 0, 20, 0, Color.WHITE));
        messageLabel.setFont(new Font("Ariel", Font.BOLD, 15));
//        messageLabel.setBackground(Color.RED);
//        int messageType = flag ? 0 : 1;
//        if (messageType == 1) {
//            messageLabel.setBackground(INFO_MESSAGE_BACKGROUND);
//            messageLabel.setForeground(new Color(0x020080));
//        } else {
//            messageLabel.setBackground(ALERT_MESSAGE_BACKGROUND);
//            messageLabel.setForeground(Color.WHITE);
//        }


        messageLabel.setOpaque(true);
        add(messageLabel, BorderLayout.CENTER);
    }

    boolean flag;

    void setMessage(String message) {
        flag ^= true;
        int messageType = flag ? 0 : 1;
        if (messageType == 1) {
            messageLabel.setBackground(INFO_MESSAGE_BACKGROUND);
            messageLabel.setForeground(new Color(0x020080));
            messageLabel.setText("Hello World From Info Fly Out !");
        } else {
            messageLabel.setBackground(ALERT_MESSAGE_BACKGROUND);
            messageLabel.setForeground(Color.WHITE);
            messageLabel.setText("H e l l o   W o r l d   F r o m   A l e r t   F l y   O u t !");
        }
        String messageToShow = templateMessage.replace("$", message);
//        messageLabel.setText(messageToShow);
        messageLabel.validate();
    }

    @Override
    public final Dimension getSize() {
        return ALERT_PANEL_SIZE;
    }

    @Override
    public final Dimension getPreferredSize() {
//        Rectangle bounds
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
