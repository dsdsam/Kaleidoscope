package dsdsse.messagepopupwindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by u0180093 on 2/28/2017.
 */
class MessagePopUpWindow extends FadingWindow {

    private final JFrame mainFrame;

    private final RoundedRectangle roundedClippingRectangle = new RoundedRectangle();

    public MessagePopUpWindow(JFrame mainFrame, boolean fadeable) {
        super(mainFrame, fadeable);
//        super(mainFrame);
        this.mainFrame = mainFrame;
        roundedClippingRectangle.setRoundingPolicy(RoundedRectangle.ROUND_ALL);
//        setShape(new RoundRectangle2D.Double(10, 10, 100, 100, 50, 50));

    }

    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        Rectangle bounds = getBounds();
        setLocation(bounds.x, bounds.y);
//        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));

//        this.setOpacity(0);
        Insets insets = getInsets();
        roundedClippingRectangle.updateSize(insets, bounds.width, bounds.height);
        if (roundedClippingRectangle.clippingPolygon != null) {
//            g2.clip(roundedClippingRectangle.clippingPolygon);
            setShape(roundedClippingRectangle.clippingPolygon);
        }
//        g.setColor(Color.LIGHT_GRAY);
//        g.fillRect(0, 0, bounds.width, bounds.height);
        super.paint(g);
//        g2.setClip(null);
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setColor(Color.LIGHT_GRAY);
//        g2.draw(roundedClippingRectangle.clippingPolygon);
    }

    public static void main(String[] args) {
//        AdfUndecoratedDialogTitleBar adfUndecoratedDialogTitleBar = new AdfUndecoratedDialogTitleBar("", Color.BLUE, Color.WHITE);
//        adfUndecoratedDialogTitleBar.setBackground(Color.GREEN);
        MessagePopUpWindow messagePopUpWindow = new MessagePopUpWindow(new JFrame(), true);

        JPanel mainPanel = new FlyDownMessageHolderPanel(RoundedRectangle.ROUND_ALL);

//        mainPanel.setOpaque(false);
//        mainPanel.setBackground(Color.LIGHT_GRAY);
        messagePopUpWindow.setContentPane(mainPanel);
//        JComponent contentPane = (JComponent) messagePopUpWindow.getContentPane();
//        contentPane.setOpaque(false);
//        contentPane.add(mainPanel);



//        AdfUndecoratedDialogPopupMenu adfUndecoratedDialogPopupMenu = new AdfUndecoratedDialogPopupMenu(adfUndecoratedDialog);

//        adfUndecoratedDialogPopupMenu.setLightWeightPopupEnabled(false);

//        ((JComponent) adfUndecoratedDialog.getContentPane()).setComponentPopupMenu(adfUndecoratedDialogPopupMenu);


        messagePopUpWindow.setSize(1350, 70);
        messagePopUpWindow.setLocation(100, 400);
//        messagePopUpWindow.setVisible(true);
        messagePopUpWindow.bringUp( );

    }
}
