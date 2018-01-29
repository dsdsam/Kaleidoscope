package dsdsse.app.Test;

import adf.menu.AdfBasicAction;
import adf.menu.AdfToggleButtonMenuItem;
import adf.menu.AdfToggleButtonAction;
import adf.menu.AdfToggleButtonActionGroup;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 3/18/2017.
 */
public class ToggleButtonMenuItemTest extends JPanel {

    public static final String ICON_CLASS_PATH_PREFIX = "C:\\MY_STUFF\\MY-WORK\\PROJECT-DATAS\\DSDSSE\\src\\main\\resources\\dsdsse-resources\\images\\app-icons\\";
//            "/dsdsse-resources/images/app-icons/";

    private static final String RENAME_PROJECT_ICON = "rename-proj.png";
    private static final String RENAME_MODEL_ICON = "rename-model.png";
    private static final String DISCARD_MODEL_ICON = "discard-model2.png";
    private static final String OPEN_PROJECT_ICON = "open.png";
    private static final String SAVE_PROJECT_AS_ICON = "save.png";
    private static final String SHOW_PRINT_PREVIEW_ICON = "show-print.png";
    private static final String HIDE_PRINT_PREVIEW_ICON = "hide-print-2.png";

    private static final String CREATION_MODE_ICON = "edit_mode.png";
    private static final String CREATING_PROPERTIES_ICON = "create-property-nodes.png";
    private static final String STOP_CREATING_PROPERTIES_ICON = "stop-creating-property-nodes.png";
    private static final String CREATING_CONDITIONS_ICON = "create-condition-nodes.png";
    private static final String STOP_CREATING_CONDITIONS_ICON = "stop-creating-condition-nodes.png";
    //    private static final String CREATING_ARCS_ICON = "stop-operation.png";
    private static final String CREATING_ARCS_ICON = "create-arcs.png";
    private static final String STOP_CREATING_ARCS_ICON = "stop-creating-arcs.png";
    private static final String CREATING_FRAGMENT_ICON = "create-fragments.png";
    private static final String STOP_CREATING_FRAGMENTS_ICON = "stop-creating-fragments.png";

    private static final String DELETE_ICON = "delete-elements.png";
    private static final String STOP_DELETING_ICON = "stop-deleting-elements.png";
    private static final String MOVE_FRAGMENT_ICON = "move-fragments.png";
    private static final String STOP_MOVING_FRAGMENTS_ICON = "stop-moving-fragments.png";
    private static final String MOVE_MODEL_ICON = "move-model.png";
    private static final String STOP_MOVING_MODEL_ICON = "stop-moving-model.png";

    private static final String INITIALIZER_ICON = "initialize-model.png";
    private static final String STOP_INITIALING_ICON = "stop-initializing-model.png";

    private static final String SIMULATION_MODE_ICON = "exec_mode.png";
    private static final String START_SIMULATION_ICON = "start_running.gif";
    private static final String STOP_SIMULATION_ICON = "stop-simulation.png";
    private static final String PAUSE_SIMULATION_ICON = "pause-simulation.png";
    private static final String RESUME_SIMULATION_ICON = "resume-simulation.png";
    private static final String SINGLE_SIMULATION_STEP_ICON = "step-simulation.png";
    private static final String RESET_SIMULATION_ICON = "reset_orange.png";

    private static final String SHOW_SETUP_ICON = "show-setup.png";
    private static final String CLOSE_SETUP_ICON = "close-setup.png";
    private static final String HIDE_SETUP_ICON = "hide-setup.png";
    private static final String SHOW_HELP_CONTENT_ICON = "show-doc.png";
    private static final String CLOSE_HELP_CONTENT_ICON = "close-doc.png";
    private static final String HIDE_HELP_CONTENT_ICON = "hide-doc.png";

    private static final String CANCEL_OPERATION_ICON = "stop-operation.png";

    public JTextPane pane;

    public JMenuBar menuBar;

    public JToolBar toolBar;

    private static final Map<String, AdfBasicAction> menuItemLabelToAction = new HashMap();
    AdfToggleButtonActionGroup adfToggleButtonActionGroup = new AdfToggleButtonActionGroup();
//    ButtonGroup group = new ButtonGroup();

    public AbstractButton buildMenuItem(String menuItemLabel, String iconName, String selectedIconName) {
        ImageIcon ii =  new ImageIcon(ICON_CLASS_PATH_PREFIX+iconName);
        ImageIcon selectedMenuIcon =  new ImageIcon(ICON_CLASS_PATH_PREFIX+selectedIconName);
        AdfToggleButtonMenuItem adfToggleButtonMenuItem = new AdfToggleButtonMenuItem(menuItemLabel,ii, false);
//        JRadioButtonMenuItem adfToggleButtonMenuItem = new JRadioButtonMenuItem(menuItemLabel,ii, false);
        adfToggleButtonMenuItem.setSelectedIcon(selectedMenuIcon);
        adfToggleButtonMenuItem.setIconTextGap(5);
//        adfToggleButtonMenuItem.setSelected(true);

        AdfBasicAction adfBasicAction =  new AdfToggleButtonAction(menuItemLabel, ii, null);

        adfToggleButtonMenuItem.setAction(adfBasicAction);
        adfToggleButtonMenuItem.setActionCommand(menuItemLabel);
        adfToggleButtonMenuItem.setBorder(new EmptyBorder(3,0,3,0));
//        adfToggleButtonMenuItem.setBorder(null);
//        adfToggleButtonMenuItem.setHorizontalTextPosition(JMenuItem.RIGHT);
//        adfToggleButtonMenuItem.setHorizontalAlignment(AdfToggleButtonMenuItem.LEFT);
        menuItemLabelToAction.put(menuItemLabel, adfBasicAction);
        adfToggleButtonActionGroup.registerAction(adfBasicAction);
        adfBasicAction.setSelected(true);
        adfBasicAction.setEnabled(false);

        return adfToggleButtonMenuItem;
    }

    public ToggleButtonMenuItemTest() {
        menuBar = new JMenuBar();
        JMenu justifyMenu = new JMenu("Justify");
//        ActionListener actionPrinter = new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    Object source = e.getSource();
//                    AdfToggleButtonMenuItem adfToggleButtonMenuItem = (AdfToggleButtonMenuItem)source;
//                    AdfToolbarToggleButtonAction adfToolbarToggleButtonAction =  (AdfToolbarToggleButtonAction) adfToggleButtonMenuItem.getAction();
//                    adfToolbarToggleButtonAction.setSelected(true);
//
//
////                    pane.getStyledDocument().insertString(0,"Action [" + e.getActionCommand()+ "] performed!\n", null);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        };
        AbstractButton leftJustify = buildMenuItem("ABC",CREATING_ARCS_ICON, STOP_CREATING_ARCS_ICON);
//        leftJustify.setHorizontalTextPosition(JMenuItem.RIGHT);
//        leftJustify.setHorizontalAlignment(AdfToggleButtonMenuItem.RIGHT);
//        leftJustify.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//        leftJustify.addActionListener(actionPrinter);
        AbstractButton rightJustify = buildMenuItem("NBV",CREATING_FRAGMENT_ICON, STOP_CREATING_FRAGMENTS_ICON);
//        rightJustify.setHorizontalTextPosition(JMenuItem.RIGHT);
//        rightJustify.setHorizontalAlignment(AdfToggleButtonMenuItem.LEFT);
//        rightJustify.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//        rightJustify.addActionListener(actionPrinter);
        AbstractButton centerJustify = buildMenuItem("aaaaa",MOVE_FRAGMENT_ICON, STOP_MOVING_FRAGMENTS_ICON);
//        centerJustify.setHorizontalTextPosition(JMenuItem.RIGHT);
//        centerJustify.setHorizontalAlignment(AdfToggleButtonMenuItem.LEFT);
//        centerJustify.setAccelerator(KeyStroke.getKeyStroke('M', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//        centerJustify.addActionListener(actionPrinter);
        AbstractButton fullJustify = buildMenuItem("ffff",MOVE_MODEL_ICON, STOP_MOVING_MODEL_ICON);
//        fullJustify.setHorizontalTextPosition(JMenuItem.RIGHT);
//        fullJustify.setHorizontalAlignment(AdfToggleButtonMenuItem.LEFT);
//        fullJustify.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//        fullJustify.addActionListener(actionPrinter);


//        group.add(leftJustify);
//        group.add(rightJustify);
//        group.add(centerJustify);
//        group.add(fullJustify);

        justifyMenu.add(leftJustify);
        justifyMenu.add(rightJustify);
        justifyMenu.add(centerJustify);
        justifyMenu.add(fullJustify);

//        JToggleButton

        menuBar.add(justifyMenu);
        menuBar.setBorder(new BevelBorder(BevelBorder.LOWERED));

        AdfBasicAction adfBasicAction =  menuItemLabelToAction.get("ABC");


    }

    public static void main(String s[]) {

        ToggleButtonMenuItemTest example = new ToggleButtonMenuItemTest();
        example.pane = new JTextPane();
        example.pane.setPreferredSize(new Dimension(250, 250));
        example.pane.setBorder(new BevelBorder(BevelBorder.LOWERED));

        JFrame frame = new JFrame("Menu Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(example.menuBar);
        frame.getContentPane().add(example.pane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
