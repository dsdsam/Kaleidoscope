package dsdsse.animation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 6/29/2017.
 */
public class GrowFrameToMaxSize {

    private static float GROWTH_FACTOR_STEP = 0.03f;

    static void growFrameToMaxSize(JFrame frame, boolean grow,
                                   FrameAnimationListener frameAnimationListener) {
        GrowFrameToMaxSize growFrameToMaxSize = new GrowFrameToMaxSize(frame, grow, frameAnimationListener);

    }

    //
    //   I n s t a n c e
    //

    private final JFrame frame;
    private FrameAnimationListener frameAnimationListener;
    private boolean growOnceMore = true;
    private final Dimension targetSize;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int fromWidth;
    private int fromHeight;
    private Timer growTimer;

    private int tickCounter;
    private float horizontalSpan;
    private float verticalSpan;

    private float growthFactor;

    /**
     *
     */
    GrowFrameToMaxSize(JFrame frame, final boolean growUp,
                       FrameAnimationListener frameAnimationListener) {
        this.frame = frame;
        this.frameAnimationListener = frameAnimationListener;

        growTimer = new Timer(5, (e) -> {
            GrowFrameToMaxSize.this.handleTick(growUp);
        });

        this.growTimer.setCoalesce(true);
        targetSize = growUp ? Toolkit.getDefaultToolkit().getScreenSize() : frame.getMinimumSize();
        init(growUp);
    }

    /**
     *
     */
    private void init(boolean growingUp) {
        tickCounter = 0;
        if (growingUp) {
            this.fromWidth = frame.getSize().width;
            this.fromHeight = frame.getSize().height;
        } else {
            this.fromWidth = screenSize.width;
            this.fromHeight = screenSize.height;
        }

        horizontalSpan = Math.abs(targetSize.width - fromWidth);
        verticalSpan = Math.abs(targetSize.height - fromHeight);
        growthFactor = 0;
        this.growOnceMore = true;
        this.growTimer.start();
    }

    /**
     *
     */
    private void handleTick(boolean growingUp) {
        if (growthFactor >= 1f && !this.growOnceMore) {
            this.growTimer.stop();
            frameAnimationListener.frameGrowthComplete(growingUp);
            frameAnimationListener = null;
            return;
        }

        int frameWidth;
        int frameHeight;
        if (growingUp) {
            frameWidth = fromWidth + (int) (horizontalSpan * growthFactor);
            frameHeight = fromHeight + (int) (verticalSpan * growthFactor);
        } else {
            frameWidth = fromWidth - (int) (horizontalSpan * growthFactor);
            frameHeight = fromHeight - (int) (verticalSpan * growthFactor);
        }

        if (growthFactor >= 1f) {
            this.growOnceMore = false;
            frameWidth = targetSize.width;
            frameHeight = targetSize.height;
        }
        frame.setSize(frameWidth, frameHeight);
        frame.setBounds((this.screenSize.width - frameWidth) / 2, (this.screenSize.height - frameHeight) / 2,
                frameWidth, frameHeight);
        growthFactor += GROWTH_FACTOR_STEP;
        tickCounter++;
    }
}