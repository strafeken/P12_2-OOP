package application.minigame.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;

import abstractengine.utils.DisplayManager;

/**
 * Represents the player's ship in the AsteroidDodgeGame.
 */
public class Ship {
    // Ship properties
    private Rectangle bounds;
    private Texture texture;

    // Constants
    private static final float WIDTH = 48;
    private static final float HEIGHT = 48;

    /**
     * Creates a new Ship
     *
     * @param texture The ship texture
     */
    public Ship(Texture texture) {
        this.texture = texture;

        if (texture != null) {
            System.out.println("Ship initialized with texture: " + texture.getWidth() + "x" + texture.getHeight());
        } else {
            System.out.println("WARNING: Ship initialized with null texture!");
            // Create fallback texture when null is passed
            Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
            pixmap.setColor(0.8f, 0.8f, 0.9f, 1);
            pixmap.fill();
            this.texture = new Texture(pixmap);
            pixmap.dispose();
        }

        float x = (DisplayManager.getScreenWidth() - WIDTH) / 2;
        float y = 100; // Start near bottom of screen
        this.bounds = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    /**
     * Move the ship
     *
     * @param dx Change in x position
     * @param dy Change in y position
     */
    public void move(float dx, float dy) {
        bounds.x += dx;
        bounds.y += dy;

        // Keep within screen bounds
        bounds.x = MathUtils.clamp(bounds.x, 0, DisplayManager.getScreenWidth() - bounds.width);
        bounds.y = MathUtils.clamp(bounds.y, 0, DisplayManager.getScreenHeight() - bounds.height);
    }

    /**
     * Get the ship's bounds
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Get the ship's texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Reset the ship to starting position
     */
    public void reset() {
        float x = (DisplayManager.getScreenWidth() - WIDTH) / 2;
        float y = 100;
        bounds.x = x;
        bounds.y = y;
    }
}
