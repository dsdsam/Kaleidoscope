package dsdsse.dialogs.creation.project;

import adf.ui.UiUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProjectAttributesSetupPanel extends JPanel{

    private static final String TITLE = "  Project Rectangle Attributes  ";
    private static final Color TITLED_BORDER_LINE_COLOR = Color.GRAY;
    private static final Color TITLED_BORDER_TEXT_COLOR = Color.DARK_GRAY;

    //
    //   I n s t a n c e
    //

    ProjectAttributesSetupPanel(){
        setOpaque(false);

        Border outerBorder = new EmptyBorder(3, 3, 3, 3);
        Border titledBorder = UiUtils.createTitledBorder(10, 10, 10, 10,
                TITLED_BORDER_LINE_COLOR, TITLE, TITLED_BORDER_TEXT_COLOR);
        Border border = BorderFactory.createCompoundBorder(outerBorder, titledBorder);
        setBorder(border);
    }
}
