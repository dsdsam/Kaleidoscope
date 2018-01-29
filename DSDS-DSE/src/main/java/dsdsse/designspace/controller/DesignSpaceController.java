package dsdsse.designspace.controller;

import dsdsse.app.AppController;
import dsdsse.app.DsdsseEnvironment;
import dsdsse.app.AppStateModel;
import dsdsse.demo.*;
import dsdsse.designspace.executor.MclnSimulationController;
import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import dsdsse.designspace.DesignSpaceModel;
import dsdsse.designspace.DesignSpaceView;
import mcln.model.MclnModel;
import mcln.model.MclnProject;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 8/24/13
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DesignSpaceController {

    private static DesignSpaceController designSpaceController = new DesignSpaceController();

    public static DesignSpaceController getInstance() {
        return designSpaceController;
    }

    //
    //   Creation, Saving and Retrieval of MCLN Projects
    //

    /**
     * Called from App Controller on New MCLN Project menu command
     */
//    public MclnProject onNewMclnProject() {
//        System.out.println("DesignSpaceController.onMewMclnProject");
//        MclnProject mclnProject = DesignSpaceModel.getInstance().onCreateNewEmptyMclnProject();
//        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);
//        return mclnProject;
//    }

//    public void onRenameMclnProject() {
//        System.out.println("DesignSpaceController.onRenameMclnProject");
//         DesignSpaceModel.getInstance().onRenameMclnProject();
//    }

    /**
     * Called to retrieve MCLN Project from Store
     */
    public MclnProject retrieveMclnProject() {
        MclnProject mclnProject = DesignSpaceModel.getInstance().retrieveMclnProject();
        if (mclnProject == null) {
            return null;
        }
        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);

        MclnModel currentMclnModel = mclnProject.getCurrentMclnModel();
        MclnSimulationController.getInstance().setMclnModel(currentMclnModel);
        return mclnProject;
    }

    /**
     *
     */
    public boolean saveProject() {
        return DesignSpaceView.getInstance().saveProject();
    }

    /**
     *
     */
    public boolean saveProjectAs() {
        return DesignSpaceView.getInstance().saveProjectAs();
    }


    //
    //  Creation of Demo Examples
    //

    public void onCreateDemoProject(String projectName) {
        if (AppController.MENU_BASIC_BLOCK.equals(projectName)) {
            onCreateBasicBlockExampleProject(projectName);
        } else if (AppController.MENU_LOGICAL_BLOCKS.equals(projectName)) {
            onCreateLogicalBlocksExampleProject(projectName);
        } else if (AppController.MENU_TWO_RULES.equals(projectName)) {
            onCreateTwoRulesExampleProject(projectName);
        } else if (AppController.MENU_THREE_RULES.equals(projectName)) {
            onCreateThreeRulesExampleProject(projectName);
        } else if (AppController.MENU_TRIGGER.equals(projectName)) {
            onCreateTriggerExampleProject(projectName);
        } else if (AppController.MENU_SINGLE_PROPERTY.equals(projectName)) {
            createOneStatementProcessExampleProject(projectName);
        } else if (AppController.MENU_TERNARY_COUNTER.equals(projectName)) {
            createTernaryCounterExampleProject(projectName);
        } else if (AppController.MENU_MUT_EXCL.equals(projectName)) {
            onCreateMutualExclusionExampleProject(projectName);
        } else if (AppController.MENU_DIN_PHIL.equals(projectName)) {
            onCreateDiningPhilosophersExampleProject(projectName);
        }
    }

    /**
     * Create Basic Block Example Project
     */
    private void onCreateBasicBlockExampleProject(String projectName) {

        MclnModel mclnModel = MclnModel.createInstance(AppController.DEMO_PROJECT_BASIC_BLOCK,
                "MG01", -15, 15, 30, 30);
        MclnProject mclnProject = MclnProject.createDemoMclnProject(projectName, mclnModel);

        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);

        MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
        BasicBlock.createBasicBlock(mclnProject, mclnGraphModel);

        finishModelReplacement(mclnProject, AppStateModel.Operation.MODELS_BASIC_BLOCK);
    }

    /**
     * Create Logical Blocks Example Project
     */
    private void onCreateLogicalBlocksExampleProject(String projectName) {

        double x = -15;
        double y = 15;
        double width = 2 * Math.abs(x);
        double height = 2 * Math.abs(y);

        MclnModel mclnModel = MclnModel.createInstance(AppController.DEMO_PROJECT_LOGICAL_BLOCKS,
                "MG01", x, y, width, height);

//        MclnModel mclnModel = MclnModel.createInstance(AppStateModel.DEMO_PROJECT_LOGICAL_BLOCKS,
//                "MG01", -50, -30, 100, 60);
        MclnProject mclnProject = MclnProject.createDemoMclnProject(projectName, mclnModel);

        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);

        MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
        SimpleBlocks.createSimplestNets(mclnProject, mclnGraphModel);

        finishModelReplacement(mclnProject, AppStateModel.Operation.MODELS_LOGICAL_BLOCKS);
    }

    /**
     * Create Two Rules Example Project
     */
    private void onCreateTwoRulesExampleProject(String projectName) {

        MclnModel mclnModel = MclnModel.createInstance(AppController.DEMO_PROJECT_TWO_RULES,
                "MG01", -20, 20, 40, 40);
        MclnProject mclnProject = MclnProject.createDemoMclnProject(projectName, mclnModel);

        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);

        MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
        TwoRules.createTwoRulesModel(mclnProject, mclnGraphModel, -13.0, 2.0, 1);
        TwoRules.createTwoRulesModel(mclnProject, mclnGraphModel, 0.0, 2.0, 2);
        TwoRules.createTwoRulesModel(mclnProject, mclnGraphModel, 13.0, 2.0, 3);

        finishModelReplacement(mclnProject, AppStateModel.Operation.MODELS_TWO_RULES);
    }

    /**
     * Create Three Rules Example Project
     */
    private void onCreateThreeRulesExampleProject(String projectName) {

        MclnModel mclnModel = MclnModel.createInstance(AppController.DEMO_PROJECT_THREE_RULES,
                "MG01", -20, 20, 40, 40);
        MclnProject mclnProject = MclnProject.createDemoMclnProject(projectName, mclnModel);

        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);

        MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
        ThreeRules.createThreeRulesModel(mclnProject, mclnGraphModel);

        finishModelReplacement(mclnProject, AppStateModel.Operation.MODELS_THREE_RULES);
    }

    /**
     * Create Trigger Example Project
     */
    private void onCreateTriggerExampleProject(String projectName) {

        MclnModel mclnModel = MclnModel.createInstance(AppController.DEMO_PROJECT_TRIGGER,
                "MG01", -20, 20, 40, 40);
        MclnProject mclnProject = MclnProject.createDemoMclnProject(projectName, mclnModel);

        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);

        MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
        Trigger.createTrigger(mclnProject, mclnGraphModel);

        finishModelReplacement(mclnProject, AppStateModel.Operation.MODELS_TRIGGER);
    }

    /**
     * Create One Statement Process Example Project
     */
    private void createOneStatementProcessExampleProject(String projectName) {

        MclnModel mclnModel = MclnModel.createInstance(AppController.DEMO_SINGLE_PROPERTY,
                "MG01", -50, 30, 100, 60);
        MclnProject mclnProject = MclnProject.createDemoMclnProject(projectName, mclnModel);

        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);

        MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
        OneStatementProcess.createOneStatementProcess(mclnProject, mclnGraphModel);

        finishModelReplacement(mclnProject, AppStateModel.Operation.MODELS_SINGLE_PROPERTY);
    }

    private void createTernaryCounterExampleProject(String projectName) {

        MclnModel mclnModel = MclnModel.createInstance(AppController.DEMO_TERNARY_COUNTER,
                "MG01", -50, 30, 100, 60);
        MclnProject mclnProject = MclnProject.createDemoMclnProject(projectName, mclnModel);

        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);

        MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
        TernaryCounter.createTernaryCounter(mclnProject, mclnGraphModel);

        finishModelReplacement(mclnProject, AppStateModel.Operation.MODELS_TERNARY_COUNTER);
    }

    boolean animatedCreation = true;

    /**
     * Create Mutual Exclusion Example Project
     */
    private void onCreateMutualExclusionExampleProject(String projectName) {

        MclnModel mclnModel = MclnModel.createInstance(AppController.DEMO_PROJECT_MUTUAL_EXCLUSION,
                "MG01", -30., 15., 60, 30);
        MclnProject mclnProject = MclnProject.createDemoMclnProject(projectName, mclnModel);
        if (animatedCreation) {
            DesignSpaceModel.getInstance().resetMclnProject(mclnProject);
            MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
            MutualExclusion.createMutualExclusion(mclnProject, mclnGraphModel);
        } else {
            MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
            MutualExclusion.createMutualExclusion(mclnProject, mclnGraphModel);
            DesignSpaceModel.getInstance().resetMclnProject(mclnProject);
        }

        finishModelReplacement(mclnProject, AppStateModel.Operation.MODELS_MUT_EX);
    }

    /**
     * Create Dining Philosophers Example Project
     */
    private void onCreateDiningPhilosophersExampleProject(String projectName) {

        double x = -25;
        double y = 25;
        double width = 2 * Math.abs(x);
        double height = 2 * Math.abs(y);
        MclnModel mclnModel = MclnModel.createInstance(AppController.DEMO_PROJECT_DINING_PHILOSOPHERS,
                "MG01", x, y, width, height);
        MclnProject mclnProject = MclnProject.createDemoMclnProject(projectName, mclnModel);

        DesignSpaceModel.getInstance().resetMclnProject(mclnProject);

        MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
        DiningPhilosophers.createDiningPhilosophers(mclnProject, mclnGraphModel);

        finishModelReplacement(mclnProject, AppStateModel.Operation.MODELS_DIN_PHIL);
    }

    /**
     * @param model
     */
    private void finishModelReplacement(MclnProject mclnProject, AppStateModel.Operation model) {

        // prepare simulation mclncontroller

        MclnModel currentMclnModel = mclnProject.getCurrentMclnModel();
        MclnSimulationController.getInstance().setMclnModel(currentMclnModel);

        //  update app info panel model section

        if (DsdsseEnvironment.getCurrentMode() != DsdsseEnvironment.EXEC_MODE) {
            DsdsseEnvironment.setCurrentMode(DsdsseEnvironment.EDIT_MODE);
        }
        AppStateModel appStateModel = AppStateModel.getInstance();
        appStateModel.updateDemoMode(model);
        MclnGraphModel mclnGraphModel = DsdsseEnvironment.getMclnGraphModel();
        mclnGraphModel.demoProjectComplete(mclnProject.getCurrentMclnModel());
    }
}
