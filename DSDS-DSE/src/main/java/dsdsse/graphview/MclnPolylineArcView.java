package dsdsse.graphview;

import adf.csys.view.CSysView;
import mcln.model.MclnArc;
import mcln.model.MclnNode;
import mcln.palette.MclnState;

import java.awt.*;

public class MclnPolylineArcView extends MclnArcView implements Cloneable {

    private static final Color DEFAULT_ARC_STATE_COLOR = Color.GRAY;

    //    private MclnGraphViewNode inpNode;
//    private MclnGraphViewNode outNode;
    private MclnGraphPolylineEntity mclnGraphPolylineEntity;

    //
    //  C r e a t i o n   m e t h o d s
    //

    /**
     * Creates incomplete Arc with Input Node only - used when the arc is created by user
     *
     * @param parentCSys
     * @param mclnArc
     * @param inpNode
     */
    MclnPolylineArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphViewNode inpNode) {
        super(parentCSys, mclnArc, inpNode);
        initBeingCreatedArc(parentCSys, inpNode);
    }

    private void initBeingCreatedArc(CSysView parentCSys, MclnGraphViewNode inpNode) {
        mclnGraphPolylineEntity = new MclnGraphPolylineEntity(parentCSys, inpNode);
        double[] cSysPnt = inpNode.getCSysPnt();
        mclnGraphPolylineEntity.createFirstCSysKnot(cSysPnt);
        mclnGraphPolylineEntity.addNextCSysKnot(cSysPnt);

        mclnGraphPolylineEntity.setSelected(true);

        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        mclnGraphPolylineEntity.setArcMclnStateColor(stateColor);
    }

    int getJointsSize() {
        return mclnGraphPolylineEntity.getJointsSize();
    }

    void addNextCSysKnot(double[] cSysPnt) {
        mclnGraphPolylineEntity.addNextCSysKnot(cSysPnt);
    }

    public boolean updateLastPoint(double[] jointPoint) {
        return mclnGraphPolylineEntity.updateLastPoint(jointPoint);
    }

    /**
     * Editor sets Output Node right when it is picked up
     *
     * @param outNode
     */
    public void setOutputNode(MclnGraphViewNode outNode) {
        this.outNode = outNode;
//        double[] cSysPnt = inpNode.getCSysPnt();
//        mclnGraphPolylineEntity.createFirstCSysKnot(cSysPnt);
//        mclnGraphPolylineEntity.addNextCSysKnot(cSysPnt);
        mclnGraphPolylineEntity.polylineCreationCompleted(outNode);
    }

      boolean removeLastSegment() {
        boolean hasMoreSegments = mclnGraphPolylineEntity.removeLastSegment();
//        polylinePointCounter = hasMoreSegments ? --polylinePointCounter : 0;
        return true;
    }

    /**
     *
     */
    void finishArcCreation() {

        mclnGraphPolylineEntity.setSelected(false);
        mclnGraphPolylineEntity.setHighlightLongestSegment(false);

        // complete MCLN Arc creation
        MclnNode mclnArcOutputNode = outNode.getTheElementModel();
        mclnArc.setOutNode(mclnArcOutputNode);
//        List<double[]> knotCSysLocations = this.getKnotCSysLocations();
//        mclnArc.setCSysKnots(knotCSysLocations);

        arcMclnState = mclnArc.getArcMclnState();

        // complete polyline creation
        Color stateColor = (arcMclnState != null) ? new Color(arcMclnState.getRGB()) : DEFAULT_ARC_STATE_COLOR;
        //  mclnGraphPolylineEntity.setArcColor(stateColor);

        mclnGraphPolylineEntity.arcCreationCompleted();


    }

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
    MclnPolylineArcView(MclnGraphDesignerView parentCSys, MclnArc mclnArc, MclnGraphViewNode inpNode,
                        MclnGraphViewNode outNode) {
        super(parentCSys, mclnArc, inpNode, outNode);
    }

    @Override
    void backupCSysKnots() {
        mclnGraphPolylineEntity.backupJointPoints();
    }

    @Override
    void restoreCSysKnots() {
        mclnGraphPolylineEntity.restoreJointPoints();
    }

    @Override
    public MclnArcView clone() {
        MclnPolylineArcView mclnArcViewClone = (MclnPolylineArcView) super.clone();
        if (mclnArcViewClone == null) {
            return null;
        }
        //  mclnArcViewClone.mcLnGraphSplineEntity = this.mcLnGraphSplineEntity.clone();
        return mclnArcViewClone;
    }

    //
    //   Moving knots
    //

    boolean selectAKnotUnderMouse(Point mousePoint) {
        return mclnGraphPolylineEntity.selectAKnotUnderMouse(mousePoint);
    }

    void updatePolylineKnotDragged(Point mousePoint) {
        mclnGraphPolylineEntity.updatePolylineKnotDragged(mousePoint);
    }

    //
    //   p l a c e m e n t
    //

    @Override
    public void updateArcPlacement(int[] scr0, double scale) {
        double[] inpNodeCSysPnt = inpNode.getCSysPnt();
        double[] outNodeCSysPnt = outNode.getCSysPnt();
//        mclnGraphSplineViewEntity.updateFirstKnotLocation(inpNodeCSysPnt);
//        mclnGraphSplineViewEntity.updateLastKnotLocation(outNodeCSysPnt);
//        mclnGraphSplineViewEntity.doCSysToScreenTransformation(scr0, scale);
    }

    //
    //   S e t t i n g   a t t r i b u t e s
    //

    @Override
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
        mclnGraphPolylineEntity.setHidden(hidden);
    }

    //
    //   D r a w i n g   A r c
    //

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
        drawConnectedEntities(g, false);
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
//        if (paintExtras) {
//            paintExtras(g);
//        }
        // draw nodes on the top
        drawConnectedEntities(g, paintExtras);
    }

    @Override
    public void drawPlainEntity(Graphics g) {
//        System.out.println("drawPlain Polyline Arc");
        if (isHidden()) {
            return;
        }
        mclnGraphPolylineEntity.drawPlainEntity(g);
    }

    void paintExtrasAtGivenScreenLocation(Graphics g) {
//        mcLnGraphSplineEntity.paintExtras(g);
        if (inpNode != null) {
            inpNode.drawPlainEntity(g);
        }
        if (outNode != null) {
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
//        mcLnGraphSplineEntity.setThreadSelected(status);
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

    @Override
    public void translateAndPaintEntityAtInterimLocation(Graphics g, double[] translationVector) {
//        mcLnGraphSplineEntity.translateAndPaintSplineAtInterimLocation(g, translationVector);
    }

    @Override
    public void resetToOriginalLocation() {

    }

    // not Override
    public void placeEntity(int[] scr0, double scale) {
    }

    //
    //  M o v i n g   t h e   A r c
    //

    @Override
    public void translate(double[] translationVector) {
//        mcLnGraphSplineEntity.translate(translationVector);
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
    boolean findArrowTipIndexOnThePolyline(int x, int y, boolean userSelectsArrowLocation) {
        boolean arrowLocationFound = false;
        if (userSelectsArrowLocation) {
//            System.out.println("Mouse moving Working with arrow x = " + x + ",  y = " + y);
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

        MclnPropertyView mclnPropertyView;
        MclnConditionView mclnConditionView;
        MclnArc mclnArc = getMclnArc();

        if (mclnArc.isRuntimeInitializationUpdatedFlag()) {
            oneLineMessageBuilder.append("* ");
        }

        if (inpNode instanceof MclnPropertyView) {
            mclnPropertyView = inpNode.toPropertyView();
            oneLineMessageBuilder.append("Recognizing Arc: [");
            oneLineMessageBuilder.append(" ID: " + getUID());

            String expectedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Expected State = ");
            oneLineMessageBuilder.append(expectedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = mclnPropertyView.getCurrentState();
            MclnState calculatedProducedState = mclnArc.getCalculatedProducedState();
            oneLineMessageBuilder.append("  Produced output: ");
            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
            oneLineMessageBuilder.append("  =  P(" + expectedState);
            oneLineMessageBuilder.append(", ");
            oneLineMessageBuilder.append(inpState.getStateName());
            oneLineMessageBuilder.append(");");
        } else {
            mclnConditionView = inpNode.toConditionView();
            oneLineMessageBuilder.append("Generating Arc: [");
            oneLineMessageBuilder.append(" ID: " + getUID());

            String generatedState = (arcMclnState != null ? arcMclnState.getStateName() : "NA");
            oneLineMessageBuilder.append(", Generated State = ");
            oneLineMessageBuilder.append(generatedState);
            oneLineMessageBuilder.append(" ]");

            MclnState inpState = mclnConditionView.getCurrentState();
            MclnState calculatedProducedState = mclnArc.getCalculatedProducedState();
            oneLineMessageBuilder.append("  Produced output: ");
            oneLineMessageBuilder.append(calculatedProducedState.getStateName());
            oneLineMessageBuilder.append("  =  G(" + generatedState);
            oneLineMessageBuilder.append(", ");
            oneLineMessageBuilder.append(inpState.getStateName());
            oneLineMessageBuilder.append(");");
        }


//        oneLineMessageBuilder.append(", Calculated State = ");

        return oneLineMessageBuilder.toString();
    }

    @Override
    public void setArrowWatermarked(boolean watermarked) {
//        mcLnGraphSplineEntity.setArrowWatermarked(watermarked);
    }

//    public void saveLocation()
////    {
////        double[] curPnt = null;
////        double[] pnt;
////
////// System.out.println( " Arc: saveLocation " + indPoints);
/////*
//// if ( indPoints != -1)
//// {
////  savedSelectedIndex = indPoints;
////  savedMovePnt = (double[])splinePoints.elementAt(indPoints);
//// } else
////*/
////        {
////            savedPoints.removeAllElements();
////
////            int nPnts = spline.getNKnots();
////            //System.out.println( " Arc: saveLocation " + indPoints);
////            for( int i=0; i < nPnts; i++ )
////            {
////                curPnt = spline.copyKnotFromKnotArr( i );
//////System.out.println( name +"  "+curPnt[0] +"  "+curPnt[1]);
////                savedPoints.addElement( curPnt );
////            }
////        }
////    }
////    // --------------------------------------------------------
////    public void restoreLocation()
////    {
////        double[] curPnt = null;
////// double[] curPnt2 = null;
//////    System.out.println( " Arc: restoreLocation " + indPoints);
/////*
//// if ( savedSelectedIndex != -1)
//// {
////  splinePoints.setElementAt( savedMovePnt, savedSelectedIndex);
////  savedSelectedIndex = -1;
//// } else
////*/
////        int nPnts = savedPoints.size();
//////System.out.println( " Arc: restoreLocation n Pnts  " + nPnts);
////        for( int i=0; i < nPnts; i++ )
////        {
////            curPnt = (double[])savedPoints.elementAt(i);
//////    curPnt2 = (double[])splinePoints.elementAt(i);
////            spline.copyKnotToKnotArr( curPnt, i );
////// System.out.println( name +"  "+curPnt[0] +"  "+curPnt[1]);
//////     System.out.println( name +    "       "+ curPnt1[0] );
//////    points.addElement( curPnt );
//////     RtsVAlgebra.copyVec3( curPnt2, curPnt1 );
//////    curPnt = (double[])points.elementAt(i);
////            //    System.out.println( name +    "       "+ curPnt[0] );
////        }
////        spline.updateSpline();
////// savedPoints.removeAllElements();
////    }


//    private synchronized void moveElement(MouseEvent me, int eventType) {
//        // System.out.println( "moveElement" );
//
//        // Reset process if Right Mouse
//        // Button is pressed
//        if (eventType == MouseEvent.MOUSE_PRESSED) {
//            if (operationStep == TAKE_ELEMENT) {
//                if (isRMBPicked(me)) {
//                    if (canRestoreLocation && moveObj != null) {
//                        moveObj.resetStateAndRedraw();
//                        moveObj.restoreLocation();
//                        canRestoreLocation = false;
//                        operationStep = TAKE_ELEMENT;
//                        repaint();
//                    } else {
//                        // System.out.println("moveElement");
//                        set_ActionAndOperation(SET_STATE, DO_NOTHING);
//                        setEditingLocked(false);
//                    }
//                    moveObj = null;
//                    return;
//                }
//
//                if (canRestoreLocation) {
//                    canRestoreLocation = false;
//                    // setEditingLocked( false );
//                }
//
//                // if (selObj != null)
//                // {// the object was selected, but new operation is activated
//                // so, reset selected obj
//                // selObj.resetStateAndRedraw();
//                // selObj.updateDisplay();
//                // selObj = null;
//                // }
//                if ((moveObj = isSomethingSelected(me)) != null) {
//                    /*
//                     * if (selObj.type == RtsMVNObject.NODE)
//                     * System.out.println("moveElement " +"NODE NODE NODE "); if
//                     * (selObj.type == RtsMVNObject.TRANSITION)
//                     * System.out.println("moveElement "
//                     * +"TRANSITION TRANSITION TRANSITION "); if (selObj.type ==
//                     * RtsMVNObject.ARC) System.out.println(selObj.name +
//                     * " moveElement " +"ARC ARC ARC ");
//                     */
//                    operationStep = TAKE_ELEMENT_NEW_PLACE;
//                    moveObj.setStateSelectedAndRedraw();
//                    moveObj.saveLocation();
//                    // repaint();
//                    return;
//                }
//            } // end of if (operationStep == TAKE_ELEMENT)
//        } // end of if (eventType == MouseEvent.MOUSE_PRESSED )
//
//        /*
//         * if (eventType == MouseEvent.MOUSE_MOVED) { if (operationStep ==
//         * TAKE_ELEMENT_NEW_PLACE) { System.out.println("moveElement "
//         * +"call ChangePosition "); selObj.changePosition(me);
//         * setProjModified(); repaint(); return; } } // End of if (eventType ==
//         * MouseEvent.MOUSE_MOVED)
//         */
//        if (eventType == MouseEvent.MOUSE_DRAGGED) {
//            if (operationStep == TAKE_ELEMENT_NEW_PLACE) {
//                if (moveStage == 0) {
//                    startMoveElement(moveObj);
//                    moveStage = 1;
//                } else {
//                    proceedMoveElement(moveObj);
//                    // System.out.println("moveElement "
//                    // +"call ChangePosition ");
//                }
//                snapMousePointToTheGrid(me, false);
//                moveObj.changePosition(me);
//                proceedMoveElement(moveObj);
//                canRestoreLocation = true;
//                setProjModified();
//                setEditingLocked(true);
//                // redisplay();
//                return;
//            }
//        } // End of if (eventType == MouseEvent.MOUSE_DRAGGED)
//
//        if (eventType == MouseEvent.MOUSE_RELEASED) {
//            if (isRMBPicked(me))
//                return;
//
//            if (operationStep == TAKE_ELEMENT_NEW_PLACE) {
//                // movement is done
//                if (moveObj != null) {
//                    proceedMoveElement(moveObj);
//                    // System.out.println("moveElement " +"MOUSE_RELEASED" );
//
//                    moveObj.resetStateAndRedraw();
//
//                    // selObj.redrawWithDep( true );
//                    if (moveObj.type == RtsMVNObject.ARC) {
//                        // ((RtsMCN_Arc)selObj).setDrawKnotsFlag( true );
//                        moveObj.redrawWithChildren(1);
//                        // ((RtsMCN_Arc)selObj).setDrawKnotsFlag( false );
//                    } else
//                        moveObj.redrawWithChildren(2);
//
//                }
//                // repaint();
//                moveStage = 0;
//                // moveObj = null;
//                operationStep = TAKE_ELEMENT;
//                return;
//            }
//
//        } // End of if (eventType == MouseEvent.MOUSE_RELEASED)
//
//    } // end of moveElement
}
