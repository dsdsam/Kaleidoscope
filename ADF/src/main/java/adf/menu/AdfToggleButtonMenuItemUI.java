package adf.menu;

import sun.awt.AppContext;
import sun.swing.MenuItemLayoutHelper;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;

/**
 * Created by Admin on 3/18/2017.
 */
public class AdfToggleButtonMenuItemUI
        extends AdfBasicMenuItemUI {//
//extends BasicRadioButtonUI {

    public static ComponentUI createUI(JComponent b) {
        return new AdfToggleButtonMenuItemUI();
    }


}
