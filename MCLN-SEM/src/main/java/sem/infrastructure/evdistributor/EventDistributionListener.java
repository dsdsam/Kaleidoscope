package sem.infrastructure.evdistributor;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Mar 6, 2012
 * Time: 7:48:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EventDistributionListener<DT, LT> extends java.util.EventListener {

    public void onGuiStateModelEventFired(SemEventDistributor.EventId eventId, Component source, DT arguments,
                                          LT callbackListener);
}
