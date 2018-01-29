package adf.menu;

import java.beans.PropertyChangeEvent;

/**
 * Created by Admin on 3/19/2017.
 */
public class AdfToggleButtonActionGroup extends AdfAbstractActionGroup{

    protected void processPropertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(AdfActionGroup.SELECTED_KEY) && evt.getNewValue().equals(Boolean.TRUE)) {
            Object sourceAction = evt.getSource();
            for (AdfBasicAction currentAction : registeredActions) {
                if(currentAction.isExcludedFromGroup()){
                    continue;
                }
                if (!currentAction.equals(sourceAction)) {
//                    if(!currentAction.isDisabledStateLocked()) {
                        currentAction.putValue(AdfActionGroup.SELECTED_KEY, Boolean.FALSE);
//                    }
                }
            }
        }

//        if (evt.getPropertyName().equals(AdfActionGroup.ENABLED_KEY) && evt.getNewValue().equals(Boolean.FALSE)) {
//            Object sourceAction = evt.getSource();
//            for (AdfBasicAction currentAction : registeredActions) {
//                if(currentAction.isExcludedFromGroup()){
//                    continue;
//                }
//                if (!currentAction.equals(sourceAction)) {
//                    if(!currentAction.isDisabledStateLocked()) {
//                        currentAction.putValue(AdfActionGroup.ENABLED_KEY, Boolean.TRUE);
//                    }
//                }
//            }
//        }
    }

    public void disableAllActions(){
        for(AdfBasicAction adfBasicAction : registeredActions){
            adfBasicAction.setEnabled(false);
        }
    }
}
