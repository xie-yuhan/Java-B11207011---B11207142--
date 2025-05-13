package �P�ڤj��;

import javax.swing.*;
import java.awt.*;

/**
 * �C�������ĤH����A�t�d���ʨåi��o�g�����������a�C
 * �ĤH�㦳�Ϥ���V�M�o�g�������p�ɥ\��C
 */
public class Enemy {
    int x, y;
    int width, height;
    Image image;
    long lastShootTime; // �ĤH�o�g�������p�ɾ��A�O���W���o�g�ɶ�

    /**
     * �c�y�s���ĤH��ҡC
     *
     * @param x ��l x �y��
     * @param y ��l y �y��
     * @param imagePath �ĤH�Ϥ��귽���|
     * @param width �Ϥ��e��
     * @param height �Ϥ�����
     */
    public Enemy(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(Enemy.class.getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * ø�s�ĤH�Ϥ�����w���O�C
     *
     * @param g Graphics ����A�Ω�ø��
     * @param panel �ؼ� JPanel�A�Ω�ø�s��m�Ѧ�
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }
}