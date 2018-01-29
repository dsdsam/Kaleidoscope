package adf.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Admin on 3/16/2017.
 */
public final class AdfJButtonAction extends AdfBasicAction {

    private String menuItemLabel;


    public AdfJButtonAction(String menuItemLabel) {
        super(menuItemLabel);
        this.menuItemLabel = menuItemLabel;
    }

    /**
     * Creating Action without Icon
     *
     * @param menuItemLabel
     * @param menuActionListener
     */
    public AdfJButtonAction(String menuItemLabel, AdfMenuActionListener menuActionListener) {
        super(menuItemLabel, menuActionListener);
        this.menuItemLabel = menuItemLabel;
    }

    /**
     * Creating Action with Icon
     *
     * @param menuItemLabel
     * @param icon
     */
    public AdfJButtonAction(String menuItemLabel, Icon icon, AdfMenuActionListener menuActionListener) {
        super(menuItemLabel, icon, menuActionListener);
        this.menuItemLabel = menuItemLabel;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (menuActionListener == null) {
            // menuActionListener is null fro simulation buttons
            return;
        }

        setEnabled(false); // menu should be disabled when action execution started
        boolean actionExecuted = menuActionListener.executeMenuOperation(ae);
        if (actionExecuted) {
            setEnabled(true);
        }
    }

    @Override
    public void setEnabled(boolean newValue) {
        String a  = menuItemLabel;
        super.setEnabled(newValue);
    }
}
