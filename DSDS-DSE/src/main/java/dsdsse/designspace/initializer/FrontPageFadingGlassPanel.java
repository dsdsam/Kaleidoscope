package dsdsse.designspace.initializer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 6/25/2016.
 */
public class FrontPageFadingGlassPanel extends JPanel   {


    static final FrontPageFadingGlassPanel getInstance(Insets insets){
        return new FrontPageFadingGlassPanel(  insets);
    }

    private final Color PANEL_BACKGROUND = new Color(0xFFFF00);
    Insets insets;

    JLabel noteLabel = new JLabel("ABS");

    private FrontPageFadingGlassPanel(Insets insets){
        super.setLayout(new BorderLayout());
        this.insets = insets;
        setOpaque(false);
//        setBackground(PANEL_BACKGROUND);
add(noteLabel, BorderLayout.CENTER);
    }

    public void setPoint(Point p) {
   //     point = p;
    }


    @Override
    protected void paintComponent(Graphics g) {
        Rectangle rect = getBounds();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(rect.x+insets.left, rect.y+insets.top+30, rect.width-(insets.left+insets.right),
                rect.height-(insets.top+insets.bottom+60));
//            g.setColor(Color.red);
//            g.fillOval(30 - 10, 40 - 10, 20, 20);
    }

//    public void paint(Graphics g){
//       setSize(300,400);
//       super.paint(g);
//    }


}
