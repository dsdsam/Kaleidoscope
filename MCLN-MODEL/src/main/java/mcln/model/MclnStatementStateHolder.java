package mcln.model;

import mcln.palette.MclnState;

/**
 * Created by Admin on 7/31/2016.
 */
public class MclnStatementStateHolder {

    private MclnState mclnState;
    private final MclnStatement mclnStatement;

   public MclnStatementStateHolder() {
        mclnStatement = null;
    }

    MclnStatementStateHolder(MclnStatement mclnStatement) {
        this.mclnStatement = mclnStatement;
    }

    MclnState getCurrentMclnState() {
        if (mclnStatement != null) {
            return mclnStatement.getCurrentMclnStateForHolder();
        }
        return mclnState;
    }

    /**
     * The call to mclnStatement.setCurrentMclnStateByHolder(mclnState);
     * will also set MclnStatementState
     * @param mclnState
     */
    void setCurrentMclnState(MclnState mclnState) {
        if (mclnStatement != null) {
            mclnStatement.setCurrentMclnStateByHolder(mclnState);
        }
        this.mclnState = mclnState;
    }
}
