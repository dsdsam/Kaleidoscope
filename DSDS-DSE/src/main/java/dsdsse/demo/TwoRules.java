package dsdsse.demo;

import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import mcln.model.*;
import mcln.palette.TwoShadesConfettiPalette;
import mcln.palette.MclnState;
import vw.valgebra.VAlgebra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 9/28/2014.
 */
public final class TwoRules {

    private static final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;

    private static final TwoShadesConfettiPalette mclnStatesPalette = TwoShadesConfettiPalette.getInstance();
    private static final MclnState initialState =
            mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID);


    public static void createTwoRulesModel(MclnProject mclnProject, MclnGraphModel mclnGraphModel,
                                           double x0, double y0, int tree) {

        Object[][] conclusionAvailableStatesData = {
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE_ID), "$ is red."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BLUE_STATE_ID), "$ is blue."},
        };

        AvailableMclnStatementStates conclusionAvailableStates =
                AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                        conclusionAvailableStatesData, initialState);

        Object[][] availableStatesData = {
                {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE_ID), "$ is red."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BLUE_STATE_ID), "$ is blue."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CANARY_STATE_ID), "$ is canary."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.DARK_BLUE_STATE_ID), "$ is green."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BROWN_STATE_ID), "$ is brown."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.PINK_STATE_ID), "$ is yellow."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.PURPLE_STATE_ID), "$ is purple."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.ORANGE_STATE_ID), "$ is mid purple."},

                {mclnStatesPalette.getMclnState(mclnStatesPalette.GREEN_STATE_ID), "$ is cyan."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.PINK_STATE_ID), "$ is pink."},

                {mclnStatesPalette.getMclnState(mclnStatesPalette.GREEN_STATE_ID), "$ is green."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.YELLOW_STATE_ID), "$ is yellow."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.MAGENTA_STATE_ID), "$ is magenta."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CYAN_STATE_ID), "$ is cyan."},
        };

        AvailableMclnStatementStates inputAvailableStates =
                AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                        availableStatesData, initialState);

        MclnState arcStates[] = {
                inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID),
                inputAvailableStates.getMclnState(TwoShadesConfettiPalette.YELLOW_STATE_ID),
                inputAvailableStates.getMclnState(TwoShadesConfettiPalette.MAGENTA_STATE_ID),
                inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CYAN_STATE_ID),
        };

        MclnState conditionToConclusionArcStates[] = {
                conclusionAvailableStates.getMclnState(TwoShadesConfettiPalette.RED_STATE_ID),
                conclusionAvailableStates.getMclnState(TwoShadesConfettiPalette.BLUE_STATE_ID),
        };

        Object[][][] programsData01 = {{      // 0
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.DARK_BLUE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},

        }, {       // 1
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.YELLOW_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.DARK_BLUE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},

        }, {   // --------------------
                // 2
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.BROWN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},

        }, {       // 3
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.DARK_BLUE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},

        }
        };

        Object[][][] programsData02 = {{      // 0
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},

        }, {       // 1
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.YELLOW_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.DARK_BLUE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},

        }, {   // --------------------
                // 2
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.MAGENTA_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},

        }, {       // 3
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CYAN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.DARK_BLUE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},

        }
        };

        Object[][][] programsData03 = {{      // 0
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CYAN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},

        }, {       // 1
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PURPLE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.DARK_BLUE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},

        }, {   // --------------------
                // 2
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.MAGENTA_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.ORANGE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},

        }, {       // 3
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CYAN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.GREEN_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.DARK_BLUE_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.CANARY_STATE_ID)},
                {3, inputAvailableStates.getMclnState(TwoShadesConfettiPalette.PINK_STATE_ID)},

        }
        };


        double dy = 6;
        double s1X = x0, s1Y = y0 + dy;

        double[] conclusionStatementLocation = {s1X, s1Y, 0};

        MclnStatement conclusionStatement = mclnProject.createMclnStatement("OutSt", conclusionAvailableStates,
                conclusionStatementLocation, initialState);

        mclnGraphModel.addMclnStatementAndUpdateView(conclusionStatement);

        double cx = x0 - 3;
        double cy = y0 - dy;
        double dx = 2;
        for (int i = 0, j = 0; i < 4; i++) {
            if (i == 1 || i == 3) {

                //  c r e a t i n g   l e f t   s t a t e m e n t

                Object[][][] programsData;
                if (tree == 1) {
                    programsData = programsData01;
                } else if (tree == 2) {
                    programsData = programsData02;
                } else {
                    programsData = programsData03;
                }

                double[] leftStatementLocation = VAlgebra.initVec3(null, cx, cy, 0);
                MclnStatement leftStatement = mclnProject.createMclnStatementWithTimeDrivenProgram("InpSt-01",
                        inputAvailableStates, leftStatementLocation, initialState, programsData[i - 1]);

                double[] rightStatementLocation = VAlgebra.initVec3(null, cx + dx, cy, 0);
                MclnStatement rightStatement = mclnProject.createMclnStatementWithTimeDrivenProgram("InpSt-01",
                        inputAvailableStates, rightStatementLocation, initialState, programsData[i]);

                double[] conditionLocation;
                if (i == 1) {
                    conditionLocation = VAlgebra.initVec3(x0 - 2, y0, 0);
                } else {
                    conditionLocation = VAlgebra.initVec3(x0 + 2, y0, 0);
                }
//                conditionLocation[1] = 0.0;
                MclnCondition mclnCondition = mclnProject.createMclnCondition("Con", conditionLocation);

                List<double[]> knotCSysLocations = new ArrayList();
                knotCSysLocations.add(conditionLocation);
                double[] knobCSysLocation = calculateKnobLocation(0, false, conclusionStatementLocation, conditionLocation);
                knotCSysLocations.add(knobCSysLocation);
                knotCSysLocations.add(conclusionStatementLocation);
                MclnArc mclnArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, knotCSysLocations,
                        conditionToConclusionArcStates[j], mclnCondition, conclusionStatement);

                List<double[]> leftArcKnotCSysLocations = new ArrayList();
                leftArcKnotCSysLocations.add(leftStatementLocation);
                double[] leftArcCSysLocation = calculateKnobLocation(0, true, leftStatementLocation, conditionLocation);
                leftArcKnotCSysLocations.add(leftArcCSysLocation);
                leftArcKnotCSysLocations.add(conditionLocation);
                MclnArc leftArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, leftArcKnotCSysLocations,
                        arcStates[i - 1], leftStatement, mclnCondition);

                List<double[]> rightArcKnotCSysLocations = new ArrayList();
                rightArcKnotCSysLocations.add(rightStatementLocation);
                double[] rightArcCSysLocation = calculateKnobLocation(0, true, rightStatementLocation, conditionLocation);
                rightArcKnotCSysLocations.add(rightArcCSysLocation);
                rightArcKnotCSysLocations.add(conditionLocation);
                MclnArc rightArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, rightArcKnotCSysLocations,
                        arcStates[i], rightStatement, mclnCondition);

                // conditions to conclusion
                mclnGraphModel.addMclnConditionAndUpdateView(mclnCondition);
                mclnGraphModel.addMclnArcAndUpdateView(mclnArc);

                // statements to conditions
                mclnGraphModel.addMclnStatementAndUpdateView(leftStatement);
                mclnGraphModel.addMclnArcAndUpdateView(leftArc);

                mclnGraphModel.addMclnStatementAndUpdateView(rightStatement);
                mclnGraphModel.addMclnArcAndUpdateView(rightArc);

                cx = x0 + 1;
                dx = 2;
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
