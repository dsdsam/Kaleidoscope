/*
 * Created on Aug 27, 2005
 *
 */
package sem.appui.controls;

import adf.csys.model.BasicModelEntity;
import adf.csys.model.ModelChangeListener;
import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.controlles.modelcontroller.ModelMotionOperation;
import sem.appui.controller.BasicOperationRequest;
import sem.appui.MissionControlCenterPanel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author xpadmin
 */
public class DirectionalNavigatonPanel extends BasicManualControlPanel {

    public static final double EPS = 5;

    private Color bgColor = Color.BLACK;

    private Color sec1EnableColor = new Color(70, 70, 70);
    private Color sec2EnableColor = new Color(10, 10, 10);
    private Color sec1DisableColor = Color.darkGray;
    private Color sec2DisableColor = Color.gray;

    private Color sec1Color = sec1DisableColor;
    private Color sec2Color = sec2DisableColor;

    private Color targetEnableColor = new Color(0, 160, 0);
    private Color arrowEnableColor = new Color(255, 0, 0);
    private Color targetDisableColor = Color.darkGray;
    private Color arrowDisableColor = Color.gray;

    private Color targetColor = targetDisableColor;
    private Color arrowColor = arrowDisableColor;

    private Color nwesEnabledColor = new Color(0xAAAA00);
    private Color nwesRedusedColor = new Color(0x555500);
    private Color nwesDisabledColor = new Color(0xAAAAAA);
    private Color nwesCurrentColor;

    private Color circleEnabledColor = new Color(0x008800);
    private Color circleRedusedColor = new Color(0x333333);
    private Color circleDisabledColor = new Color(0x222222);
    private Color circleCurrentColor;

    private int size = 100;
    private int FONT_SIZE = 10;

    private double centerX;
    private double centerY;

    private int drawingX;
    private int drawingY;
    private int drawingWidth;
    private int drawingHeight;
    private double scale = 5;

    Rectangle pickRect = new Rectangle();
    private Point north;
    private Point west;
    private Point east;
    private Point south;

    private int currentWidth;
    private int currentHeight;
    private double moveingDistance;

    private final ModelController modelController;

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
            DirectionalNavigatonPanel.this.repaint();
        }

        public void modelRedefined(BasicModelEntity basicModelEntity) {

        }
    };

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
        }

        /**
         * @param modelOperationRequest
         */
        public void executionCompleted(BasicOperationRequest<ModelMotionOperation, double[]> modelOperationRequest) {
            ModelMotionOperation modelMotionOperation = modelOperationRequest.getRequestedOperation();
//            if (modelOperationRequest.isOperationInterrupted()) {
//            System.out.println("DirectionalNavigatonPanel.executionCompleted for request = " + modelOperationRequest);
            repaint();
//            }
        }

    };


//    private final ServiceExecutionListenerAdapter serviceExecutionListenerAdapter = new ServiceExecutionListenerAdapter() {
//
//        /**
//         * Called on service enqueued request. Should be overridden by extending class.
//         *
//         * @param operationRequest
//         */
//        protected void enqueued(BasicOperationRequest operationRequest) {
//            System.out.println("DirectionalNavigatonPanel.ServiceExecutionListener for request = " + operationRequest);
//        }
//
//
//        /**
//         * Callback method that is called when action executin\on interrupted.
//         *
//         * @param operationRequest
//         */
//        protected void interrupted(BasicOperationRequest operationRequest) {
//            System.out.println("DirectionalNavigatonPanel.ServiceExecutionListener for request = " + operationRequest);
////            repaint();
//        }
//
//        /**
//         * Called on service response, when result is successful. Should be
//         * overridden by extending class.
//         *
//         * @param operationRequest
//         */
//        protected void success(BasicOperationRequest operationRequest) {
//            System.out.println("DirectionalNavigatonPanel.ServiceExecutionListener for request = " + operationRequest);
////            finishOperationExecution();
//        }
//
//        /**
//         * Called on service response, when result is failure. Should be overridden
//         * by extending class.
//         *
//         * @param operationRequest
//         */
//        protected void failure(BasicOperationRequest operationRequest) {
//            System.out.println("DirectionalNavigatonPanel.ServiceExecutionListener for request = " + operationRequest);
//        }
//
//        /**
//         * Called on service response, when timeout expired before response received.
//         * Should be overridden by extending class.
//         *
//         * @param operationRequest
//         */
//        protected void timeout(BasicOperationRequest operationRequest) {
//            System.out.println("DirectionalNavigatonPanel.ServiceExecutionListener for request = " + operationRequest);
//        }
//
////        public void executionDone(BasicOperationRequest operationRequest) {
////            OperationRequestStatus operationRequestStatus = operationRequest.getOperationRequestStatus();
////            System.out.println("Direction Rotation Done with status = " + operationRequestStatus);
////            finishOperationExecution();
////        }
//    };

    private MouseAdapter mousePickProcessor = new MouseAdapter() {

        public void mousePressed(MouseEvent e) {

            if (!DirectionalNavigatonPanel.this.isManualControlOn()) {
                return;
            }


            int x = e.getX();
            int y = e.getY();

            Dimension panelSize = getSize();
            double cx = panelSize.width / 2;
            double cy = panelSize.height / 2;
            double dx = x - cx;
            double dy = -(y - cy);

            double pickVectorLength = Math.sqrt(dx * dx + dy * dy);
            if (Math.abs(pickVectorLength) > 50d) {
                return;
            }

//            ModelController.getInstance().stopAllOperation();

            double th = Math.atan2(dy, dx);
            if (th < 0) {
                th = 2 * Math.PI + th;
            }
            double angle = th * (180 / Math.PI);
            angle = Math.round(angle);

            if (Math.abs(angle - 30) < EPS) {
                angle = 30;
            } else if (Math.abs(angle - 45) < EPS) {
                angle = 45;
            } else if (Math.abs(angle - 60) < EPS) {
                angle = 60;
            } else if (Math.abs(angle - 90) < EPS) {
                angle = 90;
            } else if (Math.abs(angle - 120) < EPS) {
                angle = 120;
            } else if (Math.abs(angle - 135) < EPS) {
                angle = 135;
            } else if (Math.abs(angle - 150) < EPS) {
                angle = 150;
            } else if (Math.abs(angle - 180) < EPS) {
                angle = 180;
            } else if (Math.abs(angle - 210) < EPS) {
                angle = 210;
            } else if (Math.abs(angle - 225) < EPS) {
                angle = 225;
            } else if (Math.abs(angle - 240) < EPS) {
                angle = 240;
            } else if (Math.abs(angle - 270) < EPS) {
                angle = 270;
            } else if (Math.abs(angle - 300) < EPS) {
                angle = 300;
            } else if (Math.abs(angle - 315) < EPS) {
                angle = 315;
            } else if (Math.abs(angle - 330) < EPS) {
                angle = 330;
            } else if (Math.abs(0 - angle) < EPS || Math.abs(360 - angle) < EPS) {
                angle = 0;
            }

//            System.out.println("mousePressed " + th + ",  " + angle + ",  " + pickVectorLength);

            // speed calculatio
            double maxSpeed = cx;
            double nSpeeds = 5;
            if (pickVectorLength > maxSpeed) {
                pickVectorLength = maxSpeed;
            }
            double speed = pickVectorLength = (int) ((pickVectorLength / maxSpeed) * nSpeeds + 0.5);

            modelController.executeMotionOperation(ModelMotionOperation.OPERATION_ROTATE_TO_THE_ANGLE,
                     new double[]{angle}, null);
//                    new double[]{angle}, serviceExecutionListenerAdapter);
        }
    };

    /**
     * @param size
     */
    public DirectionalNavigatonPanel(MissionControlCenterPanel missionControlCenterPanel, Dimension size) {
        super(missionControlCenterPanel, size);
        setLayout(null);
        setBackground(bgColor);
        addMouseListener(mousePickProcessor);
        this.addComponentListener(componentListenerAdapter);
        modelController = ModelController.getInstance();
        spaceExplorerModel = modelController.getSpaceExplorerModel();
        spaceExplorerModel.addModelChangeListener(modelChangeListener);
        modelController.addModelOperationRequestStateChangeListener(modelOperationRequestStateChangeListener);
    }

    /**
     * 
     *
     */
    protected void onSizeChanged() {
        Dimension size = getSize();
        centerX = size.width / 2;
        centerY = size.height / 2;

        pickRect = new Rectangle(0, 0, size.width, size.height);
        pickRect.grow(-15, -15);
        drawingX = pickRect.x + 1;
        drawingY = pickRect.y + 1;
        drawingWidth = pickRect.width - 3;
        drawingHeight = pickRect.height - 3;
        int charWidth = 8;
        int charHeight = 8;
        north = new Point((size.width / 2) - charWidth / 2, charHeight + 4);
        west = new Point(4, size.height / 2);
        east = new Point(size.width - charWidth - 4, size.height / 2);
        south = new Point((size.width / 2) - charWidth / 2, size.height - 5);
    }

    /**
     * 
     */
    public void paint(Graphics g) {

        super.paint(g);

        Rectangle origRect = this.getBounds();
        if (currentWidth != origRect.width || currentHeight != origRect.height) {
            currentWidth = origRect.width;
            currentHeight = origRect.height;
            onSizeChanged();
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw sections
        int angStep = 30;
        int n = 360 / angStep;
        int startAngle = -30;
        g.setColor(sec2Color);
        g.fillOval(drawingX, drawingY, drawingWidth, drawingHeight);
        g.setColor(sec1Color);
        for (int i = 0; i < n; i++) {
            if ((i % 2) == 0) {
                g.fillArc(drawingX, drawingY, drawingWidth, drawingHeight, startAngle, angStep);
            }
            startAngle += angStep;
        }

        int targetDirectionAngle = -1;
        int currectDirectionAngle = -1;
        if (powerOn) {
            targetDirectionAngle = (int) (Math.round(modelController.getTargetDirection()) / 100);
            currectDirectionAngle = (int) Math.round(spaceExplorerModel.getDirection());

            // draw target
            if (modelController.isTargetDirectionAssigned()) {
                g.setColor(targetColor);
                g.fillArc(drawingX, drawingY, drawingWidth, drawingHeight, targetDirectionAngle - 9, 18);
            }
        }

        // draw core
        int selectorOrigin = drawingX + 20;
        int selectorWidth = drawingWidth - 40;

        g.setColor(bgColor);
        g2.fillOval(selectorOrigin, selectorOrigin, selectorWidth, selectorWidth);

        // draw circles
        g.setColor(circleCurrentColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(drawingX, drawingY, drawingWidth, drawingHeight);
        g2.drawOval(selectorOrigin, selectorOrigin, selectorWidth, selectorWidth);

        g.setColor(arrowColor);
        g.fillArc(drawingX, drawingY, drawingWidth, drawingHeight, currectDirectionAngle - 3, 6);

//        g.setFont(new Font(g.getFont().getName(),    Font.PLAIN, FONT_SIZE));
//        g.setColor(nwesCurrentColor);
//        g.drawString( "N", north.x, north.y );
//        g.drawString( "W", west.x, west.y );
//        g.drawString( "E", east.x, east.y );
//        g.drawString( "S", south.x, south.y );
    }

    /**
     *
     *
     */
    @Override
    protected void panelEnergized(boolean status) {
        setEnabled(status);
        if (status) {
            sec1Color = sec1EnableColor;
            sec2Color = sec2EnableColor;
            targetColor = targetEnableColor;
            arrowColor = arrowEnableColor;
            nwesCurrentColor = nwesEnabledColor;
            circleCurrentColor = circleEnabledColor;
        } else {
            sec1Color = sec1DisableColor;
            sec2Color = sec2DisableColor;
            targetColor = targetDisableColor;
            arrowColor = arrowDisableColor;
            nwesCurrentColor = nwesDisabledColor;
            circleCurrentColor = circleDisabledColor;
        }
//        Thread.dumpStack();
        repaint();
    }

//    @Override
//    public void cancelOperation() {
//
//    }

    private void finishOperationExecution() {
//        if (!targetSelected) {
//            return;
//        }

//        targetSelected = false;
//        Thread.dumpStack();
        repaint();
        System.out.println("Finished !  Rotate model operation finished");
    }
}
