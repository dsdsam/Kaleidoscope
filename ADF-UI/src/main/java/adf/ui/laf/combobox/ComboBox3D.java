package adf.ui.laf.combobox;

import javax.swing.*;

/**
 * Created by u0180093 on 4/26/2016.
 */
public class ComboBox3D<E> extends JComboBox<E> {

    public ComboBox3D() {
        setEditable(false);
    }

    public ComboBox3D(E[] items) {
        super(items);
        setEditable(false);
    }

    @Override
    public void updateUI() {
        AdfComboBoxUI adfComboBoxUI = new AdfComboBoxUI();
        setUI(adfComboBoxUI);
    }
}
