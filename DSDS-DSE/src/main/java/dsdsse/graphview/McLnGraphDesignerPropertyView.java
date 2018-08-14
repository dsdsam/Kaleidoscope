package dsdsse.graphview;

import dsdsse.preferences.DsdsseUserPreference;
import mcln.model.MclnStatement;
import mcln.palette.MclnState;
import mclnview.graphview.MclnGraphView;
import mclnview.graphview.MclnPropertyView;

import java.awt.*;

public class McLnGraphDesignerPropertyView extends MclnPropertyView implements DesignerNodeView {

    static {
        int radius = DsdsseUserPreference.getPropertyBallSize();
        MclnPropertyView.setBallSize(radius);
        boolean status = DsdsseUserPreference.isPropertyView3D();
        MclnPropertyView.setDisplayMclnPropertyViewAs3DCircle(status);
    }

    private MclnGraphDesignerView mclnGraphDesignerView;

    McLnGraphDesignerPropertyView(MclnGraphDesignerView mclnGraphDesignerView, MclnStatement mclnStatement) {
        super(mclnGraphDesignerView, mclnStatement);
        this.mclnGraphDesignerView = mclnGraphDesignerView;
    }

    /**
     * Called from Initialization Assistant to save the result of initialization
     */
    public void repaintPropertyUponInitialization() {
        MclnState mclnState = getTheElementModel().getInitialMclnState();
        Color currentStateColor = (mclnState != null) ? new Color(mclnState.getRGB()) : DEFAULT_BALL_COLOR;
        initMclnPropertyView(currentStateColor);
        mclnGraphDesignerView.paintEntityOnly(this);
    }
}
