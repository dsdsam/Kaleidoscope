package adf.menu;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 3/14/2017.
 */
public class ThirdPartyActionGroup {

    private List actions;
    private boolean notifyLock;
    private PropertyChangeListener selectedListener;

    public ThirdPartyActionGroup() {
        actions = new ArrayList();
        selectedListener = new SelectedListener();
    }

    public void add(Action action) {
        actions.add(action);
        action.addPropertyChangeListener(selectedListener);
    }

    public void remove(Action action) {
        actions.remove(action);
        action.removePropertyChangeListener(selectedListener);
    }
    public List getActions() {
        return new ArrayList(actions);
    }

    private class SelectedListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            // prevent any poorly implemented components from
            // causing us to get stuck in a feedback loop.
            if(notifyLock) return;

            // If it isn't a selected key change, or
            // someone set it to false we just avoid doing anything.
            if(evt.getPropertyName().equals(Action.SELECTED_KEY) && evt.getNewValue().equals(Boolean.TRUE)) {
                try {
                    notifyLock = true;
                    for(int i=0; i<actions.size();i++) {
                        Action action = (Action)actions.get(i);
                        if(!action.equals(evt.getSource())) {
                            action.putValue(Action.SELECTED_KEY, Boolean.FALSE);
                        }
                    }
                }
                finally {
                    notifyLock = false;
                }
            }
        }
    }
}
