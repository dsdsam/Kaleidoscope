package mcln.model;

import mcln.palette.MclnState;

import java.util.List;

public class MclnSplineArc<InpNodeType, OutNodeType> extends MclnArc {

    MclnSplineArc(ArrowTipLocationPolicy arrowTipLocationPolicy, String arcUid, List<double[]> cSysKnots,
                    MclnState arcMclnState, InpNodeType inpNode) {
        super(arrowTipLocationPolicy, arcUid, cSysKnots, arcMclnState, inpNode);
    }
}
