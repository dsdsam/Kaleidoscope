package dsdsse.designspace;

import dsdsse.app.AppStateModel;
import dsdsse.app.DsdsDseMessagesAndDialogs;
import dsdsse.app.DsdsseEnvironment;
import dsdsse.app.DsdsseMainFrame;
import dsdsse.designspace.executor.MclnSimulationController;
import dsdsse.dialogs.creation.project.ProjectAttributesSetupMainPanel;
import mcln.model.MclnDoubleRectangle;
import mcln.model.MclnModel;
import mcln.model.MclnProject;
import mcln.model.ProjectAttributes;
import mclnview.graphview.MclnGraphModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Feb 14, 2013
 * Time: 10:25:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class DesignSpaceModel {

    private static final String INITIALIZE_PROJECT_ATTRIBUTES_DIALOG_TITLE = "Initialize Project Attributes";
    private static final String CHANGE_PROJECT_ATTRIBUTES_DIALOG_TITLE = "Change Project Attributes";

    private static DesignSpaceModel designSpaceModel;

    public static synchronized void createInstance(MclnGraphModel mclnGraphModel, MclnProject mclnProject) {
        assert designSpaceModel == null : "Design Space Model is a singleton and already created";
        designSpaceModel = new DesignSpaceModel(mclnGraphModel, mclnProject);
    }

    public static synchronized DesignSpaceModel getInstance() {
        assert designSpaceModel != null : "Design Space Model is a singleton and not yet created";
        return designSpaceModel;
    }

    //
    //   Design Space Model instance. Manages DSDSSE's projects
    //

    private DesignSpaceModelListener designSpaceModelListener;

    private int projectCounter;  // default projects number
    private int modelCounter;    // model number in current project

    private MclnProject mclnProject;
    private MclnGraphModel mclnGraphModel;


    private String projectName;

    private List models = new ArrayList();

    private boolean projectModifiedSinceTraceHistoryStarted;

    /**
     * @param mclnGraphModel
     */
    private DesignSpaceModel(MclnGraphModel mclnGraphModel, MclnProject mclnProject) {
        this.mclnGraphModel = mclnGraphModel;
        this.mclnProject = mclnProject;
        this.projectName = mclnProject.getProjectName();
        try {
            initDesignSpaceModel(mclnProject);
            projectModifiedSinceTraceHistoryStarted = true;
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
    }

    public boolean isDesignSpaceEmpty() {
        return mclnProject.isProjectEmpty();
    }

    /**
     *
     */
    private void initDesignSpaceModel(MclnProject mclnProject) {
        updateFrameTitleProjectName(mclnProject);
        ProjectStorage.createInstance(DsdsseEnvironment.MCLN_PROJECT_STORAGE_DIRECTORY_NAME);

        // set initial empty MCLN model to simulator;
        MclnModel currentMclnModel = mclnProject.getCurrentMclnModel();
        MclnSimulationController.getInstance().setMclnModel(currentMclnModel);
    }

    /**
     * @return
     */
    public boolean isProjectModifiedSinceTraceHistoryStarted() {
        boolean modelModifiedSinceTraceHistoryStarted = mclnGraphModel.isModelModifiedSinceTraceHistoryStarted();
        return projectModifiedSinceTraceHistoryStarted || modelModifiedSinceTraceHistoryStarted;
    }

    public void clearModificationFlag() {
        mclnGraphModel.clearModificationFlag();
        projectModifiedSinceTraceHistoryStarted = false;
    }

    //
    // adding Design Space View as listener
    //
    public void addDesignSpaceModelListener(DesignSpaceModelListener designSpaceModelListener) {
        this.designSpaceModelListener = designSpaceModelListener;
    }

    public MclnProject getMclnProject() {
        return mclnProject;
    }

    /**
     * Initialising project's name and design space rectangle
     *
     * @return
     */
    public MclnProject onCreateNewEmptyMclnProject() {
        JFrame mainFrame = DsdsseMainFrame.getInstance();
        String suggestedProjectName = "Mcln Project " + String.format("%03d", (projectCounter + 1));
        MclnDoubleRectangle suggestedMclnDoubleRectangle = new MclnDoubleRectangle(-20, 20, 40, 40);
        ProjectAttributes suggestedProjectAttributes =
                new ProjectAttributes(suggestedProjectName, suggestedMclnDoubleRectangle);
        ProjectAttributesSetupMainPanel projectAttributesSetupMainPanel =
                ProjectAttributesSetupMainPanel.createInstance(mainFrame, suggestedProjectAttributes);
        ProjectAttributes projectAttributesToApply =
                projectAttributesSetupMainPanel.showInitProjectAttributesDialog(mainFrame,
                        INITIALIZE_PROJECT_ATTRIBUTES_DIALOG_TITLE);
        if (projectAttributesToApply == null) {
            return null;
        }
        projectCounter++;
        String projectName = projectAttributesToApply.getProjectName();
        MclnDoubleRectangle mclnDoubleRectangleToApply = projectAttributesToApply.getMclnDoubleRectangle();
        MclnModel mclnModel = MclnModel.createInstance("Mcln Model-01", "MG01", mclnDoubleRectangleToApply);
        MclnProject mclnProject = MclnProject.createNewEmptyMclnProject(projectName, mclnDoubleRectangleToApply, mclnModel);
        projectModifiedSinceTraceHistoryStarted = true;
        return mclnProject;
    }

    /**
     * Changing project's name and design space rectangle
     *
     * @param currentMclnProject
     * @return
     */
    public ProjectAttributes onChangeProjectAttributes(MclnProject currentMclnProject) {
        JFrame mainFrame = DsdsseMainFrame.getInstance();
        String currentProjectName = currentMclnProject.getProjectName();
        MclnDoubleRectangle currentProjectSpaceRectangle = currentMclnProject.getProjectSpaceRectangleCopy();
        ProjectAttributes suggestedProjectAttributes =
                new ProjectAttributes(currentProjectName, currentProjectSpaceRectangle);
        ProjectAttributesSetupMainPanel projectAttributesSetupMainPanel =
                ProjectAttributesSetupMainPanel.createInstance(mainFrame, suggestedProjectAttributes);
        ProjectAttributes projectAttributesToApply =
                projectAttributesSetupMainPanel.showChangeProjectAttributesDialog(mainFrame,
                        CHANGE_PROJECT_ATTRIBUTES_DIALOG_TITLE);
        if (projectAttributesToApply == null) {
            return null;
        }

        MclnModel currentMclnModel = currentMclnProject.getCurrentMclnModel();
        MclnDoubleRectangle changedProjectSpaceRectangle = projectAttributesToApply.getMclnDoubleRectangle();
        currentMclnModel.setModelSpaceRectangle(changedProjectSpaceRectangle);
        projectModifiedSinceTraceHistoryStarted = true;
        return projectAttributesToApply;
    }

    //  ================================================================================================================
    //  Support for running presentation projects
    //  ================================================================================================================

    private MclnProject stashedMclnProject;

    /**
     * Method called from AppController when Presentation menu item is selected
     *
     * @param modelName
     * @return
     */
    public MclnProject replaceCurrentProjectWithPresentationProject(String modelName) {
        // stashing current project
        String projectName = "Presentation Show";
        stashedMclnProject = mclnProject;
        // creating new project
        MclnDoubleRectangle mclnDoubleRectangle = new MclnDoubleRectangle(-15, 15, 30, 30);
        MclnModel mclnModel = MclnModel.createInstance(modelName, "MP01", mclnDoubleRectangle);
        MclnProject presentationProject = MclnProject.createPresentationMclnProject(projectName, mclnDoubleRectangle, mclnModel);

        // set presentation project
        mclnGraphModel.replaceCurrentProjectWithPresentationProject(presentationProject);
        this.mclnProject = presentationProject;
        this.projectName = presentationProject.getProjectName();
        updateFrameTitleProjectName(presentationProject);
        projectModifiedSinceTraceHistoryStarted = true;
        // call Design Space View
        if (designSpaceModelListener != null) {
            designSpaceModelListener.onMclnProjectReplaced(presentationProject);
        }

        return presentationProject;
    }

    public void updatePresentationModelName(String newName) {
        MclnProject mclnProject = DesignSpaceModel.getInstance().getMclnProject();
        mclnProject.resetModelName(newName);
        updateFrameTitleProjectName(mclnProject);
    }

    /**
     *
     */
    public void restoreStashedProject() {
        if (stashedMclnProject == null) {
            return;
        }
        MclnProject recreatedMclnProject = MclnProject.recreateStashedMclnProject(stashedMclnProject);
        resetMclnProject(recreatedMclnProject);
        MclnModel currentMclnModel = mclnProject.getCurrentMclnModel();
        MclnSimulationController.getInstance().setMclnModel(currentMclnModel);
        stashedMclnProject = null;
        return;
    }

    // =================================================================================================================

    /**
     * Called to reset MCLN project
     *
     * @param mclnProjectToReset
     */
    public void resetMclnProject(MclnProject mclnProjectToReset) {
        this.mclnProject = mclnProjectToReset;
        this.projectName = mclnProject.getProjectName();
        updateFrameTitleProjectName(mclnProject);
        projectModifiedSinceTraceHistoryStarted = true;
        mclnGraphModel.resetMclnModel(mclnProject);
        // call Design Space View
        if (designSpaceModelListener != null) {
            designSpaceModelListener.onMclnProjectReplaced(mclnProject);
        }
    }

    public void resetMclnProjectUpOnAttributesModified(ProjectAttributes projectAttributes) {
        mclnProject.resetProjectAttributes(projectAttributes);
        this.projectName = projectAttributes.getProjectName();
        updateFrameTitleProjectName(mclnProject);
        projectModifiedSinceTraceHistoryStarted = true;
        mclnGraphModel.resetMclnProjectUpOnAttributesModified(mclnProject);
        // call Design Space View
        if (designSpaceModelListener != null) {
            designSpaceModelListener.onMclnProjectReplaced(mclnProject);
        }
    }

    /**
     * currently not used
     */
//    public void onAddMclnModel() {
////        hideInitAssistant();
//        modelCounter++;
//        String modelName = "Mcln Model " + String.format("%03d", modelCounter);
//
//        modelName = DsdsseEnvironment.textEntryDialog(DsdsseMainFrame.getInstance(), "Model Name Entry Dialog",
//                "Please type in the model name:", modelName);
//        MclnModel mclnModel = MclnModel.createInstance(modelName, "MG01", -15, 15, 30, 30);
//        mclnProject.addMclnModel(mclnModel);
//    }
    public void onRenameMclnModel() {
        String modelName = mclnProject.getCurrentMclnModel().getModelName();
        modelName = DsdsDseMessagesAndDialogs.textEntryDialog(DsdsseMainFrame.getInstance(),
                "Model Name Entry Dialog", "Please type in the model name:", modelName);
        mclnProject.resetModelName(modelName);
        updateFrameTitleProjectName(mclnProject);
    }

    /**
     *
     */
    public MclnProject retrieveMclnProject() {
        MclnProject mclnProject =
                ProjectStorage.getInstance().openMclnProject(DesignSpaceView.getInstance().getMclnGraphDesignerView());
        return mclnProject;
    }

    /**
     *
     */
    public boolean saveProject() {
        boolean success = ProjectStorage.getInstance().saveProject(DesignSpaceView.getInstance().
                getMclnGraphDesignerView(), mclnProject);
        if (success) {
            updateFrameTitleProjectName(mclnProject);
        }
        return success;
    }

    /**
     *
     */
    public boolean saveProjectAs() {
        boolean success = ProjectStorage.getInstance().saveProjectAs(DesignSpaceView.getInstance().
                getMclnGraphDesignerView(), mclnProject);
        if (success) {
            updateFrameTitleProjectName(mclnProject);
        }
        return success;

    }

    /**
     *
     * @param mclnProject
     */
    private void updateFrameTitleProjectName(MclnProject mclnProject) {

        String projectFileName = mclnProject.getLastSavedOrRetrievedProjectFileNameAsItIs();
        String projectName = mclnProject.getProjectName();
        String modelMame = mclnProject.getCurrentMclnModel().getModelName();
        String initialTitle = DsdsseEnvironment.getAppInitialFrameTitle();
        String newTitle = initialTitle;
        if (projectFileName != null && projectFileName.trim().length() != 0) {
            newTitle += "  |  File: \"" + projectFileName + "\"";
        }
        newTitle += "  |  Project: \"" + projectName + "\" / Model: \"" + modelMame + "\"";
        JFrame frame = DsdsseEnvironment.getMainFrame();
        frame.setTitle(newTitle);
        AppStateModel.getInstance().updateModelName(modelMame);
    }
}
