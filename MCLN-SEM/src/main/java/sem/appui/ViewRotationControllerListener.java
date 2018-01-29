package sem.appui;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 17, 2011
 * Time: 3:08:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ViewRotationControllerListener  extends EventListener {
    public boolean onRotateVertilally(int targetValue, int increment);
    public boolean onRotateHorizontally(int targetValue, int increment);
}
