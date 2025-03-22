package io.github.team2.Game.MiniGame;

import java.util.Random;

import io.github.team2.CollisionExtensions.StartMiniGameHandler;
import io.github.team2.Game.Manager.PointsManager;
import io.github.team2.SceneSystem.Scene;

/**
 * Factory for creating mini-games.
 * This follows the Factory Pattern and Single Responsibility Principle
 * by encapsulating mini-game creation logic.
 */
public class MiniGameFactory {
    private PointsManager pointsManager;
    private StartMiniGameHandler miniGameHandler;
    private Random random;
    
    /**
     * Creates a new MiniGameFactory
     * @param pointsManager The points manager for scoring
     * @param miniGameHandler The handler that manages mini-games
     */
    public MiniGameFactory(PointsManager pointsManager, StartMiniGameHandler miniGameHandler) {
        this.pointsManager = pointsManager;
        this.miniGameHandler = miniGameHandler;
        this.random = new Random();
    }
    
    /**
     * Creates a random mini-game
     * @return A new mini-game scene
     */
    public Scene createRandomMiniGame() {
        // Choose a random number between 0 and 1
        int gameChoice = random.nextInt(2);
        
        switch (gameChoice) {
            case 0:
                System.out.println("Starting Flappy Bird mini-game!");
                return createFlappyBirdMiniGame();
            case 1:
                System.out.println("Starting Asteroid Dodge mini-game!");
                return createAsteroidDodgeMiniGame();
            default:
                // Default to Flappy Bird if something goes wrong
                return createFlappyBirdMiniGame();
        }
    }
    
    /**
     * Creates a FlappyBirdMiniGame
     * @return The mini-game scene
     */
    public Scene createFlappyBirdMiniGame() {
        return new FlappyBirdMiniGame(pointsManager, miniGameHandler);
    }
    
    /**
     * Creates an AsteroidDodgeMiniGame
     * @return The mini-game scene
     */
    public Scene createAsteroidDodgeMiniGame() {
        return new AsteroidDodgeMiniGame(pointsManager, miniGameHandler);
    }
}