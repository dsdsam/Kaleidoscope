package dsdsse.demo;

import dsdsse.app.MclnPalette;
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
 * Date: 10/6/13
 * Time: 5:18 PMfrag
 * To change this template use File | Settings | File Templates.
 */
public final class DiningPhilosophers {

    private static final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;

    private static final String[] PHILOSOPHERS = {
            "Aristotle  ",
            "Plato      ",
            "Socrates   ",
            "Epicurus   ",
            "Confucius  ",
            "Avicenna   ",
            "Rene Descartes    ",
            "Bertrand Russell  ",
            "Pythagoras        "};

    private static final String CONTEMPLATING_INTERPRETATION = "$ is contemplating";
    private static final String HUNGRY_INTERPRETATION = "$ is hungry";
    private static final String ENERGIZING_INTERPRETATION = "$ is energizing";
    private static final String FINISHED_EATING_INTERPRETATION = "$ has finished eating";

    private static final ThreeShadesConfettiPalette mclnStatesPalette = ThreeShadesConfettiPalette.getInstance();
    private static final MclnState initialState = mclnStatesPalette.getMclnState(mclnStatesPalette.CREATION_STATE_ID);


//    Map<Integer, MclnStatementState> createStateToMclnStatementStateMap(Object[][] availableStatementStates) {
//        Map<Integer, MclnStatementState> stateToMclnStatementStateMap = new HashMap();
//        MclnStatementState mclnStatementState;
////        for(int i = 0; i < availableStatementStates.length; i++){
//        for (Object[] entryPair : availableStatementStates) {
//            MclnState mclnState = (MclnState) entryPair[0];
//            String interpretation = (String) entryPair[1];
//            mclnStatementState = MclnStatementState.createMclnStatementState(mclnState, interpretation);
//            stateToMclnStatementStateMap.put(mclnStatementState.getState(), mclnStatementState);
//        }
//
//        return stateToMclnStatementStateMap;
//    }


    public static void createDiningPhilosophers(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {

        int n = 5;

        double scr_x = 0, scr_y = 0, scrR_x = 0, scrR_y = 0,
                scrT1_x = 0, scrT1_y = 0, scrT2_x = 0, scrT2_y = 0,
                //      a01X, a01Y, a02X, a02Y,
                a1X, a1Y, a2X, a2Y, a3X, a3Y, a4X, a4Y,
                scrRad, tranRad, resRad;


        double nodeVec[] = new double[3];
        double t1Vec[] = new double[3];
        double t2Vec[] = new double[3];
        double rVec[] = new double[3];
        double aVec[] = new double[3];
        double zeroVec[] = {0, 0, 0};


        MclnStatement curNode, resNode;
        MclnCondition curTran, curTran2;
        MclnStatement prevRes = null;
        MclnCondition firstOuterCondition = null;
        MclnCondition firstInnerCondition = null;

        double philosophersRadius = 10;
//        scrRad = mclnDisplayPanel.scaleWorldXToCSysScrX(1.4f);
        double conditionsRadius = philosophersRadius * 1.5;
        double resourcesRadius = philosophersRadius * 0.9;
        double scrTranVec[] = {0, 0, 0};
//        scr_x = 0;
//        scr_y = scrRad;
//        scrR_x = resRad * Math.cos((Math.PI * resAngle) / 180f);
//        scrR_y = -resRad * Math.sin((Math.PI * resAngle) / 180f);

        double rotAngle = 0;
        double resAngle = -90 + (double) (180 / 5);
        double angleStep = -(double) (360 / 5);
        double[] pointOnCircle = VAlgebra.createPointOnCircle(resourcesRadius, resAngle, 0);

        double[] cSysBaseLocation = VAlgebra.initVec3(null, 0, -philosophersRadius, 0);
        double[] outerConditionBaseLocation = VAlgebra.initVec3(null, 0, -(philosophersRadius * 1.5), 0);
        double[] innerConditionBaseLocation = VAlgebra.initVec3(null, 0, -(philosophersRadius * 0.7), 0);


//        scrT1_x = 0;
//        scrT1_y = scrRad * 1.4;
//        scrT2_x = 0;
//        scrT2_y = scrRad * 0.5;
//        a1X = scrRad * 0.1;
//        a1Y = scrRad * 1.6;
//        a2X = scrRad * -0.1;
//        a2Y = scrRad * 1.6;
//
//        a3X = scrT2_x + 6;
//        a3Y = scrT2_y + 4;
//        a4X = scrT2_x + -6;
//        a4Y = scrT2_y + 4;

//        int a1pNum = 4;
//        double a1p[] = {
//                scrRad * -0.1, scrRad * 1.2,
//                scrRad * -0.18, scrRad * 1.7,
//                scrRad * -0.1, scrRad * 1.8,
//                scrRad * -0.01, scrRad * 1.7,
//        };

        double[] statementToOuterConditionCurvedArc01KnotLocations = VAlgebra.copyVec3(null, outerConditionBaseLocation);
        statementToOuterConditionCurvedArc01KnotLocations =
                VAlgebra.addVec3(statementToOuterConditionCurvedArc01KnotLocations,
                        statementToOuterConditionCurvedArc01KnotLocations, VAlgebra.initVec3(null, -2, -2, 0));

        double[] statementToOuterConditionCurvedArc02KnotLocations = VAlgebra.copyVec3(null, outerConditionBaseLocation);
        statementToOuterConditionCurvedArc02KnotLocations =
                VAlgebra.addVec3(statementToOuterConditionCurvedArc02KnotLocations,
                        statementToOuterConditionCurvedArc02KnotLocations, VAlgebra.initVec3(null, -1, -3, 0));

        double[] statementToOuterConditionCurvedArc03KnotLocations = VAlgebra.copyVec3(null, outerConditionBaseLocation);
        statementToOuterConditionCurvedArc03KnotLocations =
                VAlgebra.addVec3(statementToOuterConditionCurvedArc03KnotLocations,
                        statementToOuterConditionCurvedArc03KnotLocations, VAlgebra.initVec3(null, 0, -2, 0));

        List<double[]> statementToOuterConditionCurvedArcKnots = new ArrayList();
        statementToOuterConditionCurvedArcKnots.add(statementToOuterConditionCurvedArc01KnotLocations);
        statementToOuterConditionCurvedArcKnots.add(statementToOuterConditionCurvedArc02KnotLocations);
        statementToOuterConditionCurvedArcKnots.add(statementToOuterConditionCurvedArc03KnotLocations);


        double[] innerConditionTpStatementCurvedArcKnots01Location = VAlgebra.copyVec3(null, innerConditionBaseLocation);
        innerConditionTpStatementCurvedArcKnots01Location =
                VAlgebra.addVec3(innerConditionTpStatementCurvedArcKnots01Location,
                        innerConditionTpStatementCurvedArcKnots01Location, VAlgebra.initVec3(null, 2, 2, 0));

        double[] innerConditionTpStatementCurvedArcKnots02Location = VAlgebra.copyVec3(null, innerConditionBaseLocation);
        innerConditionTpStatementCurvedArcKnots02Location =
                VAlgebra.addVec3(innerConditionTpStatementCurvedArcKnots02Location,
                        innerConditionTpStatementCurvedArcKnots02Location, VAlgebra.initVec3(null, 1, 3, 0));

        double[] innerConditionTpStatementCurvedArcKnot03Location = VAlgebra.copyVec3(null, innerConditionBaseLocation);
        innerConditionTpStatementCurvedArcKnot03Location =
                VAlgebra.addVec3(innerConditionTpStatementCurvedArcKnot03Location,
                        innerConditionTpStatementCurvedArcKnot03Location, VAlgebra.initVec3(null, 0, 2, 0));

        List<double[]> statementToInnerConditionCurvedArcKnots = new ArrayList();
        statementToInnerConditionCurvedArcKnots.add(innerConditionTpStatementCurvedArcKnots01Location);
        statementToInnerConditionCurvedArcKnots.add(innerConditionTpStatementCurvedArcKnots02Location);
        statementToInnerConditionCurvedArcKnots.add(innerConditionTpStatementCurvedArcKnot03Location);
//
//        int a2pNum = 4;
//        double a2p[] = {
//                scrRad * 0.07, scrRad * 0.8,
//                scrRad * 0.13, scrRad * 0.3,
//                scrRad * 0.08, scrRad * 0.24,
//                scrRad * 0.02, scrRad * 0.3,
//        };

        int pgmInpStates[] = {MclnPalette.RED, MclnPalette.DARK_YELLOW};

        //
        // creating available Mcln Statement State list
        //

        MclnState philosopherInitialState = mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE);
        MclnState resourceInitialState = mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);

        MclnState philosopherContemplatingState = mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE);
        MclnState philosopherHungryState = mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.RED_STATE);
        MclnState philosopherEnergizingState = mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);
        MclnState philosopherFinishedEatingState = mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.PINK_STATE);

        Object[][] philosopherAvailableStatesData = {
                {philosopherContemplatingState, CONTEMPLATING_INTERPRETATION},
                {philosopherHungryState, HUNGRY_INTERPRETATION},
                {philosopherEnergizingState, ENERGIZING_INTERPRETATION},
                {philosopherFinishedEatingState, FINISHED_EATING_INTERPRETATION}
        };

        AvailableMclnStatementStates philosopherAvailableStates =
                AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                        philosopherAvailableStatesData, philosopherInitialState);


        Object[][] resourceAvailableStatesData = {
                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE), "is available"},
                {mclnStatesPalette.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE), "is in use"},
        };

        AvailableMclnStatementStates resourceAvailableStates =
                AvailableMclnStatementStates.createAvailableStatementStates(mclnStatesPalette,
                        resourceAvailableStatesData,                        resourceInitialState);

        philosopherContemplatingState = philosopherAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE);
        philosopherHungryState = philosopherAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE);
        philosopherEnergizingState = philosopherAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);
        philosopherFinishedEatingState = philosopherAvailableStates.getMclnState(ThreeShadesConfettiPalette.PINK_STATE);

        for (int i = 0; i < n; i++) {

            // S t a t e m e n t

            Object[][] stateDrivenProgramData = {
                    {philosopherContemplatingState, -5, philosopherHungryState},
                    {philosopherEnergizingState, -5, philosopherFinishedEatingState}
            };

            double[] statementLocation = VAlgebra.copyVec3(null, cSysBaseLocation);
            statementLocation = VAlgebra.transformVec3(statementLocation, rotAngle, scrTranVec);
//            AvailableMclnStatementStates availableMclnStatementStates =
//                    AvailableMclnStatementStates.createInstance(availableStatesData, philosopherInitialState);
            MclnStatement currentPhilosopher = mclnProject.createMclnStatementWithStateDrivenProgram(
                    PHILOSOPHERS[i], philosopherAvailableStates, statementLocation, philosopherInitialState,
                    stateDrivenProgramData);
            currentPhilosopher.setStateShouldBeLogged();

//            MclnStatement currentStatement = MclnModelFactory.createMclnStatement("Philosopher", statementLocation,
//                    availableStates, philosopherInitialState);
            mclnGraphModel.addMclnStatementAndUpdateView(currentPhilosopher);

//            curNode = mclnDisplayPanel.createNewNode(modelName, nodeVec[0], nodeVec[1],
//                    false,
//                    MclnPalette.CREATE_STATE,
//                    RtsMVNObject.TXT_DEFAILT);

//            currentStatement.setInitialState(MclnPalette.BLUE);


//            curNode.setStateDrivenISProgram(program);

            // R e s o u r c e
//            VAlgebra.initVec3(rVec, scrR_x, scrR_y, 0.);
//            VAlgebra.trfVec3(rVec, rotAngle, scrTranVec);

            double[] resourceStatementLocation = VAlgebra.copyVec3(null, pointOnCircle);
            resourceStatementLocation = VAlgebra.transformVec3(resourceStatementLocation, rotAngle, scrTranVec);

            MclnStatement currentResourceStatement = mclnProject.createMclnStatement("Chopstick",
                    resourceAvailableStates, resourceStatementLocation, resourceInitialState);
            mclnGraphModel.addMclnStatementAndUpdateView(currentResourceStatement);

//            mclnDisplayPanel.scrPntToCSysPnt(rVec, rVec);
//            resNode = mclnDisplayPanel.createNewNode(modelName,
//                    rVec[0], rVec[1], false,
//                    MclnPalette.CREATE_STATE,
//                    RtsMVNObject.TXT_DEFAILT);
//            resNode.setInitialState(MclnPalette.GREEN);
//
            //
            // O u t e r  Condition
            //

            double[] currentConditionLocation = VAlgebra.copyVec3(null, outerConditionBaseLocation);
            currentConditionLocation = VAlgebra.transformVec3(currentConditionLocation, rotAngle, scrTranVec);
            MclnCondition currentOuterCondition = mclnProject.createMclnCondition("Con", currentConditionLocation);
            mclnGraphModel.addMclnConditionAndUpdateView(currentOuterCondition);

            // outer condition to statement straight arc
            List<double[]> knotCSysLocations01 = new ArrayList();
            knotCSysLocations01.add(currentOuterCondition.getCSysLocation());
            knotCSysLocations01.add(currentPhilosopher.getCSysLocation());
            MclnState arcState =
                    philosopherAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);
            MclnArc inpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, knotCSysLocations01, arcState,
                    currentOuterCondition, currentPhilosopher);
            // currentOuterCondition.addInpArc(inpArc);
            mclnGraphModel.addMclnArcAndUpdateView(inpArc);

            // Outer Condition To Resource Inp Arc
            knotCSysLocations01 = new ArrayList();
            knotCSysLocations01.add(currentOuterCondition.getCSysLocation());
            knotCSysLocations01.add(currentResourceStatement.getCSysLocation());
            MclnState outerConditionToResourceInpArcState =
                    resourceAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE);
            MclnArc conditionToResourceInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                    knotCSysLocations01, outerConditionToResourceInpArcState, currentOuterCondition,
                    currentResourceStatement);
            //   currentOuterCondition.addInpArc(conditionToResourceInpArc);
            mclnGraphModel.addMclnArcAndUpdateView(conditionToResourceInpArc);

            // statement to outer condition curved arc
            List<double[]> newStatementToOuterConditionKnotLocations = new ArrayList();
            newStatementToOuterConditionKnotLocations.add(currentPhilosopher.getCSysLocation());
            for (double[] knot : statementToOuterConditionCurvedArcKnots) {
                double[] newKnot = VAlgebra.copyVec3(null, knot);
                newKnot = VAlgebra.transformVec3(newKnot, rotAngle, scrTranVec);
                newStatementToOuterConditionKnotLocations.add(newKnot);
            }
            newStatementToOuterConditionKnotLocations.add(currentOuterCondition.getCSysLocation());

            MclnState statementToOuterConditionArcState =
                    philosopherAvailableStates.getMclnState(ThreeShadesConfettiPalette.RED_STATE);
            MclnArc statementToOuterConditionCurvedArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                    newStatementToOuterConditionKnotLocations, statementToOuterConditionArcState,
                    currentPhilosopher, currentOuterCondition);
            //      currentOuterCondition.addInpArc(statementToOuterConditionCurvedArc);
            mclnGraphModel.addMclnArcAndUpdateView(statementToOuterConditionCurvedArc);

            // res To outer Condition Inp Arc
            List<double[]> resToOutConditionKnotCSysLocations = new ArrayList();
            resToOutConditionKnotCSysLocations.add(currentResourceStatement.getCSysLocation());

            double[] dotLocation = VAlgebra.copyVec3(null, outerConditionBaseLocation);
            dotLocation = VAlgebra.addVec3(dotLocation, dotLocation, VAlgebra.initVec3(null, 0.5, -1, 0));
            dotLocation = VAlgebra.transformVec3(dotLocation, rotAngle, scrTranVec);
            resToOutConditionKnotCSysLocations.add(dotLocation);

            resToOutConditionKnotCSysLocations.add(currentOuterCondition.getCSysLocation());

            MclnState resToConditionArcState =
                    resourceAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);
            MclnArc resToConditionInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                    resToOutConditionKnotCSysLocations, resToConditionArcState, currentResourceStatement,
                    currentOuterCondition);
            //  currentOuterCondition.addInpArc(resToConditionInpArc);
            mclnGraphModel.addMclnArcAndUpdateView(resToConditionInpArc);

            //
            // I n n e r  Condition
            //

            double[] currentInnerLocation = VAlgebra.copyVec3(null, innerConditionBaseLocation);
            currentInnerLocation = VAlgebra.transformVec3(currentInnerLocation, rotAngle, scrTranVec);
            MclnCondition currentInnerCondition = mclnProject.createMclnCondition("Con", currentInnerLocation);
            mclnGraphModel.addMclnConditionAndUpdateView(currentInnerCondition);

            // inner condition to statement straight arc
            List<double[]> knotCSysLocations02 = new ArrayList();
            knotCSysLocations02.add(currentInnerCondition.getCSysLocation());
            knotCSysLocations02.add(currentPhilosopher.getCSysLocation());
            MclnState innerConditionToStatementArcState =
                    philosopherAvailableStates.getMclnState(ThreeShadesConfettiPalette.BLUE_STATE);
            MclnArc inpArc02 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, knotCSysLocations02,
                    innerConditionToStatementArcState, currentInnerCondition,
                    currentPhilosopher);
//            currentInnerCondition.addInpArc(inpArc02);
            mclnGraphModel.addMclnArcAndUpdateView(inpArc02);


            // statement to inner condition curved arc
            List<double[]> newStatementToInnerConditionKnotLocations = new ArrayList();
            newStatementToInnerConditionKnotLocations.add(currentPhilosopher.getCSysLocation());
            for (double[] knot : statementToInnerConditionCurvedArcKnots) {
                double[] newKnot = VAlgebra.copyVec3(null, knot);
                newKnot = VAlgebra.transformVec3(newKnot, rotAngle, scrTranVec);
                newStatementToInnerConditionKnotLocations.add(newKnot);
            }
            newStatementToInnerConditionKnotLocations.add(currentInnerCondition.getCSysLocation());

            MclnState statementToInnerConditionArcState =
                    philosopherAvailableStates.getMclnState(ThreeShadesConfettiPalette.PINK_STATE);
            MclnArc statementToInnerConditionCurvedArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                    newStatementToInnerConditionKnotLocations, statementToInnerConditionArcState,
                    currentPhilosopher, currentInnerCondition);
//            currentInnerCondition.addInpArc(statementToInnerConditionCurvedArc);
            mclnGraphModel.addMclnArcAndUpdateView(statementToInnerConditionCurvedArc);


            // Inner Condition To Resource Inp Arc
            List<double[]> innerConditionToResKnotCSysLocations = new ArrayList();
            innerConditionToResKnotCSysLocations.add(currentInnerCondition.getCSysLocation());

            dotLocation = VAlgebra.copyVec3(null, innerConditionBaseLocation);
            dotLocation = VAlgebra.addVec3(dotLocation, dotLocation, VAlgebra.initVec3(null, 0.5, 1, 0));
            dotLocation = VAlgebra.transformVec3(dotLocation, rotAngle, scrTranVec);
            innerConditionToResKnotCSysLocations.add(dotLocation);

            innerConditionToResKnotCSysLocations.add(currentResourceStatement.getCSysLocation());

            MclnState innerConditionToResourceArcState =
                    resourceAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);
            MclnArc innerConditionToResourceInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                    innerConditionToResKnotCSysLocations, innerConditionToResourceArcState,
                    currentInnerCondition, currentResourceStatement);
//            currentInnerCondition.addInpArc(innerConditionToResourceInpArc);
            mclnGraphModel.addMclnArcAndUpdateView(innerConditionToResourceInpArc);

            if (i == 0) {
                firstOuterCondition = currentOuterCondition;
                firstInnerCondition = currentInnerCondition;
            } else /* create Arcs */ {
                //   VAlgebra.initVec3( arcPos, a5X, a5Y, 0 );
//                List<double[]> knotCSysLocations03 = new ArrayList();
//                knotCSysLocations03.add(currentResourceStatement.getCSysLocation());
//                knotCSysLocations03.add(currentOuterCondition.getCSysLocation());
//                MclnArc inpArc03 = MclnModelFactory.createMclnStraightArc(knotCSysLocations03, MclnPalette.GREEN,
//                        currentResourceStatement, currentOuterCondition);
//                currentOuterCondition.addInpArc(inpArc03);
//                mclnGraphModel.addMclnArcAndUpdateView(inpArc03);

                // Outer condition to last resource Arc
                knotCSysLocations01 = new ArrayList();
                knotCSysLocations01.add(currentOuterCondition.getCSysLocation());
                knotCSysLocations01.add(prevRes.getCSysLocation());

                MclnState outerConditionToLastResourceArcState =
                        resourceAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE);
                MclnArc outerConditionToLastResourceInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                        knotCSysLocations01, outerConditionToLastResourceArcState, currentOuterCondition, prevRes);
//                currentOuterCondition.addInpArc(outerConditionToLastResourceInpArc);
                mclnGraphModel.addMclnArcAndUpdateView(outerConditionToLastResourceInpArc);

                // Last resource to outer Condition Arc
                List<double[]> lastResToOutConditionKnotCSysLocations = new ArrayList();
                lastResToOutConditionKnotCSysLocations.add(prevRes.getCSysLocation());

                dotLocation = VAlgebra.copyVec3(null, outerConditionBaseLocation);
                dotLocation = VAlgebra.addVec3(dotLocation, dotLocation, VAlgebra.initVec3(null, -0.5, -1, 0));
                dotLocation = VAlgebra.transformVec3(dotLocation, rotAngle, scrTranVec);
                lastResToOutConditionKnotCSysLocations.add(dotLocation);

                lastResToOutConditionKnotCSysLocations.add(currentOuterCondition.getCSysLocation());

                MclnState lastResToConditionArcState =
                        resourceAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);
                MclnArc lastResToConditionInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                        lastResToOutConditionKnotCSysLocations, lastResToConditionArcState,
                        prevRes, currentOuterCondition);
//                currentOuterCondition.addInpArc(lastResToConditionInpArc);
                mclnGraphModel.addMclnArcAndUpdateView(lastResToConditionInpArc);

                //
                //  Inner Condition To Last resource Inp Arc
                //
                List<double[]> innerConditionToLastResKnotCSysLocations = new ArrayList();
                innerConditionToLastResKnotCSysLocations.add(currentInnerCondition.getCSysLocation());

                dotLocation = VAlgebra.copyVec3(null, innerConditionBaseLocation);
                dotLocation = VAlgebra.addVec3(dotLocation, dotLocation, VAlgebra.initVec3(null, -0.5, 1, 0));
                dotLocation = VAlgebra.transformVec3(dotLocation, rotAngle, scrTranVec);
                innerConditionToLastResKnotCSysLocations.add(dotLocation);

                innerConditionToLastResKnotCSysLocations.add(prevRes.getCSysLocation());

                MclnState innerConditionToLastResArcState =
                        resourceAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);
                MclnArc innerConditionToLastResInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                        innerConditionToLastResKnotCSysLocations, innerConditionToLastResArcState,
                        currentInnerCondition, prevRes);
//                currentInnerCondition.addInpArc(innerConditionToLastResInpArc);
                mclnGraphModel.addMclnArcAndUpdateView(innerConditionToLastResInpArc);
            }

            rotAngle -= angleStep;
            prevRes = currentResourceStatement;
        }

        // Outer condition to last resource Arc
        List<double[]> knotLocations = new ArrayList();
        knotLocations.add(firstOuterCondition.getCSysLocation());
        knotLocations.add(prevRes.getCSysLocation());

        MclnState outerConditionToLastResourceArcState =
                resourceAvailableStates.getMclnState(ThreeShadesConfettiPalette.YELLOW_STATE);
        MclnArc outerConditionToLastResourceInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                knotLocations, outerConditionToLastResourceArcState, firstOuterCondition, prevRes);
//        firstOuterCondition.addInpArc(outerConditionToLastResourceInpArc);
        mclnGraphModel.addMclnArcAndUpdateView(outerConditionToLastResourceInpArc);


        List<double[]> lastResToOutConditionKnotCSysLocations = new ArrayList();
        lastResToOutConditionKnotCSysLocations.add(prevRes.getCSysLocation());

        double[] dotLocation = VAlgebra.copyVec3(null, outerConditionBaseLocation);
        dotLocation = VAlgebra.addVec3(dotLocation, dotLocation, VAlgebra.initVec3(null, -0.5, -1, 0));
        dotLocation = VAlgebra.transformVec3(dotLocation, rotAngle, scrTranVec);
        lastResToOutConditionKnotCSysLocations.add(dotLocation);

        lastResToOutConditionKnotCSysLocations.add(firstOuterCondition.getCSysLocation());

        MclnState lastResToConditionArcState =
                resourceAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);
        MclnArc lastResToConditionInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                lastResToOutConditionKnotCSysLocations, lastResToConditionArcState, prevRes, firstOuterCondition);
//        firstOuterCondition.addInpArc(lastResToConditionInpArc);
        mclnGraphModel.addMclnArcAndUpdateView(lastResToConditionInpArc);


        //
        //  Inner Condition To Last resource Inp Arc
        //
        List<double[]> innerConditionToLastResKnotCSysLocations = new ArrayList();
        innerConditionToLastResKnotCSysLocations.add(firstInnerCondition.getCSysLocation());

        dotLocation = VAlgebra.copyVec3(null, innerConditionBaseLocation);
        dotLocation = VAlgebra.addVec3(dotLocation, dotLocation, VAlgebra.initVec3(null, -0.5, 1, 0));
        dotLocation = VAlgebra.transformVec3(dotLocation, rotAngle, scrTranVec);
        innerConditionToLastResKnotCSysLocations.add(dotLocation);

        innerConditionToLastResKnotCSysLocations.add(prevRes.getCSysLocation());

        MclnState innerConditionToLastResArcState =
                resourceAvailableStates.getMclnState(ThreeShadesConfettiPalette.GREEN_STATE);
        MclnArc innerConditionToLastResInpArc = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY,
                innerConditionToLastResKnotCSysLocations, innerConditionToLastResArcState, firstInnerCondition,
                prevRes);
//        firstInnerCondition.addInpArc(innerConditionToLastResInpArc);
        mclnGraphModel.addMclnArcAndUpdateView(innerConditionToLastResInpArc);
    }
}
