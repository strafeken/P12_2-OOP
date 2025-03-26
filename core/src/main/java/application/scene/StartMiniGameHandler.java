package application.scene;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.CollisionListener;
import abstractengine.entity.Entity;
import abstractengine.entity.IEntityManager;
import abstractengine.utils.DisplayManager;
import application.entity.Alien;
import application.entity.CollisionType;
import application.entity.EntityType;
import application.entity.PlayerStatus;

/**
 * Handles player-alien collisions.
 * Following Single Responsibility Principle, this class focuses solely on
 * handling alien interactions and respawning.
 */
public class StartMiniGameHandler implements CollisionListener {
    private IEntityManager entityManager;

    // Cooldown timer to prevent rapid collision handling
    private float interactionCooldown = 0f;
    private static final float COOLDOWN_DURATION = 0.8f; // 0.2 seconds cooldown

    // List of aliens to respawn during next update
    private List<AlienRespawnData> aliensToRespawn = new ArrayList<>();

    // Class to hold respawn data
    private static class AlienRespawnData {
        Entity alien;
        Vector2 respawnPos;

        public AlienRespawnData(Entity alien, Vector2 respawnPos) {
            this.alien = alien;
            this.respawnPos = respawnPos;
        }
    }

    /**
     * Creates a new collision handler
     * @param entityManager The entity manager for finding aliens
     */
    public StartMiniGameHandler(IEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Handles collision between player and alien
     */
    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.ALIEN_PLAYER && interactionCooldown <= 0) {
            // Set cooldown
            interactionCooldown = COOLDOWN_DURATION;
            System.out.println("Alien collision detected, cooldown set: " + interactionCooldown);

            // Get player status
            PlayerStatus playerStatus = PlayerStatus.getInstance();

            // Identify which entity is the alien - FIX: use getEntityType() instead of getType()
            Entity alien = (a.getEntityType() == EntityType.ALIEN) ? a : b;
            Entity player = playerStatus.getPlayer();

            // Move alien away
            queueAlienMoveAwayFromPlayer(alien, player);
        }
    }

    /**
     * Queues alien to be moved away from the player
     */
    private void queueAlienMoveAwayFromPlayer(Entity alien, Entity player) {
        // Calculate respawn position
        Vector2 playerPos = player.getPosition();
        Vector2 respawnPos = calculateRespawnPosition(playerPos);

        System.out.println("Queueing alien to respawn at: " + respawnPos.x + ", " + respawnPos.y);

        // Only add to the queue - don't try to move it immediately
        aliensToRespawn.add(new AlienRespawnData(alien, respawnPos));
    }

    /**
     * Calculates a random position at a distance from the player
     * @param playerPos The player's current position
     * @return A new position vector
     */
    private Vector2 calculateRespawnPosition(Vector2 playerPos) {
        // Dramatically increase safe distance
        float safeDistance = 600f; // Increased from 400f
        float margin = 50f;

        // Generate a random angle
        float angle = MathUtils.random(0, MathUtils.PI2);

        // Calculate position at safe distance
        float x = playerPos.x + safeDistance * MathUtils.cos(angle);
        float y = playerPos.y + safeDistance * MathUtils.sin(angle);

        // Ensure it's within screen bounds
        x = MathUtils.clamp(x, margin, DisplayManager.getScreenWidth() - margin);
        y = MathUtils.clamp(y, margin, DisplayManager.getScreenHeight() - margin);

        System.out.println("Calculated respawn position: " + x + ", " + y + " (distance: " + safeDistance + ")");
        return new Vector2(x, y);
    }

    /**
     * Updates the cooldown timer and processes any queued alien respawns
     * @param deltaTime Time since last update
     */
    public void update(float deltaTime) {
        // Update cooldown
        if (interactionCooldown > 0) {
            interactionCooldown -= deltaTime;
        }

        // Process any aliens that need to be respawned - THIS IS SAFE HERE
        if (!aliensToRespawn.isEmpty()) {
            System.out.println("Processing " + aliensToRespawn.size() + " aliens for respawn");
            for (AlienRespawnData respawnData : aliensToRespawn) {
                if (respawnData.alien instanceof Alien) {
                    ((Alien) respawnData.alien).respawnAt(respawnData.respawnPos);
                    System.out.println("Successfully respawned alien to: " + respawnData.respawnPos.x + ", " + respawnData.respawnPos.y);
                } else {
                    System.err.println("Error: Entity is not an Alien, cannot respawn");
                }
            }
            aliensToRespawn.clear();
        }
    }
}
