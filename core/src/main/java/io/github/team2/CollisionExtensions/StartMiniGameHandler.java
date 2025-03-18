package io.github.team2.CollisionExtensions;

import io.github.team2.Alien;
import io.github.team2.PlayerStatus;
import io.github.team2.PointsManager;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.IEntityManager;
import io.github.team2.MiniGame.FlappyBirdMiniGame;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.SceneSystem.ISceneManager;

import java.util.List;

public class StartMiniGameHandler implements CollisionListener {
    private PointsManager pointsManager;
    private ISceneManager sceneManager;
    private IEntityManager entityManager;

    // Add cooldown timer
    private float miniGameCooldown = 0f;
    private static final float COOLDOWN_DURATION = 5f; // 5 seconds cooldown

    public StartMiniGameHandler(PointsManager pointsManager, IEntityManager entityManager) {
        this.pointsManager = pointsManager;
        this.entityManager = entityManager;
        this.sceneManager = SceneManager.getInstance();
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        // If player is already in a mini-game, ignore all collisions
        PlayerStatus playerStatus = PlayerStatus.getInstance();
        if (playerStatus.isInMiniGame()) {
            return; // Skip all collision processing when in mini-game
        }

        if (type == CollisionType.ALIEN_PLAYER) {
            // Check if cooldown is active
            if (miniGameCooldown > 0) {
                return;
            }

            // Only start mini-game if not already in one
            playerStatus.setInMiniGame(true);
            System.out.println("Starting mini-game with alien!");

            // Store reference to alien for respawning later
            Entity alien = (a.getEntityType() == EntityType.ALIEN) ? a : b;
            playerStatus.setLastAlienEncounter(alien);

            try {
                // Always create a new instance to ensure fresh state
                FlappyBirdMiniGame miniGame = new FlappyBirdMiniGame(pointsManager, this);

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

    // Method to handle mini-game completion
    public void onMiniGameCompleted() {
        // Set cooldown
        miniGameCooldown = COOLDOWN_DURATION;

        // Get the alien that triggered the mini-game
        Entity alienEntity = PlayerStatus.getInstance().getLastAlienEncounter();

        // Respawn all aliens to their initial positions
        List<Entity> aliens = entityManager.getEntitiesByType(EntityType.ALIEN);
        for (Entity entity : aliens) {
            if (entity instanceof Alien) {
                ((Alien) entity).respawn();
            }
        }
    }

    // Method to update cooldown timer
    public void update(float deltaTime) {
        if (miniGameCooldown > 0) {
            miniGameCooldown -= deltaTime;
        }
    }
}
