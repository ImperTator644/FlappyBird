package flappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;


public class GamePanel extends JLabel implements ActionListener {

    protected enum Movement {NONE, UP}

    protected enum ExitReset {RESET, EXIT}

    private GamePanel.Movement movement = GamePanel.Movement.NONE;

    private final Player player;
    private final Score highScores;
    protected static final int SCREEN_WIDTH = 500;
    protected static final int SCREEN_HEIGHT = 700;
    protected static final int UNIT_SIZE = 10;
    protected static final int PLAYER_SIZE = 3 * UNIT_SIZE;
    protected static final int GAP = 10 * UNIT_SIZE;
    protected static final int MOVE_TIMER_DELAY = 100;
    protected static final int FLY_TIMER_DELAY = 20;
    protected static final int TIMER_DELAY = 50;
    private int obstaclePosX            = 0;
    private int obstaclePosY            = 0;
    private int obstaclesPassed         = 0;
    private boolean running             = false;
    private boolean jump                = false;
    private boolean restart             = false;
    private boolean exit                = false;
    private final boolean isImage;
    private Timer gameTimer;
    private Timer jumpTimer;
    private Timer flyTimer;
    private final Random random;

    public GamePanel(String s, ImageIcon background, int center, Player player) {
        super(s,background,center);
        this.player = player;
        this.highScores = new Score();
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        isImage = player.loadImage();
    }

    public void initGame() {
        addKeyPressedBinding("up.pressed", KeyEvent.VK_W, new MoveUDAction(GamePanel.Movement.UP));
        addKeyReleasedBinding("up.released", KeyEvent.VK_W, new MoveUDAction(GamePanel.Movement.NONE));
        addKeyPressedBinding("reset.pressed", KeyEvent.VK_R, new ResetGame(ExitReset.RESET));

        gameTimer = new Timer(TIMER_DELAY, e -> {
            if (!restart) {
                if (!jump) player.addPlayerY(UNIT_SIZE);
                if (player.getPlayerY() < UNIT_SIZE) {
                    player.setPlayerY(UNIT_SIZE);
                } else if (player.getPlayerY() > SCREEN_HEIGHT - UNIT_SIZE) {
                    player.setPlayerY(SCREEN_HEIGHT - UNIT_SIZE);
                }
                if (running) {
                    obstacleMove();
                    checkCollisions();
                }
            } else {
                obstaclesPassed = 0;
                running = true;
                restart = false;
                player.setPlayerY(SCREEN_HEIGHT / 2);
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
            if(isImage) g.drawImage(player.getImage(),player.getPLAYERX(),player.getPlayerY(),PLAYER_SIZE,PLAYER_SIZE,this);
            else g.fillRect(player.getPLAYERX(), player.getPlayerY(), PLAYER_SIZE, PLAYER_SIZE);
            
            g.setColor(new Color(34,69,6));
            g.fillRect(obstaclePosX, 0, UNIT_SIZE * 2, obstaclePosY);
            g.fillRect(obstaclePosX, obstaclePosY + GAP, UNIT_SIZE * 2, SCREEN_HEIGHT);

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Player: " + player.getName(), 0, g.getFont().getSize());
            g.drawString("Score: " + obstaclesPassed, (SCREEN_WIDTH - metrics.stringWidth("Score: " + obstaclesPassed)), g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Press 'r' to restart game", (SCREEN_WIDTH - metrics.stringWidth("Press 'r' to play again")) / 2, SCREEN_HEIGHT / 2 + 2 * g.getFont().getSize());
        g.drawString("Press 'Esc' to exit", (SCREEN_WIDTH - metrics.stringWidth("Press 'Esc' to exit")) / 2, SCREEN_HEIGHT / 2 + 4 * g.getFont().getSize());
        g.setColor(Color.red);
        g.drawString("Score: " + obstaclesPassed, (SCREEN_WIDTH - metrics.stringWidth("Score: " + obstaclesPassed)) / 2, g.getFont().getSize());
        addKeyPressedBinding("exit.pressed", KeyEvent.VK_ESCAPE, new ResetGame(ExitReset.EXIT));
        if (exit) {
            jumpTimer.stop();
            gameTimer.stop();
            flyTimer.stop();
            System.exit(0);
        }
    }

    private void newObstacle() {
        obstaclePosY = random.nextInt((SCREEN_HEIGHT - GAP) / UNIT_SIZE) * UNIT_SIZE;
        obstaclePosX = SCREEN_WIDTH - 2 * UNIT_SIZE;
    }

    private void obstacleMove() {
        obstaclePosX -= UNIT_SIZE;
        if (obstaclePosX == 0) {
            newObstacle();
            obstaclesPassed++;
        }
    }

    private void checkCollisions() {
        if (obstaclePosX == player.getPLAYERX()
                || obstaclePosX + UNIT_SIZE == player.getPLAYERX()
                || obstaclePosX + 2 * UNIT_SIZE == player.getPLAYERX()) {
            if (player.getPlayerY() < obstaclePosY || player.getPlayerY() + PLAYER_SIZE > obstaclePosY + GAP) running = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jump = movement == Movement.UP;
        repaint();
    }

    protected class ResetGame extends AbstractAction {
        private boolean reset;
        private boolean exitGame;
        public ResetGame(GamePanel.ExitReset decision) {
            if (decision.equals(ExitReset.RESET)) reset = true;
            else if (decision.equals(ExitReset.EXIT)) exitGame = true;
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
