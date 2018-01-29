/*
 * Created on Aug 7, 2005
 *
 */
package sem.appui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JSlider;
import javax.swing.KeyStroke;

/**
 * @author xpadmin
 *
 */
public class VerticalRotationPanel extends SESliderPanel {
    
    public AbstractAction incrementZrotationAngleAction = new AbstractAction(){
        public void actionPerformed( ActionEvent e ){
            int nextValue = VerticalRotationPanel.this.getValue()+1;
            System.out.println("VerticalRotationPanel: nextValue "+nextValue);
            if ( nextValue > 180 ){
                nextValue = -180;
            }
            setValue( nextValue );           
        }
    };
    public AbstractAction decrementZrotationAngleAction = new AbstractAction(){
        public void actionPerformed( ActionEvent e ){
            int nextValue = VerticalRotationPanel.this.getValue()-1;
            System.out.println("VerticalRotationPanel: nextValue "+nextValue);
            if ( nextValue < -180 ){
                nextValue = 180;
            }
            setValue( nextValue );    
        }
    };
    
    public VerticalRotationPanel(){
        this.setPreferredSize( new Dimension( 0, 45 ) );
        this.add( slider, BorderLayout.CENTER );
    }
    
    /**
     * 
     */
    protected void initSlider( JSlider slider ){
        slider.setOrientation(JSlider.HORIZONTAL);
        slider.setMinimum(-180);
        slider.setMaximum(180);
        slider.setBackground( Color.BLACK );
        slider.setMajorTickSpacing(45);
        slider.setMinorTickSpacing(15);
        slider.createStandardLabels(15);
        slider.setPaintTrack(true);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setInverted(false);   
        
        slider.getInputMap().clear();
        slider.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),    "none");
        slider.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),  "none");
//        slider.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "none");
//        slider.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),  "none");
        slider.getActionMap().put("none", null );
 
      
 

    }
}
