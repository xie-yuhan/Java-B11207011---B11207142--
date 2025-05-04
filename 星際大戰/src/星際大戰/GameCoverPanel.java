package 星際大戰;

import javax.swing.*;
import java.awt.*;

public class GameCoverPanel extends JPanel {
    private Image backgroundImage;

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}