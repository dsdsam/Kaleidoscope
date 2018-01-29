package adf.menu;

import javax.swing.*;

/**
 * Created by Admin on 3/13/2017.
 */
public class AdfJButton extends JButton {

    /**
     * Currently is used by DSE to represent not toggle buttons (menu ans simulation controls)
     *
     * @param icon
     */
    public AdfJButton(Icon icon){
        super(icon);
    }

    /**
     * This method prevents   the button to get
     * text when  previously created for a menu
     * item Action is set to the button and the
     * button  is  initialized  from the Action
     *
     * @param text
     */
    @Override
    public void setText(String text){
        super.setText(null);
    }
}
