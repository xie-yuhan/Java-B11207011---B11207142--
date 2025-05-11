<<<<<<< HEAD
package 星際大戰;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*; // 導入音效相關類別

public class GameOverPanel extends JPanel {
    private int finalScore;
    private Image backgroundImage;
    private java.util.function.Consumer<String> onSubmit; // 使用 Consumer<String> 接受名稱

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
=======
package 星際大戰;

import javax.swing.*;
import java.awt.*;
<<<<<<< HEAD
import javax.sound.sampled.*; // 導入音效相關類別
=======
import java.awt.event.*;
>>>>>>> e149a2d1556e47ff99f0bd23e0199a16c74576f4

public class GameOverPanel extends JPanel {
    private int finalScore;
    private Image backgroundImage;
<<<<<<< HEAD
    private java.util.function.Consumer<String> onSubmit; // 使用 Consumer<String> 接受名稱

    public GameOverPanel(int score, boolean earthDestroyed, java.util.function.Consumer<String> onSubmit) {
        this.finalScore = score;
        this.onSubmit = onSubmit;
        setLayout(new BorderLayout());
        backgroundImage = new ImageIcon(getClass().getResource("/星際大戰/gameover.jpg")).getImage();
        JLabel title = new JLabel("The Earth has been destroyed", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(Color.RED);
=======

    public GameOverPanel(int score, boolean earthDestroyed, Runnable onRestart) {
        this.finalScore = score;
        setLayout(new BorderLayout());

        // 背景圖
        backgroundImage = new ImageIcon(getClass().getResource("/星際大戰/gameover.jpg")).getImage();

        // ===== 上方：標題 =====
        JLabel title = new JLabel("The Earth has been destroyed", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(Color.RED);
        
>>>>>>> e149a2d1556e47ff99f0bd23e0199a16c74576f4
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

<<<<<<< HEAD
=======
        // ===== 中間空白區（可省略或放其他資訊）=====
>>>>>>> e149a2d1556e47ff99f0bd23e0199a16c74576f4
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);

<<<<<<< HEAD
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

    private void playGameOverSound() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/星際大戰/gameOver.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // 單次播放
        } catch (Exception e) {
            System.err.println("Failed to load or play game over sound: " + e.getMessage());
        }
=======
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
>>>>>>> e149a2d1556e47ff99f0bd23e0199a16c74576f4
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> e149a2d1556e47ff99f0bd23e0199a16c74576f4
>>>>>>> 60f338039a7d7b88f02b83bb3e248d518f3703aa
