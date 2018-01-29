package sem.mission.controlles.modelcontroller;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 29, 2011
 * Time: 2:43:17 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasicOperationExecutionController {

    abstract boolean executeStep();

    abstract boolean isOperationBeingExecuted();

    abstract void cancelCurrentOperation();

    abstract void stopOperationOnMissionStoped();


}
