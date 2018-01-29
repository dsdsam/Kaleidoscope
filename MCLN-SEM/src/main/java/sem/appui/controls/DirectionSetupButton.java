package sem.appui.controls;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 12, 2011
 * Time: 9:08:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectionSetupButton extends TwoStateRoundedCornersButton {

    private static final Color FIRST_STATE_BORDER_COLOR = new Color(0x008800);
    private static final Color FIRST_STATE_INNER_BORDER_COLOR = Color.BLACK;
    private static final Color SECOND_STATE_BORDER_COLOR = new Color(0x00DD00);
    private static final Color SECOND_STATE_INNER_BORDER_COLOR = Color.BLACK;

    public DirectionSetupButton(ImageIcon firstStateImageIcon, ImageIcon secondStateImageIcon,
                                Color firstStateOuterBorderColor, Color firstStateInnerBorderColor,
                                Color secondStateOuterBorderColor, Color secondStateInnerBorderColor,
                                Color firstStateBackgroundColor, Color secondStateBackgroundColor,
                                String tooltip) {
//        super(Color.BLACK, 1, 4, DirectionSetupButton.ROUNDING_ALL, outerBorderColor, innerBorderColor,
//                firstStateImageIcon, firstStateImageIcon,
//                firstStateBackgroundColor, null, secondStateBackgroundColor,  null, tooltip);
        super(Color.BLACK, 1, 4, DirectionSetupButton.ROUNDING_ALL,
                FIRST_STATE_BORDER_COLOR, FIRST_STATE_INNER_BORDER_COLOR,
                SECOND_STATE_BORDER_COLOR, SECOND_STATE_INNER_BORDER_COLOR,
                firstStateImageIcon, secondStateImageIcon,
                Color.BLACK, null, Color.BLACK,
                null, tooltip);
         setBlinkingColor(new Color(0x006600));
    }
}
