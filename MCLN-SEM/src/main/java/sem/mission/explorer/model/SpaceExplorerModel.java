package sem.mission.explorer.model;

import adf.csys.model.BasicModel;
import adf.csys.model.BasicModelEntity;
import adf.csys.model.ModelTreeNode;
import sem.infrastructure.SemConstants;
import sem.mission.controlles.modelcontroller.actions.ServiceExecutionEvent;
import sem.mission.controlles.modelcontroller.interfaces.BasicOperationDoneListener;
import sem.mission.controlles.modelcontroller.interfaces.ModelSensor;
import sem.mission.explorer.sensors.FrontTouchSensor;
import sem.mission.explorer.sensors.RearTouchSensor;
import sem.mission.explorer.sensors.TargetDirectionSensor;
import sem.utils.math.MathUtils;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author xpadmin
 */
public class SpaceExplorerModel extends BasicModel implements SemConstants {


    private static SpaceExplorerModel spaceExplorerModel = new SpaceExplorerModel();

    static {
        spaceExplorerModel.buildModel();
    }

    public static SpaceExplorerModel getInstance() {
        return spaceExplorerModel;
    }

    private List<ModelSensor> sensors = new ArrayList();

    private TargetDirectionSensor targetDirectionSensor;

    private Surface surface;
    private SeEngine seEngine;
    private SemBody seBody;
    private Track leftTrack;
    private Track rightTrack;

    private long longDirectionalAngle;
    private double directionalAngle;
    private final double[][] curTopToWrd = new double[4][3];
    private final double[][] trlMat = new double[4][3];

    private List<BasicModelEntity> worldEntityList;
    private BasicModelEntity[] worldEntityArray;
    private BasicModelEntity[] vehicleEntityArray;

    // initial state
    private final double INITIAL_X = 0;
    private final double INITIAL_Y = 0;
    private final double INITIAL_ANGLE = 0;

    private Color presentationColor = Color.RED;

    private long curX = 0;
    private long curY = 0;

    private BasicOperationDoneListener operationDoneListener = new BasicOperationDoneListener() {
        /**
         * Called on server response, when result is successful. Should be
         * overridden by extending class.
         *
         * @param e
         */
        void success(ServiceExecutionEvent e) {
        }

        /**
         * Called on server response, when result is failure. Should be overridden
         * by extending class.
         *
         * @param e
         */
        void failure(ServiceExecutionEvent e) {

        }

        /**
         * Called on server response, when timeout expired before response received.
         * Should be overridden by extending class.
         *
         * @param e
         */
        void timeout(ServiceExecutionEvent e) {
        }

    };

    // =========    C o n s t r u c t o r s    ====================

    private SpaceExplorerModel() {
        super("Space Explorer", SE01);
    }

    /**
     *
     *
     */
    private void buildModel() {

        double[] ltVec = {0, 3, 0};
        double[] rtVec = {0, -3, 0};
        double[] bdVec = {0, 0, 0};

        surface = new Surface(SemConstants.MODEL_OBJECT_WORLD, WD00, -50, -30, 100, 60);
        seBody = new SemBody("Control Unit", BD00, presentationColor);
        seEngine = new SeEngine("Engine", EN00);
        leftTrack = new Track("L-Track", LT00, presentationColor, 0, ltVec);
        rightTrack = new Track("R-Track", RT00, presentationColor, 0, rtVec);

        this.addModelTreeNode(seBody);
        this.addModelTreeNode(seEngine);
        this.addModelTreeNode(leftTrack);
        this.addModelTreeNode(rightTrack);
        this.printTree("  ");

        rightTrack.getFullName();

        setInitialState(INITIAL_X, INITIAL_Y, INITIAL_ANGLE);

        FrontTouchSensor frontTouchSensor = new FrontTouchSensor(leftTrack, rightTrack, surface);
        sensors.add(frontTouchSensor);
        RearTouchSensor rearTouchSensor = new RearTouchSensor(leftTrack, rightTrack, surface);
        sensors.add(rearTouchSensor);

        targetDirectionSensor = new TargetDirectionSensor(spaceExplorerModel, seBody);
        sensors.add(targetDirectionSensor);


        ModelTreeNode modelTreeNode = this.findNode(
                SE01 + PATH_DELIMITER +
                        BD00 + PATH_DELIMITER +
                        LS00 + PATH_DELIMITER +
                        LE00 + PATH_DELIMITER +
                        LG00);

        System.out.println("point 02");
//        System.out.println(" found " + modelTreeNode.getName());
//          if (robotEntityList == null) {
        addAssemblyEntityList(leftTrack.getPartEntityList());
        addAssemblyEntityList(rightTrack.getPartEntityList());
        addAssemblyEntityList(seBody.getPartEntityList());
        this.assemblyEntityListToArray();
        vehicleEntityArray = this.getAssemblyEntityArray();

//        robotEntityList = new ArrayList<BasicModelEntity>();
//        robotEntityList.addAll(leftTrack.getPartEntityList());
//        robotEntityList.addAll(rightTrack.getPartEntityList());
//        robotEntityList.addAll(seBody.getPartEntityList());
//        vehicleEntityArray = robotEntityList.toArray(new BasicModelEntity[robotEntityList.size()]);

        worldEntityList = new ArrayList<BasicModelEntity>();
        worldEntityList.addAll(surface.getPartEntityList());
        worldEntityArray = worldEntityList.toArray(new BasicModelEntity[worldEntityList.size()]);
    }

    /**
     * @param x
     * @param y
     * @param angle
     */
    public void setInitialState(double x, double y, double angle) {
        currentLocation[0] = x;
        currentLocation[1] = y;
        currentLocation[2] = 0;
        directionalAngle = setDirectioAngle(0);
        VAlgebra.initMat43(curTopToWrd, directionalAngle, currentLocation);

    }

//    @Override
//    protected void addModelEntity(BasicModelEntity basicModelEntiry) {
//        robotEntityList.add(basicModelEntiry);
//    }

//
//    public BasicModelEntity getModelEntity(int index) {
//        return robotEntityList.get(index);
//    }

    /**
     * @return Returns the worldEntityList.
     */
    public List<BasicModelEntity> getWorldEntityList() {
        return worldEntityList;
    }

    public BasicModelEntity[] getWorldEntityArray() {
        return worldEntityArray;
    }

//    /**
//     * @return Returns the robotEntityList.
//     */
//    public BasicModelEntity[] getVehicleEntityArray() {
//        return vehicleEntityArray;
//    }

    // ====================    O P E R A T I O N S    =================

    public void resetModel() {
        setInitialState(INITIAL_X, INITIAL_Y, INITIAL_ANGLE);
        executeMotion(curTopToWrd, directionalAngle, currentLocation);
    }

    /**
     * @param increment
     */
    private double updateDirectionAngle(int increment) {
        long directionAngle = longDirectionalAngle + increment;
        return setDirectioAngle(directionAngle);
//        if (longDirectionalAngle >= 36000) {
//            longDirectionalAngle -= 36000;
//        } else if (longDirectionalAngle < 0) {
//            longDirectionalAngle = 36000 + longDirectionalAngle;
//        }
//        double directionalAngle = Utils.longToDoubleWith2Dps(longDirectionalAngle);
//        double rad = (Math.PI * directionalAngle) / 180d;
//        cos = Math.cos(rad);
//        sin = Math.sin(rad);
//        return directionalAngle;
    }

    private double setDirectioAngle(long directionAngle) {
        longDirectionalAngle = directionAngle;
        if (longDirectionalAngle >= 36000) {
            longDirectionalAngle -= 36000;
        } else if (longDirectionalAngle < 0) {
            longDirectionalAngle = 36000 + longDirectionalAngle;
        }
        double directionalAngle = MathUtils.longToDoubleWith2Dps(longDirectionalAngle);
        double rad = (Math.PI * directionalAngle) / 180d;
        cos = Math.cos(rad);
        sin = Math.sin(rad);
        return directionalAngle;
    }

    /**
     *
     */
    private double[] stepVec = new double[3];
    private double cos;
    private double sin;
    private double[] currentLocation = new double[3];

    private double[] updatetMoveProperty(double step) {
        double dX = step * cos;
        double dY = step * sin;
        curX += MathUtils.doubleToLongWith2Dps(dX);
        curY += MathUtils.doubleToLongWith2Dps(dY);
        stepVec[0] = dX;
        stepVec[1] = dY;
        stepVec[2] = 0d;
        VAlgebra.xyVec3Translate(currentLocation, stepVec, currentLocation);
        return currentLocation;
    }

    /**
     * @param step
     */
    public void moveModel(double step) {
        currentLocation = updatetMoveProperty(step);
        executeMotion(curTopToWrd, directionalAngle, currentLocation);
    }

    private final double[][] rotMat = new double[4][3];

    /**
     * @param increment
     */
    public void rotateModel(int increment) {
        directionalAngle = updateDirectionAngle(increment);
        executeMotion(curTopToWrd, directionalAngle, currentLocation);
    }

    public void executeMotion(double[][] curTopToWrd, double directionalAngle, double[] currentLocation) {
        VAlgebra.initMat43(curTopToWrd, directionalAngle, currentLocation);
        // do transform and notify views
        modelToWorldTransform(curTopToWrd);
        spaceExplorerModel.fireModelChanged(directionalAngle, curTopToWrd);
    }


    private final double[][] turnModelOffsetMat = new double[4][3];
    private final double[][] turnModelBackOffsetMat = new double[4][3];
    private final double[][] turnModelWMat = new double[4][3];
    private final double[] turnModelOffsetVec = new double[3];
    private final double[] turnModelBackOffsetVec = new double[3];

    /**
     * @param increment
     * @param center
     */
    public void turnModel(int increment, double center) {
        VAlgebra.initVec3(turnModelOffsetVec, 0, -center, 0);
        VAlgebra.initVec3(turnModelBackOffsetVec, 0, center, 0);

        directionalAngle = updateDirectionAngle(increment);
        double doubleIncrement = MathUtils.longToDoubleWith2Dps(increment);
        VAlgebra.initMat43(rotMat, doubleIncrement, null);
        VAlgebra.initMat43(turnModelOffsetMat, 0, turnModelOffsetVec);
        VAlgebra.initMat43(turnModelBackOffsetMat, 0, turnModelBackOffsetVec);

        VAlgebra.trf43Xtrf43(turnModelWMat, rotMat, turnModelOffsetMat);
        VAlgebra.trf43Xtrf43(turnModelWMat, turnModelBackOffsetMat, turnModelWMat);
        VAlgebra.trf43Xtrf43(curTopToWrd, curTopToWrd, turnModelWMat);

        VAlgebra.copyVec3(currentLocation, curTopToWrd[3]);

        // do transform and notify views
        modelToWorldTransform(curTopToWrd);
        spaceExplorerModel.fireModelChanged(directionalAngle, curTopToWrd);
    }

    /**
     * @param targetDirection
     */
    public void setTargetDirection(double targetDirection) {
        targetDirectionSensor.setTargetDirection(targetDirection);
    }

    public double[] getCurrentDirectionVector() {
        return seBody.getCurrentDirectionVector();
    }

    public double[] getCurrentLocationVector() {
        return currentLocation;//seBody.getCurrentLocationVector();
    }

    public double getCurX() {
        return MathUtils.longToDoubleWith2Dps(curX);
    }

    public double getCurY() {
        return MathUtils.longToDoubleWith2Dps(curY);
    }

    public long getLongDirection() {
        return longDirectionalAngle;
    }

    public double getDirection() {
        return MathUtils.longToDoubleWith2Dps(longDirectionalAngle);
    }

//    public double[] getDirectionAsVector() {
//        return Utils.longToDoubleWith2Dps(longDirectionalAngle);
//    }


    public List<ModelSensor> getAllSensors() {
        return sensors;
    }

    public void modelToWorldTransform(double[][] mat43) {
//        List modelEntityList = getModelEntityList();
        for (int i = 0; i < vehicleEntityArray.length; i++) {
            vehicleEntityArray[i].modelToWorldTransform(mat43);
        }
    }

    /**
     * @param directionTip
     * @param directionEnd
     * @param selectedDirectionColor
     * @param visible
     */
    public void updateSelectedDirectionVector(double[] directionTip, double[] directionEnd,
                                              Color selectedDirectionColor, boolean visible) {
        BasicModelEntity basicModelEntity = surface.updateSelectedDirectionVector(directionTip, directionEnd,
                selectedDirectionColor, visible);
        fireModelRedefined(basicModelEntity);
    }
}
