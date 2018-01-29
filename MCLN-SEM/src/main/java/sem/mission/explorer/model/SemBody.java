/*
 * Created on Aug 6, 2005
 *
 */
package sem.mission.explorer.model;

import adf.csys.model.BasicModelEntity;
import adf.csys.model.ModelEntityFactory;
import adf.csys.model.ModelPolyLineEntity;
import sem.infrastructure.SemConstants;

import java.awt.*;

import vw.valgebra.VAlgebra;

/**
 * @author xpadmin
 */
public class SemBody extends BasicSeModelNode implements SemConstants {

    private double[] directionBasePoint = new double[]{0, 0, 0};
    private double[] directionTipPoint = new double[]{1, 0, 0};
    private ModelPosition modelPosition;
    private Antena antena;
    private Shoulder leftShoulder;
    private Shoulder rightShoulder;
    private Elbow leftElbow;
    private Elbow rightElbow;
    private Greep leftGreep;
    private Greep rightGreep;


    private Color presentationColor;

    SemBody(String name, String nodeId, Color presentationColor) {
        super(name, nodeId);
        this.presentationColor = presentationColor;
        build();
    }

    /**
     * 
     *
     */
    public void build() {

        modelPosition = new ModelPosition(directionBasePoint, directionTipPoint, Color.RED);
        modelPosition.setVisible(true);
        addPartEntity(modelPosition);

        BasicModelEntity basicModelEntiry;
        ModelPolyLineEntity bodyBottomPolylineEntity = new ModelPolyLineEntity(presentationColor, true);
        ModelPolyLineEntity bodyTopPolylineEntity = new ModelPolyLineEntity(presentationColor, true);

//        double[][] points5_1 = new double[5][];
//        double[][] points5_2 = new double[5][];
//        double[][] points6_1 = new double[6][];

        double angle = -180;
        double r1 = 4;
        double r2 = r1 - 0.7;
        double z1 = 5;
        double z2 = z1 + 2;
        double[] currPnt = new double[3];

        for (int i = 0; i < 5; i++) {
            currPnt = VAlgebra.createPointOnCircle(r1, angle, z1);
            bodyBottomPolylineEntity.addPoint(currPnt);

            if (i == 2 || i == 3) {
                currPnt = VAlgebra.createPointOnCircle(r1, angle, z2);
            } else {
                currPnt = VAlgebra.createPointOnCircle(r2, angle, z2);
            }
            bodyTopPolylineEntity.addPoint(currPnt);

            basicModelEntiry = ModelEntityFactory.createLineEntity(
                    bodyBottomPolylineEntity.getPoint(i),
                    bodyTopPolylineEntity.getPoint(i), presentationColor);
            addPartEntity(basicModelEntiry);

            angle += 72d;
        }

        addPartEntity(bodyBottomPolylineEntity);
        addPartEntity(bodyTopPolylineEntity);


        antena = new Antena("Antena", AN00);
        this.addModelTreeNode(antena);

        leftShoulder = new Shoulder("L-Shoulder", LS00);
        rightShoulder = new Shoulder("R-Shoulder", RS00);
        this.addModelTreeNode(leftShoulder);
        this.addModelTreeNode(rightShoulder);

        leftElbow = new Elbow("Elbow", LE00);
        rightElbow = new Elbow("Elbow", RE00);
        leftShoulder.addModelTreeNode(leftElbow);
        rightShoulder.addModelTreeNode(rightElbow);

        leftGreep = new Greep("Greep", LG00);
        rightGreep = new Greep("Greep", RG00);
        leftElbow.addModelTreeNode(leftGreep);
        rightElbow.addModelTreeNode(rightGreep);
    }

    /**
     * @return current location
     */
    public double[] getCurrentDirectionVector() {
        return modelPosition.getCurrentDirectionVector();
    }

    public double[] getCurrentLocationVector() {
        return modelPosition.getCurrentLocationVector();
    }
}
