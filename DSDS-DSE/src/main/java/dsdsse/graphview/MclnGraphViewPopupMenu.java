package dsdsse.graphview;

import dsdsse.app.AppStateModel;
import mcln.model.MclnStatementState;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 8/7/2016.
 */
public class MclnGraphViewPopupMenu extends JPopupMenu {

    public static MclnGraphViewPopupMenu getPopupMenu(MclnPropertyView mclnPropertyView) {
        if (AppStateModel.getInstance().isDevelopmentMode()) {
            return new EditorPopupMenu(mclnPropertyView);
        } else if (AppStateModel.getInstance().isSimulationMode()) {
            return new SimulatorPopupMenu(mclnPropertyView);
        } else {
            return null;
        }
    }

    public static MclnGraphViewPopupMenu getPopupMenu(MclnConditionView mclnConditionView) {
        if (AppStateModel.getInstance().isDevelopmentMode()) {
            return new EditorPopupMenu(mclnConditionView);
        }
        return null;
    }

    public static MclnGraphViewPopupMenu getPopupMenu(MclnArcView mclnArcView) {
        if (AppStateModel.getInstance().isDevelopmentMode()) {
            return new EditorPopupMenu(mclnArcView);
        }
        return null;
    }

    MclnGraphViewPopupMenu() {
        setLightWeightPopupEnabled(false);
        setBorder(BorderFactory.createRaisedSoftBevelBorder());
    }

    static class ColoredSquare implements Icon {
        static ColoredSquare createInstance(MclnStatementState mclnStatementState, int stateID, Color itemColor) {
            return new ColoredSquare(mclnStatementState, stateID, itemColor);
        }

        private final MclnStatementState mclnStatementState;
        private final int stateID;
        private final Color itemColor;

        public ColoredSquare(MclnStatementState mclnStatementState, int stateID, Color itemColor) {
            this.mclnStatementState = mclnStatementState;
            this.stateID = stateID;
            this.itemColor = itemColor;
        }

        public int getStateID() {
            return stateID;
        }

        public Color getItemColor() {
            return itemColor;
        }

        public int getIconWidth() {
            return 12;
        }

        public int getIconHeight() {
            return 12;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color oldColor = g.getColor();
            g.setColor(itemColor);
            g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
            g.setColor(oldColor);
        }

        public MclnStatementState getMclnStatementState() {
            return mclnStatementState;
        }
    }
}
