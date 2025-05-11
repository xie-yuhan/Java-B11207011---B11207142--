package 星際大戰;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*; // 導入音效相關類別

public class GameCompletePanel extends JPanel {
    private int score;
    private java.util.function.Consumer<String> onSubmit; // 使用 Consumer<String> 接受名稱

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
