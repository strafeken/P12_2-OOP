package io.github.team2.CollisionExtensions;

import io.github.team2.PlayerStatus;
import io.github.team2.PointsManager;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.MiniGame.FlappyBirdMiniGame;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.SceneSystem.ISceneManager;

public class StartMiniGameHandler implements CollisionListener {
    private PointsManager pointsManager;
    private ISceneManager sceneManager;

    public StartMiniGameHandler(PointsManager pointsManager) {
        this.pointsManager = pointsManager;
        this.sceneManager = SceneManager.getInstance();
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.ALIEN_PLAYER) {
            PlayerStatus playerStatus = PlayerStatus.getInstance();

            // Only start mini-game if not already in one
            if (!playerStatus.isInMiniGame()) {
                playerStatus.setInMiniGame(true);
                System.out.println("Starting mini-game with alien!");

                try {
                    // Always create a new instance to ensure fresh state
                    FlappyBirdMiniGame miniGame = new FlappyBirdMiniGame(pointsManager);

                    // Check if scene exists already and remove it if it does
                    if (sceneManager.hasScene(SceneID.MINI_GAME)) {
                        sceneManager.removeScene(SceneID.MINI_GAME);
                    }

                    // Add the new mini-game scene
                    sceneManager.addScene(SceneID.MINI_GAME, miniGame);

                    // Overlay the mini-game on top of the current scene
                    sceneManager.overlayScene(SceneID.MINI_GAME);
                } catch (Exception e) {
                    System.err.println("Error starting mini-game: " + e.getMessage());
                    e.printStackTrace();
                    playerStatus.setInMiniGame(false);
                }
            }
        }
    }
}
