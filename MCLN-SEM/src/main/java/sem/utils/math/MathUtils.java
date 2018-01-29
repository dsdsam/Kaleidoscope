package sem.utils.math;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 15, 2011
 * Time: 6:58:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class MathUtils {

    public static long doubleToLongWith2Dps(double doubleValue){
        return (long)(doubleValue*100.00);
    }

    public static double longToDoubleWith2Dps(long longValue){
        return ((double)longValue)/100.00;
    }
}
