package 星際大戰;

public class BossLaser {
    int x, y;
    int width, height;

    /**
     * 構造新的 BOSS 超強雷射實例。
     *
     * @param x 初始 x 座標
     * @param y 初始 y 座標
     */
    public BossLaser(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 10; // 超強雷射較粗
        this.height = 20;
    }
}