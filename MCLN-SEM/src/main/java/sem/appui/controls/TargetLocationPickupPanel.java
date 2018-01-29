package sem.appui.controls;

import adf.csys.model.BasicModelEntity;
import adf.csys.model.ModelChangeListener;
import sem.infrastructure.OperationRequestStatus;
import sem.mission.explorer.model.SpaceExplorerModel;
import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.controlles.modelcontroller.ModelMotionOperation;
import sem.mission.controlles.modelcontroller.actions.ServiceExecutionListener;
import sem.appui.controller.BasicOperationRequest;
import sem.appui.MissionControlCenterPanel;
import vw.valgebra.VAlgebra;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Nov 12, 2011
 * Time: 1:07:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class TargetLocationPickupPanel extends BasicManualControlPanel {

    private static final int N_COLUMNS = 8;
    private static final int HORIZONTAL_STEP = 15;
    private static final int N_ROWS = 5;
    private static final int VERTICAL_STEP = 15;
    private static final int GRID_STEP = HORIZONTAL_STEP;
    private static final Dimension panelSize =
            new Dimension((N_COLUMNS * HORIZONTAL_STEP) + 1, (N_ROWS * VERTICAL_STEP) + 1);

    private final Color OUTLINE_ENABLE_COLOR = new Color(0x008800);
    private final Color OUTLINE_DISABLE_COLOR = new Color(0x222222);
    private final Color GRID_COLOR = new Color(0x666666);

    private final Color CURRENT_LOCATION_ENABLE_COLOR = new Color(255, 0, 0);
    private final Color CURRENT_LOCATION_DISABLE_COLOR = Color.DARK_GRAY;
    private final Color TARGET_ENABLE_COLOR = new Color(0, 160, 0);
    private final Color TARGET_DISABLE_COLOR = Color.darkGray;
    private final Color DIRECTION_ARROW_ENABLE_COLOR = new Color(255, 255, 0);
    private final Color DIRECTION_ARROW_DISABLE_COLOR = Color.GRAY;

    private final Color CELL_GRAY_ENABLE_COLOR = new Color(50, 50, 50);
    private final Color CELL_BLACK_ENABLE_COLOR = new Color(30, 30, 30);
    private final Color CELL_GRAY_DISABLE_COLOR = Color.gray;
    private final Color CELL_BLACK_DISABLE_COLOR = Color.darkGray;


    private final double SURFACE_WIDTH = 100;
    private final double SURFACE_HEIGHT = 60;


    private Color sellGrayColor = CELL_GRAY_DISABLE_COLOR;
    private Color sellBlackColor = CELL_BLACK_DISABLE_COLOR;


    private Color targetColor = TARGET_DISABLE_COLOR;
    private Color currentLocationColor = CURRENT_LOCATION_DISABLE_COLOR;
    private Color directionArrowColor = DIRECTION_ARROW_DISABLE_COLOR;


    private Color outlineColor;


    private final double worldToControl_X_Coefficient;
    private final double worldToControl_Y_Coefficient;
    private final double controlToWorld_X_Coefficient;
    private final double controlToWorld_Y_Coefficient;
    private final int controlCenterXCoord;
    private final int controlCenterYCoord;

    private final double[] selectedDirectionVector = new double[3];

//    private boolean allowDirectionVector;
    private boolean drawDirectionVector;
    private int viewInitialXLocation;
    private int viewInitialYLocation;
    private int viewTargetXLocation;
    private int viewTargetYLocation;

    private SpaceExplorerModel spaceExplorerModel;
    private ModelController modelController;

    /**
     *
     */
    private ComponentAdapter componentListenerAdapter = new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            onSizeChanged();
        }

        public void componentShown(ComponentEvent e) {
            onSizeChanged();
        }
    };

    /**
     *
     */
    private ModelChangeListener modelChangeListener = new ModelChangeListener() {
        public void modelChanged(double currentAngle, double[][] mat43) {
//            Thread.dumpStack();
            TargetLocationPickupPanel.this.repaint();
        }

        public void modelRedefined(BasicModelEntity basicModelEntity) {

        }
    };

    /**
     * This listeners should understend interrupted operation
     */
    private final ServiceExecutionListener rotateOperationServiceExecutionListener = new ServiceExecutionListener() {

        public void executionDone(BasicOperationRequest<?, ?> operationRequest) {

            OperationRequestStatus operationRequestStatus = operationRequest.getOperationRequestStatus();
//            System.out.println("TargetLocationPickupPanel.rotateOperationServiceExecutionListener: Done ! " +
//                    " Rotate to Target Location Operation: - Rotation Done with status = " + operationRequestStatus);
            if (operationRequest.isRequestExecutedSuccessfully()) {
                if (!ModelController.getInstance().isQueueOn()) {
                    double[] taggetLocation = (double[]) operationRequest.getArgument();
                    ModelController.getInstance().executeMotionOperation(ModelMotionOperation.OPERATION_MOVE_TO_THE_POINT,
                            taggetLocation, moveOperationServiceExecutionListener);
                }

            } else {
//                finishOperationExecution();
            }
        }
    };

    private final ServiceExecutionListener moveOperationServiceExecutionListener = new ServiceExecutionListener() {

        public void executionDone(BasicOperationRequest<?, ?> operationRequest) {
            OperationRequestStatus operationRequestStatus = operationRequest.getOperationRequestStatus();
//            System.out.println("TargetLocationPickupPanel.moveOperationServiceExecutionListener: Done ! " +
//                    " Move to Target Location Operation: - Move Done with status = " + operationRequestStatus);
            if (!operationRequest.isRequestEnqueued()) {
//                finishOperationExecution();
            }
        }
    };

    /**
     *
     */
    private final ModelOperationRequestStateChangeListener modelOperationRequestStateChangeListener
            = new ModelOperationRequestStateChangeListener() {

        /**
         * @param modelOperationRequest
         */
        public void executionStarted(BasicOperationRequest<ModelMotionOperation, double[]> modelOperationRequest) {
            ModelMotionOperation modelMotionOperation = modelOperationRequest.getRequestedOperation();

            if (!(modelMotionOperation == ModelMotionOperation.OPERATION_SPIN_TO_THE_POINT ||
                    modelMotionOperation == ModelMotionOperation.OPERATION_MOVE_TO_THE_POINT ||
                    modelMotionOperation == ModelMotionOperation.OPERATION_MOVE_FORWARD ||
                    modelMotionOperation == ModelMotionOperation.OPERATION_MOVE_BACKWARD)) {
                return;
            }

            // world current location to control current location
            double[] currentLocationVector = spaceExplorerModel.getCurrentLocationVector();
            viewInitialXLocation = controlCenterXCoord + (int) Math.round(currentLocationVector[0] *
                    worldToControl_X_Coefficient);
            viewInitialYLocation = controlCenterYCoord - (int) Math.round(currentLocationVector[1] *
                    worldToControl_Y_Coefficient);

            double[] worldTargetLocationVector = null;
            if (modelMotionOperation == ModelMotionOperation.OPERATION_SPIN_TO_THE_POINT) {
                worldTargetLocationVector = modelOperationRequest.getArgument();
            } else if (modelMotionOperation == ModelMotionOperation.OPERATION_MOVE_TO_THE_POINT) {
                worldTargetLocationVector = modelOperationRequest.getArgument();
            } else if (modelMotionOperation == ModelMotionOperation.OPERATION_MOVE_FORWARD ||
                    modelMotionOperation == ModelMotionOperation.OPERATION_MOVE_BACKWARD) {

                double[] arrayOfArguments = modelOperationRequest.getArgument();
                double distanceToTheTargetLocation = 0;
                if (arrayOfArguments != null) {
                    distanceToTheTargetLocation = arrayOfArguments[0];
                }
                double[] normalizedCurrentDirectionVector = spaceExplorerModel.getCurrentDirectionVector();
                worldTargetLocationVector = new double[3];
                VAlgebra.LinCom3(worldTargetLocationVector, 1, currentLocationVector, distanceToTheTargetLocation,
                        normalizedCurrentDirectionVector);
            }
            // world target location to the control target location
            viewTargetXLocation = controlCenterXCoord + (int) Math.round(worldTargetLocationVector[0] * worldToControl_X_Coefficient);
            viewTargetYLocation = controlCenterYCoord - (int) Math.round(worldTargetLocationVector[1] * worldToControl_Y_Coefficient);

//            System.out.println("\n\nviewInitialXLocation = " + viewInitialXLocation);
//            System.out.println("viewInitialYLocation  = " + viewInitialYLocation);
//            System.out.println("viewTargetXLocation = " + viewTargetXLocation);
//            System.out.println("viewTargetYLocation  = " + viewTargetYLocation);
            drawDirectionVector = true;
            TargetLocationPickupPanel.this.repaint();
        }

        /**
         * @param modelOperationRequest
         */
        public void executionCompleted(
                BasicOperationRequest<ModelMotionOperation, double[]> modelOperationRequest) {
            drawDirectionVector = false;
//            Thread.dumpStack();
            TargetLocationPickupPanel.this.repaint();
        }
    };

    /**
     *
     */
    private MouseAdapter mouseListener = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            processMouseClicked(e);
        }
    };


    /**
     * @param missionControlCenterPanel
     */
    public TargetLocationPickupPanel(MissionControlCenterPanel missionControlCenterPanel) {
        super(missionControlCenterPanel, panelSize);
        this.missionControlCenterPanel = missionControlCenterPanel;
        modelController = ModelController.getInstance();
        setSize(panelSize);
        setPreferredSize(panelSize);
        setMaximumSize(panelSize);
        setMinimumSize(panelSize);

        setBackground(Color.BLACK);

        worldToControl_X_Coefficient = ((double) getSize().width) / SURFACE_WIDTH;
        worldToControl_Y_Coefficient = ((double) getSize().height) / SURFACE_HEIGHT;
        controlToWorld_X_Coefficient = (SURFACE_WIDTH / (double) getSize().width);
        controlToWorld_Y_Coefficient = (SURFACE_HEIGHT / (double) getSize().height);
        controlCenterXCoord = getSize().width / 2;
        controlCenterYCoord = getSize().height / 2;

        spaceExplorerModel = ModelController.getInstance().getSpaceExplorerModel();
        spaceExplorerModel.addModelChangeListener(modelChangeListener);

        addMouseListener(mouseListener);
        this.addComponentListener(componentListenerAdapter);
        String allowDrawDirectionStr = System.getProperty("draw.direction");
//        allowDirectionVector =
//                (allowDrawDirectionStr != null && allowDrawDirectionStr.equalsIgnoreCase("true")) ? true : false;

        modelController.addModelOperationRequestStateChangeListener(modelOperationRequestStateChangeListener);
//        ModelOperationRequestQueue modelOperationRequestQueue = modelController.getModelOperationRequestQueue();
//
//        modelOperationRequestQueue.addModelOperationRequestQueueListener(modelRequestQueueListener);
    }

    /**
     *
     */
    protected void onSizeChanged() {
        Dimension size = getSize();
//        centerX = size.width / 2;
//        centerY = size.height / 2;
//        drawingX = 0;
//        drawingY = 0;
//        drawingWidth = size.width;
//        drawingHeight = size.height;
    }

    /**
     * @param e
     */
    private void processMouseClicked(MouseEvent e) {

        if (!isManualControlOn()) {
            return;
        }

        int x = e.getX();
        int y = e.getY();

        Rectangle rect = this.getBounds();
        rect.translate(-rect.x, -rect.y);
//        rect.grow(-5, -5);
        if (!rect.contains(x, y)) {
//            System.out.println("Not inside " + x + "  " + y + " rect" + rect);
            return;
        } else {
//            System.out.println("Inside " + x + "  " + y + " rect" + rect);
        }

//        ModelController.getInstance().stopAllOperation();

        Dimension d = getSize();
        double cx = d.width / 2;
        double cy = d.height / 2;
        double dx = x - cx;
        double dy = -(y - cy);
        double th = Math.atan2(dy, dx);
        if (th < 0) {
            th = 2 * Math.PI + th;
        }

        double angle = th * (180 / Math.PI);

        ////////////////////////////////////

//        double halfOfWidth = ((double) drawingWidth) / 2d;
//        double halfOfHeight = ((double) drawingHeight) / 2d;
//        System.out.println("drawingWidth  = " + drawingWidth);
//        System.out.println("halfOfWidth   = " + halfOfWidth);
//        System.out.println("drawingHeight = " + drawingHeight);
//        System.out.println("halfOfHeight  = " + halfOfHeight);
//        System.out.println("cx = " + cx);
//        System.out.println("cy  = " + cy);
//        System.out.println("centerX = " + centerX);
//        System.out.println("centerY  = " + centerY);
//        System.out.println("dx = " + dx);
//        System.out.println("dy  = " + dy);

//        VAlgebra.initVec3(pickVector, dx, dy, 0d);
//        double pickVectorLength = VAlgebra.vec3Len(pickVector);
//
//        System.out.println("pickVectorLength  = " + pickVectorLength);

        double[] currentLocationVector = spaceExplorerModel.getCurrentLocationVector();

        // world current location to control current location
//        controlInitialXLocation = controlCenterXCoord + (int) Math.round(currentLocationVector[0] * worldToControl_X_Coefficient);
//        controlInitialYLocation = controlCenterYCoord - (int) Math.round(currentLocationVector[1] * worldToControl_Y_Coefficient);

        // local CSYS pick point to world CSYS pick point
        double worldX = dx * controlToWorld_X_Coefficient;
        double worldY = dy * controlToWorld_Y_Coefficient;
//        System.out.println("worldX = " + worldX);
//        System.out.println("worldY  = " + worldY);
//
        double[] currentDirectionVector = spaceExplorerModel.getCurrentDirectionVector();
        double[] worldTargetLocationVector = new double[3];
        VAlgebra.initVec3(worldTargetLocationVector, worldX, worldY, 0d);

//        System.out.println("\n\ncontrolCenterXCoord = " + controlCenterXCoord);
//        System.out.println("controlCenterYCoord  = " + controlCenterYCoord);
//        System.out.println("controlInitialXLocation = " + controlInitialXLocation);
//        System.out.println("controlInitialYLocation  = " + controlInitialYLocation);

        // world target location to the control target location
//        controlTargetXLocation = controlCenterXCoord + (int) Math.round(worldTargetLocationVector[0] * worldToControl_X_Coefficient);
//        controlTargetYLocation = controlCenterYCoord - (int) Math.round(worldTargetLocationVector[1] * worldToControl_Y_Coefficient);
//        System.out.println("controlTargetXLocation = " + controlTargetXLocation);
//        System.out.println("controlTargetYLocation  = " + controlTargetYLocation);

        // find direction from current point to target point in world CSYS
//        double[] selectedDirectionVector = new double[3];
//        selectedDirectionVector = VAlgebra.subVec3(selectedDirectionVector, worldTargetLocationVector, currentLocationVector);
//        selectedMoveingDistance = VAlgebra.vec3Len(selectedDirectionVector);
//        System.out.println("currentLocationVector-X = " + currentLocationVector[0]);
//        System.out.println("currentLocationVector-Y = " + currentLocationVector[1]);
        dx = selectedDirectionVector[0];
        dy = selectedDirectionVector[1];
        th = Math.atan2(dy, dx);
        if (th < 0) {
            th = 2 * Math.PI + th;
        }
        angle = th * (180 / Math.PI);

        // update travel distance vector in world
        //
        //   PRESERVED FOR DEBUGGING
        //
//        if (drawDirection) {
//            double rad = (Math.PI * angle) / 180d;
//            double cos = Math.cos(rad);
//            double sin = Math.sin(rad);
//            double dX = selectedMoveingDistance * cos;
//            double dY = selectedMoveingDistance * sin;
//            double[] stepVec = new double[3];
//            stepVec[0] = dX;
//            stepVec[1] = dY;
//            VAlgebra.addVec3(worldTargetLocationVector, stepVec, currentLocationVector);
//            spaceExplorerModel.updateSelectedDirectionVector(worldTargetLocationVector, currentLocationVector,
//                    Color.GRAY, true);
//        }
//        System.out.println("mousePressed " + angle);
        angle = Math.round(angle);
//        System.out.println("mousePressed " + angle);


        boolean enqueued = modelController.executeMotionOperation(ModelMotionOperation.OPERATION_SPIN_TO_THE_POINT,
                worldTargetLocationVector, rotateOperationServiceExecutionListener);

        // submit move request from here if queue is enabled
        if (enqueued) {
            modelController.executeMotionOperation(ModelMotionOperation.OPERATION_MOVE_TO_THE_POINT,
                    worldTargetLocationVector, moveOperationServiceExecutionListener);
        }
    }

    private Rectangle visitingRect = new Rectangle();

    /**
     * @param g
     */
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle rect = this.getBounds();
        // painting cells
        int cellIndex = 0;
//        Rectangle visitingRect = new Rectangle(0, 0, gridStep, gridStep);
        visitingRect.setBounds(0, 0, GRID_STEP, GRID_STEP);
        for (int i = 0; i < rect.height; i += GRID_STEP) {
            cellIndex = i;
            for (int j = 0; j < rect.width; j += GRID_STEP) {
                Color currentColor = ((cellIndex % 2) == 0) ? sellBlackColor : sellGrayColor;
                g.setColor(currentColor);
                g.fillRect(visitingRect.x, visitingRect.y, visitingRect.width, visitingRect.height);
                visitingRect.translate(GRID_STEP, 0);
                cellIndex++;
            }
            visitingRect.x = 0;
            visitingRect.translate(0, GRID_STEP);
        }

        // painting grid
        g.setColor(GRID_COLOR);
        for (int i = VERTICAL_STEP; i < rect.height; i += VERTICAL_STEP) {
            g.drawLine(1, i, rect.width, i);
        }
        for (int j = HORIZONTAL_STEP; j < rect.width; j += HORIZONTAL_STEP) {
            g.drawLine(j, 1, j, rect.height);
        }

        // painting outline
        g.setColor(outlineColor);
        g.drawRect(0, 0, rect.width - 1, rect.height - 1);

        // painting model
        double[] currentLocationVector = spaceExplorerModel.getCurrentLocationVector();
        int currentLocationX = (int) Math.round(currentLocationVector[0] * worldToControl_X_Coefficient);
        int currentLocationY = (int) Math.round(currentLocationVector[1] * worldToControl_Y_Coefficient);
        int rectX = controlCenterXCoord + currentLocationX;
        int rectY = controlCenterYCoord - currentLocationY;

//        System.out.println("allowDirectionVector = "+allowDirectionVector+ ", drawDirectionVector = "+drawDirectionVector);
//        if (allowDirectionVector && drawDirectionVector) {
        if (drawDirectionVector) {
            g.setColor(Color.CYAN);
            g.drawLine(viewInitialXLocation, viewInitialYLocation, viewTargetXLocation, viewTargetYLocation);
        }

        g.setColor(currentLocationColor);
        g.drawLine(rectX - 1, rectY - 2, rectX + 1, rectY - 2);
        g.drawLine(rectX - 2, rectY - 1, rectX + 2, rectY - 1);
        g.drawLine(rectX - 2, rectY, rectX + 2, rectY);
        g.drawLine(rectX - 2, rectY + 1, rectX + 2, rectY + 1);
        g.drawLine(rectX - 1, rectY + 2, rectX + 1, rectY + 2);

    }

    /**
     * @param status
     */
    @Override
    protected void panelEnergized(boolean status) {
        setEnabled(status);
        if (status) {
            sellGrayColor = CELL_GRAY_ENABLE_COLOR;
            sellBlackColor = CELL_BLACK_ENABLE_COLOR;
            targetColor = TARGET_ENABLE_COLOR;
            currentLocationColor = CURRENT_LOCATION_ENABLE_COLOR;
            directionArrowColor = DIRECTION_ARROW_ENABLE_COLOR;
            outlineColor = OUTLINE_ENABLE_COLOR;
        } else {
            sellGrayColor = CELL_GRAY_DISABLE_COLOR;
            sellBlackColor = CELL_BLACK_DISABLE_COLOR;
            targetColor = TARGET_DISABLE_COLOR;
            currentLocationColor = CURRENT_LOCATION_DISABLE_COLOR;
            directionArrowColor = DIRECTION_ARROW_DISABLE_COLOR;
            outlineColor = OUTLINE_DISABLE_COLOR;
        }
        repaint();
//        Thread.dumpStack();
    }

    public void stopOperation() {
    }

}