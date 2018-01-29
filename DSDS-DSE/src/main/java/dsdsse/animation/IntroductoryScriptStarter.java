package dsdsse.animation;

import dsdsse.app.AppController;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.graphview.MclnGraphDesignerView;

/**
 * Created by Admin on 6/23/2017.
 */
public class IntroductoryScriptStarter extends PresentationScriptHandler {

    public static IntroductoryScriptStarter createIntroductoryScriptStarterScript() {
        MclnGraphDesignerView mclnGraphDesignerView = DesignSpaceView.getInstance().getMclnGraphDesignerView();
        return new IntroductoryScriptStarter(mclnGraphDesignerView);
    }

    // Operations
    private static final String OPERATION_START_INTRODUCTORY_PRESENTATION = "Start Introductory Presentation";

    // Step actions and action attributes
    private static final String STEP_CLICK_MENU_BUTTON = "Click on menu button";

    //   ===============================================================================================================
    //   M e s s a g e s
    //   ===============================================================================================================

    private static final String EXPLANATION_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + (fontSize + 1) + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:700px; \">").
            append("All Presentation Shows are located under:<br>").
            append("<font color=#FFDD00>Presentation Shows & Help</font> menu.<br><br> ").
            append("Thus, the mouse is going to be moved to this menu.<br> ").
            append("Then menu item: <font color=#FFDD00>How to Use Creation Operations</font> is going to be selected<br> ").
            append("and the presentation to be started. ").
            append("</div></html>").toString();

    //   ===============================================================================================================
    //   S c r i p t
    //   ===============================================================================================================

    private static final String[][] demoScriptData = {
//
            {SET_CURSOR_TO_HOME_LOCATION, "", ""},
            {PAUSE, "1", ""},
            {INFO_MESSAGE, EXPLANATION_MESSAGE, AGENDA_MESSAGE_BACKGROUND, "0xFFFFFF", MESSAGE_EXPOSURE_LONG},
            {PAUSE, "1", ""},

            //   =======================================================================================================
            //   G e n e r a t i n g   e x a m p l e   m o d e l
            //   =======================================================================================================

            {OPERATION_START_INTRODUCTORY_PRESENTATION, STEP_CLICK_MENU_BUTTON, AppController.MENU_ITEM_HELP,
                    AppController.MENU_ITEM_HOW_TO_USE_CREATION_OPERATIONS},
    };

    private PresentationRunner.ScriptStepCompletionFeedback scriptStepCompletionFeedback;


    /**
     * @param mclnGraphDesignerView
     */
    IntroductoryScriptStarter(MclnGraphDesignerView mclnGraphDesignerView) {
        super(mclnGraphDesignerView);
        initScript();
    }

    @Override
    boolean isStarter() {
        return true;
    }

    private void initScript() {
        scriptScanIndex = 0;
        demoScript.clear();
        for (int i = 0; i < demoScriptData.length; i++) {
            String[] parameters = demoScriptData[i];
            String operation = parameters[0];
            String stepAction = parameters[1];
            String actionAttribute1 = parameters[2];
            String actionAttribute2 = parameters.length <= 3 ? "" : parameters[3];
            String actionAttribute3 = parameters.length <= 4 ? "" : parameters[4];
            String actionAttribute4 = parameters.length <= 5 ? "" : parameters[5];
            ScriptItem scriptItem = new ScriptItem(operation, stepAction, actionAttribute1,
                    actionAttribute2, actionAttribute3, actionAttribute4);
            demoScript.add(scriptItem);
        }
    }

    @Override
    String getScriptName() {
        return "Starter";
    }

    public void setScriptStepCompletionFeedback(PresentationRunner.ScriptStepCompletionFeedback scriptStepCompletionFeedback) {
        this.scriptStepCompletionFeedback = scriptStepCompletionFeedback;
        super.setScriptStepCompletionFeedback(scriptStepCompletionFeedback);
    }

    //
    //   S c r i p t   O p e r a t i o n s   E x e c u t o r
    //

    void executeScriptStep(ScriptItem scriptItem) {

        switch (scriptItem.getOperation()) {
            case INFO_MESSAGE:
            case WARNING_MESSAGE:
                presentMessage(scriptItem);
                break;

            case SET_FULL_SCREEN_MODE:
                executeOperationSetFullScreenMode(scriptItem);
                break;

            case SET_CURSOR_TO_HOME_LOCATION:
                executeOperationSetCursorToHomeLocation(scriptItem);
                break;

            case OPERATION_START_INTRODUCTORY_PRESENTATION:
                startIntroductoryScriptStepsExecutor(scriptItem);
                break;

            case PAUSE:
                pause(scriptItem);
                break;

            default:
                Thread.dumpStack();
                break;
        }
    }

    //
    //   S t a r t   I n t r o d u c t o r y   S c r i p t   S t e p s   E x e c u t o r
    //

    private void startIntroductoryScriptStepsExecutor(ScriptItem scriptItem) {
        if (STEP_CLICK_MENU_BUTTON.equals(scriptItem.getStepAction())) {
            currentTickExecutor = selectOperationFromMenuTickExecutor;
            currentScriptItem = scriptItem;
            currentAnimationTickToExecute = 1;
            executeTimerTask = true;
        } else {
            Thread.dumpStack();
        }
    }
}
