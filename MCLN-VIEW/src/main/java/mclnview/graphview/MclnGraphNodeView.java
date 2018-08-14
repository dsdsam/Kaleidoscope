package mclnview.graphview;

import adf.csys.view.CSysView;
import mcln.model.MclnNode;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: May 19, 2013
 * Time: 5:35:32 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MclnGraphNodeView extends MclnGraphEntityView<MclnNode> {

    private final MclnNode mclnNode;

    private double[] cSysPnt = {0, 0, 0};

    protected double[] scrPnt = {0, 0, 0};
    private double[] interimScrPnt = {0, 0, 0};
    protected int scrX, scrY;

    private double[] cSysTranslationVector;
    int[] scaledTranslatedInterimScrPnt = {0, 0, 0};

    private double[] tmpScrTranslationVector = {0, 0, 0};

    public final List<MclnArcView> inpArcList = new ArrayList();
    public final List<MclnArcView> outArcList = new ArrayList();

    Rectangle outline = new Rectangle();


    /**
     * This constructor creates point entity backed by model
     */
    public MclnGraphNodeView(CSysView parentCSysView, MclnNode mclnNode, Color defaultColor) {
        super(parentCSysView, mclnNode, defaultColor);
        this.mclnNode = mclnNode;
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

    public boolean isConnected() {
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

    public Point getScrPoint() {
        return new Point(scrX, scrY);
    }

    public double[] getScrPnt() {
        return scrPnt;
    }

    public int[] getScrVec() {
        return VAlgebra.initIntVec3(scrX, scrY, 0);
    }

    //  *********************************************************************************************************
    //
    //         M o v i n g   N o d e   a s   P a r t   o f   M o v e   E l e m e n t s   O p e r a t i o n
    //
    //  *********************************************************************************************************

    @Override
    public void setSprite(boolean sprite) {
        super.setSprite(sprite);
        for (MclnArcView inputArc : inpArcList) {
            inputArc.setSprite(sprite);
        }
        for (MclnArcView outputArc : outArcList) {
            outputArc.setSprite(sprite);
        }
    }

    public void setArcDependentNodeToBeASprite(boolean sprite) {
        super.setSprite(sprite);
    }

    public void restoreBackupCSysLocation() {
        lastNodeScrLocationWhileDragging = null;
        double[] cSysLocation = mclnNode.getCSysLocation();
        cSysPnt = VAlgebra.copyVec3(cSysLocation);
        doCSysToScreenTransformation(parentCSys.getScr0(), parentCSys.getMinScale());
        movingArcsInputOrOutputNodeUponOneOfItNodesIsMowed(parentCSys.getScr0(), parentCSys.getMinScale());
    }

    public void startMoving() {
        setSelected(true);
    }

    /**
     * Called when user moves model elements
     */

    public final void druggingSelectedNode(int x, int y) {
        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
        doCSysToScreenTransformation(parentCSys.getScr0(), parentCSys.getMinScale());
        movingArcsInputOrOutputNodeUponOneOfItNodesIsMowed(parentCSys.getScr0(), parentCSys.getMinScale());
    }

    int[] lastNodeScrLocationWhileDragging;

    public void preserveLastLocation() {
        lastNodeScrLocationWhileDragging = VAlgebra.initIntVec3(scrX, scrY, 0);
    }

    public void moveToLastLocation() {
        if (lastNodeScrLocationWhileDragging != null) {
            druggingSelectedNode(lastNodeScrLocationWhileDragging[0], lastNodeScrLocationWhileDragging[1]);
            lastNodeScrLocationWhileDragging = null;
        } else {
            restoreBackupCSysLocation();
        }
        parentCSys.repaint();
    }

    /**
     *
     */
    public void movingNodeCompleted() {
        lastNodeScrLocationWhileDragging = null;
        setSelected(false);
        mclnNode.setCSysLocation(cSysPnt);

        // save arcs persistent data to model
        for (MclnArcView inputArc : inpArcList) {
            inputArc.movingArcInputOrOutputNodeCompleted();
        }
        for (MclnArcView outputArc : outArcList) {
            outputArc.movingArcInputOrOutputNodeCompleted();
        }
    }


    /**
     * Called when node is dragged.
     * The method draws all arcs and
     * connected to the Arcs other end nodes
     * <p>
     * not override
     *
     * @param g
     */
    void drawTheNodesAllInputAndOutputArcsWithConnectedNodes(Graphics g) {
        for (MclnArcView inputArc : inpArcList) {
            inputArc.drawPlainEntity(g);
            inputArc.drawInputNode(g, false);
        }
        for (MclnArcView outputArc : outArcList) {
            outputArc.drawPlainEntity(g);
            outputArc.drawOutputNode(g, false);
        }
    }

    //  *********************************************************************************************************
    //
    //                                     M o v i n g   T h e   N o d e
    //               A s   a   P a r t   o f   a   F r a g m e n t   o r   E n t i r e   M o d e l
    //
    //  *********************************************************************************************************


    @Override
    public void moveEntityActivePointTo(int x, int y) {
        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
    }

    @Override
    public void moveEntity(int x, int y) {
        cSysPnt = parentCSys.screenPointToCSysPoint(x, y);
    }


    /**
     * @param scr0
     * @param scale
     */
    @Override
    public void placeEntity(int[] scr0, double scale) {
//        updateModelPointUponViewRedefined();
        doCSysToScreenTransformation(scr0, scale);
        movingArcsInputOrOutputNodeUponOneOfItNodesIsMowed(scr0, scale);
    }

    /**
     * @param scr0
     * @param scale
     */
    // not override
    private void movingArcsInputOrOutputNodeUponOneOfItNodesIsMowed(int[] scr0, double scale) {
        for (MclnArcView inputArc : inpArcList) {
            inputArc.movingArcInputOrOutputNode(scr0, scale);
        }
        for (MclnArcView outputArc : outArcList) {
            outputArc.movingArcInputOrOutputNode(scr0, scale);
        }
    }

    //  *********************************************************************************************************
    //
    //                                D e l e t i n g   G r a p h   E n t i t i e s
    //
    //  *********************************************************************************************************

    @Override
    public void prepareForDeletion() {
        setSelected(true);
    }

    @Override
    public void cancelDeletion() {
        setSelected(false);
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
        parentCSys.cSysPointToScreenPoint(scrPnt, cSysPnt);

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

    @Override
    public boolean isMouseHover(int x, int y) {
        boolean inside = outline.contains(x, y);
//        System.out.println("isMouseHover x = "+x+" y = "+y+" "+inside+" "+outline.toString());
        return inside;
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
        outline.x = minX;
        outline.y = minY;
        outline.width = (maxX - outline.x);
        outline.height = (maxY - outline.y);
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
