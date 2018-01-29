package adf.menu;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;


public class Button
{
    public static void main(String[] args){
        JFrame frame = new JFrame();
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Foo");

        ButtonGroup menuGrp = new ButtonGroup();
        JRadioButtonMenuItem itemA = new JRadioButtonMenuItem();
        JRadioButtonMenuItem itemB = new JRadioButtonMenuItem();
        JRadioButtonMenuItem itemC = new JRadioButtonMenuItem();
        menuGrp.add(itemA);
        menuGrp.add(itemB);
        menuGrp.add(itemC);
        menu.add(itemA);
        menu.add(itemB);
        menu.add(itemC);
        itemA.setSelected(true);

        bar.add(menu);
        frame.setJMenuBar(bar);

        JPanel content = new JPanel();

        ButtonGroup toolbarGrp = new ButtonGroup();
        JToggleButton buttonA = new JToggleButton();
        JToggleButton buttonB = new JToggleButton();
        JToggleButton buttonC = new JToggleButton();
        toolbarGrp.add(buttonA);
        toolbarGrp.add(buttonB);
        toolbarGrp.add(buttonC);
        buttonA.setSelected(true);

        content.add(buttonA);
        content.add(buttonB);
        content.add(buttonC);

        itemA.setAction(new MyAction(buttonA));
        itemB.setAction(new MyAction(buttonB));
        itemC.setAction(new MyAction(buttonC));
        buttonA.setAction(new MyAction(itemA));
        buttonB.setAction(new MyAction(itemB));
        buttonC.setAction(new MyAction(itemC));
        frame.setContentPane(content);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    static class MyAction extends AbstractAction
    {
        AbstractButton button;

        public MyAction(AbstractButton button){
            this.button = button;
        }

        @Override public void actionPerformed(ActionEvent e){
            button.setSelected(true);
        }


    }
}
