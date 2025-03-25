package application.minigame.asteroids;

import com.badlogic.gdx.graphics.Texture;
import abstractengine.entity.IEntityManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import abstractengine.utils.DisplayManager;

/**
 * Manages asteroid generation and recycling for AsteroidDodgeGame.
 */
public class AsteroidManager {
    // Constants for asteroid generation
    private static final float INITIAL_SPAWN_INTERVAL = 1.5f;
    private static final float MIN_SPAWN_INTERVAL = 0.5f;
    private static final float MIN_ASTEROID_SPEED = 300f;
    private static final float MAX_ASTEROID_SPEED = 600f;
    private static final float BASE_ASTEROID_SIZE = 40f;

    // Difficulty parameters with updated intervals as per requirements
    private static final float SPEED_INCREASE_INTERVAL = 9.0f;    // Speed increases every 9 seconds
    private static final float ASTEROID_COUNT_INCREASE_INTERVAL = 11.0f; // Count increases every 11 seconds
    private static final int MAX_SIMULTANEOUS_ASTEROIDS = 6;      // Maximum number of asteroids to spawn at once
    private static final float SPEED_INCREASE_FACTOR = 0.1f;     // Speed increases by 10%% each time

    // Asteroids
    private Array<Asteroid> asteroids;
    private Texture asteroidTexture;
    private float timeSinceLastAsteroid;
    private float spawnInterval;
    private IEntityManager entityManager;

    // Game parameters
    private float asteroidSizeMultiplier = 1.0f;
    private final float minAsteroidSize;
    private final float maxAsteroidSize;
    private float minSpawnInterval;
    private float maxSpawnInterval;

    // Add tracking for game time and difficulty level
    private float gameTime = 0;
    private int speedLevel = 1;
    private int asteroidCountLevel = 1;
    private int simultaneousAsteroidsCount = 1; // Start with 1 asteroid at a time
    private float speedMultiplier = 1.0f;

    /**
     * Creates a new AsteroidManager
     *
     * @param asteroidTexture The texture for asteroids
     * @param minSize Minimum asteroid size
     * @param maxSize Maximum asteroid size
     * @param minInterval Minimum spawn interval (seconds)
     * @param maxInterval Maximum spawn interval (seconds)
     */
    public AsteroidManager(IEntityManager entityManager, Texture asteroidTexture, float minSize, float maxSize, float minInterval, float maxInterval) {
        this.entityManager = entityManager;
        this.asteroidTexture = asteroidTexture;
        this.minAsteroidSize = minSize;
        this.maxAsteroidSize = maxSize;
        this.minSpawnInterval = minInterval;
        this.maxSpawnInterval = maxInterval;
        this.asteroids = new Array<>();
        this.timeSinceLastAsteroid = 0;
        this.spawnInterval = INITIAL_SPAWN_INTERVAL;
    }

    /**
     * Update asteroid positions and generate new asteroids as needed
     *
     * @param deltaTime Time since last update
     * @param currentGameTime Current game time
     */
    public void update(float deltaTime, float currentGameTime) {
        // Update game time
        this.gameTime = currentGameTime;

        // Check if speed should increase (every 9 seconds)
        int newSpeedLevel = 1 + (int)(gameTime / SPEED_INCREASE_INTERVAL);
        if (newSpeedLevel > speedLevel) {
            increaseSpeed();
            speedLevel = newSpeedLevel;
        }

        // Check if asteroid count should increase (every 20 seconds)
        int newAsteroidCountLevel = 1 + (int)(gameTime / ASTEROID_COUNT_INCREASE_INTERVAL);
        if (newAsteroidCountLevel > asteroidCountLevel) {
            increaseAsteroidCount();
            asteroidCountLevel = newAsteroidCountLevel;
        }

        // Update all existing asteroids
        for (int i = 0; i < asteroids.size; i++) {
            Asteroid asteroid = asteroids.get(i);
            asteroid.update(deltaTime);

            // Remove asteroids that are off screen
            if (asteroid.getY() + asteroid.getHeight() < 0) {
                entityManager.markForRemoval(asteroid); // Assuming this is the correct method in your implementation
                asteroids.removeIndex(i);
                i--;
            }
        }

        // Spawn new asteroids as needed
        timeSinceLastAsteroid += deltaTime;
        if (timeSinceLastAsteroid > spawnInterval) {
            // Spawn multiple asteroids based on current difficulty
            for (int i = 0; i < simultaneousAsteroidsCount; i++) {
                spawnAsteroid();
            }
            timeSinceLastAsteroid = 0;
            spawnInterval = MathUtils.random(minSpawnInterval, maxSpawnInterval);
        }
    }

    /**
     * Increase asteroid speed
     */
    private void increaseSpeed() {
        speedMultiplier += SPEED_INCREASE_FACTOR;
        System.out.println("Speed increased to level " + speedLevel +
                          ": multiplier = " + speedMultiplier);
    }

    /**
     * Increase number of simultaneous asteroids
     */
    private void increaseAsteroidCount() {
        // Increase number of simultaneous asteroids (max 3)
        if (simultaneousAsteroidsCount < MAX_SIMULTANEOUS_ASTEROIDS) {
            simultaneousAsteroidsCount++;
            System.out.println("Asteroid count increased to " + simultaneousAsteroidsCount);
        }
    }

    /**
     * Generate a new asteroid
     */
    private void spawnAsteroid() {
        float screenWidth = DisplayManager.getScreenWidth();
        float screenHeight = DisplayManager.getScreenHeight();

        // Random position at top of screen with some spacing for multiple asteroids
        float asteroidSize = MathUtils.random(minAsteroidSize, maxAsteroidSize);
        float x = MathUtils.random(0, screenWidth - asteroidSize);
        float y = screenHeight;

        // Base speed with current difficulty multiplier applied
        float baseSpeed = MathUtils.random(MIN_ASTEROID_SPEED, MAX_ASTEROID_SPEED);
        float speed = baseSpeed * speedMultiplier;

        // Create asteroid
        Asteroid asteroid = new Asteroid(asteroidTexture, x, y, asteroidSize, asteroidSize, speed);
        asteroids.add(asteroid);
        // Register with entity manager
        entityManager.addEntities(asteroid);

        // Ensure the physics body is set so drawing works correctly
        asteroid.setPhysicsBody(new application.minigame.utils.DummyPhysicsBody(asteroid));
    }

    /**
     * Reset all asteroids and difficulty settings
     */
    public void reset() {
        asteroids.clear();
        timeSinceLastAsteroid = 0;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
        speedLevel = 1;
        asteroidCountLevel = 1;
        simultaneousAsteroidsCount = 1;
        speedMultiplier = 1.0f;
        gameTime = 0;
    }

    /**
     * Get all asteroids
     */
    public Array<Asteroid> getAsteroids() {
        return asteroids;
    }
}
