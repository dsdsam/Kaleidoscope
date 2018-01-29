/*
 * Created on Aug 21, 2005
 *
 */
package sem.appui.controls;

import sem.mission.explorer.model.ModelOperation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * @author xpadmin
 */
public class DirectionControlPanel extends JPanel {

    public final static int POWER_ON = 1 << 0;
    public final static int MANUAL_CONTROL = 1 << 1;
    public final static int ENABLED = POWER_ON | MANUAL_CONTROL;

    private int state = MANUAL_CONTROL;

    private Color bgColor = Color.BLACK;
    private Color linesColor = Color.black;

    private Color sec1AColor = new Color(0x0000CC);
    private Color sec2AColor = new Color(0xCC0000);

    private Color sec1DColor = new Color(0x000055);
    private Color sec2DColor = new Color(0x550000);

    private Color sec1OffColor = Color.darkGray;
    private Color sec2OffColor = Color.gray;

    private Color sec1Color = sec1OffColor;
    private Color sec2Color = sec2OffColor;

    private int size = 100;
    private int drawingX = 1;
    private int drawingY = 1;
    private int drawingWidth;
    private int drawingHeight;
    private double scale = 5;

    private ComponentAdapter componentListenerAdapter = new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            setdrawingSize();
        }

        public void componentShown(ComponentEvent e) {
            setdrawingSize();
        }

        private void setdrawingSize() {
            Dimension size = DirectionControlPanel.this.getSize();
            drawingWidth = size.width - 3;
            drawingHeight = size.height - 3;
        }
    };


    private MouseAdapter mousePickProcessor = new MouseAdapter() {

        public void mousePressed(MouseEvent e) {
            if (state != ENABLED)
                return;

            int x = e.getX();
            int y = e.getY();

            Dimension d = getSize();
            double cx = d.width / 2;
            double cy = d.height / 2;
            double dx = x - cx;
            double dy = -(y - cy);
            double th = Math.atan2(dy, dx);
            if (th < 0)
                th = 2 * Math.PI + th;


            double ang = th * (180 / Math.PI);
            double eps = 5;
            if (Math.abs(ang - 45) < eps) {
                ang = 45;
            } else if (Math.abs(ang - 90) < eps) {
                ang = 90;
            } else if (Math.abs(ang - 135) < eps) {
                ang = 135;
            } else if (Math.abs(ang - 180) < eps) {
                ang = 180;
            } else if (Math.abs(ang - 225) < eps) {
                ang = 225;
            } else if (Math.abs(ang - 270) < eps) {
                ang = 270;
            } else if (Math.abs(ang - 315) < eps) {
                ang = 315;
            } else if (Math.abs(0 - ang) < eps || Math.abs(360 - ang) < eps) {
                ang = 0;
            }
//         ang = Math.toDegrees( th );
            double speed = Math.sqrt(dx * dx + dy * dy);
            double maxSpeed = cx;
//         System.out.println("mousePressed Max "+cx+"  "+cy +"  "+maxSpeed);
//         System.out.println("mousePressed Max "+dx+"  "+dy +"  "+speed);
            double nSpeeds = 5;
            if (speed > maxSpeed)
                speed = maxSpeed;
            speed = (int) ((speed / maxSpeed) * nSpeeds + 0.5);
//         System.out.println("mousePressed "+th+"  "+ang +"  "+speed);

//         if (seModel != null)
            {
                ModelOperation operation;

                operation = new ModelOperation("Stop", "DCP", -1, "ZZZ", ang);

//          SEOperationListener ol = seModel.getSEOperationListener();
                operation = new ModelOperation("setTargetAngle", "DCP", -1, "ZZZ", ang);
//          ol.execOperation(operation);

                // operation = new RtsModelOperation("Forward", "user", -1, "ZZZ", ang );
                // seViewOl.execOperation(operation);
            }
        }
    };

    /**
     * 
     *
     */
    public DirectionControlPanel() {
        setSize(new Dimension(size, size));
        setPreferredSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setLayout(null);
        setBackground(bgColor);
        addMouseListener(mousePickProcessor);
        this.addComponentListener(componentListenerAdapter);
//        setBorder( BorderFactory.createEmptyBorder(14,14,14,14));
    }

    /**
     * 
     */
    public void setPowerOn(boolean powerOn) {
        if (powerOn) {
            state |= POWER_ON;
        } else {
            state &= ~POWER_ON;
        }
        setColors();
    }

    /**
     * 
     */
    public void setManualControl(boolean manualControl) {
        if (manualControl) {
            state |= MANUAL_CONTROL;
        } else {
            state &= ~MANUAL_CONTROL;
        }
        setColors();
    }

    /**
     * 
     *
     */
    private void setColors() {
        if ((state & POWER_ON) != 0) {
            if (state == ENABLED) {
                sec1Color = sec1AColor;
                sec2Color = sec2AColor;
            } else {
                sec1Color = sec1DColor;
                sec2Color = sec2DColor;
            }
        } else {
            sec1Color = sec1OffColor;
            sec2Color = sec2OffColor;
        }
        Thread.dumpStack();
        repaint();
    }

    /**
     * 
     */
    public void paint(Graphics g) {

//        setBackground( bgColor );

        super.paint(g);
        g.setColor(linesColor);

        int angStep = 30;
        int n = 360 / angStep;
        int startAngle = -15;

        g.setColor(sec2Color);
        g.fillOval(drawingX, drawingY, drawingWidth, drawingHeight);
        for (int i = 0; i < n; i++) {
//      System.out.println(""+i+"  "+n);
            if ((i % 2) == 0) {
//   System.out.println("sec1Color");
                g.setColor(sec1Color);
                g.fillArc(drawingX, drawingY, drawingWidth, drawingHeight, startAngle, angStep);
            } else {
//      System.out.println("sec2Color");
                g.setColor(sec2Color);
            }

            startAngle += angStep;
            g.setColor(Color.GRAY);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawOval(drawingX, drawingY, drawingWidth, drawingHeight);
        }
    }
}


    
    


   
