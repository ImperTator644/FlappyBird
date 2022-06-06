package flappyBird;

import javax.swing.*;

public class GameFrame extends JFrame {
    private final GamePanel panel;

    /**
     * klasa odpowiadajaca za jedna klatke w grze
     * @param player stan gracza
     * @param info stan gry
     */
    public GameFrame(Player player, Info info,String color) {
        ImageIcon background = new ImageIcon("src/images/background.png");
        ImageIcon icon = new ImageIcon("src/images/birdFlyYellow.png");
        if(color.equals("beer")) background = new ImageIcon("src/images/backgroundTwo.png");
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
