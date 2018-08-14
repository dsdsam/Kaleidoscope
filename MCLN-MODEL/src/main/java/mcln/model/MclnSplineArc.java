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
