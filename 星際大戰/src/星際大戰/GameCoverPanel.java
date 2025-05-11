package �P�ڤj��;

import javax.swing.*;
import java.awt.*;

public class GameCoverPanel extends JPanel {
    private Image backgroundImage;

<<<<<<< HEAD
    public GameCoverPanel(Runnable onStartGame, Runnable onShowLeaderboard) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        backgroundImage = new ImageIcon(getClass().getResource("/�P�ڤj��/gamecover.jpg")).getImage();
        JLabel titleLabel = new JLabel("�P�ڤj�� - Space Wars");
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
        // �]�w���� BoxLayout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ���J�I���Ϥ��]�нT�{���|�^
        backgroundImage = new ImageIcon(getClass().getResource("/�P�ڤj��/gamecover.jpg")).getImage();

        // ���D Label
        JLabel titleLabel = new JLabel("�P�ڤj�� - Space Wars");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // �����m��

        // �}�l���s
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // �����m��
        startButton.addActionListener(e -> onStartGame.run());

        // �[�J����P�ťըӸm��
        add(Box.createVerticalGlue()); // �W��ťձ���
        add(titleLabel);
        add(Box.createVerticalStrut(30)); // ���D�M���s�����Z��
        add(startButton);
        add(Box.createVerticalGlue()); // �U��ťձ���
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