package mclnview.graphview.interfaces;

import mcln.model.MclnModel;

/**
 * Created by Admin on 11/15/2017.
 */
public interface MclnModeChangedListener {
    public void mclnModelCleared();

    public void mclnModelUpdated(MclnModel mclnModel);

    public void onCurrentMclnModelReplaced(MclnModel newMclnModel);

    public void demoProjectComplete(MclnModel newMclnModel);
}
