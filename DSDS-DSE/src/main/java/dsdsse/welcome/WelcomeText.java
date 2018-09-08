package dsdsse.welcome;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Admin on 3/30/2017.
 */
final class WelcomeText extends JPanel {

    private static Dimension TEXT_SIZE = new Dimension(600, 680);


    private static final String WELCOME_HTML = new StringBuilder().
            append("<html>").
//            append("<div style=\"font-size:19px; font-weight: plain; color:#780000; text-align:center; ").
        append("<div style=\"font-size:14px; font-weight: plain; color:#AA0000; text-align:center; ").
                    append(" \">").
                    append("Welcome to Discrete Symbolic Dynamical Systems<br> ").
                    append("Development and Simulating Environment<br> ").
                    append("(DSDS) (DSE)<br> ").
                    append("</div></html>").toString();

    private static final String INFO_HTML = new StringBuilder().
            append("<html>").
//            append("<div style=\"font-size:14px; font-weight: plain; color:#780000; text-align:justify; ").
        append("<div style=\" font-size:12px; font-weight: plain;    color:#550000; text-align:justify;   ").
//            append("<div  style=\" font-family:family:calibrib; font-size:13px; font-weight: plain; color:#550000; text-align:justify;  ").
//            append("<div style=\"font-family:family:Roboto-Italic; font-size:13px;    color:#550000; text-align:justify; \"  ").
//            append("<div style=\"font-size:13px; font-weight: plain; color:#333344; text-align:justify; ").
        append(" \">").
                    append("The DSDS DSE is a tool to build Discrete Symbolic Dynamical Systems").
                    append(" graphical models and simulate their behavior over time.").
                    append(" Hence, the Environment may be set to work in either Development or Simulation mode.").
                    append(" Please use \"Development\" or \"Simulation\" menu to toggle between the modes.<br>").
                    append("<br>").
                    append(" When started, the Environment is in Development mode.").
                    append(" The Multicolored Logical Net (McLN) modeling formalism is used to represent the model.").
                    append(" \"Project\" menu has general operations, like \"Save As\", \"Retrieve\" and on.").
                    append(" \"Examples\" menu suggests operations to generate several predefined McLN models.").
                    append(" These models can be edited or executed.").
                    append(" The groups of menu items under \"Creation\" and \"Modification\"" +
                            " menus are the key DSDS model creation operations.").
                    append("<br><br>").
                    append("The Simulation mode has four buttons on the toolbar.").
                    append(" \"Start\" and \"Stop\" buttons start and stop simulation process respectively.").
                    append(" \"Step\" button is used to execute one step of the simulation at a time.").
                    append(" \"Reset\" button resets the model to its initial state.").
                    append("<br><br>").
                    append("Please use \"Preferences\" menu to open DSE Preferences Setup panel and choose").
                    append(" available configuration options.").
//            append("<br>").
        append(" Follow \"Help\" menu for more detailed DSE descriptions and instructions.").
                    append("</div></html>").toString();

    private static final String TURN_OFF_HTML = new StringBuilder().
            append("<html>").
//            append("<div style=\"font-size:10px; font-weight: plain; color:#787878; text-align:justify; ").
        append("<div style=\"font-size:11px; font-weight: plain; color:#000050; text-align:justify; ").
                    append(" \">").
                    append("The option to turn this popup off is under \"Preferences\" menu.").
                    append("</div></html>").toString();

    private JLabel welcomeLabel = new JLabel(WELCOME_HTML, JLabel.CENTER);
    private JLabel infoLabel = new JLabel(INFO_HTML, JLabel.CENTER);
    //    private JTextArea infoLabel = new JTextArea(INFO_HTML);
    private JLabel turnOffLabel = new JLabel(TURN_OFF_HTML, JLabel.RIGHT);
/*
    // You should execute this part on the Event Dispatch Thread
// because it modifies a Swing component
    JFXPanel jfxPanel = new JFXPanel();
    jFrame.add(jfxPanel);

// Creation of scene and future interactions with JFXPanel
// should take place on the JavaFX Application Thread
    Platform.runLater(() -> {
        WebView webView = new WebView();
        jfxPanel.setScene(new Scene(webView));
        webView.getEngine().load("http://www.stackoverflow.com/");
    });
*/

    /**
     *
     */
    WelcomeText() {
        super(new GridBagLayout());
        setBorder(null);

        JPanel fixedSizePanel = new JPanel(new GridBagLayout());
        fixedSizePanel.setOpaque(false);
        fixedSizePanel.setBorder(new EmptyBorder(0, 0, 5, 0));
        fixedSizePanel.setPreferredSize(TEXT_SIZE);
        fixedSizePanel.setMinimumSize(TEXT_SIZE);
        fixedSizePanel.setMaximumSize(TEXT_SIZE);

        fixedSizePanel.add(welcomeLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));

        fixedSizePanel.add(infoLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20, 0, 0, 0), 0, 0));

        Component verticalSpaceHolder = Box.createVerticalGlue();
        fixedSizePanel.add(verticalSpaceHolder, new GridBagConstraints(0, 10, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        fixedSizePanel.add(turnOffLabel, new GridBagConstraints(0, 11, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(fixedSizePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
}
