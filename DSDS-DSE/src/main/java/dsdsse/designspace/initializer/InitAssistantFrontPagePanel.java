package dsdsse.designspace.initializer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 6/26/2016.
 */
public class InitAssistantFrontPagePanel extends JPanel {

    private static final int NOTE_LABEL_WIDTH = 400;
    private static final int NOTE_LABEL_HEIGHT = 500;
    private static final Dimension NOTE_LABEL_SIZE = new Dimension(NOTE_LABEL_WIDTH, NOTE_LABEL_HEIGHT);

    static final InitAssistantFrontPagePanel getInstance() {
        return new InitAssistantFrontPagePanel();
    }

    String FRONT_PAGE_TEXT = new StringBuilder().
            append("<html>").
            append("<div style=\"font-family:Times New Roman;font-size:11px; font-weight: plain;").
            append(" color:#000066;  text-align:justify; width:300px; \">").

            append("The").append("&nbsp;").
            append("<font size=\"12pt\" color=\"#AA0000\">Initialization&nbsp;Assistant</font>").append("&nbsp;").

            append("<font  color=\"#000066\" >").
            append("is the tool that leads you through the steps of initialization ").
            append("of the McLN Model Property nodes and Arcs.").
            append("<br>").
            append("<br>").
            append("The Property is initialized in 3 or 4 steps:").
            append("<ol>").
            append("<li>Selecting McLN States Palette and selecting from the Palette the list of Property's Allowed States.").
            append("<li>Assigning to the Property and the Property's Allowed States their interpretations.").
            append(" On this step you will define modeled by the Property system component and the component's property names,").
            append(" as well as specify optional interpretations for each of the Property's Allowed States.").
            append("<li>Assigning one of the Property's Allowed States as the Property's Initial State.").
            append("<li>Creating the Property's Input Generator (optional step).").
            append("</ol>").
            append("The initialization of the Arc requires only one step: assigning one of the ").
            append("Property's Allowed States as the Arc's Recognized or Generated State.").
            append("<br>").
            append("<br>").
            append("Please pick up a Property or on Arc by clicking on it in the Design Space.").

            append("</div>").append("</html>").toString();

    private JLabel noteLabel = new JLabel("ABS", JLabel.CENTER);

    private InitAssistantFrontPagePanel() {
        super.setLayout(new GridBagLayout());
        setBackground(InitAssistantUIColorScheme.FRONT_PAGE_BACKGROUND);
        noteLabel.setSize(NOTE_LABEL_SIZE);
        noteLabel.setPreferredSize(NOTE_LABEL_SIZE);
        noteLabel.setText(FRONT_PAGE_TEXT);
//        noteLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        add(noteLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    }
}

