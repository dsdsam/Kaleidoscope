package adf.csys.view;

/**
 * Created by Admin on 10/9/2014.
 */
public final class CSysUtils {

    public static final boolean isFlagSet(int options, int flag){
        return (options & flag) == flag;
    }
}
