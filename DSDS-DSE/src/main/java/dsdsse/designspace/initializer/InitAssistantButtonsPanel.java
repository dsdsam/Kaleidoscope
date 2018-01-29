package dsdsse.designspace.initializer;

import adf.app.StandardFonts;
import adf.ui.laf.button.Adf3DButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 3/16/2016.
 */
public class InitAssistantButtonsPanel extends JPanel {

    private static final int PANEL_HEIGHT = 30;
    private static final Dimension PANEL_SIZE = new Dimension(1, PANEL_HEIGHT);

    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 22;
    private static final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

    private final Color PANEL_BACKGROUND_COLOR = new Color(0xCCCCCC);
    private final Color BUTTON_FOREGROUND_COLOR = Color.BLACK;

    private InitAssistantDataModel initAssistantDataModel;

    private final JButton prevButton = new Adf3DButton("Prev");
    private final JButton nextButton = new Adf3DButton("Next");

    private final JButton cancelButton = new Adf3DButton("Cancel");
    private final JButton saveButton = new Adf3DButton("Apply");

    //
    //   L i s t e n e r s
    //

    private final ControllerRequestListener controllerRequestListener =
            (InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel,
             InitAssistantController.PageNavigationRequest operation,
             InitAssistantController.InitializationPage page) -> {
                if (operation == InitAssistantController.PageNavigationRequest.InitialPage) {
                    if (page == InitAssistantController.InitializationPage.PaletteAndAllowedStatesPage) {
                        nextButton.setEnabled(true);
                        prevButton.setEnabled(false);
                        return;
                    }
                    if (page == InitAssistantController.InitializationPage.InitArcStatePage) {
                        nextButton.setEnabled(false);
                        prevButton.setEnabled(false);
                        return;
                    }
                }
                if (operation != InitAssistantController.PageNavigationRequest.NextPage) {
                    return;
                }
                switch (page) {
                    case PaletteAndAllowedStatesPage:
                        nextButton.setEnabled(true);
                        prevButton.setEnabled(false);
                        break;
                    case InitInterpretationPage:
                        nextButton.setEnabled(true);
                        prevButton.setEnabled(true);
                        break;
                    case initInitialStatePage:
                        boolean hasProgram = initAssistantDataModel.propertyHasProgram();
                        nextButton.setEnabled(hasProgram);
                        prevButton.setEnabled(true);
                        break;
                    case InitInputGeneratorPage:
                        nextButton.setEnabled(false);
                        prevButton.setEnabled(true);
                        break;
                }
            };

    public ControllerRequestListener getControllerRequestListener() {
        return controllerRequestListener;
    }

    /**
     * Listens to Has Program checkbox selection
     */
    private final InitAssistantDataModelListener initAssistantDataModelListener = new InitAssistantDataModelListener() {
        @Override
        public void onInitAssistantDataModelChanged(InitAssistantDataModel initAssistantDataModel,
                                                    InitAssistantDataModel.AttributeID attributeID,
                                                    boolean initialized) {
            switch (attributeID) {
                case HasProgram:
                    boolean hasProgram = initAssistantDataModel.propertyHasProgram();
                    System.out.println("InitAssistantButtonsPanel - propertyHasProgram = " + hasProgram);
                    nextButton.setEnabled(initialized);
                    break;
                case AllowedStatesChoiceChanged:
                    nextButton.setEnabled(initialized);
//                    saveButton.setEnabled(initialized);
                    break;
            }

            boolean modified = initAssistantDataModel.isEntityModified();
            saveButton.setEnabled(modified);
        }

    };
//    private final InitAssistantDataModelListener initAssistantDataModelListener =
//            (InitAssistantDataModel initAssistantDataModel,
//             InitAssistantDataModel.AttributeID attributeID, boolean initialized) -> {
//                switch (attributeID) {
//                    case HasProgram:
//                        boolean hasProgram = initAssistantDataModel.propertyHasProgram();
//                        System.out.println("InitAssistantButtonsPanel - propertyHasProgram = " + hasProgram);
//                        nextButton.setEnabled(initialized);
//                        break;
//                    case AllowedStatesChoiceChanged:
//                        nextButton.setEnabled(initialized);
//                        saveButton.setEnabled(initialized);
//                        break;
//
//                }
//            };

    /**
     * C r e a t i o n   p a n e l   f o r   F r o n t   P a g e
     *
     * @param cancelButtonAction
     */
    InitAssistantButtonsPanel(Action cancelButtonAction) {
        super(new GridBagLayout());
        setBackground(PANEL_BACKGROUND_COLOR);
        setMinimumSize(PANEL_SIZE);
        setMaximumSize(PANEL_SIZE);
        setPreferredSize(PANEL_SIZE);

        initComponentsForFrontPage(cancelButtonAction);
    }

    /**
     * C r e a t i o n
     *
     * @param initAssistantDataModel
     * @param prevButtonAction
     * @param nextButtonAction
     * @param saveButtonAction
     * @param cancelButtonAction
     */
    InitAssistantButtonsPanel(InitAssistantDataModel initAssistantDataModel,
                              Action prevButtonAction, Action nextButtonAction,
                              Action saveButtonAction, Action cancelButtonAction) {
        super(new GridBagLayout());
        this.initAssistantDataModel = initAssistantDataModel;
        setBackground(PANEL_BACKGROUND_COLOR);
        setMinimumSize(PANEL_SIZE);
        setMaximumSize(PANEL_SIZE);
        setPreferredSize(PANEL_SIZE);

        initComponents(prevButtonAction, nextButtonAction, saveButtonAction, cancelButtonAction);
        initAssistantDataModel.addListener(initAssistantDataModelListener);
    }

    /**
     * Initializing components for Front Page
     *
     * @param cancelButtonAction
     */
    private final void initComponentsForFrontPage(Action cancelButtonAction) {

        cancelButton.setMinimumSize(BUTTON_SIZE);
        cancelButton.setMaximumSize(BUTTON_SIZE);
        cancelButton.setPreferredSize(BUTTON_SIZE);
        cancelButton.setForeground(BUTTON_FOREGROUND_COLOR);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);
        cancelButton.addActionListener(cancelButtonAction);

        add(cancelButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 0, 0, 30), 0, 0));
    }

    /**
     * Initializing components for to work with Properties and Arcs
     *
     * @param prevButtonAction
     * @param nextButtonAction
     * @param saveButtonAction
     * @param cancelButtonAction
     */
    private final void initComponents(Action prevButtonAction, Action nextButtonAction,
                                      Action saveButtonAction, Action cancelButtonAction) {
        prevButton.setMinimumSize(BUTTON_SIZE);
        prevButton.setMaximumSize(BUTTON_SIZE);
        prevButton.setPreferredSize(BUTTON_SIZE);
        prevButton.setForeground(BUTTON_FOREGROUND_COLOR);
        prevButton.setFocusPainted(false);
        prevButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);
        prevButton.addActionListener(prevButtonAction);

        nextButton.setMinimumSize(BUTTON_SIZE);
        nextButton.setMaximumSize(BUTTON_SIZE);
        nextButton.setPreferredSize(BUTTON_SIZE);
        nextButton.setForeground(BUTTON_FOREGROUND_COLOR);
        nextButton.setFocusPainted(false);
        nextButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);
        nextButton.addActionListener(nextButtonAction);

        saveButton.setEnabled(false);
        saveButton.setMinimumSize(BUTTON_SIZE);
        saveButton.setMaximumSize(BUTTON_SIZE);
        saveButton.setPreferredSize(BUTTON_SIZE);
        saveButton.setForeground(BUTTON_FOREGROUND_COLOR);
        saveButton.setFocusPainted(false);
        saveButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);
        saveButton.addActionListener(saveButtonAction);

        cancelButton.setMinimumSize(BUTTON_SIZE);
        cancelButton.setMaximumSize(BUTTON_SIZE);
        cancelButton.setPreferredSize(BUTTON_SIZE);
        cancelButton.setForeground(BUTTON_FOREGROUND_COLOR);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);
        cancelButton.addActionListener(cancelButtonAction);

        add(cancelButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 30, 0, 0), 0, 0));

        add(Box.createHorizontalStrut(25), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        add(prevButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));

        add(Box.createGlue(), new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(nextButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));

        add(Box.createHorizontalStrut(25), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        add(saveButton, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 0, 0, 30), 0, 0));

    }
}
