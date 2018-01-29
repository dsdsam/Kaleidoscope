package adf.menu;

import java.beans.PropertyChangeEvent;

/**
 * Created by Admin on 3/21/2017.
 */
public class AdfEnableDisableAllActionGroup extends AdfAbstractActionGroup {

    protected void processPropertyChange(PropertyChangeEvent evt) {

    }

    public void enableAllActions(boolean status){
        for(AdfBasicAction adfBasicAction : registeredActions){
            adfBasicAction.setEnabled(status);
        }
    }
}
