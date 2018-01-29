package adf.ui.components.panels;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Event;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.awt.*;
import java.util.*;
import java.awt.FontMetrics;

import javax.swing.*;

/****************************************************************/
//                C L O C K   P A N E L

/**
 * ************************************************************
 */
public class DigitalClockPanel extends JPanel implements Runnable {
    final byte[][] digToSeg = {{1, 1, 1, 1, 1, 1, 0} //   1  2  3  4  5  6  7
            , {0, 0, 1, 1, 0, 0, 0} // 0
            , {0, 1, 1, 0, 1, 1, 1} // 1
            , {0, 1, 1, 1, 1, 0, 1} // 2
            , {1, 0, 1, 1, 0, 0, 1} // 3
            , {1, 1, 0, 1, 1, 0, 1} // 4
            , {1, 1, 0, 1, 1, 1, 1} // 5
            , {0, 1, 1, 1, 0, 0, 0} // 6
            , {1, 1, 1, 1, 1, 1, 1} // 7
            , {1, 1, 1, 1, 1, 0, 1} // 8
            , // 9
    };
    // Date date = new Date();
    Color segHColor = Color.green;
    Color segLColor = new Color(50, 50, 50);
    Thread timer;
    Date date;
    int ticks = 0;
    boolean secTrigger = true;
    int h1, h2, m1, m2;
    int lastMinute = -1;
    Rectangle clockRect;
    Rectangle bgRect;

    int width = getSize().width;
    int height = getSize().height;
    int mPos = height / 2;
    int digWidth = (width / 9) * 2;
    int dw;
    int segW = 2;
    int segH = 2;
    int segW2 = 1;
    int segH2 = 1;
    int gap = 1;

    public DigitalClockPanel() {
        setBackground(Color.lightGray);
        setForeground(Color.green);

        date = new Date();
        timer = new Thread(this);
        timer.start();

    }

    public void reshape(int x, int y, int width, int height) {
//        System.out.println("DigitalClockPanel.reshape");
        super.reshape(x, y, width, height);
        updateState();
    }

    //------------------------------------------------------------------------
    public void updateState() {
        int x0 = 10;
        int y0 = 10;

        Dimension size = getSize();
        /*
         if (oldSize == null)
           oldSize = size;
         else
          if ((oldSize.width != size.width) || (oldSize.height != size.height))
          {
            painted = false;
            lastMinute = -1;
          }
         */
        /*
         clockOutline = new Rectangle( 0, 0, size.width,
         (int)((float)size.height * 0.5) );
         clockRect = new Rectangle( 0, 0, (int)(size.width*0.8),
                                          (int)((size.height * 0.5)*0.8) );
         clockRect.grow( -x0, -y0 );
         clockRect.move( x0, y0 );
         */
        bgRect = new Rectangle(0, 0, size.width, size.height);
        bgRect.grow(-x0 / 2, -y0 / 2);
        bgRect.move(x0 / 2, y0 / 2);

//        int dx = (int) (bgRect.width * 0.15);
//        int dy = (int) (bgRect.height * 0.325);

        int dx = (int) (bgRect.width * 0.25);
        int dy = (int) (bgRect.height * 0.325);

// clockRect = new Rectangle( 0, 0, size.width, (int)(size.height * 1.) );
// clockRect.grow( -dx, -dy );
// clockRect.move( dx, dy );
        int clockWidth = 85;
        float fwidth = clockWidth;
        digWidth = (int) ((fwidth * 0.9 / 4.0) + 0.5);
        dw = (int) ((float) digWidth * 0.35 + 0.5);
        dx = (bgRect.width - clockWidth) / 2;
        clockRect = new Rectangle(0, 0, clockWidth, size.height);
        clockRect.grow(0, -dy);
        clockRect.move(dx + 4, dy);

        width = size.width;
        height = size.height;
// width = clockRect.width;
// height =  clockRect.height;
        mPos = clockRect.y + clockRect.height / 2;

    }

    //------------------------------------------------------------------------
    public void run() {
        Thread me = Thread.currentThread();
        while (timer == me) {
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
            }

            updateState();
//   Dimension size = getSize();
//   Rectangle rect = this.getBounds();
//
//               System.out.println("Clock Run"+"  "+rect.x+"  "+rect.y
//                                             +"  "+rect.width
//                                             +"  "+rect.height
//                                             +"  "+mPos);
//               System.out.println("Clock Run"+"  "+clockRect.x+"  "+clockRect.y
//                                             +"  "+clockRect.width
//                                             +"  "+clockRect.height
//                                             +"  "+mPos);

            ticks++;
//            updateMinutes( null );

            repaint();
        }
    }

    private void drawDigit(Graphics g, int pos, int digit) {
        int x0 = clockRect.x;
        int x1 = clockRect.x + clockRect.width;
        int y0 = clockRect.y;
        int y1 = clockRect.y + clockRect.height;

        if (pos == 0) {
            x0 = clockRect.x;
            x1 = x0 + digWidth - dw;
        }
        if (pos == 1) {
            x0 = clockRect.x + digWidth;
            x1 = x0 + digWidth - dw;
        }
        if (pos == 2) {
            x1 = clockRect.x + clockRect.width - digWidth;
            x0 = x1 - digWidth + dw;
        }
        if (pos == 3) {
            x1 = clockRect.x + clockRect.width;
            x0 = x1 - digWidth + dw;
        }

        int[] uhSegX = {x0 + gap, x1 - gap, x1 - gap - segW, x0 + gap + segW};
        int[] uhSegY = {y0, y0, y0 + segH, y0 + segH};

        int[] lhSegX = {x0 + gap + segW, x1 - gap - segW, x1 - gap, x0 + gap};
        int[] lhSegY = {y1 - segH, y1 - segH, y1, y1};

        int[] umhSegX = {x0 + gap + 1, x0 + gap + segW - gap, x1 - gap - segW + gap, x1 - gap - 1};
        int[] umhSegY = {mPos, mPos - segH2, mPos - segH2, mPos};

        int[] lmhSegX = {x0 + gap + 1, x0 + gap + segW - gap, x1 - gap - segW + gap, x1 - gap - 1};
        int[] lmhSegY = {mPos, mPos + segH2, mPos + segH2, mPos};

        int[] ulvSegX = {x0, x0 + segW, x0 + segW, x0};
        int[] ulvSegY = {y0 + gap, y0 + gap + segH, mPos - gap - segH + 1, mPos - gap + gap};

        int[] llvSegX = {x0, x0 + segW, x0 + segW, x0};
        int[] llvSegY = {mPos + gap - gap, mPos + gap + segH - 1, y1 - gap - segH, y1 - gap};

        int[] urvSegX = {x1 - segW, x1, x1, x1 - segW};
        int[] urvSegY = {y0 + gap + segH, y0 + gap, mPos - gap + gap, mPos - gap - segH + 1};
// int[] urvSegY = { 10, 5, midle, midle-5 };

        int[] lrvSegX = {x1 - segW, x1, x1, x1 - segW};
        int[] lrvSegY = {mPos + gap + segH - 1, mPos + gap - gap, y1 - gap, y1 - gap - segH};
// int[] lrvSegY = { midle+5, midle, height-5, height-10  };

        // seg 1
        if (digToSeg[digit][0] == 1)
            g.setColor(segHColor);
        else
            g.setColor(segLColor);
        g.fillPolygon(ulvSegX, ulvSegY, 4);

        // seg 2
        if (digToSeg[digit][1] == 1)
            g.setColor(segHColor);
        else
            g.setColor(segLColor);
        g.fillPolygon(uhSegX, uhSegY, 4);

        // seg 3
        if (digToSeg[digit][2] == 1)
            g.setColor(segHColor);
        else
            g.setColor(segLColor);
        g.fillPolygon(urvSegX, urvSegY, 4);

        // seg 4
        if (digToSeg[digit][3] == 1)
            g.setColor(segHColor);
        else
            g.setColor(segLColor);
        g.fillPolygon(lrvSegX, lrvSegY, 4);

        // seg 5
        if (digToSeg[digit][4] == 1)
            g.setColor(segHColor);
        else
            g.setColor(segLColor);
        g.fillPolygon(lhSegX, lhSegY, 4);

        // seg 6
        if (digToSeg[digit][5] == 1)
            g.setColor(segHColor);
        else
            g.setColor(segLColor);
        g.fillPolygon(llvSegX, llvSegY, 4);

        // seg 7
        if (digToSeg[digit][6] == 1)
            g.setColor(segHColor);
        else
            g.setColor(segLColor);

// g.fillPolygon( uhSegX,  uhSegY,  4 );
        g.fillPolygon(umhSegX, umhSegY, 4);
        g.fillPolygon(lmhSegX, lmhSegY, 4);
    }

    // ------------------------------------------------------------------------
    public void drawSeconds(Graphics g) {
        int secWidth = 2;
        int offCenter = (int)((clockRect.height / 4.0 ) +0.5);
        int x = clockRect.x + (clockRect.width / 2) - secWidth;
//        int y = clockRect.y + clockRect.height / 2;
        int y = (getSize().height / 2) - 1;

        if (secTrigger) {
            g.setColor(segHColor);
            secTrigger = false;
        } else {
            g.setColor(segLColor);
            secTrigger = true;
        }
//        g.fillRect( x, y - offCenter, secWidth * 2, secWidth * 2 );
//        g.fillRect( x, y + offCenter, secWidth * 2, secWidth * 2 );

        g.fillRect(x, y - offCenter, 3, 3);
        g.fillRect(x, y + offCenter, 3, 3);
    }

    // ------------------------------------------------------------------------
    public void paint(Graphics g) {

        super.paint(g);
//        updateState();
// g.drawRect( 2, 2, width - 5, height - 5 );

        g.setColor(Color.lightGray);
        g.drawLine(0, 0, 0, height);
        g.drawLine(0, 0, width, 0);
        g.setColor(Color.white);
        g.drawLine(1, 1, 1, height - 1);
        g.drawLine(1, 1, width - 1, 1);

        g.setColor(Color.darkGray);
        g.drawLine(width, 0, width, height);
        g.drawLine(0, height, width, height);
        g.drawLine(width - 1, 1, width - 1, height - 1);
        g.drawLine(1, height - 1, width - 1, height - 1);

        g.setColor(Color.black);
        g.fillRect(bgRect.x, bgRect.y, bgRect.width, bgRect.height);

        g.setColor(Color.darkGray);
        g.drawLine(bgRect.x, bgRect.x, bgRect.x, bgRect.y + bgRect.height);
        g.drawLine(bgRect.x, bgRect.x, bgRect.x + bgRect.width, bgRect.x);

        g.setColor(Color.white);
        g.drawLine(bgRect.x + bgRect.width, bgRect.x,
                bgRect.x + bgRect.width, bgRect.y + bgRect.height);
        g.drawLine(bgRect.x, bgRect.y + bgRect.height,
                bgRect.x + bgRect.width, bgRect.y + bgRect.height);

        lastMinute = -1;
        updateMinutes(g);
    }

    // --------------------------------------------------------------------------
    void updateSeconds() {
        Graphics g = getGraphics();
        drawSeconds(g);
        //updateMinutes();
    }

    // --------------------------------------------------------------------------
    void updateMinutes(Graphics gg) {
        Graphics g;
        if (gg != null)
            g = gg;
        else
            g = getGraphics();

        Date date = new Date();
        /*
         g.drawString(""+date.getHours()+"  "+date.getMinutes()+"  "+
                         date.getSeconds(), 5,  getSize().height - 10 );
         */
        h1 = date.getHours() / 10;
        h2 = date.getHours() - h1 * 10;
        m1 = date.getMinutes() / 10;
        m2 = date.getMinutes() - m1 * 10;

//        System.out.println("  "+date.getHours()+" H "+h1+" h2 "+h2);

        if (m2 != lastMinute) {
            drawDigit(g, 0, h1);
            drawDigit(g, 1, h2);
            drawDigit(g, 2, m1);
            drawDigit(g, 3, m2);
            lastMinute = m2;
        }

        drawSeconds(g);
    }

} // end of DigitalClockPanel


