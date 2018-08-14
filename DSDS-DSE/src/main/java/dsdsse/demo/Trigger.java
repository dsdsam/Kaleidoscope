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
 * Date: 10/4/13
 * Time: 8:21 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Trigger {

    private static final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;

    // palette & states
    private static final ThreeShadesConfettiPalette mclnStatesPalette = ThreeShadesConfettiPalette.getInstance();
    private static final MclnState initialState = mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID);

    /**
     * @param mclnGraphModel
     */
    public static void createTrigger(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        double x0 = 0, y0 = 8;
        createTrigger1(mclnProject, mclnGraphModel, x0, y0);

    }

    /**
     * @param mclnGraphModel
     * @param x0
     * @param y0
     */
    private static void createTrigger1(MclnProject mclnProject, MclnGraphModel mclnGraphModel, double x0, double y0) {

        Object[][] availableStatesData = {
                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.CREATION_STATE_ID), "$ tack"},
                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.DARK_GRAY_STATE), "$ On"},
                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE), "$ tick"},
                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE), "$ Off"},
        };

        AvailableMclnStatementStates statementAvailableStates =
                AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                        availableStatesData, initialState);

        MclnState trigger01State01InitialState =
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.DARK_GRAY_STATE);
        MclnState trigger01State02InitialState =
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE);

        double n1X = x0, n1Y = y0;
        double n2X = x0 - 5, n2Y = y0 - 12;
        double n3X = x0 + 5, n3Y = y0 - 12;
        double t1X = x0 - 5, t1Y = n2Y + 5.0;
        double t2X = x0 + 5, t2Y = n2Y + 5.0;
        double a1X = x0 + 2.0, a1Y = t1Y + 2.0;
        double a3X = x0 - 2.0, a3Y = t1Y + 2.0;

        // Time Driven Input Simulating ProgramData
        Object[][] timeDrivenInputSimulatingProgramData = {
                {1, statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE)},
                {1, statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.CREATION_STATE_ID)},
                {1, statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE)},
                {1, statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.CREATION_STATE_ID)},

        };

        double[] statement01Position = VAlgebra.initVec3(null, n1X, n1Y, 0);
        MclnStatement statement01 = mclnProject.createMclnStatementWithTimeDrivenProgram("Inp.",
                statementAvailableStates, statement01Position, initialState, timeDrivenInputSimulatingProgramData);
        statement01.setStateShouldBeLogged();

        double[] statement02Position = VAlgebra.initVec3(null, n2X, n2Y, 0);
        MclnStatement statement02 = mclnProject.createMclnStatement("Node 1",
                statementAvailableStates, statement02Position, trigger01State01InitialState);
        statement02.setStateShouldBeLogged();

        double[] statement03Position = VAlgebra.initVec3(null, n3X, n3Y, 0);
        MclnStatement statement03 = mclnProject.createMclnStatement("Node 2",
                statementAvailableStates, statement03Position, trigger01State02InitialState);
        statement03.setStateShouldBeLogged();

        double[] cLocation = VAlgebra.initVec3(null, t1X, t1Y, 0);
        MclnCondition condition01 = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY, statement01, statement02,
                cLocation,
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE),
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE));

        double[] c2Location = VAlgebra.initVec3(null, t2X, t2Y, 0);
        MclnCondition condition02 = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY, statement01, statement03,
                c2Location,
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE),
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE));

        // arc from statement to condition
        double[] arcMidPointLocation = VAlgebra.initVec3(null, a1X, a1Y, 0);
        List<double[]> arcKnotCSysLocations;
        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(statement02.getCSysLocation());
        arcKnotCSysLocations.add(arcMidPointLocation);
        arcKnotCSysLocations.add(condition02.getCSysLocation());
        MclnArc mclnArc01 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations,
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE),
                statement02, condition02);

        // arc from condition to statement
        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(condition02.getCSysLocation());
        arcKnotCSysLocations.add(statement02.getCSysLocation());
        MclnArc mclnArc02 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations,
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.DARK_GRAY_STATE),
                condition02, statement02);

        // arc from statement to condition
        arcMidPointLocation = VAlgebra.initVec3(null, a3X, a3Y, 0);
        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(statement03.getCSysLocation());
        arcKnotCSysLocations.add(arcMidPointLocation);
        arcKnotCSysLocations.add(condition01.getCSysLocation());
        MclnArc mclnArc03 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations,
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.GRAY_STATE),
                statement03, condition01);

        // arc from condition to statement
        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(condition01.getCSysLocation());
        arcKnotCSysLocations.add(statement03.getCSysLocation());
        MclnArc mclnArc04 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations,
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.DARK_GRAY_STATE),
                condition01, statement03);

        mclnGraphModel.addMclnStatementAndUpdateView(statement01);
        mclnGraphModel.addMclnStatementAndUpdateView(statement02);
        mclnGraphModel.addMclnStatementAndUpdateView(statement03);

        mclnGraphModel.addMclnConditionAndUpdateView(condition01);
        mclnGraphModel.addMclnConditionAndUpdateView(condition02);

        //
        // straight arcs
        //

        MclnArc tmpArc;
        tmpArc = condition01.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
        tmpArc = statement02.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

        tmpArc = condition02.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
        tmpArc = statement03.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

//        mclnGraphModel.addMclnArcAndUpdateView(mclnArc02);
//        mclnGraphModel.addMclnArcAndUpdateView(mclnArc04);

        //
        // not straight arcs
        //

        mclnGraphModel.addMclnArcAndUpdateView(mclnArc01);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc02);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc03);
        mclnGraphModel.addMclnArcAndUpdateView(mclnArc04);
//        System.out.println();
    }
}
