package adf.csys.view;

/**
 * Created by Admin on 11/3/2016.
 */
public final class DoubleRectangle {

    private static double[] initVec3(double vec[], double x, double y, double z) {
        if (vec == null) {
            vec = new double[3];
        }
        vec[0] = x;
        vec[1] = y;
        vec[2] = z;
        return vec;
    }

    private static double[] copyVec3(double[] toVec, double[] fromVec) {
        if (toVec == null) {
//            new Exception("Result Vector Not Provided").printStackTrace();
            toVec = new double[3];
        }
        toVec[0] = fromVec[0];
        toVec[1] = fromVec[1];
        toVec[2] = fromVec[2];
        return toVec;
    }

    //   I n s t a n c e

    private final double x;
    private final double rightX;
    private final double y;
    private final double lowerY;
    private final double width;
    private final double height;

    private final double[] upperLeftCorner = new double[3];
    private final double[] lowerRightCorner = new double[3];

    /**
     * Copy constructor
     *
     * @param doubleRectangle
     */
    private DoubleRectangle(DoubleRectangle doubleRectangle) {
        this(doubleRectangle.x, doubleRectangle.y, doubleRectangle.width, doubleRectangle.height);
    }

    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public DoubleRectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rightX = x + width;
        lowerY = y - height;
        initVec3(upperLeftCorner, x, y, 0);
        initVec3(lowerRightCorner, rightX, lowerY, 0);
    }

    public DoubleRectangle getCopy() {
        return new DoubleRectangle(this);
    }

    public double[] getUpperLeftCorner() {
        return copyVec3(null, upperLeftCorner);
    }

    public double[] getLowerRightCorner() {
        return copyVec3(null, lowerRightCorner);
    }

    public double getX() {
        return x;
    }

    public double getRightX() {
        return rightX;
    }

    public double getY() {
        return y;
    }

    public double getLowerY() {
        return lowerY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void grow(double h, double v) {
        double x0 = this.x;
        double y0 = this.y;
        double x1 = this.width;
        double y1 = this.height;
        x1 += x0;
        y1 += y0;

        x0 -= h;
        y0 -= v;
        x1 += h;
        y1 += v;

        if (x1 < x0) {
            // Non-existant in X direction
            // Final width must remain negative so subtract x0 before
            // it is clipped so that we avoid the risk that the clipping
            // of x0 will reverse the ordering of x0 and x1.
            x1 -= x0;
            if (x1 < Integer.MIN_VALUE) x1 = Integer.MIN_VALUE;
            if (x0 < Integer.MIN_VALUE) x0 = Integer.MIN_VALUE;
            else if (x0 > Integer.MAX_VALUE) x0 = Integer.MAX_VALUE;
        } else { // (x1 >= x0)
            // Clip x0 before we subtract it from x1 in case the clipping
            // affects the representable area of the rectangle.
            if (x0 < Integer.MIN_VALUE) x0 = Integer.MIN_VALUE;
            else if (x0 > Integer.MAX_VALUE) x0 = Integer.MAX_VALUE;
            x1 -= x0;
            // The only way x1 can be negative now is if we clipped
            // x0 against MIN and x1 is less than MIN - in which case
            // we want to leave the width negative since the result
            // did not intersect the representable area.
            if (x1 < Integer.MIN_VALUE) x1 = Integer.MIN_VALUE;
            else if (x1 > Integer.MAX_VALUE) x1 = Integer.MAX_VALUE;
        }

        if (y1 < y0) {
            // Non-existant in Y direction
            y1 -= y0;
            if (y1 < Integer.MIN_VALUE) y1 = Integer.MIN_VALUE;
            if (y0 < Integer.MIN_VALUE) y0 = Integer.MIN_VALUE;
            else if (y0 > Integer.MAX_VALUE) y0 = Integer.MAX_VALUE;
        } else { // (y1 >= y0)
            if (y0 < Integer.MIN_VALUE) y0 = Integer.MIN_VALUE;
            else if (y0 > Integer.MAX_VALUE) y0 = Integer.MAX_VALUE;
            y1 -= y0;
            if (y1 < Integer.MIN_VALUE) y1 = Integer.MIN_VALUE;
            else if (y1 > Integer.MAX_VALUE) y1 = Integer.MAX_VALUE;
        }

//        reshape((int) x0, (int) y0, (int) x1, (int) y1);
    }


}
