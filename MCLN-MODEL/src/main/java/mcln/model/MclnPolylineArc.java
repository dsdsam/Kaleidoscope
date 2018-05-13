package mcln.model;

import mcln.palette.MclnState;

import java.util.List;

public class MclnPolylineArc<InpNodeType, OutNodeType> extends MclnArc {

    MclnPolylineArc(ArrowTipLocationPolicy arrowTipLocationPolicy, String arcUid, List<double[]> cSysKnots,
                    MclnState arcMclnState, InpNodeType inpNode) {
        super(arrowTipLocationPolicy, arcUid, cSysKnots, arcMclnState, inpNode);
    }
}
