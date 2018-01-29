package adf.csys.view;

import adf.csys.model.ModelPolyLineEntity;
import vw.valgebra.VAlgebra;

import java.awt.*;

/**
 * Created by Admin on 5/17/2016
 */
public class CSysPolyLineEntity extends BasicCSysEntity {

    private ModelPolyLineEntity modelPolyLineEntity;
    private double[][] viewPolyLinePoints;
    private int[][] screenPoints;
    private boolean closed;

    /**
     * @param parentCSysView
     * @param modelPolyLineEntity
     */
    public CSysPolyLineEntity(CSysView parentCSysView, ModelPolyLineEntity modelPolyLineEntity) {
        super(parentCSysView);
        if (modelPolyLineEntity.getPolyLine().size() < 2) {
            throw new RuntimeException("SysPolylineEntity.CSysPolylineEntity: " +
                    "ModelPolyLineEntity has size < 2");
        }

        this.modelPolyLineEntity = modelPolyLineEntity;
        viewPolyLinePoints = new double[modelPolyLineEntity.getPolyLine().size()][];
        for (int i = 0; i < modelPolyLineEntity.getPolyLine().size(); i++) {
            double[] modelPoint = modelPolyLineEntity.getPoint(i);
            double[] viewPoint = new double[3];
            viewPolyLinePoints[i] = viewPoint;
        }
        closed = modelPolyLineEntity.isClosed();
        int n = viewPolyLinePoints.length;
        n = closed ? n : --n;
        screenPoints = new int[n][2];
    }

    //
    //
    //   T r a n s f o r m a t i o n s
    //

    /**
     * Transforms the entity according to the matrix provided
     *
     * @param mat43
     */
    @Override
    public void doTransformation(double[][] mat43) {
        int n = modelPolyLineEntity.getPolyLine().size();
        for (int i = 0; i < n; i++) {
            double[] modelPoint = modelPolyLineEntity.getPoint(i);
            double[] viewPoint = viewPolyLinePoints[i];
            VAlgebra.Mat43XPnt3(viewPoint, mat43, modelPoint);

            viewPoint[0] = (viewPoint[0] * (1 + ((viewPoint[2]) / 600)));
            viewPoint[1] = (viewPoint[1] * (1 + ((viewPoint[2]) / 600)));

            viewPolyLinePoints[i] = viewPoint;
        }
    }

    /**
     * @param mat43
     */
    @Override
    public void doNextTransformation(double[][] mat43) {
        int n = modelPolyLineEntity.getPolyLine().size();
        for (int i = 0; i < n; i++) {
            double[] modelPoint = viewPolyLinePoints[i];
            double[] viewPoint = viewPolyLinePoints[i];
            VAlgebra.Mat43XPnt3(viewPoint, mat43, modelPoint);

            viewPoint[0] = (viewPoint[0] * (1 + ((viewPoint[2]) / 600)));
            viewPoint[1] = (viewPoint[1] * (1 + ((viewPoint[2]) / 600)));

            viewPolyLinePoints[i] = viewPoint;
        }
    }

    @Override
    public void doCSysToScreenTransformation(int[] scr0, double scale) {
        int n = viewPolyLinePoints.length;
        n = closed ? n : --n;
        for (int i = 0; i < n; i++) {
            double[] viewPoint = viewPolyLinePoints[i];

            switch (parentCSys.getProjection()) {

                case CSysView.ZOXProjection:
                    screenPoints[i][0] = scr0[0] + (int) (viewPoint[0] * scale);
                    screenPoints[i][1] = scr0[2] + (int) (-viewPoint[2] * scale);
                    break;

                case CSysView.ZOYProjection:
                    screenPoints[i][0] = scr0[0] + (int) (viewPoint[1] * scale);
                    screenPoints[i][1] = scr0[2] + (int) (-viewPoint[2] * scale);
                    break;

                default:
                    screenPoints[i][0] = scr0[0] + (int) (viewPoint[0] * scale);
                    screenPoints[i][1] = scr0[1] + (int) (-viewPoint[1] * scale);
                    break;
            }
        }
    }

    //
    //   D r a w i n g
    //

//    @Override
//    public void draw(Graphics g, int[] scr0, double scale) {
//        draw(g);
//    }

    @Override
    public void draw(Graphics g) {
        if (parentCSys == null) {
            return;
        }

        int[] currentPoint = screenPoints[0];
        for (int i = 1; i < screenPoints.length; i++) {
            g.drawLine(currentPoint[0], currentPoint[1], screenPoints[i][0], screenPoints[i][1]);
            currentPoint = screenPoints[i];
        }
        g.drawLine(currentPoint[0], currentPoint[1], screenPoints[0][0], screenPoints[0][1]);
    }
}

