package adf.flyout;

import adf.app.AdfEnv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 2/15/2017.
 */
final class FlyDownMessageManager {

    private static final Logger logger = Logger.getLogger(FlyDownMessageManager.class.getName());

    private enum Status {HIDDEN, SHOWN, SHOWING, HIDING, PAUSE}

    private static final int MESSAGE_EXPOSURE_TIME = 5000;
    private static final int MESSAGES_BREAK_TIME = 300;

    private static final int STEP = 2;
    private static final int TICK = 3;

    private static final int MESSAGE_X_LOCATION = 100;

    //
    //   I n s t a n c e
    //

    private final BlockingQueue<FlyingMessage> messageQueue = new LinkedBlockingQueue();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Semaphore semaphore = new Semaphore(1);
    private volatile boolean queueBlocked;

    private FlyDownMessageHolderPanel flyDownMessageHolderPanel;

    private final Container parentComponent;
    private final JLayeredPane layeredPane;
    private int messageWidth;

    private Status status = Status.HIDDEN;

    private Timer animatingTimer;
    private Timer unblockingQueueTimer = new Timer(5000, (e) -> {
        queueBlocked = false;
    });

    /**
     * @param messageText
     */
    final void showAlertMessage(String messageText) {
        try {
            FlyingMessage flyingMessage = FlyingMessage.createInstance(FlyingMessage.ALERT, messageText);
            messageQueue.put(flyingMessage);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Blocked put message into Message Queue Interrupted.", e);
        }
    }

    final void showWarningMessage(String messageText) {
        try {
            FlyingMessage flyingMessage = FlyingMessage.createInstance(FlyingMessage.WARNING, messageText);
            messageQueue.put(flyingMessage);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Blocked put message into Message Queue Interrupted.", e);
        }
    }

    /**
     * @param messageText
     */
    final void showInfoMessage(String messageText) {
        try {
            FlyingMessage flyingMessage = FlyingMessage.createInstance(FlyingMessage.INFO, messageText);
            messageQueue.put(flyingMessage);
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
                FlyingMessage flyingMessage = messageQueue.take();

                // waiting when showing message from queue is unblocked
                while (queueBlocked) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                SwingUtilities.invokeAndWait(() -> {
                    flyDownMessageHolderPanel.setMessage(flyingMessage);
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

    /**
     * @param mainFrame
     */
    FlyDownMessageManager(JFrame mainFrame) {
        parentComponent = mainFrame;
        layeredPane = mainFrame.getLayeredPane();
        flyDownMessageHolderPanel = new FlyDownMessageHolderPanel(RoundedRectangle.ROUNDING_RADIUS_QUITE_LARGE,
                RoundedRectangle.ROUND_BOTTOM_SIDE);

        flyDownMessageHolderPanel.setVisible(false);

        animatingTimer = new Timer(TICK, timerActionListener);

        layeredPane.addComponentListener(componentAdapter);
        layeredPane.add(flyDownMessageHolderPanel, new Integer(500));

        executor.execute(messagePresenter);
        unblockingQueueTimer.setRepeats(false);
    }

    /**
     *
     */
    private void timerTickProcessor() {

        if (status == Status.HIDING) {
            Rectangle frameBounds = layeredPane.getBounds();
            Rectangle messageBounds = flyDownMessageHolderPanel.getBounds();
            int newY = messageBounds.y - STEP;
            flyDownMessageHolderPanel.setBounds(messageBounds.x, newY, messageBounds.width, messageBounds.height);
            parentComponent.repaint();
            layeredPane.repaint();
            int currentHeight = messageBounds.height + newY;
            if (currentHeight <= 0) {
                animatingTimer.stop();
                animatingTimer.setRepeats(false);
                status = Status.PAUSE;
                flyDownMessageHolderPanel.setVisible(false);
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

            Rectangle parentPanelBounds = layeredPane.getBounds();
            Rectangle messageBounds = flyDownMessageHolderPanel.getBounds();
            messageBounds.width = parentPanelBounds.width - MESSAGE_X_LOCATION * 2;
            messageWidth = messageBounds.width;

            int visibleHeight = messageBounds.y;
            int dy = messageBounds.width - visibleHeight;
            dy = dy > STEP ? STEP : dy;
            int newY = messageBounds.y + dy;
            flyDownMessageHolderPanel.setBounds(MESSAGE_X_LOCATION, newY, messageBounds.width, messageBounds.height);
            if (visibleHeight + STEP >= 0) {
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
        flyDownMessageHolderPanel.setVisible(false);
        Dimension flyDownPanelSize = flyDownMessageHolderPanel.getSize();

        flyDownMessageHolderPanel.setBounds(MESSAGE_X_LOCATION, -flyDownPanelSize.height, messageWidth,
                flyDownPanelSize.height);

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
        Rectangle parentPanelBounds = layeredPane.getBounds();
        messageWidth = parentPanelBounds.width - MESSAGE_X_LOCATION * 2;

        Dimension flyDownPanelSize = flyDownMessageHolderPanel.getSize();

        flyDownMessageHolderPanel.setBounds(MESSAGE_X_LOCATION, -flyDownPanelSize.height, messageWidth,
                flyDownPanelSize.height);

        flyDownMessageHolderPanel.validate();
        status = Status.SHOWING;
        flyDownMessageHolderPanel.setVisible(true);
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
    private static final int FRAME_MIN_WIDTH = 1000;
    private static final int FRAME_MIN_HEIGHT = 400;

    private static void createAndShowGUI() {
        //Create and set up the window.
        String messageKey = String.format(" %04d", ++messageID);
        JFrame frame = new JFrame("Testing Fly down Alert Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(FRAME_MIN_WIDTH, FRAME_MIN_HEIGHT));
        //Display the Frame.
        frame.pack();
        frame.setVisible(true);
        AdfEnv.putMainFrame(frame);
        MainFrameFlyingMessageManager.showFlyDownAlertMessage(messageKey);

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainFrameFlyingMessageManager.showFlyDownAlertMessage("Testing Fly Down Alert.");
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
