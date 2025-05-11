package �P�ڤj��;

import javax.swing.*;
import java.awt.*;

public class Boss {
    int x, y;
    int width, height;
    int health = 1000; // �W�[�ͩR�ȡA�ݭn���ܤ[
    Image image;
    int direction = 1; // ���ʤ�V�]1: �V�k�A-1: �V���^

    public Boss(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y; // �T�w�b�e������
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }
}