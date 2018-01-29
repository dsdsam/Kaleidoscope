package adf.ui.controls.buttons;

import javax.swing.*;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class AdfToggle3DButton extends JToggleButton {

    public void updateUI() {
        setUI(OrionButtonUI.createUI(this));
    }
}
