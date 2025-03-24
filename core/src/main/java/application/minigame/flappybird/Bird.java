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
    // Physics constants
    private static final float WIDTH = 32;
    private static final float HEIGHT = 24;
    private static final float INITIAL_Y = DisplayManager.getScreenHeight() / 2;
    private static final float INITIAL_X = DisplayManager.getScreenWidth() / 4;

    // Bird-specific properties
    private float velocityY;
    private Rectangle bounds; // For collision detection with pipes
    private BirdState birdState;
    private BirdAction actionState;

    /**
     * Creates a new Bird with the given texture
     *
     * @param texture The bird texture
     */
    public Bird(Texture texture) {
        super(texture);

        // Initialize position and state
        setPosition(new Vector2(INITIAL_X, INITIAL_Y));
        setState(BirdState.IDLE);
        setActionState(BirdAction.NONE);

        // Initialize bird-specific properties
        this.velocityY = 0;
        this.bounds = new Rectangle(INITIAL_X, INITIAL_Y, WIDTH, HEIGHT);

        System.out.println("Bird created at: " + getPosition().x + "," + getPosition().y +
                          " size: " + getWidth() + "x" + getHeight());
    }

    /**
     * Creates a new Bird with all parameters
     */
    public Bird(EntityType type, String texturePath, Vector2 size, Vector2 position,
                Vector2 direction, Vector2 rotation, float speed) {
        super(type, texturePath, size, position, direction, rotation, speed,
              BirdState.IDLE, BirdAction.NONE);

        this.velocityY = 0;
        this.bounds = new Rectangle(position.x, position.y, size.x, size.y);
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
        setPosition(new Vector2(INITIAL_X, INITIAL_Y));

        if (getPhysicsBody() != null && getPhysicsBody().getBody() != null) {
            getPhysicsBody().getBody().setTransform(INITIAL_X, INITIAL_Y, 0);
            getPhysicsBody().getBody().setLinearVelocity(0, 0);
        }

        bounds.x = INITIAL_X - bounds.width / 2;
        bounds.y = INITIAL_Y - bounds.height / 2;
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
        this.birdState = state;
    }
    /**
     * Returns the bird's state
     */
    public BirdState getState() {
        return this.birdState;
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
}

