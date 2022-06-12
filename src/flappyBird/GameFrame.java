package flappyBird;

import javax.swing.*;

/**
 * Creates game frame, initializes game panel and starts the game.
 *  @author Blicharz, Kaczynski, Filiciak
 *  @version 1.0.7
 */
public class GameFrame extends JFrame {
    /**
     * Panel of the game
     */
    private final GamePanel panel;

    /**
     * Creates game frame
     * @param player object of Player class
     * @param info object of Info class, contains information about saved players
     */
    public GameFrame(Player player, Info info) {
        ImageIcon background = new ImageIcon("src/images/background.png");
        ImageIcon icon = new ImageIcon("src/images/birdFlyYellow.png");
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

    /**
     * Initializes game panel and starts the game.
     */
    public void startGame() {
        panel.initGame();
        panel.startGame();
    }
}
