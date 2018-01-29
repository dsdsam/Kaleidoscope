package dsdsse.preferences;

import adf.app.StandardFonts;
import adf.ui.laf.checkbox.AdfCheckBox;

import java.awt.*;

/**
 * Created by Admin on 10/9/2016.
 */
public class PrefsCheckBox extends AdfCheckBox {

    private static final Color FOREGROUND = Color.DARK_GRAY;

    public PrefsCheckBox() {
        setFont(StandardFonts.FONT_DIALOG_PLAIN_10);
        this.setForeground(FOREGROUND);
    }

    public PrefsCheckBox(String s) {
        super(s);
        setFont(new Font("Dialog", javax.swing.plaf.FontUIResource.PLAIN, 10));
        this.setForeground(FOREGROUND);
    }
}
