import flappyBird.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String playerName = JOptionPane.showInputDialog("Enter name: ");
        Player player = new Player(playerName);
        var frame = new GameFrame(player);
        frame.startGame();
    }
}