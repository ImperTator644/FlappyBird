package flappyBird;

import javax.swing.*;

public class GameFrame extends JFrame {
    private final GamePanel panel;
    private final ImageIcon background = new ImageIcon("src/images/background.png");
    private final ImageIcon icon = new ImageIcon("src/images/birdFlyYellow.png");

    public GameFrame(Player player, Info info) {
        panel = new GamePanel("", background, JLabel.CENTER, player, info);
        this.add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Flappy bird");
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(icon.getImage());
    }

    public void startGame() {
        panel.initGame();
        panel.startGame();
    }
}
