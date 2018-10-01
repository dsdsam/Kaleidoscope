package adf.flyout;

import adf.app.AdfEnv;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Created on 11/9/2016.
 * <p>
 * The Fly Out Message Manager is designed to present messages at the lower right
 * corner of the window. This means a new instance of Fly Out Message Manager should
 * be created for each window where the messages are supposed to show up.
 * For instance, each of the following windows: Fxall Main Frame, POMS Import Edit
 * window, and POMS New Requirement window should have its own instance of the Manager.
 * <p>
 * The Manager maintains inner Message Queue where messages can be accumulated. All
 * accumulated messages are shown one by one. The message exposure times on the screen
 * and the delay time that should elapse between a moment when one message is hidden
 * and next one is supposed to be presented are currently hardcoded in this class.
 */
final class FlyOutMessageManager {

    private static final Logger logger = Logger.getLogger(FlyOutMessageManager.class.getName());

    private enum Status {
        HIDDEN,
        SHOWN,
        SHOWING,
        HIDING,
        PAUSE
    }

    private static final int MESSAGE_EXPOSURE_TIME = 5000;
    private static final int MESSAGES_BREAK_TIME = 300;

    private static final int GAP = 5;
    private static final int STEP = 5;
    private static final int TICK = 3;

    //
    //   I n s t a n c e
    //

    private FlyOutMessageManager flyOutMessageManager;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Semaphore semaphore = new Semaphore(1);
    private volatile boolean queueBlocked;

    private FlyOutMessageHolderPanel flyOutMessageHolderPanel;

    private final JComponent parentComponent;
    private JComponent disposableParentComponent;
    private final JLayeredPane layeredPane;

    private Status status = Status.HIDDEN;

    private Timer animatingTimer;
    private Timer unblockingQueueTimer = new Timer(5000, (e) -> {
        queueBlocked = false;
    });

    final void showAlertMessage(String message) {
        showAlertMessage(null, message);
    }

    /**
     * @param disposableParentComponent
     * @param message
     */
    final void showAlertMessage(JComponent disposableParentComponent, String message) {
        flyOutMessageManager.disposableParentComponent = disposableParentComponent;
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Blocked put message into Message Queue Interrupted.", e);
        }
    }

    /**
     *
     */
    private final Runnable messagePresenter = () -> {
        while (true) {
            try {
                semaphore.acquire();
                String message = messageQueue.take();

                // waiting when showing message from queue is unblocked
                while (queueBlocked) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                SwingUtilities.invokeAndWait(() -> {
                    flyOutMessageHolderPanel.setMessage(message);
                    showPanel();
                });
            } catch (Throwable e) {
                logger.log(Level.SEVERE, "Message processing failed with Exception: " + e);
            }
        }
    };

    /**
     * Blocks showing messages from queue and
     * hides shown message, if any
     */
    private final ComponentAdapter componentAdapter = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            blockQueue();
            resetToHiddenStatus();
        }
    };

    /**
     *
     */
    private final ActionListener timerActionListener = (e) -> {
        timerTickProcessor();
    };

    //   C r e a t i n g   F l y   O u t   M e s s a g e   M a n a g e r

    FlyOutMessageManager(JFrame mainFrame) {
        this(null, mainFrame.getLayeredPane());
    }

    FlyOutMessageManager(JComponent parentComponent, JLayeredPane layeredPane) {
        flyOutMessageManager = this;
        this.parentComponent = parentComponent;
        this.layeredPane = layeredPane;
        flyOutMessageHolderPanel = new FlyOutMessageHolderPanel(RoundedRectangle.ROUNDING_RADIUS_MEDIUM,
                RoundedRectangle.ROUND_LEFT_SIDE);

        flyOutMessageHolderPanel.setVisible(false);

        animatingTimer = new Timer(TICK, timerActionListener);

        layeredPane.addComponentListener(componentAdapter);
        layeredPane.add(flyOutMessageHolderPanel, new Integer(500));

        executor.execute(messagePresenter);
        unblockingQueueTimer.setRepeats(false);
    }

    /**
     *
     */
    private void timerTickProcessor() {

        if (status == Status.HIDING) {
            Rectangle frameBounds = layeredPane.getBounds();
            Rectangle currentBounds = flyOutMessageHolderPanel.getBounds();
            int newX = currentBounds.x + STEP;
            flyOutMessageHolderPanel.setBounds(newX, currentBounds.y, currentBounds.width, currentBounds.height);
            int visibleWidth = frameBounds.width - currentBounds.x;
            if (visibleWidth <= 0) {
                animatingTimer.stop();
                animatingTimer.setRepeats(false);
                status = Status.PAUSE;
                flyOutMessageHolderPanel.setVisible(false);
                animatingTimer.setInitialDelay(MESSAGES_BREAK_TIME);
                animatingTimer.start();
            }
        } else if (status == Status.PAUSE) {

            animatingTimer.stop();
            status = Status.HIDDEN;
            animatingTimer.setRepeats(true);
            animatingTimer.setInitialDelay(TICK);
            animatingTimer.setDelay(TICK);
            semaphore.release();
        } else if (status == Status.SHOWING) {

            Rectangle frameBounds = layeredPane.getBounds();
            Rectangle currentBounds = flyOutMessageHolderPanel.getBounds();

            int visibleWidth = frameBounds.width - currentBounds.x;
            int dx = currentBounds.width - visibleWidth;
            dx = dx > STEP ? STEP : dx;
            int newX = currentBounds.x - dx;
            flyOutMessageHolderPanel.setBounds(newX, currentBounds.y, currentBounds.width, currentBounds.height);
            if (visibleWidth + STEP >= currentBounds.width) {
                animatingTimer.stop();
                animatingTimer.setRepeats(false);
                status = Status.SHOWN;
                animatingTimer.setInitialDelay(MESSAGE_EXPOSURE_TIME);
                animatingTimer.start();
            }
        } else if (status == Status.SHOWN) {

            animatingTimer.stop();
            animatingTimer.setRepeats(true);
            animatingTimer.setInitialDelay(TICK);
            animatingTimer.setDelay(TICK);
            hidePanel();
        }
    }

    /**
     *
     */
    boolean resetToHiddenStatus() {
        if (status == Status.HIDDEN) {
            return false;
        }
        animatingTimer.stop();
        animatingTimer.setRepeats(true);
        animatingTimer.setInitialDelay(TICK);
        animatingTimer.setDelay(TICK);
        status = Status.HIDDEN;
        flyOutMessageHolderPanel.setVisible(false);
        Rectangle layeredPaneBounds = layeredPane.getBounds();
        Dimension flyOutAlertPanelSize = flyOutMessageHolderPanel.getSize();

        int height = (parentComponent != null) ? parentComponent.getHeight() : layeredPane.getHeight();
        flyOutMessageHolderPanel.setBounds(layeredPaneBounds.width, height - flyOutAlertPanelSize.height - GAP,
                flyOutAlertPanelSize.width, flyOutAlertPanelSize.height);

        if (semaphore.availablePermits() == 0) {
            semaphore.release();
        }
        return true;
    }

    private void blockQueue() {
        queueBlocked = true;
        if (unblockingQueueTimer.isRunning()) {
            unblockingQueueTimer.restart();
        } else {
            unblockingQueueTimer.start();
        }
    }

    /**
     *
     */
    private void showPanel() {
        if (status != Status.HIDDEN) {
            return;
        }
        Rectangle layeredPaneBounds = layeredPane.getBounds();
        Dimension flyOutAlertPanelSize = flyOutMessageHolderPanel.getSize();

        int height = (parentComponent != null) ? parentComponent.getHeight() : layeredPane.getHeight();
        flyOutMessageHolderPanel.setBounds(layeredPaneBounds.width, height - flyOutAlertPanelSize.height - GAP,
                flyOutAlertPanelSize.width, flyOutAlertPanelSize.height);
        status = Status.SHOWING;
        flyOutMessageHolderPanel.setVisible(true);
        animatingTimer.start();
    }

    /**
     *
     */
    private void hidePanel() {
        if (status != Status.SHOWN) {
            return;
        }
        status = Status.HIDING;
        animatingTimer.start();
    }

    //
    //   T e s t i n g
    //

    private static int messageID;

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Testing Fly Out Alert Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500, 200));

        //Display the Frame.
        frame.pack();
        frame.setVisible(true);
        AdfEnv.putMainFrame(frame);

        MainFrameFlyingMessageManager.showFlyOutAlertMessage("Testing Fly Out Alert.");

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String messageKey = String.format(" %04d", ++messageID);
                MainFrameFlyingMessageManager.showFlyOutAlertMessage("Testing Fly Out Alert.");
            }
        };
        frame.addMouseListener(mouseListener);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
}


