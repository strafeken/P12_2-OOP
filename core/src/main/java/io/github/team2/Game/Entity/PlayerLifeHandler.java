package io.github.team2.Game.Entity;

import io.github.team2.Abstract.CollisionSystem.CollisionListener;
import io.github.team2.Abstract.EntitySystem.Entity;
import io.github.team2.Abstract.SceneSystem.ISceneManager;
import io.github.team2.Abstract.SceneSystem.SceneID;
import io.github.team2.Game.Manager.PlayerStatus;
import io.github.team2.Game.Manager.PointsManager;
import io.github.team2.Game.Scene.GameOverScreen;

public class PlayerLifeHandler implements CollisionListener {
    private ISceneManager sceneManager;
    private PointsManager pointsManager;
    private boolean isGameOver = false;

    public PlayerLifeHandler(ISceneManager sceneManager, PointsManager pointsManager) {
        this.sceneManager = sceneManager;
        this.pointsManager = pointsManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.NON_RECYCLABLE_PLAYER) {
            PlayerStatus playerStatus = PlayerStatus.getInstance();

            // Decrement player life
            playerStatus.decrementLife();
            System.out.println("Player collided with non-recyclable! Lives remaining: " + playerStatus.getLives());

            // Check if game over, but don't transition immediately during collision
            if (playerStatus.isGameOver()) {
                System.out.println("Game Over! No lives remaining.");
                isGameOver = true;
            }
        }
    }
    
    // This method should be called from the game scene update loop, not during collision
    public boolean checkGameOver() {
        if (isGameOver) {
            // Reset the flag
            isGameOver = false;
            
            // Create and set up game over screen with final score
            GameOverScreen gameOverScreen = new GameOverScreen();
            gameOverScreen.setFinalScore(pointsManager.getPoints());
            sceneManager.setNextScene(SceneID.GAME_OVER);
            return true;
        }
        return false;
    }
}
