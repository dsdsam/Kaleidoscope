package adf.app;

import adf.flyout.MainFrameFlyingMessageManager;
import adf.utils.TimeFormatter;

import java.awt.*;
import java.awt.event.InvocationEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Admin on 4/15/2017.
 */
public class AdfEventQueue extends EventQueue {

    private final static Logger logger = Logger.getLogger(AdfEventQueue.class.getName());

    private final Map runnables = new HashMap();

    public AdfEventQueue() {
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(this);
    }

    long timeBefore;
    long timeAfter;

    protected void dispatchEvent(AWTEvent event) {
        try {
//            analyzeEventFlow( event );
            System.out.println("dispatchEvent " + event.getSource());
//            analyzeEventFlow( event );
            timeBefore = System.currentTimeMillis();
            long timeBetweenEvents = timeBefore - timeAfter;
//            System.out.println("\nSeEventQueue: Time between events = " + timeBetweenEvents);
            super.dispatchEvent(event);
            timeAfter = System.currentTimeMillis();
            long operationTime = timeAfter - timeBefore;
            long cycleTime = timeBetweenEvents + operationTime;
//            System.out.println("\nAdfEventQueue: Time between events = " + timeBetweenEvents+
//                    "  Operation time = " + operationTime+",  cycle time "+cycleTime);

        } catch (Exception e) {
            MainFrameFlyingMessageManager.showFlyDownAlertMessage("Unhandled exception detected.  See execution log for details.");
            Thread t = Thread.currentThread();
            StringBuffer msg = new StringBuffer();
            msg.append("\n\n-------------------------------------------------------------------------\n");
            msg.append("Uncaught Exception Occurred in Thread \"");
            msg.append(t.getName());
            msg.append("\" of ThreadGroup \"");
            msg.append(t.getThreadGroup().getName());
            msg.append("\"\n");
            msg.append("-------------------------------------------------------------------------\n\n");

            logger.log(Level.SEVERE, msg.toString() + ", event = " + event);
            logger.log(Level.SEVERE, "\n", e);
        }
    }

    long prevTime;
    String strInterval = "";

    private final void analyzeEventFlow(AWTEvent event) {
        System.out.println("start time: " + System.currentTimeMillis() + "  " + event.getID() + "  " + 0x490);
        Object src = event.getSource();

        if (event instanceof InvocationEvent) {
            InvocationEvent ie = (InvocationEvent) event;
//              System.out.println("InvocationEvent: " + ie.getWhen()+"  "+getRunnable(ie.paramString()));
//              System.out.println("InvocationEvent: " + ie.getWhen()+"  "+ie.paramString());
            String key = getRunnable(ie.paramString());
            Integer cnt = (Integer) runnables.get(key);
            if (cnt == null) {
                runnables.put(key, new Integer(1));
            } else {
                int newValue = cnt.intValue() + 1;
                runnables.put(key, new Integer(newValue));
            }
            if (key.indexOf("imer") >= 0) {
                printRunnables();
            }
        }
        long currTime = TimeFormatter.getTime();
        long interval = currTime - prevTime;
        strInterval = TimeFormatter.formatTimeWithMs(interval);
        if (prevTime != 0) {
//              System.out.println("dispatchEvent time: " + System.currentTimeMillis());
//              System.out.println("dispatchEvent time: "+strInterval);
        }
        prevTime = currTime;
//            System.out.println("dispatchEvent time: " + System.currentTimeMillis());
    }

    private final String getRunnable(String ieParamString) {
        String runnableStr = "";
        if (ieParamString == null) {
            return runnableStr;
        }
//       System.out.println("getRunnable: "+ieParamString);
        int ind1 = ieParamString.indexOf("=");
        if (ind1 >= 0) {
            ieParamString = ieParamString.substring(ind1 + 1);
            int ind2 = ieParamString.indexOf("@");
//         System.out.println("getRunnable: "+ieParamString+"  "+ind1+"  "+ind2);
            if (ind2 >= 0) {
                runnableStr = ieParamString.substring(0, ind2);
            }
        }
        return runnableStr;
    }

    private final void printRunnables() {
        Set keySet = runnables.keySet();

        String[] runnablesArr = (String[]) keySet.toArray(new String[0]);
//        System.out.println();
        for (int i = 0; i < runnablesArr.length; i++) {
            int cnt = ((Integer) runnables.get(runnablesArr[i])).intValue();
            if (cnt > 100) {
//                System.out.println(" " + i + "   " + runnablesArr[i] + "   " + cnt);
            }
        }
    }
}