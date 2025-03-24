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
    protected abstractengine.entity.PhysicsBody physicsBody;

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
        // Use the entity constructor that resizes the texture
        super(EntityType.OBSTACLE,
              texture.toString().replace("Texture: file:", ""),
              new Vector2(width, height),
              new Vector2(x, y),
              new Vector2(0, -1), // Direction downward
              new Vector2(0, 0),
              speedY,
              AsteroidState.ACTIVE,
              AsteroidAction.NONE);

        this.speedY = speedY;
        this.bounds = new Rectangle(x - width/2, y - height/2, width, height);

        // Set physics body right away
        this.setPhysicsBody(new application.minigame.utils.DummyPhysicsBody(this));
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
        // Move the position downward
        Vector2 position = getPosition();
        position.y -= speedY * deltaTime;
        setPosition(position);

        // Update physics body position if it exists
        if (physicsBody != null && physicsBody instanceof application.minigame.utils.DummyPhysicsBody) {
            ((application.minigame.utils.DummyPhysicsBody)physicsBody).setDummyPosition(position);
        }

        // Update collision bounds
        bounds.x = position.x - bounds.width/2;
        bounds.y = position.y - bounds.height/2;
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
    public void setPhysicsBody(abstractengine.entity.PhysicsBody physicsBody) {
        this.physicsBody = physicsBody;
    }

    // Optionally, add a getter if needed
    public abstractengine.entity.PhysicsBody getPhysicsBody() {
        return physicsBody;
    }
}

