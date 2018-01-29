package dsdsse.designspace.initializer;

import adf.ui.laf.combobox.ComboBox3D;
import mcln.palette.BasicColorPalette;
import mcln.palette.MclnPaletteFactory;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Admin on 3/16/2016.
 */
final class InitAllowedStatesPage extends JPanel {

    private static final String[] PALETTES = {
            MclnPaletteFactory.BLACK_AND_WHITE_MONOCHROME_PALETTE,
            MclnPaletteFactory.BASIC_COLORS_PALETTE + "  (default)",
            MclnPaletteFactory.TWO_SHADES_CONFETTI_PALETTE,
            MclnPaletteFactory.THREE_SHADES_CONFETTI_PALETTE,
            MclnPaletteFactory.PAIRS_OF_OPPOSITE_STATES_PALETTE};

    private static final Dimension LABEL_SIZE = new Dimension(250, 16);
    private static final Dimension COMBO_SIZE = new Dimension(250, 23);
// DC1A0B
    private static final String PLEASE_NOTE_LABEL_TEXT = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#AC1A0B; text-align:justify; ").
            append(" width:198px; \">").
            append("<b>Please&nbsp;Notice&nbsp;! </b>Selected on this page Property States Palette is the foundation ").
            append("for all further settings.").
            append(" Hence, returning to this page and re-selecting the Palette after some or all initialization").
            append(" steps are completed, will cause all done settings to be discarded and all the steps you need have to be repeated.").
            append(" All associated with the Property Arcs are supposed to be re-initialised as well.").
            append("</div></html>").toString();

    private final InitAssistantController initAssistantController;
    private final InitAssistantDataModel initAssistantDataModel;
    private final JLabel paletteSelectorLabel = new JLabel("Selected Property States Palette:", JLabel.LEFT);
    private final JLabel pleaseNoteLabel = new JLabel(PLEASE_NOTE_LABEL_TEXT, JLabel.LEFT);

    InitAllowedStatesPage(InitAssistantController initAssistantController,
                          InitAssistantDataModel initAssistantDataModel) {
        super(new BorderLayout());
        this.initAssistantController = initAssistantController;
        this.initAssistantDataModel = initAssistantDataModel;
        setOpaque(true);
        setBackground(InitAssistantUIColorScheme.CONTAINER_PANEL_BACKGROUND);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15), BorderFactory.createEtchedBorder()
        );
        setBorder(border);
        initContents();
    }

    private JPanel initContents() {

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(true);
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(InitAssistantUIColorScheme.INPUT_PANEL_BACKGROUND);

        paletteSelectorLabel.setPreferredSize(LABEL_SIZE);
        setCompFontSize(paletteSelectorLabel, Font.BOLD, 11);
        paletteSelectorLabel.setForeground(Color.BLUE);

        JComboBox paletteSelectionComboBox = new ComboBox3D(PALETTES);
        if (!initAssistantDataModel.isInitializingArc()) {
            String mclnStatesPaletteName = initAssistantDataModel.getMclnStatesPaletteName();
            if (BasicColorPalette.PALETTE_NAME.equalsIgnoreCase(mclnStatesPaletteName)) {
                mclnStatesPaletteName += "  (default)";
            }
            paletteSelectionComboBox.setSelectedItem(mclnStatesPaletteName);
        }

        paletteSelectionComboBox.setPreferredSize(COMBO_SIZE);
        paletteSelectionComboBox.setMinimumSize(COMBO_SIZE);
        paletteSelectionComboBox.setMaximumSize(COMBO_SIZE);
        paletteSelectionComboBox.addItemListener(initAssistantController.getPaletteSelectionComboBoxItemListener());


//        mainPanel.add(Box.createVerticalStrut(1), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
//                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(paletteSelectorLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 0), 0, 0));

        mainPanel.add(paletteSelectionComboBox, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(pleaseNoteLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0));

        mainPanel.add(Box.createGlue(), new GridBagConstraints(0, 4, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        this.add(mainPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private final void setCompFontSize(Component comp, int style, int fontSize) {
        Font font = comp.getFont();
        comp.setFont(new Font(font.getName(), style, fontSize));
    }
}
