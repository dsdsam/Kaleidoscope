package adf.ui.components.dialogs.undecorated;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Admin on 4/1/2016.
 */
public class AdfUndecoratedDialogPopupMenu extends JPopupMenu {

    private final AdfUndecoratedDialog adfUndecoratedDialog;

    public AdfUndecoratedDialogPopupMenu(AdfUndecoratedDialog adfUndecoratedDialog) {
        this.adfUndecoratedDialog = adfUndecoratedDialog;

        addMenuItem("Exit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(AdfUndecoratedDialogPopupMenu.this.adfUndecoratedDialog == null){
                    return;
                }
                AdfUndecoratedDialogPopupMenu.this.adfUndecoratedDialog.fadeOutAndDestroy();
            }
        });
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


