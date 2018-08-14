package dsdsse.designspace.initializer;

import adf.preferences.GroupChangeListener;
import dsdsse.app.AllMessages;
import dsdsse.graphview.DesignerArcView;
import dsdsse.preferences.DsdsseUserPreference;
import dsdsse.preferences.GroupID;
import mclnview.graphview.MclnArcView;
import mclnview.graphview.MclnPropertyView;

import javax.swing.*;

/**
 * Created by Admin on 6/26/2016.
 */
public class InitAssistantInterface {

    private static final String[] OPTIONS = {"Save Modification", "Discard Modification", "Continue Modifying"};

    private static ModelInitializationAssistant initAssistant;

    private static final GroupChangeListener groupChangeListener = (preference) -> {
        boolean status = DsdsseUserPreference.isPropertyView3D();
        MclnPropertyView.setDisplayMclnPropertyViewAs3DCircle(status);
        if (initAssistant != null) {
            initAssistant.repaint();
        }
    };

    static {
        DsdsseUserPreference.getInstance().addGroupChangeListener(
                DsdsseUserPreference.PREF_VIEW_STYLE_KEY, GroupID.GROUP1, groupChangeListener);
    }

    public static final boolean isInitAssistantUpAndRunning() {
        return initAssistant != null;
    }

    static void cleanupOnInitAssistantClosed() {
        initAssistant = null;
    }


    //
    //   C r e a t i o n   F r o n t   P a g e   V i e w
    //

    /**
     * @param mainFrame
     */
    public static final void createInitializationAssistant(JFrame mainFrame) {
        if (InitAssistantInterface.isInitAssistantUpAndRunning()) {
            return;
        }
        initAssistant = new ModelInitializationAssistant();
        initAssistant.initWelcomeView();
        initAssistant.showInitializationAssistant(mainFrame);
    }

    //
    //   C r e a t i o n   of   I n i t i a l i z a t i o n   P a g e s
    //

    public static boolean canInitAssistantBeInterrupted(String message) {
        return initAssistant.canInitAssistantBeInterrupted(message);
    }

    /**
     * @param mainFrame
     * @param mcLnPropertyView
     * @return true when Init Assistant was open, false whe it was reset
     */
    public static final boolean setInitAssistantToInitializeProperty(JFrame mainFrame, MclnPropertyView mcLnPropertyView) {
        if (!InitAssistantInterface.isInitAssistantUpAndRunning()) {
            initAssistant = new ModelInitializationAssistant();
            initAssistant.initPropertyView(mcLnPropertyView);
            initAssistant.showInitializationAssistant(mainFrame);
            return true;
        }
        resetInitAssistantToInitializeProperty(mainFrame, mcLnPropertyView);
        return false;
    }

    /**
     * The method resets Init Assistant to initialize Property
     *
     * @param mcLnPropertyView
     */
    public static void resetInitAssistantToInitializeProperty(JFrame mainFrame, MclnPropertyView mcLnPropertyView) {
        if (!InitAssistantInterface.isInitAssistantUpAndRunning()) {
            return;
        }
        if (!canInitAssistantBeInterrupted(AllMessages.USER_WANTS_TO_REPLACE_COMPONENT_IN_INIT_ASSISTANT.getText())) {
            return;
        }
        initAssistant.resetToInitializePropertyView(mcLnPropertyView);
    }

    /**
     * @param mainFrame
     * @param mclnArcView
     * @return true when Init Assistant was open, false whe it was reset
     */
    public static boolean setInitAssistantToInitializeArc(JFrame mainFrame, DesignerArcView mclnArcView) {
        if (!InitAssistantInterface.isInitAssistantUpAndRunning()) {
            InitAssistantInterface.initAssistant = new ModelInitializationAssistant();
            initAssistant.initArcView(mclnArcView);
            initAssistant.showInitializationAssistant(mainFrame);
            return true;
        }
        resetInitAssistantToInitializeArc(mainFrame, mclnArcView);
        return false;
    }

    /**
     * The method resets Init Assistant to initialize Arc
     *
     * @param mclnArcView
     */
    private static void resetInitAssistantToInitializeArc(JFrame mainFrame, DesignerArcView mclnArcView) {
        if (!InitAssistantInterface.isInitAssistantUpAndRunning()) {
            return;
        }
        if (!canInitAssistantBeInterrupted(AllMessages.USER_WANTS_TO_REPLACE_COMPONENT_IN_INIT_ASSISTANT.getText())) {
            return;
        }
        initAssistant.resetToInitializeArcView(mclnArcView);
    }

    //
    //   S h u t d o w n   I n i t i a l i z a t i o n   A s s i s t a n t
    //

    public static boolean shutdownInitAssistantIfPossible(String question) {
        if (!canInitAssistantBeInterrupted(question)) {
            return false;
        }
        shutDownInitAssistantUnconditionally();
        return true;
    }

    /**
     * The method is used to shutdown Init Assistance if
     * it it does not have any modification to finish
     * <p>
     * Is used by AppControlled and this class
     *
     * @return
     */
    public static void shutDownInitAssistantUnconditionally() {
        if (initAssistant != null) {
            initAssistant.shutDownInitAssistantUnconditionally();
            initAssistant = null;
        }
    }
}
