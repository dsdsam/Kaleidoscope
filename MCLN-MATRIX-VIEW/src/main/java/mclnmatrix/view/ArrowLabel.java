package mclnmatrix.view;

import javax.swing.*;
import java.awt.*;

public class ArrowLabel extends JLabel {

    private static Dimension size;

    public ArrowLabel(String text, int width, int height) {
        super(text, JLabel.CENTER);
        setFont(getFont().deriveFont(Font.BOLD, 20));
        size = new Dimension(width, height);
        setSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }
}
