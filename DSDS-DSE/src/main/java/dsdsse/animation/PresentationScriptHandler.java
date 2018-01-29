package dsdsse.animation;

import adf.onelinemessage.AdfOneLineMessageManager;
import dsdsse.app.*;
import dsdsse.graphview.MclnArcView;
import dsdsse.graphview.MclnGraphDesignerView;
import dsdsse.graphview.MclnGraphViewNode;
import dsdsse.messagepopuppanel.MessageClosedListener;
import dsdsse.messagepopuppanel.MessagePopUpPanelHolder;
import dsdsse.messagepopuppanel.MessagePopupManager;
import vw.valgebra.VAlgebra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by Admin on 6/15/2017.
 */
public abstract class PresentationScriptHandler {

    private static final Logger logger = Logger.getLogger(PresentationScriptHandler.class.getName());

    private static final int TICK_PHASE = 1000;
    private static final int TICK_INTERVAL = 1000;
    protected static final int MOVING_TO_MENU_DELAY = 50;
    protected static final int ARC_CREATION_DELAY = 60;

    protected static String MESSAGE_EXPOSURE_QUICK = "3000";
    protected static String MESSAGE_EXPOSURE_SHORT = "5000";
    protected static String MESSAGE_EXPOSURE_MEDIUM = "8000";
    protected static String MESSAGE_EXPOSURE_LONG = "10000";

    protected static final String SCRIPT_NAME_MESSAGE_BACKGROUND = "0x880088";
    protected static final String AGENDA_MESSAGE_BACKGROUND = "0x00AA00";

    public static final String SKIP_STEP_FOR_INDIVIDUAL_SHOW = "SKIP STEP FOR_INDIVIDUAL SHOW";
    public static final String SKIP_MESSAGE_IF_ALREADY_PRESENTED = "SKIP MESSAGE IF ALREADY PRESENTED";

    // Operations
    protected static final String SET_DEVELOPMENT_MODE = "Set development mode";
    protected static final String PAUSE = "Pause";
    protected static final String INFO_MESSAGE = "Info message";
    protected static final String WARNING_MESSAGE = "Warning message";
    protected static final String SET_FULL_SCREEN_MODE = "Set full screen mode";
    protected static final String SET_CURSOR_TO_HOME_LOCATION = "Set mouse to initial location";
    protected static final String OPERATION_RESET_APP_SCREEN_SIZE = "Reset App screen size";

    //   ===============================================================================================================
    //   C o m m o n   M e s s a g e s
    //   ===============================================================================================================

    protected static final int fontSize = 15;
    private static final String CAUTION_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + (fontSize + 1) + "px; font-weight: bold; color:#c27101; text-align:center; ").
            append("width:600px; \">").
            append("<font color=\"#FF0000\" size=\"6\">C a u t i o n  !</font><br> ").
            append("<font color=\"#BB0000\" size=\"5\">The mouse and the keyboard are used by the show. <br>").
            append("By attempting to use them during the run you may break the presentation.</font><br><br> ").
            append("<font color=\"#FF0000\" size=\"5\">Press ESC to Cancel the Presentation.</font> <br>").
            append("<font color=\"#FF0000\" size=\"5\">Presentation is interrupted after currently executed step is completed.</font> ").
            append("</div></html>").toString();

    protected static final String SET_DEVELOPMENT_MODE_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("<font color=\"#FFFFFF\"  >Switching Environment from</font> ").
            append("<font color=\"#FFDD00\"  >Simulation</font>").
            append("<font color=\"#FFFFFF\"  > to </font> ").
            append("<font color=\"#FFDD00\"  >Development</font>").
            append("<font color=\"#FFFFFF\"  > mode </font> ").
            append("</div></html>").toString();

    private static final String END_OF_SHOW_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + (fontSize + 3) + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:600px; \">").
            append("<font  size=\"6\">End of Show</font> ").
            append("</div></html>").toString();


    //   ==============================================================================================================
    //    Operations that are supposed be executed before presentation script
    //   ==============================================================================================================

    private static final String[][] preScriptData = {

            // Switch to full screen
            {PAUSE, "1", ""},
            {SET_FULL_SCREEN_MODE, "", ""},
            {PAUSE, "1", ""},

            // Caution message
            {WARNING_MESSAGE, CAUTION_MESSAGE, "0xFFEF8C", "0xc27101", MESSAGE_EXPOSURE_MEDIUM,
                    SKIP_MESSAGE_IF_ALREADY_PRESENTED},
            {SET_CURSOR_TO_HOME_LOCATION, "", ""},
    };

    //   ==============================================================================================================
    //    Operations that are supposed be executed after presentation script
    //   ==============================================================================================================

    private static final String[][] postScriptData = {

            // Showing Thank You message as show is doing to finish
            {SET_CURSOR_TO_HOME_LOCATION, "", "", "50"},
            {WARNING_MESSAGE, END_OF_SHOW_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_QUICK},
            {PAUSE, "1", ""},
    };


    public static interface AnimatingTickExecutor {
        int executeAnimatingTick(ScriptItem scriptItem, int tickToExecute);
    }

    protected static int stringToInt(String strInt) {
        int intValue = 0;
        try {
            intValue = Integer.decode(strInt);
        } finally {
            return intValue;
        }
    }

    private static double[] strCSysLocationToDouble(String strXYModelPoint) {
        String[] strCSysXYLocation = strXYModelPoint.split(":");
        double[] doubleCSysXYLocation = {0D, 0D, 0D};
        try {
            doubleCSysXYLocation =
                    new double[]{Double.parseDouble(strCSysXYLocation[0]), Double.parseDouble(strCSysXYLocation[1]), 0D};
        } finally {
            return doubleCSysXYLocation;
        }
    }

    //
    //   I n s t a n c e
    //

    private static volatile boolean cautionMessageAlreadyPresented;

    protected DsdsseMainFrame dsdsseMainFrame = DsdsseMainFrame.getInstance();
    protected MclnGraphDesignerView mclnGraphDesignerView;
    protected PresentationRunner.ScriptStepCompletionFeedback scriptStepCompletionFeedback;

    protected volatile DseRobot robot;
    protected volatile boolean executeTimerTask;
    protected volatile AnimatingTickExecutor currentTickExecutor;
    protected volatile ScriptItem currentScriptItem;
    protected volatile int currentAnimationTickToExecute;
    protected volatile int x;
    protected volatile int y;
    protected volatile JMenuItem subMenuItem;
    protected volatile double[] lastScreenPoint = {0, 0, 0};
    private volatile int pauseTicksToCount;
    private volatile boolean showingCautionMessage;


    private MessageClosedListener messageClosedListener = new MessageClosedListener() {
        @Override
        public void messageClosed() {
            if (showingCautionMessage) {
                showingCautionMessage = false;
                AdfOneLineMessageManager.showWarningMessage(30, PresentationRunner.PRESENTATION_STARTED_MESSAGE, 18);
            }
            scriptStepCompletionFeedback.done();
        }
    };

    protected final List<ScriptItem> demoScript = new ArrayList();
    protected volatile int scriptScanIndex;
    protected volatile boolean executionCanceled;

    private volatile java.util.Timer animationTickTimer = new java.util.Timer("Self-Runnable Animation Tick Timer", true);
    private volatile TimerTask timerTask = new TimerTask() {
        public void run() {
//            logger.severe("tick");
            if (currentAnimationTickToExecute != 0 && executeTimerTask && currentTickExecutor != null) {
                currentAnimationTickToExecute =
                        currentTickExecutor.executeAnimatingTick(currentScriptItem, currentAnimationTickToExecute);
                if (currentAnimationTickToExecute == 0) {
                    scriptStepCompletionFeedback.done();
                }
            }
        }
    };

    //===========================================================

    /**
     * C o n s t r u c t i n g
     */
    PresentationScriptHandler(MclnGraphDesignerView mclnGraphDesignerView) {
        this.mclnGraphDesignerView = mclnGraphDesignerView;
        try {
            robot = new DseRobot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        animationTickTimer.schedule(timerTask, TICK_PHASE, TICK_INTERVAL);
    }

    abstract String getScriptName();

    /**
     *
     */
    void cancelCurrentStepExecution() {
        executionCanceled = true;
        robot.setIgnoreDelay();
    }

    boolean isStarter() {
        return false;
    }

    boolean isIntroductory() {
        return false;
    }

    /**
     * @return
     */
    static List<ScriptItem> getPreScriptItems() {
        List<ScriptItem> preScriptItems = new ArrayList();
        for (int i = 0; i < preScriptData.length; i++) {
            String[] parameters = preScriptData[i];
            String operation = parameters[0];
            String stepAction = parameters[1];
            String actionAttribute1 = parameters[2];
            String actionAttribute2 = parameters.length <= 3 ? "" : parameters[3];
            String actionAttribute3 = parameters.length <= 4 ? "" : parameters[4];
            String actionAttribute4 = parameters.length <= 5 ? "" : parameters[5];
            ScriptItem scriptItem = new ScriptItem(operation, stepAction, actionAttribute1,
                    actionAttribute2, actionAttribute3, actionAttribute4);
            preScriptItems.add(scriptItem);
        }
        return preScriptItems;
    }

    /**
     * @return
     */
    static List<ScriptItem> getPostScriptItems() {
        List<ScriptItem> postScriptItems = new ArrayList();
        for (int i = 0; i < postScriptData.length; i++) {
            String[] parameters = postScriptData[i];
            String operation = parameters[0];
            String stepAction = parameters[1];
            String actionAttribute1 = parameters[2];
            String actionAttribute2 = parameters.length <= 3 ? "" : parameters[3];
            String actionAttribute3 = parameters.length <= 4 ? "" : parameters[4];
            String actionAttribute4 = parameters.length <= 5 ? "" : parameters[5];
            ScriptItem scriptItem = new ScriptItem(operation, stepAction, actionAttribute1,
                    actionAttribute2, actionAttribute3, actionAttribute4);
            postScriptItems.add(scriptItem);
        }
        return postScriptItems;
    }

    public void setScriptStepCompletionFeedback(PresentationRunner.ScriptStepCompletionFeedback scriptStepCompletionFeedback) {
        this.scriptStepCompletionFeedback = scriptStepCompletionFeedback;
    }

    /**
     * @return
     */
    ScriptItem getNextItem() {
        if (scriptScanIndex == demoScript.size()) {
            return null;
        }
        ScriptItem scriptItem = demoScript.get(scriptScanIndex++);
        return scriptItem;
    }

    abstract void executeScriptStep(ScriptItem scriptItem);

    //   ===============================================================================================================
    //   O p e r a t i o n   e x e c u t o r s
    //   ===============================================================================================================

    /**
     * @param scriptItem
     */
    protected void presentMessage(ScriptItem scriptItem) {
        if (cautionMessageAlreadyPresented &&
                PresentationScriptHandler.SKIP_MESSAGE_IF_ALREADY_PRESENTED.equalsIgnoreCase(scriptItem.getMessageType())) {
            AdfOneLineMessageManager.showWarningMessage(30, PresentationRunner.PRESENTATION_STARTED_MESSAGE, 18);
            scriptStepCompletionFeedback.done();
            return;
        }
        if (scriptItem.getStepAction().equals(SET_DEVELOPMENT_MODE_MESSAGE)) {
            if (AppController.getInstance().isInDevelopmentMode()) {
                scriptStepCompletionFeedback.done();
                return;
            }
        }
        int width = -1;
        if (PresentationScriptHandler.SKIP_MESSAGE_IF_ALREADY_PRESENTED.equalsIgnoreCase(scriptItem.getMessageType()) &&
                !cautionMessageAlreadyPresented) {
            showingCautionMessage = true;
        }
        cautionMessageAlreadyPresented = true;
        String messageText = scriptItem.getMessageText();
        int messageBackground = stringToInt(scriptItem.getMessageBackground());
        int messageForeground = stringToInt(scriptItem.getMessageForeground());
        int waitingTime = stringToInt(scriptItem.getMessageTime());
        if (scriptItem.getOperation().equals(INFO_MESSAGE)) {
            MessagePopupManager.showMessagePopup(messageText, MessagePopUpPanelHolder.INFO_MESSAGE,
                    MessagePopUpPanelHolder.MESSAGE_LOCATION_NORTH, width,
                    messageBackground, messageForeground, waitingTime, messageClosedListener);
        } else if (scriptItem.getOperation().equals(WARNING_MESSAGE)) {
            MessagePopupManager.showMessagePopup(messageText, MessagePopUpPanelHolder.WARNING_MESSAGE,
                    MessagePopUpPanelHolder.MESSAGE_LOCATION_CENTER, width,
                    messageBackground, messageForeground, waitingTime, messageClosedListener);
        }
    }

    private FrameAnimationListener frameAnimationListener = new FrameAnimationListener() {
        @Override
        public void frameGrowthComplete(boolean growthUp) {

            dsdsseMainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            if (!cautionMessageAlreadyPresented) {
                // positioning mouse
                Rectangle bounds = dsdsseMainFrame.getBounds();
                int x = bounds.x + bounds.width / 2;
                int y = bounds.y + bounds.height / 2;
                robot.mouseMove(x, y + 100);
                lastScreenPoint = new double[]{x, y, 0};
            }

            DsdsseMainFrame.getInstance().setAlwaysOnTop(false);
            scriptStepCompletionFeedback.done();
        }
    };

    /**
     * @param scriptItem
     */
    void executeOperationSetFullScreenMode(ScriptItem scriptItem) {
        // setting full screen
        DsdsseMainFrame.getInstance().setAlwaysOnTop(true);
        dsdsseMainFrame.setResizable(false);
        GrowFrameToMaxSize.growFrameToMaxSize(DsdsseMainFrame.getInstance(), true, frameAnimationListener);
    }

    //
    //   Returning application to normal initial size and locating it in the center of the screen
    //
    protected void setNormalInitialState(FrameAnimationListener frameAnimationListener) {
        AdfOneLineMessageManager.clearHighPriorityMessage();
        DsdsseMainFrame dsdsseMainFrame = DsdsseMainFrame.getInstance();
        dsdsseMainFrame.setResizable(true);
        GrowFrameToMaxSize.growFrameToMaxSize(dsdsseMainFrame, false, frameAnimationListener);
    }

    /**
     * @param scriptItem
     */
    void executeOperationSetCursorToHomeLocation(ScriptItem scriptItem) {
        // positioning mouse
        String yOffsetAsString = scriptItem.getActionAttribute2();
        int yOffset = stringToInt(yOffsetAsString);
        Rectangle bounds = dsdsseMainFrame.getBounds();
        int x = bounds.x + bounds.width / 2;
        int y = bounds.y + bounds.height / 2;
        y += yOffset;
        robot.mouseMove(x, y);
        lastScreenPoint = new double[]{x, y, 0};
        scriptStepCompletionFeedback.done();
    }

    //
    //  Select operation from menu executor
    //

    private static class MenuLocationFinder implements Runnable {

        static Object[] getSystemDataInEDT(String mainMenuItemName, String subMenuItemName) {
            MenuLocationFinder menuLocationFinder = new MenuLocationFinder(mainMenuItemName, subMenuItemName);
            boolean success = true;
            try {
                SwingUtilities.invokeAndWait(menuLocationFinder);
            } catch (Exception e) {
                success = false;
            }
            if (!success) {
                return null;
            }
            return menuLocationFinder.getResult();
        }

        private final String mainMenuItemName;
        private final String subMenuItemName;
        private Object[] result = new Object[2];

        public MenuLocationFinder(String mainMenuItemName, String subMenuItemName) {
            this.mainMenuItemName = mainMenuItemName;
            this.subMenuItemName = subMenuItemName;
        }

        @Override
        public void run() {
            JMenu menu = AppController.findMenuBarItemByName(mainMenuItemName);
            if (menu == null) {
                return;
            }
            JMenuItem subMenuItem = AppController.findSubMenuItemByName(mainMenuItemName, subMenuItemName);
            if (subMenuItem == null) {
                return;
            }
            Rectangle bounds = menu.getBounds();
            Point p = menu.getLocationOnScreen();
            int x = p.x + bounds.width / 2;
            int y = p.y + bounds.height / 2;
            double[] currentScreenPoint = new double[]{x, y, 0};
            result[0] = currentScreenPoint;
            result[1] = subMenuItem;
        }

        public Object[] getResult() {
            return result;
        }
    }

    private Object[] menuData;
    protected final AnimatingTickExecutor selectOperationFromMenuTickExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        String mainMenuItemName = scriptItem.getActionAttribute1();
        String subMenuItemName = scriptItem.getActionAttribute2();
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                menuData = MenuLocationFinder.getSystemDataInEDT(mainMenuItemName, subMenuItemName);
                if (menuData == null) {
                    Thread.dumpStack();
                    return 100;
                }
                double[] currentScreenPoint = (double[]) menuData[0];
                subMenuItem = (JMenuItem) menuData[1];
                moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);

                lastScreenPoint = currentScreenPoint;
                nextTickToExecute++;
                break;
            case 2:
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                nextTickToExecute++;
                break;
            case 3:
                Point subMenuLocationPoint = subMenuItem.getLocationOnScreen();
                Rectangle subMenuBounds = subMenuItem.getBounds();
                x = subMenuLocationPoint.x + subMenuBounds.width / 2;
                y = subMenuLocationPoint.y + subMenuBounds.height / 2;
                robot.mouseMove(x, y);
                currentScreenPoint = new double[]{x, y, 0};
                lastScreenPoint = currentScreenPoint;
                nextTickToExecute++;
                break;
            case 4:
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                int newYCoordinate = stringToInt(scriptItem.getNewYCoordinate());
                if (newYCoordinate != 0) {
                    currentScreenPoint = VAlgebra.copyVec3(lastScreenPoint);
                    currentScreenPoint[1] += newYCoordinate;
                    moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                    lastScreenPoint = currentScreenPoint;
                }
                nextTickToExecute = 100;
                break;
            case 100:
                /*
                    Second Arc created. Operation complete
                 */
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;

            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

    //
    //   Moves mouse to specified location and clicks on the point under mouse
    //
    protected final AnimatingTickExecutor moveToAndClickOnExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        String strXYModelPoint = scriptItem.getActionAttribute2();
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                double[] doubleCSysLocation = strCSysLocationToDouble(strXYModelPoint);
                double[] doubleScrVec = mclnGraphDesignerView.cSysPointToScreenPoint(doubleCSysLocation);
                int[] scrLocation = mclnGraphDesignerView.doubleVec3ToInt(doubleScrVec);
                Point viewLocationOnScreen = mclnGraphDesignerView.getLocationOnScreen();

                double[] currentScreenPoint =
                        new double[]{viewLocationOnScreen.x + scrLocation[0],
                                viewLocationOnScreen.y + scrLocation[1], 0};
                moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                lastScreenPoint = currentScreenPoint;

//                robot.mouseMove(viewLocationOnScreen.x + scrLocation[0], viewLocationOnScreen.y + scrLocation[1]);
                nextTickToExecute++;
                break;
            case 2:
                doMouseClick();
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
                AppStateModel.printState();
        }
        return nextTickToExecute;
    };

    //
    //   Find and Pick Up NcLN Graph Node
    //

    MclnGraphViewNode mclnGraphNode;
    int[] nodeIntScrLocation;
    AnimatingTickExecutor findAndPickUpMclnGraphNodeViewExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        String inputNodeID = scriptItem.getActionAttribute1();
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                mclnGraphNode = mclnGraphDesignerView.getMclnNodeByID(inputNodeID);
                if (mclnGraphNode != null) {

                    double[] nodeScrLocation = mclnGraphNode.getScrPnt();
                    int[] nodeIntViewLocation = mclnGraphDesignerView.doubleVec3ToInt(nodeScrLocation);
                    Point viewLocationOnScreen = mclnGraphDesignerView.getLocationOnScreen();
                    int[] viewLocationOnScreenVec = new int[]{viewLocationOnScreen.x, viewLocationOnScreen.y, 0};
                    nodeIntScrLocation = VAlgebra.addVec3(viewLocationOnScreenVec, nodeIntViewLocation);


                    double[] currentScreenPoint = new double[]{nodeIntScrLocation[0], nodeIntScrLocation[1], 0};
                    moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                    lastScreenPoint = currentScreenPoint;

                    nextTickToExecute++;
                }
                break;
            case 2:
                /*
                    Clicking on Property Node
                 */
                doMouseClick();
                nextTickToExecute = 100;
                break;
            case 100:
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

    MclnArcView mclnArcView;
    int[] arcArrowIntScrLocation;
    AnimatingTickExecutor findAndPickUpMclnGraphArcViewExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        String arcID = scriptItem.getActionAttribute1();
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                mclnArcView = mclnGraphDesignerView.getMclnArcByID(arcID);
                if (mclnArcView != null) {
                    double[] arcArrowScrLocation = mclnArcView.getScrOutlineCenter();
                    int[] arcArrowIntViewLocation = mclnGraphDesignerView.doubleVec3ToInt(arcArrowScrLocation);
                    Point viewLocationOnScreen = mclnGraphDesignerView.getLocationOnScreen();
                    int[] viewLocationOnScreenVec = new int[]{viewLocationOnScreen.x, viewLocationOnScreen.y, 0};
                    arcArrowIntScrLocation = VAlgebra.addVec3(viewLocationOnScreenVec, arcArrowIntViewLocation);


                    double[] currentScreenPoint = new double[]{arcArrowIntScrLocation[0], arcArrowIntScrLocation[1], 0};
                    moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                    lastScreenPoint = currentScreenPoint;

                    nextTickToExecute++;
                } else {
                    nextTickToExecute = 100;
                }
                break;
            case 2:
                /*
                    Clicking on Property Node
                 */
                doMouseClick();
                nextTickToExecute++;
                break;
            case 3:
                /*
                    Clicking one more time
                 */
                doMouseClick();
                nextTickToExecute = 100;
                break;
            case 100:
                /*
                    Arc created. Operation complete
                 */
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

    //
    //
    //
    AnimatingTickExecutor clickOnCurrentPointExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                /*
                    Clicking on mouse current point
                 */
                doMouseClick();
                nextTickToExecute = 100;
                break;
            case 100:
                /*
                    Second Arc created. Operation complete
                 */
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

    //
    //
    //
    AnimatingTickExecutor moveMouseToSpecifiedLocationExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        String strXYModelPoint = scriptItem.getActionAttribute1();
        String mode = scriptItem.getActionAttribute2();
        boolean drag = mode != null && mode.equalsIgnoreCase("DRAG");
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                double[] doubleCSysLocation = strCSysLocationToDouble(strXYModelPoint);
                double[] doubleScrVec = mclnGraphDesignerView.cSysPointToScreenPoint(doubleCSysLocation);
                int[] scrLocation = mclnGraphDesignerView.doubleVec3ToInt(doubleScrVec);
                Point viewLocationOnScreen = mclnGraphDesignerView.getLocationOnScreen();

                double[] currentScreenPoint =
                        new double[]{viewLocationOnScreen.x + scrLocation[0],
                                viewLocationOnScreen.y + scrLocation[1], 0};
                if (drag) {
                    dsdsseMainFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
//                    robot.delay(1000);
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.delay(1000);
                }
                moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                if (drag) {
                    robot.delay(1000);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
//                    robot.delay(1000);
                    dsdsseMainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    robot.delay(1000);
                }
                lastScreenPoint = currentScreenPoint;
                nextTickToExecute = 100;
                break;

            case 100:
                /*
                    Second Arc created. Operation complete
                 */
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

    //   P a u s e   O p e r a t i o n
    //
    protected void pause(ScriptItem scriptItem) {
        String strTicksToCount = scriptItem.getStepAction();
        pauseTicksToCount = stringToInt(strTicksToCount);
        currentAnimationTickToExecute = 1;
        currentTickExecutor = pauseTickExecutor;
        currentScriptItem = scriptItem;
        executeTimerTask = true;
    }

    private final AnimatingTickExecutor pauseTickExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        int nextTickToExecute = tickToExecute;
        if (executionCanceled) {
            executeTimerTask = false;
            nextTickToExecute = 0;
            return nextTickToExecute;
        }

        if (tickToExecute < pauseTicksToCount) {
            nextTickToExecute++;
        } else {
            executeTimerTask = false;
            nextTickToExecute = 0;
        }
        return nextTickToExecute;
    };

    //
    //   U t i l i t i e s
    //

    protected void moveCursorFromPointAToPointB(double[] lastScreenPoint, double[] currentScreenPoint, int delay) {

        double[] vec3 = VAlgebra.initVec3(0, 0, 0);

        double length2 = VAlgebra.vec3Len(currentScreenPoint);
        double distance;
        double p = 0D;
        do {
            if (executionCanceled) {
                int[] intVec = VAlgebra.doubleVec3ToInt(currentScreenPoint);
                robot.mouseMove(intVec[0], intVec[1]);
                mclnGraphDesignerView.repaint();
                break;
            }
            vec3 = VAlgebra.LinCom3(vec3, (1.D - p), lastScreenPoint, p, currentScreenPoint);
            int[] intVec = VAlgebra.doubleVec3ToInt(vec3);
            robot.mouseMove(intVec[0], intVec[1]);
            mclnGraphDesignerView.repaint();
            robot.delay(delay);
            double length1 = VAlgebra.vec3Len(vec3);
            p = p + 0.05;
            distance = Math.abs(length1 - length2);
        } while (distance > 0.001);
    }

    /**
     *
     */

    private static class ToolBarLocationFinder implements Runnable {

        static Object[] getSystemDataInEDT(String tooBarButtonMenuItem) {
            ToolBarLocationFinder toolBarLocationFinder = new ToolBarLocationFinder(tooBarButtonMenuItem);
            boolean success = true;
            try {
                SwingUtilities.invokeAndWait(toolBarLocationFinder);
            } catch (Exception e) {
                success = false;
            }
            if (!success) {
                return null;
            }
            return toolBarLocationFinder.getResult();
        }

        private final String tooBarButtonMenuItem;
        private Object[] result = new Object[2];

        public ToolBarLocationFinder(String tooBarButtonMenuItem) {
            this.tooBarButtonMenuItem = tooBarButtonMenuItem;
        }

        @Override
        public void run() {
            AbstractButton abstractButton = DseMenuAndToolbarBuilder.getSimulationControlButton(tooBarButtonMenuItem);
            if (abstractButton == null) {
                return;
            }
            Rectangle bounds = abstractButton.getBounds();
            Point p = abstractButton.getLocationOnScreen();
            int x = p.x + bounds.width / 2;
            int y = p.y + bounds.height / 2;
            double[] currentScreenPoint = new double[]{x, y, 0};
            result[0] = abstractButton;
            result[1] = currentScreenPoint;
        }

        public Object[] getResult() {
            return result;
        }
    }

    private AbstractButton abstractButton;
    private int clicksToDo;
    private int clicksToDoCounter;
    AnimatingTickExecutor usingSimulationControlButtonsExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        String simulationButtonMenuItem = scriptItem.getActionAttribute1();
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:
                clicksToDo = stringToInt(scriptItem.getActionAttribute2());
                clicksToDoCounter = 0;

                Object[] data = ToolBarLocationFinder.getSystemDataInEDT(simulationButtonMenuItem);
                if (data == null) {
                    Thread.dumpStack();
                    return 100;
                }
                abstractButton = (AbstractButton) data[0];
                double[] currentScreenPoint = (double[]) data[1];

                moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                lastScreenPoint = currentScreenPoint;

                nextTickToExecute++;
                robot.delay(1000);
                dsdsseMainFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                robot.delay(1000);
                break;

            case 2:
                /*
                    Clicking on simulation control
                 */
                if (abstractButton != null) {
                    abstractButton.doClick();
                }
                ++clicksToDoCounter;
                if (!executionCanceled && clicksToDoCounter < clicksToDo) {
                    nextTickToExecute = 2;
                    robot.delay(1000);
                } else {
                    robot.delay(1000);
                    dsdsseMainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    robot.delay(1000);
                    double[] screenPoint = new double[]{lastScreenPoint[0], lastScreenPoint[1] + 30, 0};
                    moveCursorFromPointAToPointB(lastScreenPoint, screenPoint, MOVING_TO_MENU_DELAY);
                    lastScreenPoint = screenPoint;

                    nextTickToExecute = 100;
                }
                break;
            case 100:
                /*
                    Second Arc created. Operation complete
                 */
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

    /**
     *
     */
    AnimatingTickExecutor clickOnToolBarButtonExecutor = (ScriptItem scriptItem, int tickToExecute) -> {
        String simulationButtonMenuItem = scriptItem.getActionAttribute1();
        int nextTickToExecute = tickToExecute;
        switch (tickToExecute) {
            case 1:

                Object[] data = ToolBarLocationFinder.getSystemDataInEDT(simulationButtonMenuItem);
                if (data == null) {
                    Thread.dumpStack();
                    return 100;
                }
                abstractButton = (AbstractButton) data[0];
                double[] currentScreenPoint = (double[]) data[1];

                moveCursorFromPointAToPointB(lastScreenPoint, currentScreenPoint, MOVING_TO_MENU_DELAY);
                lastScreenPoint = currentScreenPoint;

                nextTickToExecute++;
                robot.delay(1000);
                dsdsseMainFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                robot.delay(1000);
                break;

            case 2:
                /*
                    Clicking on simulation control
                 */
                if (abstractButton != null) {
                    abstractButton.doClick();
                }

                robot.delay(1000);
                dsdsseMainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                robot.delay(1000);

                double[] screenPoint = new double[]{lastScreenPoint[0], lastScreenPoint[1] + 120, 0};
                moveCursorFromPointAToPointB(lastScreenPoint, screenPoint, MOVING_TO_MENU_DELAY);
                lastScreenPoint = screenPoint;

                nextTickToExecute = 100;

                break;
            case 100:
                /*
                    Second Arc created. Operation complete
                 */
                executeTimerTask = false;
                nextTickToExecute = 0;
                break;
            default:
                Thread.dumpStack();
        }
        return nextTickToExecute;
    };

    /**
     *
     */
    protected void doMouseClick() {
        if (executionCanceled) {
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            return;
        }
        robot.delay(1000);
        dsdsseMainFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(1000);
        dsdsseMainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * @param dsdsseMainFrame
     */
    void setMouseToPoint(DsdsseMainFrame dsdsseMainFrame, int yOffset) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle bounds = dsdsseMainFrame.getBounds();

        int appX = (screenSize.width - DSDSSEApp.DSE_INITIAL_AND_MINIMUM_SIZE.width) / 2;
        int appY = (screenSize.height - DSDSSEApp.DSE_INITIAL_AND_MINIMUM_SIZE.height) / 2;
        bounds.x = appX;
        bounds.y = appY;
        bounds.width = DSDSSEApp.DSE_INITIAL_AND_MINIMUM_SIZE.width;
        bounds.height = DSDSSEApp.DSE_INITIAL_AND_MINIMUM_SIZE.height;
        dsdsseMainFrame.setBounds(bounds);

        int x = bounds.x + bounds.width / 2;
        int y = bounds.y + bounds.height / 2;

        robot.mouseMove(x, y + yOffset);
    }

    // ================================================================================================================
    //  S h o w   E n d e d   called after all script operations are executed
    // ================================================================================================================

    void showEnded() {
        // stopping and destroying timer and task
        demoScript.clear();
        executeTimerTask = false;
        currentAnimationTickToExecute = 0;
        timerTask.cancel();
        animationTickTimer.cancel();
        animationTickTimer.purge();

        timerTask = null;
        animationTickTimer = null;
        scriptStepCompletionFeedback = null;

        mclnGraphDesignerView = null;
        currentTickExecutor = null;
        currentScriptItem = null;
        subMenuItem = null;
        messageClosedListener = null;
        dsdsseMainFrame = null;
    }
}
