/**
 * 
 */
package sem.app;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 *
 */
public class FullThreadDumper {
    
//    private FullThreadDumper fullThreadDumper = new FullThreadDumper();
    
    public static void dumpAllThreadStack(){
	ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
	System.out.println("\n\n");
	System.out.println("***  Full Thread Dump  ***");
	System.out.println("**************************");
	 Map<Thread,StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
	 Set<Thread> keySet = allStackTraces.keySet();
	 for(Iterator<Thread> i = keySet.iterator(); i.hasNext();){
	     Thread keyThread = i.next();
	     StackTraceElement[] stackTraceElements = allStackTraces.get(keyThread);
	     if(stackTraceElements.length == 0){
		 continue;
	     }
	     System.out.println("\n--- Tread: "+keyThread.getName()+"  ---");
	     for(StackTraceElement stackTraceElement : stackTraceElements){
		 System.out.println(""+stackTraceElement.toString());
	     }
	 }
	 System.out.println("**************************");
    }
    
    public static void main(String[] args){
	
	 
	FullThreadDumper.dumpAllThreadStack();
	System.out.println("Done");
    }

}
