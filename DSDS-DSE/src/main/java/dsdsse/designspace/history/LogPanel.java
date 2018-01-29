package dsdsse.designspace.history;

import dsdsse.history.TraceLogTitlePanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/5/14
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogPanel extends JPanel {

    private final String titleText = "          ID          |          Property Name          |          State Statement";

    private final TraceLogTitlePanel traceLogTitlePanel =  new TraceLogTitlePanel(titleText);
    private static LogPanel logPanel = new LogPanel();


    public static LogPanel getInstance() {
        return logPanel;
    }

    private final dsdsse.designspace.history.LogPanelModel logPanelModel;
    private final LogViewPanel logViewPanel;

    private LogPanel() {
        super(new BorderLayout());

        add(traceLogTitlePanel, BorderLayout.NORTH);

        logPanelModel = new dsdsse.designspace.history.LogPanelModel(10);
        logViewPanel = new LogViewPanel(logPanelModel);
        add(logViewPanel, BorderLayout.CENTER);
    }

    public void clearLog() {
        logPanelModel.clearLog();
        logViewPanel.repaint();
    }

    public LogEntry getLastLogEntry() {
        return logViewPanel.getLastLogEntry();
    }

    public void addLogElement(LogEntry logEntry) {
        logViewPanel.addLogElement(logEntry);
    }
}
