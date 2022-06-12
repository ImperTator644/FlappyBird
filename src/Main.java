import flappyBird.Bird;
import flappyBird.GameFrame;
import flappyBird.Info;
import flappyBird.Player;

import javax.swing.*;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Main class of the game
 * @author Blicharz, Kaczynski, Filiciak
 * @version 1.11
 */
public class Main {
    private static Info info;

    /**
     * Responsible for getting player's information and bird color
     * Creates frame and starts the game.
     */
    public static void main(String[] args) {
        loadInfo();
        String playerName = JOptionPane.showInputDialog("Enter name: ");
        checkPlayer(playerName);
        JOptionPane.showMessageDialog(null, info.displayPlayerInfo(playerName));
        String color = JOptionPane.showInputDialog("Enter color {yellow, red, green}: ");
        Player player = new Bird(playerName, color);
        var frame = new GameFrame(player, info);
        frame.startGame();
    }

    /**
     * Deserializes saved state of object info.
     * Info contains information about saved players (name, high score, last activity).
     * Uses ObjectInputStream.
     *
     */
    private static void loadInfo() {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("src/info.dat"));
            info = (Info) input.readObject();
            input.close();
        } catch (EOFException eo) {
            info = new Info();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds player if not exists
     * @param player player's name
     */
    private static void checkPlayer(String player){
        if(!info.checkIfPlayerExists(player))
            info.addPlayer(player);
    }
}