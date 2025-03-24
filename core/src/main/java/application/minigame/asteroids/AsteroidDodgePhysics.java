package application.minigame.asteroids;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Handles physics calculations for AsteroidDodgeGame.
 */
public class AsteroidDodgePhysics {
    // Game objects
    private Ship ship;
    private AsteroidManager asteroidManager;

    // Collision settings
    private float collisionGracePeriod;
    private boolean collisionEnabled = true;
    private float collisionMargin = 0.2f; // Smaller collision box than visual

    // Collision state
    private boolean hasCollided;

    /**
     * Creates a new AsteroidDodgePhysics instance
     *
     * @param ship The ship object
     * @param asteroidManager The asteroid manager
     * @param collisionGracePeriod Grace period before collisions are enabled
     */
    public AsteroidDodgePhysics(Ship ship, AsteroidManager asteroidManager, float collisionGracePeriod) {
        this.ship = ship;
        this.asteroidManager = asteroidManager;
        this.collisionGracePeriod = collisionGracePeriod;
        this.hasCollided = false;
    }

    /**
     * Update physics
     *
     * @param deltaTime Time since last update
     * @param gameTime Current game time
     */
    public void update(float deltaTime, float gameTime) {
        // Reset collision flag
        hasCollided = false;

        // Enable collisions after grace period
        collisionEnabled = gameTime >= collisionGracePeriod;

        // If collisions are disabled, no need to check
        if (!collisionEnabled) {
            return;
        }

        // Get ship bounds with margin for more forgiving collisions
        Rectangle shipBounds = ship.getBounds();
        float margin = Math.min(shipBounds.width, shipBounds.height) * collisionMargin;
        Rectangle collisionBounds = new Rectangle(
            shipBounds.x + margin,
            shipBounds.y + margin,
            shipBounds.width - 2 * margin,
            shipBounds.height - 2 * margin
        );

        // Check collisions with asteroids
        Array<Asteroid> asteroids = asteroidManager.getAsteroids();
        for (Asteroid asteroid : asteroids) {
            if (Intersector.overlaps(collisionBounds, asteroid.getBounds())) {
                hasCollided = true;
                break;
            }
        }
    }

    /**
     * Check if ship has collided
     */
    public boolean hasCollided() {
        return hasCollided;
    }

    /**
     * Check if collision detection is enabled
     */
    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    /**
     * Reset physics state
     */
    public void reset() {
        hasCollided = false;
        collisionEnabled = false; // Start with collision disabled until grace period passes
    }
}
