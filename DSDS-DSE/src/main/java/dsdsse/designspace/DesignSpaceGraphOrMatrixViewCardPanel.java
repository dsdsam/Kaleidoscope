package dsdsse.designspace;

import dsdsse.graphview.MclnGraphDesignerView;

import javax.swing.*;
import java.awt.*;

public class DesignSpaceGraphOrMatrixViewCardPanel extends JPanel {

    final static String MCLN_GRAPH_DESIGNER_VIEW_PANEL = "Mcln graph designer view panel";
    final static String MCLN_GRAPH_MATRIX_VIEW_PANEL = "Mcln graph matrix view panel";

    private static DesignSpaceGraphOrMatrixViewCardPanel designSpaceGraphOrMatrixViewCardPanel;

    public static DesignSpaceGraphOrMatrixViewCardPanel createInstance(JPanel mclnGraphRepresentationPanel,
                                                                       MclnGraphDesignerView mclnGraphDesignerView,
                                                                       JPanel mcLnGraphMatrixRepresentationPanel) {
        return designSpaceGraphOrMatrixViewCardPanel = new DesignSpaceGraphOrMatrixViewCardPanel(
                mclnGraphRepresentationPanel, mclnGraphDesignerView, mcLnGraphMatrixRepresentationPanel);
    }

    public static DesignSpaceGraphOrMatrixViewCardPanel getInstance() {
        assert designSpaceGraphOrMatrixViewCardPanel != null :
                "The instance of DesignerGraphViewOrMatrixViewCardPanel was not yet created";
        return designSpaceGraphOrMatrixViewCardPanel;
    }

    //  I n s t a n c e

    MclnGraphDesignerView mclnGraphDesignerView;
    private final CardLayout cardLayout;
    private boolean currentViewIsMclnGraphDesignerView;

    private DesignSpaceGraphOrMatrixViewCardPanel(JPanel mclnGraphRepresentationPanel,
                                                  MclnGraphDesignerView mclnGraphDesignerView,
                                                  JPanel mcLnGraphMatrixRepresentationPanel) {
        this.mclnGraphDesignerView = mclnGraphDesignerView;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(mclnGraphRepresentationPanel, MCLN_GRAPH_DESIGNER_VIEW_PANEL);
        add(mcLnGraphMatrixRepresentationPanel, MCLN_GRAPH_MATRIX_VIEW_PANEL);
        currentViewIsMclnGraphDesignerView = true;
    }

    public boolean isCurrentViewIsMclnGraphDesignerView() {
        return currentViewIsMclnGraphDesignerView;
    }

    public void setCurrentView(boolean currentViewWillBeMclnGraphDesignerView) {
        if (currentViewWillBeMclnGraphDesignerView) {
            switchToMclnGraphDesignerView();
        } else {
            switchToMclnGraphMatrixView();
        }
    }

    public JComponent getCurrentView() {
        if (currentViewIsMclnGraphDesignerView) {
            return mclnGraphDesignerView;
        } else {
            return this;
        }
    }

    public final void switchToMclnGraphDesignerView() {
        currentViewIsMclnGraphDesignerView = true;
        cardLayout.show(this, MCLN_GRAPH_DESIGNER_VIEW_PANEL);
    }

    public final void switchToMclnGraphMatrixView() {
        currentViewIsMclnGraphDesignerView = false;
        cardLayout.show(this, MCLN_GRAPH_MATRIX_VIEW_PANEL);
    }
}
