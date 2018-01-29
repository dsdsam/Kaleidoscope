package dsdsse.animation;

import adf.onelinemessage.AdfOneLineMessageManager;
import dsdsse.app.AppController;
import dsdsse.app.AppStateModel;
import dsdsse.app.DSDSSEApp;
import dsdsse.app.DsdsseMainFrame;
import dsdsse.designspace.DesignSpaceModel;
import dsdsse.messagepopuppanel.MessagePopUpPanelHolder;
import dsdsse.messagepopuppanel.MessagePopupManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Admin on 5/26/2017.
 */
public class PresentationRunner {
    // - 0 U ! ~     = & * I  <=

    static final String PRESENTATION_STARTED_MESSAGE = "Presentation started.   " +
            "Please do not click on or move the mouse during the presentation!   " +
            "Press ESC to cancel the presentation.";

    public static interface ScriptStepCompletionFeedback {
        //        void stepExecutionComplete();
        void done();
    }

    protected static final int fontSize = 17;
    private final String THANK_YOU_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + (fontSize + 1) + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:600px; \">").
            append("<font  size=\"6\">Thank you for watching the presentation.</font><br> ").
            append("You can start using the mouse and the keyboard now. ").
            append("</div></html>").toString();

    private final String STARTING_PRESENTATION_CANCELLED_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:" + fontSize + "px; font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:600px; \">").
            append("<font  size=\"6\">Starting Presentation interrupted !</font> ").
            append("</div></html>").toString();

    private final String PRESENTATION_INTERRUPTED_MESSAGE = new StringBuilder().
            append("<html>").
            append("<div style=\"  font-weight: plane; color:#FFFFFF; text-align:center; ").
            append(" width:600px; \">").
            append("<font  size=\"6\">Presentation interrupted !</font> ").
            append("</div></html>").toString();

    private static PresentationRunner starterPresentationRunner;
    private static PresentationRunner presentationRunner;

    // This flag is set to true when Introductory Script Starter
    // is launched from Welcome Panel and stays true through the
    // moment when Introductory Presentation Script is created.
    // It is only needed to configure selected from Help menu
    // script by calling prepareIntroductoryScript method
    private static volatile boolean startingIntroductoryDemo;

    // This flag prevents another script to start while one is already running
    private static volatile boolean scriptIsRunning;
    //
    private static volatile boolean cancelPresentation;

    /**
     * This method is called by AppController to prevent
     * starting Presentation when it is already started.
     *
     * @return
     */
    public static synchronized final boolean isDemoRunning() {
        if (!SwingUtilities.isEventDispatchThread()) {
            Thread.dumpStack();
        }
        return scriptIsRunning;
    }

    /**
     * C a n c e l   p r e s e n t a t i o n
     * <p>
     * Method called when user pressed ESC button
     * <p>
     * When starter script is canceled, flag cancelPresentation is set to true.
     * This will prevent next script step execution.
     * Then. If Starter script cancelled but finished, we know Introductory
     * script is started and do not reset cancelPresentation flag back to false.
     * Started Introductory script copies cancelPresentation flag into instance
     * and resets it back to false.
     * <p>
     * Processing cancellation request;
     * If cancellation request happened during
     * script step execution we want to quickly
     * finish step execution.
     * a) method  MessagePopupManager.cancelMessage
     * is called to hide message if it is shown
     * b) Demo Runner method cancelScriptStepExecution()
     * is called to speed up any script step execution
     * by skipping any programmed in the script delays.
     * <p>
     * Some short or none interruptable steps will not be cancelled.
     */
    public static synchronized void cancelPresentation() {
        if (!SwingUtilities.isEventDispatchThread()) {
            Thread.dumpStack();
        }
        if (cancelPresentation) {
            return;
        }
        cancelPresentation = true;
        MessagePopupManager.cancelMessage();
        if (starterPresentationRunner != null) {
            starterPresentationRunner.cancelScriptExecution();
        } else if (presentationRunner != null) {
            presentationRunner.cancelScriptExecution();
        }
    }

    /**
     *
     */
    public static synchronized final void startIntroductoryPresentationStarter() {
        if (!SwingUtilities.isEventDispatchThread()) {
            Thread.dumpStack();
        }
        cancelPresentation = false;  // reset is accidental was not reset before
        startingIntroductoryDemo = true;
        IntroductoryScriptStarter introductoryScriptStarter =
                IntroductoryScriptStarter.createIntroductoryScriptStarterScript();
        starterPresentationRunner = new PresentationRunner();
        starterPresentationRunner.prepareStarterScript(introductoryScriptStarter);
    }

    /**
     * Called to launch from menu individual presentation
     * <p>
     * This method returns control before presentation started
     */
    public static synchronized final void presentDemo(final PresentationScriptHandler presentationScriptHandler) {
        if (!SwingUtilities.isEventDispatchThread()) {
            Thread.dumpStack();
        }
        starterPresentationRunner = null; // reset is accidental was not reset before
        scriptIsRunning = true;

        // starting presentation

        presentationRunner = new PresentationRunner();
        if (startingIntroductoryDemo) {
            startingIntroductoryDemo = false;
            presentationRunner.prepareIntroductoryScript(presentationScriptHandler);
        } else {
            cancelPresentation = false; // reset is accidental was not reset before
            presentationRunner.prepareAndExecuteIndividualScript(presentationScriptHandler);
        }
    }

    /**
     * @param allScripts
     */
    public static synchronized final void presentAllDemoScripts(List<PresentationScriptHandler> allScripts) {
        if (!SwingUtilities.isEventDispatchThread()) {
            Thread.dumpStack();
        }
        starterPresentationRunner = null;   // reset is accidental was not reset before
        cancelPresentation = false; // reset is accidental was not reset before

        scriptIsRunning = true;
        // starting presentation
        presentationRunner = new PresentationRunner();
        presentationRunner.prepareAndExecuteFirstScript(allScripts);
    }


    //
    //   I n s t a n c e
    //

    private String executedScriptName;
    private PresentationScriptHandler presentationScriptHandler;
    private boolean cancelInstance;

    private final List<PresentationScriptHandler> allScripts = new ArrayList();
    private final BlockingQueue<ScriptItem> scriptItemQueue = new LinkedBlockingQueue();
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final Semaphore itemExecutionSemaphore = new Semaphore(1);
    private int scriptIndex = 0;

    ScriptStepCompletionFeedback scriptStepCompletionFeedback = new ScriptStepCompletionFeedback() {
        public void stepExecutionComplete() {

        }

        public void done() {
            itemExecutionSemaphore.release();
        }
    };

    //
    //   S c r i p t   p r e p a r a t i o n   m e t h o d s
    //

    /**
     * @param presentationScriptHandler
     */
    private final void prepareStarterScript(PresentationScriptHandler presentationScriptHandler) {
        this.presentationScriptHandler = presentationScriptHandler;
        executedScriptName = presentationScriptHandler.getScriptName() + " is being executed";
        presentationScriptHandler.setScriptStepCompletionFeedback(scriptStepCompletionFeedback);
        MessagePopupManager.createMessagePopup();

        List<ScriptItem> preScriptItems = PresentationScriptHandler.getPreScriptItems();
        scriptItemQueue.addAll(preScriptItems);

        ScriptItem scriptItem;
        while ((scriptItem = presentationScriptHandler.getNextItem()) != null) {
            if (PresentationScriptHandler.SKIP_STEP_FOR_INDIVIDUAL_SHOW.equalsIgnoreCase(scriptItem.getMessageType())) {
                continue;
            }
            scriptItemQueue.add(scriptItem);
        }
        executor.execute(scriptExecutor);
    }

    /**
     * @param presentationScriptHandler
     */
    private final void prepareIntroductoryScript(PresentationScriptHandler presentationScriptHandler) {
        this.presentationScriptHandler = presentationScriptHandler;
        executedScriptName = presentationScriptHandler.getScriptName() + " is being executed";

        presentationScriptHandler.setScriptStepCompletionFeedback(scriptStepCompletionFeedback);
        MessagePopupManager.createMessagePopup();

        ScriptItem scriptItem;
        while ((scriptItem = presentationScriptHandler.getNextItem()) != null) {
            if (PresentationScriptHandler.SKIP_STEP_FOR_INDIVIDUAL_SHOW.equalsIgnoreCase(scriptItem.getMessageType())) {
                continue;
            }
            scriptItemQueue.add(scriptItem);
        }

        List<ScriptItem> postScriptItems = PresentationScriptHandler.getPostScriptItems();
        scriptItemQueue.addAll(postScriptItems);

        // Propagating cancellation request from Starter to Introductory Presentation Runner
        this.cancelInstance = cancelPresentation;
        if (!presentationScriptHandler.isStarter()) {
            cancelPresentation = false;
        }

        // starting script executor tread
        executor.execute(scriptExecutor);
    }

    /**
     * @param presentationScriptHandler
     */
    private final void prepareAndExecuteIndividualScript(PresentationScriptHandler presentationScriptHandler) {
        this.presentationScriptHandler = presentationScriptHandler;

        presentationScriptHandler.setScriptStepCompletionFeedback(scriptStepCompletionFeedback);
        MessagePopupManager.createMessagePopup();


        List<ScriptItem> preScriptItems = PresentationScriptHandler.getPreScriptItems();
        scriptItemQueue.addAll(preScriptItems);


        ScriptItem scriptItem;
        while ((scriptItem = presentationScriptHandler.getNextItem()) != null) {
            if (PresentationScriptHandler.SKIP_STEP_FOR_INDIVIDUAL_SHOW.equalsIgnoreCase(scriptItem.getMessageType())) {
                continue;
            }
            scriptItemQueue.add(scriptItem);
        }

        List<ScriptItem> postScriptItems = PresentationScriptHandler.getPostScriptItems();
        scriptItemQueue.addAll(postScriptItems);
        executor.execute(scriptExecutor);
    }

    /**
     * Puts in the queue Pre Script and first script Items
     *
     * @return
     */
    private final void prepareAndExecuteFirstScript(List<PresentationScriptHandler> allScripts) {

        this.allScripts.clear();
        this.allScripts.addAll(allScripts);
        scriptIndex = 0;

        presentationScriptHandler = allScripts.get(scriptIndex);
        executedScriptName = presentationScriptHandler.getScriptName() + " is being executed";
        presentationScriptHandler.setScriptStepCompletionFeedback(scriptStepCompletionFeedback);
        MessagePopupManager.createMessagePopup();

        List<ScriptItem> preScriptItems = PresentationScriptHandler.getPreScriptItems();
        scriptItemQueue.addAll(preScriptItems);

        ScriptItem scriptItem;
        while ((scriptItem = presentationScriptHandler.getNextItem()) != null) {
            scriptItemQueue.add(scriptItem);
        }
        scriptIndex++;
        executor.execute(scriptExecutor);
    }


    /**
     * Puts in the queue next script and Post Script Items
     *
     * @return
     */
    private final void prepareAndExecuteNextPresentationScriptHandler(List<PresentationScriptHandler> allScripts) {

        presentationScriptHandler = allScripts.get(scriptIndex);
        executedScriptName = presentationScriptHandler.getScriptName() + " is being executed";
        presentationScriptHandler.setScriptStepCompletionFeedback(scriptStepCompletionFeedback);
        DesignSpaceModel.getInstance().updatePresentationModelName(presentationScriptHandler.getScriptName());

        ScriptItem scriptItem;
        while ((scriptItem = presentationScriptHandler.getNextItem()) != null) {
            scriptItemQueue.add(scriptItem);
        }
        if (scriptIndex == (allScripts.size() - 1)) {
            List<ScriptItem> postScriptItems = PresentationScriptHandler.getPostScriptItems();
            scriptItemQueue.addAll(postScriptItems);
        }
        scriptIndex++;
        executor.execute(scriptExecutor);

    }

    /**
     * Canceling script step execution
     */
    private void cancelScriptExecution() {
        cancelInstance = true;
        /*
            If ESC is pressed after script execution loop is finished
            the presentationScriptHandler is null
         */
        if (presentationScriptHandler != null) {
            presentationScriptHandler.cancelCurrentStepExecution();
        }
    }

    //
    //   M a i n   S c r i p t   E x e c u t i n g   L o o p
    //

    private final Runnable scriptExecutor = () -> {
        boolean starterScript = presentationScriptHandler.isStarter();
        boolean allStepsExecuted = false;
        do {
            try {
                if (cancelInstance) {
                    break;
                }
                ScriptItem scriptItem = scriptItemQueue.take();
                allStepsExecuted = scriptItemQueue.size() == 0;

                if (itemExecutionSemaphore.availablePermits() == 1) {
                    itemExecutionSemaphore.acquire();
                }
                SwingUtilities.invokeLater(() -> {
                    presentationScriptHandler.executeScriptStep(scriptItem);
                });
                // waiting for a step is executed
                itemExecutionSemaphore.acquire();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (cancelInstance) {
                break;
            }
        } while (scriptItemQueue.size() != 0);

        if (!cancelInstance && scriptIndex < allScripts.size()) {
            prepareAndExecuteNextPresentationScriptHandler(allScripts);
        } else {
            terminatePresentationRunner(cancelInstance, starterScript, allStepsExecuted);
        }
    };

    //   ======================================================================================================
    //   Operation: Cleanup
    //   ======================================================================================================

    /**
     * The termination conditions:
     * There are three scenarios:
     * 1) User starts Starter script that selects from Help menu and starts Introductory Presentation script.
     * 2) User starts from Help menu single Presentation script.
     * 3) User starts from Help menu All Presentations script.
     * <p>
     * The behavior of single Presentation and All Presentations scripts has no difference.
     * <p>
     * It is assumed that each Presentation script starts with expanding application frame to max screen size.
     * When script is finished the Runner returns the application to its initial size and stops the execution.
     */
    private synchronized void terminatePresentationRunner(boolean cancelInstance, boolean starterScript,
                                                          boolean allStepsExecuted) {

        boolean introductoryScriptStarted = starterScript && allStepsExecuted;

        if (starterScript) { // starter script finished or canceled
            if (cancelInstance) {
                // starter script canceled
                if (!introductoryScriptStarted) {
                    // cancelling starter script only

                    // we reset this flag here because Introductory script not started
                    // and thus did not reset it at start time
                    startingIntroductoryDemo = false;

                    // setting DSE initial size, showing Thank You message and doing cleanup

                    FrameAnimationListener frameResetToNormalListener = new FrameAnimationListener() {
                        @Override
                        public void frameGrowthComplete(boolean growthUp) {
                            if (growthUp) {
                            } else {
                                completePresentationScriptAndRunnerInstanceExecution(starterScript, cancelInstance);
                            }
                        }
                    };

                    presentationScriptHandler.showEnded();
                    presentationScriptHandler.setNormalInitialState(frameResetToNormalListener);

                } else {
                    // Introductory script was started. Only starter script execution should be stopped.
                    // When Introductory is finished its Runner will restore application size to initial.
                    presentationScriptHandler.showEnded();
                    clearPresentationRunnerAndRestoreStashedProject(starterScript, cancelInstance);
                }
            } else {
                // starter script finished
                presentationScriptHandler.showEnded();
                clearPresentationRunnerAndRestoreStashedProject(starterScript, cancelInstance);
            }
        } else { // presentation script finished or canceled

            // setting DSE initial size, showing Thank You or Interrupted message and doing cleanup

            AppController.getInstance().cleanupAfterDemoPresentationIsCompleteOrCanceled();

            FrameAnimationListener frameResetToNormalListener = new FrameAnimationListener() {
                @Override
                public void frameGrowthComplete(boolean growthUp) {
                    if (growthUp) {
                    } else {
                        completePresentationScriptAndRunnerInstanceExecution(starterScript, cancelInstance);
                    }
                }
            };
            presentationScriptHandler.showEnded();
            presentationScriptHandler.setNormalInitialState(frameResetToNormalListener);

        }
    }

    /**
     * This method end Presentation
     */
    private void completePresentationScriptAndRunnerInstanceExecution(boolean starterScript, boolean cancelInstance) {
        DsdsseMainFrame dsdsseMainFrame = DsdsseMainFrame.getInstance();
        dsdsseMainFrame.setExtendedState(JFrame.NORMAL);

        // placing mouse below message on the screen
        presentationScriptHandler.setMouseToPoint(DsdsseMainFrame.getInstance(), 120);
        clearPresentationRunnerAndRestoreStashedProject(starterScript, cancelInstance);
    }

    /**
     *
     */
    private void clearPresentationRunnerAndRestoreStashedProject(boolean starterScript, boolean cancelInstance) {

        scriptIsRunning = false;

        scriptItemQueue.clear();
        allScripts.clear();
        DsdsseMainFrame dsdsseMainFrame = DsdsseMainFrame.getInstance();
        if (starterScript) {
            starterPresentationRunner = null;
            if (cancelInstance) {
                showStartingPresentationCancelledMessage(dsdsseMainFrame, presentationScriptHandler);
            }
        } else {
            presentationRunner = null;
            if (cancelInstance) {
                showPresentationCancelledMessage(dsdsseMainFrame, presentationScriptHandler);
            } else {
                showThankYouMessage(dsdsseMainFrame, presentationScriptHandler);
            }
        }

        presentationScriptHandler = null;
        if (starterScript) {
            System.out.println("Starter script canceled and completed !!");
        } else {
            System.out.println("Presentation completed !!");
        }
    }

    /**
     *
     */
    private void showStartingPresentationCancelledMessage(DsdsseMainFrame dsdsseMainFrame,
                                                          PresentationScriptHandler presentationScriptHandler) {
        int width = 800;
        SwingUtilities.invokeLater(() -> {
            // place mouse below message on the screen   0xCA00CA
            MessagePopupManager.showMessagePopup(STARTING_PRESENTATION_CANCELLED_MESSAGE,
                    MessagePopUpPanelHolder.WARNING_MESSAGE, MessagePopUpPanelHolder.MESSAGE_LOCATION_CENTER, width,
                    0xED1C24, Color.WHITE.getRGB(), 2000, () -> {
                        dsdsseMainFrame.setAlwaysOnTop(false);
                        dsdsseMainFrame.setResizable(true);
                        // center mouse on the design space
//                        presentationScriptHandler.setMouseToPoint(DsdsseMainFrame.getInstance(), 0);
                    });
        });
    }

    /**
     *
     */
    private void showPresentationCancelledMessage(DsdsseMainFrame dsdsseMainFrame,
                                                  PresentationScriptHandler presentationScriptHandler) {
        int width = 800;
        SwingUtilities.invokeLater(() -> {
            // place mouse below message on the screen
            MessagePopupManager.showMessagePopup(PRESENTATION_INTERRUPTED_MESSAGE,
                    MessagePopUpPanelHolder.WARNING_MESSAGE, MessagePopUpPanelHolder.MESSAGE_LOCATION_CENTER, width,
                    0xED1C24, Color.WHITE.getRGB(), 2000, () -> {
                        dsdsseMainFrame.setAlwaysOnTop(false);
                        dsdsseMainFrame.setResizable(true);

                        // center mouse on the design space
//                        presentationScriptHandler.setMouseToPoint(DsdsseMainFrame.getInstance(), 0);
                    });
        });
    }

    /**
     *
     */
    private void showThankYouMessage(DsdsseMainFrame dsdsseMainFrame,
                                     PresentationScriptHandler presentationScriptHandler) {
        int width = 800;
        SwingUtilities.invokeLater(() -> {
            // place mouse below message on the screen
            MessagePopupManager.showMessagePopup(THANK_YOU_MESSAGE, MessagePopUpPanelHolder.WARNING_MESSAGE,
                    MessagePopUpPanelHolder.MESSAGE_LOCATION_CENTER, width, 0xEE7700, Color.WHITE.getRGB(), 4000, () -> {
                        dsdsseMainFrame.setAlwaysOnTop(false);
                        dsdsseMainFrame.setResizable(true);

                        // center mouse on the design space
//                        presentationScriptHandler.setMouseToPoint(DsdsseMainFrame.getInstance(), 0);
                    });
        });
    }
}
