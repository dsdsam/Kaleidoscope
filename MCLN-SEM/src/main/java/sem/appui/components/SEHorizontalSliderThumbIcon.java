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
public class SEHorizontalSliderThumbIcon implements Icon, Serializable, UIResource {
    
    protected static SEMetalBumps controlBumps;
    protected static SEMetalBumps primaryBumps;
    
    private Color focusHighlightColor = new Color( 0xFFFF00 );
    private Color focusColor = new Color( 0xDDDD00 );
    private Color focusShadowColor = new Color( 0xAAAA00 );
    
    private Color currentHighlightColor;
    private Color currentMainColor;
    private Color currentShadowColor;

    public SEHorizontalSliderThumbIcon() {
        
        primaryBumps = new SEMetalBumps( 10, 6,
                MetalLookAndFeel.getPrimaryControl(),
                MetalLookAndFeel.getPrimaryControlDarkShadow(),
                focusColor );
        controlBumps = new SEMetalBumps( 10, 6,
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
        
        g.translate( x, y );

        // Fill in the background
        g.setColor( currentMainColor );         
        g.fillRect( 1,1, 13, 8 );    
        g.drawLine( 2,9  , 12,9 );     
        g.drawLine( 3,10 , 11,10 );    
        g.drawLine( 4,11 , 10,11 );   
        g.drawLine( 5,12 ,  9,12 );    
        g.drawLine( 6,13 ,  8,13 );    
        g.drawLine( 7,14 ,  7,14 );
     
    
        // Draw the bumps
        if ( slider.isEnabled() ) {
            if ( slider.hasFocus() ) {
                primaryBumps.paintIcon( c, g, 2, 2 );
            }else{
                controlBumps.paintIcon( c, g, 2, 2 );
            }
        }
   
    // Draw the highlight
    if ( slider.isEnabled() ) {
        g.setColor( currentHighlightColor );
        g.drawLine( 1, 1, 13, 1 );
        g.drawLine( 1, 1, 1, 8 );
    }
     
    
        // Draw the frame
        g.setColor( currentHighlightColor );
        g.drawLine(  1,0  , 13,0 );  // top
        g.drawLine(  0,1  ,  0,8 );  // left
        g.drawLine(  1,9  ,  7,15 ); // left slant
        g.setColor( currentShadowColor );
        g.drawLine( 14,1  , 14,8 );  // right
        g.drawLine(  7,15 , 14,8 );  // right slant       
    
        g.translate( -x, -y );
    }

    public int getIconWidth() {
        return 15;
    }

    public int getIconHeight() {
        return 16;
    }
}

