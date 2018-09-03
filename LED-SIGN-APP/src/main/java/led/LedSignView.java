package led;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 4/17/13
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class LedSignView extends LedSignModel {

    static enum PaintMode {EDT, NOT_EDT}

    private static int SIGN_WIDTH = 400;
    private static int SIGN_HEIGHT = 70;
    private static int HEIGHT_IN_LEDS = 10;

    private Color signBackground = new Color(0, 0, 0);
    private Color highlight = new Color(100, 100, 100);
    private static Color[] COLOR_PALETTE = new Color[9]; // The array of possible colors

    static {
        COLOR_PALETTE[0] = new Color(60, 60, 60);    // off color
        COLOR_PALETTE[1] = new Color(255, 0, 0);     // Default red
        COLOR_PALETTE[1] = new Color(0xED1C24);     // Default red
        COLOR_PALETTE[2] = new Color(100, 255, 0);   // green
        COLOR_PALETTE[2] = new Color(0x96FE16);   // green
        COLOR_PALETTE[3] = new Color(0, 130, 255);   // blue
        COLOR_PALETTE[4] = new Color(255, 255, 0);   // yellow
        COLOR_PALETTE[5] = new Color(255, 125, 50);   // orange
        COLOR_PALETTE[6] = new Color(255, 0, 255);   // purple
        COLOR_PALETTE[7] = new Color(255, 255, 255); // white
        COLOR_PALETTE[8] = new Color(0, 255, 255);   // cyan
        COLOR_PALETTE[8] = new Color(0x99D9EA);
    }

    // panel
    private int offset = 4; // The offset for the sign from theupper left

    // sign
    private int leftMargin;
    private int leftMarginPlusOffset;
    private int widthInLEDs, heightInLEDs;


    private int ledSize;
    private Image pixMapImg, offImage;
    private Graphics pixelMapGraphics, offMapGraphics; // Graphics for the pixMaps
    private final LedSignView.PaintMode paintMode;
    private boolean presentClearLEDPanel;

    boolean updateStep;

    private LedOperation prevOperation;

    private ComponentListener componentListener = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            setPanelResizing(true);
            readyToRun = false;
            setSizeAttributes();
            panelResized = true;
        }

        @Override
        public void componentMoved(ComponentEvent e) {
//            System.out.println("LedSignView component Moved");
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }
    };

    /**
     *
     */
//    private synchronized
    void setSizeAttributes() {

        Rectangle bounds = getBounds();
        if (bounds.width == 0 || bounds.height == 0) {
            return;
        }

        int width = bounds.width;
        int height = bounds.height;

//        System.out.println("  " + SIGN_WIDTH + "  " + SIGN_HEIGHT);
//        System.out.println("  " + bounds.x + "  " + bounds.y + "  " + width + "  " + height);

//        stop();
        trimSize(width, height);

        // preparing images

        pixMapImg = createImage(SIGN_WIDTH, SIGN_HEIGHT);
        offImage = createImage(SIGN_WIDTH, SIGN_HEIGHT);  // A copy of the sign with all the LEDs off

        pixelMapGraphics = pixMapImg.getGraphics();
        offMapGraphics = offImage.getGraphics();

        pixelMapGraphics.setColor(signBackground);
        pixelMapGraphics.fillRect(0, 0, SIGN_WIDTH, SIGN_HEIGHT);

        offMapGraphics.setColor(signBackground);
        offMapGraphics.fillRect(0, 0, SIGN_WIDTH, SIGN_HEIGHT);

        for (int i = 0; i < SIGN_HEIGHT; i += ledSize) {
            for (int j = 0; j < SIGN_WIDTH; j += ledSize) {
                drawMatrixElement(pixelMapGraphics, j, i, false, 1);
                drawMatrixElement(offMapGraphics, j, i, false, 1);
            }
        }

        // preparation done, setting flags
        updateOperationAttributesOnSignRedefined(widthInLEDs, heightInLEDs, ledSize);
        presentClearLEDPanel = true;
    }

    /**
     * @param width
     * @param height
     */
    private void trimSize(int width, int height) {

        int newHeight;
        ledSize = -1;

        newHeight = HEIGHT_IN_LEDS * 2 + 1 + 2 * offset;
        if (newHeight <= height) {
            ledSize = 2;
        }

        newHeight = HEIGHT_IN_LEDS * 3 + 1 + 2 * offset;
        if (newHeight <= height) {
            ledSize = 3;
        }

        newHeight = HEIGHT_IN_LEDS * 4 + 1 + 2 * offset;
        if (newHeight <= height) {
            ledSize = 4;
        }

        newHeight = HEIGHT_IN_LEDS * 5 + 1 + 2 * offset;
        if (newHeight <= height) {
            ledSize = 5;
        }

        if (ledSize <= 0) {
            return;
        }

        SIGN_WIDTH = ((((width - 2 * offset)) / ledSize) * ledSize);
        if (SIGN_WIDTH / ledSize % 2 == 1) {
            SIGN_WIDTH -= ledSize; // It must be even!!!
        }

        SIGN_HEIGHT = HEIGHT_IN_LEDS * ledSize + 1;

        widthInLEDs = (SIGN_WIDTH) / ledSize;
        heightInLEDs = (SIGN_HEIGHT / ledSize) - 1;
        leftMargin = (width - SIGN_WIDTH - 2 * offset) / 2;
        leftMarginPlusOffset = leftMargin + offset;

//        System.out.println(" ledsize = " + ledSize);
//        System.out.println(" SIGN_HEIGHT = " + SIGN_HEIGHT);
//        System.out.println(" SIGN_WIDTH = " + SIGN_WIDTH);
//        System.out.println(" ledWidth = " + widthInLEDs);
//        System.out.println(" ledHeight = " + heightInLEDs);
//        System.out.println(" leftMargin = " + leftMargin);
    }


    /**
     * @param paintMode
     * @param fontFilePath
     * @param operationsProvider
     * @param hasBorder
     */
    LedSignView(LedSignView.PaintMode paintMode,
                String fontFilePath, OperationsProvider operationsProvider, boolean hasBorder) {
        super(paintMode, fontFilePath, operationsProvider);
        this.paintMode = paintMode;

        setBackground(new Color(155, 179, 201));
        setBackground(Color.BLACK);
        setOpaque(true);

        if (hasBorder) {
            Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createBevelBorder(BevelBorder.RAISED,
                            Color.LIGHT_GRAY, Color.LIGHT_GRAY,
                            Color.GRAY, Color.GRAY),
                    BorderFactory.createBevelBorder(BevelBorder.LOWERED,
                            Color.LIGHT_GRAY, Color.LIGHT_GRAY,
                            Color.GRAY, Color.GRAY)
            );
            setBorder(border);
        } else {
            offset = 0;
        }

        addComponentListener(componentListener);
    }

    @Override
    void animateSign() {
        Graphics g = getGraphics();
        if (g == null) {
            return;
        }
        if (!paintPanelInNotEdt) {
            return;
        }

        Dimension d = getSize();

        Border border = getBorder();
        if (border != null) {
            border.paintBorder(this, g, 0, 0, d.width, d.height);
        }

        g.setColor(Color.BLACK);
        g.fillRect(offset, offset, 10, d.height - (2 * offset));
        g.fillRect(d.width - offset - 10, offset, 10, d.height - (2 * offset));

        if (presentClearLEDPanel) {
            g.drawImage(offImage, leftMarginPlusOffset, offset, null);  // Turn all the LEDs off
            presentClearLEDPanel = false;
            readyToRun = true;
            updatingView.set(false);
            return;
        }


        if (currentOperationReference.get() == null) {
            g.drawImage(offImage, leftMarginPlusOffset, offset, null);
            readyToRun = true;
            updatingView.set(false);
            return;
        }
        updateSign();
        paintComponents(g);
        g.dispose();

        updatingView.set(false);
        paintPanelInNotEdt = false;
    }

    /**
     * @param gr
     */
    public void paint(Graphics gr) {
//        System.out.println("P a i n t   C a l l e d");
        if (!paintPanelInEdt) {
            gr.drawImage(offImage, leftMarginPlusOffset, offset, this);
            return;
        }
//        System.out.println(" P A I N T I N G ");
        Dimension d = getSize();

        Border border = getBorder();
        if (border != null) {
            border.paintBorder(this, gr, 0, 0, d.width, d.height);
        }

        gr.setColor(Color.BLACK);
        gr.fillRect(offset, offset, 10, d.height - (2 * offset));
        gr.fillRect(d.width - offset - 10, offset, 10, d.height - (2 * offset));

        if (presentClearLEDPanel) {
            gr.drawImage(offImage, leftMarginPlusOffset, offset, null);  // Turn all the LEDs off
            presentClearLEDPanel = false;
            readyToRun = true;
            updatingView.set(false);
            return;
        }

        if (currentOperationReference.get() == null) {
            gr.drawImage(offImage, leftMarginPlusOffset, offset, null);
            readyToRun = true;
            updatingView.set(false);
            return;
        }
        if (!updateStep) {
            gr.drawImage(offImage, leftMarginPlusOffset, offset, this);
        } else {
            gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
            updateStep = false;
        }
        updatingView.set(false);
        paintPanelInEdt = false;
    }


    @Override
    public void updateSign() {
        updateStep = updateSign2();
    }

    /**
     * This procedure contains all the different transitions
     * Each transition does one iteration and returns to the
     * "run" procedure to use its delay.  This also allows
     * the applet to be redrawn (if needed) more quickly.
     */
    public boolean updateSign2() {

        LedOperation currentOperation = currentOperationReference.get();
        LedOperation.IDs currentOperationId = currentOperation.ID;

//        System.out.println("updateSign: currentOperation = " + currentOperationId + ",  " + leftMarginPlusOffset);
        switch (currentOperationId) {
//            case CLEAR:
//                gr.drawImage(offImage, leftMarginPlusOffset, offset, null);  // Turn all the LEDs off
//                break;
            case DO:

                break;

            case APPEAR:
                if (!currentOperation.hasText()) {
                    return false;

                } else {
                    for (int x = 0; x < widthInLEDs; x++) {
                        for (int y = 0; y < heightInLEDs; y++) {
                            drawLED(x * ledSize, y * ledSize, currentOperation.getLED(x, y),
                                    currentOperation.getColor(x), pixelMapGraphics);
                        }
                    }
//                    gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, null);
                    return true;
                }
//                break;

            case SLEEP:
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, null);
                return true;
//                break;

            case SCROLL_LEFT:
                pixelMapGraphics.copyArea(ledSize, 0, SIGN_WIDTH - ledSize, SIGN_HEIGHT, -ledSize, 0);

                for (int i = 0; i < SIGN_HEIGHT; i += ledSize) {
                    drawLED(SIGN_WIDTH - ledSize, i, currentOperation.getLED(currentOperation.getStepCounter(), (i / ledSize)),
                            currentOperation.getColor(currentOperation.getStepCounter()), pixelMapGraphics);
                }

//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, null);
                return true;
//                break;

            case SCROLL_RIGHT:
//                System.out.println("updateSign: currentOperation = " + currentOperationId + ", framesCounter " + currentOperation.getStepCounter());
                pixelMapGraphics.copyArea(0, 0, SIGN_WIDTH - ledSize, SIGN_HEIGHT, ledSize, 0);

                for (int y = 0; y < SIGN_HEIGHT; y += ledSize) {
                    drawLED(0, y, currentOperation.getLED(currentOperation.stepCounter, y / ledSize),
                            currentOperation.getColor(currentOperation.stepCounter), pixelMapGraphics);
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;

            case SCROLL_UP:
                //  pixmap.copyArea(0,ledsize,WIDTH,HEIGHT-ledsize,0,-ledsize);
                pixelMapGraphics.copyArea(0, ledSize, SIGN_WIDTH, SIGN_HEIGHT, 0, -ledSize);
                if (currentOperation.stepCounter == 0) {
                    for (int x = 0; x < SIGN_WIDTH; x += ledSize) {
                        drawLED(x, SIGN_HEIGHT - ledSize - ledSize - 1, false, 1, pixelMapGraphics);
                    }
                } else {
                    for (int x = 0; x < SIGN_WIDTH; x += ledSize)
                        if (currentOperation.inRange(x / ledSize)) {
                            drawLED(x, SIGN_HEIGHT - ledSize - ledSize - 1,
                                    currentOperation.getLED(x / ledSize, currentOperation.stepCounter - 1),
                                    currentOperation.getColor(x / ledSize), pixelMapGraphics);
                        } else {
                            drawLED(x, SIGN_HEIGHT - ledSize, false, 1, pixelMapGraphics);
                        }
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;

            case SCROLL_DOWN:
                pixelMapGraphics.copyArea(0, 0, SIGN_WIDTH, SIGN_HEIGHT - ledSize, 0, ledSize);

                for (int x = 0; x < SIGN_WIDTH; x += ledSize) {
                    if (currentOperation.inRange(x / ledSize)) {
                        drawLED(x, 0, currentOperation.getLED(x / ledSize, currentOperation.stepCounter),
                                currentOperation.getColor(x / ledSize), pixelMapGraphics);
                    } else {
                        drawLED(x, 0, false, 1, pixelMapGraphics);
                    }
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;

            case PIXELS:
                int nTimes = currentOperation.stepCounter + currentOperation.times;
                Pixel[] pix = currentOperation.pixels;
                while (currentOperation.stepCounter < SIGN_WIDTH / ledSize * heightInLEDs && currentOperation.stepCounter < nTimes) {
                    if (currentOperation.inRange(pix[currentOperation.stepCounter].x)) {
                        drawLED(pix[currentOperation.stepCounter].x * ledSize, pix[currentOperation.stepCounter].y * ledSize,
                                currentOperation.getLED(pix[currentOperation.stepCounter].x, pix[currentOperation.stepCounter].y),
                                currentOperation.getColor(pix[currentOperation.stepCounter].x), pixelMapGraphics);
                    } else {
                        drawLED(pix[currentOperation.stepCounter].x * ledSize, pix[currentOperation.stepCounter].y * ledSize, false, 1, pixelMapGraphics);
                    }
                    currentOperation.stepCounter++;
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;

            case BLINK:
                if (currentOperation.stepCounter % 2 == 0) {
//                        System.out.println("######################## blinck off" + framesCounter);
//                    gr.drawImage(offImage, leftMarginPlusOffset, offset, this);
                    return false;
                } else {
//                        System.out.println("######################## blinck on" + framesCounter);
//                    gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                    return true;
                }

//                break;

            case OVER_RIGHT:
                if (currentOperation.inRange(currentOperation.stepCounter)) {
                    for (int y = 0; y < heightInLEDs; y++) {
                        drawLED(currentOperation.stepCounter * ledSize, y * ledSize, currentOperation.getLED(currentOperation.stepCounter, y),
                                currentOperation.getColor(currentOperation.stepCounter), pixelMapGraphics);
                    }
                } else {
                    for (int y = 0; y < heightInLEDs; y++) {
                        drawLED(currentOperation.stepCounter * ledSize, y * ledSize, false, 1, pixelMapGraphics);
                    }
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;

            case SCROLL_CENTER:
                // The right side
                if (widthInLEDs >= currentOperation.stepCounter * 2) {
                    pixelMapGraphics.copyArea(SIGN_WIDTH / 2, 0, SIGN_WIDTH / 2 - ledSize, SIGN_HEIGHT, ledSize, 0);

                    for (int y = 0; y < heightInLEDs; y++) {
                        if (currentOperation.inRange(widthInLEDs - currentOperation.stepCounter)) {
//                            System.out.println(currentOperation.getLED(widthInLEDs - currentOperation.stepCounter, y));
                            drawLED(SIGN_WIDTH / 2, y * ledSize,
                                    currentOperation.getLED(widthInLEDs - currentOperation.stepCounter, y),
                                    currentOperation.getColor(widthInLEDs - currentOperation.stepCounter), pixelMapGraphics);
                        } else {
                            drawLED(SIGN_WIDTH / 2, y * ledSize, false, 1, pixelMapGraphics);
                        }
                    }
                }

                if (currentOperation.stepCounter < widthInLEDs / 2) {
                    pixelMapGraphics.copyArea(ledSize, 0, SIGN_WIDTH / 2 - ledSize, SIGN_HEIGHT, -ledSize, 0);
                    for (int y = 0; y < heightInLEDs; y++) {
                        if (currentOperation.inRange(currentOperation.stepCounter)) {
                            drawLED(SIGN_WIDTH / 2 - ledSize, y * ledSize,
                                    currentOperation.getLED(currentOperation.stepCounter, y),
                                    currentOperation.getColor(currentOperation.stepCounter), pixelMapGraphics);
                        } else {
                            drawLED(SIGN_WIDTH / 2 - ledSize, y * ledSize, false, 1, pixelMapGraphics);
                        }
                    }
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;

            case OVER_CENTER:
                // The right side
                if (widthInLEDs >= currentOperation.stepCounter + widthInLEDs / 2) {
                    for (int y = 0; y < heightInLEDs; y++) {
                        if (currentOperation.inRange(widthInLEDs / 2 + currentOperation.stepCounter + 1)) {
                            drawLED(SIGN_WIDTH / 2 + currentOperation.stepCounter * ledSize + ledSize, y * ledSize,
                                    currentOperation.getLED(widthInLEDs / 2 + currentOperation.stepCounter + 1, y),
                                    currentOperation.getColor(widthInLEDs / 2 + currentOperation.stepCounter + 1), pixelMapGraphics);
                        } else {
                            drawLED(SIGN_WIDTH / 2 + currentOperation.stepCounter * ledSize + ledSize, y * ledSize, false, 1, pixelMapGraphics);
                        }
                    }
                }

                if (currentOperation.stepCounter < widthInLEDs / 2) {
                    for (int y = 0; y < heightInLEDs; y++) {
                        if (currentOperation.inRange(widthInLEDs / 2 - currentOperation.stepCounter)) {
                            drawLED(SIGN_WIDTH / 2 - currentOperation.stepCounter * ledSize, y * ledSize,
                                    currentOperation.getLED(widthInLEDs / 2 - currentOperation.stepCounter, y),
                                    currentOperation.getColor(widthInLEDs / 2 - currentOperation.stepCounter), pixelMapGraphics);
                        } else {
                            drawLED(SIGN_WIDTH / 2 - currentOperation.stepCounter * ledSize, y * ledSize, false, 1, pixelMapGraphics);
                        }
                    }
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;

            case OVER_LEFT:
                if (currentOperation.inRange(currentOperation.stepCounter)) {
                    for (int y = 0; y < heightInLEDs; y++) {
                        drawLED(currentOperation.stepCounter * ledSize, y * ledSize,
                                currentOperation.getLED(currentOperation.stepCounter, y),
                                currentOperation.getColor(currentOperation.stepCounter), pixelMapGraphics);
                    }
                } else {
                    for (int y = 0; y < heightInLEDs; y++) {
                        drawLED(currentOperation.stepCounter * ledSize, y * ledSize, false, 1, pixelMapGraphics);
                    }
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;

            case OVER_UP:
                for (int x = 0; x < widthInLEDs; x++) {
                    if (currentOperation.inRange(x)) {
                        drawLED(x * ledSize, currentOperation.stepCounter * ledSize,
                                currentOperation.getLED(x, currentOperation.stepCounter),
                                currentOperation.getColor(x), pixelMapGraphics);
                    } else {
                        drawLED(x * ledSize, currentOperation.stepCounter * ledSize, false, 1, pixelMapGraphics);
                    }
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;

            case OVER_DOWN:
                for (int x = 0; x < widthInLEDs; x++) {
                    if (currentOperation.inRange(x)) {
                        drawLED(x * ledSize, currentOperation.stepCounter * ledSize,
                                currentOperation.getLED(x, currentOperation.stepCounter),
                                currentOperation.getColor(x), pixelMapGraphics);
                    } else {
                        drawLED(x * ledSize, currentOperation.stepCounter * ledSize, false, 1, pixelMapGraphics);
                    }
                }
//                gr.drawImage(pixMapImg, leftMarginPlusOffset, offset, this);
                return true;
//                break;
        }


        return false;
    }

    /**
     * draw a pretty little LED
     *
     * @param x
     * @param y
     * @param on
     * @param col
     * @param gr
     */
    private void drawLED(int x, int y, boolean on, int col, Graphics gr) {
        y = y + ledSize;
        drawMatrixElement(gr, x, y, on, col);
    }

    /**
     * draw a pretty little LED
     *
     * @param gr
     * @param x
     * @param y
     * @param on
     * @param col
     */
    private void drawMatrixElement(Graphics gr, int x, int y, boolean on, int col) {
        y = y + 1;

        if (on) {
            gr.setColor(COLOR_PALETTE[col]);
        } else { // its off
            gr.setColor(COLOR_PALETTE[0]);
        }

        switch (ledSize) {
            case 2:    // Just a pixel
                gr.drawLine(x, y, x, y);
                break;

            case 3:    // A 2x2 rectangle
                gr.fillRect(x, y, 2, 2);
                break;

            case 4:   // A 3x3 '+'
                gr.drawLine(x, y + 1, x + 2, y + 1);
                gr.drawLine(x + 1, y, x + 1, y + 2);
                break;

            case 5:   // The original size seen in previous versions
                gr.fillRect(x + 1, y, 2, 4);
                gr.fillRect(x, y + 1, 4, 2);
                break;
        }

        if (ledSize == 5 && !on) {
            gr.setColor(highlight);
            gr.drawLine(x + 1, y + 1, x + 1, y + 1);  // the cool little highlight
        }
    }
}
