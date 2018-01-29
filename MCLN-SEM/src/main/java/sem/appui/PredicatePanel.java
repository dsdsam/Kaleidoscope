package sem.appui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PredicatePanel  extends JPanel {

    private static final PredicatePanel predicatePanel = new PredicatePanel();

    public static PredicatePanel getSingleton() {
        return predicatePanel;
    }

    //
    //      I n s t a n c e
    //

    private final JLabel label = new JLabel("Property state");

    private PredicatePanel(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(140, 20));
        setMinimumSize(new Dimension(140, 20));

        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xAAAAFF)) ,
                BorderFactory.createEmptyBorder(0,5,0,0));
        label.setBorder(border);
        label.setOpaque(true);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setBackground(new Color(0, 0, 200));
        label.setForeground(Color.white);

        add(label, BorderLayout.CENTER);
    }

    public void setText(String text){
        label.setText(text);
    }
}
