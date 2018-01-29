package sem.appui;

import led.LedOperation;
import led.LedSignPanel;
import led.OperationsProvider;
import sem.appui.controller.BasicOperationRequest;
import sem.mission.controlles.modelcontroller.ModelMotionOperation;
import sem.mission.controlles.modelcontroller.ModelTransmissionController;
import sem.appui.controls.ModelOperationRequestStateChangeListener;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Apr 20, 2013
 * Time: 2:32:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MissionTransmissionProvider extends OperationsProvider {

    private static final Logger logger = Logger.getLogger(MissionTransmissionProvider.class.getName());

    //    public static MissionTransmissionProvider missionTransmissionProvider;
    public static final AtomicReference<MissionTransmissionProvider> atomicReferenceToMissionTransmissionProvider =
            new AtomicReference<MissionTransmissionProvider>();

    /**
     * @param ledSignPanel
     */
    public static MissionTransmissionProvider createInstance(LedSignPanel ledSignPanel) {
        MissionTransmissionProvider missionTransmissionProvider = atomicReferenceToMissionTransmissionProvider.get();
        if (missionTransmissionProvider != null) {
            throw new RuntimeException("Instance Already exists");
        }
        missionTransmissionProvider = new MissionTransmissionProvider(ledSignPanel);
        atomicReferenceToMissionTransmissionProvider.set(missionTransmissionProvider);
        return missionTransmissionProvider;
    }

    /**
     */
    public static MissionTransmissionProvider getInstance() {
        MissionTransmissionProvider missionTransmissionProvider = atomicReferenceToMissionTransmissionProvider.get();
        if (missionTransmissionProvider == null) {
            throw new RuntimeException("Instance not yet created");
        }
        return missionTransmissionProvider;
    }

    private BasicOperationRequest<ModelMotionOperation, double[]> transmissionOperationRequest;

    /**
     *
     */
    private final ModelOperationRequestStateChangeListener modelOperationRequestStateChangeListener
            = new ModelOperationRequestStateChangeListener() {

        /**
         * @param modelOperationRequest
         */
        public void executionStarted(BasicOperationRequest<ModelMotionOperation, double[]> modelOperationRequest) {

            ModelMotionOperation modelOperation = modelOperationRequest.getRequestedOperation();
            if (!(modelOperation == ModelMotionOperation.OPERATION_TRANSMISSION)) {
                return;
            }
            System.out.println("MissionTransmissionProvider: Transmission Activated");
            transmissionOperationRequest = modelOperationRequest;
            double[] transmissionLocation = transmissionOperationRequest.getArgument();
            submitText(transmissionLocation);
        }

        /**
         * @param modelOperationRequest
         */
        public void executionCompleted(BasicOperationRequest<ModelMotionOperation, double[]> modelOperationRequest) {
            ModelMotionOperation modelOperation = modelOperationRequest.getRequestedOperation();
        }

    };

    /**
     *
     */
    private KeyboardFocusManager newManager = new DefaultKeyboardFocusManager() {
        public boolean dispatchEvent(AWTEvent e) {
            processHit(e);
            return super.dispatchEvent(e);
        }
    };

    /**
     * @param e
     */
    private void processHit(AWTEvent e) {
        if (!(e instanceof KeyEvent)) {
            return;
        }
        KeyEvent keyEvent = (KeyEvent) e;
        LedOperation ledOperation;
        String delayAttributes = "delay=1000";
        String attributes;

        String emptyText = "";
        String text;

        if (keyEvent.getKeyCode() == KeyEvent.VK_F1 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            submitText(new double[]{0, 0});
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F2 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F2 released");
            attributes = "delay=10 center=true";
            text = "^pOver Center";
            ledOperation = createOverCenterOperation(attributes, text);
            submitOperation(ledOperation);
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F3 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F3 released");
            attributes = "delay=10 center=true";
            text = "^cScroll of Center";
            ledOperation = createScrollOffCenterOperation(attributes, text);
            submitOperation(ledOperation);
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F4 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F4 released");
            attributes = "delay=10 pixels=30 center=true  ";
            text = "C^oo^yl^go^br^ps";
            ledOperation = createPixelsOperation(attributes, text);
            submitOperation(ledOperation);
        }

//        if (keyEvent.getKeyCode() == KeyEvent.VK_F4 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
//            System.out.println("F4 released");
//            attributes = "delay=500 times=3";
//            text = "";
//            ledOperation = createBlinkOperation(attributes, text);
//            submitOperation(ledOperation);
//        }

        //
        //  Scrolling
        //

        if (keyEvent.getKeyCode() == KeyEvent.VK_F5 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F5 released");
            attributes = "delay=50 center=true";
            text = "^yScroll Up";
            ledOperation = createScrollUpOperation(attributes, text);
            submitOperation(ledOperation);
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F6 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F6 released");
            attributes = "delay=50 center=true";
            text = "^gScroll Down";
            ledOperation = createScrollDownOperation(attributes, text);
            submitOperation(ledOperation);
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F7 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F7 released");
            attributes = "delay=30 center=true";
            text = "^pScroll Left";
            ledOperation = createScrollLeftOperation(attributes, text);
            submitOperation(ledOperation);
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F8 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F8 released");
            attributes = "delay=30 center=true";
            text = "^oScroll Right";
            ledOperation = createScrollRightOperation(attributes, text);
            submitOperation(ledOperation);
        }

        //
        //  O v e r
        //

        if (keyEvent.getKeyCode() == KeyEvent.VK_F9 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F9 released");
            attributes = "delay=50 center=true";
            text = "^yOver Up";
            ledOperation = createOverUpOperation(attributes, text);
            submitOperation(ledOperation);
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F10 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F10 released");
            attributes = "delay=50 center=true";
            text = "^rOver Down";
            ledOperation = createOverDownOperation(attributes, text);
            submitOperation(ledOperation);
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F11 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F11 released");
            attributes = "delay=10 center=true";
            text = "^bOver Left";
            ledOperation = createOverLeftOperation(attributes, text);
            submitOperation(ledOperation);
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_F12 && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            System.out.println("F12 released");
            attributes = "delay=10 center=true";
            text = "^rOver Right";
            ledOperation = createOverRightOperation(attributes, text);
            submitOperation(ledOperation);
        }
    }

    /**
     *
     */
    private KeyAdapter keyListener = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            int keyCode = e.getKeyCode();
            char keyChar = e.getKeyChar();
            System.out.println("keyChar " + keyChar);
            Character.isDefined(keyCode);
            boolean letterOrDigit = Character.isLetterOrDigit(keyChar);
            if (keyChar != KeyEvent.VK_SPACE && !letterOrDigit) {
                System.out.println("NaS");
                return;
            }
            System.out.println("K e y=\"" + keyChar + "\"");
            submitText(new double[]{0, 0});
        }
    };

    //
    //    M i s s i o n   T r a n s m i s s i o n   P r o v i d e r
    //

    private boolean transmissionStarted;

    /**
     * @param ledSignPanel
     */
    public MissionTransmissionProvider(LedSignPanel ledSignPanel) {

        KeyboardFocusManager.setCurrentKeyboardFocusManager(newManager);
        boolean standAloneMode = ledSignPanel.isStandAloneMode();
        if (standAloneMode) {
            ledSignPanel.addKeyBoardInputListener(keyListener);
        } else {
            ledSignPanel.addKeyListener(keyListener);
        }
    }

    public ModelOperationRequestStateChangeListener getModelOperationRequestStateChangeListener() {
        return modelOperationRequestStateChangeListener;
    }

    /**
     *
     */
    boolean toggleMessageFlag;

    private void submitText(double[] transmissionLocation) {
        String attributes;
        String delayAttributes = "delay=1000";
        String text;
        String emptyText = "";
        LedOperation ledOperation;
        String formatString = "";
        String location;

        if (transmissionLocation[0] == 0) {
            if (transmissionLocation[1] == 0) {
                formatString = "^gInitial Position";
            } else if (transmissionLocation[1] > 0) {
                formatString = "^ox ^g%6.2f   ^oNorth ^g%6.2f";
            } else {
                formatString = "^ox ^g%6.2f   ^oSouth ^g%6.2f";
            }
        } else if (transmissionLocation[0] > 0) {
            if (transmissionLocation[1] == 0) {
                formatString = "^oEast ^g%6.2f   ^oy ^g%6.2f";
            } else if (transmissionLocation[1] > 0) {
                formatString = "^oEast ^g%6.2f   ^oNorth ^g%6.2f";
            } else {
                formatString = "^oEast ^g%6.2f   ^oSouth ^g%6.2f";
            }
        } else if (transmissionLocation[0] < 0) {
            if (transmissionLocation[1] == 0) {
                formatString = "^oWeat ^g%6.2f   ^oy ^g%6.2f";
            } else if (transmissionLocation[1] > 0) {
                formatString = "^oWest ^g%6.2f   ^oNorth ^g%6.2f";
            } else {
                formatString = "^oWest ^g%6.2f   ^oSouth ^g%6.2f";
            }
        }
        location = String.format(formatString, Math.abs(transmissionLocation[0]), Math.abs(transmissionLocation[1]));
        try {

            String scrollLeftDelay = "delay=22 ";

            attributes = "delay=50 center=true";
            text = "^pTelemetry Report";
            ledOperation = createScrollDownOperation(attributes, text);
            submitOperation(ledOperation);

            ledOperation = createSleepOperation("delay=1500", emptyText);
            submitOperation(ledOperation);

            attributes = "delay=50 center=true";
            ledOperation = createScrollDownOperation(attributes, emptyText);
            submitOperation(ledOperation);

            ledOperation = createSleepOperation("delay=700", emptyText);
            submitOperation(ledOperation);

            attributes = scrollLeftDelay + "startspace=0 endspace=20";
            text = "^yTransmission Location:   " + location;
            ledOperation = createScrollLeftOperation(attributes, text);
            submitOperation(ledOperation);

            toggleMessageFlag ^= true;
            if (toggleMessageFlag) {
                attributes = scrollLeftDelay + "startspace=20 endspace=20";
                text = "^yEnvironment:    ^oTemperature  ^g273.16K   ^oPressure  ^g678.47 atm";
                ledOperation = createScrollLeftOperation(attributes, text);
                submitOperation(ledOperation);
            } else {
                attributes = scrollLeftDelay + "startspace=20 endspace=20";
                text = "^yRadiation Level:    ^oAlpha  ^g12.56   ^oBeta  ^g67.56   ^oGamma  ^g1634.36";
                ledOperation = createScrollLeftOperation(attributes, text);
                submitOperation(ledOperation);
            }
            // clean to left
            attributes = scrollLeftDelay + "center=true";
            text = "";
            ledOperation = createScrollLeftOperation(attributes, text);
            submitOperation(ledOperation);

            attributes = "delay=50 center=true";
            text = "^pEnd of Transmission";
            ledOperation = createScrollDownOperation(attributes, text);
            submitOperation(ledOperation);

            ledOperation = createSleepOperation("delay=600", emptyText);
            submitOperation(ledOperation);

            attributes = "delay=5 clear=true";
            ledOperation = createPixelsOperation(attributes, emptyText);
            submitOperation(ledOperation);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * @param widthInLEDs
     * @param heightInLEDs
     * @param ledSize
     */
    public LedOperation getNextLedOperation(int widthInLEDs, int heightInLEDs, int ledSize) {
        LedOperation currentLedOperation = getNextOperation();
        if (currentLedOperation == null) {
            processRequestExecuted();
            return null;
        }
        transmissionStarted = true;
        return prepareOperation(currentLedOperation, widthInLEDs, heightInLEDs, ledSize, letters);
    }

    public LedOperation recreateLedOperation(LedOperation currentLedOperation) {
        return prepareOperation(currentLedOperation, widthInLEDs, heightInLEDs, ledSize, letters);
    }

    private void processRequestExecuted() {
        if (!transmissionStarted || transmissionOperationRequest == null) {
            transmissionStarted = false;
            return;
        }
        BasicOperationRequest returnedModelOperationRequest = transmissionOperationRequest;
        transmissionOperationRequest = null;

        ModelTransmissionController modelTransmissiomController = (ModelTransmissionController)
                returnedModelOperationRequest.getSourceBasicOperationExecutionController();
        if (modelTransmissiomController != null) {
            modelTransmissiomController.setRequestExecutedSuccessfully();
        }
        transmissionStarted = false;
    }
}
