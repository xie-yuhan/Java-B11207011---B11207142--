package �P�ڤj��;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Earth {
    private int x, y;
    private int width, height;
    private Image image;

    public Earth(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        // �T�O�e�צܤ֬� 1�A���� getScaledInstance ���~
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        Image tempImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        if (tempImage == null) {
            System.err.println("Failed to load image from resource: " + imagePath);
            // ���ըϥε�����|�[��
            try {
                tempImage = new ImageIcon(new File("C:\\Users\\yuhan\\OneDrive\\Documents\\GitHub\\Java-B11207011-Xie-Yuhan-B11207142-Xu-Yuming\\�P�ڤj��\\src\\�P�ڤj��\\earth.jpg").toURI().toURL()).getImage();
                System.out.println("Loaded image from absolute path: C:\\Users\\yuhan\\OneDrive\\Documents\\GitHub\\Java-B11207011-Xie-Yuhan-B11207142-Xu-Yuming\\�P�ڤj��\\src\\�P�ڤj��\\earth.jpg");
            } catch (Exception e) {
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

    public Image getImage() {
        return image;
    }

    public void draw(Graphics g, JPanel panel) {
        // �T�Oø�s�ɨϥΥ��T�������I
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}