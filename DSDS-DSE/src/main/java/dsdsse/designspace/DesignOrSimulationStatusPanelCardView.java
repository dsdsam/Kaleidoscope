package dsdsse.designspace;

import dsdsse.app.AppStateModel;
import dsdsse.history.ExecutionHistoryPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 1/18/2016.
 */
public class DesignOrSimulationStatusPanelCardView extends JPanel {

    final static String DESIGN_STATUS_PANEL = "Design status panel";
    final static String SIMULATION_STATUS_PANEL = "Simulation status panel";

    private static DesignOrSimulationStatusPanelCardView designOrSimulationVisualizationCardView;

    public static DesignOrSimulationStatusPanelCardView createInstance(DesignSpaceView designSpaceView) {
        assert designOrSimulationVisualizationCardView == null :
                "The instance of DesignOrSimulationStatusPanelCardView already exists";
        designOrSimulationVisualizationCardView = new DesignOrSimulationStatusPanelCardView(designSpaceView);
        return designOrSimulationVisualizationCardView;
    }

    public static DesignOrSimulationStatusPanelCardView getInstance() {
        assert designOrSimulationVisualizationCardView != null :
                "The instance of DesignOrSimulationStatusPanelCardView was not yet created";
        return designOrSimulationVisualizationCardView;
    }

    private final DesignSpaceView designSpaceView;
    private final CardLayout cardLayout;
    private final DesignStatusView designStatusView;
    private final ExecutionHistoryPanel executionHistoryPanel;
    private boolean showingDesignStatusPanel;

    private DesignOrSimulationStatusPanelCardView(DesignSpaceView designSpaceView) {
        this.designSpaceView = designSpaceView;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        designStatusView = DesignStatusView.getInstance();
        executionHistoryPanel = ExecutionHistoryPanel.createInstance(designSpaceView);
        add(designStatusView, DESIGN_STATUS_PANEL);
        add(executionHistoryPanel, SIMULATION_STATUS_PANEL);
        showingDesignStatusPanel = true;
    }


    public final void switchToDesignStatusViewPanel() {
        int userSelectedDividerLocation = executionHistoryPanel.getSize().height;
        showingDesignStatusPanel = true;
        executionHistoryPanel.historyPresented(false);
        cardLayout.show(designOrSimulationVisualizationCardView, DESIGN_STATUS_PANEL);
        DesignSpaceContentManager.setSavedSimulationStatusViewPanelHeight(userSelectedDividerLocation);
        DesignSpaceContentManager.setDesignStatusViewPanelHeight();
    }

    public final void switchToSimulationStatusViewPanel() {
        showingDesignStatusPanel = false;
        cardLayout.show(designOrSimulationVisualizationCardView, SIMULATION_STATUS_PANEL);
        executionHistoryPanel.historyPresented(true);
        int executionHistoryPanelHeight = executionHistoryPanel.getViewPreferredHeight();
        DesignSpaceContentManager.restoreSimulationStatusViewPanelHeight(executionHistoryPanelHeight);
    }

    public int getCurrentCardViewHeight() {
        int cardPanelHeight;
        if (showingDesignStatusPanel) {
            cardPanelHeight = designStatusView.getViewPreferredHeight();
        } else {
            cardPanelHeight = executionHistoryPanel.getViewPreferredHeight();
        }
        return cardPanelHeight;
    }
}
