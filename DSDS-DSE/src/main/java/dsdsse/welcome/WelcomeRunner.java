package dsdsse.welcome;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 3/30/2017.
 */
public class WelcomeRunner {

    private static JFrame createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Testing Fly Out Alert Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300, 100));
        //Display the Frame.
        frame.pack();
        frame.setVisible(true);

        JPanel mainPanel = new JPanel(new GridLayout(1, 3));

        mainPanel.setOpaque(true);
        mainPanel.setBackground(Color.WHITE);
        frame.add(mainPanel);

        JToggleButton b1 = new JToggleButton("1");
        JToggleButton b2 = new JToggleButton("2");
        JToggleButton b3 = new JToggleButton("3");
        mainPanel.add(b1);
        mainPanel.add(b2);
        mainPanel.add(b3);

        frame.validate();
        System.out.println();

        return frame;
    }

    private static void showWelcomePanel(JFrame frame){
        WelcomePanel.showWelcomePanel(frame);
    }

    private static void hideWelcomePanel(){

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame =    createAndShowGUI();
            showWelcomePanel(frame);
            hideWelcomePanel();
        });
    }
}
