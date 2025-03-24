package application.minigame.asteroids;

import com.badlogic.gdx.graphics.Texture;
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
    private static final float MIN_ASTEROID_SPEED = 150f;
    private static final float MAX_ASTEROID_SPEED = 350f;
    private static final float BASE_ASTEROID_SIZE = 40f;

    // Asteroids
    private Array<Asteroid> asteroids;
    private Texture asteroidTexture;
    private float timeSinceLastAsteroid;
    private float spawnInterval;

    // Game parameters
    private float asteroidSizeMultiplier = 1.0f;

    /**
     * Creates a new AsteroidManager
     *
     * @param asteroidTexture The texture for asteroids
     */
    public AsteroidManager(Texture asteroidTexture) {
        this.asteroidTexture = asteroidTexture;
        this.asteroids = new Array<>();
        this.timeSinceLastAsteroid = 0;
        this.spawnInterval = INITIAL_SPAWN_INTERVAL;
    }

    /**
     * Update asteroid positions and generate new asteroids as needed
     *
     * @param deltaTime Time since last update
     * @param gameTime Current game time
     */
    public void update(float deltaTime, float gameTime) {
        // Update time since last asteroid
        timeSinceLastAsteroid += deltaTime;

        // Update spawn interval based on game time
        spawnInterval = INITIAL_SPAWN_INTERVAL * Math.max(0.3f, 1.0f - gameTime / 60f);

        // Update asteroid size multiplier based on game time
        asteroidSizeMultiplier = Math.min(1.5f, 1.0f + gameTime / 30f);

        // Generate new asteroid if needed
        if (timeSinceLastAsteroid > spawnInterval) {
            spawnAsteroid();
            timeSinceLastAsteroid = 0;
        }

        // Update all asteroids
        for (int i = 0; i < asteroids.size; i++) {
            Asteroid asteroid = asteroids.get(i);
            asteroid.update(deltaTime);

            // Remove asteroids that are off screen
            if (asteroid.getY() + asteroid.getBounds().height < 0) {
                asteroids.removeIndex(i);
                i--;
            }
        }
    }

    /**
     * Generate a new asteroid
     */
    private void spawnAsteroid() {
        float screenWidth = DisplayManager.getScreenWidth();
        float screenHeight = DisplayManager.getScreenHeight();

        // Random position at top of screen
        float asteroidSize = BASE_ASTEROID_SIZE * asteroidSizeMultiplier * MathUtils.random(0.7f, 1.3f);
        float x = MathUtils.random(0, screenWidth - asteroidSize);
        float y = screenHeight;

        // Random speed
        float speed = MathUtils.random(MIN_ASTEROID_SPEED, MAX_ASTEROID_SPEED);

        // Create asteroid
        Asteroid asteroid = new Asteroid(asteroidTexture, x, y, asteroidSize, asteroidSize, speed);
        asteroids.add(asteroid);
    }

    /**
     * Get all asteroids
     */
    public Array<Asteroid> getAsteroids() {
        return asteroids;
    }

    /**
     * Reset all asteroids
     */
    public void reset() {
        asteroids.clear();
        timeSinceLastAsteroid = 0;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
    }
}
