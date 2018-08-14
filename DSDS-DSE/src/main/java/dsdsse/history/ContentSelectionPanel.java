package dsdsse.history;

import adf.utils.StandardFonts;
import dsdsse.designspace.DesignSpaceContentManager;
import dsdsse.designspace.DesignSpaceModel;
import dsdsse.designspace.DesignSpaceView;
import dsdsse.graphview.MclnGraphDesignerView;
import mcln.model.MclnProject;
import mclnview.graphview.MclnPropertyView;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/6/2016.
 */
public class ContentSelectionPanel extends JPanel {

    private static int EMPTY_TRACE_LOG_HEIGHT = 90;

    private static int TRACE_LOG_MIN_HEIGHT = TraceLogTitlePanel.TITLE_HEIGHT +
            5 * ExecutionHistoryPanel.ROW_HEIGHT;

    private static int TRACE_LOG_MAX_HEIGHT = TraceLogTitlePanel.TITLE_HEIGHT +
            10 * ExecutionHistoryPanel.ROW_HEIGHT + ExecutionHistoryPanel.ROW_HEIGHT / 2;

    private static final Dimension PREFERRED_SIZE = new Dimension(300, 1);
    private static final Color PANEL_BACKGROUND = new Color(0xFDFDFD);

    private static final String titleText = "              ID               |                   State Statement";
    private static final Dimension CHECKBOX_SIZE = new Dimension(13, 13);
    private static final Dimension ID_WIDTH = new Dimension(70, 12);
    private static final Dimension PROPERTY_WIDTH = new Dimension(100, 12);

    // I n s t a n c e

    private final ExecutionHistoryPanel executionHistoryPanel;
    private final DesignSpaceView designSpaceView;
    private final DesignSpaceModel getDesignSpaceModel;
    private final MclnGraphDesignerView mclnGraphDesignerView;
    private final List<MclnPropertyView> allPropertyList = new ArrayList();
    private final List<MclnPropertyView> presentedPropertyList = new ArrayList();

    private int nOfAllRows = 0;
    private int currentNOfRows;
    private List<PropertyRow> rows = new ArrayList();
    private JPanel listPanel = new JPanel(new GridBagLayout());
    private JScrollPane scrollPane = new JScrollPane(listPanel);

    private final List<PropertyRow> propertyRowList = new ArrayList();

    private final TraceLogTitlePanel traceLogTitlePanel = new TraceLogTitlePanel(titleText);

    /**
     * @param executionHistoryPanel
     * @param designSpaceView
     */
    ContentSelectionPanel(ExecutionHistoryPanel executionHistoryPanel, DesignSpaceView designSpaceView) {

        super(new BorderLayout());

        setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        this.executionHistoryPanel = executionHistoryPanel;

        setMinimumSize(PREFERRED_SIZE);

        this.designSpaceView = designSpaceView;
        getDesignSpaceModel = designSpaceView.getDesignSpaceModel();
        this.mclnGraphDesignerView = designSpaceView.getMclnGraphDesignerView();

        setBackground(PANEL_BACKGROUND);

        // this does not work (arrows are not re-sized)
//        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
//        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));

//        Dimension preferredSize = scrollBar.getPreferredSize();
//        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
//        scrollBar.setPreferredSize(new Dimension(14, 0));

        scrollPane.setBorder(new MatteBorder(0, 0, 0, 1, Color.WHITE));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        listPanel.setOpaque(false);

        prepareData();
//        init();

        traceLogTitlePanel.setBorder(new MatteBorder(0, 0, 0, 1, new Color(0xEEEEEE)));
        add(traceLogTitlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    BoundedRangeModel getScrollBarModel() {
        return scrollPane.getVerticalScrollBar().getModel();
    }

    void clearSelectionPanel() {
        prepareData();
        showAllProperties();
        DesignSpaceContentManager.setSavedSimulationStatusViewPanelHeight(-1);
        repaint();
    }

    void resetOnProjectModified() {
        prepareData();
        updateStatementsUpOnModelStateChanged();
        showAllProperties();
        repaint();
    }

    private void prepareData() {
        this.allPropertyList.clear();
        List<MclnPropertyView> propertyViewList = mclnGraphDesignerView.getPropertyViewList();
        this.allPropertyList.addAll(propertyViewList);

        listPanel.removeAll();
        propertyRowList.clear();

        nOfAllRows = allPropertyList.size();
        currentNOfRows = nOfAllRows;

        for (int i = 0; i < nOfAllRows; i++) {
            MclnPropertyView mcLnPropertyView = allPropertyList.get(i);
            PropertyRow propertyRow = new PropertyRow(mcLnPropertyView);
            propertyRowList.add(propertyRow);
            listPanel.add(propertyRow, new GridBagConstraints(0, i, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

            if ((i + 1) == nOfAllRows) {
                JComponent placeHolder = new JPanel();
                placeHolder.setOpaque(false);
                listPanel.add(placeHolder, new GridBagConstraints(0, i + 1, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            }
        }
    }

    public int getViewPreferredHeight() {
        int traceLogHeight = calculateHistoryPanelHeight(currentNOfRows);
        return traceLogHeight;
    }

    /**
     * Shows all Property nodes of the model
     * Changes Trace Log view height to feet reasonable number of rows
     */
    void showAllProperties() {
        presentedPropertyList.clear();
        int size = propertyRowList.size();


        for (int i = 0; i < size; i++) {
            PropertyRow propertyRow = propertyRowList.get(i);
            presentedPropertyList.add(propertyRow.getMcLnPropertyView());
            propertyRow.setCurrentlyVisible(i);
        }
        validate();
        executionHistoryPanel.setContents(presentedPropertyList);
        if (size <= 0) {
            return;
        }
        int simulationStatusViewPanelHeight;
        simulationStatusViewPanelHeight = calculateHistoryPanelHeight(size);
        DesignSpaceContentManager.setSimulationStatusViewPanelHeight(simulationStatusViewPanelHeight);
    }

    /**
     * Shows selected Property nodes of the model
     * Chang3es Trace Log view height to feet reasonable number of rows
     */
    void hideIfNotSelected() {
        int rowIndex = 0;
        presentedPropertyList.clear();
        for (PropertyRow propertyRow : propertyRowList) {
            if (propertyRow.isSelected()) {
                presentedPropertyList.add(propertyRow.getMcLnPropertyView());
                propertyRow.setCurrentlyVisible(rowIndex);
                rowIndex++;
            } else {
                propertyRow.setCurrentlyHidden();
            }
        }
        validate();
        executionHistoryPanel.setContents(presentedPropertyList);
        currentNOfRows = rowIndex;
        int simulationStatusViewPanelHeight = calculateHistoryPanelHeight(currentNOfRows);
        DesignSpaceContentManager.setSimulationStatusViewPanelHeight(simulationStatusViewPanelHeight);
    }

    /**
     * Calculates reasonable number of rows to be shown Trace Log view
     *
     * @param numberOfPropertiesToShow
     * @return
     */
    private int calculateHistoryPanelHeight(int numberOfPropertiesToShow) {
        if (numberOfPropertiesToShow <= 0) {
            return EMPTY_TRACE_LOG_HEIGHT;
        }
        int traceLogHeight = TraceLogTitlePanel.TITLE_HEIGHT +
                numberOfPropertiesToShow * ExecutionHistoryPanel.ROW_HEIGHT;

        int actualTraceLogHeight = traceLogHeight;
        if (traceLogHeight < TRACE_LOG_MIN_HEIGHT) {
            actualTraceLogHeight = TRACE_LOG_MIN_HEIGHT;
        } else if (traceLogHeight > TRACE_LOG_MAX_HEIGHT) {
            actualTraceLogHeight = TRACE_LOG_MAX_HEIGHT;
        }
        // This check added to prevent Design Space to
        // change its height when switching from
        // Development to Simulation mode back and forth
        if (!MclnProject.getInstance().isDemoProject()) {
            return EMPTY_TRACE_LOG_HEIGHT;
        }
        return actualTraceLogHeight + ExecutionHistoryPanel.ROW_HEIGHT;
    }

    /**
     * Updates State Statement upon model state changed
     */
    public void updateStatementsUpOnModelStateChanged() {
        int size = propertyRowList.size();
        presentedPropertyList.clear();
        for (int i = 0; i < size; i++) {
            PropertyRow propertyRow = propertyRowList.get(i);
            propertyRow.updatePropertyStateUponSimulationStepExecuted();
        }
        repaint();
    }

    /**
     *
     */
    private static class PropertyRow extends JPanel {

        private final JCheckBox checkBox;
        private final JLabel uidLabel = new JLabel("", JLabel.LEFT);
        private final JLabel propertyLabel = new JLabel("", JLabel.LEFT);
        private final JLabel stateLabel = new JLabel("", JLabel.LEFT);

        private int rowIndex;
        private boolean selected;
        private final MclnPropertyView mcLnPropertyView;
        private final String uID;
        private final String fullSubject;
        private String currentStatementState;

        PropertyRow(MclnPropertyView mcLnPropertyView) {
            super(new GridBagLayout());
            setBorder(new MatteBorder(0, 0, 1, 0, ExecutionHistoryPanel.LINE_SEPARATOR_COLOR));
            setPreferredSize(ExecutionHistoryPanel.ROW_SIZE);
            setMinimumSize(ExecutionHistoryPanel.ROW_SIZE);
            setMaximumSize(ExecutionHistoryPanel.ROW_SIZE);

            this.mcLnPropertyView = mcLnPropertyView;
            uID = mcLnPropertyView.getUID();
            fullSubject = mcLnPropertyView.getFullSubject();
            currentStatementState = mcLnPropertyView.getPropertyStatement();

            checkBox = new JCheckBox();
            checkBox.setPreferredSize(CHECKBOX_SIZE);
            checkBox.setMinimumSize(CHECKBOX_SIZE);
            checkBox.setMaximumSize(CHECKBOX_SIZE);
            checkBox.setBorder(null);
            checkBox.addItemListener((e) -> {
                boolean status = e.getStateChange() == ItemEvent.SELECTED;
                selected = status;
            });

            uidLabel.setFont(StandardFonts.FONT_MONOSPACED_PLAIN_11);
            uidLabel.setPreferredSize(ID_WIDTH);
            uidLabel.setMinimumSize(ID_WIDTH);
            uidLabel.setMaximumSize(ID_WIDTH);

            propertyLabel.setFont(StandardFonts.FONT_HELVETICA_PLAIN_11);
            propertyLabel.setPreferredSize(PROPERTY_WIDTH);
            propertyLabel.setMinimumSize(PROPERTY_WIDTH);
            propertyLabel.setMaximumSize(PROPERTY_WIDTH);

            stateLabel.setFont(StandardFonts.FONT_HELVETICA_PLAIN_11);

            uidLabel.setText(uID);
            propertyLabel.setText(fullSubject);
            stateLabel.setText(currentStatementState);

            add(checkBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 6, 0, 0), 0, 0));

            add(uidLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 8, 0, 0), 0, 0));

            add(stateLabel, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 20, 0, 0), 0, 0));

        }

        /**
         *
         */
        private void updatePropertyStateUponSimulationStepExecuted() {
            String currentStatementState = mcLnPropertyView.getPropertyStatement();
            stateLabel.setText(currentStatementState);
        }

        boolean isSelected() {
            return selected;
        }

        private MclnPropertyView getMcLnPropertyView() {
            return mcLnPropertyView;
        }

        void setCurrentlyVisible(int rowIndex) {
            this.rowIndex = rowIndex;
            setVisible(true);
        }

        void setCurrentlyHidden() {
            rowIndex = 0;
            setVisible(false);
        }

        @Override
        public void paintComponent(Graphics g) {
            Rectangle rect = getBounds();
            Dimension size = getSize();
            Color color;
            if ((rowIndex % 2) == 0) {
                color = Color.WHITE;
            } else {
                color = new Color(0xDDDDFF);
            }
            g.setColor(color);
            g.fillRect(0, 0, size.width, size.height);
        }
    }
}
