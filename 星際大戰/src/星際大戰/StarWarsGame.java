package 星際大戰;

import javax.swing.*;
public class StarWarsGame extends JFrame {
    public StarWarsGame() {
        setTitle("星際大戰 - Space Wars");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 顯示遊戲封面面板
        GameCoverPanel coverPanel = new GameCoverPanel(() -> startGame());
        setContentPane(coverPanel);
        setVisible(true);
    }
    
    private void startGame() {
        // 切換到遊戲面板
        GamePanel gamePanel = new GamePanel();
        setContentPane(gamePanel);
        revalidate(); // 重新驗證和刷新界面
    }
    
    public static void main(String[] args) {
        new StarWarsGame();
    }
}