package dsdsse.designspace.initializer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 4/23/2016.
 */
public class AdfMainPanel extends JPanel {

    static final Dimension NOTE_PANEL_SIZE = new Dimension(60, 40);
    static final Dimension INPUT_PANEL_SIZE = new Dimension(60, 40);
    static final Dimension TABLE_PANEL_SIZE = new Dimension(60, 40);

    JPanel notePanel = new JPanel();
    JPanel inputPanel = new JPanel();
    JPanel tablePanel = new JPanel();

    private void initComponents(){
        notePanel.setOpaque(true);
        notePanel.setBackground(new Color(0xFFFFEE));
        notePanel.setPreferredSize(NOTE_PANEL_SIZE);
        notePanel.setMinimumSize(NOTE_PANEL_SIZE);
        notePanel.setMaximumSize(NOTE_PANEL_SIZE);

        inputPanel.setOpaque(true);
        inputPanel.setBackground(new Color(0xD2D2D2));
        inputPanel.setPreferredSize(INPUT_PANEL_SIZE);
        inputPanel.setMinimumSize(INPUT_PANEL_SIZE);
        inputPanel.setMaximumSize(INPUT_PANEL_SIZE);

        tablePanel.setPreferredSize(TABLE_PANEL_SIZE);
        tablePanel.setMinimumSize(TABLE_PANEL_SIZE);
        tablePanel.setMaximumSize(TABLE_PANEL_SIZE);
    }

    private void layoutComponents(){
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(true);
        topPanel.setBackground(new Color(0xEEEEEE)); //238

        topPanel.add(notePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        topPanel.add(inputPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));



        JPanel bottomPanel = new JPanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topPanel, bottomPanel);
        splitPane.setBorder(null);
        splitPane.setDividerSize(4);
        splitPane.setDividerLocation(100);
        splitPane.setResizeWeight(0);

        add(splitPane, BorderLayout.CENTER);
    }

    private static void createPanel() {
        JFrame frame = new JFrame();

        AdfMainPanel adfMainPanel = new AdfMainPanel();
        adfMainPanel.setOpaque(true);
        adfMainPanel.setBackground(new Color(0xFFFFDD));
        adfMainPanel.initComponents();
        adfMainPanel.layoutComponents();
        frame.add(adfMainPanel);

        frame.setSize(600, 200);
        frame.setLocation(300, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdfMainPanel.createPanel();
            }
        });
    }
}
