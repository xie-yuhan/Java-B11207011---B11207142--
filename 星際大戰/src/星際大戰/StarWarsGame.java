package 星際大戰;

import javax.swing.*;
import javax.sound.sampled.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.*;

/**
 * 整個星際大戰遊戲的主框架，負責遊戲初始化、畫面切換及排行榜管理。
 * 提供背景音樂播放、分數儲存與載入功能。
 */
public class StarWarsGame extends JFrame {
    private Clip backgroundMusicClip; // 共享的背景音樂 Clip
    private ArrayList<ScoreEntry> leaderboard = new ArrayList<>(); // 儲存排行榜數據

    /**
     * 構造新的星際大戰遊戲實例。
     * 初始化窗口、載入排行榜並顯示封面畫面。
     */
    public StarWarsGame() {
        setTitle("星際大戰 - Space Wars");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadLeaderboard();
        VolumePanel.loadVolume();
        
        showGameCoverPanel();
        setVisible(true);

        // 註冊窗口關閉事件，保存排行榜數據
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveLeaderboard();
                VolumePanel.saveVolume();
            }
        });
    }

    /**
     * 顯示遊戲封面畫面。
     * 設定封面面板並播放背景音樂。
     */
    public void showGameCoverPanel() {
        GameCoverPanel coverPanel = new GameCoverPanel(() -> startGame(), this::showLeaderboard, this::showVolumePanel);
        setContentPane(coverPanel);
        revalidate();

        // 重新播放背景音樂
        playBackgroundMusic();
    }
    
    /**
     * 顯示音量調整畫面。
     * 設定音量調整面板並刷新畫面。
     */
    public void showVolumePanel() {
        VolumePanel volumePanel = new VolumePanel(this::showGameCoverPanel, this);
        setContentPane(volumePanel);
        revalidate();
    }
    
    /**
     * 設定背景音樂的音量。
     *
     * @param volume 音量值（0.0 到 1.0）
     */
    public void setVolume(float volume) {
        if (backgroundMusicClip != null && backgroundMusicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = 20f * (float) Math.log10(Math.max(volume, 0.0001f)); // 避免 log(0)
            gainControl.setValue(dB);
            System.out.println("背景音樂音量已設為: " + volume + " (dB: " + dB + ")");
        }
    }

    /**
     * 顯示排行榜畫面。
     * 設定排行榜面板並刷新畫面。
     */
    public void showLeaderboard() {
        LeaderboardPanel leaderboardPanel = new LeaderboardPanel(this::showGameCoverPanel, leaderboard);
        setContentPane(leaderboardPanel);
        revalidate();
    }
    
    /**
     * 儲存排行榜數據到檔案。
     */
    private void saveLeaderboard() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("leaderboard.dat"))) {
            oos.writeObject(leaderboard);
        } catch (IOException e) {
            System.err.println("Failed to save leaderboard: " + e.getMessage());
        }
    }

    /**
     * 從檔案載入排行榜數據。
     * 若載入失敗，初始化為空列表。
     */
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

    /**
     * 播放背景音樂。
     * 載入 /星際大戰/backgroundMusic.wav 並循環播放，失敗時輸出錯誤訊息。
     */
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
     * 獲取背景音樂剪輯。
     *
     * @return 背景音樂的 Clip 物件
     */
    public Clip getBackgroundMusicClip() {
        return backgroundMusicClip;
    }

    /**
     * 開始遊戲。
     * 設定遊戲主面板並刷新畫面。
     */
    public void startGame() {
        GamePanel gamePanel = new GamePanel(this, backgroundMusicClip);
        setContentPane(gamePanel);
        revalidate();
    }

    /**
     * 添加新的分數條目到排行榜。
     * 根據通關狀態和分數排序，限制為前 10 名並保存。
     *
     * @param name 玩家名稱
     * @param score 玩家分數
     * @param isGameComplete 是否通關標誌
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
    
    

    // 內部類用於儲存分數條目，設為 public static 以便其他類存取
    /**
     * 儲存分數條目的內部類，實現序列化以支持檔案儲存。
     */
    public static class ScoreEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name; // 玩家名稱
        private int score; // 玩家分數
        private boolean isGameComplete; // 記錄是否通關

        /**
         * 構造新的分數條目。
         *
         * @param name 玩家名稱
         * @param score 玩家分數
         * @param isGameComplete 是否通關標誌
         */
        public ScoreEntry(String name, int score, boolean isGameComplete) {
            this.name = name;
            this.score = score;
            this.isGameComplete = isGameComplete;
        }

        /**
         * 獲取玩家分數。
         *
         * @return 玩家分數
         */
        public int getScore() {
            return score;
        }

        /**
         * 檢查是否通關。
         *
         * @return true 如果通關，否則 false
         */
        public boolean isGameComplete() {
            return isGameComplete;
        }

        /**
         * 將分數條目轉為字串表示。
         *
         * @return 包含名稱、分數及通關狀態的字串
         */
        @Override
        public String toString() {
            return name + ": " + score + (isGameComplete ? " (Game Complete)" : " (Game Over)");
        }
    }

    /**
     * 程式進入點，啟動遊戲。
     *
     * @param args 命令列參數（未使用）
     */
    public static void main(String[] args) {
        new StarWarsGame();
    }
}