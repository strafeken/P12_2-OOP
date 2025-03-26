package application.entity;

import abstractengine.entity.CollisionListener;
import abstractengine.entity.Entity;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.Scene;
import application.scene.GameOverScreen;
import application.scene.PointsManager;
import application.scene.SceneID;

public class PlayerLifeHandler implements CollisionListener {
    private ISceneManager sceneManager;
    private PointsManager pointsManager;
    private boolean isGameOver = false;
    private float alienCollisionCooldown = 0f;
    private static final float ALIEN_COOLDOWN_DURATION = 1.0f;

    public PlayerLifeHandler(ISceneManager sceneManager, PointsManager pointsManager) {
        this.sceneManager = sceneManager;
        this.pointsManager = pointsManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        PlayerStatus playerStatus = PlayerStatus.getInstance();

        if (type == CollisionType.NON_RECYCLABLE_PLAYER) {
            // Decrement player life by 2
            playerStatus.decrementLife();
            playerStatus.decrementLife();
            System.out.println("Player collided with non-recyclable! Lives remaining: " + playerStatus.getLives());

            if (playerStatus.isGameOver()) {
                System.out.println("Game Over! No lives remaining.");
                isGameOver = true;
            }
        }
        // Handle alien collisions with our cooldown
        else if (type == CollisionType.ALIEN_PLAYER && alienCollisionCooldown <= 0) {
            // Set our cooldown
            alienCollisionCooldown = ALIEN_COOLDOWN_DURATION;
            System.out.println("Setting alien collision cooldown in PlayerLifeHandler");

            // Decrement player life by 2 (regardless of carrying trash)
            playerStatus.decrementLife();
            playerStatus.decrementLife();
            System.out.println("Player collided with alien! Lives remaining: " + playerStatus.getLives());

            // Check for game over
            if (playerStatus.isGameOver()) {
                System.out.println("Game Over! No lives remaining.");
                isGameOver = true;
            }
        }
    }

    // Update method to process cooldown timer
    public void update(float deltaTime) {
        if (alienCollisionCooldown > 0) {
            alienCollisionCooldown -= deltaTime;
        }
    }

    // This method should be called from the game scene update loop, not during collision
    public boolean checkGameOver() {
        if (isGameOver) {
            // Reset the flag
            isGameOver = false;

            // Create and set up game over screen with final score
            //GameOverScreen gameOverScreen = new GameOverScreen();
            Scene gameOverScreen = sceneManager.getScene(SceneID.GAME_OVER);


            ((GameOverScreen) gameOverScreen).setFinalScore(pointsManager.getPoints());
            sceneManager.setNextScene(SceneID.GAME_OVER);
            return true;
        }
        return false;
    }
}
