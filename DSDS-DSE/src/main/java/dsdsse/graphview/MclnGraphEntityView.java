package dsdsse.graphview;

import adf.csys.view.BasicCSysEntity;
import adf.csys.view.CSysView;
import mcln.model.MclnEntity;

import java.awt.*;

/**
 * Created by Admin on 1/7/2016.
 */
abstract public class MclnGraphEntityView<T> extends BasicCSysEntity {

    private final MclnEntity mclnEntity;

    MclnGraphEntityView(CSysView parentCSys, MclnEntity mclnEntity) {
        super(parentCSys);
        this.mclnEntity = mclnEntity;
    }

    MclnGraphEntityView(CSysView parentCSys, MclnEntity mclnEntity, Color defaultColor) {
        super(parentCSys, defaultColor);
        this.mclnEntity = mclnEntity;
    }

    public String getEntityTypeAsString() {
        return "Arc";
    }

    public abstract T getTheElementModel();

    public String getUID() {
        return mclnEntity.getUID();
    }

    public boolean isPropertyNode(){
        return this instanceof MclnPropertyView;
    }

    public final MclnPropertyView toPropertyView() {
        return (MclnPropertyView) this;
    }

    public boolean isConditionNode(){
        return this instanceof MclnConditionView;
    }

    public final MclnConditionView toConditionView() {
        return (MclnConditionView) this;
    }

    public boolean isArc(){
        return this instanceof MclnArcView;
    }

    public final MclnArcView toArcView() {
        return (MclnArcView) this;
    }

    abstract public void placeEntity(int[] scr0, double scale);

//    public void doCSysToScreenTransformation(int[] scr0, double minScale);

//    abstract   public void redisplayEntityBeingDragged(Graphics g);

//    public void updateMovingScrPoint(double[] currentPointUnderMouseInCSysCoordinates);

//    public void drawEntityWithDependents(Graphics g);

//    abstract   public Color getDrawColor();

    //
    //   P a i n t i n g   t h e   e n t i t y
    //

    public void drawEntityOnlyAtInterimLocation(Graphics g) {

    }

    abstract public void paintExtras(Graphics g);

    abstract public void paintExtrasAtInterimLocation(Graphics g);

    //
    //   M o v i n g   t h e   E n t i t y
    //

    abstract public void translateAndPaintEntityAtInterimLocation(Graphics g, double[] translationVector);

    abstract public void resetToOriginalLocation();


    public void draw(Graphics g) {
    }

    abstract void translate(double[] translationVector);
    //
//    public int[] translateToInterimScrLocation(double[] translationVector) {
//        this.translationVector = translationVector;
//        return new int[]{0, 0, 0};
//    }

    public void takeFinalLocation(double[] translationVector) {

    }

//    public void disconnectFromGraph(){
//
//    }


//    abstract   public void setHighlighted(boolean highlighted);

    abstract public String getTooltip();

    public String getOneLineInfoMessage() {
        return "Not available";
    }

    @Override
    public int hashCode() {
        return mclnEntity.getUID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ((this == obj)) {
            return true;
        }
        if (!(obj instanceof MclnGraphEntityView)) {
            return false;
        }
        boolean equals = mclnEntity.getUID().equals(((MclnGraphEntityView) obj).getUID());
        return mclnEntity.getUID().equals(((MclnGraphEntityView) obj).getUID());
    }

    @Override
    public String toString() {
        return "" + getClass().getName() + ",  " + getUID();
    }
}
