package �P�ڤj��;

import javax.swing.*;
import java.awt.*;

/**
 * �C�������̲� BOSS�A�t�d�������ʨç������a�C
 * BOSS �㦳�ͩR�ȨèϥΫ��w�Ϥ��i���V�C
 */
public class Boss {
    int x, y;
    int width, height;
    int health=1000; // �ͩR�ȡA��l�� 1000�A�ݭn���a��������
    Image image;
    int direction=1; // ���ʤ�V�A1 ��ܦV�k�A-1 ��ܦV��

    /**
     * �c�y�s�� BOSS ��ҡC
     *
     * @param x ��l x �y��
     * @param y ��l y �y�С]�T�w�b�e�������^
     * @param imagePath �Ϥ��귽���|
     * @param width �Ϥ��e��
     * @param height �Ϥ�����
     */
    public Boss(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y; // �T�w�b�e������
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * ø�s BOSS �Ϥ�����w���O�C
     *
     * @param g Graphics ����A�Ω�ø��
     * @param panel �ؼ� JPanel�A�Ω�ø�s��m�Ѧ�
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }
}