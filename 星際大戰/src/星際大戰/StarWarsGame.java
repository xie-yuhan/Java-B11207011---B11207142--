package �P�ڤj��;

import javax.swing.*;
public class StarWarsGame extends JFrame {
    public StarWarsGame() {
        setTitle("�P�ڤj�� - Space Wars");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // ��ܹC���ʭ����O
        GameCoverPanel coverPanel = new GameCoverPanel(() -> startGame());
        setContentPane(coverPanel);
        setVisible(true);
    }
    
    private void startGame() {
        // ������C�����O
        GamePanel gamePanel = new GamePanel();
        setContentPane(gamePanel);
        revalidate(); // ���s���ҩM��s�ɭ�
    }
    
    public static void main(String[] args) {
        new StarWarsGame();
    }
}