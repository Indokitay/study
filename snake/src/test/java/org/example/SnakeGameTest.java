package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnakeGameTest {

    private SnakeGame snakeGame;

    @BeforeEach
    void setUp() {
        snakeGame = new SnakeGame(new GameWindow());
    }

    @Test
    void snakeDoesNotGrowWhenNotEatingApple() {
        int initialSize = snakeGame.dots;
        snakeGame.checkApple();
        assertEquals(initialSize, snakeGame.dots);
    }

    @Test
    void gameContinuesWhenSnakeDoesNotHitBoundary() {
        snakeGame.x[0] = snakeGame.SIZE - snakeGame.DOT_SIZE;
        snakeGame.checkCollision();
        assertTrue(snakeGame.inGame);
    }

    @Test
    void gameContinuesWhenSnakeDoesNotHitItself() {
        snakeGame.dots = 5;
        snakeGame.checkCollision();
        assertTrue(snakeGame.inGame);
    }

    @Test
    void gameEndsWhenSnakeHitsObstacle() {
        snakeGame.obstacleX[0] = snakeGame.x[0];
        snakeGame.obstacleY[0] = snakeGame.y[0];
        snakeGame.checkCollision();
        assertFalse(snakeGame.inGame);
    }
}