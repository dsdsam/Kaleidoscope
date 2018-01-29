/*
 * Created on Aug 15, 2005
 *
 */
package adf.csys.model;

import javax.swing.*;
import javax.swing.event.EventListenerList;

/**
 * @author xpadmin
 */
public class BasicModel extends Part {

    /**
     * @return Returns the modelEntityList.
     */
//    abstract public ArrayList getModelEntityList(); 

    /**
     * List of listeners
     */
    protected EventListenerList listenerList = new EventListenerList();

    public BasicModel(String name, String nodeId) {
        super(name, nodeId);
//        System.out.println("BasicModel");
    }

    /**
     * @param mat43
     */
//    public void modelToWorldTransform(double[][] mat43) {
//        List modelEntityList = getModelEntityList();
//        for (int i = 0; i < modelEntityList.size(); i++) {
//            ((BasicModelEntity) modelEntityList.get(i)).modelToWorldTransform(mat43);
//        }
//    }

    /**
     *
     * @param translationVector
     */
//    public void modelTranslate(double[] translationVector) {
//        List<BasicModelEntity> modelEntityList = getModelEntityList();
//        for (int i = 0; i < modelEntityList.size(); i++) {
//            modelEntityList.get(i).modelTranslate(translationVector);
//        }
//    }

    /**
     * Adds a listener to the list that is notified each time a change
     * to the mcln.data model occurs.
     *
     * @param l the ModelChangeListener
     */
    public void addModelChangeListener(ModelChangeListener l) {
        listenerList.add(ModelChangeListener.class, l);
    }

    /**
     * Removes a listener from the list that is notified each time a
     * change to the mcln.data model occurs.
     *
     * @param l the ModelChangeListener
     */
    public void removeModelChangeListener(ModelChangeListener l) {
        listenerList.remove(ModelChangeListener.class, l);
    }

    /**
     * Forwards the given notification event to all
     * <code>TableModelListeners</code> that registered
     * themselves as listeners for this table model.
     *
     * @param currentAngle
     * @param mat43
     */
    public void fireModelChanged(final double currentAngle, final double[][] mat43) {
        Runnable runnable = new Runnable() {
            public void run() {
                listenersUpdater.setCurrentAngle(currentAngle);
                listenersUpdater.setMat43(mat43);
//        try{
//            SwingUtilities.invokeLater( listenersUpdater );
//            listenersUpdater.run();
//        }catch( Exception e ){}

                // Guaranteed to return a non-null array
                Object[] listeners = listenerList.getListenerList();
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for (int i = listeners.length - 2; i >= 0; i -= 2) {
                    if (listeners[i] == ModelChangeListener.class) {
                        ((ModelChangeListener) listeners[i + 1]).modelChanged(currentAngle, mat43);
                    }
                }
            }
        };
//        System.out.println("isEventDispatchThread "+SwingUtilities.isEventDispatchThread());
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
//             try {
//                SwingUtilities.invokeAndWait(runnable);
//            } catch (Exception e) {
//            }
        }
    }

    public void fireModelRedefined(final BasicModelEntity basicModelEntity) {
//         System.out.println("fireModelRedefined ");
        Runnable runnable = new Runnable() {
            public void run() {
                // Guaranteed to return a non-null array
                Object[] listeners = listenerList.getListenerList();
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for (int i = listeners.length - 2; i >= 0; i -= 2) {
                    if (listeners[i] == ModelChangeListener.class) {
                        ((ModelChangeListener) listeners[i + 1]).modelRedefined(basicModelEntity);
                    }
                }
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
//            try {
//                SwingUtilities.invokeAndWait(runnable);
//            } catch (Exception e) {
//            }
        }
    }

    private ListenersUpdater listenersUpdater = new ListenersUpdater();

    private class ListenersUpdater implements Runnable {

        private double currentAngle;
        private double[][] mat43;

        /**
         * @param currentAngle The currentAngle to set.
         */
        public void setCurrentAngle(double currentAngle) {
            this.currentAngle = currentAngle;
        }

        /**
         * @param mat43 The mat43 to set.
         */
        public void setMat43(double[][] mat43) {
            this.mat43 = mat43;
        }

        public void run() {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ModelChangeListener.class) {
                    ((ModelChangeListener) listeners[i + 1]).modelChanged(currentAngle, mat43);
                }
            }
        }

    }

}
