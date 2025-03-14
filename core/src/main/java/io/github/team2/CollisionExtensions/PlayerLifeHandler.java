package io.github.team2.CollisionExtensions;

import io.github.team2.PlayerStatus;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.SceneID;

public class PlayerLifeHandler implements CollisionListener {
    private ISceneManager sceneManager;

    public PlayerLifeHandler(ISceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.NON_RECYCLABLE_PLAYER) {
            PlayerStatus playerStatus = PlayerStatus.getInstance();

            // Decrement player life
            playerStatus.decrementLife();
            System.out.println("Player collided with non-recyclable! Lives remaining: " + playerStatus.getLives());

            // Check if game over
            if (playerStatus.isGameOver()) {
                System.out.println("Game Over! No lives remaining.");
                sceneManager.setNextScene(SceneID.GAME_OVER);
            }
        }
    }
}
