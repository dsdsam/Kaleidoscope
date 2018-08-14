package mclnmatrix.view;

import javax.swing.*;
import java.awt.*;

public class ScrollableMclnViewHolder extends JPanel {

    private JScrollPane scrollPane;

    public ScrollableMclnViewHolder() {
        super(new BorderLayout());

    }

    public void initContent(JPanel panel) {
        removeAll();
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(panel);
        scrollPane.getViewport().setPreferredSize(panel.getPreferredSize());
        scrollPane.getViewport().setMinimumSize(panel.getMinimumSize());
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

}
