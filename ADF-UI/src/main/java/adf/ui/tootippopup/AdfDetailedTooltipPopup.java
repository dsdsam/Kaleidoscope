package adf.ui.tootippopup;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 1/25/14
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdfDetailedTooltipPopup extends JWindow {

    private static volatile AdfDetailedTooltipPopup adfTooltipPopup;

    public static void initPopup(JFrame frame) {
        adfTooltipPopup = new AdfDetailedTooltipPopup(frame);
    }

    public static void showTooltip(int x, int y, String tooltip) {
        adfTooltipPopup.showPopup(x, y, tooltip);
    }

    public static void hideTooltip( ) {
        adfTooltipPopup.hidePopup();
    }

    private JLabel infoLabel = new JLabel();

    /**
     *
     */
    private AdfDetailedTooltipPopup(JFrame frame) {
        super(frame);
        setBackground(new Color(0xFFFFCF));
        JPanel panel = new JPanel(new BorderLayout());
        Border border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(8,10,8,10));
        panel.setBorder(border);
        panel.add(infoLabel, BorderLayout.CENTER);
        panel.setOpaque(false);
        setContentPane(panel);
    }

    /**
     *
     */
    void showPopup(final int x, final int y, final String tooltip) {

        Runnable update = new Runnable() {

            @Override
            public void run() {
                infoLabel.setText(tooltip);
                pack();
                adfTooltipPopup.setLocation(x, y);
                adfTooltipPopup.setVisible(true);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            update.run();
        } else {
            SwingUtilities.invokeLater(update);
        }
    }

    /**
     *
     */
    void hidePopup() {
        this.setVisible(false);
    }
}