package 星際大戰;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Earth {
    private int x, y; // 圖片繪製的中心 X, Y 座標
    private int collisionX, collisionY; // 碰撞圓心的 X, Y 座標
    private int width, height;
    private int radius; // 圓形半徑
    private Image image;

    public Earth(int x, int y, int collisionX, int collisionY, String imagePath, int width, int height) {
        this.x = x; // 圖片繪製的 X 座標
        this.y = y; // 圖片繪製的 Y 座標
        this.collisionX = collisionX; // 碰撞圓心的 X 座標
        this.collisionY = collisionY; // 碰撞圓心的 Y 座標
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.radius = 800; // 半徑為 800
        Image tempImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        if (tempImage == null) {
            System.err.println("Failed to load image from resource: " + imagePath);
            try {
                tempImage = new ImageIcon(new File("C:\\Users\\yuhan\\OneDrive\\Documents\\GitHub\\Java-B11207011-Xie-Yuhan-B11207142-Xu-Yuming\\星際大戰\\src\\星際大戰\\earth.jpg").toURI().toURL()).getImage();
                System.out.println("\\星際大戰\\earth.jpg");
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

    public Image getImage() {
        return image;
    }

    public void draw(Graphics g, JPanel panel) {
        // 繪製時使用圖片繪製的 X, Y 座標
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    public int getX() { return x; } // 返回圖片的 X 座標
    public int getY() { return y; } // 返回圖片的 Y 座標
    public int getCollisionX() { return collisionX; } // 返回碰撞圓心的 X 座標
    public int getCollisionY() { return collisionY; } // 返回碰撞圓心的 Y 座標
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getRadius() { return radius; }
}