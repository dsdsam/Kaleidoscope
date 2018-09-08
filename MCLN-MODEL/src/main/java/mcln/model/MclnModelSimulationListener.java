package mcln.model;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 1/12/14
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MclnModelSimulationListener {

    public default void propertyStateChangedOnInputEvent(MclnStatement mclnStatement){

    }

    /**
     * The method is called from Mcln Model method
     * fireModelStateChanged.
     *
     * This method is implemented in Mcln Graph Model.
     * It is used to regenerate and repaint Mcln Graph
     * after model state changed as a result of input
     * event, inference, or reset.
     */
    public void mclnModelStateChanged();

    /**
     *
     */
    public void simulationStepExecuted();

    public default void propertyNewSuggestedStateInferred(MclnStatement mclnStatement){

    }

    public void mclnModelStateReset();

}
