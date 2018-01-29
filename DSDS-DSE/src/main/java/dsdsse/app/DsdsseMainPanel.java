package dsdsse.app;

//import adf.menu.AdfMenuBar;

import javax.swing.*;
import java.awt.*;

import dsdsse.designspace.DesignSpaceView;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 30, 2013
 * Time: 9:45:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class DsdsseMainPanel extends JPanel {

    public static String PREFIX = "/dsdsse-resources/images/app-icons/";
    public static String HELP_PREFIX = "/dsdsse-resources/images/splash/";

//    public static final String FILES_MNUSTR = "  Files  ";
//    public static final String NEW_PROJ_MNUSTR = "New Proj";
//    public static final String REGISTER_MNUSTR = "Register";
//    public static final String OPEN_MNUSTR = "Open";
//    public static final String SAVE_MNUSTR = "Save";
//    public static final String SAVE_AS_MNUSTR = "Save As";
//    public static final String MENU_ITEM_CLEAR_MODEL = "Erase Proj";
//    public static final String PRINT_MNUSTR = "Print";
//    public static final String EXIT_MNUSTR = "Exit";
//
//    public static final String MENU_ITEM_EXAMPLES = "  Models  ";
//    public static final String MENU_ITEM_NEW_MODEL = "New Model";
//    public static final String SIMP_BLOCKS_MNUSTR = "Simple Blocks";
//    public static final String THREE_RULS_MNUSTR = "Three Rules";
//    public static final String TRIGGERS_MNUSTR = "Trigger";
//    public static final String PROCESSES_MNUSTR = "Processes";
//    public static final String DIN_PHIL_MNUSTR = "Dining Philosophers";
//    public static final String MUT_EX_MNUSTR = "Mutual Exclusion";
//
//    public static final String MNUSTR_CREATE = "  Create  ";
//    public static final String MNUSTR_EDIT_MODE = "Set \"Create\" Mode";
//    public static final String REPAINT_MNUSTR = "Repaint";
//    public static final String CELLS_MNUSTR = "Create Balls";
//    public static final String FRAGMENTS_MNUSTR = "Create Fragments";
//    public static final String ARCS_MNUSTR = "Create Arcs";
//    public static final String LAUNCH_IA_MNUSTR =
//            "Launch Initialization Assistent";
//
//    public static final String JUNCTIONS_MNUSTR = "Create Situations";
//    public static final String V_JUNCTIONS_MNUSTR = "Vertical Orientation";
//    public static final String H_JUNCTIONS_MNUSTR = "Horizontal Orientation";
//    public static final String RS_JUNCTIONS_MNUSTR = "Right Slanted Orientation";
//    public static final String LS_JUNCTIONS_MNUSTR = "Left Slanted Orientation";
//
//    public static final String MNUSTR_GRID = "  Grid  ";
//    public static final String MNUSTR_GRID_ON_OFF = "+Grid On/Off";
//    public static final String CMDSTR_GRID_ON_OFF = "Grid On/Off";
//    public static final String MNUSTR_GRID_VISIBLE = "+Grid Visible";
//    public static final String CMDSTR_GRID_VISIBLE = "Grid Visible";
//
////  public static final String SET_STATE_MNUSTR    = "  Set State  ";
//
//
//    public static final String MOVE_MENU = "  Move  ";
//    public static final String MOVE_ELEMENT_MNUSTR = "Move Element";
//    public static final String MENU_ITEM_MOVE_MODEL = "Move Model";
//
//    public static final String DELETE_MNUSTR = "  Delete  ";
//    public static final String DEL_ELEMENT_MNUSTR = "Delete Element";
//
//    public static final String MNUSTR_EXEC = "  Exec  ";
//    public static final String MNUSTR_EXEC_MODE = "Set \"Exec\" Mode";
//    public static final String MNUSTR_START_EXEC = "Start Execution";
//    public static final String MNUSTR_STOP_EXEC = "Stop Execution";
//    public static final String MNUSTR_RESET_EXEC = "Reset Execution";
//    public static final String MNUSTR_ONE_STEP = "One Step";
//
//    public static final String MNUSTR_VIEW = "  View  ";
//    public static final String MNUSTR_ARROW_JUN = "-Draw Junction As Arrow";
//    public static final String CMDSTR_ARROW_JUN = "Draw Junction As Arrow";
//    public static final String MNUSTR_ARROW_ARC = "+Draw Arc Knob As Arrow";
//    public static final String CMDSTR_ARROW_ARC = "Draw Arc Knob As Arrow";
//    public static final String MNUSTR_ARC_TYPE = "-Switch Arc Arrow Type";
//    public static final String CMDSTR_ARC_TYPE = "Switch Arc Arrow Type";
//
//    public static final String MENU_ITEM_HELP = ">Help  ";
//    public static final String MNUSTR_DOC_HELP = "Doc_Help";
//    public static final String MENU_ITEM_WHAT_IS_DSDSSE = "How to Run DSDSSE";
//    public static final String WHAT_IS_TDP_MNUSTR = "What is the Time-Driven ISP";
//    public static final String WHAT_IS_RDP_MNUSTR = "What is the Rule-Driven ISP";
//    public static final String ABOUT_MNUSTR = "About";

//    private AdfMenuBar menuBar;
    private DesignSpaceView designSpaceView;
    //    private JToolBar toolBar;
    private DsdsseGlassPane dsdsseGlassPane;

    static DsdsseMainPanel dsdsseMainPanel;
    public static DsdsseMainPanel getInstance(){
        return dsdsseMainPanel;
    }

    public DsdsseMainPanel() {
        super(new BorderLayout());
        setFocusable(true);
        dsdsseMainPanel = this;
//        setBackground(Color.WHITE);
//
//        DsdsseEnvironment.putMainPanel(this);
//
//        add(initToolBar(), BorderLayout.NORTH);
//
//        JPanel panel = new OneLineMessagePanel();
//        panel.setPreferredSize(new Dimension(0, 20));
//        add(panel, BorderLayout.SOUTH);

    }

    /**
     *
     */
//    public JMenuBar initMenu() {
//
////        String junctionMenu[] = {
////                {AppController.MENU_ITEM_CREATE_CONDITIONS,
////                {AppController.MENU_ITEM_CREATE_CONDITIONS};
////
////        String mclnMenu[] = {
////                "MCLNet",
////                {AppController.MNUSTR_ARROW_JUN,
////                {AppController.MNUSTR_ARROW_ARC,
////                {AppController.MNUSTR_ARC_TYPE};
//
//        Object menuItems[][][] = {
//                {
//                        {AppController.MENU_ITEM_PROJECT},
//                        {"@" + AppController.MENU_ITEM_NEW_PROJECT, "new-proj.png"},
//                        {AppController.MENU_ITEM_RENAME_PROJECT},
////                {AppController.MENU_ITEM_ADD_MODEL},
//                        {AppController.MENU_ITEM_RENAME_MODEL},
//                        {AppController.MENU_ITEM_CLEAR_MODEL},
//                        {"-"},
//                        {"@" + AppController.MENU_ITEM_OPEN_PROJECT, "open.png"},
//                        {AppController.MENU_ITEM_SAVE, "save.gif"},
//                        {"@" + AppController.MENU_ITEM_SAVE_AS, "save.png"},
//                        {"-"},
//                        {"@" + AppController.MENU_ITEM_SHOW_PRINT_CONTENT, "print.png", AppController.MENU_ITEM_SET_SIMULATION_MODE},
//                        {"-"},
//                        {AppController.EXIT_MNUSTR},
//                }, {
//                {AppController.MENU_ITEM_EXAMPLES},
//                {AppController.MENU_BASIC_BLOCK},
//                {AppController.MENU_LOGICAL_BLOCKS},
//                {AppController.MENU_TWO_RULES},
//                {AppController.MENU_THREE_RULES},
//                {AppController.MENU_TRIGGER},
//                {AppController.MENU_SINGLE_PROPERTY},
//                {AppController.MENU_MUT_EXCL},
//                {AppController.MENU_DIN_PHIL},
//        }, {
//                {AppController.MENU_ITEM_CREATION_MODE},
//                {"-"},
//                {"@" + AppController.MENU_ITEM_SET_CREATION_MODE_ENABLED, "edit_mode.gif"},
//                {"-"},
//                {"@" + AppController.MENU_ITEM_CREATE_PROPERTIES, "create_node.gif"},
//                {"@" + AppController.MENU_ITEM_CREATE_CONDITIONS, "create_condition.gif"},
//                {"@" + AppController.MENU_ITEM_CREATE_ARCS, "create_arc.gif"},
//                {"@" + AppController.MENU_ITEM_CREATE_FRAGMENTS, "create_fragment.gif"},
//                {"-"},
//                {"@" + AppController.MENU_ITEM_LAUNCH_IA, "init_model.png"},
//        }, {
//                {AppController.MENU_ITEM_MODIFICATION},
//                {"@" + AppController.MENU_ITEM_DELETE_ELEMENT, "delete.png"},
//                {"-"},
//                {AppController.MENU_ITEM_MOVE_FRAGMENT},
//                {AppController.MENU_ITEM_MOVE_MODEL},
//        }, {
//                {AppController.MENU_ITEM_SIMULATION_MODE},
//                {"@" + AppController.MENU_ITEM_SET_SIMULATION_MODE, "exec_mode.gif"},
//        }, {
//                {AppController.MENU_ITEM_PREFERENCES},
//                {"@" + AppController.MENU_ITEM_SHOW_SETUP_PANEL, "setup.png"},
//                {AppController.MENU_ITEM_HIDE_SETUP_PANEL},
//        }, {
//                {AppController.MENU_ITEM_HELP},
//                {"@" + AppController.MENU_ITEM_SHOW_HELP_PANEL, "doc.png"},
//                {"-"},
//                {AppController.MENU_ITEM_WHAT_IS_DSDSSE},
//                {AppController.MENU_ITEM_RUNNING_EXAMPLES},
//                {AppController.MENU_ITEM_WHAT_IS_CREATOR},
//                {AppController.MENU_ITEM_WHAT_IS_SIM_ENG},
//                {AppController.MENU_ITEM_MENU_COMMANDS},
//                {"-"},
//                {AppController.MENU_ITEM_WHAT_IS_TD_IG},
//                {AppController.MENU_ITEM_WHAT_IS_SD_IG},
//                {"-"},
//                {AppController.MENU_ITEM_ABOUT}
//        },
//        };
//
//        String ICON_CLASS_PATH = "/app_icons/dsdsse/";
//        ActionListener menuActionListener = AppController.getInstance().getMenuListener();
//        menuBar = new AppMenuBar(ICON_CLASS_PATH, menuItems, menuActionListener, null);
//
//        return menuBar;
//    }

//    private JToolBar initToolBar() {
//        toolBar = new JToolBar();
//
//        toolBar.add(makeButton("new_proj.gif", NEW_PROJ_MNUSTR,
//                " Begin new project. "));
//
//        toolBar.add(makeButton("new_model.gif", MENU_ITEM_NEW_MODEL,
//                " Begin new model. "));
//
//        toolBar.add(makeButton("open.gif", OPEN_MNUSTR,
//                " Open existing project. "));
//
//        toolBar.add(makeButton("save.gif", SAVE_MNUSTR,
//                " Save current project. "));
//
//        toolBar.add(makeButton("print.gif", PRINT_MNUSTR,
//                " Print project. "));
//
//        toolBar.add(makeButton("refresh.gif", REPAINT_MNUSTR,
//                " Refresh screen (Repaint model). "));
//
//        // ---------------------------------------------------
//        toolBar.addSeparator();
//        // ---------------------------------------------------
//        //    E D I T   M O D E   B U T T O N S
//        // ---------------------------------------------------
//
//        JButton tbbSetEditMode = makeButton("edit_mode.gif", MNUSTR_EDIT_MODE,
//                " Set Edit Mode. ");
//        toolBar.add(tbbSetEditMode);
//
//        // toolBar.addSeparator();
//        JButton tbbCreateCell = makeButton("create_node.gif", CELLS_MNUSTR,
//                " Create cells. ");
//        toolBar.add(tbbCreateCell);
//
//        JButton tbbCreateFragnent = makeButton("create_fragment.gif",
//                FRAGMENTS_MNUSTR,
//                " Create fragments. ");
//        toolBar.add(tbbCreateFragnent);
//
//        JButton tbbCreateJunctionV = makeButton("create_v_tran.gif",
//                V_JUNCTIONS_MNUSTR,
//                " Create vertical Junctions. ");
//        toolBar.add(tbbCreateJunctionV);
//
//        JButton tbbCreateJunctionH = makeButton("create_h_tran.gif",
//                H_JUNCTIONS_MNUSTR,
//                " Create horizontal Junctions. ");
//        toolBar.add(tbbCreateJunctionH);
//
//        JButton tbbCreateJunctionR = makeButton("create_r_tran.gif",
//                RS_JUNCTIONS_MNUSTR,
//                " Create right slanted Junctions. ");
//        toolBar.add(tbbCreateJunctionR);
//
//        JButton tbbCreateJunctionL = makeButton("create_l_tran.gif",
//                LS_JUNCTIONS_MNUSTR,
//                " Create left slanted Junctions. ");
//        toolBar.add(tbbCreateJunctionL);
//
//        JButton tbbCreateArc = makeButton("create_arc.gif", ARCS_MNUSTR,
//                " Create arcs. ");
//        toolBar.add(tbbCreateArc);
//
//        toolBar.addSeparator();
//        // ---------------------------------------------------
//        //    I N I T    M O D E    B U T T O N S
//        // ---------------------------------------------------
//        JButton tbbInitAssistant = makeButton("init_model.gif",
//                LAUNCH_IA_MNUSTR,
//                " Launch Initialization Assistant. ");
//        toolBar.add(tbbInitAssistant);
//
//        toolBar.addSeparator();
//        toolBar.addSeparator();
//        // ---------------------------------------------------
//        //    E X E C U T I N     M O D E    B U T T O N S
//        // ---------------------------------------------------
//
//        JButton tbbSetRunMode = makeButton("exec_mode.gif", MNUSTR_EXEC_MODE,
//                " Set Execution mode. ");
//        toolBar.add(tbbSetRunMode);
//
//
//        ImageIcon startImageIcon = AppHelper.getImageIcon(PREFIX + "start_running.gif");
//// JButton   tbbStartRunning = AppEnv.makeButton( null, startImageIcon,
////                                    null, MNUSTR_START_EXEC,
////                              " Start or resume running the model(s). ",
////                               null, margin,  -1, -1, this );
////    toolBar.add(tbbStartRunning);
//
////    tbbStartRunning = makeButton( "start_running.gif",
////       MNUSTR_START_EXEC, " Start or resume running the model(s). " );
////    toolBar.add(tbbStartRunning);
//
////    tbbOneStep = AppEnv.makeButton( null,
////                                    AppEnv.getImageIcon( "step_running.gif" ),
////                                    null, MNUSTR_ONE_STEP,
////                              "Execute one step at a time", null, margin,
////                              -1, -1, this );
////    toolBar.add(tbbOneStep);
//
//        JButton tbbResetModel = makeButton("reset_running.gif",
//                MNUSTR_RESET_EXEC, " Reset the model(s) initial state. ");
//        toolBar.add(tbbResetModel);
//
//        ImageIcon stopImageIcon = AppHelper.getImageIcon(PREFIX + "stop_running.gif");
////    tbbStopRunning = makeButton( "stop_running.gif",
////          MNUSTR_STOP_EXEC, " Suspend running the model(s). " );
////    toolBar.add(tbbStopRunning);
//
//        // toolBar.addSeparator();
//        /*
//        Font font;
//        tbbOneStep = new JButton( "One Step" );
//        font = tbbOneStep.getFont();
//        tbbOneStep.setFont( new Font( font.getSubject(), font.BOLD, 11 ) );
//        // tbbOneStep = new JButton( dbg );
//        tbbOneStep.setToolTipText(" Run one step of execution. ");
//        tbbOneStep.addActionListener(new ActionListener()
//        {public void executeMenuOperation(ActionEvent e){onOneStep();}});
//        toolBar.add(tbbOneStep);
//        */
//        /*
//        toolBar.addSeparator();
//        tbbTwoSteps = new JButton( "Two Steps" );
//        tbbTwoSteps.setFont( new Font( font.getSubject(), font.BOLD, 11 ) );
//        tbbTwoSteps.setToolTipText(" Run two steps of execution. ");
//        tbbTwoSteps.addActionListener(new ActionListener()
//        {public void executeMenuOperation(ActionEvent e){onTwoSteps();}});
//        toolBar.add(tbbTwoSteps);
//        */
//        /*
//        toolBar.addSeparator();
//        tbbResetModel = new JButton( "Reset Model" );
//        tbbResetModel.setFont( new Font( font.getSubject(), font.BOLD, 11 ) );
//        tbbResetModel.setToolTipText(" Reset the model(s) initial state. ");
//        tbbResetModel.addActionListener(new ActionListener()
//        {public void executeMenuOperation(ActionEvent e){onResetModel();}});
//        toolBar.add(tbbResetModel);
//        */
//        // ---------------------------------------------------
//        toolBar.addSeparator();
//        toolBar.addSeparator();
//        // ---------------------------------------------------
//        //    H E L P  &  D O C   B U T T O N S
//        // ---------------------------------------------------
//
//        toolBar.add(makeButton("doc.gif", MNUSTR_DOC_HELP,
//                " Documentation. "));
//
//        toolBar.add(makeButton("help.gif", MENU_ITEM_WHAT_IS_DSDSSE,
//                " Help. "));
//        return (toolBar);
//    }

//    /**
//     * @param iconName
//     * @param command
//     * @param tipText
//     */
//    private JButton makeButton(String iconName, String command, String tipText) {
//        ImageIcon imageIcon = AppHelper.getImageIcon(PREFIX + iconName);
//        JButton button = new JButton(imageIcon);
//        button.setActionCommand(command);
//        button.setToolTipText(tipText);
//        button.addActionListener(null);
//        button.setMargin(new Insets(0, 0, 0, 0));
//        button.setHorizontalAlignment(SwingConstants.CENTER);
//        button.setVerticalAlignment(SwingConstants.TOP);
//        return button;
//    }

//    public void initGlassPane(DesignSpaceView designSpaceView) {
//        this.designSpaceView = designSpaceView;
//        JFrame frame = DsdsseMainFrame.getInstance();
//        dsdsseGlassPane = new DsdsseGlassPane(designSpaceView);
////        changeButton.addItemListener(myGlassPane);
//        frame.setGlassPane(dsdsseGlassPane);
//        dsdsseGlassPane.setVisible(true);
//    }
}
