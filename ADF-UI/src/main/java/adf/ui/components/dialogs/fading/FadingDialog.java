package adf.ui.components.dialogs.fading;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 4/13/2016.
 */
public class FadingDialog extends JDialog {

    private static final float SHOW_SLOW = 0.02f;
    private static final float SHOW_FAST = 0.06f;
    private static final float FADE_SLOW = 0.02f;
    private static final float FADE_FAST = 0.06f;

    private float opacity;
    private float delta;

    private boolean fadeAble;
    private boolean showUp;

    private final Timer transparencyTimer = new Timer(30, (e) -> {
        if (showUp) {
            processShowUp();
        } else {
            processShutDown();
        }
    });

    //   C r e a t i o n

    public FadingDialog(JFrame mainFrame, boolean modal, boolean fadeAble) {
        super(mainFrame, modal);
//        super(mainFrame);
        this.fadeAble = fadeAble;
        setUndecorated(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void paint(Graphics g) {
//        if (!showUp) {
//            Thread.dumpStack();
//        }
        super.paint(g);
    }

    //   S h o w i n g

    public void bringUp() {
//        if (isModal()) {
//            super.setVisible(true);
//        } else
        if (fadeAble) {
            // setting initial opacity slightly more than 0 will eliminate splash
            opacity = SHOW_SLOW;
            FadingDialog.this.setOpacity(opacity);
            super.setVisible(true);

            showUp = true;
            delta = SHOW_SLOW;
            transparencyTimer.start();
        } else {
            super.setVisible(true);
        }
    }

    private void processShowUp() {
        FadingDialog.this.setOpacity(opacity);
        opacity += delta;
        if (opacity > 0.2f) {
            delta = SHOW_FAST;
        }
        if (opacity > 1) {
            opacity = 1.0f;
            delta = 0;
            FadingDialog.this.setOpacity(opacity);
            transparencyTimer.stop();
        }
    }

    //   F a d i n g

    public void fadeOutAndDestroy() {
        if (fadeAble) {
            // setting initial opacity slightly less than 1 will eliminate splash
            opacity = 1f - FADE_FAST;
            FadingDialog.this.setOpacity(opacity);
            // this call will eliminate splash
            super.setVisible(true);

            showUp = false;
            delta = FADE_FAST;
            transparencyTimer.start();
        } else {
            destroyDialog();
        }
    }

    private void processShutDown() {
        FadingDialog.this.setOpacity(opacity);
        opacity -= delta;
        if (opacity < 0.3) {
            delta = FADE_SLOW;
        }
        if (opacity < 0) {
            opacity = 0.0f;
            delta = 0;
            FadingDialog.this.setOpacity(opacity);
            transparencyTimer.stop();
            destroyDialog();
        }
    }

    public boolean isFadeAble() {
        return fadeAble;
    }

    public void setFadeAble(boolean fadeAble) {
        this.fadeAble = fadeAble;
    }

    protected void destroyDialog() {
        super.setVisible(false);
        removeAll();
        dispose();
    }

    public static void main(String[] args) {
        // Determine if the GraphicsDevice supports translucency.
//        GraphicsEnvironment ge =
//                GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice gd = ge.getDefaultScreenDevice();
//
//        //If translucent windows aren't supported, exit.
//        if (!gd.isWindowTranslucencySupported(TRANSLUCENT)) {
//            System.err.println(
//                    "Translucency is not supported");
//            System.exit(0);
//        }

        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FadingDialog fadingDialog = new FadingDialog(new JFrame(), false, true);
                fadingDialog.setPreferredSize(new Dimension(300, 200));
                fadingDialog.setSize(350, 400);
                fadingDialog.setLocation(300, 400);
                fadingDialog.bringUp();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
//                fadingDialog.fadeOutAndDestroy();

                // Display the window.
//                fadingDialog.setVisible(true);
            }
        });
    }
}
