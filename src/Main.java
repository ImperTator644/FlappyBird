import flappyBird.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String playerName = JOptionPane.showInputDialog("Enter name: ");
        Player player = new Player(playerName);
        var frame = new GameFrame(player);
        frame.startGame();
    }
    /*
    public static void main(String[] args) {
        Score score = new Score();
        System.out.println(score);
        score.insert(3, "Maciek");
        System.out.println(score);
        score.insert(1, "CEP");
        System.out.println(score);
        score.insert(12, "CEP");
        System.out.println(score);
    }
    */
}