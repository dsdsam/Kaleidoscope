package adf.ui.components.panels;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 31, 2013
 * Time: 9:30:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class OneLineMessagePanel extends JPanel {
    public static final int ALARM = 0;
    public static final int WORNING = 1;
    public static final int INFO = 2;
    public static final int ERASE = 3;

    private static final Color BACKGROUND_COLOR = new Color(0xFF, 0xFF, 0xF0);

    private static final OneLineMessagePanel oneLineMessagePanel = new OneLineMessagePanel();

    public static OneLineMessagePanel getInstance() {
        return oneLineMessagePanel;
    }

    //
    //   I n s t a n c e
    //

    private final JTextField oneLineMessage;

    private OneLineMessagePanel() {
        super();
        setLayout(new BorderLayout());
// Border border = BorderFactory.createEtchedBorder();
        Border border = BorderFactory.createLoweredBevelBorder();
        setBorder(border);

        oneLineMessage = new JTextField(" ");
        oneLineMessage.setEditable(false);
        oneLineMessage.setBackground(BACKGROUND_COLOR);
        int fontSize = 10;
        Font font;
        font = oneLineMessage.getFont();
        oneLineMessage.setFont(new Font(font.getName(), font.BOLD, 12));
        oneLineMessage.setForeground(Color.black);
        this.add("Center", oneLineMessage);
    }
// ===============  M e t h o d s  =============================

    public void showMessage(int type, String message) {
        oneLineMessage.setText("  " + message);
        switch (type) {
            case ALARM:
                oneLineMessage.setBackground(Color.red);
                oneLineMessage.setForeground(Color.white);
                break;
            case WORNING:
//    oneLineMessage.setBackground( new Color( 255, 255, 170  ) );
                //   oneLineMessage.setBackground( new Color( 0xFF, 0xFF, 0xF0  ) );
                oneLineMessage.setBackground(new Color(50, 150, 50));
                oneLineMessage.setBackground(Color.lightGray);
                oneLineMessage.setForeground(Color.white);
                oneLineMessage.setForeground(new Color(50, 50, 50));
                break;
            case INFO:
                oneLineMessage.setBackground(BACKGROUND_COLOR);
                oneLineMessage.setForeground(Color.black);
                break;
            default:
                oneLineMessage.setBackground(BACKGROUND_COLOR);
                oneLineMessage.setForeground(BACKGROUND_COLOR);
                oneLineMessage.setText("");
        }

//        Graphics g = super.getGraphics();
//        oneLineMessage.paint(g);

        oneLineMessage.repaint();
    }

    /**
     *
     */
    public void clearMessage() {
        oneLineMessage.setText("");
        oneLineMessage.repaint();
    }
}
