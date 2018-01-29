package dsdsse.history;

import dsdsse.graphview.MclnPropertyView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Admin on 10/4/2016.
 */
public class TraceLogHolderPanel extends JPanel {

    private final ExecutionHistoryPanel executionHistoryPanel;

    private final String titleText = "\u2193 -  Current state.     Trace Log  \u2192";

    private final TraceLogTitlePanel traceLogTitlePanel = new TraceLogTitlePanel(titleText);
    private final SimulationTracePanel simulationTracePanel;
    private final JScrollPane traceLogScrollPane;

    public TraceLogHolderPanel(ExecutionHistoryPanel executionHistoryPanel) {
        super(new BorderLayout());
        super.setBorder(null);
        this.executionHistoryPanel = executionHistoryPanel;
        simulationTracePanel = new SimulationTracePanel(executionHistoryPanel);

//        simulationTracePanel.setPreferredSize(new Dimension(1,600));
//        simulationTracePanel.setSize(new Dimension(1,600));
//        simulationTracePanel.setMinimumSize(new Dimension(1,600));

        traceLogScrollPane = new JScrollPane(simulationTracePanel);
        traceLogScrollPane.setBorder(null);
        traceLogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        add(traceLogTitlePanel, BorderLayout.NORTH);
        add(traceLogScrollPane, BorderLayout.CENTER);
    }

    void setContents(List<MclnPropertyView> presentedPropertyList) {
        int presentedPropertyListSize = presentedPropertyList.size();
//        System.out.println("Size " + presentedPropertyListSize);
        simulationTracePanel.setContents(presentedPropertyList);
        simulationTracePanel.setPreferredSize(new Dimension(1,ExecutionHistoryPanel.ROW_HEIGHT * presentedPropertyListSize));
        simulationTracePanel.setSize(new Dimension(1,ExecutionHistoryPanel.ROW_HEIGHT * presentedPropertyListSize));
        simulationTracePanel.setMinimumSize(new Dimension(1,ExecutionHistoryPanel.ROW_HEIGHT * presentedPropertyListSize));
    }

    void setScrollBarModel(BoundedRangeModel boundedRangeModel) {
        traceLogScrollPane.getVerticalScrollBar().setModel(boundedRangeModel);
    }

    public void simulationStepExecuted() {
        simulationTracePanel.simulationStepExecuted();
    }

    public void traceLogCleared() {
        simulationTracePanel.traceLogCleared();
    }

}
