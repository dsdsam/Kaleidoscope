package sem.mission.explorer.sensors;

import adf.csys.model.ModelLineEntity;
import sem.mission.explorer.model.Surface;
import sem.mission.explorer.model.Track;

public class TrackRearTouchSensor {

    private Surface surface;
    private Track track;

    /**
     * @param track
     * @param surface
     */
    public TrackRearTouchSensor(Track track, Surface surface) {
        this.surface = surface;
        this.track = track;
        track.setRearTouchSensor(this);
    }

    /**
     * Called from the Model moving timer
     *
     * @return
     */
    public Track simulate() {
        ModelLineEntity rearLineEntity = (ModelLineEntity) track.getRearLineEntity();
        double[] point = rearLineEntity.getCurLineHead();
        boolean contains = surface.containsPoint(point);
        if (contains) {
            point = rearLineEntity.getCurLineEnd();
            contains = surface.containsPoint(point);
        }
        return (!contains) ? track : null;
    }
}
