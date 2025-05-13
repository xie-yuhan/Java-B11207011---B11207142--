package �P�ڤj��;

import javax.swing.*;
import java.awt.*;

/**
 * �C�������z���S�ġA�t�d����z���ʵe�ú޲z����ɶ��C
 * �z���b���w��m��ܡA���� 500 �@���۰ʮ����C
 */
public class Explosion {
    public int x, y; // �z�������� x, y �y��
    public long startTime; // �z���}�l���ɶ��W
    private static final int DURATION = 500; // �@��A�z������ɶ�
    private Image image; // �z�����Ϥ�

    /**
     * �c�y�s���z���S�Ĺ�ҡC
     *
     * @param x �z�������� x �y��
     * @param y �z�������� y �y��
     * @param imagePath �z���Ϥ��귽���|
     */
    public Explosion(int x, int y, String imagePath) {
        this.x = x;
        this.y = y;
        this.startTime = System.currentTimeMillis();
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
    }

    /**
     * �ˬd�z���O�_�w�W�L����ɶ��C
     *
     * @return true �p�G�z���w�L���]�W�L 500 �@��^�A�_�h false
     */
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime >= DURATION;
    }

    /**
     * ø�s�z���Ϥ�����w���O�C
     *
     * @param g Graphics ����A�Ω�ø��
     * @param panel �ؼ� JPanel�A�Ω�ø�s��m�Ѧ�
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - 30, y - 30, panel);
    }
}