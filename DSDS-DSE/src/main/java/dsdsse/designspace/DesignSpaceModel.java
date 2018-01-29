package dsdsse.designspace;

import dsdsse.app.AppStateModel;
import dsdsse.app.DsdsDseMessagesAndDialogs;
import dsdsse.app.DsdsseEnvironment;
import dsdsse.app.DsdsseMainFrame;
import dsdsse.designspace.executor.MclnSimulationController;
import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import mcln.model.MclnModel;
import mcln.model.MclnProject;

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

    public MclnProject onCreateNewEmptyMclnProject() {
        projectCounter++;
        String projectName = "Mcln Project " + String.format("%03d", projectCounter);
        MclnModel mclnModel = MclnModel.createInstance("Mcln Model-01", "MG01", -15, 15, 30, 30);
        MclnProject mclnProject = MclnProject.createNewEmptyMclnProject(projectName, mclnModel);
        projectModifiedSinceTraceHistoryStarted = true;
        return mclnProject;
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
        System.out.println("\nPresentation Project: " + projectName + ", Presentation Model \"" + modelName + "\"");
        stashedMclnProject = mclnProject;
        System.out.println("\nReplace Current Project With Presentation Project: replaced project " + mclnProject.getProjectName() +
                ", restored model \"" + mclnProject.getCurrentMclnModel().getModelName() + "\"");
        // creating new project
        MclnModel mclnModel = MclnModel.createInstance(modelName, "MP01", -15, 15, 30, 30);

        MclnProject presentationProject = MclnProject.createPresentationMclnProject(projectName, mclnModel);

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
            System.out.println("\nRestore Stashed Project: No stashed project to restore");
            return;
        }
        MclnProject recreatedMclnProject = MclnProject.recreateStashedMclnProject(stashedMclnProject);
        resetMclnProject(recreatedMclnProject);
        MclnModel currentMclnModel = mclnProject.getCurrentMclnModel();
        MclnSimulationController.getInstance().setMclnModel(currentMclnModel);
        stashedMclnProject = null;
        System.out.println("\nRestore Stashed Project: restored project " + mclnProject.getProjectName() +
                ", restored model \"" + currentMclnModel.getModelName() + "\"");
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

    public void onRenameMclnProject() {
        projectName = DsdsDseMessagesAndDialogs.textEntryDialog(DsdsseMainFrame.getInstance(),
                "Project Name Entry Dialog", "Please type in the project name:", projectName);
        mclnProject.resetProjectName(projectName);
        updateFrameTitleProjectName(mclnProject);
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
        return ProjectStorage.getInstance().saveProject(DesignSpaceView.getInstance().getMclnGraphDesignerView(), mclnProject);
    }

    /**
     *
     */
    public boolean saveProjectAs() {
        return ProjectStorage.getInstance().saveProjectAs(DesignSpaceView.getInstance().getMclnGraphDesignerView(), mclnProject);
    }

    /**
     * @param mclnProject
     */
    private void updateFrameTitleProjectName(MclnProject mclnProject) {

        String projectName = mclnProject.getProjectName();
        String modelMame = mclnProject.getCurrentMclnModel().getModelName();
        String initialTitle = DsdsseEnvironment.getAppInitialFrameTitle();
        String newTitle = initialTitle +
                " |  Current project: \"" + projectName + "\" / Current model: \"" + modelMame + "\"";
        JFrame frame = DsdsseEnvironment.getMainFrame();
        frame.setTitle(newTitle);
        AppStateModel.getInstance().updateModelName(modelMame);
    }
}
