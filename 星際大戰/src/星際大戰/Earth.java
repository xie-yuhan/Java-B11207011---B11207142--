package 星際大戰;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 遊戲中的地球，負責顯示地球圖片並提供碰撞檢測。
 * 地球具有獨立的繪製座標和碰撞圓心座標，並支援圖片載入與錯誤處理。
 */
public class Earth {
    private int x, y; // 圖片繪製的中心 X, Y 座標
    private int collisionX, collisionY; // 碰撞圓心的 X, Y 座標
    private int width, height;
    private int radius; // 圓形半徑
    private Image image;

    /**
     * 構造新的地球實例。
     *
     * @param x 圖片繪製的中心 X 座標
     * @param y 圖片繪製的中心 Y 座標
     * @param collisionX 碰撞圓心的 X 座標
     * @param collisionY 碰撞圓心的 Y 座標
     * @param imagePath 地球圖片資源路徑
     * @param width 圖片寬度（最小為 1）
     * @param height 圖片高度（最小為 1）
     */
    public Earth(int x, int y, int collisionX, int collisionY, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        this.collisionX = collisionX;
        this.collisionY = collisionY;
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

    /**
     * 獲取地球的圖片。
     *
     * @return 地球的圖片物件
     */
    public Image getImage() {
        return image;
    }

    /**
     * 繪製地球圖片到指定面板。
     *
     * @param g Graphics 物件，用於繪圖
     * @param panel 目標 JPanel，用於繪製位置參考
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }

    /**
     * 獲取地球的邊界矩形，用於碰撞檢測。
     *
     * @return 地球的邊界矩形
     */
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    /**
     * 獲取圖片的 X 座標。
     *
     * @return 圖片的中心 X 座標
     */
    public int getX() {
        return x;
    }

    /**
     * 獲取圖片的 Y 座標。
     *
     * @return 圖片的中心 Y 座標
     */
    public int getY() {
        return y;
    }

    /**
     * 獲取碰撞圓心的 X 座標。
     *
     * @return 碰撞圓心的 X 座標
     */
    public int getCollisionX() {
        return collisionX;
    }

    /**
     * 獲取碰撞圓心的 Y 座標。
     *
     * @return 碰撞圓心的 Y 座標
     */
    public int getCollisionY() {
        return collisionY;
    }

    /**
     * 獲取圖片的寬度。
     *
     * @return 圖片寬度
     */
    public int getWidth() {
        return width;
    }

    /**
     * 獲取圖片的高度。
     *
     * @return 圖片高度
     */
    public int getHeight() {
        return height;
    }

    /**
     * 獲取碰撞圓形的半徑。
     *
     * @return 碰撞圓形半徑
     */
    public int getRadius() {
        return radius;
    }
}