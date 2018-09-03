package dsdsse.messagepopuppanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 6/4/2017.
 */
public class FadableMessagePopUpPanel extends MessagePopUpRoundedCornersPanel {

    private static final int MESSAGE_POPUP_PANEL_WIDTH = 1000;

    private static final float SHOW_SLOW = 0.03f;
    private static final float SHOW_FAST = 0.08f;
    private static final float FADE_SLOW = 0.03f;
    private static final float FADE_FAST = 0.08f;

    private static final int DELAY = 30;
    private static final int IDLE = 0;
    private static final int POP_UP = 1;
    private static final int WAITING = 2;
    private static final int FADE_OUT = 3;

    private static boolean canceling;

    private int waitingTime;
    private float opacity;
    private float delta;
    private int stage;

    private JPanel mainPanel;
    private AlphaComposite alphaComposite = AlphaComposite.SrcOver.derive(0.f);

    private MessageClosedListener messageClosedListener;


//    private JPanel panel;

    private JLabel messageLabel = new JLabel("", JLabel.CENTER);

    //
    //   A n i m a t i o n
    //

    private final Timer transparencyTimer = new javax.swing.Timer(DELAY, (e) -> {
        if (stage == IDLE) {
            return;
        } else if (stage == POP_UP) {
            processShowUp();
        } else if (stage == WAITING) {
            waiting();
        } else if (stage == FADE_OUT) {
            processFadingDown();
        }
    });

    private void processShowUp() {
        if (canceling) {
            transparencyTimer.stop();
            transparencyTimer.setInitialDelay(0);
            transparencyTimer.setDelay(DELAY + 10);
            canceling = false;
            fadeDown();
            return;
        }
//        System.out.println("Show up " + opacity);
        opacity += delta;
        if (opacity > 0.2f) {
            delta = SHOW_FAST;
        }
        if (opacity > 1) {
            opacity = 1.0f;
            delta = 0;
            transparencyTimer.stop();
            transparencyTimer.setInitialDelay(waitingTime);
            transparencyTimer.setDelay(DELAY);
            stage = WAITING;
            transparencyTimer.restart();
        }
        alphaComposite = AlphaComposite.SrcOver.derive(opacity);
        repaint();
    }

    private void waiting() {
        transparencyTimer.stop();
        transparencyTimer.setInitialDelay(0);
        transparencyTimer.setDelay(DELAY + 10);
        fadeDown();
    }

    //   F a d i n g   o u t

    private void processFadingDown() {
        opacity -= delta;
        if (opacity < 0.3) {
            delta = FADE_SLOW;
        }
        if (opacity < 0) {
            opacity = 0.0f;
            delta = 0;
            transparencyTimer.stop();
            if (messageClosedListener != null) {
                messageClosedListener.messageClosed();
                messageClosedListener = null;
                stage = IDLE;
            }
        }
        alphaComposite = AlphaComposite.SrcOver.derive(opacity);
        repaint();
    }

    private void processCancelled() {
        transparencyTimer.stop();
    }

    FadableMessagePopUpPanel() {
        setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 1));
        setLayout(new GridBagLayout());
        setOpaque(false);
        messageLabel.setOpaque(true);
        messageLabel.setBackground(Color.CYAN);
        messageLabel.setBorder(null);
    }

    //
    //   A P I
    //

    Dimension panelSize;

    void setMessage(String message, int width) {
        messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setOpaque(false);
        messageLabel.setBorder(null);
        messageLabel.setText(message);
        int messageWidth = width <= 0 ? MESSAGE_POPUP_PANEL_WIDTH : width;
        panelSize = new Dimension(messageWidth, messageLabel.getPreferredSize().height + 40);
        removeAll();
        add(messageLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * @param messageBackground
     * @param messageForeground
     * @param waitingTime
     * @param messageClosedListener
     */
    void showUp(int messageBackground, int messageForeground, int waitingTime,
                MessageClosedListener messageClosedListener) {

        this.waitingTime = waitingTime;
        this.messageClosedListener = messageClosedListener;

        setBackground(new Color(messageBackground));
        messageLabel.setForeground(new Color(messageForeground));

        stage = POP_UP;
        // setting initial opacity slightly more than 0 will eliminate splash
        opacity = SHOW_SLOW;
        alphaComposite = AlphaComposite.SrcOver.derive(opacity);
        delta = SHOW_SLOW;
        transparencyTimer.setCoalesce(false);
        transparencyTimer.setInitialDelay(DELAY);
        transparencyTimer.setDelay(DELAY);
        transparencyTimer.start();
    }

    void cancelCurrentMessage() {
        if (stage == IDLE) {
            return;
        } else if (stage == POP_UP) {
            canceling = true;
        } else if (stage == WAITING) {
            transparencyTimer.stop();
            transparencyTimer.setInitialDelay(0);
            transparencyTimer.setDelay(DELAY + 10);
            fadeDown();
        }
    }

    private void fadeDown() {
        stage = FADE_OUT;
        // setting initial opacity slightly more than 0 will eliminate splash
        opacity = 1f - FADE_FAST;
        alphaComposite = AlphaComposite.SrcOver.derive(opacity);
        delta = FADE_FAST;
        transparencyTimer.restart();
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Composite currentComp = g2d.getComposite();
        g2d.setComposite(alphaComposite);
        g.setColor(getBackground());
        super.paint(g2d);
        // reset current Comp
        g2d.setComposite(currentComp);
    }

    @Override
    public final Dimension getSize() {
        return panelSize;
    }

    @Override
    public final Dimension getPreferredSize() {
//        Rectangle bounds
        return panelSize;
    }

    @Override
    public final Dimension getMinimumSize() {
        return panelSize;
    }

    @Override
    public final Dimension getMaximumSize() {
        return panelSize;
    }
}
