package application.minigame.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents an asteroid obstacle in the AsteroidDodgeGame.
 */
public class Asteroid {
    // Asteroid properties
    private Rectangle bounds;
    private Texture texture;
    private float speedY;

    /**
     * Creates a new Asteroid
     *
     * @param texture The asteroid texture
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     * @param speedY Vertical speed
     */
    public Asteroid(Texture texture, float x, float y, float width, float height, float speedY) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
        this.speedY = speedY;
    }

    /**
     * Update the asteroid position
     *
     * @param deltaTime Time since last update
     */
    public void update(float deltaTime) {
        bounds.y -= speedY * deltaTime;
    }

    /**
     * Get the asteroid's Y position
     */
    public float getY() {
        return bounds.y;
    }

    /**
     * Get the asteroid's bounds
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Get the asteroid's texture
     */
    public Texture getTexture() {
        return texture;
    }
}
