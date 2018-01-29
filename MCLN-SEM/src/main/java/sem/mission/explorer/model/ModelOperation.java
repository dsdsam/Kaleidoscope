package sem.mission.explorer.model;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 10, 2011
 * Time: 4:31:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelOperation extends Object {
    // Constants
    // Veriables
    String name;
    String sender;
    int MSLNState;
    private String strState;
    double doubleValue;
    int integerValue;

    public ModelOperation(String iname,
                          String isender,
                          int istate, String istrState,
                          double doubleValue) {
        name = iname;
        sender = isender;
        MSLNState = istate;
        strState = istrState;
        this.doubleValue = doubleValue;
    }

    // ---------------------------------------------------------------
    public String getName() {
        return name;
    }

    // ---------------------------------------------------------------
    public void setStrState(String strState) {
        this.strState = strState;
    }

    // ---------------------------------------------------------------
    public String getStrState() {
        return strState;
    }

    // ---------------------------------------------------------------
    public void setSource(String sender) {
        this.sender = sender;
    }

    // ---------------------------------------------------------------
    public String getSource() {
        return sender;
    }

    // ---------------------------------------------------------------
    public double getDoubleValue() {
        return doubleValue;
    }
} // end of RtsModelOperation

