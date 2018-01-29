package dsdsse.designspace;



import dsdsse.designspace.mcln.model.mcln.MclnGraphModel;
import dsdsse.graphview.MclnGraphDesignerView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Mar 13, 2013
 * Time: 10:36:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnSimulatorView extends JPanel {

    private DesignSpaceModel designSpaceModel;
    private MclnGraphModel mclnGraphModel;
    MclnGraphDesignerView mclnGraphDesignerView;

    MclnSimulatorView(DesignSpaceModel designSpaceModel, MclnGraphModel mclnGraphModel, MclnGraphDesignerView mclnGraphDesignerView) {
        this.designSpaceModel = designSpaceModel;
        this.mclnGraphModel = mclnGraphModel;
        this.mclnGraphDesignerView = mclnGraphDesignerView;

        mclnGraphDesignerView.setGridVisible(true);
        mclnGraphDesignerView.setOpaque(true);
        mclnGraphDesignerView.setBackground(Color.WHITE);

        setLayout(new BorderLayout());
        add(mclnGraphDesignerView, BorderLayout.CENTER);
    }
}
