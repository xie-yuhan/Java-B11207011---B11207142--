package 星際大戰;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

/**
 * 音量調整畫面，允許玩家調整背景音樂音量並保存設置。
 * 提供滑塊調整音量並返回主選單的功能。
 */
public class VolumePanel extends JPanel {
    private static float volume = 1.0f; // 音量值，範圍 0.0 ~ 1.0
    private JSlider volumeSlider; // 音量調整滑塊
    private Image backgroundImage; // 背景圖片

    /**
     * 構造新的音量調整畫面。
     *
     * @param onBack 返回主選單的回調函數
     * @param game 主遊戲框架，用於設定音量
     */
    public VolumePanel(Runnable onBack, StarWarsGame game) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // 確保圖片能正常加載
        backgroundImage = new ImageIcon(getClass().getResource("/星際大戰/volume.jpg")).getImage();
        
        // 設定元件
        JLabel title = new JLabel("音量調整");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        volumeSlider = new JSlider(0, 100, (int) (volume * 100));
        volumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        
        volumeSlider.setPreferredSize(new Dimension(400, 50));
        volumeSlider.setMaximumSize(new Dimension(400, 50));

        volumeSlider.addChangeListener(e -> {
            volume = volumeSlider.getValue() / 100f;
            game.setVolume(volume);
            System.out.println("音量：" + volume);
        });

        JButton backButton = new JButton("返回");
        backButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 20));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> onBack.run());

        add(Box.createRigidArea(new Dimension(0, 100)));
        add(title);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(volumeSlider);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(backButton);
    }

    /**
     * 覆寫 paintComponent 方法，繪製背景圖片。
     * 背景圖片會填滿整個面板。
     *
     * @param g Graphics 物件，用於繪圖
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 繪製背景圖像，填滿整個面板
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * 獲取當前音量值。
     *
     * @return 音量值，範圍 0.0 到 1.0
     */
    public static float getVolume() {
        return volume;
    }

    /**
     * 設定音量值。
     *
     * @param v 音量值，範圍 0.0 到 1.0
     */
    public static void setVolume(float v) {
        volume = v;
    }
    
    /**
     * 儲存音量設置到檔案。
     * 將音量值以整數形式（0~100）寫入 volume.dat。
     */
    public static void saveVolume() {
        try (FileOutputStream fos = new FileOutputStream("volume.dat")) {
            fos.write((int) (volume * 100));
        } catch (IOException e) {
            System.err.println("Failed to save volume: " + e.getMessage());
        }
    }

    /**
     * 從檔案載入音量設置。
     * 從 volume.dat 讀取整數值（0~100）並轉換為浮點音量值。
     */
    public static void loadVolume() {
        File file = new File("volume.dat");
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                int volumeInt = fis.read(); // 0~100
                float volume = volumeInt / 100f;
                setVolume(volume);  // 設定 VolumePanel 內的靜態變數
            } catch (IOException e) {
                System.err.println("Failed to load volume: " + e.getMessage());
            }
        }
    }
}