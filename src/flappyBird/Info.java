package flappyBird;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * serialization stands for saving player, highscore and his last activities
 */
public class Info implements Serializable {
    private final HashMap<String, Integer> playerHighScore;
    private final HashMap<String, LocalDateTime> playerLastActivity;

    public Info() {
        playerHighScore = new HashMap<>();
        playerLastActivity = new HashMap<>();
    }

    public void addPlayer(String player) {
        playerHighScore.put(player, 0);
        playerLastActivity.put(player, LocalDateTime.now());
    }

    public boolean updatePlayersHighScore(String player, int score) {
        if (playerHighScore.get(player) < score) {
            playerHighScore.put(player, score);
            return true;
        }
        return false;
    }

    public void updatePlayerActivity(String player){
        playerLastActivity.put(player, LocalDateTime.now());
    }

    public boolean checkIfPlayerExists(String player){
        return playerHighScore.containsKey(player);
    }

    public String displayPlayerInfo(String player){
        return "imie: "+player+
                "\nhighscore: "+playerHighScore.get(player) +
                "\nLast activity: "+playerLastActivity.get(player).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
