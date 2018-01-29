package sem.mission.explorer.sensors;

import adf.csys.model.ModelLineEntity;
import sem.mission.explorer.model.Surface;
import sem.mission.explorer.model.Track;

public class TrackFrontTouchSensor {

    private Surface surface;
    private Track track;

    /**
     * @param track
     * @param surface
     */
    public TrackFrontTouchSensor(Track track, Surface surface) {
        this.surface = surface;
        this.track = track;
        track.setFrontTouchSensor(this);
    }

    /**
     * Called from the Model moving timer
     *
     * @return
     */
    public Track simulate() {
        ModelLineEntity frontLineEntity = (ModelLineEntity) track.getFrontLineEntity();
        double[] point = frontLineEntity.getCurLineHead();
        boolean contains = surface.containsPoint(point);
        if (contains) {
            point = frontLineEntity.getCurLineEnd();
            contains = surface.containsPoint(point);
        }
        return (!contains) ? track : null;
    }
}
