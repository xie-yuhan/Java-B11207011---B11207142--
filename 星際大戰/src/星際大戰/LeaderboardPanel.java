package 星際大戰;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LeaderboardPanel extends JPanel {
    public LeaderboardPanel(Runnable onBack, ArrayList<StarWarsGame.ScoreEntry> leaderboard) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // 顯示排行榜
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        JLabel title = new JLabel("Leaderboard", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(20));

        for (int i = 0; i < Math.min(10, leaderboard.size()); i++) {
            JLabel entry = new JLabel((i + 1) + ". " + leaderboard.get(i));
            entry.setFont(new Font("Serif", Font.PLAIN, 18));
            entry.setForeground(Color.WHITE);
            listPanel.add(entry);
        }

        add(listPanel, BorderLayout.CENTER);

        // 返回按鈕
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        backButton.addActionListener(e -> onBack.run());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}