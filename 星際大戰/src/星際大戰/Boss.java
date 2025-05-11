package 星際大戰;

import javax.swing.*;
import java.awt.*;

public class Boss {
    int x, y;
    int width, height;
    int health = 1000; // 增加生命值，需要打很久
    Image image;
    int direction = 1; // 移動方向（1: 向右，-1: 向左）

    public Boss(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y; // 固定在畫面頂部
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }
}