package application.minigame.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.StaticTextureObject;
import abstractengine.entity.PhysicsBody;
import application.entity.EntityType;

/**
 * Represents a pipe obstacle in the FlappyBirdGame.
 * Now extends StaticTextureObject for better integration with the engine.
 */
public class Pipe extends StaticTextureObject {
    private boolean isTop;
    private boolean isPassed;
    private Vector2 size;
    private Texture pipeTexture;
    protected PhysicsBody physicsBody;

    /**
     * Creates a new pipe
     *
     * @param texture Pipe texture
     * @param x X position
     * @param y Y position
     * @param width Pipe width
     * @param height Pipe height
     * @param isTop Whether this is a top pipe (rendered upside-down)
     */
    public Pipe(Texture texture, float x, float y, float width, float height, boolean isTop) {
        super(EntityType.PIPE,
              "barrel-2.png",
              new Vector2(width, height),
              new Vector2(x, y),
              new Vector2(-1, 0)); // Added direction vector (moving left)

        this.isTop = isTop;
        this.isPassed = false;
        this.size = new Vector2(width, height);
        this.pipeTexture = texture;
    }

    /**
     * Creates a new pipe with full entity parameters
     */
    public Pipe(EntityType type, String texturePath, Vector2 size, Vector2 position,
                boolean isTop) {
        super(type, texturePath, size, position, new Vector2(-1, 0));

        this.size = size;
        this.isTop = isTop;
        this.isPassed = false;
        this.pipeTexture = new Texture(texturePath);
    }

    /**
     * Update pipe position
     *
     * @param deltaTime Time since last update
     * @param speed Pipe movement speed
     */
    public void update(float deltaTime, float speed) {
        // Move pipe to the left
        Vector2 position = getPosition();
        position.x -= speed * deltaTime;
        setPosition(position);
    }

    /**
     * Get the pipe's X position
     */
    public float getX() {
        return getPosition().x;
    }

    /**
     * Get the pipe's Y position
     */
    public float getY() {
        return getPosition().y;
    }

    /**
     * Get the width of the pipe
     */
    public float getWidth() {
        return size.x;
    }

    /**
     * Get the height of the pipe
     */
    public float getHeight() {
        return size.y;
    }

    /**
     * Check if the pipe is a top pipe
     */
    public boolean isTop() {
        return isTop;
    }

    /**
     * Check if the pipe has been passed
     */
    public boolean isPassed() {
        return isPassed;
    }

    /**
     * Mark the pipe as passed
     */
    public void setPassed(boolean passed) {
        this.isPassed = passed;
    }

    /**
     * Set the physics body for this pipe
     */
    public void setPhysicsBody(PhysicsBody physicsBody) {
        this.physicsBody = physicsBody;
    }

    /**
     * Get the physics body for this pipe
     */
    public PhysicsBody getPhysicsBody() {
        return physicsBody;
    }

    /**
     * Get the pipe texture
     */
    public Texture getTexture() {
        return pipeTexture;
    }

    /**
     * Draw the pipe
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (pipeTexture == null) return;

        Vector2 position = getPosition();
        float width = this.size.x;
        float height = this.size.y;

        batch.draw(
            pipeTexture,
            position.x - width / 2,   // X position (centered)
            position.y - height / 2,  // Y position (centered)
            width / 2,  // Origin X
            height / 2, // Origin Y
            width,      // Width
            height,     // Height
            1.0f,       // Scale X
            1.0f,       // Scale Y
            0.0f,       // Rotation (no rotation)
            0, 0,       // Source rectangle X, Y
            pipeTexture.getWidth(), pipeTexture.getHeight(), // Source width, height
            false, false // Flip X, Y
        );
    }

    /**
     * Get the pipe's bounds for collision detection
     */
    public Rectangle getBounds() {
        Vector2 position = getPosition();
        return new Rectangle(position.x - size.x / 2, position.y - size.y / 2,
                            size.x, size.y);
    }
}
