package adf.csys.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

public class BasicEntityCSysView extends CSysView {

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
            BasicEntityCSysView.this.setCrossHairs(-1, -1);
        }

        private void setCrossHairs(MouseEvent e) {
            int x = e.isControlDown() ? e.getX() : -1;
            int y = e.isControlDown() ? e.getY() : -1;
            BasicEntityCSysView.this.setCrossHairs(x, y);
        }
    };

    public BasicEntityCSysView(double cSysX, double cSysY, double cSysWidth, double cSysHeight,
                               int viewPadding, int options) {
        super(cSysX, cSysY, cSysWidth, cSysHeight, viewPadding, options);
        addMouseWheelListener(mouseCrossHairsController);
        addMouseListener(mouseCrossHairsController);
        addMouseMotionListener(mouseCrossHairsController);
    }

    @Override
    public void updateCSysEntList(double[][] mat43) {

    }

    protected int x = -1;
    protected int y = -1;

    private void setCrossHairs(int x, int y) {
        if (this.x == x && this.y == y) {
            return;
        }
        this.x = x;
        this.y = y;
        repaint();
    }

    public boolean isCrossHairsDisplayOn() {
        return (x >= 0 && y >= 0);
    }

    public void paintCrossHairs(Graphics g) {
        Rectangle bounds = getBounds();
        g.setColor(Color.BLUE);
        g.drawLine(bounds.x, y, x + bounds.width, y);
        g.drawLine(x, bounds.y, x, y + bounds.height);
    }
}
