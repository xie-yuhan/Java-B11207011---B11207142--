package �P�ڤj��;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*; // �ɤJ���Ĭ������O

/**
 * �C���q���e���A��ܳӧQ�T���B���ƨä��\���a��J�W�ٴ���C
 * �ӭ��O�]�t���ļ���\��A�æb�����Ĳ�o�^�աC
 */
public class GameCompletePanel extends JPanel {
    private int score; // ���a��o������
    private java.util.function.Consumer<String> onSubmit; // �ϥ� Consumer<String> �����W�ٴ���^��

    /**
     * �c�y�s���C���q���e���C
     *
     * @param score ���a��o������
     * @param onSubmit ����W�٫᪺�^�ը�ơA�������a��J���W��
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

    /**
     * ����ӧQ���ġC
     * ���ձq�귽���|���J�ü��� /�P�ڤj��/victory.wav�A�Y���ѫh��X���~�T���C
     */
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

    /**
     * �мg paintComponent ��k�A��R�¦�I���C
     *
     * @param g Graphics ����A�Ω�ø��
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}