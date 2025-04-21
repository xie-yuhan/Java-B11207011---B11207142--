package 星際大戰;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, Runnable {
    private int playerX = 400, playerY = 500; // 玩家飛船位置
    private int playerSpeed = 10; // 玩家移動速度
    private ArrayList<Enemy> enemies; // 敵人列表
    private ArrayList<Laser> lasers; // 雷射列表
    private int score = 0; // 分數
    private int level = 1; // 關卡
    private int health = 100; // 生命值（初始100）
    private boolean running = true;
    private boolean canShoot = true; // 控制射擊冷卻
    private long lastShootTime = 0; // 上次射擊的時間
    private final long shootCooldown = 200; // 射擊冷卻時間（毫秒）

    public GamePanel() {
        enemies = new ArrayList<>();
        lasers = new ArrayList<>();
        setFocusable(true);
        addKeyListener(this);
        new Thread(this).start(); // 啟動遊戲主循環
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK); // 背景設為黑色

        // 畫星星
        g.setColor(Color.WHITE);
        for (int i = 0; i < 100; i++) {
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            g.fillRect(x, y, 2, 2);
        }

        // 畫玩家飛船 (簡單用矩形表示TIE戰鬥機)
        g.setColor(Color.GRAY);
        g.fillRect(playerX - 20, playerY - 10, 40, 20); // 機身
        g.fillRect(playerX - 40, playerY, 20, 10); // 左翼
        g.fillRect(playerX + 20, playerY, 20, 10); // 右翼

        // 畫敵人
        g.setColor(Color.RED);
        for (Enemy enemy : enemies) {
            g.fillRect(enemy.x, enemy.y, 20, 20);
        }

        // 畫雷射
        g.setColor(Color.GREEN);
        for (Laser laser : lasers) {
            g.fillRect(laser.x, laser.y, 4, 10);
        }

        // 畫分數和關卡
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + score, 10, 20);
        g.drawString("LEVEL: " + level, 10, 40);

        // 畫生命值條
        g.setColor(Color.GREEN);
        g.fillRect(playerX - 20, playerY + 20, health / 2, 5); // 生命值條（根據health縮放）
        g.setColor(Color.RED);
        g.fillRect(playerX - 20 + health / 2, playerY + 20, (100 - health) / 2, 5); // 損失的生命值
    }

    @Override
    public void run() {
        while (running) {
            // 生成敵人
            if (Math.random() < 0.02) {
                enemies.add(new Enemy((int) (Math.random() * getWidth()), 0));
            }

            // 更新敵人位置
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);
                enemy.y += 3; // 敵人向下移動
                if (enemy.y > getHeight()) {
                    enemies.remove(i); // 移除超出螢幕的敵人
                } else {
                    // 簡單碰撞檢測（玩家與敵人）
                    if (Math.abs(enemy.x - playerX) < 30 && Math.abs(enemy.y - playerY) < 30) {
                        health -= 20; // 每次碰撞減少20生命值
                        enemies.remove(i); // 敵人消失
                        if (health <= 0) {
                            running = false; // 生命值為0，遊戲結束
                        }
                    }
                }
            }

            // 更新雷射位置
            for (int i = lasers.size() - 1; i >= 0; i--) {
                Laser laser = lasers.get(i);
                laser.y -= 5; // 雷射向上移動
                if (laser.y < 0) {
                    lasers.remove(i); // 移除超出螢幕的雷射
                } else {
                    // 雷射與敵人碰撞檢測
                    for (int j = enemies.size() - 1; j >= 0; j--) {
                        Enemy enemy = enemies.get(j);
                        if (Math.abs(laser.x - enemy.x) < 15 && Math.abs(laser.y - enemy.y) < 15) {
                            enemies.remove(j); // 敵人被擊中消失
                            lasers.remove(i); // 雷射消失
                            score += 10; // 擊敗敵人加10分
                            break;
                        }
                    }
                }
            }

            repaint();
            try {
                Thread.sleep(16); // 約60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
        System.exit(0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && playerX > 40) {
            playerX -= playerSpeed;
        }
        if (key == KeyEvent.VK_RIGHT && playerX < getWidth() - 40) {
            playerX += playerSpeed;
        }
        if (key == KeyEvent.VK_SPACE) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShootTime >= shootCooldown) {
                lasers.add(new Laser(playerX, playerY - 10)); // 按空格鍵發射雷射
                lastShootTime = currentTime;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}