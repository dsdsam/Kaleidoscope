package dsdsse.printing;

import adf.utils.StandardFonts;
import adf.utils.BuildUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created by Admin on 9/19/2016.
 */
final class GeneratingPageWaitingSign {

    public static final Color TITLE_BACKGROUND = new Color(0xFFBB00);

    static final GeneratingPageWaitingSign createInstance() {
        return new GeneratingPageWaitingSign();
    }

    private JWindow window;

    private GeneratingPageWaitingSign() {

    }

    /**
     * @param parentComponent
     */
    final void showWaitingSign(JComponent parentComponent) {
        window = new JWindow();
        window.setSize(new Dimension(240, 250));
        window.setAlwaysOnTop(true);
        window.setLocationRelativeTo(parentComponent);

        JLabel titleLabel = new JLabel("G e n e r a t i n g     P a g e", JLabel.CENTER);
        titleLabel.setFont(StandardFonts.FONT_DIALOG_BOLD_14);
        titleLabel.setPreferredSize(new Dimension(1, 23));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0xEEEEEE));
        titleLabel.setBackground(new Color(0xFFAA00));
        titleLabel.setBackground(TITLE_BACKGROUND);
        titleLabel.setForeground(new Color(0x0000CC));
        window.getContentPane().add(titleLabel, BorderLayout.NORTH);

        try {
            ImageIcon imageIcon = BuildUtils.getAdfImageIconFromClassPath("waiting-sign.gif");
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setBorder(new MatteBorder(0, 3, 3, 3, TITLE_BACKGROUND));
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.WHITE);
            window.getContentPane().add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
        } finally {
            window.setVisible(true);
        }
    }

    final void hideWaitingSign() {
        window.setVisible(false);
        window.dispose();
    }

}

