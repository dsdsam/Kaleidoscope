package sem.mission.controlles.mclncontroller;


import mcln.model.MclnDoubleRectangle;
import mcln.model.MclnModel;
import mcln.model.MclnProject;
import mclnview.graphview.MclnGraphView;
import mclnview.graphview.MclnGraphViewModel;
import sem.app.AppConstants;
import sem.appui.MclnControllerHolderPanel;

import java.awt.*;

/**
 * Created by Admin on 11/11/2017.
 */
public final class ControllersBuilder {

    static MclnGraphViewModel mclnGraphViewModel;

    public static final void buildControllers() {
        OperationController.getInstance();
        MclnDoubleRectangle mclnDoubleRectangle = new MclnDoubleRectangle(-10, 10, 20, 20);
        MclnModel currentMclnModel = MclnModel.createInstance("Default Mcln Model", "MG01", mclnDoubleRectangle);
        MclnProject defaultMclnProject = MclnProject.createInitialMclnProject(MclnProject.DEFAULT_PROJECT_NAME,mclnDoubleRectangle, currentMclnModel);
        mclnGraphViewModel = new MclnGraphViewModel(defaultMclnProject);
        MclnGraphView mclnGraphView = new MclnGraphView(mclnGraphViewModel, 20, 0);
        mclnGraphView.setBackground(Color.BLACK);
        MclnControllerHolderPanel.getSingleton().setMclnGraphView(mclnGraphView);
    }

    public static final void buildMclnProject() {
        MclnProject mclnProject = SemMclnRetriever.retrieveMclnController(AppConstants.MCLN_CONTROLLER_CLASS_PATH);
        if (mclnProject == null) {
            return;
        }
        mclnGraphViewModel.resetMclnModel(mclnProject);
        SemMclnController.createInstance(mclnProject);
    }
}
