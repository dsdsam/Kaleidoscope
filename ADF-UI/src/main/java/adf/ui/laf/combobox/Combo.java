package adf.ui.laf.combobox;

import javax.swing.*;
import java.awt.*;

/**
 * Created by u0180093 on 4/26/2016.
 */
public class Combo {
    static String LOOK_AND_FEEL = "com.fxall.gui.laf.OrionLookAndFeel";

    private static void createPanel() {
//        try {
//            UIManager.setLookAndFeel(LOOK_AND_FEEL);
//
//        } catch (ClassNotFoundException x) {
//        } catch (Exception e) {
//        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(0xFFFFEE));

        JComboBox<String> entityComboBox2 = new ComboBox3D();
        entityComboBox2.setEditable(false);
        entityComboBox2.addItem("123456");
        entityComboBox2.addItem("12345678912345");
        entityComboBox2.setPreferredSize(new Dimension(110, 25));
//        panel.add(entityComboBox2);
        panel.add(entityComboBox2, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        panel.add(Box.createGlue(), new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

//        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
//
//        String[] items ={"Item01", "Item02"};
//        JComboBox<String> comboBox = new ComboBox3D();
//        comboBox.setPreferredSize(new Dimension(200, 22));
//        comboBox.setMinimumSize(new Dimension(200, 22));
//        comboBox.addItem("Item01ABCDEF");
//        comboBox.addItem("Item02");
//        panel.add(comboBox, BorderLayout.CENTER);
        frame.add(panel);


        frame.setSize(400, 80);
        frame.setLocation(300, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Combo.createPanel();
            }
        });
    }
}

