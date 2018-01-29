package adf.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by Admin on 4/20/2017.
 */
public class WaitingSignPopup extends JLabel {

    private static Dimension WAITING_SIGN_SIZE = new Dimension(100, 100);

    private static Color WAITING_SIGN_BACKGROUND0 = new Color(255, 255, 215);
    private static Color WAITING_SIGN_BACKGROUND1 = new Color(0xEFFBFF);
    private static Color WAITING_SIGN_BACKGROUND = new Color(0xf3f3e9);
    private static Color WAITING_SIGN_BACKGROUND3 = new Color(0xf0f0ff);

    private static WaitingSignPopup waitingSignPopup;
    private JComponent backgroundComponent;

    /**
     * showSign
     */
    public static final void showWaitingSign() {
        waitingSignPopup.setVisible(true);
    }

    public static final void showWaitingSign(JComponent backgroundComponent) {
        waitingSignPopup.setVisible(true, backgroundComponent);
    }

    /**
     *
     */
    public static final void hideWaitingSign() {
        waitingSignPopup.setVisible(false, null);
    }

    //
    //   I n s t a n c e
    //

    private final JFrame frame;
    private final JLayeredPane layeredPane;

    private final ComponentAdapter componentAdapter = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            layoutTheSign();
        }
    };

    /**
     * @param frame
     */
    public WaitingSignPopup(JFrame frame) {
        super(BuildUtils.getImageIcon("/images/waiting-sign.gif"));
        this.setSize(WAITING_SIGN_SIZE);
        this.setPreferredSize(WAITING_SIGN_SIZE);
        this.setMinimumSize(WAITING_SIGN_SIZE);
        this.setMaximumSize(WAITING_SIGN_SIZE);

        this.setOpaque(true);
        this.setBackground(WAITING_SIGN_BACKGROUND);

        this.frame = frame;
        layeredPane = frame.getLayeredPane();
        setVisible(false);
        layeredPane.addComponentListener(componentAdapter);
        layeredPane.add(this, new Integer(500));

        waitingSignPopup = this;
    }

    public void setVisible(boolean status) {
        this.backgroundComponent = null;
        layoutTheSign();
        super.setVisible(status);
    }

    public void setVisible(boolean status, JComponent backgroundComponent) {
        this.backgroundComponent = backgroundComponent;
        layoutTheSign();
        super.setVisible(status);
    }

    private void layoutTheSign() {
        Rectangle tmpBounds;
        JComponent component;
        if (backgroundComponent != null) {
            tmpBounds = backgroundComponent.getBounds();
            component = backgroundComponent;
        } else {
            tmpBounds = layeredPane.getBounds();
            component = layeredPane;
        }
        int xLocation = (tmpBounds.width - WAITING_SIGN_SIZE.width) / 2;
        int yLocation = (tmpBounds.height - WAITING_SIGN_SIZE.height) / 2;
        Point point = new Point(xLocation, yLocation);
        point = SwingUtilities.convertPoint(component, point, frame.getRootPane());
        point = SwingUtilities.convertPoint(frame.getRootPane(), point, layeredPane);
        this.setBounds(point.x, point.y, WAITING_SIGN_SIZE.width, WAITING_SIGN_SIZE.height);
    }

    /**
     *
     */
//    private void layoutTheSign() {
//        Rectangle parentPanelBounds;
//        parentPanelBounds = frame.getRootPane().getBounds();
//        int xLocation = (parentPanelBounds.width - WAITING_SIGN_SIZE.width) / 2;
//        int yLocation = (parentPanelBounds.height - WAITING_SIGN_SIZE.height) / 2;
//        this.setBounds(xLocation, yLocation, WAITING_SIGN_SIZE.width, WAITING_SIGN_SIZE.height);
//        System.out.println("x = " + xLocation + "y = " + yLocation);
//    }
}

