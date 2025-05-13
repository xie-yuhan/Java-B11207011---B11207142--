package �P�ڤj��;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * �C�������a�y�A�t�d��ܦa�y�Ϥ��ô��ѸI���˴��C
 * �a�y�㦳�W�ߪ�ø�s�y�ЩM�I����߮y�СA�ä䴩�Ϥ����J�P���~�B�z�C
 */
public class Earth {
    private int x, y; // �Ϥ�ø�s������ X, Y �y��
    private int collisionX, collisionY; // �I����ߪ� X, Y �y��
    private int width, height;
    private int radius; // ��Υb�|
    private Image image;

    /**
     * �c�y�s���a�y��ҡC
     *
     * @param x �Ϥ�ø�s������ X �y��
     * @param y �Ϥ�ø�s������ Y �y��
     * @param collisionX �I����ߪ� X �y��
     * @param collisionY �I����ߪ� Y �y��
     * @param imagePath �a�y�Ϥ��귽���|
     * @param width �Ϥ��e�ס]�̤p�� 1�^
     * @param height �Ϥ����ס]�̤p�� 1�^
     */
    public Earth(int x, int y, int collisionX, int collisionY, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        this.collisionX = collisionX;
        this.collisionY = collisionY;
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.radius = 800; // �b�|�� 800
        Image tempImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        if (tempImage == null) {
            System.err.println("Failed to load image from resource: " + imagePath);
            try {
                tempImage = new ImageIcon(new File("C:\\Users\\yuhan\\OneDrive\\Documents\\GitHub\\Java-B11207011-Xie-Yuhan-B11207142-Xu-Yuming\\�P�ڤj��\\src\\�P�ڤj��\\earth.jpg").toURI().toURL()).getImage();
                System.out.println("\\�P�ڤj��\\earth.jpg");
            } 
            catch (Exception e) {
                System.err.println("Failed to load image from absolute path: " + e.getMessage());
            }
        }
        if (tempImage == null) {
            System.err.println("Image loading failed, using default blank image.");
            tempImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) tempImage.getGraphics();
            g2d.setColor(Color.BLUE);
            g2d.fillRect(0, 0, this.width, this.height);
            g2d.dispose();
        }
        this.image = tempImage.getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
    }

    /**
     * ����a�y���Ϥ��C
     *
     * @return �a�y���Ϥ�����
     */
    public Image getImage() {
        return image;
    }

    /**
     * ø�s�a�y�Ϥ�����w���O�C
     *
     * @param g Graphics ����A�Ω�ø��
     * @param panel �ؼ� JPanel�A�Ω�ø�s��m�Ѧ�
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }

    /**
     * ����a�y����ɯx�ΡA�Ω�I���˴��C
     *
     * @return �a�y����ɯx��
     */
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    /**
     * ����Ϥ��� X �y�СC
     *
     * @return �Ϥ������� X �y��
     */
    public int getX() {
        return x;
    }

    /**
     * ����Ϥ��� Y �y�СC
     *
     * @return �Ϥ������� Y �y��
     */
    public int getY() {
        return y;
    }

    /**
     * ����I����ߪ� X �y�СC
     *
     * @return �I����ߪ� X �y��
     */
    public int getCollisionX() {
        return collisionX;
    }

    /**
     * ����I����ߪ� Y �y�СC
     *
     * @return �I����ߪ� Y �y��
     */
    public int getCollisionY() {
        return collisionY;
    }

    /**
     * ����Ϥ����e�סC
     *
     * @return �Ϥ��e��
     */
    public int getWidth() {
        return width;
    }

    /**
     * ����Ϥ������סC
     *
     * @return �Ϥ�����
     */
    public int getHeight() {
        return height;
    }

    /**
     * ����I����Ϊ��b�|�C
     *
     * @return �I����Υb�|
     */
    public int getRadius() {
        return radius;
    }
}