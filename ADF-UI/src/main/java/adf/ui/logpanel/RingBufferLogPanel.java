package adf.ui.logpanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/5/14
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class RingBufferLogPanel extends JPanel {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private JScrollPane scrollPane;
    private JScrollBar  scrollBar;
    private JTextArea   textArea;

    public RingBufferLogPanel() {
        initialize();
    }

    private void initialize() {
        setBorder( BorderFactory.createMatteBorder( 1,0,1,0, Color.gray ));
//    setBorder( null );
        setLayout( new BorderLayout() );

        scrollPane = new JScrollPane();
        scrollPane.setBorder( null );
        scrollPane.setBorder(BorderFactory.createMatteBorder( 3,0,3,0, Color.lightGray ));
        scrollBar = scrollPane.getVerticalScrollBar();
//    scrollPane.setBorder( BorderFactory.createMatteBorder( 3,3,3,3, Color.lightGray ));

//    JPanel textPanel = new JPanel();
        textArea = new JTextArea("Please use Menu for to load an example\n");
        textArea.append( "and execute the automaton. \n\n\n");
        textArea.setBorder( BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder( 0,3,0,3, Color.lightGray ),
                BorderFactory.createEmptyBorder( 5,5,5,5 ) ));
        textArea.setEditable(false);
        textArea.setFocusable(false);
//    textArea.setBackground(new Color(88,128,149));
        textArea.setBackground( Color.black );
//    textArea.setBackground(new Color(25,49,64));
        textArea.setForeground(Color.white);
        scrollPane.getViewport().add(textArea);
        add( scrollPane, BorderLayout.CENTER );
    }


    public void updateRepresentation(){


        scrollBar.setValue(scrollBar.getMaximum()+10000);
//    String newData = new String(arrayList.toArray());
        System.out.println("updateRepresentation: "+scrollBar.getValue());
        System.out.println("updateRepresentation: "+scrollBar.getMaximum());
//    textArea.append( newData );
    }

    public void cleanView(){
        textArea.setText("");
    }
}
