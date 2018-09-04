package dsdsse.matrixview;

import adf.onelinemessage.AdfOneLineMessageManager;
import mcln.model.MclnEntity;
import mcln.model.MclnModel;
import mclnmatrix.app.AoSUtils;
import mclnmatrix.model.MclnMatrixModel;
import mclnmatrix.view.BasicCellLabel;
import mclnmatrix.view.MclnMatrixView;
import mclnmatrix.view.ScrollableMclnViewHolder;
import mclnview.graphview.MclnGraphModel;
import mclnview.graphview.interfaces.MclnModeChangedListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class MclnDesignerMatrixView extends JPanel {

    private static final Logger logger = Logger.getLogger(MclnDesignerMatrixView.class.getName());

    private static MclnDesignerMatrixView mcLnDesignerMatrixView;

    public static final synchronized MclnDesignerMatrixView createInstance() {
        assert mcLnDesignerMatrixView == null : "Mcln Graph Matrix Designer View is a singleton and already created";
        return mcLnDesignerMatrixView = new MclnDesignerMatrixView();
    }

    //   I n s t a n c e

    private final ScrollableMclnViewHolder currentMclnViewScrollablePanel = new ScrollableMclnViewHolder();
    private final SwitchLayoutButtonPanel switchLayoutButtonPanel;

    private final MclnGraphModel mclnGraphModel;
    private MclnMatrixModel mclnMatrixModel;
    private MclnMatrixView mclnMatrixView;
    private int propertiesSize = 0;
    private int conditionsSize = 0;
    private boolean horizontalLayout = true;

    //   Mouse listener to show cell state when mouse hovers cell

    private MouseAdapter mouseListenerAdapter = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent me) {
            if (mclnMatrixModel.isModelEmpty()) {
                return;
            }
            BasicCellLabel basicCellLabel = mclnMatrixView.isMouseHoverCell(me);
            if (basicCellLabel == null) {
                AdfOneLineMessageManager.clearMessage();
                return;
            }

            Object source = basicCellLabel.getSource();
            String entityInfo = "Empty cell.";
            if (source instanceof MclnEntity && !basicCellLabel.isEmptyState()) {
                MclnEntity mclnEntity = (MclnEntity) source;
                entityInfo = mclnEntity.getOneLineInfoMessage();
                AdfOneLineMessageManager.showInfoMessage(entityInfo);
            }
            AdfOneLineMessageManager.showInfoMessage(entityInfo);
        }
    };

    //   MclnModeChangedListener listens MsLN model creation, replacement and removal

    private MclnModeChangedListener mclnModeChangedListener = new MclnModeChangedListener() {

        @Override
        public void mclnModelCleared() {
            if (mclnMatrixView != null) {
                mclnMatrixView.removeMouseMotionListener(mouseListenerAdapter);
            }
            horizontalLayout = true;
            switchLayoutButtonPanel.resetDefaultLayout();
            buildEmptyMclnGraphMatrixDesignerView();
        }

        @Override
        public void mclnModelUpdated(MclnModel mclnModel) {
//            createAndDisplayNewOffScreenImage();
        }

        /**
         * called when new project is retrieved or when the project model changed
         *
         * Creates MCLN Graph view for each Mcln Model element
         * Creates Screen image and initializes it.
         *
         * @param newMclnModel
         */
        @Override
        public void onCurrentMclnModelReplaced(MclnModel newMclnModel) {
            if (newMclnModel.isEmpty()) {
                return;
            }
            mclnMatrixModel = ModelToModelConverter.convertMclnModelToMatrixModel(mclnGraphModel, newMclnModel);
            horizontalLayout = true;
            switchLayoutButtonPanel.resetDefaultLayout();
            buildMclnMatrixViewFromMatrixModel(horizontalLayout, mclnMatrixModel);
        }

        @Override
        public void demoProjectComplete(MclnModel newMclnModel) {
            if (newMclnModel.isEmpty()) {
                return;
            }
            mclnMatrixModel = ModelToModelConverter.convertMclnModelToMatrixModel(mclnGraphModel, newMclnModel);
            horizontalLayout = true;
            switchLayoutButtonPanel.resetDefaultLayout();
            buildMclnMatrixViewFromMatrixModel(horizontalLayout, mclnMatrixModel);
        }
    };

    //
    //   Processing Layout Switch Request
    //

    private final ActionListener switchViewLayoutActionListener = (e) -> {
        String request = (String) e.getSource();
        horizontalLayout = request.equalsIgnoreCase(MclnMatrixView.HORIZONTAL_LAYOUT_REQUEST);
        buildMclnMatrixViewFromMatrixModel(horizontalLayout, mclnMatrixModel);
    };

    private void buildMclnMatrixViewFromMatrixModel(boolean horizontalLayout, MclnMatrixModel mclnMatrixModel) {
        if (mclnMatrixView != null) {
            mclnMatrixView.removeMouseMotionListener(mouseListenerAdapter);
        }
        int propertiesSize = mclnMatrixModel.getPropertySize();
        int conditionsSize = mclnMatrixModel.getConditionSize();
        mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, mclnMatrixModel, propertiesSize, conditionsSize);
        mclnMatrixView.addMouseMotionListener(mouseListenerAdapter);
        currentMclnViewScrollablePanel.initContent(mclnMatrixView);
        switchLayoutButtonPanel.setButtonEnabled(!mclnMatrixModel.isModelEmpty());
        ((MclnBasedMatrixModel) mclnMatrixModel).setMclnDesignerMatrixView(mclnMatrixView);
        revalidate();
    }


    /**
     * C o n s t r u c t i n g
     */
    private MclnDesignerMatrixView() {
        super.setLayout(new BorderLayout());
        mclnGraphModel = MclnGraphModel.getInstance();
        setBorder(new LineBorder(Color.GRAY));
        switchLayoutButtonPanel = new SwitchLayoutButtonPanel(switchViewLayoutActionListener);
        AoSUtils.initOperations();
        buildEmptyMclnGraphMatrixDesignerView();
        mclnGraphModel.addMclnModeChangedListener(mclnModeChangedListener);
    }

    private void buildEmptyMclnGraphMatrixDesignerView() {
        removeAll();
        mclnMatrixModel = new MclnMatrixModel();

        //  ================   Model View Section   ====================
        horizontalLayout = true;
        mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, mclnMatrixModel, propertiesSize, conditionsSize);
        mclnMatrixView.addMouseMotionListener(mouseListenerAdapter);
        currentMclnViewScrollablePanel.initContent(mclnMatrixView);
        add(currentMclnViewScrollablePanel, BorderLayout.CENTER);

        //  ================   View Switch Button   ====================
        add(switchLayoutButtonPanel, BorderLayout.SOUTH);
        switchLayoutButtonPanel.setButtonEnabled(false);
    }
}
