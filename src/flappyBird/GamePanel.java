package flappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Random;
import java.util.Queue;

/**
 * Responsible for whole logic of the game
 *  @author Blicharz, Kaczynski, Filiciak
 *  @version 1.25.1
 */
public class GamePanel extends JLabel implements ActionListener {

    protected enum Movement {NONE, UP, DOWN}

    protected enum ExitReset {RESET, EXIT,NONE}

    private GamePanel.Movement movement = GamePanel.Movement.NONE;

    /**
     * Object player
     */
    private final Player player;
    /**
     * Object score
     */
    private final Score highScores;
    /**
     * Width of the panel
     */
    private static final int SCREEN_WIDTH = 500;
    /**
     * Height of the panel
     */
    protected static final int SCREEN_HEIGHT = 700;
    /**
     * Size of the unit.
     * Unit is like a pixel in our panel. It is the smallest drawable piece of our screen.
     */
    private static final int UNIT_SIZE = 10;
    /**
     * Size of the player
     */
    private static final int PLAYER_SIZE = 3 * UNIT_SIZE;
    private static int gap = 20 * UNIT_SIZE;
    private int level = 1;
    /**
     * Delay for jump timer
     */
    private static final int MOVE_TIMER_DELAY = 50;
    /**
     * Delay for fly timer
     */
    private static final int FLY_TIMER_DELAY = 10;
    /**
     * Delay for game timer
     */
    private static final int TIMER_DELAY = 30;
    private boolean running             = false;
    private boolean jump                = false;
    private boolean restart             = false;
    private boolean exit                = false;
    private boolean checkLevel          = false;
    private boolean dive                = false;
    private boolean checkScore          = true;
    private boolean checkPlayer          = true;
    private final boolean isImage;
    private Timer gameTimer;
    private Timer jumpTimer;
    private Timer flyTimer;
    /**
     * Object random
     */
    private final Random random;
    /**
     * Queue for obstacles.
     * We use queue for multiple obstacles appearance
     */
    private final Queue<Obstacle> obstacles;
    /**
     * Object info
     */
    private final Info info;

    /**
     * Initializes crucial variables and creates game panel
     * @param s JLabel text
     * @param background image for background
     * @param center place for the background
     * @param player object of Player class
     * @param info object of Info class, contains information about saved players
     */
    public GamePanel(String s, ImageIcon background, int center, Player player, Info info) {
        super(s,background,center);
        obstacles = new ArrayDeque<>();
        this.player = player;
        this.info = info;
        this.highScores = new Score();
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        isImage = player.loadImage();
    }

    /**
     * Serializes current state of object info.
     * Uses ObjectOutPutStream.
     */
    private void saveInfo(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("src/info.dat"));
            output.writeObject(info);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes game:
     * - Adds keys bindings.
     * - Creates timers.
     * GameTimer: if 'restart' is true, puts default settings, else it is responsible for player and obstacles movement
     * JumpTimer: responsible for key pressing delay
     * FlyTimer: responsible for player constant fall
     */
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
                checkPlayer = true;
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

    /**
     * Creates new obstacle and starts the timers
     */
    public void startGame() {
        newObstacle();
        running = true;
        gameTimer.start();
        jumpTimer.start();
        flyTimer.start();
    }

    /**
     * Defines which button is used
     * @param name button name
     * @param keyCode button key
     * @param action action after button press
     */
    protected void addKeyPressedBinding(String name, int keyCode, Action action) {
        KeyStroke ks = KeyStroke.getKeyStroke(keyCode, 0, false);
        addKeyBinding(name, ks, action);
    }

    /**
     * Defines, which button is released
     * @param name button name
     * @param keyCode button key
     * @param action action after button release
     */
    protected void addKeyReleasedBinding(String name, int keyCode, Action action) {
        KeyStroke ks = KeyStroke.getKeyStroke(keyCode, 0, true);
        addKeyBinding(name, ks, action);
    }

    /**
     * Creates action button
     * @param name button name
     * @param ks button key
     * @param action action for which the button is responsible
     */
    protected void addKeyBinding(String name, KeyStroke ks, Action action) {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(ks, name);
        am.put(name, action);
    }

    /**
     * Responsible for repainting the scene
     * @param g object encapsulates state information needed for the basic rendering operations that Java supports.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * If 'running' is true, puts all interface components in the correct place and draws them,
     * else calls gameOver method
     * @param g object encapsulates state information needed for the basic rendering operations that Java supports.
     */
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

    /**
     * Puts all game-over-interface components in the correct place and draws them.
     * Checks if exit button is pressed (esc), if yes, stops the timers, updates player's information, serializes object info and closes the application
     * @param g object encapsulates state information needed for the basic rendering operations that Java supports.
     */
    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Showcard gothic",Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());

        g.drawString("Score: " + player.getScore(), (SCREEN_WIDTH - metrics.stringWidth("Score: " + player.getScore())) / 2, g.getFont().getSize());
        for(int i = 0;i < 5; i++){
            g.drawString((i + 1)+". " + highScores.printItem(i), (SCREEN_WIDTH - metrics.stringWidth((i + 1)+". " + highScores.printItem(i))) / 2, (i+3)*g.getFont().getSize());
        }

        g.drawString("Press 'r' to restart game", (SCREEN_WIDTH - metrics.stringWidth("Press 'r' to play again")) / 2, (SCREEN_HEIGHT / 3) * 2 );
        g.drawString("Press 'Esc' to exit", (SCREEN_WIDTH - metrics.stringWidth("Press 'Esc' to exit")) / 2, (SCREEN_HEIGHT / 3) * 2 + g.getFont().getSize());
        g.setColor(Color.red);

        if(checkPlayer && info.updatePlayersHighScore(player.getName(),player.getScore())){
            checkPlayer = false;
        }
        if(checkScore && player.getScore() > highScores.getLast()){
            highScores.insert(player.getScore(), player.getName());
            checkScore = false;
        }
        if(!checkScore) g.drawString("NEW HIGHSCORE !!! ", (SCREEN_WIDTH - metrics.stringWidth("NEW HIGHSCORE !!! ")) / 2, SCREEN_HEIGHT / 2 );
        if(!checkPlayer)g.drawString("NEW PLAYER HIGHSCORE !!! ", (SCREEN_WIDTH - metrics.stringWidth("NEW PLAYER HIGHSCORE !!! ")) / 2, SCREEN_HEIGHT / 2 + g.getFont().getSize());
        if (exit) {
            jumpTimer.stop();
            gameTimer.stop();
            flyTimer.stop();
            System.out.println(highScores);
            highScores.saveScores();
            info.updatePlayerActivity(player.getName());
            saveInfo();
            System.exit(0);
        }
    }

    /**
     * Method which creates obstacles and adds them to queue.
     * We use queue for multiple obstacles appearance
     */
    private void newObstacle() {
        Obstacle obstacle = new Obstacle();
        obstacle.setObstaclePosY(random.nextInt((SCREEN_HEIGHT - gap) / UNIT_SIZE) * UNIT_SIZE);
        obstacle.setObstaclePosX(SCREEN_WIDTH - 2 * UNIT_SIZE);
        obstacles.add(obstacle);
    }

    /**
     * Responsible for obstacles' movement.
     * When one obstacle reaches 1/4 of the screen width, new obstacle is added to the queue.
     * When obstacle reaches the edge, it is removed from the queue and player's score increases by one.
     */
    private void obstacleMove() {
        obstacles.forEach(obstacle ->
            obstacle.decObstacleX(UNIT_SIZE)
        );
        if(!obstacles.isEmpty()) {
            if (obstacles.peek().getObstaclePosX() == ((SCREEN_WIDTH / 4)/UNIT_SIZE)*UNIT_SIZE) {
                newObstacle();
            } else if (Objects.requireNonNull(obstacles.peek()).getObstaclePosX() == 0) {
                player.setScore(player.getScore() + 1);
                checkLevel = true;
                obstacles.remove();
            }
        }
    }

    /**
     * Checks collision player - obstacle and player - screen border
     */
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

    /**
     * Responsible for frame action, increases level for every 5 points scored by player.
     * When level changes, gap between obstacles is getting thinner.
     * @param e action event
     */
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

    /**
     * Inner class.
     * Responsible for reset and exit keys.
     */
    private class ResetGame extends AbstractAction {
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

    /**
     * Inner class.
     * Responsible movement controls.
     */
    private class MoveUDAction extends AbstractAction {
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
