package mclnview.graphview;

import mcln.model.MclnCondition;
import mcln.palette.MclnState;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: May 23, 2013
 * Time: 8:49:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnConditionView extends MclnGraphNodeView {

    public static final int SCREEN_RADIUS = 5;

    private static final Color DEFAULT_DRAWING_COLOR = Color.LIGHT_GRAY.brighter();
    private static final Color DEFAULT_BORDER_COLOR = Color.GRAY;

    private Rectangle stateRect = new Rectangle();

    private MclnCondition mclnCondition;
    private MclnGraphView parentCSys;

    /**
     * @param parentCSys
     * @param mclnCondition
     */
    public MclnConditionView(MclnGraphView parentCSys, MclnCondition mclnCondition) {
        super(parentCSys, mclnCondition, DEFAULT_DRAWING_COLOR);
        this.parentCSys = parentCSys;
        this.mclnCondition = mclnCondition;
    }

    MclnState getCurrentState() {
        MclnState mclnState = mclnCondition.getCurrentMclnState();
        return mclnState;
    }

    @Override
    public String getEntityTypeAsString() {
        return "Condition";
    }

    @Override
    Rectangle findOutline(int screenRadius, int scrX, int scrY) {
        Rectangle outline = super.findOutline(SCREEN_RADIUS, scrX, scrY);
        stateRect = new Rectangle(outline);
        stateRect.grow(-3, -3);
        return outline;
    }

    public MclnCondition getMclnCondition() {
        return mclnCondition;
    }

    @Override
    public MclnCondition getTheElementModel() {
        return mclnCondition;
    }

    /**
     * @param mclnCondition
     * @return
     */
    private Color getMclnConditionStateColor(MclnCondition mclnCondition) {
        MclnState mclnState = mclnCondition.getCurrentMclnState();
        int newColor = mclnState.getRGB();
        Color stateColor = new Color(newColor);
        return stateColor;
    }

    //
    //  u p d a t e s
    //

    @Override
    public void updateViewOnModelChanged() {

    }

    /**
     * @param scr0
     * @param scale
     */
    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        super.doCSysToScreenTransformation(scr0, scale);
    }


    //
    //   D r a w i n g   C o n d i t i o n
    //

    /**
     * The method draws all the Node's input and output arcs
     * where arcs draw their self and the Nodes attached to
     * thr arcs opposite end. THen the method draws the Node
     * itself, and finally draws extras (highlighted selection).
     *
     * @param g
     * @param scr0
     * @param scale
     */
    @Override
    public void drawSpriteEntity(Graphics g, int[] scr0, double scale) {
        drawTheNodesAllInputAndOutputArcsWithConnectedNodes(g);
        drawPlainEntity(g);
        paintExtras(g);
    }

    @Override
    public void drawPlainEntity(Graphics g) {
        if (hidden || parentCSys == null) {
            return;
        }
        paintPlainCondition(g, scrX, scrY);
    }

    @Override
    public void newDrawEntityAndConnectedEntity(Graphics g, boolean detailedDrawing) {
        // draw arc first (ToDo the other arc end might have to be repainted as well)
        drawConnectedEntities(g);
        // draw Statement on the top
        paintPlainCondition(g, scrX, scrY);
    }

    @Override
    public void drawEntityOnlyAtInterimLocation(Graphics g) {
        paintPlainCondition(g, scaledTranslatedInterimScrPnt[0], scaledTranslatedInterimScrPnt[1]);
    }

    /**
     * @param g
     * @param scrX
     * @param scrY
     */
    private void paintPlainCondition(Graphics g, int scrX, int scrY) {

        if (hidden || parentCSys == null) {
            return;
        }

        Color conditionColor = getConditionColor();
        g.setColor(conditionColor);
        g.fillRect(stateRect.x + 1, stateRect.y + 1, stateRect.width - 1, stateRect.height - 1);

        Color conditionBorderColor = Color.GRAY;//getBorderColor();
        parentCSys.csysDrawScrCircle(g, conditionBorderColor, scrX, scrY, SCREEN_RADIUS-1);
    }

    /**
     * @param g
     */
    private void drawConnectedEntities(Graphics g) {

    }

    //   D r a w i n g   e x t r a s

    @Override
    public void paintExtras(Graphics g) {
        paintExtrasAtGivenScreenLocation(g, outline);
    }

    @Override
    public void paintExtrasAtInterimLocation(Graphics g) {
        paintExtrasAtGivenScreenLocation(g, outline);
    }

    /**
     * @param g
     * @param outline
     */
    private void paintExtrasAtGivenScreenLocation(Graphics g, Rectangle outline) {

        if (hidden || parentCSys == null) {
            return;
        }

        if (!(isPreSelected() || isSelected() || isMouseHover())) {
            return;
        }

        Graphics2D g2D = (Graphics2D) g;
        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Stroke currentStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke(1));

         /*
           Mouse Hover highlighting will be overridden
           by selection colors
         */
        if (isMouseHover()) {
            g2D.setColor(getMouseHoverColor());
        }

        if (isPreSelected() && !isSelected()) {
            g2D.setColor(getPreSelectedColor());
        }
        if (!isPreSelected() && isSelected()) {
            g2D.setColor(getSelectedColor());
        }
        if (isPreSelected() && isSelected()) {
            g2D.setColor(getSelectedColor());
        }
        g.drawRect(outline.x, outline.y, outline.width, outline.height);

        g2D.setStroke(currentStroke);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return
     */
    private Color getConditionColor() {
        Color color = getMclnConditionStateColor(mclnCondition);;
        if (isWatermarked()) {
            color = Color.WHITE;
        }
        return color;
    }

    @Override
    public Color getMouseHoverColor() {
        return new Color(0xFF9000);
    }

    /**
     * @return
     */
    private Color getBorderColor() {
        Color color = DEFAULT_BORDER_COLOR;

        if (isWatermarked()) {
            color = getWatermarkBorderColor();
            return color;
        }

        if (isSelected()) {
            color = getHighlightColor();
        } else if (isHighlighted()) {
            color = getHighlightColor();
        } else if (isMouseHover()) {
            color = getHighlightColor();
        }
        return color;
    }


    private void drawKnob(Graphics g, int x, int y, Color drawColor, Color fillColor) {
        g.setColor(drawColor);
        g.drawLine(x - 1, y - 3, x + 1, y - 3);
        g.drawLine(x - 2, y - 2, x + 2, y - 2);
        g.drawLine(x - 3, y - 1, x + 3, y - 1);
        g.drawLine(x - 3, y, x + 3, y);
        g.drawLine(x - 3, y + 1, x + 3, y + 1);
        g.drawLine(x - 2, y + 2, x + 2, y + 2);
        g.drawLine(x - 1, y + 3, x + 1, y + 3);
        drawKnot(g, x, y, fillColor);
    }

    private void drawKnot(Graphics g, int x, int y, Color fillColor) {
        g.setColor(fillColor);
        g.drawLine(x - 1, y - 2, x + 1, y - 2);
        g.drawLine(x - 2, y - 1, x + 2, y - 1);
        g.drawLine(x - 2, y, x + 2, y);
        g.drawLine(x - 2, y + 1, x + 2, y + 1);
        g.drawLine(x - 1, y + 2, x + 1, y + 2);
    }

    public String getTooltip() {
        if (mclnCondition == null) {
            return null;
        }
        return mclnCondition.toTooltip();
    }

    @Override
    public String getOneLineInfoMessage() {
        if (mclnCondition == null) {
            return null;
        }
        return mclnCondition.getOneLineInfoMessage() + "   // pre selected = " + isPreSelected() +
                ", selected = " + isSelected();
    }
}