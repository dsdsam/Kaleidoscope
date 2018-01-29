package adf.ui.controls.scrollbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class AdfScrollPaneUI extends BasicScrollPaneUI
{
    @Override
    public void installUI(final JComponent c)
    {
        super.installUI(c);

        c.setOpaque(false);
        ((JScrollPane) c).getViewport().setOpaque(false);
        c.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    /**
     * Create the UI
     */
    public static ComponentUI createUI(final JComponent c)
    {
        return new AdfScrollPaneUI();
    }
}

