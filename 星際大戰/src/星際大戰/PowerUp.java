package �P�ڤj��;

import javax.swing.*;
import java.awt.*;

/**
 * �C�������D��A���ɪ��a��O�]�p�W�[�p�g�ƶq�^�C
 * ��Ʀ�m�B�ؤo�M�Ϥ���V�\��A�䴩�I���˴��C
 */
public class PowerUp {
    public int x, y; // �D�㪺���� x, y �y��
    private int width, height; // �D�㪺�e�שM����
    private Image image; // �D�㪺�Ϥ�

    /**
     * �c�y�s���D���ҡC
     *
     * @param x �D�㪺��l x �y��
     * @param y �D�㪺��l y �y��
     * @param imagePath �D��Ϥ��귽���|
     * @param width �D��Ϥ��e��
     * @param height �D��Ϥ�����
     */
    public PowerUp(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * ø�s�D��Ϥ�����w���O�C
     *
     * @param g Graphics ����A�Ω�ø��
     * @param panel �ؼ� JPanel�A�Ω�ø�s��m�Ѧ�
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }

    /**
     * ����D�㪺��ɯx�ΡA�Ω�I���˴��C
     *
     * @return �D�㪺��ɯx��
     */
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }
}