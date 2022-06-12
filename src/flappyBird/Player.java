package flappyBird;

import java.awt.*;

/**
 * Contains information about the player (name, score, current position)
 *  @author Blicharz, Kaczynski, Filiciak
 *  @version 1.3
 */
public abstract class Player {
    /**
     * Name of the player
     */
    private final String name;
    private int playerY = GamePanel.SCREEN_HEIGHT / 2;
    private int score = 0;

    /**
     * Loads bird images.
     * @return true, if image has been loaded successfully, false if image hasn't been found or loaded incorrectly
     */
    public abstract boolean loadImage();

    /**
     *
     * @return bird basic image
     */
    public abstract Image getBird();

    /**
     *
     * @return bird flying up image
     */
    public abstract Image getBirdFly();

    /**
     *
     * @return bird diving image
     */
    public abstract Image getBirdDive();

    /**
     * Initializes player's name
     * @param name name for the player
     */
    public Player(String name){
        this.name = name;
    }

    /**
     *
     * @return name of the player
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return current y coordinate of the player
     */
    public int getPlayerY() {
        return playerY;
    }

    /**
     * Increases player's y coordinate by given number
     * @param unit number to increase y coordinate
     */
    public void addPlayerY(int unit) {
        this.playerY += unit;
    }

    /**
     * Decreases player's y coordinate by given number
     * @param unit number to decrease y coordinate
     */
    public void decPlayerY(int unit) {
        this.playerY -= unit;
    }

    /**
     * Sets player's y coordinate to given number
     * @param playerY new y coordinate
     */
    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    /**
     * X is final
     * @return x coordinate of the player
     */
    public int getPLAYERX() {
        return 100;
    }

    /**
     * Sets player's score to given number
     * @param score new player's score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     *
     * @return current score of the player
     */
    public int getScore() {
        return score;
    }
}
