package dsdsse.welcome;

import adf.utils.StandardFonts;
import adf.utils.BuildUtils;
import dsdsse.animation.PresentationRunner;
import dsdsse.app.DsdsseEnvironment;
import dsdsse.designspace.DesignSpaceView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Admin on 3/30/2017.
 */
public final class WelcomePanel extends RoundedCornersContainerPanel {

    private static WelcomePanel welcomePanel;

    public static void showWelcomePanel(JFrame frame) {
        WelcomePanel welcomePanel = new WelcomePanel(frame);
    }

    public static void destroyWelcomePanel() {
        if (welcomePanel == null) {
            return;
        }
        welcomePanel.disposeWelcomePanel();
    }

    public static final String ICON_CLASS_PATH_PREFIX = "/dsdsse-resources/images/app-icons/";
    private static final String CLOSE_BUTTON_ICON_NAME_ = "close-welcome-page-cross.png";

    private final Border PANEL_BORDER = new EmptyBorder(0, 80, 0, 80);
    //    private static final Color BACKGROUND_COLOR = new Color(0xE0f3f3e9, true);
    private static final Color BACKGROUND_COLOR = new Color(0xE0f3f3f6, true);

    private final JFrame frame;
    DesignSpaceView basePanel = DsdsseEnvironment.getDesignSpaceView();
    private final Dimension size = new Dimension(1, 1);
    private JLabel animatedTutorialLabel;
    private JLabel closeLabel;
    private final JPanel welcomeContent = new WelcomeText();

    private final Dimension CLOSE_BUTTON_SIZE = new Dimension(20, 20);

    private final MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            disposeWelcomePanel();
            if (e.getSource() == animatedTutorialLabel) {
                PresentationRunner.startIntroductoryPresentationStarter();
            }
        }
    };

    /**
     *
     */
    private void disposeWelcomePanel() {
        welcomePanel = null;
        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.remove(WelcomePanel.this);
        basePanel.removeComponentListener(componentListener);
        closeLabel.removeMouseListener(mouseListener);
        frame.repaint();
    }

    private final ComponentListener componentListener = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            updateSize();
        }

        @Override
        public void componentShown(ComponentEvent e) {
            updateSize();
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            updateSize();
        }
    };

    private void updateSize() {
        Rectangle contentPaneBounds = basePanel.getBounds();
        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.setBounds(contentPaneBounds.x, contentPaneBounds.y, contentPaneBounds.width, contentPaneBounds.height);
        setBounds(contentPaneBounds.x, contentPaneBounds.y, contentPaneBounds.width, contentPaneBounds.height);
        revalidate();
        welcomeContent.revalidate();
    }

    /**
     * @param frame
     */
    private WelcomePanel(JFrame frame) {
        super(11, RoundedRectangle.ROUND_ALL);
        welcomePanel = this;
        this.frame = frame;
        basePanel.addComponentListener(componentListener);
        setLayout(new BorderLayout());
        setBorder(PANEL_BORDER);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setSize(CLOSE_BUTTON_SIZE);
        titlePanel.setMaximumSize(CLOSE_BUTTON_SIZE);
        titlePanel.setBorder(new EmptyBorder(0, 0, 3, 0));
        titlePanel.setOpaque(false);
        titlePanel.setBackground(Color.CYAN);

        Component spaceHolder = Box.createHorizontalGlue();
        spaceHolder.setSize(CLOSE_BUTTON_SIZE);
        spaceHolder.setMaximumSize(CLOSE_BUTTON_SIZE);
        titlePanel.add(spaceHolder, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));

//        String text = "   Click to Start Brief Animated Tutorial Now   ";
        String text = "  Click here to watch self-running presentation: \"How to Use Model Creation Operations\"." +
                "  Show runs in full screen. ESC to cancel.  ";
        animatedTutorialLabel = createAnimatedTutorialLabel(text, mouseListener);
//        animatedTutorialLabel.setMaximumSize(CLOSE_BUTTON_SIZE);
//        animatedTutorialLabel.setMinimumSize(CLOSE_BUTTON_SIZE);
        animatedTutorialLabel.setPreferredSize(new Dimension(870, 24));
//        titlePanel.add(animatedTutorialLabel);
        titlePanel.add(animatedTutorialLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));

//        Component spaceHolder = Box.createHorizontalGlue();
//        titlePanel.add(spaceHolder, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
//                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        closeLabel = createCloseLabel(CLOSE_BUTTON_ICON_NAME_, mouseListener);
        closeLabel.setSize(CLOSE_BUTTON_SIZE);
        closeLabel.setMaximumSize(CLOSE_BUTTON_SIZE);
//        titlePanel.add(closeLabel);
        titlePanel.add(closeLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));

        this.add(titlePanel, BorderLayout.NORTH);


        welcomeContent.setOpaque(false);
        welcomeContent.setBackground(BACKGROUND_COLOR);
        add(welcomeContent, BorderLayout.CENTER);

        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.setOpaque(false);
        setOpaque(false);
        setBackground(BACKGROUND_COLOR);

        Rectangle contentPaneBounds = basePanel.getBounds();
        setBounds(contentPaneBounds.x, contentPaneBounds.y, contentPaneBounds.width, contentPaneBounds.height);

        layeredPane.add(this, JLayeredPane.PALETTE_LAYER);
    }

    private final JLabel createAnimatedTutorialLabel(String text, MouseListener mouseListener) {
        JLabel animatedTutorialLabel = new JLabel(text, JLabel.CENTER);
        animatedTutorialLabel.setFont(StandardFonts.FONT_HELVETICA_PLAIN_15);
        animatedTutorialLabel.setBackground(new Color(0x96FE16));
//        animatedTutorialLabel.setBackground( new Color(0x96ED16));
        animatedTutorialLabel.setForeground(new Color(0x000066));
        animatedTutorialLabel.setOpaque(true);
        animatedTutorialLabel.addMouseListener(mouseListener);
        return animatedTutorialLabel;
    }

    /**
     * @param iconName
     * @param mouseListener
     * @return
     */
    private final JLabel createCloseLabel(String iconName, MouseListener mouseListener) {
        ImageIcon menuIcon = BuildUtils.getImageIcon(ICON_CLASS_PATH_PREFIX + iconName);
        if (menuIcon == null) {
            new Exception("icon " + iconName + " not found").printStackTrace();
            return null;
        }
        JLabel closeLabel = new JLabel(menuIcon, JLabel.CENTER);
        closeLabel.setOpaque(false);
        closeLabel.addMouseListener(mouseListener);
        return closeLabel;
    }

    @Override
    public void paint(Graphics g) {
        Rectangle bounds = getBounds();
        setLocation(bounds.x, bounds.y);
        super.paint(g);
    }
}