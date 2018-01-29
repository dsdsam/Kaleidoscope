package dsdsse.designspace.history;

import adf.utils.RingStack;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/7/14
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
class LogPanelModel {

    private final RingStack log;

    LogPanelModel(int size) {
        log = new RingStack(size);
        preFillLog();
    }

    void clearLog() {
        log.clear();
        preFillLog();
    }

    private void preFillLog() {
        int size = log.getSize();
        for (int i = 0; i < size; i++) {
            log.add(new LogEntry());
        }
    }

    int getLogSize() {
        return log.getSize();
    }

    public LogEntry getLastLogEntry() {
        return (LogEntry) log.getLast();
    }

    synchronized void add(LogEntry logEntry) {
        log.add(logEntry);
    }

    synchronized LogEntry getLogEntryAt(int index) {
        LogEntry logEntry = (LogEntry) log.getElementAt(index);
        return logEntry;
    }

}
