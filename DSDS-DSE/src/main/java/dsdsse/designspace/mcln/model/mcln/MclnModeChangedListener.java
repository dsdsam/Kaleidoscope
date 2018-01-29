package dsdsse.designspace.mcln.model.mcln;

import mcln.model.MclnModel;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 2/1/14
 * Time: 8:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MclnModeChangedListener {

    public void mclnModelCleared();

    public void mclnModelUpdated(MclnModel mclnModel);

    public void onCurrentMclnModelReplaced(MclnModel newMclnModel);

    public void demoProjectComplete(MclnModel newMclnModel);
}
