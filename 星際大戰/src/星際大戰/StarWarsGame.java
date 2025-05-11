package 星際大戰;

import javax.swing.*;
import javax.sound.sampled.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.*;

public class StarWarsGame extends JFrame {
    private Clip backgroundMusicClip; // 共享的背景音樂 Clip
    private ArrayList<ScoreEntry> leaderboard = new ArrayList<>(); // 儲存排行榜數據

    public StarWarsGame() {
        setTitle("星際大戰 - Space Wars");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 載入排行榜數據
        loadLeaderboard();
        // 初始化並顯示 GameCoverPanel
        showGameCoverPanel();
        setVisible(true);

        // 註冊窗口關閉事件，保存排行榜數據
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveLeaderboard();
            }
        });
    }

    public void showGameCoverPanel() {
        GameCoverPanel coverPanel = new GameCoverPanel(() -> startGame(), this::showLeaderboard);
        setContentPane(coverPanel);
        revalidate();

        // 重新播放背景音樂
        playBackgroundMusic();
    }
    
    public void showLeaderboard() {
        LeaderboardPanel leaderboardPanel = new LeaderboardPanel(this::showGameCoverPanel, leaderboard);
        setContentPane(leaderboardPanel);
        revalidate();
    }

    private void playBackgroundMusic() {
        try {
            if (backgroundMusicClip != null) {
                if (backgroundMusicClip.isRunning()) {
                    backgroundMusicClip.stop();
                }
                backgroundMusicClip.close();
            }

            URL url = getClass().getResource("/星際大戰/backgroundMusic.wav");
            if (url == null) {
                System.err.println("Resource not found: /星際大戰/backgroundMusic.wav");
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioStream);
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusicClip.start();
            System.out.println("Background music loaded and playing.");
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio format for background music: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to load or play background music: " + e.getMessage());
        }
    }

    public Clip getBackgroundMusicClip() {
        return backgroundMusicClip;
    }

    public void startGame() {
        GamePanel gamePanel = new GamePanel(this, backgroundMusicClip);
        setContentPane(gamePanel);
        revalidate();
    }

    // 添加分數到排行榜並排序
    public void addScore(String name, int score, boolean isGameComplete) {
        leaderboard.add(new ScoreEntry(name, score, isGameComplete));
        // 按通關狀態和分數排序：通關優先，分數降序
        leaderboard.sort((a, b) -> {
            if (a.isGameComplete && !b.isGameComplete) return -1;
            if (!a.isGameComplete && b.isGameComplete) return 1;
            return Integer.compare(b.getScore(), a.getScore());
        });
        // 限制排行榜最多 10 筆記錄
        if (leaderboard.size() > 10) {
            leaderboard.subList(10, leaderboard.size()).clear();
        }
        saveLeaderboard(); // 立即保存更新
    }

    // 保存排行榜到檔案
    private void saveLeaderboard() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("leaderboard.dat"))) {
            oos.writeObject(leaderboard);
        } catch (IOException e) {
            System.err.println("Failed to save leaderboard: " + e.getMessage());
        }
    }

    // 載入排行榜從檔案
    @SuppressWarnings("unchecked")
    private void loadLeaderboard() {
        File file = new File("leaderboard.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                leaderboard = (ArrayList<ScoreEntry>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to load leaderboard: " + e.getMessage());
                leaderboard = new ArrayList<>(); // 載入失敗則初始化空列表
            }
        }
    }

    // 內部類用於儲存分數條目，設為 public static 以便其他類存取
    public static class ScoreEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int score;
        private boolean isGameComplete; // 記錄是否通關

        public ScoreEntry(String name, int score, boolean isGameComplete) {
            this.name = name;
            this.score = score;
            this.isGameComplete = isGameComplete;
        }

        public int getScore() {
            return score;
        }

        public boolean isGameComplete() {
            return isGameComplete;
        }

        @Override
        public String toString() {
            return name + ": " + score + (isGameComplete ? " (Game Complete)" : " (Game Over)");
        }
    }

    public static void main(String[] args) {
        new StarWarsGame();
    }
}