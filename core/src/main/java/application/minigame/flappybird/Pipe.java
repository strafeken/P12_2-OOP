package application.minigame.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.StaticTextureObject;
import application.entity.EntityType;

/**
 * Represents a pipe obstacle in the FlappyBirdGame.
 * Now extends StaticTextureObject for better integration with the engine.
 */
public class Pipe extends StaticTextureObject {
    // Pipe-specific properties
    private boolean isTop;
    private boolean passed;
    private float width;
    private float height;

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
        // Use the default constructor and then set texture manually
        super();
        setTexture(texture);
        setPosition(new Vector2(x, y));

        // Store width and height locally since we can't set them after construction
        this.width = width;
        this.height = height;

        // Set pipe-specific properties
        this.isTop = isTop;
        this.passed = false;

        // Debug info
        System.out.println("Created pipe: " + (isTop ? "TOP" : "BOTTOM") +
                          " at x=" + x + ", y=" + y +
                          ", width=" + width + ", height=" + height);
    }

    /**
     * Creates a new pipe with full entity parameters
     */
    public Pipe(EntityType type, String texturePath, Vector2 size, Vector2 position,
                boolean isTop) {
        // Use the available constructor from StaticTextureObject
        super(type, texturePath, size, position, new Vector2(0, 0));

        this.width = size.x;
        this.height = size.y;
        this.isTop = isTop;
        this.passed = false;
    }

    /**
     * Update the pipe position
     *
     * @param deltaTime Time since last update
     * @param speed Pipe movement speed
     */
    public void update(float deltaTime, float speed) {
        // Use the inherited position and update it
        Vector2 currentPosition = getPosition();
        setPosition(new Vector2(currentPosition.x - speed * deltaTime, currentPosition.y));
    }

    /**
     * Get the pipe's X position
     */
    public float getX() {
        return getPosition().x;
    }
    public float getY() {
        return getPosition().y;
    }

    /**
     * Get the pipe's bounds for collision detection
     */
    public Rectangle getBounds() {
        // Create a rectangle based on position and our stored width/height
        Vector2 position = getPosition();
        return new Rectangle(position.x - width/2, position.y - height/2,
                            width, height);
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
        return passed;
    }

    /**
     * Mark the pipe as passed
     */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    /**
     * Get the width of the pipe
     */
    public float getWidth() {
        return width;
    }

    /**
     * Get the height of the pipe
     */
    public float getHeight() {
        return height;
    }
}
