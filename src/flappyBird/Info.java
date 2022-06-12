package flappyBird;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Implements interface Serializable.
 * Contains information about player's high score and his last activity (date and time)
 * @author Blicharz
 * @version 1.0.1
 */
public class Info implements Serializable {
    /**
     * Key = player's name
     * Object = player's personal high score
     */
    private final HashMap<String, Integer> playerHighScore;
    /**
     * Key = player's name
     * Object = player's last activity
     */
    private final HashMap<String, LocalDateTime> playerLastActivity;

    /**
     * Initializes playerHighScore and playerLastActivity hash maps.
     */
    public Info() {
        playerHighScore = new HashMap<>();
        playerLastActivity = new HashMap<>();
    }

    /**
     * Adds new player to hash maps.
     * @param player name of the new player
     */
    public void addPlayer(String player) {
        playerHighScore.put(player, 0);
        playerLastActivity.put(player, LocalDateTime.now());
    }

    /**
     * Updates player high score if it is higher than his current personal best score
     * @param player name of the player to update
     * @param score score of player's last game
     * @return true, if player high score has been updated, false if it hasn't been.
     */
    public boolean updatePlayersHighScore(String player, int score) {
        if (playerHighScore.get(player) < score) {
            playerHighScore.put(player, score);
            return true;
        }
        return false;
    }

    /**
     * Updates player's last activity
     * @param player name of the player to update
     */
    public void updatePlayerActivity(String player){
        playerLastActivity.put(player, LocalDateTime.now());
    }

    /**
     * Checks if player already exists
     * @param player name of the player to check
     * @return true if player exists, false if he doesn't exist
     */
    public boolean checkIfPlayerExists(String player){
        return playerHighScore.containsKey(player);
    }

    /**
     * Displays information about the player
     * @param player name of the player to display
     * @return text representation of "name: playerName, highscore: playerHighScore, Last activity: playerLastActivity"
     */
    public String displayPlayerInfo(String player){
        return "imie: "+player+
                "\nhighscore: "+playerHighScore.get(player) +
                "\nLast activity: "+playerLastActivity.get(player).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
