package dsdsse.dialogs.creation.project;

import adf.utils.BuildUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static dsdsse.app.DSDSSEApp.BASE_IMAGE_LOCATION;

public final class ProjectAttributesInitDialog extends JPanel {

    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 400;
    private static final Dimension DIALOG_SIZE = new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT);
    private static final String DIALOG_TITLE = "Project Attributes Setup Dialog";
    private static final String HEADER_LABEL_TEXT = "Project Name:";

    private static final Color BACKGROUND = Color.WHITE;
    private static final ImageIcon imageIcon = BuildUtils.getImageIcon(BASE_IMAGE_LOCATION + "title-logo.png");

    public static ProjectAttributesInitDialog createInstance() {
        ProjectAttributesInitDialog projectAttributesInitDialog = new ProjectAttributesInitDialog();
        return projectAttributesInitDialog;
    }

    //
    //   I n s t a n c e
    //

    private JDialog dialog;
    private final JLabel headerLabel = new JLabel(HEADER_LABEL_TEXT, JLabel.CENTER);

    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            resetSize();
        }

        private void resetSize() {
            dialog.setSize(DIALOG_SIZE);
        }
    };

    /**
     *
     */
    private ProjectAttributesInitDialog() {
        Border outerBorder = BorderFactory.createEmptyBorder(1, 1, 0, 0);
        Border innerBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border border = BorderFactory.createCompoundBorder(outerBorder, innerBorder);
        setBorder(border);
        setOpaque(true);
        setBackground(BACKGROUND);

        initDialog();
    }

    /**
     *
     */
    public void initDialog() {
        setLayout(new GridBagLayout());
        ProjectAttributesSetupPanel projectAttributesSetupPanel = new ProjectAttributesSetupPanel();
        EntryChosePanel entryChosePanel = new EntryChosePanel();

        add(headerLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 10, 0, 0), 0, 0));

        add(projectAttributesSetupPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        add(entryChosePanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     *
     */
    public void showDialog() {
        dialog = new JDialog((JFrame) null, DIALOG_TITLE);
        dialog.setPreferredSize(DIALOG_SIZE);
        dialog.setMinimumSize(DIALOG_SIZE);
        dialog.setModal(true);
        dialog.add(this);
        if (imageIcon != null) {
            Image image = imageIcon.getImage();
            dialog.setIconImage(image);
        }
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addMouseListener(mouseAdapter);
        dialog.setVisible(true);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProjectAttributesInitDialog projectAttributesInitDialog = ProjectAttributesInitDialog.createInstance();
            projectAttributesInitDialog.showDialog();
        });
    }
}
