package application.entity;

import abstractengine.entity.CollisionListener;
import abstractengine.entity.Entity;
import abstractengine.scene.ISceneManager;
import application.scene.GameOverScreen;
import application.scene.PointsManager;
import application.scene.SceneID;

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
        PlayerStatus playerStatus = PlayerStatus.getInstance();

        // Handle non-recyclable collisions
        if (type == CollisionType.NON_RECYCLABLE_PLAYER) {

            playerStatus.decrementLife();

            System.out.println("Player collided with non-recyclable! Lives remaining: " + playerStatus.getLives());

            // Check if game over, but don't transition immediately during collision
            if (playerStatus.isGameOver()) {
                System.out.println("Game Over! No lives remaining.");
                isGameOver = true;
            }
        }
        // Handle alien collisions
        else if (type == CollisionType.ALIEN_PLAYER) {
            // Decrement player life by 2
            playerStatus.decrementLife();
            playerStatus.decrementLife();
            System.out.println("Player collided with alien! Lives remaining: " + playerStatus.getLives());

            // Check if game over
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
