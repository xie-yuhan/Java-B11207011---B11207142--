package StarWars;
 
 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 import java.util.ArrayList;
 
 public class GamePanel extends JPanel implements MouseMotionListener, MouseListener, Runnable {
     private int playerX = 400, playerY = 500; // 玩家飛船位置
     private Image playerImage;
     private ArrayList<Enemy> enemies; // 敵人列表
     private ArrayList<Laser> lasers; // 雷射列表
     private int score = 0; // 分數
     private int level = 1; // 關卡
     private int health = 100; // 生命值（初始100）
     private boolean running = true;
     private boolean isShooting = false; // 追蹤是否正在射擊
     private long lastShootTime = 0; // 上次射擊的時間
     private final long shootCooldown = 200; // 射擊冷卻時間（毫秒）
 
     public GamePanel() {
         enemies = new ArrayList<>();
         lasers = new ArrayList<>();
         playerImage = new ImageIcon(getClass().getResource("/星際大戰/player.jpg")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
         setFocusable(true);
         addMouseMotionListener(this);
         addMouseListener(this);
         new Thread(this).start(); // 啟動遊戲主循環
     }
 
     @Override
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         setBackground(Color.BLACK);//space
         
         g.setColor(Color.WHITE);
         for (int i = 0; i < 100; i++) {
             int x = (int) (Math.random() * getWidth());
             int y = (int) (Math.random() * getHeight());
             g.fillRect(x, y, 1, 1);
         }//stars
 
         g.drawImage(playerImage, playerX - playerImage.getWidth(this)/2, playerY - playerImage.getHeight(this)/2, this);//player
 
         for (Enemy enemy : enemies) {
             enemy.draw(g, this);
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
                enemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/星際大戰/enemy1.jpg", 40, 40));
             }
 
             // 更新敵人位置
             for (int i = enemies.size() - 1; i >= 0; i--) {
                 Enemy enemy = enemies.get(i);
                 enemy.y += 3; // 敵人向下移動
                 if (enemy.y > getHeight()) {
                     enemies.remove(i); // 移除超出螢幕的敵人
                 } 
                 else {
                     // 簡單碰撞檢測（玩家與敵人）
                     if (Math.abs(enemy.x - playerX) < 30 && Math.abs(enemy.y - playerY) < 30) {
                         health -= 10; // 每次碰撞減少20生命值
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
                         if (Math.abs(laser.x - enemy.x) < 25 && Math.abs(laser.y - enemy.y) < 25) {
                             enemies.remove(j); // 敵人被擊中消失
                             lasers.remove(i); // 雷射消失
                             score += 10; // 擊敗敵人加10分
                             break;
                         }
                     }
                 }
             }
 
             // 按住滑鼠左鍵時連續射擊
             if (isShooting) {
                 long currentTime = System.currentTimeMillis();
                 if (currentTime - lastShootTime >= shootCooldown) {
                     lasers.add(new Laser(playerX, playerY - 10)); // 發射雷射
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
         JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
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
         if (playerX < 40) playerX = 40;
         if (playerX > getWidth() - 40) playerX = getWidth() - 40;
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