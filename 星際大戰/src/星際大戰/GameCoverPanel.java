package 星際大戰;

import javax.swing.*;
import java.awt.*;

public class GameCoverPanel extends JPanel {
    private Image backgroundImage;

<<<<<<< HEAD
    public GameCoverPanel(Runnable onStartGame, Runnable onShowLeaderboard) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        backgroundImage = new ImageIcon(getClass().getResource("/星際大戰/gamecover.jpg")).getImage();
        JLabel titleLabel = new JLabel("星際大戰 - Space Wars");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> onStartGame.run());
        JButton leaderboardButton = new JButton("View Leaderboard");
        leaderboardButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.addActionListener(e -> onShowLeaderboard.run());
        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(30));
        add(startButton);
        add(Box.createVerticalStrut(20));
        add(leaderboardButton);
        add(Box.createVerticalGlue());
=======
    public GameCoverPanel(Runnable onStartGame) {
        // 設定垂直 BoxLayout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 載入背景圖片（請確認路徑）
        backgroundImage = new ImageIcon(getClass().getResource("/星際大戰/gamecover.jpg")).getImage();

        // 標題 Label
        JLabel titleLabel = new JLabel("星際大戰 - Space Wars");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 水平置中

        // 開始按鈕
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 水平置中
        startButton.addActionListener(e -> onStartGame.run());

        // 加入元件與空白來置中
        add(Box.createVerticalGlue()); // 上方空白推擠
        add(titleLabel);
        add(Box.createVerticalStrut(30)); // 標題和按鈕之間距離
        add(startButton);
        add(Box.createVerticalGlue()); // 下方空白推擠
>>>>>>> e149a2d1556e47ff99f0bd23e0199a16c74576f4
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}