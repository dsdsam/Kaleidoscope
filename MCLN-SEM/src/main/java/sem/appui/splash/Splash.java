package sem.appui.splash;

import adf.ui.components.panels.ImagePanel;
import sem.appui.MissionControlCenterPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class Splash extends JWindow {

    private final int SPLASH_WIDTH = 382;
    private final int SPLASH_HEIGHT = 272;

    private final JWindow splashWindow;

    private ImageIcon initialCloseButtonIcon;
    private ImageIcon activeCloseButtonIcon;
    private JButton closeButton;
    private Point location;


    public Splash(JFrame frame, String version, String appBuiltDateProperty, ImageIcon backgroundImageIcon,
                  ImageIcon initialCloseButtonIcon, ImageIcon activeCloseButtonIcon) {
        super(frame);
        splashWindow = this;
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

        int xOrg = 63;
        int yOrg = 25;
        int xOffset = 70;
        int yOffset = 37;
        Color labelBackground = new Color(0x88252525);
        Color textColor = new Color(0xFF0000);

        JLabel label = new JLabel("Space", JLabel.CENTER);

        int ilw = 66;
        int olw = 140;
        int alw = 120;
        int labelHight = 24;

        label.setBackground(labelBackground);
        label.setBounds(xOrg - 5, yOrg, ilw, labelHight);
        label.setForeground(textColor);
        label.setFont(new Font("Monospaced", Font.BOLD, 20));
        imagePanel.add(label);

        JLabel labelOutput = new JLabel("Exploration", JLabel.LEFT);
        labelOutput.setBackground(labelBackground);
        labelOutput.setBounds(xOrg + 1 * xOffset - 5, yOrg + 1 * yOffset + 5, olw, labelHight);
        labelOutput.setForeground(textColor);
        labelOutput.setFont(new Font("Monospaced", Font.BOLD, 20));
        imagePanel.add(labelOutput);

        JLabel labelAutomaton = new JLabel("Mission", JLabel.LEFT);
        labelAutomaton.setBackground(labelBackground);

        labelAutomaton.setBounds(xOrg + 3 * xOffset - 20, yOrg + 2 * yOffset + 10, alw, labelHight);
        labelAutomaton.setForeground(textColor);
        labelAutomaton.setFont(new Font("Monospaced", Font.BOLD, 20));
        imagePanel.add(labelAutomaton);

        JLabel labelSimulator = new JLabel("Simulating    Environment");
        labelSimulator.setBackground(labelBackground);
        labelSimulator.setBounds(46 + 20, 158 + 3, 250, labelHight);
        labelSimulator.setForeground(Color.red);
        labelSimulator.setFont(new Font("Serif", Font.BOLD, 22));
        imagePanel.add(labelSimulator);

        JLabel labelVersion = new JLabel(version);
        labelVersion.setBounds(xOrg - 38, 212, 150, 50);
        labelVersion.setForeground(Color.yellow);
        labelVersion.setFont(new Font("Arial", Font.PLAIN, 12));
        imagePanel.add(labelVersion);

        JLabel labelBuiltDate = new JLabel(appBuiltDateProperty);
        labelBuiltDate.setBounds(xOrg - 38, 229, 250, 50);
        labelBuiltDate.setForeground(Color.yellow);
        labelBuiltDate.setFont(new Font("Arial", Font.PLAIN, 12));
        imagePanel.add(labelBuiltDate);

        closeButton = new JButton(initialCloseButtonIcon);
        closeButton.setRolloverIcon(activeCloseButtonIcon);
        closeButton.setBorder(null);
        closeButton.setEnabled(true);
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setHorizontalAlignment(SwingConstants.CENTER);
        closeButton.setVerticalAlignment(SwingConstants.TOP);
        closeButton.setBounds(SPLASH_WIDTH - 71, SPLASH_HEIGHT - 24, 62, 16);
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MissionControlCenterPanel.getInstance().clickPowerOnButton();
                dispose();
            }
        });

        imagePanel.add(closeButton);

        Rectangle frameBounds = frame.getBounds();
        setLocation(frameBounds);
        pack();

        this.addMouseListener(new MouseAdapter() {

            /**
             * Invoked when the mouse enters a component.
             */
            public void mouseEntered(MouseEvent e) {
                closeButton.setIcon(Splash.this.activeCloseButtonIcon);
            }

            /**
             * Invoked when the mouse exits a component.
             */
            public void mouseExited(MouseEvent e) {
                closeButton.setIcon(Splash.this.initialCloseButtonIcon);
            }

        });


        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                closeButton.setIcon(Splash.this.activeCloseButtonIcon);

            }
        });

        this.toFront();
        setVisible(true);
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
        splashWindow.setVisible(true);
    }

    /**
     *
     */
    public final void closeSplash() {
        splashWindow.setVisible(false);
        dispose();
    }
}