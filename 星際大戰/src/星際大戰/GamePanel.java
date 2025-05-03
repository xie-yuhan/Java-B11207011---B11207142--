package �P�ڤj��;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements MouseMotionListener, MouseListener, Runnable {
    private int playerX = 400, playerY = 400; // ���a�����m�]�����I�A�V�W�վ�^
    private Image playerImage;
    private Image backgroundImage; // �P�ŭI���Ϥ�
    private ArrayList<Enemy> enemies; // ���q�ĤH�C��
    private ArrayList<Laser> lasers; // �p�g�C��
    private ArrayList<PowerUp> powerUps; // �D��C��
    private Earth earth; // �a�y��H
    private int score = 0; // ����
    private int level = 1; // ���d
    private int health = 100; // ���a�ͩR�ȡ]��l100�^
    private int earthHealth = 500; // �a�y�ͩR�ȡ]��l500�^
    private int laserCount = 1; // ��l�@�o�p�g
    private boolean running = true;
    private boolean isShooting = false; // �l�ܬO�_���b�g��
    private boolean poweredUp = false; // �ˬd�O�_��o�D��j��
    private long lastShootTime = 0; // �W���g�����ɶ�
    private long powerUpEndTime = 0; 
    private final long shootCooldown = 200; // �g���N�o�ɶ��]�@��^
    private int playerWidth, playerHeight; // ���a����Ϥ����e��

    public GamePanel() {
        enemies = new ArrayList<>();
        lasers = new ArrayList<>();
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
    public void addNotify() {
        super.addNotify();
        // ���A���B��l�� earth�A�]���ؤo�i��|���ǳƦn
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ø�s�P�ŭI���@���̩��h�A�u�л\�a�y�W�誺�ϰ�
        if (backgroundImage != null && earth != null) {
            int earthY = earth.getY() - earth.getHeight() / 2; // �a�y���W���
            g.drawImage(backgroundImage, 0, 0, getWidth(), earthY, this); // �uø�s��a�y���W�b����
        } else {
            g.setColor(Color.BLACK); // �ƥζ¦�I��
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // �T�O earth �w��l�ơA�åB�ؤo����
        if (earth == null && getWidth() > 0 && getHeight() > 0) {
            earth = new Earth(getWidth() / 2, getHeight() - 100, "/�P�ڤj��/earth.jpg", getWidth(), 200);
        }

        // ø�s�a�y
        if (earth != null) {
            earth.draw(g, this);
            g.setColor(Color.RED);
            g.drawRect(earth.getX() - earth.getWidth() / 2, earth.getY() - earth.getHeight() / 2, earth.getWidth(), earth.getHeight());
        }

        // ø�s���a����]�T�O�b�a�y���W�^
        g.drawImage(playerImage, playerX - playerWidth / 2, playerY - playerHeight / 2, this);

        // ø�s���q�ĤH
        for (Enemy enemy : enemies) {
            enemy.draw(g, this);
        }

        // ø�s�p�g
        g.setColor(Color.GREEN);
        for (Laser laser : lasers) {
            g.fillRect(laser.x, laser.y, 4, 10);
        }

        // ø�s�D��
        for (PowerUp pu : powerUps) {
            pu.draw(g, this);
        }

        // ø�s���ƩM���d
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + score, 10, 20);
        g.drawString("LEVEL: " + level, 10, 40);

        // ø�s�D��j�ƭ˭p��
        if (poweredUp) {
            long remaining = (powerUpEndTime - System.currentTimeMillis()) / 1000;
            g.drawString("POWER-UP: " + remaining + "s", 10, 60);
        }

        // ø�s���a�ͩR�ȱ��]���H������I�^
        g.setColor(Color.GREEN);
        g.fillRect(playerX - 20, playerY + 30, health / 2, 5);
        g.setColor(Color.RED);
        g.fillRect(playerX - 20 + health / 2, playerY + 30, (100 - health) / 2, 5);

        // ø�s�a�y�ͩR�ȱ��]��ܦb�e�������^
        g.setColor(Color.BLUE);
        g.fillRect(10, 10, earthHealth / 5, 10);
        g.setColor(Color.RED);
        g.fillRect(10 + earthHealth / 5, 10, (500 - earthHealth) / 5, 10);
    }

    @Override
    public void run() {
        while (running) {
            // �ͦ����q�ĤH
            if (Math.random() < 0.02) {
                enemies.add(new Enemy((int) (Math.random() * getWidth()), 0, "/�P�ڤj��/enemy1.jpg", 40, 40));
            }

            // ��s���q�ĤH��m
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);
                enemy.y += 3; // �ĤH�V�U����
                if (enemy.y > getHeight()) {
                    enemies.remove(i); // �����W�X�ù����ĤH
                } else {
                    // �I���˴��]���a�P�ĤH�^
                    if (Math.abs(enemy.x - playerX) < (enemy.width + playerWidth) / 2 &&
                        Math.abs(enemy.y - playerY) < (enemy.height + playerHeight) / 2) {
                        health -= 10; // �C���I�����10�ͩR��
                        enemies.remove(i); // �ĤH����
                        if (health <= 0) {
                            running = false; // ���a�ͩR�Ȭ�0�A�C������
                        }
                    }
                    // �I���˴��]�ĤH�P�a�y�^
                    if (earth != null && enemy.y + enemy.height >= earth.getY() - earth.getHeight() / 2 &&
                        Math.abs(enemy.x - earth.getX()) < earth.getWidth() / 2) {
                        earthHealth -= 10; // �C����F��10�ͩR��
                        enemies.remove(i);
                        if (earthHealth <= 0) {
                            running = false; // �a�y�ͩR�Ȭ�0�A�C������
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
                    if (Math.abs(laser.x - enemy.x) < (enemy.width + 4) / 2 &&
                        Math.abs(laser.y - enemy.y) < (enemy.height + 10) / 2) {
                        enemies.remove(j); // �ĤH�Q��������
                        score += 10; // ���ѼĤH�[10��
                        laserHit = true; // �аO�p�g�w�R��
                        break;
                    }
                }

                if (laserHit) {
                    lasers.remove(i); // �R���Ჾ���p�g
                }
            }

            // �ͦ��D��
            if (Math.random() < 0.005) {
                powerUps.add(new PowerUp((int)(Math.random() * getWidth()), 0, "/�P�ڤj��/powerup.jpg", 30, 30));
            }

            // ��s�D���m
            for (int i = powerUps.size() - 1; i >= 0; i--) {
                PowerUp pu = powerUps.get(i);
                pu.y += 2; // �D��V�U��

                if (pu.y > getHeight()) {
                    powerUps.remove(i); // �W�X�e��
                } else if (Math.abs(pu.x - playerX) < (pu.getBounds().width + playerWidth) / 2 &&
                           Math.abs(pu.y - playerY) < (pu.getBounds().height + playerHeight) / 2) {
                    poweredUp = true;
                    laserCount++; // �ä[�[�@�D�p�g
                    powerUpEndTime = System.currentTimeMillis() + 5000; // �j�ƮĪG����5��
                    powerUps.remove(i);
                }

                if (System.currentTimeMillis() > powerUpEndTime) {
                    poweredUp = false;
                    laserCount = 1; // �D��ĪG�������_��o�p�g
                }
            }

            // ����ƹ�����ɳs��g��
            if (isShooting) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShootTime >= shootCooldown) {
                    int spacing = 10; // �p�g�������������Z
                    int startX = playerX - (laserCount - 1) * spacing / 2;
                    for (int i = 0; i < laserCount; i++) {
                        lasers.add(new Laser(startX + i * spacing, playerY - playerHeight / 2 - 10));
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
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score + "\nEarth Destroyed!");
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
        // �ھڹϤ��ؤo�վ���ɡA�קK����W�X�e��
        if (playerX < playerWidth / 2) playerX = playerWidth / 2;
        if (playerX > getWidth() - playerWidth / 2) playerX = getWidth() - playerWidth / 2;
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