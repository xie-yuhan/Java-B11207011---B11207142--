package 星際大戰;

import javax.swing.*;
import java.awt.*;

public class Enemy {
    int x, y;
    int width, height;
    Image image;
    long lastShootTime = 0; // 敵人發射光束的計時器

    public Enemy(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(Enemy.class.getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }
}