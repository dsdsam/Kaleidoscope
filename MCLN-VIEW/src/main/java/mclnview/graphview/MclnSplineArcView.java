package mclnview.graphview;

import mcln.model.MclnArc;
import mcln.model.MclnNode;
import mcln.model.MclnSplineArc;
import mcln.palette.MclnState;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.List;

public class MclnSplineArcView extends MclnArcView implements Cloneable {

    protected final MclnSplineArc mclnSplineArc; // arc data model
    protected MclnGraphSplineEntity mcLnGraphSplineEntity;

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
    public MclnSplineArcView(MclnGraphView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode) {
        super(parentCSys, mclnArc, inpNode);
        this.mclnSplineArc = (MclnSplineArc) mclnArc;

        mcLnGraphSplineEntity = new MclnGraphSplineEntity(parentCSys, mclnSplineArc, inpNode);
        double[] splineScrStartPoint = inpNode.getScrPnt();

        createFirstScrKnotMakeItActive(splineScrStartPoint);
        addNextScrKnotAndMakeItActive(splineScrStartPoint);

        mcLnGraphSplineEntity.setSelected(true);

        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mcLnGraphSplineEntity.setArcMclnStateColor(stateColor);
    }

    /**
     * Called form Spline Arc Creator as RMB operation
     */
    public void resetArcSpline() {
        mcLnGraphSplineEntity = new MclnGraphSplineEntity(parentCSys, mclnSplineArc, inpNode);
        double[] splineScrStartPoint = inpNode.getScrPnt();

        createFirstScrKnotMakeItActive(splineScrStartPoint);
        addNextScrKnotAndMakeItActive(splineScrStartPoint);

        mcLnGraphSplineEntity.setSelected(true);
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mcLnGraphSplineEntity.setArcMclnStateColor(stateColor);
    }

    /**
     * Editor sets Output Node right when it is picked up
     *
     * @param outNode
     */
    @Override
    public void setOutputNode(MclnGraphNodeView outNode) {
        this.outNode = outNode;
        mcLnGraphSplineEntity.splineCreationCompleted(outNode);
    }

    public void updateLastKnotLocation(double[] knotCSysPnt) {
        mcLnGraphSplineEntity.updateLastKnotLocation(knotCSysPnt);
    }

    /**
     *
     */
    @Override
    public void arcInteractiveCreationFinished() {

        // complete MCLN Arc creation
        MclnNode mclnArcOutputNode = outNode.getTheElementModel();
        mclnSplineArc.setOutNode(mclnArcOutputNode);


        arcMclnState = mclnSplineArc.getArcMclnState();

        // complete spline creation
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mcLnGraphSplineEntity.setArcMclnStateColor(stateColor);
        mcLnGraphSplineEntity.calculate();

        mcLnGraphSplineEntity.storeArcPersistentAttributesIntoMclnArc(mclnSplineArc);
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
    public MclnSplineArcView(MclnGraphView parentCSys, MclnArc mclnArc, MclnGraphNodeView inpNode,
                             MclnGraphNodeView outNode) {
        super(parentCSys, mclnArc, inpNode, outNode);
        this.mclnSplineArc = (MclnSplineArc) mclnArc;

        mcLnGraphSplineEntity = new MclnGraphSplineEntity(parentCSys, mclnSplineArc, inpNode, outNode);

        arcMclnState = mclnSplineArc.getArcMclnState();
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mcLnGraphSplineEntity.setArcMclnStateColor(stateColor);

        List<double[]> knotCSysLocations = mclnSplineArc.getKnotCSysLocations();

        int arrowTipSplineIndex = mclnSplineArc.getArrowTipSplineIndex();
        mcLnGraphSplineEntity.setArrowTipSplineIndex(arrowTipSplineIndex);

        if (knotCSysLocations.size() == 2) {
            double[] midPoint = calculateKnobLocation(0, false, knotCSysLocations.get(0), knotCSysLocations.get(1));
            knotCSysLocations.add(1, midPoint);
            setKnobIndex(1);
        } else {
            setKnobIndex(mclnSplineArc.getKnobIndex());
        }
        if (knotCSysLocations.size() > 3) {
//            System.out.println();
        }
        mcLnGraphSplineEntity.setSplineCSysKnots(knotCSysLocations);

        setDrawKnots(false);
        setSelected(false);
        setThreadSelected(false);
        setAllKnotsSelected(false);
        mcLnGraphSplineEntity.setNoActiveKnots();

        mcLnGraphSplineEntity.calculate();

        mcLnGraphSplineEntity.constructRetrievedArrow(mclnSplineArc, parentCSys, stateColor, inpNode, outNode);
        mcLnGraphSplineEntity.storeArcPersistentAttributesIntoMclnArc(mclnSplineArc);
    }

    @Override
    public MclnArcView clone() {
        MclnSplineArcView mclnArcViewClone = (MclnSplineArcView) super.clone();
        if (mclnArcViewClone == null) {
            return null;
        }
        mclnArcViewClone.mcLnGraphSplineEntity = this.mcLnGraphSplineEntity.clone();
        if (mclnArcViewClone.mcLnGraphSplineEntity == null) {
            return null;
        }
        return mclnArcViewClone;
    }

    //
    //  E n t i t y   a t t r i b u t e   s e t t e r s   a n d   g e t t e r s
    //

    public void setHighlightFlatSegments(boolean highlightFlatSegments) {
        mcLnGraphSplineEntity.setHighlightFlatSegments(highlightFlatSegments);
    }

    public void setSelectingArrowTipLocation(boolean selectingArrowTipLocation) {
        mcLnGraphSplineEntity.setSelectingArrowTipLocation(selectingArrowTipLocation);
    }

    public void destroyArcArrowUpOnUndoingArrowTipSelection() {
        mcLnGraphSplineEntity.destroyArcArrowUpOnUndoingArrowTipSelection();
    }

    public void setHighlightArcKnotsForArrowTipSelection(boolean highlightArcKnotsForArrowTipSelection) {
        this.mcLnGraphSplineEntity.setHighlightArcKnotsForArrowTipSelection(highlightArcKnotsForArrowTipSelection);
    }

    //
    //  *********************************************************************************************************
    //
    //                                            K n o t s
    //
    //  *********************************************************************************************************

    private void createFirstScrKnotMakeItActive(double[] scrPnt) {
        mcLnGraphSplineEntity.createFirstScrKnotMakeItActive(scrPnt);
    }

    public void addNextScrKnotAndMakeItActive(double x, double y) {
        double[] scrPnt = {x, y, 0};
        addNextScrKnotAndMakeItActive(scrPnt);
    }

    private void addNextScrKnotAndMakeItActive(double[] scrPnt) {
        mcLnGraphSplineEntity.addNextScrKnotAndMakeItActive(scrPnt);
    }

    public void addNextScrKnotAndMakePreviousKnotActive(double x, double y) {
        double[] scrPnt = {x, y, 0};
        mcLnGraphSplineEntity.addNextScrKnotAndMakePreviousKnotActive(scrPnt);
    }

    @Override
    public void moveEntityActivePointTo(int x, int y) {
        double[] activePointInCSysCoordinates = parentCSys.screenPointToCSysPoint(x, y);
        moveEntityActivePointTo(activePointInCSysCoordinates);
    }

    @Override
    public void moveEntityActivePointTo(double[] activePointInCSysCoordinates) {
        mcLnGraphSplineEntity.moveEntityActivePointTo(activePointInCSysCoordinates);
    }

    //
    //   RMB  operations
    //

    public int deleteLastPoint() {
        int nPnts = mcLnGraphSplineEntity.deleteLastCSysKnot();
        return nPnts;
    }

    /**
     * Called form Spline Arc Creator as RMB operation
     */
    public void removeOutputNode() {
        setOutputNode(null);
        // after the arc is moved, it is not more straight.
        straightArc = false;
        updateEnds();
        mcLnGraphSplineEntity.calculate();
    }

    public void setSelectedKnotInd(int ind) {
        mcLnGraphSplineEntity.setSelectedKnotInd(ind);
    }

    public int getSelectedKnotIndex() {
        return mcLnGraphSplineEntity.getSelectedKnotIndex();
    }

    public int getNumberOfKnots() {
        return mcLnGraphSplineEntity.getNKnots();
    }


    //  *********************************************************************************************************
    //
    //                                M o v i n g   A r c   E l e m e n t s
    //
    //  *********************************************************************************************************

    @Override
    void backupCSysKnots() {
//        mclnGraphPolylineEntity.backupJointPoints();
    }

    @Override
    public void restoreBackupCSysKnots() {
        mcLnGraphSplineEntity.restoreSplineFromModel();
    }

    @Override
    public void startMoving() {
        mcLnGraphSplineEntity.setAllKnotsSelected(true);
        mcLnGraphSplineEntity.setDrawKnots(true);
        mcLnGraphSplineEntity.setDrawKnotBoxes(true);
        mcLnGraphSplineEntity.setArrowSelected(true);
    }

    @Override
    public boolean selectAKnotUnderMouse(Point point) {
        return mcLnGraphSplineEntity.selectAKnotUnderMouse(point);
    }

    /**
     * Called when user moves model elements
     *
     * @param mousePoint
     */
    @Override
    public void druggingSelectedArc(Point mousePoint) {
        mcLnGraphSplineEntity.updateSplineKnotDragged(mclnSplineArc, mousePoint);
    }

    @Override
    public void movingArcCompleted() {
        setDrawKnots(false);
        setArrowSelected(false);
        setSelected(false);

        arcMclnState = mclnSplineArc.getArcMclnState();

        // complete spline creation
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mcLnGraphSplineEntity.setArcMclnStateColor(stateColor);
        mcLnGraphSplineEntity.calculate();

        mcLnGraphSplineEntity.storeArcPersistentAttributesIntoMclnArc(mclnSplineArc);
    }

    @Override
    void movingArcInputOrOutputNode(int[] scr0, double scale) {
        updateArcPlacement(scr0, scale);
    }

    @Override
    void movingArcInputOrOutputNodeCompleted() {
        mcLnGraphSplineEntity.storeArcPersistentAttributesIntoMclnArc(mclnSplineArc);
    }

    //  *********************************************************************************************************
    //
    //                                D e l e t i n g   G r a p h   E n t i t i e s
    //
    //  *********************************************************************************************************

    @Override
   public void prepareForDeletion() {
        mcLnGraphSplineEntity.setHighlighted(true);
        mcLnGraphSplineEntity.setArrowSelected(true);
//        setHighlighted(true);
    }

    @Override
    public void cancelDeletion() {
        mcLnGraphSplineEntity.setHighlighted(false);
        mcLnGraphSplineEntity.setArrowSelected(false);
    }


    //
    //   D r a w i n g   a t t r i b u t e   s e t t e r s   a n d   g e t t e r s
    //

    @Override
    public void setDrawKnots(boolean status) {
        mcLnGraphSplineEntity.setDrawKnots(status);
    }

    public void setDrawKnotBoxes(boolean status) {
        mcLnGraphSplineEntity.setDrawKnotBoxes(status);
    }

    @Override
    public void setUnderConstruction(boolean underConstruction) {
        super.setUnderConstruction(underConstruction);
        mcLnGraphSplineEntity.setUnderConstruction(underConstruction);
    }

    //   W a t e r m a r k

    @Override
    public void setWatermarked(boolean watermarked) {
        this.watermarked = watermarked;
        mcLnGraphSplineEntity.setWatermarked(watermarked);
    }

    public void setArrowWatermarked(boolean watermarked) {
        mcLnGraphSplineEntity.setArrowWatermarked(watermarked);
    }

    //   P r e - S e l e c t i o n   a n d   S e l e c t i o n

    @Override
    public void setPreSelected(boolean preSelected) {
        mcLnGraphSplineEntity.setSelected(preSelected);
    }

    @Override
    public void setSelected(boolean status) {
        mcLnGraphSplineEntity.setSelected(status);
    }

    @Override
    public void setArrowSelected(boolean arrowSelected) {
        mcLnGraphSplineEntity.setArrowSelected(arrowSelected);
    }

    public boolean isSelected() {
        return mcLnGraphSplineEntity.isSelected();
    }

    public void setAllKnotsSelected(boolean status) {
        mcLnGraphSplineEntity.setAllKnotsSelected(status);
    }

    public void setKnobSelected(boolean status) {
        mcLnGraphSplineEntity.setKnobSelected(status);
    }

    public void setThreadSelected(boolean status) {
        mcLnGraphSplineEntity.setThreadSelected(status);
    }

    //   H i g h l i g h t i n g

    @Override
    public void setHighlighted(boolean highlighted) {
        mcLnGraphSplineEntity.setHighlighted(highlighted);
    }

    public void setArrowHighlighted(boolean highlighted1) {
        mcLnGraphSplineEntity.setArrowHighlighted(highlighted1);
    }

    @Override
    public void setHighlightColor(Color highlightedColor) {
        mcLnGraphSplineEntity.setHighlightColor(highlightedColor);
    }

    @Override
    public boolean isMouseHover(int x, int y) {
        return mcLnGraphSplineEntity.isMouseHover(x, y);
    }

    //   Moving knots


    //   M i s c

    @Override
    public void setMouseHover(boolean mouseHover) {
        super.setMouseHover(mouseHover);
        mcLnGraphSplineEntity.setMouseHover(mouseHover);
    }


    @Override
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
        mcLnGraphSplineEntity.setHidden(hidden);
    }

    /**
     * @param statementRadius
     * @param drawKnobAsArrow
     * @param point01
     * @param point2
     * @return
     */
    private double[] calculateKnobLocation(double statementRadius, boolean drawKnobAsArrow,
                                           double point01[], double point2[]) {

        double inpNodeRadius = (inpNode instanceof MclnPropertyView) ? 8 : 0;
        double outNodeRadius = (outNode instanceof MclnPropertyView) ? 8 : 0;

        double[] alongVec = new double[3];
        double[] dirVec = new double[3];
        double[] radVec = new double[3];
        double[] newVec = new double[3];
        double[] knobLocation = {0., 0., 0.};
        double factRadius;

        if (statementRadius != 0) {
//            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) inpNode).RADIUS);
            VAlgebra.subVec3(alongVec, point01, point2);
            VAlgebra.normalizeVec3(alongVec, dirVec);
            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
            VAlgebra.subVec3(alongVec, alongVec, radVec);
            VAlgebra.addVec3(newVec, point2, alongVec);
            if (drawKnobAsArrow) {
                VAlgebra.LinCom3(knobLocation, 0.25, point2, 0.75, newVec);
            } else {
                VAlgebra.LinCom3(knobLocation, 0.50, point2, 0.50, newVec);
            }
        } else {
//            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) outNode).RADIUS);
            VAlgebra.subVec3(alongVec, point2, point01);
            VAlgebra.normalizeVec3(alongVec, dirVec);
            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
            VAlgebra.subVec3(alongVec, alongVec, radVec);
            VAlgebra.addVec3(newVec, point01, alongVec);
            if (drawKnobAsArrow) {
                VAlgebra.LinCom3(knobLocation, 0.75, point01, 0.25, newVec);
            } else {
                VAlgebra.LinCom3(knobLocation, 0.50, point01, 0.50, newVec);
            }
        }
        return knobLocation;
    }

    public void setKnobIndex(int knobIndex) {
        mcLnGraphSplineEntity.setArcKnobAt(knobIndex);
    }

    /**
     * @param scrKnobLocation
     */
    public void makeThreePointArc(double[] scrKnobLocation) {
        double[] cSysKnobLocation = parentCSys.screenPointToCSysPoint(null, scrKnobLocation);
//        setKnobIndex(1);
        mcLnGraphSplineEntity.updateKnotCSysLocation(1, cSysKnobLocation);
    }


    public void updateActiveScrPoint(double x, double y) {
        mcLnGraphSplineEntity.updateActiveScrPoint(x, y, 0);
        straightArc = false;
    }

    public void updateActiveScrPoint(double x, double y, double z) {
        mcLnGraphSplineEntity.updateActiveScrPoint(x, y, 0);
    }

    //
    //   D e l e t i o n
    //

    public void deletePoint(int ind) {
        mcLnGraphSplineEntity.deleteCSysKnot(ind);
    }

    //
    //  C S y s   r e l a t e d   t r a n s f o r m a t i o n s
    //

    @Override
    public void moveEntity(int x, int y) {
//        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
//        System.out.println("move Entity: " + this.getClass().getSimpleName() + " " + cSysPnt[0]);
    }

    // not Override
    public void placeEntity(int[] scr0, double scale) {
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        mcLnGraphSplineEntity.doCSysToScreenTransformation(scr0, scale);
    }

    //  *********************************************************************************************************
    //
    //                    M o v i n g   A r c   A s   a   P a r t   o f   a   F r a g m e n t
    //
    //  *********************************************************************************************************

    @Override
    public void backupCurrentState() {

    }

    @Override
    public void translate(double[] translationVector) {
        mcLnGraphSplineEntity.translate(translationVector);
    }

    /**
     * Called when fragment is being moved to paint its last translated phase
     *
     * @param g
     * @param translationVector
     */
    @Override
    public void translateAndPaintEntityAtInterimLocation(Graphics g, double[] translationVector) {
        mcLnGraphSplineEntity.translateAndPaintSplineAtInterimLocation(g, translationVector);
    }

    /**
     * method is used by Move Graph Fragment and Move Entire Model features
     *
     * @param translationVector
     */
    @Override
    public void takeFinalLocation(double[] translationVector) {
        mcLnGraphSplineEntity.takeFinalLocation(translationVector);
        List<double[]> knotCSysLocations = mcLnGraphSplineEntity.getSplineCSysKnots();
        mclnSplineArc.setCSysKnots(knotCSysLocations);
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
        mcLnGraphSplineEntity.drawPlainEntity(g);
        // draw nodes on the top
        drawConnectedEntities(g, false);
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
        mcLnGraphSplineEntity.drawPlainEntity(g);
    }

    @Override
    public void newDrawEntityAndConnectedEntity(Graphics g, boolean paintExtras) {
        // draw arc first
        mcLnGraphSplineEntity.drawPlainEntity(g);
        if (paintExtras) {
            paintExtras(g);
        }
        // draw nodes on the top
        drawConnectedEntities(g, paintExtras);
    }

    @Override
    public void drawEntityOnlyAtInterimLocation(Graphics g) {
        mcLnGraphSplineEntity.drawEntityOnlyAtInterimLocation(g);
    }

    @Override
    public void drawEntityAndConnectedEntityExtras(Graphics g, boolean paintExtras) {
        // draw arc first
        mcLnGraphSplineEntity.drawPlainEntity(g);
        if (paintExtras) {
            paintExtras(g);
        }
        // draw nodes on the top
        drawConnectedEntities(g, paintExtras);
    }

    //
    //   O L D   p a i n t i n g
    //

    @Override
    public void draw(Graphics g) {
        if (isHidden()) {
            return;
        }
        g.setColor(getDrawColor());
        mcLnGraphSplineEntity.draw(g);
    }

    void paintExtrasAtGivenScreenLocation(Graphics g) {
        mcLnGraphSplineEntity.paintExtras(g);
        if (inpNode != null) {
            inpNode.drawPlainEntity(g);
        }
        if (outNode != null) {
            outNode.drawPlainEntity(g);
        }
    }

    //
    //   p l a c e m e n t
    //

    @Override
    public void updateArcPlacement(int[] scr0, double scale) {
        double[] inpNodeCSysPnt = inpNode.getCSysPnt();
        double[] outNodeCSysPnt = outNode.getCSysPnt();
        mcLnGraphSplineEntity.updateFirstKnotLocation(inpNodeCSysPnt);
        mcLnGraphSplineEntity.updateLastKnotLocation(outNodeCSysPnt);
        mcLnGraphSplineEntity.updateArrowPositionWhenArcNodeMoved(mclnSplineArc);
        mcLnGraphSplineEntity.doCSysToScreenTransformation(scr0, scale);
    }

    //
    //   Placing Arc Arrow on the spline
    //

    @Override
    public boolean checkIfArrowTipCanBeFound() {
        boolean arrowTipFound = mcLnGraphSplineEntity.checkIfArrowTipCanBeFound();
        return arrowTipFound;
    }

    /**
     * This method is called from Spline Arc Creator to let user to place Arc Arrow
     * by moving the mouse to desired a point
     *
     * @param x
     * @param y
     * @param userSelectsArrowLocation
     * @return
     */
    public int findArrowTipIndexOnTheSplineForJustCreatedArc(int x, int y, boolean userSelectsArrowLocation) {
        int arrowTipSplineScrIndex;
        if (userSelectsArrowLocation) {
            arrowTipSplineScrIndex = mcLnGraphSplineEntity.findArrowTipIndexOnTheSplineForJustCreatedArc(x, y);
        } else {
            arrowTipSplineScrIndex = mcLnGraphSplineEntity.getTwoMouseClosedKnotsAndFindArrowTipLocation(x, y);
        }
        return arrowTipSplineScrIndex;
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

        if (mclnSplineArc.isRuntimeInitializationUpdatedFlag()) {
            oneLineMessageBuilder.append("* ");
        }

        if (inpNode instanceof MclnPropertyView) {
            mcLnPropertyView = inpNode.toPropertyView();
            oneLineMessageBuilder.append("Recognizing Arc: [");
            oneLineMessageBuilder.append(" ID: " + getUID());

            int nKnots = mcLnGraphSplineEntity.getNKnots();
            oneLineMessageBuilder.append(", Knots = " + nKnots);

            String expectedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Expected State = ");
            oneLineMessageBuilder.append(expectedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = mcLnPropertyView.getCurrentState();
            MclnState calculatedProducedState = mclnSplineArc.getCalculatedProducedState();
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

            int nKnots = mcLnGraphSplineEntity.getNKnots();
            oneLineMessageBuilder.append(", Knots = " + nKnots);

            String generatedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Generated State = ");
            oneLineMessageBuilder.append(generatedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = mcLnConditionView.getCurrentState();
            MclnState calculatedProducedState = mclnSplineArc.getCalculatedProducedState();
            oneLineMessageBuilder.append("  Produced output: ");
            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
            oneLineMessageBuilder.append("  =  G(" + generatedState);
            oneLineMessageBuilder.append(", ");
            oneLineMessageBuilder.append(inpState.getStateName());
            oneLineMessageBuilder.append(");");
        }
        return oneLineMessageBuilder.toString();
    }
}
