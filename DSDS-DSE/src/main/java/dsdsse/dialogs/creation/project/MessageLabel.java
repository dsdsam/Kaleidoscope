package dsdsse.dialogs.creation.project;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

final class MessageLabel extends JLabel {

    private static final Dimension MESSAGE_LABEL_SIZE = new Dimension(1, 50);
    private static final Color MESSAGE_LABEL_BACKGROUND = new Color(0xFFFF00);
    private static final Color MESSAGE_LABEL_FOREGROUND = new Color(0xFF0000);

    private static final MessageLabel MESSAGE_LABEL = new MessageLabel();

    static final MessageLabel getMessageLabel() {
        return MESSAGE_LABEL;
    }

    //
    //   I n s t a n c e
    //

    private final Timer timeoutTimer = new Timer(5000, (e) -> {
        MESSAGE_LABEL.showMessage("");
    });

    private MessageLabel() {
        super("", JLabel.LEFT);
        setFont(getFont().deriveFont(Font.PLAIN));
        setFont(getFont().deriveFont(12f));
        Border border = BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 0, 0),
                new MatteBorder(1, 1, 1, 1, Color.GRAY));
        setBorder(border);
        setPreferredSize(MESSAGE_LABEL_SIZE);
        setMinimumSize(MESSAGE_LABEL_SIZE);
        setBackground(MESSAGE_LABEL_BACKGROUND);
        setForeground(MESSAGE_LABEL_FOREGROUND);
        setOpaque(true);
        setVisible(false);
    }

    final void showMessage(String text) {
        super.setText("  " + text);
        if (!timeoutTimer.isRunning()) {
            MESSAGE_LABEL.setVisible(true);
            timeoutTimer.start();
        } else {
            MESSAGE_LABEL.setVisible(false);
            timeoutTimer.stop();
        }

    }
}
