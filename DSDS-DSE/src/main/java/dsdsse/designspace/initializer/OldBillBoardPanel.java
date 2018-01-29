package dsdsse.designspace.initializer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Admin on 6/30/2016.
 */
final class OldBillBoardPanel extends JPanel {

    String introductionHeaderStr = "How to Setup the Model property.";
    String introductionTextStr =
            "This Introduction to the Model Initialization\n" +
                    "shows up only once when a new project is \n" +
                    "created. So, please read it now.\n" +
                    "\n" +
                    "The Model Initialization Assistant is a wizard-like\n" +
                    "tool which helps you to initialize the properties\n" +
                    "of the model's Cells and Arcs. First, you have to\n" +
                    "Initialize Cells and then Arcs.\n" +
                    "\n" +
                    "To initialize a Cell:\n" +
                    "Step 1. Setup the Cell's Allowed States.\n" +
                    "Step 2. Setup the Cell's Initial State.\n" +
                    "Step 3. Setup the Cell's Input Simulating\n" +
                    "        Program. This step is optional and\n" +
                    "        is accomplished only in case the Cell\n" +
                    "        is considered to be the input.\n" +
                    "\n" +
                    "The initialization of an Arc takes one step.\n" +
                    "During this step you setup the arc conductivity.\n" +
                    "\n" +
                    "The Assistant has a separate panel for each step.\n" +
                    "The actions you have to perform are described\n" +
                    "in the text located in the middle of the panel.\n" +
                    "To navigate from step to step, use \"Next\" and \n" +
                    "\"Back\" buttons.\n" +
                    "";


    String step0HeaderStr = "Start initialization.";
    String step0TextStr =
            "\n" +
                    "Please, select a Cell or an Arc you are going\n" +
                    "to initialize by clicking it.";

    String step1of3HeaderStr = "Step 1 of 3:";
    String step1of3Str = new StringBuilder().append("<html>").
            append("<body style=\"text-align:justify\">").
//            append("<div align=\"justify\">").
        append("<font color=\"#780000\" size=\"" + 2 + "\">").
            append("Here you setup the list of states the Cell is<br>").
            append("allowed to take. Select one of the predefined<br>").
            append("sets then edit it, if necessarily, using State<br>").
            append("Chooser. Finally define the states meaning.<br>").
            append("</font>").
//            append("</div></html>").
        append("</body></html>").toString();

//            "Here you setup the list of states the Cell is\n" +
//                    "allowed to take. Select one of the predefined\n" +
//                    "sets then edit it, if necessarily, using State\n" +
//                    "Chooser. Finally define the states meaning.\n";

    String step2of3HeaderStr = "Step 2 of 3:";
    String step2of3Str =
            "Enter the Cell name, then set its Initial State by\n" +
                    "picking it up from the Allowed States List below,\n" +
                    "and check the check-box if the Cell has Input Simu-\n" +
                    "lating Program.";

    String step3of3HeaderStr = "Step 3 of 3:";
    String stepTD3of3Str =
            "Define the Time-Driven Input Simulating Program (ISP).\n" +
                    "Setup the couples: \"Ticks-State\". Use \"Append\", \"Insert\"\n" +
                    "and \"Remove\" buttons to add or delete program steps.\n" +
                    "You can specify that the ISP has phase by checking the\n" +
                    "check-box. For more details about Time-Driven ISP see\n" +
                    "help under Help menu.";

    String stepRD3of3Str =
            "Define the Rule-Driven Input Simulating Program (ISP).\n" +
                    "Setup the triplets: \"Condition-Ticks-State\". Use \"Append\"\n" +
                    "\"Insert\" and \"Remove\" buttons to add or delete program\n" +
                    "steps.  For more details about Rule-Driven ISP see help\n" +
                    "under Help menu.";
    /*
    String finalHeaderStr = "The Cell property setup is done.";
    String finalText =
    "Use Apply button to save the properties,\n"+
    "Okey to save them and close the dialog,\n"+
    "or Cancel the setup. ";
    */
    // Arc setup step prompts
    String arcStep1HeaderStr = "Step 1 of 1:";
    String arcStep1Str =
            "Set the Arc conductivity by selecting it from the\n" +
                    "Allowed States List below, then push \"Apply\"\n" +
                    "button.";

    public static final int NONE = 0;
    public static final int CELL_SETUP = 1;
    public static final int ARC_SETUP = 2;

    private Color bgColor = Color.LIGHT_GRAY;

    private int setupMode = NONE;
    private String cellId;
    private String arcId;
    private boolean hasProgram;

    private JPanel dlgMainPanel;
    private NotePanel notePanel = new NotePanel();

    private boolean showIntroduction;

    OldBillBoardPanel() {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(InitAssistantUIColorScheme.PANEL_BACKGROUND);
//        setBorder(new MatteBorder(0,1,0,1, Color.LIGHT_GRAY));
        setBackground(InitAssistantUIColorScheme.CONTAINER_PANEL_BACKGROUND);
        Border empty = BorderFactory.createEmptyBorder(15, 15, 15, 15);
//        Border empty = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        setBorder(empty);

        initSetupLayout();
    }

    private void initSetupLayout() {
// System.out.println(" initSetupLayout:  init" );
// System.out.println(" initSetupLayout:  init 2" );
        if (dlgMainPanel != null) {
            dlgMainPanel.remove(dlgMainPanel);
        }
        if (showIntroduction) {   //   System.out.println(" Step -2 " );
            dlgMainPanel = initMainIntroductionPanel();
        } else {
            dlgMainPanel = initMainSetupPanel();
        }
        add(dlgMainPanel, BorderLayout.CENTER);

// System.out.println(" initSetupLayout: dialog done");
    }

    private JPanel initMainIntroductionPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                0, 0));
        mainPanel.setLayout(new BorderLayout());
//        JPanel ih = initBlockHeaderPanel(
//                "Introduction to the Initialization Assistant", true );

//        mainPanel.add( ih, BorderLayout.NORTH );
        mainPanel.add(initNotePanel(), BorderLayout.CENTER);

        return (mainPanel);
    }

    private JPanel initMainSetupPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mainPanel.setLayout(new BorderLayout());

//        switch ( setupMode )
//        {
//            case NONE:
        // System.out.println("initEmptyInfoPanel");
//                infoPanel = initEmptyInfoPanel();
//                stateTablePanel = initEmptyTablePanel();
//                break;
//            case CELL_SETUP:
//                //System.out.println("initNodeInfoPanel");
//                if ( showIntroduction )
//                    infoPanel = initEmptyInfoPanel();
//                else
//                    infoPanel = initNodeInfoPanel();
//                stateTablePanel = initTablePanel();
//                break;
//            case ARC_SETUP:
//                if ( showIntroduction )
//                    arcPropertyPanel = initEmptyInfoPanel();
//                else
//                    arcPropertyPanel = initArcInfoPanel();
//                infoPanel = arcPropertyPanel;
//                stateTablePanel = initTablePanel();
//                break;
//            default:
//                infoPanel = null;
//                stateTablePanel = null;
//                break;
//        }

//        mainPanel.add( infoPanel, BorderLayout.NORTH );
        mainPanel.add(initNotePanel(), BorderLayout.CENTER);
// stateTablePanel = initTablePanel();
//        mainPanel.add( stateTablePanel, BorderLayout.SOUTH );

        return mainPanel;
    }

    private JPanel initNotePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        mainPanel.setLayout(new BorderLayout());
        notePanel = new NotePanel();
        mainPanel.add(notePanel);
        return mainPanel;
    }

    private void initContents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                0, 0));
        mainPanel.setLayout(new BorderLayout());

//        switch ( setupMode )
//        {
//            case NONE:
//                // System.out.println("initEmptyInfoPanel");
//                infoPanel = initEmptyInfoPanel();
//                stateTablePanel = initEmptyTablePanel();
//                break;
//            case CELL_SETUP:
//                //System.out.println("initNodeInfoPanel");
//                if ( showIntroduction )
//                    infoPanel = initEmptyInfoPanel();
//                else
//                    infoPanel = initNodeInfoPanel();
//                stateTablePanel = initTablePanel();
//                break;
//            case ARC_SETUP:
//                if ( showIntroduction )
//                    arcPropertyPanel = initEmptyInfoPanel();
//                else
//                    arcPropertyPanel = initArcInfoPanel();
//                infoPanel = arcPropertyPanel;
//                stateTablePanel = initTablePanel();
//                break;
//            default:
//                infoPanel = null;
//                stateTablePanel = null;
//                break;
//        }

//        mainPanel.add( infoPanel, BorderLayout.NORTH );

        JPanel noteHolderPanel = new JPanel();
        noteHolderPanel.setOpaque(false);
        noteHolderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        noteHolderPanel.setLayout(new BorderLayout());
        notePanel = new NotePanel();
        noteHolderPanel.add(notePanel);
        mainPanel.add(noteHolderPanel, BorderLayout.CENTER);
// stateTablePanel = initTablePanel();
//        mainPanel.add( stateTablePanel, BorderLayout.SOUTH );


//        if ( showIntroduction )
//        {
////  System.out.println("showEmptyAssistant Intr "+cellSetupState);
//            notePanel.setIntrodNote( introductionHeaderStr,
//                    introductionTextStr );
//            showIntroduction = false;
//        }else{
//            notePanel.setIntrodNote( step0HeaderStr, step0TextStr );
////    System.out.println("showEmptyAssistant not Intr "+cellSetupState);
//        }
    }

    /**
     * N o t e    P a n e l
     */
    private class NotePanel extends JPanel {
        //    Color hc = new Color( 0xDD0033 );
        Color hc = new Color(0xFFCC00);
        // Color hpc = new Color( 0xFFDA00 );
// Color hpc = new Color( 0xFFAD00 );
        Color hpc = new Color(00, 130, 00);
// Color hpc = new Color( 0x33FF66 );

        JPanel h;
        JPanel n;
        JPanel b;

        JPanel introdHeader;
        JLabel introdLabel;

        JLabel header1;
        JLabel header2;
        JLabel header4;
        JLabel header6;
        JTextArea ta;
        Color bg = new Color(0xFFFFEE);

        public NotePanel() {
// setBorder(BorderFactory.createEmptyBorder( 1, 1, 1, 1 ));
// setPreferredSize(new Dimension(300, 40));
// setBackground( new Color( 0xFFFFEE ) );
// setLayout( new BorderLayout() );
            setOpaque(false);
// setBackground( Color.magenta);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            introdHeader = new JPanel();
            introdHeader.setBackground(hpc);
            introdHeader.setPreferredSize(new Dimension(300, 17));
            introdHeader.setMaximumSize(new Dimension(300, 17));
            introdHeader.setLayout(new BoxLayout(introdHeader, BoxLayout.X_AXIS));
            introdHeader.setBorder(BorderFactory.createEmptyBorder(2, 20, 0, 0));


            introdLabel = new JLabel("", JLabel.LEFT);
//            <html><div align=right width=100px>text</div></html>
            setCompFontSize(introdLabel, Font.BOLD, 11);
            introdLabel.setForeground(hc);
            introdHeader.add(introdLabel);
            introdHeader.add(Box.createHorizontalGlue());
            h = new JPanel();
// h.setOpaque( false );
            h.setBackground(hpc);
            h.setPreferredSize(new Dimension(300, 17));
            h.setMaximumSize(new Dimension(300, 17));
            h.setLayout(new BoxLayout(h, BoxLayout.X_AXIS));
// h.setBorder(BorderFactory.createEmptyBorder( 8, 20, 0, 0 ));
            h.setBorder(BorderFactory.createEmptyBorder(2, 20, 0, 0));

            header1 = new JLabel("", JLabel.LEFT);
            header1.setForeground(Color.white);
            setCompFontSize(header1, Font.BOLD, 11);
            h.add(header1);

            header2 = new JLabel("", JLabel.LEFT);
            header2.setForeground(hc);
            setCompFontSize(header2, Font.BOLD, 11);
            h.add(header2);

// h.add( Box.createRigidArea( new Dimension( 20, 0 ) ));

            JLabel header3 = new JLabel("    |    Step  ", JLabel.LEFT);
            header3.setForeground(Color.white);
            setCompFontSize(header3, Font.BOLD, 11);
            h.add(header3);

            header4 = new JLabel("", JLabel.LEFT);
            header4.setForeground(hc);
            setCompFontSize(header4, Font.BOLD, 11);
            h.add(header4);

            JLabel header5 = new JLabel("  of ", JLabel.LEFT);
            header5.setForeground(Color.white);
            setCompFontSize(header5, Font.BOLD, 11);
            h.add(header5);

            header6 = new JLabel("", JLabel.LEFT);
            header6.setForeground(hc);
            setCompFontSize(header6, Font.BOLD, 11);
            h.add(header6);
/*
 JLabel header7 = new JLabel( " .", JLabel.LEFT );
 header7.setForeground( Color.white );
 setCompFontSize( header7, Font.BOLD, 11 );
 h.add( header7 );
*/
            h.add(Box.createHorizontalGlue());

            n = new JPanel();
            n.setOpaque(false);
// n.setBackground( Color.blue);
            n.setBorder(BorderFactory.createEmptyBorder(0, 6, 2, 6));

            if (showIntroduction) {
                n.setPreferredSize(new Dimension(300, 380));
                n.setMaximumSize(new Dimension(300, 380));
                n.setMinimumSize(new Dimension(300, 380));
                ta = new JTextArea(step1of3Str, 26, 22);
            } else {
//  n.setPreferredSize(new Dimension(300, 52));
//  n.setMaximumSize(new Dimension(300, 52));
                n.setPreferredSize(new Dimension(300, 93));
                n.setMaximumSize(new Dimension(300, 93));
                n.setMinimumSize(new Dimension(300, 93));
                ta = new JTextArea(step1of3Str, 6, 22);
            }

            JLabel lab = new JLabel(step1of3Str);
//            lab.setBorder(BorderFactory.createEmptyBorder(0,16,0,16));

            ta.setForeground(Color.darkGray);
            ta.setForeground(Color.red);
            ta.setForeground(new Color(120, 0, 0));
            ta.setEditable(false);

            // "Step 1 of 3:  Setup the Cell allowed stetes by\n"+
// "selecting binary or multiple state pattern and\n"+
// "editing it if necessary.", 3, 22 );

            //ta.setBackground( new Color( 0xFFFFEE ) );
// ta.setFont( new Font("Courier", 0, 0 ));
            ta.setOpaque(false);
            setCompFontSize(ta, Font.BOLD, 10);
// ta.setLineWrap( true );
            n.add(lab);

            b = new JPanel();
            b.setOpaque(false);
// b.setBackground( Color.red);
            b.setPreferredSize(new Dimension(300, 18));
            b.setMaximumSize(new Dimension(300, 18));
            b.setLayout(new BoxLayout(b, BoxLayout.X_AXIS));
            b.add(Box.createHorizontalGlue());

//            backButton = new JButton(backStr);
//            backButton.setFocusPainted(false);
//            setCompFontSize(backButton, Font.BOLD, 11);
//            backButton.setEnabled(false);
//            backButton.setPreferredSize(new Dimension(70, 18));
//            backButton.setMaximumSize(new Dimension(70, 18));
//            backButton.addActionListener(new ActionListener() {
//                public void executeMenuOperation(ActionEvent e) {
//                    onBack();
//                }
//            });
//            b.add(backButton);

            b.add(Box.createRigidArea(new Dimension(5, 0)));

//            nextButton = new JButton(nextStr);
//            nextButton.setFocusPainted(false);
//            if (setupMode == ARC_SETUP && !showIntroduction)
//                nextButton.setText(applyStr);
//            setCompFontSize(nextButton, Font.BOLD, 11);
//            nextButton.setPreferredSize(new Dimension(70, 18));
//            nextButton.setMaximumSize(new Dimension(70, 18));
//            nextButton.addActionListener(new ActionListener() {
//                public void executeMenuOperation(ActionEvent e) {
//                    onNext();
//                }
//            });
//            b.add(nextButton);
            b.add(Box.createRigidArea(new Dimension(5, 0)));

            add(h, BorderLayout.NORTH);
            add(n, BorderLayout.CENTER);
            add(b, BorderLayout.SOUTH);
// add( h );
// add( n );
// add( b );
        }

        // ------------------------------------------------------
        public void setIntrodNote(String introdHeadLabel, String txtStr) {
            removeAll();
            introdLabel.setText(introdHeadLabel);
            add(introdHeader, BorderLayout.NORTH);
            add(n, BorderLayout.CENTER);
            add(b, BorderLayout.SOUTH);
// add( introdHeader );
// add( n );
// add( b );
            ta.setText(txtStr);
        }

        // ------------------------------------------------------
        public void setNote(int step, String txtStr) {
            removeAll();
            add(h, BorderLayout.NORTH);
            add(n, BorderLayout.CENTER);
            add(b, BorderLayout.SOUTH);

            if (setupMode == CELL_SETUP) {
                header1.setText("Cell  ");
                header2.setText(cellId);
                header4.setText("" + (step));

                if (!hasProgram)
                    header6.setText(" 2");
                else
                    header6.setText(" 3");
            }

            if (setupMode == ARC_SETUP) {
                header1.setText("Arc  ");
                header2.setText(arcId);
                header4.setText("" + (step));

                header6.setText(" 1");
            }

            ta.setText(txtStr);
        }

        // ------------------------------------------------------
        public void paint(Graphics g) {
            Dimension size = getSize();
            int width = size.width - 1;
            int height = size.height - 1;
            if (height < 20)
                return;

            float percent = 0.03f;
            int d = (int) (((float) width) * percent);
// super.paint( g );
            int[] x = {d, width, width, 0, 0};
            int[] y = {0, 0, height, height, d};
            g.setColor(bg);
            g.fillPolygon(x, y, 5);
            super.paint(g);

            int[] x2 = {d, d, 0};
            int[] y2 = {0, d, d};
            g.setColor(bg);
            g.fillPolygon(x2, y2, 3);

            int[] x1 = {0, d, 0};
            int[] y1 = {0, 0, d};
            g.setColor(bgColor);
            g.fillPolygon(x1, y1, 3);

            g.setColor(Color.gray);
            g.drawLine(d, 0, width, 0);
            g.drawLine(width, 0, width, height);
            g.drawLine(0, height, width, height);
            g.drawLine(0, d, 0, height);
            g.drawLine(0, d, d, d);
            g.drawLine(d, d, d, 0);
            g.drawLine(0, d, d, 0);
// ta.paint( g );
 /*
 for (int i=0; i<nStr; i++ )
 {

 }
 */
        }

        private void setCompFontSize(Component comp, int style,
                                     int fontSize) {
            Font font = comp.getFont();
            comp.setFont(new Font(font.getName(), style, fontSize));
        }
    } // NotePanel
}
