package dsdsse.dialogs.creation.project;

import mcln.model.MclnDoubleRectangle;

import javax.swing.*;
import java.awt.*;

public final class ProjectCornersEntryPanel extends JPanel {

    private static final int PROJECT_SPACE_ENTRY_PANEL_WIDTH = 400;
    private static final int PROJECT_SPACE_ENTRY_PANEL_HEIGHT = 110;
    private static final Dimension PROJECT_SPACE_ENTRY_PANEL_SIZE =
            new Dimension(PROJECT_SPACE_ENTRY_PANEL_WIDTH, PROJECT_SPACE_ENTRY_PANEL_HEIGHT);

    static final ProjectCornersEntryPanel createProjectSpaceEntryPanel() {
        return new ProjectCornersEntryPanel();
    }

    private final ValueEntryPanel x1EntryPanel = ValueEntryPanel.createValueEntryPanel("Upper left X:");

    private final ValueEntryPanel y1EntryPanel = ValueEntryPanel.createValueEntryPanel("Upper left Y:");

    private final ValueEntryPanel x2EntryPanel = ValueEntryPanel.createValueEntryPanel("Lower right X:");

    private final ValueEntryPanel y2EntryPanel = ValueEntryPanel.createValueEntryPanel("Lower right Y:");

    private ProjectCornersEntryPanel() {
        super(new GridLayout(2, 2));
        setOpaque(false);
        setPreferredSize(PROJECT_SPACE_ENTRY_PANEL_SIZE);
        setMinimumSize(PROJECT_SPACE_ENTRY_PANEL_SIZE);
        setMaximumSize(PROJECT_SPACE_ENTRY_PANEL_SIZE);
        initContext();
    }

    private final void initContext() {
        add(x1EntryPanel);
        add(x2EntryPanel);
        add(y1EntryPanel);
        add(y2EntryPanel);
    }

    public final void setProjectSpaceRectangle(MclnDoubleRectangle mclnDoubleRectangle) {
        double x1 = mclnDoubleRectangle.getX();
        double y1 = mclnDoubleRectangle.getY();
        double x2 = x1 + mclnDoubleRectangle.getWidth();
        double y2 = y1 - mclnDoubleRectangle.getHeight();

        x1EntryPanel.setValue("" + x1);
        y1EntryPanel.setValue("" + y1);
        x2EntryPanel.setValue("" + x2);
        y2EntryPanel.setValue("" + y2);
    }

    final MclnDoubleRectangle getProjectSpaceRectangle() {
        MclnDoubleRectangle mclnDoubleRectangle = new MclnDoubleRectangle(0, 0, 0, 0);
        String strX1 = x1EntryPanel.getValue();
        String strY1 = y1EntryPanel.getValue();
        String strX2 = x2EntryPanel.getValue();
        String strY2 = y2EntryPanel.getValue();
        double x1 = stringToDouble(strX1);
        double y1 = stringToDouble(strY1);
        double x2 = stringToDouble(strX2);
        double y2 = stringToDouble(strY2);
        if (Double.isNaN(x1) || Double.isNaN(y1) || Double.isNaN(x2) || Double.isNaN(y2)) {
            return mclnDoubleRectangle;
        }
        double width = x2 - x1;
        double height = y1 - y2;
        mclnDoubleRectangle = new MclnDoubleRectangle(x1, y1, width, height);
        return mclnDoubleRectangle;
    }

    private final double stringToDouble(String text) {
        double doubleValue = Double.NaN;
        try {
            doubleValue = Double.parseDouble(text);
        } finally {
            return doubleValue;
        }
    }
}
