package adf.ui.controls.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;

/**
 * Created by Admin on 10/22/2016.
 */
public class AdfTestToggleButton extends JToggleButton {

    public AdfTestToggleButton() {

    }

    public AdfTestToggleButton(Icon icon, boolean selected) {
        this(null, icon, selected);
    }

    public AdfTestToggleButton(String text, Icon icon, boolean selected) {
        // Create the model
        setModel(new ToggleButtonModel());

        model.setSelected(selected);

        // initialize
        init(text, icon);
    }

//    @Override
//    public void setSelected(boolean b) {
//        System.out.println();
//        super.setSelected(b);
//    }

    public void resetState( ) {
        super.setSelected(false);
    }

//    @Override
//    public void setSelected(boolean b) {
//        if (!isSelected()) {
//            super.setSelected(b);
//        }
//    }


    public void setEnabled(boolean status) {
        super.setEnabled(status);
//        setSelected(status);
//        super.setSelected(!b);
//        if (!b && model.isRollover()) {
//            model.setRollover(false);
//        }
//        super.setEnabled(b);
//        model.setEnabled(b);
    }

    String IDText;

    @Override
    public void setText(String text) {
        IDText = text;
        super.setText(text);
    }


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
            boolean status  = (stateMask & SELECTED) != 0;
            return status;
        }


        /**
         * Sets the selected state of the button.
         * @param b true selects the toggle button,
         *          false deselects the toggle button.
         */
        public void setSelected(boolean b) {
            ButtonGroup group = getGroup();
            boolean selectedInGroup = b;
            if (group != null) {
                // use the group model instead
                group.setSelected(this, b);
                selectedInGroup = group.isSelected(this);
            }

            boolean nowSelected = isSelected();
//            if ( nowSelected == selectedInGroup) {
//              return;
//            }
            if ( nowSelected == false && nowSelected == selectedInGroup) {
                return;
            }

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
         * called from BasicButtonListener
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

}
