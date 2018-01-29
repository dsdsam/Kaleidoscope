package sem.app;

public class SemAppStateModel {

    private static boolean powerON;

    public static boolean isPowerON() {
        return powerON;
    }

    public static void setPowerON(boolean powerON) {
        SemAppStateModel.powerON = powerON;
    }
}
