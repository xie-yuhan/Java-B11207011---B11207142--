package �P�ڤj��;

import javax.swing.*;
import javax.sound.sampled.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.*;

public class StarWarsGame extends JFrame {
    private Clip backgroundMusicClip; // �@�ɪ��I������ Clip
    private ArrayList<ScoreEntry> leaderboard = new ArrayList<>(); // �x�s�Ʀ�]�ƾ�

    public StarWarsGame() {
        setTitle("�P�ڤj�� - Space Wars");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ���J�Ʀ�]�ƾ�
        loadLeaderboard();
        // ��l�ƨ���� GameCoverPanel
        showGameCoverPanel();
        setVisible(true);

        // ���U���f�����ƥ�A�O�s�Ʀ�]�ƾ�
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

        // ���s����I������
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

            URL url = getClass().getResource("/�P�ڤj��/backgroundMusic.wav");
            if (url == null) {
                System.err.println("Resource not found: /�P�ڤj��/backgroundMusic.wav");
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

    // �K�[���ƨ�Ʀ�]�ñƧ�
    public void addScore(String name, int score, boolean isGameComplete) {
        leaderboard.add(new ScoreEntry(name, score, isGameComplete));
        // ���q�����A�M���ƱƧǡG�q���u���A���ƭ���
        leaderboard.sort((a, b) -> {
            if (a.isGameComplete && !b.isGameComplete) return -1;
            if (!a.isGameComplete && b.isGameComplete) return 1;
            return Integer.compare(b.getScore(), a.getScore());
        });
        // ����Ʀ�]�̦h 10 ���O��
        if (leaderboard.size() > 10) {
            leaderboard.subList(10, leaderboard.size()).clear();
        }
        saveLeaderboard(); // �ߧY�O�s��s
    }

    // �O�s�Ʀ�]���ɮ�
    private void saveLeaderboard() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("leaderboard.dat"))) {
            oos.writeObject(leaderboard);
        } catch (IOException e) {
            System.err.println("Failed to save leaderboard: " + e.getMessage());
        }
    }

    // ���J�Ʀ�]�q�ɮ�
    @SuppressWarnings("unchecked")
    private void loadLeaderboard() {
        File file = new File("leaderboard.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                leaderboard = (ArrayList<ScoreEntry>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to load leaderboard: " + e.getMessage());
                leaderboard = new ArrayList<>(); // ���J���ѫh��l�ƪŦC��
            }
        }
    }

    // �������Ω��x�s���Ʊ��ءA�]�� public static �H�K��L���s��
    public static class ScoreEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int score;
        private boolean isGameComplete; // �O���O�_�q��

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