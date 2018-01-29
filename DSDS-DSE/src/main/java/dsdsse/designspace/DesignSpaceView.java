package dsdsse.designspace;

import adf.csys.view.CSysView;
import adf.preferences.GroupChangeListener;
import dsdsse.app.AppStateModel;
import dsdsse.app.AppStatusPanel;
import dsdsse.app.DsdsseEnvironment;
import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import dsdsse.graphview.MclnGraphDesignerView;
import dsdsse.graphview.MclnGraphViewEditor;
import dsdsse.graphview.MclnPropertyView;
import dsdsse.graphview.MclnViewEditorMouseListener;
import dsdsse.preferences.DsdsseUserPreference;
import dsdsse.preferences.GroupID;
import dsdsse.printing.MclnPrintPreviewPanel;
import dsdsse.printing.PrintViewButtonPanel;
import mcln.model.MclnModel;
import mcln.model.MclnProject;

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

    private static final Color VIEW_BACKGROUND_COLOR = new Color(0xACACAC);
    private static final int TOP_PANEL_HEIGHT = 5;
    private static final int SIDE_PANEL_WIDTH = 100;

    private static final Color PAGE_PANEL_BACKGROUND_COLOR = Color.WHITE;

    public static String DS00 = "DS00"; // desine space
    public static String MG01 = "MG01"; // mcln model graph

    public static final String EDITOR_VIEW = "EDITOR_VIEW";
    public static final String SIMULATOR_VIEW = "SIMULATOR_VIEW";

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

    //   L i s t e n e r s

    private final DesignSpaceModelListener designSpaceModelListener = new DesignSpaceModelListener() {

        @Override
        public void onMclnProjectReplaced(MclnProject newMclnProject) {
            mclnProject = newMclnProject;
            currentMclnModel = mclnProject.getCurrentMclnModel();
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
            t.printStackTrace(System.out);
        }
    }

    public boolean isDesignSpaceEmpty() {
        return designSpaceModel.isDesignSpaceEmpty();
    }

    private void initDesignSpaceView() {

        appStatusPanel = new AppStatusPanel();
        add(appStatusPanel, BorderLayout.NORTH);

        // creating empty MCLN project/model
        currentMclnModel = MclnModel.createInstance("Default Mcln Model", MG01, -15, 15, 30, 30);
        mclnProject = MclnProject.createInitialMclnProject(MclnProject.DEFAULT_PROJECT_NAME, currentMclnModel);
        mclnGraphModel = new MclnGraphModel(mclnProject);


        DesignSpaceModel.createInstance(mclnGraphModel, mclnProject);
        designSpaceModel = DesignSpaceModel.getInstance();

        mclnGraphDesignerView = new MclnGraphDesignerView(mclnGraphModel, 25, CSysView.DRAW_AXIS);


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
        DesignSpaceContentManager.createInstance(this, mclnGraphDesignerView, designOrSimulationVisualizationCardView);
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
        MclnPrintPreviewPanel mclnPrintPreviewPanel = new MclnPrintPreviewPanel(designSpaceModel, mclnGraphDesignerView);
        PrintViewButtonPanel printViewButtonPanel = new PrintViewButtonPanel(mclnPrintPreviewPanel);
        DesignSpaceContentManager.initMcLNPrintPreviewContent(printViewButtonPanel, mclnPrintPreviewPanel);
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
            t.printStackTrace(System.out);
        }
    }

    public boolean saveProject() {
        return designSpaceModel.saveProject();
    }

    public boolean saveProjectAs() {
        return designSpaceModel.saveProjectAs();
    }
}
