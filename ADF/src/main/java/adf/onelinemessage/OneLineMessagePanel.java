package adf.onelinemessage;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Admin on 9/10/2017.
 */
class OneLineMessagePanel extends JPanel {

    public static final int NONE = 0;
    public static final int ALARM = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;

    private static final int DEFAULT_STYLE = Font.PLAIN;

    private static final int DEFAULT_HEIGHT = 22;
    private static final int DEFAULT_TEXT_SIZE = 12;

    private static final Color DEFAULT_WARNING_FOREGROUND = new Color(0xFF, 0x00, 0x00);
    private static final Color DEFAULT_WARNING_BACKGROUND = new Color(0xFF, 0xFF, 0xF0);

    private static final Color DEFAULT_FOREGROUND = new Color(0x00, 0x00, 0x44);
    private static final Color DEFAULT_BACKGROUND = new Color(0xFF, 0xFF, 0xF0);

    //
    //   I n s t a n c e
    //

    private final JLabel oneLineMessageLabel = new JLabel();
    private boolean showingHighPriorityMessage;

    OneLineMessagePanel() {
        super();
        setPreferredSize(new Dimension(0, DEFAULT_HEIGHT));

        setLayout(new BorderLayout());
        Border border = BorderFactory.createLoweredBevelBorder();
        setBorder(border);

        oneLineMessageLabel.setForeground(DEFAULT_FOREGROUND);
        oneLineMessageLabel.setBackground(DEFAULT_BACKGROUND);
        Font font = oneLineMessageLabel.getFont();
        oneLineMessageLabel.setFont(font.deriveFont(DEFAULT_STYLE, DEFAULT_TEXT_SIZE));
        this.add(oneLineMessageLabel, BorderLayout.CENTER);
    }

    /**
     * @param panelHeight
     * @param messageType
     * @param message
     * @param textSize
     */
    void showMessage(int panelHeight, int messageType, String message, int textSize) {

        if (panelHeight > 0) {
            setPreferredSize(new Dimension(0, panelHeight));
        }

        switch (messageType) {
            case ALARM:
                setMessage(message);
                oneLineMessageLabel.setBackground(Color.red);
                oneLineMessageLabel.setForeground(Color.white);
                break;

            case WARNING:
                setMessage(message);
                if (textSize > 0) {
                    Font font = oneLineMessageLabel.getFont();
                    oneLineMessageLabel.setFont(font.deriveFont(DEFAULT_STYLE, textSize));
                }
                oneLineMessageLabel.setHorizontalAlignment(JLabel.CENTER);
                oneLineMessageLabel.setOpaque(true);
                oneLineMessageLabel.setForeground(DEFAULT_WARNING_FOREGROUND);
                oneLineMessageLabel.setBackground(DEFAULT_WARNING_BACKGROUND);
                showingHighPriorityMessage = true;
                break;

            case INFO:
                if (showingHighPriorityMessage) {
                    return;
                }
                setMessage(message);
                oneLineMessageLabel.setOpaque(false);
                oneLineMessageLabel.setForeground(DEFAULT_FOREGROUND);
                oneLineMessageLabel.setBackground(DEFAULT_BACKGROUND);
                break;

            default:
                setMessage("Unknown type = " + messageType);
        }
        oneLineMessageLabel.repaint();
    }

    /**
     * @param message
     */
    private void setMessage(String message) {
        oneLineMessageLabel.setText("  " + message);
        Font font = oneLineMessageLabel.getFont();
        oneLineMessageLabel.setFont(font.deriveFont(DEFAULT_STYLE, DEFAULT_TEXT_SIZE));
        oneLineMessageLabel.setHorizontalAlignment(JLabel.LEFT);
    }

    /**
     * @param clearHighPriorityMessage
     * @param resetHeight
     */
    void clearMessage(boolean clearHighPriorityMessage, boolean resetHeight) {
        if (!clearHighPriorityMessage && showingHighPriorityMessage) {
            return;
        }
        if (resetHeight) {
            setPreferredSize(new Dimension(0, DEFAULT_HEIGHT));
        }
        showingHighPriorityMessage = false;
        Font font = oneLineMessageLabel.getFont();
        oneLineMessageLabel.setFont(font.deriveFont(DEFAULT_STYLE, DEFAULT_TEXT_SIZE));
        oneLineMessageLabel.setText("");
        oneLineMessageLabel.setOpaque(false);
        oneLineMessageLabel.setForeground(DEFAULT_FOREGROUND);
        oneLineMessageLabel.setBackground(DEFAULT_BACKGROUND);
        oneLineMessageLabel.repaint();
    }
}
