package 星際大戰;

import javax.swing.*;
import java.awt.*;

public class GameCoverPanel extends JPanel {
    private Image backgroundImage;

    public GameCoverPanel(Runnable onStartGame, Runnable onShowLeaderboard, Runnable onShowVolume) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        backgroundImage = new ImageIcon(getClass().getResource("/星際大戰/gamecover.jpg")).getImage();

        JLabel titleLabel = new JLabel("星際大戰 - Space Wars");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("開始遊戲");
        startButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> onStartGame.run());

        JButton leaderboardButton = new JButton("排行榜");
        leaderboardButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.addActionListener(e -> onShowLeaderboard.run());

        JButton volumeButton = new JButton("音量大小");
        volumeButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        volumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        volumeButton.addActionListener(e -> onShowVolume.run());

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(30));
        add(startButton);
        add(Box.createVerticalStrut(20));
        add(leaderboardButton);
        add(Box.createVerticalStrut(20));
        add(volumeButton);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
