package adf.ui;

import adf.ui.components.borders.RoundedLineBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Mar 26, 2012
 * Time: 8:28:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class UiUtils {

    public static final Color LINE_BORDER_BRIGHT_COLOR = new Color(0xFFFFFF);
    public static final Color LINE_BORDER_DARK_COLOR = new Color(0xaaaaaa);
    public static final Color LINE_BORDER_TEXT_COLOR = new Color(0xdddddd);

    /**
     * @param top
     * @param left
     * @param bottom
     * @param right
     * @param title
     * @param lineColor
     * @return Border
     */
    public static Border createTitledBorder(int top, int left, int bottom, int right, Color lineColor, String title) {
        Border compoundTitledBorder = createTitledBorder(top, left, bottom, right, lineColor, title,
                LINE_BORDER_TEXT_COLOR);
        return compoundTitledBorder;
    }

    /**
     *
     * @param top
     * @param left
     * @param bottom
     * @param right
     * @param lineColor
     * @param title
     * @param textColor
     * @return Border
     */
    public static Border createTitledBorder(int top, int left, int bottom, int right, Color lineColor, String title,
                                            Color textColor) {
        Border roundedLineBorder = new RoundedLineBorder(lineColor);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(roundedLineBorder, title);
        titledBorder.setTitleColor(textColor);
        Border compoundTitledBorder = BorderFactory.createCompoundBorder(titledBorder,
                BorderFactory.createEmptyBorder(top, left, bottom, right));
        return compoundTitledBorder;
    }
}