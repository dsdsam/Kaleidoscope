package adf.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Admin on 1/15/2017.
 */
public class AdfToggleButtonAction extends AdfBasicAction {


    public AdfToggleButtonAction(String menuItemLabel) {
        super(menuItemLabel);
    }

    /**
     * Creating Action without Icon
     *
     * @param menuItemLabel
     * @param menuActionListener
     */
    public AdfToggleButtonAction(String menuItemLabel, AdfMenuActionListener menuActionListener) {
        super(menuItemLabel, menuActionListener);
    }

    /**
     * Creating Action with Icon
     *
     * @param menuItemLabel
     * @param icon
     */
    public AdfToggleButtonAction(String menuItemLabel, Icon icon, AdfMenuActionListener menuActionListener) {
        super(menuItemLabel, icon, menuActionListener);
    }

    public void setSelected(Boolean selected) {
        putValue(Action.SELECTED_KEY, selected);
    }

    public void setSelected() {
        putValue(Action.SELECTED_KEY, Boolean.TRUE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (menuActionListener == null) {
            // menuActionListener is null fro simulation buttons
            return;
        }

//        setEnabled(false); // menu should be disabled when action execution started
        boolean actionExecuted = menuActionListener.executeMenuOperation(ae);
//        if (actionExecuted) {
//            setEnabled(true);
//        }
    }

    @Override
    public void setEnabled(boolean newValue) {
        super.setEnabled(newValue);
    }
}
