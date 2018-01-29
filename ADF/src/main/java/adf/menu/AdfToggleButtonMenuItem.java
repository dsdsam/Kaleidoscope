package adf.menu;

import com.sun.java.swing.plaf.windows.WindowsButtonUI;
import com.sun.java.swing.plaf.windows.WindowsRadioButtonMenuItemUI;
import com.sun.java.swing.plaf.windows.WindowsToggleButtonUI;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import javax.swing.plaf.metal.MetalRadioButtonUI;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Admin on 3/18/2017.
 */
public class AdfToggleButtonMenuItem extends JRadioButtonMenuItem {
    /**
     * @see #getUIClassID
     * @see #readObject
     */
    private static final String uiClassID = "RadioButtonMenuItemUI";

    /**
     * Creates a <code>JRadioButtonMenuItem</code> with no set text or icon.
     */
    public AdfToggleButtonMenuItem() {
        this(null, null, false);
    }

    /**
     * Creates a <code>JRadioButtonMenuItem</code> with an icon.
     *
     * @param icon the <code>Icon</code> to display on the
     *             <code>JRadioButtonMenuItem</code>
     */
    public AdfToggleButtonMenuItem(Icon icon) {
        this(null, icon, false);
    }

    /**
     * Creates a <code>JRadioButtonMenuItem</code> with text.
     *
     * @param text the text of the <code>JRadioButtonMenuItem</code>
     */
    public AdfToggleButtonMenuItem(String text) {
        this(text, null, false);
    }

    /**
     * Creates a radio button menu item whose properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> on which to base the radio
     *          button menu item
     * @since 1.3
     */
    public AdfToggleButtonMenuItem(Action a) {
        this();
        setAction(a);
    }

    /**
     * Creates a radio button menu item with the specified text
     * and <code>Icon</code>.
     *
     * @param text the text of the <code>JRadioButtonMenuItem</code>
     * @param icon the icon to display on the <code>JRadioButtonMenuItem</code>
     */
    public AdfToggleButtonMenuItem(String text, Icon icon) {
        this(text, icon, false);
    }

    /**
     * Creates a radio button menu item with the specified text
     * and selection state.
     *
     * @param text     the text of the <code>CheckBoxMenuItem</code>
     * @param selected the selected state of the <code>CheckBoxMenuItem</code>
     */
    public AdfToggleButtonMenuItem(String text, boolean selected) {
        this(text);
        setSelected(selected);
    }

    /**
     * Creates a radio button menu item with the specified image
     * and selection state, but no text.
     *
     * @param icon     the image that the button should display
     * @param selected if true, the button is initially selected;
     *                 otherwise, the button is initially unselected
     */
    public AdfToggleButtonMenuItem(Icon icon, boolean selected) {
        this(null, icon, selected);
    }

    /**
     * Creates a radio button menu item that has the specified
     * text, image, and selection state.  All other constructors
     * defer to this one.
     *
     * @param text the string displayed on the radio button
     * @param icon the image that the button should display
     */
    public AdfToggleButtonMenuItem(String text, Icon icon, boolean selected) {
        super(text, icon);
        setModel(new JToggleButton.ToggleButtonModel());
        setSelected(selected);
        setFocusable(false);
    }

    /**
     * Returns the name of the L&amp;F class that renders this component.
     *
     * @return the string "RadioButtonMenuItemUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    public String getUIClassID() {
        return uiClassID;
    }


    /**
     * Returns a string representation of this
     * <code>JRadioButtonMenuItem</code>.  This method
     * is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not
     * be <code>null</code>.
     *
     * @return a string representation of this
     * <code>JRadioButtonMenuItem</code>
     */
    protected String paramString() {
        return super.paramString();
    }

    /**
     * Overriden to return true, JRadioButtonMenuItem supports
     * the selected state.
     */
    boolean shouldUpdateSelectedStateFromAction() {
        return true;
    }

//    public void setUI(MenuItemUI ui) {
//        super.setUI(ui);
//    }
//    public void setUI((ButtonUI)UIManager.getUI(this));



    /**
     * Resets the UI property with a value from the current look and feel.
     *
     * @see JComponent#updateUI
     */
    public void updateUI() {
        ComponentUI buttonUI;
//        buttonUI = (MenuItemUI)UIManager.getUI(this);
        buttonUI = AdfToggleButtonMenuItemUI.createUI(this);
//        buttonUI = WindowsRadioButtonMenuItemUI.createUI(this);
//        buttonUI = BasicRadioButtonMenuItemUI.createUI(this);
//        buttonUI = BasicMenuItemUI.createUI(this);

        setUI(buttonUI);
    }
//    public void updateUI() {
//        setUI((MenuItemUI)UIManager.getUI(this));
//    }
//    public void updateUI() {
//        JToggleButton tb = new JToggleButton();
//        ComponentUI buttonUI = AdfToggleButtonMenuItemUI.createUI(this);//ButtonUI)UIManager.getUI(this);
////        ComponentUI wui =   new WindowsToggleButtonUI(){
////            protected void installDefaults(AbstractButton b) {
//////                super.installDefaults(b);
////
////                    String pp = getPropertyPrefix();
//////                    dashedRectGapX = ((Integer)UIManager.get("Button.dashedRectGapX")).intValue();
//////                    dashedRectGapY = ((Integer)UIManager.get("Button.dashedRectGapY")).intValue();
//////                    dashedRectGapWidth = ((Integer)UIManager.get("Button.dashedRectGapWidth")).intValue();
//////                    dashedRectGapHeight = ((Integer)UIManager.get("Button.dashedRectGapHeight")).intValue();
////                    focusColor = UIManager.getColor(pp + "focus");
////
//////                }
////
////
//////                if (xp != null) {
//////                    b.setBorder(xp.getBorder(b, WindowsButtonUI.getXPButtonType(b)));
//////                    LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
//////                    LookAndFeel.installProperty(b, "rolloverEnabled", Boolean.TRUE);
//////                }
////        }};
//        setUI(buttonUI);
//    }
}