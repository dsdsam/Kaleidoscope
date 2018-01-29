package dsdsse.designspace;

import dsdsse.designspace.mcln.model.mcln.MclnModeChangedListener;
import mcln.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Admin on 1/18/2016.
 */
public class DesignStatusView extends JPanel {

    public static final int PREFERRED_HEIGHT = 90;

    private static final Dimension HEADER_SIZE = new Dimension(200, 16);
    private static final Dimension LABEL_SIZE = new Dimension(116, 16);
    private static final Dimension VALUE_SIZE = new Dimension(0, 16);

    private static final DesignStatusView designStatusView = new DesignStatusView();

    public static DesignStatusView getInstance() {
        return designStatusView;
    }

    private static JLabel setupLabel(String text, Dimension size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("monospaced", Font.PLAIN, 12));
        label.setPreferredSize(size);
        label.setMaximumSize(size);
        label.setMinimumSize(size);
        return label;
    }

    private final JLabel numberOfCreatedEntitiesLabel = setupLabel("Number of created elements:", HEADER_SIZE);

    private final JLabel numberOfStatementsLabel = setupLabel("Property nodes:", LABEL_SIZE);
    private final JLabel numberOfStatementsValue = setupLabel(" 0", VALUE_SIZE);

    private final JLabel numberOfConditionsLabel = setupLabel("Condition nodes:", LABEL_SIZE);
    private final JLabel numberOfConditionsValue = setupLabel(" 0", VALUE_SIZE);

    private final JLabel numberOfArcsLabel = setupLabel("Connecting Arcs:", LABEL_SIZE);
    private final JLabel numberOfArcsValue = setupLabel(" 0", VALUE_SIZE);

    // Model Change Listener

    private final MclnModeChangedListener mclnModeChangedListener = new MclnModeChangedListener() {

        private static final String FORMAT1 = "%d";

        @Override
        public void mclnModelCleared() {
            numberOfStatementsValue.setText(" 0");
            numberOfConditionsValue.setText(" 0");
            numberOfArcsValue.setText(" 0");
        }

        @Override
        public void mclnModelUpdated(MclnModel mclnModel) {
            updateStatus(mclnModel);
        }

        @Override
        public void onCurrentMclnModelReplaced(MclnModel mclnModel) {
            updateStatus(mclnModel);
        }

        @Override
        public void demoProjectComplete(MclnModel mclnModel) {
            updateStatus(mclnModel);
        }

        private void updateStatus(MclnModel mclnModel) {
            int statementsSize = intToString(FORMAT1, mclnModel.getNumberOfStatements()).length();
            int conditionsSize = intToString(FORMAT1, mclnModel.getNumberOfConditions()).length();
            int arcsSize = intToString(FORMAT1, mclnModel.getNumberOfArcs()).length();
            int max = Integer.max(Integer.max(statementsSize, conditionsSize), arcsSize);
            String format2 = "%" + (max + 1) + "d";

            String statementMinID = mclnModel.getStatementMinId();
            String statementLastID = mclnModel.getStatementMaxId();
            String conditionMinID = mclnModel.getConditionMinId();
            String conditionLastID = mclnModel.getConditionMaxId();
            String arcMinID = mclnModel.getArcMinId();
            String arcLastID = mclnModel.getArcMaxId();

            numberOfStatementsValue.setText(intToString(format2, mclnModel.getNumberOfStatements()) +
                    "  :  [  Min ID = " + statementMinID + ",  Max ID = " + statementLastID + "  ]");
            numberOfConditionsValue.setText(intToString(format2, mclnModel.getNumberOfConditions()) +
                    "  :  [  Min ID = " + conditionMinID + ",  Max ID = " + conditionLastID + "  ]");
            numberOfArcsValue.setText(intToString(format2, mclnModel.getNumberOfArcs()) +
                    "  :  [  Min ID = " + arcMinID + ",  Max ID = " + arcLastID + "  ]");
        }

        private final String intToString(String format, int number) {
            return String.format(format, number);
        }

    };

    public MclnModeChangedListener getMclnModeChangedListener() {
        return mclnModeChangedListener;
    }

    /**
     *
     */
    private DesignStatusView() {
        super(new GridBagLayout());
        initLayout();
        setBorder(new EmptyBorder(0, 10, 0, 0));
    }

    private final void initLayout() {

        add(numberOfCreatedEntitiesLabel, new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 0, 2, 0), 0, 0));

        add(numberOfStatementsLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 0, 0));
//        add(Box.createHorizontalStrut(1), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
//                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));
        add(numberOfStatementsValue, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 0, 0));


        add(numberOfConditionsLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
//        add(Box.createHorizontalStrut(1), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
//                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
        add(numberOfConditionsValue, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));

        add(numberOfArcsLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
//        add(Box.createHorizontalStrut(1), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
//                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
        add(numberOfArcsValue, new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));

        add(Box.createVerticalStrut(3), new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 0, 0));
    }

    int getViewPreferredHeight() {
        return PREFERRED_HEIGHT;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), PREFERRED_HEIGHT);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(getWidth(), PREFERRED_HEIGHT);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(getWidth(), PREFERRED_HEIGHT);
    }

}
