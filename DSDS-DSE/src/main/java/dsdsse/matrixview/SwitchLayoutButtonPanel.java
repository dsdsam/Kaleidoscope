package dsdsse.matrixview;

import mclnmatrix.view.MclnMatrixView;
import mclnmatrix.view.ViewLayoutToggleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

class SwitchLayoutButtonPanel extends JPanel {

    private ViewLayoutToggleButton viewLayoutButton;

    SwitchLayoutButtonPanel(ActionListener switchViewLayoutActionListener) {
        super(new GridBagLayout());
        initLayout(switchViewLayoutActionListener);
    }

    public void setButtonEnabled(boolean status) {
        viewLayoutButton.setEnabled(status);
    }

    public void resetDefaultLayout() {
        viewLayoutButton.resetDefaultLayout(true);
    }

    /**
     * @param switchViewLayoutActionListener
     */
    private void initLayout(ActionListener switchViewLayoutActionListener) {
        viewLayoutButton = new ViewLayoutToggleButton(MclnMatrixView.HORIZONTAL_LAYOUT_REQUEST,
                MclnMatrixView.VERTICAL_LAYOUT_REQUEST, switchViewLayoutActionListener);
        add(viewLayoutButton,
                new GridBagConstraints(1, 1, 1, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(3, 0, 3, 0), 0, 0));
    }
}
