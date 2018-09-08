package mcln.simulator;

import mcln.model.MclnStatement;

public interface InputOutputStateChangeListener {

    void inputPropertyStateChanged(MclnStatement mclnStatement);

    default void outputPropertyStateChanged(MclnStatement mclnStatement) {

    }
}
