package flappyBird;

/**
 * defines obstacle state (position, gap size)
 */
public class Obstacle {
    private int obstaclePosX;
    private int obstaclePosY;

    public Obstacle() {
        this.obstaclePosX = 0;
        this.obstaclePosY = 0;
    }

    public int getObstaclePosX() {
        return obstaclePosX;
    }

    public int getObstaclePosY() {
        return obstaclePosY;
    }

    public void setObstaclePosX(int obstaclePosX) {
        this.obstaclePosX = obstaclePosX;
    }

    public void setObstaclePosY(int obstaclePosY) {
        this.obstaclePosY = obstaclePosY;
    }

    public void decObstacleX(int unit){
        this.obstaclePosX -= unit;
    }
}
