package dsdsse.messageflydownpanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 5/30/2017.
 */
class MessageFlyDownRoundedCornersPanel extends JPanel {

    private final RoundedRectangle roundedClippingRectangle = new RoundedRectangle();

    MessageFlyDownRoundedCornersPanel() {
        this(RoundedRectangle.ROUND_ALL);
    }

    public MessageFlyDownRoundedCornersPanel(int roundingPolicy) {
        super(new BorderLayout());
        setBackground(Color.BLACK);
        setOpaque(false);
        roundedClippingRectangle.setRoundingPolicy(roundingPolicy);
    }

    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        Rectangle bounds = getBounds();

        Insets insets = getInsets();
        roundedClippingRectangle.updateSize(insets, bounds.width, bounds.height);
        if (roundedClippingRectangle.clippingPolygon != null) {
            g2.clip(roundedClippingRectangle.clippingPolygon);
        }

        // fill the rectangle with background
        g.setColor(getBackground());
        g.fillRect(0, 0, bounds.width, bounds.height);

        super.paint(g);
        g2.setClip(null);

        // draw the one line border
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        g2.draw(roundedClippingRectangle.clippingPolygon);
    }

    //
    //   T e s t i n g
    //

    private void createTest() {

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(1, 22));
        titlePanel.setOpaque(true);
        titlePanel.setBackground(Color.ORANGE);
        add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(1, 22));
        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.PINK);
        add(contentPanel, BorderLayout.CENTER);

        JFrame mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        Dimension size = new Dimension(240, 200);
        mainFrame.setSize(size);
        mainFrame.setMinimumSize(size);

        mainFrame.add(this, BorderLayout.CENTER);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MessageFlyDownRoundedCornersPanel messageFlyDownRoundedCornersPanel =
                    new MessageFlyDownRoundedCornersPanel(RoundedRectangle.ROUND_LEFT_SIDE);
            messageFlyDownRoundedCornersPanel.createTest();
        });
    }
}
