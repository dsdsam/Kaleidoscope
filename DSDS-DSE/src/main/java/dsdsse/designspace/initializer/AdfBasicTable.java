package dsdsse.designspace.initializer;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Admin on 4/20/2016.
 */
public class AdfBasicTable extends JTable {

    protected TableKeyListener tableKeyListener = new TableKeyListener(this);


    class TableKeyListener implements KeyListener {
        JTable table = null;

        public TableKeyListener(JTable table) {
            this.table = table;
        }

        public void keyPressed(KeyEvent evt) {
            System.out.println(evt.getKeyCode() +"  "+ evt.getKeyChar());
            int i = evt.getModifiers();
            if (evt.getKeyCode() == KeyEvent.VK_V) {

            }
            if (evt.getKeyCode() == KeyEvent.VK_A && ((i & InputEvent.META_MASK) == InputEvent.META_MASK)) {
                selectAll();
            }
        }

        public void keyTyped(KeyEvent evt) {
        }

        public void keyReleased(KeyEvent evt) {
        }


    }

    AdfBasicTable(TableModel tableModel){
        super(tableModel);
    }
}
