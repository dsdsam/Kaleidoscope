package dsdsse.printing;

import adf.utils.StandardFonts;
import dsdsse.designspace.DesignSpaceModel;
import dsdsse.graphview.MclnGraphDesignerView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 9/13/2016.
 */
public final class MclnPrintPreviewPanel extends JPanel {

    public enum Orientation {PORTRAIT, LANDSCAPE}

    public static final int SHORT_EDGE_LENGTH = 612;
    private static final int LONG_EDGE_LENGTH = 792;
    private static final int PAGE_HEADER_HEIGHT = 0;

    private static final Dimension PORTRAIT_PAGE_SIZE =
            new Dimension(SHORT_EDGE_LENGTH, LONG_EDGE_LENGTH - PAGE_HEADER_HEIGHT);
    private static final Dimension LANDSCAPE_PAGE_SIZE =
            new Dimension(LONG_EDGE_LENGTH, SHORT_EDGE_LENGTH - PAGE_HEADER_HEIGHT);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy   HH:mm");

    private final DesignSpaceModel designSpaceModel;
    private final JComponent mclnGraphDesignerView;

    private int pageWidth = SHORT_EDGE_LENGTH;
    private int pageHeight = LONG_EDGE_LENGTH;
    private Dimension pageSize = PORTRAIT_PAGE_SIZE;

    private final JLabel projectNameLabel = new JLabel("Project: ", JLabel.LEFT);
    private final JLabel projectNameValue = new JLabel("", JLabel.LEFT);
    private final JLabel modelNameLabel = new JLabel("Model: ", JLabel.LEFT);
    private final JLabel modelNameValue = new JLabel("", JLabel.LEFT);
    private final JLabel dateLabel = new JLabel("", JLabel.RIGHT);

        private static final Color LABEL_COLOR = new Color(0xAA0000);
//    private static final Color LABEL_COLOR = new Color(0x500050);
    private static final Color TEXT_COLOR = new Color(0x0000DD);

    /**
     * @param designSpaceModel
     * @param mclnGraphDesignerView
     */
    public MclnPrintPreviewPanel(DesignSpaceModel designSpaceModel, JComponent mclnGraphDesignerView) {
        super(new BorderLayout());
        this.designSpaceModel = designSpaceModel;
        this.mclnGraphDesignerView = mclnGraphDesignerView;
        setOpaque(true);
        setBackground(Color.WHITE);

        setPreferredSize(pageSize);
        setMinimumSize(pageSize);
        setMaximumSize(pageSize);
        int border = ((3 * 72) / 4) - 6;
        Border marginBorder = new EmptyBorder(border - 10, border, border, border);
        setBorder(marginBorder);
        initContent();
    }

    /**
     * @param orientation
     */
    public void setOrientation(Orientation orientation) {
        switch (orientation) {
            case PORTRAIT:
                pageWidth = SHORT_EDGE_LENGTH;
                pageHeight = LONG_EDGE_LENGTH;
                pageSize = PORTRAIT_PAGE_SIZE;
                break;
            case LANDSCAPE:
                pageWidth = LONG_EDGE_LENGTH;
                pageHeight = SHORT_EDGE_LENGTH;
                pageSize = LANDSCAPE_PAGE_SIZE;
                break;
        }
        setPreferredSize(pageSize);
        setMinimumSize(pageSize);
        setMaximumSize(pageSize);
        initContent();

    }

    /**
     *
     */
    private void initContent() {

        removeAll();

        projectNameLabel.setForeground(LABEL_COLOR);
        projectNameValue.setForeground(TEXT_COLOR);
        projectNameValue.setFont(StandardFonts.FONT_HELVETICA_BOLD_12);

        modelNameLabel.setForeground(LABEL_COLOR);
        modelNameValue.setForeground(TEXT_COLOR);
        modelNameValue.setFont(StandardFonts.FONT_HELVETICA_BOLD_12);

        dateLabel.setForeground(TEXT_COLOR);

        projectNameLabel.setMinimumSize(new Dimension(50, 12));
        projectNameValue.setMaximumSize(new Dimension(360, 12));
        projectNameValue.setMinimumSize(new Dimension(360, 12));

        modelNameLabel.setMinimumSize(new Dimension(50, 12));
        modelNameLabel.setMaximumSize(new Dimension(360, 12));
        modelNameValue.setMinimumSize(new Dimension(360, 12));

        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setOpaque(false);

        headerPanel.add(projectNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        headerPanel.add(projectNameValue, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        headerPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        headerPanel.add(modelNameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        headerPanel.add(modelNameValue, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        headerPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        headerPanel.add(dateLabel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(mclnGraphDesignerView, BorderLayout.CENTER);
        validate();
        repaint();
    }

    /**
     *
     */
    private void populateHeader() {
        String projectName = designSpaceModel.getMclnProject().getProjectName();
        String modelName = designSpaceModel.getMclnProject().getCurrentMclnModel().getModelName();

        StringBuilder projectStringBuilder = new StringBuilder();
        projectStringBuilder.append("<html>").
                append("<font color=\"#0000AA\">").
                append("Project: &nbsp;").
                append("</font>").
                append(projectName).
                append("</html>");

        projectNameValue.setText(projectName);
        modelNameValue.setText(modelName);

        Date printDate = new Date();
        String formattedPrintDate = dateFormat.format(printDate);
        dateLabel.setText(formattedPrintDate);
    }

    @Override
    public Dimension getPreferredSize() {
        return pageSize;
    }

    @Override
    public Dimension getMinimumSize() {
        return pageSize;
    }

    @Override
    public Dimension getMaximumSize() {
        return pageSize;
    }

    @Override
    public void paintComponent(Graphics g) {
        populateHeader();
        super.paintComponent(g);
    }

    /**
     *
     */
    final void printMcLNGraphView() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        // show Print Dialog
        boolean doPrint = printerJob.printDialog();
        if (!doPrint) {
            System.out.println("Canceled");
            return;
        }
        System.out.println("Printing");
        printerJob.setPrintable(printable);
        GeneratingPageWaitingSign generatingPageWaitingSign = GeneratingPageWaitingSign.createInstance();
        generatingPageWaitingSign.showWaitingSign(this);
        SwingUtilities.invokeLater(() -> {
            SwingWorker swingWorker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    printerJob.print();
                    return null;
                }

                @Override
                protected void done() {
                    generatingPageWaitingSign.hideWaitingSign();
                }
            };
            swingWorker.execute();
        });
    }

    /**
     *
     */
    private final Printable printable = (g, pageFormat, pageIndex) -> {

        // Check if there are pages to print
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Dimension pageSize = getSize();
        int restOfThePaperSpace = pageWidth - pageSize.width;
        int leftMargin = restOfThePaperSpace > 1 ? restOfThePaperSpace / 2 : 0;
        pageFormat = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(pageWidth, pageHeight);
        paper.setImageableArea(leftMargin, 0, pageWidth, pageHeight);
        pageFormat.setPaper(paper);

        // Do rendering
        g.translate(leftMargin, 0);
        paint(g);

        // tell the caller that this page is part
        // of the printed document
        return Printable.PAGE_EXISTS;

    };
}
