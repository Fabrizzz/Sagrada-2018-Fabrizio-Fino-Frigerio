package it.polimi.se2018.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.ImageObserver;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * Class that fit images into JLabel
 */
public class StretchIcon extends ImageIcon {

    private static final Logger LOGGER = Logger.getLogger("Logger");

    /**
     * Costructor
     * @param filename
     */
    public StretchIcon(String filename) {
        super(filename);
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        Image image = getImage();
        if (image == null) {
            LOGGER.log(Level.FINE,"Immagine carta null");
            return;
        }


        Insets insets = ((Container) c).getInsets();
        int w = c.getWidth() - insets.left - insets.right;
        int h = c.getHeight() - insets.top - insets.bottom;

        int iw = image.getWidth(c);
        int ih = image.getHeight(c);

        if (iw * h < ih * w) {
            iw = (h * iw) / ih;
            x += (w - iw) / 2;
            w = iw;
        } else {
            ih = (w * ih) / iw;
            y += (h - ih) / 2;
            h = ih;
        }

        ImageObserver io = getImageObserver();
        g.drawImage(image, insets.left, insets.top, w, h, io == null ? c : io);
    }

    @Override
    public int getIconWidth() {
        return 0;
    }
    @Override
    public int getIconHeight() {
        return 0;
    }
}
