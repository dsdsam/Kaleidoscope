package dsdsse.designspace;

import adf.csys.view.CSysView;
import adf.preferences.GroupChangeListener;
import dsdsse.app.AppStateModel;
import dsdsse.app.AppStatusPanel;
import dsdsse.app.DsdsseEnvironment;
import dsdsse.graphview.MclnGraphDesignerView;
import dsdsse.graphview.MclnGraphViewEditor;
import dsdsse.graphview.MclnViewEditorMouseListener;
import dsdsse.matrixview.MclnDesignerMatrixView;
import dsdsse.preferences.DsdsseUserPreference;
import dsdsse.preferences.GroupID;
import mcln.model.MclnDoubleRectangle;
import mcln.model.MclnModel;
import mcln.model.MclnProject;
import mclnview.graphview.MclnGraphModel;
import mclnview.graphview.MclnGraphViewDefaultProperties;
import mclnview.graphview.MclnPropertyView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Feb 3, 2013
 * Time: 10:12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DesignSpaceView extends JPanel {

    public static String MG01 = "MG01"; // mcln model graph

    private static DesignSpaceView designSpaceView = new DesignSpaceView();

    /**
     * @return
     */
    public static synchronized DesignSpaceView getInstance() {
        assert designSpaceView != null : "Design Space View is a singleton and not yet created";
        return designSpaceView;
    }

    //
    //  I n s t a n c e
    //

    private AppStatusPanel appStatusPanel;
    private DesignSpaceModel designSpaceModel;
    private MclnGraphModel mclnGraphModel;
    private MclnGraphDesignerView mclnGraphDesignerView;
    private MclnGraphViewEditor mclnGraphViewEditor;

    private MclnProject mclnProject;
    private MclnModel currentMclnModel;

    public MclnGraphModel getMclnGraphModel() {
        return mclnGraphModel;
    }

    //   L i s t e n e r s

    private final DesignSpaceModelListener designSpaceModelListener = new DesignSpaceModelListener() {

        @Override
        public void onMclnProjectReplaced(MclnProject newMclnProject) {
            mclnProject = newMclnProject;
            currentMclnModel = mclnProject.getCurrentMclnModel();
            mclnGraphDesignerView.regenerateGraphView();
            mclnGraphDesignerView.repaint();
        }
    };

    // Preference change listener: Property size and 3D/Plain toggling
    private final GroupChangeListener groupMcLNGraphChangeListener = (preference) -> {
        if (DsdsseUserPreference.PREF_PROPERTY_VIEW_SIZE_KEY.equals(preference)) {
            int radius = DsdsseUserPreference.getPropertyBallSize();
            MclnPropertyView.setBallSize(radius);
        } else {
            boolean status = DsdsseUserPreference.isPropertyView3D();
            MclnPropertyView.setDisplayMclnPropertyViewAs3DCircle(status);
        }
        mclnGraphDesignerView.regenerateGraphView();
        mclnGraphDesignerView.repaint();
    };

    // Preference change listener: grid, axis and project outline
    private final GroupChangeListener groupDesignSpaceViewChangeListener = (preference) -> {
        mclnGraphDesignerView.regenerateGraphView();
        mclnGraphDesignerView.repaint();
    };

    /**
     *
     */
    private DesignSpaceView() {
        super(new BorderLayout());
        this.designSpaceView = this;

        try {
            initDesignSpaceView();
            DsdsseUserPreference.getInstance().addGroupChangeListener(
                    DsdsseUserPreference.PREF_VIEW_STYLE_KEY, GroupID.GROUP1, groupMcLNGraphChangeListener);
            DsdsseUserPreference.getInstance().addGroupChangeListener(
                    DsdsseUserPreference.PREF_PROPERTY_VIEW_SIZE_KEY, GroupID.GROUP1, groupMcLNGraphChangeListener);

            DsdsseUserPreference.getInstance().addGroupChangeListener(
                    DsdsseUserPreference.PREF_GRID_VISIBLE_KEY, GroupID.GROUP2, groupDesignSpaceViewChangeListener);
            DsdsseUserPreference.getInstance().addGroupChangeListener(
                    DsdsseUserPreference.PREF_AXES_VISIBLE_KEY, GroupID.GROUP2, groupDesignSpaceViewChangeListener);
            DsdsseUserPreference.getInstance().addGroupChangeListener(
                    DsdsseUserPreference.PREF_PS_VISIBLE_KEY, GroupID.GROUP2, groupDesignSpaceViewChangeListener);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean isDesignSpaceEmpty() {
        return designSpaceModel.isDesignSpaceEmpty();
    }

    private void initDesignSpaceView() {

        appStatusPanel = new AppStatusPanel();
        add(appStatusPanel, BorderLayout.NORTH);

        // creating default empty MCLN project/model
        MclnDoubleRectangle mclnDoubleRectangle = new MclnDoubleRectangle(-15, 15, 30, 30);
        currentMclnModel = MclnModel.createInstance("Default Mcln Model", MG01, mclnDoubleRectangle);
        mclnProject = MclnProject.createInitialMclnProject(MclnProject.DEFAULT_PROJECT_NAME, mclnDoubleRectangle, currentMclnModel);
        mclnGraphModel = new MclnGraphModel(mclnProject);


        DesignSpaceModel.createInstance(mclnGraphModel, mclnProject);
        designSpaceModel = DesignSpaceModel.getInstance();

        MclnGraphViewDefaultProperties mclnGraphViewDefaultProperties =
                new MclnGraphViewDefaultProperties(Color.WHITE, Color.WHITE, new Color(0xEEEEEE),
                        Color.RED, Color.RED);
        mclnGraphDesignerView = new MclnGraphDesignerView(mclnGraphModel, 25, CSysView.DRAW_AXIS,
                mclnGraphViewDefaultProperties);


        mclnGraphModel.addMclnModeChangedListener(DesignStatusView.getInstance().getMclnModeChangedListener());

        designSpaceModel.addDesignSpaceModelListener(designSpaceModelListener);

        mclnGraphViewEditor = MclnGraphViewEditor.createInstance(designSpaceModel, mclnGraphModel, mclnGraphDesignerView);

        MclnViewEditorMouseListener mclnViewEditorMouseListener
                = new MclnViewEditorMouseListener(mclnGraphDesignerView, mclnGraphViewEditor);

        mclnGraphDesignerView.addMouseListener(mclnViewEditorMouseListener.getMouseAdapter());
        mclnGraphDesignerView.addMouseMotionListener(mclnViewEditorMouseListener.getMouseAdapter());

        DsdsseEnvironment.setMclnModel(mclnGraphModel);
        DsdsseEnvironment.setDesignSpaceView(this);
        DsdsseEnvironment.setMclnGraphViewEditor(mclnGraphViewEditor);

        DesignOrSimulationStatusPanelCardView designOrSimulationVisualizationCardView =
                DesignOrSimulationStatusPanelCardView.createInstance(this);

        MclnDesignerMatrixView mcLnDesignerMatrixView = MclnDesignerMatrixView.createInstance();
        DesignSpaceContentManager.createInstance(this, mclnGraphDesignerView,
                mcLnDesignerMatrixView, designOrSimulationVisualizationCardView);
        this.revalidate();
    }

    public DesignSpaceModel getDesignSpaceModel() {
        return designSpaceModel;
    }

    public MclnGraphDesignerView getMclnGraphDesignerView() {
        return mclnGraphDesignerView;
    }

    /**
     * P r i n t   P r e v i e w   L a y o u t
     */
    private void initPrintPreviewContent() {
        DesignSpaceContentManager.initMcLNPrintPreviewContent();
        AppStateModel.getInstance().setPrintToolIsActive(true);
        this.validate();
        repaint();
    }

    /**
     *
     */
    public void restoreDesignSpaceView() {
        DesignSpaceContentManager.resetToDesignSpaceView();
        AppStateModel.getInstance().setPrintToolIsActive(false);
        this.validate();
    }


    public final void regenerateGraphView() {
        mclnGraphDesignerView.regenerateGraphView();
        mclnGraphDesignerView.repaint();
    }

    public void switchToPrintPreviewContent() {
        try {
            initPrintPreviewContent();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean saveProject() {
        return designSpaceModel.saveProject();
    }

    public boolean saveProjectAs() {
        return designSpaceModel.saveProjectAs();
    }
}
