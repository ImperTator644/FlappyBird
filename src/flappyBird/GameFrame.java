package flappyBird;

import javax.swing.*;

public class GameFrame extends JFrame {
    private final ImageIcon background = new ImageIcon("src/images/background.png");
    private final GamePanel panel = new GamePanel("", background, JLabel.CENTER);

    public GameFrame() {
        this.add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Flappy bird");
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void startGame() {
        panel.initGame();
        panel.startGame();
    }
}
