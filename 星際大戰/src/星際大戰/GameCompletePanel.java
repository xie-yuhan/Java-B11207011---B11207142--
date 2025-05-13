package 星際大戰;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*; // 導入音效相關類別

/**
 * 遊戲通關畫面，顯示勝利訊息、分數並允許玩家輸入名稱提交。
 * 該面板包含音效播放功能，並在提交後觸發回調。
 */
public class GameCompletePanel extends JPanel {
    private int score; // 玩家獲得的分數
    private java.util.function.Consumer<String> onSubmit; // 使用 Consumer<String> 接受名稱提交回調

    /**
     * 構造新的遊戲通關畫面。
     *
     * @param score 玩家獲得的分數
     * @param onSubmit 提交名稱後的回調函數，接受玩家輸入的名稱
     */
    public GameCompletePanel(int score, java.util.function.Consumer<String> onSubmit) {
        this.score = score;
        this.onSubmit = onSubmit;
        setLayout(new BorderLayout());

        JLabel message = new JLabel("GAME COMPLETE! YOU WIN!", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 50));
        message.setForeground(Color.GREEN);
        add(message, BorderLayout.CENTER);

        JLabel scoreLabel = new JLabel("Your score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.BLACK);

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
        bottomPanel.add(inputPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        setBackground(Color.BLACK);

        // 播放勝利音效
        playVictorySound();
    }

    /**
     * 播放勝利音效。
     * 嘗試從資源路徑載入並播放 /星際大戰/victory.wav，若失敗則輸出錯誤訊息。
     */
    private void playVictorySound() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/星際大戰/victory.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // 單次播放
        } catch (Exception e) {
            System.err.println("Failed to load or play victory sound: " + e.getMessage());
        }
    }

    /**
     * 覆寫 paintComponent 方法，填充黑色背景。
     *
     * @param g Graphics 物件，用於繪圖
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}