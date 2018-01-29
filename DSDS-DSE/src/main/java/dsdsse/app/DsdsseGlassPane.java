package dsdsse.app;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Mar 6, 2013
 * Time: 9:19:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DsdsseGlassPane extends JComponent implements ItemListener {

    private Point point;

    public DsdsseGlassPane(JPanel panel) {
        CBListener listener = new CBListener(panel, this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

      //React to change button clicks.
    public void itemStateChanged(ItemEvent e) {
        setVisible(e.getStateChange() == ItemEvent.SELECTED);
    }

    protected void paintComponent(Graphics g) {
        if (point != null) {
            g.setColor(Color.red);
            g.fillOval(point.x - 10, point.y - 10, 20, 20);
        }
    }

    public void setPoint(Point p) {
        point = p;
    }

    /**
     * Listen for all events that our check box is likely to be
     * interested in.  Redispatch them to the check box.
     */
    class CBListener extends MouseAdapter {
        Toolkit toolkit;
//        Component liveButton;
//        JMenuBar menuBar;
        DsdsseGlassPane glassPane;
        JPanel panel;
        Rectangle rect = new Rectangle();

        public CBListener(JPanel panel, DsdsseGlassPane glassPane) {
            toolkit = Toolkit.getDefaultToolkit();
//            this.liveButton = liveButton;
//            this.menuBar = menuBar;
            this.glassPane = glassPane;
            this.panel = panel;
        }

        public void mouseMoved(MouseEvent e) {
            redispatchMouseEvent(e, false);
        }

        public void mouseClicked(MouseEvent e) {
            redispatchMouseEvent(e, false);
        }

        public void mouseEntered(MouseEvent e) {
            redispatchMouseEvent(e, false);
        }

        public void mouseExited(MouseEvent e) {
            redispatchMouseEvent(e, false);
        }

        public void mousePressed(MouseEvent e) {
            redispatchMouseEvent(e, false);
        }

        public void mouseReleased(MouseEvent e) {
            redispatchMouseEvent(e, true);
        }

         public void mouseDragged(MouseEvent e){
             Point glassPanePoint = e.getPoint();
              Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, panel);
            Dimension size = panel.getSize();
              rect = panel.getBounds(rect);
            if(!rect.contains(containerPoint)){
//                System.out.println("NOT incide main panel");
                return;
            } else{
//                 System.out.println(" incide main panel");
            }
              glassPane.setPoint(glassPanePoint);
                glassPane.repaint();
             e.consume();
         }

        //A basic implementation of redispatching events.
        private void redispatchMouseEvent(MouseEvent e,
                                          boolean repaint) {
            Point glassPanePoint = e.getPoint();
            Container container = panel;
            Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, panel);
            Dimension size = panel.getSize();
              rect = panel.getBounds(rect);
            if(!rect.contains(containerPoint)){
//                System.out.println("NOT incide main panel");
                return;
            } else{
//                 System.out.println(" incide main panel");
            }
//            if(containerPoint.y <0 || containerPoint.x <0 || containerPoint.x > containerPoint.y > size.height ){
//
//            }
//            if (containerPoint.y < 0) { //we're not in the content pane
//                if (containerPoint.y + menuBar.getHeight() >= 0) {
//                    //The mouse event is over the menu bar.
//                    //Could handle specially.
//                } else {
//                    //The mouse event is over non-system window
//                    //decorations, such as the ones provided by
//                    //the Java look and feel.
//                    //Could handle specially.
//                }
//            } else {
                //The mouse event is probably over the content pane.
                //Find out exactly which component it's over.
//                Component component =
//                        SwingUtilities.getDeepestComponentAt(
//                                container,
//                                containerPoint.x,
//                                containerPoint.y);

//                if ((component != null) && (component.equals(liveButton))) {
//                    //Forward events over the check box.
//                    Point componentPoint = SwingUtilities.convertPoint(
//                            glassPane,
//                            glassPanePoint,
//                            component);
//                    component.dispatchEvent(new MouseEvent(component,
//                            e.getID(),
//                            e.getWhen(),
//                            e.getModifiers(),
//                            componentPoint.x,
//                            componentPoint.y,
//                            e.getClickCount(),
//                            e.isPopupTrigger()));
//                }
//            }

            //Update the glass pane if requested.
            if (repaint) {
                glassPane.setPoint(glassPanePoint);
                glassPane.repaint();
            }
        }
    }

}
