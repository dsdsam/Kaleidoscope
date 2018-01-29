package mclnview.graphview;

import adf.csys.view.CSysView;
import mcln.model.MclnNode;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11/14/2017.
 */
public abstract class MclnGraphViewNode extends MclnGraphEntityView<MclnNode> {

    private final MclnNode mclnNode;

    private double[] modelPnt = new double[3];
    private double[] cSysPnt = {0, 0, 0};

    protected double[] scrPnt = {0, 0, 0};
    private double[] interimScrPnt = {0, 0, 0};
    protected int scrX, scrY;

    private double[] cSysTranslationVector;
    int[] scaledTranslatedInterimScrPnt = {0, 0, 0};

    private double[] tmpScrTranslationVector = {0, 0, 0};

    final List<MclnArcView> inpArcList = new ArrayList();
    final List<MclnArcView> outArcList = new ArrayList();

    Rectangle outline = new Rectangle();

//    public MclnGraphViewPointEntity(MclnGraphView mclnGraphView) {
//        super(mclnGraphView, VAlgebra.initVec3(0, 0, 0));
//    }

    /**
     * @param mclnGraphView
     * @param x
     * @param y
     * @param z
     */
//    public MclnGraphViewPointEntity(MclnGraphView mclnGraphView, double x, double y, double z) {
//        super(mclnGraphView, VAlgebra.initVec3(x, y, z));
//
//    }

    /**
     * This constructor creates point entity backed by model
     */
//    public MclnGraphViewPointEntity(CSysView parentCSysView, double[] point, Color defaultColor) {
//        super(parentCSysView, point, defaultColor);
//    }
    public MclnGraphViewNode(CSysView parentCSysView, MclnNode mclnNode, Color defaultColor) {
        super(parentCSysView, mclnNode, defaultColor);
        this.mclnNode = mclnNode;
        modelPnt = cSysPnt;
        this.cSysPnt = VAlgebra.copyVec3(mclnNode.getCSysLocation());
    }


    //
    //   c r e a t i o n
    //

    public void addInputArc(MclnArcView mclnArcView) {
        inpArcList.add(mclnArcView);
    }

    public void removeInputArc(MclnArcView mclnArcView) {
        inpArcList.remove(mclnArcView);
    }

    public void addOutputArc(MclnArcView mclnArcView) {
        outArcList.add(mclnArcView);
    }

    public void removeOutputArc(MclnArcView mclnArcView) {
        outArcList.remove(mclnArcView);
    }

    boolean isConnected() {
        return inpArcList.size() > 0 || outArcList.size() > 0;
    }

    //
    //   A c c e s s   a n d    m o d i f i c a t i o n
    //

    public abstract MclnNode getTheElementModel();

    public abstract void updateViewOnModelChanged();

    public double[] getCSysPnt() {
        return cSysPnt;
    }

    public double[] getScrPnt() {
        return scrPnt;
    }

    public double[] getInterimScrPnt() {
        return interimScrPnt;
    }

    //
    //   M o v i n g    t h e   e n t i t y
    //


    @Override
    public void moveEntityActivePointTo(int x, int y) {
        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
    }

    @Override
    public void moveEntity(int x, int y) {
        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
//        System.out.println("move Entity: " + this.getClass().getSimpleName() + " " + cSysPnt[0]);
    }


    /**
     * called after dragging stopped (mouse released)
     */

    // public void updateEntityUponViewRedefined() {
//        VAlgebra.copyVec3(modelPnt, cSysPnt);
//    }
    @Override
    public void placeEntity(int[] scr0, double scale) {
        updateModelPointUponViewRedefined();
        doCSysToScreenTransformation(scr0, scale);
        updateArcsPlacement(getModelPoint(), scr0, scale);
    }

    public void updateModelPointUponViewRedefined() {
        VAlgebra.copyVec3(modelPnt, cSysPnt);
    }

    public double[] getModelPoint() {
        return VAlgebra.copyVec3(modelPnt);
    }

    /**
     * @param cSysPnt
     */
    private void updateArcsPlacement(double[] cSysPnt, int[] scr0, double scale) {
        updateInputArcsPlacement(cSysPnt, scr0, scale);
        updateOutputArcsPlacement(cSysPnt, scr0, scale);
    }

    /**
     * @param cSysPnt
     */
    private void updateInputArcsPlacement(double[] cSysPnt, int[] scr0, double scale) {
        for (MclnArcView inputArc : inpArcList) {
            inputArc.updateArcPlacement(scr0, scale);
        }
    }

    /**
     * @param cSysPnt
     */
    private void updateOutputArcsPlacement(double[] cSysPnt, int[] scr0, double scale) {
        for (MclnArcView outputArc : outArcList) {
            outputArc.updateArcPlacement(scr0, scale);
        }
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        scrX = scr0[0] + (int) Math.rint(cSysPnt[0] * scale);
        scrY = scr0[1] + (int) Math.rint(-cSysPnt[1] * scale);
        scrPnt[0] = scrX;
        scrPnt[1] = scrY;

        if (cSysTranslationVector == null) {
            VAlgebra.doubleVec3ToIntVec3(scaledTranslatedInterimScrPnt, scrPnt);
            outline = findOutline(0, scrX, scrY);
            return;
        }

        VAlgebra.copyVec3(interimScrPnt, scrPnt);
        parentCSys.scaleCSysPntToScrPnt(tmpScrTranslationVector, cSysTranslationVector);
        VAlgebra.translateVec(interimScrPnt, tmpScrTranslationVector);
        VAlgebra.doubleVec3ToIntVec3(scaledTranslatedInterimScrPnt, interimScrPnt);
        outline = findOutline(0, scaledTranslatedInterimScrPnt[0], scaledTranslatedInterimScrPnt[1]);
    }

    //
    //   M o v i n g   t h e   E n t i t y
    //

    @Override
    public void translate(double[] translationVector) {
        VAlgebra.copyVec3(interimScrPnt, scrPnt);
        VAlgebra.translateVec(interimScrPnt, translationVector);
        VAlgebra.doubleVec3ToIntVec3(scaledTranslatedInterimScrPnt, interimScrPnt);
        scrX = scaledTranslatedInterimScrPnt[0];
        scrY = scaledTranslatedInterimScrPnt[1];
        outline = findOutline(0, scrX, scrY);
    }

    @Override
    public void translateAndPaintEntityAtInterimLocation(Graphics g, double[] translationVector) {
        cSysTranslationVector = parentCSys.scaleScrPntToCSysPnt(translationVector);

        VAlgebra.copyVec3(interimScrPnt, scrPnt);
        VAlgebra.translateVec(interimScrPnt, translationVector);
        VAlgebra.doubleVec3ToIntVec3(scaledTranslatedInterimScrPnt, interimScrPnt);
        outline = findOutline(0, scaledTranslatedInterimScrPnt[0], scaledTranslatedInterimScrPnt[1]);

        repaintEntityAtInterimScrLocation(g);
    }

    public void repaintEntityAtInterimScrLocation(Graphics g) {
        drawEntityOnlyAtInterimLocation(g);
        paintExtrasAtInterimLocation(g);
    }

    @Override
    public void resetToOriginalLocation() {
//        double[] cSysPnt = mclnNode.getCSysLocation();
        parentCSys.cSysPointToScreenPoint(scrPnt, cSysPnt);
//        scrX = (int) scrPnt[0];
//        scrY = (int) scrPnt[1];

        VAlgebra.copyVec3(interimScrPnt, scrPnt);
        VAlgebra.doubleVec3ToIntVec3(scaledTranslatedInterimScrPnt, interimScrPnt);

        cSysTranslationVector = null;
        outline = findOutline(0, scrX, scrY);
    }

    public double[] getFinalLocationScaledScrVector() {
        double[] tmpScrTranslationVector = parentCSys.scaleCSysPntToScrPnt(null, cSysTranslationVector);
        return tmpScrTranslationVector;
    }

    @Override
    public void takeFinalLocation(double[] translationVector) {
        VAlgebra.copyVec3(scrPnt, interimScrPnt);
        scrX = scaledTranslatedInterimScrPnt[0];
        scrY = scaledTranslatedInterimScrPnt[1];

        parentCSys.screenPointToCSysPoint(cSysPnt, scrPnt);
        mclnNode.setCSysLocation(cSysPnt);

        cSysTranslationVector = null;
        outline = findOutline(0, scrX, scrY);
    }

    //
    //   p a i n t i n g   a n d   e r a s i n g
    //

    // no override
    public void setStateSelected() {
//        if (savedState != NOT_USED )
//            return;
//

//        savedState = curState;
//        savedDrawColor = curDrawColor;
//        savedFillColor = curFillColor;
//        curState = SELECTED_STATE;
//        setCurrentState( curDrawColor,
//                HIGHLIGHT_COLOR );
    }

    /**
     * @param levelsToDraw
     */
    public void redrawWithChildren(int levelsToDraw) {
        boolean draw = true;
        boolean xorMode = false;
//        Graphics g = parentCSys.getScrImgGraphics();
//        drawWithChildren(g, draw, xorMode, levelsToDraw);
//        parentCSys.redisplayOffScreenImageAndPaintExtrasOnTheScreen();
    }

    /**
     * @param g
     * @param draw
     * @param xorMode
     * @param levelsToDraw
     */
    public void drawWithChildren(Graphics g, boolean draw, boolean xorMode, int levelsToDraw) {
        boolean simleForm = false;
        if (levelsToDraw > 0) {
            levelsToDraw--;
            drawChildren(g, draw, xorMode, levelsToDraw);
        }

//        draw(g, draw, xorMode, simleForm);
    }

    /**
     * @param g
     * @param draw
     * @param xorMode
     * @param levelsToDraw
     */
    protected void drawChildren(Graphics g, boolean draw, boolean xorMode, int levelsToDraw) {
        MclnArcView mclnArcView;
        int numObjects = inpArcList.size();
        for (int i = 0; i < numObjects; i++) {
            mclnArcView = inpArcList.get(i);
//            mclnGraphArcViewEntity.drawWithChildren(g, draw, xorMode, levelsToDraw);
        }
        numObjects = outArcList.size();
        for (int i = 0; i < numObjects; i++) {
            mclnArcView = outArcList.get(i);
//            mclnGraphArcViewEntity.drawWithChildren(g, draw, xorMode, levelsToDraw);
        }
    }

    @Override
    public boolean isMouseHover(int x, int y) {
        boolean inside = outline.contains(x, y);
        return inside;
    }


    public void redisplayEntityBeingDragged(Graphics g) {
        for (MclnArcView inputArc : inpArcList) {
            inputArc.draw(g);
        }
        for (MclnArcView outputArc : outArcList) {
            outputArc.draw(g);
        }
        drawPlainEntity(g);
//        drawNodeWithArs(g, false);
    }


    /**
     * @param screenRadius
     * @return
     */
    Rectangle findOutline(int screenRadius, int scrX, int scrY) {
        int gap = 2;
        int minX = scrX - (screenRadius + gap);
        int minY = scrY - (screenRadius + gap);
        int maxX = scrX + (screenRadius + gap);
        int maxY = scrY + (screenRadius + gap);
//        scrX = (int) scrPnt[0];
//        scrY = (int) scrPnt[1];
//        scrX = (int) scrPnt[0];
//        scrY = (int) scrPnt[1];

//        minX = ctPole[0] < crPole[0] ? ctPole[0] : crPole[0];
//        minX = minX < cbPole[0] ? minX : cbPole[0];
//        minX = minX < clPole[0] ? minX : clPole[0];
//        maxX = ctPole[0] > crPole[0] ? ctPole[0] : crPole[0];
//        maxX = maxX > cbPole[0] ? maxX : cbPole[0];
//        maxX = maxX > clPole[0] ? maxX : clPole[0];
//
//        minY = ctPole[1] < crPole[1] ? ctPole[1] : crPole[1];
//        minY = minY < cbPole[1] ? minY : cbPole[1];
//        minY = minY < clPole[1] ? minY : clPole[1];
//        maxY = ctPole[1] > crPole[1] ? ctPole[1] : crPole[1];
//        maxY = maxY > cbPole[1] ? maxY : cbPole[1];
//        maxY = maxY > clPole[1] ? maxY : clPole[1];

//        outline.x = (int) (minX + 0.5);
//        outline.y = (int) (minY + 0.5);
//        outline.width = (((int) (maxX + 0.5)) - outline.x) - 1;
//        outline.height = (((int) (maxY + 0.5) - outline.y)) - 1;

        outline.x = minX;
        outline.y = minY;
        outline.width = (maxX - outline.x);
        outline.height = (maxY - outline.y);


//        outline.setLocation(scrX + outline.x, scrY + outline.y);
//        upperLeft[0] = minX;
//        upperLeft[1] = minY;
//        lowerRight[0] = maxX;
//        lowerRight[1] = maxY;

        return outline;
    }

    /**
     * Method is coled when the node is disconnected from the graph
     */
    public void disconnectFromAllArc() {
        for (MclnArcView mclnArcView : inpArcList) {
            mclnArcView.disconnectFromNode(this);
        }
        for (MclnArcView mclnArcView : outArcList) {
            mclnArcView.disconnectFromNode(this);
        }
        inpArcList.clear();
        outArcList.clear();
    }


}
