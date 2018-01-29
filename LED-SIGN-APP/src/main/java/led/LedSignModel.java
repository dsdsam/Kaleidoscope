package led;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 4/17/13
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class LedSignModel extends JPanel {

    // message
    private final LedSignView.PaintMode paintMode;
    private long currentOperationDelay;


    protected final OperationsProvider operationsProvider;
    private int swidth = 3;        // The width of the space character.

    private int widthInLEDs;
    private int heightInLEDs;
    private int ledSize;

    final AtomicBoolean updatingView = new AtomicBoolean(false);
    volatile boolean readyToRun = false;
    final AtomicReference<LedOperation> currentOperationReference = new AtomicReference<LedOperation>();
    private LedOperation storedCurrentOperation;
    //    private NewLedOperation currentLedOperation;
    private volatile boolean runSign;

    final AtomicBoolean repaint = new AtomicBoolean(false);

    private volatile boolean panelResizing;
    protected volatile boolean panelResized;
    protected volatile boolean paintPanelInEdt;
    protected volatile boolean paintPanelInNotEdt;

    protected synchronized void setPanelResizing(boolean panelResizing) {
        this.panelResizing = panelResizing;
        if (panelResizing) {
            if (storedCurrentOperation == null) {
                storedCurrentOperation = currentOperationReference.get();
                currentOperationReference.set(null);
            }
        }
    }

    /**
     *
     */
    private final Thread animatingThread = new Thread() {
        public void run() {
            while (true) {
                try {

                    if (!runSign) {
                        Thread.sleep(10L);
                        continue;
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                    System.out.println("animatingThread interrupted " + e.toString());
                    runSign = false;
                }

                if (LedSignView.PaintMode.EDT == paintMode) {
                    paintInEDT();
                } else {
//                    paintInNotEDT();
                }
            }
        }
    };

    /**
     *
     */
    private void paintInEDT() {
        try {
            if (panelResized) {
                if (storedCurrentOperation != null) {
                    storedCurrentOperation = operationsProvider.recreateLedOperation(storedCurrentOperation);
                    currentOperationReference.set(storedCurrentOperation);
                    storedCurrentOperation = null;
                }
//                updateSign();
                panelResizing = false;
                paintPanelInEdt = true;
                LedSignModel.this.repaint();
                panelResized = false;
                return;
            }

            if (updatingView.get() || !readyToRun || panelResizing) {
                Thread.sleep(10L);
                return;
            }
            LedOperation currentOperation = currentOperationReference.get();
            if (currentOperation != null) {
                boolean operationExecuted = updateStepCounter(currentOperation);
                if (operationExecuted) {
                    currentOperation = getNextLedOperation(widthInLEDs, heightInLEDs, ledSize);
                    currentOperationReference.set(currentOperation);
                }
            } else {
                currentOperation = getNextLedOperation(widthInLEDs, heightInLEDs, ledSize);
                currentOperationReference.set(currentOperation);
            }
            if (currentOperation == null) {
                Thread.sleep(40L);
                return;
            }
            updatingView.set(true);

            updateSign();
            paintPanelInEdt = true;
            LedSignModel.this.repaint();


            if (currentOperationDelay != 0) {
                Thread.sleep(currentOperationDelay);
//                System.out.println("currentOperationDelay"  + currentOperationDelay);
//                System.out.println("currentOperationDelay"  + currentOperation.toString());
//                Thread.sleep(22L);

            } else {
                Thread.sleep(30L);
            }

            if (currentOperation.ID == LedOperation.IDs.SLEEP) {
//                System.out.println();
            }

        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("animatingThread interrupted " + e.toString());
            runSign = false;
        }
    }

    /**
     *
     */
//    private void paintInNotEDT() {
//        try {
//            if (panelResized) {
//                paintPanelInNotEdt = true;
//                animateSign();
//                panelResized = false;
//                return;
//            }
//            if (updatingView.get() || !readyToRun) {
//                Thread.sleep(10L);
//                return;
//            }
//            LedOperation currentOperation = currentOperationReference.get();
//            if (currentOperation != null) {
//                boolean operationExecuted = updateStepCounter(currentOperation);
//                if (operationExecuted) {
//                    currentOperation = getNextLedOperation(widthInLEDs, heightInLEDs, ledSize);
//                    currentOperationReference.set(currentOperation);
//                }
//            } else {
//                currentOperation = getNextLedOperation(widthInLEDs, heightInLEDs, ledSize);
//                currentOperationReference.set(currentOperation);
//            }
//            if (currentOperation == null) {
//                Thread.sleep(40L);
//                return;
//            }
//            updatingView.set(true);
//
//            paintPanelInNotEdt = true;
//            animateSign();
//
//            if (currentOperationDelay != 0) {
//                Thread.sleep(currentOperationDelay);
//            } else {
//                Thread.sleep(30L);
//            }
//
//            if (currentOperation.ID == LedOperation.IDs.SLEEP) {
//                System.out.println();
//            }
//
//        } catch (Throwable e) {
//            System.out.println("animatingThread interrupted " + e.toString());
//            runSign = false;
//        }
//    }

//    private final Thread animatingThread_ = new Thread() {
//        public void run() {
//
//            while (true) {
//                if (repaint.get()) {
////                       repaint.set(false);
//                    if (LedSignView.PaintMode.EDT != paintMode) {
//                        animateSign();
//                    }
//                }
//                if (!readyToRun) {
//                    continue;
//                }
////            System.out.println("ZZ");
//                try {
//                    if (updatingView.get()) {
//                        Thread.sleep(10L);
//                        continue;
//                    }
//
//
//                    LedOperation currentOperation = currentOperationReference.get();
//                    if (currentOperation != null) {
//                        boolean operationExecuted = updateStepCounter(currentOperation);
////                        System.out.println("Operation executed " + operationExecuted + ", Current Operation: " + currentOperation);
//                        if (operationExecuted) {
//                            currentOperation = getNextLedOperation(widthInLEDs, heightInLEDs, ledSize);
//                            currentOperationReference.set(currentOperation);
//                        }
//                    } else {
//                        currentOperation = getNextLedOperation(widthInLEDs, heightInLEDs, ledSize);
//                        currentOperationReference.set(currentOperation);
//                    }
//                    if (currentOperation == null) {
//                        Thread.sleep(40L);
//                        continue;
//                    }
////                    if (runSign && !inUpdate) {
//                    updatingView.set(true);
////                    System.out.println("starting repaint");
//                    if (LedSignView.PaintMode.EDT == paintMode) {
//                        LedSignModel.this.repaint();
//                    } else {
//                        animateSign();
//                    }
//
//                    long sleepTime = 0;
//
//
//                    if (currentOperationDelay != 0) {
//                        sleepTime = currentOperationDelay;
////                        System.out.println("go sleep  " + currentOperationDelay);
//                        Thread.sleep(currentOperationDelay);
//                    } else {
//                        sleepTime = 30L;
////                        System.out.println("go sleep 2 " + sleepTime);
//                        Thread.sleep(30L);
//                    }
//
//                    if (currentOperation.ID == LedOperation.IDs.SLEEP) {
//                        System.out.println();
//                    }
////                    System.out.println("waked up after " + sleepTime);
//
//                } catch (Throwable e) {
//                    System.out.println("animatingThread interrupted " + e.toString());
//                    runSign = false;
//                }
//
//            }
//        }
//    };

    public void updateSign() {
        return;
    }

    void animateSign() {

    }

    /**
     *
     * @param paintMode
     * @param fontFilePath
     * @param operationsProvider
     */
    LedSignModel(LedSignView.PaintMode paintMode,
                 String fontFilePath, OperationsProvider operationsProvider) {
        this.paintMode = paintMode;
        this.operationsProvider = operationsProvider;
        Letters letters = new Letters(fontFilePath, swidth);
        operationsProvider.setLetters(letters);
        animatingThread.setName("Sign Animating Thread");
        animatingThread.setPriority(Thread.MIN_PRIORITY);
        animatingThread.setDaemon(true);
        animatingThread.start();
    }

    void updateOperationAttributesOnSignRedefined(int widthInLEDs, int heightInLEDs, int ledSize) {
        this.widthInLEDs = widthInLEDs;
        this.heightInLEDs = heightInLEDs;
        this.ledSize = ledSize;
        operationsProvider.updateOperationAttributesOnSignRedefined(widthInLEDs, heightInLEDs, ledSize);
    }

    public void startRunningSign() {
        runSign = true;
    }

    public void stopRunningSign() {
        runSign = false;
    }


    private LedOperation getNextLedOperation(int widthInLEDs, int heightInLEDs, int ledSize) {
//        this.widthInLEDs = widthInLEDs;
//        this.heightInLEDs = heightInLEDs;
//        this.ledSize = ledSize;


        LedOperation currentLedOperation = operationsProvider.getNextLedOperation(widthInLEDs, heightInLEDs, ledSize);
//        System.out.println("Next Current Operation: " + currentLedOperation);
        if (currentLedOperation != null && !(currentLedOperation.ID == LedOperation.IDs.DO)) {

            long operationDelay = currentLedOperation.delay;
//            System.out.println("setting Current delay " + (operationDelay != 0 ? operationDelay : 0));
            currentOperationDelay = operationDelay != 0 ? operationDelay : 0;
//            System.out.println("Current delay " + currentOperationDelay);
        }
        return currentLedOperation;
    }

//    LedOperation getNextOperation() {
//        return operationsProvider.getNextOperation();
//    }

    /**
     * @param ledOperation
     */
    private boolean updateStepCounter(LedOperation ledOperation) {
        boolean operationExecuted = false;
        int currentStepCounter = ledOperation.getStepCounter();
        switch (ledOperation.ID) {
            case DO:
                operationExecuted = true;
                break;
            case APPEAR:
                operationExecuted = true;
                break;
            case SLEEP:
                operationExecuted = true;
                break;
            case BLINK:
//                currentStepCounter = ledOperation.getTimes() * 2;  // on AND off
                currentStepCounter--;
                if (currentStepCounter == 0) {
                    operationExecuted = true;
                }
                break;

            case SCROLL_LEFT:
                currentStepCounter++;
                if (!ledOperation.inRange(currentStepCounter)) {
                    operationExecuted = true;
                    currentStepCounter = 0;
                }
                break;
            case SCROLL_RIGHT:
                currentStepCounter--;
                if (currentStepCounter < 0) {
                    operationExecuted = true;
                }
                break;
            case SCROLL_UP:
                currentStepCounter++;
                if (currentStepCounter >= heightInLEDs + 1) {
                    operationExecuted = true;
                }
                break;
            case SCROLL_DOWN:
                currentStepCounter--;
                if (currentStepCounter < 0) {
                    operationExecuted = true;
                }
                break;
            case PIXELS:

                // This randomizes the "LEDs" for the
                // Pixel function.

//                        pix = new Pixel[w * h];
//
//                        for (i = 0; i < w; i++) {
//                            for (j = 0; j < h; j++) {
//                                pix[h * i + j] = new Pixel();
//                                pix[h * i + j].x = i;
//                                pix[h * i + j].y = j;
//                            }
//                        }
//
//                        // Randomly sort all the LED's so all we have to do
//                        // is draw them in "order" and they come out all pixelly
//                        for (i = 0; i < WIDTH / ledsize * h; i++) {
//                            rand = (int) (Math.random() * (double) (WIDTH / ledsize)
//                                    * (double) h);
//                            temp = pix[i];
//                            pix[i] = pix[rand];
//                            pix[rand] = temp;
//                        }

                if (currentStepCounter >= widthInLEDs * heightInLEDs) {
                    operationExecuted = true;
                }
                break;
            case OVER_LEFT:
                currentStepCounter--;
                if (currentStepCounter < 0) {
                    operationExecuted = true;
                }
                break;
            case OVER_RIGHT:
                currentStepCounter++;
                if (currentStepCounter >= widthInLEDs) {
                    operationExecuted = true;
                }
                break;
            case SCROLL_CENTER:
                currentStepCounter++;
                if (currentStepCounter >= widthInLEDs / 2 && currentStepCounter * 2 > widthInLEDs) {
                    operationExecuted = true;
                }
                break;
            case OVER_CENTER:
                currentStepCounter++;
                if (widthInLEDs < widthInLEDs / 2 + currentStepCounter && currentStepCounter >= widthInLEDs / 2) {
                    operationExecuted = true;
                }
                break;

            case OVER_UP:
                currentStepCounter--;
                if (currentStepCounter < 0) {
                    operationExecuted = true;
                }
                break;
            case OVER_DOWN:
                currentStepCounter++;
                if (currentStepCounter >= heightInLEDs) {
                    operationExecuted = true;
                }
                break;
        }
        if (operationExecuted) {
            ledOperation.setStepCounter(0);
            currentOperationReference.set(null);
        } else {
            ledOperation.setStepCounter(currentStepCounter);
        }
        return operationExecuted;
    }
}

