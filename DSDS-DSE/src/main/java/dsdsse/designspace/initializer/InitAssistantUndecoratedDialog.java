package dsdsse.designspace.initializer;

import adf.ui.components.dialogs.undecorated.AdfDialogClosingCallback;
import adf.ui.components.dialogs.undecorated.AdfUndecoratedDialog;
import adf.ui.components.dialogs.undecorated.AdfUndecoratedDialogTitleBar;
import dsdsse.app.AppController;

import javax.swing.*;

/**
 * Created by Admin on 2/19/2017.
 */
// cleanup done
public class InitAssistantUndecoratedDialog extends AdfUndecoratedDialog {

    public InitAssistantUndecoratedDialog(JFrame mainFrame, boolean modal, boolean fadeAble,
                                          AdfUndecoratedDialogTitleBar adfUndecoratedDialogTitleBar,
                                          AdfDialogClosingCallback adfDialogClosingCallback) {
        super(mainFrame, modal, fadeAble, adfUndecoratedDialogTitleBar, adfDialogClosingCallback);
    }

    @Override
    protected boolean canTheDialogBeClosed() {
        return AppController.getInstance().onUserClickedCloseStandaloneInitAssistantButton();
    }
}
