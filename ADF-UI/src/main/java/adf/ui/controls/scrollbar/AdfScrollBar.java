package adf.ui.controls.scrollbar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class AdfScrollBar extends JScrollBar
{
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    public AdfScrollBar(final int orientation, final int value, final int extent, final int min, final int max)
    {
        super(orientation, value, extent, min, max);
        this.setCursor(HAND_CURSOR);
    }

    public AdfScrollBar(final int orientation)
    {
        super(orientation);
        this.setCursor(HAND_CURSOR);
    }

    public AdfScrollBar()
    {
        super();
        this.setCursor(HAND_CURSOR);
    }

    @Override
    public void updateUI()
    {
        this.setUI(AdfScrollBarUI.createUI(this));
    }
}

