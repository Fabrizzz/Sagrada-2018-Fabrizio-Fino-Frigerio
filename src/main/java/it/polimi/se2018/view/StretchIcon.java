package it.polimi.se2018.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

public class StretchIcon extends ImageIcon {

    public StretchIcon(String filename) {
        super(filename);
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        Image image = getImage();
        if (image == null) {
            return;
        }

        Insets insets = ((Container) c).getInsets();
        int w = c.getWidth() - insets.left - insets.right;
        int h = c.getHeight() - insets.top - insets.bottom;

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
