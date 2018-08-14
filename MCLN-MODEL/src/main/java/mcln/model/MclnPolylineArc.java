package mcln.model;

import mcln.palette.MclnState;

import java.util.List;
import java.util.Locale;

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
