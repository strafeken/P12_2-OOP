package application.minigame.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import abstractengine.entity.DynamicTextureObject;
import abstractengine.entity.PhysicsBody;
import application.entity.EntityType;

/**
 * Asteroid class for the Asteroid Dodge mini-game
 */
public class Asteroid extends DynamicTextureObject<AsteroidBehaviour.State, AsteroidBehaviour.Action> {
    private float speed;
    private boolean counted = false;
    private Rectangle bounds = new Rectangle();
    private Vector2 size; // Add a local size field
    private Texture asteroidTexture; // Store texture reference

    // Add physicsBody field like in Ship class
    protected PhysicsBody physicsBody;

    /**
     * Creates a new asteroid
     */
    public Asteroid(Texture texture, float x, float y, float width, float height, float speed) {
        super(EntityType.ASTEROID,
              "barrel-2.png",    // Use the actual file name instead of texture.toString()
              new Vector2(width, height),
              new Vector2(x, y),
              new Vector2(0, -1),
              new Vector2(0, 0),
              speed,
              AsteroidBehaviour.State.MOVING,
              AsteroidBehaviour.Action.MOVE);

        this.speed = speed;
        this.size = new Vector2(width, height);
        this.bounds.set(x - width/2, y - height/2, width, height);
        this.asteroidTexture = texture; // Store texture locally - don't call setTexture

        // Debug output to verify texture
        if (texture != null) {
            System.out.println("Asteroid created with texture: " + texture +
                              " dimensions: " + texture.getWidth() + "x" + texture.getHeight());
        } else {
            System.err.println("WARNING: Asteroid texture is null!");
        }
    }

    /**
     * Update the asteroid position based on its speed
     */
    public void update(float deltaTime) {
        // Calculate movement distance - increase speed for debugging
        float distance = speed * deltaTime;

        // Get current position
        Vector2 currentPos = getPosition();

        // Calculate new position - move downward
        float newY = currentPos.y - distance;

        // Debug output to trace movement
        System.out.println("ASTEROID: moving from y=" + currentPos.y +
                          " to y=" + newY +
                          " (delta=" + distance + ", speed=" + speed + ")");

        // Update position in parent class
        setPosition(new Vector2(currentPos.x, newY));

        // Also update our local size and bounds for collision detection
        this.bounds.setPosition(currentPos.x - bounds.width/2, newY - bounds.height/2);
    }

    // Override update() to call our custom update method - critical for movement!
    @Override
    public void update() {
        // Get delta time from Gdx.graphics
        float deltaTime = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        // Call our custom update method that moves the asteroid
        update(deltaTime);
    }

    @Override
    public Texture getTexture() {
        // Return our stored texture instead of the parent's
        return asteroidTexture;
    }

    @Override
    public boolean isOutOfBound(Vector2 direction) {
        // Return false to prevent automatic boundary checking
        return false;
    }

    @Override
    public void initActionMap() {
        // Empty implementation to satisfy abstract method
    }

    /**
     * Check if this asteroid has been counted for scoring
     */
    public boolean isCounted() {
        return counted;
    }

    /**
     * Mark this asteroid as counted for scoring
     */
    public void setCounted(boolean counted) {
        this.counted = counted;
    }

    /**
     * Get the collision bounds of this asteroid
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Set the physics body for this asteroid
     *
     * @param physicsBody The physics body
     */
    public void setPhysicsBody(PhysicsBody physicsBody) {
        this.physicsBody = physicsBody;
    }

    /**
     * Get the physics body for this asteroid
     *
     * @return The physics body
     */
    public PhysicsBody getPhysicsBody() {
        return physicsBody;
    }

    /**
     * Get the Y position (convenience method)
     *
     * @return Y position
     */
    public float getY() {
        return getPosition().y;
    }

    /**
     * Get the X position (convenience method)
     *
     * @return X position
     */
    public float getX() {
        return getPosition().x;
    }

    /**
     * Get the size of this asteroid
     *
     * @return Size as Vector2
     */
    public Vector2 getSize() {
        return size;
    }

    /**
     * Get the height (convenience method)
     *
     * @return Height
     */
    public float getHeight() {
        return size.y;
    }

    /**
     * Get the width (convenience method)
     *
     * @return Width
     */
    public float getWidth() {
        return size.x;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (getTexture() == null) {
            System.err.println("ERROR: Asteroid texture is null in draw method!");
            return;
        }

        // Get current position and use the actual size from asteroid properties
        Vector2 position = getPosition();
        float width = this.size.x;   // Use the size that was set in constructor
        float height = this.size.y;  // Use the size that was set in constructor

        // Draw the texture with the actual asteroid size, centered at the object's position
        batch.draw(
            getTexture(),
            position.x - width / 2,  // X position (centered)
            position.y - height / 2, // Y position (centered)
            width / 2,  // Origin X
            height / 2, // Origin Y
            width,      // Width
            height,     // Height
            1.0f,       // Scale X
            1.0f,       // Scale Y
            0.0f,       // Rotation (no rotation)
            0, 0,       // Source rectangle X, Y
            getTexture().getWidth(), getTexture().getHeight(), // Source width, height
            false, false // Flip X, Y
        );
    }
}
