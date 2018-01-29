package dsdsse.demo;

import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import mcln.model.*;
import mcln.palette.BasicColorPalette;
import mcln.palette.MclnState;
import mcln.palette.ThreeShadesConfettiPalette;
import vw.valgebra.VAlgebra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/7/2017.
 */
public class TernaryCounter {
    /**
     * @param mclnProject
     * @param mclnGraphModel
     */
    public static void createTernaryCounter(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {
        TernaryCounter ternaryCounter = new TernaryCounter(mclnProject, mclnGraphModel);
    }

    // Instance

    private final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;

    //   Generator Palette and Available States

    private final BasicColorPalette generatorMclnStatesPalette = BasicColorPalette.getInstance();
    private Object[][] generatorAvailableStatesData = {
            {generatorMclnStatesPalette.getMclnState(BasicColorPalette.CREATION_STATE_ID), "$ tack"},
            {generatorMclnStatesPalette.getMclnState(BasicColorPalette.YELLOW_STATE_ID), "$ tick"},
    };
    private MclnState generatorInitialState = generatorMclnStatesPalette.getMclnState(BasicColorPalette.CREATION_STATE_ID);

    private AvailableMclnStatementStates generatorAvailableStates =
            AvailableMclnStatementStates.createAvailableStatementStates(generatorMclnStatesPalette,
                    generatorAvailableStatesData, generatorInitialState);

    // Time Driven Input Simulating ProgramData
    Object[][] timeDrivenInputSimulatingProgramData = {
            {1, generatorAvailableStates.getMclnState(BasicColorPalette.YELLOW_STATE_ID)},
            {1, generatorAvailableStates.getMclnState(BasicColorPalette.CREATION_STATE_ID)},
//                {1, generatorAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE)},
//                {1, generatorAvailableStates.getMclnState(ThreeShadesConfettiPalette.CREATION_STATE_ID)},
    };


    //  Counter Palette and Available States

    private final ThreeShadesConfettiPalette counterMclnStatesPalette = ThreeShadesConfettiPalette.getInstance();
    private final Object[][] counterAvailableStatesData = {
            {counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.CREATION_STATE_ID), "$ is gray"},
            {counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE), "$ is gray"},
            {counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.RED_STATE), "$ is red"},
            {counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE), "$ is green"},
            {counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE), "$ is blue"},
            {counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE), "$ is yellow"},
            {counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.MAGENTA_STATE), "$ is purple"},
            {counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE), "$ is cyan"},
    };
    private final MclnState counterInitialState = counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.RED_STATE);

    private final AvailableMclnStatementStates counterAvailableStates =
            AvailableMclnStatementStates.createAvailableStatementStates(counterMclnStatesPalette,
                    counterAvailableStatesData, counterInitialState);

    //  Arc   States

    private final MclnState[] arcColorsForThreeStates0 = {
            counterAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
            counterAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE),
            counterAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),

    };

    private final MclnState[] arcColorsForThreeStates = {
            counterAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
            counterAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE),
            counterAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE),
            counterAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
            counterAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE),
    };

    private final MclnProject mclnProject;
    private final MclnGraphModel mclnGraphModel;

    private TernaryCounter(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        this.mclnProject = mclnProject;
        this.mclnGraphModel = mclnGraphModel;

        double gX = 22, gY = 0;
        MclnStatement tickGenerator = createTickGenerator(mclnProject, mclnGraphModel, gX, gY);

        int n = 3;
        double x = 0, y = 0;
        MclnStatement statement01 = createSingleCounter(mclnProject, mclnGraphModel, n, x, y);
//        x = -11;
//        MclnStatement statement02 = createSingleCounter(mclnProject, mclnGraphModel, n, x, y);
//        x = -33;
//        MclnStatement statement03 = createSingleCounter(mclnProject, mclnGraphModel, n, x, y);

        double[] cLocation = VAlgebra.initVec3(null, 11, 3, 0);
        MclnCondition condition01 = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY, tickGenerator, statement01,
                cLocation,
                generatorMclnStatesPalette.getMclnState(BasicColorPalette.CREATION_STATE_ID),
                counterMclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE));

        mclnGraphModel.addMclnConditionAndUpdateView(condition01);
        MclnArc tmpArc;
        tmpArc = condition01.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
        tmpArc = condition01.getOutArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
    }

    /**
     * @return
     */
    private MclnStatement createTickGenerator(MclnProject mclnProject, MclnGraphModel mclnGraphModel,
                                              double gX, double gY) {
//        Object[][] generatorAvailableStatesData = {
//                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.CREATION_STATE_ID), "$ tack"},
//                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.DARK_GRAY_STATE), "$ On"},
//                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE), "$ tick"},
//                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE), "$ Off"},
//        };

//          MclnState initialState = mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID);
//
//        AvailableMclnStatementStates statementAvailableStates =
//                AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
//                        generatorAvailableStatesData, initialState);


        double[] statement01Position = VAlgebra.initVec3(null, gX, gY, 0);
        MclnStatement tickGenerator = mclnProject.createMclnStatementWithTimeDrivenProgram("Tick generator",
                generatorAvailableStates, statement01Position, generatorInitialState, timeDrivenInputSimulatingProgramData);
        tickGenerator.setStateShouldBeLogged();
        mclnGraphModel.addMclnStatementAndUpdateView(tickGenerator);
        return tickGenerator;
    }

    /**
     * @param mclnProject
     * @param mclnGraphModel
     * @param n
     * @param x
     * @param y
     */
    private MclnStatement createSingleCounter(MclnProject mclnProject, MclnGraphModel mclnGraphModel,
                                              int n, double x, double y) {

        double statementLocation[] = VAlgebra.initVec3(null, x, y, 0);
        MclnStatement statement = mclnProject.createMclnStatement("OutSt", counterAvailableStates, statementLocation,
                counterInitialState);
        mclnGraphModel.addMclnStatementAndUpdateView(statement);

        //
        //   Creating conditions
        //

        double scrTranVec[] = {x, y, 0};
        double radiusToCondition = 9;
        double rotAngle = 180;
//        double resAngle = -90. +   (180. / n);
        double angleStep = -(360 / n);
        double[] conditionBaseLocation = VAlgebra.initVec3(null, 0, -radiusToCondition, 0);

        double[] arcMidPointLocation;
        List<double[]> arcKnotCSysLocations;

        MclnState[] arcStates = arcColorsForThreeStates;

        for (int i = 0; i < n; i++) {

            double[] currentConditionLocation = VAlgebra.copyVec3(null, conditionBaseLocation);
            currentConditionLocation = VAlgebra.transformVec3(currentConditionLocation, rotAngle, scrTranVec);
            MclnCondition currentCondition = mclnProject.createMclnCondition("Con", currentConditionLocation);
            mclnGraphModel.addMclnConditionAndUpdateView(currentCondition);

            // creating Arc 01
            arcMidPointLocation = getMidKnobLocation(statementLocation, currentConditionLocation);

            arcKnotCSysLocations = new ArrayList();
            arcKnotCSysLocations.add(statement.getCSysLocation());
            arcKnotCSysLocations.add(arcMidPointLocation);
            arcKnotCSysLocations.add(currentCondition.getCSysLocation());

            MclnArc mclnArc01 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations,
                    arcStates[i], statement, currentCondition);

            mclnGraphModel.addMclnArcAndUpdateView(mclnArc01);


            // creating Arc 02
            arcMidPointLocation = getMidKnobLocation(currentConditionLocation, statementLocation);
            arcKnotCSysLocations = new ArrayList();
            arcKnotCSysLocations.add(currentCondition.getCSysLocation());
            arcKnotCSysLocations.add(arcMidPointLocation);
            arcKnotCSysLocations.add(statement.getCSysLocation());

            MclnArc mclnArc02 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations,
                    arcStates[i + 1], currentCondition, statement);

            mclnGraphModel.addMclnArcAndUpdateView(mclnArc02);
            rotAngle -= angleStep;
        }

        return statement;
    }

    /**
     * @param inpNodeCSysLocation
     * @param outNodeCSysLocation
     * @return
     */
    private static double[] getMidKnobLocation(double[] inpNodeCSysLocation, double[] outNodeCSysLocation) {
        double[] zVec = {0, 0, 1};
        double[] inpToOutVector = VAlgebra.subVec3(outNodeCSysLocation, inpNodeCSysLocation);
        double[] normalDir = VAlgebra.cross3(inpToOutVector, zVec);
        double[] knobLocation = VAlgebra.scaleVec3(normalDir, 0.15);
        double[] arcMidPointVec = VAlgebra.linCom3((1.D - 0.5), inpNodeCSysLocation, 0.5D, outNodeCSysLocation);
        double[] midKnobLocation = VAlgebra.addVec3(arcMidPointVec, knobLocation);
        return midKnobLocation;
    }
}
