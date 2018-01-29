/*
 * Created on Aug 12, 2005
 *
 */
package sem.appui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JSlider;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * @author xpadmin
 *
 */
public class SEVerticalSliderThumbIcon implements Icon, Serializable, UIResource {
    protected static SEMetalBumps controlBumps;
    protected static SEMetalBumps primaryBumps;

    private Color focusHighlightColor = new Color( 0xFFFF00 );
    private Color focusColor = new Color( 0xEEEE00 );//new Color( 0xDDDD00 );
    private Color focusShadowColor = new Color( 0xAAAA00 );
    
    private Color currentHighlightColor;
    private Color currentMainColor;
    private Color currentShadowColor;

    
    public SEVerticalSliderThumbIcon() {
        primaryBumps = new SEMetalBumps( 6, 10,
                MetalLookAndFeel.getPrimaryControl(),
                MetalLookAndFeel.getPrimaryControlDarkShadow(),
                focusColor );     
        controlBumps = new SEMetalBumps( 6, 10,
                MetalLookAndFeel.getControlHighlight(),
                MetalLookAndFeel.getControlInfo(),
                MetalLookAndFeel.getControl() );           
    }

    public void paintIcon( Component c, Graphics g, int x, int y ) {
        JSlider slider = (JSlider)c;

        if ( slider.hasFocus() ) {
            currentHighlightColor = focusHighlightColor;
            currentMainColor = focusColor;
            currentShadowColor = focusShadowColor;
        }else{
            if ( slider.isEnabled() ){
                currentHighlightColor = MetalLookAndFeel.getControlHighlight();
                currentMainColor = MetalLookAndFeel.getControl();
                currentShadowColor = MetalLookAndFeel.getControl();
            }else{
                currentHighlightColor = focusHighlightColor;
                currentMainColor = MetalLookAndFeel.getControlDarkShadow();
                currentShadowColor = MetalLookAndFeel.getControlDarkShadow();
            }
        };
        
        boolean leftToRight = true; //MetalUtils.isLeftToRight(slider);

        g.translate( x, y );


        // Fill in the background
        g.setColor( currentMainColor );
        
        if (leftToRight) {
            g.fillRect(  1,1 ,  8,13 );
    
            g.drawLine(  9,2 ,  9,12 );
            g.drawLine( 10,3 , 10,11 );
            g.drawLine( 11,4 , 11,10 );
            g.drawLine( 12,5 , 12,9 );
            g.drawLine( 13,6 , 13,8 );
            g.drawLine( 14,7 , 14,7 );
        }
        else {
            g.fillRect(  7,1,   8,13 );
    
            g.drawLine(  6,3 ,  6,12 );
            g.drawLine(  5,4 ,  5,11 );
            g.drawLine(  4,5 ,  4,10 );
            g.drawLine(  3,6 ,  3,9 );
            g.drawLine(  2,7 ,  2,8 );
        }

        // Draw the bumps
        int offset = (leftToRight) ? 2 : 8;
        if ( slider.isEnabled() ) {
            if ( slider.hasFocus() ) {
                primaryBumps.paintIcon( c, g, offset, 2 );
            }
            else {
                controlBumps.paintIcon( c, g, offset, 2 );
            }
        }
 
        // Draw the highlight
        if ( slider.isEnabled() ) {
//            g.setColor( slider.hasFocus() ? MetalLookAndFeel.getPrimaryControl()
//                : MetalLookAndFeel.getControlHighlight() );
            g.setColor( currentHighlightColor );
            if (leftToRight) {                
                g.drawLine( 1, 1, 8, 1 );
                g.drawLine( 1, 1, 1, 13 );
            } else {
                g.drawLine(  8,1  , 14,1  ); // top
                g.drawLine(  1,7  ,  7,1  ); // top slant
            }
        }
 
        // Draw the frame
        g.setColor( currentHighlightColor );
        if (leftToRight) {
            g.drawLine(  1,0  ,  8,0  ); // top
            g.drawLine(  0,1  ,  0,13 ); // left
            g.setColor( currentShadowColor );
            g.drawLine(  1,14 ,  8,14 ); // bottom
            g.drawLine(  9,1  , 15,7  ); // top slant
            g.drawLine(  9,13 , 15,7  ); // bottom slant
        }
        else {
            g.drawLine(  7,0  , 14,0  ); // top
            g.drawLine( 15,1  , 15,13 ); // right
            g.setColor( currentShadowColor );
            g.drawLine(  7,14 , 14,14 ); // bottom
            g.drawLine(  0,7  ,  6,1  ); // top slant
            g.drawLine(  0,7  ,  6,13 ); // bottom slant
        }

        g.translate( -x, -y );
    }

    public int getIconWidth() {
        return 16;
    }

    public int getIconHeight() {
        return 15;
    }
}
