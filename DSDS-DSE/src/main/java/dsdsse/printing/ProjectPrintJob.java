package dsdsse.printing;

import dsdsse.designspace.DesignSpaceView;

/**
 * Created by Admin on 9/12/2016.
 */
public class ProjectPrintJob {

    private static boolean currentlyPrinting;

    /**
     * @return true if job started
     */
    public static synchronized boolean printProject(DesignSpaceView designSpaceView) {
        if (currentlyPrinting) {
            return false;
        }

        try {
            ProjectPrintJob projectPrintJob = new ProjectPrintJob(designSpaceView);
            projectPrintJob.startPrinting();
        } finally {
            return currentlyPrinting;
        }
    }

    private final DesignSpaceView designSpaceView;


    private ProjectPrintJob(DesignSpaceView designSpaceView) {
        this.designSpaceView = designSpaceView;
    }

    private boolean startPrinting() {
//        try {
//            currentlyPrinting = true;
//            System.out.println("Start Printing");
//            PrinterJob printerJob = PrinterJob.getPrinterJob();
//
//            // show Print Dialog
////            PageFormat pf = printerJob.pageDialog(printerJob.defaultPage());
//            boolean doPrint = printerJob.printDialog();
//            if (!doPrint) {
//                System.out.println("Canceled");
//                return false;
//            }
//            System.out.println("Printing");
//            designSpaceView.switchToPrintPreviewContent(printerJob);
//            System.out.println("returned");
//        } catch (PrinterException e) {
//            System.out.println("Printing was not successful");
//            return false;
//        } finally {
//            currentlyPrinting = false;
//        }
        return true;
    }
}
