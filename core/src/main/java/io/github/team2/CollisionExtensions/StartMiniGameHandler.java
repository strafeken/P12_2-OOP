package io.github.team2.CollisionExtensions;

import java.util.List;
import java.util.Random;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Alien;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.IEntityManager;
import io.github.team2.MiniGame.AsteroidDodgeMiniGame;
import io.github.team2.MiniGame.FlappyBirdMiniGame;
import io.github.team2.MiniGame.MiniGameFactory;
import io.github.team2.PlayerStatus;
import io.github.team2.PointsManager;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;

/**
 * Handles starting mini-games when player collides with an alien.
 * Following Single Responsibility Principle, this class focuses solely on 
 * managing mini-game initialization and alien respawning after mini-games.
 */
public class StartMiniGameHandler implements CollisionListener {
    private PointsManager pointsManager;
    private ISceneManager sceneManager;
    private IEntityManager entityManager;
    private Random random;
    private MiniGameFactory miniGameFactory;

    // Cooldown timer to prevent rapid mini-game triggers
    private float miniGameCooldown = 0f;
    private static final float COOLDOWN_DURATION = 5f; // 5 seconds cooldown
    
    // Alien respawn settings
    private static final float MIN_RESPAWN_DISTANCE = 300f;
    private static final float MAX_RESPAWN_DISTANCE = 500f;

    /**
     * Creates a new StartMiniGameHandler
     * @param pointsManager The points manager for scoring
     * @param entityManager The entity manager for finding aliens
     */
    public StartMiniGameHandler(PointsManager pointsManager, IEntityManager entityManager) {
        this.pointsManager = pointsManager;
        this.entityManager = entityManager;
        this.sceneManager = SceneManager.getInstance();
        this.random = new Random();
        this.miniGameFactory = new MiniGameFactory(pointsManager, this);
    }

    /**
     * Handles collision between player and alien
     * Following Open/Closed principle, collision handling is extensible through
     * the CollisionType enum
     */
    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.ALIEN_PLAYER) {
            PlayerStatus playerStatus = PlayerStatus.getInstance();

            // Check if cooldown is active
            if (miniGameCooldown > 0) {
                return;
            }

            // Only start mini-game if not already in one
            if (!playerStatus.isInMiniGame()) {
                playerStatus.setInMiniGame(true);
                System.out.println("Starting random mini-game with alien!");

                // Store reference to alien for respawning later
                Entity alien = (a.getEntityType() == EntityType.ALIEN) ? a : b;
                playerStatus.setLastAlienEncounter(alien);

                try {
                    startMiniGame();
                } catch (Exception e) {
                    System.err.println("Error starting mini-game: " + e.getMessage());
                    e.printStackTrace();
                    playerStatus.setInMiniGame(false);
                }
            }
        }
    }
    
    /**
     * Starts a random mini-game
     */
    private void startMiniGame() {
        // Choose a random mini-game
        Scene miniGame = miniGameFactory.createRandomMiniGame();

        // Check if scene exists already and remove it if it does
        if (sceneManager.hasScene(SceneID.MINI_GAME)) {
            sceneManager.removeScene(SceneID.MINI_GAME);
        }

        // Add the new mini-game scene
        sceneManager.addScene(SceneID.MINI_GAME, miniGame);

        // Overlay the mini-game on top of the current scene
        sceneManager.overlayScene(SceneID.MINI_GAME);
    }

    /**
     * Called when a mini-game completes
     * Respawns aliens at a distance from the player
     */
    public void onMiniGameCompleted() {
        // Set cooldown
        miniGameCooldown = COOLDOWN_DURATION;

        try {
            respawnAliensAtDistanceFromPlayer();
        } catch (Exception e) {
            // Log and use the original respawn logic as fallback
            System.err.println("Error respawning aliens after mini-game: " + e.getMessage());
            respawnAliensToOriginalPositions();
        }
    }
    
    /**
     * Respawns all aliens at a random distance from the player
     */
    private void respawnAliensAtDistanceFromPlayer() {
        // Get the player status and position
        PlayerStatus playerStatus = PlayerStatus.getInstance();
        Entity player = playerStatus.getPlayer();
        
        // Only proceed if we have a valid player
        if (player != null) {
            Vector2 playerPos = player.getPosition();
            
            // Respawn all aliens at a safe distance from the player
            List<Entity> aliens = entityManager.getEntitiesByType(EntityType.ALIEN);
            for (Entity entity : aliens) {
                if (entity instanceof Alien) {
                    Alien alien = (Alien)entity;
                    
                    // Calculate a new respawn position at a distance from the player
                    Vector2 respawnPos = calculateRespawnPosition(playerPos);
                    
                    // Set the alien to this position
                    if (alien.getPhysicsBody() != null) {
                        alien.getPhysicsBody().setLocation(respawnPos.x, respawnPos.y);
                        
                        // Also reset velocity to ensure they don't keep moving from previous state
                        alien.getPhysicsBody().setLinearVelocity(0, 0);
                        
                        System.out.println("Respawned alien at position: " + respawnPos);
                    }
                }
            }
        } else {
            // Fallback to original respawn logic if player is null
            respawnAliensToOriginalPositions();
        }
    }
    
    /**
     * Calculates a random position at a distance from the player
     * @param playerPos The player's current position
     * @return A new position vector
     */
    private Vector2 calculateRespawnPosition(Vector2 playerPos) {
        // Choose a random angle
        float angle = random.nextFloat() * 2f * (float)Math.PI;
        
        // Choose a distance between MIN and MAX
        float distance = MIN_RESPAWN_DISTANCE + random.nextFloat() * (MAX_RESPAWN_DISTANCE - MIN_RESPAWN_DISTANCE);
        
        // Calculate new position
        float newX = playerPos.x + distance * (float)Math.cos(angle);
        float newY = playerPos.y + distance * (float)Math.sin(angle);
        
        // Return a new respawn position vector
        return new Vector2(newX, newY);
    }
    
    /**
     * Fallback method that respawns aliens to their original positions
     */
    private void respawnAliensToOriginalPositions() {
        System.out.println("Using original alien respawn positions");
        List<Entity> aliens = entityManager.getEntitiesByType(EntityType.ALIEN);
        for (Entity entity : aliens) {
            try {
                if (entity instanceof Alien) {
                    ((Alien) entity).respawn();
                }
            } catch (Exception ex) {
                System.err.println("Error on alien respawn: " + ex.getMessage());
            }
        }
    }

    /**
     * Updates the cooldown timer
     * @param deltaTime Time since last update
     */
    public void update(float deltaTime) {
        if (miniGameCooldown > 0) {
            miniGameCooldown -= deltaTime;
        }
    }
}