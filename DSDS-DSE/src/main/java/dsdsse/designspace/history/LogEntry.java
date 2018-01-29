package dsdsse.designspace.history;

import mcln.palette.MclnState;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/10/14
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogEntry {

    private String time;
    private String uid;
    private String statementText;
    private MclnState mclnState;
    private boolean shownFirstTime;
    private boolean initialized;


    public void init(long currentTime, String uid, String statementText, MclnState mclnState) {
        time = String.format("%tT", currentTime);
        this.uid = uid;
        this.statementText = statementText;
        shownFirstTime = true;
        initialized = true;
        this.mclnState = mclnState;
    }

    String getTime() {
        return time;
    }

    String getUid() {
        return uid;
    }

    String getStatementText() {
        return statementText;
    }

    public MclnState getMclnState() {
        return mclnState;
    }

    boolean isShownFirstTime() {
        return shownFirstTime;
    }

    void resetFirstTimeFlag() {
        shownFirstTime = false;
    }

    boolean isInitialized() {
        return initialized;
    }

    public String toString() {
        return "" + time + "  " + uid + "   " + statementText + "   shownFirstTime = " + shownFirstTime + "  initialized = " + initialized;
    }
}
