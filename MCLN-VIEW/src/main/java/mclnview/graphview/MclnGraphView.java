package mclnview.graphview;

import adf.csys.view.*;
import adf.csys.view.DsdsseWorldScalableCSysGrid;
import adf.utils.StandardFonts;
import mcln.model.*;
import mclnview.graphview.interfaces.MclnModeChangedListener;
import mclnview.graphview.interfaces.MclnModelStructureChangedListener;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MclnGraphView extends BasicCSysView {

    private final Color[] axesColors =
            {
                    (new Color(0xFF0000)), (new Color(0xFF0000)), (new Color(0xFF0000)),
                    (new Color(0xFF0000)), (new Color(0xFF0000)), (new Color(0xFF0000)),
            };

    protected final MclnGraphViewDefaultProperties mclnGraphViewDefaultProperties;

    protected MclnGraphModel mclnGraphModel;
    protected DsdsseWorldScalableCSysGrid dsdsseCSysGridEntity;


    protected final java.util.List<MclnPropertyView> statementViews = new ArrayList();
    protected final java.util.List<MclnConditionView> conditionViews = new ArrayList();
    protected final java.util.List<MclnArcView> arcViews = new ArrayList();

    protected final java.util.List<MclnGraphNodeView> graphModelViewNodeEntityList = new ArrayList();
    protected final List<MclnArcView> graphModelViewArcEntityList = new ArrayList();

    protected final Map<String, MclnGraphNodeView> uidToMclnGraphNodeView = new HashMap();
    protected final Map<String, MclnArcView> uidToMclnGraphArcView = new HashMap();

    // images for grid and model view
    protected Image offScreenImage;
    protected Graphics offScreenImageGraphics;
    protected boolean offScreenImagePrepared;
    boolean recreateScreenImage;

    //  ===============================================================================================================
    //  Mcln Model Structure Changed Listener Implementation
    //  ===============================================================================================================

    /*
       This class is responsible for presenting and updating McLN model view on the screen.
       In order to accomplish this it has several groups of operation.
       a)method called

     */

    // Called ether when newly created entity added to McLN Model
    // or Demo Creator animates Mcln Model creation
    //
    private MclnModelStructureChangedListener mclnModelStructureChangedListener = new MclnModelStructureChangedListener() {

        @Override
        public void mclnModelViewRectangleUpdated(MclnGraphModel mclnGraphModel) {

            MclnDoubleRectangle modelRectangle = mclnGraphModel.getViewRectangle();
            DoubleRectangle projectCSysRectangle = new DoubleRectangle(modelRectangle.getX(), modelRectangle.getY(),
                    modelRectangle.getWidth(), modelRectangle.getHeight());

            MclnGraphView.this.updateViewRectangle(projectCSysRectangle);
            createScreenImageAndDrawMclnGraphOnIt();
        }

        @Override
        public MclnPropertyView mclnStatementAdded(MclnStatement mclnStatement) {
            MclnPropertyView mcLnPropertyView = createMclnStatementView(mclnStatement);
            mcLnPropertyView.doCSysToScreenTransformation(scr0, minScale);
            return mcLnPropertyView;
        }

        @Override
        public void mclnStatementRemoved(MclnStatement mclnStatement) {
            MclnGraphNodeView mclnGraphStatementView = uidToMclnGraphNodeView.remove(mclnStatement.getUID());
            mclnGraphStatementView.disconnectFromAllArc();
            statementViews.remove(mclnGraphStatementView);
            graphModelViewNodeEntityList.remove(mclnGraphStatementView);
            eraseEntityViewUpOnDeletion();
        }

        @Override
        public MclnConditionView mclnConditionAdded(MclnCondition mclnCondition) {
            MclnConditionView mcLnConditionView = createMclnConditionView(mclnCondition);
            mcLnConditionView.doCSysToScreenTransformation(scr0, minScale);
            return mcLnConditionView;
        }

        @Override
        public void mclnConditionRemoved(MclnCondition mclnCondition) {
            MclnGraphNodeView mclnGraphConditionView = uidToMclnGraphNodeView.remove(mclnCondition.getUID());
            mclnGraphConditionView.disconnectFromAllArc();
            conditionViews.remove(mclnGraphConditionView);
            graphModelViewNodeEntityList.remove(mclnGraphConditionView);
            eraseEntityViewUpOnDeletion();
        }

        @Override
        public MclnArcView incompleteMclnArcAdded(MclnArc<MclnNode, MclnNode> mclnArc) {
            MclnArcView mclnArcView = createIncompleteMclnArcView(mclnArc);
            mclnArcView.doCSysToScreenTransformation(scr0, minScale);
            return mclnArcView;
        }

        @Override
        public MclnArcView mclnArcAdded(MclnArc<MclnNode, MclnNode> mclnArc) {
            MclnArcView mclnArcView = createMclnArcView(mclnArc);
            mclnArcView.doCSysToScreenTransformation(scr0, minScale);
            return mclnArcView;
        }

        @Override
        public void mclnArcRemoved(MclnArc mclnArc) {
            MclnArcView mclnGraphArcView = uidToMclnGraphArcView.remove(mclnArc.getUID());
            mclnGraphArcView.disconnectFromInputAndOutputNodes();
            arcViews.remove(mclnGraphArcView);
            graphModelViewArcEntityList.remove(mclnGraphArcView);
            // set this variable null in case not yet finished arc is removed
//            mclnArcViewSprite = null;
            // set any being deleted entity to null
            eraseEntityViewUpOnDeletion();
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    };

    //  ===============================================================================================================
    //  Mcln Graph Model Listener Implementation
    //  ===============================================================================================================

    private MclnModeChangedListener mclnModeChangedListener = new MclnModeChangedListener() {

        @Override
        public void mclnModelCleared() {
            onMclnModelCleared();
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

//            System.out.println("building Model View");

            MclnDoubleRectangle modelRectangle = mclnGraphModel.getViewRectangle();
            DoubleRectangle projectCSysRectangle = new DoubleRectangle(modelRectangle.getX(), modelRectangle.getY(),
                    modelRectangle.getWidth(), modelRectangle.getHeight());
            updateViewRectangle(projectCSysRectangle);

            List<MclnStatement> mclnStatements = mclnGraphModel.getMclnStatements();
            for (MclnStatement mclnStatement : mclnStatements) {
                createMclnStatementView(mclnStatement);
            }

            List<MclnCondition> mclnConditions = mclnGraphModel.getMclnConditions();
            for (MclnCondition mclnCondition : mclnConditions) {
                createMclnConditionView(mclnCondition);
            }

            List<MclnArc> mclnArcs = mclnGraphModel.getMclnArcs();
            for (MclnArc mclnArc : mclnArcs) {
                createMclnArcView(mclnArc);
            }

            // this commented out because it is not needed.
            // Listener is removed from model when the model is destroyed
//            mclnGraphModel.removeMclnModelSimulationListener(mclnModelSimulationListener);
            /*
               We call addListener to ask mclnGraphModel to add listener to new Mcln Model.
               It is removed when model is cleared
             */
            mclnGraphModel.addMclnModelSimulationListener(mclnModelSimulationListener);

             /*
               In case creating Demo projects this method is called before the McLN model is created.
               Hence, there is nothing to put on the image and show on the screen.
               This repaint is needed for to show retrieved project only.
             */
            if (mclnStatements.size() != 0 || mclnConditions.size() != 0 || mclnArcs.size() != 0) {
                // The following call will transform McLN model representation to Mcln View representation
                updateCSysEntList(null);
                createAndDisplayNewOffScreenImage();
            }
            repaint();
        }

        @Override
        public void demoProjectComplete(MclnModel newMclnModel) {
            createAndDisplayNewOffScreenImage();
        }
    };

    protected void onMclnModelCleared() {
        //  We call removeListener to ask mclnGraphModel to remove listener from destroyed Mcln Model.
        // It is removed when model is cleared
        mclnGraphModel.removeMclnModelSimulationListener(mclnModelSimulationListener);

        statementViews.clear();
        conditionViews.clear();
        arcViews.clear();
        graphModelViewNodeEntityList.clear();
        graphModelViewArcEntityList.clear();
        uidToMclnGraphNodeView.clear();
        uidToMclnGraphArcView.clear();

        createAndDisplayNewOffScreenImage();
    }


    private MclnModelSimulationListener mclnModelSimulationListener = new MclnModelSimulationListener() {

        private Runnable onSimulationStepExecutedRunnable = () -> {
            onSimulationStepExecuted();
        };

        private Runnable onSimulationStateChangeRunnable = () -> {
            onSimulationStateChange();
        };

        private Runnable onSimulationStateResetRunnable = () -> {
            onSimulationStateReset();
        };

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

    protected void onSimulationStepExecuted() {
        for (MclnPropertyView mcLnPropertyView : statementViews) {
            mcLnPropertyView.recordHistory();
        }
    }

    protected void onSimulationStateChange() {
        regenerateGraphView();
        repaintAllModelNodesOnOffScreenImage();
    }

    protected void onSimulationStateReset() {
    }

    //=================================================================================================================

    //
    //   I n s t a n c e
    //

    public MclnGraphView(MclnGraphModel mclnGraphModel, int viewPadding, int options,
                         MclnGraphViewDefaultProperties mclnGraphViewDefaultProperties) {
        super(mclnGraphModel.getViewRectangle().getX(), mclnGraphModel.getViewRectangle().getY(),
                mclnGraphModel.getViewRectangle().getWidth(), mclnGraphModel.getViewRectangle().getHeight(),
                viewPadding, options);
        this.mclnGraphViewDefaultProperties = mclnGraphViewDefaultProperties;
        this.mclnGraphModel = mclnGraphModel;
        setName("MclnGraphView");
        setBorder(null);
        this.setFocusable(false);
        createAxis(axesColors);
        this.setOpaque(true);
        setBackground(mclnGraphViewDefaultProperties.getViewBackground());
        mclnGraphModel.addMclnModelBuildingListener(mclnModelStructureChangedListener);
        mclnGraphModel.addMclnModeChangedListener(mclnModeChangedListener);
        mclnGraphModel.addMclnModelSimulationListener(mclnModelSimulationListener);

        initGridAndModelRectangle();
    }

    //
    //   I n i t i a l i z a t i o n
    //

    /**
     * called to initialize this instance upon creation
     */
    protected void initGridAndModelRectangle() {

        // World is a design grid and cSys axes
        List<CSysRectangleEntity> viewWorldEntityList = new ArrayList();

        dsdsseCSysGridEntity = new DsdsseWorldScalableCSysGrid(this,
                DsdsseWorldScalableCSysGrid.DEFAULT_GRID_STEP, DsdsseWorldScalableCSysGrid.DEFAULT_GRID_MARGIN,
                DsdsseWorldScalableCSysGrid.DEFAULT_GRID_COLOR);

        dsdsseCSysGridEntity.doCSysToScreenTransformation(scr0, minScale);
        viewWorldEntityList.add(dsdsseCSysGridEntity);

        // this World Entity array contains design space grid only
        viewWorldEntityArray = viewWorldEntityList.toArray(new CSysRectangleEntity[viewWorldEntityList.size()]);
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
     * This method will never draw sprite entity on the image.
     * <p>
     * The method is called from many other methods each time
     * when the entire model  has to be  redrawn on the image.
     */
    protected synchronized void createScreenImageAndDrawMclnGraphOnIt() {
        offScreenImagePrepared = false;
        if (!createEmptyScreenImage()) {
            return;
        }

        int nElements = graphModelViewArcEntityList.size();
        for (int i = 0; i < nElements; i++) {
            MclnArcView mclnArcView = graphModelViewArcEntityList.get(i);
            if (mclnArcView.isUnderConstruction() || mclnArcView.isSprite()) {
                continue;
            }
            boolean currentMouseHover = mclnArcView.isMouseHover();
            mclnArcView.setMouseHover(false);
            mclnArcView.drawPlainEntity(offScreenImageGraphics);
            mclnArcView.setMouseHover(currentMouseHover);
        }

        nElements = graphModelViewNodeEntityList.size();
        for (int i = 0; i < nElements; i++) {
            MclnGraphNodeView mclnGraphViewEntity = graphModelViewNodeEntityList.get(i);
            if (mclnGraphViewEntity.isSprite()) {
                continue;
            }
            boolean currentMouseHover = mclnGraphViewEntity.isMouseHover();
            mclnGraphViewEntity.setMouseHover(false);
            mclnGraphViewEntity.drawPlainEntity(offScreenImageGraphics);
            mclnGraphViewEntity.setMouseHover(currentMouseHover);
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
    protected synchronized boolean createEmptyScreenImage() {
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
        offScreenImageGraphics.setColor(mclnGraphViewDefaultProperties.getViewBackground());
        offScreenImageGraphics.fillRect(0, 0, currentSize.width, currentSize.height);

        Graphics2D g2 = (Graphics2D) offScreenImageGraphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        paintWorld(offScreenImageGraphics);
//        paintAxes(offScreenImageGraphics);
        if (isProjectSpaceRectangleVisible()) {
            drawProjectRectangle(g2);
            drawProjectSpaceCoordinates(g2);
        }
        paintAxes(offScreenImageGraphics);

        offScreenImagePrepared = true;
        return offScreenImagePrepared;
    }

    protected boolean isWorldVisible() {
        return true;
    }

    protected boolean areAxesVisible() {
        return true;
    }

    protected boolean isProjectSpaceRectangleVisible() {
        return true;
    }

    /**
     * Called from extending class when model cSys rectangle changed
     *
     * @param cSysViewRectangle
     */
    protected void updateViewRectangle(DoubleRectangle cSysViewRectangle) {
        super.updateViewRectangle(cSysViewRectangle);
    }

    //=================================================================================================================

    //
    //   Methods   to   recreate   and   to   re-display   off   screen   image
    //

    //=================================================================================================================

    /**
     * called from Model Simulation Listener on Mcln Model state change event
     */
    protected void repaintAllModelNodesOnOffScreenImage() {

        System.out.println("\n\n***********************************************************");
        System.out.println(" Simulation State Change         Repainting Mcln Graph View");
        System.out.println("**************************************************************\n");

        for (MclnPropertyView mcLnPropertyView : statementViews) {
            mcLnPropertyView.updateViewOnModelChanged();
            mcLnPropertyView.drawPlainEntity(offScreenImageGraphics);
        }

        for (MclnConditionView mcLnConditionView : conditionViews) {
            mcLnConditionView.updateViewOnModelChanged();
            mcLnConditionView.drawPlainEntity(offScreenImageGraphics);
        }

        repaint();
    }

    /**
     * Called from Move Fragment and Move Entire Model operations
     * when moving finished and moved entities should be painted
     * on final location
     * <p>
     * If model data changed the model view should be scaled
     * before this method is called
     *
     * @return
     */
    protected void createAndDisplayNewOffScreenImage() {
        // The image will be painted by paintComponent method.
        // This approach eliminate flickering on the screen.
        createScreenImageAndDrawMclnGraphOnIt();
        repaint();
    }

    //  re-displaying   without   recreating

    public Graphics redisplayOffScreenImage() {
        Graphics g = super.getGraphics();
        g.drawImage(offScreenImage, 0, 0, null);
        return g;
    }


    //=================================================================================================================

    //   Methods to create McLN Graph elements

    //=================================================================================================================

    /**
     * @param mclnStatement
     */
    protected MclnPropertyView createMclnStatementView(MclnStatement mclnStatement) {
        MclnPropertyView mcLnPropertyView = MclnGraphVewFactory.createMclnGraphViewStatement(this,
                mclnStatement);
        // adding Statement view to the Graph
        statementViews.add(mcLnPropertyView);
        graphModelViewNodeEntityList.add(mcLnPropertyView);
        uidToMclnGraphNodeView.put(mcLnPropertyView.getUID(), mcLnPropertyView);
        return mcLnPropertyView;
    }

    /**
     * @param mclnCondition
     */
    protected MclnConditionView createMclnConditionView(MclnCondition mclnCondition) {
        MclnConditionView mcLnConditionView = MclnGraphVewFactory.createMclnGraphViewCondition(this,
                mclnCondition);
        // adding Condition view to the Graph
        conditionViews.add(mcLnConditionView);
        graphModelViewNodeEntityList.add(mcLnConditionView);
        uidToMclnGraphNodeView.put(mcLnConditionView.getUID(), mcLnConditionView);
        return mcLnConditionView;
    }

    /**
     * THis method should be overriden by Mcln Graph Designer
     *
     * @param mclnArc
     * @return
     */
    protected MclnArcView createIncompleteMclnArcView(MclnArc mclnArc) {

        return null;
    }

    /**
     * @param mclnArc
     */
    protected MclnArcView createMclnArcView(MclnArc mclnArc) {
        String inpNodeUID = mclnArc.getInpNodeUID();
        String outNodeUID = mclnArc.getOutNodeUID();
        MclnGraphNodeView inpNode = uidToMclnGraphNodeView.get(inpNodeUID);
        MclnGraphNodeView outNode = uidToMclnGraphNodeView.get(outNodeUID);
        MclnArcView mclnArcView = MclnGraphVewFactory.createMclnGraphViewArc(this, mclnArc, inpNode, outNode);

        inpNode.addOutputArc(mclnArcView);
        outNode.addInputArc(mclnArcView);

        // adding Arc view to the Graph
        addMclnGraphArcView(mclnArcView);
        return mclnArcView;
    }

    /**
     * @param mclnGraphArcView
     */
    protected final void addMclnGraphArcView(MclnArcView mclnGraphArcView) {
        arcViews.add(mclnGraphArcView);
        graphModelViewArcEntityList.add(mclnGraphArcView);
        uidToMclnGraphArcView.put(mclnGraphArcView.getUID(), mclnGraphArcView);
    }

    protected void eraseEntityViewUpOnDeletion() {
        createScreenImageAndDrawMclnGraphOnIt();
        repaint();
    }

    //=================================================================================================================

    @Override
    protected void paintWorld(Graphics g) {
        if (!isWorldVisible()) {
            return;
        }
        int nElements = worldEntityList.size();
        for (int i = 0; i < nElements; i++) {
            CSysLineEntity cSysEntity = (CSysLineEntity) worldEntityList.get(i);
            g.setColor(cSysEntity.getDrawColor());
            cSysEntity.draw(g);
        }
    }

    /**
     * @param g
     */
    @Override
    protected void paintAxes(Graphics g) {
        if (!areAxesVisible()) {
            return;
        }
        int nOfAxes = worldAxesList.size();
        for (int i = 0; i < nOfAxes; i++) {
            CSysLineEntity cSysEntity = (CSysLineEntity) worldAxesList.get(i);
            g.setColor(cSysEntity.getDrawColor());
            g.setColor(mclnGraphViewDefaultProperties.getAxesColor());
            cSysEntity.draw(g);
        }
    }

    /**
     * @param g
     */
    private void drawProjectRectangle(Graphics g) {

        if (!isWorldVisible()) {
            offScreenImageGraphics.setColor(mclnGraphViewDefaultProperties.getProjectAreaBackground());
            offScreenImageGraphics.fillRect(projectSpaceViewXMin, projectSpaceViewYMin, projectSpaceViewWidth,
                    projectSpaceViewHeight);
        }

        g.setColor(mclnGraphViewDefaultProperties.getProjectAreaBorderColor());
        // h
        g.drawLine(
                projectSpaceViewXMin, projectSpaceViewYMin,
                projectSpaceViewXMin, projectSpaceViewYMax);

        g.drawLine(
                projectSpaceViewXMax, projectSpaceViewYMin,
                projectSpaceViewXMax, projectSpaceViewYMax);

        // v
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

        Color coordinatesBackground = mclnGraphViewDefaultProperties.getProjectAreaCoordinatesBackground();

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

        g.setColor(coordinatesBackground);
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
        g.setColor(coordinatesBackground);
        g.fillRect(projectSpaceViewXMax - coordinatesPadWidth - border / 2 - 2, projectSpaceViewYMax + 5,
                coordinatesPadWidth + border, coordinatesPadHeight);

        g.setColor(Color.DARK_GRAY);
        g.drawString(text, projectSpaceViewXMax - coordinatesPadWidth + 1 - border / 2,
                projectSpaceViewYMax + coordinatesPadHeight);

    }

    //=================================================================================================================


    // added for SEM

    public MclnPropertyView getPropertyNodeAtCoordinates(int x, int y) {
        for (MclnPropertyView mclnPropertyView : statementViews) {
            if (mclnPropertyView.isMouseHover(x, y)) {
                return mclnPropertyView;
            }
        }
        return null;
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

    }

    /**
     * The method updates entity view coordinates after:
     * 1) model created or changed
     * 2) view changed: re-sized, rotated, or distorted
     *
     * @param mat43
     */
    @Override
    public void updateCSysEntList(double[][] mat43) {
        // scale Grid
        super.scaleEntArray(viewWorldEntityArray); // this is just a grid

        // scale axes
        super.scaleEntityCollection(super.getWorldAxesList()); // these are axes

        // scale Nodes
        super.scaleEntityCollection(graphModelViewNodeEntityList);

        // scale Arcs
        super.scaleEntityCollection(graphModelViewArcEntityList); // this updates arc arrows and some how all entities
    }
}
