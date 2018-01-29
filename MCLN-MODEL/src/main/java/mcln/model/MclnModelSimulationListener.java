package mcln.model;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 1/12/14
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MclnModelSimulationListener {

    public void simulationStepExecuted();

    public void mclnModelStateChanged();

    public void mclnModelStateReset();

}
