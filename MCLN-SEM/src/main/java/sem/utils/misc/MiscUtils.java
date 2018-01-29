package sem.utils.misc;


/**
* Created by IntelliJ IDEA.
* User: vlakin
* Date: 2/27/12
* Time: 2:05 PM
* To change this template use File | Settings | File Templates.
*/

public class MiscUtils {

    public static String formatTime(long time, boolean showMs) {

        int ms = (int) (time % 1000L);
        time = (time - ms) / 1000L;
        int secs = (int) (time % 60L);
        time = (time - secs) / 60L;
        int mins = (int) (time % 60L);
        time = (time - mins) / 60L;
        int hours = (int) (time % 24L);
        int usHours = hours - 5;
        time = (time - hours) / 24L;
        StringBuffer stringBuffer = new StringBuffer();

        if (showMs) {
            stringBuffer.append(String.format("Time: %02d:%02d:%02d:%03d", usHours, mins, secs, ms));
        } else {
            stringBuffer.append(String.format("Time: %02d:%02d:%02d ", usHours, mins, secs));
        }

        return stringBuffer.toString();
    }



    public static void main(String[] args) {
        System.out.println(formatTime(System.currentTimeMillis(), true));
    }

}
