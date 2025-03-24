package application.minigame.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.DynamicTextureObject;
import abstractengine.utils.DisplayManager;
import application.entity.EntityType;
import application.minigame.asteroids.ShipState;
import application.minigame.asteroids.ShipAction;

/**
 * Represents the player's ship in the AsteroidDodgeGame.
 */
public class Ship extends DynamicTextureObject<ShipState, ShipAction> {
    // Ship constants
    private static final float DEFAULT_SPEED = 300.0f;
    private static final float MAX_SPEED = 600.0f;

    // Ship properties
    private float speed;
    private Rectangle bounds; // For collision detection
    private float invulnerabilityTimer;

    // Added state variables and accessor methods for ship state and action
    private ShipState state;
    private ShipAction actionState;

    public ShipState getState() {
        return state;
    }

    public void setState(ShipState state) {
        this.state = state;
    }

    public ShipAction getActionState() {
        return actionState;
    }

    public void setActionState(ShipAction actionState) {
        this.actionState = actionState;
    }

    /**
     * Creates a new Ship
     *
     * @param texture The ship texture
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     */
    public Ship(Texture texture, float x, float y, float width, float height) {
        super(texture);

        // Set position
        setPosition(new Vector2(x, y));

        // Set ship-specific properties
        this.speed = DEFAULT_SPEED;
        this.bounds = new Rectangle(x - width/2, y - height/2, width, height);
        this.invulnerabilityTimer = 0;

        // Set initial state
        setState(ShipState.IDLE);
        setActionState(ShipAction.NONE);
    }

    /**
     * Creates a new Ship with full entity parameters
     */
    public Ship(EntityType type, String texturePath, Vector2 size, Vector2 position,
               Vector2 direction, Vector2 rotation, float speed) {
        super(type, texturePath, size, position, direction, rotation, speed,
              ShipState.IDLE, ShipAction.NONE);

        this.speed = speed;
        this.bounds = new Rectangle(position.x - size.x/2, position.y - size.y/2,
                                   size.x, size.y);
        this.invulnerabilityTimer = 0;
    }

    /**
     * Update the ship position
     *
     * @param deltaTime Time since last update
     * @param moveDirection -1 for left, 0 for none, 1 for right
     */
    public void update(float deltaTime, int moveDirection) {
        // Update position based on input
        Vector2 position = getPosition();

        if (moveDirection != 0) {
            float moveAmount = speed * deltaTime * moveDirection;
            position.x += moveAmount;

            // Set state and action based on movement
            setState(ShipState.MOVING);
            setActionState(moveDirection < 0 ? ShipAction.MOVE_LEFT : ShipAction.MOVE_RIGHT);
        } else {
            setState(ShipState.IDLE);
            setActionState(ShipAction.NONE);
        }

        // Constrain position to screen bounds
        float halfWidth = getWidth() / 2;
        position.x = Math.max(halfWidth, Math.min(DisplayManager.getScreenWidth() - halfWidth, position.x));

        setPosition(position);

        // Update collision bounds to match position
        bounds.x = position.x - bounds.width/2;
        bounds.y = position.y - bounds.height/2;

        // Update invulnerability timer
        if (invulnerabilityTimer > 0) {
            invulnerabilityTimer -= deltaTime;

            if (invulnerabilityTimer <= 0) {
                setState(ShipState.IDLE);
            }
        }
    }

    /**
     * Get the ship's bounds for collision detection
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Make the ship temporarily invulnerable
     */
    public void makeInvulnerable(float duration) {
        setState(ShipState.INVULNERABLE);
        invulnerabilityTimer = duration;
    }

    /**
     * Check if the ship is currently invulnerable
     */
    public boolean isInvulnerable() {
        return getState() == ShipState.INVULNERABLE;
    }

    /**
     * Set the ship to destroyed state
     */
    public void destroy() {
        setState(ShipState.DESTROYED);
    }

    /**
     * Check if ship is destroyed
     */
    public boolean isDestroyed() {
        return getState() == ShipState.DESTROYED;
    }

    /**
     * Set the ship's speed
     */
    public void setSpeed(float speed) {
        this.speed = Math.min(speed, MAX_SPEED);
    }

    /**
     * Boost the ship's speed temporarily
     */
    public void boost() {
        setSpeed(MAX_SPEED);
        setActionState(ShipAction.BOOST);
    }

    /**
     * Reset the ship to its default state
     */
    public void reset(float x, float y) {
        setPosition(new Vector2(x, y));
        setState(ShipState.IDLE);
        setActionState(ShipAction.NONE);
        this.speed = DEFAULT_SPEED;
        this.invulnerabilityTimer = 0;

        bounds.x = x - bounds.width/2;
        bounds.y = y - bounds.height/2;
    }

    /**
     * Move the ship by a specified amount
     *
     * @param dx Change in x position
     * @param dy Change in y position
     */
    public void move(float dx, float dy) {
        // Get current position
        Vector2 pos = getPosition();
        pos.add(dx, dy);

        // Optionally update state based on movement
        if (dx != 0 || dy != 0) {
            // For example, if moving horizontally, update state accordingly
            if (dx < 0) {
                setActionState(ShipAction.MOVE_LEFT);
            } else if (dx > 0) {
                setActionState(ShipAction.MOVE_RIGHT);
            }
            setState(ShipState.MOVING);
        } else {
            setState(ShipState.IDLE);
            setActionState(ShipAction.NONE);
        }

        setPosition(pos);

        // Update collision bounds if you maintain them separately
        bounds.x = pos.x - bounds.width / 2;
        bounds.y = pos.y - bounds.height / 2;
    }
}
