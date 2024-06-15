package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SnakeGame extends JPanel implements ActionListener {

    // Game variables
    final int SIZE = 300; // Size of the game board
    final int DOT_SIZE = 10; // Size of the snake and apple
    private final int ALL_DOTS = 900; // Maximum number of possible dots on the board (900 = (300*300)/(10*10))
    private final int DELAY = 140; // Delay in milliseconds between game updates
    private final int RANDOM_POSITION = 29; // Used to calculate random position for apple
    final int[] x = new int[ALL_DOTS]; // x coordinates of all joints of snake
    final int[] y = new int[ALL_DOTS]; // y coordinates of all joints of snake
    int dots; // Current size of the snake
    int appleX; // X position of the apple
    int appleY; // Y position of the apple

    // Additional variables for the obstacles
    private final int NUM_OBSTACLES = 5; // Number of obstacles
    int[] obstacleX = new int[NUM_OBSTACLES]; // X positions of the obstacles
    int[] obstacleY = new int[NUM_OBSTACLES]; // Y positions of the obstacles
    private Image obstacle; // Image of the obstacle

    // Game state variables
    boolean left = false; // Is snake moving left
    boolean right = true; // Is snake moving right
    boolean up = false; // Is snake moving up
    boolean down = false; // Is snake moving down
    boolean inGame = true; // Is game currently going on

    // System objects
    private final GameWindow gameWindow;
    private Timer timer; // Timer used for game updates
    private Image ball; // Image of the snake's joint
    private Image apple; // Image of the apple
    private Image head; // Image of the snake's head

    // Constructor for SnakeGame
    public SnakeGame(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        initBoard();
    }

    // Method to initialize the game board
    public void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);

        setPreferredSize(new Dimension(SIZE, SIZE));
        loadImages();
        initGame();
    }

    // Method to initialize the game
    public void initGame() {
        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10; // Initialize x position of the snake's joints
            y[z] = 50; // Initialize y position of the snake's joints
        }

        locateApple();
        locateObstacles(); // Place the obstacles on the game board

        timer = new Timer(DELAY, this);
        timer.start();
    }

    // Method to load images for the game
    public void loadImages() {
        ImageIcon iid = new ImageIcon("src/main/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/main/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/main/resources/dot.png");
        head = iih.getImage();

        ImageIcon iio = new ImageIcon("src/main/resources/obstacle.png");
        obstacle = iio.getImage(); // Load the obstacle image
    }

    // Method to locate the apple in the game
    public void locateApple() {
        int r = (int) (Math.random() * RANDOM_POSITION);
        appleX = ((r * DOT_SIZE));

        r = (int) (Math.random() * RANDOM_POSITION);
        appleY = ((r * DOT_SIZE));
    }

    // Method to locate the obstacles in the game
    public void locateObstacles() {
        for (int i = 0; i < NUM_OBSTACLES; i++) {
            int r = (int) (Math.random() * RANDOM_POSITION);
            obstacleX[i] = ((r * DOT_SIZE));

            r = (int) (Math.random() * RANDOM_POSITION);
            obstacleY[i] = ((r * DOT_SIZE));
        }
    }

    // Method to check if the snake has eaten the apple
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            dots++;
            locateApple();
        }
    }

    // Method to paint the components of the game
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < NUM_OBSTACLES; i++) {
                g.drawImage(obstacle, obstacleX[i], obstacleY[i], this); // Draw the obstacles
            }

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }
    }

    // Method to display game over
    public void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (SIZE - metr.stringWidth(msg)) / 2, SIZE / 2);

        repaint();
    }

    // Method to perform action on each timer tick
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
            gameWindow.updateScore(dots); // Update the score
        }

        repaint();
    }

    // Method to move the snake
    public void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (left) {
            x[0] -= DOT_SIZE;
        }

        if (right) {
            x[0] += DOT_SIZE;
        }

        if (up) {
            y[0] -= DOT_SIZE;
        }

        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    // Method to check if the snake has collided with itself, the border, or any of the obstacles
    public void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
                break;
            }
        }

        if (y[0] >= SIZE) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= SIZE) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        for (int i = 0; i < NUM_OBSTACLES; i++) {
            if ((x[0] == obstacleX[i]) && (y[0] == obstacleY[i])) {
                inGame = false; // End the game if the snake hits an obstacle
                break;
            }
        }

        if (!inGame) {
            timer.stop();
        }
    }

    // Inner class to handle key events
    private class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!right)) {
                left = true;
                up = false;
                down = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                right = true;
                up = false;
                down = false;
            }

            if ((key == KeyEvent.VK_UP) && (!down)) {
                up = true;
                right = false;
                left = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!up)) {
                down = true;
                right = false;
                left = false;
            }
        }
    }
}