package adf.ui.components.panels;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Admin on 5/19/2016.
 */
public class DimmingPanel extends JPanel   {

    public interface ChangeConfirmer {
        public void confirmChange(ChangeConfirmer confirmer);
        public void changeConfirmed();
    }

    private static final Logger logger = Logger.getLogger(DimmingPanel.class.getName());

    public static float DIMMING_COEFFICIENT = 0.65f;

    // Create an AlphaComposite with defined translucency.
    private Composite dimmingComp = AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, DIMMING_COEFFICIENT);

    protected JPanel actualPanel;
    private boolean dimmed;

    private MouseInputListener mouseInputListener = new MouseInputListener() {

        public void mouseClicked(MouseEvent e) {
            if (dimmed) {
                e.consume();
            }
        }

        public void mousePressed(MouseEvent e) {
            if (dimmed) {
                e.consume();
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (dimmed) {
                e.consume();
            }
        }

        public void mouseEntered(MouseEvent e) {
            if (dimmed) {
                e.consume();
            }
        }

        public void mouseExited(MouseEvent e) {
            if (dimmed) {
                e.consume();
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (dimmed) {
                e.consume();
            }
        }

        public void mouseMoved(MouseEvent e) {
            if (dimmed) {
                e.consume();
            }
        }
    };

    /**
     *
     */
    public DimmingPanel() {
        super(new BorderLayout());
        this.setOpaque(false);
        this.addMouseListener(mouseInputListener);
        this.addMouseMotionListener(mouseInputListener);
    }

    public void setDimmed(boolean dimmed) {
        logger.log(Level.SEVERE, "Tracing call to Dimmable Panel", new RuntimeException("Tracing call to Dimmable Panel"));
        if ((this.dimmed && !dimmed) || dimmed) {
            enableDisableAll(!dimmed);
        }
        this.dimmed = dimmed;
        repaint();
    }

    public JPanel getActualPanel() {
        return actualPanel;
    }

    public void setActualPanel(JPanel actualPanel) {
        this.actualPanel = actualPanel;
        this.add(actualPanel, BorderLayout.CENTER);
    }

    private void enableDisableAll(final boolean enable) {

        Runnable enabledisable = new Runnable() {
            public void run() {
                enableAllComponents(DimmingPanel.this, enable);
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            enabledisable.run();
        } else {
            SwingUtilities.invokeLater(enabledisable);
        }
    }

    /**
     * @param comp
     * @param status
     */
    private void enableAllComponents(final Component comp, boolean status) {
        comp.setEnabled(status);
        if (comp instanceof Container) {
            Component[] components = ((Container) comp).getComponents();
            for (int i = 0; i < components.length; i++) {
                enableAllComponents(components[i], status);
            }
        }
    }

    /**
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (dimmed) {
            Graphics2D g2d = (Graphics2D) g;
            // Save the original composite.
            Composite oldComp = g2d.getComposite();
            // Set the composite on the Graphics2D object.
            g2d.setComposite(dimmingComp);
            // Invoke paint methods, which will paint
            // with given translucency.
            int w = getWidth();
            int h = getHeight();
            g2d.setColor(Color.lightGray);
            g2d.fillRect(0, 0, w, h);
            // Restore the old composite.
            g2d.setComposite(oldComp);
        }
    }

    /**
     * @param cb
     */
    public void confirmChange(ChangeConfirmer cb) {
        if (dimmed) {
            cb.changeConfirmed();
            return;
        }
        if (!(actualPanel instanceof ChangeConfirmer)) {
            return;
        }
        ChangeConfirmer changeConfirmer = (ChangeConfirmer) actualPanel;
        changeConfirmer.confirmChange(cb);
    }

    /**
     *
     */
    public void changeConfirmed() {
    }
}

