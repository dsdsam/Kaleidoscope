package sem.mission.explorer.sensors;

import sem.infrastructure.MclnEvent;
import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.controlles.modelcontroller.interfaces.ModelSensor;
import sem.mission.explorer.model.Surface;
import sem.mission.explorer.model.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Apr 21, 2013
 * Time: 4:53:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class FrontTouchSensor implements ModelSensor {

    private List<TrackFrontTouchSensor> sensors = new ArrayList();

    public FrontTouchSensor(Track leftTrack, Track rightTrack, Surface surface) {
        TrackFrontTouchSensor leftTrackTrackFrontTouchSensor = new TrackFrontTouchSensor(leftTrack, surface);
        sensors.add(leftTrackTrackFrontTouchSensor);
        TrackFrontTouchSensor rightTrackTrackFrontTouchSensor = new TrackFrontTouchSensor(rightTrack, surface);
        sensors.add(rightTrackTrackFrontTouchSensor);
    }

    /**
     * implements Simulatable
     */
    @Override
    public void simulate() {
        for (TrackFrontTouchSensor trackFrontTouchSensor : sensors) {
            Track touchedTrack = trackFrontTouchSensor.simulate();
            if (touchedTrack != null) {
                fireFrontTouchEvent(touchedTrack);
                break;
            }
        }
    }

    private void fireFrontTouchEvent(Track touchedTrack) {
        MclnEvent mclnEvent = new MclnEvent(MclnEvent.FRONT_EOS_SENSOR_ON, touchedTrack.getNodeId());
        ModelController.getInstance().processSensorEvent(mclnEvent);
    }

}

