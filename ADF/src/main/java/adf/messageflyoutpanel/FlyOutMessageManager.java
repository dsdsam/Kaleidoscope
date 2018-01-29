package adf.messageflyoutpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by u0180093 on 11/9/2016.
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

    private enum Status {HIDDEN, SHOWN, SHOWING, HIDING, PAUSE}

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

    private FlyOutHolderPanel flyOutHolderPanel;

    private final JComponent parentComponent;
    private JComponent disposableParentComponent;
    private final JLayeredPane layeredPane;

    private Status status = Status.HIDDEN;

    private Timer animatingTimer;
    private Timer unblockingQueueTimer = new Timer(5000, (e) -> {
        logger.severe("\n QUEUE - UN UN UN - BLOCKED");
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
//            messageQueue.put("00001");
//            messageQueue.put("00002");
//            messageQueue.put("00003");
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
                    flyOutHolderPanel.setMessage(message);
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

    FlyOutMessageManager(JLayeredPane layeredPane) {
        this(null, layeredPane);
    }

    FlyOutMessageManager(JComponent parentComponent, JLayeredPane layeredPane) {
        flyOutMessageManager = this;
        this.parentComponent = parentComponent;
        this.layeredPane = layeredPane;
        this.flyOutHolderPanel = new FlyOutHolderPanel(RoundedRectangle.ROUND_LEFT_SIDE);

        this.flyOutHolderPanel.setVisible(false);

        animatingTimer = new Timer(TICK, timerActionListener);

        layeredPane.addComponentListener(componentAdapter);
        layeredPane.add(this.flyOutHolderPanel, new Integer(500));

        executor.execute(messagePresenter);
        unblockingQueueTimer.setRepeats(false);
    }

    /**
     *
     */
    private void timerTickProcessor() {

        if (status == Status.HIDING) {
            Rectangle frameBounds = layeredPane.getBounds();
            Rectangle currentBounds = flyOutHolderPanel.getBounds();
            int newX = currentBounds.x + STEP;
            flyOutHolderPanel.setBounds(newX, currentBounds.y, currentBounds.width, currentBounds.height);
            int visibleWidth = frameBounds.width - currentBounds.x;
            if (visibleWidth <= 0) {
                FlyOutMessageManager.this.animatingTimer.stop();
                FlyOutMessageManager.this.animatingTimer.setRepeats(false);
                status = Status.PAUSE;
                flyOutHolderPanel.setVisible(false);
                FlyOutMessageManager.this.animatingTimer.setInitialDelay(MESSAGES_BREAK_TIME);
                FlyOutMessageManager.this.animatingTimer.start();
            }

        } else if (status == Status.PAUSE) {

            FlyOutMessageManager.this.animatingTimer.stop();
            status = Status.HIDDEN;
            FlyOutMessageManager.this.animatingTimer.setRepeats(true);
            FlyOutMessageManager.this.animatingTimer.setInitialDelay(TICK);
            FlyOutMessageManager.this.animatingTimer.setDelay(TICK);
            semaphore.release();

        } else if (status == Status.SHOWING) {

            Rectangle frameBounds = layeredPane.getBounds();
            Rectangle currentBounds = flyOutHolderPanel.getBounds();

            int visibleWidth = frameBounds.width - currentBounds.x;
            int dx = currentBounds.width - visibleWidth;
            dx = dx > STEP ? STEP : dx;
            int newX = currentBounds.x - dx;
            flyOutHolderPanel.setBounds(newX, currentBounds.y, currentBounds.width, currentBounds.height);
            if (visibleWidth + STEP >= currentBounds.width) {
                FlyOutMessageManager.this.animatingTimer.stop();
                FlyOutMessageManager.this.animatingTimer.setRepeats(false);
                status = Status.SHOWN;
                FlyOutMessageManager.this.animatingTimer.setInitialDelay(MESSAGE_EXPOSURE_TIME);
                FlyOutMessageManager.this.animatingTimer.start();
            }

        } else if (status == Status.SHOWN) {

            FlyOutMessageManager.this.animatingTimer.stop();
            FlyOutMessageManager.this.animatingTimer.setRepeats(true);
            FlyOutMessageManager.this.animatingTimer.setInitialDelay(TICK);
            FlyOutMessageManager.this.animatingTimer.setDelay(TICK);
            hidePanel();
        }
    }

    /**
     *
     */
    void resetToHiddenStatus() {
        if (status == Status.HIDDEN) {
            return;
        }
        animatingTimer.stop();
        FlyOutMessageManager.this.animatingTimer.setRepeats(true);
        FlyOutMessageManager.this.animatingTimer.setInitialDelay(TICK);
        FlyOutMessageManager.this.animatingTimer.setDelay(TICK);
        status = Status.HIDDEN;
        flyOutHolderPanel.setVisible(false);
        Rectangle layeredPaneBounds = layeredPane.getBounds();
        Dimension flyOutAlertPanelSize = flyOutHolderPanel.getSize();

        int height = (parentComponent != null) ? parentComponent.getHeight() : layeredPane.getHeight();
        flyOutHolderPanel.setBounds(layeredPaneBounds.width,
                height - flyOutAlertPanelSize.height - GAP,
                flyOutAlertPanelSize.width, flyOutAlertPanelSize.height);

        if (semaphore.availablePermits() == 0) {
            semaphore.release();
        }
    }


    private void blockQueue() {
        logger.severe("\n QUEUE BLOCKED");
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
        Dimension flyOutAlertPanelSize = flyOutHolderPanel.getSize();

        int height = (parentComponent != null) ? parentComponent.getHeight() : layeredPane.getHeight();
        flyOutHolderPanel.setBounds(layeredPaneBounds.width,
                height - flyOutAlertPanelSize.height - GAP,
                flyOutAlertPanelSize.width, flyOutAlertPanelSize.height);
        status = Status.SHOWING;
        flyOutHolderPanel.setVisible(true);
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

        JPanel mainPanel = new JPanel();
        frame.add(mainPanel);

        frame.validate();
        JLayeredPane layeredPane = frame.getLayeredPane();

        layeredPane.setBorder(null);
        FlyOutMessageManager flyOutAlertManager = new FlyOutMessageManager(layeredPane);

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String messageKey = String.format(" %04d", ++messageID);
                flyOutAlertManager.showAlertMessage(null, messageKey);
            }
        };
        layeredPane.addMouseListener(mouseListener);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
}


