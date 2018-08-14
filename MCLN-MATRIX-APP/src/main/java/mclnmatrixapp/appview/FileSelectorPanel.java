package mclnmatrixapp.appview;

import mclnmatrixapp.app.AppMatrixViewMainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FileSelectorPanel extends JPanel {

    private static final Dimension COMBO_BOX_SIZE = new Dimension(240, 22);
    private static final String MODEL_FILE_PREFIX = "Model file: ";
    private static final int FONT_SIZE = 4;
    private static StringBuilder stringBuilder = new StringBuilder();

    private static final String createLabelText(String fileName) {
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append("<html><p>");
        stringBuilder.append("<font color=\"#800080\" size=\"" + FONT_SIZE + "\">");
        stringBuilder.append(MODEL_FILE_PREFIX);
        stringBuilder.append("</font>");
        stringBuilder.append("&nbsp;&nbsp;");
        stringBuilder.append("<font color=\"#020080\" size=\"" + FONT_SIZE + "\">");
        stringBuilder.append(fileName);
        stringBuilder.append("</font>");
        stringBuilder.append("</p></html>");
        return stringBuilder.toString();
    }

    //
    //   I n s t a n c e
    //

    private JLabel fileNameLabel = new JLabel(MODEL_FILE_PREFIX, JLabel.LEFT);
    private JLabel modelSelectorLabel = new JLabel("Select a model:", JLabel.RIGHT);
    private JComboBox fileSelectionComboBox;
    private ActionListener comboBoxActionListener;

    private final ActionListener comboBoxLocalActionListener = (e) -> {
        JComboBox source = (JComboBox) e.getSource();
        int fileIndex = source.getSelectedIndex();
        String filePath = AppMatrixViewMainPanel.OPTION_TO_FILE_PATH[fileIndex];
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String labelText = FileSelectorPanel.createLabelText(fileName);
        fileNameLabel.setText(labelText);
        comboBoxActionListener.actionPerformed(e);
    };

    /**
     * @param selectionOptions
     * @param comboBoxActionListener
     */
    public FileSelectorPanel(String[] selectionOptions, ActionListener comboBoxActionListener) {
        super(new GridBagLayout());
        initContentAndLayout(selectionOptions);
        this.comboBoxActionListener = comboBoxActionListener;
        fileSelectionComboBox.addActionListener(comboBoxLocalActionListener);
    }

    private void initContentAndLayout(String[] selectionOptions) {
        fileSelectionComboBox = new JComboBox(selectionOptions);
        fileSelectionComboBox.setPreferredSize(COMBO_BOX_SIZE);
        ((JLabel) fileSelectionComboBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        String filePath = AppMatrixViewMainPanel.DEFAULT_MODEL_RELATIVE_FILE_PATH;
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String labelText = FileSelectorPanel.createLabelText(fileName);
        fileNameLabel.setText(labelText);
        add(fileNameLabel,
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(0, 10, 5, 0), 0, 0));
        JPanel placeHolder = new JPanel();
//placeHolder.setOpaque(true);
//placeHolder.setBackground(Color.YELLOW);
        add(placeHolder,
                new GridBagConstraints(1, 0, 1, 1, 1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 5, 0), 0, 0));

        modelSelectorLabel.setFont(modelSelectorLabel.getFont().deriveFont(Font.BOLD, 14));
        add(modelSelectorLabel,
                new GridBagConstraints(2, 0, 1, 1, 0, 1,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(0, 0, 5, 10), 0, 0));
        add(fileSelectionComboBox,
                new GridBagConstraints(3, 0, 1, 1, 0, 1,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(0, 0, 5, 10), 0, 0));
    }
}
