package adf.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;

/**
 * Created by Admin on 3/6/2017.
 */
public class AdfToggleButton extends JToggleButton {

    /**
     * Creates an initially unselected toggle button
     * without setting the text or image.
     */
    public AdfToggleButton() {
        this(null, null, false);
    }

    /**
     * Creates an initially unselected toggle button
     * with the specified image but no text.
     *
     * @param icon  the image that the button should display
     */
    public AdfToggleButton(Icon icon) {
        this(null, icon, false);
    }


    /**
     * Creates a toggle button where properties are taken from the
     * Action supplied.
     *
     * @since 1.3
     */
    public AdfToggleButton(Action a) {
        this();
        setAction(a);
    }

    /**
     * Creates a toggle button that has the specified text and image,
     * and that is initially unselected.
     *
     * @param text the string displayed on the button
     * @param icon  the image that the button should display
     */
    public AdfToggleButton(String text, Icon icon) {
        this(text, icon, false);
    }

    /**
     * Creates a toggle button with the specified text, image, and
     * selection state.
     *
     * @param text the text of the toggle button
     * @param icon  the image that the button should display
     * @param selected  if true, the button is initially selected;
     *                  otherwise, the button is initially unselected
     */
    public AdfToggleButton(String text, Icon icon, boolean selected) {
        // Create the model
        setModel(new ToggleButtonModel());

        model.setSelected(selected);

        // initialize
        init(text, icon);
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






    // *********************************************************************

    /**
     * The ToggleButton model
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases. The current serialization support is
     * appropriate for short term storage or RMI between applications running
     * the same version of Swing.  As of 1.4, support for long term storage
     * of all JavaBeans&trade;
     * has been added to the <code>java.beans</code> package.
     * Please see {@link java.beans.XMLEncoder}.
     */
    public static class ToggleButtonModel extends DefaultButtonModel {

        /**
         * Creates a new ToggleButton Model
         */
        public ToggleButtonModel () {
        }

        /**
         * Checks if the button is selected.
         */
        public boolean isSelected() {
//              if(getGroup() != null) {
//                  return getGroup().isSelected(this);
//              } else {
            return (stateMask & SELECTED) != 0;
//              }
        }


        /**
         * Sets the selected state of the button.
         * @param b true selects the toggle button,
         *          false deselects the toggle button.
         */
        public void setSelected(boolean b) {
            ButtonGroup group = getGroup();
            if (group != null) {
                // use the group model instead
                group.setSelected(this, b);
                b = group.isSelected(this);
            }

            // removed VL
//            if (isSelected() == b) {
//                return;
//            }

            if (b) {
                stateMask |= SELECTED;
            } else {
                stateMask &= ~SELECTED;
            }

            // Send ChangeEvent
            fireStateChanged();

            // Send ItemEvent
            fireItemStateChanged(
                    new ItemEvent(this,
                            ItemEvent.ITEM_STATE_CHANGED,
                            this,
                            this.isSelected() ?  ItemEvent.SELECTED : ItemEvent.DESELECTED));

        }

        /**
         * Sets the pressed state of the toggle button.
         */
        public void setPressed(boolean b) {
            if ((isPressed() == b) || !isEnabled()) {
                return;
            }

            if (b == false && isArmed()) {
                setSelected(!this.isSelected());
            }

            if (b) {
                stateMask |= PRESSED;
            } else {
                stateMask &= ~PRESSED;
            }

            fireStateChanged();

            if(!isPressed() && isArmed()) {
                int modifiers = 0;
                AWTEvent currentEvent = EventQueue.getCurrentEvent();
                if (currentEvent instanceof InputEvent) {
                    modifiers = ((InputEvent)currentEvent).getModifiers();
                } else if (currentEvent instanceof ActionEvent) {
                    modifiers = ((ActionEvent)currentEvent).getModifiers();
                }
                fireActionPerformed(
                        new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                getActionCommand(),
                                EventQueue.getMostRecentEventTime(),
                                modifiers));
            }

        }
    }


    /**
     * Returns a string representation of this JToggleButton. This method
     * is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not
     * be <code>null</code>.
     *
     * @return  a string representation of this JToggleButton.
     */
    protected String paramString() {
        return super.paramString();
    }




}
