package mclnmatrix.view;

import adf.ui.components.panels.ImagePanel;
import adf.utils.BuildUtils;

import javax.swing.*;
import java.awt.*;

public class ScrollableMclnViewHolder extends ImagePanel {

    private static final String IMAGES_LOCATION_CLASSPATH = "/mclnmatrix-resources/images/";

    private JScrollPane scrollPane;

    public ScrollableMclnViewHolder() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.BLUE);

        setOpaque(false);
        setBorder(null);

        String image01 = "bg-page.jpg";
        String image02 = "canvas.jpg";
        String image03 = "line-bg.png";
        String image04 = "paint-bg.jpg";
        String image05 = "bg-3.jpg";
        String imageClassPath = IMAGES_LOCATION_CLASSPATH + image05;
        ImageIcon backgroundImage = BuildUtils.getImageIcon(imageClassPath);
        setImage(backgroundImage);
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
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panel.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }

}
