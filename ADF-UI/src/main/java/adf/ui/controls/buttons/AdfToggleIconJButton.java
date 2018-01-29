package adf.ui.controls.buttons;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Admin on 10/19/2016.
 */
public class AdfToggleIconJButton extends JToggleButton {

    private Icon defaultIcon;
    private Icon selectedIcon;
    private String defaultMenuItem;
    private String selectedMenuItem;
    private String defaultStateTooltip = "Default State Tooltip";
    private String selectedStateTooltip = "Selected State Tooltip";
    private final boolean initialState;
    private boolean selected;
    private boolean enabled;
    private ItemListener itemListener;

    ItemListener innerItemListener = (e) -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
         System.out.println("Selected "+defaultIcon.toString());
        } else {
            System.out.println("UNSelected "+defaultIcon.toString());
        }
    };

    ChangeListener changeListener = new ChangeListener(){
        @Override
        public void stateChanged(ChangeEvent e) {
        }
    };

    public AdfToggleIconJButton() {
        initialState = false;
    }

    public AdfToggleIconJButton(ImageIcon defaultImageIcon, boolean selected) {
        this(defaultImageIcon, null, "", "", selected, true, null);
    }

    public AdfToggleIconJButton(Icon defaultIcon, String defaultMenuItem, String selectedMenuItem,
                                boolean selected, ItemListener itemListener) {
        this(defaultIcon, null, defaultMenuItem, selectedMenuItem, selected, true, itemListener);
    }

    public AdfToggleIconJButton(Icon defaultIcon, Icon selectedIcon,
                                String defaultMenuItem, String selectedMenuItem,
                                boolean selected, boolean enabled, ItemListener itemListener) {
//        super(defaultIcon);
        this.defaultIcon = defaultIcon;
        this.selectedIcon = selectedIcon;
        this.defaultMenuItem = defaultMenuItem;
        this.selectedMenuItem = selectedMenuItem;
        initialState = selected;
        this.selected = selected;
        this.enabled = enabled;
        this.itemListener = itemListener;
//        setBorder(new LineBorder(Color.LIGHT_GRAY));
        init();
//        this.addChangeListener(changeListener);
     addItemListener(innerItemListener);
    }

    private void init() {
        if (selected) {
            // from selected to deselected
            setIcon(defaultIcon);
        } else {
            // from deselected to selected
            setIcon(selectedIcon);
        }
        setEnabled(enabled);
        ActionListener actionListener = (e) -> {
            System.out.println("AdfToggleIconJButton " + e.getActionCommand());
            if (itemListener != null) {
                ItemEvent itemEvent = new ItemEvent(AdfToggleIconJButton.this, 0, "",
                        !selected ? ItemEvent.SELECTED : ItemEvent.DESELECTED  );
                selected ^= true;
                itemListener.itemStateChanged(itemEvent);
                doUpdateOnStateChanged(selected);
            } else {
                selected ^= true;
                doUpdateOnStateChanged(selected);
            }
        };
        addActionListener(actionListener);

    }

    public void setSelectedIcon(Icon selectedIcon) {
        super.setSelectedIcon(selectedIcon);
        this.selectedIcon = selectedIcon;
        this.selectedStateTooltip = "Selected State Tooltip";
        doUpdateOnStateChanged(selected);
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        super.isSelected();
    }

//    @Override
//    public void setEnabled(boolean newValue) {
//        super.setEnabled(newValue);
//
//    }

    public void resetState(boolean selected ) {
        this.selected = selected;
        doUpdateOnStateChanged(selected);
    }

    public void resetState() {
        selected = initialState;
        doUpdateOnStateChanged(selected);
    }

    private void doUpdateOnStateChanged(boolean selected) {
        if (selected && selectedIcon != null) {
            setIcon(selectedIcon);
            setToolTipText(selectedStateTooltip);
        } else {
            setIcon(defaultIcon);
            setToolTipText(defaultStateTooltip);
        }
    }


    private static void testButton() {

        Dimension PREFERRED_SIZE = new Dimension(100, 22);
        JFrame frame = new JFrame("Test Controls");
//        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create and set up the content pane.
        JPanel newContentPane = new JPanel();
        newContentPane.setLayout(new GridLayout(0, 1));
        newContentPane.setOpaque(true);
        //content panes must be opaque

        newContentPane.setBorder(new EmptyBorder(10, 10, 10, 10));


        // AdfToggle3DButton
        AdfToggleIconJButton adfToggleIconJButton = new AdfToggleIconJButton();
//        toggle3DButton.setBackground(Color.GREEN);
//        toggle3DButton.setForeground(Color.DARK_GRAY);
        adfToggleIconJButton.setText("Toggle Button. State 1");
        adfToggleIconJButton.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                adfToggleIconJButton.setText("TB state: Selected");
            } else {
                adfToggleIconJButton.setText("TB state: Not Selected1");
            }
        });
//        toggle3DButton.setS.setText("Toggle Button. State 1");

        adfToggleIconJButton.setPreferredSize(PREFERRED_SIZE);
        adfToggleIconJButton.setMinimumSize(PREFERRED_SIZE);
        adfToggleIconJButton.setMaximumSize(PREFERRED_SIZE);
        newContentPane.add(adfToggleIconJButton);


        //Display the window.
//        frame.setMaximumSize(new Dimension(400, 200));
        frame.setSize(new Dimension(400, 200));
        frame.setContentPane(newContentPane);
//        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("Test Controls");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                testButton();
            }
        });
    }
}
