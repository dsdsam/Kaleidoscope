package dsdsse.dialogs.creation.project;

import adf.ui.UiUtils;
import mcln.model.MclnDoubleRectangle;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ProjectSpaceSetupPanel extends JPanel {

    static final String INIT_SIZE_ID = "Init Size Panel";
    static final String INIT_CORNERS_ID = "Init Corners Panel";

    private static final String TITLE = "   Project Rectangle Size   ";
    private static final Color TITLED_BORDER_TEXT_COLOR = new Color(0x0000AA);
    private static final Color TITLED_BORDER_LINE_COLOR = new Color(0x0000AA);


    //
    //   I n s t a n c e
    //

    private EntryChosePanel entryChosePanel;
    private ProjectSizeEntryPanel projectSizeEntryPanel;
    private ProjectCornersEntryPanel projectCornersEntryPanel;
    private CardLayout cardLayout;

    ProjectSpaceSetupPanel() {
        setOpaque(true);

        Border outerBorder = new EmptyBorder(3, 3, 3, 3);
        Border titledBorder = UiUtils.createTitledBorder(0, 10, 5, 10,
                TITLED_BORDER_LINE_COLOR, TITLE, TITLED_BORDER_TEXT_COLOR, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        Border border = BorderFactory.createCompoundBorder(outerBorder, titledBorder);
        setBorder(border);
        initContent();
    }

    /**
     *
     */
    private final void initContent() {
        JPanel cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        setLayout(new GridBagLayout());
        projectSizeEntryPanel = ProjectSizeEntryPanel.createProjectSizeEntryPanel();
        projectCornersEntryPanel = ProjectCornersEntryPanel.createProjectSpaceEntryPanel();
        entryChosePanel = new EntryChosePanel(cardPanel);

        cardPanel.add(projectSizeEntryPanel, ProjectSpaceSetupPanel.INIT_SIZE_ID);
        cardPanel.add(projectCornersEntryPanel, ProjectSpaceSetupPanel.INIT_CORNERS_ID);

        cardLayout.first(cardPanel);

        add(cardPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

//        add(entryChosePanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
//                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//                new Insets(0, 0, 0, 0), 0, 0));

    }

    public final void setProjectSpaceRectangle(MclnDoubleRectangle mclnDoubleRectangle) {
        projectSizeEntryPanel.setProjectSpaceRectangle(mclnDoubleRectangle);
        projectCornersEntryPanel.setProjectSpaceRectangle(mclnDoubleRectangle);
    }

    final MclnDoubleRectangle getProjectSpaceRectangle() {
        MclnDoubleRectangle mclnDoubleRectangle;
        if (entryChosePanel.isActivePanelWidthAndHeight()) {
            mclnDoubleRectangle = projectSizeEntryPanel.getProjectSpaceRectangle();
        } else {
            mclnDoubleRectangle = projectCornersEntryPanel.getProjectSpaceRectangle();
        }
        return mclnDoubleRectangle;
    }
}
