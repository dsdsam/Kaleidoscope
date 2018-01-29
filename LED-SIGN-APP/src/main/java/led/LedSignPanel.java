package led;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 4/17/13
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class LedSignPanel extends JPanel {

    private final String MENU_ITEM_SIZE_S = "Size S";
    private final String MENU_ITEM_SIZE_M = "Size M";
    private final String MENU_ITEM_SIZE_L = "Size L";
    private final String MENU_ITEM_SIZE_XL = "Size XL";

    private String fontName = "";

    private JFrame parentFrame;
    private OperationsProvider operationsProvider;
    private LedSignView controllableLEDSignView;
    private LedSignView.PaintMode paintMode;
    private boolean hasBorder;

    public LedSignPanel(boolean useEDT, String fontName) {
        this(useEDT, null, fontName);
    }

    /**
     * @param useEDT
     * @param parentFrame
     * @param fontName
     */
    public LedSignPanel(boolean useEDT, JFrame parentFrame, String fontName) {
        super(new BorderLayout());
        this.paintMode = useEDT ? LedSignView.PaintMode.EDT : LedSignView.PaintMode.NOT_EDT;
        this.parentFrame = parentFrame;
        this.fontName = fontName;

    }

    public boolean isStandAloneMode() {
        return parentFrame instanceof UndecoratedFrame;
    }

    /**
     *
     */
    public void initLedSignPanel(OperationsProvider operationsProvider, boolean hasBorder) {
        this.hasBorder = hasBorder;
        controllableLEDSignView = new LedSignView(paintMode, fontName, operationsProvider, hasBorder);
        add(controllableLEDSignView, BorderLayout.CENTER);

        this.operationsProvider = operationsProvider;
        this.setFocusable(true);

        //adding close menu
        if (parentFrame != null) {
            SignPopUpMenu frameControlsPopUpMenu = new SignPopUpMenu(parentFrame);
            frameControlsPopUpMenu.setLightWeightPopupEnabled(false);
            ((JComponent) parentFrame.getContentPane()).setComponentPopupMenu(frameControlsPopUpMenu);
        }

        controllableLEDSignView.startRunningSign();
    }

    public void addKeyBoardInputListener(KeyListener keyListener) {
        if (parentFrame == null) {
            return;
        }
        parentFrame.addKeyListener(keyListener);
    }

    private void setPanelSize(int newHeight) {
        if (parentFrame instanceof UndecoratedFrame) {
            Dimension frameSize = parentFrame.getSize();
            frameSize.height = newHeight;
            parentFrame.setSize(frameSize);
        } else {
            Dimension panelSize = this.getSize();
            panelSize.height = newHeight - 8;
            setSize(panelSize);
            setPreferredSize(panelSize);
            setMinimumSize(panelSize);
            setMaximumSize(panelSize);
            parentFrame.pack();
            parentFrame.validate();
        }
    }

    /**
     * P o p u p    M e n u
     */
    private class SignPopUpMenu extends JPopupMenu {

        private Window frame;

        ActionListener menuSizeActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MENU_ITEM_SIZE_S.equalsIgnoreCase(e.getActionCommand())) {
                    setPanelSize(29);
                } else if (MENU_ITEM_SIZE_M.equalsIgnoreCase(e.getActionCommand())) {
                    setPanelSize(39);
                } else if (MENU_ITEM_SIZE_L.equalsIgnoreCase(e.getActionCommand())) {
                    setPanelSize(49);
                } else if (MENU_ITEM_SIZE_XL.equalsIgnoreCase(e.getActionCommand())) {
                    setPanelSize(59);
                }
            }
        };

        public SignPopUpMenu(JFrame frame) {
            this((Window) frame);
        }

        public SignPopUpMenu(Window frame) {
            this.frame = frame;

            addMenuItem(MENU_ITEM_SIZE_S, menuSizeActionListener);
            addMenuItem(MENU_ITEM_SIZE_M, menuSizeActionListener);
            addMenuItem(MENU_ITEM_SIZE_L, menuSizeActionListener);
            addMenuItem(MENU_ITEM_SIZE_XL, menuSizeActionListener);

            if (parentFrame instanceof UndecoratedFrame) {

                addSeparator();

                addMenuItem("Exit", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SignPopUpMenu.this.frame.removeAll();
                        SignPopUpMenu.this.frame.setVisible(false);
                        SignPopUpMenu.this.frame.dispose();
                        System.out.println("Frame closed");
                        System.exit(0);
                    }
                });
            }
        }

        /**
         * @param text
         * @param actionListener
         */
        private void addMenuItem(String text, ActionListener actionListener) {
            JMenuItem menuItem = new JMenuItem(text);
            menuItem.setBackground(Color.LIGHT_GRAY);
            menuItem.setForeground(Color.BLACK);
            menuItem.setUI(new BasicMenuItemUI() {
                {
                    selectionBackground = Color.ORANGE;
                    selectionForeground = Color.DARK_GRAY;
                }
            });
            menuItem.addActionListener(actionListener);
            add(menuItem);
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//                createPanel();
            }
        });
    }


}
