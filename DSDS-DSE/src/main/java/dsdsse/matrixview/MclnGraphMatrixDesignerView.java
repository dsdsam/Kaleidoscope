package dsdsse.matrixview;

import mclnmatrix.app.AoSUtils;
import mclnmatrix.model.MclnMatrixModel;
import mclnmatrix.view.MclnMatrixView;
import mclnmatrix.view.ScrollableMclnViewHolder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class MclnGraphMatrixDesignerView extends JPanel {

    private static final Logger logger = Logger.getLogger(MclnGraphMatrixDesignerView.class.getName());

    public static final String SIMPLE_MODEL_RELATIVE_FILE_PATH = "/relation/Simple Model.txt";
    public static final String MODEL_RELATIVE_FILE_PATH = "/relation/Model.txt";
    public static final String MODEL_IN_COMPACT_FORMAT_FILE_PATH = "/relation/modeCompactFormat .txt";

    public static final String DEFAULT_MODEL_RELATIVE_FILE_PATH = MODEL_IN_COMPACT_FORMAT_FILE_PATH;

    private static MclnGraphMatrixDesignerView mcLnGraphMatrixDesignerView;

    public static final synchronized MclnGraphMatrixDesignerView createInstance() {
        assert mcLnGraphMatrixDesignerView == null : "Mcln Graph Matrix Designer View is a singleton and already created";
        return mcLnGraphMatrixDesignerView = new MclnGraphMatrixDesignerView();
    }

    //   I n s t a n c e

    private final ScrollableMclnViewHolder currentMclnViewScrollablePanel = new ScrollableMclnViewHolder();
    private final SwitchLayoutButtonPanel switchLayoutButtonPanel;

    private MclnMatrixModel mclnMatrixModel;
    private MclnMatrixView mclnMatrixView;
    private int propertiesSize = 0;
    private int conditionsSize = 0;
    private boolean horizontalLayout = true;


    private final ActionListener switchViewLayoutActionListener = (e) -> {
        processSwitchViewLayoutEven(e);
    };

    //
    //   Processing Layout Switch Request
    //

    private void processSwitchViewLayoutEven(ActionEvent e) {
        String request = (String) e.getSource();
        if (request.equalsIgnoreCase(MclnMatrixView.VERTICAL_LAYOUT_REQUEST)) {
            horizontalLayout = false;
            mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, mclnMatrixModel, propertiesSize, conditionsSize);
        } else if (request.equalsIgnoreCase(MclnMatrixView.HORIZONTAL_LAYOUT_REQUEST)) {
            horizontalLayout = true;
            mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, mclnMatrixModel, propertiesSize, conditionsSize);
        }
        currentMclnViewScrollablePanel.initContent(mclnMatrixView);
    }

    /**
     * C o n s t r u c t i n g
     */
    private MclnGraphMatrixDesignerView() {
        super.setLayout(new BorderLayout());
        setBorder(new LineBorder(Color.GRAY));
        switchLayoutButtonPanel = new SwitchLayoutButtonPanel(switchViewLayoutActionListener);
        buildMclnGraphMatrixDesignerView();
    }

    private void buildMclnGraphMatrixDesignerView() {
        mclnMatrixModel = new MclnMatrixModel();
        boolean success = mclnMatrixModel.createModelFromFile(DEFAULT_MODEL_RELATIVE_FILE_PATH);
        if (!success) {
            System.out.println("The Model: " + DEFAULT_MODEL_RELATIVE_FILE_PATH + ",  was not loaded !");
            return;
        }

        //  ================   Model View Section   ====================

        AoSUtils.initOperations();
        propertiesSize = mclnMatrixModel.getPropertySize();
        conditionsSize = mclnMatrixModel.getConditionSize();
        mclnMatrixView = MclnMatrixView.createInstance(horizontalLayout, mclnMatrixModel, propertiesSize, conditionsSize);
        currentMclnViewScrollablePanel.initContent(mclnMatrixView);
        add(currentMclnViewScrollablePanel, BorderLayout.CENTER);

        //  ================   View Switch Button   ====================

        add(switchLayoutButtonPanel, BorderLayout.SOUTH);
    }
}
