package application.minigame.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import abstractengine.utils.DisplayManager;

/**
 * Bird class for FlappyBirdGame.
 * This class represents the bird that the player controls.
 */
public class Bird {
    // Bird properties
    private Rectangle bounds;
    private float velocityY;
    private Texture texture;

    // Physics constants
    private static final float WIDTH = 32;
    private static final float HEIGHT = 24;
    private static final float INITIAL_Y = DisplayManager.getScreenHeight() / 2;
    private static final float INITIAL_X = DisplayManager.getScreenWidth() / 4;

    /**
     * Creates a new Bird
     *
     * @param texture The bird texture
     */
    public Bird(Texture texture) {
        this.texture = texture;

        // Initialize position
        float screenWidth = DisplayManager.getScreenWidth();
        float screenHeight = DisplayManager.getScreenHeight();
        float x = INITIAL_X;
        float y = INITIAL_Y;

        this.bounds = new Rectangle(x, y, WIDTH, HEIGHT);
        this.velocityY = 0;

        // Debug info when created
        System.out.println("Bird created at: " + x + "," + y +
                          " size: " + WIDTH + "x" + HEIGHT +
                          ", texture: " + (texture != null ? texture.getWidth() + "x" + texture.getHeight() : "null"));
    }

    /**
     * Get the bird's bounds
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Get the bird's texture
     */
    public Texture getTexture() {
        return texture;
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
    }

    /**
     * Reset the bird to its initial position
     */
    public void reset() {
        bounds.x = INITIAL_X;
        bounds.y = INITIAL_Y;
        velocityY = 0;
    }
}
