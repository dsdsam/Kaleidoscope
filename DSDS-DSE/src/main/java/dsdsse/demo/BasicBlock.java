package dsdsse.demo;

import mcln.model.*;
import mcln.palette.MclnState;
import mcln.palette.PairsOfOppositeStatesPalette;
import mclnview.graphview.MclnGraphModel;
import vw.valgebra.VAlgebra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 12/20/13
 * Time: 9:12 PM
 * To change this template use File | Settings | File Templates.
 */
public final class BasicBlock {

    private static final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;

    /**
     * @param mclnProject
     * @param mclnGraphModel
     */
    public static void createBasicBlock(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {
        double x0 = 0, y0 = 0;

//        mclnGraphModel.clearMclnModel();
//
//        MclnDoubleRectangle doubleRectangle = new MclnDoubleRectangle(-50, -30, 100, 60);
//        mclnGraphModel.setModelSpaceRectangle(doubleRectangle);

        createTwoNodeBlock(mclnProject, mclnGraphModel, "S01", x0, y0);

//        mclnGraphModel.fireModelUpdated();
    }

    /**
     * @param statementRadius
     * @param drawKnobAsArrow
     * @param point01
     * @param point2
     * @return
     */
    private static double[] calculateKnobLocation(double statementRadius, boolean drawKnobAsArrow,
                                                  double point01[], double point2[]) {
        double[] alongVec = new double[3];
        double[] dirVec = new double[3];
        double[] radVec = new double[3];
        double[] newVec = new double[3];
        double[] knobLocation = {0., 0., 0.};
        double factRadius;

        if (statementRadius != 0) {
//            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) inpNode).RADIUS);
            VAlgebra.subVec3(alongVec, point01, point2);
            VAlgebra.normalizeVec3(alongVec, dirVec);
            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
            VAlgebra.subVec3(alongVec, alongVec, radVec);
            VAlgebra.addVec3(newVec, point2, alongVec);
            if (drawKnobAsArrow) {
                VAlgebra.LinCom3(knobLocation, 0.25, point2, 0.75, newVec);
            } else {
                VAlgebra.LinCom3(knobLocation, 0.50, point2, 0.50, newVec);
            }
        } else {
//            factRadius = parentCSys.scrPointToCSysPnt(((MclnGraphFactViewEntity) outNode).RADIUS);
            VAlgebra.subVec3(alongVec, point2, point01);
            VAlgebra.normalizeVec3(alongVec, dirVec);
            VAlgebra.scaleVec3(radVec, statementRadius, dirVec);
            VAlgebra.subVec3(alongVec, alongVec, radVec);
            VAlgebra.addVec3(newVec, point01, alongVec);
            if (drawKnobAsArrow) {
                VAlgebra.LinCom3(knobLocation, 0.75, point01, 0.25, newVec);
            } else {
                VAlgebra.LinCom3(knobLocation, 0.50, point01, 0.50, newVec);
            }
        }
        return knobLocation;
    }

    /**
     * @param mclnGraphModel
     * @param modelName
     * @param x0
     * @param y0
     */
    private static void createTwoNodeBlock(MclnProject mclnProject, MclnGraphModel mclnGraphModel,
                                           String modelName, double x0, double y0) {

        double xBase = x0;
        double yBase = y0;
        double[] statement01Location = {xBase, yBase + 6, 0};
        double[] conditionLocation = {xBase, yBase + 3, 0};
        double[] statement02Location = {xBase, yBase, 0};
        double N3Pos[] = {0, 0, 0};
        double T1Pos[] = {0, 0, 0};

        String INP_STATEMENT_NAME = "Premise";
        String OUT_STATEMENT_NAME = "Conclusion";

        PairsOfOppositeStatesPalette mclnStatesPalette = PairsOfOppositeStatesPalette.getInstance();
        MclnState initialMclnState = mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID);

        AvailableMclnStatementStates statement01AvailableStates =
                createInputStatementAvailableStates(mclnStatesPalette, initialMclnState);

        Object[][] firstStatementTimeDrivenInputSimulatingProgramData = {
                {3, statement01AvailableStates.getMclnState(mclnStatesPalette.RED_STATE)},
                {3, statement01AvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
                {3, statement01AvailableStates.getMclnState(mclnStatesPalette.NOT_RED_STATE)},
                {3, statement01AvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
        };

        MclnStatement mclnStatement01 = mclnProject.createMclnStatementWithTimeDrivenProgram(INP_STATEMENT_NAME,
                statement01AvailableStates, statement01Location, initialMclnState,
                firstStatementTimeDrivenInputSimulatingProgramData);
        mclnStatement01.setStateShouldBeLogged();

        AvailableMclnStatementStates statement02AvailableStates = createStatement02AvailableStates(mclnStatesPalette, initialMclnState);
        MclnStatement mclnStatement02 = mclnProject.createMclnStatement(OUT_STATEMENT_NAME,
                statement02AvailableStates, statement02Location, initialMclnState);
        mclnStatement02.setStateShouldBeLogged();

        MclnCondition mclnCondition = mclnProject.createMclnCondition("Condition", conditionLocation);

        List<double[]> knotCSysLocations = new ArrayList();
        knotCSysLocations.add(statement01Location);
        double[] knobCSysLocation = calculateKnobLocation(0, false, statement01Location, conditionLocation);
        knotCSysLocations.add(knobCSysLocation);
        knotCSysLocations.add(conditionLocation);
        MclnState statementToConditionArcState =
                statement01AvailableStates.getMclnState(mclnStatesPalette.RED_STATE);
        MclnArc mclnArc01 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, knotCSysLocations,
                statementToConditionArcState, mclnStatement01, mclnCondition);
        // arc from statement to condition
//        mclnStatement01.addOutArc(mclnArc01);
//        mclnCondition.addInpArc(mclnArc01);

        List<double[]> arc2KnotCSysLocations = new ArrayList();
        arc2KnotCSysLocations.add(conditionLocation);
        double[] arc2KnobCSysLocation = calculateKnobLocation(0, false, conditionLocation, statement02Location);
        arc2KnotCSysLocations.add(arc2KnobCSysLocation);
        arc2KnotCSysLocations.add(statement02Location);
        MclnState conditionToStatementArcState =
                statement02AvailableStates.getMclnState(mclnStatesPalette.BLUE_STATE);
        MclnArc mclnArc02 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arc2KnotCSysLocations,
                conditionToStatementArcState, mclnCondition, mclnStatement02);
        // arc from condition to statement
//        mclnCondition.addOutArc(mclnArc02);
//        mclnStatement02.addInpArc(mclnArc02);

        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement01);
        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement02);
        mclnGraphModel.addMclnConditionAndUpdateView(mclnCondition);

        mclnGraphModel.addMclnArcAndUpdateView(mclnArc01);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc02);
    }

    /**
     * @return
     */
    private static AvailableMclnStatementStates createInputStatementAvailableStates(
            PairsOfOppositeStatesPalette mclnStatesPalette, MclnState initialMclnState) {

        Object[][] availableStatesData = {
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE), "$ is red"},
                //   {mclnStatesPalette.getState(mclnStatesPalette.NOT_RED), "$ is not rad."}
        };
        AvailableMclnStatementStates availableSymmetricStatementStates =
                AvailableMclnStatementStates.createAvailableSymmetricStatementStates(mclnStatesPalette,
                        availableStatesData, initialMclnState);

        return availableSymmetricStatementStates;

    }

    /**
     * @return
     */
    private static AvailableMclnStatementStates createStatement02AvailableStates(
            PairsOfOppositeStatesPalette mclnStatesPalette, MclnState initialMclnState) {

        Object[][] availableStatesData = {
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BLUE_STATE), "$ is blue"},
                // {mclnStatesPalette.getState(mclnStatesPalette.NOT_BLUE), "$ is not blue."}
        };

        AvailableMclnStatementStates availableSymmetricStatementStates =
                AvailableMclnStatementStates.createAvailableSymmetricStatementStates(mclnStatesPalette,
                        availableStatesData, initialMclnState);

        return availableSymmetricStatementStates;

//        String[] OUT_STATEMENT_STATE_INTERPRETATION = {"$ is gray.", "$ is blue.", "$ is not blue."};
//
//        //
//        // creating available Mcln Statement State list
//        //
//
//        Object[][] statementAvailableStatesData = {
//                {Statement02MclnStates.DEVELOPMENT.getState(), OUT_STATEMENT_STATE_INTERPRETATION[0]},
//                {Statement02MclnStates.BLUE.getState(), OUT_STATEMENT_STATE_INTERPRETATION[1]},
//                {Statement02MclnStates.NOT_BLUE.getState(), OUT_STATEMENT_STATE_INTERPRETATION[2]},
//        };
//
//        AvailableMclnStatementStates availableMclnStatementStates =
//                AvailableMclnStatementStates.createInstance(statementAvailableStatesData,
//                        Statement02MclnStates.DEVELOPMENT.getState());
//        return null;//availableMclnStatementStates;

    }

}
