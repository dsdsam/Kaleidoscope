package dsdsse.dialogs.creation.project;

import adf.utils.BuildUtils;
import mcln.model.MclnDoubleRectangle;
import mcln.model.ProjectAttributes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static dsdsse.app.DSDSSEApp.BASE_IMAGE_LOCATION;

public final class ProjectAttributesSetupMainPanel extends JPanel {

    private static final int DIALOG_WIDTH = 400;
    private static final int DIALOG_HEIGHT = 300;
    private static final Dimension DIALOG_SIZE = new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT);

    private static final String MESSAGE_LABEL_TEXT_INVALID_PROJECT_NANE =
            "Project Name can not be empty";
    private static final String MESSAGE_LABEL_TEXT_INVALID_PROJECT_SPACE =
            "The Width and Height values should be a number bigger than 1.0";
    private static final MessageLabel messageLabel = MessageLabel.getMessageLabel();

    static final Color ENTRY_FIELD_BACKGROUND = new Color(0xFFFFF8);

    private static final ImageIcon imageIcon = BuildUtils.getImageIcon(BASE_IMAGE_LOCATION + "title-logo.png");

    public static ProjectAttributesSetupMainPanel createInstance(JFrame parentFrame, ProjectAttributes projectAttribute) {
        ProjectAttributesSetupMainPanel projectAttributesSetupMainPanel =
                new ProjectAttributesSetupMainPanel(parentFrame, projectAttribute);
        return projectAttributesSetupMainPanel;
    }

    //
    //   I n s t a n c e
    //

    private JDialog dialog;

    private final MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            resetSize();
        }

        private void resetSize() {
            dialog.setSize(DIALOG_SIZE);
        }
    };

    private final Action cancelAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            discardDialog();
        }
    };

    private final Action applyAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            applyAttributes();
        }
    };

    private final ProjectNameEntryPanel projectNameEntryPanel =
            ProjectNameEntryPanel.createProjectNameEntryPanel();
    private final ProjectSpaceSetupPanel projectSpaceSetupPanel = new ProjectSpaceSetupPanel();
    private final ProjectSetupButtonsPanel projectSetupButtonsPanel =
            ProjectSetupButtonsPanel.createButtonsPanel(cancelAction, applyAction);

    private final JFrame parentFrame;
    private ProjectAttributes suggestedProjectAttributes;
    private ProjectAttributes projectAttributesToReturn;

    /**
     *
     */
    private ProjectAttributesSetupMainPanel(JFrame parentFrame, ProjectAttributes projectAttributes) {
        this.parentFrame = parentFrame;
        suggestedProjectAttributes = projectAttributes;
        Border outerBorder = BorderFactory.createEmptyBorder(1, 1, 0, 0);
        Border innerBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border border = BorderFactory.createCompoundBorder(outerBorder, innerBorder);
        setBorder(border);
        initDialog();
    }

    /**
     *
     */
    public final void initDialog() {
        setLayout(new GridBagLayout());

        projectNameEntryPanel.setProjectName(suggestedProjectAttributes.getProjectName());
        projectSpaceSetupPanel.setProjectSpaceRectangle(suggestedProjectAttributes.getMclnDoubleRectangle());

        add(projectNameEntryPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 0, 10, 0), 0, 0));

        add(messageLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 3, 0, 3), 0, 0));

        add(projectSpaceSetupPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 0, 15, 0), 0, 0));

        add(projectSetupButtonsPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 3, 3, 3), 0, 0));
    }

    /**
     * @param parentFrame
     * @param title
     * @return
     */
    public final ProjectAttributes showInitProjectAttributesDialog(JFrame parentFrame, String title) {
        dialog = new JDialog(parentFrame, title);
        dialog.setPreferredSize(DIALOG_SIZE);
        dialog.setMinimumSize(DIALOG_SIZE);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.add(this);
        if (imageIcon != null) {
            Image image = imageIcon.getImage();
            dialog.setIconImage(image);
        }
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addMouseListener(mouseAdapter);

        dialog.setVisible(true);
        return projectAttributesToReturn;
    }

    /**
     * @param parentFrame
     * @param title
     * @return
     */
    public final ProjectAttributes showChangeProjectAttributesDialog(JFrame parentFrame, String title) {
        dialog = new JDialog(parentFrame, title);
        dialog.setPreferredSize(DIALOG_SIZE);
        dialog.setMinimumSize(DIALOG_SIZE);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.add(this);
        if (imageIcon != null) {
            Image image = imageIcon.getImage();
            dialog.setIconImage(image);
        }
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addMouseListener(mouseAdapter);

        dialog.setVisible(true);
        return projectAttributesToReturn;
    }

    /**
     *
     */
    private final void discardDialog() {
        dialog.dispose();
    }

    private final void applyAttributes() {
        String projectName = projectNameEntryPanel.getProjectName();
        MclnDoubleRectangle mclnDoubleRectangle = projectSpaceSetupPanel.getProjectSpaceRectangle();
        ProjectAttributes projectAttributes = new ProjectAttributes(projectName, mclnDoubleRectangle);
        if (!projectAttributes.isProjectNameValid()) {
            messageLabel.showMessage(MESSAGE_LABEL_TEXT_INVALID_PROJECT_NANE);
            return;
        }
        if (!projectAttributes.isProjectSpaceValid()) {
            messageLabel.showMessage(MESSAGE_LABEL_TEXT_INVALID_PROJECT_SPACE);
            return;
        }
        projectAttributesToReturn = projectAttributes;
        discardDialog();
    }
}
