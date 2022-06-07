package flappyBird;

import java.awt.*;

public abstract class Player {
    private final String name;
    private int playerY = GamePanel.SCREEN_HEIGHT / 2;
    private int score = 0;

    public abstract boolean loadImage();

    public Player(String name){
        this.name = name;
    }

    public abstract Image getBird();

    public abstract Image getBirdFly();

    public abstract Image getBirdDive();


    public String getName() {
        return name;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void addPlayerY(int unit) {
        this.playerY += unit;
    }

    public void decPlayerY(int unit) {
        this.playerY -= unit;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getPLAYERX() {
        return 100;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
