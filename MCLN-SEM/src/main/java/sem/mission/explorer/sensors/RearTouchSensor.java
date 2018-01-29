package sem.mission.explorer.sensors;

import sem.infrastructure.MclnEvent;
import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.controlles.modelcontroller.interfaces.ModelSensor;
import sem.mission.explorer.model.Surface;
import sem.mission.explorer.model.Track;

import java.util.ArrayList;
import java.util.List;

public class RearTouchSensor implements ModelSensor {

    // I n s t a n c e

    private List<TrackRearTouchSensor> sensors = new ArrayList();

    public RearTouchSensor(Track leftTrack, Track rightTrack, Surface surface) {
        TrackRearTouchSensor leftTrackTrackRearTouchSensor = new TrackRearTouchSensor(leftTrack, surface);
        sensors.add(leftTrackTrackRearTouchSensor);
        TrackRearTouchSensor rightTrackTrackRearTouchSensor = new TrackRearTouchSensor(rightTrack, surface);
        sensors.add(rightTrackTrackRearTouchSensor);
    }

    /**
     * implements Simulateable
     */
    @Override
    public void simulate() {
        for (TrackRearTouchSensor trackRearTouchSensor : sensors) {
            Track touchedTrack = trackRearTouchSensor.simulate();
            if (touchedTrack != null) {
                fireRearTouchEvent(touchedTrack);
                break;
            }
        }
    }

    private void fireRearTouchEvent(Track touchedTrack) {
        MclnEvent mclnEvent = new MclnEvent(MclnEvent.REAR_EOS_SENSOR_ON, touchedTrack.getNodeId());
        ModelController.getInstance().processSensorEvent(mclnEvent);
    }

}
