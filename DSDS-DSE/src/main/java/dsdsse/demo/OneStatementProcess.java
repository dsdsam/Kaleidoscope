package dsdsse.demo;

import mcln.model.*;
import mcln.palette.*;
import mclnview.graphview.MclnGraphModel;
import vw.valgebra.VAlgebra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 10/16/13
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */
public final class OneStatementProcess {

    private static final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;

//    // palette & states
    private static final ThreeShadesConfettiPalette mclnStatesPalette = ThreeShadesConfettiPalette.getInstance();
    private static final MclnState initialState = mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE);

    private static final Object[][] availableStatesData = {
            {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.CREATION_STATE_ID), "$ is gray"},
            {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE), "$ is gray"},
            {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.RED_STATE), "$ is red"},
            {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE), "$ is green"},
            {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE), "$ is blue"},
            {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE), "$ is yellow"},
            {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.MAGENTA_STATE), "$ is purple"},
            {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE), "$ is cyan"},
    };

    private static final AvailableMclnStatementStates propertyAvailableStates =
            AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                    availableStatesData, initialState);

    private static final MclnState[] arcColorsForThreeStates = {
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
    };

    private static final MclnState[] arcColorsForThreeStates2 = {
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
    };

    private static final MclnState[] arcColorsForSixStates = {
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.MAGENTA_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE),
            propertyAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
    };

    /**
     * @param mclnProject
     * @param mclnGraphModel
     */
    public static void createOneStatementProcess(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        int n = 6;
        double x = 0, y = 0;

        double statementLocation[] = VAlgebra.initVec3(null, x, y, 0);
        MclnStatement statement = mclnProject.createMclnStatement("OutSt", propertyAvailableStates, statementLocation,
                initialState);
        mclnGraphModel.addMclnStatementAndUpdateView(statement);

        //
        //   Creating conditions
        //

        double scrTranVec[] = {0, 0, 0};
        double radiusToCondition = 12;
        double rotAngle = 0;
//        double resAngle = -90. +   (180. / n);
        double angleStep = -(360 / n);
        double[] conditionBaseLocation = VAlgebra.initVec3(null, 0, -radiusToCondition, 0);

        double[] arcMidPointLocation;
        List<double[]> arcKnotCSysLocations;

        MclnState[] arcStates = n == 3 ? arcColorsForThreeStates2 : arcColorsForSixStates;

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
