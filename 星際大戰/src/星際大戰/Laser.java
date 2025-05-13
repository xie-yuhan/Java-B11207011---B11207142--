package 星際大戰;

/**
 * 遊戲中的雷射光束，用於玩家或敵人的攻擊。
 * 包含位置資訊，供繪製和碰撞檢測使用。
 */
public class Laser {
    int x, y; // 雷射的 x, y 座標

    /**
     * 構造新的雷射光束實例。
     *
     * @param x 雷射的初始 x 座標
     * @param y 雷射的初始 y 座標
     */
    public Laser(int x, int y) {
        this.x = x;
        this.y = y;
    }
}