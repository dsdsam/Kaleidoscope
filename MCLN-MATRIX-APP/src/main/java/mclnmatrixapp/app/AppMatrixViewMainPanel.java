package mclnmatrixapp.app;

import adf.app.AdfEnv;
import mclnmatrix.app.AoSUtils;
import mclnmatrix.model.MclnMatrixModel;
import mclnmatrix.view.MatrixDataModelListener;
import mclnmatrix.view.MclnMatrixView;
import mclnmatrix.view.ScrollableMclnViewHolder;
import mclnmatrixapp.appview.ButtonPanel;
import mclnmatrixapp.appview.FileSelectorPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AppMatrixViewMainPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(AppMatrixViewMainPanel.class.getName());

    public static final String SIMPLE_MODEL_RELATIVE_FILE_PATH =
            "/mclnmatrixapp-resources/relations/Simple-Model.txt";
    public static final String MODEL_RELATIVE_FILE_PATH =
            "/mclnmatrixapp-resources/relations/Model.txt";
    public static final String FRAGMENTED_MODEL_RELATIVE_FILE_PATH =
            "/mclnmatrixapp-resources/relations/Fragmented-Model.txt";
    public static final String MODEL_IN_COMPACT_FORMAT_FILE_PATH =
            "/mclnmatrixapp-resources/relations/modeCompactFormat.txt";

    //    public static final String DEFAULT_MODEL_RELATIVE_FILE_PATH = SIMPLE_MODEL_RELATIVE_FILE_PATH;
    public static final String DEFAULT_MODEL_RELATIVE_FILE_PATH = MODEL_IN_COMPACT_FORMAT_FILE_PATH;

    private static final String OPTION_1 = "One Conclusion Per Rule";
    private static final String OPTION_2 = "Model";//"Many Conclusions Per Rule";
    private static final String OPTION_3 = "Fragmented Model";
    private static final String OPTION_4 = "Whole State to Whole State Rules";
    public static final String[] SELECTION_OPTIONS = {OPTION_1, OPTION_2, OPTION_3, OPTION_4};

    public static final String[] OPTION_TO_FILE_PATH = {
            SIMPLE_MODEL_RELATIVE_FILE_PATH,
            MODEL_RELATIVE_FILE_PATH,
            FRAGMENTED_MODEL_RELATIVE_FILE_PATH,
            MODEL_IN_COMPACT_FORMAT_FILE_PATH};

    //   C r e a t i o n

    private static AppMatrixViewMainPanel appMatrixViewMainPanel;

    public static synchronized AppMatrixViewMainPanel createInstance(boolean createDefaultModel) {
        if (appMatrixViewMainPanel != null) {
            return appMatrixViewMainPanel;
        }
        appMatrixViewMainPanel = new AppMatrixViewMainPanel(createDefaultModel);
        return appMatrixViewMainPanel;
    }

    //
    //   I n s t a n c e
    //

    private final ScrollableMclnViewHolder currentMclnViewScrollablePanel = new ScrollableMclnViewHolder();
    private ButtonPanel buttonPanel;

    private FileBasedMatrixModel fileBasedMatrixModel;
    private MclnMatrixView mclnMatrixView;
    private int propertiesSize = 0;
    private int conditionsSize = 0;
    private boolean horizontalLayout = true;

    private MclnStateCalculator mclnStateCalculator;

    /**
     *
     */
    private final ActionListener comboBoxActionListener = (e) -> {
        processFileSelection(e);
    };

    /**
     *
     */
    private final ActionListener simulationControlsListener = (e) -> {
        processSimulationRequest(e);
    };

    //
    //   Processing Model Selection
    //

    private void processFileSelection(ActionEvent e) {
        JComboBox source = (JComboBox) e.getSource();
        String selectedFile = source.getSelectedItem().toString();
        int fileIndex = source.getSelectedIndex();
        System.out.println("Selected File: " + fileIndex + ",  " + selectedFile);
        String selectedFilePath = OPTION_TO_FILE_PATH[fileIndex];
        System.out.println("Selected File path: " + selectedFilePath);
        reloadModel(selectedFilePath);
    }

    /**
     * R e l o a d i n g   M o d e l
     *
     * @param selectedFilePath
     */
    private void reloadModel(String selectedFilePath) {
        fileBasedMatrixModel = FileBasedMatrixModel.createInstance();
        boolean success = fileBasedMatrixModel.createModelFromFile(selectedFilePath);
        if (!success) {
            System.out.println("The Model: " + MODEL_RELATIVE_FILE_PATH + ",  was not loaded !");
            return;
        }
        setCurrentModel(fileBasedMatrixModel);
        revalidate();
    }


    //
    //   Processing Layout Switch Request
    //

    private void processSimulationRequest(ActionEvent e) {
        String request = (String) e.getSource();
        System.out.println("Simulation Request: " + request);

        if (request.equalsIgnoreCase(ButtonPanel.START_SIMULATION_REQUEST)) {
            mclnStateCalculator.startSimulation();
        } else if (request.equalsIgnoreCase(ButtonPanel.STOP_SIMULATION_REQUEST)) {
            mclnStateCalculator.stopSimulation();
        } else if (request.equalsIgnoreCase(ButtonPanel.ONE_SIMULATION_STEP_REQUEST)) {
            mclnStateCalculator.calculateStateToStateTransition();
            AdfEnv.getMainFrame().repaint();
        }
    }

    private final ActionListener toggleRequestActionListener = (e) -> {
        processToggleEven(e);
    };

    //
    //   Processing Layout Switch Request
    //

    private void processToggleEven(ActionEvent e) {
        String request = (String) e.getSource();
        if (request.equalsIgnoreCase(MclnMatrixView.VERTICAL_LAYOUT_REQUEST)) {
            horizontalLayout = false;
            mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, fileBasedMatrixModel, propertiesSize, conditionsSize);
        } else if (request.equalsIgnoreCase(MclnMatrixView.HORIZONTAL_LAYOUT_REQUEST)) {
            horizontalLayout = true;
            mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, fileBasedMatrixModel, propertiesSize, conditionsSize);
        }
        currentMclnViewScrollablePanel.initContent(mclnMatrixView);
        MatrixDataModelListener matrixDataModelListener = mclnMatrixView.getMatrixDataModelListener();
        mclnStateCalculator.setMatrixDataModelListener(matrixDataModelListener);
    }


    //
    //  ================   C r e a t i o n    ====================
    //

    private AppMatrixViewMainPanel(boolean createDefaultModel) {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        AoSUtils.initOperations();
        buildAppMatrixViewMainPanel(createDefaultModel);
    }

    /**
     *
     */
    private void buildAppMatrixViewMainPanel(boolean createDefaultModel) {


        //  ================   File Selection Panel   ===================

        FileSelectorPanel fileSelectorPanel = new FileSelectorPanel(SELECTION_OPTIONS, comboBoxActionListener);
        Border fileSelectorPanelBorder = BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 1, 0, Color.GRAY),
                new EmptyBorder(5, 5, 0, 5));
        fileSelectorPanel.setBorder(fileSelectorPanelBorder);
        add(fileSelectorPanel, BorderLayout.NORTH);

        //  ================   Model View Section   ====================

        add(currentMclnViewScrollablePanel, BorderLayout.CENTER);

        //  ================   View Switch Button   ====================

        buttonPanel = new ButtonPanel(toggleRequestActionListener, simulationControlsListener);
        Border compoundBorder = BorderFactory.createCompoundBorder(buttonPanel.getBorder(),
                new EmptyBorder(5, 5, 0, 5));
        buttonPanel.setBorder(compoundBorder);
        add(buttonPanel, BorderLayout.SOUTH);

        if (createDefaultModel) {
            fileBasedMatrixModel = FileBasedMatrixModel.createInstance();
            boolean success = fileBasedMatrixModel.createModelFromFile(DEFAULT_MODEL_RELATIVE_FILE_PATH);
            if (!success) {
                System.out.println("The Model: " + DEFAULT_MODEL_RELATIVE_FILE_PATH + ",  was not loaded !");
                return;
            }
            setCurrentModel(fileBasedMatrixModel);
        }
    }

    public void setCurrentModel(MclnMatrixModel fileBasedMatrixModel) {
        propertiesSize = fileBasedMatrixModel.getPropertySize();
        conditionsSize = fileBasedMatrixModel.getConditionSize();

        boolean autoLayout = false;
        if (autoLayout) {
            if (propertiesSize >= conditionsSize) {
                horizontalLayout = false;
                mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, fileBasedMatrixModel, propertiesSize, conditionsSize);
            } else {
                horizontalLayout = true;
                mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, fileBasedMatrixModel, propertiesSize, conditionsSize);
            }
            buttonPanel.resetDefaultLayout(horizontalLayout);
        } else {
            mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, fileBasedMatrixModel, propertiesSize, conditionsSize);
        }

        currentMclnViewScrollablePanel.initContent(mclnMatrixView);
        mclnStateCalculator = new MclnStateCalculator(fileBasedMatrixModel);

        MatrixDataModelListener matrixDataModelListener = mclnMatrixView.getMatrixDataModelListener();
        mclnStateCalculator.setMatrixDataModelListener(matrixDataModelListener);
    }

}
