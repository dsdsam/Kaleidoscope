package mclnview.graphview;

import adf.csys.view.CSysView;
import mcln.model.MclnArc;
import mcln.model.MclnNode;
import mcln.model.MclnPolylineArc;
import mcln.palette.MclnState;

import java.awt.*;
import java.util.List;

public class MclnPolylineArcView extends MclnArcView implements Cloneable {

    protected final MclnPolylineArc mclnPolylineArc; // arc data model
    protected MclnGraphPolylineEntity mclnGraphPolylineEntity;

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
    public MclnPolylineArcView(MclnGraphView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode) {
        super(parentCSys, mclnArc, inpNode);
        this.mclnPolylineArc = (MclnPolylineArc) mclnArc;
        initArcCreated(parentCSys, inpNode);
    }

    /**
     * @param parentCSys
     * @param inpNode
     */
    private final void initArcCreated(CSysView parentCSys, MclnGraphNodeView inpNode) {
        mclnGraphPolylineEntity = new MclnGraphPolylineEntity(parentCSys, mclnPolylineArc, inpNode);
        double[] cSysPnt = inpNode.getCSysPnt();
        mclnGraphPolylineEntity.createFirstCSysKnot(cSysPnt);
        mclnGraphPolylineEntity.addNextCSysKnot(cSysPnt);

        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mclnGraphPolylineEntity.setArcMclnStateColor(stateColor);
    }

    //    K n o t s   a n d   s e g m e n t s   c r e a t i o n

    int getJointsSize() {
        return mclnGraphPolylineEntity.getJointsSize();
    }

    /**
     * Called when creator clicks on the screen to create next joint
     *
     * @param cSysPnt
     */
    public void addNextCSysKnot(double[] cSysPnt) {
        mclnGraphPolylineEntity.addNextCSysKnot(cSysPnt);
    }

    /**
     * Called form Polyline Arc Creator when user moves mouse
     *
     * @param jointPoint
     * @return
     */
    public boolean updateLastPoint(double[] jointPoint) {
        return mclnGraphPolylineEntity.updateLastPoint(jointPoint);
    }

    /**
     * Editor sets Output Node right when it is picked up
     *
     * @param outNode
     */
    @Override
    public void setOutputNode(MclnGraphNodeView outNode) {
        this.outNode = outNode;
        mclnGraphPolylineEntity.polylineCreationCompleted(outNode);
    }

    public void unselectSelectedOutputNode() {
        mclnGraphPolylineEntity.unselectSelectedOutputNode();
    }

    public boolean removeLastSegment() {
        boolean hasMoreSegments = mclnGraphPolylineEntity.removeLastSegment();
        return hasMoreSegments;
    }

    public void setSelectingArrowTipLocation(boolean selectingArrowTipLocation) {
        mclnGraphPolylineEntity.setSelectingArrowTipLocation(selectingArrowTipLocation);
    }

    /**
     *
     */
    @Override
    public void arcInteractiveCreationFinished() {

        mclnGraphPolylineEntity.setSelected(false);

        mclnGraphPolylineEntity.setSelectingArrowTipLocation(false);

        // complete MCLN Arc creation
        MclnNode mclnArcOutputNode = outNode.getTheElementModel();
        mclnPolylineArc.setOutNode(mclnArcOutputNode);

        arcMclnState = mclnPolylineArc.getArcMclnState();

        // complete polyline creation
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mclnGraphPolylineEntity.setArcMclnStateColor(stateColor);

        mclnGraphPolylineEntity.storeArcPersistentAttributesIntoMclnArc(mclnPolylineArc);
    }

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
    public MclnPolylineArcView(MclnGraphView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode,
                               MclnGraphNodeView outNode) {
        super(parentCSys, mclnArc, inpNode, outNode);
        this.mclnPolylineArc = (MclnPolylineArc) mclnArc;
        List<double[]> knotCSysLocations = mclnArc.getKnotCSysLocations();
        int arrowSegmentIndex = mclnPolylineArc.getArrowSegmentIndex();
        double[] arrowTipCSysLocation = mclnPolylineArc.getArrowTipCSysLocation();
        mclnGraphPolylineEntity = new MclnGraphPolylineEntity(parentCSys, mclnPolylineArc, knotCSysLocations,
                arrowSegmentIndex, arrowTipCSysLocation, inpNode, outNode);
        arcMclnState = mclnArc.getArcMclnState();
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mclnGraphPolylineEntity.setArcMclnStateColor(stateColor);

        mclnGraphPolylineEntity.createJointPointsAndArrow(knotCSysLocations);

        setDrawKnots(false);
        setSelected(false);
        setThreadSelected(false);
        setAllKnotsSelected(false);

        mclnGraphPolylineEntity.storeArcPersistentAttributesIntoMclnArc(mclnPolylineArc);
    }

    //  *********************************************************************************************************
    //
    //                                M o v i n g   A r c   E l e m e n t s
    //
    //  *********************************************************************************************************

    @Override
    void backupCSysKnots() {
        mclnGraphPolylineEntity.backupJointPoints();
    }

    @Override
    public void restoreBackupCSysKnots() {
        mclnGraphPolylineEntity.restoreBackupJointPointsUponModificationCancelled(mclnPolylineArc);
    }

    @Override
    public void startMoving() {
        mclnGraphPolylineEntity.backupJointPoints();
        mclnGraphPolylineEntity.setDrawKnots(true);
        mclnGraphPolylineEntity.setArrowSelected(true);
    }

    @Override
    public boolean selectAKnotUnderMouse(Point mousePoint) {
        return mclnGraphPolylineEntity.selectAKnotUnderMouse(mousePoint);
    }

    @Override
    public void druggingSelectedArc(Point mousePoint) {
        mclnGraphPolylineEntity.updatePolylineKnotDragged(mousePoint);
    }

    /**
     * This should be called when moving kinors completed
     * finish arc creation should not be called as the arc already created,
     * only location is changed abd should be updated here.
     */
    @Override
    public void movingArcCompleted() {
        setDrawKnots(false);
        setArrowSelected(false);
        setSelected(false);
        mclnGraphPolylineEntity.storeArcPersistentAttributesIntoMclnArc(mclnPolylineArc);
    }

    @Override
    void movingArcInputOrOutputNode(int[] scr0, double scale) {
        updateArcPlacement(scr0, scale);
    }

    @Override
    void movingArcInputOrOutputNodeCompleted() {
        mclnGraphPolylineEntity.storeArcPersistentAttributesIntoMclnArc(mclnPolylineArc);
    }

    //  *********************************************************************************************************
    //
    //                                D e l e t i n g   G r a p h   E n t i t i e s
    //
    //  *********************************************************************************************************

    @Override
    public void prepareForDeletion() {
        mclnGraphPolylineEntity.setHighlighted(true);
        mclnGraphPolylineEntity.setArrowSelected(true);
    }

    @Override
    public void cancelDeletion() {
        mclnGraphPolylineEntity.setHighlighted(false);
        mclnGraphPolylineEntity.setArrowSelected(false);
    }

    @Override
    public MclnArcView clone() {
        MclnPolylineArcView mclnArcViewClone = (MclnPolylineArcView) super.clone();
        if (mclnArcViewClone == null) {
            return null;
        }
        mclnArcViewClone.mclnGraphPolylineEntity = this.mclnGraphPolylineEntity.clone();
        if (mclnArcViewClone.mclnGraphPolylineEntity == null) {
            return null;
        }
        return mclnArcViewClone;
    }

    //
    //   p l a c e m e n t
    //

    @Override
    public void updateArcPlacement(int[] scr0, double scale) {
        double[] inpNodeCSysPnt = inpNode.getCSysPnt();
        double[] outNodeCSysPnt = outNode.getCSysPnt();
        mclnGraphPolylineEntity.updateFirstKnotLocation(inpNodeCSysPnt);
        mclnGraphPolylineEntity.updateLastKnotLocation(outNodeCSysPnt);
        mclnGraphPolylineEntity.findArrowTipLocationAtTheMiddleOfTheSegment();
        mclnGraphPolylineEntity.doCSysToScreenTransformation(scr0, scale);
    }

    //
    //   S e t t i n g   a t t r i b u t e s
    //

    @Override
    public void setUnderConstruction(boolean underConstruction) {
        super.setUnderConstruction(underConstruction);
        mclnGraphPolylineEntity.setUnderConstruction(underConstruction);
    }

    @Override
    public void setMouseHover(boolean mouseHover) {
        super.setMouseHover(mouseHover);
        mclnGraphPolylineEntity.setMouseHover(mouseHover);
    }

    @Override
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
        mclnGraphPolylineEntity.setHidden(hidden);
    }

    @Override
    void setArrowSelected(boolean arrowSelected) {
        mclnGraphPolylineEntity.setArrowSelected(arrowSelected);
    }


    //  *********************************************************************************************************
    //
    //                    M o v i n g   A r c   A s   a   P a r t   o f   a   F r a g m e n t
    //
    //  *********************************************************************************************************

    @Override
    public void backupCurrentState() {
        mclnGraphPolylineEntity.backupJointPoints();
    }

    @Override
    public void translate(double[] translationVector) {
        mclnGraphPolylineEntity.translate(translationVector);
    }


    /**
     * Called when fragment is being moved to paint its last translated phase
     *
     * @param g
     * @param translationVector
     */
    @Override
    public void translateAndPaintEntityAtInterimLocation(Graphics g, double[] translationVector) {
        mclnGraphPolylineEntity.translateAndPaintSplineAtInterimLocation(g, translationVector);
    }

    /**
     * method is used by Move Graph Fragment and Move Entire Model features
     *
     * @param translationVector
     */
    @Override
    public void takeFinalLocation(double[] translationVector) {
        mclnGraphPolylineEntity.takeFinalLocation(translationVector);
        mclnGraphPolylineEntity.storeArcPersistentAttributesIntoMclnArc(mclnPolylineArc);
    }

    @Override
    public void resetToOriginalLocation() {

    }


    //  *********************************************************************************************************
    //
    //                                        D r a w i n g   A r c
    //
    //  *********************************************************************************************************

    /**
     * Called during entity or entity knot drugging
     *
     * @param g
     * @param scr0
     * @param scale
     */
    @Override
    public void drawSpriteEntity(Graphics g, int[] scr0, double scale) {
        // as csys location is changed during drugging
        // screen location should be changed as well
        doCSysToScreenTransformation(scr0, scale);
        // draw arc first
        mclnGraphPolylineEntity.drawPlainEntity(g);
        // draw nodes on the top
        drawConnectedEntities(g, true);
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        mclnGraphPolylineEntity.doCSysToScreenTransformation(scr0, scale);
    }

    /**
     * @param g
     * @param paintExtras
     */
    @Override
    public void newDrawEntityAndConnectedEntity(Graphics g, boolean paintExtras) {
        // draw arc first
        mclnGraphPolylineEntity.drawPlainEntity(g);
        if (paintExtras) {
            paintExtras(g);
        }
        // draw nodes on the top
        drawConnectedEntities(g, paintExtras);
    }

    /**
     * The arc should not paint its connected nodes when it paint
     * itself. This is because many Arcs may be connected to
     * a single node. This means each node input or output arc
     * will repaint the same note multiple times.
     *
     * @param g
     */
    @Override
    public void drawPlainEntity(Graphics g) {
        if (isHidden()) {
            return;
        }
        mclnGraphPolylineEntity.drawPlainEntity(g);
    }

    void paintExtrasAtGivenScreenLocation(Graphics g) {
        System.out.println("NODE PAINT");
        mclnGraphPolylineEntity.paintExtras(g);
        if (inpNode != null) {
            inpNode.drawPlainEntity(g);
        }
        if (outNode != null) {
            System.out.println("NODE PAINT");
            outNode.drawPlainEntity(g);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        mclnGraphPolylineEntity.setSelected(selected);
    }

    @Override
    public boolean isMouseHover(int x, int y) {
        return mclnGraphPolylineEntity.isMouseHover(x, y);
    }

    @Override
    public void setThreadSelected(boolean status) {
//        mclnGraphPolylineEntity.setThreadSelected(status);
    }

    @Override
    public void setDrawKnots(boolean status) {
        mclnGraphPolylineEntity.setDrawKnots(status);
    }

    @Override
    public void setAllKnotsSelected(boolean status) {
//        mclnGraphPolylineEntity.setAllKnotsSelected(status);
    }

    public void setSelectedKnotInd(int selectedKnotIndex) {

    }


    // not Override
    public void placeEntity(int[] scr0, double scale) {
    }


    @Override
    boolean checkIfArrowTipCanBeFound() {
        boolean arrowTipFound = mclnGraphPolylineEntity.checkIfArrowTipCanBeFound();
        return arrowTipFound;
    }

    /**
     * @param x
     * @param y
     * @param userSelectsArrowLocation
     * @return
     */
    public boolean findArrowTipIndexOnThePolyline(int x, int y, boolean userSelectsArrowLocation) {
        boolean arrowLocationFound = false;
        if (userSelectsArrowLocation) {
            arrowLocationFound = mclnGraphPolylineEntity.findArrowTipIndexOnThePolyline(x, y);
        }
//        else {
//            arrowTipSplineScrIndex = mclnGraphPolylineEntity.getTwoMouseClosedKnotsAndFindArrowTipLocation(x, y);
//        }
        return arrowLocationFound;
    }

    /**
     * @return
     */
    @Override
    public String getOneLineInfoMessage() {
        if (inpNode == null || outNode == null) {
            return "Arc creation is not complete.";
        }
        oneLineMessageBuilder.delete(0, oneLineMessageBuilder.length());

        MclnPropertyView mcLnPropertyView;
        MclnConditionView mcLnConditionView;
        MclnArc mclnArc = getTheElementModel();

        if (mclnArc.isRuntimeInitializationUpdatedFlag()) {
            oneLineMessageBuilder.append("* ");
        }

        if (inpNode instanceof MclnPropertyView) {
            mcLnPropertyView = inpNode.toPropertyView();
            oneLineMessageBuilder.append("Recognizing Arc: [");
            oneLineMessageBuilder.append(" ID: " + getUID());

            int nKnots = mclnGraphPolylineEntity.getNKnots();
            oneLineMessageBuilder.append(", Knots = " + nKnots);

            String expectedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Expected State = ");
            oneLineMessageBuilder.append(expectedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = mcLnPropertyView.getCurrentState();
            MclnState calculatedProducedState = mclnArc.getCalculatedProducedState();
            oneLineMessageBuilder.append("  Produced output: ");
            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
            oneLineMessageBuilder.append("  =  P(" + expectedState);
            oneLineMessageBuilder.append(", ");
            oneLineMessageBuilder.append(inpState.getStateName());
            oneLineMessageBuilder.append(");");
        } else {
            mcLnConditionView = inpNode.toConditionView();
            oneLineMessageBuilder.append("Generating Arc: [");
            oneLineMessageBuilder.append(" ID: " + getUID());

            int nKnots = mclnGraphPolylineEntity.getNKnots();
            oneLineMessageBuilder.append(", Knots = " + nKnots);

            String generatedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Generated State = ");
            oneLineMessageBuilder.append(generatedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = mcLnConditionView.getCurrentState();
            MclnState calculatedProducedState = mclnArc.getCalculatedProducedState();
            oneLineMessageBuilder.append("  Produced output: ");
            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
            oneLineMessageBuilder.append("  =  G(" + generatedState);
            oneLineMessageBuilder.append(", ");
            oneLineMessageBuilder.append(inpState.getStateName());
            oneLineMessageBuilder.append(");");
        }

        return oneLineMessageBuilder.toString();
    }

    @Override
    public void setArrowWatermarked(boolean watermarked) {
//        mcLnGraphSplineEntity.setArrowWatermarked(watermarked);
    }
}
