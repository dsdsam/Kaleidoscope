package dsdsse.demo;

import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import adf.csys.view.DoubleRectangle;
import mcln.model.*;
import mcln.palette.MclnState;
import mcln.palette.ThreeShadesConfettiPalette;
import vw.valgebra.VAlgebra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 10/11/13
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MutualExclusion {

    private static final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;

    private static MclnStatement semaphore = null;
    // palette & states
    private static final ThreeShadesConfettiPalette mclnStatesPalette = ThreeShadesConfettiPalette.getInstance();
    private static final MclnState initialState = mclnStatesPalette.getMclnState(mclnStatesPalette. CREATION_STATE_ID);

    private static final Object[][] availableStatesData = {
            {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray"},
            {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE), "$ is red"},
            {mclnStatesPalette.getMclnState(mclnStatesPalette.YELLOW_STATE), "$ is yellow"},
//            {mclnStatesPalette.getState(mclnStatesPalette.PURPLE_STATE), "$ open"},
            {mclnStatesPalette.getMclnState(mclnStatesPalette.DARK_GREEN_STATE), "$ is executed"},
            {mclnStatesPalette.getMclnState(mclnStatesPalette.DARK_BLUE_STATE), "$ is executed"},
//            {mclnStatesPalette.getState(mclnStatesPalette.PINK_STATE), "$ close"},
            {mclnStatesPalette.getMclnState(mclnStatesPalette.CYAN_STATE), "$ time taking operation"},
    };

    private static final Object[][] criticalOperationAvailableStatesData = {
            {mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID), "$ is gray"},
            {mclnStatesPalette.getMclnState(mclnStatesPalette.RED_STATE), "$ is red"},
            {mclnStatesPalette.getMclnState(mclnStatesPalette.CYAN_STATE), "$ time taking operation"},
    };

    private static final Object[][] semaphoreStatementAvailableStatesData = {

            {mclnStatesPalette.getMclnState(mclnStatesPalette.ORANGE_STATE), "$ open"},
            {mclnStatesPalette.getMclnState(mclnStatesPalette.DARK_GREEN_STATE), "$ locked by green process"},
            {mclnStatesPalette.getMclnState(mclnStatesPalette.DARK_BLUE_STATE), "$ locked by blue process"},
//            {mclnStatesPalette.getState(mclnStatesPalette.PINK_STATE), "$ close"},
    };

    private static final AvailableMclnStatementStates statementAvailableStates =
            AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,availableStatesData,
                    initialState);

    private static final AvailableMclnStatementStates semaphoreStatementAvailableStates =
            AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                    semaphoreStatementAvailableStatesData, initialState);


    private static MclnState processStepInactiveState = null;
    private static MclnState processActivatingState = null;

    private static MclnState semaphoreOpenState = null;
    private static MclnState semaphoreCloseState = null;

    private static MclnStatement firstProcessUpperLeftSideStatement = null;
    private static MclnStatement firstProcessLowerLeftSideStatement = null;
    private static MclnStatement firstProcessUpperRightSideStatement = null;
    private static MclnStatement firstProcessLowerRightSideStatement = null;


    private static MclnStatement secondProcessUpperLeftSideStatement = null;
    private static MclnStatement secondProcessLowerLeftSideStatement = null;
    private static MclnStatement secondProcessUpperRightSideStatement = null;
    private static MclnStatement secondProcessLowerRightSideStatement = null;

    private static MclnCondition firstProcessLeftSideCondition;
    private static MclnCondition secondProcessLeftSideCondition;

    private static double semaphoreXLocation = 0;
    private static MclnCondition firstProcessProhibitingCondition = null;
    private static MclnCondition firstProcessReleasingCondition = null;
    private static MclnCondition secondProcessProhibitingCondition = null;
    private static MclnCondition secondProcessReleasingCondition = null;

    private static MclnState resetArcState = null;
    private static MclnState firstProcessArcState = null;
    private static MclnState secondProcessArcState = null;
    private static MclnState exclusiveSectionArcState = null;
    private static MclnState inpArcCurrentState;
    private static MclnState outArcCurrentState;

    /**
     * @param mclnProject
     * @param mclnGraphModel
     */
    public static void createMutualExclusion(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        boolean createDelayStatement = true;

        DoubleRectangle viewRectangle = new DoubleRectangle(-25., 10., 50., 20.);


        processStepInactiveState =
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.CREATION_STATE_ID);
        processActivatingState =
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE);

        semaphoreOpenState = semaphoreStatementAvailableStates.getMclnState(ThreeShadesConfettiPalette.ORANGE_STATE);
//        semaphoreCloseState = semaphoreStatementAvailableStates.getMclnState(MclnStatesPositivePalette.PINK);

        resetArcState =
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.CREATION_STATE_ID);
        firstProcessArcState =
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.DARK_GREEN_STATE);
        secondProcessArcState =
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.DARK_BLUE_STATE);
        exclusiveSectionArcState =
                statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE);

        firstProcessUpperLeftSideStatement = null;
        firstProcessLowerLeftSideStatement = null;
        firstProcessUpperRightSideStatement = null;
        firstProcessLowerRightSideStatement = null;

        firstProcessLeftSideCondition = null;
        secondProcessLeftSideCondition = null;

        semaphore = null;

        secondProcessUpperLeftSideStatement = null;
        secondProcessLowerLeftSideStatement = null;
        secondProcessUpperRightSideStatement = null;
        secondProcessLowerRightSideStatement = null;

        semaphoreXLocation = 0;
        firstProcessProhibitingCondition = null;
        secondProcessProhibitingCondition = null;


        int nStatements = 9, m = 3, k = 2;
        double vec3[] = {0., 0., 0.};
        double vec1[] = {0., 0., 0.};
        double vec2[] = {0., 0., 0.};

        double cX = viewRectangle.getX();
        double cY = viewRectangle.getY();
        double process01RightX = viewRectangle.getRightX();
        double modelWidth = process01RightX - cX;
        double dx = modelWidth / (nStatements - 1);
        nStatements = ((int) Math.round(modelWidth / dx)) + 1;

        MclnStatement prevStatement = null;
        List<double[]> arcKnotsLocations;

        //
        //   F i r s t   p r o c e s s
        //


        inpArcCurrentState = firstProcessArcState;
        outArcCurrentState = firstProcessArcState;

        // First process upper line

        for (int i = 0; i < nStatements; i++) {
            double[] firstProcessUpperLineLocation = VAlgebra.initVec3(null, cX, cY, 0);
//            VAlgebra.printVec3(firstProcessUpperLineLocation);
            MclnStatement currentStatement;
            if (i == (nStatements - 1)) {
                Object[][] stateDrivenProgram = {
                        {statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE), 2,
                                firstProcessArcState},
                };
                double[] statementLocation = VAlgebra.copyVec3(null, firstProcessUpperLineLocation);
                currentStatement = mclnProject.createMclnStatementWithStateDrivenProgram("OutSt",
                        statementAvailableStates, statementLocation, processActivatingState, stateDrivenProgram);
            } else {
                currentStatement = mclnProject.createMclnStatement("OutSt", statementAvailableStates,
                        firstProcessUpperLineLocation, processStepInactiveState);
            }
            mclnGraphModel.addMclnStatementAndUpdateView(currentStatement);

            if (i == 0) {
                firstProcessUpperLeftSideStatement = currentStatement;
            } else {
                // create Condition

                double[] cLocation = VAlgebra.LinCom3(null, 0.5, currentStatement.getCSysLocation(),
                        0.5, prevStatement.getCSysLocation());
                MclnCondition currentCondition = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY,
                        currentStatement, prevStatement, cLocation, firstProcessArcState, firstProcessArcState);
                mclnGraphModel.addMclnConditionAndUpdateView(currentCondition);

                MclnArc tmpArc = currentCondition.getInpArc(0);
                mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
                tmpArc = currentCondition.getOutArc(0);
                mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

                // reset arc
                double[] midPointLocation = VAlgebra.LinCom3(null, 0.5, currentCondition.getCSysLocation(), 0.5,
                        currentStatement.getCSysLocation());
                midPointLocation[0] = currentCondition.getCSysLocation()[0] - 0.3;
                midPointLocation[1] += 0.6;
                arcKnotsLocations = new ArrayList();
                arcKnotsLocations.add(currentCondition.getCSysLocation());
                arcKnotsLocations.add(midPointLocation);
                arcKnotsLocations.add(currentStatement.getCSysLocation());
                MclnArc firstProcessResetArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                        arcKnotsLocations, resetArcState, currentCondition, currentStatement);
                mclnGraphModel.addMclnArcAndUpdateView(firstProcessResetArc);
            }

            prevStatement = currentStatement;

            if (i == (nStatements - 1)) {
                firstProcessUpperRightSideStatement = currentStatement;
            }

            cX += dx;
        }

        //
        // First process lower line
        //

        cX = viewRectangle.getX();
        cY = 5;
        process01RightX = viewRectangle.getRightX();
        modelWidth = process01RightX - cX;
        dx = modelWidth / (nStatements - 1);
        nStatements = ((int) Math.round(modelWidth / dx)) + 1;

        for (int i = 0; i < nStatements; i++) {
            double[] firstProcessLowerLineLocation = VAlgebra.initVec3(null, cX, cY, 0);
//            VAlgebra.printVec3(firstProcessLowerLineLocation);
            MclnStatement currentStatement = mclnProject.createMclnStatement("OutSt", statementAvailableStates,
                    firstProcessLowerLineLocation, processStepInactiveState);
            mclnGraphModel.addMclnStatementAndUpdateView(currentStatement);

            if (i == 0) {
                firstProcessLowerLeftSideStatement = currentStatement;
                outArcCurrentState = exclusiveSectionArcState;
            } else {
                // create Condition

                double[] cLocation = VAlgebra.LinCom3(null, 0.5, currentStatement.getCSysLocation(),
                        0.5, prevStatement.getCSysLocation());
                MclnCondition currentCondition = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY,
                        prevStatement, currentStatement, cLocation, inpArcCurrentState, outArcCurrentState);
                mclnGraphModel.addMclnConditionAndUpdateView(currentCondition);

                MclnArc tmpArc = currentCondition.getInpArc(0);
                mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
                tmpArc = currentCondition.getOutArc(0);
                mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

                // reset arc
                double[] midPointLocation = VAlgebra.LinCom3(null, 0.5, currentCondition.getCSysLocation(),
                        0.5, prevStatement.getCSysLocation());
                midPointLocation[0] = currentCondition.getCSysLocation()[0] + 0.3;
                midPointLocation[1] += 0.6;
                arcKnotsLocations = new ArrayList();
                arcKnotsLocations.add(currentCondition.getCSysLocation());
                arcKnotsLocations.add(midPointLocation);
                arcKnotsLocations.add(prevStatement.getCSysLocation());
                MclnArc firstProcessResetArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                        arcKnotsLocations, resetArcState, currentCondition, prevStatement);
                mclnGraphModel.addMclnArcAndUpdateView(firstProcessResetArc);

                if (i == 1) {
                    double[] semaphoreLocationVector = VAlgebra.LinCom3(null, 0.5, cLocation,
                            0.5, firstProcessLowerLeftSideStatement.getCSysLocation());
                    semaphoreXLocation = semaphoreLocationVector[0];
                    firstProcessProhibitingCondition = currentCondition;
                    inpArcCurrentState = exclusiveSectionArcState;
                } else if (i == (nStatements - 2)) {
                    outArcCurrentState = firstProcessArcState;
                } else if (i == (nStatements - 1)) {
                    firstProcessReleasingCondition = currentCondition;
                    firstProcessLowerRightSideStatement = currentStatement;
                    inpArcCurrentState = firstProcessArcState;
                }

            }

            prevStatement = currentStatement;

            cX += dx;
        }

        //
        // First process left side
        //

        // condition
        cX = viewRectangle.getX() - 2.5;
        cY = viewRectangle.getY() - 2.5;
        double[] firstProcessLeftSideConditionLocation = VAlgebra.initVec3(null, cX, cY, 0);
        firstProcessLeftSideCondition =
                mclnProject.createMclnCondition("Con", firstProcessLeftSideConditionLocation);
        mclnGraphModel.addMclnConditionAndUpdateView(firstProcessLeftSideCondition);

        // upper left fragment arcs

        // Statement to Condition arc
        double[] firstProcessUpperLeftSideUpperArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, 135, viewRectangle.getX(), cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(firstProcessUpperLeftSideStatement.getCSysLocation());
        arcKnotsLocations.add(firstProcessUpperLeftSideUpperArcLocation);
        arcKnotsLocations.add(firstProcessLeftSideCondition.getCSysLocation());
        MclnArc firstProcessUpperLeftSideUpperArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, firstProcessArcState, firstProcessUpperLeftSideStatement,
                firstProcessLeftSideCondition);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessUpperLeftSideUpperArc);

        // inhibit
        double[] firstProcessUpperLeftSideUpperInhibitArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(1.5, 135, viewRectangle.getX(), cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(firstProcessLeftSideCondition.getCSysLocation());
        arcKnotsLocations.add(firstProcessUpperLeftSideUpperInhibitArcLocation);
        arcKnotsLocations.add(firstProcessUpperLeftSideStatement.getCSysLocation());
        MclnArc firstProcessLeftSideResetArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, resetArcState, firstProcessLeftSideCondition,
                firstProcessUpperLeftSideStatement);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessLeftSideResetArc);

        //  Condition to Statement arc
        double[] firstProcessUpperLeftSideLowerArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, 225, viewRectangle.getX(), cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(firstProcessLeftSideCondition.getCSysLocation());
        arcKnotsLocations.add(firstProcessUpperLeftSideLowerArcLocation);
        arcKnotsLocations.add(firstProcessLowerLeftSideStatement.getCSysLocation());
        MclnArc firstProcessUpperLeftSideLowerArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, firstProcessArcState, firstProcessLeftSideCondition,
                firstProcessLowerLeftSideStatement);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessUpperLeftSideLowerArc);

        //
        // First process right side
        //

        // first process right condition
        cX = viewRectangle.getRightX();
        cY = viewRectangle.getY() - 2.5;
        double[] firstProcessRightSideConditionLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, 0, cX, cY, 0);
        MclnCondition firstProcessRightSideCondition =
                mclnProject.createMclnCondition("Con", firstProcessRightSideConditionLocation);
        mclnGraphModel.addMclnConditionAndUpdateView(firstProcessRightSideCondition);

        // first process right fragment arcs
        double[] firstProcessUpperRightSideUpperArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, 45, cX, cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(firstProcessRightSideCondition.getCSysLocation());
        arcKnotsLocations.add(firstProcessUpperRightSideUpperArcLocation);
        arcKnotsLocations.add(firstProcessUpperRightSideStatement.getCSysLocation());
        MclnArc firstProcessUpperRightSideUpperArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, firstProcessArcState, firstProcessRightSideCondition,
                firstProcessUpperRightSideStatement);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessUpperRightSideUpperArc);

        //
        double[] firstProcessUpperRightSideLowerArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, -45, cX, cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(firstProcessLowerRightSideStatement.getCSysLocation());
        arcKnotsLocations.add(firstProcessUpperRightSideLowerArcLocation);
        arcKnotsLocations.add(firstProcessRightSideCondition.getCSysLocation());
        MclnArc firstProcessUpperRightSideLowerArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, firstProcessArcState, firstProcessLowerRightSideStatement,
                firstProcessRightSideCondition);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessUpperRightSideLowerArc);

        // inhibit
        double[] firstProcessUpperRightSideLowerInhibitArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(1.5, -45, cX, cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(firstProcessRightSideCondition.getCSysLocation());
        arcKnotsLocations.add(firstProcessUpperRightSideLowerInhibitArcLocation);
        arcKnotsLocations.add(firstProcessLowerRightSideStatement.getCSysLocation());
        MclnArc firstProcessRightSideResetArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, resetArcState, firstProcessRightSideCondition,
                firstProcessLowerRightSideStatement);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessRightSideResetArc);

        //
        //  S e c o n d   p r o c e s s
        //

        inpArcCurrentState = secondProcessArcState;
        outArcCurrentState = secondProcessArcState;

        // Second process upper line

        cX = viewRectangle.getX();
        cY = -5;//modelSpaceRectangle.getY() - 5;
        process01RightX = viewRectangle.getRightX();
        modelWidth = process01RightX - cX;
        dx = modelWidth / (nStatements - 1);
        nStatements = ((int) Math.round(modelWidth / dx)) + 1;

        for (int i = 0; i < nStatements; i++) {
            double[] s01Location = VAlgebra.initVec3(null, cX, cY, 0);
//            VAlgebra.printVec3(s01Location);
            MclnStatement currentStatement = mclnProject.createMclnStatement("OutSt", statementAvailableStates,
                    s01Location, processStepInactiveState);
            mclnGraphModel.addMclnStatementAndUpdateView(currentStatement);

            if (i == 0) {
                outArcCurrentState = exclusiveSectionArcState;
                secondProcessUpperLeftSideStatement = currentStatement;
            } else {
                // create Transition
                double[] cLocation = VAlgebra.LinCom3(null, 0.5, currentStatement.getCSysLocation(),
                        0.5, prevStatement.getCSysLocation());
                MclnCondition currentCondition = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY,
                        prevStatement, currentStatement, cLocation, inpArcCurrentState, outArcCurrentState);
                mclnGraphModel.addMclnConditionAndUpdateView(currentCondition);

                MclnArc tmpArc = currentCondition.getInpArc(0);
                mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
                tmpArc = currentCondition.getOutArc(0);
                mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

                // inhibit arc
                double[] midPointLocation = VAlgebra.LinCom3(null, 0.5, currentCondition.getCSysLocation(),
                        0.5, prevStatement.getCSysLocation());
                midPointLocation[0] += 1.8;
                midPointLocation[1] -= 0.5;
                arcKnotsLocations = new ArrayList();
                arcKnotsLocations.add(currentCondition.getCSysLocation());
                arcKnotsLocations.add(midPointLocation);
                arcKnotsLocations.add(prevStatement.getCSysLocation());
                MclnArc secondProcessResetArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                        arcKnotsLocations, resetArcState, currentCondition, prevStatement);
                mclnGraphModel.addMclnArcAndUpdateView(secondProcessResetArc);

                if (i == 1) {
                    secondProcessProhibitingCondition = currentCondition;
                    inpArcCurrentState = exclusiveSectionArcState;
                }
                if (i == (nStatements - 2)) {
                    outArcCurrentState = secondProcessArcState;
                } else if (i == (nStatements - 1)) {
                    secondProcessReleasingCondition = currentCondition;
                    secondProcessUpperRightSideStatement = currentStatement;
                    inpArcCurrentState = secondProcessArcState;
                }
            }

            prevStatement = currentStatement;


            cX += dx;
        }

        // Second process lower line

        cX = viewRectangle.getX();
        cY = -10;//modelSpaceRectangle.getY() - 5;
        process01RightX = viewRectangle.getRightX();
        modelWidth = process01RightX - cX;
        dx = modelWidth / (nStatements - 1);
        nStatements = ((int) Math.round(modelWidth / dx)) + 1;

        for (int i = 0; i < nStatements; i++) {
            double[] s01Location = VAlgebra.initVec3(null, cX, cY, 0);
//            VAlgebra.printVec3(s01Location);

            MclnStatement currentStatement;
            if (i == (nStatements - 1)) {
                Object[][] stateDrivenProgram =
                        {{statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE),
                                2, secondProcessArcState}
                        };
                double[] statementLocation = VAlgebra.copyVec3(null, s01Location);
                currentStatement = mclnProject.createMclnStatementWithStateDrivenProgram("OutSt",
                        statementAvailableStates, statementLocation, processActivatingState, stateDrivenProgram);
            } else {

                if (createDelayStatement && i == (nStatements - 5)) {
                    Object[][] stateDrivenProgram = {
                            {statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE),
                                    4, secondProcessArcState},
                    };
                    currentStatement = mclnProject.createMclnStatementWithStateDrivenProgram(
                            "OutSt", statementAvailableStates, s01Location, processStepInactiveState, stateDrivenProgram);
                } else {
                    currentStatement = mclnProject.createMclnStatement("OutSt", statementAvailableStates,
                            s01Location, processStepInactiveState);
                }
            }

            mclnGraphModel.addMclnStatementAndUpdateView(currentStatement);

            if (i == 0) {
                secondProcessLowerLeftSideStatement = currentStatement;
            } else {
                // create Condition
                double[] cLocation = VAlgebra.LinCom3(null, 0.5, currentStatement.getCSysLocation(),
                        0.5, prevStatement.getCSysLocation());
                MclnCondition currentCondition;
                if (createDelayStatement && i == (nStatements - 4)) {
                    currentCondition = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY,
                            currentStatement, prevStatement, cLocation, secondProcessArcState,
                            statementAvailableStates.getMclnState(ThreeShadesConfettiPalette.CYAN_STATE));
                } else {
                    currentCondition = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY,
                            currentStatement, prevStatement, cLocation, secondProcessArcState, secondProcessArcState);
                }
                mclnGraphModel.addMclnConditionAndUpdateView(currentCondition);

                MclnArc tmpArc = currentCondition.getInpArc(0);
                mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
                tmpArc = currentCondition.getOutArc(0);
                mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

                // inhibit arc
                double[] midPointLocation = VAlgebra.LinCom3(null, 0.5, currentCondition.getCSysLocation(), 0.5,
                        currentStatement.getCSysLocation());
                midPointLocation[0] = currentCondition.getCSysLocation()[0] - 0.3;
                midPointLocation[1] -= 0.6;
                arcKnotsLocations = new ArrayList();
                arcKnotsLocations.add(currentCondition.getCSysLocation());
                arcKnotsLocations.add(midPointLocation);
                arcKnotsLocations.add(currentStatement.getCSysLocation());
                MclnArc secondProcessLowerResetArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                        arcKnotsLocations, resetArcState, currentCondition, currentStatement);
//                currentCondition.addOutArc(secondProcessLowerResetArc);
//                currentStatement.addInpArc(secondProcessLowerResetArc);
                mclnGraphModel.addMclnArcAndUpdateView(secondProcessLowerResetArc);
            }

            prevStatement = currentStatement;

            if (i == (nStatements - 1)) {
                secondProcessLowerRightSideStatement = currentStatement;
            }
            cX += dx;
        }

        //
        // Second process left side fragment
        //

        // second process left condition
        cX = viewRectangle.getX() - 2.5;
        cY = viewRectangle.getLowerY() + 2.5;
        double[] secondProcessLeftSideConditionLocation = VAlgebra.initVec3(null, cX, cY, 0);
        secondProcessLeftSideCondition =
                mclnProject.createMclnCondition("Con", secondProcessLeftSideConditionLocation);
        mclnGraphModel.addMclnConditionAndUpdateView(secondProcessLeftSideCondition);

        // second process left fragment arcs

        //
        double[] secondProcessUpperLeftSideUpperArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, 135, viewRectangle.getX(), cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(secondProcessLeftSideCondition.getCSysLocation());
        arcKnotsLocations.add(secondProcessUpperLeftSideUpperArcLocation);
        arcKnotsLocations.add(secondProcessUpperLeftSideStatement.getCSysLocation());
        MclnArc secondProcessUpperLeftSideUpperArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, secondProcessArcState, secondProcessLeftSideCondition,
                secondProcessUpperLeftSideStatement);
//        secondProcessLeftSideCondition.addOutArc(secondProcessUpperLeftSideUpperArc);
//        secondProcessUpperLeftSideStatement.addInpArc(secondProcessUpperLeftSideUpperArc);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessUpperLeftSideUpperArc);

        //
        double[] secondProcessUpperLeftSideLowerArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, 225, viewRectangle.getX(), cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(secondProcessLowerLeftSideStatement.getCSysLocation());
        arcKnotsLocations.add(secondProcessUpperLeftSideLowerArcLocation);
        arcKnotsLocations.add(secondProcessLeftSideCondition.getCSysLocation());
        MclnArc secondProcessUpperLeftSideLowerArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, secondProcessArcState, secondProcessLowerLeftSideStatement,
                secondProcessLeftSideCondition);
//        secondProcessLowerLeftSideStatement.addOutArc(secondProcessUpperLeftSideLowerArc);
//        secondProcessLeftSideCondition.addInpArc(secondProcessUpperLeftSideLowerArc);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessUpperLeftSideLowerArc);

        // inhibit arc
        double[] secondProcessLeftSideInhibitArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(1.5, 225, viewRectangle.getX(), cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(secondProcessLeftSideCondition.getCSysLocation());
        arcKnotsLocations.add(secondProcessLeftSideInhibitArcLocation);
        arcKnotsLocations.add(secondProcessLowerLeftSideStatement.getCSysLocation());
        MclnArc secondProcessLeftSideResetArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, resetArcState, secondProcessLeftSideCondition,
                secondProcessLowerLeftSideStatement);
//        secondProcessLeftSideCondition.addOutArc(secondProcessLeftSideResetArc);
//        secondProcessLowerLeftSideStatement.addInpArc(secondProcessLeftSideResetArc);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessLeftSideResetArc);

        //
        // Second process right side fragment
        //

        // second process right condition
        cX = viewRectangle.getRightX();
        cY = viewRectangle.getLowerY() + 2.5;
        double[] secondProcessRightSideConditionLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, 0, cX, cY, 0);
        MclnCondition secondProcessRightSideCondition =
                mclnProject.createMclnCondition("Con", secondProcessRightSideConditionLocation);
        mclnGraphModel.addMclnConditionAndUpdateView(secondProcessRightSideCondition);


        // second process right fragment arcs

        // Statement to Condition arc
        double[] secondProcessUpperRightSideUpperArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, 45, cX, cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(secondProcessUpperRightSideStatement.getCSysLocation());
        arcKnotsLocations.add(secondProcessUpperRightSideUpperArcLocation);
        arcKnotsLocations.add(secondProcessRightSideCondition.getCSysLocation());
        MclnArc secondProcessUpperRightSideUpperArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, secondProcessArcState, secondProcessUpperRightSideStatement,
                secondProcessRightSideCondition);
//        secondProcessUpperRightSideStatement.addOutArc(secondProcessUpperRightSideUpperArc);
//        secondProcessRightSideCondition.addInpArc(secondProcessUpperRightSideUpperArc);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessUpperRightSideUpperArc);

        // inhibit arc
        double[] secondProcessRightSideInhibitArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(1.5, 45, cX, cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(secondProcessRightSideCondition.getCSysLocation());
        arcKnotsLocations.add(secondProcessRightSideInhibitArcLocation);
        arcKnotsLocations.add(secondProcessUpperRightSideStatement.getCSysLocation());
        MclnArc secondProcessRightSideResetArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, resetArcState, secondProcessRightSideCondition,
                secondProcessUpperRightSideStatement);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessRightSideResetArc);

        // Condition to Statement arc
        double[] secondProcessUpperRightSideLowerArcLocation =
                VAlgebra.createPointOnCircleAndTranslate(2.5, -45, cX, cY, 0);
        arcKnotsLocations = new ArrayList();
        arcKnotsLocations.add(secondProcessRightSideCondition.getCSysLocation());
        arcKnotsLocations.add(secondProcessUpperRightSideLowerArcLocation);
        arcKnotsLocations.add(secondProcessLowerRightSideStatement.getCSysLocation());
        MclnArc secondProcessUpperRightSideLowerArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotsLocations, secondProcessArcState, secondProcessRightSideCondition,
                secondProcessLowerRightSideStatement);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessUpperRightSideLowerArc);

        //
        // S e m a p h o r e
        //

//        createSemaphore(mclnProject, mclnGraphModel);
//        createSemaphore2(mclnProject, mclnGraphModel);
        createOLDSemaphore(mclnProject, mclnGraphModel);
    }

    /**
     * creates semaphore as one lock for all processes
     * (this method duplicates active nodes)
     *
     * @param mclnProject
     * @param mclnGraphModel
     */
    private static void createSemaphore(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {
        double[] statementLocation = VAlgebra.initVec3(null, semaphoreXLocation, 0, 0);
        semaphore = mclnProject.createMclnStatement("OutSt", statementAvailableStates,
                statementLocation, semaphoreOpenState);
        semaphore.setStateShouldBeLogged();
        mclnGraphModel.addMclnStatementAndUpdateView(semaphore);


        // First process

        double[] cLocation;
        cLocation = VAlgebra.LinCom3(null, 0.5, firstProcessLowerLeftSideStatement.getCSysLocation(),
                0.5, semaphore.getCSysLocation());
        MclnCondition semaphoreInpArc1Condition = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY,
                firstProcessLowerLeftSideStatement, semaphore, cLocation, firstProcessArcState, semaphoreCloseState);
        mclnGraphModel.addMclnConditionAndUpdateView(semaphoreInpArc1Condition);

        MclnArc tmpArc;
        tmpArc = semaphoreInpArc1Condition.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
        tmpArc = semaphore.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

        // first process prohibiting arcs

        double[] conditionLocation = semaphoreInpArc1Condition.getCSysLocation();
        double[] arcMidPointLocation = VAlgebra.initVec3(null, conditionLocation[0] - 0.6, conditionLocation[1] - 0.2, 0);
        List<double[]> arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(semaphore.getCSysLocation());
        arcKnotCSysLocations.add(arcMidPointLocation);
        arcKnotCSysLocations.add(semaphoreInpArc1Condition.getCSysLocation());
        MclnArc firstProcessProhibitingArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotCSysLocations, semaphoreOpenState, semaphore, semaphoreInpArc1Condition);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessProhibitingArc);


        firstProcessProhibitingArc = mclnProject.createMclnStraightArc(ARROW_TIP_LOCATION_POLICY,
                semaphoreOpenState, semaphore, firstProcessProhibitingCondition);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessProhibitingArc);


        // Second process

        cLocation = VAlgebra.LinCom3(null, 0.5, secondProcessUpperLeftSideStatement.getCSysLocation(),
                0.5, semaphore.getCSysLocation());
        MclnCondition secondProcessSemaphoreInpArc1Condition =
                mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY, secondProcessUpperLeftSideStatement,
                        semaphore, cLocation, secondProcessArcState, semaphoreCloseState);
        mclnGraphModel.addMclnConditionAndUpdateView(secondProcessSemaphoreInpArc1Condition);

        tmpArc = secondProcessSemaphoreInpArc1Condition.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
        tmpArc = semaphore.getInpArc(1);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

        // second process prohibiting arcs

        double[] condition2Location = secondProcessSemaphoreInpArc1Condition.getCSysLocation();
        double[] arcMidPoint2Location = VAlgebra.initVec3(null, condition2Location[0] - 0.6, condition2Location[1] + 0.2, 0);
        List<double[]> arcKnot2CSysLocations = new ArrayList();
        arcKnot2CSysLocations.add(semaphore.getCSysLocation());
        arcKnot2CSysLocations.add(arcMidPoint2Location);
        arcKnot2CSysLocations.add(secondProcessSemaphoreInpArc1Condition.getCSysLocation());
        MclnArc secondProcessProhibitingArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnot2CSysLocations, semaphoreOpenState, semaphore, secondProcessSemaphoreInpArc1Condition);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessProhibitingArc);


        secondProcessProhibitingArc = mclnProject.createMclnStraightArc(ARROW_TIP_LOCATION_POLICY,
                semaphoreOpenState, semaphore, secondProcessProhibitingCondition);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessProhibitingArc);

        // finish creation

        finishSemaphoreCreationWithArcFragment(mclnProject, mclnGraphModel);
    }

    /**
     * creates semaphore as one lock for all processes that locks from condition
     * (this method duplicates active nodes)
     *
     * @param mclnProject
     * @param mclnGraphModel
     */
    private static void createSemaphore2(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        double[] conditionLocation = firstProcessProhibitingCondition.getCSysLocation();
        double[] semaphoreLocation = VAlgebra.initVec3(null, conditionLocation[0], 0, 0);
        semaphore = mclnProject.createMclnStatement("OutSt", statementAvailableStates,
                semaphoreLocation, semaphoreOpenState);
        semaphore.setStateShouldBeLogged();
        mclnGraphModel.addMclnStatementAndUpdateView(semaphore);

        double[] arcMidPointLocation;
        List<double[]> arcKnotCSysLocations;

        //  first process

        arcMidPointLocation = VAlgebra.copyVec3(conditionLocation);
        arcMidPointLocation[0] = arcMidPointLocation[0] - 1;
        arcMidPointLocation[1] = (conditionLocation[1] - semaphoreLocation[1]) / 2;
        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(semaphore.getCSysLocation());
        arcKnotCSysLocations.add(arcMidPointLocation);
        arcKnotCSysLocations.add(conditionLocation);
        MclnArc firstProcessProhibitingArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotCSysLocations, semaphoreOpenState, semaphore, firstProcessProhibitingCondition);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessProhibitingArc);

        arcMidPointLocation = VAlgebra.copyVec3(conditionLocation);
        arcMidPointLocation[0] = arcMidPointLocation[0] + 1;
        arcMidPointLocation[1] = (conditionLocation[1] - semaphoreLocation[1]) / 2;
        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(conditionLocation);
        arcKnotCSysLocations.add(arcMidPointLocation);
        arcKnotCSysLocations.add(semaphore.getCSysLocation());
        MclnArc firstProcessSemaphoreConditionToSemArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotCSysLocations, semaphoreCloseState, firstProcessProhibitingCondition, semaphore);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessSemaphoreConditionToSemArc);

        //  second process
        double[] secondProcessConditionLocation = secondProcessProhibitingCondition.getCSysLocation();
        arcMidPointLocation = VAlgebra.copyVec3(secondProcessConditionLocation);
        arcMidPointLocation[0] = arcMidPointLocation[0] - 1;
        arcMidPointLocation[1] = (secondProcessConditionLocation[1] - semaphoreLocation[1]) / 2;
        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(semaphore.getCSysLocation());
        arcKnotCSysLocations.add(arcMidPointLocation);
        arcKnotCSysLocations.add(secondProcessConditionLocation);
        MclnArc secondProcessProhibitingArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotCSysLocations, semaphoreOpenState, semaphore, secondProcessProhibitingCondition);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessProhibitingArc);

        arcMidPointLocation = VAlgebra.copyVec3(secondProcessConditionLocation);
        arcMidPointLocation[0] = arcMidPointLocation[0] + 1;
        arcMidPointLocation[1] = (secondProcessConditionLocation[1] - semaphoreLocation[1]) / 2;
        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(secondProcessConditionLocation);
        arcKnotCSysLocations.add(arcMidPointLocation);
        arcKnotCSysLocations.add(semaphore.getCSysLocation());
        MclnArc secondProcessSemaphoreConditionToSemArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotCSysLocations, semaphoreCloseState, secondProcessProhibitingCondition, semaphore);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessSemaphoreConditionToSemArc);

        // finish creation

        finishSemaphoreCreationWithArc(mclnProject, mclnGraphModel);
    }


    /**
     * @param mclnProject
     * @param mclnGraphModel
     */
    private static void createOLDSemaphore(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        double[] statementLocation = VAlgebra.initVec3(null, semaphoreXLocation, 0, 0);
        semaphore = mclnProject.createMclnStatement("Semaphore", semaphoreStatementAvailableStates, statementLocation,
                semaphoreOpenState);
        semaphore.setStateShouldBeLogged();
        mclnGraphModel.addMclnStatementAndUpdateView(semaphore);


        // First process

        double[] cLocation;
        cLocation = VAlgebra.LinCom3(null, 0.5, firstProcessLowerLeftSideStatement.getCSysLocation(),
                0.5, semaphore.getCSysLocation());
        MclnCondition semaphoreInpArc1Condition = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY,
                firstProcessLowerLeftSideStatement, semaphore, cLocation, firstProcessArcState, firstProcessArcState);
        mclnGraphModel.addMclnConditionAndUpdateView(semaphoreInpArc1Condition);

        MclnArc tmpArc;
        tmpArc = semaphoreInpArc1Condition.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
        tmpArc = semaphore.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

        // first process prohibiting arcs

        double[] conditionLocation = semaphoreInpArc1Condition.getCSysLocation();
        double[] arcMidPointLocation = VAlgebra.initVec3(null, conditionLocation[0] - 0.6, conditionLocation[1] - 0.2, 0);
        List<double[]> arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(semaphore.getCSysLocation());
        arcKnotCSysLocations.add(arcMidPointLocation);
        arcKnotCSysLocations.add(semaphoreInpArc1Condition.getCSysLocation());
        MclnArc firstProcessProhibitingArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnotCSysLocations, semaphoreOpenState, semaphore, semaphoreInpArc1Condition);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessProhibitingArc);

        firstProcessProhibitingArc = mclnProject.createMclnStraightArc(ARROW_TIP_LOCATION_POLICY,
                firstProcessArcState, semaphore, firstProcessProhibitingCondition);
        mclnGraphModel.addMclnArcAndUpdateView(firstProcessProhibitingArc);


        // Second process

        cLocation = VAlgebra.LinCom3(null, 0.5, secondProcessUpperLeftSideStatement.getCSysLocation(),
                0.5, semaphore.getCSysLocation());
        MclnCondition secondProcessSemaphoreInpArc1Condition =
                mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY, secondProcessUpperLeftSideStatement,
                        semaphore, cLocation, secondProcessArcState, secondProcessArcState);
        mclnGraphModel.addMclnConditionAndUpdateView(secondProcessSemaphoreInpArc1Condition);

        tmpArc = secondProcessSemaphoreInpArc1Condition.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
        tmpArc = semaphore.getInpArc(1);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

        // second process prohibiting arcs

        double[] condition2Location = secondProcessSemaphoreInpArc1Condition.getCSysLocation();
        double[] arcMidPoint2Location = VAlgebra.initVec3(null, condition2Location[0] - 0.6, condition2Location[1] + 0.2, 0);
        List<double[]> arcKnot2CSysLocations = new ArrayList();
        arcKnot2CSysLocations.add(semaphore.getCSysLocation());
        arcKnot2CSysLocations.add(arcMidPoint2Location);
        arcKnot2CSysLocations.add(secondProcessSemaphoreInpArc1Condition.getCSysLocation());
        MclnArc secondProcessProhibitingArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                arcKnot2CSysLocations, semaphoreOpenState, semaphore, secondProcessSemaphoreInpArc1Condition);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessProhibitingArc);


        secondProcessProhibitingArc = mclnProject.createMclnStraightArc(ARROW_TIP_LOCATION_POLICY,
                secondProcessArcState, semaphore, secondProcessProhibitingCondition);
        mclnGraphModel.addMclnArcAndUpdateView(secondProcessProhibitingArc);

        // finish creation

          finishSemaphoreCreationWithArcFragment(mclnProject, mclnGraphModel);


//        finishSemaphoreCreationWithArc(mclnProject, mclnGraphModel);

//        MclnArc tmpArcX1 = mclnProject.createMclnStraightArc(processStepInactiveState, firstProcessLowerLeftSideStatement,
//                secondProcessLeftSideCondition);
//        mclnGraphModel.addMclnArcAndUpdateView(tmpArcX1);
//
//        MclnArc tmpArcX2 = mclnProject.createMclnStraightArc(processStepInactiveState, secondProcessUpperLeftSideStatement,
//                firstProcessLeftSideCondition);
//        mclnGraphModel.addMclnArcAndUpdateView(tmpArcX2);
    }

    /**
     * @param mclnProject
     * @param mclnGraphModel
     */
    private static void finishSemaphoreCreationWithArcFragment(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        // finish first process

        double[] cLocation;
        MclnArc tmpArc;
        cLocation = VAlgebra.LinCom3(null, 0.5, firstProcessLowerRightSideStatement.getCSysLocation(),
                0.5, semaphore.getCSysLocation());
        MclnCondition semaphoreInpArc2Condition = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY,
                firstProcessLowerRightSideStatement, semaphore, cLocation, firstProcessArcState, semaphoreOpenState);
        mclnGraphModel.addMclnConditionAndUpdateView(semaphoreInpArc2Condition);
//
        tmpArc = semaphoreInpArc2Condition.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
        tmpArc = semaphoreInpArc2Condition.getOutArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);

        // finish second process

        cLocation = VAlgebra.LinCom3(null, 0.5, secondProcessUpperRightSideStatement.getCSysLocation(),
                0.5, semaphore.getCSysLocation());
        MclnCondition secondProcessSemaphoreInpArc2Condition =
                mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY, secondProcessUpperRightSideStatement,
                        semaphore, cLocation, secondProcessArcState, semaphoreOpenState);
        mclnGraphModel.addMclnConditionAndUpdateView(secondProcessSemaphoreInpArc2Condition);
//
        tmpArc = secondProcessSemaphoreInpArc2Condition.getInpArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
        tmpArc = secondProcessSemaphoreInpArc2Condition.getOutArc(0);
        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
    }

    /**
     * @param mclnProject
     * @param mclnGraphModel
     */
    private static void finishSemaphoreCreationWithArc(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        double[] arcMidPointLocation;
        MclnArc semaphoreInpArc;
        List<double[]> arcKnotCSysLocations;

        // finish first process

        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(firstProcessReleasingCondition.getCSysLocation());

        arcMidPointLocation = VAlgebra.copyVec3(firstProcessReleasingCondition.getCSysLocation());
        arcMidPointLocation[0] = arcMidPointLocation[0] + 1.3;
        arcMidPointLocation[1] = arcMidPointLocation[1] - 0.3;
        arcKnotCSysLocations.add(arcMidPointLocation);

        arcMidPointLocation = VAlgebra.LinCom3(null, 0.5, firstProcessReleasingCondition.getCSysLocation(),
                0.5, semaphore.getCSysLocation());
        arcMidPointLocation[0] = arcMidPointLocation[0] + 3;
        arcMidPointLocation[1] = arcMidPointLocation[1] - 1.5;
        arcKnotCSysLocations.add(arcMidPointLocation);

        arcKnotCSysLocations.add(semaphore.getCSysLocation());

        semaphoreInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations,
                semaphoreOpenState, firstProcessReleasingCondition, semaphore);
        mclnGraphModel.addMclnArcAndUpdateView(semaphoreInpArc);

        // finish second process

        arcKnotCSysLocations = new ArrayList();
        arcKnotCSysLocations.add(secondProcessReleasingCondition.getCSysLocation());

        arcMidPointLocation = VAlgebra.copyVec3(secondProcessReleasingCondition.getCSysLocation());
        arcMidPointLocation[0] = arcMidPointLocation[0] + 1.3;
        arcMidPointLocation[1] = arcMidPointLocation[1] + 0.3;
        arcKnotCSysLocations.add(arcMidPointLocation);

        arcMidPointLocation = VAlgebra.LinCom3(null, 0.5, secondProcessReleasingCondition.getCSysLocation(),
                0.5, semaphore.getCSysLocation());
        arcMidPointLocation[0] = arcMidPointLocation[0] + 3;
        arcMidPointLocation[1] = arcMidPointLocation[1] + 1.5;
        arcKnotCSysLocations.add(arcMidPointLocation);

        arcKnotCSysLocations.add(semaphore.getCSysLocation());

        semaphoreInpArc =
                mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations, semaphoreOpenState,
                        secondProcessReleasingCondition, semaphore);
        mclnGraphModel.addMclnArcAndUpdateView(semaphoreInpArc);
    }
}