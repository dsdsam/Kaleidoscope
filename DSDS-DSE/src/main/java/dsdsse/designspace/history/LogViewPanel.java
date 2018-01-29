package dsdsse.designspace.history;

import adf.app.StandardFonts;
import mcln.palette.MclnState;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/5/14
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogViewPanel extends JPanel {

//    private static final Color LIGHT_GRAY_BACKGROUND = new Color(0xFDFDFD);
//    private static final Color LIGHT_BLUE_BACKGROUND = new Color(0xDCEBFF);

    //    private static final Color LIGHT_GRAY_BACKGROUND = new Color(0xFAFAFA);
//    private static final Color LIGHT_BLUE_BACKGROUND = new Color(0xDEEDFF);
    //    private static final Color DARK_BLUE_BACKGROUND = new Color(0xD0E1FF);
//    private static final Color DARK_BACKGROUND = new Color(0xAAAAFF);
    private static final Color LIGHT_BACKGROUND = Color.WHITE;
    private static final Color DARK_BACKGROUND = new Color(0xD6EAFF);

//    private static final Color LIGHT_YELLOW_BACKGROUND = new Color(0xFFFFF8);
//    private static final Color DARK_YELLOW_BACKGROUND = new Color(0xFFFACD);
//    setBackground(new Color(0xFFFACD));
//        setBackground(new Color(0xFFFCE4));
//        setBackground(new Color(0xFFFFF0));

//    146 208, 80 gr     92D050
//
//
//
//    255 228 104 ye   FFE468
//
//
//
//    141 180 226 bl 8DB4E2
//
//
//
//    252 213 180 body  FCD5B4
//
//
//
//    98  240 80    bright 62F050
//
//
//
//    192 192 192  C0C0C0
//
//    0 176 80 gr 00B050 grass
//
//    183 219 255  B7DBFF   light blue     B9DFFF      D0E1FF    D7E6FF  DCEBFF  DEEDFF  E1F0FF  E6F5FF

    private final int ROW_HEIGHT = 16;
    private final int ROW_HALF_HEIGHT = 9;
    private final int TEXT_PADDING = 4;

    private final dsdsse.designspace.history.LogPanelModel logPanelModel;
    private final int logSize;
    private boolean firstLineLight;

    LogViewPanel(dsdsse.designspace.history.LogPanelModel logPanelModel) {
        this.logPanelModel = logPanelModel;
        logSize = logPanelModel.getLogSize();
        setBackground(Color.YELLOW);


//        public static final Font FONT  = new Font("DialogInput", Font.PLAIN, 14);
//
//        Java SE defines the following five logical font families:
//
//        Dialog
//                DialogInput
//        Monospaced
//                Serif
//        SansSerif
    }

    public LogEntry getLastLogEntry() {
        return logPanelModel.getLastLogEntry();
    }

    public void addLogElement(LogEntry logEntry) {
        logPanelModel.add(logEntry);
        firstLineLight ^= true;
        Graphics g = getGraphics();
        paint(g);
//        repaint();
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int y = ROW_HEIGHT;

        boolean highlightedRow = firstLineLight;
        int logEntryIndex = 0;
        while (y <= height + ROW_HEIGHT) {
            Color backgroundColor;
            if (highlightedRow) {
                backgroundColor = DARK_BACKGROUND;
            } else {
                backgroundColor = LIGHT_BACKGROUND;
            }
            g.setColor(backgroundColor);
            g.fillRect(0, y - ROW_HEIGHT, width, y);

            if (logEntryIndex < logSize) {
                LogEntry logEntry = logPanelModel.getLogEntryAt(logEntryIndex);
                if (logEntry != null && logEntry.isInitialized()) {
                    if (logEntry.isShownFirstTime()) {
                        g.setFont(StandardFonts.FONT_HELVETICA_BOLD_11);
                    } else {
                        g.setFont(StandardFonts.FONT_HELVETICA_PLAIN_11);
                    }
                    String time = logEntry.getTime();
                    g.setColor(Color.BLACK);
                    g.drawString(time, 5, y - TEXT_PADDING);

                    String uid = logEntry.getUid();
                    g.setColor(Color.BLACK);
                    g.drawString(uid, 55, y - TEXT_PADDING);

                    MclnState mclnState = logEntry.getMclnState();
                    int color = mclnState.getRGB();
                    Color fillColor = new Color(color);
                    drawKnob(g, 119, y - ROW_HALF_HEIGHT, Color.BLACK, fillColor);

                    String statementText = logEntry.getStatementText();
                    g.setColor(Color.BLACK);
                    g.setFont(StandardFonts.FONT_DIALOG_INPUT_PLAIN_14);
                    g.drawString(statementText, 131, y - TEXT_PADDING);
//                    g.drawString(mclnState.toString(), 125, y - TEXT_PADDING);

                    logEntry.resetFirstTimeFlag();
                }
            }
            logEntryIndex++;
            highlightedRow ^= true;
            y += ROW_HEIGHT;
        }
    }

    private void drawKnob(Graphics g, int x, int y, Color drawColor, Color fillColor) {
        g.setColor(drawColor);
        g.drawLine(x - 1, y - 3, x + 1, y - 3);
        g.drawLine(x - 2, y - 2, x + 2, y - 2);
        g.drawLine(x - 3, y - 1, x + 3, y - 1);
        g.drawLine(x - 3, y, x + 3, y);
        g.drawLine(x - 3, y + 1, x + 3, y + 1);
        g.drawLine(x - 2, y + 2, x + 2, y + 2);
        g.drawLine(x - 1, y + 3, x + 1, y + 3);
        drawKnot(g, x, y, fillColor);
    }

    private void drawKnot(Graphics g, int x, int y, Color fillColor) {
        g.setColor(fillColor);
        g.drawLine(x - 1, y - 2, x + 1, y - 2);
        g.drawLine(x - 2, y - 1, x + 2, y - 1);
        g.drawLine(x - 2, y, x + 2, y);
        g.drawLine(x - 2, y + 1, x + 2, y + 1);
        g.drawLine(x - 1, y + 2, x + 1, y + 2);
    }
//    private JScrollPane scrollPane;
//    private JScrollBar scrollBar;
//    private JTextArea textArea;
//
//    LogViewPanel() {
//        initLogViewPanel();
//    }
//
//    private void initLogViewPanel() {
//        setBackground(Color.WHITE);
//        setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.gray));
////    setBorder( null );
//        setLayout(new BorderLayout());
//
//        scrollPane = new JScrollPane();
//        scrollPane.setBorder(null);
//        scrollPane.setBorder(BorderFactory.createMatteBorder(3, 0, 3, 0, Color.lightGray));
//        scrollBar = scrollPane.getVerticalScrollBar();
////    scrollPane.setBorder( BorderFactory.createMatteBorder( 3,3,3,3, Color.lightGray ));
//
////    JPanel textPanel = new JPanel();
//        textArea = new JTextArea("Please use Menu for to load an example\n");
//        textArea.append("and execute the automaton. \n\n\n");
//        textArea.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createMatteBorder(0, 3, 0, 3, Color.lightGray),
//                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
//        textArea.setEditable(false);
//        textArea.setFocusable(false);
////    textArea.setBackground(new Color(88,128,149));
//        textArea.setBackground(Color.black);
////    textArea.setBackground(new Color(25,49,64));
//        textArea.setForeground(Color.white);
//        scrollPane.getViewport().add(textArea);
//        add(scrollPane, BorderLayout.CENTER);
//    }
//
//    public void updateRepresentation() {
//        scrollBar.setValue(scrollBar.getMaximum() + 10000);
////    String newData = new String(arrayList.toArray());
//        System.out.println("updateRepresentation: " + scrollBar.getValue());
//        System.out.println("updateRepresentation: " + scrollBar.getMaximum());
////    textArea.append( newData );
//    }
//
//    public void clearView() {
//        textArea.setText("");
//    }
}
