package dsdsse.graphview;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 1/31/2016.
 */
public class GraphElementTooltip extends JPanel {

    private final JLabel label = new JLabel();

    public GraphElementTooltip(){

        setBackground(Color.RED);
        setOpaque(true);
        add(label);
    }

    void setText(String text){
        label.setText(text);
    }
}
