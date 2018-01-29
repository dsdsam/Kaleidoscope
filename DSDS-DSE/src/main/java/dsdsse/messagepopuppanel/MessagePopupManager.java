package dsdsse.messagepopuppanel;


import dsdsse.app.DsdsseMainFrame;
import dsdsse.designspace.DesignSpaceContentManager;
import dsdsse.designspace.DesignSpaceView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Admin on 5/30/2017.
 */
public class MessagePopupManager {


    private static final Logger logger = Logger.getLogger(MessagePopupManager.class.getName());

//    private static final Color BACKGROUND_COLOR = new Color(0xE0f3f3f6, true);

//    private static final Color BACKGROUND_COLOR = new Color(0xE0f3f3f6, true);

    private static MessagePopupManager messagePopupManager = new MessagePopupManager(DsdsseMainFrame.getInstance());

    private enum Status {HIDDEN, SHOWN, SHOWING, HIDING, PAUSE}

    private static final int MESSAGE_EXPOSURE_TIME = 5000;
    private static final int MESSAGES_BREAK_TIME = 300;

    private static final int GAP = 5;
    private static final int STEP = 2;
    private static final int TICK = 3;

    /**
     * called to create class
     */
    public static synchronized void createMessagePopup() {
//        if (messagePopupManager == null) {
//
//            messagePopupManager.showMessage(MessagePopUpPanelHolder.WARNING_MESSAGE,
//                    MessagePopUpPanelHolder.MESSAGE_LOCATION_CENTER, "");
//        }
    }

    // called for Menu new project
//    public synchronized static void showMessagePopup(JFrame frame) {
//        if (messagePopupManager != null) {
////            new MessagePopupManager(frame);
//            messagePopupManager.showMessage("", MessagePopUpPanelHolder.INFO_MESSAGE,
//                    MessagePopUpPanelHolder.MESSAGE_LOCATION_NORTH, null);
//        }
//    }

    public synchronized static void showMessagePopup(String messageText, int messageType, int messageLocation,
                                                     int width,
                                                     int messageBackground, int messageForeground, int waitingTime,
                                                     MessageClosedListener messageClosedListener) {
        if (messagePopupManager != null) {
            messagePopupManager.showMessage(messageText, messageType, messageLocation, width,
              messageBackground,   messageForeground,   waitingTime, messageClosedListener);
        }
    }

    public static void cancelMessage(){
        if (messagePopupManager != null) {
            messagePopupManager.cancelCurrentMessage();
        }
    }

    //
    //   I n s t a n c e
    //

    private JFrame frame;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Semaphore semaphore = new Semaphore(1);
    private volatile boolean queueBlocked;

    private MessagePopUpPanelHolder messagePopUpPanelHolder;
    private JComponent basePanel;
    private JComponent disposableParentComponent;
    private final JLayeredPane layeredPane;
    private int messageXLocation = 20;
    private int messageWidth;


    void showMessage(String messageText, int messageType, int messageLocation,  int width,    int messageBackground,
                     int messageForeground, int waitingTime,MessageClosedListener messageClosedListener) {
//        String infoMessage = "Hello World From Info Popup !";
        String warningMessage = "H e l l o   W o r l d   F r o m   W a r n i n g   P o p u p !";
        messagePopUpPanelHolder.showMessage(messageText, messageType, messageLocation, width,
          messageBackground,   messageForeground,   waitingTime,messageClosedListener);
    }

    void cancelCurrentMessage() {
        String infoMessage = "Cancel !   Cancel !   Cancel !";
        String warningMessage = "H e l l o   W o r l d   F r o m   W a r n i n g   P o p u p !";
        messagePopUpPanelHolder.cancelCurrentMessage();
    }


    private Status status = Status.HIDDEN;

    private final ComponentListener componentListener = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            setMessageSize();
        }

        @Override
        public void componentShown(ComponentEvent e) {
            setMessageSize();
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            setMessageSize();
        }


    };

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
//    private final Runnable messagePresenter = () -> {
//        while (true) {
//            try {
//                semaphore.acquire();
//                String message = messageQueue.take();
//
//                // waiting when showing message from queue is unblocked
//                while (queueBlocked) {
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                    }
//                }
//                SwingUtilities.invokeAndWait(() -> {
//                    flyOutHolderPanel.setMessage(message);
//                    showPanel();
//                });
//            } catch (Throwable e) {
//                logger.log(Level.SEVERE, "Message processing failed with Exception: " + e);
//            }
//        }
//    };

//    /**
//     * Blocks showing messages from queue and
//     * hides shown message, if any
//     */
//    private final ComponentAdapter componentAdapter = new ComponentAdapter() {
//        @Override
//        public void componentResized(ComponentEvent e) {
//            blockQueue();
//            resetToHiddenStatus();
//        }
//    };

    /**
     *
     */
//    private final ActionListener timerActionListener = (e) -> {
//        timerTickProcessor();
//    };

    /**
     *
     */
    private void setMessageSize() {
        Rectangle basePanelRectangle = basePanel.getBounds();
        Point location = basePanelRectangle.getLocation();
        Point updatedLocation = SwingUtilities.convertPoint(DesignSpaceView.getInstance(), location, null);
//        Point updatedLocation = SwingUtilities.convertPoint(DsdsseMainPanel.getInstance(), location, null);
        updatedLocation.x += 2;
        updatedLocation.y -= 6;
        basePanelRectangle.setLocation(updatedLocation);

        Rectangle fb = frame.getBounds();
        fb.x = 0;
        fb.y = 0;
        layeredPane.setBounds(fb);
        messagePopUpPanelHolder.setBounds(basePanelRectangle);
        messagePopUpPanelHolder.revalidate();
    }

    //   C r e a t i n g   M e s s a g e   P o p u p   M a n a g e r

    private MessagePopupManager(JFrame frame) {
        this.frame = frame;
        messagePopupManager = this;
        Rectangle fb = frame.getBounds();
        basePanel = DesignSpaceView.getInstance().getMclnGraphDesignerView();
//        basePanel = DesignSpaceView.getInstance();
        basePanel.addComponentListener(componentListener);

        messagePopUpPanelHolder = new MessagePopUpPanelHolder();
//        messagePopUpPanelHolder.setOpaque(false);
        messagePopUpPanelHolder.setBackground(Color.ORANGE);


        layeredPane = frame.getLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setBackground(Color.GREEN);

        Rectangle cpb = frame.getContentPane().getBounds();

        Rectangle lpb = layeredPane.getBounds();
        Rectangle dsb = DesignSpaceView.getInstance().getBounds();


        Rectangle cmb = DesignSpaceContentManager.designSpaceContentManager.getPanelBounds();

//

//        Rectangle basePanelRectangle = basePanel.getBounds();
//        Point location = basePanelRectangle.getLocation();
//        Point updatedLocation = SwingUtilities.convertPoint(DesignSpaceView.getInstance(), location, null);
//        updatedLocation.x += 2;
//        updatedLocation.y -= 6;
//        basePanelRectangle.setLocation(updatedLocation);
//        fb.x = 0;
//        fb.y = 0;
////        basePanelRectangle.y =DesignSpaceContentManager.designSpaceContentManager.getPanelBounds().y;
//        layeredPane.setBounds(fb);
////        messagePopupPanel.setBounds(basePanel.getX(),0,basePanel.getWidth(), basePanel.getHeight());
//
//        messagePopupPanel.setBounds(basePanelRectangle);
//
//        Rectangle contentPaneBounds = basePanel.getBounds();
//        setBounds(contentPaneBounds.x, contentPaneBounds.y, contentPaneBounds.width, contentPaneBounds.height);


        layeredPane.add(messagePopUpPanelHolder, JLayeredPane.PALETTE_LAYER);
        setMessageSize();
//

//        super(11, dsdsse.welcome.RoundedRectangle.ROUND_ALL);
//        this.frame = frame;
//        basePanel.addComponentListener(componentListener);
//        setLayout(new BorderLayout());
//        setBorder(PANEL_BORDER);
//
//        JPanel titlePanel = new JPanel(new GridBagLayout());
//        titlePanel.setSize(CLOSE_BUTTON_SIZE);
//        titlePanel.setMaximumSize(CLOSE_BUTTON_SIZE);
//        titlePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
//        titlePanel.setOpaque(false);
//        titlePanel.setBackground(Color.CYAN);
//
//        Component spaceHolder = Box.createHorizontalGlue();
//        spaceHolder.setSize(CLOSE_BUTTON_SIZE);
//        spaceHolder.setMaximumSize(CLOSE_BUTTON_SIZE);
//        titlePanel.add(spaceHolder, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
//                GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
//
////        String text = "   Click to Start Brief Animated Tutorial Now   ";
//        String text = "   Click here to watch self-running presentation: \"How to Use Model Creation Operations\"." +
//                "  Show runs in full screen.   ";
//        animatedTutorialLabel = createAnimatedTutorialLabel(text, mouseListener);
////        titlePanel.add(animatedTutorialLabel);
//        titlePanel.add(animatedTutorialLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
//                GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
//
////        Component spaceHolder = Box.createHorizontalGlue();
////        titlePanel.add(spaceHolder, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
////                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//
//        closeLabel = createCloseLabel(CLOSE_BUTTON_ICON_NAME_, mouseListener);
//        closeLabel.setSize(CLOSE_BUTTON_SIZE);
//        closeLabel.setMaximumSize(CLOSE_BUTTON_SIZE);
////        titlePanel.add(closeLabel);
//        titlePanel.add(closeLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
//                GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
//
//        this.add(titlePanel, BorderLayout.NORTH);
//
//
//        welcomeContent.setOpaque(false);
//        welcomeContent.setBackground(BACKGROUND_COLOR);
//        add(welcomeContent, BorderLayout.CENTER);
//
//        JLayeredPane layeredPane = frame.getLayeredPane();
//        layeredPane.setOpaque(false);
//        setOpaque(false);
//        setBackground(BACKGROUND_COLOR);
//
//        Rectangle contentPaneBounds = basePanel.getBounds();
//        setBounds(contentPaneBounds.x, contentPaneBounds.y, contentPaneBounds.width, contentPaneBounds.height);
//
//        layeredPane.add(this, JLayeredPane.PALETTE_LAYER);
    }


//    /**
//     * @param parentComponent
//     */
//    MessagePopupManager(JComponent parentComponent, JLayeredPane layeredPane) {
//        messagePopupManager = this;
//
//        this.layeredPane = layeredPane;
//        this.flyOutHolderPanel = new MessagePopUpHolderPanel();
//
//        this.flyOutHolderPanel.setVisible(false);
//
//        animatingTimer = new Timer(TICK, timerActionListener);
//
//        layeredPane.addComponentListener(componentAdapter);
//        layeredPane.add(this.flyOutHolderPanel, new Integer(500));
//
//        executor.execute(messagePresenter);
//        unblockingQueueTimer.setRepeats(false);
//    }


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


//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            createAndShowGUI();
//        });
//    }
}
