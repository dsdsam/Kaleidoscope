package vw;


/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 31, 2011
 * Time: 9:58:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class VectorPoint  {
    private static final double X = 0;
    private static final double Y = 0;
    private static final double Z = 0;

    private final double[] point;

    public VectorPoint() {
        point = new double[]{X, Y, Z};
    }

    public VectorPoint(double x, double y) {
        this(x, y, Z);
    }

    public VectorPoint(double[] point) {
        this(point[0], point[1], point[2]);
    }

    public VectorPoint(double x, double y, double z) {
        point = new double[]{x, y, z};
    }

    public boolean isInitialized() {
        return point[0] != X && point[1] != Y && point[2] != Z;
    }

    public double getX() {
        return point[0];
    }

    public double getY() {
        return point[1];
    }

    public double[] getVector() {
        return point;
    }

    public String toString() {
        return "X = " + point[0] + ", Y = " + point[1] + ", Z = " + point[2];
    }
}
