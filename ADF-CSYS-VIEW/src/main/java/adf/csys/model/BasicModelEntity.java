/*
 * Created on Aug 6, 2005
 *
 */
package adf.csys.model;

import java.awt.Color;


/**
 * @author xpadmin
 */
abstract public class BasicModelEntity {

    protected String entityId;

    private Color color = Color.BLACK;

    protected boolean modified = true;
    protected boolean invisible;

    public BasicModelEntity() {
    }

    /**
     * @param color
     */
    public BasicModelEntity(Color color) {
        this.color = color;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    /**
     * @return Returns the color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color The color to set.
     */
    public void setColor(Color color) {
        if (color != null) {
            this.color = color;
        } else {
            this.color = Color.BLACK;
        }
    }

    /**
     * @return Returns the modified.
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified The modified to set.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setVisible(boolean invisible) {
        this.invisible = invisible;
    }

    /**
     * @param mat43
     */
    abstract public void modelToWorldTransform(double[][] mat43);

//    abstract public void modelTranslate(double[] translationVector);

}
