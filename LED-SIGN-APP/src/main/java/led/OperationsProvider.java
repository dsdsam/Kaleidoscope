package led;


import java.net.URL;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 2/21/13
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */
abstract public class OperationsProvider {

    private static final Logger logger = Logger.getLogger(OperationsProvider.class.getName());

    //
    //   C o n t r o l
    //
    protected LedOperation createBlinkOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.BLINK, text, attributes, "Blink");
    }

    protected LedOperation createSleepOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.SLEEP, text, attributes, "Sleep");
    }

    //
    //   A p p e a r a n c e
    //

    protected LedOperation createAppearOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.APPEAR, text, attributes, "Appear");
    }

    protected LedOperation createPixelsOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.PIXELS, text, attributes, "Pixel");
    }

    protected LedOperation createOverCenterOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.OVER_CENTER, text, attributes, "OverCenter");
    }

    protected LedOperation createScrollOffCenterOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.SCROLL_CENTER, text, attributes, "ScrollCenter");
    }

    //
    //   S c r o l l i n g
    //

    protected LedOperation createScrollUpOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.SCROLL_UP, text, attributes, "ScrollUp");
    }

    protected LedOperation createScrollDownOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.SCROLL_DOWN, text, attributes, "ScrollDown");
    }

    protected LedOperation createScrollLeftOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.SCROLL_LEFT, text, attributes, "ScrollLeft");
    }

    protected LedOperation createScrollRightOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.SCROLL_RIGHT, text, attributes, "ScrollRight");
    }

    //
    //   O v e r
    //

    protected LedOperation createOverUpOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.OVER_UP, text, attributes, "OverUp");
    }

    protected LedOperation createOverDownOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.OVER_DOWN, text, attributes, "OverDown");
    }

    protected LedOperation createOverLeftOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.OVER_LEFT, text, attributes, "OverLeft");
    }

    protected LedOperation createOverRightOperation(String attributes, String text) {
        return createOperation(LedOperation.IDs.OVER_RIGHT, text, attributes, "OverRight");
    }

    /**
     *
     * @param operationType
     * @param text
     * @param attributes
     * @param operation
     */
    private LedOperation createOperation(LedOperation.IDs operationType, String text,
                                            String attributes, String operation) {
        String line = operation + "  " + attributes + " text=" + text;
        LedOperation ledOperation = OperationBuilder.buildOperation(line);
        return ledOperation;
    }

    // just a simple function to see if it is a color code
    boolean isColor(char t) {
        if (t == 'r' || t == 'g' || t == 'b' || t == 'y' || t == 'o' || t == 'p' || t == 'w' || t == 'c')
            return true;
        else
            return false;
    }

    //
    //   I n s t a n c e
    //

    private LinkedBlockingQueue<LedOperation> operationsQueue = new LinkedBlockingQueue<LedOperation>();
    protected Letters letters;
    protected int widthInLEDs;
    protected int heightInLEDs;
    protected int ledSize;

    void setLetters(Letters letters) {
        this.letters = letters;
    }

    void updateOperationAttributesOnSignRedefined(int widthInLEDs, int heightInLEDs, int ledSize) {
//        this.SIGN_WIDTH = SIGN_WIDTH;
        this.widthInLEDs = widthInLEDs;
        this.heightInLEDs = heightInLEDs;
        this.ledSize = ledSize;
    }

    /**
     *
     * @param ledOperation
     */
    protected void submitOperation(LedOperation ledOperation) {
        try {
            operationsQueue.put(ledOperation);
            logger.severe("Submitting LED Operation " + ledOperation);
        } catch (InterruptedException e) {
            logger.severe("Failed to put LED Operation " + ledOperation + " into Operations Queue");
            e.printStackTrace();
        }
    }

    abstract public LedOperation getNextLedOperation(int widthInLEDs, int heightInLEDs, int ledSize);

    /**
     *
     */
    protected LedOperation getNextOperation() {
        LedOperation ledOperation;
        ledOperation = operationsQueue.poll();
        if (ledOperation != null) {
            logger.severe("Retrieved submitted LED Operation " + ledOperation);
        }
        return ledOperation;
    }

    /**
     *
     * @param ledOperation
     * @param widthInLEDs
     * @param heightInLEDs
     * @param ledSize
     * @param letters
     */
    protected LedOperation prepareOperation(LedOperation ledOperation,
                                    int widthInLEDs, int heightInLEDs, int ledSize, Letters letters) {
        LedText message = new LedText(widthInLEDs, heightInLEDs, letters);
        ledOperation.setMessage(message);
        setFrameCounter(ledOperation, widthInLEDs, heightInLEDs);
        return ledOperation;
    }

    public LedOperation recreateLedOperation(LedOperation currentLedOperation){
       return prepareOperation(currentLedOperation, widthInLEDs, heightInLEDs, ledSize, letters);
    }

//    protected void prepareOperation(LedOperation ledOperation,
//                                    int widthInLEDs, int heightInLEDs, int ledSize, Letters letters) {
//        LedText message = new LedText(widthInLEDs, heightInLEDs, letters);
//        ledOperation.setMessage(message);
//        setFrameCounter(ledOperation, widthInLEDs, heightInLEDs);
//    }

    /**
     * @param ledOperation
     * @param widthInLEDs
     * @param heightInLEDs
     */
    private void setFrameCounter(LedOperation ledOperation, int widthInLEDs, int heightInLEDs) {
        int stepCounter = 0;
        switch (ledOperation.ID) {
            case APPEAR:
                stepCounter = 0;
                break;
            case SLEEP:
                stepCounter = 0;
                break;
            case SCROLL_LEFT:
                stepCounter = 0;
                break;
            case SCROLL_RIGHT:
                stepCounter = ledOperation.getMessageLength() - 1;
                break;
            case SCROLL_UP:
                stepCounter = 0;
                break;
            case SCROLL_DOWN:
                stepCounter = heightInLEDs - 1;
                stepCounter = heightInLEDs;
                break;
            case PIXELS:
                stepCounter = 0;

                // This randomizes the "LEDs" for the
                // Pixel function.
                Pixel[] pixels = new Pixel[widthInLEDs * heightInLEDs];

                for (int i = 0; i < widthInLEDs; i++) {
                    for (int j = 0; j < heightInLEDs; j++) {
                        pixels[heightInLEDs * i + j] = new Pixel();
                        pixels[heightInLEDs * i + j].x = i;
                        pixels[heightInLEDs * i + j].y = j;
                    }
                }

                // Randomly sort all the LED's so all we have to do
                // is draw them in "order" and they come out all pixelly
                int max = widthInLEDs * heightInLEDs;
                for (int i = 0; i < max; i++) {
                    int rand = (int) (Math.random() * (double) (widthInLEDs)
                            * (double) heightInLEDs);
                    Pixel temp = pixels[i];
                    pixels[i] = pixels[rand];
                    pixels[rand] = temp;
                }
                ledOperation.pixels = pixels;
                break;

            case BLINK:
                stepCounter = ledOperation.times * 2;  // on AND off
                break;

            case SCROLL_CENTER:
                stepCounter = 0;
                break;
            case OVER_CENTER:
                stepCounter = 0;
                break;
            case OVER_LEFT:
                stepCounter = widthInLEDs;
                break;
            case OVER_RIGHT:
                stepCounter = 0;
                break;
            case OVER_UP:
                stepCounter = heightInLEDs - 1;
                stepCounter = heightInLEDs;
                break;
            case OVER_DOWN:
                stepCounter = 0;
                break;
        }
        ledOperation.setStepCounter(stepCounter);
    }
}

