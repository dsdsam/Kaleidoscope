package adf.menu.examples;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

public class RadioButtonMenuItemExample extends JPanel {

    public JTextPane pane;

    public JMenuBar menuBar;

    public JToolBar toolBar;

    public RadioButtonMenuItemExample() {
        menuBar = new JMenuBar();
        JMenu justifyMenu = new JMenu("Justify");
        ActionListener actionPrinter = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    pane.getStyledDocument().insertString(0,"Action [" + e.getActionCommand()+ "] performed!\n", null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        JRadioButtonMenuItem leftJustify = new JRadioButtonMenuItem("Left",new ImageIcon("1.gif"));
        leftJustify.setHorizontalTextPosition(JMenuItem.RIGHT);
        leftJustify.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        leftJustify.addActionListener(actionPrinter);
        JRadioButtonMenuItem rightJustify = new JRadioButtonMenuItem("Right",new ImageIcon("2.gif"));
        rightJustify.setHorizontalTextPosition(JMenuItem.RIGHT);
        rightJustify.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        rightJustify.addActionListener(actionPrinter);
        JRadioButtonMenuItem centerJustify = new JRadioButtonMenuItem("Center",new ImageIcon("3.gif"));
        centerJustify.setHorizontalTextPosition(JMenuItem.RIGHT);
        centerJustify.setAccelerator(KeyStroke.getKeyStroke('M', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        centerJustify.addActionListener(actionPrinter);
        JRadioButtonMenuItem fullJustify = new JRadioButtonMenuItem("Full",new ImageIcon("4.gif"));
        fullJustify.setHorizontalTextPosition(JMenuItem.RIGHT);
        fullJustify.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fullJustify.addActionListener(actionPrinter);

        ButtonGroup group = new ButtonGroup();
        group.add(leftJustify);
        group.add(rightJustify);
        group.add(centerJustify);
        group.add(fullJustify);

        justifyMenu.add(leftJustify);
        justifyMenu.add(rightJustify);
        justifyMenu.add(centerJustify);
        justifyMenu.add(fullJustify);

        menuBar.add(justifyMenu);
        menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
    }

    public static void main(String s[]) {

        RadioButtonMenuItemExample example = new RadioButtonMenuItemExample();
        example.pane = new JTextPane();
        example.pane.setPreferredSize(new Dimension(250, 250));
        example.pane.setBorder(new BevelBorder(BevelBorder.LOWERED));

        JFrame frame = new JFrame("Menu Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(example.menuBar);
        frame.getContentPane().add(example.pane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
