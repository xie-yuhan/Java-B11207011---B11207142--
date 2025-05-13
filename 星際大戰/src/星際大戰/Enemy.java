package 星際大戰;

import javax.swing.*;
import java.awt.*;

/**
 * 遊戲中的敵人角色，負責移動並可能發射光束攻擊玩家。
 * 敵人具有圖片渲染和發射光束的計時功能。
 */
public class Enemy {
    int x, y;
    int width, height;
    Image image;
    long lastShootTime; // 敵人發射光束的計時器，記錄上次發射時間

    /**
     * 構造新的敵人實例。
     *
     * @param x 初始 x 座標
     * @param y 初始 y 座標
     * @param imagePath 敵人圖片資源路徑
     * @param width 圖片寬度
     * @param height 圖片高度
     */
    public Enemy(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(Enemy.class.getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * 繪製敵人圖片到指定面板。
     *
     * @param g Graphics 物件，用於繪圖
     * @param panel 目標 JPanel，用於繪製位置參考
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }
}