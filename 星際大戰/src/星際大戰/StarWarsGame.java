package �P�ڤj��;

import javax.swing.*;
import javax.sound.sampled.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.*;

/**
 * ��ӬP�ڤj�ԹC�����D�ج[�A�t�d�C����l�ơB�e�������αƦ�]�޲z�C
 * ���ѭI�����ּ���B�����x�s�P���J�\��C
 */
public class StarWarsGame extends JFrame {
    private Clip backgroundMusicClip; // �@�ɪ��I������ Clip
    private ArrayList<ScoreEntry> leaderboard = new ArrayList<>(); // �x�s�Ʀ�]�ƾ�

    /**
     * �c�y�s���P�ڤj�ԹC����ҡC
     * ��l�Ƶ��f�B���J�Ʀ�]����ܫʭ��e���C
     */
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

    /**
     * ��ܹC���ʭ��e���C
     * �]�w�ʭ����O�ü���I�����֡C
     */
    public void showGameCoverPanel() {
        GameCoverPanel coverPanel = new GameCoverPanel(() -> startGame(), this::showLeaderboard, this::showVolumePanel);
        setContentPane(coverPanel);
        revalidate();

        // ���s����I������
        playBackgroundMusic();
    }
    
    /**
     * ��ܭ��q�վ�e���C
     * �]�w���q�վ㭱�O�è�s�e���C
     */
    public void showVolumePanel() {
        VolumePanel volumePanel = new VolumePanel(this::showGameCoverPanel, this);
        setContentPane(volumePanel);
        revalidate();
    }
    
    /**
     * �]�w�I�����֪����q�C
     *
     * @param volume ���q�ȡ]0.0 �� 1.0�^
     */
    public void setVolume(float volume) {
        if (backgroundMusicClip != null && backgroundMusicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = 20f * (float) Math.log10(Math.max(volume, 0.0001f)); // �קK log(0)
            gainControl.setValue(dB);
            System.out.println("�I�����֭��q�w�]��: " + volume + " (dB: " + dB + ")");
        }
    }

    /**
     * ��ܱƦ�]�e���C
     * �]�w�Ʀ�]���O�è�s�e���C
     */
    public void showLeaderboard() {
        LeaderboardPanel leaderboardPanel = new LeaderboardPanel(this::showGameCoverPanel, leaderboard);
        setContentPane(leaderboardPanel);
        revalidate();
    }
    
    /**
     * �x�s�Ʀ�]�ƾڨ��ɮסC
     */
    private void saveLeaderboard() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("leaderboard.dat"))) {
            oos.writeObject(leaderboard);
        } catch (IOException e) {
            System.err.println("Failed to save leaderboard: " + e.getMessage());
        }
    }

    /**
     * �q�ɮ׸��J�Ʀ�]�ƾڡC
     * �Y���J���ѡA��l�Ƭ��ŦC��C
     */
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

    /**
     * ����I�����֡C
     * ���J /�P�ڤj��/backgroundMusic.wav �ô`������A���Ѯɿ�X���~�T���C
     */
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

    /**
     * ����I�����ְſ�C
     *
     * @return �I�����֪� Clip ����
     */
    public Clip getBackgroundMusicClip() {
        return backgroundMusicClip;
    }

    /**
     * �}�l�C���C
     * �]�w�C���D���O�è�s�e���C
     */
    public void startGame() {
        GamePanel gamePanel = new GamePanel(this, backgroundMusicClip);
        setContentPane(gamePanel);
        revalidate();
    }

    /**
     * �K�[�s�����Ʊ��ب�Ʀ�]�C
     * �ھڳq�����A�M���ƱƧǡA����e 10 �W�ëO�s�C
     *
     * @param name ���a�W��
     * @param score ���a����
     * @param isGameComplete �O�_�q���лx
     */
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
    /**
     * �x�s���Ʊ��ت��������A��{�ǦC�ƥH����ɮ��x�s�C
     */
    public static class ScoreEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name; // ���a�W��
        private int score; // ���a����
        private boolean isGameComplete; // �O���O�_�q��

        /**
         * �c�y�s�����Ʊ��ءC
         *
         * @param name ���a�W��
         * @param score ���a����
         * @param isGameComplete �O�_�q���лx
         */
        public ScoreEntry(String name, int score, boolean isGameComplete) {
            this.name = name;
            this.score = score;
            this.isGameComplete = isGameComplete;
        }

        /**
         * ������a���ơC
         *
         * @return ���a����
         */
        public int getScore() {
            return score;
        }

        /**
         * �ˬd�O�_�q���C
         *
         * @return true �p�G�q���A�_�h false
         */
        public boolean isGameComplete() {
            return isGameComplete;
        }

        /**
         * �N���Ʊ����ର�r���ܡC
         *
         * @return �]�t�W�١B���Ƥγq�����A���r��
         */
        @Override
        public String toString() {
            return name + ": " + score + (isGameComplete ? " (Game Complete)" : " (Game Over)");
        }
    }

    /**
     * �{���i�J�I�A�ҰʹC���C
     *
     * @param args �R�O�C�Ѽơ]���ϥΡ^
     */
    public static void main(String[] args) {
        new StarWarsGame();
    }
}