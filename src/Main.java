import flappyBird.Bird;
import flappyBird.GameFrame;
import flappyBird.Info;
import flappyBird.Player;

import javax.swing.*;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {
    private static Info info;

    public static void main(String[] args) {
        loadInfo();
        String playerName = JOptionPane.showInputDialog("Enter name: ");
        checkPlayer(playerName);
        JOptionPane.showMessageDialog(null, info.displayPlayerInfo(playerName));
        String color = JOptionPane.showInputDialog("Enter color {yellow, red, green}: ");
        Player player = new Bird(playerName, color);
        var frame = new GameFrame(player, info,color);
        frame.startGame();
    }

    private static void loadInfo() {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("src/info.dat"));
            info = (Info) input.readObject();
        } catch (EOFException eo) {
            info = new Info();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void checkPlayer(String player){
        if(!info.checkIfPlayerExists(player))
            info.addPlayer(player);
    }
}