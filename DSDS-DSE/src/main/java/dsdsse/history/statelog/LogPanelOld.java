package dsdsse.history.statelog;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/5/14
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogPanelOld extends JPanel {

    private static LogPanelOld logPanel = new LogPanelOld();

    public static LogPanelOld getInstance() {
        return logPanel;
    }

    private final LogPanelModel logPanelModel;
    private final LogViewPanel logViewPanel;

    private LogPanelOld() {
        super(new BorderLayout());
        logPanelModel = new LogPanelModel(10);
        logViewPanel = new LogViewPanel(logPanelModel);
        add(logViewPanel, BorderLayout.CENTER);
    }

    public void clearLog(){
        logPanelModel.clearLog();
        Graphics g = logViewPanel.getGraphics();
        logViewPanel.paint(g);
//        logViewPanel.repaint();
    }

    public LogEntry getLastLogEntry(){
         return logViewPanel.getLastLogEntry();
    }

    public void addLogElement(LogEntry logEntry) {
        logViewPanel.addLogElement(  logEntry);
    }
}
