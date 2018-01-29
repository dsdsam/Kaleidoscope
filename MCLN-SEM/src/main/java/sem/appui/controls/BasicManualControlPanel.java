package sem.appui.controls;

import sem.mission.explorer.model.SpaceExplorerModel;
import sem.appui.MissionControlCenterPanel;
import sem.infrastructure.evdistributor.EventDistributionAdapter;
import sem.infrastructure.evdistributor.SemEventDistributor;
import sem.mission.controlles.modelcontroller.actions.CallbackListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Nov 19, 2011
 * Time: 2:18:26 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasicManualControlPanel extends JPanel {

    protected SpaceExplorerModel spaceExplorerModel;
    MissionControlCenterPanel missionControlCenterPanel;

    protected boolean powerOn;
    private boolean manualControlOn = false;

    /**
     *
     */
    private EventDistributionAdapter missionEventDistributionListener = new EventDistributionAdapter<Boolean,
            CallbackListener>() {

        @Override
        public void onGuiStateModelEventFired(SemEventDistributor.EventId eventId, Component comp,
                                              Boolean argument, CallbackListener callbackListener) {

            switch (eventId) {
                case POWER:
                    boolean localPowerOn = argument;
                    powerOn = localPowerOn;
//                    panelEnergized(powerOn && manualControlOn);
                    panelEnergized(powerOn);
                    break;

                case MANUAL_CONTROL:
                    boolean autoManualSwitch = argument;
                    manualControlOn = autoManualSwitch;
//                    panelEnergized(powerOn && manualControlOn);
                    panelEnergized(powerOn);
                    break;

//                case RESET_TO_INITIAL_POSITION:
//                    panelEnergized(powerOn & manualControlOn);
//                    break;
            }

            repaint();
        }
    };


    protected boolean isManualControlOn(){
        return manualControlOn;
    }


    BasicManualControlPanel(MissionControlCenterPanel missionControlCenterPanel, Dimension size) {
        this.missionControlCenterPanel = missionControlCenterPanel;
        setSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setOpaque(false);
        SemEventDistributor.addEventDistributionListener(SemEventDistributor.EventGroup.MISSION_EVENT,
                missionEventDistributionListener);
    }

    /**
     * should be overriden
     *
     * @param status
     */
     protected abstract void panelEnergized(boolean status);

}
