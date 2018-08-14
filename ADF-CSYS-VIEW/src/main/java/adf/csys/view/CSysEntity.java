/*
 * Created on Aug 4, 2005
 *
 */
package adf.csys.view;

import java.awt.*;

/**
 * @author xpadmin
 */
public interface CSysEntity {

    //
    // The interface defines groups of methods
    //
    // 1. Entity attribute setters and getters
    //


    // 5. Methods used to update entity
    // 6. Methods used for CSys related transformations
    // 7. Drawing methods
    //

    //
    //  E n t i t y   a t t r i b u t e   s e t t e r s   a n d   g e t t e r s
    //

    public default Color getCurrentColor() {
        return getDrawColor();
    }

    public Color getDrawColor();

    public void setDrawColor(Color drawColor);

    //
    //  U p d a t e s
    //

    default void updateEntityUponViewRedefined() {

    }

    default void updateUponModelRedefined() {

    }

    //
    //  C S y s   r e l a t e d   t r a n s f o r m a t i o n s
    //

    public void doCSysToScreenTransformation(int[] scr0, double scale);

    default int[] doCSysToScreenTransformation(int[] scr0, double scale, double[] cSysPnt) {
        return new int[]{0, 0, 0};
    }

    default void doTransformation(double[][] mat43) {
    }

    default void doNextTransformation(double[][] mat43) {
    }

    default void doXClipping(double clippingPlane) {
    }

    default void doProspectiveDistortion() {

    }

    //   m o v i n g   e n t i t y

    default void moveEntityActivePointTo(int x, int y) {
    }

    default void moveEntityActivePointTo(double[] cSysPnt) {

    }

    default void moveEntity(int x, int y) {

    }

    default void placeEntity(int[] scr0, double scale) {
    }

    //
    //  D r a w i n g   m e t h o d s
    //

    default void drawSpriteEntity(Graphics g, int[] scr0, double scale) {

    }

    default void drawPlainEntity(Graphics g) {

    }

    default void drawEntityAndConnectedEntity(Graphics g) {
        newDrawEntityAndConnectedEntity(g, false);
    }

    default void newDrawEntityAndConnectedEntity(Graphics g, boolean detailedDrawing) {

    }

    default void drawEntityAndConnectedEntityExtras(Graphics g, boolean detailedDrawing) {

    }

    default void draw(Graphics g) {

    }
}
