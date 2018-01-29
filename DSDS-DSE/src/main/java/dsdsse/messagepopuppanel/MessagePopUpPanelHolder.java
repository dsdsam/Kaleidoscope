package dsdsse.messagepopuppanel;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Admin on 5/30/2017.
 */
public class MessagePopUpPanelHolder extends JPanel {

    public static final int INFO_MESSAGE = 1;
    public static final int WARNING_MESSAGE = 2;
    public static final int MESSAGE_LOCATION_NORTH = 1;
    public static final int MESSAGE_LOCATION_CENTER = 2;
    public static final int MESSAGE_LOCATION_SOUTH = 3;

//    private static final int MESSAGE_POPUP_PANEL_WIDTH = 800;
//    private static final int MESSAGE_POPUP_PANEL_HEIGHT = 80;
//    private static final Dimension MESSAGE_POPUP_PANEL_SIZE =
//            new Dimension(MESSAGE_POPUP_PANEL_WIDTH, MESSAGE_POPUP_PANEL_HEIGHT);

    private static final Color INFO_MESSAGE_BACKGROUND = new Color(0x00aa00);//Color.GREEN;
    private static final Color WARNING_MESSAGE_BACKGROUND = Color.RED;
    private static final Color MESSAGE_FOREGROUND = Color.WHITE;

    private static final String templateMessage = new StringBuilder().append("<html><p>").
            append("<center>").append("&nbsp;$&nbsp;<br><br>").
            append("</center>").append("</p></html>").toString();

    private JLabel messageLabel = new JLabel("", JLabel.CENTER);
    private JLabel attentionSignLabel = new JLabel("", JLabel.CENTER);
    private final JPanel messagePositioningPanel;
    private final FadableMessagePopUpPanel fadableMessagePopUpPanel = new FadableMessagePopUpPanel();

    MessagePopUpPanelHolder() {
//        setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 1));
        setOpaque(false);
        setLayout(new BorderLayout());
        messagePositioningPanel = this;
//        messagePositioningPanel = new JPanel(new BorderLayout());
        messagePositioningPanel.setOpaque(false);
//
//        messagePositioningPanel.setBorder(new EmptyBorder(30, 60, 30, 60));
//
//        add(messagePositioningPanel, BorderLayout.CENTER);
    }

    /**
     * @param messageText
     * @param messageType
     * @param messageLocation
     * @param messageClosedListener
     */
    void showMessage(String messageText, int messageType, int messageLocation, int width, int messageBackground,
                     int messageForeground, int waitingTime, MessageClosedListener messageClosedListener) {

        fadableMessagePopUpPanel.setMessage(messageText, width);

        messagePositioningPanel.setLayout(new GridBagLayout());
        int adj = 12;
        if (messageLocation == MESSAGE_LOCATION_NORTH) {

            messagePositioningPanel.add(fadableMessagePopUpPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(30, adj, 0, 0), 0, 0));

            messagePositioningPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        } else if (messageLocation == MESSAGE_LOCATION_NORTH) {

            messagePositioningPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

            messagePositioningPanel.add(fadableMessagePopUpPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, adj, 30, 0), 0, 0));

        } else {

            messagePositioningPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

            messagePositioningPanel.add(fadableMessagePopUpPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, adj, 0, 0), 0, 0));

            messagePositioningPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
        }
        revalidate();
//        fadableMessagePopUpPanel.setSize(MESSAGE_POPUP_PANEL_SIZE);
//        fadableMessagePopUpPanel.setMinimumSize(MESSAGE_POPUP_PANEL_SIZE);
//        fadableMessagePopUpPanel.setPreferredSize(MESSAGE_POPUP_PANEL_SIZE);
//        fadableMessagePopUpPanel.setMaximumSize(MESSAGE_POPUP_PANEL_SIZE);

//        messagePositioningPanel.revalidate();
        fadableMessagePopUpPanel.showUp(messageBackground, messageForeground, waitingTime, messageClosedListener);
    }

    void cancelCurrentMessage(){
        fadableMessagePopUpPanel.cancelCurrentMessage();
    }


//    boolean flag;

//    void setMessage(String message) {
//        flag ^= true;
//        int messageType = flag ? 0 : 1;
////        if (messageType == 1) {
////            messageLabel.setBackground(INFO_MESSAGE_BACKGROUND);
////            messageLabel.setForeground(new Color(0x020080));
////            messageLabel.setText("Hello World From Info Fly Out !");
////        } else {
////            messageLabel.setBackground(ALERT_MESSAGE_BACKGROUND);
////            messageLabel.setForeground(Color.WHITE);
////            messageLabel.setText("H e l l o   W o r l d   F r o m   A l e r t   F l y   O u t !");
////        }
//        String messageToShow = templateMessage.replace("$", message);
////        messageLabel.setText(messageToShow);
//        messageLabel.validate();
//    }

//    @Override
//    public final Dimension getSize() {
//        return ALERT_PANEL_SIZE;
//    }
//
//    @Override
//    public final Dimension getPreferredSize() {
////        Rectangle bounds
//        return ALERT_PANEL_SIZE;
//    }
//
//    @Override
//    public final Dimension getMinimumSize() {
//        return ALERT_PANEL_SIZE;
//    }
//
//    @Override
//    public final Dimension getMaximumSize() {
//        return ALERT_PANEL_SIZE;
//    }
}
