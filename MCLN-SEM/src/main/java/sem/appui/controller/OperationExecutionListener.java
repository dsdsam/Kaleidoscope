package sem.appui.controller;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 2, 2011
 * Time: 4:49:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OperationExecutionListener {
    public void requestExecuted(BasicOperationRequest<?,?> basicOperationRequest);
}
