package adf.ui.components.dialogs.undecorated;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

/**
 * Created by Admin on 4/1/2016.
 */
public class AdfDialogClosingCallback extends WindowAdapter {

    private static final Logger logger = Logger.getLogger(AdfDialogClosingCallback.class.getName());

    private AdfUndecoratedDialog adfUndecoratedDialog;

    void setAdfUndecoratedDialog(AdfUndecoratedDialog adfUndecoratedDialog) {
        this.adfUndecoratedDialog = adfUndecoratedDialog;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (adfUndecoratedDialog != null) {
            adfUndecoratedDialog = null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\n");
        stringBuilder.append("************************************************************\n");
        stringBuilder.append("           Adf Undecorated Dialog   C l o s e d            *\n");
        stringBuilder.append("************************************************************");
        logger.severe(stringBuilder.toString());
    }
}

