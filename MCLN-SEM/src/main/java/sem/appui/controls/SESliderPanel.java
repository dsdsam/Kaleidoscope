/*
 * Created on Aug 7, 2005
 *
 */
package sem.appui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

/**
 * @author xpadmin
 *
 */
abstract public class SESliderPanel extends JPanel {
    
    private Color background = Color.BLACK; 
    protected JSlider slider;   
    
    public SESliderPanel(){
        setBackground( background );
        this.setBorder( BorderFactory.createEmptyBorder(2,2,2,2) );
        this.setPreferredSize( new Dimension( 50, 0 ) );
        setLayout( new BorderLayout() );
        slider = new JSlider(){
            public void setValueIsAdjusting(boolean b){
                super.setValueIsAdjusting(false);
            }
        };
        slider.setBackground( Color.BLACK );
        slider.setBorder(null);
        slider.setValue(0);
        
        initSlider( slider );
        
        Dictionary dictionary = slider.getLabelTable(); 
        Enumeration enumeration = dictionary.keys();
        Font labelFont = new Font( "", Font.PLAIN, 9 );
        while(enumeration.hasMoreElements()){
            Object element = enumeration.nextElement();
            JLabel label = new JLabel(""+element);
            label.setFont( labelFont );
            label.setForeground( new Color( 0x888888 ) );
            dictionary.put(element, label );
        }
        SESliderUI seSliderUI = new SESliderUI();     
        slider.setUI(seSliderUI);
        slider.setLabelTable(dictionary);
    }
    
    /**
     * 
     * @param sliderChangeListener
     */
    public void addSliderChangeListener( ChangeListener sliderChangeListener ){
        slider.addChangeListener(sliderChangeListener);
    }
    
    /**
     * 
     * @param slider
     */
    abstract protected void initSlider( JSlider slider );
    
    /**
     * @return Returns the slider.
     */
    public JSlider getSlider() {
        return slider;
    }
    
    /**
     * @return Returns the value.
     */
    public int getValue() {
        return slider.getValue();
    }
    /**
     * @param value The value to set.
     */
    public void setValue(int value) {       
        slider.setValue( value );
    }


}