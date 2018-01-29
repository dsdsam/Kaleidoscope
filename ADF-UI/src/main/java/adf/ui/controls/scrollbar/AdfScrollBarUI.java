package adf.ui.controls.scrollbar;

import adf.ui.controls.ColorFilter;
import adf.ui.controls.StyleSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class AdfScrollBarUI extends BasicScrollBarUI {

    final static int TRACK_WIDTH = 16;

    /**
     * Buttons on the scrollbar can be oriented such that they
     * are at either end of the track or flush together at one end.
     *
     * @see #getButtonOrientation()
     */
    public final static int BUTTONS_AT_ENDS = 0;

    /**
     * Buttons on the scrollbar can be oriented such that they
     * are at either end of the track or flush together at one end.
     *
     * @see #getButtonOrientation()
     */
    public final static int BUTTONS_TOGETHER = 1;

    /**
     * Default location of buttons
     */
    private static final int DEFAULT_BUTTON_ORIENTATION = BUTTONS_AT_ENDS;

    /*
     * The last int in the following Data arrays is the
     * pixel width of the image represented by the data.
     */
    final static int[] vThumbHeaderData = {
            0x00ffffff, 0x00ffffff, 0xff1d1e2f, 0xff0b0b11, 0xff141520, 0xff181927, 0xff141520, 0xff191a28, 0xff141520,
            0xff141520, 0xff141520, 0xff141520, 0xff09090e, 0xff212336, 0x00ffffff, 0x00ffffff, 0x00ffffff, 0xff0b0b11,
            0xff25273d, 0xff6d72a7, 0xff6d72a7, 0xff6d72a7, 0xff6d72a7, 0xff6d72a7, 0xff6d72a7, 0xff6d72a7, 0xff6d72a7,
            0xff6d72a7, 0xff515586, 0xff1d1e2f, 0xff060609, 0x00ffffff, 0xff1d1e2f, 0xff484c77, 0xff9296bd, 0xff9296bd,
            0xff8084b2, 0xff8084b2, 0xff8084b2, 0xff8084b2, 0xff8084b2, 0xff8084b2, 0xff8084b2, 0xff8084b2, 0xff6d72a7,
            0xff656aa2, 0xff343756, 0xff141520, 0xff1d1e2f, 0xffc9cbde, 0xffa4a7c8, 0xffa4a7c8, 0xff898db8, 0xff898db8,
            0xff898db8, 0xff898db8, 0xff898db8, 0xff898db8, 0xff898db8, 0xff898db8, 0xff8084b2, 0xff6d72a7, 0xff585d91,
            0xff141520, 0xff343756, 0xffc9cbde, 0xffc0c2d9, 0xffa4a7c8, 0xffa4a7c8, 0xff9296bd, 0xff9296bd, 0xff9296bd,
            0xff9296bd, 0xff9296bd, 0xff9296bd, 0xff9296bd, 0xff898db8, 0xff777bad, 0xff5e639b, 0xff181927, 0xff343756,
            0xffc9cbde, 0xffc0c2d9, 0xffb6b8d3, 0xffa4a7c8, 0xffa4a7c8, 0xff9c9fc3, 0xff9c9fc3, 0xff9c9fc3, 0xff9c9fc3,
            0xff9c9fc3, 0xff9296bd, 0xff898db8, 0xff8084b2, 0xff5e639b, 0xff1d1e2f, 16
    };
    final static int[] vThumbCenterData = {
            0xff343756, 0xffc9cbde, 0xffc0c2d9, 0xffb6b8d3, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8,
            0xffa4a7c8, 0xff9c9fc3, 0xff9296bd, 0xff898db8, 0xff8084b2, 0xff656aa2, 0xff1d1e2f, 16
    };
    final static int[] vThumbFooterData = {
            0xff343756, 0xffc9cbde, 0xffc0c2d9, 0xffb6b8d3, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8,
            0xffa4a7c8, 0xffa4a7c8, 0xff9296bd, 0xff9296bd, 0xff8084b2, 0xff5e639b, 0xff1d1e2f, 0xff343756, 0xffc9cbde,
            0xffc9cbde, 0xffb6b8d3, 0xffb6b8d3, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8,
            0xffa4a7c8, 0xff9296bd, 0xff8084b2, 0xff6d72a7, 0xff1d1e2f, 0xff343756, 0xffc9cbde, 0xffdadbe8, 0xffc9cbde,
            0xffb6b8d3, 0xffb6b8d3, 0xffb6b8d3, 0xffb6b8d3, 0xffb6b8d3, 0xffb6b8d3, 0xffb6b8d3, 0xffa4a7c8, 0xff9c9fc3,
            0xff8084b2, 0xff5e639b, 0xff141520, 0xff212336, 0xff9296bd, 0xffdadbe8, 0xffdadbe8, 0xffc9cbde, 0xffc9cbde,
            0xffbabcd5, 0xffb6b8d3, 0xffc0c2d9, 0xffb6b8d3, 0xffb6b8d3, 0xffb6b8d3, 0xffa4a7c8, 0xff9296bd, 0xff41446b,
            0xff141520, 0x00ffffff, 0xff343756, 0xff9296bd, 0xffd1d2e3, 0xffcccee0, 0xffc9cbde, 0xffc0c2d9, 0xffc9cbde,
            0xffc0c2d9, 0xffc9cbde, 0xffb1b4d0, 0xffc0c2d9, 0xffa4a7c8, 0xff585d91, 0xff141520, 0x00ffffff, 0x00ffffff,
            0x00ffffff, 0xff343756, 0xff212336, 0xff343756, 0xff343756, 0xff343756, 0xff343756, 0xff343756, 0xff343756,
            0xff343756, 0xff212336, 0xff1d1e2f, 0xff141520, 0x00ffffff, 0x00ffffff, 16
    };
    final static int[] vTrackCenterData = {
            0xff3e3f4c, 0xff707289, 0xff8f91a3, 0xff9799aa, 0xff9fa1b0, 0xffa7a8b7, 0xffafb0bd, 0xffafb0bd, 0xffafb0bd,
            0xffafb0bd, 0xffafb0bd, 0xffafb0bd, 0xffbebfca, 0xffc7c8d1, 0xffcfd0d7, 0xff3e3f4c, 16
    };
    final static int[] hThumbHeaderData = {
            0x00ffffff, 0x00ffffff, 0xff141520, 0xff141520, 0xff181927, 0xff1d1e2f, 0x00ffffff, 0xff060609, 0xff343756,
            0xff585d91, 0xff5e639b, 0xff5e639b, 0xff212336, 0xff1d1e2f, 0xff656aa2, 0xff6d72a7, 0xff777bad, 0xff8084b2,
            0xff09090e, 0xff515586, 0xff6d72a7, 0xff8084b2, 0xff898db8, 0xff898db8, 0xff141520, 0xff6d72a7, 0xff8084b2,
            0xff898db8, 0xff9296bd, 0xff9296bd, 0xff141520, 0xff6d72a7, 0xff8084b2, 0xff898db8, 0xff9296bd, 0xff9c9fc3,
            0xff141520, 0xff6d72a7, 0xff8084b2, 0xff898db8, 0xff9296bd, 0xff9c9fc3, 0xff141520, 0xff6d72a7, 0xff8084b2,
            0xff898db8, 0xff9296bd, 0xff9c9fc3, 0xff191a28, 0xff6d72a7, 0xff8084b2, 0xff898db8, 0xff9296bd, 0xff9c9fc3,
            0xff141520, 0xff6d72a7, 0xff8084b2, 0xff898db8, 0xff9296bd, 0xff9c9fc3, 0xff181927, 0xff6d72a7, 0xff8084b2,
            0xff898db8, 0xff9296bd, 0xffa4a7c8, 0xff141520, 0xff6d72a7, 0xff8084b2, 0xff898db8, 0xffa4a7c8, 0xffa4a7c8,
            0xff0b0b11, 0xff6d72a7, 0xff9296bd, 0xffa4a7c8, 0xffa4a7c8, 0xffb6b8d3, 0xff1d1e2f, 0xff25273d, 0xff9296bd,
            0xffa4a7c8, 0xffc0c2d9, 0xffc0c2d9, 0x00ffffff, 0xff0b0b11, 0xff484c77, 0xffc9cbde, 0xffc9cbde, 0xffc9cbde,
            0x00ffffff, 0x00ffffff, 0xff1d1e2f, 0xff1d1e2f, 0xff343756, 0xff343756, 6
    };
    final static int[] hThumbCenterData = {
            0xff1d1e2f, 0xff656aa2, 0xff8084b2, 0xff898db8, 0xff9296bd, 0xff9c9fc3, 0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8,
            0xffa4a7c8, 0xffa4a7c8, 0xffa4a7c8, 0xffb6b8d3, 0xffc0c2d9, 0xffc9cbde, 0xff343756, 1
    };
    final static int[] hThumbFooterData = {
            0xff1d1e2f, 0xff1d1e2f, 0xff141520, 0xff141520, 0x00ffffff, 0x00ffffff, 0xff5e639b, 0xff6d72a7, 0xff5e639b,
            0xff41446b, 0xff141520, 0x00ffffff, 0xff8084b2, 0xff8084b2, 0xff8084b2, 0xff9296bd, 0xff585d91, 0xff141520,
            0xff9296bd, 0xff9296bd, 0xff9c9fc3, 0xffa4a7c8, 0xffa4a7c8, 0xff1d1e2f, 0xff9296bd, 0xffa4a7c8, 0xffa4a7c8,
            0xffb6b8d3, 0xffc0c2d9, 0xff212336, 0xffa4a7c8, 0xffa4a7c8, 0xffb6b8d3, 0xffb6b8d3, 0xffb1b4d0, 0xff343756,
            0xffa4a7c8, 0xffa4a7c8, 0xffb6b8d3, 0xffb6b8d3, 0xffc9cbde, 0xff343756, 0xffa4a7c8, 0xffa4a7c8, 0xffb6b8d3,
            0xffc0c2d9, 0xffc0c2d9, 0xff343756, 0xffa4a7c8, 0xffa4a7c8, 0xffb6b8d3, 0xffb6b8d3, 0xffc9cbde, 0xff343756,
            0xffa4a7c8, 0xffa4a7c8, 0xffb6b8d3, 0xffbabcd5, 0xffc0c2d9, 0xff343756, 0xffa4a7c8, 0xffa4a7c8, 0xffb6b8d3,
            0xffc9cbde, 0xffc9cbde, 0xff343756, 0xffa4a7c8, 0xffb6b8d3, 0xffb6b8d3, 0xffc9cbde, 0xffcccee0, 0xff343756,
            0xffb6b8d3, 0xffb6b8d3, 0xffc9cbde, 0xffdadbe8, 0xffd1d2e3, 0xff212336, 0xffc0c2d9, 0xffc9cbde, 0xffdadbe8,
            0xffdadbe8, 0xff9296bd, 0xff343756, 0xffc9cbde, 0xffc9cbde, 0xffc9cbde, 0xff9296bd, 0xff343756, 0x00ffffff,
            0xff343756, 0xff343756, 0xff343756, 0xff212336, 0x00ffffff, 0x00ffffff, 6
    };
    final static int[] hTrackCenterData = {
            0xff3e3f4c, 0xffcfd0d7, 0xffc7c8d1, 0xffbebfca, 0xffafb0bd, 0xffafb0bd, 0xffafb0bd, 0xffafb0bd, 0xffafb0bd,
            0xffafb0bd, 0xffa7a8b7, 0xff9fa1b0, 0xff9799aa, 0xff8f91a3, 0xff707289, 0xff3e3f4c, 1
    };

    // =================================================================================== //
    // =================================================================================== //
    // =================================================================================== //

    protected int buttonOrientation = DEFAULT_BUTTON_ORIENTATION;

    private Color buttonForeground = Color.GRAY;
    private Color buttonBackground = new Color(0xDDDDDD);

    Color sourceBg = new Color(152, 155, 211);
    Color SCROLL_COLOR = new Color(0xDDDDDD);//SkinManager.getInstance().getColor("ScrollBar.highlight.color");
    ColorFilter thumbColorFilter = new ColorFilter(sourceBg, SCROLL_COLOR);
    Color trackRootBg = Color.decode("#afb0bd");
    Color TRACK_COLOR = new Color(0xDDDDDD);
    ;//SkinManager.getInstance().getColor("ScrollBar.background.color");
    ColorFilter trackColorFilter = new ColorFilter(trackRootBg, TRACK_COLOR);

//    Color sourceBg = new Color(152, 155, 211);
//    Color SCROLL_COLOR = Color.YELLOW;
//    ColorFilter thumbColorFilter = new ColorFilter(sourceBg, SCROLL_COLOR);
//    Color trackRootBg = Color.decode("#afb0bd");
//    Color TRACK_COLOR = Color.PINK;
//    ColorFilter trackColorFilter = new ColorFilter(trackRootBg, TRACK_COLOR);

    // it seems for the vertical scrollbar the only top- and bottom-images are inversed and only
    //   from left-to-right so revert their pixels back and exchange those images with each-other
    Icon vThumbHeader = getIcon(reverseImageData(reverseImageDataLeftRight(vThumbFooterData, 16)), thumbColorFilter);
    Icon vThumbCenter = getIcon(vThumbCenterData, thumbColorFilter);
    Icon vThumbFooter = getIcon(reverseImageData(reverseImageDataLeftRight(vThumbHeaderData, 16)), thumbColorFilter);
    Icon vTrackCenter = getIcon(vTrackCenterData, trackColorFilter);
    // it seems the images for the horisontal scrollbar are completely inversed so revert them back to normal
    Icon hThumbHeader = getIcon(reverseImageData(hThumbFooterData), thumbColorFilter);
    Icon hThumbCenter = getIcon(reverseImageData(hThumbCenterData), thumbColorFilter);
    Icon hThumbFooter = getIcon(reverseImageData(hThumbHeaderData), thumbColorFilter);
    Icon hTrackCenter = getIcon(reverseImageData(hTrackCenterData), trackColorFilter);


    static ImageIcon getIcon(int[] data, ImageFilter filter) {
        int w = data[data.length - 1];
        int h = (data.length - 1) / w;
        ImageIcon icon = new ImageIcon(StyleSupport.getImage(w, h, data));
        return getColoredIcon(icon, filter);
    }

    public static ImageIcon getColoredIcon(ImageIcon imageIcon, ImageFilter filter) {
        ImageProducer imageProducer;
        imageProducer = new FilteredImageSource(imageIcon.getImage().getSource(), filter);
        Image image = Toolkit.getDefaultToolkit().createImage(imageProducer);
        ImageIcon coloredIcon = new ImageIcon(image);
        return coloredIcon;
    }

    private int[] reverseImageData(int[] arg_iaImgArray) {
        if (arg_iaImgArray == null)
            return null;
        int iLen = arg_iaImgArray.length;
        int iCount = ((iLen % 2) == 0) ? iLen : (iLen - 1);

        int[] iaNewImg = new int[iLen];
        int iRead = 0;
        int iWrite = iCount - 1;

        for (int i = 0; i < iCount; i++) {
            int iPix = arg_iaImgArray[iRead++];
            iaNewImg[iWrite--] = iPix;
        }
        iaNewImg[iLen - 1] = arg_iaImgArray[iLen - 1];
        return iaNewImg;
    }

    private int[] reverseImageDataLeftRight(int[] arg_iaImgArray, int arg_iWidth) {
        if (arg_iaImgArray == null)
            return null;
        int iLen = arg_iaImgArray.length;
        int iCount = ((iLen % 2) == 0) ? iLen : (iLen - 1);

        int[] iaNewImg = new int[iLen];
        int iRead = 0;
        int iWrite = iCount - 1;

        while (iRead < iCount) {
            iWrite = (iRead + arg_iWidth) - 1;
            for (int x = 0; x < arg_iWidth; x++) {
                int iPix = arg_iaImgArray[iRead++];
                iaNewImg[iWrite--] = iPix;
            }
        }
        iaNewImg[iLen - 1] = arg_iaImgArray[iLen - 1];
        return iaNewImg;
    }

    static ImageIcon getIcon(int[] data) {
        int w = data[data.length - 1];
        int h = (data.length - 1) / w;

        return new ImageIcon(StyleSupport.getImage(w, h, data));
    }

    public static ComponentUI createUI(JComponent c) {
        return new AdfScrollBarUI();
    }

    protected BasicScrollBarUI.TrackListener createTrackListener() {
        return new TrackListener();
    }

    /**
     * Returns the orientation of the renderedButtons on the scrollbar which
     * can be at opposite ends of the track or flush together at one end.
     *
     * @see #BUTTONS_TOGETHER
     * @see #BUTTONS_AT_ENDS
     */
    protected int getButtonOrientation() {
        return buttonOrientation;
    }

    protected void setButtonOrientation(int orientation) {
        switch (orientation) {
            case BUTTONS_TOGETHER:
                buttonOrientation = orientation;

                return;

            case BUTTONS_AT_ENDS:
                buttonOrientation = orientation;

                return;

            default:
                // runtime error - but fail silently
        }
    }

    protected void layoutVScrollbar(JScrollBar sb) {
        int buttonOrientation = getButtonOrientation();

        if (buttonOrientation == BUTTONS_AT_ENDS) {
            layoutVScrollbarButtonsAtEnds(sb);
        } else {
            layoutVScrollbarButtonsAtEnds(sb);
        }
    }

    public Dimension getMinimumThumbSize() {
        Dimension d = super.getMinimumThumbSize();

        int orientation = scrollbar.getOrientation();

        if (orientation == JScrollBar.VERTICAL) {
            d.height = 12;
        } else {
            d.width = 12;
        }

        return d;
    }

    protected void layoutHScrollbar(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();

        /*
         * Height and top edge of the renderedButtons and thumb.
         *
         */
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;

        if (itemH > TRACK_WIDTH) {
            itemH = TRACK_WIDTH;
        }

        /*
         * If component is oriented LeftToRight then the left button
         * will be the decrButton and the right button will be the
         * incrButton; otherwise the renderedButtons will be reversed
         *
         */
        boolean ltr = sb.getComponentOrientation().isLeftToRight();

        /*
         * If the button orientation is BUTTONS_TOGETHER then the
         * left and right button will be flush together at the
         * rightmost end of the scrollbar
         *
         */
        boolean bt = getButtonOrientation() == BUTTONS_TOGETHER;

        /*
         * Nominal locations of the renderedButtons, assuming their preferred
         * size will fit.
         *
         */
        int leftButtonW = (ltr ? decrButton : incrButton).getPreferredSize().width;
        int rightButtonW = (ltr ? incrButton : decrButton).getPreferredSize().width;
        int leftButtonX;
        int rightButtonX;

        if (bt) {
            rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);
            leftButtonX = rightButtonX - leftButtonW;
        } else {
            leftButtonX = sbInsets.left;
            rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);
        }

        /*
         * The thumb must fit within the width left over after we
         * subtract the preferredSize of the renderedButtons and the insets.
         *
         */
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = leftButtonW + rightButtonW;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW);

        /*
         * Compute the width and origin of the thumb.  Enforce
         * the thumbs min/max dimensions.  The case where the thumb
         * is at the right edge is handled specially to avoid numerical
         * problems in computing thumbX.  If the thumb doesn't
         * fit in the track (trackH) we'll hide it later.
         *
         */
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();

        int thumbW = (range <= 0) ? getMaximumThumbSize().width : (int) (trackW * (extent / range));

        thumbW = Math.max(thumbW, getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, getMaximumThumbSize().width);

        int thumbX;

        if (bt) {
            thumbX = ltr ? (leftButtonX - thumbW) : sbInsets.left;
        } else {
            thumbX = ltr ? (rightButtonX - thumbW) : (leftButtonX + leftButtonW);
        }

        if (sb.getValue() < (max - sb.getVisibleAmount())) {
            float thumbRange = trackW - thumbW;

            if (ltr) {
                thumbX = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            } else {
                thumbX = (int) (0.5f + (thumbRange * ((max - extent - value) / (range - extent))));
            }

            if (bt) {
                thumbX += sbInsets.left;
            } else {
                thumbX += (leftButtonX + leftButtonW);
            }
        }

        /*
         * If the renderedButtons don't fit, allocate half of the available
         * space to each and move the right one over.
         */
        int sbAvailButtonW = (sbSize.width - sbInsetsW);

        if (sbAvailButtonW < sbButtonsW) {
            rightButtonW = leftButtonW = sbAvailButtonW / 2;
            rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);

            if (bt) {
                leftButtonX = rightButtonX - leftButtonW;
            }
        }

        /*
         * commit the button bounds
         *
         */
        (ltr ? decrButton : incrButton).setBounds(leftButtonX, itemY, leftButtonW, itemH);
        (ltr ? incrButton : decrButton).setBounds(rightButtonX, itemY, rightButtonW, itemH);

        /*
         * Update the trackRect field.
         */
        int itrackX;
        int itrackW;

        itrackX = bt ? sbInsets.left : (leftButtonX + leftButtonW);
        itrackW = bt ? (leftButtonX - itrackX) : (rightButtonX - itrackX);
        trackRect.setBounds(itrackX, itemY, itrackW, itemH);

        /*
         * Make sure the thumb fits between the renderedButtons.  Note
         * that setting the thumbs bounds causes a repaint.
         */
        if (thumbW >= (int) trackW) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if (bt) {
                if ((thumbX + thumbW) > leftButtonX) {
                    thumbX = leftButtonX - thumbW;
                }

                if (thumbX < sbInsets.left) {
                    thumbX = sbInsets.left + 1;
                }
            } else {
                if ((thumbX + thumbW) > rightButtonX) {
                    thumbX = rightButtonX - thumbW;
                }

                if (thumbX < (leftButtonX + leftButtonW)) {
                    thumbX = leftButtonX + leftButtonW + 1;
                }
            }

            setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }

    protected void layoutVScrollbarButtonsAtEnds(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();

        /*
         * Width and left edge of the renderedButtons and thumb.
         *
         */
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;

        if (itemW > TRACK_WIDTH) {
            itemX += (itemW - TRACK_WIDTH);
            itemW = TRACK_WIDTH;
        }

        boolean bt = getButtonOrientation() == BUTTONS_TOGETHER;

        /*
         * Nominal locations of the renderedButtons, assuming their preferred
         * size will fit.
         *
         */
        int incrButtonH = incrButton.getPreferredSize().height;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        int decrButtonH = bt ? decrButton.getPreferredSize().height : decrButton.getPreferredSize().height;
        int decrButtonY = bt ? (incrButtonY - decrButtonH) : sbInsets.top;

        /*
         * The thumb must fit within the height left over after we
         * subtract the preferredSize of the renderedButtons and the insets.
         *
         */
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButtonH + incrButtonH;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH);

        /*
         * Compute the height and origin of the thumb.   The case
         * where the thumb is at the bottom edge is handled specially
         * to avoid numerical problems in computing thumbY.  Enforce
         * the thumbs min/max dimensions.  If the thumb doesn't
         * fit in the vTrackCenter (trackH) we'll hide it later.
         *
         */
        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = sb.getMaximum() - min;
        float value = sb.getValue();

        /*
         * Determine height of thumb
         *
         */
        int thumbH = (range <= 0) ? getMaximumThumbSize().height : (int) (trackH * (extent / range));

        thumbH = Math.max(thumbH, getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, getMaximumThumbSize().height);

        int thumbY = bt ? (decrButtonY - thumbH) : (incrButtonY - thumbH);

        if (sb.getValue() < (sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - thumbH;

            thumbY = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            thumbY += (bt ? sbInsets.top : (decrButtonY + decrButtonH));
        }

        /*
         * If the renderedButtons don't fit, allocate half of the available
         * space to each and move the lower one (incrButton) down.
         *
         */
        int sbAvailButtonH = (sbSize.height - sbInsetsH);

        if (sbAvailButtonH < sbButtonsH) {
            incrButtonH = decrButtonH = sbAvailButtonH / 2;
            incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        }

        /*
         * Commit button bounds
         *
         */
        if (bt) {
            decrButton.setBounds(itemX, incrButtonY - decrButtonH, itemW, decrButtonH);
            incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);
        } else {
            decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
            incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);
        }

        /*
         * Update the trackRect field.
         *
         */
        int itrackY = bt ? 0 : (decrButtonY + decrButtonH);
        int itrackH = bt ? (decrButtonY - itrackY) : (incrButtonY - itrackY);

        trackRect.setBounds(itemX, itrackY, itemW, itrackH);

        /*
         * If the thumb isn't going to fit, zero it's bounds.  Otherwise
         * make sure it fits between the renderedButtons.  Note that setting the
         * thumbs bounds will cause a repaint.
         *
         */
        if (thumbH >= (int) trackH) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if (bt) {
                if ((thumbY + thumbH) > decrButtonY) {
                    thumbY = decrButtonY - thumbH;
                }

                if (thumbY < itrackY) {
                    thumbY = itrackY + 1;
                }
            } else {
                if ((thumbY + thumbH) > incrButtonY) {
                    thumbY = incrButtonY - thumbH;
                }

                if (thumbY < (decrButtonY + decrButtonH)) {
                    thumbY = decrButtonY + decrButtonH + 1;
                }
            }

            setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }

    protected void layoutVScrollbarButtonsTogether(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();

        /*
         * Width and left edge of the renderedButtons and thumb.
         */
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;

        // force vTrackCenter width == TRACK_WIDTH
        if (itemW > TRACK_WIDTH) {
            //itemX = (sbInsets.right - sbInsets.left - TRACK_WIDTH);
            itemX += (itemW - TRACK_WIDTH);
            itemW = TRACK_WIDTH;
        }

        /* Nominal locations of the renderedButtons, assuming their preferred
         * size will fit.
         */
        int incrButtonH = incrButton.getPreferredSize().height;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        int decrButtonH = decrButton.getPreferredSize().height;
        int decrButtonY = incrButtonY - decrButtonH;

        /* The thumb must fit within the height left over after we
         * subtract the preferredSize of the renderedButtons and the insets.
         */
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButtonH + incrButtonH;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH);

        /* Compute the height and origin of the thumb.   The case
        * where the thumb is at the bottom edge is handled specially
        * to avoid numerical problems in computing thumbY.  Enforce
        * the thumbs min/max dimensions.  If the thumb doesn't
        * fit in the vTrackCenter (trackH) we'll hide it later.
        */
        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = sb.getMaximum() - min;
        float value = sb.getValue();

        /*
         * Determine height of thumb
         */
        int thumbH = (range <= 0) ? getMaximumThumbSize().height : (int) (trackH * (extent / range));

        thumbH = Math.max(thumbH, getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, getMaximumThumbSize().height);

        int thumbY = decrButtonY - thumbH;

        if (sb.getValue() < (sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - thumbH;

            thumbY = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));

            //thumbY +=  decrButtonY + decrButtonH;
        }

        /* If the renderedButtons don't fit, allocate half of the available
         * space to each and move the lower one (incrButton) down.
         */
        int sbAvailButtonH = (sbSize.height - sbInsetsH);

        if (sbAvailButtonH < sbButtonsH) {
            incrButtonH = decrButtonH = sbAvailButtonH / 2;
            incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        }

        /*
         * Put renderedButtons next to each other
         */
        decrButton.setBounds(itemX, incrButtonY - decrButtonH, itemW, decrButtonH);
        incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);

        /*
         * Update the trackRect field.
         */
        int itrackY = 0;
        int itrackH = decrButtonY - itrackY;

        trackRect.setBounds(itemX, itrackY, itemW, itrackH);

        /* If the thumb isn't going to fit, zero it's bounds.  Otherwise
        * make sure it fits between the renderedButtons.  Note that setting the
        * thumbs bounds will cause a repaint.
        */
        if (thumbH >= (int) trackH) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if ((thumbY + thumbH) > decrButtonY) {
                thumbY = decrButtonY - thumbH;
            }

            if (thumbY < itrackY) {
                thumbY = itrackY + 1;
            }

            setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }

    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        switch (scrollbar.getOrientation()) {
            case JScrollBar.VERTICAL:
                paintVTrack(trackBounds, c, g);

                break;

            case JScrollBar.HORIZONTAL:
                paintHTrack(trackBounds, c, g);

                break;

            default:
        }
    }

    private void paintVTrack(Rectangle trackBounds, JComponent c, Graphics g) {
        for (int i = 0; i < trackBounds.height; i++) {
            vTrackCenter.paintIcon(c, g, trackBounds.x, trackBounds.y + i);
        }

        if (trackHighlight == DECREASE_HIGHLIGHT) {
            paintDecreaseHighlight(g);
        } else if (trackHighlight == INCREASE_HIGHLIGHT) {
            paintIncreaseHighlight(g);
        }
    }

    private void paintHTrack(Rectangle trackBounds, JComponent c, Graphics g) {
        for (int i = 0; i < trackBounds.width; i++) {
            hTrackCenter.paintIcon(c, g, trackBounds.x + i, trackBounds.y);
        }

        if (trackHighlight == DECREASE_HIGHLIGHT) {
            paintDecreaseHighlight(g);
        } else if (trackHighlight == INCREASE_HIGHLIGHT) {
            paintIncreaseHighlight(g);
        }
    }

    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        switch (scrollbar.getOrientation()) {
            case JScrollBar.VERTICAL:
                paintVThumb(thumbBounds, g, c);

                break;

            case JScrollBar.HORIZONTAL:
                paintHThumb(thumbBounds, g, c);

                break;

            default:
        }
    }

    /**
     * Paint a thumb for a vertical scrollbar
     *
     * @param thumbBounds
     * @param g
     * @param c
     */
    private void paintVThumb(Rectangle thumbBounds, Graphics g, JComponent c) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        g.translate(thumbBounds.x, thumbBounds.y);

        vThumbHeader.paintIcon(c, g, 0, 0);

        int count = thumbBounds.height - vThumbHeader.getIconHeight() - vThumbFooter.getIconHeight();

        if (count > 0) {
            for (int i = 0; i < count; i++) {
                vThumbCenter.paintIcon(c, g, 0, vThumbHeader.getIconHeight() + i);
            }
        }

        vThumbFooter.paintIcon(c, g, 0, thumbBounds.height - vThumbFooter.getIconHeight());

        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

    /**
     * Paint a Thumb for a horizontal scrollbar
     *
     * @param thumbBounds
     * @param g
     * @param c
     */
    private void paintHThumb(Rectangle thumbBounds, Graphics g, JComponent c) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        g.translate(thumbBounds.x, thumbBounds.y);

        hThumbHeader.paintIcon(c, g, 0, 0);

        int count = thumbBounds.width - hThumbHeader.getIconWidth() - hThumbFooter.getIconWidth();

        if (count > 0) {
            for (int i = 0; i < count; i++) {
                hThumbCenter.paintIcon(c, g, hThumbHeader.getIconWidth() + i, 0);
            }
        }

        hThumbFooter.paintIcon(c, g, thumbBounds.width - hThumbFooter.getIconWidth(), 0);

        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

    class TrackListener extends BasicScrollBarUI.TrackListener {
        /*
         * From BasicScrollBarUI here due to accessibility
         */
        private transient int direction = +1;

        /*
         * From BasicScrollBarUI here due to accessibility
         */
        private boolean supportsAbsolutePositioning;

        public void mousePressed(MouseEvent e) {
            currentMouseX = e.getX();
            currentMouseY = e.getY();

            // Goodbye if we're not even in the Track area!
            if (!getTrackBounds().contains(currentMouseX, currentMouseY)) {
                return;
            }

            super.mousePressed(e);
        }

        /**
         * Set the models value to the position of the thumb's vThumbHeader of Vertical
         * scrollbar, or the left/right of Horizontal scrollbar in
         * left-to-right/right-to-left scrollbar relative to the origin of the
         * vTrackCenter.
         */
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)
                    || (!getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e))) {
                return;
            }

            if (!scrollbar.isEnabled() || getThumbBounds().isEmpty()) {
                return;
            }

            if (isDragging) {
                setValueFrom(e);
            } else {
                currentMouseX = e.getX();
                currentMouseY = e.getY();
                startScrollTimerIfNecessary();
            }
        }

        /*
         * From BasicScrollBarUI here due to accessibility
         */
        private boolean getSupportsAbsolutePositioning() {
            return supportsAbsolutePositioning;
        }

        /*
         * From BasicScrollBarUI here due to accessibility
         */
        private void startScrollTimerIfNecessary() {
            if (scrollTimer.isRunning()) {
                return;
            }

            switch (scrollbar.getOrientation()) {
                case JScrollBar.VERTICAL:

                    if (direction > 0) {
                        if ((getThumbBounds().y + getThumbBounds().height) < currentMouseY) {
                            scrollTimer.start();
                        }
                    } else if (getThumbBounds().y > currentMouseY) {
                        scrollTimer.start();
                    }

                    break;

                case JScrollBar.HORIZONTAL:

                    if (direction > 0) {
                        if ((getThumbBounds().x + getThumbBounds().width) < currentMouseX) {
                            scrollTimer.start();
                        }
                    } else if (getThumbBounds().x > currentMouseX) {
                        scrollTimer.start();
                    }

                    break;
            }
        }

        /*
         * Private method from BasicScrollBarUI was incompatible with the concept
         * of button orientation because it assumed that the renderedButtons were at either
         * end of the vTrackCenter.  The need to implement our own setValueFrom() method
         * necessitated duplicating a lot of code simply due to limitations on access.
         */
        private void setValueFrom(MouseEvent e) {
            BoundedRangeModel model = scrollbar.getModel();
            Rectangle thumbR = getThumbBounds();
            float trackLength;
            int thumbMin;
            int thumbMax;
            int thumbPos;
            int buttonOrientation = getButtonOrientation();

            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                if (buttonOrientation == BUTTONS_AT_ENDS) {
                    thumbMin = decrButton.getY() + decrButton.getHeight();
                    thumbMax = incrButton.getY() - thumbR.height;
                    thumbPos = Math.min(thumbMax, Math.max(thumbMin, (e.getY() - offset)));
                    setThumbBounds(thumbR.x, thumbPos, thumbR.width, thumbR.height);
                    trackLength = getTrackBounds().height;
                } else {
                    thumbMin = 0;
                    thumbMax = decrButton.getY() - thumbR.height;
                    thumbPos = Math.min(thumbMax, Math.max(thumbMin, (e.getY() - offset)));
                    setThumbBounds(thumbR.x, thumbPos, thumbR.width, thumbR.height);
                    trackLength = getTrackBounds().height;
                }
            } else {
                if (buttonOrientation == BUTTONS_AT_ENDS) {
                    if (scrollbar.getComponentOrientation().isLeftToRight()) {
                        thumbMin = decrButton.getX() + decrButton.getWidth();
                        thumbMax = incrButton.getX() - thumbR.width;
                    } else {
                        thumbMin = incrButton.getX() + incrButton.getWidth();
                        thumbMax = decrButton.getX() - thumbR.width;
                    }

                    thumbPos = Math.min(thumbMax, Math.max(thumbMin, (e.getX() - offset)));
                    setThumbBounds(thumbPos, thumbR.y, thumbR.width, thumbR.height);
                    trackLength = getTrackBounds().width;
                } else {
                    if (scrollbar.getComponentOrientation().isLeftToRight()) {
                        thumbMin = 0;
                        thumbMax = decrButton.getX() - thumbR.width;
                    } else {
                        thumbMin = decrButton.getX() - thumbR.width;
                        thumbMax = 0;
                    }

                    thumbPos = Math.min(thumbMax, Math.max(thumbMin, (e.getX() - offset)));
                    setThumbBounds(thumbPos, thumbR.y, thumbR.width, thumbR.height);
                    trackLength = getTrackBounds().width;
                }
            }

            /* Set the scrollbars value.  If the thumb has reached the end of
             * the scrollbar, then just set the value to its maximum.  Otherwise
             * compute the value as accurately as possible.
             */
            if (thumbPos == thumbMax) {
                if ((scrollbar.getOrientation() == JScrollBar.VERTICAL)
                        || scrollbar.getComponentOrientation().isLeftToRight()) {
                    scrollbar.setValue(model.getMaximum() - model.getExtent());
                } else {
                    scrollbar.setValue(model.getMinimum());
                }
            } else {
                float valueMax = model.getMaximum() - model.getExtent();
                float valueRange = valueMax - model.getMinimum();
                float thumbValue = thumbPos - thumbMin;
                float thumbRange = thumbMax - thumbMin;
                int value;

                if ((scrollbar.getOrientation() == JScrollBar.VERTICAL)
                        || scrollbar.getComponentOrientation().isLeftToRight()) {
                    value = (int) (0.5 + ((thumbValue / thumbRange) * valueRange));
                } else {
                    value = (int) (0.5 + (((thumbMax - thumbPos) / thumbRange) * valueRange));
                }

                scrollbar.setValue(value + model.getMinimum());
            }
        }
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new OrionScrollBarButton(orientation);
//        return new BasicArrowButton(orientation,
//                UIManager.getColor("ScrollBar.thumb"),
//                UIManager.getColor("ScrollBar.thumbShadow"),
//                UIManager.getColor("ScrollBar.thumbDarkShadow"),
//                UIManager.getColor("ScrollBar.thumbHighlight"));
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new OrionScrollBarButton(orientation);
//        return new BasicArrowButton(orientation,
//                UIManager.getColor("ScrollBar.thumb"),
//                UIManager.getColor("ScrollBar.thumbShadow"),
//                UIManager.getColor("ScrollBar.thumbDarkShadow"),
//                UIManager.getColor("ScrollBar.thumbHighlight"));
    }

    private class OrionScrollBarButton extends JButton {
        private Icon arrowIcon;
        private final int orientation;
        private long rgbHash;

        public OrionScrollBarButton(int orientation) {
            this.setOpaque(false);
            this.orientation = orientation;
            this.createNewIcon();
            this.rgbHash = (long) AdfScrollBarUI.this.buttonForeground.getRGB() + (long) AdfScrollBarUI.this.buttonForeground.getRGB() << 32;
        }

        private void createNewIcon() {
            this.arrowIcon = getArrowIcon(this.orientation, AdfScrollBarUI.this.buttonForeground, AdfScrollBarUI.this.buttonForeground, 3, this.orientation == 5 ? 1 : 0);
        }

        private Icon getArrowIcon(int orientation, Color colour1, Color colour2, int width, int offset) {
            return new AdfArrowIcon(orientation, colour1, colour2, (Color) null, (Color) null, width, offset);
        }

        public Dimension getPreferredSize() {
            return new Dimension(12, 12);
        }

        protected void paintBorder(Graphics g) {
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            long rgbHash = (long) AdfScrollBarUI.this.buttonForeground.getRGB() + (long) AdfScrollBarUI.this.buttonForeground.getRGB() << 32;
            if (rgbHash != this.rgbHash) {
                this.createNewIcon();
                this.rgbHash = rgbHash;
            }

            Dimension d = this.getSize();
            g2.setColor(AdfScrollBarUI.this.buttonBackground);
            g2.fillRect(0, 0, d.width, d.height);
            int x = (d.width - this.arrowIcon.getIconWidth()) / 2;
            int y = (d.height - this.arrowIcon.getIconHeight()) / 2;
            this.arrowIcon.paintIcon(this, g2, x, y);
            g2.dispose();
        }

        public boolean isFocusTraversable() {
            return false;
        }
    }

    private static class AdfArrowIcon implements Icon {
        private final Polygon triangle;
        private final int triangleWidth;
        private final int triangleHeight;
        private final Color colour1;
        private final Color colour2;
        private final Color armedColour1;
        private final Color armedColour2;
        private final int x_offset;
        private final float[] hsb = new float[3];

        public AdfArrowIcon(int orientation, Color colour1, Color colour2, Color armedColour1, Color armedColour2, int width, int x_offset) {
            int[] x;
            int[] y;
            switch (orientation) {
                case 1:
                    x = new int[]{width, 0, width * 2 + 1, width + 2};
                    y = new int[]{0, width, width, 0};
                    break;
                case 2:
                case 4:
                case 6:
                default:
                    throw new IllegalArgumentException("Invalid orientation: " + orientation);
                case 3:
                    x = new int[]{width, 0, 0, width};
                    y = new int[]{width, 0, width * 2 + 1, width + 1};
                    break;
                case 5:
                    x = new int[]{width, 0, width * 2, width};
                    y = new int[]{width, 0, 0, width};
                    break;
                case 7:
                    x = new int[]{0, width, width, 0};
                    y = new int[]{width, 0, width * 2 + 1, width + 2};
            }

            this.triangle = new Polygon(x, y, x.length);
            Rectangle r = this.triangle.getBounds();
            this.triangleWidth = r.width;
            this.triangleHeight = r.height;
            this.colour1 = colour1;
            this.colour2 = colour2;
            this.armedColour1 = armedColour1;
            this.armedColour2 = armedColour2;
            this.x_offset = x_offset;
        }

        public int getIconHeight() {
            return this.triangleHeight;
        }

        public int getIconWidth() {
            return this.triangleWidth + this.x_offset;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            Color c1 = this.colour1;
            Color c2 = this.colour2;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            if (c instanceof AbstractButton) {
                AbstractButton paint = (AbstractButton) c;
                ButtonModel m = paint.getModel();
                if (!m.isEnabled()) {
                    c1 = UIManager.getColor("Button.darkShadow");
                    c2 = UIManager.getColor("Button.darkShadow");
                }

                if (m.isRollover() || m.isArmed()) {
                    if (this.armedColour1 != null) {
                        c1 = this.armedColour1;
                        c2 = this.armedColour2;
                    } else {
                        Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), this.hsb);
                        if (this.hsb[2] > 0.5F) {
                            c1 = c1.darker();
                            c2 = c2.darker();
                        } else {
                            c1 = c1.brighter();
                            c2 = c2.brighter();
                        }
                    }
                }
            }

            GradientPaint paint1 = new GradientPaint(0.0F, 0.0F, c1, (float) this.triangleWidth, (float) this.triangleHeight, c2);
            x += this.x_offset;
            g2.translate(x, y);
            g2.setPaint(paint1);
            g2.fill(this.triangle);
            g2.dispose();
        }
    }
}

