package flappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Player {
    private final String name;
    private Image image;
    private final int PLAYERX            = 100;
    private int playerY = GamePanel.SCREEN_HEIGHT / 2;

    public Player(String name){
        this.name = name;
    }

    public boolean loadImage(){
        boolean temp = false;
        try{
            image = ImageIO.read(new File("src/images/bird.png"));
            temp = true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return temp;
    }

    public Image getImage() {
        return image;
    }

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
        return PLAYERX;
    }
}
