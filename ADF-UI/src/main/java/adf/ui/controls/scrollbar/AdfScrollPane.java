package adf.ui.controls.scrollbar;

import javax.swing.*;
import javax.swing.plaf.ScrollPaneUI;
import java.awt.*;

/**
 * Created by u0180093 on 10/12/2016.
 */
public class AdfScrollPane extends JScrollPane {

    public AdfScrollPane() {
        super();
    }

    public AdfScrollPane(Component view) {
        super(view);
    }

    public void setUI(ScrollPaneUI ui) {

        AdfScrollPaneUI adfScrollPaneUI = new AdfScrollPaneUI();
        super.setUI(adfScrollPaneUI);
    }

    public void updateUI() {
        AdfScrollPaneUI adfScrollPaneUI = new AdfScrollPaneUI();
        setUI(adfScrollPaneUI);
    }

    @Override
    public JScrollBar createHorizontalScrollBar()
    {
        return new AdfScrollBar(Adjustable.HORIZONTAL);
    }

    @Override
    public JScrollBar createVerticalScrollBar()
    {
        return new AdfScrollBar(Adjustable.VERTICAL);
    }

}

