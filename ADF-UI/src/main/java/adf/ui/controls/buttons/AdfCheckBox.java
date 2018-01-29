package adf.ui.controls.buttons;

import javax.swing.*;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class AdfCheckBox extends JCheckBox {

    public AdfCheckBox() {
        super();
        setUI(new AdfCheckBoxUI());
    }

    public AdfCheckBox(String s) {
        super(s);
        setUI(new AdfCheckBoxUI());
    }

}
