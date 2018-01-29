package sem.appui.controls;

import sem.appui.components.RoundedRectangleShape;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Sep 11, 2011
 * Time: 8:02:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TwoStateRoundedCornersButton extends RoundedCornersButton {

    private final static int FIRST = 3;
    private final static int SECOND = 4;


    private Color dbg = new Color(0x808080);
    private Color dtc = new Color(0x606060);

    private Color bc1 = new Color(0x808080);
    private Color tc1 = new Color(0x808080);
    private Color bc2 = new Color(0x808080);
    private Color tc2 = new Color(0x808080);


    private String firstStateName, secondStateName;
    private ImageIcon firstStateImage = null, secondStateImage = null;

    private Color firstStateOuterBorderColor, firstStateInnerBorderColor;
    private Color secondStateOuterBorderColor, secondStateInnerBorderColor;

    private Color firstStateBackgroundColor, firstStateForegroundColor;
    private Color secondStateBackgroundColor, secondStateForegroundColor;

    private int state = TwoStateRoundedCornersButton.FIRST;

    private ActionListener addThisButtonActionListener;

    /**
     *
     */
    public TwoStateRoundedCornersButton() {
        this(null, 0, 0, 0, null, null, null, null, "", "",
                null, null, null, null,
                "");
    }

    /**
     * @param firstStateName
     * @param secondStateName
     * @param firstStateBackgroundColor
     * @param firstStateForegroundColor
     * @param secondStateBackgroundColor
     * @param secondStateForegroundColor
     * @param tooltip
     */
    public TwoStateRoundedCornersButton(String firstStateName, String secondStateName,
                                        Color firstStateBackgroundColor, Color firstStateForegroundColor,
                                        Color secondStateBackgroundColor, Color secondStateForegroundColor,
                                        String tooltip) {
        this(null, 0, 0, 0, null, null, null, null, firstStateName, secondStateName,
                firstStateBackgroundColor, firstStateForegroundColor,
                secondStateBackgroundColor, secondStateForegroundColor,
                tooltip);
    }

    /**
     * @param externalBackground
     * @param thickness
     * @param roundingRadius
     * @param roundingPolicy
     * @param firstStateOuterBorderColor
     * @param firstStateInnerBorderColor
     * @param secondStateOuterBorderColor
     * @param secondStateInnerBorderColor
     * @param firstStateImage
     * @param secondStateImage
     * @param firstStateBackgroundColor
     * @param firstStateForegroundColor
     * @param secondStateBackgroundColor
     * @param secondStateForegroundColor
     * @param tooltip
     */
    public TwoStateRoundedCornersButton(Color externalBackground, int thickness, int roundingRadius,
                                        int roundingPolicy,
                                        Color firstStateOuterBorderColor, Color firstStateInnerBorderColor,
                                        Color secondStateOuterBorderColor, Color secondStateInnerBorderColor,
                                        ImageIcon firstStateImage, ImageIcon secondStateImage,
                                        Color firstStateBackgroundColor, Color firstStateForegroundColor,
                                        Color secondStateBackgroundColor, Color secondStateForegroundColor,
                                        String tooltip) {

        this(externalBackground, thickness, roundingRadius, roundingPolicy,
                firstStateOuterBorderColor, firstStateInnerBorderColor,
                secondStateOuterBorderColor, secondStateInnerBorderColor,
                firstStateImage, secondStateImage,
                "", "",
                firstStateBackgroundColor, firstStateForegroundColor,
                secondStateBackgroundColor, secondStateForegroundColor,
                tooltip);
    }

    /**
     * @param externalBackground
     * @param thickness
     * @param roundingRadius
     * @param roundingPolicy
     * @param firstStateOuterBorderColor
     * @param firstStateInnerBorderColor
     * @param secondStateOuterBorderColor
     * @param secondStateInnerBorderColor
     * @param firstStateName
     * @param secondStateName
     * @param firstStateBackgroundColor
     * @param firstStateForegroundColor
     * @param secondStateBackgroundColor
     * @param secondStateForegroundColor
     * @param tooltip
     */
    public TwoStateRoundedCornersButton(Color externalBackground, int thickness, int roundingRadius,
                                        int roundingPolicy,
                                        Color firstStateOuterBorderColor, Color firstStateInnerBorderColor,
                                        Color secondStateOuterBorderColor, Color secondStateInnerBorderColor,
                                        String firstStateName, String secondStateName,
                                        Color firstStateBackgroundColor, Color firstStateForegroundColor,
                                        Color secondStateBackgroundColor, Color secondStateForegroundColor,
                                        String tooltip) {

        this(externalBackground, thickness, roundingRadius, roundingPolicy,
                firstStateOuterBorderColor, firstStateInnerBorderColor,
                secondStateOuterBorderColor, secondStateInnerBorderColor,
                null, null, firstStateName, secondStateName,
                firstStateBackgroundColor, firstStateForegroundColor,
                secondStateBackgroundColor, secondStateForegroundColor,
                tooltip);
    }

    /**
     * @param externalBackground
     * @param thickness
     * @param roundingRadius
     * @param roundingPolicy
     * @param firstStateOuterBorderColor
     * @param firstStateInnerBorderColor
     * @param secondStateOuterBorderColor
     * @param secondStateInnerBorderColor
     * @param firstStateImage
     * @param secondStateImage
     * @param firstStateName
     * @param secondStateName
     * @param firstStateBackgroundColor
     * @param firstStateForegroundColor
     * @param secondStateBackgroundColor
     * @param secondStateForegroundColor
     * @param tooltip
     */
    public TwoStateRoundedCornersButton(Color externalBackground, int thickness, int roundingRadius,
                                        int roundingPolicy,
                                        Color firstStateOuterBorderColor, Color firstStateInnerBorderColor,
                                        Color secondStateOuterBorderColor, Color secondStateInnerBorderColor,
                                        ImageIcon firstStateImage, ImageIcon secondStateImage,
                                        String firstStateName, String secondStateName,
                                        Color firstStateBackgroundColor, Color firstStateForegroundColor,
                                        Color secondStateBackgroundColor, Color secondStateForegroundColor,
                                        String tooltip) {

        super(externalBackground, thickness, roundingRadius, roundingPolicy,
                firstStateOuterBorderColor, firstStateInnerBorderColor,
                firstStateImage, firstStateName);

        this.firstStateImage = firstStateImage;
        this.secondStateImage = secondStateImage;

        this.firstStateName = firstStateName;
        this.secondStateName = secondStateName;

        this.firstStateOuterBorderColor = firstStateOuterBorderColor;
        this.firstStateInnerBorderColor = firstStateInnerBorderColor;
        this.secondStateOuterBorderColor = secondStateOuterBorderColor;
        this.secondStateInnerBorderColor = secondStateInnerBorderColor;

        this.firstStateBackgroundColor = firstStateBackgroundColor;
        this.firstStateForegroundColor = firstStateForegroundColor;
        this.secondStateBackgroundColor = secondStateBackgroundColor;
        this.secondStateForegroundColor = secondStateForegroundColor;

//        setToolTipText(tooltip);

        this.setFocusPainted(false);
        this.setContentAreaFilled(false);

        // setting border

//        setBorder(new LineBorder(FIRST_STATE_BORDER_COLOR, 1, true));
//        alignedRoundedBorder = new AlignedRoundedBorder(2, 4, AlignedRoundedBorder.ROUNDING_BOTH,
//                FIRST_STATE_BORDER_COLOR, Color.BLACK);
//        this.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createEmptyBorder(1, 1, 1, 1),
//                alignedRoundedBorder
//        ));

        this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setBorder(new EmptyBorder(2, 2, 2, 2));
        if (roundingPolicy != RoundedRectangleShape.ROUNDING_NONE) {
            setRoundingProperties(thickness, roundingRadius, roundingPolicy, firstStateOuterBorderColor,
                    firstStateInnerBorderColor);
        }

        setFirstStateCurrent();
        setEnabled(false);
    }


    public boolean isInFirstState() {
        return state == TwoStateRoundedCornersButton.FIRST;
    }

    public boolean isInSecondState() {
        return state == TwoStateRoundedCornersButton.SECOND;
    }

    /**
     * @param addThisButtonActionListener
     */
    public void addThisButtonActionListener(ActionListener addThisButtonActionListener) {
        this.addThisButtonActionListener = addThisButtonActionListener;
    }

    /**
     * @param currentName
     * @param currentStateImage
     * @param currentOuterBorderColor
     * @param currentInnerBorderColor
     * @param currentBackground
     * @param currentForeground
     * @param state
     */
    private void setState(String currentName, ImageIcon currentStateImage,
                          Color currentOuterBorderColor, Color currentInnerBorderColor,
                          Color currentBackground, Color currentForeground, int state) {
        this.state = state;
        setText(currentName);
        this.setIcon(currentStateImage);
        setOuterBorderColor(currentOuterBorderColor);
        setInnerBorderColor(currentInnerBorderColor);
        setBackground(currentBackground);
        setForeground(currentForeground);
        repaint();
//        Thread.dumpStack();
    }

    /**
     *
     */
    public void setFirstStateCurrent() {
        setState(firstStateName, firstStateImage, firstStateOuterBorderColor, firstStateInnerBorderColor,
                firstStateBackgroundColor, firstStateForegroundColor, FIRST);
    }

    /**
     *
     */
    public void setSecondStateCurrent() {
        setState(secondStateName, secondStateImage, secondStateOuterBorderColor, secondStateInnerBorderColor,
                secondStateBackgroundColor, secondStateForegroundColor, SECOND);
    }

    @Override
    public void reset() {
        setFirstStateCurrent();
    }

    /**
     * @param status
     */
    @Override
    public void setEnabled(boolean status) {
//        setFirstStateCurrent();
        super.setEnabled(status);
    }

    /**
     * @param e
     */
    @Override
    void processActionEvent(ActionEvent e) {


        if (state == TwoStateRoundedCornersButton.FIRST) {
            setSecondStateCurrent();
        } else {
            setFirstStateCurrent();
        }

        if (addThisButtonActionListener != null) {
            ActionEvent be;
            if (state == FIRST) {
                be = new ActionEvent(this, 0, firstStateName, 0);
            } else {
                be = new ActionEvent(this, 1, secondStateName, 0);
            }
            addThisButtonActionListener.actionPerformed(be);
        }
        this.doBlink(170);
        repaint();
//        Thread.dumpStack();
    }

//    @Override
//   void resetGroup() {
//        if (groupOfButtons == null) {
//            return;
//        }
//
//        RoundedCornersButton button;
//        int n = groupOfButtons.size();
//        for (int i = 0; i < n; i++) {
//            button = groupOfButtons.get(i);
//            if (button != this) {
////                button.setFirstStateCurrent();
//            }
//        }
//    }

    /**
     * @param args
     */
    public static void main(String args[]) {

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(600, 600);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(new BorderLayout());
        Color BORDER_COLOR = new Color(0x00DD00);
        RoundedCornersButton roundedCornersButton = new RoundedCornersButton(Color.BLACK,
                1, 4, DirectionSetupButton.ROUNDING_ALL, BORDER_COLOR, Color.BLACK, null, "Text");
        roundedCornersButton.setBackground(new Color(0x00AA00));
        Dimension buttonSize = new Dimension(37, 37);
        roundedCornersButton.setSize(buttonSize);

//        roundedCornersButton.setProperties(1, 4, DirectionSetupButton.ROUNDING_ALL, BORDER_COLOR, Color.RED);

        mainPanel.add(roundedCornersButton);

        JFrame frame = new JFrame("Testing Frame");
        frame.setSize(600, 600);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
