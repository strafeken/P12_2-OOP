package application.minigame.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.DynamicTextureObject;
import application.entity.EntityType;
import application.minigame.asteroids.AsteroidState;
import application.minigame.asteroids.AsteroidAction;
/**
 * Represents an asteroid obstacle in the AsteroidDodgeGame.
 * Now extends DynamicTextureObject for better integration with the engine.
 */
public class Asteroid extends DynamicTextureObject<AsteroidState, AsteroidAction> {
    // Asteroid properties
    private float speedY;
    private Rectangle bounds; // For collision detection

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
        super(texture);

        // Set position
        setPosition(new Vector2(x, y));

        // Set asteroid-specific properties
        this.speedY = speedY;
        this.bounds = new Rectangle(x - width/2, y - height/2, width, height);

        // Set initial state
        setState(AsteroidState.ACTIVE);
        setActionState(AsteroidAction.NONE);
    }

    /**
     * Creates a new Asteroid with full entity parameters
     */
    public Asteroid(EntityType type, String texturePath, Vector2 size, Vector2 position,
                   Vector2 direction, Vector2 rotation, float speed, float speedY) {
        super(type, texturePath, size, position, direction, rotation, speed,
              AsteroidState.ACTIVE, AsteroidAction.NONE);

        this.speedY = speedY;
        this.bounds = new Rectangle(position.x - size.x/2, position.y - size.y/2,
                                   size.x, size.y);
    }

    private AsteroidState state;
    private AsteroidAction action;

    public void setState(AsteroidState state) {
        this.state = state;
    }

    public AsteroidState getState() {
        return state;
    }

    public void setActionState(AsteroidAction action) {
        this.action = action;
    }

    /**
     * Update the asteroid position
     *
     * @param deltaTime Time since last update
     */
    public void update(float deltaTime) {
        // Update position based on speed
        Vector2 position = getPosition();
        position.y -= speedY * deltaTime;
        setPosition(position);

        // Update collision bounds to match position
        bounds.y = position.y - bounds.height/2;
        bounds.x = position.x - bounds.width/2;

        // Set action state to MOVE while moving
        setActionState(AsteroidAction.MOVE);
    }

    /**
     * Get the asteroid's Y position
     */
    public float getY() {
        return getPosition().y;
    }

    /**
     * Get the asteroid's bounds for collision detection
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Set the asteroid to destroyed state
     */
    public void destroy() {
        setState(AsteroidState.DESTROYED);
        setActionState(AsteroidAction.EXPLODE);
    }

    /**
     * Check if asteroid is destroyed
     */
    public boolean isDestroyed() {
        return getState() == AsteroidState.DESTROYED;
    }

    /**
     * Set the asteroid's speed
     */
    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    /**
     * Get the asteroid's speed
     */
    public float getSpeedY() {
        return speedY;
    }
}
