package dsdsse.history;

import adf.app.StandardFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Admin on 10/4/2016.
 */
public class TraceLogTitlePanel extends JPanel {

    static final int TITLE_HEIGHT = 18;

    private static final Color TITLE_BACKGROUND = new Color(0x0000AA);
    private static final Color TITLE_FOREGROUND = new Color(0xFFFFFF);
    private static final Dimension TITLE_SIZE = new Dimension(1, TITLE_HEIGHT);

    private final JLabel titleLabel = new JLabel("", JLabel.LEFT);

    public TraceLogTitlePanel(String text) {
        super(new BorderLayout());
        init(text);
    }

    private void init(String text) {

//        setPreferredSize(TITLE_SIZE);
//        setMinimumSize(TITLE_SIZE);
//        setMaximumSize(TITLE_SIZE);

        // init Title Label
        titleLabel.setText(text);
        titleLabel.setOpaque(true);
//        titleLabel.setFont(StandardFonts.FONT_HELVETICA_PLAIN_11);
        titleLabel.setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
        titleLabel.setBackground(TITLE_BACKGROUND);
        titleLabel.setForeground(TITLE_FOREGROUND);
        titleLabel.setPreferredSize(TITLE_SIZE);
        titleLabel.setMinimumSize(TITLE_SIZE);
        titleLabel.setMaximumSize(TITLE_SIZE);
        titleLabel.setBorder(new EmptyBorder(0, 6, 1, 0));
        add(titleLabel, BorderLayout.CENTER);
    }
}