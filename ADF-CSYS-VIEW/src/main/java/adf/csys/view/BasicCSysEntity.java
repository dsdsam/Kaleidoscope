/*
 * Created on Aug 4, 2005
 *
 */
package adf.csys.view;

import java.awt.Color;


/**
 * @author xpadmin
 *
 * BasicCSysEntity is the base class for many CSys view package classes
 * The CSys view package classes support wireframe drawing in CSys View.
 *
 * The class is also a base class for MclnGraphEntityView in DSDSSE package.
 * MclnGraphEntityView extends BasicCSysEntity to provide McLN Graph grawing
 * support in CSys View.
 */
public abstract class BasicCSysEntity implements CSysEntity {

    protected static final Color DEFAULT_DRAWING_COLOR = Color.GRAY;
    protected static final Color DEFAULT_HIGHLIGHTED_COLOR = Color.RED;
    protected static final Color DEFAULT_SELECTED_COLOR = Color.MAGENTA;
    protected static final Color DEFAULT_WATERMARK_COLOR = new Color(0xDDDDDD);
    protected static final Color DEFAULT_WATERMARK_BORDER_COLOR = new Color(0xBBBBBB);

    //   S t a n d a r d   S t a t u s   C o l o r s

    protected Color drawColor = DEFAULT_DRAWING_COLOR;
    protected Color highlightColor = DEFAULT_HIGHLIGHTED_COLOR;
    protected Color preSelectedColor = DEFAULT_DRAWING_COLOR;
    protected Color selectedColor = DEFAULT_SELECTED_COLOR;
    protected Color watermarkColor = DEFAULT_WATERMARK_COLOR;
    protected Color watermarkBorderColor = DEFAULT_WATERMARK_BORDER_COLOR;

    //   T h e   E n t i t y   a t t r i b u t e s

    protected boolean underConstruction;  // drawColor
    protected boolean hidden;
    protected boolean mouseHover;         // highlightColor
    protected boolean highlighted;        // highlightColor
    protected boolean preSelected;        // preSelectedColor
    protected boolean selected;           // selectedColor
    protected boolean watermarked;        // watermarkColor
    protected boolean clippedOff;

    protected CSysView parentCSys;

    //
    //  C r e a t i o n   m e t h o d s
    //

    public BasicCSysEntity(CSysView parentCSys) {
        this(parentCSys, DEFAULT_DRAWING_COLOR);
    }

    /**
     * @param parentCSys
     * @param drawColor
     */
    public BasicCSysEntity(CSysView parentCSys, Color drawColor) {
        this.parentCSys = parentCSys;
        this.drawColor = drawColor;
    }

    public void setParentCSys(CSysView parentCSys) {
        this.parentCSys = parentCSys;
    }

    CSysView getParentCSys() {
        return parentCSys;
    }

    //
    //  E n t i t y   a t t r i b u t e   s e t t e r s   a n d   g e t t e r s
    //

    public boolean isUnderConstruction() {
        return underConstruction;
    }

    public void setUnderConstruction(boolean underConstruction) {
        this.underConstruction = underConstruction;
    }

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

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isPreSelected() {
        return preSelected;
    }

    public void setPreSelected(boolean preSelected) {
        this.preSelected = preSelected;
    }

    // not Override
    public boolean isSelected() {
        return selected;
    }

    // not Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // not Override
    public boolean isHighlightedOrSelected() {
        return highlighted || selected;
    }

    public boolean isWatermarked() {
        return watermarked;
    }

    public void setWatermarked(boolean watermarked) {
        this.watermarked = watermarked;
    }

    public void setClippedOff(boolean clippedOut) {
        this.clippedOff = clippedOut;
    }

    public boolean isClippedOff() {
        return clippedOff;
    }

    //  S t a t u s   C o l o r   G e t t e r s   a n d   S e t t e r s

    @Override
    public Color getDrawColor() {
        return highlighted ? highlightColor : drawColor;
    }

    @Override
    public void setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }

    public Color getPreSelectedColor() {
        return preSelectedColor;
    }

    public Color getSelectedColor() {
        return selectedColor;
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
}
