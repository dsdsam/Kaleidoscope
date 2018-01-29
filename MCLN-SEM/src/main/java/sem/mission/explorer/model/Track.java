/*
 * Created on Aug 16, 2005
 *
 */
package sem.mission.explorer.model;

import adf.csys.model.BasicModelEntity;
import adf.csys.model.ModelEntityFactory;
import sem.infrastructure.SemConstants;
import sem.mission.explorer.sensors.TrackFrontTouchSensor;
import sem.mission.explorer.sensors.TrackRearTouchSensor;
import vw.valgebra.VAlgebra;

import java.awt.*;

/**
 * @author xpadmin
 */
public class Track extends BasicSeModelNode implements SemConstants {

    float bodyRadius = 3;
    private final double[][] placementMatrix = new double[4][3];

    private TrackFrontTouchSensor trackFrontTouchSensor;
    private TrackRearTouchSensor trackRearTouchSensor;

    private BasicModelEntity frontLineEntity;
    private BasicModelEntity rearLineEntity;
    String nodeId;

    private Color presentationColor;

    Track(String name, String nodeId, Color presentationColor, double angle, double[] translationVector) {
        super(name, nodeId);
        this.nodeId = nodeId;
        this.presentationColor = presentationColor;
        VAlgebra.initMat43(placementMatrix, angle, translationVector);
        VAlgebra.printMat43("Track Placement", placementMatrix);
        build();
    }

    /**
     *
     *
     */
    public void build() {

//        float trackWidth = bodyRadius * 0.8f;
//        float trackLenght = bodyRadius * 2.6f;
        float trHW = bodyRadius * 0.5f;
        float trHL = bodyRadius * 1.3f;
//        float rackOffset = bodyRadius/2 + trackWidth/2;

        double bottom = 0;
        double top = bottom + 1.5;

        double[] line01Pnt1 = {-trHL, trHW, bottom};
        double[] line01Pnt2 = {trHL, trHW, bottom};

        double[] line02Pnt1 = {trHL, trHW, bottom};
        double[] line02Pnt2 = {trHL, -trHW, bottom};

        double[] line03Pnt1 = {trHL, -trHW, bottom};
        double[] line03Pnt2 = {-trHL, -trHW, bottom};

        double[] line04Pnt1 = {-trHL, -trHW, bottom};
        double[] line04Pnt2 = {-trHL, trHW, bottom};

        VAlgebra.Mat43XPnt3(line01Pnt1, placementMatrix, line01Pnt1);
        VAlgebra.Mat43XPnt3(line01Pnt2, placementMatrix, line01Pnt2);

        VAlgebra.Mat43XPnt3(line02Pnt1, placementMatrix, line02Pnt1);
        VAlgebra.Mat43XPnt3(line02Pnt2, placementMatrix, line02Pnt2);

        VAlgebra.Mat43XPnt3(line03Pnt1, placementMatrix, line03Pnt1);
        VAlgebra.Mat43XPnt3(line03Pnt2, placementMatrix, line03Pnt2);

        VAlgebra.Mat43XPnt3(line04Pnt1, placementMatrix, line04Pnt1);
        VAlgebra.Mat43XPnt3(line04Pnt2, placementMatrix, line04Pnt2);

        BasicModelEntity basicModelEntiry;

        basicModelEntiry = ModelEntityFactory.createLineEntity(line01Pnt1, line01Pnt2, presentationColor);
        addPartEntity(basicModelEntiry);

        frontLineEntity = ModelEntityFactory.createLineEntity(line02Pnt1, line02Pnt2, presentationColor);
        addPartEntity(frontLineEntity);

        basicModelEntiry = ModelEntityFactory.createLineEntity(line03Pnt1, line03Pnt2, presentationColor);
        addPartEntity(basicModelEntiry);

        rearLineEntity = ModelEntityFactory.createLineEntity(line04Pnt1, line04Pnt2, presentationColor);
        addPartEntity(rearLineEntity);

        double[] line11Pnt1 = {-trHL, trHW, top};
        double[] line11Pnt2 = {trHL, trHW, top};

        double[] line12Pnt1 = {trHL, trHW, top};
        double[] line12Pnt2 = {trHL, -trHW, top};

        double[] line13Pnt1 = {trHL, -trHW, top};
        double[] line13Pnt2 = {-trHL, -trHW, top};

        double[] line14Pnt1 = {-trHL, -trHW, top};
        double[] line14Pnt2 = {-trHL, trHW, top};
        /*
        
        double[] line11Pnt1 = { -trHW,   trHL,  top };
        double[] line11Pnt2 = {  trHW,   trHL,  top };
        
        double[] line12Pnt1 = {  trHW,   trHL,  top };
        double[] line12Pnt2 = {  trHW,  -trHL,  top };
        
        double[] line13Pnt1 = {  trHW,  -trHL,  top };
        double[] line13Pnt2 = { -trHW,  -trHL,  top };
        
        double[] line14Pnt1 = { -trHW,  -trHL,  top };
        double[] line14Pnt2 = { -trHW,   trHL,  top };
        */
        VAlgebra.printMat43("Truck Placement", placementMatrix);

        VAlgebra.Mat43XPnt3(line11Pnt1, placementMatrix, line11Pnt1);
        VAlgebra.Mat43XPnt3(line11Pnt2, placementMatrix, line11Pnt2);

        VAlgebra.Mat43XPnt3(line12Pnt1, placementMatrix, line12Pnt1);
        VAlgebra.Mat43XPnt3(line12Pnt2, placementMatrix, line12Pnt2);

        VAlgebra.Mat43XPnt3(line13Pnt1, placementMatrix, line13Pnt1);
        VAlgebra.Mat43XPnt3(line13Pnt2, placementMatrix, line13Pnt2);

        VAlgebra.Mat43XPnt3(line14Pnt1, placementMatrix, line14Pnt1);
        VAlgebra.Mat43XPnt3(line14Pnt2, placementMatrix, line14Pnt2);

        basicModelEntiry = ModelEntityFactory.createLineEntity(line11Pnt1, line11Pnt2, presentationColor);
        addPartEntity(basicModelEntiry);

        basicModelEntiry = ModelEntityFactory.createLineEntity(line12Pnt1, line12Pnt2, presentationColor);
        addPartEntity(basicModelEntiry);

        basicModelEntiry = ModelEntityFactory.createLineEntity(line13Pnt1, line13Pnt2, presentationColor);
        addPartEntity(basicModelEntiry);

        basicModelEntiry = ModelEntityFactory.createLineEntity(line14Pnt1, line14Pnt2, presentationColor);
        addPartEntity(basicModelEntiry);


        basicModelEntiry = ModelEntityFactory.createLineEntity(line01Pnt1, line11Pnt1, presentationColor);
        addPartEntity(basicModelEntiry);

        basicModelEntiry = ModelEntityFactory.createLineEntity(line02Pnt1, line12Pnt1, presentationColor);
        addPartEntity(basicModelEntiry);

        basicModelEntiry = ModelEntityFactory.createLineEntity(line03Pnt1, line13Pnt1, presentationColor);
        addPartEntity(basicModelEntiry);

        basicModelEntiry = ModelEntityFactory.createLineEntity(line04Pnt1, line14Pnt1, presentationColor);
        addPartEntity(basicModelEntiry);

        /*
          outline = new RtsCSysFRect( 0, trackLenght,
                                      trackWidth, 0 );
        */
        /*
          outline = new RtsCSysFRect( -trHW, trHL,
                                      trHW,  -trHL );
        */
/*
        outline = new RtsCSysFRect( -trHL, trHW,
                                      trHL,  -trHW );

        RtsMBoolParam ltMotorState;
        ltMotorState = new RtsMBoolParam( rtsMedia, "MS001",
                                          "Motor State", false );
        insertObj( this, ltMotorState );
  */
        /*
        
        double[] line01Pnt1 = { -2,  4,  2 };
        double[] line01Pnt2 = {  2,  4,  2 };
        
        double[] line02Pnt1 = {  2,  4,  2 };
        double[] line02Pnt2 = {  2, -4,  2 };

        double[] line03Pnt1 = {  2, -4,  2 };
        double[] line03Pnt2 = { -2, -4,  2 };

        double[] line04Pnt1 = { -2, -4,  2 };
        double[] line04Pnt2 = { -2,  4,  2 };
       
               
        basicModelEntiry = ModelEntityFactory.createLineEntity( line01Pnt1, line01Pnt2 );
        addModelEntity( basicModelEntiry );
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line02Pnt1, line02Pnt2 );
        addModelEntity( basicModelEntiry );
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line03Pnt1, line03Pnt2 );
        addModelEntity( basicModelEntiry );
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line04Pnt1, line04Pnt2 );
        addModelEntity( basicModelEntiry );
         
        
        double[] line11Pnt1 = { -2,  4,  2 };
        double[] line11Pnt2 = { -2,  4,  0 };
        
        double[] line12Pnt1 = {  2,  4,  2 };
        double[] line12Pnt2 = {  2,  4,  0 };
        
        double[] line13Pnt1 = {  2, -4,  2 };
        double[] line13Pnt2 = {  2, -4,  0 };
        
        double[] line14Pnt1 = { -2, -4,  2 };
        double[] line14Pnt2 = { -2, -4,  0 };
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line11Pnt1, line11Pnt2 );
        addModelEntity( basicModelEntiry );
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line12Pnt1, line12Pnt2 );
        addModelEntity( basicModelEntiry );
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line13Pnt1, line13Pnt2 );
        addModelEntity( basicModelEntiry );
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line14Pnt1, line14Pnt2 );
        addModelEntity( basicModelEntiry );
        
        double[] line21Pnt1 = { -2,  4,  0 };
        double[] line21Pnt2 = {  2,  4,  0 };
        
        double[] line22Pnt1 = {  2,  4,  0 };
        double[] line22Pnt2 = {  2, -4,  0 };

        double[] line23Pnt1 = {  2, -4,  0 };
        double[] line23Pnt2 = { -2, -4,  0 };

        double[] line24Pnt1 = { -2, -4,  0 };
        double[] line24Pnt2 = { -2,  4,  0 };
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line21Pnt1, line21Pnt2 );
        addModelEntity( basicModelEntiry );
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line22Pnt1, line22Pnt2 );
        addModelEntity( basicModelEntiry );
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line23Pnt1, line23Pnt2 );
        addModelEntity( basicModelEntiry );
        
        basicModelEntiry = ModelEntityFactory.createLineEntity( line24Pnt1, line24Pnt2 );
        addModelEntity( basicModelEntiry );
        
        */
/*


        double[] line12Pnt1 = { -1, -2, -3 };
        double[] line12Pnt2 = {  1,  2,  3 };

        double[] line13Pnt1 = { -1, -2, -3 };
        double[] line13Pnt2 = {  1,  2,  3 };

        double[] line14Pnt1 = { -1, -2, -3 };
        double[] line14Pnt2 = {  1,  2,  3 };

        double[] line21Pnt1 = { -1, -2, -3 };
        double[] line21Pnt2 = {  1,  2,  3 };

        double[] line22Pnt1 = { -1, -2, -3 };
        double[] line22Pnt2 = {  1,  2,  3 };

        double[] line23Pnt1 = { -1, -2, -3 };
        double[] line23Pnt2 = {  1,  2,  3 };

        double[] line24Pnt1 = { -1, -2, -3 };
        double[] line24Pnt2 = {  1,  2,  3 };
*/

    }

    /**
     * @return the trackFrontTouchSensor
     */
    public TrackFrontTouchSensor getFrontTouchSensor() {
        return trackFrontTouchSensor;
    }

    /**
     * @param trackFrontTouchSensor the trackFrontTouchSensor to set
     */
    public void setFrontTouchSensor(TrackFrontTouchSensor trackFrontTouchSensor) {
        this.trackFrontTouchSensor = trackFrontTouchSensor;
    }

    public void setRearTouchSensor(TrackRearTouchSensor trackRearTouchSensor) {
        this.trackRearTouchSensor = trackRearTouchSensor;

    }

    /**
     *
     * @return
     */
    public boolean isLeftTrack() {
        return nodeId.equalsIgnoreCase(LT00);
    }

    public boolean isRightTrack() {
        return nodeId.equalsIgnoreCase(RT00);
    }

    public BasicModelEntity getFrontLineEntity() {
        return frontLineEntity;
    }

    public BasicModelEntity getRearLineEntity() {
        return rearLineEntity;
    }
}
