package sem.mission.controlles.mclncontroller;

/**
 * Created by Admin on 11/11/2017.
 */
public final class OperationController {

    private static OperationController operationController;

    static OperationController getInstance() {
        return operationController;
    }

    private OperationController() {

    }
}
