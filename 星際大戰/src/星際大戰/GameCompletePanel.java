package �P�ڤj��;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*; // �ɤJ���Ĭ������O

public class GameCompletePanel extends JPanel {
    private int score;
    private java.util.function.Consumer<String> onSubmit; // �ϥ� Consumer<String> �����W��

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

        // �K�[��J�W�٩M������s
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        JTextField nameField = new JTextField(10);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                onSubmit.accept(name); // �ǻ��W�ٵ��^��
            }
        });
        inputPanel.add(new JLabel("Enter your name: "));
        inputPanel.add(nameField);
        inputPanel.add(submitButton);
        bottomPanel.add(inputPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        setBackground(Color.BLACK);

        // ����ӧQ����
        playVictorySound();
    }

    private void playVictorySound() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/�P�ڤj��/victory.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // �榸����
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
