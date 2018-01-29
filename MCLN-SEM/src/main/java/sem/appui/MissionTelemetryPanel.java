package sem.appui;

import led.LedSignPanel;
import sem.mission.controlles.modelcontroller.actions.CallbackListener;
import sem.infrastructure.evdistributor.EventDistributionAdapter;
import sem.infrastructure.evdistributor.SemEventDistributor;

import javax.swing.*;
import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Mar 30, 2012
 * Time: 9:23:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class MissionTelemetryPanel extends JPanel {

    protected boolean powerOn;
    private boolean manualControlOn = true;
    //    private LEDSign ledSign;
    private LedSignPanel ledSignPanel;

    private EventDistributionAdapter missionEventDistributionListener = new EventDistributionAdapter<Boolean,
            CallbackListener>() {

        @Override
        public void onGuiStateModelEventFired(SemEventDistributor.EventId eventId, Component comp,
                                              Boolean argument, CallbackListener callbackListener) {

            switch (eventId) {
                case POWER:
                    boolean localPoverOn = argument;
                    powerOn = localPoverOn;
                    panelEnergized(powerOn & manualControlOn);
                    break;

                case MANUAL_CONTROL:
                    boolean autoManualSwitch = argument;
                    manualControlOn = autoManualSwitch;
                    panelEnergized(powerOn & manualControlOn);
                    break;

                case RESET_TO_INITIAL_POSITION:
                    panelEnergized(powerOn & manualControlOn);
                    break;
            }

            repaint();
        }
    };

    /**
     *
     */
    public MissionTelemetryPanel(LedSignPanel ledSignPanel) {
        this.setLayout(new BorderLayout());
//        ledSign = new LEDSign();
        this.ledSignPanel = ledSignPanel;
        add(ledSignPanel, BorderLayout.CENTER);
//        add(ledSign, new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0,
//                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//                new Insets(0, 0, 0, 0), 0, 0));
        SemEventDistributor.addEventDistributionListener(SemEventDistributor.EventGroup.MISSION_EVENT,
                missionEventDistributionListener);
    }

    public void initPanel() {

    }

    /**
     * @param status
     */
    protected void panelEnergized(boolean status) {
        System.out.println("MissionTelemetryPanel Energized = " + status);
        if (status) {
//           ledSignPanel.startRunning();
        } else {
//           ledSignPanel.stopRunning();
        }
    }
}
