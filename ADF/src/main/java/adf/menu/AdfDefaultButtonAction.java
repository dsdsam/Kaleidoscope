package adf.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Admin on 3/16/2017.
 */
public class AdfDefaultButtonAction extends AdfBasicAction {

    public AdfDefaultButtonAction(String menuItemLabel) {
        super(menuItemLabel);
    }

    /**
     * Creating Action without Icon
     *
     * @param menuItemLabel
     * @param menuActionListener
     */
    public AdfDefaultButtonAction(String menuItemLabel, AdfMenuActionListener menuActionListener) {
        super(menuItemLabel, menuActionListener);
    }

    /**
     * Creating Action with Icon
     *
     * @param menuItemLabel
     * @param icon
     */
    public AdfDefaultButtonAction(String menuItemLabel, Icon icon, AdfMenuActionListener menuActionListener) {
        super(menuItemLabel, icon, menuActionListener);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (menuActionListener == null) {
            return;
        }
        boolean actionExecuted = menuActionListener.executeMenuOperation(ae);
    }

}
