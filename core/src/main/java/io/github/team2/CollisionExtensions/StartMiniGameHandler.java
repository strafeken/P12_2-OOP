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
import io.github.team2.PlayerStatus;
import io.github.team2.PointsManager;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;

public class StartMiniGameHandler implements CollisionListener {
    private PointsManager pointsManager;
    private ISceneManager sceneManager;
    private IEntityManager entityManager;
    private Random random;

    // Add cooldown timer
    private float miniGameCooldown = 0f;
    private static final float COOLDOWN_DURATION = 5f; // 5 seconds cooldown

    public StartMiniGameHandler(PointsManager pointsManager, IEntityManager entityManager) {
        this.pointsManager = pointsManager;
        this.entityManager = entityManager;
        this.sceneManager = SceneManager.getInstance();
        this.random = new Random();
    }

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
                    // Choose a random mini-game
                    Scene miniGame = createRandomMiniGame();

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
    
    // Create a random mini-game
    private Scene createRandomMiniGame() {
        // Choose a random number between 0 and 1 (removed SpaceQuiz option)
        int gameChoice = random.nextInt(2);
        
        switch (gameChoice) {
            case 0:
                System.out.println("Starting Flappy Bird mini-game!");
                return new FlappyBirdMiniGame(pointsManager, this);
            case 1:
                System.out.println("Starting Asteroid Dodge mini-game!");
                return new AsteroidDodgeMiniGame(pointsManager, this);
            default:
                // Default to Flappy Bird if something goes wrong
                return new FlappyBirdMiniGame(pointsManager, this);
        }
    }

    // Method to handle mini-game completion
    public void onMiniGameCompleted() {
        // Set cooldown
        miniGameCooldown = COOLDOWN_DURATION;

        try {
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
                        // Choose a random angle
                        float angle = random.nextFloat() * 2f * (float)Math.PI;
                        
                        // Choose a distance between 300 and 500 units
                        float distance = 300f + random.nextFloat() * 200f;
                        
                        // Calculate new position
                        float newX = playerPos.x + distance * (float)Math.cos(angle);
                        float newY = playerPos.y + distance * (float)Math.sin(angle);
                        
                        // Create a new respawn position vector
                        Vector2 respawnPos = new Vector2(newX, newY);
                        
                        // Set the alien to this position
                        if (alien.getPhysicsBody() != null) {
                            alien.getPhysicsBody().setLocation(respawnPos.x, respawnPos.y);
                            
                            // Also reset velocity to ensure they don't keep moving from previous state
                            alien.getPhysicsBody().setLinearVelocity(0, 0);
                            
                            System.out.println("Respawned alien at distance " + distance + " from player");
                        }
                    }
                }
            } else {
                // Fallback to original respawn logic if player is null
                System.out.println("Player not found, using original alien respawn positions");
                List<Entity> aliens = entityManager.getEntitiesByType(EntityType.ALIEN);
                for (Entity entity : aliens) {
                    if (entity instanceof Alien) {
                        ((Alien) entity).respawn();
                    }
                }
            }
        } catch (Exception e) {
            // Log and use the original respawn logic as fallback
            System.err.println("Error respawning aliens after mini-game: " + e.getMessage());
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
    }

    // Method to update cooldown timer
    public void update(float deltaTime) {
        if (miniGameCooldown > 0) {
            miniGameCooldown -= deltaTime;
        }
    }
}