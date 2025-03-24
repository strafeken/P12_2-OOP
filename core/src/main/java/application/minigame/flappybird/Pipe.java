package application.minigame.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents a pipe obstacle in the FlappyBirdGame.
 */
public class Pipe {
    private Rectangle bounds;
    private Texture texture;
    private boolean isTop;
    private boolean passed;

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
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
        this.isTop = isTop;
        this.passed = false;

        // Debug info
        System.out.println("Created pipe: " + (isTop ? "TOP" : "BOTTOM") +
                          " at x=" + x + ", y=" + y +
                          ", width=" + width + ", height=" + height);
    }

    /**
     * Update the pipe position
     *
     * @param deltaTime Time since last update
     * @param speed Pipe movement speed
     */
    public void update(float deltaTime, float speed) {
        bounds.x -= speed * deltaTime;
    }

    /**
     * Get the pipe's X position
     */
    public float getX() {
        return bounds.x;
    }

    /**
     * Get the pipe's bounds
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Get the pipe's texture
     */
    public Texture getTexture() {
        return texture;
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
}
