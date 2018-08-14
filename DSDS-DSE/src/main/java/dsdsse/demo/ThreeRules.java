package dsdsse.demo;

import mcln.model.*;
import mcln.palette.MclnState;
import mcln.palette.ThreeShadesConfettiPalette;
import mclnview.graphview.MclnGraphModel;
import vw.valgebra.VAlgebra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/29/13
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
public final class ThreeRules {

    private static final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;

    private static final ThreeShadesConfettiPalette mclnStatesPalette = ThreeShadesConfettiPalette.getInstance();
    private static final MclnState initialState = mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID);


    public static void createThreeRulesModel(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        Object[][] conclusionAvailableStatesData = {
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE), "$ is red"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.GREEN_STATE), "$ is green"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BLUE_STATE), "$ is blue"},
        };

        AvailableMclnStatementStates conclusionAvailableStates =
                AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                        conclusionAvailableStatesData, initialState);

        Object[][] availableStatesData = {
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.GRAY_STATE), "$ is gray"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE), "$ is red"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.GREEN_STATE), "$ is green"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BLUE_STATE), "$ is blue"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.YELLOW_STATE), "$ is yellow"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.MAGENTA_STATE), "$ is purple"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CYAN_STATE), "$ is cyan"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.PINK_STATE), "$ is pink"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.SWAMP_STATE), "$ is dark yellow"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BROWN_STATE), "$ is brown"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.ORANGE_STATE), "$ is orange"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CANARY_STATE), "$ is canary"},
        };

        AvailableMclnStatementStates inputAvailableStates =
                AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                        availableStatesData, initialState);

        MclnState arcStates[] = {
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.MAGENTA_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE),
                inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE),
        };

        MclnState conditionToConclusionStates[] = {
                conclusionAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE),
                conclusionAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE),
                conclusionAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE)
        };

        Object[][][] programStateSlices = {{      //   1
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.MAGENTA_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},

        }, {       //   2
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},

        }, {       //   3
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.MAGENTA_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},

        }, {       //   4
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.MAGENTA_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},

        }, {       //   5
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.MAGENTA_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE)},

        }, {       //   5
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE)},
                {3, inputAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE)},

        },

        };
        Object[][][] programData = new Object[9][][];
        for (int i = 0; i < programData.length; i++) {
            Object[][] oneStatementStatesOverTime = new Object[programStateSlices.length][];
            for (int j = 0; j < programStateSlices.length; j++) {
                oneStatementStatesOverTime[j] = programStateSlices[j][i];
            }
            programData[i] = oneStatementStatesOverTime;
        }

        double x0 = 0;
        double y0 = 0;

        double s1X = x0, s1Y = y0 + 7;

        double[] conclusionStatementLocation = {s1X, s1Y, 0};

        MclnStatement conclusionStatement = mclnProject.createMclnStatement("Conclusion", conclusionAvailableStates,
                conclusionStatementLocation, initialState);
        conclusionStatement.setStateShouldBeLogged();

        mclnGraphModel.addMclnStatementAndUpdateView(conclusionStatement);

        int cx = -7;
        int cy = -7;
        double dx = 2;
        String nodeNamePrefix = "Premise ";
        String nodeName;
        int conjunctionsCnt = -1;
        for (int i = 0, j = 0; i < 9; i++) {
            if (i == 1 || i == 4 || i == 7) {
                conjunctionsCnt++;
                int conjunctionNumber = (conjunctionsCnt % 3 + 1);
                nodeName = nodeNamePrefix + conjunctionNumber + ".2";
                double[] midStatementLocation = VAlgebra.initVec3(null, cx, cy, 0);
                MclnStatement midStatement = mclnProject.createMclnStatementWithTimeDrivenProgram(nodeName,
                        inputAvailableStates, midStatementLocation, initialState, programData[i]);
                midStatement.setStateShouldBeLogged();

                double[] conditionLocation =
                        VAlgebra.LinCom3(null, 0.5d, conclusionStatementLocation, 0.5d, midStatementLocation);
                conditionLocation[1] = conditionLocation[1] + 1;
                MclnCondition mclnCondition = mclnProject.createMclnCondition("Con", conditionLocation);

                List<double[]> knotCSysLocations = new ArrayList();
                knotCSysLocations.add(conditionLocation);
                double[] knobCSysLocation = calculateKnobLocation(0, false, conclusionStatementLocation, conditionLocation);
                knotCSysLocations.add(knobCSysLocation);
                knotCSysLocations.add(conclusionStatementLocation);
                MclnArc mclnArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, knotCSysLocations,
                        conditionToConclusionStates[j], mclnCondition, conclusionStatement);
                // arc from condition to statement
//                mclnCondition.addOutArc(mclnArc);
//                conclusionStatement.addInpArc(mclnArc);


                // statement to condition arc
                List<double[]> scKnotCSysLocations = new ArrayList();
                scKnotCSysLocations.add(midStatementLocation);
                double[] scKnobCSysLocation = calculateKnobLocation(0, false, conditionLocation, midStatementLocation);
                scKnotCSysLocations.add(scKnobCSysLocation);
                scKnotCSysLocations.add(conditionLocation);
                MclnArc scMidArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, scKnotCSysLocations,
                        arcStates[i], midStatement, mclnCondition);
                // arc from statement to condition
//                midStatement.addOutArc(scMidArc);
//                mclnCondition.addInpArc(scMidArc);

                nodeName = nodeNamePrefix + conjunctionNumber + ".1";
                double[] leftStatementLocation = VAlgebra.initVec3(null, cx - dx, cy, 0);
                MclnStatement leftStatement = mclnProject.createMclnStatementWithTimeDrivenProgram(nodeName,
                        inputAvailableStates, leftStatementLocation, initialState, programData[i - 1]);
                leftStatement.setStateShouldBeLogged();

                List<double[]> leftArcKnotCSysLocations = new ArrayList();
                leftArcKnotCSysLocations.add(leftStatementLocation);
                double[] leftArcCSysLocation = calculateKnobLocation(0, true, leftStatementLocation, conditionLocation);
                leftArcKnotCSysLocations.add(leftArcCSysLocation);
                leftArcKnotCSysLocations.add(conditionLocation);
                MclnArc leftArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, leftArcKnotCSysLocations,
                        arcStates[i - 1], leftStatement, mclnCondition);
                // arc from statement to condition
//                leftStatement.addOutArc(leftArc);
//                mclnCondition.addInpArc(leftArc);


//                Object[][] timeDrivenInputSimulatingProgramForRightStatement = {
//                        {3, DEFAULT_STATES_PALETTE.getState(DEFAULT_STATES_PALETTE.RED_STATE)},
//                        {3, DEFAULT_STATES_PALETTE.getState(DEFAULT_STATES_PALETTE.RED_STATE)},
//                        {3, DEFAULT_STATES_PALETTE.getState(DEFAULT_STATES_PALETTE.BLUE_STATE)},
//                        {3, DEFAULT_STATES_PALETTE.getState(DEFAULT_STATES_PALETTE.RED_STATE)},
//                };
                nodeName = nodeNamePrefix + conjunctionNumber + ".3";
                double[] rightStatementLocation = VAlgebra.initVec3(null, cx + dx, cy, 0);
                MclnStatement rightStatement = mclnProject.createMclnStatementWithTimeDrivenProgram(nodeName,
                        inputAvailableStates, rightStatementLocation, initialState, programData[i + 1]);
                rightStatement.setStateShouldBeLogged();
//                MclnStatement rightStatement = MclnModelFactory.createMclnStatement("InpSt", rightStatementLocation);

                List<double[]> rightArcKnotCSysLocations = new ArrayList();
                rightArcKnotCSysLocations.add(rightStatementLocation);
                double[] rightArcCSysLocation = calculateKnobLocation(0, true, rightStatementLocation, conditionLocation);
                rightArcKnotCSysLocations.add(rightArcCSysLocation);
                rightArcKnotCSysLocations.add(conditionLocation);
                MclnArc rightArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, rightArcKnotCSysLocations,
                        arcStates[i + 1], rightStatement, mclnCondition);
                // arc from statement to condition
//                rightStatement.addOutArc(rightArc);
//                mclnCondition.addInpArc(rightArc);

                // conditions to conclusion
                mclnGraphModel.addMclnConditionAndUpdateView(mclnCondition);
                mclnGraphModel.addMclnArcAndUpdateView(mclnArc);
//
//                // statements to conditions
                mclnGraphModel.addMclnStatementAndUpdateView(leftStatement);
                mclnGraphModel.addMclnArcAndUpdateView(leftArc);
//
                mclnGraphModel.addMclnStatementAndUpdateView(midStatement);
                mclnGraphModel.addMclnArcAndUpdateView(scMidArc);
//
                mclnGraphModel.addMclnStatementAndUpdateView(rightStatement);
                mclnGraphModel.addMclnArcAndUpdateView(rightArc);

                cx += 7;
                j++;
            }
        }
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
                VAlgebra.LinCom3(knobLocation, 0.250, point01, 0.750, newVec);
            }
        }
        return knobLocation;
    }
}
