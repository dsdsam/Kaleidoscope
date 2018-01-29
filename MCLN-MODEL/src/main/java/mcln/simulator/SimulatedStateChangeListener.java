package mcln.simulator;

import mcln.model.MclnStatement;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 12/12/13
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SimulatedStateChangeListener {

    public void simulatedPropertyStateChanged(MclnStatement mclnStatement);
}
