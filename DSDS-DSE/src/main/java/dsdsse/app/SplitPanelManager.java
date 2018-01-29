package dsdsse.app;

import dsdsse.graphview.MclnGraphDesignerView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 8/22/2016.
 */
public class SplitPanelManager extends JPanel {

    private static SplitPanelManager splitPanelManager;

//    public static SplitPanelManager createInstance( JComponent splitPanelHolder, MclnGraphView mclnGraphView) {
//        if (splitPanelManager != null) {
//            throw new RuntimeException("SplitPanelManager instance already created.");
//        }
//        splitPanelManager = new SplitPanelManager(splitPanelHolder, mclnGraphView);
//        return splitPanelManager;
//    }

//    public static SplitPanelManager getInstance() {
//        if (splitPanelManager == null) {
//            throw new RuntimeException("SplitPanelManager has not yet been created.");
//        }
//        return splitPanelManager;
//    }

    //
    //   I n s t a n c e
    //

    private final JComponent splitPanelHolder;
    private final MclnGraphDesignerView mclnGraphDesignerView;
    private JComponent westPanel;
    private JComponent centerPanel;
    private JComponent eastPanel;

    private SplitPanelManager(JComponent splitPanelHolder, MclnGraphDesignerView mclnGraphDesignerView) {
        super(new BorderLayout());
        this.splitPanelHolder = splitPanelHolder;
        this.mclnGraphDesignerView = mclnGraphDesignerView;
    }

//    public final void repaintNeighbours() {
//        if (mclnGraphView != null) {
//            mclnGraphView.regenerateGraphView();
//            mclnGraphView.repaint();
//        }
//    }

    public void setWestPanel(JPanel westPanel) {
        this.westPanel = westPanel;
        if (westPanel != null) {
            add(westPanel, BorderLayout.WEST);
        }
    }

    public void setCenterPanel(JComponent centerPanel) {
        this.centerPanel = centerPanel;
        if (centerPanel != null) {
            add(centerPanel, BorderLayout.CENTER);
        }
    }

    public void setEastPanel(JPanel eastPanel) {
        if (this.eastPanel != null) {
            remove(this.eastPanel);
        }
        this.eastPanel = eastPanel;
        if (eastPanel != null) {
            add(eastPanel, BorderLayout.EAST);
        }
        splitPanelHolder.revalidate();
        splitPanelHolder.repaint();
    }

    public void hideSetupPanel() {
        if (eastPanel != null) {
            eastPanel.setVisible(false);
        }
    }

    public void showEastPanel() {
        if (eastPanel != null) {
            eastPanel.setVisible(true);
        }
    }
}
