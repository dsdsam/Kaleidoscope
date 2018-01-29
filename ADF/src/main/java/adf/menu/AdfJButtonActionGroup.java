package adf.menu;

import java.beans.PropertyChangeEvent;

/**
 * Created by Admin on 3/15/2017.
 */
public final class AdfJButtonActionGroup extends AdfAbstractActionGroup {

    protected void processPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(AdfActionGroup.ENABLED_KEY) && evt.getNewValue().equals(Boolean.FALSE)) {
            Object sourceAction = evt.getSource();
            for (AdfBasicAction currentAction : registeredActions) {
                if(currentAction.isExcludedFromGroup()){
                    continue;
                }
                if (!currentAction.equals(sourceAction)) {
                    if(!currentAction.isDisabledStateLocked()) {
                        currentAction.putValue(AdfActionGroup.ENABLED_KEY, Boolean.TRUE);
                    }
                }
            }
        }
    }
}
