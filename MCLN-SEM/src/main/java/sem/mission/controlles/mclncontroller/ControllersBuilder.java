package sem.mission.controlles.mclncontroller;


import mcln.model.MclnDoubleRectangle;
import mcln.model.MclnModel;
import mcln.model.MclnProject;
import mclnview.graphview.MclnGraphView;
import mclnview.graphview.MclnGraphModel;
import mclnview.graphview.MclnGraphViewDefaultProperties;
import mclnview.graphview.MclnPropertyView;
import sem.app.AppConstants;
import sem.appui.MclnControllerHolderPanel;

import java.awt.*;

/**
 * Created by Admin on 11/11/2017.
 */
public final class ControllersBuilder {

    static MclnGraphModel mcLnGraphModel;

    public static final void buildControllers() {
        MclnPropertyView.setBallSize(7);
        OperationController.getInstance();
        MclnDoubleRectangle mclnDoubleRectangle = new MclnDoubleRectangle(-10, 10, 20, 20);
        MclnModel currentMclnModel = MclnModel.createInstance("Default Mcln Model", "MG01", mclnDoubleRectangle);
        MclnProject defaultMclnProject = MclnProject.createInitialMclnProject(MclnProject.DEFAULT_PROJECT_NAME, mclnDoubleRectangle, currentMclnModel);
        mcLnGraphModel = new MclnGraphModel(defaultMclnProject);
        MclnGraphViewDefaultProperties mclnGraphViewDefaultProperties =
                new MclnGraphViewDefaultProperties(Color.BLACK, new Color(0x222222), new Color(0xD6EAFF),
                        new Color(0xFF4444), new Color(0xFF4444));
        MclnGraphView mclnGraphView = new MclnControllerGraphView(mcLnGraphModel, 15, 0,
                mclnGraphViewDefaultProperties);
        MclnControllerHolderPanel.getSingleton().setMclnGraphView(mclnGraphView);
    }

    public static final void buildMclnProject() {
        MclnProject mclnProject = SemMclnRetriever.retrieveMclnController(AppConstants.MCLN_CONTROLLER_CLASS_PATH);
        if (mclnProject == null) {
            return;
        }
        mcLnGraphModel.resetMclnModel(mclnProject);
        SemMclnController.createInstance(mclnProject);
    }
}
