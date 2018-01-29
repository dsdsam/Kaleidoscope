package mclnview.graphview;

import adf.csys.view.CSysView;
import mcln.model.MclnCondition;
import mcln.palette.MclnState;

import java.awt.*;

/**
 * Created by Admin on 11/14/2017.
 */
public class MclnConditionView extends MclnGraphViewNode {

    public static final int SCREEN_RADIUS = 4;

    private static final Color DEFAULT_DRAWING_COLOR = Color.LIGHT_GRAY.brighter();
    private static final Color DEFAULT_BORDER_COLOR = Color.GRAY;

    private double scrCenter[] = {0, 0, 0};

    private double clPole[] = {0, 0, 0};
    private double crPole[] = {0, 0, 0};
    private double ctPole[] = {0, 0, 0};
    private double cbPole[] = {0, 0, 0};

    private double leftSide[] = {0, 0, 0};
    private double rightSide[] = {0, 0, 0};

    private double scrBase[] = {0, 0, 0};

    // Graph properties
    MclnPropertyView inpFact;
    MclnPropertyView outFact;


    private Rectangle stateRect = new Rectangle();

    private Color stateColor = new Color(0xEEEEEE);

    private MclnCondition mclnCondition;
    private CSysView parentCSys;

    /**
     * @param parentCSys
     * @param mclnCondition
     */
    public MclnConditionView(CSysView parentCSys, MclnCondition mclnCondition) {
        super(parentCSys, mclnCondition, DEFAULT_DRAWING_COLOR);
        this.parentCSys = parentCSys;
        this.mclnCondition = mclnCondition;
        stateColor = getMclnConditionStateColor(mclnCondition);
    }

    MclnState getCurrentState(){
        MclnState mclnState = mclnCondition.getCurrentMclnState();
        return mclnState;
    }

    @Override
    public String getEntityTypeAsString(){
        return "Condition";
    }

    @Override
    Rectangle findOutline(int screenRadius, int scrX, int scrY) {
        Rectangle outline = super.findOutline(SCREEN_RADIUS, scrX, scrY);
        stateRect = new Rectangle(outline);
        stateRect.grow(-2, -2);
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
        stateColor = getMclnConditionStateColor(mclnCondition);
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

    @Override
    public void drawPlainEntity(Graphics g) {
        if (hidden || parentCSys == null) {
            return;
        }
        paintPlaneCondition(g, scrX, scrY);
    }

    @Override
    public void newDrawEntityAndConnectedEntity(Graphics g, boolean detailedDrawing) {
        // draw arc first (ToDo the other arc end might have to be repainted as well)
        drawConnectedEntities(g);
        // draw Statement on the top
        paintPlaneCondition(g, scrX, scrY);
    }

    @Override
    public void drawEntityOnlyAtInterimLocation(Graphics g) {
        paintPlaneCondition(g, scaledTranslatedInterimScrPnt[0], scaledTranslatedInterimScrPnt[1]);
    }

    /**
     *
     * @param g
     * @param scrX
     * @param scrY
     */
    private void paintPlaneCondition(Graphics g, int scrX, int scrY) {

        if (hidden || parentCSys == null) {
            return;
        }

        Color conditionColor = getConditionColor();
        g.setColor(conditionColor);
        g.fillRect(stateRect.x + 1, stateRect.y + 1, stateRect.width - 1, stateRect.height - 1);

        Color conditionBorderColor = getBorderColor();
        parentCSys.csysDrawScrCircle(g, conditionBorderColor, scrX, scrY, 4);
    }

    /**
     * @param g
     */
    private void drawConnectedEntities(Graphics g ) {

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
     *
     * @param g
     * @param outline
     */
    private void paintExtrasAtGivenScreenLocation(Graphics g, Rectangle outline) {

        if (hidden || parentCSys == null) {
            return;
        }

        if (!(isPreSelected() || isSelected())) {
            return;
        }

        Graphics2D g2D = (Graphics2D) g;
        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Stroke currentStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke(1));

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
        Color color = stateColor;
        if (isWatermarked()) {
            color = Color.WHITE;
            return color;
        }
        return color;
    }

    /**
     *
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


    public void drawKnob(Graphics g, int x, int y, Color drawColor, Color fillColor) {
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

    public void drawKnot(Graphics g, int x, int y, Color fillColor) {
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