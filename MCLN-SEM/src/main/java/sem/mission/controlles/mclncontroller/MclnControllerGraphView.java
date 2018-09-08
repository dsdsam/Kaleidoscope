package sem.mission.controlles.mclncontroller;

import mclnview.graphview.MclnGraphModel;
import mclnview.graphview.MclnGraphView;
import mclnview.graphview.MclnGraphViewDefaultProperties;

public class MclnControllerGraphView extends MclnGraphView {

    /**
     *
     * @param mclnGraphModel
     * @param viewPadding
     * @param options
     * @param mclnGraphViewDefaultProperties
     */
    public MclnControllerGraphView(MclnGraphModel mclnGraphModel, int viewPadding, int options,
                                   MclnGraphViewDefaultProperties mclnGraphViewDefaultProperties) {
        super(mclnGraphModel, viewPadding, options, mclnGraphViewDefaultProperties);
    }

    @Override
    protected SUGGESTED_ARROW_SIZE getSuggestedArrowSize() {
        return SUGGESTED_ARROW_SIZE.SMALL;
    }

    @Override
    protected boolean isWorldVisible() {
        return false;
    }

    @Override
    protected boolean areAxesVisible() {
        return false;
    }

    @Override
    protected boolean isProjectSpaceRectangleVisible() {
        return true;
    }
}
