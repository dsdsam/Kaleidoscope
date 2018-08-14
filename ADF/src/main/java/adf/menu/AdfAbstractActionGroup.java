package adf.menu;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The Action Group maintains a list of the actions it groups.
 * It is a Property Change Listener of each action in the group.
 * <p>
 * Created by Admin on 3/16/2017.
 */
abstract public class AdfAbstractActionGroup implements AdfActionGroup {

    protected List<AdfBasicAction> registeredActions = new ArrayList();

    private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            processPropertyChange(evt);
        }
    };


    public void registerAction(AdfBasicAction action) {
        registeredActions.add(action);
        action.addPropertyChangeListener(propertyChangeListener);
    }


    public void unregisterAction(AdfBasicAction action) {
        registeredActions.remove(action);
        action.removePropertyChangeListener(propertyChangeListener);
    }

    //
    // response on Menu Item or Button state change
    //

    abstract protected void processPropertyChange(PropertyChangeEvent evt);
}

