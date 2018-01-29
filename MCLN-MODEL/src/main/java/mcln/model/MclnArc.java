package mcln.model;

import mcln.palette.MclnState;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Project MCLN-MODEL
 * <p>
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/3/13
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */
//public class MclnArc extends ModelPolyLineEntity {
public class MclnArc<InpNodeType, OutNodeType> extends MclnEntity {

    public static final String MCLN_ARC_XML_TAG = "Mcln-Arc";
    public static final String MCLN_ARC_UID_TAG = "Arc-UID";
    public static final String MCLN_ARC_KNOTS_TAG = "Arc-Knot-Locations";
    public static final String MCLN_ARC_KNOT_LOCATION_TAG = "XY-Location";
    public static final String MCLN_ARC_KNOB_INDEX_TAG = "Arc-Knob-Index";
    public static final String MCLN_ARC_ARROW_TIP_INDEX_TAG = "Arc-Arrow-Tip-Index";
    public static final String MCLN_ARC_STATE_TAG = "Arc-State";
    public static final String MCLN_ARC_NODES_UID_TAG = "Arc-Node-UIDs";
    public static final String MCLN_ARC_VIEW_POINTS_TAG = "View-Points";
    public static final String MCLN_ARC_ARROW_POINTS_TAG = "Arrow-Points";

    private ArrowTipLocationPolicy arrowTipLocationPolicy;
    private MclnState calculatedProducedState = MclnState.EMPTY_STATE;

//    private final String arcUid;

    private String inpNodeUID;
    private InpNodeType inpNode;
    private String outNodeUID;
    private OutNodeType outNode;

    private final List<double[]> cSysKnots = new ArrayList();
    private MclnState arcMclnState;
    private MclnArcKnob mclnArcKnob;
    private int knobIndex = -1;
    private int arrowTipSplineIndex = -1;

    // view elements for XML
    private List<double[]> splineCSysPoints = new ArrayList();
    private List<double[]> arrowCSysPoints = new ArrayList();
//    private MclnArcPolyLineEntity mclnArcArrowPolyLineEntity = new MclnArcPolyLineEntity();

    private MclnCondition outboundMclnCondition;
    private MclnState currentOutputState = MclnState.CORE_STATE_CONTRADICTION;

    private StringBuilder oneLineMessageBuilder = new StringBuilder();

    /**
     * Creates incomplete mclnArc. Is used by Editor
     */
    MclnArc(ArrowTipLocationPolicy arrowTipLocationPolicy, String arcUid, List<double[]> cSysKnots,
            MclnState arcMclnState, InpNodeType inpNode) {
        this(arrowTipLocationPolicy, arcUid, cSysKnots, arcMclnState, inpNode, null);
    }

    /**
     * Setting output Node completes the arc creation
     *
     * @param outNode
     */
    public void setOutNode(OutNodeType outNode) {
        this.outNode = outNode;
        outNodeUID = ((MclnNode) outNode).getUID();
        ((MclnNode) outNode).addInpArc(this);
    }

    /**
     * Is used
     * 1) To create Demo projects
     * 2) By Editor for creating simple fragment complete mclnASrc.
     * 3) To create MclnArc when Project retrieved from XML
     *
     * @param arcUid
     * @param cSysKnots may be null if arc is straight, view will calculate knob location
     */

    MclnArc(ArrowTipLocationPolicy arrowTipLocationPolicy, String arcUid, List<double[]> cSysKnots,
            MclnState arcMclnState, InpNodeType inpNode, OutNodeType outNode) {
        super(arcUid);
        this.arrowTipLocationPolicy = arrowTipLocationPolicy;
//        this.arcUid = arcUid;
        createArc(cSysKnots, arcMclnState, inpNode, outNode);
    }

    /**
     * @param cSysKnots
     * @param arcMclnState
     * @param inpNode
     * @param outNode
     */
    private void createArc(List<double[]> cSysKnots, MclnState arcMclnState,
                           InpNodeType inpNode, OutNodeType outNode) {

        this.arcMclnState = arcMclnState;

        this.inpNode = inpNode;
        inpNodeUID = ((MclnNode) inpNode).getUID();
        ((MclnNode) inpNode).addOutArc(this);

        if (outNode == null) {
            knobIndex = -1;
            return;
        }

        setOutNode(outNode);

//        this.outNode = outNode;
//        outNodeUID = ((MclnNode) outNode).getUID();
//        ((MclnNode) outNode).addInpArc(this);

        if (cSysKnots != null) {
            this.cSysKnots.addAll(cSysKnots);
            knobIndex = cSysKnots.size() / 2;
        } else {
//            assert (inpNode != null && outNode != null) : "Both Nodes should exist!";
            List<double[]> tmpKnotCSysLocations = new ArrayList<>();
//            tmpKnotCSysLocations.add(inpNode.getCSysPoint());
//            tmpKnotCSysLocations.add(outNode.getCSysPoint());
            knobIndex = 1;
        }
    }

    public ArrowTipLocationPolicy getArrowTipLocationPolicy() {
        return arrowTipLocationPolicy;
    }

    //    public void setArrowCSysPoints(double[][] arrowCSysPoints){
////        mclnArcArrowPolyLineEntity = new MclnArcPolyLineEntity(arrowCSysPoints);
//        this.arrowCSysPoints.clear();
//        for (double[] point : arrowCSysPoints) {
//            this.arrowCSysPoints.add(point);
//        }
//    }

//    public void setArrowCSysPoints(List<double[]> arrowCSysPoints){
//        mclnArcArrowPolyLineEntity = new MclnArcPolyLineEntity(arrowCSysPoints);
//    }

//    public String getUID() {
//        return arcUid;
//    }

    public MclnState getArcMclnState() {
        return arcMclnState;
    }

    public void setArcMclnState(MclnState arcMclnState) {
        this.arcMclnState = arcMclnState;
    }

    //    public void addKnotCSysLocation(double[] knotCSysLocation) {
//        knotCSysLocations.add(knotCSysLocation);
//    }
//
//    public double[] getKnotCSysLocation(int index) {
//        assert index < knotCSysLocations.size() : "Knot index out of boundary " + index + ">=" + knotCSysLocations.size();
//        return knotCSysLocations.get(index);
//    }

//    public void setKnotCSysLocation(int index, double[] knotCSysLocation) {
//        assert index < knotCSysLocations.size() : "Knot index out of boundary " + index + ">=" + knotCSysLocations.size();
//        knotCSysLocations.set(index, knotCSysLocation);
//    }

    public final void setCSysKnots(List<double[]> cSysKnots) {
        this.cSysKnots.clear();
        this.cSysKnots.addAll(cSysKnots);
    }

    public final List<double[]> getKnotCSysLocations() {
        return new ArrayList(cSysKnots);
    }

//    public List<double[]> getKnobCSysPoints(List<double[]> arrowCSysPoints){
//        List<double[]> knobCSysPoints =  mclnArcArrowPolyLineEntity.getPolyLinePoints();
//        return knobCSysPoints;
//    }


    public int getArrowTipSplineIndex() {
        return arrowTipSplineIndex;
    }

    public void setArrowTipSplineIndex(int arrowTipSplineIndex) {
        this.arrowTipSplineIndex = arrowTipSplineIndex;
    }

    public int getKnobIndex() {
        return knobIndex;
    }

    public void setKnobIndex(int knobIndex) {
        this.knobIndex = knobIndex;
    }

    public String getInpNodeUID() {
        return inpNodeUID;
    }

    public String getOutNodeUID() {
        return outNodeUID;
    }

    public InpNodeType getInpNode() {
        return inpNode;
    }

    public OutNodeType getOutNode() {
        return outNode;
    }

    //
    //   M e t h o d s   t o   d i s c o n n e c t   t h e   A r c   f r o m   t h e   g r a p h
    //

    /**
     * Called when a node removed from the graph
     *
     * @param nodeToDisconnectFrom
     */
    void disconnectFromNode(MclnNode nodeToDisconnectFrom) {
        if (nodeToDisconnectFrom == inpNode) {
            inpNode = null;
        }
        if (nodeToDisconnectFrom == outNode) {
            outNode = null;
        }
    }

    /**
     * Called when this arc is disconnected from its input and output nodes
     */
    public void disconnectFromInputAndOutputNodes() {
        if (inpNode != null) {
            ((MclnNode) inpNode).removeOutputArc(this);
        }
        if (outNode != null) {
            ((MclnNode) outNode).removeInputArc(this);
        }
        inpNode = null;
        outNode = null;
    }

    //
    //   Saving and retrieving debugging information
    //

    public MclnState getCalculatedProducedState() {
        return calculatedProducedState;
    }

    public void setCalculatedProducedState(MclnState calculatedProducedState) {
        this.calculatedProducedState = calculatedProducedState;
    }

    //
    //  X M L i z e r
    //

    public void setSplineCSysPoints(List<double[]> splineCSysPoints, double[][] arrowCSysPoints) {
        this.splineCSysPoints = splineCSysPoints;
        this.arrowCSysPoints.clear();
        for (double[] point : arrowCSysPoints) {
            this.arrowCSysPoints.add(point);
        }
    }

    /**
     * @return
     */
    public String toXml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(MCLN_ARC_XML_TAG).append(">");
        arcUIDToXml(stringBuilder);
        if (cSysKnots.size() > 2) {
            cSysPointsToXml(stringBuilder);
        }
        arcKnobAndArrowTipLocationsToXml(stringBuilder);
        arrowPointsToXml(stringBuilder);
        arcMclnState.toXml(stringBuilder, MCLN_ARC_STATE_TAG);
        nodeUIDsToXml(stringBuilder);
        stringBuilder.append("</").append(MCLN_ARC_XML_TAG).append(">");
        return stringBuilder.toString();
    }

    private StringBuilder arcUIDToXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_ARC_UID_TAG).append(">");
        stringBuilder.append(getUID());
        stringBuilder.append("</").append(MCLN_ARC_UID_TAG).append(">");
        return stringBuilder;
    }

    private StringBuilder cSysPointsToXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_ARC_VIEW_POINTS_TAG).append(">");
        for (double[] splineCSysPoint : splineCSysPoints) {
            toXYXmlLocation(stringBuilder, splineCSysPoint[0], splineCSysPoint[1]);
        }
        stringBuilder.append("</").append(MCLN_ARC_VIEW_POINTS_TAG).append(">");
        return stringBuilder;
    }

    private void arcKnobAndArrowTipLocationsToXml(StringBuilder stringBuilder) {
        if (arrowTipLocationPolicy == ArrowTipLocationPolicy.DETERMINED_BY_USER) {
            arcArrowTipIndexToXml(stringBuilder, arrowTipSplineIndex);
            arcKnobIndexToXml(stringBuilder, 0);
        } else if (arrowTipLocationPolicy == ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION) {
            arcArrowTipIndexToXml(stringBuilder, 0);
            arcKnobIndexToXml(stringBuilder, knobIndex);
        }
        knotsLocationToXml(stringBuilder);
    }

    /**
     * @param stringBuilder
     * @return
     */
    private StringBuilder knotsLocationToXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_ARC_KNOTS_TAG).append(">");
        for (double[] cSysKnot : cSysKnots) {
            toXYXmlLocation(stringBuilder, cSysKnot[0], cSysKnot[1]);
        }
        stringBuilder.append("</").append(MCLN_ARC_KNOTS_TAG).append(">");
        return stringBuilder;
    }

    private StringBuilder arcArrowTipIndexToXml(StringBuilder stringBuilder, int arrowTipSplineIndex) {
        stringBuilder.append("<").append(MCLN_ARC_ARROW_TIP_INDEX_TAG).append(">");
        stringBuilder.append(arrowTipSplineIndex);
        stringBuilder.append("</").append(MCLN_ARC_ARROW_TIP_INDEX_TAG).append(">");
        return stringBuilder;
    }

    private StringBuilder arcKnobIndexToXml(StringBuilder stringBuilder, int knobIndex) {
        stringBuilder.append("<").append(MCLN_ARC_KNOB_INDEX_TAG).append(">");
        stringBuilder.append(knobIndex);
        stringBuilder.append("</").append(MCLN_ARC_KNOB_INDEX_TAG).append(">");
        return stringBuilder;
    }

    private StringBuilder arrowPointsToXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_ARC_ARROW_POINTS_TAG).append(">");
//        List<double[]> getKnobCSysPoints = mclnArcArrowPolyLineEntity.getPolyLinePoints();
//        for (double[] arrowCSysPoint : getKnobCSysPoints) {
        for (double[] arrowCSysPoint : arrowCSysPoints) {
            toXYXmlLocation(stringBuilder, arrowCSysPoint[0], arrowCSysPoint[1]);
        }
        stringBuilder.append("</").append(MCLN_ARC_ARROW_POINTS_TAG).append(">");
        return stringBuilder;
    }

    private StringBuilder nodeUIDsToXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_ARC_NODES_UID_TAG).append(">");
        stringBuilder.append(inpNodeUID).append(" : ").append(outNodeUID);
        stringBuilder.append("</").append(MCLN_ARC_NODES_UID_TAG).append(">");
        return stringBuilder;
    }

    private StringBuilder toXYXmlLocation(StringBuilder stringBuilder, double x, double y) {
        stringBuilder.append("<XY-Location>").
                append(doubleToFormattedString(x)).
                append(" : ").
                append(doubleToFormattedString(y)).
                append("</XY-Location>");
        return stringBuilder;
    }

    private String doubleToFormattedString(double number) {
        return String.format(Locale.getDefault(), "%012.6f", number);
    }
}