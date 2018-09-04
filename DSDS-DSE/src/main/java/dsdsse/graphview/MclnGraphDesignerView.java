package dsdsse.graphview;

import adf.csys.view.BasicCSysEntity;
import adf.csys.view.CSysRectangleEntity;
import adf.csys.view.DoubleRectangle;
import adf.utils.StandardFonts;
import dsdsse.history.ExecutionHistoryPanel;
import dsdsse.preferences.DsdsseUserPreference;
import mcln.model.*;
import mclnview.graphview.*;

import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Feb 8, 2013
 * Time: 9:35:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnGraphDesignerView extends MclnGraphView {

    private final Color[] axesColors =
            {
                    (new Color(0xFF0000)), (new Color(0xFF0000)), (new Color(0xFF0000)),
                    (new Color(0xFF0000)), (new Color(0xFF0000)), (new Color(0xFF0000)),
            };

    // Creating Arc.
    private MclnGraphNodeView createdArcInputNodeView;
    // This is Arc View sprite;
    private MclnArcView mclnArcViewSprite;

    private MclnGraphEntityView mclnGraphSpriteEntity;

    // Creating fragment
    private MclnPropertyView curFragmentInpNode;
    private MclnPropertyView curFragmentOutNode;

    // Moving fragment
    private final Set<MclnGraphNodeView> nodesInInterimLocation = new HashSet();
    private final Set<MclnArcView> arcsInInterimLocation = new HashSet();
    private final Set<MclnArcView> selectedFragmentConnectingArcs = new HashSet();

    // Moving entire model
    private final List<MclnGraphNodeView> spriteNodesAreBeingMovedList = new ArrayList();

    // Deleting model entity
    private MclnGraphEntityView modelEntityToBeDeleted;

    private MclnGraphEntityView mouseHoveredEntityView;

    //=================================================================================================================

    //
    //   Methods to create McLN Graph elements
    //

    /**
     * @param mclnStatement
     */
    @Override
    public MclnPropertyView createMclnStatementView(MclnStatement mclnStatement) {
        McLnGraphDesignerPropertyView mcLnPropertyView = MclnGraphDesignerViewFactory.
                createMclnGraphDesignerPropertyView(this, mclnStatement);
        // adding Statement view to the Graph
        statementViews.add(mcLnPropertyView);
        graphModelViewNodeEntityList.add(mcLnPropertyView);
        uidToMclnGraphNodeView.put(mcLnPropertyView.getUID(), mcLnPropertyView);
        return mcLnPropertyView;
    }

    /**
     * @param mclnCondition
     */
    @Override
    public MclnConditionView createMclnConditionView(MclnCondition mclnCondition) {
        MclnGraphDesignerConditionView mcLnConditionView = MclnGraphDesignerViewFactory.createMclnGraphDesignerConditionView(
                this, mclnCondition);
        // adding Condition view to the Graph
        conditionViews.add(mcLnConditionView);
        graphModelViewNodeEntityList.add(mcLnConditionView);
        uidToMclnGraphNodeView.put(mcLnConditionView.getUID(), mcLnConditionView);
        return mcLnConditionView;
    }

    /**
     * @param mclnArc
     * @return
     */
    @Override
    public MclnArcView createIncompleteMclnArcView(MclnArc mclnArc) {
        String inpNodeUID = mclnArc.getInpNodeUID();
        MclnGraphNodeView inpNodeView = uidToMclnGraphNodeView.get(inpNodeUID);
        MclnArcView mclnArcView = null;
        if (mclnArc instanceof MclnPolylineArc) {
            mclnArcView = MclnGraphDesignerViewFactory.createIncompleteMclnGraphDesignerPolylineArcView(
                    this, mclnArc, inpNodeView);
        } else if (mclnArc instanceof MclnSplineArc) {
            mclnArcView = MclnGraphDesignerViewFactory.createIncompleteMclnGraphDesignerSplineArcView(
                    this, mclnArc, inpNodeView);
        }
        addMclnGraphArcView(mclnArcView);
        return mclnArcView;
    }

    /**
     * @param mclnArc
     */
    @Override
    public MclnArcView createMclnArcView(MclnArc mclnArc) {
        String inpNodeUID = mclnArc.getInpNodeUID();
        String outNodeUID = mclnArc.getOutNodeUID();
        MclnGraphNodeView inpNode = uidToMclnGraphNodeView.get(inpNodeUID);
        MclnGraphNodeView outNode = uidToMclnGraphNodeView.get(outNodeUID);
        MclnArcView mclnArcView = MclnGraphDesignerViewFactory.createMclnGraphDesignerArcView(
                this, mclnArc, inpNode, outNode);
        inpNode.addOutputArc(mclnArcView);
        outNode.addInputArc(mclnArcView);

        // adding Arc view to the Graph
        addMclnGraphArcView(mclnArcView);
        return mclnArcView;
    }

    @Override
    protected void onMclnModelCleared() {
      /*
        We call removeListener to ask mclnGraphModel to remove listener from destroyed Mcln Model.
        It is removed when model is cleared
       */
        clearInterimEntityCollections();
        super.onMclnModelCleared();
        ExecutionHistoryPanel.getInstance().clearUpOnModelErased();
    }

    //
    // Called from base class Mcln Model Simulation Listener implementation
    //

    @Override
    protected void onSimulationStateChange() {
        regenerateGraphView();
        repaintAllModelNodesOnOffScreenImage();
    }

    @Override
    protected void onSimulationStepExecuted() {
        int historySize = 0;
        for (MclnPropertyView mcLnPropertyView : statementViews) {
            historySize = mcLnPropertyView.recordHistory();
        }
        ExecutionHistoryPanel.getInstance().simulationStepExecuted(historySize);
    }

    @Override
    protected void onSimulationStateReset() {
        ExecutionHistoryPanel.getInstance().simulationExecutionReset();
    }

//    //=================================================================================================================

    //
    //   C o n s t r u c t i n g   M c l n   G r a p h   V i e w
    //

    /**
     * @param mclnGraphModel
     * @param options
     */
    public MclnGraphDesignerView(MclnGraphModel mclnGraphModel, int viewPadding, int options,
                                 MclnGraphViewDefaultProperties mclnGraphViewDefaultProperties) {
        super(mclnGraphModel, viewPadding, options, mclnGraphViewDefaultProperties);
        setBorder(new LineBorder(Color.GRAY));
    }

    //
    //   I n i t i a l i z a t i o n
    //

    /**
     * called to initialize this instance upon creation
     */
    @Override
    protected void initGridAndModelRectangle() {
        super.initGridAndModelRectangle();
        // add what you need below this line
    }

    //=================================================================================================================

    //
    //   V i e w   U p d a t e s   a n d   G e t t e r s
    //

    /**
     * called when base class (CSysView) model rectangle is reset
     */
    @Override
    protected void onProjectCSysRectangleReset() {
        dsdsseCSysGridEntity.updateProjectRectangle();
    }


    /**
     * Returns Property View list
     *
     * @return
     */
    public List<MclnPropertyView> getPropertyViewList() {
        List<MclnPropertyView> propertyViewListClone = new ArrayList(statementViews);
        return propertyViewListClone;
    }


    //=================================================================================================================
    //   W o r k i n g    w i t h    o f f    S c r e e    I m a g e
    //=================================================================================================================

    public void repaintImageAndSpriteEntities() {
        repaint();
    }

    //=================================================================================================================

    //
    //   Painting Design Space details: axes, grid and coordinates
    //

    //=================================================================================================================

    @Override
    protected boolean isWorldVisible() {
        return true;
    }

    @Override
    protected boolean areAxesVisible() {
        return true;
    }

    @Override
    protected boolean isProjectSpaceRectangleVisible() {
        return DsdsseUserPreference.isProjectSpaceRectangleVisible();
    }

    /**
     * This method paints Design Space Grid
     *
     * @param g
     */
    @Override
    protected void paintWorld(Graphics g) {
        if (!DsdsseUserPreference.isGriVisible()) {
            return;
        }
        int nElements = viewWorldEntityArray.length;
        for (int i = 0; i < nElements; i++) {
            CSysRectangleEntity cSysEntity = viewWorldEntityArray[i];
            cSysEntity.draw(g);
        }
    }

    /**
     * This method paints Design Space Axes
     *
     * @param g
     */
    @Override
    protected void paintAxes(Graphics g) {
        if (!DsdsseUserPreference.areAxesVisible()) {
            return;
        }
        g.setColor(Color.RED);
        super.paintAxes(g);
    }

    /**
     * @param g
     */
    private void drawProjectSpaceCoordinates(Graphics g) {

        if (!DsdsseUserPreference.isProjectSpaceRectangleVisible()) {
            return;
        }

        Color COORDINATE_PAD_BACKGROUND = new Color(0xEEEEEE);

        Font font = StandardFonts.FONT_MONOSPACED_BOLD_10;

        g.setFont(font);

        int coordinatesPadHeight = 18;

        String text = "";
        DoubleRectangle projectSpaceRect = getCSysRectangle();
        StringBuilder stringBuilder = new StringBuilder();

        // upper left corner

        stringBuilder.append("(").append(projectSpaceRect.getX()).append(" , ").
                append(projectSpaceRect.getY()).append(")");
        text = stringBuilder.toString();
        int coordinatesPadWidth = g.getFontMetrics(font).stringWidth(text);


        int border = 6;
        // draw pad

        g.setColor(COORDINATE_PAD_BACKGROUND);
        g.fillRect(projectSpaceViewXMin, projectSpaceViewYMin - coordinatesPadHeight - 5,
                coordinatesPadWidth + border, coordinatesPadHeight);

        // draw coordinates
        g.setColor(Color.DARK_GRAY);
        g.drawString(text, projectSpaceViewXMin + border / 2, projectSpaceViewYMin - 11);

        // lower right corner

        stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(projectSpaceRect.getRightX()).append(" , ").
                append(projectSpaceRect.getLowerY()).append(")");
        text = stringBuilder.toString();
        coordinatesPadWidth = g.getFontMetrics(font).stringWidth(text);

        // draw pad
        g.setColor(COORDINATE_PAD_BACKGROUND);
        g.fillRect(projectSpaceViewXMax - coordinatesPadWidth - border / 2 - 2, projectSpaceViewYMax + 5,
                coordinatesPadWidth + border, coordinatesPadHeight);

        g.setColor(Color.DARK_GRAY);
        g.drawString(text, projectSpaceViewXMax - coordinatesPadWidth + 1 - border / 2,
                projectSpaceViewYMax + coordinatesPadHeight);

    }

    //=================================================================================================================

    /**
     * @param me
     */
    void snapToGridLine(MouseEvent me) {
        dsdsseCSysGridEntity.snapMouseToTheGrid(me);
    }

    //=================================================================================================================

    //
    //   S c a l i n g   a n d   t r a n s f o r m a t i o n s
    //

    /**
     * The method updates entire model entity view screen
     * coordinates after cSys coordinates are changed
     * 1) model created or changed
     * 2) view changed: re-sized, rotated, or distorted
     *
     * @param mat43
     */
    @Override
    public final void updateCSysEntList(double[][] mat43) {
        super.updateCSysEntList(mat43);
        // scale interim Arcs
        super.scaleEntityCollection(arcsInInterimLocation);
    }

    //=================================================================================================================

    //
    //   S e l e c t i o n
    //

    public MclnGraphEntityView getOtherThanThisEntityAtCoordinates(MclnGraphEntityView thisEntity, int x, int y) {
        for (MclnGraphNodeView mclnGraphViewNode : graphModelViewNodeEntityList) {

            if (mclnGraphViewNode != thisEntity && mclnGraphViewNode.isMouseHover(x, y)) {
                return mclnGraphViewNode;
            }
        }
        for (MclnArcView mclnArcView : arcViews) {
            if (mclnArcView != thisEntity && mclnArcView.isMouseHover(x, y)) {
                return mclnArcView;
            }
        }
        return null;
    }


    public MclnGraphEntityView getGraphEntityAtCoordinates(int x, int y) {
        MclnGraphNodeView mclnGraphViewNode = getGraphNodeAtCoordinates(x, y);
        if (mclnGraphViewNode != null) {
            return mclnGraphViewNode;
        }
        MclnArcView mclnArcView = getArcAtCoordinates(x, y);
        if (mclnArcView != null) {
            return mclnArcView;
        }
        return null;
    }

    public MclnGraphNodeView getGraphNodeAtCoordinates(int x, int y) {
        for (MclnGraphNodeView mclnGraphViewNode : graphModelViewNodeEntityList) {
            if (mclnGraphViewNode.isMouseHover(x, y)) {
                return mclnGraphViewNode;
            }
        }
        return null;
    }

    public MclnPropertyView getPropertyNodeAtCoordinates(int x, int y) {
        for (MclnPropertyView mcLnPropertyView : statementViews) {
            if (mcLnPropertyView.isMouseHover(x, y)) {
                return mcLnPropertyView;
            }
        }
        return null;
    }

    public MclnConditionView getConditionAtCoordinates(int x, int y) {
        for (MclnConditionView mcLnConditionView : conditionViews) {
            if (mcLnConditionView.isMouseHover(x, y)) {
                return mcLnConditionView;
            }
        }
        return null;
    }

    public MclnArcView getArcAtCoordinates(int x, int y) {
        for (MclnArcView mclnArcView : arcViews) {
            if (mclnArcView.isMouseHover(x, y)) {
                return mclnArcView;
            }
        }
        return null;
    }

    public void placeEntity(BasicCSysEntity mclnGraphViewEntity) {
        mclnGraphViewEntity.placeEntity(scr0, minScale);

    }

    //=================================================================================================================

    //
    //   S e r c h   f o r   m o d e l   e l e m e n t s
    //

    public MclnGraphNodeView getMclnNodeByID(String propertyNodeID) {
        return uidToMclnGraphNodeView.get(propertyNodeID);
    }

    public MclnArcView getMclnArcByID(String arcID) {
        return uidToMclnGraphArcView.get(arcID);
    }

    //=================================================================================================================

    //
    //   P a i n t i n g   w h i l e   c o n s t r u c t i o n   A r c s   a n d
    //   P r o p e r t y - C o n d i t i o n - P r o p e r t y   f r a g m e n t s
    //

    //=================================================================================================================

    //
    //   P a i n t i n g   A r c   w h i l e   i t   i s   b e i n g   c r e a t e d
    //

    void setArcInputNodeWhileCreatingArc(MclnGraphNodeView arcInputNodeView) {
        // this createdArcInputNodeView will be used for very short time
        // after the Arc input node clicked only. As soon as user
        // start moving the arc's active point the arc input node
        // will be repainted as part of the arc dependent nodes
        this.createdArcInputNodeView = arcInputNodeView;
        repaint();
    }

    /**
     * is used to paint Arc thread is being dragged during Arc creation
     * Currently is used for Spline arc only 05-13-2018
     * xxxxxxx
     *
     * @param x
     * @param y
     * @param mclnArcView
     */
    public void paintMclnArcViewWhileCreatingKnotsOnTheScreenAtPoint(int x, int y, MclnArcView mclnArcView) {
        createdArcInputNodeView = null;
        mclnArcView.moveEntityActivePointTo(x, y);
        mclnArcView.placeEntity(scr0, minScale);
        this.mclnArcViewSprite = mclnArcView;
        repaint();
    }

    /**
     * method called to repaint arc after knot or node is clicked
     *
     * @param mclnArcView
     */
    public void paintArcAndConnectedEntityOnScreen(MclnArcView mclnArcView) {
        repaint();
    }

    /**
     * Called when arc creation is complete
     * Arc sprite is not set to null here
     * If arrow is not yet created it will be painted on screen only by sprite Arc
     * <p>
     * This method is used to put acr thread on the image.
     * This prevents arc thread flickering that may happen
     * otherwise if arc is painted individually
     * when user places arrow on the arc's thread.
     * <p>
     * Above is obsolete.
     * It is called after arrow state was changed by Init Assistant
     *
     * @param mclnArcView
     */
    public void drawArcAndConnectedEntitiesOnTheImageAndCallRepaint(MclnArcView mclnArcView) {
        mclnArcView.newDrawEntityAndConnectedEntity(offScreenImageGraphics, true);
        repaint();
    }

    public void paintArcArrowWhileIsBeingCreatedOnScreen(MclnArcView mclnArcView) {
        repaint();
    }

    /**
     * The method is called to un-create Arc when RMB clicked to reset creation to previous step
     *
     * @param mclnGraphViewEntity
     */
    public void eraseArcOnlyFromImageAndCallRepaint(BasicCSysEntity mclnGraphViewEntity) {

        mclnGraphViewEntity.setHidden(true);
        createScreenImageAndDrawMclnGraphOnIt();
        mclnGraphViewEntity.setHidden(false);
        repaint();
    }

    /**
     * The method is called to completes Arc creation
     *
     * @param mclnArcView
     */
    public void eraseAndPaintArcViewWithConnectedEntities(MclnArcView mclnArcView) {
        this.mclnArcViewSprite = null;
        eraseAndPaintEntityWithConnectedEntities(mclnArcView);
    }

    //=================================================================================================================

    //
    //   P a i n t i n g   P r o p e r t y - C o n d i t i o n - P r o p e r t y
    //   f r a g m e n t   w h i l e   i t   i s   b e i n g   c r e a t e d
    //

    //=================================================================================================================

    public void setCurFragmentInpNodeToPaintExtras(MclnPropertyView curFragmentInpNode) {
        this.curFragmentInpNode = curFragmentInpNode;
        repaint();
    }

    public void setCurFragmentOutNodeToPaintExtras(MclnPropertyView curFragmentOutNode) {
        this.curFragmentOutNode = curFragmentOutNode;
        repaint();
    }

    public void fragmentCreationComplete() {
        curFragmentInpNode = null;
        curFragmentOutNode = null;
        createAndDisplayNewOffScreenImage();
    }

    //=================================================================================================================

    //
    //  M o v i n g   a n d   p a i n t i n g   t h e   g r a p h   f r a g m e n t   o n   t h e   s c r e e n
    //

    //=================================================================================================================

    /**
     * The method is called to paint graph fragment when its nodes are
     * being selected or re-selected initially or at interim location
     *
     * @param selectedMclnNodesToBeMoved
     * @param selectedMclnArcsToBeMoved
     * @param selectedFragmentConnectingArcs
     */
    public void paintTheModelFragmentOnTheScreen(Set<MclnGraphNodeView> selectedMclnNodesToBeMoved,
                                                 Set<MclnArcView> selectedMclnArcsToBeMoved,
                                                 Set<MclnArcView> selectedFragmentConnectingArcs) {
        createScreenImageAndDrawMclnGraphOnIt();
        updateInterimEntitiesToBePaintedAsModelExtras(selectedMclnNodesToBeMoved, selectedMclnArcsToBeMoved,
                selectedFragmentConnectingArcs);
        repaint();
    }

    /**
     * This method populates graph entity collections that represent entities being
     * selected and moved. These entities are the clones of the model entities.
     * The collections are painted on the screen after off screen image painted after design
     * frame is re-sized and repainted or just repainted by Java painting mechanism.
     *
     * @param selectedMclnNodesToBeMoved
     * @param selectedMclnArcsToBeMoved
     * @param selectedFragmentConnectingArcs
     */
    private void updateInterimEntitiesToBePaintedAsModelExtras(Set<MclnGraphNodeView> selectedMclnNodesToBeMoved,
                                                               Set<MclnArcView> selectedMclnArcsToBeMoved,
                                                               Set<MclnArcView> selectedFragmentConnectingArcs) {
        if (selectedMclnNodesToBeMoved != null) {
            this.nodesInInterimLocation.clear();
            this.nodesInInterimLocation.addAll(selectedMclnNodesToBeMoved);
        }
        if (selectedMclnArcsToBeMoved != null) {
            arcsInInterimLocation.clear();
            this.arcsInInterimLocation.addAll(selectedMclnArcsToBeMoved);
        }
        if (selectedFragmentConnectingArcs != null) {
            this.selectedFragmentConnectingArcs.clear();
            this.selectedFragmentConnectingArcs.addAll(selectedFragmentConnectingArcs);
        }
    }

    /**
     * Called when fragment is being moved to paint its last translated phase
     *
     * @param translationVector
     * @param selectedMclnNodesToBeMoved
     * @param selectedMclnArcsToBeMoved
     */
    void translateGraphFragmentBeingMovedAndPaintOnTheScreen(double[] translationVector,
                                                             Set<MclnGraphNodeView> selectedMclnNodesToBeMoved,
                                                             Set<MclnArcView> selectedMclnArcsToBeMoved,
                                                             Set<MclnArcView> selectedFragmentConnectingArcs) {
        Graphics g = redisplayOffScreenImage();

        for (MclnArcView mclnArcView : selectedMclnArcsToBeMoved) {
            mclnArcView.translateAndPaintEntityAtInterimLocation(g, translationVector);
        }

//        for (MclnGraphEntityView mclnGraphEntityView : selectedFragmentConnectingArcs) {
//            mclnGraphEntityView.newDrawEntityAndConnectedEntity(g, true);
//        }

        for (MclnGraphEntityView mclnGraphEntityView : selectedFragmentConnectingArcs) {
            mclnGraphEntityView.drawPlainEntity(g);
        }

        for (MclnGraphNodeView mclnGraphViewNode : selectedMclnNodesToBeMoved) {
            mclnGraphViewNode.translateAndPaintEntityAtInterimLocation(g, translationVector);
        }

        updateInterimEntitiesToBePaintedAsModelExtras(selectedMclnNodesToBeMoved, selectedMclnArcsToBeMoved, null);
    }

    /**
     * @param nodeUIDs
     * @param arcUIDs
     * @param translationVector
     */
    void takeFinalLocationThenRecreateImageAndRepaintItOnTheScreen(List<String> nodeUIDs, List<String> arcUIDs,
                                                                   double[] translationVector) {

        for (String arcUID : arcUIDs) {
            MclnArcView mclnArcView = uidToMclnGraphArcView.get(arcUID);
            mclnArcView.takeFinalLocation(translationVector);
        }

        for (String nodeUID : nodeUIDs) {
            MclnGraphNodeView mclnGraphViewNode = uidToMclnGraphNodeView.get(nodeUID);
            mclnGraphViewNode.takeFinalLocation(translationVector);
        }

        clearInterimEntityCollections();
        createAndDisplayNewOffScreenImage();
    }

    /**
     *
     */
    void clearInterimEntityCollectionsAndRecreateOffScreenImage() {
        clearInterimEntityCollections();
        createAndDisplayNewOffScreenImage();
    }

    /**
     * method cleans extra entities when they are not needed any more
     */
    private void clearInterimEntityCollections() {
        nodesInInterimLocation.clear();
        arcsInInterimLocation.clear();
        selectedFragmentConnectingArcs.clear();
    }

    //=================================================================================================================

    //
    //   M o v i n g   e n t i r e   g r a p h
    //

    //=================================================================================================================

    /**
     * Preparing Model to dragging
     */
    void createEmptyOffScreenImageAndModelSpriteView() {
        createEmptyScreenImage();
        spriteNodesAreBeingMovedList.clear();
        spriteNodesAreBeingMovedList.addAll(graphModelViewNodeEntityList);
        repaint();
    }

    /**
     *
     */
    void resetModelViewAsUserDidNotStartDragging() {
        createScreenImageAndDrawMclnGraphOnIt();
        spriteNodesAreBeingMovedList.clear();
        repaint();
    }

    /**
     * Painting while McLN Model is dragged
     *
     * @param translationVector
     */
    void translateAndPaintTheGraphOnTheScreen(double[] translationVector) {
        Graphics g = redisplayOffScreenImage();
        for (MclnArcView mclnArcView : arcViews) {
            mclnArcView.translate(translationVector);
        }
        for (MclnGraphEntityView mclnGraphEntityView : graphModelViewNodeEntityList) {
            mclnGraphEntityView.translate(translationVector);
            mclnGraphEntityView.drawPlainEntity(g);
        }
    }

    /**
     * Moving Model: move complete
     */
    void takeMovedModelFinalLocationAndRecreateOffScreenImage(double[] translationVector) {
        for (MclnArcView mclnArcView : arcViews) {
            mclnArcView.takeFinalLocation(translationVector);
        }
        for (MclnGraphEntityView mclnGraphEntityView : graphModelViewNodeEntityList) {
            mclnGraphEntityView.takeFinalLocation(translationVector);
        }
        createAndDisplayNewOffScreenImage();
        spriteNodesAreBeingMovedList.clear();
    }

    //=================================================================================================================

    //
    //   M o d e l   e n t i t y   d e l e t i o n
    //

    //=================================================================================================================

    /**
     * The argument may be null
     *
     * @param mclnGraphEntityView
     */
    void highlightEntityViewToBeDeleted(MclnGraphEntityView mclnGraphEntityView) {
        modelEntityToBeDeleted = mclnGraphEntityView;
        repaint();
    }

    @Override
    protected void eraseEntityViewUpOnDeletion() {
        // set this variable null in case not yet finished arc is removed
        mclnArcViewSprite = null;
        modelEntityToBeDeleted = null;
        super.eraseEntityViewUpOnDeletion();
    }

    //
    //   e r a s i n g   e n t i t y
    //

    /**
     * called when:
     * 1) dragged entity release.
     * erasing initial (water marked location) painting on new location
     * 2) arc creation finished - erasing highlighted knots and painting in drawing color
     *
     * @param mclnGraphViewEntity
     */
    public void eraseAndPaintEntityWithConnectedEntities(BasicCSysEntity mclnGraphViewEntity) {
        if (!offScreenImagePrepared) {
            return;
        }
        this.mclnArcViewSprite = null;
        mclnGraphViewEntity.setHidden(true);
        createScreenImageAndDrawMclnGraphOnIt();
        mclnGraphViewEntity.setHidden(false);

        mclnGraphViewEntity.newDrawEntityAndConnectedEntity(offScreenImageGraphics, true);//.newDrawEntityOnly(offScreenImageGraphics, true);
        repaint();
    }

    //=================================================================================================================

    //
    //  J a v a   a c t i v a t e d   r e p a i n t i n g
    //

    //=================================================================================================================

    /**
     * Repainting screen
     */
    @Override
    public void repaint() {
        super.repaint();
    }


    /**
     * @param g
     */
    @Override
    public synchronized void paintComponent(Graphics g) {
        if (!offScreenImagePrepared) {
            return;
        }
        g.drawImage(offScreenImage, 0, 0, null);

        if (isCrossHairsDisplayOn()) {
            paintCrossHairs(g);
        }

        if (createdArcInputNodeView != null) {
            createdArcInputNodeView.paintExtras(g);
        }

        // this is used when arc thread is created
        if (mclnArcViewSprite != null) {
            this.mclnArcViewSprite.newDrawEntityAndConnectedEntity(g, true);
        }

        // This painting is needed for moving entire model to prevent flickering
        if (graphModelViewNodeEntityList.size() != 0) {
            for (MclnGraphEntityView mclnGraphEntityView : graphModelViewNodeEntityList) {
                mclnGraphEntityView.drawPlainEntity(g);
            }
        }

        if (curFragmentInpNode != null) {
            curFragmentInpNode.paintExtras(g);
        }
        if (curFragmentOutNode != null) {
            curFragmentOutNode.paintExtras(g);
        }

        if (modelEntityToBeDeleted != null) {
            modelEntityToBeDeleted.paintExtras(g);
        }

        paintEditedEntityExtras(g, nodesInInterimLocation);

        // this is used when moving nodes or arc knots
        if (mclnGraphSpriteEntity != null) {
            mclnGraphSpriteEntity.drawSpriteEntity(g, scr0, minScale);
        }

        if (mouseHoveredEntityView != null) {
            if (mouseHoveredEntityView.isMouseHover()) {
                mouseHoveredEntityView.paintExtras(g);
            }
        }
    }

    /**
     * @param mouseHoveredEntityView
     */
    void setMouseHoveredEntity(MclnGraphEntityView mouseHoveredEntityView) {
        if (mouseHoveredEntityView != null) {
            this.mouseHoveredEntityView = mouseHoveredEntityView;
            repaint();
        } else {
            if (this.mouseHoveredEntityView != null) {
                this.mouseHoveredEntityView.setMouseHover(false);
                this.mouseHoveredEntityView = null;
                repaint();
            }
        }
    }

    /**
     * This method sets the entity to be a sprite.
     * Sprite entity exists on the screen only, so method
     * recreates image that excludes sprite entity.
     * <p>
     * When called with argument as null method removes
     * makes entity not a sprite and recreates image to make
     * entity drawn on it.
     * <p>
     * It is mandatory to calle the method for each entity twice:
     * First time to make entity sprite, and
     * second time to remove the attribute.
     *
     * @param mclnGraphSpriteEntity
     */
    void makeGraphEntityToBeASpritePaintedOnTheScreenOnly(MclnGraphEntityView mclnGraphSpriteEntity) {
        if (mclnGraphSpriteEntity != null) {
            mclnGraphSpriteEntity.setSprite(true);  // this will make it drawn directly on the screen
            createScreenImageAndDrawMclnGraphOnIt();
            System.out.println("makeGraphEntityToBeASpritePaintedOnTheScreenOnly " + mclnGraphSpriteEntity.isArc());
        } else {
            assert this.mclnGraphSpriteEntity != null : "this.mclnGraphSpriteEntity is null";
            this.mclnGraphSpriteEntity.setSprite(false); // this will make it drawn on the image
            createScreenImageAndDrawMclnGraphOnIt();
            System.out.println("makeGraphEntityToBeASpritePaintedOnTheScreenOnly REM " + this.mclnGraphSpriteEntity.isArc());
        }
        this.mclnGraphSpriteEntity = mclnGraphSpriteEntity;
        repaint();
    }

    /**
     * @param g
     * @param selectedMclnNodesToBeMoved
     */
    private void paintEditedEntityExtras(Graphics g, Set<MclnGraphNodeView> selectedMclnNodesToBeMoved) {
        for (MclnGraphNodeView mclnGraphViewNode : selectedMclnNodesToBeMoved) {
            mclnGraphViewNode.repaintEntityAtInterimScrLocation(g);
        }
    }

    //=================================================================================================================

    //
    //  p a i n t i n g   i n d i v i d u a l   e n t i t y   o n   s c r e e n   i m a g e
    //

    //=================================================================================================================

    /**
     * The need for this method has to be investigated
     * <p>
     * called when:
     * 1) entity created.
     * 2) mouse hover or just left the entity
     * 3) paints entity in watermark color before dragging
     *
     * @param mclnGraphViewEntity
     */
    public void paintEntityOnly(MclnGraphEntityView mclnGraphViewEntity) {
        if (!offScreenImagePrepared) {
            return;
        }
        mclnGraphViewEntity.drawPlainEntity(offScreenImageGraphics);
        Graphics g = super.getGraphics();
        g.drawImage(offScreenImage, 0, 0, null);
        mclnGraphViewEntity.paintExtras(g);
    }

    //   ==============================================================================================================
    //       C l e a n u p   a f t e r   D e m o   P r e s e n t a t i o n   c a n c e l l e d
    //   ==============================================================================================================

    public synchronized void cleanupAfterDemoPresentationCanceled() {

        // Creating Arc
        mclnArcViewSprite = null;
        // Creating fragment
        curFragmentInpNode = null;
        curFragmentOutNode = null;
        // Deleting model entity
        modelEntityToBeDeleted = null;
        // moving arc
        mclnGraphSpriteEntity = null;
        // Moving fragment
        clearInterimEntityCollections();
        // Moving entire model
        spriteNodesAreBeingMovedList.clear();
    }
}
