package adf.menu;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

/** @see http://stackoverflow.com/questions/4038605 */
public class FileMenu {

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FileMenu().create();
            }
        });
    }

    void create() {
        File userDir = new File(System.getProperty("user.dir"));
        File[] files = userDir.listFiles();

        JMenu menu = new JMenu("Recent Files");
        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        JLabel label = new JLabel(" ", JLabel.CENTER);
        for (File f : files) {
            if (f.isFile() && !f.isHidden()) {
                RecentFile rf = new RecentFile(f, label);
                menu.add(new JMenuItem(rf.getAction()));
                toolBar.add(rf.getAction());
            }
        }
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);

        JFrame f = new JFrame("FileMenu");
        f.setJMenuBar(menuBar);
        f.add(toolBar, BorderLayout.CENTER);
        f.add(label, BorderLayout.SOUTH);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

class RecentFile extends AbstractAction {

    private final File file;
    private final JLabel label;

    public RecentFile(final File file, final JLabel label) {
        this.file = file;
        this.label = label;
        this.putValue(Action.NAME, file.getName());
        this.putValue(Action.SHORT_DESCRIPTION, file.getAbsolutePath());
    }

    public void actionPerformed(ActionEvent e) {
        label.setText(file.getName());

    }

    public Action getAction() {
        return this;
    }
}