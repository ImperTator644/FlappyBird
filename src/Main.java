import flappyBird.Bird;
import flappyBird.GameFrame;
import flappyBird.Player;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String playerName = JOptionPane.showInputDialog("Enter name: ");
        String color = JOptionPane.showInputDialog("Enter color {yellow, red, green}: ");
        Player player = new Bird(playerName, color);
        var frame = new GameFrame(player);
        frame.startGame();
    }
}