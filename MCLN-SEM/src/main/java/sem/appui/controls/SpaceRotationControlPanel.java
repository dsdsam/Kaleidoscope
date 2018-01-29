/*
 * Created on Aug 7, 2005
 *
 */
package sem.appui.controls;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.KeyStroke;

/**
 * @author xpadmin
 *
 */
public class SpaceRotationControlPanel extends SESliderPanel {

    public SpaceRotationControlPanel(){
        setBorder( BorderFactory.createEmptyBorder(10,0,0,12) );
        this.setPreferredSize( new Dimension( 50, 0 ) );
        this.add( slider, BorderLayout.EAST );
    }
    
    /**
     * 
     */
    protected void initSlider( JSlider slider ){
        slider.setOrientation(JSlider.VERTICAL);
        slider.setMinimum(0);
        slider.setMaximum(90);
     
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.createStandardLabels(10);
        slider.setPaintTrack(true);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setInverted(false);
//        slider.setValue(slider.getMaximum());
        slider.setInverted( true );
        
//        slider.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),    "none");
//        slider.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),  "none");                
        slider.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),  "none");
        slider.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "none");
        slider.getActionMap().put("none", null );
       
    }
}
