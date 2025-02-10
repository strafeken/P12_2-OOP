package io.github.team2;

import io.github.team2.EntitySystem.PlayerEntityManager;
import io.github.team2.InputSystem.PlayerInputManager;
import io.github.team2.Utils.Singleton;

public class GameManager extends Singleton<GameManager> {
    private static GameManager instance;
    private PointsManager pointsManager;
    private PlayerEntityManager playerEntityManager;
    private PlayerInputManager playerInputManager;  // Add this field

    private GameManager() {
        pointsManager = new PointsManager();
        playerEntityManager = new PlayerEntityManager();
        playerInputManager = new PlayerInputManager(playerEntityManager.getPlayer());  // Initialize with player
    }

    public static GameManager getInstance() {
        if (instance == null) {
            synchronized (GameManager.class) {
                if (instance == null) {
                    instance = new GameManager();
                }
            }
        }
        return instance;
    }

    public PointsManager getPointsManager() {
        return pointsManager;
    }

    public PlayerEntityManager getPlayerEntityManager() {
        return playerEntityManager;
    }

    // Add this method
    public PlayerInputManager getPlayerInputManager() {
        return playerInputManager;
    }

    public void resetGame() {
        pointsManager = new PointsManager();
        playerInputManager = new PlayerInputManager(playerEntityManager.getPlayer());  // Reset player input manager
    }
}