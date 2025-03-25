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
    private static final float COOLDOWN_DURATION = 2f; // 2 seconds cooldown

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
        if (type == CollisionType.ALIEN_PLAYER) {
            // Check if cooldown is active
            if (interactionCooldown > 0) {
                return;
            }

            // Get player status
            PlayerStatus playerStatus = PlayerStatus.getInstance();

            // Store reference to alien for respawning
            Entity alien = (a.getEntityType() == EntityType.ALIEN) ? a : b;
            playerStatus.setLastAlienEncounter(alien);

            try {
                // Check if player is holding trash
                if (playerStatus.isHoldingTrash()) {
                    // Make the player drop the trash
                    playerStatus.dropTrash();
                    System.out.println("Player dropped trash due to alien collision!");
                } else {
                    // Queue alien to move away from player instead of moving it immediately
                    queueAlienMoveAwayFromPlayer(alien);
                    System.out.println("Alien will move away from player!");
                }

                // Start cooldown
                interactionCooldown = COOLDOWN_DURATION;

            } catch (Exception e) {
                System.err.println("Error handling collision: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Queues alien to be moved away from the player
     */
    private void queueAlienMoveAwayFromPlayer(Entity alien) {
        // Get the player status and position
        PlayerStatus playerStatus = PlayerStatus.getInstance();
        Entity player = playerStatus.getPlayer();

        // Only proceed if we have a valid player and alien
        if (player != null && alien != null) {
            Vector2 playerPos = player.getPosition();

            // Calculate a new respawn position away from the player
            Vector2 respawnPos = calculateRespawnPosition(playerPos);

            // Queue the alien for respawning instead of doing it immediately
            aliensToRespawn.add(new AlienRespawnData(alien, respawnPos));
        } else {
            System.err.println("Error: Player or alien is null, can't move alien away.");
        }
    }

    /**
     * Calculates a random position at a distance from the player
     * @param playerPos The player's current position
     * @return A new position vector
     */
    private Vector2 calculateRespawnPosition(Vector2 playerPos) {
        // Calculate a new position that is at a distance from the player
        // but still within the screen bounds
        float safeDistance = 200.0f;  // Minimum distance from player

        // Screen dimensions with margins
        float margin = 100.0f;
        float screenWidth = DisplayManager.getScreenWidth() - margin*2;
        float screenHeight = DisplayManager.getScreenHeight() - margin*2;

        // Calculate a position within the screen bounds
        float x, y;

        do {
            // Generate a random angle
            float angle = MathUtils.random(0, MathUtils.PI2);

            // Calculate position at safe distance
            x = playerPos.x + safeDistance * MathUtils.cos(angle);
            y = playerPos.y + safeDistance * MathUtils.sin(angle);

            // Ensure it's within screen bounds
            x = MathUtils.clamp(x, margin, DisplayManager.getScreenWidth() - margin);
            y = MathUtils.clamp(y, margin, DisplayManager.getScreenHeight() - margin);

            // Check if this position is far enough from the player
            float distanceSquared = (x - playerPos.x)*(x - playerPos.x) + (y - playerPos.y)*(y - playerPos.y);
            if (distanceSquared >= safeDistance*safeDistance*0.75f) {
                // Position is good, exit the loop
                break;
            }
            // Otherwise, try again
        } while(true);

        System.out.println("Respawning alien at: " + x + ", " + y + " (player at " + playerPos.x + ", " + playerPos.y + ")");
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

        // Process any aliens that need to be respawned
        if (!aliensToRespawn.isEmpty()) {
            for (AlienRespawnData respawnData : aliensToRespawn) {
                if (respawnData.alien instanceof Alien) {
                    ((Alien) respawnData.alien).respawnAt(respawnData.respawnPos);
                }
            }
            // Clear the list after processing
            aliensToRespawn.clear();
        }
    }
}
