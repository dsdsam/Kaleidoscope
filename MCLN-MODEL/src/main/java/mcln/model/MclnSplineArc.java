package mcln.model;

import mcln.palette.MclnState;

import java.util.List;

public class MclnSplineArc<InpNodeType, OutNodeType> extends MclnArc {

    MclnSplineArc(ArrowTipLocationPolicy arrowTipLocationPolicy, String arcUid, List<double[]> cSysKnots,
                  MclnState arcMclnState, InpNodeType inpNode) {
        super(arrowTipLocationPolicy, arcUid, cSysKnots, arcMclnState, inpNode);
    }

    MclnSplineArc(ArrowTipLocationPolicy arrowTipLocationPolicy, String arcUid, List<double[]> cSysKnots,
                  MclnState arcMclnState, InpNodeType inpNode, OutNodeType outNode) {
        super(arrowTipLocationPolicy, arcUid, cSysKnots, arcMclnState, inpNode, outNode);
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

    /**
     * @return
     */
    @Override
    public String toXml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(MCLN_ARC_XML_TAG).append(" type=\"spline\">");
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
}
