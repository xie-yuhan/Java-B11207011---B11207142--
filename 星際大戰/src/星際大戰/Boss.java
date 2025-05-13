package 星際大戰;

import javax.swing.*;
import java.awt.*;

/**
 * 遊戲中的最終 BOSS，負責水平移動並攻擊玩家。
 * BOSS 具有生命值並使用指定圖片進行渲染。
 */
public class Boss {
    int x, y;
    int width, height;
    int health=1000; // 生命值，初始為 1000，需要玩家長期擊敗
    Image image;
    int direction=1; // 移動方向，1 表示向右，-1 表示向左

    /**
     * 構造新的 BOSS 實例。
     *
     * @param x 初始 x 座標
     * @param y 初始 y 座標（固定在畫面頂部）
     * @param imagePath 圖片資源路徑
     * @param width 圖片寬度
     * @param height 圖片高度
     */
    public Boss(int x, int y, String imagePath, int width, int height) {
        this.x = x;
        this.y = y; // 固定在畫面頂部
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * 繪製 BOSS 圖片到指定面板。
     *
     * @param g Graphics 物件，用於繪圖
     * @param panel 目標 JPanel，用於繪製位置參考
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - width / 2, y - height / 2, panel);
    }
}