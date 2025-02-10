package io.github.team2;

import io.github.team2.EntitySystem.PlayerEntityManager;
import io.github.team2.Utils.Singleton;

public class GameManager extends Singleton<GameManager> {
    private static GameManager instance;
    private PointsManager pointsManager;
    private PlayerEntityManager playerEntityManager;

    private GameManager() {
        pointsManager = new PointsManager();
        playerEntityManager = new PlayerEntityManager();
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

    public void resetGame() {
        pointsManager = new PointsManager();
    }
}
