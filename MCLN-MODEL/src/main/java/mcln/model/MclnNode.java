package mcln.model;

import mcln.palette.MclnState;
import vw.valgebra.VAlgebra;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/22/13
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MclnNode<ThisNodeType, OtherNodeType> extends MclnEntity implements Comparable<MclnNode> {

    static final String doubleToFormattedString(double number) {
        return String.format(Locale.getDefault(), "%012.6f", number);
    }


    private final List<MclnArc<OtherNodeType, ThisNodeType>> inpArcs = new ArrayList();
    private final List<MclnArc<ThisNodeType, OtherNodeType>> outArcs = new ArrayList();
    private final double[] cSysPoint;
    private MclnState initialMclnState;
    private MclnState currentMclnState;

    StringBuilder tooltipBuilder = new StringBuilder();
    StringBuilder oneLineMessageBuilder = new StringBuilder();

    MclnNode(String uid, double[] cSysLocation) {
        super(uid);
        cSysPoint = cSysLocation.clone();
    }

    boolean hasOutputArcs() {
        return !outArcs.isEmpty();
    }

    public abstract double[] getCSysLocation();

    public abstract void setCSysLocation(double[] cSysPnt);

    /**
     * @return Returns the pnt1.
     */
    double[] getCSysPoint() {
        return cSysPoint;
    }

    /**
     * @param cSysPoint
     */
    void setCSysPoint(double[] cSysPoint) {
        VAlgebra.copyVec3(this.cSysPoint, cSysPoint);
    }

    /**
     * Method is coled when the node is disconnected from the graph
     */
    void disconnectFromAllArc() {
        for (MclnArc mclnArc : inpArcs) {
            mclnArc.disconnectFromNode(this);
        }
        for (MclnArc mclnArc : outArcs) {
            mclnArc.disconnectFromNode(this);
        }
        inpArcs.clear();
        outArcs.clear();
    }

    // Input arcs

    void addInpArc(MclnArc mclnArc) {
        inpArcs.add(mclnArc);
    }

    public void removeInputArc(MclnArc mclnArc) {
        inpArcs.remove(mclnArc);
    }

    public List<MclnArc<OtherNodeType, ThisNodeType>> getClonedInboundArcs() {
        return new ArrayList(inpArcs);
    }

    public List<MclnArc<ThisNodeType, OtherNodeType>> getClonedOutboundArcs() {
        return new ArrayList(outArcs);
    }

    List<MclnArc<OtherNodeType, ThisNodeType>> getInboundArcs() {
        return inpArcs;
    }

    List<MclnArc<ThisNodeType, OtherNodeType>> getOutboundArcs() {
        return outArcs;
    }

    public MclnArc getInpArc(int index) {
        if (inpArcs.size() <= index) {
            return null;
        }
        return inpArcs.get(index);
    }

    // Output arcs

    void addOutArc(MclnArc mclnArc) {
        outArcs.add(mclnArc);
    }

    public void removeOutputArc(MclnArc mclnArc) {
        outArcs.remove(mclnArc);
    }

    public MclnArc getOutArc(int index) {
        return outArcs.get(index);
    }

    //   State

    public MclnState getInitialMclnState() {
        return initialMclnState;
    }

    void setInitialMclnState(MclnState initialState) {
        this.initialMclnState = initialState;
    }

    public MclnState getCurrentMclnState() {
        return currentMclnState;
    }

    void setCurrentMclnState(MclnState currentMclnState) {
        this.currentMclnState = currentMclnState;
    }

    void resetCurrentState() {
        setCurrentMclnState(getInitialMclnState());
    }

    boolean isNodeInBound(OtherNodeType node) {
        for (MclnArc<OtherNodeType, ThisNodeType> currentInboundArc : inpArcs) {
            OtherNodeType inboundNode = currentInboundArc.getInpNode();
            if (inboundNode == node) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(MclnNode other) {
        if (getUidNumberPart() < other.getUidNumberPart()) {
            return -1;
        } else if (getUidNumberPart() == other.getUidNumberPart()) {
            return 0;
        } else {
            return 1;
        }
    }

}
