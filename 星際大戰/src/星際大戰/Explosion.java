package 星際大戰;

import javax.swing.*;
import java.awt.*;

/**
 * 遊戲中的爆炸特效，負責顯示爆炸動畫並管理持續時間。
 * 爆炸在指定位置顯示，持續 500 毫秒後自動消失。
 */
public class Explosion {
    public int x, y; // 爆炸的中心 x, y 座標
    public long startTime; // 爆炸開始的時間戳
    private static final int DURATION = 500; // 毫秒，爆炸持續時間
    private Image image; // 爆炸的圖片

    /**
     * 構造新的爆炸特效實例。
     *
     * @param x 爆炸的中心 x 座標
     * @param y 爆炸的中心 y 座標
     * @param imagePath 爆炸圖片資源路徑
     */
    public Explosion(int x, int y, String imagePath) {
        this.x = x;
        this.y = y;
        this.startTime = System.currentTimeMillis();
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
    }

    /**
     * 檢查爆炸是否已超過持續時間。
     *
     * @return true 如果爆炸已過期（超過 500 毫秒），否則 false
     */
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime >= DURATION;
    }

    /**
     * 繪製爆炸圖片到指定面板。
     *
     * @param g Graphics 物件，用於繪圖
     * @param panel 目標 JPanel，用於繪製位置參考
     */
    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x - 30, y - 30, panel);
    }
}