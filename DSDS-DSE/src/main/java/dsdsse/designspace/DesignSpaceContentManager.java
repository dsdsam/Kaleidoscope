package dsdsse.designspace;

import dsdsse.app.DsdsseEnvironment;
import dsdsse.printing.MclnPrintPreviewPanel;
import dsdsse.graphview.MclnGraphDesignerView;
import dsdsse.printing.PrintViewButtonPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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
                               DesignOrSimulationStatusPanelCardView designOrSimulationVisualizationCardView) {
        if (designSpaceContentManager != null) {
            throw new RuntimeException("DesignSpaceContentManager instance already created.");
        }
        designSpaceContentManager = new DesignSpaceContentManager(designSpaceView, mclnGraphDesignerView,
                designOrSimulationVisualizationCardView);
    }

    /**
     * @param printViewButtonPanel
     * @param mclnPrintPreviewPanel
     */
    static void initMcLNPrintPreviewContent(PrintViewButtonPanel printViewButtonPanel,
                                            MclnPrintPreviewPanel mclnPrintPreviewPanel) {
        designSpaceContentManager.initMcLNPagePreviewContent(printViewButtonPanel, mclnPrintPreviewPanel);
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
        int upperPanelHeight = splitPaneHeight - (DesignStatusView.PREFERRED_HEIGHT + 3);
        designSpaceContentManager.splitPane.setDividerLocation(upperPanelHeight);
        designSpaceContentManager.splitPane.setEnabled(false);
    }

    public static final void restoreSimulationStatusViewPanelHeight(int executionHistoryPanelHeight) {
        designSpaceContentManager.splitPane.setEnabled(true);
        if (savedSimulationStatusViewPanelHeight > 0) {
            int splitPaneHeight = designSpaceContentManager.splitPane.getHeight();
            int upperPanelHeight = splitPaneHeight - (savedSimulationStatusViewPanelHeight + 3);
            designSpaceContentManager.splitPane.setDividerLocation(upperPanelHeight);
        } else {
            int splitPaneHeight = designSpaceContentManager.splitPane.getHeight();
            int upperPanelHeight = splitPaneHeight - (executionHistoryPanelHeight + 3);
            designSpaceContentManager.splitPane.setDividerLocation(upperPanelHeight);
        }

    }

    public static final void setSimulationStatusViewPanelHeight(int historyPanelHeight) {
        savedSimulationStatusViewPanelHeight = -1;
        int splitPaneHeight = designSpaceContentManager.splitPane.getHeight();
        int upperPanelHeight = (splitPaneHeight - historyPanelHeight + 3);
        designSpaceContentManager.splitPane.setDividerLocation(upperPanelHeight);
//        setSimulationStatusViewPanelResizable();
    }


    // I n s t a n c e

    private final DesignSpaceView designSpaceView;
    private final MclnGraphDesignerView mclnGraphDesignerView;
    private final DesignOrSimulationStatusPanelCardView designOrSimulationVisualizationCardView;
    private JPanel designSpaceSidePanelHolder;
    private JComponent westPanel;
    private JComponent centerPanel;
    private JPanel eastPanel;
    private boolean showingDesignSpace;
    private JSplitPane splitPane;

    private DesignSpaceContentManager(DesignSpaceView designSpaceView, MclnGraphDesignerView mclnGraphDesignerView,
                                      DesignOrSimulationStatusPanelCardView designOrSimulationVisualizationCardView) {
        this.designSpaceView = designSpaceView;
        this.mclnGraphDesignerView = mclnGraphDesignerView;
        this.designOrSimulationVisualizationCardView = designOrSimulationVisualizationCardView;
        designSpaceSidePanelHolder = initDesignSpaceView(mclnGraphDesignerView, designOrSimulationVisualizationCardView);
    }


    /**
     * @param mclnGraphDesignerView
     * @param cardLayoutPanel
     */
    private JPanel initDesignSpaceView(MclnGraphDesignerView mclnGraphDesignerView, JPanel cardLayoutPanel) {

        JPanel mclnGraphViewHolderPanel = new JPanel(new BorderLayout());
        mclnGraphViewHolderPanel.setBackground(PAGE_PANEL_BACKGROUND_COLOR);
        Border marginBorder = new EmptyBorder(15, 15, 15, 15);
        Border lineBorder = new LineBorder(Color.GRAY);
        mclnGraphViewHolderPanel.setBorder(BorderFactory.createCompoundBorder(marginBorder, lineBorder));
        mclnGraphViewHolderPanel.add(mclnGraphDesignerView, BorderLayout.CENTER);

        JPanel designSpaceViewPanel = new JPanel(new BorderLayout());
        designSpaceViewPanel.setOpaque(true);
        designSpaceViewPanel.setBackground(VIEW_BACKGROUND_COLOR);
        Border designSpacePanelSidesBorder = new EmptyBorder(TOP_GAP, SIDE_PANEL_WIDTH, BOTTOM_GAP, SIDE_PANEL_WIDTH);
//        Border designSpacePaneTopBorder = new MatteBorder(1, 0, 0, 0, Color.DARK_GRAY);
//        Border compoundBorder = BorderFactory.createCompoundBorder(designSpacePaneTopBorder, designSpacePanelSidesBorder);
        designSpaceViewPanel.setBorder(designSpacePanelSidesBorder);
//        designSpaceViewPanel.setBorder(null);
        designSpaceViewPanel.add(mclnGraphViewHolderPanel, BorderLayout.CENTER);

        JPanel designSpaceSidePanelHolder = new JPanel(new BorderLayout());
        designSpaceSidePanelHolder.add(designSpaceViewPanel, BorderLayout.CENTER);

        splitPane = initSplitPanel(designSpaceSidePanelHolder, cardLayoutPanel);
        splitPane.setBorder(null);
        centerPanel = new JPanel(new BorderLayout());
//        centerPanel.setBorder(null);
        centerPanel.add(splitPane, BorderLayout.CENTER);
//        designSpaceView.setBorder(null);
        designSpaceView.add(centerPanel, BorderLayout.CENTER);
        showingDesignSpace = true;
        return designSpaceSidePanelHolder;
    }

    /**
     * @param designSpaceSidePanelHolder
     * @param cardLayoutPanel
     * @return
     */
    private JSplitPane initSplitPanel(JPanel designSpaceSidePanelHolder, JPanel cardLayoutPanel) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, designSpaceSidePanelHolder, cardLayoutPanel);
        splitPane.setResizeWeight(1);

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(3);

        JFrame frame = DsdsseEnvironment.getMainFrame();
        int frameHeight = frame.getHeight();
        int defaultDividerLocation = frameHeight - 210;
        if (frameHeight < 520) {
            defaultDividerLocation = frameHeight - 180;
        }
//        splitPane.setDividerLocation(defaultDividerLocation);

        splitPane.setContinuousLayout(true);
        splitPane.validate();
        return splitPane;
    }

    //
    //   Initializing McLN Print Preview Content
    //

    private void initMcLNPagePreviewContent(PrintViewButtonPanel printViewButtonPanel,
                                            MclnPrintPreviewPanel mclnPrintPreviewPanel) {
        centerPanel.removeAll();

        mclnGraphDesignerView.setBorder(new MatteBorder(1, 1, 1, 1, Color.GRAY));

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

        mclnGraphDesignerView.setBorder(null);

        JPanel mclnGraphViewHolderPanel = new JPanel(new BorderLayout());
        mclnGraphViewHolderPanel.setBackground(PAGE_PANEL_BACKGROUND_COLOR);
        Border marginBorder = new EmptyBorder(15, 15, 15, 15);
        Border lineBorder = new LineBorder(Color.GRAY);
        mclnGraphViewHolderPanel.setBorder(BorderFactory.createCompoundBorder(marginBorder, lineBorder));
        mclnGraphViewHolderPanel.add(mclnGraphDesignerView, BorderLayout.CENTER);

        JPanel designSpaceViewPanel = new JPanel(new BorderLayout());
        designSpaceViewPanel.setOpaque(true);
        designSpaceViewPanel.setBackground(VIEW_BACKGROUND_COLOR);
        Border designSpacePanelSidesBorder = new EmptyBorder(TOP_GAP, SIDE_PANEL_WIDTH, BOTTOM_GAP, SIDE_PANEL_WIDTH);
        Border designSpacePaneTopBorder = new MatteBorder(1, 0, 0, 0, Color.DARK_GRAY);
        Border compoundBorder = BorderFactory.createCompoundBorder(designSpacePaneTopBorder, designSpacePanelSidesBorder);
        designSpaceViewPanel.setBorder(designSpacePanelSidesBorder);
        designSpaceViewPanel.add(mclnGraphViewHolderPanel, BorderLayout.CENTER);

        designSpaceSidePanelHolder = new JPanel(new BorderLayout());
        designSpaceSidePanelHolder.add(designSpaceViewPanel, BorderLayout.CENTER);

        // restoring Design Status or Simulation History panel to its height
        //
        int height = splitPane.getHeight();
//        int cardLayoutPanelHeight = designOrSimulationVisualizationCardView.getHeight();
        int cardLayoutPanelHeight = designOrSimulationVisualizationCardView.getCurrentCardViewHeight();
        int actualHeight = height - cardLayoutPanelHeight;
        if (actualHeight < 0) {
            actualHeight = height;
        }

        splitPane = initSplitPanel(designSpaceSidePanelHolder, designOrSimulationVisualizationCardView);
        splitPane.setDividerLocation(actualHeight);

        // restoring East panel if existed
        if (eastPanel != null) {
            designSpaceSidePanelHolder.add(eastPanel, BorderLayout.EAST);
        }

        centerPanel.add(splitPane, BorderLayout.CENTER);

        designSpaceSidePanelHolder.validate();
        centerPanel.validate();
        centerPanel.repaint();

        showingDesignSpace = true;
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
