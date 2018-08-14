package dsdsse.history;

import adf.onelinemessage.AdfOneLineMessageManager;
import adf.utils.BuildUtils;
import adf.utils.RingStack;
import dsdsse.designspace.ProjectStorage;
import mcln.palette.MclnState;
import mclnview.graphview.MclnPropertyView;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/4/2016.
 */
public class TraceLogHolderPanel extends JPanel {

    private final String titleText = "\u2193 -  Current state.     Trace Log  \u2192";

    private final TraceLogTitlePanel traceLogTitlePanel = new TraceLogTitlePanel(titleText);
    private final JButton saveTraceLogButton = new JButton("Save Trace Log");
    private final SimulationTracePanel simulationTracePanel;
    private final JScrollPane traceLogScrollPane;

    private final ActionListener saveTraceLogButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            processSaveTraceLogButtonClicked();
        }
    };

    public TraceLogHolderPanel(ExecutionHistoryPanel executionHistoryPanel) {
        super(new GridBagLayout());
        setBorder(null);
        simulationTracePanel = new SimulationTracePanel();

        traceLogScrollPane = new JScrollPane(simulationTracePanel);
        traceLogScrollPane.setBorder(null);
        traceLogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        add(traceLogTitlePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new MatteBorder(1, 0, 2, 2, TraceLogTitlePanel.TITLE_BACKGROUND));
        Dimension saveButtonSize = new Dimension(100, TraceLogTitlePanel.TITLE_HEIGHT);
        buttonPanel.setPreferredSize(saveButtonSize);
        buttonPanel.setMaximumSize(saveButtonSize);
        buttonPanel.setMinimumSize(saveButtonSize);

        saveTraceLogButton.setBorder(null);
        saveTraceLogButton.setFocusPainted(false);
        saveTraceLogButton.addActionListener(saveTraceLogButtonListener);
        BuildUtils.resetComponentFont(saveTraceLogButton, Font.PLAIN, 10);
        buttonPanel.add(saveTraceLogButton, BorderLayout.CENTER);

        add(buttonPanel, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));


        add(traceLogScrollPane, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }


    private final List<MclnPropertyView> tracedPropertyList = new ArrayList();

    void setContents(List<MclnPropertyView> presentedPropertyList) {
        this.tracedPropertyList.clear();
        this.tracedPropertyList.addAll(presentedPropertyList);
        int presentedPropertyListSize = presentedPropertyList.size();
        simulationTracePanel.setContents(presentedPropertyList);
        simulationTracePanel.setPreferredSize(new Dimension(1, ExecutionHistoryPanel.ROW_HEIGHT * presentedPropertyListSize));
        simulationTracePanel.setSize(new Dimension(1, ExecutionHistoryPanel.ROW_HEIGHT * presentedPropertyListSize));
        simulationTracePanel.setMinimumSize(new Dimension(1, ExecutionHistoryPanel.ROW_HEIGHT * presentedPropertyListSize));
    }

    void setScrollBarModel(BoundedRangeModel boundedRangeModel) {
        traceLogScrollPane.getVerticalScrollBar().setModel(boundedRangeModel);
    }

    public void simulationStepExecuted(int historySize) {
        traceLogTitlePanel.updateTraceHistorySize(historySize);
        simulationTracePanel.simulationStepExecuted();
    }

    public void traceLogCleared() {
        simulationTracePanel.traceLogCleared();
    }

    //
    //   Creating and writing Trace Log File
    //

    private final void processSaveTraceLogButtonClicked() {
        List<String> traceLogList = createTraceLogFile(tracedPropertyList);
        if (traceLogList.isEmpty()) {
            traceLogList.add("Trace Log is empty !");
        }
        printTraceLogFile(traceLogList);
        ProjectStorage.getInstance().saveTraceLog(traceLogList);
        System.out.println("processSaveTraceLogButtonClicked");
        String message = "Trace Log saved to file:  \"Trace Log.log \"";
        AdfOneLineMessageManager.showInfoMessage(message);
    }

    private void printTraceLogFile(List<String> traceLogFile) {
        System.out.println("Trace Log File: size = " + traceLogFile.size());
        for (String entry : traceLogFile) {
            System.out.println(entry);
        }
    }

    /**
     * @param presentedPropertyList
     */
    private List<String> createTraceLogFile(List<MclnPropertyView> presentedPropertyList) {

        StringBuilder sb = new StringBuilder();
        List<String> traceLogList = new ArrayList();

        List<MclnPropertyView> mcLnPropertyViews = new ArrayList();
        List<RingStack> ringStacks = new ArrayList();
        int propertySize = presentedPropertyList.size();
        int traceLogFileLength = 0;
        String separator = System.getProperty("line.separator");

        sb.append("@PROPERTY-NAME-LIST : ");

        for (int i = 0; i < propertySize; i++) {
            MclnPropertyView mcLnPropertyView = presentedPropertyList.get(i);
            RingStack ringStack = mcLnPropertyView.getHistory();
            traceLogFileLength = ringStack.getCurrentSize();
            if (traceLogFileLength <= 0) {
                return traceLogList;
            }
            MclnState mclnState = (MclnState) ringStack.getElementAt(0);

            mcLnPropertyViews.add(mcLnPropertyView);
            ringStacks.add(ringStack);

            String uid = mcLnPropertyView.getUID();
            if (i > 0) {
                sb.append(",");
            }
            sb.append(uid);
        }

        sb.append(separator);
        sb.append("#");
        traceLogList.add(sb.toString());

        sb.delete(0, sb.length());

        List<String> logList = new ArrayList();
        for (int i = 0; i < traceLogFileLength; i++) {
            int propertyCounter = 0;
            for (int j = 0; j < propertySize; j++) {
                MclnPropertyView mcLnPropertyView = mcLnPropertyViews.get(j);
                RingStack ringStack = ringStacks.get(j);
                MclnState mclnState = (MclnState) ringStack.getElementAt(i);
                String stateName = "";
                if (mclnState == null) {
                    stateName = "XXX";
                } else {
                    stateName = mclnState.getStateName();
                }
                if (propertyCounter > 0) {
                    sb.append(",");
                }
                sb.append(stateName);
                propertyCounter++;
            }
            logList.add(sb.toString());
            sb.delete(0, sb.length());
        }
//
        // Reverse log list
        int logSize = logList.size();
        int reversedLogHalfSize = logList.size() / 2;
        for (int i = 0; i < reversedLogHalfSize; i++) {
            String tmpData = logList.get(i);
            logList.set(i, logList.get(logSize - 1 - i));
            logList.set((logSize - 1 - i), tmpData);
        }

        for (int i = 0; i < logSize; i++) {
            logList.set(i, "("+(i+1)+") "+logList.get(i));
        }

        traceLogList.addAll(logList);
        traceLogList.add("- End of Trace Log -");
        return traceLogList;
    }

}
