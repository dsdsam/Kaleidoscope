package led;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 2/19/13
 * Time: 10:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class UndecoratedFrame extends JFrame {

    public static enum FrameMode {EDT, NOT_EDT}

    public static UndecoratedFrame undecoratedFrame;
    private JPanel contentPane;

    private boolean resizeNorth;
    private boolean resizeWest;
    private boolean resizeSouth;
    private boolean resizeEast;

    private Insets insets = new Insets(0, 0, 0, 0);

    private MouseAdapter resizeListener = new MouseAdapter() {
        private int NOTHING = 0;
        private int NORTH = 1;
        private int WEST = 2;
        private int SOUTH = 3;
        private int EAST = 4;
        private int gripOn;

        private Point point = new Point();

        public void mouseMoved(MouseEvent e) {
            gripOn = NOTHING;
            if (e.getY() < insets.top) {
                gripOn = NORTH;
            } else if (e.getX() < insets.left) {
                gripOn = WEST;
                undecoratedFrame.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            } else if (e.getY() > (getHeight() - insets.bottom)) {
                gripOn = SOUTH;
            } else if (e.getX() > (getWidth() - insets.right)) {
                gripOn = EAST;
                undecoratedFrame.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            } else {
                undecoratedFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

//        public void mouseMoved(MouseEvent e) {
//            gripOn = NOTHING;
//            if (e.getY() < 10) {
//                gripOn = NORTH;
//            } else if (e.getX() < 10) {
//                gripOn = WEST;
//                undecoratedFrame.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
//            } else if (e.getY() > (getHeight() - 10)) {
//                gripOn = SOUTH;
//            } else if (e.getX() > (getWidth() - 10)) {
//                gripOn = EAST;
//                undecoratedFrame.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
//            } else {
//                undecoratedFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//            }
//        }

        @Override
        public synchronized void mousePressed(MouseEvent e) {
            System.out.println("mousePressed " + UndecoratedFrame.this.hasFocus());
            point.x = e.getX();
            point.y = e.getY();
        }

        @Override
        public synchronized void mouseReleased(MouseEvent e) {
            System.out.println("mousePressed " + UndecoratedFrame.this.hasFocus());
            gripOn = NOTHING;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (gripOn == NOTHING) {
                System.out.println("mouseDragged " + UndecoratedFrame.this.hasFocus());
                int deltaX = e.getX() - point.x;
                int deltaY = e.getY() - point.y;
                Point currentLocation = getLocation();
                setLocation(currentLocation.x + deltaX, currentLocation.y + deltaY);
                return;
            }

            Rectangle bounds = getBounds();
            Point p = e.getPoint();
            System.out.println();
            if (resizeNorth && gripOn == NORTH) {
                invalidate();
                SwingUtilities.convertPointToScreen(p, getContentPane());
                int diff = bounds.y - p.y;
                bounds.y = p.y;
                bounds.height += diff;
                setBounds(bounds);
                validate();
            } else if (resizeWest && gripOn == WEST) {
                invalidate();
                SwingUtilities.convertPointToScreen(p, getContentPane());
                int diff = bounds.x - p.x;
                bounds.x = p.x;
                bounds.width += diff;
                setBounds(bounds);
                validate();
            } else if (resizeSouth && gripOn == SOUTH) {
                invalidate();
                bounds.height = p.y;
                setBounds(bounds);
                validate();
            } else if (resizeEast && gripOn == EAST) {
                invalidate();
                bounds.width = p.x;
                setBounds(bounds);
                validate();
            }
        }
    };

    /**
     *
     */
    MouseAdapter mouseListener = new MouseAdapter() {

        private Point point = new Point();

        @Override
        public synchronized void mousePressed(MouseEvent e) {
            System.out.println("mousePressed " + UndecoratedFrame.this.hasFocus());
            point.x = e.getX();
            point.y = e.getY();
        }

        @Override
        public synchronized void mouseDragged(MouseEvent e) {
            System.out.println("mouseDragged " + UndecoratedFrame.this.hasFocus());
            int deltaX = e.getX() - point.x;
            int deltaY = e.getY() - point.y;
            Point currentLocation = getLocation();
            setLocation(currentLocation.x + deltaX, currentLocation.y + deltaY);
        }
    };

    /**
     *
     */
    public UndecoratedFrame(FrameMode mode, boolean hasBorder,
                            boolean resizeNorth, boolean resizeWest, boolean resizeSouth, boolean resizeEast) {
        super("");
        this.resizeNorth = resizeNorth;
        this.resizeWest = resizeWest;
        this.resizeSouth = resizeSouth;
        this.resizeEast = resizeEast;
        this.setFocusable(true);
        undecoratedFrame = this;
        setUndecorated(true);

//        ((JPanel) this.getContentPane()).setBorder(new MatteBorder(3, 3, 3, 3, Color.red));

        // adding dragging mechanism
        contentPane = (JPanel) getContentPane();
        contentPane.addMouseListener(resizeListener);
        contentPane.addMouseMotionListener(resizeListener);

        if (mode == FrameMode.NOT_EDT) {
            // this staff is for direct painting
            setIgnoreRepaint(true); // disable OS-triggered repaint requests
            // disable double-buffering since we do this in the BufferStrategy
            RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);
        }

        if (hasBorder) {
            Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createBevelBorder(BevelBorder.RAISED,
                            Color.LIGHT_GRAY, Color.LIGHT_GRAY,
                            Color.GRAY, Color.GRAY),
                    BorderFactory.createBevelBorder(BevelBorder.LOWERED,
                            Color.LIGHT_GRAY, Color.LIGHT_GRAY,
                            Color.GRAY, Color.GRAY)
            );
            contentPane.setBorder(border);
            insets = contentPane.getInsets();
        }
    }

    public static void main(String[] args) {
        final JFrame frame = new UndecoratedFrame(FrameMode.EDT, true, false, true, false, true);

        JPanel mainPanel = new JPanel();
//        mainPanel.setBorder(new MatteBorder(3, 3, 3, 3, Color.red));
        mainPanel.setBackground(Color.WHITE);

        JComponent contentPane = (JComponent) frame.getContentPane();
        contentPane.add(mainPanel);
        frame.setSize(600, 53);
        frame.setLocation(300, 400);
        frame.setVisible(true);
    }


//    public void resizeWindowWidth(java.awt.event.MouseEvent evt) {
//        this.positionx = evt.getXOnScreen();
//        if (this.positionx > this.x1) {
//            this.x2 = this.positionx - this.x1;
//            this.frame.setSize(this.frame.getSize().width + this.x2, this.frame.getSize().height);
//        } else if (this.positionx < this.x1) {
//            this.x2 = this.x1 - this.positionx;
//            this.frame.setSize(this.frame.getSize().width - this.x2, this.frame.getSize().height);
//        }
//        this.x1 = this.positionx;
//    }
//
//    public void resizeWindowHeight(java.awt.event.MouseEvent evt) {
//        this.positiony = evt.getYOnScreen();
//        if (this.positiony > this.y1) {
//            this.y2 = this.positiony - this.y1;
//            this.frame.setSize(this.frame.getSize().width, this.frame.getSize().height + this.y2);
//        } else if (this.positiony < this.y1) {
//            this.y2 = this.y1 - this.positiony;
//            this.frame.setSize(this.frame.getSize().width, this.frame.getSize().height - this.y2);
//        }
//        this.y1 = this.positiony;
//    }
//
//    public void onPress(java.awt.event.MouseEvent evt) {
//        this.x1 = evt.getXOnScreen();
//        this.y1 = evt.getYOnScreen();
//    }

}
