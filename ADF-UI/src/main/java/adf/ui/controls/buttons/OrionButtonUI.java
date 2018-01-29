package adf.ui.controls.buttons;

import adf.ui.controls.ColorFilter;
import adf.ui.controls.StyleSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.logging.Logger;

/**
 * Created by u0180093 on 10/12/2016.
 */
class OrionButtonUI extends MetalButtonUI {
    static ImageIcon[] sourceImages = null;
    static ImageIcon[] pressedSourceImages = null;

    /*
     * reduce workload by storing images for any
     * Color once we load it the first time.
     */
    protected final static Hashtable buttonImagesHashtable = new Hashtable();
    final static Hashtable pressedImagesHashtable = new Hashtable();
    protected final static Hashtable renderedButtons = new Hashtable();
    final static Hashtable renderedButtonsPressed = new Hashtable();

    // QF-364 "For all buttons/drop-down-boxes where the text is too large to fit,
    // the text will be truncated and suffixed by a '...' - with one exception.
    // On the Provider buttons, the text should shrink to fit the button."
    static public final boolean bDEFAULT_FIT_TO_WIDTH = false; // old value=true
    static public final String sFIT_TEXT_TO_WIDTH_METHOD = "getFitTextToWidth";
    static public final String sSET_FIT_TEXT_TO_WIDTH_METHOD = "setFitTextToWidth";

    /* These rectangles/insets are allocated once for all
     * ButtonUI.paint() calls.  Re-using rectangles rather than
     * allocating them in each paint call substantially reduced the time
     * it took paint to run.  Obviously, this method can't be re-entered.
     */
    protected static Rectangle viewRect = new Rectangle();
    protected static Rectangle textRect = new Rectangle();
    protected static Rectangle iconRect = new Rectangle();
    protected Color cachedBg = null;
    protected int cachedWidth = 0;
    protected int cachedHeight = 0;
    private HashKey renderingHashKey = null;
    protected Color whiteSourceBg = new Color(230, 230, 230);
    protected Color whitePressedSourceBg = new Color(220, 220, 220);
    protected Color sourceBg = whiteSourceBg;
    protected Color pressedSourceBg = whitePressedSourceBg;
    protected boolean fitTextToWidth = bDEFAULT_FIT_TO_WIDTH;

    /**
     * This UI will try to use a JButton.fitTextToWidth() before using
     * this UIs getFitTextToWidth() so that a buton instance can override
     * this UIs behavior.  If JButton.fitTextToWidth() is not defined
     * (which is highly likely, then this UI defers to getFitTextToWidth)
     */
    private Hashtable m_htTextToWidthMethods = new Hashtable();

    /*
     * These are used in the install and uninstall phase
     */
    private boolean ancientBorderPainted;
    private boolean ancientOpaque;
    private boolean ancientFocusPainted;

    public static ComponentUI createUI(JComponent c) {
        return new OrionButtonUI();
    }

    private static void initSourceImages(JComponent c) {
        sourceImages = new ImageIcon[SourceImages.DATA.length];

        for (int i = 0; i < sourceImages.length; i++) {
            int w = SourceImages.DATA[i][SourceImages.DATA[i].length - 1];
            int h = (SourceImages.DATA[i].length - 1) / w;
            Image img = StyleSupport.getImage(w, h, SourceImages.DATA[i]);
            ImageIcon imgIcon = new ImageIcon(img);

            sourceImages[i] = imgIcon;
        }
    }

    private static void initPressedSourceImages(JComponent c) {
        pressedSourceImages = new ImageIcon[PressedSourceImages.DATA.length];

        for (int i = 0; i < pressedSourceImages.length; i++) {
            int w = PressedSourceImages.DATA[i][PressedSourceImages.DATA[i].length - 1];
            int h = (PressedSourceImages.DATA[i].length - 1) / w;
            Image img = StyleSupport.getImage(w, h, PressedSourceImages.DATA[i]);
            ImageIcon imgIcon = new ImageIcon(img);

            pressedSourceImages[i] = imgIcon;
        }
    }

    /**
     * Identical to the method it overrides except that
     * it handles the fittowidth property
     *
     * @param g
     * @param c
     */
    public void paint(Graphics g, JComponent c) {
        paint(g, c, c.getInsets());
    }

    public void paint(Graphics g, JComponent c, Insets i) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = b.getWidth() - (i.right + viewRect.x);
        viewRect.height = b.getHeight() - (i.bottom + viewRect.y);

        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

        Font f = c.getFont();

        g.setFont(f);

        // account for the fact that f could be null if c.getFont() returned null
        f = f == null ? g.getFont() : f;

        // layout the text and icon
        String text = layoutText(g, b, viewRect, textRect, iconRect, f, getFitTextToWidth(b));
        adjustRectangles(text);
        clearTextShiftOffset();

        // perform UI specific press action, e.g. Windows L&F shifts text
        if (model.isArmed() && model.isPressed()) {
            paintButtonPressed(g, b);
        }

        // Paint the Icon
        if (b.getIcon() != null) {
            paintIcon(g, c, iconRect);
        }

        if ((text != null) && !text.equals("")) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);

            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
            }
        }

        if (b.isFocusPainted() && b.hasFocus()) {
            // paint UI specific focus
            paintFocus(g, b, viewRect, textRect, iconRect);
        }
    }

    protected Color getFocusColor() {
        return Color.GRAY;
    }

    // Now we need to provide the text-shrinking behaviour to other UI's which
    // are not the successors of this class, so we extract the shrinking mechanism
    // into a separate method which could be used by any class to provide the
    // consistent behaviour accross the FXAll application.
    static public String layoutText(Graphics g,
                                    AbstractButton b,
                                    Rectangle viewRect,
                                    Rectangle textRect,
                                    Rectangle iconRect,
                                    Font f,
                                    boolean bFitTextToWidth) {
        if ((g == null) || (b == null) || (viewRect == null) || (textRect == null) || (iconRect == null))
            return null;
        FontMetrics fm = g.getFontMetrics();

        // layout the text and icon
        String text = SwingUtilities.layoutCompoundLabel(b, fm, b.getText(), b.getIcon(), b.getVerticalAlignment(),
                b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect,
                iconRect, textRect, (b.getText() == null) ? 0 : b.getIconTextGap());

        if (bFitTextToWidth && (text != null)) {
            if (f == null) {
                Logger.getLogger(b.getClass().getName()).warning("Cannot fit text to width with NULL Font");
            } else {
                Font origFont = f;
                AffineTransform atr = origFont.getTransform();
                double[] fontMatrix = new double[6];

                //keep shrinking font til it fits
                while (!text.equals(b.getText())) {
                    if (b.getText() == null)
                        break;
                    textRect.x = textRect.y = textRect.width = textRect.height = 0;
                    iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

                    // reuse fontMatrix for the sake of performance/
                    fontTransform(atr, 0.95, 0.95, fontMatrix); // for best readability: 0.95, 0.985
                    f = origFont.deriveFont(atr);

                    g.setFont(f);
                    fm = g.getFontMetrics();
                    text = SwingUtilities.layoutCompoundLabel(b, fm, b.getText(), b.getIcon(), b.getVerticalAlignment(),
                            b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                            viewRect, iconRect, textRect, (b.getText() == null) ? 0 : b.getIconTextGap());
                }
            }
        }
        return text;
    }

    //
    static public void fontTransform(AffineTransform atr, double mx, double my, double[] mat) {
        if (atr == null)
            return;
        if (mat == null)
            mat = new double[6];
        atr.getMatrix(mat);
        mat[0] *= mx;
        mat[1] *= mx;
        mat[2] *= my;
        mat[3] *= my;
        mat[4] *= mx;
        mat[5] *= my;
        atr.setTransform(mat[0], mat[1], mat[2], mat[3], mat[4], mat[5]);
    }


    public void update(Graphics g, JComponent c) {
        boolean renderedImagesUpdated = false;

        if ((c.getWidth() != cachedWidth) || (c.getHeight() != cachedHeight)) {
            updateSize(g, (AbstractButton) c);
            renderedImagesUpdated = true;
        }

        if (!c.getBackground().equals(cachedBg)) {
            updateColor((AbstractButton) c);
            renderedImagesUpdated = true;
        }

        if (renderedImagesUpdated) {
            renderingHashKey = new HashKey(cachedWidth, cachedHeight, cachedBg);
        }

        if (renderedButtons.get(renderingHashKey) == null) {
            createRenderedButton((AbstractButton) c);
            createRenderedButtonPressed((AbstractButton) c);
        }

        /*
         * in this ui, contentAreaFilled means we paintButton()
         */
        if (((AbstractButton) c).isContentAreaFilled()) {
            g.setColor(c.getBackground());
            paintButton(g, (AbstractButton) c);
        }

        paint(g, c);
    }

    // ********************************
    //          Install
    // ********************************
    //
    protected Object putTextToWidthMethod(Object arg_Obj, Method arg_oMethod) {
        if ((arg_Obj == null) || (arg_oMethod == null))
            return null;
        Object oldVal = m_htTextToWidthMethods.put(arg_Obj, arg_oMethod);
        return oldVal;
    }

    //
    protected Method getTextToWidthMethod(Object arg_Obj) {
        if (arg_Obj == null)
            return null;
        Method oMeth = (Method) m_htTextToWidthMethods.get(arg_Obj);
        return oMeth;
    }

    //
    public void installUI(JComponent arg_oComp) {
        super.installUI(arg_oComp);
        Method mFitTextToWidth = null;
        try {
            boolean methodIsThere = false;
            for (Method m : arg_oComp.getClass().getMethods()) {
                if (m.getName().equals(sFIT_TEXT_TO_WIDTH_METHOD)) {
                    methodIsThere = true;
                    break;
                }
            }
            if (methodIsThere) {
                mFitTextToWidth = (arg_oComp != null) ? arg_oComp.getClass().getMethod(sFIT_TEXT_TO_WIDTH_METHOD, null) : null;
                putTextToWidthMethod(arg_oComp, mFitTextToWidth);
            }
        } catch (NoSuchMethodException e) {
            Logger.getLogger(arg_oComp.getClass().getName()).severe(e.getMessage());
        }
        String sMeth = (mFitTextToWidth != null) ? mFitTextToWidth.getName() : "";
    }


    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);

        if (sourceImages == null) {
            initSourceImages(b);
        }
        if (pressedSourceImages == null) {
            initPressedSourceImages(b);
        }

        /*
         * preserved the values that existed
         * before this UI was installed
         */
        ancientBorderPainted = b.isBorderPainted();
        ancientOpaque = b.isOpaque();
        ancientFocusPainted = b.isFocusPainted();

        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setFocusPainted(false);
        updateColor(b);
    }

    public void uninstallDefaults(AbstractButton b) {
        super.uninstallDefaults(b);
        b.setBorderPainted(ancientBorderPainted);
        b.setOpaque(ancientOpaque);
        b.setFocusPainted(ancientFocusPainted);
    }

    protected void updateSize(Graphics g, AbstractButton b) {
        cachedWidth = b.getWidth();
        cachedHeight = b.getHeight();
    }

    protected void adjustRectangles(String text) {

    }

    protected void updateColor(AbstractButton b) {
        Color c = b.getBackground();

        /*
         * preserve background
         */
        cachedBg = c;

        if (c == null) {
            return;
        }

        boolean loaded = buttonImagesHashtable.containsKey(cachedBg);

        if (loaded) {
            return;
        }

        ImageIcon[] buttonImages = new ImageIcon[9];
        ImageIcon[] pressedImages = new ImageIcon[11];

        /*
         * setup button images
         */
        ImageFilter filter = new ColorFilter(sourceBg, c);
        ImageProducer imageProducer;

        for (int i = 0; i < buttonImages.length; i++) {
            imageProducer = new FilteredImageSource(sourceImages[i].getImage().getSource(), filter);

            Image image = Toolkit.getDefaultToolkit().createImage(imageProducer);

            buttonImages[i] = new ImageIcon(image);
        }

        ImageFilter pressedFilter = new ColorFilter(sourceBg, pressedSourceBg);

        /*
         * setup pressed button images
         */
        for (int i = 0; i < pressedImages.length; i++) {
            imageProducer = new FilteredImageSource(pressedSourceImages[i].getImage().getSource(), filter);

            Image image = Toolkit.getDefaultToolkit().createImage(imageProducer);

            imageProducer = new FilteredImageSource(image.getSource(), pressedFilter);
            image = Toolkit.getDefaultToolkit().createImage(imageProducer);
            pressedImages[i] = new ImageIcon(image);
        }

        /*
         * preserve these images
         */
        buttonImagesHashtable.put(cachedBg, buttonImages);
        pressedImagesHashtable.put(cachedBg, pressedImages);
    }

    protected void paintButton(Graphics g, AbstractButton b) {
        if (!b.isContentAreaFilled()) {
            return;
        }

        Icon button = (Icon) renderedButtons.get(renderingHashKey);

        button.paintIcon(b, g, 0, 0);
    }

    protected void createRenderedButton(AbstractButton b) {
        int w = cachedWidth;
        int h = cachedHeight;

        if ((w <= 0) || (h <= 0)) {
            return;
        }

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        ImageIcon[] buttonImages = (ImageIcon[]) buttonImagesHashtable.get(cachedBg);

        int x = buttonImages[SourceImages.NE].getIconWidth();
        int y = buttonImages[SourceImages.NE].getIconHeight();

        int innerWidth = w - (2 * x);
        int innerHeight = h - (2 * y);
        int innerWidthPlusX = innerWidth + x;
        int innerHeightPlusY = innerHeight + y;

        buttonImages[SourceImages.NW].paintIcon(b, g, 0, 0);
        buttonImages[SourceImages.NE].paintIcon(b, g, innerWidthPlusX, 0);
        buttonImages[SourceImages.SE].paintIcon(b, g, innerWidthPlusX, innerHeightPlusY);
        buttonImages[SourceImages.SW].paintIcon(b, g, 0, innerHeightPlusY);

        g.drawImage(buttonImages[SourceImages.N].getImage(), x, 0, innerWidth, y, b);
        g.drawImage(buttonImages[SourceImages.S].getImage(), x, innerHeightPlusY, innerWidth, y, b);

        g.drawImage(buttonImages[SourceImages.W].getImage(), 0, y, x, innerHeight, b);
        g.drawImage(buttonImages[SourceImages.E].getImage(), innerWidthPlusX, y, x, innerHeight, b);

        g.drawImage(buttonImages[SourceImages.C].getImage(), x, y, innerWidth, innerHeight, b);

        renderedButtons.put(new HashKey(cachedWidth, cachedHeight, cachedBg), new ImageIcon(image));
    }

    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (!b.isContentAreaFilled()) {
            return;
        }

        Icon buttonPressed = (Icon) renderedButtonsPressed.get(renderingHashKey);

        buttonPressed.paintIcon(b, g, 0, 0);
    }

    protected void createRenderedButtonPressed(AbstractButton b) {
        int w = cachedWidth;
        int h = cachedHeight;

        if ((w <= 0) || (h <= 0)) {
            return;
        }

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        ImageIcon[] pressedImages = (ImageIcon[]) pressedImagesHashtable.get(cachedBg);

        Image centerImage = pressedImages[PressedSourceImages.C].getImage();
        Image topHighlightImage = pressedImages[PressedSourceImages.TH].getImage();
        Image bottomHighlightImage = pressedImages[PressedSourceImages.BH].getImage();

        //setup measurements
        int top = getTopSpace(h);
        int bottom = getBottomSpace(h);
        int innerWidth = w - pressedImages[PressedSourceImages.NW].getIconWidth()
                - pressedImages[PressedSourceImages.NE].getIconWidth();
        int highlightInnerWidth = w - pressedImages[PressedSourceImages.TLH].getIconWidth()
                - pressedImages[PressedSourceImages.TRH].getIconWidth();
        int topHighlightInnerWidth = w - pressedImages[PressedSourceImages.TLH].getIconWidth()
                - pressedImages[PressedSourceImages.TRH].getIconWidth();
        int bottomHighlightInnerWidth = w - pressedImages[PressedSourceImages.BLH].getIconWidth()
                - pressedImages[PressedSourceImages.BRH].getIconWidth();
        int headerHeight = h - pressedImages[PressedSourceImages.NE].getIconWidth()
                - pressedImages[PressedSourceImages.SE].getIconWidth();
        int highlightHeight = h - pressedImages[PressedSourceImages.TRH].getIconHeight()
                - pressedImages[PressedSourceImages.BRH].getIconHeight() - top - bottom;

        //draw edges
        pressedImages[PressedSourceImages.NW].paintIcon(b, g, 0, 0);
        pressedImages[PressedSourceImages.NE].paintIcon(b, g,
                innerWidth + pressedImages[PressedSourceImages.NW].getIconWidth(), 0);
        pressedImages[PressedSourceImages.SE].paintIcon(b, g,
                innerWidth + pressedImages[PressedSourceImages.NW].getIconWidth(),
                headerHeight + pressedImages[PressedSourceImages.NW].getIconHeight());
        pressedImages[PressedSourceImages.SW].paintIcon(b, g, 0,
                headerHeight + pressedImages[PressedSourceImages.NW].getIconHeight());

        //draw top and bottom
        g.drawImage(centerImage, pressedImages[PressedSourceImages.NW].getIconWidth(), 0, innerWidth,
                pressedImages[PressedSourceImages.NW].getIconHeight(), b);
        g.drawImage(centerImage, pressedImages[PressedSourceImages.NW].getIconWidth(),
                pressedImages[PressedSourceImages.NW].getIconHeight() + headerHeight, innerWidth,
                pressedImages[PressedSourceImages.NW].getIconHeight(), b);

        //draw highlight edges
        pressedImages[PressedSourceImages.TLH].paintIcon(b, g, 0, top);
        pressedImages[PressedSourceImages.TRH].paintIcon(b, g,
                pressedImages[PressedSourceImages.TLH].getIconWidth() + highlightInnerWidth, top);
        pressedImages[PressedSourceImages.BLH].paintIcon(b, g, 0,
                top + pressedImages[PressedSourceImages.TLH].getIconHeight() + highlightHeight);
        pressedImages[PressedSourceImages.BRH].paintIcon(b, g,
                pressedImages[PressedSourceImages.TLH].getIconWidth() + highlightInnerWidth,
                top + pressedImages[PressedSourceImages.TLH].getIconHeight() + highlightHeight);

        //draw highlight body
        g.drawImage(topHighlightImage, pressedImages[PressedSourceImages.TLH].getIconWidth(), top,
                topHighlightInnerWidth, pressedImages[PressedSourceImages.TH].getIconHeight(), b);
        g.drawImage(bottomHighlightImage, pressedImages[PressedSourceImages.BLH].getIconWidth(),
                top + pressedImages[PressedSourceImages.TLH].getIconHeight() + highlightHeight, bottomHighlightInnerWidth,
                pressedImages[PressedSourceImages.BH].getIconHeight(), b);

        //draw center
        g.drawImage(centerImage, 0, top + pressedImages[PressedSourceImages.TLH].getIconHeight(), w, highlightHeight, b);

        //draw spaces between highlight and header
        if (top > pressedImages[PressedSourceImages.NW].getIconHeight()) {
            g.drawImage(centerImage, 0, pressedImages[PressedSourceImages.NW].getIconHeight(), w,
                    top - pressedImages[PressedSourceImages.NW].getIconHeight(), b);
            g.drawImage(centerImage, 0,
                    top + pressedImages[PressedSourceImages.TLH].getIconHeight() + highlightHeight
                            + pressedImages[PressedSourceImages.BLH].getIconHeight(), w,
                    top - pressedImages[PressedSourceImages.NW].getIconHeight(), b);
        }

        renderedButtonsPressed.put(new HashKey(cachedWidth, cachedHeight, cachedBg), new ImageIcon(image));
    }

    private int getTopSpace(int size) {
        return (int) Math.floor((Math.max(size - 24, 0) / 4) + 1);
    }

    private int getBottomSpace(int size) {
        return (int) Math.floor((Math.max(size - 24, 0) / 6) + 1);
    }

    //
    //class OrionButtonListener extends BasicButtonListener {
    //    public OrionButtonListener(AbstractButton b) {
    //        super(b);
    //    }
    //
    //    public void focusGained(FocusEvent e) {
    //        System.out.println("focus gained in orionbuttonlistener");
    //        Component c = (Component) e.getSource();
    //        c.repaint();
    //    }
    //
    //    public void focusLost(FocusEvent e) {
    //        System.out.println("focus lost in orionbuttonlistener");
    //        AbstractButton b = (AbstractButton)e.getSource();
    //        b.getModel().setArmed(false);
    //        b.repaint();
    //    }
    //
    //    /*
    //     * BasicButtonListener.mousePressed allows model.setPressed
    //     * and model.setArmed even if the component with focus
    //     * refuses to yield focus.  don't want that;try to avoid it.
    //     */
    //    public void mousePressed(MouseEvent e) {
    //        boolean shouldPerformMousePressed = doesFocusOwnerYield();
    //
    //        if(shouldPerformMousePressed){
    //            super.mousePressed(e);
    //        }
    //    }
    //
    //    /**
    //     * Tries to determine whether the current focus owner will
    //     * yield focus.
    //     * @return
    //     */
    //    private boolean doesFocusOwnerYield() {
    //        boolean shouldPerformMousePressed = true;
    //        Component focusOwner =
    //            KeyboardFocusManager.getCurrentKeyboardFocusManager().
    //                getFocusOwner();
    //
    //        try {
    //            JComponent jFocusOwner = (JComponent)focusOwner;
    //            InputVerifier iv = jFocusOwner.getInputVerifier();
    //
    //            if (iv!=null && !iv.shouldYieldFocus(jFocusOwner)){
    //                shouldPerformMousePressed = false;
    //            }
    //        } catch (Exception e1) { }
    //        return shouldPerformMousePressed;
    //    }
    //
    //    /*
    //     * dup'd for accessibility
    //     */
    //    ActionMap createActionMap(AbstractButton c) {
    //        ActionMap retValue = new javax.swing.plaf.ActionMapUIResource();
    //
    //        retValue.put("pressed", new PressedAction((AbstractButton)c));
    //        retValue.put("released", new ReleasedAction((AbstractButton)c));
    //        return retValue;
    //    }
    //
    //    class PressedAction extends AbstractAction {
    //        AbstractButton b = null;
    //        PressedAction(AbstractButton b) {
    //            this.b = b;
    //        }
    //
    //        /*
    //         * does nothing if the current focus owner will
    //         * not yield focus; we don't want to setArmed or
    //         * setPressed on the button model if we will be
    //         * unable to attain focus.
    //         */
    //        public void executeMenuOperation(ActionEvent e) {
    //            if( ! doesFocusOwnerYield() ){
    //                return;
    //            }
    //
    //            ButtonModel model = b.getModel();
    //            model.setArmed(true);
    //            model.setPressed(true);
    //            if(!b.hasFocus()) {
    //                b.requestFocus();
    //            }
    //        }
    //
    //        public boolean isEnabled() {
    //            if(!b.getModel().isEnabled()) {
    //                return false;
    //            } else {
    //                return true;
    //            }
    //        }
    //    }
    //
    //    /*
    //     * dup'd for accessibility
    //     */
    //    class ReleasedAction extends AbstractAction {
    //        AbstractButton b = null;
    //        ReleasedAction(AbstractButton b) {
    //            this.b = b;
    //        }
    //
    //        public void executeMenuOperation(ActionEvent e) {
    //            ButtonModel model = b.getModel();
    //            model.setPressed(false);
    //            model.setArmed(false);
    //        }
    //
    //        public boolean isEnabled() {
    //            if(!b.getModel().isEnabled()) {
    //                return false;
    //            } else {
    //                return true;
    //            }
    //        }
    //    }
    //
    //
    //}

    /**
     * Returns true if text should be made to
     * fit on the button by shrinking the font.
     * <p>If the Button delegating painting to this
     * UI defines getFitTextToWidth():Boolean then
     * this method defers to that method
     *
     * @return
     */
    public boolean getFitTextToWidth(Object arg_Obj) {
        try {
            Method mFitTextToWidth = getTextToWidthMethod(arg_Obj);
            boolean bResult = (mFitTextToWidth != null) ?
                    ((Boolean) mFitTextToWidth.invoke(arg_Obj, null)).booleanValue() : fitTextToWidth;
            return bResult;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return fitTextToWidth;
    }

    public void setFitTextToWidth(boolean fit) {
        fitTextToWidth = fit;
    }

    protected interface SourceImages {
        final static int[][] DATA = {
                {
                        0x00ffffff, 0x00ffffff, 0xff6f566b, 0xff6f566b, 0xff6f566b, 0xff6f566b, 0xff6f566b, 0x00ffffff,
                        0xff6f566b, 0xffd5d5d5, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xff6f566b, 0xffd5d5d5,
                        0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xff6f566b, 0xffffffff, 0xffffffff,
                        0xffffffff, 0xfff7f7f7, 0xfff7f7f7, 0xfff7f7f7, 0xff6f566b, 0xffffffff, 0xffffffff, 0xfff7f7f7,
                        0xfff7f7f7, 0xffe6e6e6, 0xffe6e6e6, 0xff6f566b, 0xffffffff, 0xffffffff, 0xfff7f7f7, 0xffe6e6e6,
                        0xffe6e6e6, 0xffe6e6e6, 7
                },
                {0xff6f566b, 0xffffffff, 0xffffffff, 0xfff7f7f7, 0xffe6e6e6, 0xffe6e6e6, 1},
                {
                        0xff6f566b, 0xff6f566b, 0xff6f566b, 0xff423d42, 0xff423d42, 0x00ffffff, 0x00ffffff, 0xffffffff,
                        0xffffffff, 0xffffffff, 0xffffffff, 0xff8a7a8a, 0xff232323, 0x00ffffff, 0xffffffff, 0xffffffff,
                        0xffffffff, 0xffe6e6e6, 0xffd5d5d5, 0xff8a7a8a, 0xff423d42, 0xfff7f7f7, 0xfff7f7f7, 0xffe6e6e6,
                        0xffe6e6e6, 0xffd5d5d5, 0xffb3b3b3, 0xff232323, 0xffe6e6e6, 0xffe6e6e6, 0xffe6e6e6, 0xffcdcdcd,
                        0xffc4c4c4, 0xffb3b3b3, 0xff2f342f, 0xffe6e6e6, 0xffe6e6e6, 0xffd5d5d5, 0xffcdcdcd, 0xffc4c4c4,
                        0xffb3b3b3, 0xff31393d, 7
                },
                {0xffe6e6e6, 0xffdedede, 0xffd5d5d5, 0xffcdcdcd, 0xffc4c4c4, 0xffb3b3b3, 0xff2f342f, 7},
                {
                        0xffdedede, 0xffdedede, 0xffd5d5d5, 0xffcdcdcd, 0xffc4c4c4, 0xffb3b3b3, 0xff423d42, 0xffd5d5d5,
                        0xffd5d5d5, 0xffd5d5d5, 0xffcdcdcd, 0xffc4c4c4, 0xffb3b3b3, 0xff423d42, 0xffcdcdcd, 0xffcdcdcd,
                        0xffcdcdcd, 0xffc4c4c4, 0xffb3b3b3, 0xff8d9191, 0xff231b1b, 0xffc4c4c4, 0xffc4c4c4, 0xffbcbcbc,
                        0xffb3b3b3, 0xffb3b3b3, 0xff8d9191, 0xff1b1b1b, 0xffa2a2a2, 0xffa2a2a2, 0xffa2a2a2, 0xff9a9a9a,
                        0xff8d9191, 0xff1b1b1b, 0x00ffffff, 0xff423d42, 0xff423d42, 0xff423d42, 0xff231b1b, 0xff1b1b1b,
                        0x00ffffff, 0x00ffffff, 7
                },
                {0xffdedede, 0xffd5d5d5, 0xffcdcdcd, 0xffc4c4c4, 0xffa2a2a2, 0xff423d42, 1},
                {
                        0xff6f566b, 0xffffffff, 0xfff7f7f7, 0xfff7f7f7, 0xffe6e6e6, 0xffe6e6e6, 0xffdedede, 0xff423d42,
                        0xffffffff, 0xfff7f7f7, 0xffe6e6e6, 0xffe6e6e6, 0xffd5d5d5, 0xffd5d5d5, 0xff423d42, 0xffe6e6e6,
                        0xffe6e6e6, 0xffdedede, 0xffd5d5d5, 0xffd5d5d5, 0xffcdcdcd, 0xff2f342f, 0xff9a9a9a, 0xffd5d5d5,
                        0xffc4c4c4, 0xffc4c4c4, 0xffc4c4c4, 0xffc4c4c4, 0x00ffffff, 0xff2f342f, 0xff7b7274, 0xffa2a2a2,
                        0xffb3b3b3, 0xffa2a2a2, 0xffa2a2a2, 0x00ffffff, 0x00ffffff, 0xff2f342f, 0xff2f342f, 0xff423d42,
                        0xff423d42, 0xff423d42, 7
                },
                {0xff6f566b, 0xffffffff, 0xffffffff, 0xfff7f7f7, 0xffe6e6e6, 0xffe6e6e6, 0xffe6e6e6, 7},
                {0xffe6e6e6, 1},
        };
        int NW = 0;
        int N = 1;
        int NE = 2;
        int E = 3;
        int SE = 4;
        int S = 5;
        int SW = 6;
        int W = 7;
        int C = 8;
    }

    interface PressedSourceImages {
        final static int[][] DATA = {
                {
                        0x00ffffff, 0x00ffffff, 0xffe0e0e0, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0x00ffffff, 0xffe0e0e0,
                        0xffe0e0e0, 3
                },
                {
                        0xffe0e0e0, 0x00ffffff, 0x00ffffff, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffe0e0e0, 0xffe0e0e0,
                        0x00ffffff, 3
                },
                {
                        0xffdedede, 0xffe0e0e0, 0xffe0e0e0, 0xffe1e1e1, 0xffe2e2e2, 0xffe3e3e3, 0xffe4e4e4, 0xffe6e6e6,
                        0xffe6e6e6, 0xffe6e6e6, 0xffe0e0e0, 0xffe2e2e2, 0xffe3e3e3, 0xffe6e6e6, 0xffe9e9e9, 0xffebebeb,
                        0xffededed, 0xffeeeeee, 0xffefefef, 0xffefefef, 0xffe2e2e2, 0xffe5e5e5, 0xffe9e9e9, 0xffededed,
                        0xfff0f0f0, 0xfff3f3f3, 0xfff7f7f7, 0xfffafafa, 0xfffafafa, 0xfffbfbfb, 0xffe3e3e3, 0xffe8e8e8,
                        0xffebebeb, 0xffefefef, 0xfff3f3f3, 0xfff7f7f7, 0xfffbfbfb, 0xfffefefe, 0xfffefefe, 0xffffffff,
                        0xffe2e2e2, 0xffe5e5e5, 0xffe8e8e8, 0xffebebeb, 0xffeeeeee, 0xfff1f1f1, 0xfff4f4f4, 0xfff6f6f6,
                        0xfff7f7f7, 0xfff7f7f7, 0xffdedede, 0xffdfdfdf, 0xffe0e0e0, 0xffe1e1e1, 0xffe4e4e4, 0xffe4e4e4,
                        0xffe5e5e5, 0xffe6e6e6, 0xffe7e7e7, 0xffe7e7e7, 10
                },
                {0xffe6e6e6, 0xffefefef, 0xfffafafa, 0xfffefefe, 0xfff7f7f7, 0xffe7e7e7, 1},
                {
                        0xffe6e6e6, 0xffe6e6e6, 0xffe5e5e5, 0xffe5e5e5, 0xffe3e3e3, 0xffe1e1e1, 0xffe1e1e1, 0xffe0e0e0,
                        0xffe0e0e0, 0xffdfdfdf, 0xfff0f0f0, 0xffefefef, 0xffededed, 0xffededed, 0xffebebeb, 0xffe9e9e9,
                        0xffe8e8e8, 0xffe4e4e4, 0xffe2e2e2, 0xffe1e1e1, 0xfffcfcfc, 0xfffbfbfb, 0xfffafafa, 0xfff8f8f8,
                        0xfff4f4f4, 0xfff0f0f0, 0xffeeeeee, 0xffe9e9e9, 0xffe5e5e5, 0xffe4e4e4, 0xffffffff, 0xffffffff,
                        0xffffffff, 0xfffefefe, 0xfff9f9f9, 0xfff4f4f4, 0xffefefef, 0xffebebeb, 0xffe8e8e8, 0xffe6e6e6,
                        0xfff8f8f8, 0xfff7f7f7, 0xfff7f7f7, 0xfff5f5f5, 0xfff1f1f1, 0xffeeeeee, 0xffececec, 0xffe8e8e8,
                        0xffe4e4e4, 0xffe3e3e3, 0xffe4e4e4, 0xffe4e4e4, 0xffe4e4e4, 0xffe4e4e4, 0xffe3e3e3, 0xffe2e2e2,
                        0xffe2e2e2, 0xffe0e0e0, 0xffe0e0e0, 0xffdfdfdf, 10
                },
                {0xffdddddd, 1},
                {
                        0xffdedede, 0xffdfdfdf, 0xffdedede, 0xffdfdfdf, 0xffdfdfdf, 0xffdfdfdf, 0xffdfdfdf, 0xffe0e0e0,
                        0xffdfdfdf, 0xffdfdfdf, 0xffe0e0e0, 0xffe0e0e0, 0xffdfdfdf, 0xffe1e1e1, 0xffe3e3e3, 0xffe3e3e3,
                        0xffe4e4e4, 0xffe6e6e6, 0xffe6e6e6, 0xffe6e6e6, 0xffe0e0e0, 0xffe2e2e2, 0xffe4e4e4, 0xffe6e6e6,
                        0xffe8e8e8, 0xffebebeb, 0xffededed, 0xffefefef, 0xfff0f0f0, 0xfff0f0f0, 0xffdddddd, 0xffdddddd,
                        0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 10
                },
                {0xffe0e0e0, 0xffe6e6e6, 0xffefefef, 0xffdddddd, 1},
                {
                        0xffdedede, 0xffdfdfdf, 0xffe0e0e0, 0xffdfdfdf, 0xffdedede, 0xffdedede, 0xffdedede, 0xffdfdfdf,
                        0xffdfdfdf, 0xffdfdfdf, 0xffe6e6e6, 0xffe6e6e6, 0xffe6e6e6, 0xffe5e5e5, 0xffe4e4e4, 0xffe3e3e3,
                        0xffe2e2e2, 0xffe1e1e1, 0xffe0e0e0, 0xffdfdfdf, 0xffefefef, 0xffefefef, 0xffefefef, 0xffeeeeee,
                        0xffebebeb, 0xffe9e9e9, 0xffe7e7e7, 0xffe4e4e4, 0xffe2e2e2, 0xffe1e1e1, 0xffdddddd, 0xffdddddd,
                        0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffdddddd, 10
                },
                {
                        0xffe1e1e1, 0xffe1e1e1, 0xffe1e1e1, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0x00ffffff, 0xffe1e1e1,
                        0xffe1e1e1, 3
                },
                {
                        0xffe1e1e1, 0xffe1e1e1, 0xffe1e1e1, 0xffdddddd, 0xffdddddd, 0xffdddddd, 0xffe1e1e1, 0xffe1e1e1,
                        0x00ffffff, 3
                },
        };
        int NW = 0;
        int NE = 1;
        int TLH = 2;
        int TH = 3;
        int TRH = 4;
        int C = 5;
        int BLH = 6;
        int BH = 7;
        int BRH = 8;
        int SW = 9;
        int SE = 10;
    }

    protected class HashKey {
        final static int seed = 23;
        final static int prime = 37;
        final int width;
        final int height;
        final Color color;

        public HashKey(int w, int h, Color bg) {
            if (bg == null) {
                throw new IllegalArgumentException("color can't be null");
            }

            width = w;
            height = h;
            color = bg;
        }

        public boolean equals(Object obj) {
            boolean isEqual = false;

            if ((obj == null) || !(obj instanceof HashKey)) {
                return false;
            }

            HashKey o = (HashKey) obj;

            if ((width == o.width) && (height == o.height) && color.equals(o.color)) {
                isEqual = true;
            }

            return isEqual;
        }

        public int hashCode() {
            return seed + (prime * seed) + width + (prime * seed) + height + (prime * seed) + color.hashCode();
        }
    }
}
