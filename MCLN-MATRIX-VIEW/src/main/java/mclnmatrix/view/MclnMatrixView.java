package mclnmatrix.view;

import adf.ui.components.panels.ImagePanel;
import adf.utils.BuildUtils;
import mclnmatrix.model.MclnMatrixModel;
import mclnmatrix.model.VectorDataModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MclnMatrixView extends ImagePanel {

    private static final String IMAGES_LOCATION_CLASSPATH = "/mclnmatrix-resources/images/";

    static final Color MATRIX_AND_VECTOR_BORDER_COLOR = Color.GRAY;
    static final Color MATRIX_AND_VECTOR_GRID_COLOR = Color.LIGHT_GRAY;
    static final int CELL_SIZE = 24;
    static final int VIEW_FONT_SIZE = 10;
    static Font FONT = new Font("monospaced", Font.PLAIN, VIEW_FONT_SIZE);

    private static final Color ARROW_COLOR = new Color(0x000044);//Color.BLUE;

    private static final String LEFT_ARROW_UNICODE = "\u2190";
    private static final String RIGHT_ARROW_UNICODE = "\u2192";
    private static final String UP_ARROW_UNICODE = "\u2191";
    private static final String DOWN_ARROW_UNICODE = "\u2193";

    private static final String LEFT_DOUBLE_ARROW_UNICODE = "\u21D0";
    private static final String RIGHT_DOUBLE_ARROW_UNICODE = "\u21D2";
    private static final String UP_DOUBLE_ARROW_UNICODE = "\u21D1";
    private static final String DOWN_DOUBLE_ARROW_UNICODE = "\u21D3";

    public static final String HORIZONTAL_LAYOUT_REQUEST = "Switch to Horizontal Layout";
    public static final String VERTICAL_LAYOUT_REQUEST = "Switch to Vertical Layout";

    private static boolean SHOW_SINGLE_ARROW = true;

    public static MclnMatrixView createInstance(boolean buildHorizontalLayout, MclnMatrixModel mclnMatrixModel,
                                                int propertiesSize, int conditionsSize) {
        return new MclnMatrixView(buildHorizontalLayout, mclnMatrixModel, propertiesSize, conditionsSize);
    }

    //   I n s t a n c e

    private String leftArrow;
    private String rightArrow;
    private String upArrow;
    private String downArrow;

    //    private static int propertiesSize = 0;
//    private static int conditionsSize = 0;
    public MclnStateVector inputVector;
    public MclnConditionVector conditionsVector;
    public GeneratedVector suggestedStatesVector;
    public boolean horizontalLayout = true;

    /**
     * Tis is the McLN View updating mechanism
     */
    private MatrixDataModelListener matrixDataModelListener = new MatrixDataModelListener() {
        @Override
        public void onConditionsVectorUpdated(List<String> conditions) {
            System.out.println("Conditions");
            conditionsVector.updateRecalculatedState(conditions);
        }

        @Override
        public void onSuggestedStatesVectorUpdate(List<String> suggestedStateValues) {
            System.out.println("Suggested States");
            suggestedStatesVector.updateRecalculatedState(suggestedStateValues);
        }

        @Override
        public void onInputVectorUpdate(List<String> updatedStateVectorValues) {
            System.out.println("Input States");
            inputVector.updateRecalculatedState(updatedStateVectorValues);
        }
    };

    //

    private MclnMatrixView(boolean buildHorizontalLayout, MclnMatrixModel mclnMatrixModel,
                           int propertiesSize, int conditionsSize) {
        setLayout(new GridBagLayout());
        setOpaque(true);
        setBorder(null);

        String image01 = "bg-page.jpg";
        String image02 = "canvas.jpg";
        String image03 = "line-bg.png";
        String image04 = "paint-bg.jpg";
        String image05 = "bg-3.jpg";
        String imageClassPath = IMAGES_LOCATION_CLASSPATH + image05;
        ImageIcon backgroundImage = BuildUtils.getImageIcon(imageClassPath);
        setImage(backgroundImage);

        configureArrows(SHOW_SINGLE_ARROW);
        if (buildHorizontalLayout) {
            buildHorizontalLayout(mclnMatrixModel, propertiesSize, conditionsSize);
        } else {
            buildVerticalLayout(mclnMatrixModel, propertiesSize, conditionsSize);
        }
    }

    private void configureArrows(boolean showSingleArrow) {
        if (showSingleArrow) {
            leftArrow = LEFT_ARROW_UNICODE;
            rightArrow = RIGHT_ARROW_UNICODE;
            upArrow = UP_ARROW_UNICODE;
            downArrow = DOWN_ARROW_UNICODE;
        } else {
            leftArrow = LEFT_DOUBLE_ARROW_UNICODE;
            rightArrow = RIGHT_DOUBLE_ARROW_UNICODE;
            upArrow = UP_DOUBLE_ARROW_UNICODE;
            downArrow = DOWN_DOUBLE_ARROW_UNICODE;
        }
    }

    public MatrixDataModelListener getMatrixDataModelListener() {
        return matrixDataModelListener;
    }

    //
    //   H o r i z o n t a l   L a y o u t
    //

    private void buildHorizontalLayout(MclnMatrixModel mclnMatrixModel,
                                       int propertiesSize, int conditionsSize) {
        int PROPERTY_VECTOR_WIDTH = CELL_SIZE * (propertiesSize - 1);
        int PROPERTY_VECTOR_HEIGHT = CELL_SIZE;
        int CONDITION_VECTOR_WIDTH = CELL_SIZE;
        int CONDITION_VECTOR_HEIGHT = CELL_SIZE * (conditionsSize - 1);

        int MATRIX_HOLDER_WIDTH = PROPERTY_VECTOR_WIDTH * 2 + CONDITION_VECTOR_WIDTH + 176;
        int MATRIX_HOLDER_HEIGHT = PROPERTY_VECTOR_HEIGHT + CONDITION_VECTOR_HEIGHT + 120;

        Dimension matrixHolderHorizontalLayoutSize = new Dimension(MATRIX_HOLDER_WIDTH, MATRIX_HOLDER_HEIGHT);
        setSize(matrixHolderHorizontalLayoutSize);
        setPreferredSize(matrixHolderHorizontalLayoutSize);
        setMinimumSize(matrixHolderHorizontalLayoutSize);

//
//        mvlArrayView.initContent(matrixHolderPanel);

        //  ================   Northern space holder   ====================

        JPanel northPlaceHolder = new JPanel();
        northPlaceHolder.setOpaque(false);
//        northPlaceHolder.setBackground(Color.WHITE);
        add(northPlaceHolder,
                new GridBagConstraints(0, 0, 7, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   Western space holder   ====================

        JPanel westPlaceHolder = new JPanel();
        westPlaceHolder.setOpaque(false);
//        westPlaceHolder.setBackground(Color.WHITE);
        add(westPlaceHolder,
                new GridBagConstraints(0, 1, 1, 3, 1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   O R   ====================

        VectorDataModel suggestedStateVectorDataModel = mclnMatrixModel.getSuggestedStateVectorDataModel();
        suggestedStatesVector = new GeneratedVector(true, suggestedStateVectorDataModel,
                propertiesSize, PROPERTY_VECTOR_WIDTH, PROPERTY_VECTOR_HEIGHT);
        add(suggestedStatesVector,
                new GridBagConstraints(1, 1, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        JLabel matToOutVecArrow = new ArrowLabel(upArrow, PROPERTY_VECTOR_WIDTH, 40);
        matToOutVecArrow.setForeground(ARROW_COLOR);
//        suggestVecToMatArrow.setOpaque(false);
//        suggestVecToMatArrow.setBackground(Color.ORANGE);
        add(matToOutVecArrow,
                new GridBagConstraints(1, 2, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        MclnMatrix orMatrix = new MclnMatrix(mclnMatrixModel.getOrMatrixDataModelForHorizontalLayout(),
                PROPERTY_VECTOR_WIDTH, CONDITION_VECTOR_HEIGHT,
                conditionsSize, propertiesSize);
        add(orMatrix,
                new GridBagConstraints(1, 3, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   Conditions Vector   ====================

        JLabel outVecToInpVecArrow = new ArrowLabel(rightArrow, 40, 24);
        outVecToInpVecArrow.setForeground(ARROW_COLOR);
//        suggestVecToInputVecArrow.setOpaque(false);
//        suggestVecToInputVecArrow.setBackground(Color.PINK);
        add(outVecToInpVecArrow,
                new GridBagConstraints(2, 1, 3, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        JLabel condVecToMatArrow = new ArrowLabel(leftArrow, 40, CONDITION_VECTOR_HEIGHT);
        condVecToMatArrow.setForeground(ARROW_COLOR);
        add(condVecToMatArrow,
                new GridBagConstraints(2, 3, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 10, 0), 0, 0));

        VectorDataModel conditionVectorDataModel = mclnMatrixModel.getConditionVectorDataModel();
        conditionsVector = new MclnConditionVector(false, conditionVectorDataModel,
                conditionsSize, CONDITION_VECTOR_WIDTH, CONDITION_VECTOR_HEIGHT);
//        conditionsVector.setOpaque(false);
//        conditionsVector.setBackground(Color.CYAN);
        add(conditionsVector,
                new GridBagConstraints(3, 3, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        JLabel matToCondVecArrow = new ArrowLabel(leftArrow, 40, CONDITION_VECTOR_HEIGHT);
        matToCondVecArrow.setForeground(ARROW_COLOR);
//        matToCondToMatArrow.setOpaque(false);
//        matToCondToMatArrow.setBackground(Color.PINK);
        add(matToCondVecArrow,
                new GridBagConstraints(4, 3, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 10, 0), 0, 0));

        //  ================   A N D   ====================

//        InitialStateDataModel initialStateDataModel = mclnMatrixModel.getInitialStateDataModel();
        VectorDataModel inputStateVectorDataModel = mclnMatrixModel.getInputStateVectorDataModel();
        inputVector = new MclnStateVector(true, inputStateVectorDataModel,
                propertiesSize, PROPERTY_VECTOR_WIDTH, PROPERTY_VECTOR_HEIGHT);
        add(inputVector,
                new GridBagConstraints(5, 1, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        JLabel inpVecToMatArrow = new ArrowLabel(downArrow, PROPERTY_VECTOR_WIDTH, 40);
        inpVecToMatArrow.setForeground(ARROW_COLOR);
//        inpVecToMatArrow.setOpaque(false);
//        inpVecToMatArrow.setBackground(Color.PINK);
        add(inpVecToMatArrow,
                new GridBagConstraints(5, 2, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        MclnMatrix andMatrix = new MclnMatrix(mclnMatrixModel.getAndMatrixDataModelForHorizontalLayout(),
                PROPERTY_VECTOR_WIDTH, CONDITION_VECTOR_HEIGHT, conditionsSize, propertiesSize);
        add(andMatrix,
                new GridBagConstraints(5, 3, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   Eastern space holder   ====================

        JPanel eastPlaceHolder = new JPanel();
        eastPlaceHolder.setOpaque(false);
//        eastPlaceHolder.setBackground(Color.WHITE);
        add(eastPlaceHolder,
                new GridBagConstraints(6, 1, 1, 3, 1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   Southern space holder   ====================

        JPanel southPlaceHolder = new JPanel();
        southPlaceHolder.setOpaque(false);
//        southPlaceHolder.setBackground(Color.WHITE);
        add(southPlaceHolder,
                new GridBagConstraints(0, 4, 7, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
    }

    //
    //   V e r t i c a l   L a y o u t
    //

    private void buildVerticalLayout(MclnMatrixModel mclnMatrixModel,
                                     int propertiesSize, int conditionsSize) {

        int PROPERTY_VECTOR_WIDTH = CELL_SIZE;
        int PROPERTY_VECTOR_HEIGHT = CELL_SIZE * (propertiesSize - 1);
        int CONDITION_VECTOR_WIDTH = CELL_SIZE * (conditionsSize - 1);
        int CONDITION_VECTOR_HEIGHT = CELL_SIZE;

        int MATRIX_HOLDER_WIDTH = CONDITION_VECTOR_WIDTH + PROPERTY_VECTOR_WIDTH + 140;
        int MATRIX_HOLDER_HEIGHT = PROPERTY_VECTOR_HEIGHT * 2 + CONDITION_VECTOR_HEIGHT + 140;

        Dimension matrixHolderVerticalLayoutSize = new Dimension(MATRIX_HOLDER_WIDTH, MATRIX_HOLDER_HEIGHT);
        setSize(matrixHolderVerticalLayoutSize);
        setPreferredSize(matrixHolderVerticalLayoutSize);
        setMinimumSize(matrixHolderVerticalLayoutSize);


//        int left = 0;

        //  ================   Northern space holder   ====================

        JPanel northPlaceHolder = new JPanel();
        northPlaceHolder.setOpaque(false);
//        northPlaceHolder.setBackground(Color.WHITE);
        add(northPlaceHolder,
                new GridBagConstraints(0, 0, 5, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   Western space holder   ====================

        JPanel westPlaceHolder = new JPanel();
        westPlaceHolder.setOpaque(false);
//        westPlaceHolder.setBackground(Color.WHITE);
        add(westPlaceHolder,
                new GridBagConstraints(0, 1, 1, 5, 1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   A N D   ====================

        MclnMatrix andMatrix = new MclnMatrix(mclnMatrixModel.getAndMatrixDataModelForVerticalLayout(),
                CONDITION_VECTOR_WIDTH, PROPERTY_VECTOR_HEIGHT, propertiesSize, conditionsSize);
//        andMatrix.setOpaque(false);
//        andMatrix.setBackground(Color.YELLOW);
        add(andMatrix,
                new GridBagConstraints(1, 1, 1, 1, 0, 0,
                        GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        JLabel inpVecToMatArrow = new ArrowLabel(leftArrow, 40, PROPERTY_VECTOR_HEIGHT);
        inpVecToMatArrow.setForeground(ARROW_COLOR);
//        inpVecToMatArrow.setOpaque(false);
//        inpVecToMatArrow.setBackground(Color.PINK);
        add(inpVecToMatArrow,
                new GridBagConstraints(2, 1, 1, 1, 0, 0,
                        GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        VectorDataModel inputStateVectorDataModel = mclnMatrixModel.getInputStateVectorDataModel();
        inputVector = new MclnStateVector(false, inputStateVectorDataModel,
                propertiesSize, PROPERTY_VECTOR_WIDTH, PROPERTY_VECTOR_HEIGHT);
//        inputVector.setOpaque(false);
//        inputVector.setBackground(Color.ORANGE);
        add(inputVector,
                new GridBagConstraints(3, 1, 1, 1, 0, 0,
                        GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   Conditions Vector   ====================

        JLabel matToCondVecArrow = new ArrowLabel(downArrow, CONDITION_VECTOR_WIDTH, 40);
        matToCondVecArrow.setForeground(ARROW_COLOR);
//        matToCondVecArrow.setOpaque(false);
//        matToCondVecArrow.setBackground(Color.PINK);
        add(matToCondVecArrow,
                new GridBagConstraints(1, 2, 1, 1, 0, 0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        VectorDataModel conditionVectorDataModel = mclnMatrixModel.getConditionVectorDataModel();
        conditionsVector = new MclnConditionVector(true, conditionVectorDataModel,
                conditionsSize, CONDITION_VECTOR_WIDTH, CONDITION_VECTOR_HEIGHT);
        add(conditionsVector,
                new GridBagConstraints(1, 3, 1, 1, 0, 0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        JLabel outVecToInpVecArrow = new ArrowLabel(upArrow, 24, 40);
        outVecToInpVecArrow.setForeground(ARROW_COLOR);
//        outVecToInpVecArrow.setOpaque(false);
//        outVecToInpVecArrow.setBackground(Color.PINK);
        add(outVecToInpVecArrow,
                new GridBagConstraints(3, 3, 1, 1, 0, 0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        JLabel condVecToMatArrow = new ArrowLabel(downArrow, CONDITION_VECTOR_WIDTH, 40);
        condVecToMatArrow.setForeground(ARROW_COLOR);
//        condVecToMatArrow.setOpaque(false);
//        condVecToMatArrow.setBackground(Color.PINK);
        add(condVecToMatArrow,
                new GridBagConstraints(1, 4, 1, 1, 0, 0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   O R   ====================

        MclnMatrix orMatrix = new MclnMatrix(mclnMatrixModel.getOrMatrixDataModelForVerticalLayout(),
                CONDITION_VECTOR_WIDTH, PROPERTY_VECTOR_HEIGHT, propertiesSize, conditionsSize);
        add(orMatrix,
                new GridBagConstraints(1, 5, 1, 1, 0, 0,
                        GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        JLabel matToOutVecArrow = new ArrowLabel(rightArrow, 40, PROPERTY_VECTOR_HEIGHT);
        matToOutVecArrow.setForeground(ARROW_COLOR);
//        matToOutVecArrow.setOpaque(false);
//        matToOutVecArrow.setBackground(Color.PINK);
        add(matToOutVecArrow,
                new GridBagConstraints(2, 5, 1, 1, 0, 0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        VectorDataModel suggestedStateVectorDataModel = mclnMatrixModel.getSuggestedStateVectorDataModel();
        suggestedStatesVector = new GeneratedVector(false, suggestedStateVectorDataModel,
                propertiesSize, PROPERTY_VECTOR_WIDTH, PROPERTY_VECTOR_HEIGHT);
        add(suggestedStatesVector,
                new GridBagConstraints(3, 5, 1, 1, 0, 0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   Eastern space holder   ====================

        JPanel eastPlaceHolder = new JPanel();
        eastPlaceHolder.setOpaque(false);
//        eastPlaceHolder.setBackground(Color.WHITE);
        add(eastPlaceHolder,
                new GridBagConstraints(4, 1, 1, 5, 1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

        //  ================   Southern space holder   ====================

        JPanel southPlaceHolder = new JPanel();
        southPlaceHolder.setOpaque(false);
//        southPlaceHolder.setBackground(Color.WHITE);
        add(southPlaceHolder,
                new GridBagConstraints(0, 6, 5, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
    }
}
