package flappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Random;
import java.util.Queue;


public class GamePanel extends JLabel implements ActionListener {

    protected enum Movement {NONE, UP, DOWN}

    protected enum ExitReset {RESET, EXIT,NONE}

    private GamePanel.Movement movement = GamePanel.Movement.NONE;

    private final Player player;
    private final Score highScores;
    //private final Obstacle obstacle;
    protected static final int SCREEN_WIDTH = 500;
    protected static final int SCREEN_HEIGHT = 700;
    protected static final int UNIT_SIZE = 10;
    protected static final int PLAYER_SIZE = 3 * UNIT_SIZE;
    protected static int gap = 20 * UNIT_SIZE;
    private int level = 1;
    protected static final int MOVE_TIMER_DELAY = 100;
    protected static final int FLY_TIMER_DELAY = 15;
    protected static final int TIMER_DELAY = 30;
    private boolean running             = false;
    private boolean jump                = false;
    private boolean restart             = false;
    private boolean exit                = false;
    private boolean checkLevel          = false;
    private boolean dive                = false;
    private boolean checkScore          = true;
    private final boolean isImage;
    private Timer gameTimer;
    private Timer jumpTimer;
    private Timer flyTimer;
    private final Random random;
    private final Queue<Obstacle> obstacles;

    public GamePanel(String s, ImageIcon background, int center, Player player) {
        super(s,background,center);
        obstacles = new ArrayDeque<>();
        //this.obstacle = new Obstacle();
        this.player = player;
        this.highScores = new Score();
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        isImage = player.loadImage();
    }

    public void initGame() {
        addKeyPressedBinding("up.pressed", KeyEvent.VK_W, new MoveUDAction(GamePanel.Movement.UP));
        addKeyReleasedBinding("up.released", KeyEvent.VK_W, new MoveUDAction(GamePanel.Movement.NONE));
        addKeyPressedBinding("down.pressed", KeyEvent.VK_S, new MoveUDAction(GamePanel.Movement.DOWN));
        addKeyReleasedBinding("down.released", KeyEvent.VK_S, new MoveUDAction(GamePanel.Movement.NONE));
        addKeyPressedBinding("up.pressed", KeyEvent.VK_UP, new MoveUDAction(GamePanel.Movement.UP));
        addKeyReleasedBinding("up.released", KeyEvent.VK_UP, new MoveUDAction(GamePanel.Movement.NONE));
        addKeyPressedBinding("down.pressed", KeyEvent.VK_DOWN, new MoveUDAction(GamePanel.Movement.DOWN));
        addKeyReleasedBinding("down.released", KeyEvent.VK_DOWN, new MoveUDAction(GamePanel.Movement.NONE));
        addKeyPressedBinding("exit.pressed", KeyEvent.VK_ESCAPE, new ResetGame(ExitReset.EXIT));
        addKeyReleasedBinding("exit.released", KeyEvent.VK_ESCAPE, new ResetGame(ExitReset.NONE));
        addKeyPressedBinding("reset.pressed", KeyEvent.VK_R, new ResetGame(ExitReset.RESET));


        gameTimer = new Timer(TIMER_DELAY, e -> {
            if (!restart) {
                if (!jump) {
                    if(dive) player.addPlayerY(UNIT_SIZE * 2);
                    else player.addPlayerY(UNIT_SIZE);
                }
                if (running) {
                    obstacleMove();
                    checkCollisions();
                }
            } else {
                level = 1;
                gameTimer.setDelay(TIMER_DELAY);
                gap = 20 * UNIT_SIZE;
                player.setPlayerY(SCREEN_HEIGHT / 2);
                player.setScore(0);
                checkLevel = false;
                restart = false;
                running = true;
                checkScore = true;
                obstacles.clear();
                newObstacle();
            }
            repaint();
        });
        jumpTimer = new Timer(MOVE_TIMER_DELAY, this);

        flyTimer = new Timer(FLY_TIMER_DELAY, e -> {
            if (jump) player.decPlayerY(UNIT_SIZE);
            repaint();
        });
    }

    public void startGame() {
        newObstacle();
        running = true;
        gameTimer.start();
        jumpTimer.start();
        flyTimer.start();
    }

    protected void addKeyPressedBinding(String name, int keyCode, Action action) {
        KeyStroke ks = KeyStroke.getKeyStroke(keyCode, 0, false);
        addKeyBinding(name, ks, action);
    }

    protected void addKeyReleasedBinding(String name, int keyCode, Action action) {
        KeyStroke ks = KeyStroke.getKeyStroke(keyCode, 0, true);
        addKeyBinding(name, ks, action);
    }

    protected void addKeyBinding(String name, KeyStroke ks, Action action) {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(ks, name);
        am.put(name, action);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            if(isImage) {
                if(jump) g.drawImage(player.getBirdFly(),player.getPLAYERX(),player.getPlayerY(),PLAYER_SIZE,PLAYER_SIZE,this);
                else if(dive) g.drawImage(player.getBirdDive(),player.getPLAYERX(),player.getPlayerY(),PLAYER_SIZE,PLAYER_SIZE,this);
                else g.drawImage(player.getBird(),player.getPLAYERX(),player.getPlayerY(),PLAYER_SIZE,PLAYER_SIZE,this);
            }
            else g.fillRect(player.getPLAYERX(), player.getPlayerY(), PLAYER_SIZE, PLAYER_SIZE);
            
            g.setColor(new Color(34,69,6));
            obstacles.forEach(obstacle -> {
                g.fillRect(obstacle.getObstaclePosX(), 0, UNIT_SIZE * 2, obstacle.getObstaclePosY());
                g.fillRect(obstacle.getObstaclePosX(), obstacle.getObstaclePosY()+ gap, UNIT_SIZE * 2, SCREEN_HEIGHT);
            });

            g.setColor(Color.red);
            g.setFont(new Font("Showcard gothic", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Player: " + player.getName(), 0, g.getFont().getSize());
            g.drawString("Level: " + level, 0, 50);
            g.drawString("Score: " + player.getScore(), (SCREEN_WIDTH - metrics.stringWidth("Score: " + player.getScore())), g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Showcard gothic",Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("1. " + highScores.printItem(0), (SCREEN_WIDTH - metrics.stringWidth("1. " + highScores.printItem(0))) / 2, 100);
        g.drawString("2. " + highScores.printItem(1), (SCREEN_WIDTH - metrics.stringWidth("2. " + highScores.printItem(1))) / 2, 130);
        g.drawString("3. " + highScores.printItem(2), (SCREEN_WIDTH - metrics.stringWidth("3. " + highScores.printItem(2))) / 2, 160);
        g.drawString("4. " + highScores.printItem(3), (SCREEN_WIDTH - metrics.stringWidth("4. " + highScores.printItem(3))) / 2, 190);
        g.drawString("5. " + highScores.printItem(4), (SCREEN_WIDTH - metrics.stringWidth("5. " + highScores.printItem(4))) / 2, 220);
        g.drawString("Score: " + player.getScore(), (SCREEN_WIDTH - metrics.stringWidth("Score: " + player.getScore())) / 2, g.getFont().getSize());
        g.drawString("Press 'r' to restart game", (SCREEN_WIDTH - metrics.stringWidth("Press 'r' to play again")) / 2, SCREEN_HEIGHT / 2 );//+ 2 * g.getFont().getSize());
        g.drawString("Press 'Esc' to exit", (SCREEN_WIDTH - metrics.stringWidth("Press 'Esc' to exit")) / 2, SCREEN_HEIGHT / 2 + g.getFont().getSize());
        g.setColor(Color.red);

        if(checkScore && player.getScore() > highScores.getLast()){
            highScores.insert(player.getScore(), player.getName());
            checkScore = false;
            //player.setScore(0);
        }
        if (exit) {
            jumpTimer.stop();
            gameTimer.stop();
            flyTimer.stop();
            System.out.println(highScores);
            highScores.saveScores();
            System.exit(0);
        }
    }

    private void newObstacle() {
        Obstacle obstacle = new Obstacle();
        obstacle.setObstaclePosY(random.nextInt((SCREEN_HEIGHT - gap) / UNIT_SIZE) * UNIT_SIZE);
        obstacle.setObstaclePosX(SCREEN_WIDTH - 2 * UNIT_SIZE);
        obstacles.add(obstacle);
    }

    private void obstacleMove() {
        obstacles.forEach(obstacle ->
            obstacle.decObstacleX(UNIT_SIZE)
        );
        if(!obstacles.isEmpty()) {
            if (obstacles.peek().getObstaclePosX() == ((SCREEN_WIDTH / 4)/UNIT_SIZE)*UNIT_SIZE) {
                newObstacle();
            } else if (Objects.requireNonNull(obstacles.peek()).getObstaclePosX() == 0) {
                //newObstacle();
                player.setScore(player.getScore() + 1);
                checkLevel = true;
                obstacles.remove();
            }
        }
    }

    private void checkCollisions() {
        int temp = Objects.requireNonNull(obstacles.peek()).getObstaclePosX();
        if (player.getPlayerY() < UNIT_SIZE || player.getPlayerY() > SCREEN_HEIGHT - UNIT_SIZE)
            running = false;
        else if (!obstacles.isEmpty()) {
            if (temp == player.getPLAYERX()
                    || temp + UNIT_SIZE == player.getPLAYERX()
                    || temp + 2 * UNIT_SIZE == player.getPLAYERX()
                    || temp - UNIT_SIZE == player.getPLAYERX()
                    || temp - 2 * UNIT_SIZE == player.getPLAYERX()) {
                if (player.getPlayerY() < Objects.requireNonNull(obstacles.peek()).getObstaclePosY()
                        || player.getPlayerY() + PLAYER_SIZE > Objects.requireNonNull(obstacles.peek()).getObstaclePosY() + gap)
                    running = false;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jump = movement == Movement.UP;
        dive = movement == Movement.DOWN;
        if(checkLevel && player.getScore() % 5 == 0 && level <= 10){
            checkLevel = false;
            level++;
            if(level > 5) gap -= UNIT_SIZE;
            else gap -= 2 * UNIT_SIZE;
            if (level == 5)gameTimer.setDelay(TIMER_DELAY - 10);
        }
        repaint();
    }

    protected class ResetGame extends AbstractAction {
        private boolean reset;
        private boolean exitGame;
        public ResetGame(GamePanel.ExitReset decision) {
            if (decision.equals(ExitReset.RESET)) reset = true;
            else exitGame = decision.equals(ExitReset.EXIT);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            restart = reset;
            exit = exitGame;
        }
    }

    protected class MoveUDAction extends AbstractAction {
        private final GamePanel.Movement UDmovement;

        public MoveUDAction(GamePanel.Movement movement) {
            this.UDmovement = movement;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            movement = UDmovement;
        }
    }
}
