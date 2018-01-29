package sem.appui.controls;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 12, 2011
 * Time: 9:13:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Roundable {

    public static final int ROUNDING_NONE = 0;

    public static final int ROUNDING_LEFT = 1;

    public static final int ROUNDING_RIGHT = 2;

    public static final int ROUNDING_TOP = 3;

    public static final int ROUNDING_BOTTOM = 4;

    public static final int ROUNDING_ALL = 5;

    public static final int[][][] BORDER_OR04 = {
            {{2, 0}, {2, -1}, {1, -1}, {1, -2}, {0, -2}},
            {{0, 2}, {1, 2}, {1, 1}, {2, 1}, {2, 0}},
            {{-2, 0}, {-2, 1}, {-1, 1}, {-1, 2}, {0, 2}},
            {{0, -2}, {-1, -2}, {-1, -1}, {-2, -1}, {-2, 0}}};

    public static final int[][][] BORDER_IR04 = {
            {{3, -1}, {3, -2}, {2, -2}, {2, -3}, {1, -3}},
            {{1, 3}, {2, 3}, {2, 2}, {3, 2}, {3, 1}},
            {{-3, 1}, {-3, 2}, {-2, 2}, {-2, 3}, {-1, 3}},
            {{-1, -3}, {-2, -3}, {-2, -2}, {-3, -2}, {-3, -1}}};

    public static int[][][] BORDER_OR05 = {
            {{4, 0}, {3, -1}, {2, -1}, {1, -2}, {1, -3}, {0, -4}},
            {{0, 4}, {1, 3}, {1, 2}, {2, 1}, {3, 1}, {4, 0}},
            {{-4, 0}, {-3, 1}, {-2, 1}, {-1, 2}, {-1, 3}, {0, 4}},
            {{0, -4}, {-1, -3}, {-1, -2}, {-2, -1}, {-3, -1},
                    {-4, 0}}};

    public static int[][][] BORDER_IR05 = {
            {{4, -1}, {3, -2}, {2, -2}, {2, -3}, {1, -4}},
            {{1, 4}, {2, 3}, {2, 2}, {3, 2}, {4, 1}},
            {{-4, 1}, {-3, 2}, {-2, 2}, {-2, 3}, {-1, 4}},
            {{-1, -4}, {-2, -3}, {-2, -2}, {-3, -2}, {-4, -1}}};

}
