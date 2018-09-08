package adf.csys.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class BasicCSysView extends CSysView {

    protected CSysRectangleEntity[] viewWorldEntityArray = new CSysRectangleEntity[0];

    private MouseAdapter mouseCrossHairsController = new MouseAdapter() {

        @Override
        public void mouseDragged(MouseEvent e) {
            setCrossHairs(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            setCrossHairs(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCrossHairs(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            BasicCSysView.this.setCrossHairs(-1, -1);
        }

        private void setCrossHairs(MouseEvent e) {
            int x = e.isControlDown() & e.isShiftDown() ? e.getX() : -1;
            int y = e.isControlDown() & e.isShiftDown() ? e.getY() : -1;
            BasicCSysView.this.setCrossHairs(x, y);
        }
    };

    public BasicCSysView(double cSysX, double cSysY, double cSysWidth, double cSysHeight,
                         int viewPadding, int options) {
        super(cSysX, cSysY, cSysWidth, cSysHeight, viewPadding, options);
        addMouseWheelListener(mouseCrossHairsController);
        addMouseListener(mouseCrossHairsController);
        addMouseMotionListener(mouseCrossHairsController);
    }

    @Override
    public void updateCSysEntList(double[][] mat43) {

    }

    private int crossHairsX = -1;
    private int crossHairsY = -1;

    private void setCrossHairs(int x, int y) {
        if (this.crossHairsX == x && this.crossHairsY == y) {
            return;
        }
        this.crossHairsX = x;
        this.crossHairsY = y;
        repaint();
    }

    public boolean isCrossHairsDisplayOn() {
        return (crossHairsX >= 0 && crossHairsY >= 0);
    }

    public void paintCrossHairs(Graphics g) {
        Rectangle bounds = getBounds();
        g.setColor(Color.BLUE);
        g.drawLine(bounds.x, crossHairsY, crossHairsX + bounds.width, crossHairsY);
        g.drawLine(crossHairsX, bounds.y, crossHairsX, crossHairsY + bounds.height);
    }
}
