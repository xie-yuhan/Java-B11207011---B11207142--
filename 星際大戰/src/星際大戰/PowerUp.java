package 星際大戰;

import javax.swing.*;
import java.awt.*;

/**
 * 遊戲中的道具，提升玩家能力（如增加雷射數量）。
 * 具備位置、尺寸和圖片渲染功能，支援碰撞檢測。
 */
public class PowerUp {
    public int x, y; // 道具的中心 x, y 座標
    private int width, height; // 道具的寬度和高度
    private Image image; // 道具的圖片

    /**
     * 構造新的道具實例。
     *
     * @param x 道具的初始 x 座標
     * @param y 道具的初始 y 座標
     * @param imagePath 道具圖片資源路徑
     * @param width 道具圖片寬度
     * @param height 道具圖片高度
     */
    public PowerUp(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * 繪製道具圖片到指定面板。
     *
     * @param g Graphics 物件，用於繪圖
     * @param panel 目標 JPanel，用於繪製位置參考
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }

    /**
     * 獲取道具的邊界矩形，用於碰撞檢測。
     *
     * @return 道具的邊界矩形
     */
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }
}