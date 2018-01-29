package dsdsse.messageflydownpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Admin on 5/30/2017.
 */
public class MessageFlyDownManager {


    private static final Logger logger = Logger.getLogger(MessageFlyDownManager.class.getName());

    private enum Status {HIDDEN, SHOWN, SHOWING, HIDING, PAUSE}

    private static final int MESSAGE_EXPOSURE_TIME = 5000;
    private static final int MESSAGES_BREAK_TIME = 300;

    private static final int GAP = 5;
    private static final int STEP = 2;
    private static final int TICK = 3;

    //
    //   I n s t a n c e
    //

    private MessageFlyDownManager messagePopupManager;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Semaphore semaphore = new Semaphore(1);
    private volatile boolean queueBlocked;

    private MessageFlyDownHolderPanel messageFlyDownHolderPanel;

    private final JComponent parentComponent;
    private JComponent disposableParentComponent;
    private final JLayeredPane layeredPane;
    private int messageXLocation = 20;
    private int messageWidth;

    private Status status = Status.HIDDEN;

    private Timer animatingTimer;
    private Timer unblockingQueueTimer = new Timer(5000, (e) -> {
        logger.severe("\n QUEUE - UN UN UN - BLOCKED");
        queueBlocked = false;
    });

    /**
     * @param message
     */
    final void showAlertMessage(String message) {
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
                    messageFlyDownHolderPanel.setMessage(message);
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
     * @param parentComponent
     */
    MessageFlyDownManager(JComponent parentComponent, JLayeredPane layeredPane) {
        messagePopupManager = this;
        this.parentComponent = parentComponent;
        this.layeredPane = layeredPane;
        this.messageFlyDownHolderPanel = new MessageFlyDownHolderPanel(RoundedRectangle.ROUND_BOTTOM_SIDE);

        this.messageFlyDownHolderPanel.setVisible(false);

        animatingTimer = new Timer(TICK, timerActionListener);

        layeredPane.addComponentListener(componentAdapter);
        layeredPane.add(this.messageFlyDownHolderPanel, new Integer(500));

        executor.execute(messagePresenter);
        unblockingQueueTimer.setRepeats(false);
    }

    /**
     *
     */
    private void timerTickProcessor() {

        if (status == Status.HIDING) {
            Rectangle frameBounds = layeredPane.getBounds();
            Rectangle messageBounds = messageFlyDownHolderPanel.getBounds();
            int newY = messageBounds.y - STEP;
            messageFlyDownHolderPanel.setBounds(messageBounds.x, newY, messageBounds.width, messageBounds.height);
            parentComponent.repaint();
            layeredPane.repaint();
//            messageFlyDownHolderPanel.validate();
            int currentHeight = messageBounds.height + newY;
            System.out.println("currentHeight  " + currentHeight);
            if (currentHeight <= 0) {
                MessageFlyDownManager.this.animatingTimer.stop();
                MessageFlyDownManager.this.animatingTimer.setRepeats(false);
                status = Status.PAUSE;
                messageFlyDownHolderPanel.setVisible(false);
                MessageFlyDownManager.this.animatingTimer.setInitialDelay(MESSAGES_BREAK_TIME);
                MessageFlyDownManager.this.animatingTimer.start();
            }

//            Rectangle frameBounds = layeredPane.getBounds();
//            Rectangle currentBounds = messageFlyDownHolderPanel.getBounds();
//            int newX = currentBounds.x + STEP;
//            messageFlyDownHolderPanel.setBounds(newX, currentBounds.y, currentBounds.width, currentBounds.height);
//            int visibleWidth = frameBounds.width - currentBounds.x;
//            if (visibleWidth <= 0) {
//                FlyDownMessageManager.this.animatingTimer.stop();
//                FlyDownMessageManager.this.animatingTimer.setRepeats(false);
//                status = Status.PAUSE;
//                messageFlyDownHolderPanel.setVisible(false);
//                FlyDownMessageManager.this.animatingTimer.setInitialDelay(MESSAGES_BREAK_TIME);
//                FlyDownMessageManager.this.animatingTimer.start();
//            }

        } else if (status == Status.PAUSE) {

            MessageFlyDownManager.this.animatingTimer.stop();
            status = Status.HIDDEN;
            MessageFlyDownManager.this.animatingTimer.setRepeats(true);
            MessageFlyDownManager.this.animatingTimer.setInitialDelay(TICK);
            MessageFlyDownManager.this.animatingTimer.setDelay(TICK);
            semaphore.release();

        } else if (status == Status.SHOWING) {

            Rectangle parentPanelBounds = layeredPane.getBounds();
            Rectangle messageBounds = messageFlyDownHolderPanel.getBounds();
            messageBounds.width = parentPanelBounds.width - 2 * messageXLocation;
            messageWidth = messageBounds.width;

            int visibleHeight = messageBounds.y;
            int dy = messageBounds.width - visibleHeight;
            dy = dy > STEP ? STEP : dy;
            int newY = messageBounds.y + dy;
            messageFlyDownHolderPanel.setBounds(messageXLocation, newY, messageBounds.width, messageBounds.height);
            if (visibleHeight + STEP >= 0) {
                MessageFlyDownManager.this.animatingTimer.stop();
                MessageFlyDownManager.this.animatingTimer.setRepeats(false);
                status = Status.SHOWN;
                MessageFlyDownManager.this.animatingTimer.setInitialDelay(MESSAGE_EXPOSURE_TIME);
                MessageFlyDownManager.this.animatingTimer.start();
            }

//            Rectangle frameBounds = layeredPane.getBounds();
//            Rectangle currentBounds = messageFlyDownHolderPanel.getBounds();
//
//            int visibleWidth = frameBounds.width - currentBounds.x;
//            int dx = currentBounds.width - visibleWidth;
//            dx = dx > STEP ? STEP : dx;
//            int newX = currentBounds.x - dx;
//            messageFlyDownHolderPanel.setBounds(newX, currentBounds.y, currentBounds.width, currentBounds.height);
//            if (visibleWidth + STEP >= currentBounds.width) {
//                FlyDownMessageManager.this.animatingTimer.stop();
//                FlyDownMessageManager.this.animatingTimer.setRepeats(false);
//                status = Status.SHOWN;
//                FlyDownMessageManager.this.animatingTimer.setInitialDelay(MESSAGE_EXPOSURE_TIME);
//                FlyDownMessageManager.this.animatingTimer.start();
//            }

        } else if (status == Status.SHOWN) {

            MessageFlyDownManager.this.animatingTimer.stop();
            MessageFlyDownManager.this.animatingTimer.setRepeats(true);
            MessageFlyDownManager.this.animatingTimer.setInitialDelay(TICK);
            MessageFlyDownManager.this.animatingTimer.setDelay(TICK);
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
        MessageFlyDownManager.this.animatingTimer.setRepeats(true);
        MessageFlyDownManager.this.animatingTimer.setInitialDelay(TICK);
        MessageFlyDownManager.this.animatingTimer.setDelay(TICK);
        status = Status.HIDDEN;
        messageFlyDownHolderPanel.setVisible(false);
        Dimension flyOutAlertPanelSize = messageFlyDownHolderPanel.getSize();

        messageFlyDownHolderPanel.setBounds(messageXLocation, -flyOutAlertPanelSize.height,
                messageWidth, flyOutAlertPanelSize.height);
//        messageFlyDownHolderPanel.setBounds(layeredPaneBounds.width,
//                height - flyOutAlertPanelSize.height - GAP,
//                flyOutAlertPanelSize.width, flyOutAlertPanelSize.height);

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
        Rectangle parentPanelBounds = layeredPane.getBounds();
        messageWidth = parentPanelBounds.width - 2 * messageXLocation;

        Dimension flyOutAlertPanelSize = messageFlyDownHolderPanel.getSize();

        messageFlyDownHolderPanel.setBounds(messageXLocation, -flyOutAlertPanelSize.height,
                messageWidth, flyOutAlertPanelSize.height);
        messageFlyDownHolderPanel.validate();
//        messageFlyDownHolderPanel.setBounds(layeredPaneBounds.width,
//                height - flyOutAlertPanelSize.height - GAP,
//                flyOutAlertPanelSize.width, flyOutAlertPanelSize.height);
        status = Status.SHOWING;
        messageFlyDownHolderPanel.setVisible(true);
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
        frame.setMinimumSize(new Dimension(1400, 400));
        //Display the Frame.
        frame.pack();
        frame.setVisible(true);

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.setOpaque(true);
        mainPanel.setBackground(Color.WHITE);
        frame.add(mainPanel);

        JLayeredPane layeredPane = new JLayeredPane();
        JPanel contentPanel = new JPanel(new BorderLayout());
        layeredPane.add(contentPanel, new Integer(1));
        contentPanel.add(new Button("ABCD"), BorderLayout.NORTH);
        layeredPane.setBorder(null);
        mainPanel.add(layeredPane, BorderLayout.CENTER);

        frame.validate();

        MessageFlyDownManager messageFlyDownManager = new MessageFlyDownManager(mainPanel, layeredPane);

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String messageKey = String.format(" %04d", ++messageID);
                messageFlyDownManager.showAlertMessage(messageKey);
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
