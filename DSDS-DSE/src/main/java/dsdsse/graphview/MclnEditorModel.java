package dsdsse.graphview;

import dsdsse.app.AppStateModel;
import dsdsse.graphview.MclnGraphViewEditor;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: May 18, 2013
 * Time: 6:26:43 PM
 * To change this template use File | Settings | File Templates.
 */
 public class MclnEditorModel {

    private AppStateModel.Operation prevOperation = AppStateModel.Operation.NONE;
    private AppStateModel.OperationStep prevStep = AppStateModel.OperationStep.NONE;

    private AppStateModel.Operation currentOperation = AppStateModel.Operation.NONE;
    private AppStateModel.OperationStep currentStep = AppStateModel.OperationStep.NONE;

    private MclnGraphViewEditor mclnGraphViewEditor;

    public MclnEditorModel(MclnGraphViewEditor mclnGraphViewEditor) {
        this.mclnGraphViewEditor = mclnGraphViewEditor;
    }

//    public boolean setOperationAndStep(AppStateModel.Operation operation, AppStateModel.OperationStep operationStep) {
//        System.out.println("Mcln Editor Model: simulatedPropertyStateChanged");
//        if (operation.isNone()) {
//            execStep(operationStep);
//            return true;
//        }
//
//        // Transition to the new operation
//        boolean newOperation = false;
//        AppStateModel.Operation prevOperation;
//        AppStateModel.OperationStep prevStep;
//
//        boolean success = terminateCurrentOperation(currentOperation, currentStep);
//        if (!success)
//            return success;
//
////  System.out.println(" setEditMode: "+currentOperation + " ----- "+ currentOperationStep);
//        newOperation = true;
//
//        prevOperation = currentOperation;
//        prevStep = this.currentStep;
//        clearOperation();
//        currentOperation = operation;
//        currentStep = operationStep;
//
//        mclnGraphViewEditor.prepareOperation(currentOperation, currentStep);
//
////        if (currentOperation == SET_STATE) {
////        } else {
////            MCLNMedia.hideInitAssistant();
////        }
//        return true;
//    }

    private void clearOperation() {

    }



    private boolean terminateCurrentOperation(AppStateModel.Operation currentOperation,
                                              AppStateModel.OperationStep currentOperationStep) {
        boolean ret;

//        if (dumpWhatToDo == CREATE_ARCS && dumpOper != TAKE_FIRST_NODE) {
//            switch (dumpOper) {
//                case TAKE_SECOND_NODE:
//                    ret = interruptDlg(dumpWhatToDo);
//                    if (!ret) return (ret);
//                    interruptTakeSecondArcNode();
//                    break;
//                case TAKE_ARC_THIRD_POINT:
//                    ret = interruptDlg(dumpWhatToDo);
//                    if (!ret) return (ret);
//                    interruptTakeArcThirdPoint();
//                    break;
//                case TAKE_ARC_KNOT_OR_NODE:
//                    ret = interruptDlg(dumpWhatToDo);
//                    if (!ret) return (ret);
//                    interruptTakeArcKnotOrNode();
//                    break;
//                case TAKE_ARC_KNOB:
//                    finishArcTakeKnob(true);
//                    break;
//            }
//        } // end  if ( dumpWhatToDo == CREATE_ARCS )
//
//        if (dumpWhatToDo == CREATE_FRAGMENT) {
//            switch (dumpOper) {
//                case TAKE_SECOND_NODE:
//                case TAKE_TRAN_PLACE:
//                    ret = interruptDlg(dumpWhatToDo);
//                    if (!ret) return (ret);
//                    interruptCreateModelTranFragment();
//                    break;
//            }
//        } // end  if ( dumpWhatToDo == CREATE_FRAGMENT )
        return true;
    }

    private void execStep(AppStateModel.OperationStep operationStep) {
        switch (operationStep) {
//            case SET_GRID_ON:
//                setGridOn(true);
//                if (isGridVisible())
//                    recreateScreenImage();
//                break;
//            case SET_GRID_OFF:
//                setGridOn(false);
//                if (isGridVisible())
//                    recreateScreenImage();
//                break;
//            case SET_GRID_VISIBLE:
//                setGridVisible(true);
//                recreateScreenImage();
//                break;
//            case SET_GRID_INVISIBLE:
//                setGridVisible(false);
//                recreateScreenImage();
//                break;
        }
    }


}
