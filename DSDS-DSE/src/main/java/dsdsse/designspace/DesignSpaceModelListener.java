package dsdsse.designspace;

import mcln.model.MclnProject;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/1/13
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DesignSpaceModelListener {

    public void onMclnProjectReplaced(MclnProject newMclnProject);
}
