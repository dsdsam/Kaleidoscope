package dsdsse.splash;

import adf.app.AppManifestAttributes;
import adf.ui.components.panels.ImagePanel;
import adf.utils.BuildUtils;
import dsdsse.app.DsdsDseDesignSpaceHolderPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by Admin on 9/11/2016.
 */
public class DsdsseSplash extends JDialog {

    private static final String BUILD_DATE = "Built on:  ";

    private static final String SPLASH_IMAGE_PATH = "carina-nebula-505x324.jpg";
    private static final String SPLASH_CLOSE_HIDDEN_BUTTON_PATH = "close5.gif";
    private static final String SPLASH_CLOSE_ACTIVE_BUTTON_PATH = "close25.gif";

    private static final int SPLASH_WIDTH = 505;
    private static final int SPLASH_HEIGHT = 324;
    private static final Dimension SPLASH_SIZE = new Dimension(SPLASH_WIDTH, SPLASH_HEIGHT);

    private static final Color PROJECT_NAME_FOREGROUND = new Color(0xFF0000);
    private static final Color PROJECT_NAME_FOREGROUND1 = new Color(0xEE0099);
    private static final Color PROJECT_NAME_FOREGROUND2 = new Color(0xB66CFF);
    private static final Color PROJECT_NAME_FOREGROUND3 = new Color(0x8000FF);

    private void closeAboutPopup() {
        if (splashPopup == null) {
            return;
        }
        splashPopup.setVisible(false);
        splashPopup.dispose();
        splashPopup = null;
    }

    public static void showAboutPopup(JFrame frame) {

        String appVersion = AppManifestAttributes.getAppVersion();
        String buildDate = AppManifestAttributes.getAppBuiltDate();

        ImageIcon backgroundImageIcon = BuildUtils.getImageIcon(DsdsDseDesignSpaceHolderPanel.HELP_PREFIX + SPLASH_IMAGE_PATH);
        ImageIcon initialCloseButtonIcon = BuildUtils.getImageIcon(DsdsDseDesignSpaceHolderPanel.HELP_PREFIX +
                SPLASH_CLOSE_HIDDEN_BUTTON_PATH);
        ImageIcon activeCloseButtonIcon = BuildUtils.getImageIcon(DsdsDseDesignSpaceHolderPanel.HELP_PREFIX +
                SPLASH_CLOSE_ACTIVE_BUTTON_PATH);
        DsdsseSplash dsdsseSplash = new DsdsseSplash(frame, appVersion, buildDate, backgroundImageIcon,
                initialCloseButtonIcon, activeCloseButtonIcon);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screenSize.getWidth() - SPLASH_SIZE.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - SPLASH_SIZE.getHeight()) / 2);
        dsdsseSplash.setLocation(x, y);
        dsdsseSplash.toFront();
        dsdsseSplash.setVisible(true);
    }

    private JDialog splashPopup;

    private ImageIcon initialCloseButtonIcon;
    private ImageIcon activeCloseButtonIcon;
    private JButton closeButton;
    private Point location;


    private DsdsseSplash(JFrame frame, String version, String buildDate, ImageIcon backgroundImageIcon,
                         ImageIcon initialCloseButtonIcon, ImageIcon activeCloseButtonIcon) {
        super(frame);
        splashPopup = this;
        splashPopup.setUndecorated(true);
        splashPopup.setModal(false);
        Image image = backgroundImageIcon.getImage();
        this.initialCloseButtonIcon = initialCloseButtonIcon;
        this.activeCloseButtonIcon = activeCloseButtonIcon;

        this.setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
        image = image.getScaledInstance(SPLASH_WIDTH, SPLASH_HEIGHT, Image.SCALE_SMOOTH);
        backgroundImageIcon.setImage(image);

        ImagePanel imagePanel = new ImagePanel();
        imagePanel.setLayout(null);
        imagePanel.setImage(backgroundImageIcon);


        Dimension imagePanelSize = new Dimension(SPLASH_WIDTH, SPLASH_HEIGHT);
        imagePanel.setPreferredSize(imagePanelSize);
        getContentPane().add(imagePanel, BorderLayout.CENTER);

        System.getProperties().list(System.out);

        int xOrg = 0;
        int yOrg = 26;

        int olw = 240;
        int alw = 220;
        int labelHeight = 24;

        int textSize = 14;

        // Label Discrete
        JLabel labelDiscrete = new JLabel("Discrete Symbolic Dynamical Systems", JLabel.CENTER);
        labelDiscrete.setOpaque(false);
        labelDiscrete.setBounds(xOrg, yOrg, SPLASH_WIDTH, labelHeight);
        labelDiscrete.setFont(new Font("Monospaced", Font.BOLD, 22));
        imagePanel.add(labelDiscrete);

        // Label Simulating Environment
        JLabel labelSimulator = new JLabel("Development  &  Simulating   Environment", JLabel.CENTER);
        labelSimulator.setBounds(0, 56, SPLASH_WIDTH, labelHeight);
        labelSimulator.setFont(new Font("Serif", Font.BOLD, 22));
        imagePanel.add(labelSimulator);

        labelDiscrete.setForeground(PROJECT_NAME_FOREGROUND);
        labelSimulator.setForeground(PROJECT_NAME_FOREGROUND);

        // Label Version
        int infoYBase = 125;
        JLabel versionLabel = new JLabel("Version:  " + version, JLabel.LEFT);
        versionLabel.setBounds(xOrg + 35, infoYBase, 400, labelHeight);
        versionLabel.setForeground(Color.YELLOW);
        versionLabel.setFont(new Font("Arial", Font.BOLD, textSize + 1));
        imagePanel.add(versionLabel);

        // Label Build
        JLabel buildLabel = new JLabel(BUILD_DATE + buildDate, JLabel.LEFT);
        buildLabel.setBounds(xOrg + 35, infoYBase + 20, 400, labelHeight);
        buildLabel.setForeground(Color.yellow);
        buildLabel.setFont(new Font("Arial", Font.BOLD, textSize));
        imagePanel.add(buildLabel);

        // Label JAE
        String jreValue = System.getProperty("java.runtime.version");
        JLabel jreLabel = new JLabel("JRE: " + jreValue, JLabel.LEFT);
        jreLabel.setBounds(xOrg + 35, infoYBase + 50, 400, labelHeight);
        jreLabel.setForeground(Color.yellow);
        jreLabel.setFont(new Font("Arial", Font.PLAIN, textSize));
        imagePanel.add(jreLabel);

        // Label JVM
        String jvmValue = System.getProperty("java.vm.name") + " by " + System.getProperty("java.vm.vendor");
        JLabel jvmLabel = new JLabel("JVM: " + jvmValue, JLabel.LEFT);
        jvmLabel.setBounds(xOrg + 35, infoYBase + 70, 460, labelHeight);
        jvmLabel.setForeground(Color.yellow);
        jvmLabel.setFont(new Font("Arial", Font.PLAIN, textSize));
        imagePanel.add(jvmLabel);

        // Label Rights
        JLabel rightsLabel = new JLabel("\u00a9 2015-2018 Vlad Lakin. All right reserved.", JLabel.LEFT);
        rightsLabel.setBounds(xOrg + 190, SPLASH_HEIGHT - 27, 400, labelHeight);
        rightsLabel.setForeground(Color.WHITE);
        rightsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        imagePanel.add(rightsLabel);

        closeButton = new JButton(initialCloseButtonIcon);
        closeButton.setRolloverIcon(activeCloseButtonIcon);
        closeButton.setBorder(null);
        closeButton.setEnabled(true);
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setHorizontalAlignment(SwingConstants.CENTER);
        closeButton.setVerticalAlignment(SwingConstants.TOP);
        closeButton.setBounds(SPLASH_WIDTH - 71, SPLASH_HEIGHT - 24, 62, 16);
        closeButton.addActionListener((e) -> {
            closeAboutPopup();
        });

        imagePanel.add(closeButton);

        Rectangle frameBounds = frame.getBounds();
        setLocation(frameBounds);
        pack();

        imagePanel.addMouseListener(new MouseAdapter() {

            /**
             * Invoked when the mouse enters a component.
             */
            public void mouseEntered(MouseEvent e) {
                closeButton.setIcon(DsdsseSplash.this.activeCloseButtonIcon);
            }

            /**
             * Invoked when the mouse exits a component.
             */
            public void mouseExited(MouseEvent e) {
                closeButton.setIcon(DsdsseSplash.this.initialCloseButtonIcon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                closeAboutPopup();
                e.consume();
            }
        });


        imagePanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                closeButton.setIcon(DsdsseSplash.this.activeCloseButtonIcon);

            }
        });

        this.setAlwaysOnTop(true);
    }

    /**
     * @param frameBounds
     */
    private final void setLocation(Rectangle frameBounds) {
        int myWidth = this.getWidth();
        int myHeight = this.getHeight();
        location = new Point(frameBounds.x + (frameBounds.width - myWidth) / 2,
                frameBounds.y + (frameBounds.height - myHeight) / 2);
        setLocation(location);
    }

    /**
     *
     */
    public final void showSplash() {
        splashPopup.setVisible(true);
    }

    /**
     *
     */
    public final void closeSplash() {
        splashPopup.setVisible(false);
        dispose();
    }
}