package flappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Contains bird images
 *  @author Blicharz, Kaczynski, Filiciak
 *  @version 1.0
 */
public class Bird extends Player {
    /**
     * Color of the bird
     */
    private final String color;
    private Image bird;
    private Image birdFly;
    private Image birdDive;

    /**
     * Initializes player's name and color of the bird
     * @param name name for the player
     * @param color String, which defines bird color
     */
    public Bird(String name, String color) {
        super(name);
        this.color = color;
    }

    /**
     * Loads bird images.
     * @return true, if image has been loaded successfully, false if image hasn't been found or loaded incorrectly
     */
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

    /**
     *
     * @return bird basic image
     */
    public Image getBird() {
        return bird;
    }

    /**
     *
     * @return bird flying up image
     */
    public Image getBirdFly() {
        return birdFly;
    }

    /**
     *
     * @return bird diving image
     */
    public Image getBirdDive() {
        return birdDive;
    }
}
