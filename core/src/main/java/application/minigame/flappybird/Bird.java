package application.minigame.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef;

import abstractengine.entity.DynamicTextureObject;
import abstractengine.entity.PhysicsBody;
import abstractengine.utils.DisplayManager;
import application.entity.EntityType;
import application.minigame.flappybird.BirdBehaviour;

/**
 * Bird class for FlappyBirdGame.
 * This class represents the bird that the player controls.
 */
public class Bird extends DynamicTextureObject<BirdBehaviour.State, BirdBehaviour.Action> {
    // Bird constants
    public static final float WIDTH = 40f;
    private static final float DEFAULT_SPEED = 10.0f;
    public static final float HEIGHT = 40f;

    // Bird properties
    private final Vector2 size;
    private final Vector2 initialPosition;
    private BirdBehaviour.State state;
    private float velocityY;
    private Rectangle bounds; // For collision detection with pipes
    private BirdBehaviour.Action actionState;
    protected PhysicsBody physicsBody;

    /**
     * Creates a new Bird with a texture
     */
    public Bird(Texture texture, float x, float y, float width, float height) {
        super(EntityType.PLAYER,
              texture.toString().replace("Texture: file:", ""), // Clean texture path
              new Vector2(width, height), // Size parameter for scaling
              new Vector2(x, y),
              new Vector2(0, 0),
              new Vector2(0, 0),
              DEFAULT_SPEED,
              BirdBehaviour.State.IDLE,
              BirdBehaviour.Action.NONE);

        // Initialize final fields
        this.size = new Vector2(width, height);
        this.initialPosition = new Vector2(x, y);

        // Set ship-specific properties
        this.bounds = new Rectangle(x - width/2, y - height/2, width, height);

        // Set initial state
        setState(BirdBehaviour.State.IDLE);
        setActionState(BirdBehaviour.Action.NONE);
    }

    public Bird(EntityType type, String texturePath, Vector2 size, Vector2 position,
               Vector2 direction, Vector2 rotation, float speed) {
        super(type, texturePath, size, position, direction, rotation, speed,
              BirdBehaviour.State.IDLE, BirdBehaviour.Action.NONE);

        this.size = size;
        this.initialPosition = new Vector2(position);
        setSpeed(speed);
        this.bounds = new Rectangle(position.x - size.x/2, position.y - size.y/2,
                                   size.x, size.y);
        this.velocityY = 0;
    }

    /**
     * Initialize physics body for the bird
     */
    public void initPhysics(World world) {
        initPhysicsBody(world, BodyDef.BodyType.DynamicBody);
        getPhysicsBody().getBody().setFixedRotation(true);
    }

    /**
     * Update bird position based on physics
     */
    public void update(float deltaTime) {
        // Update the bird's position using its vertical velocity
        Vector2 pos = getPosition();
        pos.y += velocityY * deltaTime;
        setPosition(pos);

        // Update the rectangle bounds to match the bird's new position
        bounds.x = pos.x - bounds.width / 2;
        bounds.y = pos.y - bounds.height / 2;

        // Update state based on vertical velocity
        if (velocityY > 0) {
            setState(BirdBehaviour.State.FLYING);
        } else {
            setState(BirdBehaviour.State.FALLING);
        }
    }

    /**
     * Get the bird's bounds for collision detection
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Get the bird's Y velocity
     */
    public float getVelocityY() {
        return velocityY;
    }

    /**
     * Set the bird's Y velocity
     */
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;

        // Apply velocity to physics body if available
        if (getPhysicsBody() != null && getPhysicsBody().getBody() != null) {
            getPhysicsBody().getBody().setLinearVelocity(0, velocityY);
        }
    }

    /**
     * Reset the bird to its initial position
     */
    public void reset() {
        setPosition(new Vector2(initialPosition.x, initialPosition.y));

        if (getPhysicsBody() != null && getPhysicsBody().getBody() != null) {
            getPhysicsBody().getBody().setTransform(initialPosition.x, initialPosition.y, 0);
            getPhysicsBody().getBody().setLinearVelocity(0, 0);
        }

        bounds.x = initialPosition.x - bounds.width / 2;
        bounds.y = initialPosition.y - bounds.height / 2;
        velocityY = 0;
        setState(BirdBehaviour.State.IDLE);
    }

    /**
     * Make the bird flap (jump)
     */
    public void flap() {
        setActionState(BirdBehaviour.Action.FLAP);
    }

    /**
     * Set the bird to dead state
     */
    public void die() {
        setState(BirdBehaviour.State.DEAD);
    }

    /**
     * Sets the bird's state
     */
    public void setState(BirdBehaviour.State state) {
        this.state = state;
    }

    /**
     * Returns the bird's state
     */
    public BirdBehaviour.State getState() {
        return state;
    }

    /**
     * Sets the bird's action state
     */
    public void setActionState(BirdBehaviour.Action action) {
        this.actionState = action;
    }

    /**
     * Returns the bird's action state
     */
    public BirdBehaviour.Action getActionState() {
        return this.actionState;
    }

    /**
     * Check if bird is dead
     */
    public boolean isDead() {
        return getState() == BirdBehaviour.State.DEAD;
    }

    /**
     * Set the physics body
     */
    public void setPhysicsBody(PhysicsBody physicsBody) {
        this.physicsBody = physicsBody;
    }

    /**
     * Get the physics body
     */
    public PhysicsBody getPhysicsBody() {
        return physicsBody;
    }

    /**
     * Get the width (convenience method)
     */
    public float getWidth() {
        return size.x;
    }

    /**
     * Get the height (convenience method)
     */
    public float getHeight() {
        return size.y;
    }

    /**
     * Custom draw method to ensure proper rendering
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (getTexture() == null) return;

        Vector2 position = getPosition();
        float width = this.size.x;
        float height = this.size.y;

        batch.draw(
            getTexture(),
            position.x - width / 2,  // X position (centered)
            position.y - height / 2, // Y position (centered)
            width / 2,   // Origin X
            height / 2,  // Origin Y
            width,       // Width
            height,      // Height
            1.0f,        // Scale X
            1.0f,        // Scale Y
            0.0f,        // Rotation
            0, 0,        // Source rectangle X, Y
            getTexture().getWidth(), getTexture().getHeight(), // Source width, height
            false, false // Flip X, Y
        );
    }
}

