package adf.menu;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 1/15/2017.
 */
public class AdfMenuItem extends JMenuItem {

    public static AdfMenuItem getInstance(String label, ActionListener menuActionListener) {
        return new AdfMenuItem(label, menuActionListener);
    }

    public static AdfMenuItem getInstance(String label, AbstractAction menuAction) {
        return new AdfMenuItem(label, menuAction);
    }

    private String label;
    private AbstractAction menuAction;
    private List<JMenuItem> subMenu = new ArrayList();

    public AdfMenuItem(String label) {
        this(label, null);
    }

    public AdfMenuItem(String label, ActionListener menuActionListener) {
        super(label);
        this.label = label;
    }
}

