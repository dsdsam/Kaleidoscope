/**
 * 
 */
package sem.infrastructure;

import javax.swing.event.EventListenerList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Administrator
 */
public class TimeTicker extends Timer {

    private int timeoutDelay = 1000;

    private TimerTask timeoutTask = new TimerTask() {

        public void run() {
            fireTick();
        }
    };

    private static TimeTicker timeTicker = new TimeTicker();

    public static TimeTicker getInstance() {
        return timeTicker;
    }

    private TimeTicker() {
        super(true);
        scheduleAtFixedRate(timeoutTask, timeoutDelay, timeoutDelay);
    }

    /**
     * List of model chaned listeners
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * Adds a listener to the list that's notified each tick
     *
     * @param tickListener of the TickListener
     */
    public synchronized void addTickListener(TickListener tickListener) {
        listenerList.add(TickListener.class, tickListener);
    }

    /**
     * Removes a listener from the list that's notified each tick
     *
     * @param tickListener of the TickListener
     */
    public synchronized void removeTickListener(TickListener tickListener) {
        listenerList.remove(TickListener.class, tickListener);
    }

    /**
     *
     *
     */
    private synchronized void fireTick() {
        System.out.println("tick");
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TickListener.class) {
                ((TickListener) listeners[i + 1]).onTick();
            }
        }
    }

}
