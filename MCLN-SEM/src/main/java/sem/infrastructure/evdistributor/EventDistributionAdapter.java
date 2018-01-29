package sem.infrastructure.evdistributor;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 3/6/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventDistributionAdapter<DT, LT> implements EventDistributionListener<DT, LT> {

    public void onGuiStateModelEventFired(SemEventDistributor.EventId eventId, Component comp, DT arguments,
                                          LT callbackListener) {
    }
}
