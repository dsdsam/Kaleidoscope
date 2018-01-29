package dsdsse.messagepopupwindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by u0180093 on 2/28/2017.
 */
class FadingWindow extends JWindow {

    private static final float SHOW_SLOW = 0.02f;
    private static final float SHOW_FAST = 0.05f;
    private static final float FADE_SLOW = 0.05f;
    private static final float FADE_FAST = 0.05f;

    private float opacity;
    private float delta;
    private boolean fadeAble;
    private boolean showUp;

    private final Timer transparencyTimer = new Timer(30, (e) -> {
//        System.out.println("transparencyTimer " + showUp);
        if (showUp) {
            processShowUp();
        } else {
            processShutDown();
        }
    });


    //
    //   C r e a t i o n
    //

    public FadingWindow(JFrame mainFrame, boolean fadeAble) {
        super(mainFrame);
        this.fadeAble = fadeAble;
//        this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
//        addWindowStateListener();
//        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

//    public void paint(Graphics g) {
//
////        if (!showUp) {
//
////            Thread.dumpStack();
//
////        }
//
//        super.paint(g);
//
//    }


    //   S h o w i n g

    public void bringUp() {
//        if (isModal()) {
//            super.setVisible(true);
//        } else

        this.setAlwaysOnTop(true);
        if (fadeAble) {
            // setting initial opacity slightly more than 0 will eliminate splash
            opacity = SHOW_SLOW;
            FadingWindow.this.setOpacity(opacity);
            super.setVisible(true);
            showUp = true;
            delta = SHOW_SLOW;
            transparencyTimer.start();
        } else {
            super.setVisible(true);
        }
    }

    private void processShowUp() {
//        System.out.println("processShowUp ");
        FadingWindow.this.setOpacity(opacity);
        opacity += delta;
        if (opacity > 0.2f) {
            delta = SHOW_FAST;
        }

        if (opacity > 1) {
            opacity = 1.0f;
            delta = 0;
            FadingWindow.this.setOpacity(opacity);
            transparencyTimer.stop();
//            repaint();
//            System.out.println("processShowUp stopped");


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            fadeOutAndDestroy();
        }
    }

    //   F a d i n g

    public void fadeOutAndDestroy() {
        if (fadeAble) {
            // setting initial opacity slightly less than 1 will eliminate splash
            opacity = 1f - FADE_FAST;
            FadingWindow.this.setOpacity(opacity);
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
        FadingWindow.this.setOpacity(opacity);
        opacity -= delta;
        if (opacity < 0.3) {
            delta = FADE_SLOW;
        }
        if (opacity >= 0) {
            return;
        }
        opacity = 0.0f;
        delta = 0;
        FadingWindow.this.setOpacity(opacity);
        transparencyTimer.stop();
        destroyDialog();
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
                JFrame mainFrame = new JFrame();
                mainFrame.setVisible(true);
                mainFrame.setSize(350, 400);

                FadingWindow fadingWindow = new FadingWindow(mainFrame, true);
                fadingWindow.setPreferredSize(new Dimension(300, 200));
                fadingWindow.setSize(1100, 100);
                fadingWindow.setLocation(500, 400);
                fadingWindow.bringUp();
//                System.out.println("after bringUp ");
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                }
//                System.out.println("after sleep ");
//                fadingWindow.fadeOutAndDestroy();
//                System.out.println("after fade ");


                // Display the window.

//                fadingDialog.setVisible(true);

            }

        });

    }
}
