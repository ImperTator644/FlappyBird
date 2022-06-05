package flappyBird;

import javax.swing.*;

public class GameFrame extends JFrame {
    private final GamePanel panel;

<<<<<<< HEAD
    public GameFrame(Player player, Info info) {
        panel = new GamePanel("", background, JLabel.CENTER, player, info);
=======
    public GameFrame(Player player) {
        ImageIcon background = new ImageIcon("src/images/background.png");
        ImageIcon icon = new ImageIcon("src/images/birdFlyYellow.png");
        panel = new GamePanel("", background, JLabel.CENTER, player);
>>>>>>> 310118ca63818d2b72ba147b91878eeb625045c7
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
