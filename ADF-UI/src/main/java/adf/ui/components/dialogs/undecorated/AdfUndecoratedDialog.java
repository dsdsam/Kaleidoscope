package adf.ui.components.dialogs.undecorated;

import adf.ui.components.dialogs.fading.FadingDialog;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;

/**
 * Created by Admin on 4/1/2016.
 */
public class AdfUndecoratedDialog extends FadingDialog {

    private static final Logger logger = Logger.getLogger(AdfUndecoratedDialog.class.getName());

    private static class AdfUndecoratedDialogContentPane extends JPanel {

        Cursor lastCursor;
        boolean mousePressed;
        boolean mouseDragged;

        public AdfUndecoratedDialogContentPane() {
            super.setLayout(new BorderLayout());
            enableEvents(AWTEvent.MOUSE_EVENT_MASK);
            enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
        }

        @Override
        protected void processMouseMotionEvent(MouseEvent e) {
            if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
                mouseDragged = true;
            }
            super.processMouseMotionEvent(e);
        }

        @Override
        protected void processMouseEvent(MouseEvent e) {

            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                mousePressed = true;
            }
            if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                mousePressed = false;
            }
            if (e.getID() == MouseEvent.MOUSE_EXITED) {
                lastCursor = adfUndecoratedDialog.getCursor();
                if (!mousePressed ) {
                    adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
//                if (!mousePressed && !mouseDragged) {
//                    adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//                }
            }
            super.processMouseEvent(e);
        }

//        @Override
//        protected void processMouseMotionEvent(MouseEvent e) {
//            super.processMouseMotionEvent(e);
//        }
    }


    private static AdfUndecoratedDialog adfUndecoratedDialog;
    private final JPanel contentPane;

    private boolean resizeNorth;
    private boolean resizeWest;
    private boolean resizeSouth;
    private boolean resizeEast;

    private Insets insets = new Insets(0, 0, 0, 0);

    private MouseAdapter resizeListener = new MouseAdapter() {
        private final int EXT = 0;
        private final int NOTHING = 0;
        private final int NO = 1;
        private final int WE = 2;
        private final int SO = 3;
        private final int EA = 4;
        private final int NW = 5;
        private final int NE = 6;
        private final int SW = 7;
        private final int SE = 8;
        private int gripOn;

        private Point point = new Point();

        public void mouseMoved(MouseEvent e) {
            insets = contentPane.getInsets();
//            insets.top = (insets.top > (insets.top + EXT)) ? insets.top : insets.top + EXT;
//            insets.left = (insets.left > (insets.left + EXT)) ? insets.left : insets.left + EXT;
//            insets.bottom = (insets.bottom > (insets.bottom + EXT)) ? insets.bottom : insets.bottom + EXT;
//            insets.right = (insets.right > (insets.right + EXT)) ? insets.right : insets.right + EXT;

            gripOn = NOTHING;
            adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (resizeNorth && e.getY() < insets.top) {
                if (resizeWest && e.getX() < insets.left) {
                    gripOn = NW;
                    adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                } else if (resizeEast && e.getX() > (getWidth() - insets.right)) {
                    gripOn = NE;
                    adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                } else {
                    gripOn = NO;
                    adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                }

            } else if (resizeSouth && e.getY() > (getHeight() - insets.bottom)) {
                if (resizeWest && e.getX() < insets.left) {
                    gripOn = SW;
                    adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                } else if (resizeEast && e.getX() > (getWidth() - insets.right)) {
                    gripOn = SE;
                    adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                } else {
                    gripOn = SO;
                    adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                }

            } else if (resizeWest && e.getX() < insets.left) {
                gripOn = WE;
                adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            } else if (resizeEast && e.getX() > (getWidth() - insets.right)) {
                gripOn = EA;
                adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            } else {
                adfUndecoratedDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            e.consume();
        }

//        @Override
//        public synchronized void mousePressed(MouseEvent e) {
//            point.x = e.getX();
//            point.y = e.getY();
//            e.consume();
//        }

        @Override
        public synchronized void mouseReleased(MouseEvent e) {
            gripOn = NOTHING;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (gripOn == NOTHING) {
//                int deltaX = e.getX() - point.x;
//                int deltaY = e.getY() - point.y;
//                Point currentLocation = getLocation();
//                setLocation(currentLocation.x + deltaX, currentLocation.y + deltaY);
                e.consume();
                return;
            }

            Rectangle bounds = getBounds();
            Point p = e.getPoint();
            int diff = 0;
            invalidate();
            switch (gripOn) {
                case NO:
                    SwingUtilities.convertPointToScreen(p, getContentPane());
                    diff = bounds.y - p.y;
                    bounds.y = p.y;
                    bounds.height += diff;
                    setBounds(bounds);
                    break;

                case NW:
                    SwingUtilities.convertPointToScreen(p, getContentPane());
                    diff = bounds.x - p.x;
                    bounds.x = p.x;
                    bounds.width += diff;
                    diff = bounds.y - p.y;
                    bounds.y = p.y;
                    bounds.height += diff;
                    setBounds(bounds);
                    break;
                case NE:
                    bounds.width = p.x;
                    SwingUtilities.convertPointToScreen(p, getContentPane());
                    diff = bounds.y - p.y;
                    bounds.y = p.y;
                    bounds.height += diff;
                    setBounds(bounds);
                    break;

                case WE:
                    SwingUtilities.convertPointToScreen(p, getContentPane());
                    diff = bounds.x - p.x;
                    bounds.x = p.x;
                    bounds.width += diff;
                    setBounds(bounds);
                    break;

                case EA:
                    bounds.width = p.x + 1;
                    setBounds(bounds);
                    break;

                case SO:
                    bounds.height = p.y + 1;
                    setBounds(bounds);
                    break;

                case SW:
                    bounds.height = p.y + 1;
                    SwingUtilities.convertPointToScreen(p, getContentPane());
                    diff = bounds.x - p.x;
                    bounds.x = p.x;
                    bounds.width += diff;
                    setBounds(bounds);
                    break;

                case SE:
                    bounds.width = p.x + 1;
                    bounds.height = p.y + 1;
                    setBounds(bounds);
                    break;
            }
            validate();
            e.consume();
        }
    };

    //
    //     I n s t a n c e
    //

    public AdfUndecoratedDialog() {
        this(null, false, true, null, null, true, true, true, true, true);
    }

    public AdfUndecoratedDialog(JFrame mainFrame, boolean modal, boolean fadeAble,
                                AdfUndecoratedDialogTitleBar adfUndecoratedDialogTitleBar,
                                AdfDialogClosingCallback adfDialogClosingCallback) {
        this(mainFrame, modal, fadeAble, adfUndecoratedDialogTitleBar, adfDialogClosingCallback, true, true, true,
                true, true);
    }

    private AdfUndecoratedDialog(JFrame mainFrame, boolean modal, boolean fadeAble,
                                 AdfUndecoratedDialogTitleBar adfUndecoratedDialogTitleBar,
                                 AdfDialogClosingCallback adfDialogClosingCallback, boolean hasBorder,
                                 boolean resizeNorth, boolean resizeWest, boolean resizeSouth, boolean resizeEast) {
        super(mainFrame, modal, fadeAble);
        this.setContentPane(new AdfUndecoratedDialogContentPane());
        contentPane = (JPanel) getContentPane();

        if (adfDialogClosingCallback == null) {
            adfDialogClosingCallback = new AdfDialogClosingCallback();
        }
        adfDialogClosingCallback.setAdfUndecoratedDialog(this);

        this.resizeNorth = resizeNorth;
        this.resizeWest = resizeWest;
        this.resizeSouth = resizeSouth;
        this.resizeEast = resizeEast;
        this.setFocusable(true);
        adfUndecoratedDialog = this;
//        setAlwaysOnTop(true);

        // adding dialog's header
        if (adfUndecoratedDialogTitleBar == null) {
            adfUndecoratedDialogTitleBar = new AdfUndecoratedDialogTitleBar("","", Color.BLUE, Color.WHITE);
        }
        adfUndecoratedDialogTitleBar.setAdfUndecoratedDialog(this);
        contentPane.add(adfUndecoratedDialogTitleBar, BorderLayout.NORTH);

        // adding dragging mechanism
        contentPane.addMouseListener(resizeListener);
        contentPane.addMouseMotionListener(resizeListener);

        // adding border
        Border border = null;
        if (hasBorder) {
            border = BorderFactory.createCompoundBorder(
                    BorderFactory.createBevelBorder(BevelBorder.RAISED,
                            Color.LIGHT_GRAY, Color.LIGHT_GRAY,
                            Color.LIGHT_GRAY, Color.LIGHT_GRAY),
                    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY)
            );
            border = BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY), border);
            contentPane.setBorder(border);
        }

        this.addWindowListener(adfDialogClosingCallback);

        // setting dialog min size that is equal to header panel
        // height plus dialog top and bottom border height.
        int dialogMinHeight = AdfUndecoratedDialogTitleBar.PANEL_HEIGHT;
        if (border != null) {
            Insets borderInsets = border.getBorderInsets(this);
            dialogMinHeight = dialogMinHeight + borderInsets.top + borderInsets.bottom;
        }
        setMinimumSize(new Dimension(20, dialogMinHeight));
        insets = contentPane.getInsets();
    }


//    public Insets getBorderInsets() {
////     return   super.getInsets();
//        return contentPane.getInsets();
//
//    }

    public void addAppHeaderPanel(AdfUndecoratedDialogTitleBar adfUndecoratedDialogTitleBar) {
//        this.adfUndecoratedDialogHeader = adfUndecoratedDialogHeader;
        adfUndecoratedDialogTitleBar.setAdfUndecoratedDialog(this);
        contentPane.add(adfUndecoratedDialogTitleBar, BorderLayout.NORTH);
    }

    public void addAppPanel(JPanel appPanel) {
        contentPane.add(appPanel, BorderLayout.CENTER);
    }

    /**
     * Called when Close Window button is clicked
     */
    protected boolean canTheDialogBeClosed() {
       return true;
    }

    @Override
    protected void destroyDialog() {
        removeAll();
        WindowEvent windowEvent = new WindowEvent(adfUndecoratedDialog, WindowEvent.WINDOW_CLOSING);
        processWindowEvent(windowEvent);
        super.setVisible(false);
        dispose();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
//        throw new UnsupportedOperationException("setVisible is not supported in AdfUndecoratedDialog");
    }

    public static void main(String[] args) {
        AdfDialogClosingCallback adfDialogClosingCallback = new AdfDialogClosingCallback() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (adfUndecoratedDialog != null) {
                    adfUndecoratedDialog.setVisible(false);
                    adfUndecoratedDialog = null;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\n\n");
                stringBuilder.append("************************************************************\n");
                stringBuilder.append("            A p p l i c a t i o n   C l o s e d            *\n");
                stringBuilder.append("************************************************************");
                logger.severe(stringBuilder.toString());
                System.exit(0);
            }
        };

        AdfUndecoratedDialogTitleBar adfUndecoratedDialogTitleBar = new AdfUndecoratedDialogTitleBar("", "", Color.BLUE, Color.WHITE);
        adfUndecoratedDialogTitleBar.setBackground(Color.GREEN);

        final AdfUndecoratedDialog adfUndecoratedDialog = new AdfUndecoratedDialog(new JFrame(), false, true,
                adfUndecoratedDialogTitleBar, adfDialogClosingCallback, true, true, true, true, true);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        JComponent contentPane = (JComponent) adfUndecoratedDialog.getContentPane();
        contentPane.add(mainPanel);

        AdfUndecoratedDialogPopupMenu adfUndecoratedDialogPopupMenu = new AdfUndecoratedDialogPopupMenu(adfUndecoratedDialog);
        adfUndecoratedDialogPopupMenu.setLightWeightPopupEnabled(false);
        ((JComponent) adfUndecoratedDialog.getContentPane()).setComponentPopupMenu(adfUndecoratedDialogPopupMenu);

        adfUndecoratedDialog.setSize(350, 400);
        adfUndecoratedDialog.setLocation(300, 400);
//        adfUndecoratedDialog.setVisible(true);
        adfUndecoratedDialog.bringUp();
    }
}
