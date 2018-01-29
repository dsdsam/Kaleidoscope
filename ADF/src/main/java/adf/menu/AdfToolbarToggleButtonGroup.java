package adf.menu;

import javax.swing.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by Admin on 3/6/2017.
 */
public class AdfToolbarToggleButtonGroup extends ButtonGroup {

    // the list of buttons participating in this group
    protected Vector<AbstractButton> buttons = new Vector<AbstractButton>();

    /**
     * The current selection.
     */
    ButtonModel selection = null;

    /**
     * Creates a new <code>ButtonGroup</code>.
     */
    public AdfToolbarToggleButtonGroup() {}

    /**
     * Adds the button to the group.
     * @param b the button to be added
     */
    public void add(AbstractButton b) {
        if(b == null) {
            return;
        }
        buttons.addElement(b);

        if (b.isSelected()) {
            if (selection == null) {
                selection = b.getModel();
            } else {
                b.setSelected(false);
            }
        }

        b.getModel().setGroup(this);
    }

    /**
     * Removes the button from the group.
     * @param b the button to be removed
     */
    public void remove(AbstractButton b) {
        if(b == null) {
            return;
        }
        buttons.removeElement(b);
        if(b.getModel() == selection) {
            selection = null;
        }
        b.getModel().setGroup(null);
    }

    /**
     * Clears the selection such that none of the buttons
     * in the <code>ButtonGroup</code> are selected.
     *
     * @since 1.6
     */
    public void clearSelection() {
        if (selection != null) {
            ButtonModel oldSelection = selection;
            selection = null;
            oldSelection.setSelected(false);
        }
    }

    /**
     * Returns all the buttons that are participating in
     * this group.
     * @return an <code>Enumeration</code> of the buttons in this group
     */
    public Enumeration<AbstractButton> getElements() {
        return buttons.elements();
    }

    /**
     * Returns the model of the selected button.
     * @return the selected button model
     */
    public ButtonModel getSelection() {
        return selection;
    }

    /**
     * Sets the selected value for the <code>ButtonModel</code>.
     * Only one button in the group may be selected at a time.
     * @param m the <code>ButtonModel</code>
     * @param b <code>true</code> if this button is to be
     *   selected, otherwise <code>false</code>
     */
    public void setSelected(ButtonModel m, boolean b) {
        if (b && m != null && m != selection) {
            ButtonModel oldSelection = selection;
            selection = m;
            if (oldSelection != null) {
                oldSelection.setSelected(false);
            }
            m.setSelected(true);
        }else if (!b && m != null && m == selection) {
            selection = null;
            m.setSelected(false);
        }
    }

    /**
     * Returns whether a <code>ButtonModel</code> is selected.
     * @return <code>true</code> if the button is selected,
     *   otherwise returns <code>false</code>
     */
    public boolean isSelected(ButtonModel m) {
        return (m == selection);
    }

    /**
     * Returns the number of buttons in the group.
     * @return the button count
     * @since 1.3
     */
    public int getButtonCount() {
        if (buttons == null) {
            return 0;
        } else {
            return buttons.size();
        }
    }

}
