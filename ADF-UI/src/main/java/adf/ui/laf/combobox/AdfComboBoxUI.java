package adf.ui.laf.combobox;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalComboBoxButton;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by u0180093 on 4/26/2016.
 */
public class AdfComboBoxUI extends MetalComboBoxUI {

    private JComponent component;

    public boolean isResizable = false;

    public AdfComboBoxUI() {
        super();
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
        component = c;
    }

    public static ComponentUI createUI(JComponent c) {
        return new AdfComboBoxUI();
    }

    public void setUseComboBoxFont(boolean useComboBoxFont) {
        if (arrowButton instanceof AdfArrowComboBoxButton) {
            ((AdfArrowComboBoxButton) arrowButton).setUseComboBoxFont(useComboBoxFont);
        }
    }

    /**
     * The default "arrow button" is replaces with our "special" button that draws
     * the icon instead of the button.
     *
     * @see javax.swing.plaf.basic.BasicComboBoxUI#createArrowButton()
     */
    protected JButton createArrowButton() {
        return new AdfArrowComboBoxButton(comboBox);
    }

    public JButton getArrowButton() {
        return arrowButton;
    }

    public void enableInternalTooltips(boolean arg_bEnable) {
        if (arrowButton instanceof AdfArrowComboBoxButton)
            ((AdfArrowComboBoxButton) arrowButton).m_bDisableInternalTooltips = !arg_bEnable;
    }

    /**
     * The reason this needs to be overriden is because
     * {@link MetalComboBoxUI#getMinimumSize(javax.swing.JComponent)}
     * only really works if the arrow button is an instance of
     * {@link MetalComboBoxButton}.
     */
    public Dimension getMinimumSize(JComponent c) {
        if (!isMinimumSizeDirty) {
            return new Dimension(cachedMinimumSize);
        }

        Dimension size = null;

        if (!comboBox.isEditable() && arrowButton != null) // This is the line that's different
        {
            JButton button = (JButton) arrowButton;
            Insets buttonInsets = button.getInsets();
            Insets insets = comboBox.getInsets();

            size = getDisplaySize();
            size.width += insets.left + insets.right;
            size.width += buttonInsets.left + buttonInsets.right;
            size.width += buttonInsets.right + ((button.getIcon() == null) ? 0 : button.getIcon().getIconWidth());
            size.height += insets.top + insets.bottom;
            size.height += buttonInsets.top + buttonInsets.bottom;
        } else if (comboBox.isEditable() &&
                arrowButton != null &&
                editor != null) {
            size = super.getMinimumSize(c);
            Insets margin = arrowButton.getMargin();
            size.height += margin.top + margin.bottom;
            size.width += margin.left + margin.right;
        } else {
            size = super.getMinimumSize(c);
        }

        cachedMinimumSize.setSize(size.width, size.height);
        isMinimumSizeDirty = false;

        return new Dimension(cachedMinimumSize);
    }

    /**
     * Sets whether to resize the text to fit or not
     */
    public void setResizable(boolean isResizable) {
        this.isResizable = isResizable;
    }


//    /**
//     * <p>Title: VetoableMouseProxy</p>
//     * <p>Description: The instance of this class should be interposed between
//     * an arrow button and each actual mouse listener to prevent the unwanted show
//     * of the combobox's dropdown list.</p>
//     *
//     * @author Vladimir Karpushin
//     * @see com.fxall.gui.component.VetoableMouseListener
//     * @see com.fxall.gui.component.VetoableMouseAdapter
//     * @see com.fxall.gui.component.IMouseVetoableComponent
//     * @see com.fxall.gui.component.BasicMouseVetoableComponent
//     */
    protected class VetoableMouseProxy implements MouseListener {
        private MouseListener m_vmpTargetListener;
        private MouseListener mouseListener;
        //        private IMouseVetoableComponent m_VetoChecker;
        private boolean m_bLeadVetoListener;

        public VetoableMouseProxy(MouseListener vmpTargetListener,
                                  Object vmpVetoableMouseComponent) {
            m_vmpTargetListener = vmpTargetListener;
            mouseListener = vmpTargetListener;
//            m_VetoChecker = vmpVetoableMouseComponent;
        }

        /**
         * Only the Lead Veto Listener is eligible to request the veto state recalculation.
         */
        public void setLeadVetoListener(boolean arg_bValue) {
            m_bLeadVetoListener = arg_bValue;
        }

        public void mousePressed(MouseEvent e) {
            //System.out.println("------> mousePressed():  Lead = " + m_bLeadVetoListener + ", " + m_vmpTargetListener);
//            if (m_VetoChecker.isMouseEventEnabled(e, m_bLeadVetoListener)) {
//                if (m_vmpTargetListener != null)
//                    m_vmpTargetListener.mousePressed(e);
//            }
            m_vmpTargetListener.mousePressed(e);
        }

        public void mouseReleased(MouseEvent e) {
//            if (m_VetoChecker.isMouseEventEnabled(e, false)) {
//                if (m_vmpTargetListener != null)
//                    m_vmpTargetListener.mouseReleased(e);
//            }
            m_vmpTargetListener.mouseReleased(e);
        }

        public void mouseClicked(MouseEvent e) {
//            if (m_VetoChecker.isMouseEventEnabled(e, false)) {
//                if (m_vmpTargetListener != null)
//                    m_vmpTargetListener.mouseClicked(e);
//            }
            m_vmpTargetListener.mouseClicked(e);
        }

        public void mouseEntered(MouseEvent e) {
//            if (m_VetoChecker.isMouseEventEnabled(e, false)) {
//                if (m_vmpTargetListener != null)
//                    m_vmpTargetListener.mouseEntered(e);
//            }
            m_vmpTargetListener.mouseEntered(e);
        }

        public void mouseExited(MouseEvent e) {
//            if (m_VetoChecker.isMouseEventEnabled(e, false)) {
//                if (m_vmpTargetListener != null)
//                    m_vmpTargetListener.mouseExited(e);
//            }
            m_vmpTargetListener.mouseExited(e);
        }
    }


    /**
     * <ul>
     * <li>Uses the special {@link AdfComboBoxArrowButtonUI} delegate
     * <li>Uses custom border insets
     * <li>Delegates to the combobox for the text
     * </ul>
     */
    class AdfArrowComboBoxButton extends JButton { //implements ResizableTextButton {
        static private final String uiClassID = "OrionComboBoxArrowButtonUI";

        private JComboBox cb;
        private String sToolTipText;
        //        protected BasicMouseVetoableComponent m_MouseVetoHolder;
        protected boolean m_bDisableInternalTooltips;

        private AdfArrowComboBoxButton(JComboBox cb) {
            super();
            this.cb = cb;
            setHorizontalTextPosition(LEADING);
            setHorizontalAlignment(RIGHT);

            // Unless someone changes the default metal button borders, we need to reduce the
            // insets of this button's border.  The outside is fine, the inside is too big
//            Border outsideBorder = ((CompoundBorder) getBorder()).getOutsideBorder();
//            Border border = BorderFactory.createCompoundBorder(outsideBorder, BorderFactory.createEmptyBorder(0, 4, 0, 0));
//            setBorder(border);
            setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));

            // this component aimed at collecting all the registered vetoable listeners and
            //   requesting them one-by-one when the "isMouseEventEnabled" method is called.
//            m_MouseVetoHolder = new BasicMouseVetoableComponent();

        }

        @Override
        public Dimension getPreferredSize() {
            return cb.getPreferredSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return cb.getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return cb.getPreferredSize();
        }

        @Override
        public int getWidth(){
            Dimension size =  cb.getPreferredSize();
            return size.width;
        }

        public void setUseComboBoxFont(boolean useComboBoxFont) {
            ButtonUI ui = getUI();
            if (ui instanceof AdfComboBoxArrowButtonUI) {
                ((AdfComboBoxArrowButtonUI) ui).setUseComboBoxFont(useComboBoxFont);
            }
        }

        public JComboBox getJComboBox() {
            return cb;
        }

        private AdfArrowComboBoxButton(String name) {
            super(name);
        }

        public void updateUI() {
            setUI((ButtonUI) AdfComboBoxArrowButtonUI.createUI(this));
        }

        public String getUIClassID() {
            return uiClassID;
        }

        // QF-529 Mouse events should be disregarded on behalf of the
        //        external verifier's veto.
        public void addMouseListener(MouseListener targetListener) {
            if (targetListener == null)
                return;
//            VetoableMouseProxy vmp = new VetoableMouseProxy(targetListener, null);
            super.addMouseListener(targetListener);
            updateLeadVetoListener(1);
        }

        //
        public void removeMouseListener(MouseListener targetListener) {
            if (targetListener == null)
                return;
            MouseListener[] mouseListeners = getMouseListeners();
            if (mouseListeners == null)
                return;
            int iLsnrCount = mouseListeners.length;
            if (iLsnrCount == 0)
                return;
            for (int i = iLsnrCount - 1; i >= 0; i--) {
                MouseListener lsnr = mouseListeners[i];
                if (lsnr instanceof VetoableMouseProxy) {
                    if (((VetoableMouseProxy) lsnr).m_vmpTargetListener == targetListener) {
                        ((VetoableMouseProxy) lsnr).m_vmpTargetListener = null;
                        super.removeMouseListener(lsnr);
                        //break; there might be duplicates(?), so do not break
                    }
                }
            }
            updateLeadVetoListener(2);
        }

        //
//        public void addVetoableMouseListener(VetoableMouseListener arg_Listener) {
//            m_MouseVetoHolder.addVetoableMouseListener(arg_Listener);
//        }
//
//        //
//        public void removeVetoableMouseListener(VetoableMouseListener arg_Listener) {
//            m_MouseVetoHolder.removeVetoableMouseListener(arg_Listener);
//        }
//
//        //
//        public boolean isMouseEventEnabled(MouseEvent ev, boolean arg_bShouldRefresh) {
//            return m_MouseVetoHolder.isMouseEventEnabled(ev, arg_bShouldRefresh);
//        }

        // QF_627
        // The lead one is the listener which is called first in the chain of same-type listeners.
        private void updateLeadVetoListener(int arg_iOperation) {
            MouseListener[] mouseListeners = getMouseListeners();
            if (mouseListeners == null)
                return;
            int iLsnrCount = mouseListeners.length;
            if (iLsnrCount == 0)
                return;
            //String sOperation = (arg_iOperation == 1) ? sOperation = "ADD" : (arg_iOperation == 2) ? sOperation = "DEL" : "???";
            int k = 0;
            for (int i = 0; i < iLsnrCount; i++) {
                MouseListener lsnr = mouseListeners[i];
                // take into account only the active VetoableMouseProxy instances
                if ((lsnr instanceof VetoableMouseProxy) && (((VetoableMouseProxy) lsnr).m_vmpTargetListener != null)) {
                    // if the leading listener (called first)
                    if (k == 0) {
                        ((VetoableMouseProxy) lsnr).setLeadVetoListener(true);
                        //System.out.println("---------->> LeadVetoListener: " + sOperation + " " +
                        //	((VetoableMouseProxy)lsnr).m_vmpTargetListener + ", index = " + i + ", Listeners = " + iLsnrCount);
                    } else {
                        ((VetoableMouseProxy) lsnr).setLeadVetoListener(false);
                    }
                    k++;
                }
            }
        }

        // QF-364 "For all buttons/drop-down-boxes where the text is too large to fit,
        // the text will be truncated and suffixed by a '...' - with one exception.
        // On the Provider buttons, the text should shrink to fit the button."
        //
        public boolean getFitTextToWidth() {
            return false;
        }

        //
        public void setToolTipText(String text) {
            sToolTipText = text;
            super.setToolTipText(text);
        }

        /**
         * This button doesn't really have text of its own, it just shows the combobox's model's selected item
         */
        public String getText() {
            Object item = "";

            if (cb != null
                    && cb.getModel() != null
                    && cb.getModel().getSelectedItem() != null) item = cb.getModel().getSelectedItem();

            if (item instanceof FileFilter) {
                return ((FileFilter) item).getDescription();
            }
//            if (item instanceof CheckComboStore) {
//                StringBuffer buf = new StringBuffer();
//                int sizeOfCombo = cb.getModel().getSize();
//                DefaultComboBoxModel cmbBoxModel = (DefaultComboBoxModel) cb.getModel();
//                for (int h = 0; h < sizeOfCombo; h++) {
//                    String text = ((CheckComboStore) cmbBoxModel.getElementAt(h)).getId();
//                    Boolean state = ((CheckComboStore) cmbBoxModel.getElementAt(h)).getState();
//                    if (state.booleanValue() == true) {
//                        buf.append(getDisplayableAccountName(text));
//                    }
//                }
//                return buf.toString();
//            }
            String text = item.toString();
            // julia - 8/10/2004: Setup panel account box does not use the AccountsComboBoxModel
            ///////////////////////////////////////////////////////////////////////////
            // DL:	Took out the next two lines because they don't belong in gui-lib.
            // 		Instead, have the button's model determine what the text should be
            ///////////////////////////////////////////////////////////////////////////
            if (cb != null)
                text = getDisplayableAccountName(text);

            // check the conditions locally to avoid too many extra calls of "setToolTipText()"
            if (!m_bDisableInternalTooltips) {
                if ((sToolTipText == null) || !sToolTipText.equals(text))
                    this.setToolTipText(text);
            }
            return text.toString();
        }

        public boolean resizeText() {
            return isResizable;
        }

        /**
         * Returns an account name stripped of an "@customer" suffix
         * if one exists.
         *
         * @param accountname
         * @return
         */
        public String getDisplayableAccountName(String accountname) {
            if (accountname == null) {
                return "";
            }

            StringBuffer prefix = new StringBuffer();
            char[] accountCh = accountname.toCharArray();

            for (int i = 0; i < accountCh.length; i++) {
                if (accountCh[i] != '@') {
                    prefix.append(accountCh[i]);
                } else {
                    break;
                }
            }

            return prefix.toString();
        }
    }
}


