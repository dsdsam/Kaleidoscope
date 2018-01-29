package adf.ui.laf.checkbox;

import javax.swing.*;

/**
 * Created by Admin on 10/9/2016.
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
