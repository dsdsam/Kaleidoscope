package mclnview.graphview;

import java.awt.*;

public class MclnGraphViewDefaultProperties {

    private final Color viewBackground;
    private final Color projectAreaBackground;
    private final Color projectAreaCoordinatesBackground;
    private final Color projectAreaBorderColor;
    private final Color axesColor;

    public MclnGraphViewDefaultProperties(Color viewBackground, Color projectAreaBackground,
                                          Color projectAreaCoordinatesBackground,
                                          Color projectAreaBorderColor, Color axesColor) {
        this.viewBackground = viewBackground;
        this.projectAreaBackground = projectAreaBackground;
        this.projectAreaCoordinatesBackground = projectAreaCoordinatesBackground;
        this.projectAreaBorderColor = projectAreaBorderColor;
        this.axesColor = axesColor;
    }

    public Color getViewBackground() {
        return viewBackground;
    }

    public Color getProjectAreaBackground() {
        return projectAreaBackground;
    }

    public Color getProjectAreaCoordinatesBackground() {
        return projectAreaCoordinatesBackground;
    }

    public Color getProjectAreaBorderColor() {
        return projectAreaBorderColor;
    }

    public Color getAxesColor() {
        return axesColor;
    }
}
