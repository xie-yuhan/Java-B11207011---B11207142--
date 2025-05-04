package �P�ڤj��;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverPanel extends JPanel {
    private int finalScore;
    private Image backgroundImage;

    public GameOverPanel(int score, boolean earthDestroyed, Runnable onRestart) {
        this.finalScore = score;
        setLayout(new BorderLayout());

        // �I����
        backgroundImage = new ImageIcon(getClass().getResource("/�P�ڤj��/gameover.jpg")).getImage();

        // ===== �W��G���D =====
        JLabel title = new JLabel("The Earth has been destroyed", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(Color.RED);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // ===== �����ťհϡ]�i�ٲ��Ω��L��T�^=====
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);

        // ===== �U��G���� + ���s =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JLabel scoreLabel = new JLabel("Your score: " + finalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 22));
        scoreLabel.setForeground(Color.WHITE);
        bottomPanel.add(scoreLabel, BorderLayout.NORTH); // ���Ʀb�U��϶�����

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton restartButton = new JButton("Play Again");
        restartButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        restartButton.addActionListener(e -> onRestart.run());

        JButton exitButton = new JButton("Exit Game");
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
