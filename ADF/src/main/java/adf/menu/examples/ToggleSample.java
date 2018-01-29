package adf.menu.examples;

import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.event.MouseInputListener;

public class ToggleSample {
    public static void main(String args[]) {
        JFrame frame = new JFrame("JToggleButtonMenuItem Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic('f');
        JMenuItem newItem = new JMenuItem("New", 'N');
        file.add(newItem);
        JMenuItem openItem = new JMenuItem("Open", 'O');
        file.add(openItem);
        JMenuItem closeItem = new JMenuItem("Close", 'C');
        file.add(closeItem);
        file.addSeparator();
        JMenuItem saveItem = new JMenuItem("Save", 'S');
        file.add(saveItem);
        file.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit", 'X');
        file.add(exitItem);
        bar.add(file);
        JMenu edit = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut", 'T');
        cutItem.setAccelerator(KeyStroke.getKeyStroke('X', Event.CTRL_MASK));
        edit.add(cutItem);
        JMenuItem copyItem = new JMenuItem("Copy", 'C');
        copyItem.setAccelerator(KeyStroke.getKeyStroke('C', Event.CTRL_MASK));
        edit.add(copyItem);
        JMenuItem pasteItem = new JMenuItem("Paste", 'P');
        pasteItem.setAccelerator(KeyStroke.getKeyStroke('V', Event.CTRL_MASK));
        pasteItem.setEnabled(false);
        edit.add(pasteItem);
        edit.addSeparator();
        JMenuItem findItem = new JMenuItem("Find", 'F');
        findItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        edit.add(findItem);
        edit.setMnemonic('e');
        Icon atIcon = new ImageIcon("at.gif");
        JMenu findOptions = new JMenu("Options");
        findOptions.setIcon(atIcon);
        findOptions.setMnemonic('O');
        ButtonGroup directionGroup = new ButtonGroup();
        JRadioButtonMenuItem forward = new JRadioButtonMenuItem("Forward", true);
        findOptions.add(forward);
        directionGroup.add(forward);
        JRadioButtonMenuItem backward = new JRadioButtonMenuItem("Backward");
        findOptions.add(backward);
        directionGroup.add(backward);
        findOptions.addSeparator();
        JCheckBoxMenuItem caseItem = new JCheckBoxMenuItem("Case Insensitive");
        findOptions.add(caseItem);
        edit.add(findOptions);
        JToggleButtonMenuItem toggleItem = new JToggleButtonMenuItem(
                "Ballon Help");
        toggleItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Selected");
            }
        });
        edit.add(toggleItem);
        bar.add(edit);
        frame.setJMenuBar(bar);
        frame.setSize(350, 250);
        frame.setVisible(true);
    }
}

class JToggleButtonMenuItem extends JToggleButton implements MenuElement {
    Color savedForeground = null;

    private static MenuElement NO_SUB_ELEMENTS[] = new MenuElement[0];

    public JToggleButtonMenuItem() {
        init();
    }

    public JToggleButtonMenuItem(String label) {
        super(label);
        init();
    }

    public JToggleButtonMenuItem(String label, Icon icon) {
        super(label, icon);
        init();
    }

    public JToggleButtonMenuItem(Action action) {
        super(action);
        init();
    }

    private void init() {
        updateUI();
        setRequestFocusEnabled(false);
        // Borrows heavily from BasicMenuUI
        MouseInputListener mouseInputListener = new MouseInputListener() {
            // If mouse released over this menu item, activate it
            public void mouseReleased(MouseEvent mouseEvent) {
                MenuSelectionManager menuSelectionManager = MenuSelectionManager
                        .defaultManager();
                Point point = mouseEvent.getPoint();
                if ((point.x >= 0) && (point.x < getWidth()) && (point.y >= 0)
                        && (point.y < getHeight())) {
                    menuSelectionManager.clearSelectedPath();
                    // component automatically handles "selection" at this point
                    // doClick(0); // not necessary
                } else {
                    menuSelectionManager.processMouseEvent(mouseEvent);
                }
            }

            // If mouse moves over menu item, add to selection path, so it
            // becomes armed
            public void mouseEntered(MouseEvent mouseEvent) {
                MenuSelectionManager menuSelectionManager = MenuSelectionManager
                        .defaultManager();
                menuSelectionManager.setSelectedPath(getPath());
            }

            // When mouse moves away from menu item, disaarm it and select
            // something else
            public void mouseExited(MouseEvent mouseEvent) {
                MenuSelectionManager menuSelectionManager = MenuSelectionManager
                        .defaultManager();
                MenuElement path[] = menuSelectionManager.getSelectedPath();
                if (path.length > 1) {
                    MenuElement newPath[] = new MenuElement[path.length - 1];
                    for (int i = 0, c = path.length - 1; i < c; i++) {
                        newPath[i] = path[i];
                    }
                    menuSelectionManager.setSelectedPath(newPath);
                }
            }

            // Pass along drag events
            public void mouseDragged(MouseEvent mouseEvent) {
                MenuSelectionManager.defaultManager().processMouseEvent(
                        mouseEvent);
            }

            public void mouseClicked(MouseEvent mouseEvent) {
            }

            public void mousePressed(MouseEvent mouseEvent) {
            }

            public void mouseMoved(MouseEvent mouseEvent) {
            }
        };
        addMouseListener(mouseInputListener);
        addMouseMotionListener(mouseInputListener);
    }

    // MenuElement methods
    public Component getComponent() {
        return this;
    }

    public MenuElement[] getSubElements() {
        // no subelements
        return NO_SUB_ELEMENTS;
    }

    public void menuSelectionChanged(boolean isIncluded) {
        ButtonModel model = getModel();
        // only change armed state if different
        if (model.isArmed() != isIncluded) {
            model.setArmed(isIncluded);
        }

        if (isIncluded) {
            savedForeground = getForeground();
            if (!savedForeground.equals(Color.blue)) {
                setForeground(Color.blue);
            } else {
                // In case foreground blue, use something different
                setForeground(Color.red);
            }
        } else {
            setForeground(savedForeground);
            // if null, get foreground from installed look and feel
            if (savedForeground == null) {
                updateUI();
            }
        }
    }

    public void processKeyEvent(KeyEvent keyEvent, MenuElement path[],
                                MenuSelectionManager manager) {
        // If user presses space while menu item armed, select it
        if (getModel().isArmed()) {
            int keyChar = keyEvent.getKeyChar();
            if (keyChar == KeyEvent.VK_SPACE) {
                manager.clearSelectedPath();
                doClick(0); // inherited from AbstractButton
            }
        }
    }

    public void processMouseEvent(MouseEvent mouseEvent, MenuElement path[],
                                  MenuSelectionManager manager) {
        // For when mouse dragged over menu and button released
        if (mouseEvent.getID() == MouseEvent.MOUSE_RELEASED) {
            manager.clearSelectedPath();
            doClick(0); // inherited from AbstractButton
        }
    }

    // Borrows heavily from BasicMenuItemUI.getPath()
    private MenuElement[] getPath() {
        MenuSelectionManager menuSelectionManager = MenuSelectionManager
                .defaultManager();
        MenuElement oldPath[] = menuSelectionManager.getSelectedPath();
        MenuElement newPath[];
        int oldPathLength = oldPath.length;
        if (oldPathLength == 0)
            return new MenuElement[0];
        Component parent = getParent();
        if (oldPath[oldPathLength - 1].getComponent() == parent) {
            // Going deeper under the parent menu
            newPath = new MenuElement[oldPathLength + 1];
            System.arraycopy(oldPath, 0, newPath, 0, oldPathLength);
            newPath[oldPathLength] = this;
        } else {
            // Sibling/child menu item currently selected
            int newPathPosition;
            for (newPathPosition = oldPath.length - 1; newPathPosition >= 0; newPathPosition--) {
                if (oldPath[newPathPosition].getComponent() == parent) {
                    break;
                }
            }
            newPath = new MenuElement[newPathPosition + 2];
            System.arraycopy(oldPath, 0, newPath, 0, newPathPosition + 1);
            newPath[newPathPosition + 1] = this;
        }
        return newPath;
    }
}

