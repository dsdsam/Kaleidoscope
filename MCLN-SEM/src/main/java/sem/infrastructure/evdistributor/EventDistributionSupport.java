package sem.infrastructure.evdistributor;

import sun.awt.EventListenerAggregate;

import java.awt.*;

import sem.mission.controlles.modelcontroller.actions.CallbackListener;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 3/6/12
 * Time: 9:28 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EventDistributionSupport {

    // the listener list.
    private static final EventListenerAggregate listeners = new EventListenerAggregate(EventDistributionListener.class);

    /**
     * Hashtable for managing listeners for specific properties.
     * Maps property names to GuiComponentMediator objects.
     */
    private static java.util.Hashtable<SemEventDistributor.EventGroup, EventListenerAggregate> children;

    /**
     * The object to be provided as the "source" for any generated events.
     *
     * @serial
     */
    private static Object source;


    /**
     * Add a EventDistributionListener to the listener list.
     * The listener is registered for all events.
     * The same listener object may be added more than once, and will be called
     * as many times as it is added.
     * If <code>listener</code> is null, no exception is thrown and no action
     * is taken.
     *
     * @param listener The EventDistributionListener to be added
     */
    public static synchronized void addEventDistributionListener(EventDistributionListener listener) {
        if (listener == null) {
            return;
        }
        listeners.add(listener);
    }

    /**
     * Remove a EventDistributionListener from the listener list.
     * This removes a EventDistributionListener that was registered
     * for all events.
     * If <code>listener</code> was added more than once to the same event
     * source, it will be notified one less time after being removed.
     * If <code>listener</code> is null, or was never added, no exception is
     * thrown and no action is taken.
     *
     * @param listener The EventDistributionListener to be removed
     */
    public static synchronized void removeEventDistributionListener(EventDistributionListener listener) {
        if (listener == null) {
            return;
        }

        if (listeners == null) {
            return;
        }
        listeners.remove(listener);

    }


    /**
     * Add a PropertyChangeListener for a specific property.  The listener
     * will be invoked only when a call on distributeEventsToListenere names that
     * specific property.
     * The same listener object may be added more than once.  For each
     * property,  the listener will be invoked the number of times it was added
     * for that property.
     * If <code>propertyName</code> or <code>listener</code> is null, no
     * exception is thrown and no action is taken.
     *
     * @param eventGroup  The name of the property to listen on.
     * @param listener The EventDistributionListener to be added
     */

    public static synchronized void addEventDistributionListener(SemEventDistributor.EventGroup eventGroup,
                                                                 EventDistributionListener listener) {
        if (eventGroup == null || listener == null) {
            return;
        }
        if (children == null) {
            children = new java.util.Hashtable<SemEventDistributor.EventGroup, EventListenerAggregate>();
        }
        EventListenerAggregate child = children.get(eventGroup);
        if (child == null) {
            child = new EventListenerAggregate(EventDistributionListener.class);
            children.put(eventGroup, child);
        }
        child.add(listener);
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     * If <code>listener</code> was added more than once to the same event
     * source for the specified property, it will be notified one less time
     * after being removed.
     * If <code>propertyName</code> is null,  no exception is thrown and no
     * action is taken.
     * If <code>listener</code> is null, or was never added for the specified
     * property, no exception is thrown and no action is taken.
     *
     * @param eventId  The name of the property that was listened on.
     * @param listener The EventDistributionListener to be removed
     */

    public static synchronized void removeEventDistributionListener(String eventId, EventDistributionListener listener) {
        if (eventId == null || listener == null) {
            return;
        }
        if (children == null) {
            return;
        }
        EventListenerAggregate child = children.get(eventId);
        if (child == null) {
            return;
        }
        child.remove(listener);
    }

    /**
     * @param eventId
     */
//    protected static void distributeEventsToListeners(String eventId, Component comp, Object arg) {
//
//        if (children == null || eventId == null || eventId.length() == 0) {
//            return;
//        }
//        EventListenerAggregate child = children.get(eventId);
//        if (child == null) {
//            return;
//        }
//        EventDistributionListener[] groupedListeners = (EventDistributionListener[]) child.getListenersInternal();
//        for (EventDistributionListener target : groupedListeners) {
//            target.controlStateChanged(comp, arg);
//        }
//    }

    protected static void distributeEventsToListeners(SemEventDistributor.EventGroup eventGroup,
                                                      SemEventDistributor.EventId eventId,
                                                      Component comp, Object arg, CallbackListener callbackListener) {

        if (children == null || eventGroup == null || eventGroup.getId().length() == 0) {
            return;
        }
        EventListenerAggregate child = children.get(eventGroup);
        if (child == null) {
            return;
        }
        EventDistributionListener[] groupedListeners = (EventDistributionListener[]) child.getListenersInternal();
        for (EventDistributionListener eventDistributionListener : groupedListeners) {
            eventDistributionListener.onGuiStateModelEventFired(eventId, comp, arg, callbackListener);
        }
    }


    /**
     * Fire an existing PropertyChangeEvent to any registered listeners.
     * No event is fired if the given event's old and new values are
     * equal and non-null.
     *
     * @param evt The PropertyChangeEvent object.
     */
//    protected void distributeEventsToListenere(PropertyChangeEvent evt) {
//        Object oldValue = evt.getOldValue();
//        Object newValue = evt.getNewValue();
//        String propertyName = evt.getPropertyName();
//        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
//            return;
//        }
//
//        if (listeners != null) {
//            Object[] list = listeners.getListenersInternal();
//            for (int i = 0; i < list.length; i++) {
//                PropertyChangeListener target = (PropertyChangeListener) list[i];
//                target.propertyChange(evt);
//            }
//        }
//
//        if (children == null || propertyName == null) {
//            return;
//        }
//        EventListenerAggregate child = children.get(propertyName);
//        if (child == null) {
//            return;
//        }
//        EventDistributionListener[] groupedListeners = (EventDistributionListener[]) child.getListenersInternal();
//        for (EventDistributionListener target : groupedListeners) {
//            target.controlSelected(null);
//        }
//    }

}
