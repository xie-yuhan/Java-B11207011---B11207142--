package 星際大戰;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements MouseMotionListener, MouseListener, Runnable {
    private int playerX = 400, playerY = 400; // 玩家飛船位置（中心點，向上調整）
    private Image playerImage;
    private Image backgroundImage; // 星空背景圖片
    private ArrayList<Enemy> enemies; // 普通敵人列表
    private ArrayList<Laser> lasers; // 雷射列表
    private ArrayList<PowerUp> powerUps; // 道具列表
    private Earth earth; // 地球對象
    private int score = 0; // 分數
    private int level = 1; // 關卡
    private int health = 100; // 玩家生命值（初始100）
    private int earthHealth = 500; // 地球生命值（初始500）
    private int laserCount = 1; // 初始一發雷射
    private boolean running = true;
    private boolean isShooting = false; // 追蹤是否正在射擊
    private boolean poweredUp = false; // 檢查是否獲得道具強化
    private long lastShootTime = 0; // 上次射擊的時間
    private long powerUpEndTime = 0; 
    private final long shootCooldown = 200; // 射擊冷卻時間（毫秒）
    private int playerWidth, playerHeight; // 玩家飛船圖片的寬高

    public GamePanel() {
        enemies = new ArrayList<>();
        lasers = new ArrayList<>();
        powerUps = new ArrayList<>();
        // 加載玩家飛船圖片並設置尺寸
        playerImage = new ImageIcon(getClass().getResource("/星際大戰/player.jpg")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        // 加載調整後的星空背景圖片
        backgroundImage = new ImageIcon(getClass().getResource("/星際大戰/stars.jpg")).getImage();
        if (backgroundImage == null) {
            System.err.println("Failed to load background image: /星際大戰/stars.jpg");
        }
        playerWidth = 60; // 圖片寬度
        playerHeight = 60; // 圖片高度
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);
        new Thread(this).start(); // 啟動遊戲主循環
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // 不再此處初始化 earth，因為尺寸可能尚未準備好
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 繪製星空背景作為最底層，只覆蓋地球上方的區域
        if (backgroundImage != null && earth != null) {
            int earthY = earth.getY() - earth.getHeight() / 2; // 地球的上邊界
            g.drawImage(backgroundImage, 0, 0, getWidth(), earthY, this); // 只繪製到地球的上半部分
        } else {
            g.setColor(Color.BLACK); // 備用黑色背景
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // 確保 earth 已初始化，並且尺寸有效
        if (earth == null && getWidth() > 0 && getHeight() > 0) {
            earth = new Earth(getWidth() / 2, getHeight() - 100, "/星際大戰/earth.jpg", getWidth(), 200);
        }

        // 繪製地球
        if (earth != null) {
            earth.draw(g, this);
            g.setColor(Color.RED);
            g.drawRect(earth.getX() - earth.getWidth() / 2, earth.getY() - earth.getHeight() / 2, earth.getWidth(), earth.getHeight());
        }

        // 繪製玩家飛船（確保在地球之上）
        g.drawImage(playerImage, playerX - playerWidth / 2, playerY - playerHeight / 2, this);

        // 繪製普通敵人
        for (Enemy enemy : enemies) {
            enemy.draw(g, this);
        }

        // 繪製雷射
        g.setColor(Color.GREEN);
        for (Laser laser : lasers) {
            g.fillRect(laser.x, laser.y, 4, 10);
        }

        // 繪製道具
        for (PowerUp pu : powerUps) {
            pu.draw(g, this);
        }

        // 繪製分數和關卡
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + score, 10, 20);
        g.drawString("LEVEL: " + level, 10, 40);

        // 繪製道具強化倒計時
        if (poweredUp) {
            long remaining = (powerUpEndTime - System.currentTimeMillis()) / 1000;
            g.drawString("POWER-UP: " + remaining + "s", 10, 60);
        }

        // 繪製玩家生命值條（跟隨飛船中心點）
        g.setColor(Color.GREEN);
        g.fillRect(playerX - 20, playerY + 30, health / 2, 5);
        g.setColor(Color.RED);
        g.fillRect(playerX - 20 + health / 2, playerY + 30, (100 - health) / 2, 5);

        // 繪製地球生命值條（顯示在畫面頂部）
        g.setColor(Color.BLUE);
        g.fillRect(10, 10, earthHealth / 5, 10);
        g.setColor(Color.RED);
        g.fillRect(10 + earthHealth / 5, 10, (500 - earthHealth) / 5, 10);
    }

    @Override
    public void run() {
        while (running) {
            // 生成普通敵人
            if (Math.random() < 0.02) {
                enemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/星際大戰/enemy1.jpg", 40, 40));
            }

            // 更新普通敵人位置
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);
                enemy.y += 3; // 敵人向下移動
                if (enemy.y > getHeight()) {
                    enemies.remove(i); // 移除超出螢幕的敵人
                } else {
                    // 碰撞檢測（玩家與敵人）
                    if (Math.abs(enemy.x - playerX) < (enemy.width + playerWidth) / 2 &&
                        Math.abs(enemy.y - playerY) < (enemy.height + playerHeight) / 2) {
                        health -= 10; // 每次碰撞減少10生命值
                        enemies.remove(i); // 敵人消失
                        if (health <= 0) {
                            running = false; // 玩家生命值為0，遊戲結束
                        }
                    }
                    // 碰撞檢測（敵人與地球）
                    if (earth != null && enemy.y + enemy.height >= earth.getY() - earth.getHeight() / 2 &&
                        Math.abs(enemy.x - earth.getX()) < earth.getWidth() / 2) {
                        earthHealth -= 10; // 每次到達扣10生命值
                        enemies.remove(i);
                        if (earthHealth <= 0) {
                            running = false; // 地球生命值為0，遊戲結束
                        }
                    }
                }
            }

            // 更新雷射位置
            for (int i = lasers.size() - 1; i >= 0; i--) {
                Laser laser = lasers.get(i);
                laser.y -= 5; // 雷射向上移動
                boolean laserHit = false;

                if (laser.y < 0) {
                    lasers.remove(i); // 移除超出螢幕的雷射
                    continue;
                }

                // 雷射與敵人碰撞檢測
                for (int j = enemies.size() - 1; j >= 0; j--) {
                    Enemy enemy = enemies.get(j);
                    if (Math.abs(laser.x - enemy.x) < (enemy.width + 4) / 2 &&
                        Math.abs(laser.y - enemy.y) < (enemy.height + 10) / 2) {
                        enemies.remove(j); // 敵人被擊中消失
                        score += 10; // 擊敗敵人加10分
                        laserHit = true; // 標記雷射已命中
                        break;
                    }
                }

                if (laserHit) {
                    lasers.remove(i); // 命中後移除雷射
                }
            }

            // 生成道具
            if (Math.random() < 0.005) {
                powerUps.add(new PowerUp((int)(Math.random() * getWidth()), 0, "/星際大戰/powerup.jpg", 30, 30));
            }

            // 更新道具位置
            for (int i = powerUps.size() - 1; i >= 0; i--) {
                PowerUp pu = powerUps.get(i);
                pu.y += 2; // 道具向下掉

                if (pu.y > getHeight()) {
                    powerUps.remove(i); // 超出畫面
                } else if (Math.abs(pu.x - playerX) < (pu.getBounds().width + playerWidth) / 2 &&
                           Math.abs(pu.y - playerY) < (pu.getBounds().height + playerHeight) / 2) {
                    poweredUp = true;
                    laserCount++; // 永久加一道雷射
                    powerUpEndTime = System.currentTimeMillis() + 5000; // 強化效果持續5秒
                    powerUps.remove(i);
                }

                if (System.currentTimeMillis() > powerUpEndTime) {
                    poweredUp = false;
                    laserCount = 1; // 道具效果結束後恢復單發雷射
                }
            }

            // 按住滑鼠左鍵時連續射擊
            if (isShooting) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShootTime >= shootCooldown) {
                    int spacing = 10; // 雷射之間的水平間距
                    int startX = playerX - (laserCount - 1) * spacing / 2;
                    for (int i = 0; i < laserCount; i++) {
                        lasers.add(new Laser(startX + i * spacing, playerY - playerHeight / 2 - 10));
                    }
                    lastShootTime = currentTime;
                }
            }

            repaint();
            try {
                Thread.sleep(16); // 約60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score + "\nEarth Destroyed!");
        System.exit(0);
    }

    // 當滑鼠移動時（未按住左鍵），更新飛船位置
    @Override
    public void mouseMoved(MouseEvent e) {
        updatePlayerPosition(e.getX());
    }

    // 當按住滑鼠左鍵並拖曳時（射擊時移動），更新飛船位置
    @Override
    public void mouseDragged(MouseEvent e) {
        updatePlayerPosition(e.getX());
    }

    // 更新飛船位置的共用方法
    private void updatePlayerPosition(int mouseX) {
        playerX = mouseX;
        // 根據圖片尺寸調整邊界，避免飛船超出畫面
        if (playerX < playerWidth / 2) playerX = playerWidth / 2;
        if (playerX > getWidth() - playerWidth / 2) playerX = getWidth() - playerWidth / 2;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // 按下滑鼠左鍵開始射擊
        if (e.getButton() == MouseEvent.BUTTON1) {
            isShooting = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // 放開滑鼠左鍵停止射擊
        if (e.getButton() == MouseEvent.BUTTON1) {
            isShooting = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}