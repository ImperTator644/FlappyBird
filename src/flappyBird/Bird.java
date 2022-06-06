package flappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Bird extends Player {
    private String color = "";
    private Image bird;
    private Image birdFly;
    private Image birdDive;

    public Bird(String name, String color) {
        super(name);
        this.color = color;
    }

    public boolean loadImage(){
        boolean temp = false;
            try {
                switch (color) {
                    case "red" -> {
                        bird = ImageIO.read(new File("src/images/birdRed.png"));
                        birdFly = ImageIO.read(new File("src/images/birdFlyRed.png"));
                        birdDive = ImageIO.read(new File("src/images/birdDiveRed.png"));
                        temp = true;
                    }
                    case "green" -> {
                        bird = ImageIO.read(new File("src/images/birdGreen.png"));
                        birdFly = ImageIO.read(new File("src/images/birdFlyGreen.png"));
                        birdDive = ImageIO.read(new File("src/images/birdDiveGreen.png"));
                        temp = true;
                    }
                    case "beer" ->{
                        bird = ImageIO.read(new File("src/images/birdOne.png"));
                        birdFly = ImageIO.read(new File("src/images/birdThree.png"));
                        birdDive = ImageIO.read(new File("src/images/birdTwo.png"));
                        temp = true;
                    }
                    default -> {
                        bird = ImageIO.read(new File("src/images/birdYellow.png"));
                        birdFly = ImageIO.read(new File("src/images/birdFlyYellow.png"));
                        birdDive = ImageIO.read(new File("src/images/birdDiveYellow.png"));
                        temp = true;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        return temp;
    }

    public Image getBird() {
        return bird;
    }

    public Image getBirdFly() {
        return birdFly;
    }

    public Image getBirdDive() {
        return birdDive;
    }

    public String getColor() {
        return color;
    }
}
