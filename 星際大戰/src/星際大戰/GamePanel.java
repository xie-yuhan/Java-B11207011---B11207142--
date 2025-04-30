package �P�ڤj��;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements MouseMotionListener, MouseListener, Runnable {
    private int playerX = 400, playerY = 500; // ���a�����m
    private Image playerImage;
    private ArrayList<Enemy> enemies; // �ĤH�C��
    private ArrayList<Laser> lasers; // �p�g�C��
    private ArrayList<PowerUp> powerUps;//Power the lasers
    private int score = 0; // ����
    private int level = 1; // ���d
    private int health = 100; // �ͩR�ȡ]��l100�^
    private int laserCount = 1; // ��l�@�o�p�g
    private boolean running = true;
    private boolean isShooting = false; // �l�ܬO�_���b�g��
    private boolean poweredUp = false;//checking if the props have been eaten
    private long lastShootTime = 0; // �W���g�����ɶ�
    private long powerUpEndTime = 0; 
    private final long shootCooldown = 200; // �g���N�o�ɶ��]�@��^

    public GamePanel() {
        enemies = new ArrayList<>();
        lasers = new ArrayList<>();
        powerUps = new ArrayList<>();
        playerImage = new ImageIcon(getClass().getResource("/�P�ڤj��/player.jpg")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);
        new Thread(this).start(); // �ҰʹC���D�`��
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

        // �e�p�g
        g.setColor(Color.GREEN);
        for (Laser laser : lasers) {
            g.fillRect(laser.x, laser.y, 4, 10);
        }
        
        for (PowerUp pu : powerUps) {
            pu.draw(g, this);
        }

        // �e���ƩM���d
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + score, 10, 20);
        g.drawString("LEVEL: " + level, 10, 40);
        
        if (poweredUp) {
            long remaining = (powerUpEndTime - System.currentTimeMillis()) / 1000;
            g.drawString("POWER-UP: " + remaining + "s", 10, 60);
        }

        // �e�ͩR�ȱ�
        g.setColor(Color.GREEN);
        g.fillRect(playerX - 20, playerY + 20, health / 2, 5); // �ͩR�ȱ��]�ھ�health�Y��^
        g.setColor(Color.RED);
        g.fillRect(playerX - 20 + health / 2, playerY + 20, (100 - health) / 2, 5); // �l�����ͩR��
    }

    @Override
    public void run() {
        while (running) {
            // �ͦ��ĤH
            if (Math.random() < 0.02) {
               enemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/�P�ڤj��/enemy1.jpg", 40, 40));
            }

            // ��s�ĤH��m
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);
                enemy.y += 3; // �ĤH�V�U����
                if (enemy.y > getHeight()) {
                    enemies.remove(i); // �����W�X�ù����ĤH
                } 
                else {
                    // ²��I���˴��]���a�P�ĤH�^
                    if (Math.abs(enemy.x - playerX) < 30 && Math.abs(enemy.y - playerY) < 30) {
                        health -= 10; // �C���I�����20�ͩR��
                        enemies.remove(i); // �ĤH����
                        if (health <= 0) {
                            running = false; // �ͩR�Ȭ�0�A�C������
                        }
                    }
                }
            }

            // ��s�p�g��m
            for (int i = lasers.size() - 1; i >= 0; i--) {
                Laser laser = lasers.get(i);
                laser.y -= 5; // �p�g�V�W����
                boolean laserHit = false;
            
                if (laser.y < 0) {
                    lasers.remove(i); // �����W�X�ù����p�g
                    continue;
                }
            
                // �p�g�P�ĤH�I���˴�
                for (int j = enemies.size() - 1; j >= 0; j--) {
                    Enemy enemy = enemies.get(j);
                    if (Math.abs(laser.x - enemy.x) < 25 && Math.abs(laser.y - enemy.y) < 25) {
                        enemies.remove(j); // �ĤH�Q��������
                        score += 10; // ���ѼĤH�[10��
                        laserHit = true; // �аO�p�g�w�R��
                        break;
                    }
                }
            
                if (laserHit) {
                    lasers.remove(i); // �R���Ჾ���p�g�]��b�~�h�A�קK�O�M�����^
                }
            }
            
            if (Math.random() < 0.005) {
               powerUps.add(new PowerUp((int)(Math.random() * getWidth()), 0, "/�P�ڤj��/powerup.jpg", 30, 30));
            }
            for (int i = powerUps.size() - 1; i >= 0; i--) {
               PowerUp pu = powerUps.get(i);
               pu.y += 2; // �D��V�U��

               if (pu.y > getHeight()) {
                  powerUps.remove(i); // �W�X�e��
               } 
               else if (Math.abs(pu.x - playerX) < 30 && Math.abs(pu.y - playerY) < 30) {
                  poweredUp = true;
                  laserCount++; // �ä[�[�@�D�p�g
                  powerUpEndTime = System.currentTimeMillis() + 5000; // �j�ƮĪG����5��
                  powerUps.remove(i);
               }
               if (System.currentTimeMillis() > powerUpEndTime) {
                  poweredUp = false;
                  laserCount = 1;
               }
            }
            

            // ����ƹ�����ɳs��g��
            if (isShooting) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShootTime >= shootCooldown) {
                    int spacing = 10; // �p�g�������������Z
                    int startX = playerX - (laserCount - 1) * spacing / 2;
                    for (int i = 0; i < laserCount; i++) {
                        lasers.add(new Laser(startX + i * spacing, playerY - 10));
                    }
                    lastShootTime = currentTime;
                }
            }

            repaint();
            try {
                Thread.sleep(16); // ��60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
        System.exit(0);
    }

    // ��ƹ����ʮɡ]��������^�A��s�����m
    @Override
    public void mouseMoved(MouseEvent e) {
        updatePlayerPosition(e.getX());
    }

    // �����ƹ�����é즲�ɡ]�g���ɲ��ʡ^�A��s�����m
    @Override
    public void mouseDragged(MouseEvent e) {
        updatePlayerPosition(e.getX());
    }

    // ��s�����m���@�Τ�k
    private void updatePlayerPosition(int mouseX) {
        playerX = mouseX;
        if (playerX < 40) playerX = 40;
        if (playerX > getWidth() - 40) playerX = getWidth() - 40;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // ���U�ƹ�����}�l�g��
        if (e.getButton() == MouseEvent.BUTTON1) {
            isShooting = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // ��}�ƹ����䰱��g��
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