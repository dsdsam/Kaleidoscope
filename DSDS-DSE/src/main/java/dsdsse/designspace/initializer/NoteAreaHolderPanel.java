package dsdsse.designspace.initializer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 7/23/2016.
 */
public class NoteAreaHolderPanel extends JPanel {

    private final BillBoardPanel billBoardPanel;
    private final GeneratorTestingPanel generatorTestingPanel;
    private JPanel currentPanel;


    NoteAreaHolderPanel(InitAssistantController initAssistantController, InitAssistantDataModel initAssistantDataModel,
                        BillBoardPanel billBoardPanel, GeneratorTestingPanel generatorTestingPanel) {
        super(new BorderLayout());
        this.billBoardPanel = billBoardPanel;
        this.generatorTestingPanel = generatorTestingPanel;

        setOpaque(true);
        setBackground(InitAssistantUIColorScheme.CONTAINER_PANEL_BACKGROUND);

        add(billBoardPanel, BorderLayout.CENTER);
        currentPanel = billBoardPanel;

        EndInitializationRequestListener endInitializationRequestListener =
                (InitAssistantDataModel initAssistantDataModelArg,
                 InitAssistantController.EndInitializationRequest requestID) -> {
                    switch (requestID) {
                        case InitAssistantClosing:
                            generatorTestingPanel.destroyGeneratorTestingPanel();
                            break;
                    }
                };

        /**
         * This listener listens to Leaving Page request
         */
        ControllerRequestListener controllerRequestListener =
                (InitAssistantController initAssistantControllerArg, InitAssistantDataModel initAssistantDataModelArg,
                 InitAssistantController.PageNavigationRequest operation,
                 InitAssistantController.InitializationPage page) -> {
                    switch (operation) {
                        case LeavingPage:
                            if (page == InitAssistantController.InitializationPage.InitInputGeneratorPage) {
                                System.out.println("NoteArea Holder Panel:  Destroy Generator Testing Panel ");
                                generatorTestingPanel.destroyGeneratorTestingPanel();
                                resetContent(this.billBoardPanel);
                            }
                            break;
                    }
                };

        /**
         * This listener listens to Start/Stop Testing Button
         */
        final ShowNoteRequestListener showNoteRequestListener = noteID -> {
            switch (noteID) {
                case ToggleContext:
                    if (currentPanel == billBoardPanel) {
                        resetContent(this.generatorTestingPanel);
                        generatorTestingPanel.buildAndShowGeneratorTestPanel();
                    } else {
                        generatorTestingPanel.destroyGeneratorTestingPanel();
                        resetContent(this.billBoardPanel);
                    }
            }
        };

        initAssistantController.addListener(controllerRequestListener);
        initAssistantController.addListener(showNoteRequestListener);
        initAssistantController.addListener(endInitializationRequestListener);
//        initAssistantDataModel.addListener(initAssistantDataModelListener);
    }

    private void resetContent(JPanel panel) {
        removeAll();
        add(panel, BorderLayout.CENTER);
        currentPanel = panel;
        revalidate();
        repaint();
    }
}
