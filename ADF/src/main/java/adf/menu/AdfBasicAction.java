package adf.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * It is assumed that this class's default functionality will be extended in a subclass
 * <p>
 * Created by Admin on 3/16/2017.
 */
abstract public class AdfBasicAction extends AbstractAction {

    private String menuItemLabel;
    protected AdfMenuActionListener menuActionListener;
    private boolean disabledStateLocked; // is used to lock control - prevents setting it enabled
    private boolean excludedFromGroup;   // is used to temporally exclude from the group

    public AdfBasicAction(String menuItemLabel) {
        super(menuItemLabel);
        this.menuItemLabel = menuItemLabel;
    }

    /**
     * Creating Action without Icon
     *
     * @param menuItemLabel
     * @param menuActionListener
     */
    public AdfBasicAction(String menuItemLabel, AdfMenuActionListener menuActionListener) {
        super(menuItemLabel);
        this.menuItemLabel = menuItemLabel;
        this.menuActionListener = menuActionListener;
    }

    /**
     * Creating Action with Icon
     *
     * @param menuItemLabel
     * @param icon
     */
    public AdfBasicAction(String menuItemLabel, Icon icon, AdfMenuActionListener menuActionListener) {
        super(menuItemLabel, icon);
        this.menuItemLabel = menuItemLabel;
        this.menuActionListener = menuActionListener;
    }

    public boolean isExcludedFromGroup() {
        return excludedFromGroup;
    }

    public void setExcludedFromGroup(boolean excludedFromGroup) {
//        this.excludedFromGroup = excludedFromGroup;
    }


    public void setSelected(Boolean selected) {

    }

    public void setSelected() {

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    /**
     * @param status
     */
    public void setEnabledAnLock(boolean status) {
        setEnabled(status);
        disabledStateLocked = !status;
    }

    /**
     *
     */
    public void setDisabledAndLock() {
        if (excludedFromGroup) {
            return;
        }
//        setEnabled(false);
//        disabledStateLocked = true;
    }

    /**
     *
     */
    public void setEnabledAndUnlock() {
//        disabledStateLocked = false;
//        setEnabled(true);
    }

    /**
     * @return
     */
    public boolean isDisabledStateLocked() {
        return disabledStateLocked;
    }

    /**
     *
     */
    public void setDisabledStateLocked() {
        disabledStateLocked = true;
    }

    /**
     *
     */
    public void setDisabledStateUnlocked() {
        disabledStateLocked = false;
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (excludedFromGroup) {
            return;
        }
        if (changeSupport == null ||
                (oldValue != null && newValue != null && oldValue.equals(newValue))) {
            return;
        }
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
