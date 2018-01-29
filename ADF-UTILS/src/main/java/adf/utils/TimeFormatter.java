package adf.utils;

import java.util.Date;

/**
 * Created by Admin on 4/15/2017.
 */
public class TimeFormatter {

    public static long getTime() {

        Date curDate = new Date();

        String s = curDate.toLocaleString();
        Date localDate = new Date(s);

        long time1 = localDate.getHours() * 3600000;
        long time2 = localDate.getMinutes() * 60000;
        long time3 = localDate.getSeconds() * 1000;
        long ct = curDate.getTime();
        long time4 = (ct % 1000L);
        long time = time1 + time2 + time3 + time4;
        return time;
    }

    public static String formatTime(long time) {
        int intTime = (int) (time / 1000);
        int hous = intTime / 3600;
        int mins = (intTime / 60) - hous * 60;
        int secs = (intTime - hous * 3600 - mins * 60);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(numberToTwoDigitsString(hous));
        stringBuffer.append(":");
        stringBuffer.append(numberToTwoDigitsString(mins));
        stringBuffer.append(":");
        stringBuffer.append(numberToTwoDigitsString(secs));
        return stringBuffer.toString();
    }

    public static String formatTimeWithMs(long time) {
        int ms = (int) (time % 1000L);
        int intTime = (int) (time / 1000);
        int hous = intTime / 3600;
        int mins = (intTime / 60) - hous * 60;
        int secs = (intTime - hous * 3600 - mins * 60);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(numberToTwoDigitsString(hous));
        stringBuffer.append(":");
        stringBuffer.append(numberToTwoDigitsString(mins));
        stringBuffer.append(":");
        stringBuffer.append(numberToTwoDigitsString(secs));
        stringBuffer.append(".");
        stringBuffer.append(numberToThreeDigitsString(ms));
        return stringBuffer.toString();
    }

    public static String dayTimeWithMs(Date date) {

        Date startOfDay = new Date();
        startOfDay.setHours(0);
        startOfDay.setMinutes(0);
        startOfDay.setSeconds(0);

        long time = date.getTime() - (startOfDay.getTime() / 1000) * 1000;

        int ms = (int) (time % 1000L);
        int intTime = (int) ((time - ms) / 1000);
        int hous = intTime / 3600;
        int mins = (intTime / 60) - hous * 60;
        int secs = (intTime - hous * 3600 - mins * 60);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(numberToTwoDigitsString(hous));
        stringBuffer.append(":");
        stringBuffer.append(numberToTwoDigitsString(mins));
        stringBuffer.append(":");
        stringBuffer.append(numberToTwoDigitsString(secs));
        stringBuffer.append(":");
        stringBuffer.append(numberToThreeDigitsString(ms));
        return stringBuffer.toString();
    }

    private static String numberToTwoDigitsString(int number) {
        byte firstDigit = (byte) (number / 10);
        byte secondDigit = (byte) (number - firstDigit * 10);
        return new String(new byte[]{(byte) (firstDigit + 48), (byte) (secondDigit + 48)});
    }

    private static String numberToThreeDigitsString(int number) {
        byte firstDigit = (byte) (number / 100);
        byte secondDigit = (byte) ((number - firstDigit * 100) / 10);
        byte thirdDigit = (byte) (number - firstDigit * 100 - secondDigit * 10);
        String result = new String(new byte[]{(byte) (firstDigit + 48),
                (byte) (secondDigit + 48),
                (byte) (thirdDigit + 48)});
        return result;
    }
}