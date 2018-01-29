package adf.ui.laf.button;

import javax.swing.*;

/**
 * Created by Admin on 4/20/2016.
 */
public class Adf3DButton extends JButton{

    public Adf3DButton(){
        super("");
    }

    public Adf3DButton(String text){
        super(text);
    }

    public void updateUI() {
        setUI(AdfButton3DUI.createUI(this));
    }
}
