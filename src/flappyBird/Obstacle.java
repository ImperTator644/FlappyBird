package flappyBird;

/**
 * Defines obstacle state (position, gap size)
 * @author Filiciak
 * @version 25.1.2
 */
public class Obstacle {
    private int obstaclePosX;
    private int obstaclePosY;

    /**
     * Initializes coordinates x and y
     */
    public Obstacle() {
        this.obstaclePosX = 0;
        this.obstaclePosY = 0;
    }

    /**
     *
     * @return current x coordinate
     */
    public int getObstaclePosX() {
        return obstaclePosX;
    }

    /**
     *
     * @return current y coordinate
     */
    public int getObstaclePosY() {
        return obstaclePosY;
    }

    /**
     * Sets obstacle x coordinate
     * @param obstaclePosX new x coordinate
     */
    public void setObstaclePosX(int obstaclePosX) {
        this.obstaclePosX = obstaclePosX;
    }

    /**
     * Sets obstacle y coordinate
     * @param obstaclePosY new y coordinate
     */
    public void setObstaclePosY(int obstaclePosY) {
        this.obstaclePosY = obstaclePosY;
    }

    /**
     * Decreases x coordinate by a given number
     * @param unit number by which x coordinate is supposed to decrease
     */
    public void decObstacleX(int unit){
        this.obstaclePosX -= unit;
    }
}
