package sem.appui;

import mclnview.graphview.MclnGraphView;
import mclnview.graphview.MclnPropertyView;
import sem.app.SemAppStateModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Admin on 11/16/2017.
 */
public final class MclnControllerHolderPanel extends JPanel {

    private static final MclnControllerHolderPanel mclnGraphViewHolderPanel = new MclnControllerHolderPanel();

    public static final MclnControllerHolderPanel getSingleton() {
        return mclnGraphViewHolderPanel;
    }

    //
    //   I n s t a n c e
    //

    private MclnGraphView mclnGraphView;

    private final MouseAdapter mouseAdapter = new MouseAdapter() {

        MclnPropertyView mclnPropertyView;

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!SemAppStateModel.isPowerON()) {
                return;
            }
            super.mouseReleased(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (!SemAppStateModel.isPowerON()) {
                return;
            }
            if ((mclnPropertyView = getPropertyNodeFromUnderMouse(e)) != null) {
                String subject = mclnPropertyView.getSubject();
                String propertyName = mclnPropertyView.getPropertyName();
                String text = subject + ": " + propertyName;
                mclnGraphView.setToolTipText(text);
            } else {
                mclnGraphView.setToolTipText(null);
            }
        }

        private MclnPropertyView getPropertyNodeFromUnderMouse(MouseEvent e) {
            if (mclnGraphView.isPointInsideProjectRectangle(e.getX(), e.getY())) {
                MclnPropertyView mclnPropertyView = mclnGraphView.getPropertyNodeAtCoordinates(e.getX(), e.getY());
                return mclnPropertyView;
            }
            return null;
        }
    };

    private MclnControllerHolderPanel() {
        super(new GridBagLayout());
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(450, 0));
        setMinimumSize(new Dimension(450, 0));
        Border outsideBorder = BorderFactory.createEmptyBorder(1, 2, 0, 2);
        Border insideBorder = BorderFactory.createEtchedBorder(Color.LIGHT_GRAY, Color.GRAY);
        Border border = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
        setBorder(border);
        setVisible(true);
    }

    public void setMclnGraphView(MclnGraphView mclnGraphView) {
        this.mclnGraphView = mclnGraphView;
        add(mclnGraphView, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(7, 0, 7, 0), 0, 0));
        mclnGraphView.addMouseListener(mouseAdapter);
        mclnGraphView.addMouseMotionListener(mouseAdapter);
    }
}
