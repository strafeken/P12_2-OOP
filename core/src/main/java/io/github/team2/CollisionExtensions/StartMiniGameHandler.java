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
import io.github.team2.Utils.DisplayManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

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


    public void onMiniGameCompleted() {
        System.out.println("Mini-game completed callback received");

        // Get the player and last alien that triggered the mini-game
        PlayerStatus playerStatus = PlayerStatus.getInstance();
        Entity lastAlienEncounter = playerStatus.getLastAlienEncounter();
        Entity player = playerStatus.getPlayer();

        // If there was an alien in the encounter, respawn it safely
        if (lastAlienEncounter != null && lastAlienEncounter instanceof Alien && player != null) {
            Alien alien = (Alien) lastAlienEncounter;
            respawnAlienAtSafeDistance(alien, player);
        }
    }

    // New method to respawn alien at a safe distance from player
    private void respawnAlienAtSafeDistance(Alien alien, Entity player) {
        // Calculate a position at least 300 units away from player
        float safeDistance = 300f;
        Vector2 playerPos = player.getPosition();
        Vector2 newPosition = new Vector2();

        // Generate a random angle
        float angle = MathUtils.random(0f, MathUtils.PI2);

        // Calculate new position using polar coordinates
        newPosition.x = playerPos.x + safeDistance * MathUtils.cos(angle);
        newPosition.y = playerPos.y + safeDistance * MathUtils.sin(angle);

        // Make sure position is within the screen bounds
        float margin = 50f;
        newPosition.x = MathUtils.clamp(newPosition.x,
                                       margin,
                                       DisplayManager.getScreenWidth() - margin);
        newPosition.y = MathUtils.clamp(newPosition.y,
                                       margin,
                                       DisplayManager.getScreenHeight() - margin);

        // Set the alien to the new position
        if (alien.getPhysicsBody() != null) {
            alien.getPhysicsBody().setLocation(newPosition.x, newPosition.y);
            alien.getPhysicsBody().getBody().setLinearVelocity(0, 0);
            System.out.println("Respawned alien at safe distance: " + newPosition);
        }
    }

    // Method to update cooldown timer
    public void update(float deltaTime) {
        if (miniGameCooldown > 0) {
            miniGameCooldown -= deltaTime;
        }
    }
}
