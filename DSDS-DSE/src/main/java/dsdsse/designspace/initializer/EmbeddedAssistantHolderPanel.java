package dsdsse.designspace.initializer;

import dsdsse.app.AppController;
import dsdsse.designspace.DesignSpaceContentManager;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by Admin on 8/23/2016.
 */
public class EmbeddedAssistantHolderPanel extends JPanel {

    private static final String PLAIN_TITLE = "Initialization Assistant";
    private static final String TITLE_SUFFIX = " - Initializing ";

    private static final float DARK_COLOR_PERCENT = 60;
    private static final Color HEADER_BACKGROUND = new Color(0x008200);
    private static final Color HEADER_BACKGROUND_MAY_BE = new Color(0x01743A);
    private static final Color HEADER_FOREGROUND = Color.WHITE;

    private static EmbeddedAssistantHolderPanel embeddedAssistantHolderPanel;

    public static EmbeddedAssistantHolderPanel createInstance(ModelInitializationAssistant initializationAssistant) {
        if (embeddedAssistantHolderPanel != null) {
            throw new RuntimeException("EmbeddedAssistantHolderPanel instance already created.");
        }
        embeddedAssistantHolderPanel = new EmbeddedAssistantHolderPanel(initializationAssistant);
        return embeddedAssistantHolderPanel;
    }

    public static EmbeddedAssistantHolderPanel getInstance() {
        if (embeddedAssistantHolderPanel == null) {
            throw new RuntimeException("EmbeddedAssistantHolderPanel has not yet been created.");
        }
        return embeddedAssistantHolderPanel;
    }

    public static void destroyInstance() {
        if (embeddedAssistantHolderPanel == null) {
            throw new RuntimeException("EmbeddedAssistantHolderPanel was not ever created.");
        }
        DesignSpaceContentManager.hideEastPanel(embeddedAssistantHolderPanel);
        embeddedAssistantHolderPanel = null;
        InitAssistantInterface.cleanupOnInitAssistantClosed();
    }

    //
    // I n s t a n c e
    //

    private final JLabel headerLabel = new JLabel(PLAIN_TITLE, JLabel.LEFT);

    private EmbeddedAssistantHolderPanel(ModelInitializationAssistant initializationAssistant) {
        super(new BorderLayout());
//        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        TitleBatPanel titleBatPanel = new TitleBatPanel();
        add(titleBatPanel, BorderLayout.NORTH);
        add(initializationAssistant, BorderLayout.CENTER);
    }

    void resetTitle() {
        headerLabel.setText(PLAIN_TITLE);
    }

    void extendTitle(String text) {
        String extendedHeaderText = PLAIN_TITLE + ":  " + text;
        headerLabel.setText(extendedHeaderText);
    }

    void extendTitle(String entity, String entityID) {
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        String extendedHeaderText = PLAIN_TITLE + TITLE_SUFFIX + entity + ": " + entityID;
        headerLabel.setText(extendedHeaderText);
    }

    /**
     *
     */
    private class TitleBatPanel extends JPanel {

        private TitleBatPanel() {
            super(new GridBagLayout());
            setBackground(HEADER_BACKGROUND);
            initLayout();
        }

        private void initLayout() {
            headerLabel.setOpaque(false);
            headerLabel.setForeground(Color.WHITE);
            add(headerLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));

            JButton hideButton = new JButton("Hide");
            hideButton.addActionListener(e -> {
                AppController.getInstance().onUserClickedCloseEmbeddedInitAssistantButton();
                return;
            });
            add(hideButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
    }
}
