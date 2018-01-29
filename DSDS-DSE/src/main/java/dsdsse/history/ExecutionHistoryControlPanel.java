package dsdsse.history;

import adf.app.StandardFonts;
import dsdsse.app.AppController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 2/13/14
 * Time: 8:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionHistoryControlPanel extends JPanel {

    private static final Dimension PREFERRED_SIZE = new Dimension(58, 1);

    //    private static final Dimension HEADER_PREFERRED_SIZE = new Dimension(1, 20);
    private static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;  //new Color(0xEEEEEE);//Color.LIGHT_GRAY;
//    private static final Color BACKGROUND_COLOR = new Color(0xFEFEFE);

//    private final Color TITLE_BACKGROUND = new Color(0x0000FF);
//    private   Color SWITCH_BACKGROUND = new Color(255,153,51);

//                                                             private final Color SWITCH_BACKGROUND = new Color(253, 176, 72);
    //  private final Color SWITCH_BACKGROUND = new Color(109,165,186);

//        private final Color SWITCH_BACKGROUND = new Color(144,186,202);
    //  private final Color SWITCH_BACKGROUND = new Color(125,174,193);

    //   private final Color SWITCH_BACKGROUND = new Color(255,108,156);
    // private final Color SWITCH_BACKGROUND = new Color(98,158,181);
//                                  private   Color SWITCH_BACKGROUND = new Color(88,148,171);
//    private final Color SWITCH_BACKGROUND = new Color(255,127,40);
//    private final Color SWITCH_BACKGROUND = new Color(79,141,166);//new Color(0,102,255);
    private final Color SWITCH_BACKGROUND0 = new Color(0x3333dd);//.MAGENTA;  //new Color(79,141,206);008040
    private final Color SWITCH_BACKGROUND1 = new Color(0x408080);
    private final Color SWITCH_BACKGROUND = new Color(0xFA00FA);
//    private final Color SWITCH_BACKGROUND = new Color(0xEE00EE);

    private final Color SWITCH_FOREGROUND = new Color(0xFFFFFF);

    private final Color GAP_BACKGROUND1 = new Color(0x408080);
    private Color GAP_BACKGROUND = SWITCH_BACKGROUND;// new Color(0xAA00AA);//Color.GRAY;//SWITCH_BACKGROUND;

    private final TraceLogTitlePanel traceLogTitlePanel = new TraceLogTitlePanel("");

    private final JLabel switchLabel = new JLabel("Switch to:", JLabel.CENTER);

    private final JLabel emptyLabel = new JLabel("", JLabel.CENTER);
    private final JLabel emptyLabel2 = new JLabel("", JLabel.CENTER);

    {
        switchLabel.setToolTipText("Switch between \"Selected\" and \"All\" Properties.");
        switchLabel.setOpaque(true);
        switchLabel.setFont(StandardFonts.FONT_HELVETICA_BOLD_10);
        switchLabel.setBackground(SWITCH_BACKGROUND);
        switchLabel.setForeground(SWITCH_FOREGROUND);
        switchLabel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 0, 1, 0, Color.WHITE),
                new EmptyBorder(0, 0, 1, 2)));
        switchLabel.setPreferredSize(new Dimension(56, 19));
        switchLabel.setMinimumSize(new Dimension(56, 19));
        switchLabel.setMaximumSize(new Dimension(56, 19));

//        emptyLabel.setOpaque(true);
//        emptyLabel.setBackground(Color.WHITE);
//        emptyLabel.setPreferredSize(new Dimension(56, 1));
//        emptyLabel.setMinimumSize(new Dimension(56, 1));
//        emptyLabel.setMaximumSize(new Dimension(56, 1));
//
//        emptyLabel2.setOpaque(true);
//        emptyLabel2.setBackground(new Color(253, 176, 72));
////        emptyLabel2.setBackground(new Color(0xCC00CC));
////        emptyLabel2.setBackground(new Color(0xAA00AA));
//        emptyLabel2.setPreferredSize(new Dimension(56, 3));
//        emptyLabel2.setMinimumSize(new Dimension(56, 3));
    }


    private JToggleButton showAllButton;
    private JButton clearButton;

    private final ExecutionHistoryPanel executionHistoryPanel;

    ExecutionHistoryControlPanel(ExecutionHistoryPanel executionHistoryPanel) {
        super(new BorderLayout());
        setBorder(new MatteBorder(1, 0, 0, 0, new Color(0x7A8A99)));
        this.executionHistoryPanel = executionHistoryPanel;

        setBackground(BACKGROUND_COLOR);
        initComponents();
        initLayout();
    }

    private void initComponents() {
        setPreferredSize(PREFERRED_SIZE);
        setMinimumSize(PREFERRED_SIZE);

        // creating State Switch toggle button

        ItemListener itemListener = (e) -> {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                executionHistoryPanel.onSwitchAllSelectedButtonClicked();
            } else {
                executionHistoryPanel.onSwitchToSelectedPropertiesButtonClicked();
            }
        };

        showAllButton = AppController.createToggleIconButton("bluish-button-3.png", "olive-button-3.png", false, true,
                itemListener);
        showAllButton.setOpaque(true);
        showAllButton.setBackground(Color.WHITE);

        // creating Clear button

        ActionListener clearButtonActionListener = (e) -> {
            executionHistoryPanel.clearTraceLog();
        };

        clearButton = AppController.createIconButton("clear-trace-3.png", true, " Clear the Simulation Trace Log. ",
                clearButtonActionListener);
        clearButton.setBorder(null);
        clearButton.setBackground(Color.WHITE);

    }

    private void initLayout() {
        add(traceLogTitlePanel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(0, 1, 0, 0));
        panel.setOpaque(false);
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        panel.add(switchLabel);
        panel.add(Box.createVerticalStrut(2));
        panel.add(showAllButton);
        panel.add(Box.createVerticalStrut(1));
        panel.add(clearButton);
        panel.add(Box.createVerticalStrut(2));
        panel.add(emptyLabel);

        panel.add(Box.createVerticalGlue());
        add(panel, BorderLayout.CENTER);
    }
}
