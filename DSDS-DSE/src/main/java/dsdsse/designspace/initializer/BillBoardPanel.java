package dsdsse.designspace.initializer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 4/20/2016.
 */
final class BillBoardPanel extends JPanel {

    private static final String FONT_FAMILY_NAME_LUCIDA = "Lucida";
    private static final String FONT_FAMILY_NAME_TIMES = "Times new Roman";
    private static final String FONT_FAMILY_NAME_CALIBRI = "Calibri";

    private static final String PROPERTY_STEP_1_TEXT = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#780000; text-align:justify; ").
            append(" width:150px; \">").
            append("On this page you set up the set of states the Property is ").
            append("allowed to take. Select one of the predefined ").
            append("Allowed States Palettes. Selected palette is shown in Palette table below.").
            append(" You create Allowed States set by ticking or un-ticking checkboxes on the table rows.").
            append(" Ticked rows define the Property Allowed States.").
            append("</div></html>").toString();


    private static final String PROPERTY_STEP_2_TEXT = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#780000; text-align:justify; ").
            append(" width:150px; \">").
            append("On this page you enter modeled component and property names.").
            append("The table below is the table of Allowed State.").
            append("The State Interpretation column of the table represents states default interpretations. ").
            append("The component and property names concatenated with state interpretation ").
            append("constitute statement that describes component state. Examples are on the information bar. ").
            append("</div></html>").toString();


    private static final String PROPERTY_STEP_3_TEXT = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#780000; text-align:justify; ").
            append(" width:150px; \">").
            append("Set up the Property Initial State by picking it up from the ").
            append("Allowed States table below. ").

            append("Then tick (or un-tick) checkbox \"The Property has Input Generator\" if the Property ").
            append("is supposed to simulate its state transitions in response to external affects. ").
            append("</div></html>").toString();


    private static final String PROPERTY_TD_STEP_4_TEXT = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#780000; text-align:justify; ").
            append(" width:150px; \">").
            append("Define the Time-Driven Input Simulating Program (ISP). ").
            append("Setup the couples: \"Ticks-State\". Use \"Append\", \"Insert\" ").
            append("and \"Remove\" buttons to add or delete program steps. ").
            append("You can specify that the ISP has phase by checking the ").
            append("check-box. For more details about Time-Driven ISP see ").
            append("help under Help menu.").
            append("</div></html>").toString();


    private static final String PROPERTY_RD_STEP_4_TEXT = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#780000; text-align:justify; ").
            append(" width:150px; \">").
            append("Define the Rule-Driven Input Simulating Program (ISP). ").
            append("Setup the triplets: \"Condition-Ticks-State\". Use \"Append\" ").
            append("\"Insert\" and \"Remove\" buttons to add or delete program ").
            append("steps. For more details about Rule-Driven ISP see help ").
            append("under Help menu.").
            append("</div></html>").toString();

    private static final String PROPERTY_INIT_COMPLETED_TITLE = "The Property initialization completed.";
    private static final String PROPERTY_INIT_COMPLETED_TEXT = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#780000; text-align:justify; ").
            append(" width:150px; \">").
            append("The Property initialization completed. ").
            append("Use Apply button to save the Property's attributes, ").
            append("or Cancel the initialization. ").
            append("</div></html>").toString();

    private static final String ARC_STEP_1_OF_1_TITLE = new StringBuilder().
            append("<html>").
            append("<font color=white>Step</font>").
            append("<font color=\"#FFCC00\">&nbsp;&nbsp;1&nbsp;&nbsp;</font>").
            append("<font color=white>of</font>").
            append("<font color=\"#FFCC00\">&nbsp;&nbsp;1&nbsp;&nbsp;</font>").
            append("</html>").toString();
    private static final String ARC_STEP_1_TEXT = new StringBuilder().
            append("<html>").
            append("<div style=\"font-size:8px; font-weight: plain; color:#780000; text-align:justify; ").
            append(" width:150px; \">").
            append("Set up the Arc conductivity by selecting it from the ").
            append("Allowed States table below, then push \"Apply\" ").
            append("button. ").
            append("</div></html>").toString();

    //
    //   I n s t a n c e
    //

    private final Map<InitAssistantController.NoteID, String> billboardPageIDtoNoteMap =
            new HashMap();

    {
        billboardPageIDtoNoteMap.put(
                InitAssistantController.NoteID.SelectAllowedStates, PROPERTY_STEP_1_TEXT);
        billboardPageIDtoNoteMap.put(
                InitAssistantController.NoteID.InitInterpretation, PROPERTY_STEP_2_TEXT);
        billboardPageIDtoNoteMap.put(
                InitAssistantController.NoteID.initInitialState, PROPERTY_STEP_3_TEXT);
        billboardPageIDtoNoteMap.put(
                InitAssistantController.NoteID.InitTimeDrivenGenerator, PROPERTY_TD_STEP_4_TEXT);
        billboardPageIDtoNoteMap.put(
                InitAssistantController.NoteID.InitRuleDrivenGenerator, PROPERTY_RD_STEP_4_TEXT);
        billboardPageIDtoNoteMap.put(
                InitAssistantController.NoteID.PropertyCompleted, PROPERTY_INIT_COMPLETED_TEXT);
        billboardPageIDtoNoteMap.put(
                InitAssistantController.NoteID.InitArcState, ARC_STEP_1_TEXT);
    }


    private Color bgColor = Color.LIGHT_GRAY;

    private final Border border = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 0,
            Color.WHITE), new EmptyBorder(0, 12, 0, 0));
    private final JLabel titleBarStepLabel = new JLabel();
    private final JPanel titleBar = new JPanel();
    private final JLabel noteLabel = new JLabel();

    private static int maxPages = 0;

    private NoteBigPanel noteBigPanel = new NoteBigPanel();
    private InitAssistantController.NoteID currentNoteID;

    /**
     * Listens to Has Program checkbox selection
     */
    private final InitAssistantDataModelListener initAssistantDataModelListener =
            (InitAssistantDataModel initAssistantDataModel,
             InitAssistantDataModel.AttributeID attributeID, boolean initialized) -> {
                switch (attributeID) {
                    case HasProgram:
                        maxPages = initAssistantDataModel.propertyHasProgram() ? 4 : 3;
                        updateNoteTitle(currentNoteID);
                        break;
                }


            };

    private ShowNoteRequestListener showNoteRequestListener = noteID -> {
        if (noteID == InitAssistantController.NoteID.ToggleContext) {
            return;
        }
        updateNoteTitle(noteID);
        String noteText = billboardPageIDtoNoteMap.get(noteID);
        noteLabel.setText(noteText);
        currentNoteID = noteID;
    };

//
//   C r e a t i o n
//

    BillBoardPanel(InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel) {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(InitAssistantUIColorScheme.CONTAINER_PANEL_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        if (initAssistantDataModel.isInitializingProperty()) {
            maxPages = initAssistantDataModel.propertyHasProgram() ? 4 : 3;
        }

        add(initNotePanel(), BorderLayout.CENTER);

        initAssistantController.addListener(showNoteRequestListener);
        initAssistantDataModel.addListener(initAssistantDataModelListener);
    }

    /**
     *
     */
    private void updateNoteTitle(InitAssistantController.NoteID noteID) {
        String noteTitle;
        switch (noteID) {
            case SelectAllowedStates:
                noteTitle = new StringBuilder().
                        append("<html>").
                        append("<font color=white>Step</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;1&nbsp;&nbsp;</font>").
                        append("<font color=white>of</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;").append(maxPages).append("&nbsp;&nbsp;</font>").
                        append("</html>").toString();
                break;
            case InitInterpretation:
                noteTitle = new StringBuilder().
                        append("<html>").
                        append("<font color=white>Step</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;2&nbsp;&nbsp;</font>").
                        append("<font color=white>of</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;").append(maxPages).append("&nbsp;&nbsp;</font>").
                        append("</html>").toString();
                break;
            case initInitialState:
                noteTitle = new StringBuilder().
                        append("<html>").
                        append("<font color=white>Step</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;3&nbsp;&nbsp;</font>").
                        append("<font color=white>of</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;").append(maxPages).append("&nbsp;&nbsp;</font>").
                        append("</html>").toString();

                break;
            case InitTimeDrivenGenerator:
                noteTitle = new StringBuilder().
                        append("<html>").
                        append("<font color=white>Step</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;4&nbsp;&nbsp;</font>").
                        append("<font color=white>of</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;").append(maxPages).append("&nbsp;&nbsp;</font>").
                        append("</html>").toString();
                break;
            case InitRuleDrivenGenerator:
                noteTitle = new StringBuilder().
                        append("<html>").
                        append("<font color=white>Step</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;4&nbsp;&nbsp;</font>").
                        append("<font color=white>of</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;").append(maxPages).append("&nbsp;&nbsp;</font>").
                        append("</html>").toString();
                break;
            case PropertyCompleted:
                noteTitle = "The Property initialization completed.";
                break;
            case InitArcState:
                noteTitle = new StringBuilder().
                        append("<html>").
                        append("<font color=white>Step</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;1&nbsp;&nbsp;</font>").
                        append("<font color=white>of</font>").
                        append("<font color=\"#FFCC00\">&nbsp;&nbsp;1&nbsp;&nbsp;</font>").
                        append("</html>").toString();
                break;
            default:
                noteTitle = "";
                break;
        }
        titleBarStepLabel.setText(noteTitle);
    }

    /**
     * @return
     */
    private JPanel initNotePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        mainPanel.setLayout(new BorderLayout());
        noteBigPanel = new NoteBigPanel();
        mainPanel.add(noteBigPanel);
        return mainPanel;
    }

    /**
     * N o t e    P a n e l
     */
    private class NoteBigPanel extends JPanel {

        private final Color hc = new Color(0xFFCC00);
        private final Color NOTE_AREA_BACKGROUND = new Color(00, 130, 00);
        private final Dimension TITLE_BAR_SIZE = new Dimension(300, 18);
        private final Dimension NOTE_LABEL_PANEL_SIZE = new Dimension(300, 93);

        private final JPanel noteLabelPanel;

        private final Color bg = new Color(0xFFFFEE);

        private NoteBigPanel() {
            setOpaque(false);
            setLayout(new BorderLayout());
            titleBar.setBackground(NOTE_AREA_BACKGROUND);
            titleBar.setPreferredSize(TITLE_BAR_SIZE);
            titleBar.setMaximumSize(TITLE_BAR_SIZE);
            titleBar.setLayout(new GridBagLayout());
            titleBar.setBorder(BorderFactory.createEmptyBorder(2, 20, 1, 0));

            titleBarStepLabel.setBorder(border);
            setConfigureLabelFont(titleBarStepLabel, Font.BOLD, 11);
            titleBar.add(titleBarStepLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));

            titleBar.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

            noteLabelPanel = new JPanel(new GridBagLayout());
            noteLabelPanel.setOpaque(false);
            noteLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 2, 6));

            noteLabelPanel.setPreferredSize(NOTE_LABEL_PANEL_SIZE);
            noteLabelPanel.setMaximumSize(NOTE_LABEL_PANEL_SIZE);
            noteLabelPanel.setMinimumSize(NOTE_LABEL_PANEL_SIZE);

            noteLabelPanel.add(noteLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

            add(titleBar, BorderLayout.NORTH);
            add(noteLabelPanel, BorderLayout.CENTER);
        }

        /**
         * P A I N T I N G
         *
         * @param g
         */
        public void paint(Graphics g) {
            Dimension size = getSize();
            int width = size.width - 1;
            int height = size.height - 1;
            if (height < 20)
                return;

            float percent = 0.03f;
            int d = (int) (((float) width) * percent);
            int[] x = {d, width, width, 0, 0};
            int[] y = {0, 0, height, height, d};
            g.setColor(bg);
            g.fillPolygon(x, y, 5);
            super.paint(g);

            int[] x2 = {d, d, 0};
            int[] y2 = {0, d, d};
            g.setColor(bg);
            g.fillPolygon(x2, y2, 3);

            int[] x1 = {0, d, 0};
            int[] y1 = {0, 0, d};
            g.setColor(bgColor);
            g.fillPolygon(x1, y1, 3);

            g.setColor(Color.gray);
            g.drawLine(d, 0, width, 0);
            g.drawLine(width, 0, width, height);
            g.drawLine(0, height, width, height);
            g.drawLine(0, d, 0, height);
            g.drawLine(0, d, d, d);
            g.drawLine(d, d, d, 0);
            g.drawLine(0, d, d, 0);
        }

        /**
         * @param comp
         * @param style
         * @param fontSize
         */
        private void setConfigureLabelFont(Component comp, int style, int fontSize) {
            Font font = comp.getFont();
            comp.setFont(new Font(font.getName(), style, fontSize));
        }
    }
}
