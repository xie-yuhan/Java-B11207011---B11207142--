package 星際大戰;

import javax.swing.*;
import java.awt.*;

public class Enemy {
    int x, y;
    Image image;
    int width, height;

    public Enemy(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x, y, panel);
    }
}