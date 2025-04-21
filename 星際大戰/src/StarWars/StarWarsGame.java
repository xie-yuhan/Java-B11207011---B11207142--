package StarWars;

import javax.swing.*;

public class StarWarsGame extends JFrame {
    private GamePanel gamePanel;

    public StarWarsGame() {
        setTitle("Star Wars Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new StarWarsGame();
    }
}
