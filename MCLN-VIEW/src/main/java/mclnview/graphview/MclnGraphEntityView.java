package mclnview.graphview;

import adf.csys.view.BasicCSysEntity;
import adf.csys.view.CSysView;
import mcln.model.MclnEntity;

import java.awt.*;

/**
 * Created by Admin on 1/7/2016.
 *
 * All CSysEntity have two set of data
 *   1) double data that describe entity in cSys coordinates
 *   2) int data that describe entity in screen coordinates
 *
 *   The entity is created or modified in screen coordinates.
 *   When creation or modification completed screen coordinates
 *   are converted to cSys coordinates.
 *
 *   CSys data are used for rescaling screen representation
 *   when vindow is resized.
 *
 *   Created entity may be stored in the file and later retrieved.
 *   Thses entity have persistent model. When When creation or
 *   modification completed, cSys coordinates are copied to model classes.
 *
 *   Model classes are populated when saved model is retieved.
 *   If created model is used to recreated cSys view the data
 *   are copied from nodel classes to cSys entity.
 *
 *   The model classes can be used for creating views different
 *   then cSys.
 *
 */
abstract public class MclnGraphEntityView<T> extends BasicCSysEntity {

    // High level operations that user can apply to entity
    //
    //  The operations may be called from such components as:
    //  Entity creators when new entity is created
    //  Editors when entity is modified, moved or deleted
    //  Retrievers when saved view is retrieved from the file
    //  Graph View when view is resized.

    // 1. Methods used at entity development
    //        CreationStarted
    //        CreationContinues
    //        CreationFinished

    // 2. Methods used to move existing entity
    //        MovingStarted
    //        MovingContinues
    //        MovingCompleted

    // 3. Methods used to create retrieved entity
    //        BuildingRetrievedEntityStarted
    //        BuildingRetrievedEntityContinues
    //        BuildingRetrievedEntityCompleted


    private final MclnEntity mclnEntity;
    private boolean sprite;

    MclnGraphEntityView(CSysView parentCSys, MclnEntity mclnEntity) {
        super(parentCSys);
        this.mclnEntity = mclnEntity;
    }

    MclnGraphEntityView(CSysView parentCSys, MclnEntity mclnEntity, Color defaultColor) {
        super(parentCSys, defaultColor);
        this.mclnEntity = mclnEntity;
    }

    //
    // High level operations that user can apply to entity
    //

    public String getEntityTypeAsString() {
        return "Arc";
    }

    public abstract T getTheElementModel();

    public String getUID() {
        return mclnEntity.getUID();
    }

    public boolean isMclnGraphNode(){
        return this instanceof MclnGraphNodeView;
    }

    public final MclnGraphNodeView toMclnGraphNodeView() {
        return (MclnGraphNodeView) this;
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

    //
    //   M o v i n g   t h e   E n t i t y
    //

    public abstract void translate(double[] translationVector);

    abstract public void translateAndPaintEntityAtInterimLocation(Graphics g, double[] translationVector);

    public abstract void takeFinalLocation(double[] translationVector)  ;

    abstract public void resetToOriginalLocation();

    //  *********************************************************************************************************
    //
    //                                D e l e t i n g   G r a p h   E n t i t i e s
    //
    //  *********************************************************************************************************

    public abstract void prepareForDeletion();

    public abstract void cancelDeletion();


    //
    //   P a i n t i n g   t h e   e n t i t y
    //


    public boolean isSprite() {
        return sprite;
    }

    public void setSprite(boolean sprite) {
        this.sprite = sprite;
    }

    public void drawEntityOnlyAtInterimLocation(Graphics g) {

    }

    abstract public void paintExtras(Graphics g);

    abstract public void paintExtrasAtInterimLocation(Graphics g);

    @Override
    public void draw(Graphics g) {
    }

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
        return mclnEntity.getUID().equals(((MclnGraphEntityView) obj).getUID());
    }

    @Override
    public String toString() {
        return "" + getClass().getName() + ",  " + getUID();
    }
}
