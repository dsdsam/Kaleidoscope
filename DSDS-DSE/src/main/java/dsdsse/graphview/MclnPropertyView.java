package dsdsse.graphview;

import adf.utils.RingStack;
import dsdsse.preferences.DsdsseUserPreference;
import mcln.model.MclnStatement;
import mcln.model.MclnStatementState;
import mcln.palette.CreationStatePalette;
import mcln.palette.MclnState;

import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * User: XP Admin
 * Date: May 19, 2013
 * Time: 11:59:47 AM
 * To change this template use File | Settings | File Templates.
 * <p>
 * CSysEntity (adf.csysbasedviews.view)
 * ^
 * BasicCSysEntity (adf.csysbasedviews.view)
 * ^
 * BasicCSysEntityMclnAdapter
 * ^
 * MclnGraphEntityView
 * ^
 * MclnGraphViewNode
 * ^
 * MclnPropertyView
 */
public class MclnPropertyView extends MclnGraphViewNode implements Cloneable {

    public static final int DEFAULT_TRACE_HISTORY_LENGTH = 200;
    public static int RADIUS = 0;
    public static int SELECTED_CIRCLE_RADIUS = RADIUS + 3;

    private static final Color DEFAULT_DRAWING_COLOR = Color.GRAY;
    private static final Color DEFAULT_BALL_COLOR = new Color(CreationStatePalette.CREATION_STATE.getRGB());

    static {
        int radius = DsdsseUserPreference.getPropertyBallSize();
        MclnPropertyView.setBallSize(radius);
        boolean status = DsdsseUserPreference.isPropertyView3D();
        MclnPropertyView.setDisplayMclnPropertyViewAs3DCircle(status);
    }

    public static void setBallSize(int radius) {
        RADIUS = radius;
        SELECTED_CIRCLE_RADIUS = RADIUS + 3;
        PropertyViewBall.setBallSize(radius);
    }

    // setting 3D/plain circle representation
    public static void setDisplayMclnPropertyViewAs3DCircle(boolean displayMclnPropertyViewAs3DCircle) {
        PropertyViewBall.setDisplayMclnPropertyViewAs3DCircle(displayMclnPropertyViewAs3DCircle);
    }

    //   I n s t a n c e

    private PropertyViewBall propertyViewBall;

    private MclnStatement mclnStatement;
    private MclnGraphDesignerView mclnGraphDesignerView;
    private Rectangle stateRect = new Rectangle();

    private final RingStack historyBuffer = new RingStack(DEFAULT_TRACE_HISTORY_LENGTH);

    /**
     * @param mclnGraphDesignerView
     * @param mclnStatement
     */
    MclnPropertyView(MclnGraphDesignerView mclnGraphDesignerView, MclnStatement mclnStatement) {
        super(mclnGraphDesignerView, mclnStatement, DEFAULT_DRAWING_COLOR);
        this.mclnGraphDesignerView = mclnGraphDesignerView;
        this.mclnStatement = mclnStatement;
        MclnState mclnState = mclnStatement.getInitialMclnState();
        Color currentStateColor = (mclnState != null) ? new Color(mclnState.getRGB()) : DEFAULT_BALL_COLOR;
        propertyViewBall = PropertyViewBall.createInstance(currentStateColor);
    }

    @Override
    public String getEntityTypeAsString() {
        return "Property";
    }

    public boolean hasInputGeneratingProgram() {
        return mclnStatement.hasInputGeneratingProgram();
    }

    public String getFullSubject() {
        String subject = mclnStatement.getSubject();
        String propertyName = mclnStatement.getPropertyName();
        if (subject == null || subject.length() == 0) {
            return "There is no subject";
        }
        String fullStatement = subject + "  " + propertyName;
        return fullStatement;
    }

    public String getPropertyStatement() {
        return mclnStatement.getStatementText();
    }

    List<MclnStatementState> getAllowedStatesList() {
        List<MclnStatementState> allowedStatesList =
                mclnStatement.getAvailableMclnStatementStates().getPropertyStatesAsList();
        return allowedStatesList;
    }

    /**
     * Called from Initialization Assistant to save the result of initialization
     */
    public void repaintPropertyUponInitialization() {
        MclnState mclnState = mclnStatement.getInitialMclnState();
        Color currentStateColor = (mclnState != null) ? new Color(mclnState.getRGB()) : DEFAULT_BALL_COLOR;
        initMclnPropertyView(currentStateColor);
        mclnGraphDesignerView.paintEntityOnly(this);
    }

    public String getMclnStatesPaletteName() {
        return mclnStatement.getMclnStatesPaletteName();
    }

    @Override
    public MclnPropertyView clone() {
        MclnPropertyView clonedMclnPropertyView = null;
        try {
            clonedMclnPropertyView = (MclnPropertyView) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            return clonedMclnPropertyView;
        }
    }

    @Override
    public MclnStatement getTheElementModel() {
        return mclnStatement;
    }

    @Override
    Rectangle findOutline(int screenRadius, int scrX, int scrY) {
        Rectangle outline = super.findOutline(RADIUS, scrX, scrY);
        stateRect = new Rectangle(outline);
        stateRect.grow(-6, -6);
        return outline;
    }

    //
    //  u p d a t e s
    //

    @Override
    public void updateViewOnModelChanged() {
        MclnState mclnState = mclnStatement.getCurrentMclnState();
        int newColor = mclnState.getRGB();
        Color currentStateColor = (mclnState != null) ? new Color(newColor) : DEFAULT_BALL_COLOR;
        initMclnPropertyView(currentStateColor);
    }

    private void initMclnPropertyView(Color currentStateColor) {
        propertyViewBall.setState(currentStateColor);
    }

    boolean isInitialized() {
        return true;
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        super.doCSysToScreenTransformation(scr0, scale);
    }


    //
    //   D r a w i n g   S t a t e m e n t
    //

    @Override
    public void drawPlainEntity(Graphics g) {
        drawStatement(g, scrX, scrY);
    }

    @Override
    public void newDrawEntityAndConnectedEntity(Graphics g, boolean detailedDrawing) {
        // draw arc first (ToDo the other arc end might have to be repainted as well)
        drawConnectedEntities(g);
        // draw Statement on the top
        drawStatement(g, scrX, scrY);
    }

    @Override
    public void drawEntityOnlyAtInterimLocation(Graphics g) {
        drawStatement(g, scaledTranslatedInterimScrPnt[0], scaledTranslatedInterimScrPnt[1]);
    }

    /**
     * @param g
     */
    private void drawStatement(Graphics g, int scrX, int scrY) {
        if (hidden || parentCSys == null) {
            return;
        }
        propertyViewBall.draw(g, scrX, scrY, watermarked);
    }

    /**
     * @param g
     */
    private void drawConnectedEntities(Graphics g) {

    }

    //   D r a w i n g   e x t r a s

    @Override
    public void paintExtras(Graphics g) {
        paintExtrasAtGivenScreenLocation(g, scrX, scrY);
    }

    @Override
    public void paintExtrasAtInterimLocation(Graphics g) {
        paintExtrasAtGivenScreenLocation(g, scaledTranslatedInterimScrPnt[0], scaledTranslatedInterimScrPnt[1]);
    }

    /**
     * @param g
     * @param scrX
     * @param scrY
     */
    private void paintExtrasAtGivenScreenLocation(Graphics g, int scrX, int scrY) {

        if (hidden || parentCSys == null) {
            return;
        }

        // painting highlighting circle as node border
        //     parentCSys.csysDrawScrCircle(g, getCircleColor(), scrX, scrY, RADIUS);

        if (!(isPreSelected() || isSelected())) {
            return;
        }

        paintBorder(g, scrX, scrY);

//        Graphics2D g2D = (Graphics2D) g;
//        Object currentSetting = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
//        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        Stroke currentStroke = g2D.getStroke();
//        g2D.setStroke(new BasicStroke(1));
//
//        if (isPreSelected() && !isSelected()) {
//            g2D.setColor(getPreSelectedColor());
//        }
//        if (!isPreSelected() && isSelected()) {
//            g2D.setColor(getSelectedColor());
//        }
//        if (isPreSelected() && isSelected()) {
//            g2D.setColor(getSelectedColor());
//        }
//        g.drawOval(scrX - SELECTED_CIRCLE_RADIUS, scrY - SELECTED_CIRCLE_RADIUS, 2 * SELECTED_CIRCLE_RADIUS,
//                2 * SELECTED_CIRCLE_RADIUS);
//
//        g2D.setStroke(currentStroke);
//        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
    }

    private void paintBorder(Graphics g, int scrX, int scrY) {

        if (hidden || parentCSys == null) {
            return;
        }

        g.setColor(getCircleColor());
        g.drawOval(scrX - RADIUS, scrY - RADIUS, 2 * RADIUS, 2 * RADIUS);

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
        g.drawOval(scrX - SELECTED_CIRCLE_RADIUS, scrY - SELECTED_CIRCLE_RADIUS, 2 * SELECTED_CIRCLE_RADIUS,
                2 * SELECTED_CIRCLE_RADIUS);

        g2D.setStroke(currentStroke);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, currentSetting);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return
     */
    private Color getCircleColor() {
        Color color = DEFAULT_DRAWING_COLOR;
        if (isWatermarked()) {
            color = getWatermarkColor();
        } else if (isHighlighted()) {
            color = getHighlightColor();
        } else if (isMouseHover()) {
            color = getHighlightColor();
        }
        return color;
    }

    @Override
    public String getTooltip() {
        if (mclnStatement == null) {
            return null;
        }
        return mclnStatement.toTooltip();
    }

    MclnState getCurrentState() {
        MclnState mclnState = mclnStatement.getCurrentMclnState();
        return mclnState;
    }

    @Override
    public String getOneLineInfoMessage() {
        if (mclnStatement == null) {
            return null;
        }
        MclnState calculatedSuggestedState = mclnStatement.getCalculatedSuggestedState();
        return mclnStatement.getOneLineInfoMessage() +
                ", Suggested State = " + calculatedSuggestedState.getStateName() +
                "   // pre selected = " + isPreSelected() + ", selected = " + isSelected();
    }

    //
    //   S i m u l a t i o n   H i s t o r y   R e c o r d i n g
    //

    private void createHistoryRingBuffer() {
//        int buffSize = AppEnv.getIntConfigProperty("history.buffer.size");
        historyBuffer.initBuffer(DEFAULT_TRACE_HISTORY_LENGTH);
    }

    public void recordHistory() {
        MclnState mclnState = mclnStatement.getCurrentMclnState();
//        if (!this.getUID().equals("S-0000001")) {
//            return;
//        }
        MclnState clonedMclnState = mclnState.clone();
        historyBuffer.add(clonedMclnState);
        historyBuffer.printStack();
//        System.out.println();
    }

    public RingStack getHistory() {
        return historyBuffer;
    }

    public void clearHistory() {
        historyBuffer.clear();
    }
}
