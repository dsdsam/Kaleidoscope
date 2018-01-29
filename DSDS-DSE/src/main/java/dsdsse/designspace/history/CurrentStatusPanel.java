package dsdsse.designspace.history;

import dsdsse.designspace.history. LogViewPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/5/14
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class CurrentStatusPanel extends JPanel {

    private static CurrentStatusPanel currentStatusPanel = new CurrentStatusPanel();

    public static CurrentStatusPanel getInstance() {
        return currentStatusPanel;
    }

    private final LogPanelModel logPanelModel;
    private final LogViewPanel logViewPanel;

    private CurrentStatusPanel() {
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
        return currentStatusPanel.getLastLogEntry();
    }

    public void addLogElement(LogEntry logEntry) {
        currentStatusPanel.addLogElement(logEntry);
    }
}
