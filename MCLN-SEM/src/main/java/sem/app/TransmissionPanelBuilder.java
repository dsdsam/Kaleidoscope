package sem.app;

import led.LedSignPanel;

import javax.swing.*;
import java.util.logging.Logger;
import java.awt.*;

import sem.appui.MissionTransmissionProvider;
import sem.appui.MissionTelemetryPanel;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Apr 20, 2013
 * Time: 2:37:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransmissionPanelBuilder { //extends BasicLauncher {

    private static final Logger logger = Logger.getLogger(TransmissionPanelBuilder.class.getName());
    private static final String FONT_FILE_NAME = "/led-app-resources/fonts/default.font";
    private static TransmissionPanelBuilder transmissionPanelBuilder = new TransmissionPanelBuilder();
    public static TransmissionPanelBuilder getInstance() {
        return transmissionPanelBuilder;
    }

    /**
     *
     */
    public MissionTelemetryPanel buildEmbeddedMissionTelemetryPanel() {
        boolean useEDT = true;
        LedSignPanel ledSignPanel = new LedSignPanel(useEDT, FONT_FILE_NAME);
        MissionTransmissionProvider missionTransmissionProvider =
                MissionTransmissionProvider.createInstance(ledSignPanel);
        ledSignPanel.initLedSignPanel(missionTransmissionProvider, false);

        MissionTelemetryPanel missionTelemetryPanel = new MissionTelemetryPanel(ledSignPanel);
         missionTelemetryPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));

        // the setting preferred size height determines the height of Running Sign text
//        missionTelemetryPanel.setPreferredSize(new Dimension(1, 25));
        missionTelemetryPanel.setPreferredSize(new Dimension(1, 35));

        return missionTelemetryPanel;
    }

}

