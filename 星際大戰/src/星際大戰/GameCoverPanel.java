package �P�ڤj��;

import javax.swing.*;
import java.awt.*;

/**
 * �C�����ʭ��e���A��ܼ��D�P�\����s�C
 * ���Ѷ}�l�C���B�d�ݱƦ�]�P�վ㭵�q���J�f�C
 */
public class GameCoverPanel extends JPanel {
    private Image backgroundImage;

    /**
     * �c�y�s���C���ʭ��e���C
     *
     * @param onStartGame �}�l�C�����^�ը��
     * @param onShowLeaderboard ��ܱƦ�]���^�ը��
     * @param onShowVolume ��ܭ��q�վ�e�����^�ը��
     */
    public GameCoverPanel(Runnable onStartGame, Runnable onShowLeaderboard, Runnable onShowVolume) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        backgroundImage = new ImageIcon(getClass().getResource("/�P�ڤj��/gamecover.jpg")).getImage();

        JLabel titleLabel = new JLabel("�P�ڤj�� - Space Wars");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("�}�l�C��");
        startButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> onStartGame.run());

        JButton leaderboardButton = new JButton("�Ʀ�]");
        leaderboardButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.addActionListener(e -> onShowLeaderboard.run());

        JButton volumeButton = new JButton("���q�j�p");
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

    /**
     * �мg paintComponent ��k�Aø�s�I���Ϥ��C
     *
     * @param g Graphics ����A�Ω�ø��
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}