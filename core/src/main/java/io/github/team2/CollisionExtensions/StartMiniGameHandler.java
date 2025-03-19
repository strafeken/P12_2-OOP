package io.github.team2.CollisionExtensions;

import java.util.List;
import java.util.Random;

import io.github.team2.Alien;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.IEntityManager;
import io.github.team2.MiniGame.AsteroidDodgeMiniGame;
import io.github.team2.MiniGame.FlappyBirdMiniGame;
import io.github.team2.MiniGame.SpaceQuizMiniGame;
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
        // Choose a random number between 0 and 2
        int gameChoice = random.nextInt(3);
        
        switch (gameChoice) {
            case 0:
                System.out.println("Starting Flappy Bird mini-game!");
                return new FlappyBirdMiniGame(pointsManager, this);
            case 1:
                System.out.println("Starting Asteroid Dodge mini-game!");
                return new AsteroidDodgeMiniGame(pointsManager, this);
            case 2:
                System.out.println("Starting Space Quiz mini-game!");
                return new SpaceQuizMiniGame(pointsManager, this);
            default:
                // Default to Flappy Bird if something goes wrong
                return new FlappyBirdMiniGame(pointsManager, this);
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
