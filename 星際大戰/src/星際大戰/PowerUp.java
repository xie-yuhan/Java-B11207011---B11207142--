package 星際大戰;

import javax.swing.*;
import java.awt.*;

public class PowerUp {
    public int x, y;
    private int width, height;
    private Image image;

    public PowerUp(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }
}