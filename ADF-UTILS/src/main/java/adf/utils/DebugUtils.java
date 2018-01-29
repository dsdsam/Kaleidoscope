package adf.utils;

/**
 * Created by Admin on 12/6/2017.
 */
public class DebugUtils {

    //   P r i n t i n g

    private String printAttayToString(Object[] array) {
        StringBuilder sb = new StringBuilder().append("{");
        if (array ==null) {
            sb.append("}");
        } else {
            for (int i = 0 ; i < array.length-1; i++) {
                sb.append(array[i]).append(",");
            }
            sb.append(array[array.length-1]).append("}");
        }
        return sb.toString();
    }
}
