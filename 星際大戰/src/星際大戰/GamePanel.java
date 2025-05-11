package �P�ڤj��;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;

public class GamePanel extends JPanel implements MouseMotionListener, MouseListener, Runnable {
    private int playerX = 400, playerY = 400; // ���a�����m�]�����I�A�V�W�վ�^
    private Image playerImage;
    private Image backgroundImage; // �P�ŭI���Ϥ�
    private ArrayList<Enemy> enemies; // ���q�ĤH�C��
    private ArrayList<Enemy> attackingEnemies; // �������a�ĤH�C��
    private ArrayList<Laser> lasers; // ���a�p�g�C��
    private ArrayList<Laser> enemyLasers; // �ĤH�����C��
    private ArrayList<PowerUp> powerUps; // �D��C��
    private Earth earth; // �a�y��H
    private Boss boss; // BOSS ��H
    private int score = 0; // ����
    private int level = 1; // ���d
    private int health = 500; // ���a�ͩR�ȡ]��l500�A�P��ܤ@�P�^
    private int earthHealth = 500; // �a�y�ͩR�ȡ]��l500�A�P��ܤ@�P�^
    private int laserCount = 1; // ��l�@�o�p�g
    private boolean running = true;
    private boolean isShooting = false; // �l�ܬO�_���b�g��
    private boolean poweredUp = false; // �ˬd�O�_��o�D��j��
    private long lastShootTime = 0; // �W���g�����ɶ�
    private long powerUpEndTime = 0; 
    private long lastBossAttackTime = 0; // �W�� BOSS �����ɶ�
    private final long shootCooldown = 200; // �g���N�o�ɶ��]�@��^
    private final long bossAttackCooldown = 5000; // BOSS �����N�o�ɶ�
    private final long enemyLaserCooldown = 500; // �ĤH�����N�o�ɶ��]�Y�u�� 500 �@��^
    private int playerWidth, playerHeight; // ���a����Ϥ����e��
    private boolean hasSpawnedFirstWave = false; // �l�ܲĤ@�i�ĤH�O�_�w�ͦ�
    private boolean hasSpawnedSecondWave = false; // �l�ܲĤG�i�ĤH�O�_�w�ͦ�
    private Random random = new Random();
    private StarWarsGame gameFrame; // �ޥΥD�ج[
    private Clip backgroundMusicClip; // �I������
    private Clip bossMusicClip; // BOSS ����

    public GamePanel(StarWarsGame gameFrame, Clip backgroundMusicClip) {
        this.gameFrame = gameFrame;
        this.backgroundMusicClip = backgroundMusicClip;
        enemies = new ArrayList<>();
        attackingEnemies = new ArrayList<>();
        lasers = new ArrayList<>();
        enemyLasers = new ArrayList<>();
        powerUps = new ArrayList<>();
        // �[�����a����Ϥ��ó]�m�ؤo
        playerImage = new ImageIcon(getClass().getResource("/�P�ڤj��/player.jpg")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        // �[���վ�᪺�P�ŭI���Ϥ�
        backgroundImage = new ImageIcon(getClass().getResource("/�P�ڤj��/stars.jpg")).getImage();
        if (backgroundImage == null) {
            System.err.println("Failed to load background image: /�P�ڤj��/stars.jpg");
        }
        playerWidth = 60; // �Ϥ��e��
        playerHeight = 60; // �Ϥ�����
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);
        new Thread(this).start(); // �ҰʹC���D�`��
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ø�s�P�ŭI���@���̩��h�A�u�л\�a�y�W�誺�ϰ�
        if (backgroundImage != null && earth != null) {
            int earthY = earth.getY() - earth.getHeight() / 2; // �a�y���W���
            g.drawImage(backgroundImage, 0, 0, getWidth(), earthY, this); // �uø�s��a�y���W�b����
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        if (earth == null && getWidth() > 0 && getHeight() > 0) {
            earth = new Earth(getWidth() / 2, getHeight() - 100, getWidth() / 2 + 40, getHeight() + 600, "/�P�ڤj��/earth.jpg", getWidth(), 200);
        }

        if (earth != null) {
            earth.draw(g, this);
            g.setColor(Color.RED);
            g.drawArc(earth.getCollisionX() - earth.getRadius(), earth.getCollisionY() - earth.getRadius(), earth.getRadius() * 2, earth.getRadius() * 2, 0, 180);
        }

        g.drawImage(playerImage, playerX - playerWidth / 2, playerY - playerHeight / 2, this);

        for (Enemy enemy : enemies) enemy.draw(g, this);
        for (Enemy enemy : attackingEnemies) enemy.draw(g, this);
        if (boss != null) boss.draw(g, this);

        g.setColor(Color.GREEN);
        for (Laser laser : lasers) {
            g.fillRect(laser.x, laser.y, 4, 10);
        }

        g.setColor(Color.RED);
        for (Laser laser : enemyLasers) {
            g.fillRect(laser.x, laser.y, 4, 10);
        }

        for (PowerUp pu : powerUps) {
            pu.draw(g, this);
        }

        g.setColor(Color.WHITE);
        g.drawString("LEVEL: " + level, 10, 40);
        g.drawString("SCORE: " + score, 10, 60);

        if (poweredUp) {
            long remaining = (powerUpEndTime - System.currentTimeMillis()) / 1000;
            g.drawString("POWER-UP: " + remaining + "s", 10, 80);
        }

        int playerHealthBarWidth = 100;
        int playerHealthBarHeight = 5;
        int playerHealthBarX = playerX - playerHealthBarWidth / 2;
        int playerHealthBarY = playerY + 50;
        g.setColor(Color.GRAY);
        g.fillRect(playerHealthBarX, playerHealthBarY, playerHealthBarWidth, playerHealthBarHeight);
        g.setColor(Color.GREEN);
        g.fillRect(playerHealthBarX, playerHealthBarY, (int)(playerHealthBarWidth * (health / 500.0)), playerHealthBarHeight);
        g.setColor(Color.WHITE);
        g.drawString("Player HP: " + health + "/500", playerHealthBarX, playerHealthBarY - 5);

        int earthHealthBarWidth = 200;
        int earthHealthBarHeight = 10;
        int earthHealthBarX = 10;
        int earthHealthBarY = 30;
        g.setColor(Color.GRAY);
        g.fillRect(earthHealthBarX, earthHealthBarY, earthHealthBarWidth, earthHealthBarHeight);
        g.setColor(Color.BLUE);
        g.fillRect(earthHealthBarX, earthHealthBarY, (int)(earthHealthBarWidth * (earthHealth / 500.0)), earthHealthBarHeight);
        g.setColor(Color.WHITE);
        g.drawString("Earth HP: " + earthHealth + "/500", earthHealthBarX, earthHealthBarY - 5);

        if (boss != null) {
            int bossHealthBarWidth = 300;
            int bossHealthBarHeight = 10;
            int bossHealthBarX = getWidth() / 2 - bossHealthBarWidth / 2;
            int bossHealthBarY = 50;
            g.setColor(Color.GRAY);
            g.fillRect(bossHealthBarX, bossHealthBarY, bossHealthBarWidth, bossHealthBarHeight);
            g.setColor(Color.RED);
            g.fillRect(bossHealthBarX, bossHealthBarY, (int)(bossHealthBarWidth * (boss.health / 1000.0)), bossHealthBarHeight);
            g.setColor(Color.WHITE);
            g.drawString("BOSS HP: " + boss.health + "/1000", bossHealthBarX, bossHealthBarY - 5);
        }
    }

    @Override
    public void run() {
        while (running) {
            if (score >= 500 && level < 3) {
                level = 3;
                boss = new Boss(getWidth() / 2, 50, "/�P�ڤj��/boss.jpg", 200, 200);
                boss.health = 1000;
                if (!hasSpawnedFirstWave) {
                    spawnEnemyWave(7, 3);
                    hasSpawnedFirstWave = true;
                }
                switchToBossMusic();
            } else if (score >= 100 && level < 2) {
                level = 2;
            }

            double normalEnemySpawnRate = (level == 3) ? 0.03 : 0.02;
            if (Math.random() < normalEnemySpawnRate) {
                enemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/�P�ڤj��/enemy1.jpg", 40, 40));
            }

            if (level >= 2) {
                double attackingEnemySpawnRate = (level == 3) ? 0.02 : 0.01;
                if (Math.random() < attackingEnemySpawnRate) {
                    attackingEnemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/�P�ڤj��/enemy2.jpg", 40, 40));
                }
            }

            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);
                enemy.y += 3;
                if (enemy.y > getHeight()) {
                    enemies.remove(i);
                } else {
                    if (Math.abs(enemy.x - playerX) < (enemy.width + playerWidth) / 2 &&
                        Math.abs(enemy.y - playerY) < (enemy.height + playerHeight) / 2) {
                        health -= 10;
                        enemies.remove(i);
                        if (health <= 0) {
                            running = false;
                        }
                    }
                    if (earth != null) {
                        double distance = Math.sqrt(Math.pow(enemy.x - earth.getCollisionX(), 2) + Math.pow(enemy.y - earth.getCollisionY(), 2));
                        if (enemy.y <= earth.getY() && distance <= earth.getRadius() + enemy.height / 2) {
                            earthHealth -= 10;
                            enemies.remove(i);
                            if (earthHealth <= 0) {
                                running = false;
                            }
                        }
                    }
                }
            }

            for (int i = attackingEnemies.size() - 1; i >= 0; i--) {
                Enemy enemy = attackingEnemies.get(i);
                enemy.lastShootTime += 16;
                int dx = playerX - enemy.x;
                int dy = playerY - enemy.y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance > 5) {
                    enemy.x += (int) (dx * 0.02);
                    enemy.y += (int) (dy * 0.02);
                }
                if (enemy.y > getHeight()) {
                    attackingEnemies.remove(i);
                    continue;
                }
                if (Math.abs(enemy.x - playerX) < (enemy.width + playerWidth) / 2 &&
                    Math.abs(enemy.y - playerY) < (enemy.height + playerHeight) / 2) {
                    health -= 15;
                    attackingEnemies.remove(i);
                    if (health <= 0) {
                        running = false;
                    }
                }
                if (enemy.lastShootTime >= enemyLaserCooldown) {
                    enemyLasers.add(new Laser(enemy.x, enemy.y + enemy.height / 2));
                    enemy.lastShootTime = 0;
                }
            }

            for (int i = enemyLasers.size() - 1; i >= 0; i--) {
                Laser laser = enemyLasers.get(i);
                laser.y += 5;
                if (laser.y > getHeight()) {
                    enemyLasers.remove(i);
                    continue;
                }
                if (Math.abs(laser.x - playerX) < (playerWidth + 4) / 2 &&
                    Math.abs(laser.y - playerY) < (playerHeight + 10) / 2) {
                    health -= 10;
                    enemyLasers.remove(i);
                    if (health <= 0) {
                        running = false;
                    }
                }
            }

            if (boss != null) {
                boss.x += boss.direction * 2;
                if (boss.x <= boss.width / 2 || boss.x >= getWidth() - boss.width / 2) {
                    boss.direction *= -1;
                }
                if (System.currentTimeMillis() - lastBossAttackTime >= bossAttackCooldown) {
                    health -= 20;
                    lastBossAttackTime = System.currentTimeMillis();
                    if (Math.random() < 0.4) enemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/�P�ڤj��/enemy1.jpg", 40, 40));
                    if (Math.random() < 0.5) attackingEnemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/�P�ڤj��/enemy2.jpg", 40, 40));
                }
                if (boss.health <= 500 && !hasSpawnedSecondWave) {
                    spawnEnemyWave(10, 5);
                    hasSpawnedSecondWave = true;
                }
            }

            for (int i = lasers.size() - 1; i >= 0; i--) {
                Laser laser = lasers.get(i);
                laser.y -= 5;
                boolean laserHit = false;

                if (laser.y < 0) {
                    lasers.remove(i);
                    continue;
                }

                for (int j = enemies.size() - 1; j >= 0; j--) {
                    Enemy enemy = enemies.get(j);
                    if (Math.abs(laser.x - enemy.x) < (enemy.width + 4) / 2 &&
                        Math.abs(laser.y - enemy.y) < (enemy.height + 10) / 2) {
                        enemies.remove(j);
                        score += 10;
                        laserHit = true;
                        break;
                    }
                }

                for (int j = attackingEnemies.size() - 1; j >= 0; j--) {
                    Enemy enemy = attackingEnemies.get(j);
                    if (Math.abs(laser.x - enemy.x) < (enemy.width + 4) / 2 &&
                        Math.abs(laser.y - enemy.y) < (enemy.height + 10) / 2) {
                        attackingEnemies.remove(j);
                        score += 15;
                        laserHit = true;
                        break;
                    }
                }

                if (boss != null && Math.abs(laser.x - boss.x) < (boss.width + 4) / 2 && Math.abs(laser.y - boss.y) < (boss.height + 10) / 2) {
                    boss.health -= 5;
                    laserHit = true;
                    if (boss.health <= 0) {
                        boss = null;
                        running = false;
                    }
                }

                if (laserHit) {
                    lasers.remove(i);
                }
            }

            if (Math.random() < 0.005) {
                powerUps.add(new PowerUp((int)(Math.random() * getWidth()), 0, "/�P�ڤj��/powerup.jpg", 30, 30));
            }

            for (int i = powerUps.size() - 1; i >= 0; i--) {
                PowerUp pu = powerUps.get(i);
                pu.y += 2;

                if (pu.y > getHeight()) {
                    powerUps.remove(i);
                } else if (Math.abs(pu.x - playerX) < (pu.getBounds().width + playerWidth) / 2 &&
                           Math.abs(pu.y - playerY) < (pu.getBounds().height + playerHeight) / 2) {
                    poweredUp = true;
                    laserCount++;
                    powerUpEndTime = System.currentTimeMillis() + 5000;
                    powerUps.remove(i);
                }

                if (System.currentTimeMillis() > powerUpEndTime) {
                    poweredUp = false;
                    laserCount = 1;
                }
            }

            if (isShooting) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShootTime >= shootCooldown) {
                    int spacing = 10;
                    int startX = playerX - (laserCount - 1) * spacing / 2;
                    for (int i = 0; i < laserCount; i++) {
                        lasers.add(new Laser(startX + i * spacing, playerY - playerHeight / 2 - 10));
                    }
                    playShootSound();
                    lastShootTime = currentTime;
                }
            }

            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopAllMusic();
        SwingUtilities.invokeLater(() -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (earthHealth <= 0 || health <= 0) {
                topFrame.setContentPane(new GameOverPanel(score, true, name -> {
                    gameFrame.addScore(name, score, false); // ���ѡAisGameComplete = false
                    gameFrame.showGameCoverPanel();
                }));
            } else if (boss == null && level == 3) {
                topFrame.setContentPane(new GameCompletePanel(score, name -> {
                    gameFrame.addScore(name, score, true); // �q���AisGameComplete = true
                    gameFrame.showGameCoverPanel();
                }));
            }
            topFrame.revalidate();
        });
    }

    private void spawnEnemyWave(int normalCount, int attackingCount) {
        for (int i = 0; i < normalCount; i++) {
            enemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/�P�ڤj��/enemy1.jpg", 40, 40));
        }
        for (int i = 0; i < attackingCount; i++) {
            attackingEnemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/�P�ڤj��/enemy2.jpg", 40, 40));
        }
    }

    private void playShootSound() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/�P�ڤj��/shoot.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // �榸����
        } catch (Exception e) {
            System.err.println("Failed to load or play shoot sound: " + e.getMessage());
        }
    }

    private void switchToBossMusic() {
        try {
            // ����I������
            if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
                backgroundMusicClip.stop();
            }
            // ���� BOSS ����
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/�P�ڤj��/bossMusic.wav"));
            bossMusicClip = AudioSystem.getClip();
            bossMusicClip.open(audioStream);
            bossMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // �`������
            bossMusicClip.start();
            System.out.println("Boss music loaded and playing.");
        } catch (Exception e) {
            System.err.println("Failed to load or play boss music: " + e.getMessage());
        }
    }

    private void stopAllMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
        if (bossMusicClip != null && bossMusicClip.isRunning()) {
            bossMusicClip.stop();
            bossMusicClip.close();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        updatePlayerPosition(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        updatePlayerPosition(e.getX(), e.getY());
    }

    private void updatePlayerPosition(int mouseX, int mouseY) {
        playerX = mouseX;
        playerY = mouseY;
        if (playerX < playerWidth / 2) playerX = playerWidth / 2;
        if (playerX > getWidth() - playerWidth / 2) playerX = getWidth() - playerWidth / 2;
        if (playerY < playerHeight / 2) playerY = playerHeight / 2;
        if (playerY > getHeight() - playerHeight / 2) playerY = getHeight() - playerHeight / 2;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isShooting = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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