package dsdsse.demo;

import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import mcln.model.*;
import mcln.palette.MclnState;
import mcln.palette.PairsOfOppositeStatesPalette;
import vw.valgebra.VAlgebra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/26/13
 * Time: 10:12 PM
 * To change this template use File | Settings | File Templates.
 */
public final class SimpleBlocks {

    private static final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;

    // palette & states
    public static final PairsOfOppositeStatesPalette mclnStatesPalette = PairsOfOppositeStatesPalette.getInstance();
    public static final MclnState initialState = mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID);

    /**
     * @param mclnGraphModel
     */
    public static void createSimplestNets(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        double x1 = -10, y1 = 0;
        createSimpleAnd(mclnProject, mclnGraphModel, "And", x1, y1);

        double x2 = 0, y2 = 0;
        createSimpleOr(mclnProject, mclnGraphModel, "Or", x2, y2);

        double x3 = 10, y3 = 0;
        createSimpleMem(mclnProject, mclnGraphModel, "Mem", x3, y3);
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
     * @param modelName
     * @param x0
     * @param y0
     */
    private static void createSimpleAnd(MclnProject mclnProject, MclnGraphModel mclnGraphModel, String modelName, double x0, double y0) {

        double s1X = x0 - 1.5, n1Y = y0 + 6;
        double s2X = x0 + 1.5, n2Y = y0 + 6;
        double c1X = x0, c1Y = y0 + 3;
        double s3X = x0, n3Y = y0;

        double[] statement01Location = {s1X, n1Y, 0};
        double[] statement02Location = {s2X, n2Y, 0};
        double[] conditionLocation = {c1X, c1Y, 0};
        double[] statement03Location = {s3X, n3Y, 0};


        Object[][] availableStatesData = {
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE), "$ is red"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.GREEN_STATE), "$ is green"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BLUE_STATE), "$ is blue"}
        };
        AvailableMclnStatementStates statementAvailableStates =
                AvailableMclnStatementStates.createAvailableSymmetricStatementStates(mclnStatesPalette,
                        availableStatesData, initialState);

        Object[][] timeDrivenInputSimulatingProgramData01 = {
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
        };

        Object[][] timeDrivenInputSimulatingProgramData02 = {
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
        };


        MclnStatement mclnStatement01 = mclnProject.createMclnStatementWithTimeDrivenProgram("And premise 1",
                statementAvailableStates, statement01Location, initialState, timeDrivenInputSimulatingProgramData01);
        mclnStatement01.setStateShouldBeLogged();

        MclnStatement mclnStatement02 = mclnProject.createMclnStatementWithTimeDrivenProgram("And premise 2",
                statementAvailableStates, statement02Location, initialState, timeDrivenInputSimulatingProgramData02);
        mclnStatement02.setStateShouldBeLogged();

        MclnCondition mclnCondition = mclnProject.createMclnCondition("Con", conditionLocation);

        MclnStatement mclnStatement03 = mclnProject.createMclnStatement("And conclusion",
                statementAvailableStates, statement03Location, initialState);
        mclnStatement03.setStateShouldBeLogged();

        List<double[]> knotCSysLocations = new ArrayList();
        knotCSysLocations.add(statement01Location);
        double[] knobCSysLocation = calculateKnobLocation(0, false, statement01Location, conditionLocation);
        knotCSysLocations.add(knobCSysLocation);
        knotCSysLocations.add(conditionLocation);
        MclnState statement01ToConditionArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE);
        MclnArc mclnArc01 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, knotCSysLocations,
                statement01ToConditionArcState, mclnStatement01, mclnCondition);
        // arc from statement to condition
//        mclnStatement01.addOutArc(mclnArc01);
//        mclnCondition.addInpArc(mclnArc01);

        List<double[]> arc2KnotCSysLocations = new ArrayList();
        arc2KnotCSysLocations.add(statement02Location);
        double[] arc2KnobCSysLocation = calculateKnobLocation(0, false, statement02Location, conditionLocation);
        arc2KnotCSysLocations.add(arc2KnobCSysLocation);
        arc2KnotCSysLocations.add(conditionLocation);
        MclnState statement02ToConditionArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE);
        MclnArc mclnArc02 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arc2KnotCSysLocations,
                statement02ToConditionArcState, mclnStatement02, mclnCondition);
        // arc from statement to condition
//        mclnStatement02.addOutArc(mclnArc02);
//        mclnCondition.addInpArc(mclnArc02);

        List<double[]> arc3KnotCSysLocations = new ArrayList();
        arc3KnotCSysLocations.add(conditionLocation);
        double[] arc3KnobCSysLocation = calculateKnobLocation(0, false, conditionLocation, statement03Location);
        arc3KnotCSysLocations.add(arc3KnobCSysLocation);
        arc3KnotCSysLocations.add(statement03Location);
        MclnState conditionToStatement03ArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.BLUE_STATE);
        MclnArc mclnArc03 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arc3KnotCSysLocations,
                conditionToStatement03ArcState, mclnCondition, mclnStatement03);
        // arc from condition to statement
//        mclnCondition.addOutArc(mclnArc03);
//        mclnStatement03.addInpArc(mclnArc03);

        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement01);
        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement02);
        mclnGraphModel.addMclnConditionAndUpdateView(mclnCondition);
        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement03);

        mclnGraphModel.addMclnArcAndUpdateView(mclnArc01);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc02);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc03);
    }

    /**
     * @param modelName
     * @param x0
     * @param y0
     */
    private static void createSimpleOr(MclnProject mclnProject, MclnGraphModel mclnGraphModel, String modelName, double x0, double y0) {
//                                       double dx, double dy) {

        double s1X = x0 - 1.5, n1Y = y0 + 6;
        double s2X = x0 + 1.5, n2Y = y0 + 6;
        double c1X = s1X, c1Y = y0 + 3;
        double c2X = s2X, c2Y = y0 + 3;
        double s3X = x0, n3Y = y0;

//        double s1X = x0 - 2, n1Y = y0 + 6;
//        double s2X = x0 + 2, n2Y = y0 + 6;
//        double s3X = x0, n3Y = y0 - 6;
//        double c1X = s1X, c1Y = y0;
//        double c2X = s2X, c2Y = y0;

        double[] statement01Location = {s1X, n1Y, 0};
        double[] statement02Location = {s2X, n2Y, 0};
        double[] condition01Location = {c1X, c1Y, 0};
        double[] condition02Location = {c2X, c2Y, 0};
        double[] statement03Location = {s3X, n3Y, 0};

        Object[][] availableStatesData = {
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE), "$ is red"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.GREEN_STATE), "$ is green"},
//                {mclnStatesPalette.getState(mclnStatesPalette.NOT_GREEN_STATE), "$ is not green."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BLUE_STATE), "$ is blue"}
        };
        AvailableMclnStatementStates statementAvailableStates =
                AvailableMclnStatementStates.createAvailableSymmetricStatementStates(mclnStatesPalette,
                        availableStatesData, initialState);

        Object[][] timeDrivenInputSimulatingProgramData01 = {
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE)},
        };

        Object[][] timeDrivenInputSimulatingProgramData02 = {
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE)},
        };

        MclnStatement mclnStatement01 = mclnProject.createMclnStatementWithTimeDrivenProgram("Or  premise 1",
                statementAvailableStates, statement01Location, initialState, timeDrivenInputSimulatingProgramData01);
        mclnStatement01.setStateShouldBeLogged();

        MclnStatement mclnStatement02 = mclnProject.createMclnStatementWithTimeDrivenProgram("Or  premise 2",
                statementAvailableStates, statement02Location, initialState, timeDrivenInputSimulatingProgramData02);
        mclnStatement02.setStateShouldBeLogged();

        MclnCondition mclnCondition01 = mclnProject.createMclnCondition("Con", condition01Location);
        MclnCondition mclnCondition02 = mclnProject.createMclnCondition("Con", condition02Location);
        MclnStatement mclnStatement03 = mclnProject.createMclnStatement("Or  conclusion", statementAvailableStates,
                statement03Location, initialState);
        mclnStatement03.setStateShouldBeLogged();

        List<double[]> knotCSysLocations = new ArrayList();
        knotCSysLocations.add(statement01Location);
        double[] knobCSysLocation = calculateKnobLocation(0, false, statement01Location, condition01Location);
        knotCSysLocations.add(knobCSysLocation);
        knotCSysLocations.add(condition01Location);
        MclnState statement01ToCondition01ArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE);
        MclnArc mclnArc01 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, knotCSysLocations,
                statement01ToCondition01ArcState, mclnStatement01, mclnCondition01);
        // arc from statement to condition
//        mclnStatement01.addOutArc(mclnArc01);
//        mclnCondition01.addInpArc(mclnArc01);

        List<double[]> arc2KnotCSysLocations = new ArrayList();
        arc2KnotCSysLocations.add(statement02Location);
        double[] arc2KnobCSysLocation = calculateKnobLocation(0, false, statement02Location, condition02Location);
        arc2KnotCSysLocations.add(arc2KnobCSysLocation);
        arc2KnotCSysLocations.add(condition02Location);
        MclnState statement02ToCondition02ArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE);
        MclnArc mclnArc02 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arc2KnotCSysLocations,
                statement02ToCondition02ArcState, mclnStatement02, mclnCondition02);
        // arc from statement to condition
//        mclnStatement02.addOutArc(mclnArc02);
//        mclnCondition02.addInpArc(mclnArc02);

        List<double[]> arc3KnotCSysLocations = new ArrayList();
        arc3KnotCSysLocations.add(condition01Location);
        double[] arc3KnobCSysLocation = calculateKnobLocation(0, false, condition01Location, statement03Location);
        arc3KnotCSysLocations.add(arc3KnobCSysLocation);
        arc3KnotCSysLocations.add(statement03Location);
        MclnState condition01ToStatement03ArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.BLUE_STATE);
        MclnArc mclnArc03 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arc3KnotCSysLocations,
                condition01ToStatement03ArcState, mclnCondition01, mclnStatement03);
        // arc from condition to statement
//        mclnCondition01.addOutArc(mclnArc03);
//        mclnStatement03.addInpArc(mclnArc03);

        List<double[]> arc4KnotCSysLocations = new ArrayList();
        arc4KnotCSysLocations.add(condition02Location);
        double[] arc4KnobCSysLocation = calculateKnobLocation(0, false, condition02Location, statement03Location);
        arc4KnotCSysLocations.add(arc4KnobCSysLocation);
        arc4KnotCSysLocations.add(statement03Location);
        MclnState condition02ToStatement03ArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.NOT_GREEN_STATE);
        MclnArc mclnArc04 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arc4KnotCSysLocations,
                condition02ToStatement03ArcState, mclnCondition02, mclnStatement03);
        // arc from condition to statement
//        mclnCondition02.addOutArc(mclnArc04);
//        mclnStatement03.addInpArc(mclnArc04);

        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement01);
        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement02);
        mclnGraphModel.addMclnConditionAndUpdateView(mclnCondition01);
        mclnGraphModel.addMclnConditionAndUpdateView(mclnCondition02);
        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement03);

        mclnGraphModel.addMclnArcAndUpdateView(mclnArc01);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc02);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc03);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc04);
    }

    /**
     * @param modelName
     * @param x0
     * @param y0
     */
    private static void createSimpleMem(MclnProject mclnProject, MclnGraphModel mclnGraphModel, String modelName, double x0, double y0) {


        double s1X = x0, n1Y = y0 + 6;
        double c1X = x0 - 1.5, c1Y = y0 + 3;
        double c2X = x0 + 1.5, c2Y = y0 + 3;
        double s2X = x0, n2Y = y0;

        double[] statement01Location = {s1X, n1Y, 0};
        double[] statement02Location = {s2X, n2Y, 0};
        double[] condition01Location = {c1X, c1Y, 0};
        double[] condition02Location = {c2X, c2Y, 0};

        Object[][] availableStatesData = {
                {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE), "$ is red"},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.GREEN_STATE), "$ is green"},
//                {mclnStatesPalette.getState(mclnStatesPalette.NOT_RED_STATE), "$ is not red."},
//                {mclnStatesPalette.getState(mclnStatesPalette.NOT_GREEN_STATE), "$ is not green."},
                {mclnStatesPalette.getMclnState(mclnStatesPalette.BLUE_STATE), "$ is blue"},
//                {mclnStatesPalette.getState(mclnStatesPalette.NOT_BLUE_STATE), "$ is not blue."},
        };
        AvailableMclnStatementStates statementAvailableStates =
                AvailableMclnStatementStates.createAvailableSymmetricStatementStates(mclnStatesPalette,
                        availableStatesData, initialState);


        Object[][] timeDrivenInputSimulatingProgramData01 = {
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.NOT_RED_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.NOT_GREEN_STATE)},
                {3, statementAvailableStates.getMclnState(mclnStatesPalette.CREATION_STATE_ID)},
        };

//        MclnStatement mclnStatement01 = mclnProject.createMclnStatementWithTimeDrivenProgram("Top Prem.     Prop.   ",
        MclnStatement mclnStatement01 = mclnProject.createMclnStatementWithTimeDrivenProgram("Premise",
                statementAvailableStates, statement01Location, initialState, timeDrivenInputSimulatingProgramData01);
        mclnStatement01.setStateShouldBeLogged();

        MclnCondition mclnCondition01 = mclnProject.createMclnCondition("Con-01", condition01Location);
        MclnCondition mclnCondition02 = mclnProject.createMclnCondition("Con-02", condition02Location);
//        MclnStatement mclnStatement02 = mclnProject.createMclnStatement("Bottom Concl. Prop.   ", statementAvailableStates,
        MclnStatement mclnStatement02 = mclnProject.createMclnStatement("Conclusion", statementAvailableStates,
                statement02Location, initialState);
        mclnStatement02.setStateShouldBeLogged();

        List<double[]> knotCSysLocations = new ArrayList();
        knotCSysLocations.add(statement01Location);
        double[] knobCSysLocation = calculateKnobLocation(0, false, statement01Location, condition01Location);
        knotCSysLocations.add(knobCSysLocation);
        knotCSysLocations.add(condition01Location);
        MclnState statement01ToCondition01ArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.RED_STATE);
        MclnArc mclnArc01 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, knotCSysLocations,
                statement01ToCondition01ArcState, mclnStatement01, mclnCondition01);
        // arc from statement to condition
//        mclnStatement01.addOutArc(mclnArc01);
//        mclnCondition01.addInpArc(mclnArc01);

        List<double[]> arc2KnotCSysLocations = new ArrayList();
        arc2KnotCSysLocations.add(statement01Location);
        double[] arc2KnobCSysLocation = calculateKnobLocation(0, false, statement01Location, condition02Location);
        arc2KnotCSysLocations.add(arc2KnobCSysLocation);
        arc2KnotCSysLocations.add(condition02Location);
        MclnState statement01ToCondition02ArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.GREEN_STATE);
        MclnArc mclnArc02 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arc2KnotCSysLocations,
                statement01ToCondition02ArcState, mclnStatement01, mclnCondition02);
        // arc from statement to condition
//        mclnStatement01.addOutArc(mclnArc02);
//        mclnCondition02.addInpArc(mclnArc02);

        List<double[]> arc3KnotCSysLocations = new ArrayList();
        arc3KnotCSysLocations.add(condition01Location);
        double[] arc3KnobCSysLocation = calculateKnobLocation(0, false, condition01Location, statement02Location);
        arc3KnotCSysLocations.add(arc3KnobCSysLocation);
        arc3KnotCSysLocations.add(statement02Location);
        MclnState condition01ToStatement03ArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.BLUE_STATE);
        MclnArc mclnArc03 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arc3KnotCSysLocations,
                condition01ToStatement03ArcState, mclnCondition01, mclnStatement02);
        // arc from condition to statement
//        mclnCondition01.addOutArc(mclnArc03);
//        mclnStatement02.addInpArc(mclnArc03);

        List<double[]> arc4KnotCSysLocations = new ArrayList();
        arc4KnotCSysLocations.add(condition02Location);
        double[] arc4KnobCSysLocation = calculateKnobLocation(0, false, condition02Location, statement02Location);
        arc4KnotCSysLocations.add(arc4KnobCSysLocation);
        arc4KnotCSysLocations.add(statement02Location);
        MclnState condition02ToStatement03ArcState =
                statementAvailableStates.getMclnState(mclnStatesPalette.NOT_GREEN_STATE);
        MclnArc mclnArc04 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arc4KnotCSysLocations,
                condition02ToStatement03ArcState, mclnCondition02, mclnStatement02);
        // arc from condition to statement
//        mclnCondition02.addOutArc(mclnArc04);
//        mclnStatement02.addInpArc(mclnArc04);

        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement01);
        mclnGraphModel.addMclnStatementAndUpdateView(mclnStatement02);
        mclnGraphModel.addMclnConditionAndUpdateView(mclnCondition01);
        mclnGraphModel.addMclnConditionAndUpdateView(mclnCondition02);

        mclnGraphModel.addMclnArcAndUpdateView(mclnArc01);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc02);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc03);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc04);
    }
}
