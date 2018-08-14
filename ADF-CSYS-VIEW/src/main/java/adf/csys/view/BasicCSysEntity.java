/*
 * Created on Aug 4, 2005
 *
 */
package adf.csys.view;

import java.awt.Color;

/*
     The usage of the attributes.

     This class has many attributes that control CSys entity behavior

     1) sprite - Sprite entity is not drawn on the image, but is drawn directly on the screen
     2) hidden - Is used to hide entity by not drawing it on image or the screen
     3) drugging - Drugged image should be a sprite. When this attribute is set drugged
                   the entity preserves its location and restores preserved location
                   when drugging is cancelled.
 */

/**
 * @author xpadmin
 * <p>
 * BasicCSysEntity is the base class for many CSys view package classes
 * The CSys view package classes support wireframe drawing in CSys View.
 * <p>
 * The class is also a base class for MclnGraphEntityView in DSDSSE package.
 * MclnGraphEntityView extends BasicCSysEntity to provide McLN Graph grawing
 * support in CSys View.
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
 *   Thses entity have persistent model. When creation or
 *   modification completed, cSys coordinates are copied to model classes.
 *
 *   Model classes are populated when saved model is retieved.
 *   If created model is used to recreated cSys view the data
 *   are copied from nodel classes to cSys entity.
 *
 *   The model classes can be used for creating views different
 *   than cSys.
 *
 */
/*
       Attribute         Property             Condition            Arc
                          color                color              color
    ========================================================================

        creation            -                     -                 RED

        drawing            gray                  gray               gray

        watermarked border
        watermarked

        mouse hover        orange               orange            orange
                           external             external          arrow
                           circle               square            border

        emphasized         cyan                 cyan              cyan

        highlighted        red                  red               red

        selected           magenta               magenta          magenta (arrow border)

        thread selected                                           magenta
        knots selected                                            magenta

 */
public abstract class BasicCSysEntity implements CSysEntity {

    protected static final Color DEFAULT_CREATION_COLOR = Color.RED;
    protected static final Color DEFAULT_DRAWING_COLOR = Color.GRAY;

    protected static final Color DEFAULT_WATERMARK_COLOR = new Color(0xDDDDDD);
    protected static final Color DEFAULT_WATERMARK_BORDER_COLOR = new Color(0xBBBBBB);

    protected static final Color DEFAULT_MOUSE_HOVER_COLOR = new Color(0xFF6000);
    protected static final Color DEFAULT_EMPHASIZED_COLOR = Color.CYAN; // emphasized state is interim and hence overrides selection
    protected static final Color DEFAULT_HIGHLIGHTED_COLOR = Color.RED;
    protected static final Color DEFAULT_SELECTED_COLOR = Color.MAGENTA;


    //   S t a n d a r d   S t a t u s   C o l o r s

    protected Color creationColor = DEFAULT_CREATION_COLOR;
    protected Color drawColor = DEFAULT_DRAWING_COLOR;

    protected Color watermarkColor = DEFAULT_WATERMARK_COLOR;
    protected Color watermarkBorderColor = DEFAULT_WATERMARK_BORDER_COLOR;

    protected Color mouseHoverColor = DEFAULT_MOUSE_HOVER_COLOR;
    protected Color emphasizedColor = DEFAULT_EMPHASIZED_COLOR;
    protected Color highlightColor = DEFAULT_HIGHLIGHTED_COLOR;
    protected Color preSelectedColor = DEFAULT_DRAWING_COLOR;
    protected Color selectedColor = DEFAULT_SELECTED_COLOR;


    //   T h e   E n t i t y   a t t r i b u t e s

    protected CSysView parentCSys;

    protected boolean underConstruction;  // drawColor
    protected boolean clippedOff;

    private boolean emphasized;
    protected boolean watermarked;        // watermarkColor

    private boolean mouseHover;         // highlightColor
    protected boolean preSelected;        // preSelectedColor
    protected boolean selected;           // selectedColor
    private boolean highlighted;        // highlightColor

    protected boolean hidden;


    //
    //  C r e a t i o n   m e t h o d s
    //

    public BasicCSysEntity(CSysView parentCSys) {
        this(parentCSys, DEFAULT_DRAWING_COLOR, DEFAULT_SELECTED_COLOR);
    }

    /**
     * @param parentCSys
     * @param drawColor
     */
    public BasicCSysEntity(CSysView parentCSys, Color drawColor) {
        this(parentCSys, drawColor, DEFAULT_SELECTED_COLOR);
    }

    public BasicCSysEntity(CSysView parentCSys, Color drawColor, Color selectedColor) {
        this.parentCSys = parentCSys;
        this.drawColor = drawColor;
        this.selectedColor = selectedColor;
    }

    public void setParentCSys(CSysView parentCSys) {
        this.parentCSys = parentCSys;
    }

    CSysView getParentCSys() {
        return parentCSys;
    }

    //
    //  C S y s   r e l a t e d   t r a n s f o r m a t i o n s
    //

    @Override
    public int[] doCSysToScreenTransformation(int[] scr0, double scale, double[] cSysPnt) {
        int[] scrPoint = {0, 0, 0};
        switch (parentCSys.getProjection()) {

            case CSysView.ZOXProjection:
                scrPoint[0] = (int) Math.rint(scr0[0] + cSysPnt[0] * scale);
                scrPoint[2] = (int) Math.rint(scr0[2] - cSysPnt[2] * scale);
                break;

            case CSysView.ZOYProjection:
                scrPoint[1] = (int) Math.rint(scr0[0] + cSysPnt[1] * scale);
                scrPoint[2] = (int) Math.rint(scr0[2] - cSysPnt[2] * scale);
                break;

            default:
                scrPoint[0] = (int) Math.rint(scr0[0] + cSysPnt[0] * scale);
                scrPoint[1] = (int) Math.rint(scr0[1] - cSysPnt[1] * scale);
                break;
        }
        return scrPoint;
    }


    //  =========================================================================
    //   E n t i t y   a t t r i b u t e   s e t t e r s   a n d   g e t t e r s
    //  =========================================================================


    //  C r e a t i o n


    public boolean isUnderConstruction() {
        return underConstruction;
    }

    public void setUnderConstruction(boolean underConstruction) {
        this.underConstruction = underConstruction;
    }

    public Color getCreationColor() {
        return creationColor;
    }

    public void setCreationColor(Color creationColor) {
        this.creationColor = creationColor;
    }

    public void setClippedOff(boolean clippedOut) {
        this.clippedOff = clippedOut;
    }

    public boolean isClippedOff() {
        return clippedOff;
    }


    //   E m p h a s i z i n g   o r   d e - e m p h a s i z i n g


    public boolean isEmphasized() {
        return emphasized;
    }

    public void setEmphasized(boolean emphasized) {
        this.emphasized = emphasized;
    }

    public Color getEmphasizedColor() {
        return emphasizedColor;
    }

    public boolean isWatermarked() {
        return watermarked;
    }

    public void setWatermarked(boolean watermarked) {
        this.watermarked = watermarked;
    }

    public Color getWatermarkColor() {
        return watermarkColor;
    }

    public void setWatermarkColor(Color watermarkColor) {
        this.watermarkColor = watermarkColor;
    }

    public Color getWatermarkBorderColor() {
        return watermarkBorderColor;
    }

    public void setWatermarkBorderColor(Color watermarkBorderColor) {
        this.watermarkBorderColor = watermarkBorderColor;
    }


    //   S e l e c t i n g   a n d   h i g h l i g h t i n g


    public boolean isPreSelected() {
        return preSelected;
    }

    public void setPreSelected(boolean preSelected) {
        this.preSelected = preSelected;
    }

    public Color getPreSelectedColor() {
        return preSelectedColor;
    }

    //    = = =

    // not Override
    public boolean isSelected() {
        return selected;
    }

    // not Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    //   = = =

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }

    // not Override
    public boolean isHighlightedOrSelected() {
        return highlighted || selected;
    }


    //   D r a w i n g


    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isMouseHover(int x, int y) {
        return false;
    }

    public boolean isMouseHover() {
        return mouseHover;
    }

    public void setMouseHover(boolean mouseHover) {
        this.mouseHover = mouseHover;
    }

    public Color getMouseHoverColor() {
        return mouseHoverColor;
    }

    public void setMouseHoverColor(Color mouseHoverColor) {
        this.mouseHoverColor = mouseHoverColor;
    }

    @Override
    public Color getDrawColor() {
        return highlighted ? highlightColor : drawColor;
    }

    @Override
    public void setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
    }
}
