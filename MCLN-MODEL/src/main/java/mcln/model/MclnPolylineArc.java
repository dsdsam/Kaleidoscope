package mcln.model;

import mcln.palette.MclnState;

import java.util.List;

public class MclnPolylineArc<InpNodeType, OutNodeType> extends MclnArc {

    private double[] arrowTipCSysLocation = {0, 0, 0};
    private int[] arrowTipScrLocation = {0, 0, 0};
    private int arrowSegmentIndex = -1;

    MclnPolylineArc(ArrowTipLocationPolicy arrowTipLocationPolicy, String arcUid, List<double[]> cSysKnots,
                    MclnState arcMclnState, InpNodeType inpNode) {
        super(arrowTipLocationPolicy, arcUid, cSysKnots, arcMclnState, inpNode);
    }

    MclnPolylineArc(ArrowTipLocationPolicy arrowTipLocationPolicy, String arcUid, List<double[]> cSysKnots,
                    int arrowSegmentIndex, double[] arrowTipCSysLocation, MclnState arcMclnState,
                    InpNodeType inpNode, OutNodeType outNode) {
        super(arrowTipLocationPolicy, arcUid, cSysKnots, arcMclnState, inpNode, outNode);
        this.arrowSegmentIndex = arrowSegmentIndex;
        this.arrowTipCSysLocation = arrowTipCSysLocation;
    }

    @Override
    public String getOneLineInfoMessage() {
        if (inpNode == null || outNode == null) {
            return "Arc creation is not complete.";
        }
        oneLineMessageBuilder.delete(0, oneLineMessageBuilder.length());

        if (isRuntimeInitializationUpdatedFlag()) {
            oneLineMessageBuilder.append("* ");
        }

        if (inpNode instanceof MclnStatement) {
            oneLineMessageBuilder.append("Recognizing Arc: [");
            oneLineMessageBuilder.append(" ID: " + getUID());

            int nKnots = cSysKnots.size();
            oneLineMessageBuilder.append(", Knots = " + nKnots);

            String expectedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Expected State = ");
            oneLineMessageBuilder.append(expectedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = ((MclnStatement) inpNode).getCurrentMclnState();
            MclnState calculatedProducedState = getCalculatedProducedState();
            oneLineMessageBuilder.append("  Produced output: ");
            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
            oneLineMessageBuilder.append("  =  P(" + expectedState);
            oneLineMessageBuilder.append(", ");
            oneLineMessageBuilder.append(inpState.getStateName());
            oneLineMessageBuilder.append(");");
        } else {
            oneLineMessageBuilder.append("Generating Arc: [");
            oneLineMessageBuilder.append(" ID: " + getUID());

            int nKnots = cSysKnots.size();
            oneLineMessageBuilder.append(", Knots = " + nKnots);

            String generatedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Generated State = ");
            oneLineMessageBuilder.append(generatedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = ((MclnCondition) inpNode).getCurrentMclnState();
            MclnState calculatedProducedState = getCalculatedProducedState();
            oneLineMessageBuilder.append("  Produced output: ");
            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
            oneLineMessageBuilder.append("  =  G(" + generatedState);
            oneLineMessageBuilder.append(", ");
            oneLineMessageBuilder.append(inpState.getStateName());
            oneLineMessageBuilder.append(");");
        }

        return oneLineMessageBuilder.toString();
    }

    //
    //  X M L i z e r
    //

    public void setArrowSegmentIndex(int arrowSegmentIndex) {
        this.arrowSegmentIndex = arrowSegmentIndex;
    }

    public int getArrowSegmentIndex() {
        return arrowSegmentIndex;
    }

    public void setArrowTipCSysLocation(double[] arrowTipCSysLocation) {
        this.arrowTipCSysLocation = arrowTipCSysLocation;
    }

    public double[] getArrowTipCSysLocation() {
        return arrowTipCSysLocation;
    }

    public void setArrowTipScrLocation(int[] arrowTipScrLocation) {
        this.arrowTipScrLocation = arrowTipScrLocation;
    }


    /**
     * @return
     */
    @Override
    public String toXml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(MCLN_ARC_XML_TAG).append(" type=\"polyline\">");
        arcUIDToXml(stringBuilder);
        nodeUIDsToXml(stringBuilder);
        knotsLocationToXml(stringBuilder);
        arrowTipCSysLocationToXml(stringBuilder);
        arrowPointsToXml(stringBuilder);

        arcMclnState.toXml(stringBuilder, MCLN_ARC_STATE_TAG);

        stringBuilder.append("</").append(MCLN_ARC_XML_TAG).append(">");
        return stringBuilder.toString();
    }

    StringBuilder arrowTipCSysLocationToXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_POLYLINE_ARC_ARROW_TIP_SEGMENT_INDEX_TAG).append(">");
        stringBuilder.append(arrowSegmentIndex);
        stringBuilder.append("</").append(MCLN_POLYLINE_ARC_ARROW_TIP_SEGMENT_INDEX_TAG).append(">");

        stringBuilder.append("<").append(MCLN_POLYLINE_ARC_ARROW_TIP_LOCATION_TAG).append(">");
        toXYXmlLocation(stringBuilder, arrowTipCSysLocation[0], arrowTipCSysLocation[1]);
        stringBuilder.append("</").append(MCLN_POLYLINE_ARC_ARROW_TIP_LOCATION_TAG).append(">");
        return stringBuilder;
    }
}
