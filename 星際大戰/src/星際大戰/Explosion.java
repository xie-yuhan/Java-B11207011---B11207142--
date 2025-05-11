package 星際大戰;

import javax.swing.*;
import java.awt.*;

public class Explosion {
    public int x, y;
    public long startTime;
    private static final int DURATION = 500; // milliseconds
    private Image image;

    public Explosion(int x, int y, String imagePath) {
        this.x = x;
        this.y = y;
        this.startTime = System.currentTimeMillis();
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startTime >= DURATION;
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - 30, y - 30, panel);
    }
}