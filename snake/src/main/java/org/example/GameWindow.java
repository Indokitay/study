package org.example;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    private SnakeGame snakeGame;
    private JLabel scoreLabel;

    public GameWindow() {
        setupGame();
        setupAdminPanel();

        setTitle("Snake Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
    }

    private void setupGame() {
        snakeGame = new SnakeGame(this);
        add(snakeGame, BorderLayout.CENTER);
    }

    private void setupAdminPanel() {
        JPanel adminPanel = new JPanel();
        setupRestartButton(adminPanel);
        setupScoreLabel(adminPanel);
        add(adminPanel, BorderLayout.SOUTH);
    }

    private void setupRestartButton(JPanel adminPanel) {
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        adminPanel.add(restartButton);
    }

    private void setupScoreLabel(JPanel adminPanel) {
        scoreLabel = new JLabel("Score: 0");
        adminPanel.add(scoreLabel);
    }

    private void restartGame() {
        remove(snakeGame);
        setupGame();
        pack();
        snakeGame.requestFocusInWindow();
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
}