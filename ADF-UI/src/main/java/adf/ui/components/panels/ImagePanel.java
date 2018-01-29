package adf.ui.components.panels;

import javax.swing.JPanel;
import javax.swing.Icon;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ImagePanel extends JPanel {

    private Image image;
    private int imgWidth;
    private int imgHeight;

    /**
     * 
     *
     */
    public ImagePanel() {
        this( null );
    }
    
    /**
     * 
     * @param imageIcon
     */
    public ImagePanel( Icon imageIcon ) {
        setImage( imageIcon );
        setOpaque( true);
    }

    /**
     * 
     * @param imageIcon
     */
    public void setImage( Icon imageIcon ) {
        if ( imageIcon == null ){
            return;
        }
        this.image = ((ImageIcon)imageIcon).getImage();
        imgWidth = image.getWidth(null);
        imgHeight = image.getHeight(null);
    }

    public void paintComponent( Graphics g ) {

        int width  = getSize().width;
        int height = getSize().height;

        if ( image == null ){
            g.clearRect( 0, 0, width, height );
            g.setColor( getBackground() );
            g.fillRect( 0, 0, width, height );
            return;
        }

        int x = 0;
        int y = 0;

        while( y < height ){
            x = 0;
            while( x < width ){
                g.drawImage( image, x, y, this );
                x += imgWidth;
            }
            y += imgHeight;
        }
    }

    public static void main(String[] args) {
        ImagePanel imagePanel1 = new ImagePanel();
    }

}