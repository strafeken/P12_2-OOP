package application.minigame.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef;

import abstractengine.entity.DynamicTextureObject;
import abstractengine.utils.DisplayManager;
import application.entity.EntityType;
import application.minigame.flappybird.BirdState;

import application.minigame.flappybird.BirdAction;

/**
 * Bird class for FlappyBirdGame.
 * This class represents the bird that the player controls.
 */
public class Bird extends DynamicTextureObject<BirdState, BirdAction> {
    // Configurable properties (initialize in constructor)
    public static final float WIDTH = 40f;
    private static final float DEFAULT_SPEED = 10.0f;
    public static final float HEIGHT = 40f;
    private final Vector2 size;
    private final Vector2 initialPosition;
    private BirdState state;
    // Bird-specific properties
    private float velocityY;
    private Rectangle bounds; // For collision detection with pipes

    private BirdAction actionState;
    protected abstractengine.entity.PhysicsBody physicsBody;

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
              BirdState.IDLE,
              BirdAction.NONE);

        // Initialize final fields
        this.size = new Vector2(width, height);
        this.initialPosition = new Vector2(x, y);

        // Set ship-specific properties
        this.bounds = new Rectangle(x - width/2, y - height/2, width, height);

        // Set initial state
        setState(BirdState.IDLE);
        setActionState(BirdAction.NONE);
    }





    public Bird(EntityType type, String texturePath, Vector2 size, Vector2 position,
               Vector2 direction, Vector2 rotation, float speed) {
        super(type, texturePath, size, position, direction, rotation, speed,
              BirdState.IDLE, BirdAction.NONE);

        this.size = size;
        this.initialPosition = new Vector2(position);
        setSpeed(speed);
        this.bounds = new Rectangle(position.x - size.x/2, position.y - size.y/2,
                                   size.x, size.y);
        this.velocityY = 0;
    }


    /**
     * Creates a new Bird with all parameters
    public Bird(EntityType type, String texturePath, Vector2 size, Vector2 position,
                Vector2 direction, Vector2 rotation, float speed) {
        super(type, texturePath, size, position, direction, rotation, speed,
              BirdState.IDLE, BirdAction.NONE);

        this.size = size;
        this.initialPosition = new Vector2(position);
        this.bounds = new Rectangle(position.x - size.x/2, position.y - size.y/2, size.x, size.y);
        this.velocityY = 0;
    }
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
            setState(BirdState.FLYING);
        } else {
            setState(BirdState.FALLING);
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
        setState(BirdState.IDLE);
    }

    /**
     * Make the bird flap (jump)
     */
    public void flap() {
        setActionState(BirdAction.FLAP);
    }

    /**
     * Set the bird to dead state
     */
    public void die() {
        setState(BirdState.DEAD);
    }
    /**
     * Sets the bird's state
     */
    public void setState(BirdState state) {
        this.state = state;
    }
    /**
     * Returns the bird's state
     */
    public BirdState getState() {
        return state;
    }

    /**
     * Sets the bird's action state
     */
    public void setActionState(BirdAction action) {
        this.actionState = action;
    }

    /**
     * Returns the bird's action state
     */
    public BirdAction getActionState() {
        return this.actionState;
    }

    /**
     * Check if bird is dead
     */
    public boolean isDead() {
        return getState() == BirdState.DEAD;
    }

    public void setPhysicsBody(abstractengine.entity.PhysicsBody physicsBody) {
        this.physicsBody = physicsBody;
    }

    public abstractengine.entity.PhysicsBody getPhysicsBody() {
        return physicsBody;
    }
}

