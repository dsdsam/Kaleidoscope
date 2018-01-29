package sem.appui.controls;

import adf.ui.components.borders.AlignedRoundedBorder;
import sem.appui.components.RoundedRectangleShape;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 13, 2011
 * Time: 8:50:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoundedCornersButton extends JButton implements Roundable {

    private static final Color DISABLED_BORDER_COLOR = Color.GRAY;

    private Dimension defaultSize = new Dimension(37, 37);
    public final static int NONE = 0;
    public final static int DISABLED = 1;
    private AlignedRoundedBorder alignedRoundedBorder;

    private int thickness;
    private int roundingRadius;
    private int roundingPolicy;
    private Color outerBorderColor;
    private Color innerBorderColor;

    private Color blinkingColor;

    private RoundedRectangleShape outerClipPolygon;
    private RoundedRectangleShape outerBorderPolygon;
    private RoundedRectangleShape innerBorderPolygon;

    private boolean externalAreaOpaque = true;
    private Color externalBackground = Color.BLACK; //Color.LIGHT_GRAY;

    private boolean internalAreaOpaque = true;
    private Color internalBackground = Color.LIGHT_GRAY;
    private Insets internalInsets = new Insets(0, 0, 0, 0);

    private int currentWidth;
    private int currentHeight;

//    private List<RoundedCornersButton> groupOfButtons;

    private ComponentAdapter componentListenerAdapter = new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            setdrawingSize();
        }

        public void componentShown(ComponentEvent e) {
            setdrawingSize();
        }

        private void setdrawingSize() {
//            Dimension size = RoundedCornersButton.this.getSize();
//            if (currentWidth != size.width || currentHeight != size.height) {
//                currentWidth = size.width;
//                currentHeight = size.height;
//                Rectangle rect = new Rectangle(size);
//                 System.out.println("rect " + rect.x + "  " + rect.y + "  " + rect.width + "  " + rect.height+"   "+SwingUtilities.isEventDispatchThread());
//                outerClipPolygon.setSize(0, 0, currentWidth - 1, currentHeight - 2);
//                outerBorderPolygon.setSize(0, 0, currentWidth - 1, currentHeight - 2);
//                innerBorderPolygon.setSize(0, 0, currentWidth - 1, currentHeight - 2);
//                RoundedCornersButton.this.paintImmediately(rect);
//            }
//            drawingWidth = size.width - 3;
//            drawingHeight = size.height - 3;
        }
    };

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            processActionEvent(e);
        }
    };

    public RoundedCornersButton(Color externalBackground, int thickness, int roundingRadius,
                                int roundingPolicy, Color outerBorderColor, Color innerBorderColor,
                                ImageIcon imageIcon, String text) {
        super(imageIcon);
        this.externalBackground = externalBackground;
        setBorder(null);
        setText(text);
//        setFont(new Font("Lucida", Font.BOLD, 12));
        setSize(defaultSize);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setPreferredSize(defaultSize);
        if (roundingPolicy != RoundedRectangleShape.ROUNDING_NONE) {
            setRoundingProperties(thickness, roundingRadius, roundingPolicy, outerBorderColor, innerBorderColor);
        }
        super.addActionListener(actionListener);
//        this.addComponentListener(componentListenerAdapter);
    }

    /**
     * @param thickness
     * @param roundingRadius
     * @param roundingPolicy
     * @param outerBorderColor
     * @param innerBorderColor
     */
    public void setRoundingProperties(int thickness, int roundingRadius,
                                      int roundingPolicy, Color outerBorderColor, Color innerBorderColor) {
        this.thickness = thickness;
        this.roundingRadius = roundingRadius;
        this.roundingPolicy = roundingPolicy;
        this.outerBorderColor = outerBorderColor;
        this.innerBorderColor = innerBorderColor;
        this.setOpaque(false);

        outerClipPolygon = new RoundedRectangleShape(RoundedRectangleShape.BORDER_OR05, true,
                RoundedRectangleShape.ROUNDING_ALL);
        outerBorderPolygon = new RoundedRectangleShape(RoundedRectangleShape.BORDER_OR05, true,
                RoundedRectangleShape.ROUNDING_ALL);
        innerBorderPolygon = new RoundedRectangleShape(RoundedRectangleShape.BORDER_IR05, false,
                RoundedRectangleShape.ROUNDING_ALL);

    }

    public void setOuterBorderColor(Color outerBorderColor) {
        this.outerBorderColor = outerBorderColor;
    }

    public void setInnerBorderColor(Color innerBorderColor) {
        this.innerBorderColor = innerBorderColor;
    }

    /**
     *
     * @param blinkingColor
     */
    public void setBlinkingColor(Color blinkingColor) {
        this.blinkingColor = blinkingColor;
    }

    /**
     *
     */
    public void reset() {
        this.outerBorderColor = outerBorderColor;
    }

    /**
     *
     * @param millis
     */
    public void doBlink(long millis) {
        if (blinkingColor == null) {
            return;
        }
        Color currentBackGround = this.getBackground();
        this.setBackground(blinkingColor);
        Rectangle rect = this.getBounds();
        rect.x = 0;
        rect.y = 0;

        this.paintImmediately(rect);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
        this.setBackground(currentBackGround);
        this.paintImmediately(rect);
    }

//    Rectangle origRect;

    /**
     * @param g
     */
    public void paint(Graphics g) {

        if (roundingRadius == 0) {
            super.paint(g);
            return;
        }

        Rectangle origRect = this.getBounds(); //g.getClipBounds();
//        System.out.println("origRect " + origRect.x + "  " + origRect.y + "  " + origRect.width + "  " + origRect.height);
//        if (currentWidth <= 0) {
//            return;
//        }

        if (externalAreaOpaque && externalBackground != null) {
            g.setColor(externalBackground);
//            g.fillRect(origRect.x, origRect.y, origRect.width, origRect.height);
        }

        int x = 0; //origRect.x;
        int y = 0; //origRect.y;
        int width = origRect.width;
        int height = origRect.height;

//        int x = 0;
//        int y = 0;
//        int width = currentWidth;
//        int height = currentHeight;

//        x += externalInsets.left;
//        y += externalInsets.right;
//        width -= (externalInsets.left + externalInsets.right);
//        height -= (externalInsets.top + externalInsets.bottom);
        Insets insets = getInsets();
        x += insets.left;
        y += insets.right;
        width -= (insets.left + insets.right);
        height -= (insets.top + insets.bottom);

//        if (true) {
////            super.paint(g);
//            g.setColor(Color.CYAN);
//            g.fillRect(x, y, width, height);
//            return;
//        }
//         g.setColor(Color.CYAN);
//        g.fillRect(x, y, width, height);

        if (currentWidth != width || currentHeight != height) {
            currentWidth = width;
            currentHeight = height;

            outerClipPolygon.setSize(x, y, width - 1, height - 2);
            outerBorderPolygon.setSize(x, y, width - 1, height - 2);
            innerBorderPolygon.setSize(x, y, width - 1, height - 2);
        }

        Shape origClipShape = g.getClip();
        g.setClip(outerClipPolygon);
//        g.setColor(new Color(0x004400));
        g.setColor(this.getBackground());
        g.fillRect(x, y, width, height);

        super.paint(g);
        g.setClip(origClipShape);

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (thickness >= 1) {
            if (this.isEnabled()) {
                g.setColor(outerBorderColor);
            } else {
                g.setColor(DISABLED_BORDER_COLOR);
            }
            g.drawPolygon(outerBorderPolygon);
        }
        if (thickness == 2) {
            if (this.isEnabled()) {
                g.setColor(innerBorderColor);
            } else {
                g.setColor(DISABLED_BORDER_COLOR);
            }
            g.drawPolygon(innerBorderPolygon);
        }
    }

    public void setEnabled(boolean status) {
        super.setEnabled(status);
    }

    /**
     * @param e
     */
    void processActionEvent(ActionEvent e) {
         this.doBlink(100);
    }

    /**
     * @param args
     */
    public static void main(String args[]) {

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(600, 600);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(Color.BLUE);
        mainPanel.setLayout(new BorderLayout());
        Color BORDER_COLOR = Color.WHITE;//new Color(0x00DD00);
        RoundedCornersButton roundedCornersButton = new RoundedCornersButton(Color.BLUE,
                1, 4, DirectionSetupButton.ROUNDING_ALL, BORDER_COLOR, Color.BLACK, null, "Text");
        roundedCornersButton.setBackground(new Color(0x00AA00));
        Dimension buttonSize = new Dimension(37, 37);
        roundedCornersButton.setSize(buttonSize);

//        roundedCornersButton.setProperties(1, 4, DirectionSetupButton.ROUNDING_ALL, BORDER_COLOR, Color.RED);

        mainPanel.add(roundedCornersButton);

        JFrame frame = new JFrame("Testing Frame");
        frame.setSize(600, 600);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
