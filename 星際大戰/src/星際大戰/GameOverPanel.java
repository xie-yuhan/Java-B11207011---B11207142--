package 星際大戰;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*; // 導入音效相關類別

/**
 * 遊戲結束畫面，顯示失敗訊息、分數並允許玩家輸入名稱提交。
 * 該面板在地球被摧毀時顯示，並播放失敗音效。
 */
public class GameOverPanel extends JPanel {
    private int finalScore; // 玩家最終獲得的分數
    private Image backgroundImage; // 遊戲結束的背景圖片
    private java.util.function.Consumer<String> onSubmit; // 使用 Consumer<String> 接受名稱提交回調

    /**
     * 構造新的遊戲結束畫面。
     *
     * @param score 玩家最終獲得的分數
     * @param earthDestroyed 是否地球被摧毀的標誌，決定顯示的訊息
     * @param onSubmit 提交名稱後的回調函數，接受玩家輸入的名稱
     */
    public GameOverPanel(int score, boolean earthDestroyed, java.util.function.Consumer<String> onSubmit) {
        this.finalScore = score;
        this.onSubmit = onSubmit;
        setLayout(new BorderLayout());
        backgroundImage = new ImageIcon(getClass().getResource("/星際大戰/gameover.jpg")).getImage();
        JLabel title = new JLabel("The Earth has been destroyed", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(Color.RED);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        JLabel scoreLabel = new JLabel("Your score: " + finalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 22));
        scoreLabel.setForeground(Color.WHITE);
        bottomPanel.add(scoreLabel, BorderLayout.NORTH);

        // 添加輸入名稱和提交按鈕
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        JTextField nameField = new JTextField(10);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                onSubmit.accept(name); // 傳遞名稱給回調
            }
        });
        inputPanel.add(new JLabel("Enter your name: "));
        inputPanel.add(nameField);
        inputPanel.add(submitButton);
        bottomPanel.add(inputPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // 播放失敗音效
        playGameOverSound();
    }

    /**
     * 播放遊戲結束音效。
     * 嘗試從資源路徑載入並播放 /星際大戰/gameOver.wav，若失敗則輸出錯誤訊息。
     */
    private void playGameOverSound() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/星際大戰/gameOver.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // 單次播放
        } catch (Exception e) {
            System.err.println("Failed to load or play game over sound: " + e.getMessage());
        }
    }

    /**
     * 覆寫 paintComponent 方法，繪製背景圖片。
     *
     * @param g Graphics 物件，用於繪圖
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}