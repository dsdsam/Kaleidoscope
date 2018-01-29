package dsdsse.demo;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 2/21/14
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class OtherTriggerNotYetReady {

//    private static final ArrowTipLocationPolicy ARROW_TIP_LOCATION_POLICY =
//            ArrowTipLocationPolicy.DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION;
//
//    // palette & states
//    private static final MclnStatePalette DEFAULT_STATES_PALETTE = MclnPaletteFactory.getDefaultStatesPalette();
//    private static final List<MclnState> availableStates = MclnPaletteFactory.getDefaultStatesPalette().getAvailableStates();
//    private static final MclnState initialState = DEFAULT_STATES_PALETTE.getState(MclnStatePalette.DEVELOPMENT);
////    private static final MclnState initialState = DefaultStatesPalette.MCLN_CREATION_STATE;
//
//
//    /**
//     * @param mclnGraphModel
//     */
//    public static void createAnotherTrigger(MclnProject mclnProject, MclnGraphModel mclnGraphModel) {
//
//        double x0 = 0, y0 = 8;
//        createSimpleDFANonInpTick(mclnProject, mclnGraphModel, x0, y0);
//    }
//
//    /**
//     * @param x0
//     * @param y0
//     */
//    private static void createSimpleDFANonInpTick(MclnProject mclnProject, MclnGraphModel mclnGraphModel, double x0, double y0) {
//
//        MclnState statement01InitialState = DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.GRAY);
//        MclnState statement02InitialState = DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.RED);
//        MclnState statement03InitialState = DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.BLUE);
////
//        double n1X = x0, n1Y = y0 - 2;
//        double n2X = x0 - 1, n2Y = y0 - 2;
//        double n3X = x0 + 1, n3Y = y0 - 2;
//        double t1X = x0, t1Y = n2Y + 1.5;
//        double t2X = x0, t2Y = n2Y - 1.5;
//        double a1X = x0 - 0.25, a1Y = n1Y + 0.5;
//        double a2X = x0 + 0.25, a2Y = n1Y - 0.5;
//        double NPos[] = {0, 0, 0};
//        double TPos[] = {0, 0, 0};
//        double APos[] = {0, 0, 0};
////
//
//        Object[][] timeDrivenInputSimulatingProgramData = {
//                {3, DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.BLUE)},
//                {1, DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.GRAY)},
//                {2, DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.RED)},
//                {1, DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.GRAY)},
//
//        };
////        RtsMCN_Node Node_1, Node_2, Node_3;
////        RtsMCN_Transition Tran_1, Tran_2, Tran_3, Tran_4;
////
//        double n1Position[] = VAlgebra.initVec3(null, n1X, n1Y, 0);
//        MclnStatement statement01 = mclnProject.createMclnStatementWithTimeDrivenProgram("OutSt", null, n1Position,
//                statement01InitialState, timeDrivenInputSimulatingProgramData);
//        mclnGraphModel.addMclnStatementAndUpdateView(statement01);
//
////        VAlgebra.initVec3(NPos, n1X, n1Y, 0);
////        Node_1 = mclnDisplayPanel.createNewNode(modelName,
////                (float) NPos[0],
////                (float) NPos[1], true,
////                states[0],   //  RtsMVNObject.CREATE_STATE,
////                RtsMVNObject.TXT_NORTH);
//
//
//        double n2Position[] = VAlgebra.initVec3(null, n2X, n2Y, 0);
//        MclnStatement statement02 = mclnProject.createMclnStatement("OutSt", n2Position,
//                statement02InitialState);
//        mclnGraphModel.addMclnStatementAndUpdateView(statement02);
////        VAlgebra.initVec3(NPos, n2X, n2Y, 0);
////        Node_2 = mclnDisplayPanel.createNewNode(modelName,
////                (float) NPos[0],
////                (float) NPos[1], false,
////                states[0],//    RtsMVNObject.CREATE_COLOR,
////                RtsMVNObject.TXT_WEST_SOUTH);
////
//
//        double n3Position[] = VAlgebra.initVec3(null, n3X, n3Y, 0);
//        MclnStatement statement03 = mclnProject.createMclnStatement("OutSt", n3Position,
//                statement03InitialState);
//        mclnGraphModel.addMclnStatementAndUpdateView(statement03);
////        VAlgebra.initVec3(NPos, n3X, n3Y, 0);
////        Node_3 = mclnDisplayPanel.createNewNode(modelName,
////                (float) NPos[0],
////                (float) NPos[1], false,
////                MclnPalette.CREATE_STATE,
////                RtsMVNObject.TXT_SOUTH);
////
//
//        double[] cLocation = VAlgebra.initVec3(null, t1X, t1Y, 0);
//        MclnCondition condition01 = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY, statement02, statement03,
//                cLocation,
//                DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.RED),
//                DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.BLUE));
//        mclnGraphModel.addMclnConditionAndUpdateView(condition01);
//
//        double[] c2Location = VAlgebra.initVec3(null, t2X, t2Y, 0);
//        MclnCondition condition02 = mclnProject.createSCSFragment(ARROW_TIP_LOCATION_POLICY, statement03, statement02,
//                c2Location,
//                DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.BLUE),
//                DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.RED));
//        mclnGraphModel.addMclnConditionAndUpdateView(condition02);
//
//        MclnArc tmpArc;
//        tmpArc = condition01.getInpArc(0);
//        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
//        tmpArc = statement03.getInpArc(0);
//        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
//
//        tmpArc = condition02.getInpArc(0);
//        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
//        tmpArc = statement02.getInpArc(0);
//        mclnGraphModel.addMclnArcAndUpdateView(tmpArc);
//
////        VAlgebra.initVec3(TPos, t1X, t1Y, 0);
////        Tran_1 = mclnDisplayPanel.createNewTranFragment(modelName,
////                Node_2, Node_3,
////                (float) TPos[0],
////                (float) TPos[1],
////                states[0], states[1],
////                RtsLAlgebra.NO_THRESHOLD,
////                RtsLAlgebra.NO_THRESHOLD);
////
////        VAlgebra.initVec3(TPos, t2X, t2Y, 0);
////        Tran_2 = mclnDisplayPanel.createNewTranFragment(modelName,
////                Node_3, Node_2,
////                (float) TPos[0],
////                (float) TPos[1],
////                states[1], states[0],
////                RtsLAlgebra.NO_THRESHOLD,
////                RtsLAlgebra.NO_THRESHOLD);
////
//
//        double[] arcMidPointLocation;
//        List<double[]> arcKnotCSysLocations;
//
//        arcMidPointLocation = VAlgebra.initVec3(null, a1X, a1Y, 0);
//        arcKnotCSysLocations = new ArrayList();
//        arcKnotCSysLocations.add(statement01.getCSysLocation());
//        arcKnotCSysLocations.add(arcMidPointLocation);
//        arcKnotCSysLocations.add(condition01.getCSysLocation());
//        MclnArc mclnArc01 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations,
//                DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.RED), statement01, condition01);
//        // arc from statement to condition
////        statement01.addOutArc(mclnArc01);
////        condition01.addInpArc(mclnArc01);
//
//
//        arcMidPointLocation = VAlgebra.initVec3(null, a2X, a2Y, 0);
//        arcKnotCSysLocations = new ArrayList();
//        arcKnotCSysLocations.add(statement01.getCSysLocation());
//        arcKnotCSysLocations.add(arcMidPointLocation);
//        arcKnotCSysLocations.add(condition02.getCSysLocation());
//        MclnArc mclnArc02 = mclnProject.createMclnArc(ARROW_TIP_LOCATION_POLICY, arcKnotCSysLocations,
//                DEFAULT_STATES_PALETTE.getState(ThreeShadesConfettiPalette.BLUE), statement01, condition02);
//        // arc from statement to condition
////        statement01.addOutArc(mclnArc02);
////        condition02.addInpArc(mclnArc02);
//
//        mclnGraphModel.addMclnArcAndUpdateView(mclnArc01);
//        mclnGraphModel.addMclnArcAndUpdateView(mclnArc02);
////        VAlgebra.initVec3(APos, a1X, a1Y, 0);
////        mclnDisplayPanel.createNewArc(modelName,
////                Node_1, Tran_1,
////                (float) APos[0],
////                (float) APos[1],
////                states[0],
////                RtsLAlgebra.NO_THRESHOLD);
////
////        VAlgebra.initVec3(APos, a2X, a2Y, 0);
////        mclnDisplayPanel.createNewArc(modelName,
////                Node_1, Tran_2,
////                (float) APos[0],
////                (float) APos[1],
////                states[1],
////                RtsLAlgebra.NO_THRESHOLD);
////
////        int pgmN1States[] = {MclnPalette.BLUE, MclnPalette.GREEN,
////                MclnPalette.RED, MclnPalette.GREEN};
////        int prog1[][] = {{1, 0x000000},
////                {5, MclnPalette.BLUE},
////                {1, MclnPalette.CREATE_STATE},
////                {5, MclnPalette.RED},
////                {1, MclnPalette.CREATE_STATE}};
////        Node_1.setTimeDrivenISProgram(prog1);
//    }
//

}
