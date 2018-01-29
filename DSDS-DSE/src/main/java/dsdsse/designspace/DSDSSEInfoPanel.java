package dsdsse.designspace;

import dsdsse.app.DSDSSEConstants;
import dsdsse.app.DsdsseEnvironment;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Feb 3, 2013
 * Time: 9:41:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class DSDSSEInfoPanel extends JPanel implements DSDSSEConstants {
   private final int height = 27;
    int textBase = 13;
    //
// Color textPadColor = Color.blue;
// Color textPadColor = new Color( 0x887766 );
    private final Color TEXT_PAD_COLOR = new Color(0x4455FF);
     private final Color TEXT_COLOR = new Color(0xFFFFFF);
// Color textPadColor = new Color( 0x0000FF );
    //olor textPadColor = new Color( 100, 100, 255 );
// Color textPadColor = new Color( 80, 100, 255 );
// Color textPadColor = new Color( 00, 130, 00 );
    //  textPadColor = Color.blue;
// Color statePadColor = new Color( 255, 255, 170 );
// Color statePadColor = new Color( 0xD0D0D0 );
    Color statePadColor = new Color(0xdddddd);
// Color messagePadColor = new Color( 255, 100, 80 );
    Color messagePadColor = statePadColor;

    private String ticksStr = "  Ticks:  ";

// Color bgColor = new Color( 80, 100, 255 );
    Color bgColor = messagePadColor;
    Color fgColor = Color.yellow;
    Color fieldTextColor = Color.white;
    Color messageColor = Color.darkGray;
    DsdsseEnvironment mclnMedia;
    int whatToDo;
    int operationStep;
    boolean canRestoreLocation;

    String strMode;
    String operation;
    String step;
    String message;

// ========================================================

    /* Conctructor */
    public DSDSSEInfoPanel(DsdsseEnvironment mclnMedia) {
        super(new FlowLayout(FlowLayout.LEFT));
        this.mclnMedia = mclnMedia;

        setMinimumSize(new Dimension(1,height));
// this.mclnMedia.infoPanel = this;

        setBackground(bgColor);
        setForeground(fgColor);
        setPreferredSize(new Dimension(0, height));
// g.setFont( new Font( font.getSubject(), font.BOLD, font.getNKnots()) );
        Font font = new Font("Dialog", Font.PLAIN, 11);

        JLabel modeLabel = new JLabel("Current Mode:"){
            public void paintComponent(Graphics g){
            }
            public void paint(Graphics g){
                super.paint(g);
                Font font = g.getFont();
                g.setFont(new Font(font.getName(), font.BOLD, 12));
                  g.setColor(fieldTextColor);
                g.drawString("Current Mode:", 5, 15);
            }
        };
        modeLabel.setMinimumSize(new Dimension(0, height));
        modeLabel.setOpaque(true);
//        modeLabel.setFont(font);
        modeLabel.setForeground(TEXT_COLOR);
        modeLabel.setBackground(TEXT_PAD_COLOR);

        JLabel modeValue = new JLabel("Editing");
        modeValue.setBackground(TEXT_PAD_COLOR);
        add(modeLabel);
        add(modeValue);

//        setBorder(new MatteBorder(3,3,3,3, Color.BLACK));
    }
// ----------------------------------

    private void setEditModePrompts()
// String strMode, String operation,
//                                 String step )
    {
//System.out.println("Prompt  "+whatToDo+"   "+operationStep);
        strMode = "Not set.";
        operation = "Not set.";
        step = "Not set.";

        strMode = "Editing.";
        if (whatToDo == SET_STATE) {
            operation = "Set state.";
            step = "Pick up a ball or an arc knob to set state.";
        }
        if (whatToDo == CREATE_NODES) {
            operation = "Create Balls.";
            step = "Pick up a place to locate a ball or a ball to set state.";
        }

        if (whatToDo == CREATE_FRAGMENT) {
            operation = "Create Fragment.";
            if (operationStep == TAKE_FIRST_NODE)
                step = "Pick up input ball.";
            if (operationStep == TAKE_SECOND_NODE)
                step = "Pick up output ball.  RMB:  to unselect first one.";
            if (operationStep == TAKE_TRAN_PLACE)
                step = "Pick up a place to locate a situation.   RMB:  to unselect balls.";
        }

        if ((whatToDo == CREATE_V_TRAN) ||
                (whatToDo == CREATE_H_TRAN) ||
                (whatToDo == CREATE_RS_TRAN) ||
                (whatToDo == CREATE_LS_TRAN)) {
            if (whatToDo == CREATE_V_TRAN)
                operation = "Create Vertical situation.";
            if (whatToDo == CREATE_H_TRAN)
                operation = "Create Horizontal situation.";
            if (whatToDo == CREATE_RS_TRAN)
                operation = "Create Right Slanted situation.";
            if (whatToDo == CREATE_LS_TRAN)
                operation = "Create Left Slanted situation.";

            if (operationStep == TAKE_TRAN_PLACE)
                step = "Pick up a place to locate a situation.";
        }

        if (whatToDo == CREATE_ARCS) {
            operation = "Create Arcs.";
            if (operationStep == TAKE_FIRST_NODE)
                step = "Pick up input ball.";

            if (operationStep == TAKE_SECOND_NODE)
                step =
                        "Pick up output ball or knot place.  RMB:  to unselect first one.";

            if (operationStep == TAKE_ARC_THIRD_POINT)
                step =
                        "Pick up an arc knob location.   RMB:  to undo.";

            if (operationStep == TAKE_ARC_KNOT_OR_NODE)
                step =
                        "Pick up next knot location or output ball to finish.   RMB:  to undo.";

            if (operationStep == TAKE_ARC_KNOB)
                step =
                        "Pick up the knot where the arc knob whould be located.   RMB:  to undo.";
        }

/*
  if (operationStep == TAKE_ARC_PLACE)
    step = "Pick arc spline knot place.   RMB:  to finish.";
  if (operationStep == TAKE_ARC_POINT)
    step = "Pick arc point.";
*/
        if (whatToDo == MOVE_ELEMENT) {
            operation = "Move Element.";
            if (operationStep == TAKE_ELEMENT) {
                step = "Pick up:  a ball, a situation or an arc knot.";
                if (canRestoreLocation)
                    step = step + "   RMB:  to Undo last move.";
                else
                    step = step + "   RMB:  to quit Move operation.";
            }
            if (operationStep == TAKE_ELEMENT_NEW_PLACE)
                step = "Drag it to new place and then release the button.";
        }

        if (whatToDo == MOVE_MODEL) {
            operation = "Move Model.";
            if (operationStep == TAKE_MODEL) {
                step = "Click the model at any place.";
                if (canRestoreLocation)
                    step = step + "   RMB:  to Undo last move.";
            }
            if (operationStep == TAKE_MODEL_NEW_PLACE)
                step = "Drag it to new place and then release the button.";
        }

        if (whatToDo == DELETE_ELEMENT) {
            operation = "Delete Element.";
            step = "";
            if (operationStep == TAKE_ELEMENT)
                step = "Pick up:  a ball, a situation or an arc knot.";
            step = step + "   RMB:  to quit Delete operation.";
            if (operationStep == REMOVE_ELEMENT)
                step =
                        "LMB:   to remove selected element,   RMB:   to unselect.";
        }
    }
// ----------------------------------

//    private void drawPane(Graphics g) {
//        strMode = "Not set.";
//        operation = "Not set.";
//        step = "Not set.";
//        Dimension size = getNKnots();
//
///*
// int dx = g.getFontMetrics().getMaxAdvance();
// int dLine = (int)( g.getFontMetrics().getHeight() +
//                    g.getFontMetrics().getHeight() * 0.0 );
// g.drawString( objName, x0+
//   g.getFontMetrics().stringWidth( objTitle ), y0 );
//*/
//        // setFont
//        Font font = g.getFont();
//// g.setFont( new Font( font.getSubject(), font.BOLD, font.getNKnots()) );
//        g.setFont(new Font(font.getSubject(), font.BOLD, 11));
//
//        // Show Message insted of operation step
//        if (message != null) {
//            String mes = "     " + message + "          ";
//            g.setColor(messagePadColor);
//            g.fillRect(1, 1, size.width, size.height - 1);
//            g.setColor(fieldTextColor);
//            g.drawString(mes, 0, textBase);
//            message = null;
//            return;
//        }
//
////        if (AppStateModel.getCurrentMode() == AppStateModel.Mode.EDITING) {
////            strMode = "Simulation";
//
////            if (mclnMedia.execMode == DONT_EXEC)
////                strMode = "Simulation Model Type is not set.";
////            else {
////                if (mclnMedia.execMode == DDLS_EXEC)
////                    strMode = "Discrete Dynamic Linear System";
////                if (mclnMedia.execMode == DDRS_EXEC)
////                    strMode = "Discrete Dynamic Random System";
////                if (mclnMedia.execMode == DEDS_EXEC)
////                    strMode = "Discrete Event Dynamic System";
////
////                strMode += " Simulating.";
////            }
////
////            if (mclnMedia.runningActivated)
////                operation = "Auto Exec.";
////            else
////                operation = "  By Step. ";
//
////  step = "  "+mclnMedia.getTicks();
////  if (!mclnMedia.calcTrans)
//            step += ".5";
//        }
//
////        if (mclnMedia.getCurrentMode() == mclnMedia.EDIT_MODE)
////            setEditModePrompts();
//
////        int gap = 2;
////        int t1x = 1;
////        int t1w = g.getFontMetrics().stringWidth("  Current Mode:  ");
////        g.setColor(TEXT_PAD_COLOR);
////        g.fillRect(t1x, 1, t1w, 16);
////        g.setColor(fieldTextColor);
////        g.drawString("Current Mode:", t1x + 5, textBase);
////
////        int t2x = t1x + t1w + gap;
////        int t2w = g.getFontMetrics().stringWidth("  " + strMode + "  ");
////        g.setColor(statePadColor);
////        g.fillRect(t2x, 0, t2w, 20);
////        g.setColor(messageColor);
////        g.drawString(strMode, t2x + 5, textBase);
////
////        int t3x = t2x + t2w + 2;
////        int t3w = g.getFontMetrics().stringWidth("  Operation:  ");
////        g.setColor(TEXT_PAD_COLOR);
////        g.fillRect(t3x, 1, t3w, 16);
////        g.setColor(fieldTextColor);
////        g.drawString("Operation:", t3x + 5, textBase);
////
////        int t4x = t3x + t3w + 2;
////        int t4w;
//////        if (mclnMedia.getCurrentMode() == mclnMedia.EDIT_MODE)
//////            t4w = g.getFontMetrics().stringWidth("  " + operation + "  ");
//////        else
//////            t4w = g.getFontMetrics().stringWidth("a") * 11;
////        t4w = 14;
////        g.setColor(statePadColor);
////        g.fillRect(t4x, 0, t4w, 20);
////        g.setColor(messageColor);
////        g.drawString(operation, t4x + 5, textBase);
////
////        // Show Message insted of operation step
////        if (message != null) {
////            int t5x = t4x + t4w + 2;
////            int t5w;
////            if (mclnMedia.getCurrentMode() == mclnMedia.EDIT_MODE)
////                t5w = g.getFontMetrics().stringWidth("  Step:  ");
////            else
////                t5w = g.getFontMetrics().stringWidth("  Ticks:  ");
////            g.setColor(TEXT_PAD_COLOR);
////            g.fillRect(t5x, 1, t5w, 16);
////            g.setColor(fieldTextColor);
////            if (mclnMedia.getCurrentMode() == mclnMedia.EDIT_MODE)
////                g.drawString("Step:", t5x + 5, textBase);
////            else
////                g.drawString("Ticks:", t5x + 5, textBase);
////
////            String mes = "     " + message + "          ";
////            int t6x = t5x + t5w + 2;
////            int t6w = g.getFontMetrics().stringWidth(mes);
////            g.setColor(messagePadColor);
////            g.fillRect(t6x, 0, t6w, 20);
////            g.setColor(fieldTextColor);
////            g.drawString(mes, t6x + 5, textBase);
////            message = null;
////            return;
//        }
//
//        int t5x = t4x + t4w + 2;
//        int t5w;
////        if (mclnMedia.getCurrentMode() == mclnMedia.EDIT_MODE)
////            t5w = g.getFontMetrics().stringWidth("  Step:  ");
////        else
////            t5w = g.getFontMetrics().stringWidth("  Ticks:  ");
//        t5w = 67;
//        g.setColor(TEXT_PAD_COLOR);
//        g.fillRect(t5x, 1, t5w, 16);
//        g.setColor(fieldTextColor);
////        if (mclnMedia.getCurrentMode() == mclnMedia.EDIT_MODE)
////            g.drawString("Step:", t5x + 5, textBase);
////        else
////            g.drawString("Ticks:", t5x + 5, textBase);
//
//        int t6x = t5x + t5w + 2;
//        int t6w = g.getFontMetrics().stringWidth("  " + step + "  ");
//        g.setColor(statePadColor);
//        g.fillRect(t6x, 0, t6w, 20);
//        g.setColor(messageColor);
//        g.drawString(step, t6x + 5, textBase);
//    }
// ---------------------------------------------------------

    public void paint(Graphics g) {
        super.paint(g);
        String name = g.getFont().getFontName();
         System.out.println(g.getFont().getName());
        System.out.println(name);
         System.out.println(g.getFont().getFamily());
// if ( mclnMedia.mclnGraphModel != null ){
//  mclnMedia.mclnGraphModel.setCuttentMediaState();
// }
// whatToDo = AppEnv.getMCLNetPanel().getCurrentOperation();
// operationStep = AppEnv.getMCLNetPanel().getCurrentOperStep();
// canRestoreLocation = AppEnv.getMCLNetPanel().getCanRestorLocation();
//        drawPane(g);
    }
// -------------------------------------------------------------

    public void showMessage(String message) {
        this.message = message;
// this.repaint();

// Graphics g = super.getGraphics();
// g.setColor(bgColor);
// Dimension size = getNKnots();
// g.fillRect( 0, 0, size.width, size.height );
// drawPane( g );
// this.repaint();
/*
 g.setColor(bgColor);
 Dimension size = getNKnots();
   g.fillRect( t5x, 0, t5w, 20 );
 super.paint(g);
 drawPane( g, message );
*/
    }
}
