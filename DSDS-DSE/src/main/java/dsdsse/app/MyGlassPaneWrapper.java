package dsdsse.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;

/**
 * Created by Admin on 2/16/2017.
 */
public class MyGlassPaneWrapper extends JLayeredPane {
    private JPanel glassPanel = new JPanel();

    public MyGlassPaneWrapper(JComponent myPanel) {
        glassPanel.setOpaque(false);
        glassPanel.setVisible(false);
        glassPanel.addMouseListener(new MouseAdapter() {});
        glassPanel.setFocusable(true);

        myPanel.setSize(myPanel.getPreferredSize());
        add(myPanel, JLayeredPane.DEFAULT_LAYER);
        add(glassPanel, JLayeredPane.PALETTE_LAYER);

        glassPanel.setPreferredSize(myPanel.getPreferredSize());
        glassPanel.setSize(myPanel.getPreferredSize());
        setPreferredSize(myPanel.getPreferredSize());
    }

    public void activateGlassPane(boolean activate) {
        glassPanel.setVisible(activate);
        if (activate) {
            glassPanel.requestFocusInWindow();
            glassPanel.setFocusTraversalKeysEnabled(false);
        }
    }

    private static void createAndShowGui() {
        final MyPanel myPanel = new MyPanel();
        final MyGlassPaneWrapper myGlassPaneWrapper = new MyGlassPaneWrapper(myPanel);
        final String activateGlassPane = "Activate GlassPane";
        final String deactivateGlassPane = "Deactivate GlassPane";

        JToggleButton toggleButton = new JToggleButton(activateGlassPane);
        toggleButton.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                AbstractButton source = (AbstractButton) e.getSource();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    source.setText(deactivateGlassPane);
                    myGlassPaneWrapper.activateGlassPane(true);
                } else {
                    source.setText(activateGlassPane);
                    myGlassPaneWrapper.activateGlassPane(false);
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(toggleButton);

        JFrame frame = new JFrame("MyGlassPane");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(myGlassPaneWrapper);
        frame.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

    private static class MyPanel extends JPanel {

        public MyPanel() {
            super(new BorderLayout());
            add(new JTextField(5), BorderLayout.NORTH);
            add(new JComboBox(new String[]{"Monday", "Tuesday", "Wednesday"}),  BorderLayout.CENTER);
            add(new JButton(new AbstractAction("Push Me") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(MyPanel.this, "Button Pushed");
                }
            }), BorderLayout.SOUTH);
        }
    }

}

