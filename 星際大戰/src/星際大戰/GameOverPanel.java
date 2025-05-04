package 星際大戰;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverPanel extends JPanel {
    private int finalScore;
    private Image backgroundImage;

    public GameOverPanel(int score, boolean earthDestroyed, Runnable onRestart) {
        this.finalScore = score;
        setLayout(new BorderLayout());

        // 背景圖
        backgroundImage = new ImageIcon(getClass().getResource("/星際大戰/gameover.jpg")).getImage();

        // ===== 上方：標題 =====
        JLabel title = new JLabel("The Earth has been destroyed", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(Color.RED);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // ===== 中間空白區（可省略或放其他資訊）=====
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);

        // ===== 下方：分數 + 按鈕 =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JLabel scoreLabel = new JLabel("Your score: " + finalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 22));
        scoreLabel.setForeground(Color.WHITE);
        bottomPanel.add(scoreLabel, BorderLayout.NORTH); // 分數在下方區塊頂部

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
