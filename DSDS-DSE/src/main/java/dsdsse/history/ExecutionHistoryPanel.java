package dsdsse.history;


import adf.preferences.GroupChangeListener;
import dsdsse.app.DsdsseEnvironment;
import dsdsse.designspace.DesignSpaceModel;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.graphview.MclnGraphDesignerView;
import dsdsse.graphview.MclnPropertyView;
import dsdsse.preferences.DsdsseUserPreference;
import dsdsse.preferences.GroupID;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 2/13/14
 * Time: 8:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionHistoryPanel extends JPanel {

    static int stateDotSize;

    static {
        int propertyStateDisplaySize = DsdsseUserPreference.getPropertyStateDisplaySize();
        SimulationTracePanel.stateDotSize = propertyStateDisplaySize;
    }

    public static final Color LINE_SEPARATOR_COLOR = new Color(0xD0D0D0);

    private static int ROW_WIDTH = 1;
    public static int ROW_HEIGHT = 16;
    public static Dimension ROW_SIZE = new Dimension(ROW_WIDTH, ROW_HEIGHT);

    private static ExecutionHistoryPanel executionHistoryPanel;

    public static ExecutionHistoryPanel createInstance(DesignSpaceView designSpaceView) {
        assert executionHistoryPanel == null :
                "The instance of ExecutionHistoryPanel already exists";
        executionHistoryPanel = new ExecutionHistoryPanel(designSpaceView);
        return executionHistoryPanel;
    }

    public static ExecutionHistoryPanel getInstance() {
        assert executionHistoryPanel != null :
                "The instance of ExecutionHistoryPanel does not yet created";
        return executionHistoryPanel;
    }

    private static final GroupChangeListener groupChangeListener = (preference) -> {
        int propertyStateDisplaySize = DsdsseUserPreference.getPropertyStateDisplaySize();
        if (executionHistoryPanel != null) {
            SimulationTracePanel.stateDotSize = propertyStateDisplaySize;
            executionHistoryPanel.repaint();
        }
    };

    static {
        DsdsseUserPreference.getInstance().addGroupChangeListener(
                DsdsseUserPreference.PREF_STATE_DISPLAY_SIZE_KEY, GroupID.TRACE_LOG, groupChangeListener);
    }


    private final DesignSpaceView designSpaceView;
    private final DesignSpaceModel designSpaceModel;
    private final MclnGraphDesignerView mclnGraphDesignerView;

    private final ExecutionHistoryControlPanel executionHistoryControlPanel;
    private final ContentSelectionPanel contentSelectionPanel;
    private final TraceLogHolderPanel traceLogHolderPanel;

    private boolean executionHistoryPanelVisible;

    ExecutionHistoryPanel(DesignSpaceView designSpaceView) {
        super(new BorderLayout());

        this.designSpaceView = designSpaceView;
        designSpaceModel = designSpaceView.getDesignSpaceModel();
        this.mclnGraphDesignerView = designSpaceView.getMclnGraphDesignerView();

        executionHistoryControlPanel = new ExecutionHistoryControlPanel(this);
        contentSelectionPanel = new ContentSelectionPanel(this, designSpaceView);
        traceLogHolderPanel = new TraceLogHolderPanel(this);

        traceLogHolderPanel.setScrollBarModel(contentSelectionPanel.getScrollBarModel());

        initLayout();
    }

    /**
     *
     */
    private void initLayout() {
        add(executionHistoryControlPanel, BorderLayout.WEST);

        JFrame frame = DsdsseEnvironment.getMainFrame();
        int frameWidth = frame.getWidth();
        JSplitPane historySplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, contentSelectionPanel, traceLogHolderPanel);
//        historySplitPane.setBorder(null);
        historySplitPane.setResizeWeight(0);
        historySplitPane.setOneTouchExpandable(true);
        historySplitPane.setDividerSize(2);
//        historySplitPane.setDividerLocation((frameWidth * 47) / 100);
        historySplitPane.setDividerLocation(400);
        historySplitPane.setContinuousLayout(true);
        historySplitPane.validate();

        add(historySplitPane, BorderLayout.CENTER);
    }

    /**
     * Called to clear both: Property List and Trace Log panels
     */
    public void clearUpOnModelErased() {
        contentSelectionPanel.clearSelectionPanel();
        clearTraceLog();
    }

    public void simulationStepExecuted() {
        traceLogHolderPanel.simulationStepExecuted();
        contentSelectionPanel.updateStatementsUpOnModelStateChanged();
    }

    public void simulationExecutionReset() {
        clearTraceLog();
        contentSelectionPanel.updateStatementsUpOnModelStateChanged();
    }

    /**
     *
     */
    public void historyPresented(boolean executionHistoryPanelVisible) {
        this.executionHistoryPanelVisible = executionHistoryPanelVisible;
        if (!executionHistoryPanelVisible) {
            return;
        }

        boolean projectModifiedSinceTraceHistoryStarted = designSpaceModel.isProjectModifiedSinceTraceHistoryStarted();
        designSpaceModel.clearModificationFlag();
        if (projectModifiedSinceTraceHistoryStarted) {
            contentSelectionPanel.resetOnProjectModified();
        }
    }

    public int getViewPreferredHeight() {
        return contentSelectionPanel.getViewPreferredHeight();
    }

    /**
     *
     */
    void onSwitchAllSelectedButtonClicked() {
        contentSelectionPanel.showAllProperties();
        repaint();
    }

    void onSwitchToSelectedPropertiesButtonClicked() {
        contentSelectionPanel.hideIfNotSelected();
        repaint();
    }

    /**
     * @param presentedPropertyList
     */
    void setContents(List<MclnPropertyView> presentedPropertyList) {
        traceLogHolderPanel.setContents(presentedPropertyList);
    }

    void clearTraceLog() {
        List<MclnPropertyView> propertyViewList = mclnGraphDesignerView.getPropertyViewList();
        int nOfAllRows = propertyViewList.size();
        for (int i = 0; i < nOfAllRows; i++) {
            MclnPropertyView mclnPropertyView = propertyViewList.get(i);
            mclnPropertyView.clearHistory();
        }
        traceLogHolderPanel.traceLogCleared();
    }
}
