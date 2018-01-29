package mclnview.graphview;

import mcln.model.*;
import mclnview.graphview.interfaces.MclnModeChangedListener;
import mclnview.graphview.interfaces.MclnModelStructureChangedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Admin on 11/14/2017.
 */
 public class MclnGraphViewModel {

    private static MclnGraphViewModel mclnGraphViewModel;

    public static MclnGraphViewModel getInstance() {
        return mclnGraphViewModel;
    }

    //
    //    I n s t a n c e
    //

    private MclnModelStructureChangedListener mclnModelStructureChangedListener;

    private final CopyOnWriteArrayList<MclnModeChangedListener> mclnModeChangedListeners = new CopyOnWriteArrayList();

    private MclnProject currentMclnProject;
    private MclnModel currentMclnModel;

    private boolean modelModifiedSinceTraceHistoryStarted;

    /**
     * @param currentMclnProject is empty model that is going to be used by Editor
     */
    public MclnGraphViewModel(MclnProject currentMclnProject) {
        this.currentMclnProject = currentMclnProject;
        this.currentMclnModel = currentMclnProject.getCurrentMclnModel();
        mclnGraphViewModel = this;
    }

    public boolean isModelModifiedSinceTraceHistoryStarted() {
        return modelModifiedSinceTraceHistoryStarted;
    }

    public void clearModificationFlag() {
        modelModifiedSinceTraceHistoryStarted = false;
    }

    public void replaceCurrentProjectWithPresentationProject(MclnProject newMclnProject) {
        fireMclnModelCleared();

        this.currentMclnProject = newMclnProject;
        currentMclnModel = newMclnProject.getCurrentMclnModel();

        fireCurrentMclnModelReplaced(currentMclnModel);
        fireMclnModelUpdated(currentMclnModel);
    }

    /**
     * called when new project is retrieved or when the project model changed
     *
     * @param newMclnProject
     */
    public void resetMclnModel(MclnProject newMclnProject) {
        this.currentMclnProject = newMclnProject;
        clearCurrentMclnModel();
        currentMclnModel = newMclnProject.getCurrentMclnModel();
        fireCurrentMclnModelReplaced(currentMclnModel);
    }

    public void demoProjectComplete(MclnModel mclnModel) {
        this.currentMclnModel = mclnModel;
        fireDemoProjectComplete(currentMclnModel);
    }

    public MclnModel getMclnModel() {
        return currentMclnModel;
    }

    //
    //  adding model structure change listeners
    //

    public void addMclnModelBuildingListener(MclnModelStructureChangedListener mclnModelStructureChangedListener) {
//        mclnModelStructureChangedListeners.add(mclnModelStructureChangedListener);
        this.mclnModelStructureChangedListener = mclnModelStructureChangedListener;
    }

    //
    // adding MCLN Graph View as model listeners
    //

    public void addMclnModeChangedListener(MclnModeChangedListener mclnModeChangedListener) {
        mclnModeChangedListeners.add(mclnModeChangedListener);
    }

    public void removeMclnModeChangedListener(MclnModeChangedListener mclnModeChangedListener) {
        mclnModeChangedListeners.remove(mclnModeChangedListener);
    }

    //
    //  adding simulation listeners
    //

    /**
     * This method adds mclnModelSimulationListener to just added Mcln Model when
     * the Application is started or when new model is placed into Design Space.
     *
     * @param mclnModelSimulationListener
     */
    public void addMclnModelSimulationListener(MclnModelSimulationListener mclnModelSimulationListener) {
        currentMclnModel.addMclnModelSimulationListener(mclnModelSimulationListener);
    }

    /**
     * This method removes mclnModelSimulationListener from current Mcln Model when the model is destroyed
     *
     * @param mclnModelSimulationListener
     */
    public void removeMclnModelSimulationListener(MclnModelSimulationListener mclnModelSimulationListener) {
        currentMclnModel.removeMclnModelSimulationListener(mclnModelSimulationListener);
    }

//    public void addMclnArc(MclnArc mclnArc) {
//        currentMclnModel.addMclnArc(mclnArc);
//    }

    public List<MclnStatement> getMclnStatements() {
        return currentMclnModel.getMclnStatements();
    }

    public List<MclnCondition> getMclnConditions() {
        return currentMclnModel.getMclnConditions();
    }

    public List<MclnArc> getMclnArcs() {
        return currentMclnModel.getMclnArcs();
    }

    public void clearCurrentMclnModel() {
        currentMclnModel.clearMclnModel();
        fireMclnModelCleared();
//        LogPanel.getInstance().clearLog();
    }

    public MclnDoubleRectangle getViewRectangle() {
        return currentMclnModel.getModelSpaceRectangle();
    }

    /**
     * @param modelSpaceRectangle
     */
//    public void setModelSpaceRectangle(MclnDoubleRectangle modelSpaceRectangle) {
//        currentMclnModel.setModelSpaceRectangle(modelSpaceRectangle);
//        fireMclnModelViewRectangleUpdated();
//    }

    //
    //   C r e a t i n g    M c l n    G r a p h    E n t i t i e s    b y    E d i t o r
    //

    /**
     * is used to create Statement by Editor
     *
     * @param cSysPoint
     * @return
     */
    public MclnPropertyView createMclnStatementAndUpdateView(double[] cSysPoint) {
        MclnStatement mclnStatement = currentMclnProject.createMclnStatement("", cSysPoint);
        MclnPropertyView mclnPropertyView = addMclnStatementCreateViewUpdateScreen(mclnStatement, true);
        return mclnPropertyView;
    }









    //
    //   A d d i n g   M c l n   M o d e l   E n t i t i e s   a n d   c r e a t i n g
    //      t h e i r   M c l n   G r a p h   V i e w s   f o r   D e m o   M o d e l s
    //

    /**
     * @param mclnStatement
     * @return
     */
    public MclnPropertyView addMclnStatementAndUpdateView(MclnStatement mclnStatement) {
        return addMclnStatementCreateViewUpdateScreen(mclnStatement, false);
    }

    /**
     * @param mclnCondition
     * @return
     */
    public MclnConditionView addMclnConditionAndUpdateView(MclnCondition mclnCondition) {
        return addMclnConditionCreateViewUpdateScreen(mclnCondition, false);
    }

    /**
     * @param mclnArc
     * @return
     */
    public MclnArcView addMclnArcAndUpdateView(MclnArc mclnArc) {
        return addMclnArcCreateViewUpdateScreen(mclnArc, false);
    }

    //
    //   private creators used by editor and Demo creators
    //

    private MclnPropertyView addMclnStatementCreateViewUpdateScreen(MclnStatement mclnStatement, boolean updateScreen) {
        currentMclnModel.addMclnStatement(mclnStatement);
        MclnPropertyView mclnPropertyView = mclnModelStructureChangedListener.mclnStatementAdded(mclnStatement);
        if (updateScreen) {
            fireMclnModelUpdated(currentMclnModel); // redisplay image
        }
        return mclnPropertyView;
    }

    private MclnConditionView addMclnConditionCreateViewUpdateScreen(MclnCondition mclnCondition, boolean updateScreen) {
        currentMclnModel.addMclnCondition(mclnCondition);
        MclnConditionView mclnConditionView = mclnModelStructureChangedListener.mclnConditionAdded(mclnCondition);
        if (updateScreen) {
            fireMclnModelUpdated(currentMclnModel); // redisplay image
        }
        return mclnConditionView;
    }

    private MclnArcView addMclnArcCreateViewUpdateScreen(MclnArc mclnArc, boolean updateScreen) {
        currentMclnModel.addMclnArc(mclnArc);
        MclnArcView mclnArcView = mclnModelStructureChangedListener.mclnArcAdded(mclnArc);
        if (updateScreen) {
            fireMclnModelUpdated(currentMclnModel); // redisplay image
        }
        return mclnArcView;
    }

    //
    //    R e m o v i n g    M c l n    G r a p h    E n t i t i e s
    //

    public void removeMclnStatementAndUpdateView(MclnStatement mclnStatement) {
        List<MclnArc<MclnCondition, MclnStatement>> inboundArcList = mclnStatement.getClonedInboundArcs();
        List<MclnArc<MclnStatement, MclnCondition>> outboundArcList = mclnStatement.getClonedOutboundArcs();
        currentMclnModel.removeMclnStatement(mclnStatement);
        mclnModelStructureChangedListener.mclnStatementRemoved(mclnStatement);
        for (MclnArc mclnArc : inboundArcList) {
            currentMclnModel.removeMclnArc(mclnArc);
            mclnModelStructureChangedListener.mclnArcRemoved(mclnArc);
        }
        for (MclnArc mclnArc : outboundArcList) {
            currentMclnModel.removeMclnArc(mclnArc);
            mclnModelStructureChangedListener.mclnArcRemoved(mclnArc);
        }
        fireMclnModelUpdated(currentMclnModel);
    }

    public void removeMclnConditionAndUpdateView(MclnCondition mclnCondition) {
        List<MclnArc<MclnStatement, MclnCondition>> inboundArcList = mclnCondition.getClonedInboundArcs();
        List<MclnArc<MclnCondition, MclnStatement>> outboundArcList = mclnCondition.getClonedOutboundArcs();
        currentMclnModel.removeMclnCondition(mclnCondition);
        mclnModelStructureChangedListener.mclnConditionRemoved(mclnCondition);
        for (MclnArc mclnArc : inboundArcList) {
            currentMclnModel.removeMclnArc(mclnArc);
            mclnModelStructureChangedListener.mclnArcRemoved(mclnArc);
        }
        for (MclnArc mclnArc : outboundArcList) {
            currentMclnModel.removeMclnArc(mclnArc);
            mclnModelStructureChangedListener.mclnArcRemoved(mclnArc);
        }
        fireMclnModelUpdated(currentMclnModel);
    }

    public void removeMclnArcAndUpdateView(MclnArc mclnArc) {
        currentMclnModel.removeMclnArc(mclnArc);
        mclnModelStructureChangedListener.mclnArcRemoved(mclnArc);
        fireMclnModelUpdated(currentMclnModel);
    }

    /*
      called when new Demo Model created
     */
//    public void fireModelUpdated(MclnModel newCurrentMclnModel) {
//        fireCurrentMclnModelReplaced(newCurrentMclnModel);
//    }

//    private void fireMclnModelViewRectangleUpdated() {
//        mclnModelStructureChangedListener.mclnModelViewRectangleUpdated(this);
//    }

    private void fireMclnModelCleared() {
        for (MclnModeChangedListener mclnModeChangedListener : mclnModeChangedListeners) {
            mclnModeChangedListener.mclnModelCleared();
        }
    }

    private void fireMclnModelUpdated(MclnModel newCurrentMclnModel) {
        for (MclnModeChangedListener mclnModeChangedListener : mclnModeChangedListeners) {
            mclnModeChangedListener.mclnModelUpdated(newCurrentMclnModel);
        }
    }

    private void fireCurrentMclnModelReplaced(MclnModel newMclnModel) {
        for (MclnModeChangedListener mclnModeChangedListener : mclnModeChangedListeners) {
            mclnModeChangedListener.onCurrentMclnModelReplaced(newMclnModel);
        }
    }

    private void fireDemoProjectComplete(MclnModel newMclnModel) {
        for (MclnModeChangedListener mclnModeChangedListener : mclnModeChangedListeners) {
            mclnModeChangedListener.demoProjectComplete(newMclnModel);
        }
    }
}

