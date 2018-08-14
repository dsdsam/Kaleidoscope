package mclnview.graphview;

import mcln.model.MclnArc;
import mcln.palette.MclnState;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: May 26, 2013
 * Time: 7:35:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MclnArcView extends MclnGraphEntityView<MclnArc> implements Cloneable {

    public static final Color DEFAULT_ARC_STATE_COLOR = Color.LIGHT_GRAY;

    private final MclnArc mclnArc; // arc data model

    MclnGraphView parentCSys;
    protected MclnState arcMclnState;
    protected MclnGraphNodeView inpNode;
    protected MclnGraphNodeView outNode;

    boolean straightArc;

    StringBuilder oneLineMessageBuilder = new StringBuilder();

    //  ***********************************************************************************************
    //
    //   T h e   M e t h o d s   t o   C r e a t   A r c   v i a   G r a h i c a l   I n t e r f a c e
    //
    //  ***********************************************************************************************

    /**
     * Creates incomplete Arc with Input Node only - used when the arc is created by user
     *
     * @param parentCSys
     * @param mclnArc
     * @param inpNode
     */
    MclnArcView(MclnGraphView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode) {
        super(parentCSys, mclnArc);
        this.parentCSys = parentCSys;

        this.mclnArc = mclnArc;
        this.inpNode = inpNode;

        // set arc to be a spline
        straightArc = false;
        arcMclnState = mclnArc.getArcMclnState();
    }

    /**
     * The method is used during Arc creation to check if the Arc's
     * thread length or shape allows to place the Arc Arrow on it.
     *
     * @return
     */
    abstract boolean checkIfArrowTipCanBeFound();

    /**
     * Editor sets Output Node right when it is picked up
     *
     * @param outNode
     */
    abstract void setOutputNode(MclnGraphNodeView outNode);


    //
    //   Arc Creation API
    //
    abstract void arcInteractiveCreationFinished();


    //  *********************************************************************************************************
    //
    //   T h e   M e t h o d s   t o   I n s t a n t i a t e   A r c   W i t h   P r o g r a m m a t i c a l l y
    //                              P r o v i d e d    A t t r i b u t e s
    //
    //  *********************************************************************************************************

    /**
     * This constructor is used when arc model is available:
     * 1) When ArcView is created as part of Demo-project
     * 2) when McLN Fragment is created by Editor.
     * 3) When McLN recreated from saved model
     *
     * @param parentCSys
     * @param mclnArc
     * @param inpNode
     * @param outNode
     */
    MclnArcView(MclnGraphView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode,
                MclnGraphNodeView outNode) {
        super(parentCSys, mclnArc);
        this.parentCSys = parentCSys;
        this.mclnArc = mclnArc;
        this.inpNode = inpNode;
        this.outNode = outNode;
    }


    //  *********************************************************************************************************
    //
    //    M o v i n g   A r c   K n o t s   a s   P a r t   o f   M o v e   E l e m e n t s   O p e r a t i o n
    //
    //  *********************************************************************************************************

    abstract void backupCSysKnots();

    public abstract void restoreBackupCSysKnots();

    abstract public void startMoving();

    public abstract boolean selectAKnotUnderMouse(Point mousePoint);

    /**
     * Called when user moves model elements
     *
     * @param mousePoint
     */
    public abstract void druggingSelectedArc(Point mousePoint);

    public abstract void movingArcCompleted();

    abstract void movingArcInputOrOutputNode(int[] scr0, double scale);

    abstract void movingArcInputOrOutputNodeCompleted();

    //  *********************************************************************************************************
    //
    //    Just getters
    //
    //  *********************************************************************************************************

    @Override
    public MclnArc getTheElementModel() {
        return mclnArc;
    }

    public final boolean isRecognizingArc() {
        return inpNode != null && (inpNode instanceof MclnPropertyView);
    }

    public final boolean isGeneratingArc() {
        return inpNode != null && (inpNode instanceof MclnConditionView);
    }

    public boolean isConnected() {
        return inpNode != null && outNode != null;
    }

    // not in use noe, but potentially useful
    public boolean isInitialized() {
        return true;
    }

    public String getUID() {
        return mclnArc.getUID();
    }

    public MclnGraphNodeView getInpNode() {
        return inpNode;
    }

    public MclnGraphNodeView getOutNode() {
        return outNode;
    }

    public MclnArcView clone() {
        MclnArcView mclnArcViewClone = null;
        try {
            mclnArcViewClone = (MclnArcView) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            return mclnArcViewClone;
        }
    }

    //
    //   M e t h o d s   t o   d i s c o n n e c t   t h e   A r c   f r o m   t h e   g r a p h
    //

    /**
     * Called when a node removed from the graph
     *
     * @param nodeToDisconnectFrom
     */
    void disconnectFromNode(MclnGraphNodeView nodeToDisconnectFrom) {
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
            inpNode.removeOutputArc(this);
        }
        if (outNode != null) {
            outNode.removeInputArc(this);
        }
        inpNode = null;
        outNode = null;
    }

    //
    //   D r a w i n g   m e t h o d s   a n d   a t t r i b u t e s
    //

    abstract void setDrawKnots(boolean status);

    abstract public void setAllKnotsSelected(boolean status);

    abstract void setThreadSelected(boolean status);

    abstract void setArrowSelected(boolean arrowSelected);

    //
    //   W a t e r m a r k
    //

    abstract public void setArrowWatermarked(boolean watermarked);

    @Override
    public void paintExtras(Graphics g) {
        paintExtrasAtGivenScreenLocation(g);
    }

    public void paintExtrasAtInterimLocation(Graphics g) {
        paintExtrasAtGivenScreenLocation(g);
    }

    abstract void paintExtrasAtGivenScreenLocation(Graphics g);

    //  *********************************************************************************************************
    //
    //                    M o v i n g   A r c   A s   a   P a r t   o f   a   F r a g m e n t
    //
    //  *********************************************************************************************************

    @Override
    public void setSprite(boolean sprite) {
        super.setSprite(sprite);
        inpNode.setArcDependentNodeToBeASprite(sprite);
        if (outNode == null) {
            return;
        }
        outNode.setArcDependentNodeToBeASprite(sprite);
    }

    abstract public void translateAndPaintEntityAtInterimLocation(Graphics g, double[] translationVector);

    abstract public void backupCurrentState();

    /**
     * draws connected entity + extras if paintExtras is true
     * <p>
     * does not Overrides
     *
     * @param g
     * @param paintExtras
     */
    void drawConnectedEntities(Graphics g, boolean paintExtras) {
        drawInputNode(g, paintExtras);
        drawOutputNode(g, paintExtras);
    }

    void drawInputNode(Graphics g, boolean paintExtras) {
        inpNode.drawPlainEntity(g);
        if (paintExtras) {
            inpNode.paintExtras(g);
        }
    }

    void drawOutputNode(Graphics g, boolean paintExtras) {
        if (outNode == null) {
            return;
        }
        outNode.drawPlainEntity(g);
        if (paintExtras) {
            outNode.paintExtras(g);
        }

    }


    //
    //   M i s c   m e t h o d s
    //

    abstract void updateArcPlacement(int[] scr0, double scale);

    public String getTooltip() {
        if (mclnArc == null) {
            return null;
        }
        return mclnArc.getUID();
    }

    void updateEnds() {
    }

    abstract public String getOneLineInfoMessage();

}

