package dsdsse.designspace;

import dsdsse.graphview.MclnGraphDesignerView;
import dsdsse.matrixview.MclnDesignerMatrixView;
import dsdsse.printing.MclnPrintPreviewPanel;
import dsdsse.printing.PrintViewButtonPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created by Admin on 9/16/2016.
 */
public class DesignSpaceContentManager {

    private static final Color VIEW_BACKGROUND_COLOR = Color.LIGHT_GRAY;//  new Color(0xACACAC);
    private static final int TOP_GAP = 5;
    private static final int BOTTOM_GAP = 6;
    private static final int SIDE_PANEL_WIDTH = 10;
    private static final int SPLIT_PANEL_BAR_HEIGHT = 5;

    private static final Color PAGE_PANEL_BACKGROUND_COLOR = Color.WHITE;

    public static DesignSpaceContentManager designSpaceContentManager;

    public Rectangle getPanelBounds() {
        return designSpaceSidePanelHolder.getBounds();
    }

    public JPanel getPanel() {
        return designSpaceSidePanelHolder;
    }

    /**
     * @param designSpaceView
     * @param mclnGraphDesignerView
     * @param designOrSimulationVisualizationCardView
     */
    static void createInstance(DesignSpaceView designSpaceView, MclnGraphDesignerView mclnGraphDesignerView,
                               MclnDesignerMatrixView mcLnDesignerMatrixView,
                               DesignOrSimulationStatusPanelCardView designOrSimulationVisualizationCardView) {
        if (designSpaceContentManager != null) {
            throw new RuntimeException("DesignSpaceContentManager instance already created.");
        }
        designSpaceContentManager = new DesignSpaceContentManager(designSpaceView, mclnGraphDesignerView,
                mcLnDesignerMatrixView, designOrSimulationVisualizationCardView);
    }

    static void initMcLNPrintPreviewContent() {
        designSpaceContentManager.initMcLNPagePreviewContent();
    }

    public static boolean isPrintPreviewContentWestPanel() {
        return designSpaceContentManager.isPrintPreviewContentInserted();
    }

    public static boolean isThisComponentEastPanel(JComponent component) {
        return designSpaceContentManager.isThisComponentInserted(component);
    }

    /**
     *
     */
    static void resetToDesignSpaceView() {
        designSpaceContentManager.restoreDesignSpaceView();
    }

    /**
     * @param eastPanel
     */
    public static JPanel setEastPanel(JPanel eastPanel) {
        if (designSpaceContentManager == null) {
            throw new RuntimeException("DesignSpaceContentManager has not yet been created.");
        }
        return designSpaceContentManager.insertEastPanel(eastPanel);
    }

    public static final JPanel clearEastSideSpace() {
        if (designSpaceContentManager == null) {
            throw new RuntimeException("DesignSpaceContentManager has not yet been created.");
        }
        return designSpaceContentManager.removeEastPanel(null);
    }

    /**
     *
     */
    public static final JPanel hideEastPanel(JComponent component) {
        if (designSpaceContentManager == null) {
            throw new RuntimeException("DesignSpaceContentManager has not yet been created.");
        }
        return designSpaceContentManager.removeEastPanel(component);
    }

    private static int savedSimulationStatusViewPanelHeight = -1;

    public static void setSavedSimulationStatusViewPanelHeight(int savedSimulationStatusViewPanelHeight) {
        DesignSpaceContentManager.savedSimulationStatusViewPanelHeight = savedSimulationStatusViewPanelHeight;
    }

    public static final void setDesignStatusViewPanelHeight() {
        int splitPaneHeight = designSpaceContentManager.splitPane.getHeight();
        int upperPanelHeight = splitPaneHeight - (DesignStatusView.PREFERRED_HEIGHT + SPLIT_PANEL_BAR_HEIGHT);
        designSpaceContentManager.splitPane.setDividerLocation(upperPanelHeight);
    }

    public static final void restoreSimulationStatusViewPanelHeight(int executionHistoryPanelHeight) {
        designSpaceContentManager.splitPane.setEnabled(true);
        if (savedSimulationStatusViewPanelHeight > 0) {
            int splitPaneHeight = designSpaceContentManager.splitPane.getHeight();
            int upperPanelHeight = splitPaneHeight - (savedSimulationStatusViewPanelHeight + SPLIT_PANEL_BAR_HEIGHT);
            designSpaceContentManager.splitPane.setDividerLocation(upperPanelHeight);
        } else {
            int splitPaneHeight = designSpaceContentManager.splitPane.getHeight();
            int upperPanelHeight = splitPaneHeight - (executionHistoryPanelHeight + SPLIT_PANEL_BAR_HEIGHT);
            designSpaceContentManager.splitPane.setDividerLocation(upperPanelHeight);
        }

    }

    public static final void setSimulationStatusViewPanelHeight(int historyPanelHeight) {
        savedSimulationStatusViewPanelHeight = -1;
        int splitPaneHeight = designSpaceContentManager.splitPane.getHeight();
        int upperPanelHeight = (splitPaneHeight - historyPanelHeight + SPLIT_PANEL_BAR_HEIGHT);
        designSpaceContentManager.splitPane.setDividerLocation(upperPanelHeight);
    }


    // I n s t a n c e

    private final DesignSpaceView designSpaceView;
    private final MclnGraphDesignerView mclnGraphDesignerView;
    private final MclnDesignerMatrixView mcLnDesignerMatrixView;
    private final DesignOrSimulationStatusPanelCardView designOrSimulationVisualizationCardView;
    private JPanel designSpaceSidePanelHolder;
    private JComponent westPanel;
    private JComponent centerPanel;
    private JPanel eastPanel;
    private boolean showingDesignSpace;
    private JSplitPane splitPane;

    private DesignSpaceContentManager(DesignSpaceView designSpaceView, MclnGraphDesignerView mclnGraphDesignerView,
                                      MclnDesignerMatrixView mcLnDesignerMatrixView,
                                      DesignOrSimulationStatusPanelCardView designOrSimulationVisualizationCardView) {
        this.designSpaceView = designSpaceView;
        this.mclnGraphDesignerView = mclnGraphDesignerView;
        this.mcLnDesignerMatrixView = mcLnDesignerMatrixView;
        this.designOrSimulationVisualizationCardView = designOrSimulationVisualizationCardView;
        designSpaceSidePanelHolder = initDesignSpaceView(mclnGraphDesignerView,
                mcLnDesignerMatrixView, designOrSimulationVisualizationCardView);
    }


    /**
     * @param mclnGraphDesignerView
     * @param cardLayoutPanel
     */
    private JPanel initDesignSpaceView(MclnGraphDesignerView mclnGraphDesignerView,
                                       MclnDesignerMatrixView mcLnDesignerMatrixView,
                                       JPanel cardLayoutPanel) {

        JPanel graphViewHolderPanel = new JPanel(new BorderLayout());
        graphViewHolderPanel.setOpaque(true);
        graphViewHolderPanel.setBackground(VIEW_BACKGROUND_COLOR);
        Border designSpacePanelSidesBorder = new EmptyBorder(TOP_GAP, SIDE_PANEL_WIDTH, BOTTOM_GAP, SIDE_PANEL_WIDTH);
        Border marginBorder = new MatteBorder(15, 15, 15, 15, Color.WHITE);
        Border border = BorderFactory.createCompoundBorder(designSpacePanelSidesBorder, marginBorder);
        graphViewHolderPanel.setBorder(border);
        graphViewHolderPanel.add(mclnGraphDesignerView, BorderLayout.CENTER);

        designSpaceGraphOrMatrixViewCardPanel = DesignSpaceGraphOrMatrixViewCardPanel.createInstance(
                graphViewHolderPanel, mclnGraphDesignerView, mcLnDesignerMatrixView);

        JPanel designSpaceSidePanelHolder = new JPanel(new BorderLayout());
        designSpaceSidePanelHolder.add(designSpaceGraphOrMatrixViewCardPanel, BorderLayout.CENTER);

        splitPane = initSplitPanel(designSpaceSidePanelHolder, cardLayoutPanel);
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(splitPane, BorderLayout.CENTER);
        designSpaceView.add(centerPanel, BorderLayout.CENTER);
        showingDesignSpace = true;
        return designSpaceSidePanelHolder;
    }

    DesignSpaceGraphOrMatrixViewCardPanel designSpaceGraphOrMatrixViewCardPanel;

    /**
     * @param designSpaceSidePanelHolder
     * @param cardLayoutPanel
     * @return
     */
    private JSplitPane initSplitPanel(JPanel designSpaceSidePanelHolder, JPanel cardLayoutPanel) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, designSpaceSidePanelHolder, cardLayoutPanel);
        splitPane.setBorder(null);
        splitPane.setResizeWeight(1);

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(SPLIT_PANEL_BAR_HEIGHT);

        splitPane.setContinuousLayout(true);
        splitPane.validate();
        return splitPane;
    }

    //
    //   Initializing McLN Print Preview Content
    //

    private void initMcLNPagePreviewContent() {
        MclnPrintPreviewPanel mclnPrintPreviewPanel = new MclnPrintPreviewPanel(DesignSpaceModel.getInstance(),
                designSpaceGraphOrMatrixViewCardPanel.getCurrentView());
        PrintViewButtonPanel printViewButtonPanel = new PrintViewButtonPanel(mclnPrintPreviewPanel);

        centerPanel.removeAll();

        JPanel printPreviewHolderPanel = new JPanel(new GridBagLayout());
        printPreviewHolderPanel.setOpaque(true);
        printPreviewHolderPanel.setBackground(Color.LIGHT_GRAY);
        printPreviewHolderPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        printPreviewHolderPanel.add(mclnPrintPreviewPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        centerPanel.add(printViewButtonPanel, BorderLayout.WEST);
        centerPanel.add(printPreviewHolderPanel, BorderLayout.CENTER);
        if (eastPanel != null) {
            centerPanel.add(eastPanel, BorderLayout.EAST);
        }
        centerPanel.validate();
        centerPanel.repaint();

        showingDesignSpace = false;
    }

    private boolean isPrintPreviewContentInserted() {
        return showingDesignSpace == false;
    }

    private boolean isThisComponentInserted(JComponent component) {
        return component == eastPanel;
    }

    //
    //   Called when West panel (e.g. Print Preview panel) is closed
    //   Reset to Design Space View
    //

    public void restoreDesignSpaceView() {

        centerPanel.removeAll();

        JPanel graphViewHolderPanel = new JPanel(new BorderLayout());
        graphViewHolderPanel.setOpaque(true);
        graphViewHolderPanel.setBackground(VIEW_BACKGROUND_COLOR);
        Border designSpacePanelSidesBorder = new EmptyBorder(TOP_GAP, SIDE_PANEL_WIDTH, BOTTOM_GAP, SIDE_PANEL_WIDTH);
        Border marginBorder = new MatteBorder(15, 15, 15, 15, Color.WHITE);
        Border border = BorderFactory.createCompoundBorder(designSpacePanelSidesBorder, marginBorder);
        graphViewHolderPanel.setBorder(border);
        graphViewHolderPanel.add(mclnGraphDesignerView, BorderLayout.CENTER);

        boolean currentViewIsMclnGraphDesignerView =
                designSpaceGraphOrMatrixViewCardPanel.isCurrentViewIsMclnGraphDesignerView();

        designSpaceGraphOrMatrixViewCardPanel = DesignSpaceGraphOrMatrixViewCardPanel.createInstance(
                graphViewHolderPanel, mclnGraphDesignerView, mcLnDesignerMatrixView);

        designSpaceGraphOrMatrixViewCardPanel.setCurrentView(currentViewIsMclnGraphDesignerView);

        designSpaceSidePanelHolder = new JPanel(new BorderLayout());
        designSpaceSidePanelHolder.add(designSpaceGraphOrMatrixViewCardPanel, BorderLayout.CENTER);
        //
        // restoring Design Status or Simulation History panel to its height
        //
        int height = splitPane.getHeight();
        int cardLayoutPanelHeight = designOrSimulationVisualizationCardView.getCurrentCardViewHeight();
        int actualHeight = height - cardLayoutPanelHeight;
        if (actualHeight < 0) {
            actualHeight = height;
        }
        splitPane = initSplitPanel(designSpaceSidePanelHolder, designOrSimulationVisualizationCardView);
        splitPane.setDividerLocation(actualHeight);

        centerPanel.add(splitPane, BorderLayout.CENTER);

        showingDesignSpace = true;

        // restoring East panel if existed
        if (eastPanel != null) {
            insertEastPanel(eastPanel);
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    /**
     * @param eastPanel
     */
    private final JPanel insertEastPanel(JPanel eastPanel) {
        if (this.eastPanel != null) {
            centerPanel.remove(this.eastPanel);
            designSpaceSidePanelHolder.remove(this.eastPanel);
        }
        JPanel removedPanel = this.eastPanel;
        this.eastPanel = eastPanel;

        if (showingDesignSpace) {
            designSpaceSidePanelHolder.add(eastPanel, BorderLayout.EAST);
        } else {
            if (eastPanel != null) {
                centerPanel.add(eastPanel, BorderLayout.EAST);
            }
        }

        designSpaceView.revalidate();
        designSpaceView.repaint();
        return removedPanel;
    }

    /**
     *
     */
    private final JPanel removeEastPanel(JComponent eastPanelToRemove) {
        if (eastPanel == null || (eastPanelToRemove != null && eastPanel != eastPanelToRemove)) {
            return null;
        }
        if (showingDesignSpace) {
            designSpaceSidePanelHolder.remove(eastPanel);
            mclnGraphDesignerView.regenerateGraphView();
        } else {
            centerPanel.remove(eastPanel);
        }
        JPanel removedPanel = eastPanel;
        eastPanel = null;
        designSpaceView.validate();
        designSpaceView.repaint();
        return removedPanel;
    }
}
