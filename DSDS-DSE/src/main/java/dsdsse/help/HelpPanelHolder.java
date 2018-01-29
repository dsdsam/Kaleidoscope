package dsdsse.help;

import adf.app.StandardFonts;
import adf.ui.laf.button.Adf3DButton;
import dsdsse.animation.HowToUseCreateOperationsScript;
import dsdsse.animation.HowToUseModificationOperationsScript;
import dsdsse.animation.HowToUseSimulationOperationsScript;
import dsdsse.app.AppController;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 8/29/2016.
 */
public class HelpPanelHolder extends JPanel {

    private static final String HELP_HTML_PREFIX = "/dsdsse-resources/htmls/";

    public enum HelpMenuItems {
        HelpStructure, WhatIsDSDSSE, RunningExamples, MenuCommands, WhatIsModelCreator, SimpleProject, WhatIsSimEng,
        WhatIsTimeDrivenIG, WhatIsStateDrivenIG
    }

    private static final String START_CREATION_PRESENTATION = "Start Creation Presentation";
    private static final String START_MODIFICATION_PRESENTATION = "Start Modification Presentation";
    private static final String START_SIMULATION_PRESENTATION = "Start Simulation Presentation";

    private static final String WHAT_IS_DSDSSE_TITLE = "Help:    " + AppController.MENU_ITEM_WHAT_IS_DSDS_DSE;
    private static final String HELP_TITLE_RUNNING_EXAMPLES = "Help:    " + AppController.MENU_ITEM_RUNNING_EXAMPLES;
    private static final String WHAT_IS_CREATOR_TITLE = "Help:    " + AppController.MENU_ITEM_WHAT_IS_CREATOR;
    private static final String HELP_TITLE_SIMPLE_PROJECT = "Help:    " + AppController.MENU_ITEM_SIMPLE_PROJECT;
    private static final String WHAT_IS_SIM_ENG_TITLE = "Help:    " + AppController.MENU_ITEM_WHAT_IS_SIM_ENG;
    private static final String HELP_TITLE_MENU_COMMANDS = "Help:    " + AppController.MENU_ITEM_MENU_COMMANDS;

    private static final String WHAT_IS_TD_IG_TITLE = "Help:    " + AppController.MENU_ITEM_WHAT_IS_TD_IG;
    private static final String WHAT_IS_SD_IG_TITLE = "Help:    " + AppController.MENU_ITEM_WHAT_IS_SD_IG;

    private static final String DSDSSE_HTML_FILE_NAME = "what-is-dsdsse.html";
    private static final String RUNNING_EXAMPLES_HTML_FILE_NAME = "running-model-examples.html";
    private static final String CREATOR_HTML_FILE_NAME = "what-is-model-creator.html";
    private static final String SIMPLE_PROJECT_HTML_FILE_NAME = "creating-simple-project.html";
    private static final String SIM_ENG_HTML_FILE_NAME = "what-is-simulating-engine.html";
    private static final String MENU_COMMANDS_HTML_FILE_NAME = "menu-commands.html";
    //
    private static final String TD_IG_HTML_FILE_NAME = "what-is-td-ig.html";
    private static final String SD_IG_HTML_FILE_NAME = "what-is-sd-ig.html";

    private static final String DSDSSE_HTML_CLASS_PATH = HELP_HTML_PREFIX + DSDSSE_HTML_FILE_NAME;
    private static final String RUNNING_EXAMPLES_HTML_CLASS_PATH = HELP_HTML_PREFIX + RUNNING_EXAMPLES_HTML_FILE_NAME;
    private static final String CREATOR_HTML_CLASS_PATH = HELP_HTML_PREFIX + CREATOR_HTML_FILE_NAME;
    private static final String SIMPLE_PROJECT_HTML_CLASS_PATH = HELP_HTML_PREFIX + SIMPLE_PROJECT_HTML_FILE_NAME;
    private static final String SIM_ENG_HTML_CLASS_PATH = HELP_HTML_PREFIX + SIM_ENG_HTML_FILE_NAME;
    private static final String MENU_COMMANDS_HTML_CLASS_PATH = HELP_HTML_PREFIX + MENU_COMMANDS_HTML_FILE_NAME;
    //
    private static final String TD_IG_HTML_CLASS_PATH = HELP_HTML_PREFIX + TD_IG_HTML_FILE_NAME;
    private static final String SD_IG_HTML_CLASS_PATH = HELP_HTML_PREFIX + SD_IG_HTML_FILE_NAME;


    private static final Map<String, String> pageRefToPageTitle = new HashMap();

    static {
        pageRefToPageTitle.put(DSDSSE_HTML_FILE_NAME, WHAT_IS_DSDSSE_TITLE);
        pageRefToPageTitle.put(RUNNING_EXAMPLES_HTML_FILE_NAME, HELP_TITLE_RUNNING_EXAMPLES);
        pageRefToPageTitle.put(CREATOR_HTML_FILE_NAME, WHAT_IS_CREATOR_TITLE);
        pageRefToPageTitle.put(SIMPLE_PROJECT_HTML_FILE_NAME, HELP_TITLE_SIMPLE_PROJECT);
        pageRefToPageTitle.put(SIM_ENG_HTML_FILE_NAME, WHAT_IS_SIM_ENG_TITLE);
        pageRefToPageTitle.put(MENU_COMMANDS_HTML_FILE_NAME, HELP_TITLE_MENU_COMMANDS);

        pageRefToPageTitle.put(TD_IG_HTML_FILE_NAME, WHAT_IS_TD_IG_TITLE);
        pageRefToPageTitle.put(SD_IG_HTML_FILE_NAME, WHAT_IS_SD_IG_TITLE);
    }

    private static final int THE_PANEL_MAX_WIDTH = 380;
    public static final Dimension THE_PANEL_MAX_SIZE = new Dimension(THE_PANEL_MAX_WIDTH, 1);

    private static final Color HEADER_BACKGROUND = new Color(0x0000AA);

    private static final HelpPanelHolder helpPanelHolder = new HelpPanelHolder();

    public static HelpPanelHolder getInstance() {
        return helpPanelHolder;
    }

    public static HelpPanelHolder getInitializedInstance(HelpMenuItems helpMenuItem) {
        helpPanelHolder.initHelpContent(helpMenuItem);
        return helpPanelHolder;
    }

    //
    //   I n s t a n c e
    //

    private HeaderPanel headerPanel = new HeaderPanel();
    private JPanel buttonsPanel = new ButtonsPanel();

    private JEditorPane htmlEditorPane;
    private JScrollPane scrollPane;

    /**
     *
     */
    private HelpPanelHolder() {
        super(new BorderLayout());
        initContent();
    }

    private void initContent() {

        setBorder(new MatteBorder(0, 1, 0, 0, Color.GRAY));

        setBackground(Color.GRAY);
        setMaximumSize(THE_PANEL_MAX_SIZE);
        setMinimumSize(THE_PANEL_MAX_SIZE);
        setPreferredSize(THE_PANEL_MAX_SIZE);

        htmlEditorPane = new JEditorPane();
        htmlEditorPane.setContentType("text/html");
        htmlEditorPane.setEditable(false);
        htmlEditorPane.setAutoscrolls(false);
        htmlEditorPane.addHyperlinkListener(e -> {
            if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
                return;
            }
            URL requestedPageURL = e.getURL();
            String urlStr = requestedPageURL.toExternalForm();
            int fileNameIndex = urlStr.lastIndexOf("/");
            String htmlFileName = urlStr.substring(++fileNameIndex);
            if (START_CREATION_PRESENTATION.equalsIgnoreCase(htmlFileName)) {
                AppController.getInstance().startPresentationShow(HowToUseCreateOperationsScript.SCRIPT_NAME);
                return;
            } else if (START_MODIFICATION_PRESENTATION.equalsIgnoreCase(htmlFileName)) {
                AppController.getInstance().startPresentationShow(HowToUseModificationOperationsScript.SCRIPT_NAME);
                return;
            } else if (START_SIMULATION_PRESENTATION.equalsIgnoreCase(htmlFileName)) {
                AppController.getInstance().startPresentationShow(HowToUseSimulationOperationsScript.SCRIPT_NAME);
                return;
            }
            String pageTitle = pageRefToPageTitle.get(htmlFileName);
            headerPanel.setTitle(pageTitle);
            try {
                htmlEditorPane.setPage(requestedPageURL);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });

        scrollPane = new JScrollPane(htmlEditorPane);
        scrollPane.setBackground(Color.GRAY);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private final void initHelpContent(HelpMenuItems helpMenuItem) {
        try {
            URL helpURL;
            switch (helpMenuItem) {
                case WhatIsDSDSSE:
                    headerPanel.setTitle(WHAT_IS_DSDSSE_TITLE);
                    helpURL = this.getClass().getResource(DSDSSE_HTML_CLASS_PATH);
                    break;
                case RunningExamples:
                    headerPanel.setTitle(HELP_TITLE_RUNNING_EXAMPLES);
                    helpURL = this.getClass().getResource(RUNNING_EXAMPLES_HTML_CLASS_PATH);
                    break;
                case WhatIsModelCreator:
                    headerPanel.setTitle(WHAT_IS_CREATOR_TITLE);
                    helpURL = this.getClass().getResource(CREATOR_HTML_CLASS_PATH);
                    break;
                case SimpleProject:
                    headerPanel.setTitle(HELP_TITLE_SIMPLE_PROJECT);
                    helpURL = this.getClass().getResource(SIMPLE_PROJECT_HTML_CLASS_PATH);
                    break;
                case WhatIsSimEng:
                    headerPanel.setTitle(WHAT_IS_SIM_ENG_TITLE);
                    helpURL = this.getClass().getResource(SIM_ENG_HTML_CLASS_PATH);
                    break;
                case MenuCommands:
                    headerPanel.setTitle(HELP_TITLE_MENU_COMMANDS);
                    helpURL = this.getClass().getResource(MENU_COMMANDS_HTML_CLASS_PATH);
                    break;
                case WhatIsTimeDrivenIG:
                    headerPanel.setTitle(WHAT_IS_TD_IG_TITLE);
                    helpURL = this.getClass().getResource(TD_IG_HTML_CLASS_PATH);
                    break;
                case WhatIsStateDrivenIG:
                    headerPanel.setTitle(WHAT_IS_SD_IG_TITLE);
                    helpURL = this.getClass().getResource(SD_IG_HTML_CLASS_PATH);
                    break;
                default:
                    return;
            }
            htmlEditorPane.setPage(helpURL);

        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    /**
     *
     */
    private class HeaderPanel extends JPanel {

        private final Dimension HEADER_SIZE = new Dimension(1, 22);
        private final Dimension BUTTON_SIZE = new Dimension(60, 21);
        private final JLabel headerLabel = new JLabel("Help:  What is the DSDSSE", JLabel.LEFT);
        private final JButton hideButton = new JButton("Hide");

        HeaderPanel() {
            super(new GridBagLayout());
            setBackground(HEADER_BACKGROUND);
            setPreferredSize(HEADER_SIZE);
            hideButton.setPreferredSize(BUTTON_SIZE);
            hideButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
            hideButton.setBorder(null);
            hideButton.setFocusPainted(false);
            initLayout();
        }

        void setTitle(String title) {
            headerLabel.setText(title);
        }

        private void initLayout() {
            headerLabel.setOpaque(false);
            headerLabel.setForeground(Color.WHITE);
            add(headerLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));

            hideButton.addActionListener(e -> {
                AppController.getInstance().onUserClosesHelpContent();
            });
            add(hideButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
    }

    /**
     *
     */
    private class ButtonsPanel extends JPanel {

        private static final int PANEL_HEIGHT = 15;
        private final Dimension PANEL_SIZE = new Dimension(1, PANEL_HEIGHT);

        private static final int BUTTON_WIDTH = 70;
        private static final int BUTTON_HEIGHT = 22;
        private final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        private final Color PANEL_BACKGROUND_COLOR = new Color(0xCCCCCC);
        private final Color BUTTON_FOREGROUND_COLOR = Color.BLACK;

        private final JButton hideButton = new Adf3DButton("Hide");

        ButtonsPanel() {
            super(new GridBagLayout());
            setBackground(PANEL_BACKGROUND_COLOR);
            setMinimumSize(PANEL_SIZE);
            setMaximumSize(PANEL_SIZE);
            setPreferredSize(PANEL_SIZE);
            initLayout();
        }

        private void initLayout() {
            hideButton.setMinimumSize(BUTTON_SIZE);
            hideButton.setMaximumSize(BUTTON_SIZE);
            hideButton.setPreferredSize(BUTTON_SIZE);
            hideButton.setForeground(BUTTON_FOREGROUND_COLOR);
            hideButton.setFocusPainted(false);
            hideButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
        }
    }
}
