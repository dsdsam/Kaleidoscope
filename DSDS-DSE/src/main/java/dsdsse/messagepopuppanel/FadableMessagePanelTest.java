package dsdsse.messagepopuppanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 6/5/2017.
 */
public class FadableMessagePanelTest extends JPanel {

    private static final float SHOW_SLOW = 0.05f;
    private static final float SHOW_FAST = 0.10f;
    private static final float FADE_SLOW = 0.05f;
    private static final float FADE_FAST = 0.10f;

    private static final int DELAY = 30;
    private static final int POP_UP = 1;
    private static final int WAITING = 2;
    private static final int FADE_OUT = 3;

    private float opacity;
    private float delta;
    private int stage;

    private int delay = 100000;
    JPanel mainPanel;
    //    private float transparentValue = 1.0f;
    int trans = 0xFF000000;
    AlphaComposite alphaComposite;

    private JPanel panel;
    Color background = Color.GREEN;


    private final Timer transparencyTimer = new javax.swing.Timer(DELAY, (e) -> {
        if (stage == POP_UP) {
            processShowUp();
        } else if (stage == WAITING) {
            waiting();
        } else if (stage == FADE_OUT) {
            processFadingDown();
        }
    });

    public void showUp() {
        stage = POP_UP;
        // setting initial opacity slightly more than 0 will eliminate splash
        opacity = SHOW_SLOW;
        alphaComposite = AlphaComposite.SrcOver.derive(opacity);
        delta = SHOW_SLOW;
        transparencyTimer.setDelay(DELAY);
        transparencyTimer.start();
    }

    public void waiting() {
        transparencyTimer.setDelay(DELAY);
        fadeDown();
    }

    public void fadeDown() {
        stage = FADE_OUT;
        // setting initial opacity slightly more than 0 will eliminate splash
        opacity = 1f - FADE_FAST;
        alphaComposite = AlphaComposite.SrcOver.derive(opacity);
        delta = FADE_FAST;
        transparencyTimer.start();
    }

    private void processShowUp() {
//        System.out.println("Show up " + opacity);
        opacity += delta;
        if (opacity > 0.2f) {
            delta = SHOW_FAST;
        }
        if (opacity > 1) {
            opacity = 1.0f;
            delta = 0;
            transparencyTimer.stop();

            transparencyTimer.setDelay(30000);
            transparencyTimer.start();
            stage = WAITING;
        }
        alphaComposite = AlphaComposite.SrcOver.derive(opacity);
        panel.repaint();
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
        }
        alphaComposite = AlphaComposite.SrcOver.derive(opacity);
        panel.repaint();
    }

    /**
     *
     */
    FadableMessagePanelTest() {

    }

    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200, 200));
        //Display the Frame.
        frame.pack();
        frame.setVisible(true);

        mainPanel = new JPanel(new BorderLayout());

        mainPanel.setOpaque(true);
        mainPanel.setBackground(Color.WHITE);
        frame.getContentPane().add(mainPanel);
//        frame.add(mainPanel);

        JLabel label1 = new JLabel("Label 01");
        label1.setOpaque(false);
        JLabel label2 = new JLabel("Label 02");
//        JButton button = new JButton("Close");
        panel = new JPanel(new BorderLayout()) {
            //            public void paintComponent(Graphics g) {
//                g.setColor(background);
//                Rectangle r = g.getClipBounds();
//                g.fillRect(r.x, r.y, r.width, r.height);
//                super.paintComponent(g);
//
////                Graphics2D g2d = (Graphics2D) g;
////                g2d.setComposite(FadingTest.this.alphaComp);
////                super.paintComponent(g2d);
//            }
            public void paint(Graphics g) {
                System.out.println("paint " + opacity);
                Graphics2D g2d = (Graphics2D) g;
                Composite oldComp = g2d.getComposite();
//                alphaComposite = AlphaComposite.SrcOver.derive(0.f);
                g2d.setComposite(alphaComposite);

                g.setColor(background);
                Rectangle r = g.getClipBounds();
                g.fillRect(r.x, r.y, r.width, r.height);

                super.paint(g2d);
                g2d.setComposite(oldComp);
            }
        };
        panel.setBackground(background);
        panel.setOpaque(false);

        panel.add(label1, BorderLayout.NORTH);
        panel.add(label2, BorderLayout.CENTER);
//        panel.add(button, BorderLayout.SOUTH);
        mainPanel.add(panel);

        frame.validate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FadableMessagePanelTest fadingTest = new FadableMessagePanelTest();
            fadingTest.createAndShowGUI();
            fadingTest.showUp();

        });
    }
}
