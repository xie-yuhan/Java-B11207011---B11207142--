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

        loadLeaderboard();
        VolumePanel.loadVolume();
        
        showGameCoverPanel();
        setVisible(true);

        // ���U���f�����ƥ�A�O�s�Ʀ�]�ƾ�
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveLeaderboard();
                VolumePanel.saveVolume();
            }
        });
    }

    public void showGameCoverPanel() {
        GameCoverPanel coverPanel = new GameCoverPanel(() -> startGame(), this::showLeaderboard,this::showVolumePanel);
        setContentPane(coverPanel);
        revalidate();

        // ���s����I������
        playBackgroundMusic();
    }
    
    public void showVolumePanel() {
        VolumePanel volumePanel = new VolumePanel(this::showGameCoverPanel, this);
        setContentPane(volumePanel);
        revalidate();
    }
    
    public void setVolume(float volume) {
        if (backgroundMusicClip != null && backgroundMusicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = 20f * (float) Math.log10(Math.max(volume, 0.0001f)); // �קK log(0)
            gainControl.setValue(dB);
            System.out.println("�I�����֭��q�w�]��: " + volume + " (dB: " + dB + ")");
        }
    }

    public void showLeaderboard() {
        LeaderboardPanel leaderboardPanel = new LeaderboardPanel(this::showGameCoverPanel, leaderboard);
        setContentPane(leaderboardPanel);
        revalidate();
    }
    
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
   
           setVolume(VolumePanel.getVolume());
   
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

    public void addScore(String name, int score, boolean isGameComplete) {
        leaderboard.add(new ScoreEntry(name, score, isGameComplete));

        leaderboard.sort((a, b) -> {
            if (a.isGameComplete && !b.isGameComplete) return -1;
            if (!a.isGameComplete && b.isGameComplete) return 1;
            return Integer.compare(b.getScore(), a.getScore());
        });
        
        if (leaderboard.size() > 10) {
            leaderboard.subList(10, leaderboard.size()).clear();
        }
        saveLeaderboard();
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