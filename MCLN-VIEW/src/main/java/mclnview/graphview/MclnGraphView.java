package mclnview.graphview;

import adf.csys.view.*;
import mcln.model.*;
import mclnview.graphview.interfaces.MclnModeChangedListener;
import mclnview.graphview.interfaces.MclnModelStructureChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Admin on 11/14/2017.
 */
public class MclnGraphView extends CSysView {

    public static String MG01 = "MG01"; // mcln model graph

    private final Color[] axesColors =
            {
                    (new Color(0xFF0000)), (new Color(0xFF0000)), (new Color(0xFF0000)),
                    (new Color(0xFF0000)), (new Color(0xFF0000)), (new Color(0xFF0000)),
            };

    private static final Color GRID_COLOR = Color.LIGHT_GRAY;
    private MclnGraphViewModel mclnGraphViewModel;
    private DsdsseWorldScalableCSysGrid dsdsseCSysGridEntity;

    private final java.util.List<MclnPropertyView> statementViews = new ArrayList();
    private final java.util.List<MclnConditionView> conditionViews = new ArrayList();
    private final java.util.List<MclnArcView> arcViews = new ArrayList();

    private final java.util.List<MclnGraphViewNode> graphModelViewNodeEntityList = new ArrayList();
    private final java.util.List<MclnArcView> graphModelViewArcEntityList = new ArrayList();
    private CSysEntity[] mclnGraphEntityArray = new CSysEntity[0];

    private final Map<String, MclnGraphViewNode> uidToMclnGraphNodeView = new HashMap();
    private final Map<String, MclnArcView> uidToMclnGraphArcView = new HashMap();

    // Creating Arc.
    private MclnGraphViewNode arcInputNodeView;
    // This is Arc View sprite;
    private MclnArcView mclnArcViewSprite;

    // Moving fragment
    private final Set<MclnGraphViewNode> nodesInInterimLocation = new HashSet();
    private final Set<MclnArcView> arcsInInterimLocation = new HashSet();
    private final Set<MclnArcView> selectedFragmentConnectingArcs = new HashSet();

    // Deleting model entity
    private MclnGraphEntityView modelEntityToBeDeleted;

    private CSysRectangleEntity[] viewWorldEntityArray = new CSysRectangleEntity[0];

    // images for grid and model view
    private Image offScreenImage;
    private Graphics offScreenImageGraphics;
    boolean offScreenImagePrepared;

    /*
       This class is responsible for presenting and updating McLN model view on the screen.
       In order to accomplish this it has several groups of operation.
       a)method called

     */


    //=================================================================================================================

    //
    //  Mcln Model Structure Changed Listener
    //

    // Called ether when Editor added newly created entity to McLN Model
    // or Demo Creator animate Mcln Model creation
    //
    private MclnModelStructureChangedListener mclnModelStructureChangedListener = new MclnModelStructureChangedListener() {

        @Override
        public void mclnModelViewRectangleUpdated(MclnGraphViewModel MclnGraphViewModel) {

            MclnDoubleRectangle modelRectangle = MclnGraphViewModel.getViewRectangle();
            DoubleRectangle projectCSysRectangle = new DoubleRectangle(modelRectangle.getX(), modelRectangle.getY(),
                    modelRectangle.getWidth(), modelRectangle.getHeight());

            MclnGraphView.this.updateViewRectangle(projectCSysRectangle);
            createScreenImageAndDrawMclnGraphOnIt();
        }

        @Override
        public MclnPropertyView mclnStatementAdded(MclnStatement mclnStatement) {
            MclnPropertyView mclnPropertyView = createMclnStatementView(mclnStatement);
            return mclnPropertyView;
        }

        @Override
        public void mclnStatementRemoved(MclnStatement mclnStatement) {
            MclnGraphViewNode mclnGraphStatementView = uidToMclnGraphNodeView.remove(mclnStatement.getUID());
            mclnGraphStatementView.disconnectFromAllArc();
            statementViews.remove(mclnGraphStatementView);
            graphModelViewNodeEntityList.remove(mclnGraphStatementView);
            mclnGraphEntityArray =
                    graphModelViewNodeEntityList.toArray(new BasicCSysEntity[graphModelViewNodeEntityList.size()]);
            eraseEntityViewUpOnDeletion();
        }

        @Override
        public MclnConditionView mclnConditionAdded(MclnCondition mclnCondition) {
            MclnConditionView mclnConditionView = createMclnConditionView(mclnCondition);
            return mclnConditionView;
        }

        @Override
        public void mclnConditionRemoved(MclnCondition mclnCondition) {
            MclnGraphViewNode mclnGraphConditionView = uidToMclnGraphNodeView.remove(mclnCondition.getUID());
            mclnGraphConditionView.disconnectFromAllArc();
            conditionViews.remove(mclnGraphConditionView);
            graphModelViewNodeEntityList.remove(mclnGraphConditionView);
            mclnGraphEntityArray =
                    graphModelViewNodeEntityList.toArray(new BasicCSysEntity[graphModelViewNodeEntityList.size()]);
            eraseEntityViewUpOnDeletion();
        }

        @Override
        public MclnArcView incompleteMclnArcAdded(MclnArc<MclnNode, MclnNode> mclnArc) {
            MclnArcView mclnArcView = createIncompleteMclnArcView(mclnArc);
            return mclnArcView;
        }

        @Override
        public MclnArcView mclnArcAdded(MclnArc<MclnNode, MclnNode> mclnArc) {
            MclnArcView mclnArcView = createMclnArcView(mclnArc);
            return mclnArcView;
        }

        @Override
        public void mclnArcRemoved(MclnArc mclnArc) {
            MclnArcView mclnGraphArcView = uidToMclnGraphArcView.remove(mclnArc.getUID());
            mclnGraphArcView.disconnectFromInputAndOutputNodes();
            arcViews.remove(mclnGraphArcView);
            graphModelViewArcEntityList.remove(mclnGraphArcView);
            // set this variable null in case not yet finished arc is removed
            mclnArcViewSprite = null;
            // set any being deleted entity to null
            eraseEntityViewUpOnDeletion();
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    };

    //=================================================================================================================

    //
    //  Mcln Graph Mode Listener
    //

    private MclnModeChangedListener mclnModeChangedListener = new MclnModeChangedListener() {

        @Override
        public void mclnModelCleared() {
            /*
              We call removeListener to ask mclnGraphViewModel to remove listener from destroyed Mcln Model.
              It is removed when model is cleared
             */
            mclnGraphViewModel.removeMclnModelSimulationListener(mclnModelSimulationListener);
            statementViews.clear();
            conditionViews.clear();
            arcViews.clear();

            graphModelViewNodeEntityList.clear();
            graphModelViewArcEntityList.clear();
            mclnGraphEntityArray = new CSysEntity[0];

            uidToMclnGraphNodeView.clear();
            uidToMclnGraphArcView.clear();

            clearInterimEntityCollections();

            createAndDisplayNewOffScreenImage();
        }

        @Override
        public void mclnModelUpdated(MclnModel mclnModel) {
            createAndDisplayNewOffScreenImage();
        }

        /**
         * called when new project is retrieved or when the project model changed
         *
         * Creates MCLN Graph view for each Mcln Model element
         * Creates Screen image and initializes it.
         *
         * @param newCurrentMclnModel
         */
        @Override
        public void onCurrentMclnModelReplaced(MclnModel newCurrentMclnModel) {

            MclnDoubleRectangle modelRectangle = mclnGraphViewModel.getViewRectangle();
            DoubleRectangle projectCSysRectangle = new DoubleRectangle(modelRectangle.getX(), modelRectangle.getY(),
                    modelRectangle.getWidth(), modelRectangle.getHeight());
            updateViewRectangle(projectCSysRectangle);

            java.util.List<MclnStatement> mclnStatements = mclnGraphViewModel.getMclnStatements();
            for (MclnStatement mclnStatement : mclnStatements) {
                createMclnStatementView(mclnStatement);
            }

            java.util.List<MclnCondition> mclnConditions = mclnGraphViewModel.getMclnConditions();
            for (MclnCondition mclnCondition : mclnConditions) {
                createMclnConditionView(mclnCondition);
            }

            java.util.List<MclnArc> mclnArcs = mclnGraphViewModel.getMclnArcs();
            for (MclnArc mclnArc : mclnArcs) {
                createMclnArcView(mclnArc);
            }
            /*
               We call addListener to ask mclnGraphViewModel to add listener to new Mcln Model.
               It is removed when model is cleared
             */
            mclnGraphViewModel.addMclnModelSimulationListener(mclnModelSimulationListener);

             /*
               In case creating Demo projects this method is called before the McLN model is created.
               Hence, there is nothing to put on the image and show on the screen.
               This repaint is needed for to shoe retrieved project only.
             */
            if (mclnStatements.size() != 0 || mclnConditions.size() != 0 || mclnArcs.size() != 0) {
                createAndDisplayNewOffScreenImage();
            }
        }

        @Override
        public void demoProjectComplete(MclnModel newMclnModel) {
            createAndDisplayNewOffScreenImage();
        }
    };

    //
    //   Methods to create McLN Graph elements
    //

    /**
     * @param mclnStatement
     */
    private MclnPropertyView createMclnStatementView(MclnStatement mclnStatement) {
        MclnPropertyView mclnPropertyView = MclnGraphVewFactory.createMclnGraphViewStatement(this,
                mclnStatement);
        // adding Statement view to the Graph
        statementViews.add(mclnPropertyView);
        graphModelViewNodeEntityList.add(mclnPropertyView);
        uidToMclnGraphNodeView.put(mclnPropertyView.getUID(), mclnPropertyView);
        mclnGraphEntityArray =
                graphModelViewNodeEntityList.toArray(new BasicCSysEntity[graphModelViewNodeEntityList.size()]);
        updateModelEntList(combinedRotatingMatrix);
        return mclnPropertyView;
    }

    /**
     * @param mclnCondition
     */
    private MclnConditionView createMclnConditionView(MclnCondition mclnCondition) {
        MclnConditionView mclnConditionView = MclnGraphVewFactory.createMclnGraphViewCondition(this,
                mclnCondition);
        // adding Condition view to the Graph
        conditionViews.add(mclnConditionView);
        graphModelViewNodeEntityList.add(mclnConditionView);
        uidToMclnGraphNodeView.put(mclnConditionView.getUID(), mclnConditionView);
        mclnGraphEntityArray =
                graphModelViewNodeEntityList.toArray(new BasicCSysEntity[graphModelViewNodeEntityList.size()]);
        updateModelEntList(combinedRotatingMatrix);
        return mclnConditionView;
    }

    /**
     * @param mclnArc
     * @return
     */
    private MclnArcView createIncompleteMclnArcView(MclnArc mclnArc) {
        String inpNodeUID = mclnArc.getInpNodeUID();
        MclnGraphViewNode inpNodeView = uidToMclnGraphNodeView.get(inpNodeUID);
        MclnArcView mclnArcView = MclnGraphVewFactory.createNewIncompleteMclnGraphArcViewForEditor(this,
                mclnArc, inpNodeView);
        addMclnGraphArcView(mclnArcView);
        return mclnArcView;
    }

    /**
     * @param mclnArc
     */
    private MclnArcView createMclnArcView(MclnArc mclnArc) {
        String inpNodeUID = mclnArc.getInpNodeUID();
        String outNodeUID = mclnArc.getOutNodeUID();
        MclnGraphViewNode inpNode = uidToMclnGraphNodeView.get(inpNodeUID);
        MclnGraphViewNode outNode = uidToMclnGraphNodeView.get(outNodeUID);
        MclnArcView mclnArcView = MclnGraphVewFactory.createMclnGraphViewArc(this, mclnArc, inpNode, outNode);

        inpNode.addOutputArc(mclnArcView);
        outNode.addInputArc(mclnArcView);

        addMclnGraphArcView(mclnArcView);
        return mclnArcView;
    }

    /**
     * @param mclnGraphArcView
     */
    private final void addMclnGraphArcView(MclnArcView mclnGraphArcView) {
        arcViews.add(mclnGraphArcView);
        graphModelViewArcEntityList.add(mclnGraphArcView);
        uidToMclnGraphArcView.put(mclnGraphArcView.getUID(), mclnGraphArcView);
        updateGraphArcList(combinedRotatingMatrix);
    }

    //=================================================================================================================

    //
    //  Mcln Model Simulation Listener
    //

    private MclnModelSimulationListener mclnModelSimulationListener = new MclnModelSimulationListener() {

        @Override
        public void simulationStepExecuted() {
            if (SwingUtilities.isEventDispatchThread()) {
                onSimulationStepExecutedRunnable.run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(onSimulationStepExecutedRunnable);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                }
            }
        }

        @Override
        public void mclnModelStateChanged() {
            if (SwingUtilities.isEventDispatchThread()) {
                onSimulationStateChangeRunnable.run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(onSimulationStateChangeRunnable);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                }
            }
        }

        @Override
        public void mclnModelStateReset() {
            if (SwingUtilities.isEventDispatchThread()) {
                onSimulationStateResetRunnable.run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(onSimulationStateResetRunnable);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                }
            }
        }
    };

    private Runnable onSimulationStepExecutedRunnable = () -> {
        for (MclnPropertyView mclnPropertyView : statementViews) {
            mclnPropertyView.recordHistory();
        }
    };

    private Runnable onSimulationStateChangeRunnable = () -> {
        regenerateGraphView();
        repaintAllModelNodesOnOffScreenImage();
    };

    private Runnable onSimulationStateResetRunnable = () -> {
        System.out.println("\nOn  M c l n  M o d e l  S t a t e  R e s e t event: repainting graph nodes.");
    };

    //=================================================================================================================

    //
    //   C o n s t r u c t i n g   M c l n   G r a p h   V i e w
    //

    /**
     * @param MclnGraphViewModel
     * @param options
     */
    public MclnGraphView(MclnGraphViewModel MclnGraphViewModel, int viewPadding, int options) {
        super(MclnGraphViewModel.getViewRectangle().getX(), MclnGraphViewModel.getViewRectangle().getY(),
                MclnGraphViewModel.getViewRectangle().getWidth(), MclnGraphViewModel.getViewRectangle().getHeight(),
                viewPadding, options);
        setBorder(null);
        this.setFocusable(false);
        this.mclnGraphViewModel = MclnGraphViewModel;
        setName("MclnGraphView");
        createAxis(axesColors);
        this.setOpaque(true);
        setBackground(Color.BLACK);
        MclnGraphViewModel.addMclnModelBuildingListener(mclnModelStructureChangedListener);
        MclnGraphViewModel.addMclnModeChangedListener(mclnModeChangedListener);
        MclnGraphViewModel.addMclnModelSimulationListener(mclnModelSimulationListener);

        initGridAndModelRectangle();
    }

    //
    //   I n i t i a l i z a t i o n
    //

    /**
     * called to initialize this instance upon creation
     */
    public void initGridAndModelRectangle() {

        // World is a design grid and cSys axes
        java.util.List<CSysRectangleEntity> viewWorldEntityList = new ArrayList();

        dsdsseCSysGridEntity = new DsdsseWorldScalableCSysGrid(this,
                DsdsseWorldScalableCSysGrid.DEFAULT_GRID_STEP, DsdsseWorldScalableCSysGrid.DEFAULT_GRID_MARGIN,
                DsdsseWorldScalableCSysGrid.DEFAULT_GRID_COLOR);

        dsdsseCSysGridEntity.doCSysToScreenTransformation(scr0, minScale);
        viewWorldEntityList.add(dsdsseCSysGridEntity);

        // this World Entity array contains design space grid only
        viewWorldEntityArray = viewWorldEntityList.toArray(new CSysRectangleEntity[viewWorldEntityList.size()]);

        // this Graph Entity array contains design space grid as first element
        mclnGraphEntityArray = viewWorldEntityList.toArray(new CSysEntity[viewWorldEntityList.size()]);
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
    public java.util.List<MclnPropertyView> getPropertyViewList() {
        java.util.List<MclnPropertyView> propertyViewListClone = new ArrayList(statementViews);
        return propertyViewListClone;
    }


    //=================================================================================================================

    //
    //   W o r k i n g    w i t h    o f f    S c r e e    I m a g e
    //

    @Override
    protected void updatePresentationUponViewResized() {
        createScreenImageAndDrawMclnGraphOnIt();
    }

    public void repaintImageAndSpriteEntities() {
        repaint();
    }

    /**
     * Called from other components when layout or state of the model changed
     * Created new image paints grid and paints graph
     * Does not displays image
     */
    public void regenerateGraphView() {
        createScreenImageAndDrawMclnGraphOnIt();
    }

    /**
     * The method is called from many other methods each time
     * when the entire model  has to be  redrawn on the image
     */
    private synchronized void createScreenImageAndDrawMclnGraphOnIt() {
        offScreenImagePrepared = false;
        if (!createEmptyScreenImage()) {
            return;
        }

        int nElements = graphModelViewArcEntityList.size();
        for (int i = 0; i < nElements; i++) {
            MclnArcView mclnArcView = graphModelViewArcEntityList.get(i);
            if (mclnArcView.isUnderConstruction()) {
                continue;
            }
            mclnArcView.drawPlainEntity(offScreenImageGraphics);
        }

        nElements = graphModelViewNodeEntityList.size();
        for (int i = 0; i < nElements; i++) {
            MclnGraphViewNode mclnGraphViewEntity = graphModelViewNodeEntityList.get(i);
            mclnGraphViewEntity.drawPlainEntity(offScreenImageGraphics);
        }

        offScreenImagePrepared = true;
    }

    /**
     * This is the method that creates screen image
     * filled with:
     * a) grid
     * b) axes
     * c) arcs
     * d) nodes
     */
    private synchronized boolean createEmptyScreenImage() {
        offScreenImagePrepared = false;
        Dimension currentSize = getSize();
        if (currentSize.width <= 0) {
            return false;
        }

        //
        // image creation
        //

        if (offScreenImage != null) {
            offScreenImageGraphics.dispose();
        }

        offScreenImage = createImage(currentSize.width, currentSize.height);
        offScreenImageGraphics = offScreenImage.getGraphics();

        offScreenImageGraphics.setFont(getFont());
        offScreenImageGraphics.setColor(new Color(0x000000));
        offScreenImageGraphics.fillRect(0, 0, currentSize.width, currentSize.height);

        Graphics2D g2 = (Graphics2D) offScreenImageGraphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        drawProjectRectangle(g2);
        drawProjectSpaceCoordinates(g2);
        offScreenImagePrepared = true;
        return offScreenImagePrepared;
    }

    //=================================================================================================================

    //
    //   Painting Design Space details: axes, grid and coordinates
    //

    //=================================================================================================================

    /**
     * This method paints Design Space Grid
     *
     * @param g
     */
    @Override
    protected void paintWorld(Graphics g) {
        if (!true) {
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
        if (!true) {
            return;
        }
        g.setColor(Color.RED);
        super.paintAxes(g);
    }

    /**
     * @param g
     */
    private void drawProjectRectangle(Graphics g) {

        if (!true) {
            return;
        }

        offScreenImageGraphics.setColor(new Color(0x222222));
        offScreenImageGraphics.fillRect(projectSpaceViewXMin, projectSpaceViewYMin, projectSpaceViewWidth,
                projectSpaceViewHeight);

        g.setColor(new Color(0xFF4444));

        // horizontal
        g.drawLine(
                projectSpaceViewXMin, projectSpaceViewYMin,
                projectSpaceViewXMin, projectSpaceViewYMax);

        g.drawLine(
                projectSpaceViewXMax, projectSpaceViewYMin,
                projectSpaceViewXMax, projectSpaceViewYMax);

        // vertical
        g.drawLine(
                projectSpaceViewXMin, projectSpaceViewYMin,
                projectSpaceViewXMax, projectSpaceViewYMin);
        g.drawLine(
                projectSpaceViewXMin, projectSpaceViewYMax,
                projectSpaceViewXMax, projectSpaceViewYMax);
    }

    /**
     * @param g
     */
    private void drawProjectSpaceCoordinates(Graphics g) {

        if (!true) {
            return;
        }

        Color COORDINATE_PAD_BACKGROUND = new Color(0xEEEEEE);

        Font font = new Font("Monospaced", Font.BOLD, 10);

        g.setFont(font);

        int coordinatesPadHeight = 16;

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
        g.drawString(text, projectSpaceViewXMin + border / 2, projectSpaceViewYMin - 9);

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

    public void updateModelEntList(double[][] mat43) {
        scaleEntArray(mclnGraphEntityArray);
    }

    public void updateGraphArcList(double[][] mat43) {
        updateCSysWorldEntList(mat43, graphModelViewArcEntityList);
    }

    /**
     * The method updates entity view coordinates after:
     * 1) model created or changed
     * 2) view changed: re-sized, rotated, or distorted
     *
     * @param mat43
     */
    @Override
    public final void updateCSysEntList(double[][] mat43) {

        // scale Grid
        super.scaleEntArray(viewWorldEntityArray); // this is just a grid

        // scale axes
        super.scaleEntityCollection(super.getWorldAxesList()); // these are axes

        // scale Nodes
        super.scaleEntityCollection(graphModelViewNodeEntityList);

        // scale Arcs
        super.scaleEntityCollection(graphModelViewArcEntityList); // this updates arc arrows and some how all entities

        // scale interim Arcs
        super.scaleEntityCollection(arcsInInterimLocation);
    }

    //=================================================================================================================

    //
    //   S e l e c t i o n
    //

    public MclnGraphEntityView getGraphEntityAtCoordinates(int x, int y) {
        MclnGraphViewNode mclnGraphViewNode = getGraphNodeAtCoordinates(x, y);
        if (mclnGraphViewNode != null) {
            return mclnGraphViewNode;
        }
        MclnArcView mclnArcView = getArcAtCoordinates(x, y);
        if (mclnArcView != null) {
            return mclnArcView;
        }
        return null;
    }

    public MclnGraphViewNode getGraphNodeAtCoordinates(int x, int y) {
        for (MclnGraphViewNode mclnGraphViewNode : graphModelViewNodeEntityList) {
            if (mclnGraphViewNode.isMouseHover(x, y)) {
                return mclnGraphViewNode;
            }
        }
        return null;
    }

    public MclnPropertyView getPropertyNodeAtCoordinates(int x, int y) {
        for (MclnPropertyView mclnPropertyView : statementViews) {
            if (mclnPropertyView.isMouseHover(x, y)) {
                return mclnPropertyView;
            }
        }
        return null;
    }

    public MclnConditionView getConditionAtCoordinates(int x, int y) {
        for (MclnConditionView mclnConditionView : conditionViews) {
            if (mclnConditionView.isMouseHover(x, y)) {
                return mclnConditionView;
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

    //=================================================================================================================

    //
    //   S e r c h   f o r   m o d e l   e l e m e n t s
    //

    public MclnGraphViewNode getMclnNodeByID(String propertyNodeID) {
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

    void setArcInputNodeWhileCreatingArc(MclnGraphViewNode arcInputNodeView) {
        // this arcInputNodeView will be used for very short time
        // after the Arc input node clicked only. As soon as user
        // start moving the arc's active point the arc input node
        // will be repainted as part of the arc dependent nodes
        this.arcInputNodeView = arcInputNodeView;
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

    //=================================================================================================================

    //
    //  M o v i n g   a n d   p a i n t i n g   t h e   g r a p h   f r a g m e n t   o n   t h e   s c r e e n
    //

    //=================================================================================================================

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
    private void updateInterimEntitiesToBePaintedAsModelExtras(Set<MclnGraphViewNode> selectedMclnNodesToBeMoved,
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
     * method cleans extra entities when they are not needed any more
     */
    private void clearInterimEntityCollections() {
        nodesInInterimLocation.clear();
        arcsInInterimLocation.clear();
        selectedFragmentConnectingArcs.clear();
    }

    //=================================================================================================================

    //
    //   M o d e l   e n t i t y   d e l e t i o n
    //

    //=================================================================================================================

    private void eraseEntityViewUpOnDeletion() {
        createScreenImageAndDrawMclnGraphOnIt();
        modelEntityToBeDeleted = null;
        repaint();
    }

    //=================================================================================================================

    //
    //  J a v a   a c t i v a t e d   r e p a i n t i n g
    //

    //=================================================================================================================

    /**
     * @param g
     */
    @Override
    public synchronized void paintComponent(Graphics g) {
        if (!offScreenImagePrepared) {
            return;
        }
        g.drawImage(offScreenImage, 0, 0, null);

        if (arcInputNodeView != null) {
            arcInputNodeView.paintExtras(g);
        }

        if (mclnArcViewSprite != null) {
            this.mclnArcViewSprite.newDrawEntityAndConnectedEntity(g, true);
        }

        if (graphModelViewNodeEntityList.size() != 0) {
            for (MclnGraphEntityView mclnGraphEntityView : graphModelViewNodeEntityList) {
                mclnGraphEntityView.drawPlainEntity(g);
            }
        }

        if (modelEntityToBeDeleted != null) {
            modelEntityToBeDeleted.paintExtras(g);
        }

        paintEditedEntityExtras(g, nodesInInterimLocation);
    }

    /**
     * @param g
     * @param selectedMclnNodesToBeMoved
     */
    private void paintEditedEntityExtras(Graphics g, Set<MclnGraphViewNode> selectedMclnNodesToBeMoved) {
        for (MclnGraphViewNode mclnGraphViewNode : selectedMclnNodesToBeMoved) {
            mclnGraphViewNode.repaintEntityAtInterimScrLocation(g);
        }
    }

    //=================================================================================================================

    //
    //   Methods   to   recreate   and   to   re-display   off   screen   image
    //

    //=================================================================================================================

    /**
     * Called from Move Fragment and Move Entire Model operations
     * when moving finished and moved entities should be painted
     * on final location
     *
     * @return
     */
    private void createAndDisplayNewOffScreenImage() {
        // The image will be painted by paintComponent method.
        // This approach eliminate flickering on the screen.
        createScreenImageAndDrawMclnGraphOnIt();
        repaint();
    }

    /**
     * called from Model Simulation Listener on Mcln Model state change event
     */
    private void repaintAllModelNodesOnOffScreenImage() {

        System.out.println("\n\n***********************************************************");
        System.out.println(" Simulation State Change         Repainting Mcln Graph View");
        System.out.println("**************************************************************\n");

        for (MclnPropertyView mclnPropertyView : statementViews) {
            mclnPropertyView.updateViewOnModelChanged();
            mclnPropertyView.drawPlainEntity(offScreenImageGraphics);
        }

        for (MclnConditionView mclnConditionView : conditionViews) {
            mclnConditionView.updateViewOnModelChanged();
            mclnConditionView.drawPlainEntity(offScreenImageGraphics);
        }

        repaint();
    }

    //=================================================================================================================

    //
    //  p a i n t i n g   i n d i v i d u a l   e n t i t y   o n   s c r e e n   i m a g e
    //

    //=================================================================================================================

    /**
     * called when:
     * 1) entity created.
     * 2) mouse hover or just left the entity
     * 3) paints entity in watermark color before dragging
     *
     * @param mclnGraphViewEntity
     */

    /**
     * The need for this method has to be investigated
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

    //=================================================================================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setSize(600, 300);
                MclnDoubleRectangle mclnDoubleRectangle = new MclnDoubleRectangle(-15, 15, 30, 30);
                MclnModel currentMclnModel = MclnModel.createInstance("Default Mcln Model", MG01, mclnDoubleRectangle);
                MclnProject mclnProject = MclnProject.createInitialMclnProject(MclnProject.DEFAULT_PROJECT_NAME, mclnDoubleRectangle, currentMclnModel);
                MclnGraphViewModel mclnGraphViewModel = new MclnGraphViewModel(mclnProject);
                MclnGraphView mclnGraphView = new MclnGraphView(mclnGraphViewModel, 20, 0);
                frame.getContentPane().add(mclnGraphView);
                frame.setVisible(true);
            }
        });
    }
}
